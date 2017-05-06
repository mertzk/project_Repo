import time
import datetime
import sys
import zpidFinder
import detailFinder
import earningsFinder

def help(args):
    helpList=['h','-h','help','--help','?','-?']
    if len(args) > 1:
        if args[1] in helpList:
            print("""
 _______________________________________________________________________
|      ------This program requres 0 (optional 5) arguments------        |
|-----------------------------------------------------------------------|
|-OPTIONAL:                                                             |
|   -The first argument can be a help argument                          |
|       Help args are: 'h','-h','help','--help','?','-?'                |
|   -All other arguments are independent of order                       |
|       Args are:                                                       |
|           date%Y,%m-%d_%h:%m (any or all components of the date)      |
|           zpidFinder (does not when date arg is called)               |
|           earningsFinder                                              |
|           detailFinder                                                |
|                                                                       |
|-----------------------------------------------------------------------|
|***FULL EXAMPLE***                                                     |
|   -Unix Terminal Command from ./Econ Homes directory                  |
|       python3 Run.py zpidFinder                                       |
|       python3 Run.py date05-12 earningsFinder detailFinder            |
|       python3 Run.py -?                                               |
|-----------------------------------------------------------------------|
| This program is:  Run.py                                              |
| The current excuting program is:  """ + args[0] + ' ' * (36 - len(args[0]))
+ '|\n' +
"""| Current arguments: """ + str(args[1:]) + ' ' * (51 - len(str(args[1:])))
+ '|\n' +
"""|_______________________________________________________________________|""")
            return True
        return False

def main(args):
    helpNeeded = help(args)
    if helpNeeded == True:
        return
######################    NEEDS WORK TO MAKE ARGS WORK WELL    ########################################################
    hasDate = False
    for arg in args:
        if "date" in arg:
            hasDate = True
            date = arg[4:]

    if 'zpidFinder' in args or len(args) == 1:
        if hasDate != True:
            zpidFinder.zpidFinder()
    if 'detailFinder' in args or len(args) == 1:
        if hasDate:
            detailFinder.main(date)
        else:
            detailFinder.main()

    if 'earningsFinder' in args or len(args) == 1:
        if hasDate:
            earningsFinder.main(date)
        else:
            earningsFinder.main()


main(sys.argv)
