
// OrgUnit GroupSet Change Function
function orgUnitGroupSetCB()
{
	var orgUnitGroupSetList = document.getElementById( 'orgUnitGroupSetListCB' );
	var orgUnitList = document.getElementById( 'orgUnitListCB' );
	if(document.getElementById( 'ougSetCB' ).checked)
	{
		orgUnitGroupSetList.disabled = false;
		getOrgUnitGroups();		
	}
	else
	{
		orgUnitGroupSetList.disabled = true;
	}
	clearList(orgUnitList);
}

// Removes slected orgunits from the Organisation List
function remOUFunction()
{
	var index = document.ChartGenerationForm.orgUnitListCB.options.length;
    var i=0;
    for(i=index-1;i>=0;i--)
    {
    	if(document.ChartGenerationForm.orgUnitListCB.options[i].selected)
    	document.ChartGenerationForm.orgUnitListCB.options[i] = null;
    }
}// remOUFunction end

// DataElement and Its options Change Function
function deSelectionChangeFuntion(evt)
{
    var availableDataElements = document.getElementById("availableDataElements");
    var selectedDataElements = document.getElementById("selectedDataElements");

	clearList(availableDataElements);
	clearList(selectedDataElements);
	
	getDataElements();
}

// Category ListBox Change function
function categoryChangeFunction(evt)
{
	selCategory = evt.target.value;
	if(selCategory == "period")
	{
		document.ChartGenerationForm.facilityLB.disabled = true;
		var index = document.ChartGenerationForm.orgUnitListCB.options.length;
        for(i=0;i<index;i++)
    	{
    		document.ChartGenerationForm.orgUnitListCB.options[0] = null;
    	}
	}
	else
	{
		document.ChartGenerationForm.facilityLB.disabled = false;
	}
}// categoryChangeFunction end
			          
//Facility ListBox Change Function
function facilityChangeFunction(evt)
{
	selFacility = evt.target.value;
	if(selFacility == "children")
	{
		var index = document.ChartGenerationForm.orgUnitListCB.options.length;
        for(i=0;i<index;i++)
    	{
    		document.ChartGenerationForm.orgUnitListCB.options[0] = null;
    	}
	}
}// facilityChangeFunction end

// Indicator or Dataelement radio button changed function
function riradioSelection(evt)
{
	selriRadioButton = evt.target.value;
    if(selriRadioButton == "dataElementsRadio")
    {
		document.ChartGenerationForm.indicatorGroupId.disabled = true;
	    document.ChartGenerationForm.availableIndicators.disabled = true;
	    document.ChartGenerationForm.selectedIndicators.disabled = true;
	    
	    document.ChartGenerationForm.dataElementGroupId.disabled = false;
	    document.ChartGenerationForm.availableDataElements.disabled = false;
	    document.ChartGenerationForm.selectedDataElements.disabled = false;
  	}// if block end
	else
	{
		document.ChartGenerationForm.indicatorGroupId.disabled = false;
	    document.ChartGenerationForm.availableIndicators.disabled = false;
	    document.ChartGenerationForm.selectedIndicators.disabled = false;
	    
	    document.ChartGenerationForm.dataElementGroupId.disabled = true;
	    document.ChartGenerationForm.availableDataElements.disabled = true;
	    document.ChartGenerationForm.selectedDataElements.disabled = true;
	}// else end
}// function riradioSelection end

// Selected Button (ie ViewSummary or ViewChart) Function
function selButtonFunction(selButton)
{
	document.ChartGenerationForm.selectedButton.value = selButton;
}// selButtonFunction end


//Graphical Analysis Form Validations
function formValidations()
{
		
	var selOUListLength = document.ChartGenerationForm.orgUnitListCB.options.length;
	var selDEListSize  = document.ChartGenerationForm.selectedDataElements.options.length;
	var selIndListSize  = document.ChartGenerationForm.selectedIndicators.options.length;
    sDateIndex    = document.ChartGenerationForm.sDateLB.selectedIndex;
    eDateIndex    = document.ChartGenerationForm.eDateLB.selectedIndex;
    category = document.ChartGenerationForm.categoryLB.selectedIndex;
    sDate = document.ChartGenerationForm.sDateLB.options[sDateIndex].text;
    eDate = document.ChartGenerationForm.eDateLB.options[eDateIndex].text;
    categoryName = document.ChartGenerationForm.categoryLB.options[category].text;

    if(selOUListLength <= 0) {alert("Please Select OrganisationUnit");return false;}
    else if(selriRadioButton == "dataElementsRadio" && selDEListSize <= 0)	 {alert("Please Select DataElement(s)");return false;}
    else if(selriRadioButton == "indicatorsRadio" && selIndListSize <= 0) {alert("Please Select Indicator(s)");return false;}
    else if(sDateIndex < 0) {alert("Please Select Starting Period");return false;}
    else if(eDateIndex < 0) {alert("Please Select Ending Period");return false;}
    else if(category < 0) {alert("Please Select Category");return false;}
    else if(sDate > eDate) {alert("Starting Date is Greater");return false;}

	var k=0;
	if(selriRadioButton == "dataElementsRadio")
	{
		for(k=0;k<document.ChartGenerationForm.selectedDataElements.options.length;k++)
    	{
    		document.ChartGenerationForm.selectedDataElements.options[k].selected = true;
        } // for loop end
	}
	else
	{
		for(k=0;k<document.ChartGenerationForm.selectedIndicators.options.length;k++)
    	{
    		document.ChartGenerationForm.selectedIndicators.options[k].selected = true;
        } // for loop end
    }
    
    if(document.getElementById( 'ougSetCB' ).checked)
    {
    	if(document.ChartGenerationForm.orgUnitListCB.selectedIndex <= -1) 
    		{alert("Please Select OrganisationUnit");return false;}
    }
    else
    {
    	for(k=0;k<selOUListLength;k++)
    	{
    		document.ChartGenerationForm.orgUnitListCB.options[k].selected = true;
    	}
	}
	
    var sWidth = 850;
	var sHeight = 650;
    var LeftPosition=(screen.width)?(screen.width-sWidth)/2:100;
    var TopPosition=(screen.height)?(screen.height-sHeight)/2:100;

    window.open('','chartWindow1','width=' + sWidth + ', height=' + sHeight + ', ' + 'left=' + LeftPosition + ', top=' + TopPosition + ', ' + 'location=no, menubar=no, ' +  'status=no, toolbar=no, scrollbars=yes, resizable=yes');
  	return true;
} // formValidations Function End

