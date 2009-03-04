// JavaScript Document

var layer_actived;
var j = 0;

function exportRaster(){

var mapFileName = document.getElementById('mapFileName').value;



 window.open ("exportImage.action?mapFileName=" + mapFileName,"Exported Image", 'width=500,height=500,scrollbars=yes');

/*	
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( showInfoDetailsRecieved);
  request.send( "exportImage.action" );

*/
		 
}
function exportExcel(){

	var mapFileName = document.getElementById('mapFileName').value;	
	var indicatorId = document.getElementById("indicatorIdS").value;
	var startDate = document.getElementById("startDateS").value;
	var endDate = document.getElementById("endDateS").value;
	
	var url = "exportExcel.action?mapFileName="+mapFileName+"&indicatorId="+indicatorId+"&startDate=" + startDate + "&endDate="+endDate+"";
	//alert(url);
 window.open (url,"Exported Excel", 'width=500,height=500,scrollbars=yes');
		 
}

function readNode(node){
	
	var buffer ="";
	
	buffer += readAttributes(node);
	var nodeName = node.nodeName;
	var childNodes  = node.childNodes;
	
	if(childNodes!=null){
		if(childNodes.length!=0){
			for(i=0;i<childNodes.length;i++){
				var childNode = childNodes.item(i);
				buffer+=readNode(childNode);		
			}	
			buffer+="</" + nodeName + ">\n";
		}
	}
	
	return buffer;
	
}

function readAttributes(node){
	var nodeName = node.nodeName;
	var buffer = "<" +nodeName;
	var listAttribute = node.attributes;
	if(listAttribute!=null){
		for(i=0;i<listAttribute.length;i++){
			var attribute = listAttribute.item(i);
			buffer+=  " " + attribute.name + "=" + "\""  + attribute.nodeValue + "\"";
		}
	}
	
	buffer+=">";
	
	return buffer;
}



function zoomIn(){
	 var svg = document.embeds['map'].getSVGDocument().getElementsByTagName('svg');
	 svg = svg.item(0);	
	 var currenZoom = new Number(svg.getAttribute('width').replace("%",""));	
	 currenZoom+=10;
	 svg.setAttributeNS(null, "width", currenZoom+"%");
	 svg.setAttributeNS(null, "height", currenZoom+"%");
}
function zoomOut(){
	 var svg = document.embeds['map'].getSVGDocument().getElementsByTagName('svg');
	 svg = svg.item(0);	
	 var currenZoom = new Number(svg.getAttribute('width').replace("%",""));	
	 currenZoom-=10;
	 svg.setAttributeNS(null, "width", currenZoom+"%");
	 svg.setAttributeNS(null, "height", currenZoom+"%");
}

function retoreZoom(){
	 var svg = document.embeds['map'].getSVGDocument().getElementsByTagName('svg');	
	 svg = svg.item(0);	
	 svg.setAttributeNS(null, "width", "100%");
	 svg.setAttributeNS(null, "height", "100%");
}
function showlayer(checkbox, element_id){     

	   var svgobj = document.embeds['map'].getSVGDocument().getElementById(element_id);

		if (!checkbox.checked){

                svgobj.setAttributeNS(null,'visibility','hidden');

	    } else {

	        svgobj.setAttributeNS(null,'visibility','visible');		              

	        var radio_id = checkbox.id.replace('check', 'radio'); 
		
			document.getElementById(radio_id).checked = true;
			
			activelayer(element_id);

		}

}

function activelayer(element_id){

		var element = document.embeds['map'].getSVGDocument().getElementById(element_id);	

		element.parentNode.appendChild(element);		
		
		setAction(element_id);

		this.layer_actived=element_id;

		//getColorDefaut(this.layer_actived);

	/*	if(element_id=='hcmc1'){	

			removeEventForElement('csyt');

			removeEventForElement('dgt');

		}else if(element_id=='csyt'){

			removeEventForElement('hcmc1');

			removeEventForElement('dgt');

		}else{

			removeEventForElement('csyt');

			removeEventForElement('hcmc1');

		}		
*/
		

}
var lastFearureChoise;
var lastClickFill;
function selectedFeature(featureCode){
	if(featureCode!=""){
		var element =document.embeds['map'].getSVGDocument().getElementById(layer_actived);	
		var nodeList = element.getElementsByTagName('polygon');
			for(var i=0;i<nodeList.length;i++){
				g_element = nodeList.item(i);
				var id = g_element.getAttribute("id");
				if(featureCode==id){
					lastClickFill = g_element.getAttribute("fill");
					g_element.setAttributeNS(null, "fill", "red");
					lastFearureChoise = featureCode;
					
				}
				
			}
	}
	
}

