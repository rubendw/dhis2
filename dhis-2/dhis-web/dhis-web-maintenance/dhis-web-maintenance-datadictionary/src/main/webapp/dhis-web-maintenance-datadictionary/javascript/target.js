
function getTargetDetails( targetId )
{
	var request = new Request();    
    request.setResponseTypeXML( 'target' );    
    request.setCallbackSuccess( targetReceived );      
    request.send( 'getTarget.action?targetId=' + targetId );
}

function targetReceived( targetElement ) 
{				
	setFieldValue( 'nameField', getElementValue( targetElement, 'name' ) );	
	setFieldValue( 'shortNameField', getElementValue( targetElement, 'shortName') );
	setFieldValue( 'valueField', getElementValue( targetElement, 'value') );
	setFieldValue( 'descriptionField', getElementValue( targetElement, 'description') );
	setFieldValue( 'indicatorNameField', getElementValue( targetElement, 'indicatorName') );
	setFieldValue( 'currentIndicatorValueField', getElementValue( targetElement, 'currentIndicatorValue') );
	setFieldValue( 'periodIdField', getElementValue( targetElement, 'periodId') );			
				
	showDetails();
}

function showDetails()
{
    document.getElementById( 'detailsArea' ).style.display = 'block';
}

function hideDetails()
{
	document.getElementById( 'detailsArea' ).style.display = 'none';
}

function setFieldValue( fieldId, value )
{
    document.getElementById( fieldId ).innerHTML = value;
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

/**
 * Get the value of the specified field
 * 
 * @param fieldId id of the field to get the value for
 * @return String value of the specified field,
 * @throws Exception if the given field doesn't exist.
 */
function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
}

/**
 * Returns the first value of the specified select box
 * 
 * @param selectId
 * @return The first (or only) String value of the given select box, 
 * 		or null if no options are selected
 */
function getSelectValue( selectId )
{
	var select = document.getElementById( selectId );
	var option = select.options[select.selectedIndex];
	
	if ( option )
	{
		return option.value;
	}
	else
	{
		return null;
	}
}

function removeTarget( targetId, targetName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + targetName );
    
    if ( result )
    {
        window.location.href = 'removeTarget.action?targetId=' + targetId;
    }
}
