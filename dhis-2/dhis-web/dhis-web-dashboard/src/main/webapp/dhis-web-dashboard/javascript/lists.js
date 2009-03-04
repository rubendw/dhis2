
/*
 * Move selected elements in a list to the target list
 */
function moveSelectedById( fromListId, targetListId )
{
    var fromList = document.getElementById( fromListId );
    var targetList = document.getElementById( targetListId );

    moveSelected( fromList, targetList );
}

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

/*
 * Move all elements in a list to the target list
 */
function moveAllById( fromListId, targetListId )
{
    var fromList = document.getElementById( fromListId );
    var targetList = document.getElementById( targetListId );

    moveAll( fromList, targetList );
}

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

/*
 * Clears the list
 */
function clearListById( listId )
{
    var list = document.getElementById( listId );
    
    clearList( list );
}

function clearList( list )
{
    list.options.length = 0;
}

/*
 * Returns true if the list contains the value
 */
function listContainsById( listId, value )
{
    var list = document.getElementById( listId );
    
    return listContains( list, value );
}

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

/*
 * Marks all elements in a list as selected
 */
function selectAllById( listId )
{
	var list = document.getElementById( listId );
	
	selectAll( list );
}

function selectAll( list )
{
	for ( var i = 0; i < list.options.length; i++ )
	{
		list.options[i].selected = true;
	}
}

/*
 * Marks all elements in a list as not selected
 */
function deselectAllById( listId )
{
	var list = document.getElementById( listId );
	
	deselectAll( list );
}

function deselectAll( list )
{
	for ( var i = 0; i < list.options.length; i++ )
	{
		list.options[i].selected = false;
	}
}