function changeAction(element_id){
	
	var element =document.embeds['map'].getSVGDocument().getElementById(element_id);	
	var nodeList = element.getElementsByTagName('polygon');
	for(var i=0;i<nodeList.length;i++){
		g_element = nodeList.item(i);
		g_element.removeEventListener("mouseover", showInfo, false);
		g_element.removeEventListener("mouseout", hiddenInfo, false);
		g_element.addEventListener("click", getInfo, false);
	}
	this.layer_actived=element_id;
}

function nonSelect(element_id){
	var element =document.embeds['map'].getSVGDocument().getElementById(element_id);	
	var nodeList = element.getElementsByTagName('polygon');
	for(var i=0;i<nodeList.length;i++){
		g_element = nodeList.item(i);	
		
		var id = g_element.getAttribute("id");
		if(lastFearureChoise==id){
			g_element.setAttributeNS(null, "fill", lastClickFill);
		}
		
	}
}

function getInfo(evt){
	var target = evt.target;	
	if(lastFearureChoise){
		nonSelect(layer_actived);
	}
	
	
	if(layer_actived=="polyline"){
		  target.setAttributeNS(null, "stroke", stroke);
		  target.setAttributeNS(null, "stroke-width",stroke_width);
	}else{
		lastClickFill = target.getAttribute("fill");
		target.setAttributeNS(null, "fill", "red");
		var orgCode = target.getAttribute("id");
		lastFearureChoise = orgCode;
		document.getElementById('organisationUnitCode').value = orgCode;
		
	}
	
}


function removeAction(element_id){

	var element =document.embeds['map'].getSVGDocument().getElementById(element_id);	
	var nodeList = element.getElementsByTagName('polygon');
	for(var i=0;i<nodeList.length;i++){
		g_element = nodeList.item(i);
		g_element.removeEventListener("mouseover", showInfo, false);
		g_element.removeEventListener("mouseout", hiddenInfo, false);
	}
}

function setAction(element_id){
	//activelayer(element_id);
	var element =document.embeds['map'].getSVGDocument().getElementById(element_id);
	var nodeList = element.getElementsByTagName('polygon');
	for(var i=0;i<nodeList.length;i++){
			g_element = nodeList.item(i);
			g_element.addEventListener("mouseover", showInfo, false);
			//g_element.addEventListener("click", showInfo, false);
			g_element.addEventListener("mouseout", hiddenInfo, false);
	}	
	if(element_id=="polygon"){
		removeAction("polyline");
		removeAction("point");
	}else if(element_id=="polyline"){
		removeAction("polygon");
		removeAction("point");
	}else{
		removeAction("polygon");
		removeAction("polyline");
	}
	
}
var fill, stroke, stroke_width;
function hiddenInfo(evt){
	var target = evt.target;
	if(layer_actived=="polyline"){
		  target.setAttributeNS(null, "stroke", stroke);
		  target.setAttributeNS(null, "stroke-width",stroke_width);
	}else{
		target.setAttributeNS(null, "fill", fill);
	}
	
}

function showInfo(evt){

	var target = evt.target;	

	var name = target.getAttribute("attrib:district");
	
	var orgCode = target.getAttribute("id");
	
	var x = evt.clientX ;
	
	var y = evt.clientY ;
	
	
	
	fill = target.getAttribute("fill");
	stroke = target.getAttribute("stroke");
	stroke_width = target.getAttribute("stroke-width");
	if(layer_actived=="polyline"){
		  target.setAttributeNS(null, "stroke", "red");
		  target.setAttributeNS(null, "stroke-width", "200");
	}else{
		target.setAttributeNS(null, "fill", "#67F906");
		//showInfoDetails(orgCode);
	}
	
	
	
}


