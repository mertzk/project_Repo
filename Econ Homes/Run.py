import time
import datetime
import sys

import zpidFinder
import detailFinder

def help(args):
    helpList=['h','-h','help','--help','?','-?']
    if len(args) > 1:
        if args[1] in helpList:
            print("""
 _______________________________________________________________________
|      ------This program requres 0 (optional 2) arguments------        |
|-----------------------------------------------------------------------|
|-OPTIONAL: The first argument can be a help argument                   |
|   Help args are: 'h','-h','help','--help','?','-?'                    |
|-OPTIONAL: The first or second argument can be a...                    |
|-----------------------------------------------------------------------|
|***FULL EXAMPLE***                                                     |
|   -Unix Terminal Command from ./Econ Homes directory                  |
|       python3 zpidFinder.py                                           |
|       python3 zpidFinder.py help                                      |
|       python3 zpidFinder.py -?                                        |
|-----------------------------------------------------------------------|
| This program is:  zpidFinder.py                                       |
| The current excuting program is:  """ + args[0] + ' ' * (36 - len(args[0]))
+ '|\n' +
"""|_______________________________________________________________________|""")
        return True
    return False

def main(args):
    helpNeeded = help(args)
    if helpNeeded == True:
        return
######################    NEEDS WORK TO MAKE ARGS WORK WELL    ########################################################
    if 'zpidFinder' in args or len(args) == 1:
        zpidList = zpidFinder.zpidFinder()
        valueListofList = detailFinder.main()

main(sys.argv)
