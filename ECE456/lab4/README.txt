Author: Zachary Smith
Class: ECE456
Assignment: Lab 4

This lab has been written entirely in Python3. It will need to be installed to run the programs.

Make the files executable by running `chmod +x <file>` on each python file.

Run the following:
    `./client <dest ip address> <dest port> <key/file> [--file [-f] <path/to/file>]`
    `./server <listening port> <key/file>`

NOTE on the keys: the keys must be formatted, in one line, like this (this package has an example):
dc 21 45 22 1a 1d 14 ce
They are encoded as hexadecimal bytes and are space separated.

The client(s) and the server must use the same key file, as in the client(s) have the same key file
as the server.

To execute the DES algorithm by itself:
    `./des [--decrypt [-d] | --encrypt [-e]] <path/to/key/file> <path/to/file>`
