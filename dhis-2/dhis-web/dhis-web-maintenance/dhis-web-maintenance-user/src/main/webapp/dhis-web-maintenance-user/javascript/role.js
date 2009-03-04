
// -----------------------------------------------------------------------------
// Remove role
// -----------------------------------------------------------------------------

function removeRole(id, role)
{
	if ( confirm( i18n_confirm_delete ) )
	{
		window.location.href = 'removeRole.action?id=' + id;						
	}		
}

function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
}

// -----------------------------------------------------------------------------
// Add role
// -----------------------------------------------------------------------------

function validateAddRole()
{
	var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    
    request.send( 'validateRole.action?name=' + getFieldValue( 'name' ) +
        '&description=' + getFieldValue( 'description' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        selectAll( document.getElementById( 'selectedList' ) );
        selectAll( document.getElementById( 'selectedListReport' ) );
		selectAll( document.getElementById( 'selectedListAuthority' ) );

        var form = document.getElementById( 'addRoleForm' );
        
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_role_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}

// -----------------------------------------------------------------------------
// Update role
// -----------------------------------------------------------------------------

function validateUpdateRole()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateRole.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) +
        '&description=' + getFieldValue( 'description' ) );
        
    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        selectAll( document.getElementById( 'selectedList' ) );
        selectAll( document.getElementById( 'selectedListReport' ) );
		selectAll( document.getElementById( 'selectedListAuthority' ) );

        var form = document.getElementById( 'updateRoleForm' );
        
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_user_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
