// SPDX-License-Identifier: MIT
// Compiled into mender-update only for Ci4Rail protected images.
#pragma once

#include <nlohmann/json.hpp>
#include <openssl/evp.h>
#include <openssl/pem.h>
#include <openssl/rsa.h>
#include <sys/stat.h>
#include <cstdlib>
#include <fstream>
#include <iostream>
#include <memory>
#include <set>
#include <stdexcept>
#include <string>
#include <vector>

namespace ci4rail_ota {
using Json = nlohmann::json;

inline std::string Lower(std::string value) {
    for (auto &c : value) {
        if (c >= 'A' && c <= 'Z') c += 'a' - 'A';
    }
    return value;
}

// Reject symlink components, FIFOs/devices and unreasonably large inputs.
inline std::string Read(const std::string &path) {
    struct stat st {};
    for (size_t end = 1; end <= path.size(); ++end) {
        if (end != path.size() && path[end] != '/') continue;
        const auto component = path.substr(0, end);
        if (lstat(component.c_str(), &st) != 0 || S_ISLNK(st.st_mode)) {
            throw std::runtime_error("missing or symlinked path: " + component);
        }
    }
    if (!S_ISREG(st.st_mode) || st.st_size <= 0 || st.st_size > 1024 * 1024) {
        throw std::runtime_error("invalid file type or size: " + path);
    }
    std::ifstream input(path, std::ios::binary);
    if (!input) throw std::runtime_error("cannot read: " + path);
    std::string value(static_cast<size_t>(st.st_size), '\0');
    input.read(&value[0], value.size());
    if (!input || input.peek() != std::char_traits<char>::eof()) {
        throw std::runtime_error("file changed while reading: " + path);
    }
    return value;
}

inline Json Parse(const std::string &value) {
    std::vector<std::set<std::string>> objects;
    auto parsed = Json::parse(value, [&objects](int, Json::parse_event_t event, Json &node) {
        if (event == Json::parse_event_t::object_start) objects.emplace_back();
        if (event == Json::parse_event_t::key &&
            !objects.back().insert(Lower(node.get<std::string>())).second) {
            throw std::runtime_error("duplicate JSON property (case insensitive)");
        }
        if (event == Json::parse_event_t::object_end) objects.pop_back();
        return true;
    });
    if (!parsed.is_object()) throw std::runtime_error("configuration must be a JSON object");
    return parsed;
}

inline Json Normalize(const Json &value) {
    if (value.is_object()) {
        Json result = Json::object();
        for (auto it = value.begin(); it != value.end(); ++it) {
            result[Lower(it.key())] = Normalize(it.value());
        }
        return result;
    }
    if (value.is_array()) {
        Json result = Json::array();
        for (const auto &item : value) result.push_back(Normalize(item));
        return result;
    }
    return value;
}

using Key = std::unique_ptr<EVP_PKEY, decltype(&EVP_PKEY_free)>;
inline Key PublicKey(const std::string &pem) {
    std::unique_ptr<BIO, decltype(&BIO_free)> bio(
        BIO_new_mem_buf(pem.data(), static_cast<int>(pem.size())), BIO_free);
    Key key(bio ? PEM_read_bio_PUBKEY(bio.get(), nullptr, nullptr, nullptr) : nullptr,
            EVP_PKEY_free);
    if (!key) throw std::runtime_error("invalid PEM public key");
    return key;
}

inline void Verify(const std::string &delegation, const std::string &customer,
                   const std::string &signature) {
    auto key = PublicKey(delegation);
    if (EVP_PKEY_base_id(key.get()) != EVP_PKEY_RSA || EVP_PKEY_bits(key.get()) < 3072) {
        throw std::runtime_error("delegation key must be RSA with at least 3072 bits");
    }
    std::unique_ptr<EVP_MD_CTX, decltype(&EVP_MD_CTX_free)> ctx(EVP_MD_CTX_new(), EVP_MD_CTX_free);
    EVP_PKEY_CTX *pctx = nullptr;
    if (!ctx || EVP_DigestVerifyInit(ctx.get(), &pctx, EVP_sha256(), nullptr, key.get()) != 1 ||
        EVP_PKEY_CTX_set_rsa_padding(pctx, RSA_PKCS1_PADDING) <= 0 ||
        EVP_DigestVerify(ctx.get(), reinterpret_cast<const unsigned char *>(signature.data()),
                         signature.size(), reinterpret_cast<const unsigned char *>(customer.data()),
                         customer.size()) != 1) {
        throw std::runtime_error("customer key delegation signature invalid");
    }
    auto customer_key = PublicKey(customer);
    const int type = EVP_PKEY_base_id(customer_key.get());
    if (type != EVP_PKEY_RSA && type != EVP_PKEY_EC) {
        throw std::runtime_error("unsupported customer artifact key type");
    }
}

inline void ValidateConfig(Json actual, Json expected, const std::string &path) {
    actual = Normalize(actual);
    expected = Normalize(expected);
    // These operational values may be provisioned without weakening signature checks.
    for (const auto *name : {"tenanttoken", "serverurl", "servers",
                            "updatepollintervalseconds", "inventorypollintervalseconds",
                            "retrypollintervalseconds"}) {
        actual.erase(name);
        expected.erase(name);
    }
    if (actual != expected) throw std::runtime_error("configuration differs from protected policy: " + path);
}

// The root argument is used only by the native test harness. Production passes "".
inline void ValidateFiles(const std::string &root) {
    const std::string base = root + "/usr/share/ci4rail/ota/";
    auto policy = Parse(Read(base + "policy.json"));
    ValidateConfig(Parse(Read(root + "/etc/mender/mender.conf")), policy.at("main"), "main");
    ValidateConfig(Parse(Read(root + "/data/mender/mender.conf")), policy.at("fallback"), "fallback");
    // Mender's default data store is /var/lib/mender; ensure it resolves to what we checked.
    std::unique_ptr<char, decltype(&std::free)> resolved(
        realpath((root + "/var/lib/mender").c_str(), nullptr), std::free);
    if (!resolved || std::string(resolved.get()) != root + "/data/mender") {
        throw std::runtime_error("/var/lib/mender does not resolve to /data/mender");
    }
    PublicKey(Read(base + "ci4rail-artifact-pub.pem"));
    Verify(Read(base + "ci4rail-delegation-pub.pem"),
           Read(root + "/data/ci4rail/ota/customer-artifact-pub.pem"),
           Read(root + "/data/ci4rail/ota/customer-artifact-pub.pem.sig"));
}

inline void ValidateInvocation(int argc, char **argv) {
    for (const auto *name : {"MENDER_CONF_DIR", "MENDER_DATA_DIR", "MENDER_DATASTORE_DIR"}) {
        if (std::getenv(name)) throw std::runtime_error(std::string("path override forbidden: ") + name);
    }
    for (int i = 1; i < argc; ++i) {
        const std::string arg = argv[i];
        const auto option = arg.substr(0, arg.find('='));
        if (option == "--config" || option == "-c" || option == "--fallback-config" ||
            option == "-b" || option == "--data" || option == "--datastore" || option == "-d") {
            throw std::runtime_error("path override forbidden: " + option);
        }
    }
}

inline bool Check(int argc, char **argv) {
    // Inventory must still be able to report the client version before provisioning.
    if (argc == 2 && (std::string(argv[1]) == "--version" || std::string(argv[1]) == "-v" ||
                      std::string(argv[1]) == "--help" || std::string(argv[1]) == "-h")) return true;
    try {
        ValidateInvocation(argc, argv);
        ValidateFiles("");
        return true;
    } catch (const std::exception &error) {
        std::cerr << "OTA protection: " << error.what() << std::endl;
        return false;
    }
}
} // namespace ci4rail_ota
