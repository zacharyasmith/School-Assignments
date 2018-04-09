"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

import socket
import os
import datetime


def cls():
    os.system('cls' if os.name == 'nt' else 'clear')


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]


def send_udp_msg(data, ip_address, port, debug=False):
    # Start the send socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # UDP
    sent_length = s.sendto(data, (ip_address, port))
    if debug:
        print("Sent {} bytes from port {} to {}:{}.".format(sent_length, s.getsockname()[1],
                                                            ip_address, port))
    return s.getsockname()[1]


def exec_command(cmd):
    filename = "tmp.txt"
    try:
        os.remove(filename)
    except OSError:
        pass
    os.system("{} > {}".format(cmd, filename))
    return filename


def time_str():
    return '{:%Y-%m-%d %H:%M:%S}'.format(datetime.datetime.now())
