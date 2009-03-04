
// -----------------------------------------------------------------------------
// Export
// -----------------------------------------------------------------------------

function submitDataValueExportForm()
{
	selectAll( document.getElementById( "selectedDataSets" ) );
	
	if ( validate() )
	{
	   document.getElementById( "exportForm" ).submit();
	}
}

// -----------------------------------------------------------------------------
// Toggle
// -----------------------------------------------------------------------------

function toggle( knob )
{
	var toggle = false;
	
	if ( knob == "all" )
	{
		toggle = true;
	}
	
	document.getElementById( "dataElements" ).checked = toggle;
	document.getElementById( "dataElementGroups" ).checked = toggle;
	document.getElementById( "dataSets" ).checked = toggle;
	document.getElementById( "indicators" ).checked = toggle;
	document.getElementById( "indicatorGroups" ).checked = toggle;
	document.getElementById( "dataDictionaries" ).checked = toggle;
	document.getElementById( "organisationUnits" ).checked = toggle;
	document.getElementById( "organisationUnitGroups" ).checked = toggle;
	document.getElementById( "organisationUnitGroupSets" ).checked = toggle;
	document.getElementById( "validationRules" ).checked = toggle;	
    document.getElementById( "reportTables" ).checked = toggle;   
}

// -----------------------------------------------------------------------------
// Validation
// -----------------------------------------------------------------------------

function validate()
{    
    if ( selectedOrganisationUnitIds == null || selectedOrganisationUnitIds.length == 0 )
    {
        showMessage( i18n_select_organisation_unit );
        return false;
    }
    if ( !hasText( "startDate" ) )
    {
        showMessage( i18n_select_startdate );
        return false;
    }
    if ( !hasText( "endDate" ) )
    {
        showMessage( i18n_select_enddate );
        return false;
    }
    if ( !hasElements( "selectedDataSets" ) )
    {
        showMessage( i18n_select_datasets );
        return false;
    }
    
    return true;
}

var selectedOrganisationUnitIds = null;

function setSelectedOrganisationUnitIds( ids )
{
    selectedOrganisationUnitIds = ids;
}

selectionTreeSelection.setListenerFunction( setSelectedOrganisationUnitIds );

function hasText( inputId )
{
    return document.getElementById( inputId ).value != "";
}

function hasElements( listId )
{
    return document.getElementById( listId ).options.length > 0;
}

function showMessage( message )
{
    document.getElementById( 'message' ).innerHTML = message;    
    document.getElementById( 'message' ).style.display = 'block';
}

function hideMessage()
{
    document.getElementById( 'message' ).style.display = 'none';
}
