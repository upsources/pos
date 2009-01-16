var req;
  var place;


 
  function retrieveURL(url, place2) {
    
    //get the (form based) params to push up as part of the get request
    //url=url+getFormAsString(nameOfFormToPost);
    place = place2;
    //Do the Ajax call
    if (window.XMLHttpRequest) { // Non-IE browsers
      req = new XMLHttpRequest();
      req.onreadystatechange = processStateChange;
      try {
        req.open("GET", url, true); //was get
      } catch (e) {
        alert("Problem Communicating with Server\n"+e);
      }
      req.send(null);
    } else if (window.ActiveXObject) { // IE
      
      req = new ActiveXObject("Microsoft.XMLHTTP");
      if (req) {
        req.onreadystatechange = processStateChange;
        req.open("GET", url, true);
        alert("Problem Communicating with Server\n");
        req.send();
      }
    }
  }

/*
   * Set as the callback method for when XmlHttpRequest State Changes 
   * used by retrieveUrl
  */
  function processStateChange() {
  //document.getElementById("jsLog").innerHTML += req.responseText;
  
          if (req.readyState == 4) { // Complete
                if (req.status == 200) { // OK response
                
                       // alert("Ajax response: "+req.responseText);
                
                        //Split the text response into Span elements
                        spanElements = splitTextIntoSpan(req.responseText);
                       // alert("tu dziwka3" + req.responseText);
                        //Use these span elements to update the page
                        replaceExistingWithNewHtml(spanElements, place);
                } else {
                        alert("Problem with server response:\n " + req.statusText);
                }
      }
  }

 function splitTextIntoSpan(textToSplit){
 
        //Split the document
        returnElements=textToSplit.split("</span>")
        //alert("tu dziwka2" + "spanPos");
        //Process each of the elements  
        for ( var i=returnElements.length-1; i>=0; --i ){
                
                //Remove everything before the 1st span
                aspanPos = returnElements[i].indexOf("<span");           
                //alert("tu dziwka" + aspanPos);
                //if we find a match , take out everything before the span
                if(aspanPos>0){
                        subString=returnElements[i].substring(aspanPos);
                        returnElements[i]=subString;
                
                } 
        }
        
        return returnElements;
 }
 
 /*
  * Replace html elements in the existing (ie viewable document)
  * with new elements (from the ajax requested document)
  * WHERE they have the same name AND are  elements
  * @param newTextElements (output of splitTextIntoSpan)
  *                                     in the format id=name>texttoupdate
  */
 function replaceExistingWithNewHtml(newTextElements, place){
 
        //loop through newTextElements
        for ( var i=newTextElements.length-1; i>=0; --i ){
  
                //check that this begins with 
                if(newTextElements[i].indexOf("<span")>-1){
                        
                        //get the name - between the 1st and 2nd quote mark
                        startNamePos=newTextElements[i].indexOf('"')+1;
                        endNamePos=newTextElements[i].indexOf('"',startNamePos);
                        name=newTextElements[i].substring(startNamePos,endNamePos);
                        
                        //get the content - everything after the first > mark
                        startContentPos=newTextElements[i].indexOf('>')+1;
                        content=newTextElements[i].substring(startContentPos);
                        
                        //Now update the existing Document with this element
                        
                                //check that this element exists in the document
                                if(document.getElementById(place)){
                                
                                        //alert("Replacing Element:"+name);
                                        document.getElementById(place).innerHTML = content;
                                } else {
                                        //alert("Element:"+name+"not found in existing document");
                                }
                }
        }
 }
 