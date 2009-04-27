
//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see http://www.gnu.org/licenses/.

var categoryId;
var floorId;

function getIndexBack(place){

    var tables = document.getElementsByTagName('table');
    var score = 'place='+place+ '&cat=' + categoryId + '&';
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == 'pickme') {
				var tbodies = tables[m].getElementsByTagName('tbody');
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName('tr');
                    for (var i=0; i<rows.length; i++) {
                        if(rows[i].className == 'highlight selected' || rows[i].className == 'odd highlight selected'){
                            score += 'parameters=' + i +'&';
                        }
                    }

                }
        }
    }
    window.location = 'addProduct.do?' + score;
}

function getIndexBackByRemoving(place){

    var tables = document.getElementsByTagName('table');
    var score = 'id=' + place +'&mode=1&';
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == 'pickme') {
				var tbodies = tables[m].getElementsByTagName('tbody');
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName('tr');
                    for (var i=0; i<rows.length; i++) {
                        if(rows[i].className == 'highlight selected' || rows[i].className == 'odd highlight selected'){
                            score += 'parameters=' + i +'&';
                        }
                    }

                }
        }
    }
    window.location='showPlace.do?' + score;
}

function getIndexBackByEditing(place){

    var tables = document.getElementsByTagName('table');
    var score = 'id=' + place +'&mode=0&';
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == 'pickme') {
				var tbodies = tables[m].getElementsByTagName('tbody');
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName('tr');
                    for (var i=0; i<rows.length; i++) {
                        if(rows[i].className == 'highlight selected' || rows[i].className == 'odd highlight selected'){
                            score += 'parameters=' + i +'&';
                        }
                    }

                }
        }
    }

    window.location ='modifyProduct.do?' + score;
}

function getIndexBackByEditing(nr, place){
    var score = 'id=' + place +'&mode=2&parameters=' + nr + '&parameters=' + document.getElementById('input'+nr).value;

    window.location ='showPlace.do?' + score;
}

function getIndexBackByAdding(nr, place){

    var score = 'id=' + place +'&mode=3&parameters=' + nr;

    window.location ='showPlace.do?' + score;
}

/*
 * Windows Mobile 5.0 Only
 */
function getIndexBackByAddingWM5(nr, place){

   var score = 'place=' + place + '&cat=' + categoryId +  '&parameters=' + nr;

    window.location ='addProduct.do?' + score;
}

function getIndexBackByRemoving(nr, place){

    var score = 'id=' + place +'&mode=1&parameters=' + nr;

    window.location='showPlace.do?' + score;
}

function setCategoryId(id){
    categoryId = id;
}

function saveFloorId(id){
    floorId = id;   
}

function getLocation(place){
   window.location = 'showPlace.do?id='+ place + '&floorId=' +floorId;
}

//addLoadEvent(stripeTables);



