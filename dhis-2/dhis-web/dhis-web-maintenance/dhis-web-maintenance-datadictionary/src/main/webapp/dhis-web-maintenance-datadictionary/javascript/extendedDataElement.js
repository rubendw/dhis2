
// -----------------------------------------------------------------------------
// Add data element
// -----------------------------------------------------------------------------

function validateAddExtendedDataElement()
{
	var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    
    var params = 'name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&alternativeName=' + getFieldValue( 'alternativeName' ) +
        '&code=' + getFieldValue( 'code' ) +
        '&description=' + getFieldValue( 'description' ) +
        '&mnemonic=' + getFieldValue( 'mnemonic' ) +
        '&version=' + getFieldValue( 'version' ) +
        '&keywords=' + getFieldValue( 'keywords' ) +
        '&dataElementType=' + getListValue( 'dataElementType' ) +
        '&minimumSize=' + getFieldValue( 'minimumSize' ) +
        '&maximumSize=' + getFieldValue( 'maximumSize' ) +
        '&responsibleAuthority=' + getFieldValue( 'responsibleAuthority' ) +
        '&location=' + getFieldValue( 'location' ) +
        '&reportingMethods=' + getFieldValue( 'reportingMethods' ) +
        '&versionStatus=' + getFieldValue( 'versionStatus' );
    
    request.sendAsPost( params );
    request.send( 'validateExtendedDataElement.action' );

    return false;
}

function addValidationCompleted( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addExtendedDataElementForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_data_element_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}

function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
}

function getListValue( listId )
{
	var list = document.getElementById( listId );
	
	return list.options[ list.selectedIndex ].value;
}

// -----------------------------------------------------------------------------
// Update data element
// -----------------------------------------------------------------------------

function validateUpdateExtendedDataElement()
{
	var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    
    var params = 'id=' + getFieldValue( 'id' ) + 
    	'&name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&alternativeName=' + getFieldValue( 'alternativeName' ) +
        '&code=' + getFieldValue( 'code' ) +
        '&description=' + getFieldValue( 'description' ) +
        '&mnemonic=' + getFieldValue( 'mnemonic' ) +
        '&version=' + getFieldValue( 'version' ) +
        '&keywords=' + getFieldValue( 'keywords' ) +
        '&dataElementType=' + getListValue( 'dataElementType' ) +        
        '&minimumSize=' + getFieldValue( 'minimumSize' ) +
        '&maximumSize=' + getFieldValue( 'maximumSize' ) +
        '&responsibleAuthority=' + getFieldValue( 'responsibleAuthority' ) +
        '&location=' + getFieldValue( 'location' ) +
        '&reportingMethods=' + getFieldValue( 'reportingMethods' ) +
        '&versionStatus=' + getFieldValue( 'versionStatus' );
    
    request.sendAsPost( params );
    request.send( 'validateExtendedDataElement.action' );
    
    return false;
}

function updateValidationCompleted( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateExtendedDataElementForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_data_element_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
