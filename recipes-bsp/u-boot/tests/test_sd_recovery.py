"""Exercise the patched C boot policy with mocked U-Boot services.

Run with python3 -m unittest discover -s recipes-bsp/u-boot/tests -v.
This checks control flow, not MMC hardware or cryptographic verification.
"""

from pathlib import Path
import subprocess
import tempfile
import unittest


PATCH = Path(__file__).resolve().parents[1] / "files/0001-moducop-boot-sd-recovery-before-environment.patch"

HARNESS = r'''
#include <assert.h>
#include <setjmp.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>

#define FS_TYPE_ANY 0
struct fdt_header { uint32_t words[10]; };
enum scenario {
    NO_CARD, NO_FILE, EMPTY, OVERSIZE, LOAD_ERROR, BAD_HEADER,
    TRUNCATED, ENV_ERROR, BOOT_ERROR, BOOT_OK
};
static int scenario, ctrlc, loads, boots, writes;
static jmp_buf outcome;
static char bootargs[256];

static int disable_ctrlc(int value)
{
    int old = ctrlc;
    ctrlc = value;
    return old;
}
static int fs_set_blk_dev(const char *media, const char *part, int type)
{
    assert(!strcmp(media, "mmc") && !strcmp(part, "1:1"));
    assert(type == FS_TYPE_ANY && ctrlc == 1);
    return scenario == NO_CARD;
}
static int fs_size(const char *file, loff_t *size)
{
    assert(!strcmp(file, "/recovery.itb"));
    *size = scenario == EMPTY ? 0 : scenario == OVERSIZE ? 0x10000001 : 4096;
    return scenario == NO_FILE ? -1 : 0;
}
static int fdt_check_header(const void *fit)
{
    assert((uintptr_t)fit == 0x60000000);
    return scenario == BAD_HEADER;
}
static unsigned int fdt_totalsize(const void *fit)
{
    assert((uintptr_t)fit == 0x60000000);
    return scenario == TRUNCATED ? 8192 : 4096;
}
static int env_set(const char *name, const char *value)
{
    ++writes;
    if (scenario == ENV_ERROR)
        return -1;
    if (!strcmp(name, "bootargs"))
        snprintf(bootargs, sizeof(bootargs), "%s", value);
    else if (!strcmp(name, "bootm_low"))
        assert(!strcmp(value, "0x40000000"));
    else if (!strcmp(name, "bootm_size"))
        assert(!strcmp(value, "0x58000000"));
    else if (!strcmp(name, "silent_linux"))
        assert(!strcmp(value, "no"));
    else if (!strcmp(name, "verify") || !strcmp(name, "autostart"))
        assert(!strcmp(value, "yes"));
    else
        assert(value == NULL);
    return 0;
}
static int run_command(const char *command, int flags)
{
    assert(flags == 0 && ctrlc == 1);
    if (!strncmp(command, "load ", 5)) {
        assert(!strcmp(command, "load mmc 1:1 0x60000000 /recovery.itb 0x10000000"));
        ++loads;
        return scenario == LOAD_ERROR;
    }
    assert(!strcmp(command, "bootm 0x60000000#recovery"));
    ++boots;
    assert(writes == 9);
    if (scenario == BOOT_OK)
        longjmp(outcome, 2); /* Linux takes control. */
    return 1;
}
static _Noreturn void panic(const char *message)
{
    assert(strstr(message, "ModuCop:"));
    longjmp(outcome, 1);
}
'''

MAIN = r'''
int main(int argc, char **argv)
{
    assert(argc == 3);
    scenario = atoi(argv[1]);
    ctrlc = atoi(argv[2]);
    int initial_ctrlc = ctrlc;
    strcpy(bootargs, "untrusted persisted bootargs");
    int result = setjmp(outcome);
    if (!result)
        moducop_try_sd_recovery();
    if (scenario <= NO_FILE) {
        assert(result == 0 && ctrlc == initial_ctrlc);
        assert(loads == 0 && boots == 0 && writes == 0);
        assert(!strcmp(bootargs, "untrusted persisted bootargs"));
    } else {
        assert(result == (scenario == BOOT_OK ? 2 : 1));
        assert(ctrlc == 1);
        assert(boots == (scenario >= BOOT_ERROR ? 1 : 0));
        if (scenario >= BOOT_ERROR) {
#ifdef CONFIG_TARGET_VERDIN_IMX8MP
            const char *console = "console=ttymxc2,115200 ";
#else
            const char *console = "console=ttymxc0,115200 ";
#endif
            char expected[256];
            snprintf(expected, sizeof(expected), "%sroot=/dev/ram0 rootfstype=squashfs ro ramdisk_size=393216", console);
            assert(!strcmp(bootargs, expected));
        }
    }
    return 0;
}
'''


class RecoveryPolicyTest(unittest.TestCase):
    def test_both_platforms(self):
        patch = PATCH.read_text()
        section = patch.split("+++ b/common/moducop-recovery.c\n", 1)[1].split("diff --git", 1)[0]
        source = "".join(line[1:] for line in section.splitlines(keepends=True)
                         if line.startswith("+") and not line.startswith("+#include"))
        with tempfile.TemporaryDirectory() as directory:
            c_file = Path(directory) / "recovery.c"
            binary = Path(directory) / "recovery"
            c_file.write_text(HARNESS + source + MAIN)
            for platform in ("CONFIG_TARGET_VERDIN_IMX8MM", "CONFIG_TARGET_VERDIN_IMX8MP"):
                subprocess.run(["cc", "-Wall", "-Wextra", "-Werror", "-D" + platform,
                                str(c_file), "-o", str(binary)], check=True)
                for scenario in range(10):
                    for ctrlc in (0, 1):
                        with self.subTest(platform=platform, scenario=scenario, ctrlc=ctrlc):
                            subprocess.run([str(binary), str(scenario), str(ctrlc)],
                                           check=True, stdout=subprocess.DEVNULL)


if __name__ == "__main__":
    unittest.main()
