
/**
 * Returns true if the element with the given identifier has text, false if not.
 * 
 * @param inputId the identifier of the input element.
 */
function hasText( inputId )
{
    return document.getElementById( inputId ).value != "";
}

/**
 * Returns true if the element with the given identifier is checked, false if not.
 * 
 * @param checkboxId the identifier of the checkbox element.
 */
function isChecked( checkboxId )
{
    return document.getElementById( checkboxId ).checked;
}

/**
 * Returns true if the element with the given identifier has selected elements
 * associated with it, false if not.
 * 
 * @param listId the identifier of the list element.
 */
function hasElements( listId )
{
    return document.getElementById( listId ).options.length > 0;
}

/**
 * Returns true if the element with the given identifier exists, false if not.
 * 
 * @param elementId the identifier of the element.
 */
function isNotNull( elementId )
{
    return document.getElementById( elementId ) != null ? true : false;
}

/**
 * HTML encodes the given string.
 * 
 * @param str the input string.
 * @return the HTML encoded string.
 */
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

/**
 * Gets the value for the element with the given name from the DOM object.
 * 
 * @param parentElement the DOM object.
 * @param childElementName the name of the element.
 */
function getElementValue( parentElement, childElementName )
{
    var textNode = parentElement.getElementsByTagName( childElementName )[0].firstChild;
    
    return textNode ? textNode.nodeValue : null;
}

/**
 * Sets a value on the given element.
 * 
 * @param fieldId the identifier of the element.
 * @param value the value to set.
 */
function setFieldValue( fieldId, value )
{
    document.getElementById( fieldId ).innerHTML = value;
}

/**
 * Sets a message in the "message" span and makes the span visible.
 * 
 * @param message the message. 
 */
function setMessage( message )
{
    document.getElementById( 'message' ).innerHTML = message;   
    document.getElementById( 'message' ).style.display = 'block';
}

/**
 * Makes the "message" span invisible.
 */
function hideMessage()
{
    document.getElementById( 'message' ).style.display = 'none';
}

/**
 * Makes the "detailsArea" span visible.
 */
function showDetails()
{
    document.getElementById( 'detailsArea' ).style.display = 'block';
}

/**
 * Makes the "detailsArea" span invisible.
 */
function hideDetails()
{
    document.getElementById( 'detailsArea' ).style.display = 'none';
}

/**
 * Makes the "warningArea" span visible.
 */
function showWarning()
{
    document.getElementById( 'warningArea' ).style.display = 'block';
}

/**
 * Makes the "warningArea" span invisible.
 */
function hideWarning()
{
    document.getElementById( 'warningArea' ).style.display = 'none';
}
