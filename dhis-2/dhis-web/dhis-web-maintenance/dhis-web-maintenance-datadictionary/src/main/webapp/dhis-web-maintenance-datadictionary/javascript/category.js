
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showDataElementCategoryDetails( dataElementCategoryId )
{	
    var request = new Request();
    request.setResponseTypeXML( 'dataElementCategory' );
    request.setCallbackSuccess( dataElementCategoryReceived );
    request.send( 'getDataElementCategory.action?dataElementCategoryId=' + dataElementCategoryId );
}

function dataElementCategoryReceived( dataElementCategoryElement )
{
	setFieldValue( 'idField', getElementValue( dataElementCategoryElement, 'id' ) );
    setFieldValue( 'nameField', getElementValue( dataElementCategoryElement, 'name' ) );    
    setFieldValue( 'categoryOptionsCountField', getElementValue( dataElementCategoryElement, 'categoryOptionCount' ) );
          
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

var tmpDataElementCategoryId;

function removeDataElementCategory( dataElementCategoryId, dataElementCategoryName )
{
  var result = window.confirm( i18n_confirm_delete + '\n\n' + dataElementCategoryName );

  if ( result )
  {
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( removeDataElementCategoryCompleted );
    request.send( 'removeDataElementCategory.action?id=' + dataElementCategoryId );
  }
}

function removeDataElementCategoryCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        window.location.href = 'category.action';
    }
    else if ( type = 'error' )
    {
        setFieldValue( 'warningField', message );
        
        showWarning();
    }
}

// ----------------------------------------------------------------------
// Validation
// ----------------------------------------------------------------------

function validateAddDataElementCategory()
{
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( addDataElementCategoryValidationCompleted );

  var requestString = 'validateDataElementCategory.action?name=' + document.getElementById( 'nameField' ).value;

  request.send( requestString );
  
  return false;
}

function addDataElementCategoryValidationCompleted( messageElement )
{
  var type = messageElement.getAttribute( 'type' );
  var message = messageElement.firstChild.nodeValue;

  if ( type == 'success' )
  {           
      document.forms['addDataElementCategoryForm'].submit();
  }
  
  else if ( type == 'input' )
  {
    document.getElementById( 'message' ).innerHTML = message;
    document.getElementById( 'message' ).style.display = 'block';
  }
}

function validateEditDataElementCategory()
{
  var request = new Request();
  request.setResponseTypeXML( 'message' );
  request.setCallbackSuccess( editDataElementCategoryValidationCompleted );

  var requestString = 'validateDataElementCategory.action?name=' + document.getElementById( 'nameField' ).value
          + '&dataElementCategoryId=' + document.getElementById( 'dataElementCategoryId' ).value;

  request.send( requestString );
    
  return false;
}

  function editDataElementCategoryValidationCompleted( messageElement )
  {
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;

    if ( type == 'success' )
    {
        // Both edit and add form has id='dataSetForm'
        document.forms['editDataElementCategoryForm'].submit();
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

