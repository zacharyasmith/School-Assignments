#!/usr/bin/env python3
"""
Author: Zachary Smith
Class: ECE456
Assignment: Lab 5
"""
from tools import *
import sys
import re
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
            print("Client asking `{}`".format(str(question, 'utf-8')))
            q_response = input("Proceed? [Y/n] ")
            if str(q_response).lower() not in ["", "y", "yes"]:
                # not accepted
                client.send(bytes('NO', 'utf-8'))
                cls()
                continue
            # get file size from question
            match = re.search(r'(?:\?\s\[)(\d+)(?:\sB\])', str(question, 'utf-8'))
            if not match:
                print("Regex error!")
                exit(1)
            else:
                file_size = int(match.group(1))
            # proceed
            client.send(bytes('OK', 'utf-8'))
            file_data = bytes(0)
            # accept first chunk
            data_count = 0
            data = client.recv(1024)
            while data:
                # handle
                data_count += len(data)
                print("Received {} B".format(len(data)))
                file_data += data
                data = client.recv(1024)
            # send acknowledgement if full file was received
            print("Received total of {} B and expected {} B.".format(data_count, file_size))
            if data_count == file_size:
                print("Sending acknowledgement.")
            else:
                print("Incomplete file received.")
                continue
            client.send(bytes('ACK', 'utf-8'))
            # save to file
            file_name = input("Done receiving data.\nFilename to save as? ")
            exists = os.path.isfile(file_name)
            if exists:
                overwrite = input("File exists. Overwrite? [Y/n] ")
                if str(overwrite).lower() not in ["", "y", "yes"]:
                    cls()
                    print("Resetting...")
                    continue
            with open(file_name, 'wb') as f:
                f.write(file_data)
                cls()
                print("Wrote {} B to {}\nResetting...".format(len(file_data), file_name))
    except KeyboardInterrupt:
        print('Closing server...')
        s.close()
