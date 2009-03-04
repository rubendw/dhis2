
function performMaintenance()
{
    var hierarchyHistory = document.getElementById( "hierarchyHistory" ).checked;
    var aggregatedDataValues = document.getElementById( "aggregatedDataValues" ).checked;
    var aggregatedIndicatorValues = document.getElementById( "aggregatedIndicatorValues" ).checked;
    var zeroValues = document.getElementById( "zeroValues" ).checked;
    var dataSetCompleteness = document.getElementById( "dataSetCompleteness" ).checked;
    
    if ( hierarchyHistory || aggregatedDataValues || aggregatedIndicatorValues || zeroValues ||dataSetCompleteness )
    {
        setMessage( i18n_performing_maintenance );
        
        var params = "hierarchyHistory=" + hierarchyHistory +
            "&aggregatedDataValues=" + aggregatedDataValues + 
            "&aggregatedIndicatorValues=" + aggregatedIndicatorValues +
            "&zeroValues=" + zeroValues +
            "&dataSetCompleteness=" + dataSetCompleteness;
            
        var url = "performMaintenance.action";
        
        var request = new Request();
        request.sendAsPost( params );
        request.setCallbackSuccess( performMaintenanceReceived );
        request.send( url );
    }
    else
    {
        setMessage( i18n_select_options );
    }
}

function performMaintenanceReceived( messageElement )
{
    setMessage( i18n_maintenance_performed );
}
