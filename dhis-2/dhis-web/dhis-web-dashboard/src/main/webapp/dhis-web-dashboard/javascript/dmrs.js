
// Removes slected orgunits from the Organisation List
function remOUFunction()
{
	//var index = document.ChartGenerationForm.ouIDTB.options.length;
    //var i=0;
    //for(i=index-1;i>=0;i--)
    //{
    	if(document.ChartGenerationForm.ouIDTB.selected)
    	document.ChartGenerationForm.ouIDTB = null;
    //}
}// remOUFunction end
         
function selButtonFunction(selButton)
{
	document.ChartGenerationForm.indicatorGroupId.disabled = false;
    document.ChartGenerationForm.availableIndicators.disabled = false;
    document.ChartGenerationForm.selectedIndicators.disabled = false;
	
	document.ChartGenerationForm.selectedButton.value = selButton;
  	if(selButton == "ShowReport")
  	{
  		var k=0;
		var i = -1;
         	moveAllById( 'selectedIndicators', 'availableIndicators' );
            //getIndicators();
		    moveAllById( 'availableIndicators', 'selectedIndicators' );
		    for(k=0;k<document.ChartGenerationForm.selectedIndicators.options.length;k++)
    	    {
    			document.ChartGenerationForm.selectedIndicators.options[k].selected = true;
            } // for loop end
  	}//if block end
}// selButtonFunction end


//Graphical Analysis Form Validations
function formValidationsForDashBoardMatrix()
{
	var selOUListIndex = document.ChartGenerationForm.ouIDTB.value;
	var selIndListSize  = document.ChartGenerationForm.selectedIndicators.options.length;
    sDateIndex = document.ChartGenerationForm.sDateLB.selectedIndex;
    eDateIndex = document.ChartGenerationForm.eDateLB.selectedIndex;
    sDate = document.ChartGenerationForm.sDateLB.options[sDateIndex].text;
    eDate = document.ChartGenerationForm.eDateLB.options[eDateIndex].text;

    if(selOUListIndex == null || selOUListIndex == "") {alert("Please Select OrganisationUnit"); return false;}
    else if(selIndListSize <= 0) {alert("Please Select Indicator(s)"); return false;}
    else if(sDateIndex < 0) {alert("Please Select Starting Period"); return false;}
    else if(eDateIndex < 0) {alert("Please Select Ending Period"); return false;}
    else if(sDate > eDate) {alert("Starting Date is Greater"); return false;}

	var k=0;
	for(k=0;k<document.ChartGenerationForm.selectedIndicators.options.length;k++)
    {
    	document.ChartGenerationForm.selectedIndicators.options[k].selected = true;
    } // for loop end

    //for(k=0;k<selOUListIndex;k++)
    //{
    	document.ChartGenerationForm.ouIDTB.selected = true;
    //}

    var sWidth = 850;
	var sHeight = 650;
    var LeftPosition=(screen.width)?(screen.width-sWidth)/2:100;
    var TopPosition=(screen.height)?(screen.height-sHeight)/2:100;

    window.open('','chartWindow1','width=' + sWidth + ', height=' + sHeight + ', ' + 'left=' + LeftPosition + ', top=' + TopPosition + ', ' + 'location=no, menubar=no, ' +  'status=no, toolbar=no, scrollbars=yes, resizable=yes');
  	return true;

} // formValidations Function End		 

