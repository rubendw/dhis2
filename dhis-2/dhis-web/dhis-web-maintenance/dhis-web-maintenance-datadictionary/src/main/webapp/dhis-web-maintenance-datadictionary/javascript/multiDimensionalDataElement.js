
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showDataElementDetails( dataElementId )
{
    var request = new Request();
    request.setResponseTypeXML( 'dataElement' );
    request.setCallbackSuccess( dataElementReceived );
    request.send( 'getMultiDimensionalDataElement.action?id=' + dataElementId );
}

function dataElementReceived( dataElementElement )
{
    setFieldValue( 'nameField', getElementValue( dataElementElement, 'name' ) );
    
    setFieldValue( 'shortNameField', getElementValue( dataElementElement, 'shortName' ) );

    var alternativeName = getElementValue( dataElementElement, 'alternativeName' );
    setFieldValue( 'alternativeNameField', alternativeName ? alternativeName : '[' + i18n_none + ']' );
    
    var code = getElementValue( dataElementElement, 'code' );
    setFieldValue( 'codeField', code ? code : '[' + i18n_none + ']' );

    var description = getElementValue( dataElementElement, 'description' );
    setFieldValue( 'descriptionField', description ? description : '[' + i18n_none + ']' );

    var active = getElementValue( dataElementElement, 'active' );
    setFieldValue( 'activeField', active == 'true' ? i18n_yes : i18n_no );
    
    var typeMap = { 'int':i18n_number, 'bool':i18n_yes_no, 'string':i18n_text };
    var type = getElementValue( dataElementElement, 'type' );
    setFieldValue( 'typeField', typeMap[type] );
    
    var aggregationOperator = getElementValue( dataElementElement, 'aggregationOperator' );
    var aggregationOperatorText = i18n_none;
    if ( aggregationOperator == 'sum' )
    {
        aggregationOperatorText = i18n_sum;
    }
    else if ( aggregationOperator == 'average' )
    {
        aggregationOperatorText = i18n_average;
    }
    setFieldValue( 'aggregationOperatorField', aggregationOperatorText );
    
    var selectedCategoryComboId = getElementValue( dataElementElement, 'selectedCategoryComboId' );
    setFieldValue( 'selectedCategoryComboIdField', selectedCategoryComboId );
    
    showDetails();
}

function getDataElements( dataElementGroupId, type )
{
	
    var url = "getMultiDimensionalDataElements.action?";

    if ( dataElementGroupId == '[select]' )
    {
    	return;
    }

	if ( dataElementGroupId != null )
	{
		url += "dataElementGroupId=" + dataElementGroupId;				
	}
	
	if ( type != null )
	{
		url += "&type=" + type
	}
	
	alert(url);
	
    var request = new Request();
    request.setResponseTypeXML( 'dataElement' );
    request.setCallbackSuccess( getDataElementsReceived );
    request.send( url );
	
}

