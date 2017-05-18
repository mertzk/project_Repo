"""
    config.py
    Keaton Mertz, 3 May 2017

    Configure file containing user logging variables
"""
#Zillow Setup
ZWSID = 'X1-ZWz197vcqeqrrf_8jmj2'
getZestimateAPI = 'http://www.zillow.com/webservice/GetZestimate.htm?zws-id=' + ZWSID + '&zpid='

urlList = [
    #Dakota County URL

    "https://www.zillow.com/homes/for_sale/Dakota-County-MN/fsba,fsbo_lt/townhouse_type/971_rid/globalrelevanceex_sort/44.967227,-92.583161,44.426424,-93.477173_rect/9_zm/0_mmm/",
    #Rice County URL
    "https://www.zillow.com/homes/for_sale/Rice-County-MN/fsba,fsbo_lt/townhouse_type/304_rid/globalrelevanceex_sort/44.665966,-93.027077,44.073033,-93.537941_rect/10_zm/0_mmm/"
    ]

def getDeepSearch(address,city,state):
    deepSearchUrl = 'http://www.zillow.com/webservice/GetDeepSearchResults.htm?zws-id=' + ZWSID + '&address=' + address + '&citystatezip=' + city + '%2C+' + state +'&rentzestimate=true'
    return deepSearchUrl
