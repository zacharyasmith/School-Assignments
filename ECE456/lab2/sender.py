#!/usr/bin/env python

from lab2.Exceptions import InvalidArgumentsError
from lab2.udp import UDP

if __name__ == "__main__":
    import sys
    try:
        if len(sys.argv) != 7:
            raise InvalidArgumentsError
        udp = UDP(sys.argv[2], sys.argv[4], sys.argv[3], sys.argv[5])
        udp.send_datagram(sys.argv[1])
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('{} <path/to/message> <source ip addr> <dest ip addr> <source port> <dest port> <output/file/name>'
              .format(sys.argv[0]))
        exit(1)
