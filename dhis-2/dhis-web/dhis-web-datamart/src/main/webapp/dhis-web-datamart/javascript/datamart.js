/*
 * Depends on dhis-web-commons/lists/lists.js for List functionality
 */

// -----------------------------------------------------------------------------
// Data retrieval methods
// -----------------------------------------------------------------------------

function getDataElements()
{
	var dataElementGroupList = document.getElementById( "dataElementGroupId" );
	var dataElementGroupId = dataElementGroupList.options[ dataElementGroupList.selectedIndex ].value;
	
	if ( dataElementGroupId != null )
	{
		var url = "../dhis-web-commons-ajax/getDataElements.action?id=" + dataElementGroupId + "&aggregate=true";
				
		var request = new Request();
	    request.setResponseTypeXML( 'dataElement' );
	    request.setCallbackSuccess( getDataElementsReceived );
	    request.send( url );
	}
}

function getIndicators()
{
	var indicatorGroupList = document.getElementById( "indicatorGroupId" );
	var indicatorGroupId = indicatorGroupList.options[ indicatorGroupList.selectedIndex ].value;
	
	if ( indicatorGroupId != null )
	{
		var url = "../dhis-web-commons-ajax/getIndicators.action?id=" + indicatorGroupId;
		
		var request = new Request();
	    request.setResponseTypeXML( 'indicator' );
	    request.setCallbackSuccess( getIndicatorsReceived );
	    request.send( url );	    
	}
}

function getOrganisationUnits()
{
	var organisationUnitLevelList = document.getElementById( "organisationUnitLevel" );
	var organisationUnitLevel = organisationUnitLevelList.options[ organisationUnitLevelList.selectedIndex ].value;
	
	if ( organisationUnitLevel != null )
	{
		var url = "../dhis-web-commons-ajax/getOrganisationUnits.action?level=" + organisationUnitLevel;
		
		var request = new Request();
	    request.setResponseTypeXML( 'organisationUnit' );
	    request.setCallbackSuccess( getOrganisationUnitsReceived );
	    request.send( url );	    
	}
}

function getOrganisationUnitChildren()
{
	var organisationUnitList = document.getElementById( "availableOrganisationUnits" );
	var organisationUnitId = organisationUnitList.options[ organisationUnitList.selectedIndex ].value;
	
	if ( organisationUnitId != null )
	{
		var url = "../dhis-web-commons-ajax/getOrganisationUnitChildren.action?id=" + organisationUnitId;
		
		var request = new Request();
	    request.setResponseTypeXML( 'organisationUnit' );
	    request.setCallbackSuccess( getOrganisationUnitChildrenReceived );
	    request.send( url );	
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
	    request.setResponseTypeXML( 'period' );
	    request.setCallbackSuccess( getPeriodsReceived );
	    request.send( url );
	}
}

