/*
 * Data element selector plugin for FCK editor.
 * Christian Mikalsen <chrismi@ifi.uio.no>
 */

// Register the command.
var dataSetIdField = window.parent.document.getElementById( 'dataSetIdField' );
var dataSetId = dataSetIdField.value;

var urlLocation = window.parent.location.href;
var urlParts = new Array();
urlParts = urlLocation.split('viewDataEntryForm.action');
var urlPath = urlParts[0]+'selectDataElement.action?dataSetId='+dataSetId

FCKCommands.RegisterCommand( 'InsertDataElement', new FCKDialogCommand( 'InsertDataElement', 'DataElement Selector', urlPath, 700, 550 ) ) ;


// Create the "Insert Data element" toolbar button.
var oInsertDataElementItem = new FCKToolbarButton( 'InsertDataElement', FCKLang.PlaceholderBtn ) ;
oInsertDataElementItem.IconPath = FCKPlugins.Items['dataelement'].Path + 'dataElement.gif' ;
FCKToolbarItems.RegisterItem( 'InsertDataElement', oInsertDataElementItem ) ;

// The object used for all operations.
var FCKSelectElement = new Object() ;

// Called by the popup to insert the selected data element.
FCKSelectElement.Add = function( dataElementId, dataElementName, dataElementType, dispName, viewByValue, selectedOptionComboIds, selectedOptionComboNames ) 
{ 
	viewByValue = "@@"+viewByValue+"@@";
	
	for(k=0; k<selectedOptionComboIds.length; k++)
	{	  	
		var optionComboId = selectedOptionComboIds[k];
		var optionComboName = selectedOptionComboNames[k];
		
		var titleValue = "-- "+dataElementId + ". "+ dataElementName+" "+optionComboId+". "+optionComboName+" ("+dataElementType+") --";
		var displayName = dispName+" - "+optionComboName+" ]";
		var dataEntryId = "value[" + dataElementId + "].value:value[" + optionComboId + "].value";
		var boolDataEntryId = "value[" + dataElementId + "].value:value[" + optionComboId + "].value";
		
		if (dataElementType == "bool")
		{
			FCK.InsertHtml("<input title=\"" + titleValue + "\" view=\""+viewByValue+"\" value=\"" + displayName + "\" name=\"entryselect\" id=\""+ boolDataEntryId +"\" style=\"width:4em;text-align:center\"/>");			
			
		}	
		else 
		{	
			FCK.InsertHtml("<input title=\"" + titleValue + "\" view=\""+viewByValue+"\" value=\"" + displayName + "\" name=\"entryfield\" id=\"" + dataEntryId + "\" style=\"width:4em;text-align:center\"/>");			
			 	
		}
	}				
}
