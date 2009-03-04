
// -----------------------------------------------------------------------------
// $Id: general.js 1813 2006-09-01 12:08:08Z margrsto $
// -----------------------------------------------------------------------------
// Selection
// -----------------------------------------------------------------------------

function organizationUnitSelected( orgUnits )
{
    document.getElementById( 'selectForm' ).submit();
}

selection.setListenerFunction( organizationUnitSelected );

function changeOrder( dataSetId )
{
    window.open( 'getDataElementOrder.action?dataSetId=' + dataSetId, '_blank', 'width=700,height=500,scrollbars=yes' );
}

// -----------------------------------------------------------------------------
// Comments
// -----------------------------------------------------------------------------

function commentSelected( dataElementId )
{
    var commentSelector = document.getElementById( 'value[' + dataElementId + '].comments' );
    var commentField = document.getElementById( 'value[' + dataElementId + '].comment' );

    var value = commentSelector.options[commentSelector.selectedIndex].value;
    
    if ( value == 'custom' )
    {
        commentSelector.style.display = 'none';
        commentField.style.display = 'inline';
        
        commentField.select();
        commentField.focus();
    }
    else
    {
        commentField.value = value;
        
        saveComment( dataElementId, value );
    }
}

function commentLeft( dataElementId )
{
    var commentField = document.getElementById( 'value[' + dataElementId + '].comment' );
    var commentSelector = document.getElementById( 'value[' + dataElementId + '].comments' );

    saveComment( dataElementId, commentField.value );

    var value = commentField.value;
    
    if ( value == '' )
    {
        commentField.style.display = 'none';
        commentSelector.style.display = 'inline';

        commentSelector.selectedIndex = 0;
    }
}

// -----------------------------------------------------------------------------
// Save
// -----------------------------------------------------------------------------

function saveValue( dataElementId, dataElementName )
{
    var field = document.getElementById( 'value[' + dataElementId + '].value' );
    var type = document.getElementById( 'value[' + dataElementId + '].type' ).innerHTML;
    
    field.style.backgroundColor = '#ffffcc';
    
    if ( field.value != '' )
    {
        if ( type == 'int' )
        {
            if ( !isInt( field.value ))
            {
                field.style.backgroundColor = '#ffcc00';

                window.alert( i18n_value_must_integer + '\n\n' + dataElementName );

                field.select();
                field.focus();

                return;
            }
            else
            {
                var minString = document.getElementById( 'value[' + dataElementId + '].min' ).innerHTML;
                var maxString = document.getElementById( 'value[' + dataElementId + '].max' ).innerHTML;

                if ( minString.length != 0 && maxString.length != 0 )
                {
                    var value = new Number( field.value );
                    var min = new Number( minString );
                    var max = new Number( maxString );

                    if ( value < min )
                    {
                        var valueSaver = new ValueSaver( dataSetId, dataElementId, periodId, field.value, '#ffcccc' );
                        valueSaver.save();
                        
                        window.alert( i18n_value_of_data_element_less + '\n\n' + dataElementName );
                        
                        return;
                    }

                    if ( value > max )
                    {
                        var valueSaver = new ValueSaver( dataSetId, dataElementId, periodId, field.value, '#ffcccc' );
                        valueSaver.save();
                        
                        window.alert( i18n_value_of_data_element_greater + '\n\n' + dataElementName);
                        
                        return;
                    }
                }
            }
        }
    }

    var valueSaver = new ValueSaver( dataSetId, dataElementId, periodId, field.value, '#ccffcc' );
    valueSaver.save();
}

function saveBoolean( dataElementId )
{
    var select = document.getElementById( 'value[' + dataElementId + '].boolean' );
    
    select.style.backgroundColor = '#ffffcc';
    
    var valueSaver = new ValueSaver( dataSetId, dataElementId, periodId, select.options[select.selectedIndex].value );
    valueSaver.save();
}

function saveComment( dataElementId, commentValue )
{
    var field = document.getElementById( 'value[' + dataElementId + '].comment' );
    var select = document.getElementById( 'value[' + dataElementId + '].comments' );
    
    field.style.backgroundColor = '#ffffcc';
    select.style.backgroundColor = '#ffffcc';
    
    var commentSaver = new CommentSaver( dataSetId, dataElementId, periodId, commentValue );
    commentSaver.save();
}

function isInt( value )
{
    var number = new Number( value );
    
    if ( isNaN( number ))
    {
        return false;
    }
    
    return true;
}

// -----------------------------------------------------------------------------
// Saver objects
// -----------------------------------------------------------------------------

