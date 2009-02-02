  /*
    Openbravo POS is a point of sales application designed for touch screens.
    Copyright (C) 2007 Openbravo, S.L.
    http://sourceforge.net/projects/openbravopos

    This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
   */

var categoryId, floorId;

function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      oldonload();
      func();
    }
  }
}

function update() {
    var t = setTimeout('stripeTables(), highlightRows(), lockRow()', 300 );
}

function addClass(element,value) {
  if (!element.className) {
    element.className = value;
  } else {
    newClassName = element.className;
    newClassName+= ' ';
    newClassName+= value;
    element.className = newClassName;
  }
}

function stripeTables() {
	var tables = document.getElementsByTagName('table');
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == 'pickme') {
			var tbodies = tables[m].getElementsByTagName('tbody');
			for (var i=0; i<tbodies.length; i++) {
				var odd = true;
				var rows = tbodies[i].getElementsByTagName('tr');
				for (var j=0; j<rows.length; j++) {
					if (odd == false) {
						odd = true;
					} else {
						addClass(rows[j],'odd');
						odd = false;
					}
				}
			}
		}
	}
}
function highlightRows() {
  if(!document.getElementsByTagName) return false;
  	var tables = document.getElementsByTagName('table');
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == 'pickme') {
			  var tbodies = tables[m].getElementsByTagName('tbody');
			  for (var j=0; j<tbodies.length; j++) {
				 var rows = tbodies[j].getElementsByTagName('tr');
				 for (var i=0; i<rows.length; i++) {
					   rows[i].oldClassName = rows[i].className
					   rows[i].onmouseover = function() {
						  if( this.className.indexOf('selected') == -1)
							 addClass(this,'highlight');
					   }
					   rows[i].onmouseout = function() {
						  if( this.className.indexOf('selected') == -1)
							 this.className = this.oldClassName
					   }
				 }
			  }
		}
	}
}

function lockRow() {
  	var tables = document.getElementsByTagName('table');
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == 'pickme') {
				var tbodies = tables[m].getElementsByTagName('tbody');
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName('tr');
					for (var i=0; i<rows.length; i++) {
						rows[i].oldClassName = rows[i].className;
						rows[i].onclick = function() {
							if (this.className.indexOf('selected') != -1) {
								this.className = this.oldClassName;
							} else {
								addClass(this,'selected');
							}
							selectRowCheckbox(this);
						}
					}
				}
		}
	}
}

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
    var score = 'id=' + place +'&mode=2&';
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

    window.location ='showPlace.do?' + score;
}

function getIndexBackByEditing(nr, place){

    var score = 'id=' + place +'&mode=2&parameters=' + nr;

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

function getFloorId(){
    return floorId;
}

addLoadEvent(stripeTables);
addLoadEvent(highlightRows);