<!-- Ajax ---->
function showInfoDetails( orgCode )
{
  var request = new Request();
  request.setResponseTypeXML( 'organisationUnit' );
  request.setCallbackSuccess( showInfoDetailsRecieved);
  request.send( 'showInfoDetails.action?orgCode=' + orgCode );
}
function showInfoDetailsRecieved( mapObject )
{	
	var name = getElementValue(mapObject, "name");
	var shortName = getElementValue(mapObject,"shortName");
	var comment = getElementValue(mapObject,"comment");
	
	document.getElementById('name').innerHTML=name;
	document.getElementById('comment').innerHTML=comment;

}

function getElementValue( parentElement, childElementName )
{	
    var textNode = parentElement.getElementsByTagName( childElementName )[0].firstChild;

    if ( textNode )
    {
        return textNode.nodeValue;
    }
    else
    {
        return null;
    }
}
//---------get DataElement-------------------------
function getDataElementsByDataSet(dataSetId){	
	var request = new Request();
 	request.setResponseTypeXML( 'dataElements' );
    request.setCallbackSuccess( getDataElementByDataSetRecieved);
    request.send( 'getDataElement.action?dataSetId=' + dataSetId );
}
function getDataElementByDataSetRecieved(dataElements){
	var dataElements = dataElements.getElementsByTagName("dataElement");
	var selectedDataElement = document.getElementById("selectedDataElementId");
	var innerHTML = "<option value='null'>-----------------</option>";
	for(var i=0;i<dataElements.length;i++){
		var dataElement = dataElements.item(i);
		var id = dataElement.getElementsByTagName('id')[0].firstChild.nodeValue;
		var name =  dataElement.getElementsByTagName('name')[0].firstChild.nodeValue;
		innerHTML += "<option value="+id+">"+name+"</option>";
	}
	selectedDataElement.innerHTML = innerHTML;
}
//-----------getAllPeriodsType------------------------------------------------
function getPeriodTypes(){
	var request = new Request();
 	request.setResponseTypeXML( 'periodTypes' );
    request.setCallbackSuccess( getPeriodTypesRecieved);
    request.send( 'getPeiodTypes.action');
}
function getPeriodTypesRecieved(periodTypes){
	var periodTypes = periodTypes.getElementsByTagName("periodType");
	var periodTypeId = document.getElementById("periodTypeId");
	var innerHTML = "<option value='null'>-----------------</option>";
	for(var i=0;i<periodTypes.length;i++){
		var periodType = periodTypes.item(i);
		var id = periodType.getElementsByTagName('id')[0].firstChild.nodeValue;
		var name =  periodType.getElementsByTagName('name')[0].firstChild.nodeValue;
		innerHTML += "<option value="+id+">"+name+"</option>";
	}
	periodTypeId.innerHTML = innerHTML;
}

//-----------getPeriodByPeriodTypes------------------------------------------------
function getPeriodByPeriodType(periodTypeId){
	//alert(periodTypeName);
	var request = new Request();
 	request.setResponseTypeXML( 'periods' );
    request.setCallbackSuccess( getPeriodByPeriodTypeRecieved);
    request.send( "getPeriods.action?periodTypeId=" + periodTypeId);
}
function getPeriodByPeriodTypeRecieved(periods){
	var periodList = periods.getElementsByTagName("period");
	var periodSelect = document.getElementById("selectedPeriod");
	
	var innerHTML = "";
	for(var i=0;i<periodList.length;i++){
		var period = periodList.item(i);		
		var id = period.getElementsByTagName('id')[0].firstChild.nodeValue;
		var name =  period.getElementsByTagName('name')[0].firstChild.nodeValue;
		//alert(name);
		innerHTML += "<option value="+id+">"+name+"</option>";
	}
	periodSelect.innerHTML = innerHTML;
}

//-----------Fill Map------------------------------------------------

function fillMap(){
	
	//window.alert("sfddgfs");
	
	var orgUnitId = document.getElementById("orgUnit").value;
	var periodId = document.getElementById("selectedPeriodId").value;
	var selectedDataElementId = document.getElementById("selectedDataElementId").value;
	
	var url = "fillMap.action?orgUnitId="+orgUnitId+"&periodId="+periodId+"&selectedDataElementId="+selectedDataElementId;
	
	var request = new Request();
 	request.setResponseTypeXML( 'features' );
    request.setCallbackSuccess( fillMapRecieved);
    request.send( url);
	
}