function getDataElementsReceived( xmlObject )
{	
	var availableDataElements = document.getElementById( "availableDataElements" );
	var selectedDataElements = document.getElementById( "selectedDataElements" );
	
	clearList( availableDataElements );
	
	var dataElements = xmlObject.getElementsByTagName( "dataElement" );
	
	for ( var i = 0; i < dataElements.length; i++ )
	{
		var id = dataElements[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var dataElementName = dataElements[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		if ( listContains( selectedDataElements, id ) == false )
		{				
			var option = document.createElement( "option" );
			option.value = id;
			option.text = dataElementName;
			availableDataElements.add( option, null );
		}
	}
}

function getIndicatorsReceived( xmlObject )
{	
	var availableIndicators = document.getElementById( "availableIndicators" );
	var selectedIndicators = document.getElementById( "selectedIndicators" );
	
	clearList( availableIndicators );
	
	var indicators = xmlObject.getElementsByTagName( "indicator" );
	
	for ( var i = 0; i < indicators.length; i++ )
	{
		var id = indicators[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var indicatorName = indicators[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		if ( listContains( selectedIndicators, id ) == false )
		{				
			var option = document.createElement( "option" );
			option.value = id;
			option.text = indicatorName;
			availableIndicators.add( option, null );
		}
	}
}

function getOrganisationUnitsReceived( xmlObject )
{	
	var availableOrganisationUnits = document.getElementById( "availableOrganisationUnits" );
	var selectedOrganisationUnits = document.getElementById( "selectedOrganisationUnits" );
	
	clearList( availableOrganisationUnits );
	
	var organisationUnits = xmlObject.getElementsByTagName( "organisationUnit" );
	
	for ( var i = 0; i < organisationUnits.length; i++ )
	{
		var id = organisationUnits[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var organisationUnitName = organisationUnits[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		if ( listContains( selectedOrganisationUnits, id ) == false )
		{						
			var option = document.createElement( "option" );
			option.value = id;
			option.text = organisationUnitName;
			availableOrganisationUnits.add( option, null );
		}
	}
}

function getOrganisationUnitChildrenReceived( xmlObject )
{
	var selectedOrganisationUnits = document.getElementById( "selectedOrganisationUnits" );
	
	var organisationUnits = xmlObject.getElementsByTagName( "organisationUnit" );
	
	for ( var i = 0; i < organisationUnits.length; i++ )
	{
		var id = organisationUnits[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		
		var organisationUnitName = organisationUnits[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		if ( listContains( selectedOrganisationUnits, id ) == false )
		{
			var option = document.createElement( "option" );
			option.value = id;
			option.text = organisationUnitName;
			selectedOrganisationUnits.add( option, null );
		}
	}
}

function getPeriodsReceived( xmlObject )
{	
	var availablePeriods = document.getElementById( "availablePeriods" );
	var selectedPeriods = document.getElementById( "selectedPeriods" );
	
	clearList( availablePeriods );
	
	var periods = xmlObject.getElementsByTagName( "period" );
	
	for ( var i = 0; i < periods.length; i++ )
	{
		var id = periods[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var periodName = periods[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		if ( listContains( selectedPeriods, id ) == false )
		{						
			var option = document.createElement( "option" );
			option.value = id;
			option.text = periodName;
			availablePeriods.add( option, null );
		}			
	}
}

// -----------------------------------------------------------------------------
// DataMartExport details
// -----------------------------------------------------------------------------

function showDataMartExportDetails( id )
{
    var request = new Request();
    request.setResponseTypeXML( 'dataMartExport' );
    request.setCallbackSuccess( dataMartExportReceived );
    request.send( 'getDataMartExport.action?id=' + id );
}

function dataMartExportReceived( xmlObject )
{
    setFieldValue( "nameField", getElementValue( xmlObject, "name" ) );
    setFieldValue( "dataElementField", getElementValue( xmlObject, "dataElements" ) );
    setFieldValue( "indicatorField", getElementValue( xmlObject, "indicators" ) );
    setFieldValue( "organisationUnitField", getElementValue( xmlObject, "organisationUnits" ) );
    setFieldValue( "periodField", getElementValue( xmlObject, "periods" ) );
    
    showDetails();
}

// -----------------------------------------------------------------------------
// DatmartExport
// -----------------------------------------------------------------------------

function saveExport()
{
    if ( validateCollections() )
    {
        var exportId = document.getElementById( "exportId" ).value;
        var exportName = document.getElementById( "exportName" ).value;
        
        var url = "validateDataMartExport.action?id=" + exportId + "&name=" + exportName;
        
        var request = new Request();
        request.setResponseTypeXML( 'message' );
        request.setCallbackSuccess( saveExportReceived );
        request.send( url );
    }
}

function saveExportReceived( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == "input" )
    {
        setMessage( message );
    }
    else if ( type == "success" )
    {
        var params = getExportParams();

        var url = "saveDataMartExport.action";
            
        var request = new Request();
        request.sendAsPost( params );
        request.setCallbackSuccess( saveReceived );    
        request.send( url );
    }
}

function saveReceived( messageElement )
{
    setMessage( i18n_export_saved );
}

// -----------------------------------------------------------------------------
// Export
// -----------------------------------------------------------------------------

function exportValues()
{
    if ( validateCollections() )
    {
        var params = getExportParams();
        
        var url = "export.action";
		
    	var request = new Request();
    	request.sendAsPost( params );
        request.setCallbackSuccess( exportReceived );    
        request.send( url );
    }
}

function exportReceived( messageElement )
{
	waitAndGetExportStatus( 500 );
}

function getExportStatus()
{
	var url = "getExportStatus.action";
	
	var request = new Request();
    request.setCallbackSuccess( exportStatusReceived );    
    request.send( url );
}

function exportStatusReceived( messageElement )
{
	setMessage( messageElement );
	
	waitAndGetExportStatus( 2000 );
}

function waitAndGetExportStatus( millis )
{
	setTimeout( "getExportStatus();", millis );
}

function cancelExport()
{
	var url = "cancelExport.action";
		
	var request = new Request();  
    request.send( url );
}

function removeDatamartExport( exportId, exportName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + exportName );
    
    if ( result )
    {
        window.location.href = 'removeDataMartExport.action?id=' + exportId;
    }
}

function getExportParams()
{
    var exportId = document.getElementById( "exportId" ).value;
    var exportName = document.getElementById( "exportName" ).value;
    var dataElements = document.getElementById( "selectedDataElements" );
    var indicators = document.getElementById( "selectedIndicators" );
    var organisationUnits = document.getElementById( "selectedOrganisationUnits" );
    var periods = document.getElementById( "selectedPeriods" );
    
    var params = "id=" + exportId + "&name=" + exportName;
    
    for ( var i = 0; i < dataElements.options.length; i++ )
    {
        params += "&selectedDataElements=" + dataElements.options[i].value;
    }
    
    for ( var i = 0; i < indicators.options.length; i++ )
    {
        params += "&selectedIndicators=" + indicators.options[i].value;
    }
        
    for ( var i = 0; i < organisationUnits.options.length; i++ )
    {
        params += "&selectedOrganisationUnits=" + organisationUnits.options[i].value;
    }
    
    for ( var i = 0; i < periods.options.length; i++ )
    {
        params += "&selectedPeriods=" + periods.options[i].value;
    }
    
    return params;
}

// -----------------------------------------------------------------------------
// Validation
// -----------------------------------------------------------------------------

function validateCollections()
{
    if ( !hasElements( "selectedDataElements" ) && !hasElements( "selectedIndicators" ) )
    {
        setMessage( i18n_must_select_at_least_one_dataelement_or_indicator );
        
        return false;
    }
    
    if ( !hasElements( "selectedOrganisationUnits" ) )
    {
        setMessage( i18n_must_select_at_least_one_organisation_unit );
        
        return false;
    }
    
    if ( !hasElements( "selectedPeriods" ) )
    {
        setMessage( i18n_must_select_at_least_one_period );
        
        return false;
    }
    
    return true;
}
