
function filterDataElements( aggregationOperatorRadioGroupName, dataElementGroupSelectName, filterName )
{
	var aggregationOperator = getSelectedRadioValue( aggregationOperatorRadioGroupName );
	var dataElementGroup = document.getElementById( dataElementGroupSelectName );
	var dataElementGroupId = dataElementGroup.options[ dataElementGroup.selectedIndex ].value;
	var filter = htmlEncode( document.getElementById( filterName ).value );
	
	var url = "getFilteredDataElements.action?aggregationOperator=" + aggregationOperator +
		"&dataElementGroupId=" + dataElementGroupId + "&filter=" + filter;
	
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

function getSelectedRadioValue( radioGroupName )
{
	var radioGroup = document.getElementsByName( radioGroupName );
	
	for ( var i = 0; i < radioGroup.length; i++ )
	{
		if ( radioGroup[ i ].checked )
		{
			return radioGroup[ i ].value;
		}
	}
	
	return null;
}

function disableRadioGroup( radioGroupName )
{
	var radioGroup = document.getElementsByName( radioGroupName );
	
	for ( var i = 0; i < radioGroup.length; i++ )
	{
		radioGroup[ i ].disabled = true;
	}
}

function enableRadioGroup( radioGroupName )
{
	var radioGroup = document.getElementsByName( radioGroupName );
	
	for ( var i = 0; i < radioGroup.length; i++ )
	{
		radioGroup[ i ].disabled = false;
	}
}

function validate( inputAreaName, radioGroupName )
{
	var inputArea = document.getElementById( inputAreaName );
	
	if ( inputArea.value.length == 0 )
	{
		enableRadioGroup( radioGroupName );
	}
	else
	{
		disableRadioGroup( radioGroupName );
	}
	
	updateFormulaText( inputAreaName );
}

function confirmAction( message, url )
{
	var res = window.confirm( message );
	
	if ( res )
	{
		window.location.href = url;
	}
}

function validateDenum()
{
	var formula = htmlEncode( document.getElementById( "formula" ).value );

	var url = "validateDenum.action?formula=" + formula;

	var request = new Request();
	request.setResponseTypeXML( "message" );
    request.setCallbackSuccess( validateDenumReceived );
    request.send( url );	
}

function validateDenumReceived( xmlObject )
{
	var type = xmlObject.getAttribute( 'type' );
	var message = xmlObject.firstChild.nodeValue;
	
	if ( type == "success" )
	{
		saveDenum();
	}
	else if ( type == "error" )
	{
		document.getElementById( "formulaText" ).innerHTML = message;
	}	
}

function saveDenum()
{
	enableRadioGroup( "aggregationOperator" );
	
	var formula = document.getElementById( "formula" ).value;
    var description = document.getElementById( "description" ).value;
    var aggregationOperator = getSelectedRadioValue( "aggregationOperator" );
    
	var type = htmlEncode( document.getElementById( "type" ).value );
    
    if ( window.opener && !window.opener.closed )
	{
		if ( type == "numerator" )
		{
			window.opener.document.getElementById( "numerator" ).value = formula;
			window.opener.document.getElementById( "numeratorDescription" ).value = description;
			window.opener.document.getElementById( "numeratorAggregationOperator" ).value = aggregationOperator;
		}
		else if ( type == "denominator" )
		{
			window.opener.document.getElementById( "denominator" ).value = formula;
			window.opener.document.getElementById( "denominatorDescription" ).value = description;
			window.opener.document.getElementById( "denominatorAggregationOperator" ).value = aggregationOperator;
		}
	}
	
	window.close();
}

function showEditNumeratorForm()
{
	var formula = htmlEncode( document.getElementById( "numerator" ).value );
    var description = htmlEncode( document.getElementById( "numeratorDescription" ).value );
    var aggregationOperator = htmlEncode( document.getElementById( "numeratorAggregationOperator" ).value );
    
    openDenum( "numerator", formula, description, aggregationOperator );
}

function showEditDenominatorForm()
{
    var formula = htmlEncode( document.getElementById( "denominator" ).value );
    var description = htmlEncode( document.getElementById( "denominatorDescription" ).value );
    var aggregationOperator = htmlEncode( document.getElementById( "denominatorAggregationOperator" ).value );
    
    openDenum( "denominator", formula, description, aggregationOperator );
}

function openDenum( type, formula, description, aggregationOperator )
{	
	var url = "editDenum.action?type=" + type + "&formula=" + formula + 
        "&description=" + description + "&aggregationOperator=" + aggregationOperator;
    						
    var dialog = window.open( url, "_blank", "directories=no, height=560, width=790, location=no, menubar=no, status=no, toolbar=no, resizable=yes, scrollbars=yes" );
}

function insertText( inputAreaName, inputText, radioGroupName )
{
	var inputArea = document.getElementById( inputAreaName );
	
	var startPos = inputArea.selectionStart;
	var endPos = inputArea.selectionEnd;
	
	var existingText = inputArea.value;
	var textBefore = existingText.substring( 0, startPos );
	var textAfter = existingText.substring( endPos, existingText.length );
	
	inputArea.value = textBefore + inputText + textAfter;
	
	disableRadioGroup( radioGroupName );
	
	updateFormulaText( inputAreaName );	
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
	document.getElementById( "formulaText" ).innerHTML = messageElement;
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
