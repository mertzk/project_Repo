/*
 *  books.js
 *  Jeff Ondich, 27 April 2016
 *
 *  A little bit of Javascript showing one small example of AJAX
 *  within the "books and authors" sample for Carleton CS257,
 *  Spring Term 2017.
 */

/**passes input params into searchresult url, then opens url**/
function goToApi() {
    var currentLocation = window.location.href;
    var location = currentLocation.search("searchresults/")+14;
    var queryString = currentLocation.slice(location);
    var apiUrl = 'http://'+ api_base_url + '/search/' + queryString;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', apiUrl);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            jsonToTable(xmlHttpRequest.responseText);
        }
    };
    xmlHttpRequest.send(null);
}

function jsonToTable(responseText) {
    document.getElementById('loadingtext').innerHTML = responseText;
    var drawNumberEntries = JSON.parse(responseText);
    var tableBody = '<tr><th>' + 'Search Results' + '</th></tr>';
    tableBody += '<tr>';
    tableBody += '<td>' + 'Draw Number' + '</td>';
    tableBody += '<td>' + 'Building' + '</td>';
    tableBody += '<td>' + 'Room Number' + '</td>';
    tableBody += '<td>' + 'Room Type' + '</td>';
    tableBody += '</tr>';
    for (var k = 0; k < drawNumberEntries.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td>' + drawNumberEntries[k]['drawnumber'] + '</td>';
        tableBody += '<td>' + drawNumberEntries[k]['building'] + '</td>';
        tableBody += '<td>' + drawNumberEntries[k]['roomnumber'] + '</td>';
        tableBody += '<td>' + drawNumberEntries[k]['roomtype'] + '</td>';
        tableBody += '</tr>';
    }
    document.getElementById('loadingtext').innerHTML = '';
    var resultsTableElement = document.getElementById('results_table');
    resultsTableElement.innerHTML = tableBody;
}
