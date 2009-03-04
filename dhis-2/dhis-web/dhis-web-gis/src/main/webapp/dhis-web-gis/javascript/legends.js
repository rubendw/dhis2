    var selectedIndicatorList;
	var availableIndicatorList;
	var selectedList;
	var availableList;

function move( listId ) {
	

  var fromList = document.getElementById(listId);

  if ( fromList.selectedIndex == -1 ) { return; }

  if ( ! availableList ) {
    availableList = document.getElementById('availableList');
  }

  if ( ! selectedList ) {
    selectedList = document.getElementById('selectedList');
  }

  var toList = ( fromList == availableList ? selectedList : availableList );

  while ( fromList.selectedIndex > -1 ) {
    option = fromList.options.item(fromList.selectedIndex);
    fromList.remove(fromList.selectedIndex);
    toList.add(option, null);
  }

}

function moveIndicator( listId ) {
	
  var fromList = document.getElementById(listId);

  if ( fromList.selectedIndex == -1 ) { return; }

  if ( ! availableIndicatorList ) {
    availableIndicatorList = document.getElementById('availableIndicatorList');
  }

  if ( ! selectedIndicatorList ) {
    selectedIndicatorList = document.getElementById('selectedIndicatorList');
  }

  var toList = ( fromList == availableIndicatorList ? selectedIndicatorList : availableIndicatorList );

  while ( fromList.selectedIndex > -1 ) {
    option = fromList.options.item(fromList.selectedIndex);
    fromList.remove(fromList.selectedIndex);
    toList.add(option, null);
  }

}

function submitForm() {
	
  if ( ! availableIndicatorList ) {
    availableIndicatorList = document.getElementById('availableIndicatorList');
  }

  if ( ! selectedIndicatorList ) {
    selectedIndicatorList = document.getElementById('selectedIndicatorList');
  }

  if ( ! availableList ) {
    availableList = document.getElementById('availableList');
  }

  if ( ! selectedList ) {
    selectedList = document.getElementById('selectedList');
  }

  // selectAll(availableList);
  selectAll(selectedList);
  selectAll(selectedIndicatorList);

  return false;

}

function selectAll(list) {

  for ( var i = 0, option; option = list.options.item(i); i++ ) {
    option.selected = true;
  }

}

// legend-----------------------------------------------

function validateAddLegend(){
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( addLegendValidationCompleted ); 
  
  var name = document.getElementById( 'name' ).value;
  var color = document.getElementById( 'color' ).value;
  var minValue = document.getElementById( 'min' ).value;
  var maxValue = document.getElementById( 'max' ).value;
  var autoMax =  document.getElementById( 'automax' ).checked;
  
  var requestString = "validateLegend.action?nameField=" + name + "&colorField=" + color + "&minField=" + minValue + "&maxField=" + maxValue + "&action=add" + "&autoCreateMax=" + 	autoMax;

  request.send( requestString );
  
  return false;
}
function addLegendValidationCompleted(messageElement){
	var type = messageElement.getAttribute( 'type' );
	var message = messageElement.firstChild.nodeValue;
	 if ( type == 'success' )
 	 {
      // Both edit and add form has id='dataSetForm'      
      document.forms['addLegend'].submit();
  	 } else if ( type == 'input' )
 		 {
   			 document.getElementById( 'message' ).innerHTML = message;
   			 document.getElementById( 'message' ).style.display = 'block';
  		  }
}

function validateUpdateLegend(){
	var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( updateLegendValidationCompleted ); 
  
  var name = document.getElementById( 'name' ).value;
  var color = document.getElementById( 'color' ).value;
  var minValue = document.getElementById( 'min' ).value;
  var maxValue = document.getElementById( 'max' ).value;
  var autoMax =  document.getElementById( 'automax' ).checked;
  
  var requestString = "validateLegend.action?nameField=" + name + "&colorField=" + color + "&minField=" + minValue + "&maxField=" + maxValue + "&action=update"	+ "&autoCreateMax=" + 	autoMax;
	
  request.send( requestString );
  
  return false;
}

function updateLegendValidationCompleted(messageElement){
	var type = messageElement.getAttribute( 'type' );
	
	var message = messageElement.firstChild.nodeValue;
	 if ( type == 'success' )
 	 {
      // Both edit and add form has id='dataSetForm'      
      document.forms['editLegend'].submit();
  	 } else if ( type == 'input' )
 		 {
   			 document.getElementById( 'message' ).innerHTML = message;
   			 document.getElementById( 'message' ).style.display = 'block';
  		  }
}

