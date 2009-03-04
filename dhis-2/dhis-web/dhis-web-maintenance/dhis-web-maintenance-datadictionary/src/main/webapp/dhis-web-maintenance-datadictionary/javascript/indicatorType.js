
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showIndicatorTypeDetails( indicatorTypeId )
{
    var request = new Request();
    request.setResponseTypeXML( 'indicatorType' );
    request.setCallbackSuccess( indicatorTypeReceived );
    request.send( 'getIndicatorType.action?id=' + indicatorTypeId );
}

function indicatorTypeReceived( indicatorTypeElement )
{
    setFieldValue( 'nameField', getElementValue( indicatorTypeElement, 'name' ) );
    setFieldValue( 'factorField', getElementValue( indicatorTypeElement, 'factor' ) );

    showDetails();
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

// -----------------------------------------------------------------------------
// Remove indicator type
// -----------------------------------------------------------------------------

function removeIndicatorType( indicatorTypeId, indicatorTypeName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + indicatorTypeName );
    
    if ( result )
    {
        var request = new Request();
        request.setResponseTypeXML( 'message' );
        request.setCallbackSuccess( removeIndicatorTypeCompleted );
        request.send( 'removeIndicatorType.action?id=' + indicatorTypeId );
    }
}

function removeIndicatorTypeCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        window.location.href = 'indicatorType.action';
    }
    else if ( type = 'error' )
    {
        setFieldValue( 'warningField', message );
        
        showWarning();
    }
}

// -----------------------------------------------------------------------------
// Add indicator type
// -----------------------------------------------------------------------------

function validateAddIndicatorType()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    request.send( 'validateIndicatorType.action?name=' + getFieldValue( 'name' ) +
        '&factor=' + getFieldValue( 'factor' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addIndicatorTypeForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_indicator_type_failed + ':' + '\n' + message );
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
// Update indicator type
// -----------------------------------------------------------------------------

function validateUpdateIndicatorType()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateIndicatorType.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) +
        '&factor=' + getFieldValue( 'factor' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateIndicatorTypeForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_indicator_type_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
