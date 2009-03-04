
function filterValues( filter )
{
    var list = document.getElementById( 'list' );
    
    var rows = list.getElementsByTagName( 'tr' );
    
    for ( var i = 0; i < rows.length; ++i )
    {
        var cell = rows[i].getElementsByTagName( 'td' )[0];
        
        var value = cell.firstChild.nodeValue;

        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            rows[i].style.display = 'table-row';
        }
        else
        {
            rows[i].style.display = 'none';
        }
    }
}

function dataElementTypeChanged( list )
{
	var type = list.options[ list.selectedIndex ].value;
	
	var url = "getFilteredExtendedDataElements.action?dataElementType=" + type;
	
	window.location.href = url;
}
