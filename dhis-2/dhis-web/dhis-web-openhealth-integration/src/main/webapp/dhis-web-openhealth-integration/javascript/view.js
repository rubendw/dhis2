
function removeView( viewId )
{
	var dialog = confirm( i18n_confirm_remove_view );
	
	if ( dialog )
	{
		window.location.href = "deleteOlapURL.action?id=" + viewId;
	}
}
