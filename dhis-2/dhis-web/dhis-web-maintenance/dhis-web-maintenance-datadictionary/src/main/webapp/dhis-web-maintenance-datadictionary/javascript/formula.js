
function editFormula()
{
	var element_name = htmlEncode( document.getElementById( "name" ).value );
	var formula = htmlEncode( document.getElementById( "formula" ).value );
	var textualFormula = htmlEncode( document.getElementById( "textualFormula" ).value );
	var numeratorDescription = htmlEncode( document.getElementById( "numeratorDescription" ).value );
	var denominatorDescription = htmlEncode( document.getElementById( "denominatorDescription" ).value );
	
	var url = "editFormula.action?name=" + element_name + 
		"&formula=" + formula + 
		"&textualFormula=" + textualFormula + 
		"&numeratorDescription=" + numeratorDescription + 
		"&denominatorDescription=" + denominatorDescription;
    
    var dialog = window.open( url, "_blank", "directories=no, \
    		 height=560, width=790, location=no, menubar=no, status=no, \
    		 toolbar=no, resizable=no");
}

function insertText( inputAreaName, inputText )
{
	var inputArea = document.getElementById( inputAreaName );
	
	var startPos = inputArea.selectionStart;
	var endPos = inputArea.selectionEnd;
	
	var existingText = inputArea.value;
	var textBefore = existingText.substring( 0, startPos );
	var textAfter = existingText.substring( endPos, existingText.length );
	
	inputArea.value = textBefore + inputText + textAfter;
	
	updateFormulaText( inputAreaName );	
}

function filterDataElements( dataElementGroupSelectName, filterName )
{
	var dataElementGroup = document.getElementById( dataElementGroupSelectName );
	var dataElementGroupId = dataElementGroup.options[ dataElementGroup.selectedIndex ].value;	
	var filter = htmlEncode( document.getElementById( filterName ).value );
	
	var url = "getFilteredFormulaElements.action?dataElementGroupId=" + dataElementGroupId + "&filter=" + filter;
	
	var request = new Request();
	request.setResponseTypeXML( 'dataElement' );
    request.setCallbackSuccess( getFilteredDataElementsReceived );
    request.send( url );
}

function getFilteredDataElementsReceived( xmlObject )
{
	var dataElementList = document.getElementById( "dataElementId" );
			
	dataElementList.options.length = 0;
	
	var dataElements = xmlObject.getElementsByTagName( "dataElement" );
	
	for ( var i = 0; i < dataElements.length; i++)
	{
		var id = dataElements[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var elementName = dataElements[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		var option = document.createElement( "option" );
		option.value = "[" + id + "]";
		option.text = elementName;
		dataElementList.add( option, null );	
	}
}

function updateFormulaText( formulaFieldName )
{	
	var formula = htmlEncode( document.getElementById( formulaFieldName ).value );
	
	var url = "getFormulaText.action?formula=" + formula;
	
	var request = new Request();
	request.setCallbackSuccess( updateFormulaTextReceived );
    request.send( url );
}

function updateFormulaTextReceived( messageElement )
{
	document.getElementById( "textualFormula" ).innerHTML = messageElement;
}

function htmlEncode( str )
{	
	str = str.replace( /\(/g, "%28" );
	str = str.replace( /\)/g, "%29" );
	str = str.replace( /\*/g, "%2a" );
	str = str.replace( /\+/g, "%2b" );
	str = str.replace( /\-/g, "%2d" );
	str = str.replace( /\//g, "%2f" );
	
	return str;
}

function updateFormula()
{
	var formula = document.getElementById( "formula" ).value;	
	var textualFormula = document.getElementById( "textualFormula" ).innerHTML;
	var numeratorDescription = document.getElementById( "numeratorDescription" ).value;
	var denominatorDescription = document.getElementById( "denominatorDescription" ).value;
	
	if ( window.opener && !window.opener.closed )
	{
		window.opener.document.getElementById( "formula" ).value = formula;
		window.opener.document.getElementById( "textualFormula" ).value = textualFormula;
		window.opener.document.getElementById( "numeratorDescription" ).value = numeratorDescription;
		window.opener.document.getElementById( "denominatorDescription" ).value = denominatorDescription;
	}
	
	window.close();
}