function fillMapRecieved(features){
	
	var featureList = features.getElementsByTagName("feature");
	
	var legendList = features.getElementsByTagName("legend");
	
	//var section = features.getElementsByTagName("section");
	var innerHTML ="<legend>Legend</legend>";
		innerHTML+="<table width='100%' border='0'>";	
		innerHTML+="<tr align='center'>";	
		innerHTML+="<td>Color</td>"
		innerHTML+="<td></td>"
		innerHTML+="<td>Min</td>"
		innerHTML+="<td></td>"
		innerHTML+="<td>Max</td>"
		innerHTML+="</tr>";	
		for(var i=0;i<legendList.length;i++){	
			var legend = legendList.item(i);
			innerHTML+="<tr align='center'>";
			var color = legend.getElementsByTagName('color')[0].firstChild.nodeValue;
			var minValue = legend.getElementsByTagName('min')[0].firstChild.nodeValue;
			var maxValue = legend.getElementsByTagName('max')[0].firstChild.nodeValue;
			innerHTML+="<td width='50%' bgcolor='#"+color+"'>&nbsp;&nbsp;</td>";
			innerHTML+="<td width='10%'>&nbsp;</td>";
			innerHTML+="<td width='19%'>" + minValue + "</td>";
			innerHTML+="<td width='2%'>-</td>";
			innerHTML+="<td width='19%'>" + maxValue + "</td>";
			innerHTML+="</tr>";
		}			
	innerHTML+="</table>";	
	document.getElementById('legend').style.display="block";
	document.getElementById('legend').innerHTML = innerHTML;
	for(var i=0;i<featureList.length;i++){
		var feature = featureList.item(i);
		var orgCode = feature.getElementsByTagName('orgCode')[0].firstChild.nodeValue;
		var color =  feature.getElementsByTagName('color')[0].firstChild.nodeValue;	
		var value = feature.getElementsByTagName('value')[0].firstChild.nodeValue;	
		showLabel(orgCode,value);
		fillColor(orgCode,color);
	}
	
	document.getElementById('image').disabled = false;
	document.getElementById('excel').disabled = false;
	showHideAll('divunder');
	showHideAll('alert');
	
}

function fillColor(orgCode,color){
	var element =document.embeds['map'].getSVGDocument().getElementById('polygon');	
	
	var nodeList = element.getElementsByTagName('polygon');
	for(var i=0;i<nodeList.length;i++){
		polygon = nodeList.item(i);		
		//polygon.addEventListener("click", showIndicatorValue, false);
		var id = polygon.getAttribute("id");
		if(id==orgCode){
			polygon.setAttributeNS(null, "fill", color);	
			
		}
		
	}
	
}

function showIndicatorValue(){
	//var element =document.embeds['map'].getSVGDocument().getElementById('polygon');
	alert(1);
}
function showLabel(orgCode,value){
	var element =document.embeds['map'].getSVGDocument().getElementById('label');	
	//alert(element);
	
		var nodeList = element.getElementsByTagName('text');
		for(var i=0;i<nodeList.length;i++){
			text = nodeList.item(i);
			var id = text.getAttribute("id");
			if(id==orgCode){
				var lable = text.firstChild.nodeValue.split(":");	
				var newText = "";
				if(lable.length==1){
					newText = text.firstChild.nodeValue + ":" + value;
				}else{
					newText = lable[0] + ":" + value;
				}
				
				text.firstChild.nodeValue = newText;
			}
		}
	
	
}


function getIndicatorByGroup(groupId){
	var request = new Request();
 	request.setResponseTypeXML( 'indicators' );
    request.setCallbackSuccess( responseGetIndicatorByGroup);
    request.send( "getIndicatorByGroup.action?indicatorGroupId=" + groupId);
}
function responseGetIndicatorByGroup(indicators){
	var indicatorList = indicators.getElementsByTagName("indicator");
	var indicatorId = document.getElementById("indicatorId");
	var innerHTML = "<option value='null'>-----------------</option>";
	for(var i=0;i<indicatorList.length;i++){
		var indicator = indicatorList.item(i);
		var id = indicator.getElementsByTagName('id')[0].firstChild.nodeValue;
		var name =  indicator.getElementsByTagName('name')[0].firstChild.nodeValue;
		innerHTML += "<option value="+id+">"+name+"</option>";
	}
	indicatorId.innerHTML = innerHTML;
}

