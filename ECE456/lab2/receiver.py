#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 2
"""

import sys
sys.path.append('./')

from Exceptions import InvalidArgumentsError
from udp import UDP

if __name__ == "__main__":
    import sys
    try:
        # there should be 4 or 6 arguments
        if len(sys.argv) not in [4, 6]:
            raise InvalidArgumentsError
        # read in des key file if intended
        key = None
        try:
            if sys.argv[4] in ['--decrypt', '-d']:
                key = sys.argv[5]
        except Exception:
            pass
        udp = UDP(sys.argv[1], sys.argv[2])
        udp.receive_datagram(sys.argv[3], key)
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('{} <source ip addr> <dest ip addr> <path/to/datagram> [--decrypt [-d] <key/file>]'
              .format(sys.argv[0]))
        exit(1)
