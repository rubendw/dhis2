// -----------------------------------------------------------------------------
// Change DataDictionary in Indicator Screen
// -----------------------------------------------------------------------------

function indicatorDataDictionaryChanged( list )
{
    var indgId = document.getElementById( "indGroupList" ).value;
    
    var id = list.options[list.selectedIndex].value;
    
    var url = "getIndicatorList.action?indicatorGroupId=" + indgId + "&id=" + id;
    
    window.location.href = url;
}

// -----------------------------------------------------------------------------
// Change DataElementGroup
// -----------------------------------------------------------------------------

function indicatorGroupChanged( list )
{
    var ddId = document.getElementById( "dataDictionaryList" ).value;
    
    var id = list.options[list.selectedIndex].value;
    
    var url = "getIndicatorList.action?indicatorGroupId=" + id + "&id=" + ddId;
    
    window.location.href = url;
}

// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showIndicatorDetails( indicatorId )
{
    var request = new Request();
    request.setResponseTypeXML( 'indicator' );
    request.setCallbackSuccess( indicatorReceived );
    request.send( 'getIndicator.action?id=' + indicatorId );
}

function indicatorReceived( indicatorElement )
{
    setFieldValue( 'nameField', getElementValue( indicatorElement, 'name' ) );
    
    setFieldValue( 'shortNameField', getElementValue( indicatorElement, 'shortName' ) );
    
    var alternativeName = getElementValue( indicatorElement, 'alternativeName' );
    setFieldValue( 'alternativeNameField', alternativeName ? alternativeName : '[' + i18n_none + ']' );
    
    var code = getElementValue( indicatorElement, 'code' );
    setFieldValue( 'codeField', code ? code : '[' + i18n_none + ']' );
    
    var description = getElementValue( indicatorElement, 'description' );
    setFieldValue( 'descriptionField', description ? description : '[' + i18n_none + ']' );
    
    var annualized = getElementValue( indicatorElement, 'annualized' );
    setFieldValue( 'annualizedField', annualized == "true" ? i18n_yes : i18n_no );
    
    setFieldValue( 'indicatorTypeNameField', getElementValue( indicatorElement, 'indicatorTypeName' ) );
    
    var numeratorDescription = getElementValue( indicatorElement, 'numeratorDescription' );
    setFieldValue( 'numeratorDescriptionField', numeratorDescription ? numeratorDescription : '[' + i18n_none + ']' );

    var denominatorDescription = getElementValue( indicatorElement, 'denominatorDescription' );
    setFieldValue( 'denominatorDescriptionField', denominatorDescription ? denominatorDescription : '[' + i18n_none + ']' );

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
// Remove indicator
// -----------------------------------------------------------------------------

function removeIndicator( indicatorId, indicatorName )
{
    var result = window.confirm( i18n_confirm_delete + '\n\n' + indicatorName );
    
    if ( result )
    {
        window.location.href = 'removeIndicator.action?id=' + indicatorId;
    }
}

// -----------------------------------------------------------------------------
// Add indicator
// -----------------------------------------------------------------------------

function validateAddIndicator()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    request.send( 'validateIndicator.action?name=' + getFieldValue( 'name' ) +
    	'&shortName=' + getFieldValue( 'shortName' ) +
    	'&alternativeName=' + getFieldValue( 'alternativeName' ) +
    	'&code=' + getFieldValue( 'code' ) +
        '&indicatorTypeId=' + getSelectedValue( 'indicatorTypeId' ) +
        '&numerator=' + getFieldValue( 'numerator' ) +
        '&numeratorDescription=' + getFieldValue( 'numeratorDescription' ) +
        '&numeratorAggregationOperator=' + getFieldValue( 'numeratorAggregationOperator' ) +
        '&denominator=' + getFieldValue( 'denominator' ) +
        '&denominatorDescription=' + getFieldValue( 'denominatorDescription' ) +
        '&denominatorAggregationOperator=' + getFieldValue( 'denominatorAggregationOperator' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addIndicatorForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_adding_indicator_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}

function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
}

function getSelectedValue( selectId )
{
    var select = document.getElementById( selectId );

    return select.options[select.selectedIndex].value;
}

// -----------------------------------------------------------------------------
// Update indicator
// -----------------------------------------------------------------------------

function validateUpdateIndicator()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateIndicator.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&alternativeName=' + getFieldValue( 'alternativeName' ) +
    	'&code=' + getFieldValue( 'code' ) +
        '&indicatorTypeId=' + getSelectedValue( 'indicatorTypeId' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateIndicatorForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( i18n_saving_indicator_failed + ':' + '\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
