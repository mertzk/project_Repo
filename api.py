'''
    api.py
    Kha Huynh and Keaton Mertz, 3 May 2017

    Flask app used in Carleton room draw history API.
    Includes methods for general search queries, as well as statistical analysis queries
    (e.g. class year distribution across all buildings and a specific building's breakdown by class year).
'''
import sys
import flask
import json
import config
import psycopg2
import urllib

app = flask.Flask(__name__, static_folder='static', template_folder='templates')


def _fetch_all_rows_for_query(query):
    '''
    Returns a list of rows obtained from the room draw history database
    specified by the SQL query. If the query fails for any reason,
    an empty list is returned.
    '''
    try:
        connection = psycopg2.connect(database=config.database, user=config.user, password=config.password)
    except Exception as e:
        print('Connection error:', e, file=sys.stderr)
        return []

    rows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query)
        rows = cursor.fetchall()  # This can be trouble if your query results are really big.
    except Exception as e:
        print('Error querying database:', e, file=sys.stderr)

    connection.close()
    return rows


@app.after_request
def set_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    return response

@app.route('/search/')
def search():
    '''
    Returns a list of dictionaries, wherein each dictionary represents a draw number entry
    with the keys 'drawnumber', 'building', 'room number', 'room type' based on search filters
    applied by user. Search filters (class year, draw number range, building, floor, room, and roomtype)
    are accessed as arguments of the SQL query.
    '''

    #Parse through the query to determine search filters are applied
    classYearArgument = flask.request.args.get('classyear')
    if classYearArgument == None :
        classYear = 'all'
    else:
        classYear = classYearArgument

    rangeArgument = flask.request.args.get('drawnumberrange')
    if rangeArgument == None :
        drawNumberRange = 'all'
    else:
        drawNumberRange = rangeArgument

    buildingArgument = flask.request.args.get('building')
    if buildingArgument == None :
        building = 'all'
    else:
        building = buildingArgument

    floorArgument = flask.request.args.get('floor')
    if floorArgument == None :
        floor = 'all'
    else:
        floor = floorArgument

    roomArgument = flask.request.args.get('room')
    if roomArgument == None :
        room = 'all'
    else:
        room = roomArgument

    roomTypeArgument = flask.request.args.get('roomtype')
    if roomTypeArgument == None :
        roomType = 'all'
    else:
        roomType = roomTypeArgument

    #Retrieve all draw entries from database
    query = '''SELECT drawnumberid, roomid FROM idmatching'''
    drawNumberList = []
    for row in _fetch_all_rows_for_query(query):
        drawNumberEntry = {'drawnumberid': row[0], 'roomid': row[1]}
        drawNumberList.append(drawNumberEntry)

    #Apply search filters on the general list of draw number entries
    drawNumberList = classYearSearch(classYear,drawNumberList)
    drawNumberList = drawNumberRangeSearch(drawNumberRange, drawNumberList)
    drawNumberList = buildingSearch(building, drawNumberList)
    drawNumberList = floorSearch(floor, drawNumberList)
    drawNumberList = roomSearch(room, drawNumberList)
    drawNumberList = roomTypeSearch(roomType, drawNumberList)
    drawNumberList = formatResults(drawNumberList)
    return json.dumps(drawNumberList)


def classYearSearch(classYear,drawNumberList):
    '''
    Takes draw number entry list and filters out entries that do not have the specified class year,
    then returns the filtered list with the desired entries.
    '''
    returnList = []
    drawNumber = 10000
    if(classYear == 'all'):
        return drawNumberList
    elif (classYear ==  'sophomore'):
        classYearInt = '3'
    elif(classYear == 'junior'):
        classYearInt = '2'
    elif(classYear == 'senior'):
        classYearInt = '1'
    else:
        return returnList
    for drawNumberEntry in drawNumberList:
        #Convert draw number id into draw number
        drawNumberId = str(drawNumberEntry['drawnumberid'])
        query = '''SELECT drawnumber FROM drawnumbers WHERE id=''' + "'" + drawNumberId + "'"
        for row in _fetch_all_rows_for_query(query):
            drawNumber =  row[0]
        #Convert drawnumber into class year
        drawNumberStr = str(drawNumber)
        classYearOfEntry = drawNumberStr[0]
        if (classYearOfEntry == classYearInt):
            returnList.append(drawNumberEntry)
    return returnList

