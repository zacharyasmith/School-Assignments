Author: Zachary Smith
Class: ECE456
Assignment: Lab 1

The file, `des.py`, contains all of the source code for this project. It is written in Python 3 and
may be used, either by OOP or through the CLI. I will show how to use it in the CLI.

Make the file, `des.py`, executable (or you will need to always prepend `python3` in all calls to
the program) by running `chmod +x des.py`.

Run the following: `./des [--decrypt [-d] | --encrypt [-e]] <path/to/key/file> <path/to/file>`

NOTE on the keys: the keys must be formatted, in one line, like this (this package has an example):
dc 21 45 22 1a 1d 14 ce
They are encoded as hexadecimal bytes and are space separated.

This package has two plaintext examples that can be used (the beginning sentence/paragraph of Moby
Dick).
