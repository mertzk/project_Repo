import config
import dataLogger
import sys
import collections
def comboFinder(valuesList):
    initialInvestment, houseValue, cumulativeIncome, zpid = valuesList[0]
    comboList = {}
    if initialInvestment > 60000:
        return {}
    for house1 in valuesList[1:]:
        if initialInvestment+house1[0] > 60000:
            continue
        for house2 in valuesList[2:]:
            comboCost = house1[0]+house2[0]+initialInvestment
            comboReward = house1[1] + house1[2] + house2[1] + house2[2] + houseValue + cumulativeIncome
            if comboCost < 60000 and comboReward > 950000:
                comboList.update({comboReward:[comboCost,["http://www.zillow.com/homes/"+str(zpid)+"_zpid", "http://www.zillow.com/homes/"+str(house1[3])+"_zpid","http://www.zillow.com/homes/"+str(house2[3])+"_zpid"]]})
    return comboList





def main(*args):
    possibleCombos = {}
    if len(args)>0:
        earningsList = dataLogger.dataPull('earningsFinder')
    else:
        earningsList = dataLogger.dataPull('earningsFinder')
    while len(earningsList)>2:
        print('\n')
        temp = comboFinder(earningsList)
        possibleCombos.update(temp)
        earningsList = earningsList[1:]
    dataLogger.dataCache('comboFinder',collections.OrderedDict(sorted(possibleCombos.items())))
