
// -----------------------------------------------------------------------------
// Recreate ReportTable
// -----------------------------------------------------------------------------

function reCreateTable( tableId )
{
    var url = "reCreateTable.action?id=" + tableId + "&mode=table";
    
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
    
    var request = new Request();
    request.setCallbackSuccess( reCreateTableReceived );    
    request.send( url );
}

function reCreateTableReceived( messageElement )
{
    getTableStatus();
}

function getTableStatus()
{
    var url = "getStatus.action";
    
    var request = new Request();
    request.setResponseTypeXML( 'status' );
    request.setCallbackSuccess( tableStatusReceived );    
    request.send( url );
}

function tableStatusReceived( xmlObject )
{
    var statusMessage = getElementValue( xmlObject, 'statusMessage' );
    var finished = getElementValue( xmlObject, 'finished' );
    
    if ( finished == "true" )
    {
        setMessage( i18n_process_completed );
    }
    else if ( statusMessage == null )
    {
        setMessage( i18n_please_wait );
    }
    else
    {
        setMessage( i18n_please_wait + ". " + statusMessage + "..."  );
    }
    
    waitAndGetTableStatus( 2000 );
}

function waitAndGetTableStatus( millis )
{
    setTimeout( "getTableStatus();", millis );
}

// -----------------------------------------------------------------------------
// Create and save ReportTable
// -----------------------------------------------------------------------------

function createAndSaveTable()
{
    if ( validateCollections() )
    {
        var tableId = document.getElementById( "tableId" ).value;
        var tableName = document.getElementById( "tableName" ).value;
        
        var url = "validateTable.action?id=" + tableId + "&name=" + tableName;
        
        var request = new Request();
        request.setResponseTypeXML( 'message' );
        request.setCallbackSuccess( createAndSaveTableReceived );
        request.send( url );
    }
}

function createAndSaveTableReceived( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == "input" )
    {
        setMessage( message );
        
        return false;
    }
    else if ( type == "success" )
    {        
        selectTableForm();
        
        var form = document.getElementById( "tableForm" );
        
        form.action = "createTable.action";
        
        form.submit();
    }
}

// -----------------------------------------------------------------------------
// Save ReportTable
// -----------------------------------------------------------------------------

function saveTable()
{
    if ( validateCollections() )
    {
        var tableId = document.getElementById( "tableId" ).value;
        var tableName = document.getElementById( "tableName" ).value;
        
        var url = "validateTable.action?id=" + tableId + "&name=" + tableName;
        
        var request = new Request();
        request.setResponseTypeXML( 'message' );
        request.setCallbackSuccess( saveTableReceived );
        request.send( url );
    }
}

function saveTableReceived( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == "input" )
    {
        setMessage( message );
        
        return false;
    }
    else if ( type == "success" )
    {        
        selectTableForm();
        
        var form = document.getElementById( "tableForm" );
        
        form.action = "createTable!save.action";
        
        form.submit();
    }    
}

function removeTable( tableId, tableName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + tableName );
    
    if ( result )
    {
        window.location.href = "removeTable.action?id=" + tableId;
    }
}

function selectTableForm()
{
    if ( isNotNull( "selectedDataElements" ) )
    {
        selectAllById( "selectedDataElements" );
    }
    
    if ( isNotNull( "selectedIndicators" ) )
    {
       selectAllById( "selectedIndicators" );
    }
        
    if ( isNotNull( "selectedDataSets" ) )
    {
        selectAllById( "selectedDataSets" );
    }
    
    selectAllById( "selectedPeriods" );
    selectAllById( "selectedOrganisationUnits" );   
}

// -----------------------------------------------------------------------------
// Validation
// -----------------------------------------------------------------------------

function validateCollections()
{
    if ( isChecked( "doIndicators" ) && isChecked( "doPeriods" ) && isChecked( "doOrganisationUnits" ) )
    {
        setMessage( i18n_cannot_crosstab_all_dimensions );
        
        return false;
    }
        
    if ( !isChecked( "doIndicators" ) && !isChecked( "doPeriods" ) && !isChecked( "doOrganisationUnits" ) )
    {
        setMessage( i18n_cannot_crosstab_no_dimensions );
        
        return false;
    }
    
    if ( isNotNull( "selectedDataElements" ) && !hasElements( "selectedDataElements" ) )
    {
        setMessage( i18n_must_select_at_least_one_dataelement );
        
        return false;
    }
    
    if ( isNotNull( "selectedIndicators" ) && !hasElements( "selectedIndicators" ) )
    {
        setMessage( i18n_must_select_at_least_one_indicator );
        
        return false;
    }
    
    if ( !hasElements( "selectedOrganisationUnits" ) && !organisationUnitReportParamsChecked() )
    {
        setMessage( i18n_must_select_at_least_one_unit );
        
        return false;
    }
    
    if ( !hasElements( "selectedPeriods" ) && !relativePeriodsChecked() )
    {
        setMessage( i18n_must_select_at_least_one_period );
        
        return false;
    }
    
    return true;
}

function relativePeriodsChecked()
{
    if ( isChecked( "reportingMonth" ) == true ||
         isChecked( "last3Months" ) == true ||
         isChecked( "last6Months" ) == true ||
         isChecked( "last9Months" ) == true ||
         isChecked( "last12Months" ) == true ||
         isChecked( "last3To6Months" ) == true ||
         isChecked( "last6To9Months" ) == true ||
         isChecked( "last9To12Months" ) == true ||
         isChecked( "last12IndividualMonths" ) == true ||
         isChecked( "soFarThisYear" ) == true ||
         isChecked( "soFarThisFinancialYear" ) == true ||
         isChecked( "individualMonthsThisYear" ) == true ||
         isChecked( "individualQuartersThisYear" ) == true )
    {
        return true;
    }
    
    return false;
}

function organisationUnitReportParamsChecked()
{
    if ( isChecked( "paramParentOrganisationUnit" ) == true ||
         isChecked( "paramOrganisationUnit" ) == true )
    {
        return true;
    }
    
    return false;
}

// -----------------------------------------------------------------------------
// Details
// -----------------------------------------------------------------------------

function showTableDetails( tableId )
{
	var request = new Request();
    request.setResponseTypeXML( 'reportTable' );
    request.setCallbackSuccess( tableReceived );
    request.send( 'getTable.action?id=' + tableId );	
}

function tableReceived( xmlObject )
{
	setFieldValue( 'nameField', getElementValue( xmlObject, 'name' ) );
	setFieldValue( 'tableNameField', getElementValue( xmlObject, 'tableName' ) );
	setFieldValue( 'indicatorsField', getElementValue( xmlObject, 'indicators' ) );
	setFieldValue( 'periodsField', getElementValue( xmlObject, 'periods' ) );
	setFieldValue( 'unitsField', getElementValue( xmlObject, 'units' ) );
	setFieldValue( 'doIndicatorsField', parseBool( getElementValue( xmlObject, 'doIndicators' ) ) );
	setFieldValue( 'doPeriodsField', parseBool( getElementValue( xmlObject, 'doPeriods' ) ) );
	setFieldValue( 'doUnitsField', parseBool( getElementValue( xmlObject, 'doUnits' ) ) );
	
	showDetails();
}

function parseBool( bool )
{
	return ( bool == "true" ) ? i18n_yes : i18n_no;
}
