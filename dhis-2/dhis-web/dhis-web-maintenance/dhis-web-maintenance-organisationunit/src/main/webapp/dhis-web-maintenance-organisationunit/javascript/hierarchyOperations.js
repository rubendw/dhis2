
// -----------------------------------------------------------------------------
// Organisation unit to move
// -----------------------------------------------------------------------------

function organisationUnitToMoveSelected( orgUnitIds )
{
    document.getElementById( 'message' ).style.display = 'none';

    if ( orgUnitIds.length == 1 )
    {
        var request = new Request();
        request.setResponseTypeXML( 'organisationUnit' );
        request.setCallbackSuccess( organisationUnitToMoveReceived );
        request.send( 'getOrganisationUnit.action?id=' + orgUnitIds[0] );
    }
}

selection.setListenerFunction( organisationUnitToMoveSelected );

function organisationUnitToMoveReceived( unitElement )
{
    var organisationUnit = parseUnitElement( unitElement );
    
    document.getElementById( 'organisationUnitToMoveId' ).value = organisationUnit['id'];
    document.getElementById( 'toMoveNameField' ).innerHTML = organisationUnit['name'];
    
    document.getElementById( 'confirmOrganisationUnitToMoveButton' ).disabled = false;
}

function organisationUnitToMoveConfirmed()
{
    var id = document.getElementById( 'organisationUnitToMoveId' ).value;

    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( organisationUnitToMoveFeedback );
    request.send( 'validateOrganisationUnitToMove.action?organisationUnitToMoveId=' + id );
}

function organisationUnitToMoveFeedback( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        document.getElementById( 'confirmOrganisationUnitToMoveButton' ).disabled = true;
        document.getElementById( 'confirmNewParentOrganisationUnitButton' ).disabled = false;
        
        document.getElementById( 'step1' ).style.backgroundColor = 'white';
        document.getElementById( 'step2' ).style.backgroundColor = '#ccffcc';
        
        selection.setListenerFunction( newParentSelected );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
    else if ( type == 'error' )
    {
        window.alert( verication_of_the_org_unit_to_move_failed + ':\n\n' + message );
    }
}

// -----------------------------------------------------------------------------
// New parent organisation unit
// -----------------------------------------------------------------------------

function newParentSelected( orgUnitIds )
{
    document.getElementById( 'message' ).style.display = 'none';

    if ( orgUnitIds.length == 1 )
    {
        var request = new Request();
        request.setResponseTypeXML( 'organisationUnit' );
        request.setCallbackSuccess( newParentOrganisationUnitReceived );
        request.send( 'getOrganisationUnit.action?id=' + orgUnitIds[0] );
    }
    else if ( orgUnitIds.length == 0 )
    {
        document.getElementById( 'newParentOrganisationUnitId' ).value = '';
        document.getElementById( 'newParentNameField' ).innerHTML = '[' + not_selected_moved_to_root_position + ']';
    }
}

function newParentOrganisationUnitReceived( unitElement )
{
    var organisationUnit = parseUnitElement( unitElement );
    
    document.getElementById( 'newParentOrganisationUnitId' ).value = organisationUnit['id'];
    document.getElementById( 'newParentNameField' ).innerHTML = organisationUnit['name'];
}

function newParentOrganisationUnitConfirmed()
{
    var toMoveId = document.getElementById( 'organisationUnitToMoveId' ).value
    var newParentId = document.getElementById( 'newParentOrganisationUnitId' ).value;

    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( newParentOrganisationUnitFeedback );
    request.send( 'validateNewParentOrganisationUnit.action?organisationUnitToMoveId=' + toMoveId +
        '&newParentOrganisationUnitId=' + newParentId );
}

function newParentOrganisationUnitFeedback( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        document.getElementById( 'confirmNewParentOrganisationUnitButton' ).disabled = true;
        document.getElementById( 'submitButton' ).disabled = false;
        
        document.getElementById( 'step2' ).style.backgroundColor = 'white';
        document.getElementById( 'step3' ).style.backgroundColor = '#ccffcc';
        
        selection.setListenerFunction( null );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
    else if ( type == 'error' )
    {
        window.alert( verification_of_the_new_parent_org_unit_failed + ':\n\n' + message );
    }
}

// -----------------------------------------------------------------------------
// Common
// -----------------------------------------------------------------------------

function parseUnitElement( unitElement )
{
    var organisationUnit = new Object();

    organisationUnit['id'] = unitElement.getElementsByTagName( 'id' )[0].firstChild.nodeValue;
    organisationUnit['name'] = unitElement.getElementsByTagName( 'name' )[0].firstChild.nodeValue;
    
    return organisationUnit;
}
