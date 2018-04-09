#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 6
"""

import struct

from Exceptions import InvalidArgumentsError
from tools import *

if __name__ == "__main__":
    import sys
    debug = False
    try:
        if len(sys.argv) != 7:
            raise InvalidArgumentsError
        # parse arguments
        tcp = False
        if sys.argv[1] == "-tcp":
            tcp = True
        hostname = sys.argv[2]
        ip_address = socket.gethostbyname(hostname)
        port = int(sys.argv[3])
        repetition = int(sys.argv[4])
        delay = int(sys.argv[5])
        command = sys.argv[6]
        print("Command to {}:{} ({}) is".format(hostname, port, ip_address))
        print("\t[{},{}] `{}`".format(repetition, delay, command))
        # form the packet
        packet = struct.pack('ii', repetition, delay)
        packet += bytes(command, 'utf-8')
        size = bytes(str(10 + len(packet))[::-1], 'utf-8')
        # pad with zeroes
        for i in range(10 - len(size)):
            size += bytes('0', 'utf-8')
        packet = size + packet
        # send it (gets back port connected with)
        if not tcp:
            response = send_udp_msg(packet, ip_address, port, debug)
        else:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((ip_address, port))
            s.send(packet)
        received = 0
        # Start the listen socket
        if not tcp:
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s.bind((get_ip_address(), response))
        while received < repetition:
            if not tcp:
                response, address = s.recvfrom(4096)
            else:
                response = s.recv(4096)
            print(str(response, 'utf-8'))
            received += 1
        s.close()
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./rcmd (-tcp | -udp) <hostname> <port no.>'
              ' <execution count> <time delay (s)> <command>')
        exit(1)

