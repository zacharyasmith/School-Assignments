#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 4
"""

from tools import *
from Exceptions import InvalidArgumentsError
sys.path.append('./')


def add_message(ctr, ip, port, message):
    """
    Keeps only 5 most recent messages
    """
    new_tup = (datetime.now().strftime('%Y-%m-%d %H:%M:%S'), '{}:{}'.format(ip, port),
               message.decode("utf-8"))
    ret = [new_tup]
    if len(ctr) > 0:
        ret.extend(ctr[0:4 if len(ctr) >= 4 else len(ctr)])
    return ret


def string_messages(ctr):
    """
    ('%Y-%m-%d %H:%M:%S', '{ip}:{port}', message)
    """
    ret = ''
    for t in ctr:
        ret += '––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––\n' \
               'Message received on {} from {}:\n{}\n\n'.format(t[0], t[1], t[2])
    return ret


def respond(ip_address, port, msg):
    # Start the send socket
    _respond = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  # UDP
    sent_length = _respond.sendto(msg, (ip_address, port))
    print("Responded with {} bytes.".format(sent_length))

if __name__ == "__main__":
    try:
        # Should have either 7 or 9 arguments
        if len(sys.argv) not in [3]:
            raise InvalidArgumentsError
        # read in des key file if intended
        listening_port = int(sys.argv[1])
        cls()
        print("Starting server. ({}:{})".format(get_ip_address(), listening_port))
        key = sys.argv[2]
        # Blank messages container
        messages = []
        # Start the listen socket
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.bind((get_ip_address(), listening_port))
        while True:
            response, address = s.recvfrom(4096)
            print("Received {} bytes from client {}:{}"
                  .format(len(response), address[0], address[1]))
            # decrypt
            data = decrypt(key, response)
            # add message to message container
            messages = add_message(messages, address[0], address[1], data)
            cls()
            # print to prompt
            message_str = string_messages(messages)
            print(message_str)
            enc_response = encrypt(key, bytes(message_str, 'utf-8'))
            send_msg(enc_response, address[0], address[1], False)
    except InvalidArgumentsError:
        print('Invalid use of program. Correct use:')
        print('./server <listening port> <key/file>')
        exit(1)
    except KeyboardInterrupt:
        print('Closing server...')
        s.close()
