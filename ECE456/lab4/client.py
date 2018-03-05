#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

import signal
from tools import *
from Exceptions import InvalidArgumentsError, TimeoutException
sys.path.append('./')


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]


def handler(signum, frame):
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
        if len(sys.argv) not in [4, 6]:
            raise InvalidArgumentsError
        # read in des key file if intended
        ip_address = sys.argv[1]
        port = int(sys.argv[2])
        key = sys.argv[3]
        file = search_args(sys.argv, ['--file', '-f'])
        data = None

        # clear prompt
        cls()

        # Get data to send
        if file:
            # read the whole file in
            with open(file, 'r+b') as f:
                data = f.read()
            print("Read {} bytes from `{}`.".format(os.path.getsize(file), file))
        else:
            print("Welcome to the UDP communication client.")
            # prompt keyboard data
            input_str = input("Keyboard message to send:\n")
            if len(input_str) > 250:
                print("Truncating input to 250 characters.")
                data = bytes(input_str[0:250], 'utf-8')
            else:
                data = bytes(input_str, 'utf-8')

        # encrypt
        data = encrypt(key, data)
        # send the data
        my_port = send_msg(data, ip_address, port)
        # Start timeout after sent
        signal.signal(signal.SIGALRM, handler)
        signal.alarm(5)

        # Start the listen socket
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.bind((get_ip_address(), my_port))
        response, address = s.recvfrom(4096)
        print("Received {} bytes from server {}:{}".format(len(response), address[0], address[1]))
        dec_response = decrypt(key, response)
        print(dec_response.decode("utf-8"))
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./client <dest ip address> <dest port> <key/file> '
              '[--file [-f] <path/to/file>]')
        exit(1)
    except TimeoutException:
        print("Timeout! Response not received in time.\nClosing connection...")
        s.close()
