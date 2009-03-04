
function showValidationRuleDetails( validationId )
{
    var request = new Request();
    request.setResponseTypeXML( 'validationRule' );
    request.setCallbackSuccess( validationRuleReceived );
    request.send( 'getValidationRule.action?id=' + validationId );
}

function validationRuleReceived( validationElement )
{
    setFieldValue( 'nameField', getElementValue( validationElement, 'name' ) );
    var description = getElementValue( validationElement, 'description' );
    setFieldValue( 'descriptionField', description ? description : '[' + i18n_none + ']' );
    setFieldValue( 'leftSideDescriptionField', getElementValue( validationElement, 'leftSideDescription' ) );
    var operator = getElementValue( validationElement, 'operator' );
    setFieldValue( 'operatorField', i18nalizeOperator( operator ) );
	setFieldValue( 'rightSideDescriptionField', getElementValue( validationElement, 'rightSideDescription' ) );

    showDetails();
}

function i18nalizeOperator( operator )
{
    if ( operator == "equal_to" )
    {
        return i18n_equal_to;
    }
    else if ( operator == "not_equal_to" )
    {
        return i18n_not_equal_to;
    }
    else if ( operator == "greater_than" )
    {
        return i18n_greater_than;       
    }
    else if ( operator == "greater_than_or_equal_to" )
    {
        return i18n_greater_than_or_equal_to;
    }
    else if ( operator == "less_than" )
    {
        return i18n_less_than;
    }
    else if ( operator == "less_than_or_equal_to" )
    {
        return i18n_less_than_or_equal_to;
    }
    
    return null;
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

function setFieldValue( fieldId, value )
{
    document.getElementById( fieldId ).innerHTML = value;
}

function showDetails()
{
    document.getElementById( 'detailsArea' ).style.display = 'block';
}

function hideDetails()
{
	document.getElementById( 'detailsArea' ).style.display = 'none';
}

function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
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

function removeValidationRule( ruleId, ruleName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + ruleName );
    
    if ( result )
    {
        window.location.href = 'removeValidationRule.action?id=' + ruleId;
    }
}
