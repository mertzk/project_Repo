#Constants
intrestRate = 0.0473
downPayment = 0.20
numberMonths = 360
yearCounter = 0
yearList = []
while len(yearList) < 30:
    yearCounter+=1
    yearList.append(yearCounter)
insuranceAnnual = 300
managementCompanyRentPercent = 0.01
monthlyFee = 89
taxSavings = 1000
rentlessMonths = 1
annualCostRepairs = 1000
inflation = 0.02
investmentReturnRate = .05
#variables
price = 300000
propertyTax = 4000
HOA = 0
expectedRent = 1600
#Formulas
mortgagePayment = ((price*(1-downPayment)*(intrestRate/12.0)) / (1 - ((1 + (intrestRate/12))**(-numberMonths))))*12#PMT
initialInvestment = downPayment*price

inflationTiedCostList = []
totalEarningsList = []
monthlyRentList = []
for year in yearList:
    inflationTiedCostList.append(-(propertyTax + (HOA * 12) + insuranceAnnual + ( 12 - rentlessMonths ) * managementCompanyRentPercent*expectedRent + monthlyFee * 12  + annualCostRepairs)*(1+inflation)**(year-1))
    totalEarningsList.append((expectedRent*(12-rentlessMonths)+taxSavings)*(1+inflation)**(year-1))
    monthlyRentList.append(expectedRent*(1+inflation)^(year-1))
totalCostList = []
for inflationTiedCost in inflationTiedCostList:
    totalCostList.append(inflationTiedCost+mortgagePayment)
annualIncomeList = []
while len(annualIncomeList) < 30:
    counter=len(annualIncomeList)-1
    annualIncomeList.append(totalCostList[]+totalEarningsList[counter])
