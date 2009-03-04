
// -----------------------------------------------------------------------------
// Add indicator
// -----------------------------------------------------------------------------

function validateAddExtendedIndicator()
{
	var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    
    var params = 'name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&alternativeName=' + getFieldValue( 'alternativeName' ) +
        '&code=' + getFieldValue( 'code' ) +
        '&description=' + getFieldValue( 'description' ) +
        '&numeratorDescription=' + getFieldValue( 'numeratorDescription' ) +
        '&denominatorDescription=' + getFieldValue( 'denominatorDescription' ) +
        '&mnemonic=' + getFieldValue( 'mnemonic' ) +
        '&version=' + getFieldValue( 'version' ) +
        '&keywords=' + getFieldValue( 'keywords' ) +
        '&minimumSize=' + getFieldValue( 'minimumSize' ) +
        '&maximumSize=' + getFieldValue( 'maximumSize' ) +
        '&responsibleAuthority=' + getFieldValue( 'responsibleAuthority' ) +
        '&location=' + getFieldValue( 'location' ) +
        '&reportingMethods=' + getFieldValue( 'reportingMethods' ) +
        '&versionStatus=' + getFieldValue( 'versionStatus' );
    
    request.sendAsPost( params );
    request.send( 'validateExtendedIndicator.action' );

    return false;
}

function addValidationCompleted( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addExtendedIndicatorForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_indicator_failed + ':' + '\n' + message );
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

// -----------------------------------------------------------------------------
// Update data element
// -----------------------------------------------------------------------------

function validateUpdateExtendedIndicator()
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
        '&numeratorDescription=' + getFieldValue( 'numeratorDescription' ) +
        '&denominatorDescription=' + getFieldValue( 'denominatorDescription' ) +
        '&mnemonic=' + getFieldValue( 'mnemonic' ) +
        '&version=' + getFieldValue( 'version' ) +
        '&keywords=' + getFieldValue( 'keywords' ) +
        '&minimumSize=' + getFieldValue( 'minimumSize' ) +
        '&maximumSize=' + getFieldValue( 'maximumSize' ) +
        '&responsibleAuthority=' + getFieldValue( 'responsibleAuthority' ) +
        '&location=' + getFieldValue( 'location' ) +
        '&reportingMethods=' + getFieldValue( 'reportingMethods' ) +
        '&versionStatus=' + getFieldValue( 'versionStatus' );
    
    request.sendAsPost( params );
    request.send( 'validateExtendedIndicator.action' );
    
    return false;
}

function updateValidationCompleted( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateExtendedIndicatorForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_indicator_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
