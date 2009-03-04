
function getOUDeatilsForAA(orgUnitIds)
{
	var url = "getOrgUnitDetails.action?orgUnitId=" + orgUnitIds;
	
	var request = new Request();
	request.setResponseTypeXML( 'orgunit' );
	request.setCallbackSuccess( getOUDetailsForAARecevied );
	request.send( url );	    
}

function getOUDetailsForAARecevied(xmlObject)
{
	var orgUnits = xmlObject.getElementsByTagName("orgunit");

    for ( var i = 0; i < orgUnits.length; i++ )
    {
        var id = orgUnits[ i ].getElementsByTagName("id")[0].firstChild.nodeValue;
        var orgUnitName = orgUnits[ i ].getElementsByTagName("name")[0].firstChild.nodeValue;
		
		document.ChartGenerationForm.ouIDTB.value = id;
		document.ChartGenerationForm.ouNameTB.value = orgUnitName;
    }    		
}


function getOUDeatilsForGA(orgUnitIds)
{
	var url = "getOrgUnitDetails.action?orgUnitId=" + orgUnitIds;
	
	var request = new Request();
	request.setResponseTypeXML( 'orgunit' );
	request.setCallbackSuccess( getOUDetailsForGARecevied );
	request.send( url );	    
}

function getOUDetailsForGARecevied(xmlObject)
{
	var categoryIndex = document.ChartGenerationForm.categoryLB.selectedIndex;
	var facilityIndex =  document.ChartGenerationForm.facilityLB.selectedIndex;
    var index = 0;		
	var i=0;
		
	var orgUnits = xmlObject.getElementsByTagName("orgunit");

    for ( var i = 0; i < orgUnits.length; i++ )
    {
        var id = orgUnits[ i ].getElementsByTagName("id")[0].firstChild.nodeValue;
        var orgUnitName = orgUnits[ i ].getElementsByTagName("name")[0].firstChild.nodeValue;

    	if(document.ChartGenerationForm.categoryLB.options[categoryIndex].value == "period" || document.ChartGenerationForm.facilityLB.options[facilityIndex].value == "children")
    	{
        	index = document.ChartGenerationForm.orgUnitListCB.options.length;                			
        	for(i=0;i<index;i++)
	    	{
		        document.ChartGenerationForm.orgUnitListCB.options[0] = null;	
        	}
        	document.ChartGenerationForm.orgUnitListCB.options[0] = new Option(orgUnitName,id,false,false);
    	}
    	else
    	{
        	index = document.ChartGenerationForm.orgUnitListCB.options.length;		
    	    for(i=0;i<index;i++)
    	    {
        	    if(id == document.ChartGenerationForm.orgUnitListCB.options[i].value) return; 
    	    }
    		document.ChartGenerationForm.orgUnitListCB.options[index] = new Option(orgUnitName,id,false,false);
    	}    
    }	
    		
}


function getOUDetails(orgUnitIds)
{
	var url = "getOrgUnitDetails.action?orgUnitId=" + orgUnitIds;
	
	var request = new Request();
	request.setResponseTypeXML( 'orgunit' );
	request.setCallbackSuccess( getOUDetailsRecevied );
	request.send( url );	    

}

function getOUDetailsRecevied(xmlObject)
{

	var categoryIndex = document.ChartGenerationForm.categoryLB.selectedIndex;
	var facilityIndex =  document.ChartGenerationForm.facilityLB.selectedIndex;
    var index = 0;		
	var i=0;
		
	var orgUnits = xmlObject.getElementsByTagName("orgunit");

    for ( var i = 0; i < orgUnits.length; i++ )
    {
        var id = orgUnits[ i ].getElementsByTagName("id")[0].firstChild.nodeValue;
        var orgUnitName = orgUnits[ i ].getElementsByTagName("name")[0].firstChild.nodeValue;

    	if(document.ChartGenerationForm.categoryLB.options[categoryIndex].value == "period" || document.ChartGenerationForm.facilityLB.options[facilityIndex].value == "children")
    	{
        	index = document.ChartGenerationForm.orgUnitListCB.options.length;                			
        	for(i=0;i<index;i++)
	    	{
	        	document.ChartGenerationForm.orgUnitListCB.options[0] = null;	
        	}
        	document.ChartGenerationForm.orgUnitListCB.options[0] = new Option(orgUnitName,id,false,false);
    	}
    	else
    	{
        	index = document.ChartGenerationForm.orgUnitListCB.options.length;		
    	    for(i=0;i<index;i++)
    	    {
        		if(id == document.ChartGenerationForm.orgUnitListCB.options[i].value) return; 
    	    }
    		document.ChartGenerationForm.orgUnitListCB.options[index] = new Option(orgUnitName,id,false,false);
    	}    
    }	
    		
}


