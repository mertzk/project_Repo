#!/usr/bin/env python3
'''
    zillow.py
    Keaton Mertz, 3 May 2017

    Scrape zillow website for data
'''

import sys
import argparse
import json
import urllib.request

def get_first_listing(word):
    '''
    get_first_listing
        parameter: search
        return: title and value

    This program takes in a search query which has spaces between words
    replaced with %20. This function then finds the first listing that appears
    under the search term provided and the json parsing finds the title and
    value of the item!
    '''
# Construct the request
    url = "http://svcs.ebay.com/services/search/FindingService/v1"
    url += "?OPERATION-NAME=findItemsByKeywords"
    url += "&SERVICE-VERSION=1.0.0"
    url += "&SECURITY-APPNAME=KeatonMe-Carleton-PRD-f090fc79c-989afe1c"
    url += "&GLOBAL-ID=EBAY-US"
    url += "&RESPONSE-DATA-FORMAT=JSON"
    url += "&callback=_cb_findItemsByKeywords"
    url += "&REST-PAYLOAD"
    url += "&keywords="
    url += word
    url += "&paginationInput.entriesPerPage=1"

    data_from_server = urllib.request.urlopen(url).read()
    string_from_server = data_from_server.decode('utf-8')
    string_from_server = string_from_server.replace('/**/_cb_findItemsByKeywords(','')
    string_from_server = string_from_server[:-1]
    root_word_list = json.loads(string_from_server)
    result_list = []
    mainDictionary = eval(str(root_word_list['findItemsByKeywordsResponse'])[1:-1])
    searchResultDictionary = eval(str(mainDictionary['searchResult'])[1:-1])
    itemDictionary = eval(str(searchResultDictionary['item'])[1:-1])
    title = (str(itemDictionary['title']))[2:-2]
    sellingStatusDictionary = eval(str(itemDictionary['sellingStatus'])[1:-1])
    convertedCurrentPriceDictionary = eval(str(sellingStatusDictionary['convertedCurrentPrice'])[1:-1])
    value = convertedCurrentPriceDictionary['__value__']
    if type(title) != type(''):
        raise Exception('title: "{0}"'.format(title))
    if type(value) != type(''):
        raise Exception('value has wrong type: "{0}"'.format(part_of_speech))
    return[title, value]



def main(args):
    """
    main
        parameter: args (two expected args{0} = str args{1} = str)

    The main function preps args{1} into a list of searches and then prints
    the values in a reasonable manor.
    """
    if args.search.lower() == 'search':
        #prepare the searches by storting them into a list
        wordlist = args.words.split(',')
        modwordlist = []
        for index in range(len(wordlist)):
            modwordlist.append(wordlist[index].replace(' ','%20'))
        # send the list to getlistings and get back a result list
        resultList = getlistings(modwordlist,args.number)
        print('\n')
        #process and print result list!
        index = 0
        queryCount=0
        while len(resultList)!= 0:
            if queryCount % args.number==0:
                print('\n\nSearch Results For: ',wordlist[index])
                index+=1
                queryCount=0
            queryCount+=1
            Title = resultList.pop(0)
            Value = resultList.pop(0)
            print("\tProduct Name: ",Title)
            print("\tPrice ($): ",Value,'\n')

if __name__ == '__main__':
    # When I use argparse to parse my command line, I usually
    # put the argparse setup here in the global code, and then
    # call a function called main to do the actual work of
    # the program.
    parser = argparse.ArgumentParser(description='Get listings from Ebay')
    #This statement is to help someone out if they don't know the args to give
    if len(sys.argv)==1 or len(sys.argv)>4 or sys.argv[1] == '-h' or sys.argv[1] == '--help':
        print("""
 _______________________________________________________________________
|       ------This program requres two (accepts 3) arguments------      |
|-----------------------------------------------------------------------|
|-The first argument must be 'search' (for now)                         |
|-The second argument contains items to be searched on ebay.            |
|    ~multiple item searches must seperate items by a comma             |
|            hat,bat,car                                                |
|            poster,uranium,plants,watches                              |
|    ~strings containing are multi-word items must use quotes           |
|            'tools,running shoes,bike'                                 |
|            'baseball cards'                                           |
|-The third argument is the number of results to appear (optional arg)  |
|    ~default = 1 if field is left blank                                |
|-----------------------------------------------------------------------|
|***FULL EXAMPLE***                                                     |
|   -Unix Terminal Command from ./UsingAPIs directory                   |
|       python3 api_test.py search keys 4                               |
|       python3 api_test.py search 'bike lock,painting' 2               |
|       python3 api_test.py search 'paper,paper clip,stapler'           |
|_______________________________________________________________________|""")
        sys.exit(1)
    parser.add_argument('search',
                        type=str,
                        help='(only "search" so far) type the arg "search"',
                        choices=['search','Search','SEARCH'])

    parser.add_argument('words',
                        type=str,
                        help='the words you want to search')
    if len(sys.argv)==4:
        parser.add_argument('number',
                            type=int,
                            help='number of results to appear(default=0)')
    else:
        parser.add_argument('-number',
                            default='1',
                            type=int,
                            help='number of results to appear(default=0)')
    args = parser.parse_args()
    main(args)
