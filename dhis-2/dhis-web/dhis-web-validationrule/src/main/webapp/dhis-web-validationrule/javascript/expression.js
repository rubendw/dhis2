
function showEditLeftSideExpressionForm()
{
	var description = htmlEncode( document.getElementById( "leftSideDescription" ).value );
	var expression = htmlEncode( document.getElementById( "leftSideExpression" ).value );
	var textualExpression = htmlEncode( document.getElementById( "leftSideTextualExpression" ).value );
	
	showExpressionForm( "left", description, expression, textualExpression );
}

function showEditRightSideExpressionForm()
{
	var description = htmlEncode( document.getElementById( "rightSideDescription" ).value );
	var expression = htmlEncode( document.getElementById( "rightSideExpression" ).value );
	var textualExpression = htmlEncode( document.getElementById( "rightSideTextualExpression" ).value );
	
	showExpressionForm( "right", description, expression, textualExpression );
}

function showExpressionForm( side, description, expression, textualExpression )
{
	var url = "showEditExpressionForm.action?side=" + side +		
		"&description=" + description +
		"&expression=" + expression +
		"&textualExpression=" + textualExpression;
		
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
	
	updateTextualExpression( inputAreaName );	
}

function filterDataElements( dataElementGroupSelectName, filterName )
{
	var dataElementGroup = document.getElementById( dataElementGroupSelectName );
	var dataElementGroupId = dataElementGroup.options[ dataElementGroup.selectedIndex ].value;
	var filter = htmlEncode( document.getElementById( filterName ).value );
	
	var url = "getFilteredDataElements.action?dataElementGroupId=" + dataElementGroupId + "&filter=" + filter;

    var request = new Request();
	request.setResponseTypeXML( 'operand' );
    request.setCallbackSuccess( getFilteredDataElementsReceived );
    request.send( url );
}

function getFilteredDataElementsReceived( xmlObject )
{
	var operandList = document.getElementById( "dataElementId" );
			
	operandList.options.length = 0;
	
	var operands = xmlObject.getElementsByTagName( "operand" );
	
	for ( var i = 0; i < operands.length; i++)
	{
		var id = operands[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var elementName = operands[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		var option = document.createElement( "option" );
		option.value = "[" + id + "]";
		option.text = elementName;
		operandList.add( option, null );	
	}
}

function updateTextualExpression( expressionFieldName )
{	
	var expression = htmlEncode( document.getElementById( expressionFieldName ).value );
	
	var url = "getTextualExpression.action?expression=" + expression;
	
	var request = new Request();
	request.setCallbackSuccess( updateTextualExpressionReceived );
    request.send( url );
}

function updateTextualExpressionReceived( messageElement )
{
	document.getElementById( "textualExpression" ).innerHTML = messageElement;
}

function validateExpression()
{
	var description = htmlEncode( document.getElementById( "description" ).value );
	var expression = htmlEncode( document.getElementById( "expression" ).value );
    
    var url = "validateExpression.action?description=" + description + "&expression=" + expression;

    var request = new Request();
    request.setResponseTypeXML( "message" );
    request.setCallbackSuccess( validateExpressionReceived );
    request.send( url );    
}

function validateExpressionReceived( xmlObject )
{
    var type = xmlObject.getAttribute( 'type' );
    var message = xmlObject.firstChild.nodeValue;
    
    if ( type == "success" )
    {
        saveExpression();
    }
    else if ( type == "error" )
    {
        document.getElementById( "textualExpression" ).innerHTML = message;
    }   
}

function saveExpression()
{
    var description = document.getElementById( "description" ).value;
    var expression = document.getElementById( "expression" ).value;
    var textualDescription = document.getElementById( "textualExpression" ).innerHTML;
    
    var side = htmlEncode( document.getElementById( "side" ).value );
    
    if ( window.opener && !window.opener.closed )
    {
	    if ( side == "left" )
	    {
	    	window.opener.document.getElementById( "leftSideDescription" ).value = description;
			window.opener.document.getElementById( "leftSideExpression" ).value = expression;
			window.opener.document.getElementById( "leftSideTextualExpression" ).value = textualDescription;
		}
		else if ( side == "right" )
		{
			window.opener.document.getElementById( "rightSideDescription" ).value = description;
			window.opener.document.getElementById( "rightSideExpression" ).value = expression;
			window.opener.document.getElementById( "rightSideTextualExpression" ).value = textualDescription;
		}
    }

    window.close();
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
