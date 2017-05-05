import sys
import config
import dataLogger
import json
import urllib.request
import urllib

def getValues(address,city,state):
    pass
def getAddress(zpidList):
    baseURL = config.getZestimateAPI
    for zpid in zpidList:
        urlZestimate = baseURL + str(zpid)
        htmlZestimate = urllib.request.urlopen(urlZestimate).read()
        htmlZestimate = htmlZestimate.decode('utf-8')
        start = htmlZestimate.find('<street>')+ 8
        end = htmlZestimate.find('</street>')
        address = htmlZestimate[start:end].replace(' ','+')
        start = htmlZestimate.find('<city>')+ 6
        end = htmlZestimate.find('</city>')
        city = htmlZestimate[start:end].replace(' ','+')
        start = htmlZestimate.find('<state>')+ 7
        end = htmlZestimate.find('</state>')
        state = htmlZestimate[start:end].replace(' ','+')

        urlDeepSearch = config.getDeepSearch(address,city,state)

        htmlDeepSearch = urllib.request.urlopen(urlDeepSearch).read()
        htmlDeepSearch = htmlDeepSearch.decode('utf-8')



def main(*args):
    if len(args)>0:
        zpidList = dataLogger.dataPull('zpid',args[0])
    else:
        zpidList = dataLogger.dataPull('zpid')
    getAddress(zpidList)
