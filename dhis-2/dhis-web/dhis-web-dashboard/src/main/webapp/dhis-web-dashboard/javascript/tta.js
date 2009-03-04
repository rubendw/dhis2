
// -----------------------------------------------------------------------------
// Get OptionCombos of a selected DataSet
// -----------------------------------------------------------------------------
function getOptionCombos()
{
  var request = new Request();
  request.setResponseTypeXML( 'optionCombo' );
  request.setCallbackSuccess( getOptionCombosCompleted );
  
  var dataSetSelector = document.getElementById( 'availableDataSets' );
  var dataSetId = dataSetSelector.options[dataSetSelector.selectedIndex].value;
  
  // Clear the OptionCombo list
  var optionComboList = document.getElementById( 'availableOptionCombos' );
  optionComboList.options.length = 0;

  var requestString = 'getOptionCombos.action';
  
  var params = 'dataSetId=' + dataSetId;  
          
  request.sendAsPost( params );
  request.send( requestString );

  return false;
}

function getOptionCombosCompleted( optionComboElement )
{
  var categoryOptions = optionComboElement.getElementsByTagName( 'categoryOptions' )[0];
  var optionsList = optionComboElement.getElementsByTagName( 'categoryOption' );

  var optionComboSelector = document.getElementById( 'availableOptionCombos' );
  
  for ( var i = 0; i < optionsList.length; i++ )
  {
    var categoryOption = optionsList[i];
    var name = categoryOption.firstChild.nodeValue;
    var id = categoryOption.getAttribute( 'id' );	
		
	var option = new Option( name, id );
	    
    optionComboSelector.add( option, null );
  }
  
  var messageContainer = document.getElementById('message');
  if( (optionsList.length ==0) )
  {
  	messageContainer.innerHTML = "No more Options to select";  	  	
  }
  else
  {
  	messageContainer.innerHTML = " ";  	  	
  }
}


// TopTen Analysis Form Validations
function formValidationsForTTA()
{
	var ouId = document.ChartGenerationForm.ouIDTB.value;
	var dataSetIndex  = document.ChartGenerationForm.availableDataSets.selectedIndex;
	var optionComboIndex = document.ChartGenerationForm.availableOptionCombos.selectedIndex;
    sDateIndex = document.ChartGenerationForm.sDateLB.selectedIndex;
    eDateIndex = document.ChartGenerationForm.eDateLB.selectedIndex;
    sDate = document.ChartGenerationForm.sDateLB.options[sDateIndex].text;
    eDate = document.ChartGenerationForm.eDateLB.options[eDateIndex].text;

    if(ouId == null || ouId== "" ) {alert("Please Select OrganisationUnit"); return false;}
    else if(dataSetIndex < 0) {alert("Please Select DataSet"); return false;}
    else if(optionComboIndex < 0) {alert("Please Select OptionCombo(s)"); return false;}
    else if(sDateIndex < 0) {alert("Please Select Starting Period"); return false;}
    else if(eDateIndex < 0) {alert("Please Select Ending Period"); return false;}
    else if(sDate > eDate) {alert("Starting Date is Greater"); return false;}

    var sWidth = 850;
	var sHeight = 650;
    var LeftPosition=(screen.width)?(screen.width-sWidth)/2:100;
    var TopPosition=(screen.height)?(screen.height-sHeight)/2:100;

    window.open('','chartWindow1','width=' + sWidth + ', height=' + sHeight + ', ' + 'left=' + LeftPosition + ', top=' + TopPosition + ', ' + 'location=no, menubar=no, ' +  'status=no, toolbar=no, scrollbars=yes, resizable=yes');
  	return true;

} // formValidations Function End		 
