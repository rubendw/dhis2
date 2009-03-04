
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showIndicatorGroupDetails( indicatorGroupId )
{
    var request = new Request();
    request.setResponseTypeXML( 'indicatorGroup' );
    request.setCallbackSuccess( indicatorGroupReceived );
    request.send( 'getIndicatorGroup.action?id=' + indicatorGroupId );
}

function indicatorGroupReceived( indicatorGroupElement )
{
    setFieldValue( 'nameField', getElementValue( indicatorGroupElement, 'name' ) );
    setFieldValue( 'memberCountField', getElementValue( indicatorGroupElement, 'memberCount' ) );

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
// Remove indicator group
// -----------------------------------------------------------------------------

function removeIndicatorGroup( indicatorGroupId, indicatorGroupName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + indicatorGroupName );
    
    if ( result )
    {
        window.location.href = 'removeIndicatorGroup.action?id=' + indicatorGroupId;
    }
}

// -----------------------------------------------------------------------------
// Add indicator group
// -----------------------------------------------------------------------------

function validateAddIndicatorGroup()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    request.send( 'validateIndicatorGroup.action?name=' + getFieldValue( 'name' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var availableIndicators = document.getElementById( 'availableIndicators' );
        availableIndicators.selectedIndex = -1;
        
        var groupMembers = document.getElementById( 'groupMembers' );
        for ( var i = 0; i < groupMembers.options.length; ++i )
        {
            groupMembers.options[i].selected = true;
        }
        
        var form = document.getElementById( 'addIndicatorGroupForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_indicator_group_failed + ':' + '\n' + message );
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
// Update indicator group
// -----------------------------------------------------------------------------

function validateUpdateIndicatorGroup()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateIndicatorGroup.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var availableIndicators = document.getElementById( 'availableIndicators' );
        availableIndicators.selectedIndex = -1;
        
        var groupMembers = document.getElementById( 'groupMembers' );
        for ( var i = 0; i < groupMembers.options.length; ++i )
        {
            groupMembers.options[i].selected = true;
        }

        var form = document.getElementById( 'updateIndicatorGroupForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_indicator_group_failed + ':' + '\n' + message );
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

    list = document.getElementById( 'availableIndicators' );

    for ( id in availableIndicators )
    {
        list.add( new Option( availableIndicators[id], id ), null );
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

function filterAvailableIndicators()
{
    var filter = document.getElementById( 'availableIndicatorsFilter' ).value;
    var list = document.getElementById( 'availableIndicators' );
    
    list.options.length = 0;
    
    for ( var id in availableIndicators )
    {
        var value = availableIndicators[id];
        
        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add( new Option( value, id ), null );
        }
    }
}

function addGroupMembers()
{
    var list = document.getElementById( 'availableIndicators' );

    while ( list.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value;

        list.options[list.selectedIndex].selected = false;

        groupMembers[id] = availableIndicators[id];
        
        delete availableIndicators[id];        
    }
    
    filterGroupMembers();
    filterAvailableIndicators();
}

function removeGroupMembers()
{
    var list = document.getElementById( 'groupMembers' );

    while ( list.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value;

        list.options[list.selectedIndex].selected = false;

        availableIndicators[id] = groupMembers[id];
        
        delete groupMembers[id];        
    }
    
    filterGroupMembers();
    filterAvailableIndicators();
}
