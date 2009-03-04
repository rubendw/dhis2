
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

//Survey Analysis Form Validations
function formValidations()
{
	var selOUListIndex = document.ChartGenerationForm.orgUnitListCB.options.length;
	var selDEListText  = document.ChartGenerationForm.selectedDataElements.options[0].text;
	var selIndListText  = document.ChartGenerationForm.selectedIndicators.options[0].text;
    sDateIndex    = document.ChartGenerationForm.sDateLB.selectedIndex;
    eDateIndex    = document.ChartGenerationForm.eDateLB.selectedIndex;    
    sDate = document.ChartGenerationForm.sDateLB.options[sDateIndex].text;
    eDate = document.ChartGenerationForm.eDateLB.options[eDateIndex].text;    

    if(selOUListIndex <= 0) {alert("Please Select OrganisationUnit");return false;}
    else if((selDEListText == null || selDEListText == "") && (selIndListText == null || selIndListText == ""))	 {alert("Please Select DataElement(s)");return false;}
    else if(sDateIndex < 0) {alert("Please Select Starting Period");return false;}
    else if(eDateIndex < 0) {alert("Please Select Ending Period");return false;}    
    else if(sDate > eDate) {alert("Starting Date is Greater");return false;}

	var k=0;
	for(k=0;k<document.ChartGenerationForm.selectedDataElements.options.length;k++)
   	{
   		document.ChartGenerationForm.selectedDataElements.options[k].selected = true;
    } // for loop end
	for(k=0;k<document.ChartGenerationForm.selectedIndicators.options.length;k++)
   	{
  		document.ChartGenerationForm.selectedIndicators.options[k].selected = true;
    } // for loop end
    for(k=0;k<selOUListIndex;k++)
    {
    	document.ChartGenerationForm.orgUnitListCB.options[k].selected = true;
    }

    var sWidth = 850;
	var sHeight = 650;
    var LeftPosition=(screen.width)?(screen.width-sWidth)/2:100;
    var TopPosition=(screen.height)?(screen.height-sHeight)/2:100;

    window.open('','chartWindow1','width=' + sWidth + ', height=' + sHeight + ', ' + 'left=' + LeftPosition + ', top=' + TopPosition + ', ' + 'location=no, menubar=no, ' +  'status=no, toolbar=no, scrollbars=yes, resizable=yes');
  	return true;
} // formValidations Function End

