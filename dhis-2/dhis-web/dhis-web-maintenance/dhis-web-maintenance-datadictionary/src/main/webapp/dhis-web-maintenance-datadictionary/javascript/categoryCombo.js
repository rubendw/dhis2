
function showDataElementCategoryComboDetails( dataElementCategoryComboId )
{
    var request = new Request();
    request.setResponseTypeXML( 'dataElementCategoryCombo' );
    request.setCallbackSuccess( dataElementCategoryComboReceived );
    request.send( 'getDataElementCategoryCombo.action?dataElementCategoryComboId=' + dataElementCategoryComboId );
}

function dataElementCategoryComboReceived( dataElementCategoryComboElement )
{
	setFieldValue( 'idField', getElementValue( dataElementCategoryComboElement, 'id' ) );
    setFieldValue( 'nameField', getElementValue( dataElementCategoryComboElement, 'name' ) );
	setFieldValue( 'dataElementCategoryCountField', getElementValue( dataElementCategoryComboElement, 'dataElementCategoryCount' ) );
          
    showDetails();
}


function getElementValue( parentElement, childElementName )
{
    var textNode = parentElement.getElementsByTagName( childElementName )[0].firstChild;

    if ( textNode )
    {
        return textNode.nodeValue;
    }
    else
    {
        return null;
    }
}

// -----------------------------------------------------------------------------
// Delete Category
// -----------------------------------------------------------------------------

function removeDataElementCategoryCombo( dataElementCategoryComboId, dataElementCategoryComboName )
{
  var result = window.confirm( i18n_confirm_delete + '\n\n' + dataElementCategoryComboName );

  if ( result )
  {
    window.location.href = 'removeDataElementCategoryCombo.action?dataElementCategoryComboId=' + dataElementCategoryComboId;
  }
}

// ----------------------------------------------------------------------
// Validation
// ----------------------------------------------------------------------

function validateAddDataElementCategoryCombo()
{
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( addDataElementCategoryComboValidationCompleted );

  var requestString = 'validateDataElementCategoryCombo.action';  

  var selectedList = document.getElementById( 'selectedList' );
  
  var params = 'name=' + document.getElementById( 'nameField' ).value;

  for ( var i = 0; i < selectedList.options.length; ++i)
  {  	
  	params += '&selectedCategories=' + selectedList.options[i].value;
  }
  
  request.sendAsPost( params );
    
  request.send( requestString );
  
  return false;
}

function addDataElementCategoryComboValidationCompleted( messageElement )
{
  var type = messageElement.getAttribute( 'type' );
  var message = messageElement.firstChild.nodeValue;

  if ( type == 'success' )
  {           
      document.forms['addDataElementCategoryComboForm'].submit();
  }
  
  else if ( type == 'input' )
  {
    document.getElementById( 'message' ).innerHTML = message;
    document.getElementById( 'message' ).style.display = 'block';
  }
}

function validateEditDataElementCategoryCombo()
{
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( editDataElementCategoryComboValidationCompleted );
  
  var requestString = 'validateDataElementCategoryCombo.action';
    
  var selectedList = document.getElementById( 'selectedList' );
  
  var params = 'name=' + document.getElementById( 'nameField' ).value;
  	  params += '&dataElementCategoryComboId=' + document.getElementById( 'dataElementCategoryComboId' ).value;

  for ( var i = 0; i < selectedList.options.length; ++i)
  {   	
  	params += '&selectedCategories=' + selectedList.options[i].value;
  }
  
  request.sendAsPost( params );
    
  request.send( requestString );
  

  /*var requestString = 'validateDataElementCategoryCombo.action?name=' + document.getElementById( 'nameField' ).value
          + '&dataElementCategoryComboId=' + document.getElementById( 'dataElementCategoryComboId' ).value;*/

  
    
  return false;
}

  function editDataElementCategoryComboValidationCompleted( messageElement )
  {
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;

    if ( type == 'success' )
    {
        // Both edit and add form has id='dataSetForm'
        document.forms['editDataElementCategoryComboForm'].submit();
    }
    else if ( type == 'input' )
    {
      document.getElementById( 'message' ).innerHTML = message;
      document.getElementById( 'message' ).style.display = 'block';
    }
  }

  function setFieldValue( fieldId, value )
  {
      document.getElementById( fieldId ).innerHTML = value;
  }

  function showDetails()
  {
      document.getElementById( 'detailsArea' ).style.display = 'block';
  }

  function hideDetails()
  {
	document.getElementById( 'detailsArea' ).style.display = 'none';
  }
  
  function getFieldValue( fieldId )
  {
      return document.getElementById( fieldId ).value;
  }

