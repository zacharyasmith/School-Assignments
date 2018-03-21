"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 5
"""

import socket
import os


def cls():
    os.system('cls' if os.name == 'nt' else 'clear')


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    to_ret = s.getsockname()[0]
    s.close()
    return to_ret
