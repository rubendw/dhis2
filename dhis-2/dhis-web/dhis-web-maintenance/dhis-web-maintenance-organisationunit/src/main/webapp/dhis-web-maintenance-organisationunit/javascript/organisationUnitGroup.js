
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showOrganisationUnitGroupDetails( unitId )
{
    var request = new Request();
    request.setResponseTypeXML( 'organisationUnitGroup' );
    request.setCallbackSuccess( organisationUnitGroupReceived );
    request.send( 'getOrganisationUnitGroup.action?id=' + unitId );
}

function organisationUnitGroupReceived( unitGroupElement )
{
    setFieldValue( 'nameField', getElementValue( unitGroupElement, 'name' ) );
    setFieldValue( 'memberCountField', getElementValue( unitGroupElement, 'memberCount' ) );
    
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
// Remove organisation unit group
// -----------------------------------------------------------------------------

function removeOrganisationUnitGroup( unitGroupId, unitGroupName )
{
    var result = window.confirm( confirm_to_delete_org_unit_group + '\n\n' + unitGroupName );
    
    if ( result )
    {
        window.location.href = 'removeOrganisationUnitGroup.action?id=' + unitGroupId;
    }
}

// -----------------------------------------------------------------------------
// Add organisation unit group
// -----------------------------------------------------------------------------

function validateAddOrganisationUnitGroup()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    request.send( 'validateOrganisationUnitGroup.action?name=' + getFieldValue( 'name' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addOrganisationUnitGroupForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( adding_the_org_unit_group_failed + ':\n' + message );
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
// Update organisation unit group
// -----------------------------------------------------------------------------

function validateUpdateOrganisationUnitGroup()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateOrganisationUnitGroup.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateOrganisationUnitGroupForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( saving_the_org_unit_group_failed + ':\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
