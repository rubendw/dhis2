
var selectedOrganisationUnitId = null;

function setSelectedOrganisationUnitId( ids )
{
    if ( ids != null && ids.length == 1 )
    {
        selectedOrganisationUnitId = ids[0];
    }

    displayCompleteness();
}

selectionTreeSelection.setListenerFunction( setSelectedOrganisationUnitId );

function displayCompleteness()
{
    var dataSetList = document.getElementById( "dataSetId" );
    
    var dataSetId = dataSetList.options[ dataSetList.selectedIndex ].value;
    
    var periodList = document.getElementById( "periodId" );

    var periodId = null;
    
    if ( periodList.disabled == false )
    {
        periodId = periodList.options[ periodList.selectedIndex ].value;
    }
    
    if ( periodId != null && selectedOrganisationUnitId != null )
    {
        clearTable( "resultTable" );
        
        setMessage( i18n_processing + "..." );
        
        var request = new Request();
        
        var url = null;
        
        request.setResponseTypeXML( "dataSetCompletenessResult" );
        
        if ( dataSetId == "ALL" )
        {            
            // -----------------------------------------------------------------
            // Display completeness by DataSets
            // -----------------------------------------------------------------
            
            url = "getDataCompleteness.action?periodId=" + periodId + 
                  "&organisationUnitId=" + selectedOrganisationUnitId;
            
            request.setCallbackSuccess( displayCompletenessByDataSetReceived );
        }
        else
        {
            // -----------------------------------------------------------------
            // Display completeness by child OrganisationUnits for a DataSet
            // -----------------------------------------------------------------
            
            url = "getDataCompleteness.action?periodId=" + periodId + 
                  "&organisationUnitId=" + selectedOrganisationUnitId +
                  "&dataSetId=" + dataSetId;
            
            request.setCallbackSuccess( displayCompletenessByOrganisationUnitReceived );
        }               
        
        request.send( url );
    }
}

function clearTable( tableId )
{
    var table = document.getElementById( tableId );
    
    while ( table.rows.length >  0 )
    {
        table.deleteRow( 0 );
    }
}

function displayCompletenessByDataSetReceived( xmlObject )
{
    var headerText = i18n_dataset;
    
    displayCompletenessTable( xmlObject, headerText );
}

function displayCompletenessByOrganisationUnitReceived( xmlObject )
{
    var headerText = i18n_organisation_unit;
    
    displayCompletenessTable( xmlObject, headerText );
}

function displayCompletenessTable( xmlObject, headerText )
{
    hideMessage();
    
    var table = document.getElementById( "resultTable" );
    
    // -------------------------------------------------------------------------
    // Adding header
    // -------------------------------------------------------------------------
    
    var headerRow = table.insertRow( 0 );
    
    var columnWidth = "55px";
    
    var headerA = document.createElement( "th" );
    headerA.innerHTML = headerText;
    headerRow.appendChild( headerA );
    
    var headerB = document.createElement( "th" );
    headerB.innerHTML = i18n_actual;
    headerB.width = columnWidth;
    headerRow.appendChild( headerB );
    
    var headerC = document.createElement( "th" );
    headerC.innerHTML = i18n_target;
    headerC.width = columnWidth;
    headerRow.appendChild( headerC );
    
    var headerD = document.createElement( "th" );
    headerD.innerHTML = i18n_percent;
    headerD.width = columnWidth;
    headerRow.appendChild( headerD );
    
    var headerE = document.createElement( "th" );
    headerE.innerHTML = i18n_on_time;
    headerE.width = columnWidth;
    headerRow.appendChild( headerE );
    
    var headerF = document.createElement( "th" );
    headerF.innerHTML = i18n_percent;
    headerF.width = columnWidth;
    headerRow.appendChild( headerF );    
    
    // -------------------------------------------------------------------------
    // Adding rows
    // -------------------------------------------------------------------------
    
    var results = xmlObject.getElementsByTagName( "dataSetCompletenessResult" );
    
    var mark = false;
    
    var rowIndex = 1;
    
    for ( var i = 0; i < results.length; i++ )
    {
        var resultName = results[i].getElementsByTagName( "name" )[0].firstChild.nodeValue;
        var sources = results[i].getElementsByTagName( "sources" )[0].firstChild.nodeValue;
        var registrations = results[i].getElementsByTagName( "registrations" )[0].firstChild.nodeValue;
        var percentage = results[i].getElementsByTagName( "percentage" )[0].firstChild.nodeValue;
        var registrationsOnTime = results[i].getElementsByTagName( "registrationsOnTime" )[0].firstChild.nodeValue;
        var percentageOnTime = results[i].getElementsByTagName( "percentageOnTime" )[0].firstChild.nodeValue;
        
        var row = table.insertRow( rowIndex++ );
        
        var cellA = row.insertCell( 0 );
        cellA.style.height = "32px";
        cellA.innerHTML = resultName;
        
        var cellB = row.insertCell( 1 );
        cellB.innerHTML = registrations;
        
        var cellC = row.insertCell( 2 );
        cellC.innerHTML = sources;
        
        var cellD = row.insertCell( 3 );
        cellD.innerHTML = percentage;
        
        var cellE = row.insertCell( 4 );
        cellE.innerHTML = registrationsOnTime;
                
        var cellF = row.insertCell( 5 );
        cellF.innerHTML = percentageOnTime;
        
        if ( mark )
        {
            row.style.backgroundColor = "#dddddd";
        }
        
        mark = mark ? false : true;
	}
}

function getPeriods()
{
	var periodTypeList = document.getElementById( "periodTypeId" );
    var periodTypeId = periodTypeList.options[ periodTypeList.selectedIndex ].value;
    
    if ( periodTypeId != null )
    {       
        var url = "../dhis-web-commons-ajax/getPeriods.action?name=" + periodTypeId;
        
        var request = new Request();
        request.setResponseTypeXML( "period" );
        request.setCallbackSuccess( getPeriodsReceived );
        request.send( url );
    }
}

function getPeriodsReceived( xmlObject )
{
    var periodList = document.getElementById( "periodId" );
    
    periodList.disabled = false;
       
    clearList( periodList );
    
    var periods = xmlObject.getElementsByTagName( "period" );
    
    for ( var i = 0; i < periods.length; i++)
    {
        var id = periods[i].getElementsByTagName( "id" )[0].firstChild.nodeValue;
        var startDate = periods[i].getElementsByTagName( "startDate" )[0].firstChild.nodeValue;
        var endDate = periods[i].getElementsByTagName( "endDate" )[0].firstChild.nodeValue;
        var periodName = periods[i].getElementsByTagName( "name" )[0].firstChild.nodeValue;
        
        var option = document.createElement( "option" );
        option.value = id;
        option.text = periodName;
        periodList.add( option, null );
    }
}
