/*
 *  books.js
 *  Jeff Ondich, 27 April 2016
 *
 *  A little bit of Javascript showing one small example of AJAX
 *  within the "books and authors" sample for Carleton CS257,
 *  Spring Term 2017.
 */

/**passes input params into searchresult url, then opens url**/
function onSearchResultButton(classyear, drawnumberrange, bldg, floor, room, roomtype) {
    var url = '/search/searchresults/';
    var first = true;
    if (classyear != 'All'){
        url = url + '?classyear=' + classyear;
        first = false;}
    if (drawnumberrange != '' && first == true){
        url = url + '?drawnumberrange=' + drawnumberrange;
        first = false;
    }else if(drawnumberrange != ''){url = url + '&drawnumberrange=' + drawnumberrange;}
    if (bldg != 'All' && first == true){
        url = url + '?building=' + bldg;
        first = false;
    }else if(bldg != 'All'){url = url + '&building=' + bldg;}
    if (room != '' && first == true){
        url = url + '?room=' + room;
        first = false;
    }else if(room != ''){url = url + '&room=' + room;}
    if (floor != '' && first == true ){
        url = url + '?floor=' + floor;
        first = false;
    }else if(floor != '' ){url = url + '&floor=' + floor;}
    if (roomtype != 'All' && first == true){
        url = url + '?roomtype=' + roomtype;
        first = false;
    }else if(roomtype != 'All'){url = url + '&roomtype=' + roomtype;}
    window.location.href = url
}
