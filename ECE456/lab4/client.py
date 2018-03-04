#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

import sys
import socket
import signal
import os

from des import *
from Exceptions import InvalidArgumentsError, TimeoutException
sys.path.append('./')


def handler(signum, frame):
    print("Timeout! Datagram not sent or response not received.")
    raise TimeoutException()


def search_args(args, flags):
    i = 0
    for a in args:
        if a in flags and i + 1 < len(args):
            return args[i + 1]
        i += 1
    return False


if __name__ == "__main__":
    try:
        # Should have either 7 or 9 arguments
        if len(sys.argv) not in [5, 7]:
            raise InvalidArgumentsError
        # read in des key file if intended
        ip_address = sys.argv[1]
        port = int(sys.argv[2])
        print("Connecting to: {}:{}".format(ip_address, port))
        key = search_args(sys.argv, ['--encrypt', '-e'])
        if not key:
            raise InvalidArgumentsError
        file = search_args(sys.argv, ['--file', '-f'])
        data = None

        # Get data to send
        if file:
            # read the whole file in
            with open(file, 'r+b') as f:
                data = f.read()
            print("Read {} bytes from `{}`.".format(os.path.getsize(file), file))
        else:
            # prompt keyboard data
            input_str = input("Keyboard input:\n")
            if len(input_str) > 250:
                print("Truncating input to 250 characters.")
                data = bytes(input_str[0:250], 'utf-8')
            else:
                data = bytes(input_str, 'utf-8')

        # encrypt
        _des = DES(key)
        tmp = 'tmp_{}.txt'.format(datetime.now().strftime('%Y-%m-%d %H%M%S'))
        out = 'out_{}.txt'.format(datetime.now().strftime('%Y-%m-%d %H%M%S'))
        # temporarily write data
        with open(tmp, 'wb') as w:
            w.write(data)
        _des.encrypt(tmp, out)
        with open(out, 'r+b') as r:
            data = r.read()
        # remove tmp files
        os.remove(tmp)
        os.remove(out)

        # Start the send socket
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # UDP
        sent_length = s.sendto(data, (ip_address, port))
        print("Sent {} bytes.".format(sent_length))

        # Start timeout after sent
        signal.signal(signal.SIGALRM, handler)
        signal.alarm(10)

        # Start the listen socket
        # s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        # s.bind((socket.gethostname(), my_port))
        # response, address = s.recvfrom(4096)
        # print("Received {} bytes from server ({}):\n{}".format(len(response), address, response))
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./client <dest ip address> <dest port> [--file [-f] <path/to/file>] '
              '[--encrypt [-e] <key/file>]')
        exit(1)
