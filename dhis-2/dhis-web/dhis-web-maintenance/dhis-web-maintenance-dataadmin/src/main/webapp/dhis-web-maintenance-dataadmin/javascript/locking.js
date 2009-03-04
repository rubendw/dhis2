
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

function getPeriodsReceived( xmlObject )
{	
	document.getElementById( "periodId" ).disabled = false;
	
	var periodList = document.getElementById( "periodId" );
	
	var periods = xmlObject.getElementsByTagName( "period" );
	
	periodList.options.length = 1;
	
	for ( var i = 0; i < periods.length; i++)
	{
		var id = periods[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var periodName = periods[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		var option = document.createElement( "option" );
		option.value = id;
		option.text = periodName;
		
		periodList.add( option, null );
	}
}

function getDataSets()
{
	var periodList = document.getElementById( "periodId" );
	var periodId = periodList.options[ periodList.selectedIndex ].value;
	
	if ( periodId != null )
	{
		var url = "getDataSets.action?periodId=" + periodId;
		
		var request = new Request();
		request.setResponseTypeXML( 'dataSet' );
		request.setCallbackSuccess( getDataSetsReceived );
		request.send( url );
	}
}

function getDataSetsReceived( xmlObject )
{
	var unlockedDataSetList = document.getElementById( "unlockedDataSets" );
	var lockedDataSetList = document.getElementById( "lockedDataSets" );
	
	unlockedDataSetList.disabled = false;
	lockedDataSetList.disabled = false;
	
	clearList( unlockedDataSetList );
	clearList( lockedDataSetList );
	
	var dataSets = xmlObject.getElementsByTagName( "dataSet" );
	
	for ( var i = 0; i < dataSets.length; i++ )
	{
		var id = dataSets[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var dataSetName = dataSets[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		var locked = dataSets[ i ].getElementsByTagName( "locked" )[0].firstChild.nodeValue;
		
		var option = document.createElement( "option" );
		option.value = id;
		option.text = dataSetName;
		
		if ( locked == "true" )
		{
			lockedDataSetList.add( option, null );
		}
		else
		{
			unlockedDataSetList.add( option, null );
		}
	}
}

function updateDataSets()
{
	selectAllById( "lockedDataSets" );
	selectAllById( "unlockedDataSets" );
	
	document.getElementById( "lockingForm" ).submit();
}
