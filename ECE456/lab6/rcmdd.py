#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 6
"""

from tools import *
import struct
import threading
from time import sleep
from Exceptions import InvalidArgumentsError


queue = [
    # ["ls -la", 3, 5, datetime.datetime.now(), addr, connection_info]
]
tcp = False


def process_queue(lock):
    global queue
    while True:
        curr_time = datetime.datetime.now()
        for cmd in queue:
            if cmd[1] <= 0:
                continue
            # check for delay
            if curr_time - cmd[3] >= datetime.timedelta(seconds=cmd[2]):
                lock.acquire()
                try:
                    cmd[3] = curr_time
                    cmd[1] -= 1
                    # for signalling
                    if cmd[1] == 0:
                        print("{}, {}:{}, `{}`, disconnected".
                              format(time_str(), cmd[4][0], cmd[4][1], cmd[0]))
                finally:
                    lock.release()
                filename = exec_command(cmd[0])
                with open(filename, 'r+b') as f:
                    if not tcp:
                        send_udp_msg(f.read(), cmd[4][0], cmd[4][1], False)
                    else:
                        cmd[5].send(f.read())
        sleep(1)

if __name__ == "__main__":
    import sys
    try:
        if len(sys.argv) != 3:
            raise InvalidArgumentsError
        # parse arguments
        if sys.argv[1] == "-tcp":
            tcp = True
        port = int(sys.argv[2])
        # display welcome
        cls()
        print("Welcome to the command daemon. Awaiting commands at: {}:{}".
              format(socket.gethostname(), port))
        # create lock and thread
        lock = threading.Lock()
        locker = threading.Thread(target=process_queue, args=(lock,), name='Queue Processor')
        locker.setDaemon(True)
        locker.start()
        # start listening on socket
        if not tcp:
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s.bind((get_ip_address(), port))
        else:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.bind((get_ip_address(), port))
            s.listen(5)
        # make connections based on flag
        while True:
            if not tcp:
                request, address = s.recvfrom(4096)
            else:
                client, address = s.accept()
                request = client.recv(4096)
            # 10 bytes for length (ASCII)
            size = int(str(request[0:9], 'utf-8')[::-1])
            # 4 bytes for repetition
            repetition = struct.unpack('i', request[10:14])[0]
            # 4 bytes for second delay
            delay = struct.unpack('i', request[14:18])[0]
            # rest for command
            command = str(request[18:], 'utf-8')
            print("{}, {}:{}, `{}`, connected".format(time_str(), address[0], address[1], command))
            lock.acquire()
            try:
                if not tcp:
                    queue.append([command, repetition, delay, datetime.datetime.now(), address])
                else:
                    queue.append([command, repetition, delay, datetime.datetime.now(),
                                  address, client])
            finally:
                lock.release()
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./rcmdd (-tcp | -udp) <listening port>')
        exit(1)
