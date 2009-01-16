var categoryId;

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
    var t = setTimeout("stripeTables(), highlightRows(), lockRow()", 300 );
}

function addClass(element,value) {
  if (!element.className) {
    element.className = value;
  } else {
    newClassName = element.className;
    newClassName+= " ";
    newClassName+= value;
    element.className = newClassName;
  }
}

function stripeTables() {
	var tables = document.getElementsByTagName("table");
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == "pickme") {
			var tbodies = tables[m].getElementsByTagName("tbody");
			for (var i=0; i<tbodies.length; i++) {
				var odd = true;
				var rows = tbodies[i].getElementsByTagName("tr");
				for (var j=0; j<rows.length; j++) {
					if (odd == false) {
						odd = true;
					} else {
						addClass(rows[j],"odd");
						odd = false;
					}
				}
			}
		}
	}
}
function highlightRows() {
  if(!document.getElementsByTagName) return false;
  	var tables = document.getElementsByTagName("table");
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == "pickme") {
			  var tbodies = tables[m].getElementsByTagName("tbody");
			  for (var j=0; j<tbodies.length; j++) {
				 var rows = tbodies[j].getElementsByTagName("tr");
				 for (var i=0; i<rows.length; i++) {
					   rows[i].oldClassName = rows[i].className
					   rows[i].onmouseover = function() {
						  if( this.className.indexOf("selected") == -1)
							 addClass(this,"highlight");
					   }
					   rows[i].onmouseout = function() {
						  if( this.className.indexOf("selected") == -1)
							 this.className = this.oldClassName
					   }
				 }
			  }
		}
	}
}

function lockRow() {
  	var tables = document.getElementsByTagName("table");
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == "pickme") {
				var tbodies = tables[m].getElementsByTagName("tbody");
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName("tr");
					for (var i=0; i<rows.length; i++) {
						rows[i].oldClassName = rows[i].className;
						rows[i].onclick = function() {
							if (this.className.indexOf("selected") != -1) {
								this.className = this.oldClassName;
							} else {
								addClass(this,"selected");
							}
							selectRowCheckbox(this);
						}
					}
				}
		}
	}
}

function getIndexBack(place){

    var tables = document.getElementsByTagName("table");
    var score = "place="+place+ "&cat=" + categoryId + "&";
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == "pickme") {
				var tbodies = tables[m].getElementsByTagName("tbody");
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName("tr");
                    for (var i=0; i<rows.length; i++) {
                        if(rows[i].className == "highlight selected" || rows[i].className == "odd highlight selected"){
                            score += "parameters=" + i +"&";
                        }
                    }

                }
        }
    }
    window.location = "addProduct.do?" + score;
}

function getIndexBackByRemoving(place){

    var tables = document.getElementsByTagName("table");
    var score = "id=" + place +"&mode=1&";
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == "pickme") {
				var tbodies = tables[m].getElementsByTagName("tbody");
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName("tr");
                    for (var i=0; i<rows.length; i++) {
                        if(rows[i].className == "highlight selected" || rows[i].className == "odd highlight selected"){
                            score += "parameters=" + i +"&";
                        }
                    }

                }
        }
    }
    window.location="showPlace.do?" + score;
}

function getIndexBackByEditing(place){

    var tables = document.getElementsByTagName("table");
    var score = "id=" + place +"&mode=2&";
	for (var m=0; m<tables.length; m++) {
		if (tables[m].className == "pickme") {
				var tbodies = tables[m].getElementsByTagName("tbody");
				for (var j=0; j<tbodies.length; j++) {
					var rows = tbodies[j].getElementsByTagName("tr");
                    for (var i=0; i<rows.length; i++) {
                        if(rows[i].className == "highlight selected" || rows[i].className == "odd highlight selected"){
                            score += "parameters=" + i +"&";
                        }
                    }

                }
        }
    }

    window.location ="showPlace.do?" + score;
}

function getIndexBackByEditing(nr, place){

    var score = "id=" + place +"&mode=2&parameters=" + nr;

    window.location ="showPlace.do?" + score;
}

function getIndexBackByAdding(nr, place){

    var score = "id=" + place +"&mode=3&parameters=" + nr;

    window.location ="showPlace.do?" + score;
}

function getIndexBackByRemoving(nr, place){

    var score = "id=" + place +"&mode=1&parameters=" + nr;

    window.location="showPlace.do?" + score;
}

function setCategoryId(id){
    categoryId = id;
}

addLoadEvent(stripeTables);
addLoadEvent(highlightRows);



