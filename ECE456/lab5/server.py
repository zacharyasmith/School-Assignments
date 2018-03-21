#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 5
"""
from tools import *
import sys
sys.path.append('./')


if __name__ == "__main__":
    try:
        # read in des key file if intended
        cls()
        listening_port = 5000
        print("Starting server.".format(get_ip_address(), listening_port))
        # Start the listen socket
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((get_ip_address(), listening_port))
        s.listen(5)
        while True:
            print("Listening on {}:{}".format(get_ip_address(), listening_port))
            # accept connections from outside
            client, address = s.accept()
            # accept data
            question = client.recv(1024)
            print("Question was `{}`".format(str(question)))
            q_response = input("Proceed? [Y/n] ")
            if str(q_response).lower() not in ["", "y", "yes"]:
                # not accepted
                client.send(bytes('NO', 'utf-8'))
                continue
            # proceed
            client.send(bytes('OK', 'utf-8'))
            done = False
            file_data = bytes(0)
            while not done:
                # TODO better way to calculate done
                # accept data
                data = client.recv(1024)
                file_data += data
                print("Received {} B".format(len(data)))
                # done if
                done = len(data) < 1024
            file_name = input("Done receiving data.\nFilename to save as? ")
            with open(file_name, 'wb') as f:
                f.write(file_data)
                cls()
                print("Wrote {} B to {}\nResetting...".format(len(file_data), file_name))
    except KeyboardInterrupt:
        print('Closing server...')
        s.close()
