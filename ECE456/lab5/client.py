#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

import sys
from tools import *
sys.path.append('./')


class InvalidArgumentsError(Exception):
    def __init__(self, *args, **kwargs):
        Exception.__init__(self, *args, **kwargs)


if __name__ == "__main__":
    try:
        # Should have 3 args
        if len(sys.argv) not in [3]:
            raise InvalidArgumentsError
        ip_address = sys.argv[1]
        file = sys.argv[2]
        data = None
        # clear prompt / welcome
        cls()
        print("Welcome! Opening file `{}`".format(file))
        with open(file, 'r+b') as f:
            # create socket
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # connect via IP address to set port, 5000
            s.connect((ip_address, 5000))
            # need to ask if can transfer
            question = "Can I send file `{}` to you?".format(os.path.basename(file))
            s.send(bytes(question, 'utf-8'))
            print("Asking server `{}`".format(question))
            response = s.recv(1024)
            print("Response was `{}`".format(str(response)))
            if response != b'OK':
                print("Closing connection...")
                s.close()
                exit(0)
            data = f.read(1024)
            while data:
                print("Read {} B from `{}`.\nSending...".format(os.path.getsize(file), file))
                s.send(data)
                data = f.read(1024)
            # Close connection after successful data send
            print("Done. Closing connection...")
            s.close()
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./client <dest ip address> <path/to/file>')
        exit(1)