def drawNumberRangeSearch(drawNumberRange,drawNumberList):
    '''
    Takes draw number entry list and filters out entries that do not have a draw number within the specified
    range, then returns the filtered list with the desired entries.
    '''
    returnList = []
    drawNumber = 10000
    if (drawNumberRange == 'all'):
        return drawNumberList
    else:
        #Check to see if argument has integers, then ceate draw number range from argument
        try:
            lowerBound = int(drawNumberRange[0:4])
        except ValueError:
            return returnList
        try:
            upperBound = int(drawNumberRange[5:9])
        except ValueError:
            return returnList
        for drawNumberEntry in drawNumberList:
        #Convert draw number id into draw number
            drawNumberId = str(drawNumberEntry['drawnumberid'])
            query = '''SELECT drawnumber FROM drawnumbers WHERE id=''' + "'" + drawNumberId + "'"
        for row in _fetch_all_rows_for_query(query):
            drawNumber =  row[0]
        if (lowerBound <= drawNumber <= upperBound):
            returnList.append(drawNumberEntry)
    return returnList

def buildingSearch(building,drawNumberList):
        '''
        Takes draw number entry list and filters out entries that do not have a specified building,
        then returns the filtered list with the desired entries.
        '''
        returnList = []
        if(building == 'all'):
            return drawNumberList
        else:
            #Convert building name into building id
            building = str(building)
            query1 = '''SELECT id FROM buildings WHERE name=''' + "'" + building + "'"
            buildingId = '10000'
            for row in _fetch_all_rows_for_query(query1):
                buildingId = str(row[0])
            for drawNumberEntry in drawNumberList:
                roomId = str(drawNumberEntry['roomid'])
                query2 = '''SELECT id, buildingid FROM rooms WHERE id=''' +"'" + roomId + "'" + ''' AND buildingid=''' + "'" + buildingId + "'"
                rows = _fetch_all_rows_for_query(query2)
                if len(rows)> 0:
                    returnList.append(drawNumberEntry)
        return returnList


def floorSearch(floor,drawNumberList):
        '''
        Takes draw number entry list and filters out entries that do not have a room from a specified floor,
        then returns the filtered list with the desired entries.
        '''
        returnList = []
        roomNumber = '10000'
        floor = str(floor)
        if(floor == 'all'):
            return drawNumberList
        else:
            for drawNumberEntry in drawNumberList:
                # Convert room id into room number
                roomId = str(drawNumberEntry['roomid'])
                query = '''SELECT roomnumber FROM rooms WHERE id=''' + "'" + roomId + "'"
                for row in _fetch_all_rows_for_query(query):
                    roomNumber = row[0]
                # Convert room number into floor number (but keep in string type)
                if (roomNumber == 'OPT'):
                    floorOfEntry = 'OPT'
                elif (len(roomNumber) < 3):
                    floorOfEntry = '0'
                else:
                    floorOfEntry = roomNumber[0]
                if (floorOfEntry == floor):
                    returnList.append(drawNumberEntry)
        return returnList


def roomSearch(room,drawNumberList):
        '''
        Takes draw number entry list and filters out entries that do not have the specified room,
        then returns the filtered list with the desired entries.
        '''
        returnList = []
        room = str(room)
        if(room == 'all'):
            return drawNumberList
        else:
            #Check for ground floor exception, remove leading '0's from room if applicable
            if (room[0:2] == '00'):
                room = room[2]
            elif(room[0] == '0'):
                room = room[1:]
            for drawNumberEntry in drawNumberList:
                roomId = str(drawNumberEntry['roomid'])
                query = '''SELECT id, roomnumber FROM rooms WHERE id='''+ "'" + roomId + "'" + ''' AND roomnumber=''' + "'"+ room + "'"
                rows = _fetch_all_rows_for_query(query)
                if len(rows) > 0:
                    returnList.append(drawNumberEntry)
        return returnList


