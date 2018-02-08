import re
import struct
import os
from math import ceil
from datetime import datetime

import sys
sys.path.append('./')
from des import DES


def translate_ip(ip):
    match = re.match(r"(\d+)\.(\d+)\.(\d+)\.(\d+)", ip)
    if match:
        ip_ints = [int(match.group(i + 1)) for i in range(4)]
        ip_ints.reverse()
        to_ret = struct.pack('BBBB', *ip_ints)
        return to_ret, int.from_bytes(to_ret, 'big')


def translate_port(port):
    return struct.pack('H', int(port)), int(port)


def half_word(b_array, i, j):
    return (b_array[i] << 8) | b_array[j]


class UDP:
    def __init__(self, source_ip, dest_ip, source_port=None, dest_port=None):
        self.source_ip, source_ip_int = translate_ip(source_ip)
        self.dest_ip, dest_ip_int = translate_ip(dest_ip)
        # Required for sender, optional for receiver
        if source_port:
            self.source_port, source_port_int = translate_port(source_port)
            self.dest_port, dest_port_int = translate_port(dest_port)
        self.byte_count = None
        self.temp_file = 'temp_{}.txt'.format(datetime.now().strftime('%Y%m%d%H%M%S'))

        if source_port:
            print('Source port:', source_port_int)
            print('Destination port:', dest_port_int)

        print('Source IP:', source_ip_int)
        print('Destination IP:', dest_ip_int)

    def receive_datagram(self, file):
        with open(file, 'r+b') as f:
            # discard first 4
            f.read(4)
            total_length = half_word(f.read(2), 1, 0)
            odd = (total_length % 2) == 1
            read_checksum = half_word(f.read(2), 1, 0)
        checksum = self.calculate_checksum(total_length, False, file)
        checksum_passed = read_checksum == checksum
        print('Checksum passed:', checksum_passed)
        if checksum_passed:
            # rewrite data without header
            with open(self.temp_file, 'wb') as w:
                with open(file, 'r+b') as r:
                    r.read(8)
                    for i in range((total_length + 1 if odd else total_length) - 8):
                        w.write(r.read(1))
            des = DES('key.txt')
            des.decrypt(self.temp_file, 'output')
        os.remove(self.temp_file)
        print('Datagram received.')

    def send_datagram(self, file, output):
        # initiate encryption algorithm
        des = DES('key.txt')
        byte_count = des.encrypt(file, self.temp_file)
        odd = True if byte_count % 2 == 1 else False
        total_length = 8 + byte_count
        print('Total length:', total_length, 'B')
        # Calculate the checksum
        checksum = self.calculate_checksum(byte_count)
        with open(output, 'wb') as w:
            w.write(self.source_port)
            w.write(self.dest_port)
            w.write(struct.pack('H', total_length))
            w.write(struct.pack('H', checksum))
            # add bytes from encrypted file
            with open(self.temp_file, 'r+b') as r:
                for i in range(byte_count + 1 if odd else byte_count):
                    w.write(r.read(1))
        os.remove(self.temp_file)
        print('File successfully written with datagram.')

    def calculate_checksum(self, byte_count, send=True, file=None):
        comparator = 65535
        total_length = 8 + byte_count if send else byte_count
        odd = True if byte_count % 2 == 1 else False
        # add ip addresses
        checksum = half_word(self.source_ip, 1, 0) + half_word(self.source_ip, 3, 2)
        checksum += half_word(self.dest_ip, 1, 0) + half_word(self.dest_ip, 3, 2)
        # protocol (17)
        checksum += 17
        # total length
        checksum += total_length
        if send:
            # ports
            checksum += half_word(self.source_port, 1, 0) + half_word(self.dest_port, 1, 0)
            # total length
            checksum += total_length
            # add bytes from encrypted file
        with open(self.temp_file if send else file, 'r+b') as r:
            for i in range(int(ceil(byte_count / 2))):
                # skip if checksum during reception
                if send or i != 3:
                    checksum += half_word(r.read(2), 1, 0)
                else:
                    r.read(2)
        # Add carry until less than 0xFFFF
        while checksum > comparator:
            carry = checksum >> 16
            checksum = (checksum & comparator) + carry
        # Complement for 16 bits
        checksum = checksum ^ comparator
        print('Checksum:', hex(checksum), '({})'.format(checksum))
        return checksum
