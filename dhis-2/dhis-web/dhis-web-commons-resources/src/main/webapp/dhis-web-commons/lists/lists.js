
/**
 * Moves selected options in the source list to the target list.
 * 
 * @param fromList the id of the source list.
 * @param targetList the id of the target list.
 */
function moveSelectedById( fromListId, targetListId )
{
    var fromList = document.getElementById( fromListId );
    var targetList = document.getElementById( targetListId );

    moveSelected( fromList, targetList );
}

/**
 * Moves selected options in the source list to the target list.
 * 
 * @param fromList the source list.
 * @param targetList the target list.
 */
function moveSelected( fromList, targetList )
{
    if ( fromList.selectedIndex == -1 )
    {
        return;
    }

    while ( fromList.selectedIndex > -1 )
    {
        option = fromList.options[ fromList.selectedIndex ];
        fromList.remove( fromList.selectedIndex );
        targetList.add(option, null);
        option.selected = true;
    }
}

/**
 * Moves all elements in a list to the target list
 * 
 * @param fromListId the id of the source list.
 * @param targetListId the id of target list.
 */
function moveAllById( fromListId, targetListId )
{
    var fromList = document.getElementById( fromListId );
    var targetList = document.getElementById( targetListId );

    moveAll( fromList, targetList );
}

/**
 * Moves all elements in a list to the target list
 * 
 * @param fromList the source list.
 * @param targetList the target list.
 */
function moveAll( fromList, targetList )
{
    for ( var i = fromList.options.length - 1; i >= 0; i-- )
    {
        option = fromList.options[i];
        fromList.remove( i );
        targetList.add(option, null);
        option.selected = true;
    }
}

/**
 * Clears the list.
 * 
 * @param listId the id of the list.
 */
function clearListById( listId )
{
    var list = document.getElementById( listId );
    
    clearList( list );
}

/**
 * Clears the list.
 * 
 * @param list the list.
 */
function clearList( list )
{
    list.options.length = 0;
}

/**
 * Tests whether the list contains the value.
 * 
 * @param listId the id of the list.
 * @param value the value.
 */
function listContainsById( listId, value )
{
    var list = document.getElementById( listId );
    
    return listContains( list, value );
}

/**
 * Tests whether the list contains the value.
 * 
 * @param list the list.
 * @param value the value.
 */
function listContains( list, value )
{
    for ( var i = 0; i < list.options.length; i++ )
    {
        if ( list.options[i].value == value )
        {
            return true;
        }
    }

    return false;
}

/**
 * Marks all elements in a list as selected
 * 
 * @param listId the id of the list.
 */
function selectAllById( listId )
{
	var list = document.getElementById( listId );
	
	selectAll( list );
}

/**
 * Marks all elements in a list as selected
 * 
 * @param list the list.
 */
function selectAll( list )
{
	for ( var i = 0; i < list.options.length; i++ )
	{
		list.options[i].selected = true;
	}
}

/**
 * Marks all elements in a list as not selected.
 * 
 * @param listId the id of the list.
 */
function deselectAllById( listId )
{
	var list = document.getElementById( listId );
	
	deselectAll( list );
}

/**
 * Marks all elements in a list as not selected.
 * 
 * @param list the list.
 */
function deselectAll( list )
{
	for ( var i = 0; i < list.options.length; i++ )
	{
		list.options[i].selected = false;
	}
}

/**
 * Adds an option to a select list.
 * 
 * @param listId the id of the list.
 * @param text the text of the option.
 * @param value the value of the option.
 */
function addOption( listId, text, value )
{
	var list = document.getElementById( listId );
	
	var option = new Option( text, value );
	
	list.add( option, null );
}

/**
 * Removes the selected option from a select list.
 * 
 * @param listId the id of the list.
 */
function removeSelectedOption( listId )
{
	var list = document.getElementById( "levelNames" );
	
	for ( var i = list.length - 1; i >= 0; i-- )
	{
		if ( list.options[ i ].selected )
		{
			list.remove( i );
		}
	}
}

/**
 * Moves the selected option in a select list up one position.
 * 
 * @param listId the id of the list.
 */
function moveUpSelectedOption( listId )
{
	var list = document.getElementById( listId );
	
	for ( var i = 0; i < list.length; i++ )
	{
		if ( list.options[ i ].selected )
		{
			if ( i > 0 )
			{
				var precedingOption = new Option( list.options[ i - 1 ].text, list.options[ i - 1 ].value );
				var currentOption = new Option( list.options[ i ].text, list.options[ i ].value );
				
				list.options[ i - 1 ] = currentOption;
				list.options[ i - 1 ].selected = true;
				list.options[ i ] = precedingOption;
			}
		}
	}
}

/**
 * Moves the selected option in a list down one position.
 * 
 * @param listId the id of the list.
 */
function moveDownSelectedOption( listId )
{
	var list = document.getElementById( listId );
	
	for ( var i = list.options.length - 1; i >= 0; i-- )
	{
		if ( list.options[ i ].selected )
		{
			if ( i <= list.options.length - 1 )
			{
				var subsequentOption = new Option( list.options[ i + 1 ].text, list.options[ i + 1 ].value );
				var currentOption = new Option( list.options[ i ].text, list.options[ i ].value );
				
				list.options[ i + 1 ] = currentOption;
				list.options[ i + 1 ].selected = true;
				list.options[ i ] = subsequentOption;
			}
		}
	}
}