def roomTypeSearch(roomType,drawNumberList):
        '''
        Takes draw number entry list and filters out entries that do not have a room with the specified room type,
        then returns the filtered list with the desired entries.
        '''
        returnList = []
        if(roomType == 'all'):
            return drawNumberList
        else:
            #Convert room type name into room type id
            roomType = roomType.title()
            roomTypeId = '10000'
            query1 = '''SELECT id FROM roomtype WHERE type = ''' + "'"+  roomType + "'"
            for row in _fetch_all_rows_for_query(query1):
                roomTypeId = str(row[0])
            for drawNumberEntry in drawNumberList:
                roomId = str(drawNumberEntry['roomid'])
                query2 = '''SELECT id, roomtypeid FROM rooms WHERE id=''' + "'" + roomId + "'" + ''' AND roomtypeid=''' +"'" + roomTypeId + "'"
                rows = _fetch_all_rows_for_query(query2)
                if (len(rows) > 0):
                    returnList.append(drawNumberEntry)
        return returnList


def formatResults(drawNumberList):
    '''
    Takes the filtered draw number entry list and converts each entry from id-based format to
    a format that include the attributes 'drawnumber,' 'roomnumber,' 'building,' and 'roomtype'
    for readability and final json dump.
    '''
    for drawNumberEntry in drawNumberList:
        drawNumber = 10000
        #Replace drawnumberid with drawnumber
        drawNumberId = str(drawNumberEntry['drawnumberid'])
        query1 = '''SELECT drawnumber FROM drawnumbers WHERE id=''' + "'" + drawNumberId + "'"
        for row in _fetch_all_rows_for_query(query1):
            drawNumber = row[0]
        drawNumberEntry['drawnumber'] = drawNumberEntry.pop('drawnumberid')
        drawNumberEntry['drawnumber'] = drawNumber
        #Replace roomid with roomnumber
        roomId = str(drawNumberEntry['roomid'])
        query2 = '''SELECT id, buildingid, roomnumber, roomtypeid FROM rooms WHERE id =''' + "'" + roomId + "'"
        for row in _fetch_all_rows_for_query(query2):
            roomNumber = row[2]
            buildingId = str(row[1])
            roomTypeId = str(row[3])
        drawNumberEntry['roomnumber'] = drawNumberEntry.pop('roomid')
        drawNumberEntry['roomnumber'] = roomNumber
        #Replace buildingid with buildingname
        query3 = '''SELECT name FROM buildings WHERE id=''' + "'" + buildingId + "'"
        for row in _fetch_all_rows_for_query(query3):
            building = row[0]
        drawNumberEntry['building'] = building
        #Replace roomtypeid with roomtype
        query4 = '''SELECT type FROM roomtype WHERE id=''' + "'" + roomTypeId + "'"
        for row in _fetch_all_rows_for_query(query4):
            roomType = row[0]
        drawNumberEntry['roomtype'] = roomType

    return drawNumberList


