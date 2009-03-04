
// -----------------------------------------------------------------------------
// Report params
// -----------------------------------------------------------------------------

function getOrganisationUnitsParent()
{
    var organisationUnitLevelList = document.getElementById( "organisationUnitLevelParent" );
    var organisationUnitLevel = organisationUnitLevelList.options[ organisationUnitLevelList.selectedIndex ].value;
    
    if ( organisationUnitLevel != null )
    {
        var url = "../dhis-web-commons-ajax/getOrganisationUnits.action?level=" + organisationUnitLevel;
        
        var request = new Request();
        request.setResponseTypeXML( 'organisationUnit' );
        request.setCallbackSuccess( getOrganisationUnitsParentReceived );
        request.send( url );        
    }
}

function getOrganisationUnitsParentReceived( xmlObject )
{   
    var availableOrganisationUnits = document.getElementById( "parentOrganisationUnitId" );
    
    clearList( availableOrganisationUnits );
    
    var organisationUnits = xmlObject.getElementsByTagName( "organisationUnit" );
    
    for ( var i = 0; i < organisationUnits.length; i++ )
    {
        var id = organisationUnits[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
        var organisationUnitName = organisationUnits[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
        
        var option = document.createElement( "option" );
        option.value = id;
        option.text = organisationUnitName;
        availableOrganisationUnits.add( option, null );
    }
}

function getOrganisationUnitsSingle()
{
    var organisationUnitLevelList = document.getElementById( "organisationUnitLevelSingle" );
    var organisationUnitLevel = organisationUnitLevelList.options[ organisationUnitLevelList.selectedIndex ].value;
    
    if ( organisationUnitLevel != null )
    {
        var url = "../dhis-web-commons-ajax/getOrganisationUnits.action?level=" + organisationUnitLevel;
        
        var request = new Request();
        request.setResponseTypeXML( 'organisationUnit' );
        request.setCallbackSuccess( getOrganisationUnitsSingleReceived );
        request.send( url );        
    }
}

function getOrganisationUnitsSingleReceived( xmlObject )
{   
    var availableOrganisationUnits = document.getElementById( "organisationUnitId" );
    
    clearList( availableOrganisationUnits );
    
    var organisationUnits = xmlObject.getElementsByTagName( "organisationUnit" );
    
    for ( var i = 0; i < organisationUnits.length; i++ )
    {
        var id = organisationUnits[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
        var organisationUnitName = organisationUnits[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
        
        var option = document.createElement( "option" );
        option.value = id;
        option.text = organisationUnitName;
        availableOrganisationUnits.add( option, null );
    }
}
