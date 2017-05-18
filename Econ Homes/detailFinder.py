import config
import dataLogger
import sys
import json
import urllib.request
import urllib

def getValues(address,city,state):
    pass
def getAddress(zpidList):
    baseURL = config.getZestimateAPI
    dataToWrite = []
    errorList = []
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

        start = htmlDeepSearch.find('<rentzestimate>')
        end = htmlDeepSearch.find('</rentzestimate>')
        if start == -1 or end == -1:
            errorList.append(['rentzestimate',zpidList.index(zpid) ,zpid])
            continue
        rentZestimate = htmlDeepSearch[start+15:end]
        rentZestimate = eval(rentZestimate[rentZestimate.find('>')+1:rentZestimate.find('</amount>')])

        start = htmlDeepSearch.find('<taxAssessment>')
        end = htmlDeepSearch.find('</taxAssessment>')
        if start == -1 or end == -1:
            errorList.append(['taxAssessment',zpidList.index(zpid) ,zpid])
            continue
        taxAssessment = eval(htmlDeepSearch[start+15:end])
        propertyTax=taxAssessment*0.011345

        start = htmlDeepSearch.find('<homedetails>')
        end = htmlDeepSearch.find('</homedetails>')
        htmlHomeDetails = urllib.request.urlopen(htmlDeepSearch[start+13:end]).read()
        htmlHomeDetails = htmlHomeDetails.decode('utf-8')
        start = htmlHomeDetails.find('property="product:price:amount')
        if start == -1:
            start = htmlHomeDetails.find('price":')
            if start == -1:
                start = htmlHomeDetails.find('price:')
                if start == -1:
                    errorList.append(['price',zpidList.index(zpid) ,zpid])
                    continue
                else:
                    priceDetails = htmlHomeDetails[start+6:end+16]
                    end = priceDetails.find(',')
                    if priceDetails[:end] == "": price = 0
                    else: price = eval(priceDetails[:end])
            else:
                priceDetails = htmlHomeDetails[start+8:end+16]
                end = htmlHomeDetails.find('"')
                if priceDetails[:end] == "": price = 0
                else: price = eval(priceDetails[:end])
        else:
            priceDetails = htmlHomeDetails[start+30:start+65]
            start = priceDetails.find('="')
            priceDetails = priceDetails[start+2:]
            end = priceDetails.find('"')
            price = eval(priceDetails[:end])
        if price == 0:
            price = 50000000

        start = htmlHomeDetails.find('HOA Fee:')
        if start == -1:
            HOA=price*.001
        else:
            HOADetails = htmlHomeDetails[start+28:start+65]
            start = HOADetails.find('>$')
            end = HOADetails.find('/mo')
            HOA = eval(HOADetails[start+2:end])
        dataToWrite.append([price,propertyTax,rentZestimate,HOA,zpid])
    return dataToWrite, errorList

def main(*args):
    if len(args)>0:
        zpidList = dataLogger.dataPull('zpid',args[0])
    else:
        zpidList = dataLogger.dataPull('zpid')
    dataToWrite, errorList = getAddress(zpidList)
    dataLogger.dataCache('detailFinder',dataToWrite)
    dataLogger.dataCache('ErrorLog',errorList)
