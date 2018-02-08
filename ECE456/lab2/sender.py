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
        # Should have either 7 or 9 arguments
        if len(sys.argv) not in [7, 9]:
            raise InvalidArgumentsError
        # read in des key file if intended
        key = None
        try:
            if sys.argv[7] in ['--encrypt', '-e']:
                key = sys.argv[8]
        except Exception:
            pass
        udp = UDP(sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
        udp.send_datagram(sys.argv[1], sys.argv[6], key)
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('{} <path/to/message> <source ip addr> <dest ip addr> <source port> <dest port> <output/file/name> '
              '[--encrypt [-e] <key/file>]'
              .format(sys.argv[0]))
        exit(1)
