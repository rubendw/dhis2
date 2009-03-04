
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showDataElementGroupDetails( dataElementGroupId )
{
    var request = new Request();
    request.setResponseTypeXML( 'dataElementGroup' );
    request.setCallbackSuccess( dataElementGroupReceived );
    request.send( 'getDataElementGroup.action?id=' + dataElementGroupId );
}

function dataElementGroupReceived( dataElementGroupElement )
{
    setFieldValue( 'nameField', getElementValue( dataElementGroupElement, 'name' ) );
    setFieldValue( 'memberCountField', getElementValue( dataElementGroupElement, 'memberCount' ) );

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
// Remove data element group
// -----------------------------------------------------------------------------

function removeDataElementGroup( dataElementGroupId, dataElementGroupName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + dataElementGroupName );
    
    if ( result )
    {
        window.location.href = 'removeDataElementGroup.action?id=' + dataElementGroupId;
    }
}

// -----------------------------------------------------------------------------
// Add data element group
// -----------------------------------------------------------------------------

function validateAddDataElementGroup()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    request.send( 'validateDataElementGroup.action?name=' + getFieldValue( 'name' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var availableDataElements = document.getElementById( 'availableDataElements' );
        availableDataElements.selectedIndex = -1;
        
        var groupMembers = document.getElementById( 'groupMembers' );
        for ( var i = 0; i < groupMembers.options.length; ++i )
        {
            groupMembers.options[i].selected = true;
        }
        
        var form = document.getElementById( 'addDataElementGroupForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_data_element_group_failed + ':' + '\n' + message );
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
// Update data element group
// -----------------------------------------------------------------------------

function validateUpdateDataElementGroup()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateDataElementGroup.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var availableDataElements = document.getElementById( 'availableDataElements' );
        availableDataElements.selectedIndex = -1;
        
        var groupMembers = document.getElementById( 'groupMembers' );
        for ( var i = 0; i < groupMembers.options.length; ++i )
        {
            groupMembers.options[i].selected = true;
        }

        var form = document.getElementById( 'updateDataElementGroupForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_data_element_group_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}

// -----------------------------------------------------------------------------
// Select lists
// -----------------------------------------------------------------------------

function initLists()
{
    var list = document.getElementById( 'groupMembers' );
    var id;

    for ( id in groupMembers )
    {
        list.add( new Option( groupMembers[id], id ), null );
    }

    list = document.getElementById( 'availableDataElements' );

    for ( id in availableDataElements )
    {
        list.add( new Option( availableDataElements[id], id ), null );
    }
}

function filterGroupMembers()
{
    var filter = document.getElementById( 'groupMembersFilter' ).value;
    var list = document.getElementById( 'groupMembers' );
    
    list.options.length = 0;
    
    for ( var id in groupMembers )
    {
        var value = groupMembers[id];
        
        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add( new Option( value, id ), null );
        }
    }
}

function filterAvailableDataElements()
{
    var filter = document.getElementById( 'availableDataElementsFilter' ).value;
    var list = document.getElementById( 'availableDataElements' );
    
    list.options.length = 0;
    
    for ( var id in availableDataElements )
    {
        var value = availableDataElements[id];
        
        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add( new Option( value, id ), null );
        }
    }
}

function addGroupMembers()
{
    var list = document.getElementById( 'availableDataElements' );

    while ( list.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value;

        list.options[list.selectedIndex].selected = false;

        groupMembers[id] = availableDataElements[id];
        
        delete availableDataElements[id];        
    }
    
    filterGroupMembers();
    filterAvailableDataElements();
}

function removeGroupMembers()
{
    var list = document.getElementById( 'groupMembers' );

    while ( list.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value;

        list.options[list.selectedIndex].selected = false;

        availableDataElements[id] = groupMembers[id];
        
        delete groupMembers[id];        
    }
    
    filterGroupMembers();
    filterAvailableDataElements();
}
