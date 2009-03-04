
function getDataElementByDataSet( dataSetId )
{
  var request = new Request();

  var requestString = 'filterAvailableDataElementsByDataSet.action';
  
  var params = 'dataSetId=' + dataSetId;

  var selectedList = document.getElementById( 'selectedList' );

  for ( var i = 0; i < selectedList.options.length; ++i)
  {
	params += '&selectedDataElements=' + selectedList.options[i].value;
	// process list.options[i].value / list.options[i].text
  }
   // Clear the list
  var availableList = document.getElementById( 'availableList' );
  availableList.options.length = 0;

  /**
  for ( var i = availableList.options.length; i > 0; i-- )
  {
    availableList.options[i-1] = null;
  }
  */

  request.setResponseTypeXML( 'dataElementGroup' );
  request.setCallbackSuccess( filterByDataElementGroupCompleted );
  request.sendAsPost( params );
  request.send( requestString );
}



function addSectionValidationCompleted( messageElement )
{
  var type = messageElement.getAttribute( 'type' );
  var message = messageElement.firstChild.nodeValue;

  if ( type == 'success' )
  {
      // Both edit and add form has id='dataSetForm'      
      document.forms['addSectionForm'].submit();
  }
  /**
  else if ( type == 'error' )
  {
      window.alert( 'Adding the organisation unit failed with the following message:\n' + message );
  }
  */
  else if ( type == 'input' )
  {
    document.getElementById( 'message' ).innerHTML = message;
    document.getElementById( 'message' ).style.display = 'block';
  }
}

function validateAddSection()
{
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( addSectionValidationCompleted );

  var requestString = 'validateSection.action?name=' + document.getElementById( 'sectionName' ).value + '&label=' + document.getElementById( 'sectionLabel' ).value;
  
  request.send( requestString );
    /**
    request.send( 'validateOrganisationUnit.action?name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&organisationUnitCode=' + getFieldValue( 'organisationUnitCode' ) +
        '&openingDate=' + getFieldValue( 'openingDate' ) );
        */

  return false;
}