//Depends on dhis-web-commons/lists/lists.js for List functionality

function getOrgUnitGroups()
{
	var orgUnitGroupSetList = document.getElementById("orgUnitGroupSetListCB");
	var orgUnitGroupSetId = orgUnitGroupSetList.options[ orgUnitGroupSetList.selectedIndex ].value;
	
	if ( orgUnitGroupSetId != null )
	{
		var url = "getOrgUnitGroups.action?orgUnitGroupSetId=" + orgUnitGroupSetId;
		
		var request = new Request();
	    request.setResponseTypeXML( 'orgunitgroup' );
	    request.setCallbackSuccess( getOrgUnitGroupsReceived );
	    request.send( url );	    
	}	
}
function groupChangeFunction(evt)
{
	document.ChartGenerationForm.selectedGroup.value = true;
	document.ChartGenerationForm.orgUnitListCB.value = null;
} //groupChangeFunction end

function getOrgUnitGroupsReceived(xmlObject)
{
	var orgUnitGroupList = document.getElementById("orgUnitListCB");
	clearList(orgUnitGroupList);
	
	var orgUnitGroups = xmlObject.getElementsByTagName("orgunitgroup");

    for ( var i = 0; i < orgUnitGroups.length; i++ )
    {
        var id = orgUnitGroups[ i ].getElementsByTagName("id")[0].firstChild.nodeValue;
        var orgUnitGroupName = orgUnitGroups[ i ].getElementsByTagName("name")[0].firstChild.nodeValue;

        var option = document.createElement("option");
        option.value = id;
        option.text = orgUnitGroupName;
        option.title = orgUnitGroupName;
        orgUnitGroupList.add(option, null);
    }	
}

//--------------------------------------
//
//--------------------------------------
function getDataElements()
{
    var dataElementGroupList = document.getElementById("dataElementGroupId");
    var dataElementGroupId = dataElementGroupList.options[ dataElementGroupList.selectedIndex ].value;
    
    var deSelectionList = document.getElementById("deSelection");    
    var deOptionValue = deSelectionList.options[ deSelectionList.selectedIndex ].value;
    
    if ( dataElementGroupId != null )
    {
        var url = "getDataElements.action?id=" + dataElementGroupId + "&deOptionValue=" + deOptionValue;
        var request = new Request();
        request.setResponseTypeXML('dataElement');
        request.setCallbackSuccess(getDataElementsReceived);
        request.send(url);
    }
}// getDataElements end           

//Depends on dhis-web-commons/lists/lists.js for List functionality
function getDataElementsWithOutOptionCombo()
{
    var dataElementGroupList = document.getElementById("dataElementGroupId");
    var dataElementGroupId = dataElementGroupList.options[ dataElementGroupList.selectedIndex ].value;
    
    var deSelectionList = document.getElementById("deSelection");   
    
    if ( dataElementGroupId != null )
    {
        var url = "getDataElements.action?id=" + dataElementGroupId;
        var request = new Request();
        request.setResponseTypeXML('dataElement');
        request.setCallbackSuccess(getDataElementsReceived);
        request.send(url);
    }
}// getDataElements end          


function getDataElementsReceived( xmlObject )
{
    var availableDataElements = document.getElementById("availableDataElements");
    var selectedDataElements = document.getElementById("selectedDataElements");

    clearList(availableDataElements);

    var dataElements = xmlObject.getElementsByTagName("dataElement");

    for ( var i = 0; i < dataElements.length; i++ )
    {
        var id = dataElements[ i ].getElementsByTagName("id")[0].firstChild.nodeValue;
        var dataElementName = dataElements[ i ].getElementsByTagName("name")[0].firstChild.nodeValue;
        if ( listContains(selectedDataElements, id) == false )
        {
            var option = document.createElement("option");
            option.value = id;
            option.text = dataElementName;
            option.title = dataElementName;
            availableDataElements.add(option, null);
        }
    }
    
    // If the list of available dataelements is empty, an empty placeholder will be added
    addOptionPlaceHolder( availableDataElements );
}// getDataElementsReceived end


function getIndicators()
{
	var indicatorGroupList = document.getElementById( "indicatorGroupId" );
	var indicatorGroupId = indicatorGroupList.options[ indicatorGroupList.selectedIndex ].value;
	
	if ( indicatorGroupId != null )
	{
		var url = "getIndicators.action?id=" + indicatorGroupId;
		
		var request = new Request();
	    request.setResponseTypeXML( 'indicator' );
	    request.setCallbackSuccess( getIndicatorsReceived );
	    request.send( url );	    
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
	
	// If the list of available indicators is empty, an empty placeholder will be added
	addOptionPlaceHolder( availableIndicators );
}