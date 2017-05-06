import dataLogger
#Constants
intrestRate = 0.0473
downPayment = 0.20
numberMonths = 360
yearCounter = 0
yearList = list(range(1,31))
insuranceAnnual = 300
managementCompanyRentPercent = 0.01
monthlyFee = 89
taxSavings = 1000
rentlessMonths = 1
annualCostRepairs = 1000
inflation = 0.02
investmentReturnRate = .05
def earningsFinder(dataList):
    price = dataList[0]
    propertyTax = dataList[1]
    expectedRent = dataList[2]
    HOA = dataList[3]
    zpid = dataList[4]
    #Formulas
    mortgagePayment = -((price*(1-downPayment)*(intrestRate/12.0)) / (1 - ((1 + (intrestRate/12))**(-numberMonths))))*12#PMT
    initialInvestment = downPayment*price

    cumulativeIncome = 0
    houseValue = price
    for year in yearList:
        #first im adding inflationTiedCost, then mortgagePayment, then totalEarnings
        cumulativeIncome += (-(propertyTax + (HOA * 12) + insuranceAnnual + ( 12 - rentlessMonths ) * managementCompanyRentPercent*expectedRent + monthlyFee * 12  + annualCostRepairs)*(1+inflation)**(year-1))
        cumulativeIncome += mortgagePayment
        cumulativeIncome += ((expectedRent*(12-rentlessMonths)+taxSavings)*(1+inflation)**(year-1))
        if year>1:
            houseValue = houseValue*(1+inflation)
    return [initialInvestment, houseValue, cumulativeIncome, zpid]

def main(*args):
    dataToWrite = []
    if len(args)>0:
        dataFile = dataLogger.dataPull('detailFinder',args[0])
    else:
        dataFile = dataLogger.dataPull('detailFinder')
    for dataList in dataFile:
        dataToWrite.append(earningsFinder(dataList))
    dataLogger.dataCache('earningsFinder',dataToWrite)
    #dataLogger.dataCache('ErrorLog',errorList)