function ValueSaver( dataSetId_, dataElementId_, periodId_, value_, resultColor_ )
{
    var SUCCESS = '#ccffcc';
    var ERROR = '#ccccff';

    var dataSetId = dataSetId_;
    var dataElementId = dataElementId_;
    var periodId = periodId_;
    var value = value_;
    var resultColor = resultColor_;
    
    this.save = function()
    {
        var request = new Request();
        request.setCallbackSuccess( handleResponse );
        request.setCallbackError( handleHttpError );
        request.setResponseTypeXML( 'status' );
        request.send( 'saveValue.action?dataSetId=' + dataSetId + '&dataElementId=' +
                dataElementId + '&periodId=' + periodId + '&value=' + value );
    };
    
    function handleResponse( rootElement )
    {
        var codeElement = rootElement.getElementsByTagName( 'code' )[0];
        var code = parseInt( codeElement.firstChild.nodeValue );
        
        if ( code == 0 )
        {
            markValue( resultColor );
            
            //var textNode;
            
            //var timestampElement = rootElement.getElementsByTagName( 'timestamp' )[0];
            //var timestampField = document.getElementById( 'value[' + dataElementId + '].timestamp' );
            //textNode = timestampElement.firstChild;
            
            //timestampField.innerHTML = ( textNode ? textNode.nodeValue : '' );
            
            //var storedByElement = rootElement.getElementsByTagName( 'storedBy' )[0];
            //var storedByField = document.getElementById( 'value[' + dataElementId + '].storedBy' );
            //textNode = storedByElement.firstChild;

            //storedByField.innerHTML = ( textNode ? textNode.nodeValue : '' );
        }
        else
        {
            markValue( ERROR );
            window.alert( i18n_saving_value_failed_status_code + '\n\n' + code );
        }
    }
    
    function handleHttpError( errorCode )
    {
        markValue( ERROR );
        window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
    }
    
    function markValue( color )
    {
        var type = document.getElementById( 'value[' + dataElementId + '].type' ).innerText;
        var element;
        
        if ( type == 'bool' )
        {
            element = document.getElementById( 'value[' + dataElementId + '].boolean' );
        }
        else
        {
            element = document.getElementById( 'value[' + dataElementId + '].value' );
        }

        element.style.backgroundColor = color;
    }
}

function CommentSaver( dataSetId_, dataElementId_, periodId_, value_ )
{
    var SUCCESS = '#ccffcc';
    var ERROR = '#ccccff';

    var dataSetId = dataSetId_;
    var dataElementId = dataElementId_;
    var periodId = periodId_;
    var value = value_;
    
    this.save = function()
    {
        var request = new Request();
        request.setCallbackSuccess( handleResponse );
        request.setCallbackError( handleHttpError );
        request.setResponseTypeXML( 'status' );
        request.send( 'saveComment.action?dataSetId=' + dataSetId + '&dataElementId=' +
                dataElementId + '&periodId=' + periodId + '&comment=' + value );
    };
    
    function handleResponse( rootElement )
    {
        var codeElement = rootElement.getElementsByTagName( 'code' )[0];
        var code = parseInt( codeElement.firstChild.nodeValue );
        
        if ( code == 0 )
        {
            markComment( SUCCESS );
            
            //var textNode;
            
            //var timestampElement = rootElement.getElementsByTagName( 'timestamp' )[0];
            //var timestampField = document.getElementById( 'value[' + dataElementId + '].timestamp' );
            //textNode = timestampElement.firstChild;
            
            //timestampField.innerHTML = ( textNode ? textNode.nodeValue : '' );
            
            //var storedByElement = rootElement.getElementsByTagName( 'storedBy' )[0];
            //var storedByField = document.getElementById( 'value[' + dataElementId + '].storedBy' );
            //textNode = storedByElement.firstChild;

            //storedByField.innerHTML = ( textNode ? textNode.nodeValue : '' );
        }
        else
        {
            markComment( ERROR );
            window.alert( i18n_saving_comment_failed_status_code + '\n\n' + code );
        }
    }
    
    function handleHttpError( errorCode )
    {
        markComment( ERROR );
        window.alert( i18n_saving_comment_failed_error_code + '\n\n' + errorCode );
    }
    
    function markComment( color )
    {
        var field = document.getElementById( 'value[' + dataElementId + '].comment' );
        var select = document.getElementById( 'value[' + dataElementId + '].comments' );

        field.style.backgroundColor = color;
        select.style.backgroundColor = color;
    }
}

// -----------------------------------------------------------------------------
// View history
// -----------------------------------------------------------------------------

function viewHistory( dataElementId, periodId, dataSetId )
{
    window.open( 'viewHistory.action?dataElementId=' + dataElementId + '&periodId=' + periodId + '&dataSetId=' + dataSetId, '_blank', 'width=560,height=550,scrollbars=yes' );
}

function showHideAll(id){
	var status = document.getElementById(id).style.display;
	//document.getElementById(id).style.height = "30px";
	if(status=='none'){
		document.getElementById(id).style.display = 'block';		
	}else{
		document.getElementById(id).style.display = 'none';		
	}
}