# @ symbol indicates a "python decorator"
@app.route('/classyeardistribution/<classyear>')
def classyeardistribution(classyear):
    '''
    Returns a JSON list of Dictionaries. Each building that at least one student
    from the class year in question has drawn into is listed. The dictionary
    value is a percentage value representing the number of stuendts of thw class
    that used their drawn number to get into a building divided by the total
    number of students that used their draw number in that class. This just show
    how most draw numbers are used by a particular class year.

        http://.../classyeardistribution/sophomore
        http://.../classyeardistribution/senior
        http://.../classyeardistribution/senior
    '''

    buildings = '''SELECT * FROM buildings'''
    rooms = '''SELECT id, buildingid FROM rooms'''
    drawnumbers = '''SELECT id FROM drawnumbers WHERE drawnumber < '''
    drawnumbers_pt2 = '''AND drawnumber > '''
    idmatching = '''SELECT * FROM idmatching'''

    if classyear == 'sophomore':
        drawnumbers+= '3999'+drawnumbers_pt2+'2999'
    elif classyear == 'junior':
        drawnumbers+= '2999'+drawnumbers_pt2+'1999'
    elif classyear == 'senior':
        drawnumbers+= '1999'
    else:
        drawnumbers+= '0'

    drawNumberIdList = []
    for row in _fetch_all_rows_for_query(drawnumbers):
        drawNumberIdList.append(row[0])
    totalDraws = len(drawNumberIdList)

    roomsList = []
    for row in _fetch_all_rows_for_query(idmatching):
        if row[0] in drawNumberIdList:
            roomsList.append(row[1])

    buildingDict = {}
    for row in _fetch_all_rows_for_query(rooms):
        if row[0] in roomsList:
            if row[1] in buildingDict:
                buildingDict[row[1]] += 1
            else:
                buildingDict[row[1]] = 1
    distribution = []
    for row in _fetch_all_rows_for_query(buildings):
        if row[0] in buildingDict:
            idSet = {'name':row[1], 'percent':round((buildingDict[row[0]])*100/totalDraws,2)}
            distribution.append(idSet)
    return json.dumps(distribution)



@app.route('/buildingbreakdown/<buildingname>')
def buildingbreakdown(buildingname):
    ''''
    Returns a JSON dictionary of each class year and an associated percent. The
    percent corrolates to what percent of students from each class year used
    their daw number to get into the building in question. The percent is
    students in a class year who used their draw number over the total number
    of rooms drawn into in the building.
        http://.../buildingbreakdown/Watson%20Hall
        http://.../buildingbreakdown/Myers%20Hall
    '''

    buildings = '''SELECT id FROM buildings WHERE name = '''
    rooms = '''SELECT id FROM rooms WHERE buildingid = '''
    drawnumbers = '''SELECT * FROM drawnumbers'''
    idmatching = '''SELECT * FROM idmatching'''

    buildings +="'"+ buildingname+ "'"
    #return json.dumps(buildings)
    boolean = False
    for row in _fetch_all_rows_for_query(buildings):
        boolean = True
        selectedBuildingId=row[0]
    if boolean == True:
        rooms += str(selectedBuildingId)
    else:
        return json.dumps('')
    roomsIds = []
    for row in _fetch_all_rows_for_query(rooms):
        roomsIds.append(row[0])

    drawNumberIdList = []
    for row in _fetch_all_rows_for_query(idmatching):
        if row[1] in roomsIds:
            drawNumberIdList.append(row[0])

    totalDrawnRooms=0
    classYearDict = {'sophomore':0,'junior':0,'senior':0}
    for row in _fetch_all_rows_for_query(drawnumbers):
        if row[0] in drawNumberIdList:
            totalDrawnRooms+=1
            if row[1]>2999:
                classYearDict['sophomore']+=1
            elif row[1]>1999:
                classYearDict['junior']+=1
            elif row[1]>999:
                classYearDict['senior']+=1


    classYearDict['sophomore']=round(classYearDict['sophomore']*100/totalDrawnRooms , 2)
    classYearDict['junior']=round(classYearDict['junior']*100/totalDrawnRooms , 2)
    classYearDict['senior']=round(classYearDict['senior']*100/totalDrawnRooms , 2)

    return json.dumps(classYearDict)

@app.route('/help')
def help():
    """
    Simple help function to show the different options of queries an possible
    searches.
    """
    rule_list = []
    for rule in app.url_map.iter_rules():
        rule_text = rule.rule.replace('<', '&lt;').replace('>', '&gt;')
        rule_list.append(rule_text)
    return json.dumps(rule_list)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print ('Usage: {0} host port'.format(sys.argv[0]), file=sys.stderr)
        exit()

    host = sys.argv[1]
    port = sys.argv[2]
    app.run(host='thacker.mathcs.carleton.edu', port=5118, debug=True)