function getDataElementsReceived( xmlObject )
{	
	var availableDataElements = document.getElementById( "availableDataElements" );
	//var selectedDataElements = document.getElementById( "selectedDataElements" );
	
	clearList( availableDataElements );
	
	var dataElements = xmlObject.getElementsByTagName( "dataElement" );
	
	for ( var i = 0; i < dataElements.length; i++ )
	{
		var id = dataElements[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
		var dataElementName = dataElements[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
		
		//if ( listContains( selectedDataElements, id ) == false )
		//{				
			var option = document.createElement( "option" );
			option.value = id;
			option.text = dataElementName;
			option.title = dataElementName;
			availableDataElements.add( option, null );
		//}
	}
	
	// If the list of available dataelements is empty, an empty placeholder will be added
	addOptionPlaceHolder( availableDataElements );
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
// -----------------------------------------------------------------------------
// Remove data element
// -----------------------------------------------------------------------------

function removeDataElement( dataElementId, dataElementName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + dataElementName );
    
    if ( result )
    {
        window.location.href = 'removeMultiDimensionalDataElement.action?id=' + dataElementId;
    }
}

// -----------------------------------------------------------------------------
// Add data element
// -----------------------------------------------------------------------------

function validateAddDataElement()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    
    request.send( 'validateMultiDimensionalDataElement.action?name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&alternativeName=' + getFieldValue( 'alternativeName' ) +
        '&code=' + getFieldValue( 'code' ) +
        '&type=' + getSelectValue( 'type' ) +
        '&calculated=' + getCheckboxValue( 'calculated' ) +
        '&selectedCategoryComboId=' + getSelectValue( 'selectedCategoryComboId' ) +
        makeValueString('dataElementIds', getInputValuesByParentId('selectedDataElements', 'dataElementIds'))
    );

    return false;
}

/**
 * Make a CGI parameter string for the given field name and set of values
 * 
 * @param fieldName name of the field to make a string for
 * @param values array of values to add to the string
 * @returns String on the form '&fieldName=value1...$fieldName=valueN'
 */
function makeValueString( fieldName, values )
{
	var valueStr = "";
	for ( var i = 0, value; value = values[i]; i++ )
	{
		valueStr += "&" + fieldName + "=" + value;
	}
	
	return valueStr;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addMultiDimensionalDataElementForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_data_element_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
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
function getSelectValue( selectId )
{
	var select = document.getElementById( selectId );
	var option = select.options[select.selectedIndex];
	
	if ( option )
	{
		return option.value;
	}
	else
	{
		return null;
	}
}

/**
 * Returns the values for the specified select box
 * 
 * @param id id of the select box to get values for
 * @return Array of String values from the given select box,
 * 		or an empty array if no options are selected
 */
function getSelectValues( selectId )
{
	var select = document.getElementById( selectId );
	var values = [];
	for ( var i = 0, option; option = select.options[i]; i++ )
	{
		if ( option.selected )
		{
			values.push(option.value);
		}
	}
	
	return values;
}

/**
 * Returns the value for the specified checkbox
 * 
 * @param checkboxId id of the checkbox to get a value for
 * @return String value for the specified checkbox,
 * 		or null if the checkbox is not checked
 */
function getCheckboxValue( checkboxId )
{
	var checkbox = document.getElementById( checkboxId );
	
	return ( checkbox.checked ? checkbox.value : null );
}

/**
 * Returns the values for a set of inputs with the same name,
 * under a specified parent node.
 * 
 * @param parentId id of the parent node to limit the search to
 * @param fieldName form name of the inputs to get values for
 * @return Array with the String values for the specified inputs,
 * 		or an empty Array if no inputs with that name exist under the specified parent node
 */
function getInputValuesByParentId( parentId, fieldName )
{
	var node = document.getElementById(parentId);
	
	if ( ! node )
	{
		return [];
	}
	
	var inputs = node.getElementsByTagName("input");
	values = [];
	
	for ( var i = 0, input; input = inputs[i]; i++ )
	{
		if ( input.name == fieldName )
		{
			values.push(input.value);
		}
	}
	
	return values;
	
}

// -----------------------------------------------------------------------------
// Update data element
// -----------------------------------------------------------------------------

function validateUpdateDataElement()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateMultiDimensionalDataElement.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&alternativeName=' + getFieldValue( 'alternativeName' ) +
        '&selectedCategoryComboId=' + getSelectValue( 'selectedCategoryComboId' ) +
        '&code=' + getFieldValue( 'code' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateMultiDimensionalDataElementForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_data_element_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}

/**
 * Adds a set of data elements to the CDE table.
 * Either add the selected ones, or add all.
 * @param requireSelect Whether to only add the selected data elements
 */
function addCDEDataElements( requireSelected )
{
    var available = byId('availableDataElements');
    var option;

    removeTablePlaceholder();

    for ( var i = available.options.length - 1; i >= 0; i-- )
    {
    	option = available.options[i];

        if ( requireSelected && ! option.selected )
        {
            continue;
        }
        
        available.remove(i);
        addCDEDataElement( option.value, option.firstChild.nodeValue );
    }
}

/**
 * Add a single data element to the CDE table.
 */
function addCDEDataElement( dataElementId, dataElementName )
{		
	//Add data to the table of selected data elements
    var tr = document.createElement('tr');

    var nameTd = tr.appendChild(document.createElement('td'));
    nameTd.appendChild(document.createTextNode(dataElementName));
    var idInput = nameTd.appendChild(document.createElement('input'));
    idInput.type = 'hidden';
    idInput.name = 'dataElementIds';
    idInput.value = dataElementId;

    var factorTd = tr.appendChild(document.createElement('td'));
    var factorInput = factorTd.appendChild(document.createElement('input'));
    factorInput.type = 'text';
    factorInput.name = 'factors';
    factorInput.value = 1;

    var opTd = tr.appendChild(document.createElement('td'));
    var button = opTd.appendChild(document.createElement('button'));
    // i18n?
    button.setAttribute('title', 'Remove from list');
    button.onclick = removeCDEDataElement;
    var delIcon = button.appendChild(document.createElement('img'));
    delIcon.setAttribute( 'src', '../images/delete.png' );
    delIcon.setAttribute( 'alt', 'Delete icon' );
    
    var selectedTable = byId('selectedDataElements');
    selectedTable.appendChild(tr);
}

/**
 * Remove one data element row from the CDE form.
 */
function removeCDEDataElement( e )
{
    var tr = this.parentNode.parentNode;

    //Add data back to the list of available data elements
    var nameTd = tr.getElementsByTagName('td')[0];
    var dataElementName = nameTd.firstChild.nodeValue;
    var dataElementId = nameTd.getElementsByTagName('input')[0].value;
    var option = document.createElement('option');
    option.value = dataElementId;
    option.appendChild(document.createTextNode(dataElementName));
    byId('availableDataElements').add(option, null);

    //Remove data from the table of data elements
    tr.parentNode.removeChild(tr);
}

/**
 * Remove placeholder from the selected data elements table
 */
function removeTablePlaceholder()
{
	var table = byId('selectedDataElements').getElementsByTagName('tbody')[0];
	var trs = table.getElementsByTagName('tr');
	
	for ( var i = 0, tr; tr = trs[i]; i++ )
	{
    	if ( tr.getAttribute('class') == 'placeholder' )
    	{
        	table.removeChild(tr);
      	}
    }
}
