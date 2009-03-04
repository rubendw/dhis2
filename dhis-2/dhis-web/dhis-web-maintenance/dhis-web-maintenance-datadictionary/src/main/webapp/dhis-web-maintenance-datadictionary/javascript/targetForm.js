
function validateAddTarget()
{
	var request = new Request();
	request.setResponseTypeXML( 'message' );
	request.setCallbackSuccess( addTargetValidationCompleted );
	
	request.send( 'validateTarget.action?name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&value=' + getFieldValue( 'value' ) +
        '&periodId=' + getFieldValue( 'periodId' ) +
        '&indicatorId=' + getFieldValue( 'indicatorId' )
    );
  
  	return false;
}

function addTargetValidationCompleted( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
  	var message = messageElement.firstChild.nodeValue;
  	
  	if ( type == 'success' )
  	{           
      	document.forms['addTargetForm'].submit();
  	}
  
  	else if ( type == 'input' )
  	{
    	document.getElementById( 'message' ).innerHTML = message;
    	document.getElementById( 'message' ).style.display = 'block';
  	}
}

function validateUpdateTarget()
{
  	var request = new Request();
  	request.setResponseTypeXML( 'message' );
  	request.setCallbackSuccess( updateTargetValidationCompleted );
  	
  	request.send( 'validateTarget.action?name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&value=' + getFieldValue( 'value' ) +
        '&periodId=' + getFieldValue( 'periodId' )+
        '&indicatorId=' + getFieldValue( 'indicatorId' ) + 
        '&targetId=' + getFieldValue( 'targetId' )
    );
    
  	return false;
}

function updateTargetValidationCompleted( messageElement )
{
	var type = messageElement.getAttribute( 'type' );
  	var message = messageElement.firstChild.nodeValue;
  
  	if ( type == 'success' )
  	{
      	document.forms['updateTargetForm'].submit();
  	}
  	else if ( type == 'input' )
  	{
    	document.getElementById( 'message' ).innerHTML = message;
    	document.getElementById( 'message' ).style.display = 'block';
  	}
}


// ----------------------------------------------------------------------
// Get Periods
// ----------------------------------------------------------------------

function getPeriods( periodTypeName )
{
  	var request = new Request();

  	var requestString = 'getPeriods.action';

  	var params = 'periodTypeName=' + periodTypeName;

  	request.setResponseTypeXML( 'periods' );
  	request.setCallbackSuccess( getPeriodsCompleted );
  	request.sendAsPost( params );
  	request.send( requestString );
}


function getPeriodsCompleted( periodSet )
{
  	var periods = periodSet.getElementsByTagName( 'periods' )[0];
  	var periodList = periods.getElementsByTagName( 'period' );

  	var selectList = document.getElementById( 'periodId' );
  	selectList.length = 0;

  	for ( var i = 0; i < periodList.length; i++ )
  	{
    	var period = periodList[i];
    	var name = period.firstChild.nodeValue;
    	var id = period.getAttribute( 'id' );

    	selectList.add( new Option( name, id ), null );
  	}

  	document.getElementById('periodList').style.visibility = "visible";  
}

function nameChanged()
{
  	var name = document.getElementById( 'name' );
  	var shortName = document.getElementById( 'shortName' );
  
  	shortName.value = name.value;
}

/**
 * Get the value of the specified field
 * 
 * @param fieldId id of the field to get the value for
 * @return String value of the specified field,
 * @throws Exception if the given field doesn't exist.
 */
function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
}

/**
 * Returns the first value of the specified select box
 * 
 * @param selectId
 * @return The first (or only) String value of the given select box, 
 * 		or null if no options are selected
 */
function getSelectedValue( selectId )
{
	var select = document.getElementById( selectId );
	var option = select.options[ select.selectedIndex ];
	
	if ( option )
	{
		return option.value;
	}
	else
	{
		return null;
	}
}