function getIndicatorByIndicatorGroup(groupId){
	var request = new Request();
 	request.setResponseTypeXML( 'indicators' );
    request.setCallbackSuccess( responseGetIndicatorByIndicatorGroup);
    request.send( "getIndicatorByGroup.action?indicatorGroupId=" + groupId);
}
function responseGetIndicatorByIndicatorGroup(indicators){
	var indicatorList = indicators.getElementsByTagName("indicator");
	var indicatorId = document.getElementById("indicatorIdS");
	var innerHTML = "";
	for(var i=0;i<indicatorList.length;i++){
		var indicator = indicatorList.item(i);
		var id = indicator.getElementsByTagName('id')[0].firstChild.nodeValue;
		var name =  indicator.getElementsByTagName('name')[0].firstChild.nodeValue;
		innerHTML += "<option value="+id+">"+name+"</option>";
	}
	indicatorId.innerHTML = innerHTML;
}







function searchIndicators(){
		
	
	var indicatorId = document.getElementById("indicatorIdS").value;	
	var beginValue = document.getElementById('beginValue').value;
	var endValue = document.getElementById('endValue').value;
	
	var str = "";
	if (indicatorId=='null' || indicatorId=='')
	{
		str += "You must select an indicator! \n";
		document.getElementById('endDateS').focus();
	}
	
	
	if (document.getElementById("orgUnit").value=='')
	{
		str += "You must select an organisation unit in the tree on the left hand side! \n";		
	}
	var getIndicatorFrom = document.getElementById("getIndicatorFromId").value;
	if(getIndicatorFrom=='aggregation_service'){
		var startDate = document.getElementById("startDateS").value;
		var endDate = document.getElementById("endDateS").value;
		if (startDate=='' || endDate=='')
		{
			str += "You must select an start and end date! \n";
			document.getElementById('endDateS').focus();
		}
	
		var url = "fillMapByIndicator.action?indicatorId="+indicatorId+"&startDate="+startDate+"&endDate="+endDate+"&valueBegin="+beginValue+"&valueEnd="+endValue;
	}else{
		var periodId = document.getElementById("selectedPeriod").value;
		
		if(periodId==''){
			str += "You must select an period\n";		
		}
		
		var url = "fillMapByIndicatorGetFromAgg.action?indicatorId="+indicatorId+"&periodId="+periodId +"&valueBegin="+beginValue+"&valueEnd="+endValue;
		
	}
	
	if (str!='')	
	{
		alert(str);
		return false;
	}
	
	refreshColor("#CCCCCC");
	
	showHideAll('divunder');
	showCenter('alert');
	
	var request = new Request();
	request.setResponseTypeXML( 'features' );
    request.setCallbackSuccess( fillMapRecieved);
    request.send( url);
}

function refreshColor(color){
	var element =document.embeds['map'].getSVGDocument().getElementById('polygon');	
	//window.alert(color);
//	alert(element);
	var nodeList = element.getElementsByTagName('polygon');
	for(var i=0;i<nodeList.length;i++){
		polygon = nodeList.item(i);		
		polygon.setAttributeNS(null, "fill", color);
	}
	
}

function addMapFile(){
	
	var mapFileName = document.getElementById("mapFileName").value;
	var request = new Request();
	request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addMapFileSuccess);
    request.send( "addMapFile.action?mapFileName=" + mapFileName );
	
	
}

function addMapFileSuccess(message){
 	var message = message.firstChild.nodeValue;	
	document.getElementById( 'message' ).innerHTML = message;
	
}

function drillDown(){
	var organisationUnitCode = document.getElementById('organisationUnitCode').value;
	window.location = "drillDown.action?organisationUnitCode=" + organisationUnitCode;
}

function showCenter(id){
	var div = document.getElementById(id);
	var width = div.style.width;
	var height = div.style.height;
	var x = (document.documentElement.clientHeight / 2) - new Number(height.replace('px','')/2);
	var y = (document.documentElement.clientWidth / 2) - new Number(width.replace('px',''))/2;	
	div.style.display = 'block';
	div.style.top = x +"px";
	div.style.left  = y +"px";	
	
}
