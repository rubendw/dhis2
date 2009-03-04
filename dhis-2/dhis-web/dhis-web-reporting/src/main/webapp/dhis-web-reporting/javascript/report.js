
var tempUrl = null;

function runAndViewReport( reportId, reportUrl )
{
    var url = "reCreateTable.action?id=" + reportId + "&mode=report";
    
    if ( document.getElementById( "reportingPeriod" ) != null )
    {
        url += "&reportingPeriod=" + document.getElementById( "reportingPeriod" ).value;
    }
    
    if ( document.getElementById( "parentOrganisationUnitId" ) != null )
    {
        url += "&parentOrganisationUnitId=" + document.getElementById( "parentOrganisationUnitId" ).value;
    }
    
    if ( document.getElementById( "organisationUnitId" ) != null )
    {
        url += "&organisationUnitId=" + document.getElementById( "organisationUnitId" ).value;
    }
    
    tempUrl = reportUrl;
    
    var request = new Request();
    request.setCallbackSuccess( runAndViewReportReceived );    
    request.send( url );
}

function runAndViewReportReceived( messageElement )
{   
    getReportStatus();
}

function getReportStatus()
{   
    var url = "getStatus.action";
    
    var request = new Request();
    request.setResponseTypeXML( "status" );
    request.setCallbackSuccess( reportStatusReceived );    
    request.send( url );
}

function reportStatusReceived( xmlObject )
{
    var statusMessage = getElementValue( xmlObject, "statusMessage" );
    var finished = getElementValue( xmlObject, "finished" );
    
    if ( finished == "true" )
    {
        setMessage( i18n_process_completed );
        
        viewReport( tempUrl );        
    }
    else if ( statusMessage == null )
    {
        setMessage( i18n_please_wait );
        
        waitAndGetReportStatus( 2000 );
    }
    else
    {
        setMessage( i18n_please_wait + ". " + statusMessage + "..."  );
        
        waitAndGetReportStatus( 2000 );
    }
}

function waitAndGetReportStatus( millis )
{
    setTimeout( "getReportStatus();", millis );
}

function viewReport( url )
{
	var dialog = window.open( url, "_blank", "directories=no, height=800, width=800, \
		location=no, menubar=no, status=no, toolbar=no, resizable=yes, scrollbars=yes" );
}

function addReport()
{
    selectAllById( "selectedReportTables" );
    
    document.getElementById( "reportForm" ).submit();
}

function removeReport( id )
{
	var dialog = window.confirm( i18n_confirm_remove_report );
	
	if ( dialog )
	{
		window.location.href = "removeReport.action?id=" + id;
	}
}
