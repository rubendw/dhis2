window.onload = function () 
{
	var inputs = document.getElementsByName( "entryfield" ) 

	for ( var i = 0, input; input = inputs[i]; i++ )
	{
		input.addEventListener('focus', valueFocus, false);
	}

    var selects = document.getElementsByName( "entryselect" );

	for ( var i = 0, select; select = selects[i]; i++ )
	{
		select.addEventListener('focus', valueFocus, false);
	}
}

/**
 * Display data element name in selection display when a value field recieves
 * focus.
 * XXX May want to move this to a separate function, called by valueFocus.
 * @param e focus event
 * @author Hans S. Tommerholt
 */
var customDataEntryFormExists = "false";

function valueFocus(e) 
{
	//Retrieve the data element id from the id of the field
	var baseId = e.target.id;
	var match = /.*\[(.*)\]/.exec(baseId); //value[-dataElementId-]

	if ( ! match )
	{
		return;
	}

	deId = match[1];
	
	//Get the data element name
	var nameContainer = document.getElementById('value['+deId+'].name');
	var minContainer = document.getElementById('value['+deId+'].min');
	var maxContainer = document.getElementById('value['+deId+'].max');
	
	if ( ! nameContainer )
	{
		return;
	}

	var name = '';
	var as = nameContainer.getElementsByTagName('a');

	if ( as.length > 0 )	//Admin rights: Name is in a link
	{
		name = as[0].firstChild.nodeValue;
	} 
	else 
	{
		if(customDataEntryFormExists == "true")
		{
		    name = nameContainer.firstChild.nodeValue;
		    name += " - "+minContainer.firstChild.nodeValue;
		    name += " - "+maxContainer.firstChild.nodeValue; 
		}
		else
		{
		    name = nameContainer.firstChild.nodeValue;
		}
	}

    var curDeSpan = document.getElementById('currentDataElement');
    curDeSpan.firstChild.nodeValue = name;
	
}

function keyPress( event, field )
{
    var key = 0;
    if ( event.charCode )
    {
    	key = event.charCode; /* Safari2 (Mac) (and probably Konqueror on Linux, untested) */
    }
    else
    {
		if ( event.keyCode )
		{
			key = event.keyCode; /* Firefox1.5 (Mac/Win), Opera9 (Mac/Win), IE6, IE7Beta2, Netscape7.2 (Mac) */
		}
		else
		{
			if ( event.which )
			{
				key = event.which; /* Older Netscape? (No browsers triggered yet) */
			}
	    }
	}
    
    if ( key == 13 ) /* CR */
    {
		nextField = getNextEntryField( field );
        if ( nextField )
        {
            nextField.focus(); /* Does not seem to actually work in Safari, unless you also have an Alert in between */
        }
        return true;
    }
    
    /* Illegal characters can be removed with a new if-block and return false */
    return true;
}

function getNextEntryField( field )
{
    var inputs = document.getElementsByName( "entryfield" );
    
    // Simple bubble sort
    for ( i = 0; i < inputs.length - 1; ++i )
    {
        for ( j = i + 1; j < inputs.length; ++j )
        {
            if ( inputs[i].tabIndex > inputs[j].tabIndex )
            {
                tmp = inputs[i];
                inputs[i] = inputs[j];
                inputs[j] = tmp;
            }
        }
    }
    
    i = 0;
    for ( ; i < inputs.length; ++i )
    {
        if ( inputs[i] == field )
        {
            break;
        }
    }
    
    if ( i == inputs.length - 1 )
    {
    	// No more fields after this:
    	return false;
    	// First field:
        //return inputs[0];
    }
    else
    {
        return inputs[i + 1];
    }
}

/**
 * Set min/max limits for dataelements that has one or more values, and no 
 * manually entred min/max limits.
 */
function SetGeneratedMinMaxValues()
{
    this.save = function()
    {
        var request = new Request();
        request.setCallbackSuccess( handleResponse );
        request.setCallbackError( handleHttpError );
        request.setResponseTypeXML( 'minmax' );
        request.send( 'minMaxGeneration.action' );
    };
    
    function handleResponse( rootElement )
    {
        var dataElements = rootElement.getElementsByTagName( 'dataelement' );
        
        for( i = 0; i < dataElements.length; i++ )
        {
            var id = getElementValue( dataElements[i], 'dataelementId' );
            setFieldValue('value[' + id + '].min', getElementValue( dataElements[i], 'minLimit'));
            setFieldValue('value[' + id + '].max', getElementValue( dataElements[i], 'maxLimit'));
        }
        
    }
    
    function handleHttpError( errorCode )
    {
        window.alert( i18n_saving_minmax_failed_error_code + '\n\n' + errorCode );
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
}

function generateMinMaxValues()
{    
    var setGeneratedMinMaxValues = new SetGeneratedMinMaxValues();
    setGeneratedMinMaxValues.save();
}