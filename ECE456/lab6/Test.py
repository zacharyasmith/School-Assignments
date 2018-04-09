from tools import *
from time import sleep

queue = [
    # command, count, seconds delay, last executed
    ["ls -la", 3, 5, datetime.datetime.now()]
]


def process_queue():
    global queue
    while True:
        curr_time = datetime.datetime.now()
        for cmd in queue:
            if cmd[1] == 0:
                continue
            # check for delay
            if curr_time - cmd[3] >= datetime.timedelta(seconds=cmd[2]):
                print("exec `{}`".format(cmd[0]))
                cmd[3] = curr_time
                exec_command(cmd[0])
                cmd[1] -= 1
        sleep(1)


process_queue()
