
function getImportStatus()
{
	var request = new Request();
	request.setResponseTypeXML( "response" );
    request.setCallbackSuccess( importStatusReceived );    
    request.send( "getImportStatus.action" );
}

function importStatusReceived( messageElement )
{
	var actionType = getElementValue( messageElement, "actionType" );
	
	if ( actionType == "info" )
	{
		var percentageCompleted = getElementValue( messageElement, "percentageCompleted" );
		var statusMessage = getElementValue( messageElement, "statusMessage" );
		var fileMessage = getElementValue( messageElement, "fileMessage" );
		
		var message = percentageCompleted + " % " + i18n_completed + ": " + statusMessage;
		var info = i18n_current_import_file + ": " + fileMessage;
		
		setMessage( message );
		setInfo( info );
		
		waitAndGetImportStatus( 1500 );
	}
	else if ( actionType == "preview" )
	{
		window.location.href = "displayPreviewForm.action";
	}
}

function waitAndGetImportStatus( millis )
{
	setTimeout( "getImportStatus();", millis );
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
