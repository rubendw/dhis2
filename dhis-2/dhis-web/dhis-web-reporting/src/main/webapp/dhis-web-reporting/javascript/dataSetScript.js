function previewDataSetReport()
{
	document.getElementById("reportForm").action = "getDataSetReportTypeForPreview.action";
	document.getElementById("reportForm").submit();	
}

function generateDataSetReport()
{
	document.getElementById("reportForm").action = "getDataSetReportTypeForPDF.action";
	document.getElementById("reportForm").submit();
}