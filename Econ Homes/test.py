import time
import datetime
import os
import sys
import json
import urllib.request
x= [1,2,3,4,5]
def hello():
    comboList = []
    for row in x:
        for row1 in x:
            if row1 ==3: continue
            for row2 in x:
                comboList.append({row:[row1, row2]})
    return comboList
print(hello())
