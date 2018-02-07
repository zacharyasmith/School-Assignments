import re
import struct

from lab2.des import DES


def translate_ip(ip):
    match = re.match(r"(\d+)\.(\d+)\.(\d+)\.(\d+)", ip)
    if match:
        ip_ints = [int(match.group(i + 1)) for i in range(4)]
        ip_ints.reverse()
        to_ret = struct.pack('BBBB', *ip_ints)
        return to_ret, int.from_bytes(to_ret, 'big')


def translate_port(port):
    return struct.pack('H', int(port)), int(port)


class UDP:
    def __init__(self, source_ip, source_port, dest_ip, dest_port):
        self.source_ip, source_ip_int = translate_ip(source_ip)
        self.dest_ip, dest_ip_int = translate_ip(dest_ip)
        self.source_port, source_port_int = translate_port(source_port)
        self.dest_port, dest_port_int = translate_port(dest_port)

        print('Source port:', source_port_int)
        print('Destination port:', dest_port_int)
        print('Source IP:', source_ip_int)
        print('Destination IP:', dest_ip_int)

    def send_datagram(self, file):
        des = DES('key.txt')
        byte_count = des.encrypt(file, 'data')

        print('File size:', byte_count, 'B')
