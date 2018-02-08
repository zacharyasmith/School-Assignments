Author: Zachary Smith
Class: ECE456
Assignment: Lab 2

The files, `sender.py` and `receiver.py`, contains all of the source code for this project. It is written in Python 3.

To execute the functions, ensure that they are executable via `chmod +x <filename>`.

Usage:
./sender.py <path/to/message> <source ip addr> <dest ip addr> <source port> <dest port> <output/file/name> [--encrypt [-e] <key/file>]
./receiver.py <source ip addr> <dest ip addr> <path/to/datagram> [--decrypt [-d] <key/file>]
