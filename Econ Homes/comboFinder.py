import config
import dataLogger
import sys
zpidList
def comboFinder(valuesList, cost, zpidList):
    initialInvestment, houseValue, cumulativeIncome, zpid = valuesList[0]
    if len(valuesList) == 0 or cost >= 60000:
        return
    recursion = 1
    while initialInvestment + cost <60000 and len(valuesList) == 0:
        comboFinder(valuesList[recursion:cost+initialInvestment])
        recursion += 1



def main(*args):
    possibleCombos = []
    if len(args)>0:
        earningsList = dataLogger.dataPull('earningsFinder',args[0])
    else:
        earningsList = dataLogger.dataPull('earningsFinder', 0, [])
    while len(earningsList)>0:
        possibleCombos.append(comboFinder(earningsList,0,[]))
        earningsList = earningsList[1:]

    dataLogger.dataCache('comboFinder',possibleCombos)
