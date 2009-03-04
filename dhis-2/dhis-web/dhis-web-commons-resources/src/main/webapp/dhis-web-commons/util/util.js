/**
 * JavaScript utility functions
 */

/**
 * Convenience method for document.getElementById()
 * @param id Id of the element to get
 */
function byId(id)
{
  return document.getElementById(id);
}

/**
 * Display or hide an element
 * @param id Id of the element to toggle
 * @param display Whether or not to display the element
 */
function toggleByIdAndFlag( id, display )
{
	var node = byId(id);
	
	if ( ! node )
	{
		return;
	}
    node.style.display = (display ? 'block' : 'none');
}

function toggleById(id)
{
    var node = byId(id);

    if ( ! node )
    {
    	return;
    }
    
    var display = node.style.display;
    // XXX Class values for display don't seem to make it into the DOM.
    // Assume it's hidden.
    node.style.display = ( display == 'none' || display == '' ? 'block' : 'none' );
}