function showLegendDetails(id){
  var request = new Request();
  request.setResponseTypeXML( 'legend' );
  request.setCallbackSuccess( showLegendDetailCompleted ); 
 
  var requestString = "showLegendDetails.action?id=" + id;
	
  request.send( requestString );
}

function showLegendDetailCompleted(legend){	

	var name = legend.getElementsByTagName('name')[0].firstChild.nodeValue;
	var color = legend.getElementsByTagName('color')[0].firstChild.nodeValue;
	var minValue = legend.getElementsByTagName('min')[0].firstChild.nodeValue;
	var maxValue = legend.getElementsByTagName('max')[0].firstChild.nodeValue;
	var indicators = legend.getElementsByTagName('indicators');
	
	for(var i=0;i<indicators.length;i++){
		var inName = indicators[i].getElementsByTagName('name')[0].firstChild.nodeValue;
		var inType = indicators[i].getElementsByTagName('type')[0].firstChild.nodeValue;
	}
	
}

// legend set-----------------------------------------------

function validateAddLegendSet(){
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( addLegendSetValidationCompleted ); 
  
  var name = document.getElementById( 'name' ).value;
  var indicatorId = document.getElementById( 'indicatorId' ).value; 
  
  var requestString = "validateLegendSet.action?name=" + name + "&indicatorId=" + indicatorId +"&action=add"	;

  request.send( requestString );
  
  return false;
}
function addLegendSetValidationCompleted(messageElement){
	var type = messageElement.getAttribute( 'type' );
	var message = messageElement.firstChild.nodeValue;
	 if ( type == 'success' )
 	 {
      // Both edit and add form has id='dataSetForm'      
      document.forms['addLegendSet'].submit();
  	 } else if ( type == 'input' )
 		 {
   			 document.getElementById( 'message' ).innerHTML = message;
   			 document.getElementById( 'message' ).style.display = 'block';
  		  }
}

function validateUpdateLegendSet(){
	var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( updateLegendSetValidationCompleted ); 
  
  var id = document.getElementById( 'id' ).value;
  var name = document.getElementById( 'name' ).value;
  var indicatorId = document.getElementById( 'indicatorId' ).value; 
  
  var requestString = "validateLegendSet.action?id=" + id + "&name=" + name + "&indicatorId=" + indicatorId +"&action=update"	;
	
  request.send( requestString );
  
  return false;
}

function updateLegendSetValidationCompleted(messageElement){
	var type = messageElement.getAttribute( 'type' );
	
	var message = messageElement.firstChild.nodeValue;
	 if ( type == 'success' )
 	 {
      // Both edit and add form has id='dataSetForm'      
      document.forms['editLegendSet'].submit();
  	 } else if ( type == 'input' )
 		 {
   			 document.getElementById( 'message' ).innerHTML = message;
   			 document.getElementById( 'message' ).style.display = 'block';
  		  }
}

function getIndicatorByIndicatorGroup(groupId, action){
	var request = new Request();
 	request.setResponseTypeXML( 'indicators' );
    request.setCallbackSuccess( responseGetIndicatorByIndicatorGroup);
	if(action=='add'){
		request.send( "getIndicatorByIndicatorGroup.action?indicatorGroupId=" + groupId);
	}else{
		var id = document.getElementById( 'id' ).value;
		request.send( "getIndicatorByIndicatorGroup.action?indicatorGroupId=" + groupId + "&legendSetId=" + id);
	}
	
    
}
function responseGetIndicatorByIndicatorGroup(indicators){
	var indicatorList = indicators.getElementsByTagName("indicator");
	var indicatorId = document.getElementById("availableIndicatorList");	
	var innerHTML="";
	for(var i=0;i<indicatorList.length;i++){
		var indicator = indicatorList.item(i);
		var id = indicator.getElementsByTagName('id')[0].firstChild.nodeValue;
		var name =  indicator.getElementsByTagName('name')[0].firstChild.nodeValue;
		innerHTML += "<option value="+id+">"+name+"</option>";
	}
	indicatorId.innerHTML = innerHTML;
}