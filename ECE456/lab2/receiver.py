#!/usr/bin/env python

from lab2.Exceptions import InvalidArgumentsError
from lab2.udp import UDP

if __name__ == "__main__":
    import sys
    try:
        if len(sys.argv) != 4:
            raise InvalidArgumentsError
        udp = UDP(sys.argv[1], sys.argv[2])
        udp.receive_datagram(sys.argv[3])
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('{} <source ip addr> <dest ip addr> <path/to/datagram>'
              .format(sys.argv[0]))
        exit(1)
