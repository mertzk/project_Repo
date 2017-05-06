#!/usr/bin/env python3
'''
    zillow.py
    Keaton Mertz, 3 May 2017

    Scrape zillow website for zpid list
'''
import config
import dataLogger
import sys
import urllib.request

def zpidFinder():
# Construct the request
    keyPhrase = 'data:{zpid:'

    htmlList=[]
    for url in config.urlList:
        html = urllib.request.urlopen(url).read()
        html = html.decode('utf-8')
        html = html[html.index(keyPhrase):]
        html = html[len(keyPhrase):html.index(']')+1]
        htmlList.extend(eval(html))
        pageNumber = 2
        morePages = True
        while morePages == True:
            html = urllib.request.urlopen(url + str(pageNumber) + '_p').read()
            html = html.decode('utf-8')
            html = html[html.index(keyPhrase):]
            html = html[len(keyPhrase):html.index(']')+1]
            pageNumber += 1
            if eval(html)[0] in htmlList:
                morePages = False
            else:
                htmlList.extend(eval(html))
        print(htmlList)
    if len(htmlList) > 0:
        dataLogger.dataCache('zpid',htmlList)
    else:
        print('Error in file zpidFinder, no HTML addresses found:\nurls searched:'+str(config.urlList)+ '\nfrom: ' + str(sys.argv))
    return
