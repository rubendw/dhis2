// OrgUnit GroupSet Change Function
function orgUnitGroupSetCB()
{
	document.ChartGenerationForm.ShowReport.disabled = false;			
	var orgUnitGroupSetList = document.getElementById( 'orgUnitGroupSetListCB' );
	var orgUnitList = document.getElementById( 'orgUnitListCB' );
	if(document.getElementById( 'ougSetCB' ).checked)
	{
		document.ChartGenerationForm.selectedGroup.value = null;
		document.ChartGenerationForm.selectedGroup.disabled = true;			
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

function groupChangeFunction(evt)
{
	document.ChartGenerationForm.selectedGroup.value = null;
	document.ChartGenerationForm.orgUnitListCB.value = null;
} //groupChangeFunction end

function formValidationsForDashBoardMatrix()
{
	//var selOUListIndex = document.ChartGenerationForm.ouIDTB.value;
	var selOUListIndex = document.ChartGenerationForm.orgUnitListCB.options.length;
	var selIndListSize  = document.ChartGenerationForm.selectedIndicators.options.length;
    sDateIndex = document.ChartGenerationForm.sDateLB.selectedIndex;
    eDateIndex = document.ChartGenerationForm.eDateLB.selectedIndex;
    sDate = document.ChartGenerationForm.sDateLB.options[sDateIndex].text;
    eDate = document.ChartGenerationForm.eDateLB.options[eDateIndex].text;

    //if(selOUListIndex == null || selOUListIndex == "") {alert("Please Select OrganisationUnit"); return false;}
    if(selOUListIndex <= 0) {alert("Please Select OrganisationUnit");return false;}
    else if(selIndListSize <= 0) {alert("Please Select Indicator(s)"); return false;}
    else if(sDateIndex < 0) {alert("Please Select Starting Period"); return false;}
    else if(eDateIndex < 0) {alert("Please Select Ending Period"); return false;}
    else if(sDate > eDate) {alert("Starting Date is Greater"); return false;}

	var k=0;
	for(k=0;k<document.ChartGenerationForm.selectedIndicators.options.length;k++)
    {
    	document.ChartGenerationForm.selectedIndicators.options[k].selected = true;
    } // for loop end

    if(document.getElementById( 'ougSetCB' ).checked)
    {
    	if(document.ChartGenerationForm.orgUnitListCB.selectedIndex <= -1) 
    		{alert("Please Select OrganisationUnit");return false;}
    }
    else
    {
    	for(k=0;k<selOUListIndex;k++)
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
