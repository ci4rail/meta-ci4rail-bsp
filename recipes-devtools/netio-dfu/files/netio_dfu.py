#!/usr/bin/python3

import argparse
import sys
import os
import socket
import ssl
import threading
import time
import http.server

EEPROM_SIZE=512
CERT_DIR="/usr/lib/netio-dfu"
#CERT_DIR="."

class HTTPS_Server(threading.Thread):

    def __init__(self, server_ip, server_port):
        threading.Thread.__init__(self)
        self.server_ip = server_ip
        self.server_port = server_port

    def run(self):
        server_file = os.path.join(CERT_DIR, 'ca_cert.pem')
        key_file = os.path.join(CERT_DIR, 'ca_key.pem')
        httpd = http.server.HTTPServer((self.server_ip, self.server_port), http.server.SimpleHTTPRequestHandler)
        httpd.socket = ssl.wrap_socket(httpd.socket,
                                       keyfile=key_file,
                                       certfile=server_file, server_side=True)
        httpd.serve_forever()

def main(args):
    
    https_thread = HTTPS_Server(args.myip, args.port)
    https_thread.start()
    time.sleep(2)
    
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((args.netioip, 1234))

    if not args.no_eeprom:
        data = s.recv(EEPROM_SIZE)

    url = "https://" + args.myip + ":" + str(args.port) + "/" + args.fwfile
    print(f"Sending URL={url}")
    s.send(url.encode())
    print("Press CTRL-C to exit")
    

tool_description = """
Tool to load firmware into NETIO module via network.
"""

def command_line_args_parsing():
    parser = argparse.ArgumentParser(description=tool_description)
    parser.add_argument("myip", help="IP of NETIF on this host leading to NETIO module")
    parser.add_argument("netioip", help="IP of NETIO module")
    parser.add_argument("fwfile", help="FW file to upload")
    parser.add_argument(
        "-p",
        "--port",
        help="TCP Port to serve file (default: 8070)",
        type=int,
        default=8070,
    )
    parser.add_argument(
        "-v",
        "--verbose",
        help="Be verbose",
        action="store_true",
    )
    parser.add_argument(
        "--no_eeprom",
        help="Disable EEPROM read",
        action="store_true",
    )
    return parser.parse_args()


if __name__ == "__main__":
    args = command_line_args_parsing()
    sys.exit(main(args))