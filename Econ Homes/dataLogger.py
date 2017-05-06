import time
import datetime
import os
import sys

save_path = './data'

def dataCache(*args):
    if len(args) != 2:
        print('Error in file Caching, proper arguments not provided:\nexpected: dataType,data\nreceived: ' + str(args) + '\nfrom: ' + str(sys.argv))
        return
    completeName = os.path.join(save_path, args[0] + datetime.datetime.now().strftime("%Y,%m-%d_%H:%M") + '.txt')
    file = open(completeName, 'w')
    file.write(str(args[1]))
    file.close()


def dataPull(*args):
    files = []
    if len(args) == 1:
        for file in os.listdir(save_path):
            if args[0] in file:
                if file.endswith(".txt"):
                    files.append(file)
    elif len(args) == 2:
        for file in os.listdir(save_path):
            if args[0] in file:
                if args[1] in file:
                    if file.endswith(".txt"):
                        files.append(file)
    else:
        print('Error in file Pulling, proper arguments not provided:\nexpected: dataType or dataType,data \nreceived: ' + str(args) + '\nfrom: ' + str(sys.argv))
        return
    if len(files) > 0:
        return(max(files))
    else:
        print('Error in file Pulling, 0 files found with provided arguuments:\nexpected: dataType or dataType,data \nreceived: ' + str(args) + '\nfrom: ' + str(sys.argv))
        return
