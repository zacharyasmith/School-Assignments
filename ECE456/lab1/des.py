#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 1
"""

import re
import binascii
from datetime import datetime

import struct

class DES:
    """
    DES class which will encrypt or decrypt data according to external specifications.
    """
    def __init__(self, key_path):
        """
        Constructor. See `read_key` for specifications on key file requirements.

        Args:
            key_path: String describing the path to the key file.
        """
        self.__keys = []
        self.read_key(key_path)

    def read_key(self, file_path):
        """
        The symmetric keys for this program are to be saved in a file with the
        hexadecimal representation of 8 separate bytes saved on a single line with
        spaces in between each of them:
        e.g.
            88 99 aa bb cc dd ee ff

        Regex: ([0-9a-f]{2}\s?){8}

        Args:
            file_path: String describing the path to the key file.
        """
        # open file at path
        try:
            with open(file_path) as f:
                line = f.readline()
                # regex match for input file (explained in method desc.)
                # loosely matches in case of extra white spaces
                match = re.match(r'([0-9a-f]{2})\s+([0-9a-f]{2})\s+([0-9a-f]{2})\s+([0-9a-f]{2})\s+'
                                 r'([0-9a-f]{2})\s+([0-9a-f]{2})\s+([0-9a-f]{2})\s+([0-9a-f]{2})\s+',
                                 line)
                if match:
                    for i in range(8):
                        # parse byte array for each regex matching group
                        self.__keys.append(binascii.unhexlify(match.group(i + 1)))
                else:
                    raise Exception("Key formatted incorrectly. Expecting ([0-9a-f]{2}\\s?){8}")
        except FileNotFoundError as e:
            print("read_key::Error:: File `{}` not found.".format(e.filename))

    def __des_iteration(self, left, right, key):
        """
        Transforms two bytes in a single (simplified) DES iteration.

        Args:
            left: (int) Most significant byte of data to be transformed
            right: (int) Least significant byte of data to be transformed
            key: Int index of key (to address self.__keys)

        Returns:
            The combined and separated left, and right after transformation
        """
        # Swap left and right
        right_ = left
        left_ = right
        # xor the right side with the key (at index `key`) as int
        right_ = right_ ^ self.__keys[key][0]
        # pack left and right as half word
        out = struct.pack('>H', (left_ << 8) | right_)
        return out, left_, right_

    def __des(self, read, write, encrypt=True):
        """
        The simplified data encryption standard algorithm. Used for encryption and decryption.

        Args:
            read: String describing path to file to be read (as binary)
            write: String describing path to file to be written to
            encrypt: True | False If being encrypted

        Returns:
            Number of bytes operated on over course of algorithm.
        """
        if len(self.__keys) != 8:
            print("__des::Error:: Keys must be set before running DES algorithm.")
            return -1
        byte_count = 0
        try:
            # open read plaintext
            with open(read, 'r+b') as r:
                # open write cypher text
                with open(write, 'wb') as w:
                    while r.readable():
                        # except out of range errors
                        left = None
                        try:
                            # read next two bytes
                            left = r.read(1)[0]
                            right = r.read(1)[0]
                            byte_count += 2
                        except IndexError:
                            if left is None:
                                # read all contents
                                break
                            else:
                                # uneven number of bytes
                                # pad zeros to fill in last
                                right = 0
                                byte_count += 1
                        # 8 iterations of DES with different keys (index i)
                        for i in range(8):
                            out, left, right = self.__des_iteration(left, right, i)
                        # write half words after 8 iterations
                        if not encrypt and right == 0:
                            # is null ==> odd number of bytes in plaintext
                            # adjusting by writing `left` only
                            byte_count -= 1
                            w.write(struct.pack('B', left))
                        else:
                            w.write(out)
        except FileNotFoundError as e:
            print("__des::Error:: File `{}` not found.".format(e.filename))
            return -1
        return byte_count

    def encrypt(self, file_path):
        """
        Using the keys, this method will encrypt the data found in the file
        and output it to a file with the current datetime:
        e.g.
            cypher_2018-01-20 8:38:10.txt

        Args:
            file_path: String describing the path to the plaintext file to be encrypted
        """
        # format output file with current datetime
        out_file = 'cypher_{}.txt'.format(datetime.now().strftime('%Y-%m-%d %H%M%S'))
        # execute DES
        count = self.__des(file_path, out_file, True)
        if count >= 0:
            print('Done encrypting {} bytes.'.format(count))

    def decrypt(self, file_path):
        """
        Using the keys, this method will decrypt the data found in the file
        and output it to a file with the current datetime:
        e.g.
            plain_2018-01-20 8:38:10.txt

        Args:
            file_path: String describing the path to the plaintext file to be decrypted
        """
        out_file = 'plain_{}.txt'.format(datetime.now().strftime('%Y-%m-%d %H%M%S'))
        # execute DES
        count = self.__des(file_path, out_file, False)
        if count >= 0:
            print('Done decrypting {} bytes.'.format(count))


class InvalidArgumentsError(Exception):
    def __init__(self, *args, **kwargs):
        Exception.__init__(self, *args, **kwargs)

if __name__ == "__main__":
    import sys
    # ./des.py [--decrypt [-d] | --encrypt [-e]] <path/to/key/file> <path/to/txt/file>
    try:
        if len(sys.argv) != 4:
            raise InvalidArgumentsError
        _DES = DES(sys.argv[2])
        if sys.argv[1] in ['--decrypt', '-d']:
            _DES.decrypt(sys.argv[3])
        elif sys.argv[1] in ['--encrypt', '-e']:
            _DES.encrypt(sys.argv[3])
        else:
            raise InvalidArgumentsError
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('{} [--decrypt [-d] | --encrypt [-e]] <path/to/key/file> <path/to/file>'
              .format(sys.argv[0]))
