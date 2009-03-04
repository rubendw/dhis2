
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


function formValidations()
{
	var selOUListIndex = document.ChartGenerationForm.orgUnitListCB.options.length;
	var selVRListText  = document.ChartGenerationForm.selectedValidationRules.options[0].text;
    sDateIndex = document.ChartGenerationForm.sDateLB.selectedIndex;
    eDateIndex = document.ChartGenerationForm.eDateLB.selectedIndex;
    sDate = document.ChartGenerationForm.sDateLB.options[sDateIndex].text;
    eDate = document.ChartGenerationForm.eDateLB.options[eDateIndex].text;
	
    if(selOUListIndex <= 0) {alert("Please Select OrganisationUnit");return false;}
    else if(selVRListText == null || selVRListText == "") {alert("Please Select Validation Rule(s)");return false;}
    else if(sDateIndex < 0) {alert("Please Select Starting Period");return false;}
    else if(eDateIndex < 0) {alert("Please Select Ending Period");return false;}
    else if(sDate > eDate) {alert("Starting Date is Greater");return false;}
    
    var k=0;
	for(k=0;k<document.ChartGenerationForm.selectedValidationRules.options.length;k++)
    {
    	document.ChartGenerationForm.selectedValidationRules.options[k].selected = true;
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