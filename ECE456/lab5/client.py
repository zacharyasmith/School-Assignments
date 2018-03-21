#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

import sys
from tools import *
import signal
sys.path.append('./')


def handler(signum, frame):
    raise TimeoutException()


class InvalidArgumentsError(Exception):
    def __init__(self, *args, **kwargs):
        Exception.__init__(self, *args, **kwargs)


class TimeoutException(Exception):
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
        # what is the size of the file?
        file_size = os.path.getsize(file)
        with open(file, 'r+b') as f:
            # create socket
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # connect via IP address to set port, 5000
            s.connect((ip_address, 5000))
            # need to ask if can transfer
            question = "Can I send file `{}` to you? [{} B]".format(os.path.basename(file), str(file_size))
            s.send(bytes(question, 'utf-8'))
            print("Asking server `{}`".format(question))
            response = s.recv(1024)
            print("Response was `{}`".format(str(response, 'utf-8')))
            # check response
            if response != b'OK':
                print("Closing connection...")
                s.close()
                exit(0)
            # begin file read and transfer
            data = f.read(1024)
            while data:
                print("Read {} B from `{}`. Sending...".format(len(data), file))
                s.send(data)
                data = f.read(1024)
            # Await acknowledgement
            print("Done sending. Awaiting acknowledgement.")
            try:
                # Start timeout after sent
                signal.signal(signal.SIGALRM, handler)
                signal.alarm(5)
                ack = True
                while not ack:
                    ack = s.read(1024)
                    ack = ack == b'ACK'
                # reset alarm
                signal.alarm(0)
                print("Acknowledgement received.")
            except TimeoutException:
                print("Timeout on awaiting acknowledgement.")
            print("Closing connection...")
            s.close()
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./client <dest ip address> <path/to/file>')
        exit(1)
