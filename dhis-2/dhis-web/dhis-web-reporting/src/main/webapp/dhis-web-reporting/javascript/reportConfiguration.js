
function homeChanged()
{
	var homeField = document.getElementById( "home" );
	var directoryField = document.getElementById( "directory" );
	
	var homeValue = homeField.value;
	
	var index = homeValue.lastIndexOf( "/" );
	
	if ( index == -1 )
	{
		index = homeValue.lastIndexOf( "\\" );
	}
	
	if ( index != -1 )
	{
	   var defaultDirectory = homeValue.substring( index + 1 );
	   
	   directoryField.value = defaultDirectory;
	}
}
