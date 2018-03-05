"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

import socket
from des import *


def cls():
    os.system('cls' if os.name == 'nt' else 'clear')


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]


def search_args(args, flags):
    i = 0
    for a in args:
        if a in flags and i + 1 < len(args):
            return args[i + 1]
        i += 1
    return False


def encrypt(key, data):
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
    return data


def decrypt(key, response):
    # decrypt
    _des = DES(key)
    tmp = 'tmp_{}.txt'.format(datetime.now().strftime('%Y-%m-%d %H%M%S'))
    out = 'out_{}.txt'.format(datetime.now().strftime('%Y-%m-%d %H%M%S'))
    # temporarily write data
    with open(tmp, 'wb') as w:
        w.write(response)
    _des.decrypt(tmp, out)
    # container for response
    with open(out, 'r+b') as r:
        data = r.read()
    # remove tmp files
    os.remove(tmp)
    os.remove(out)
    return data


def send_msg(data, ip_address, port, debug=True):
    # Start the send socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # UDP
    sent_length = s.sendto(data, (ip_address, port))
    if debug or True:
        print("Sent {} bytes from port {} to {}:{}.".format(sent_length, s.getsockname()[1],
                                                            ip_address, port))
    return s.getsockname()[1]
