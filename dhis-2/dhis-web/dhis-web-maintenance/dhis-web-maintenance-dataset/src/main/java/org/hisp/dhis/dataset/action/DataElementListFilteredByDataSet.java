package org.hisp.dhis.dataset.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;

import com.opensymphony.xwork.Action;

public class DataElementListFilteredByDataSet implements Action{
	
	private Integer dataSetId;
	
	private List<DataElement> dataElements;
	
	private String selectedDataElements[];	
	
	private DataSetService dataSetService;
	
	/*----------------------------------------------------------
	 * getter & setter 
	 */
	public String[] getSelectedDataElements() {
		return selectedDataElements;
	}


	public void setSelectedDataElements(String[] selectedDataElements) {
		this.selectedDataElements = selectedDataElements;
	}


	public List<DataElement> getDataElements() {
		return dataElements;
	}


	public void setDataSetId(Integer dataSetId) {
		this.dataSetId = dataSetId;
	}
	
	
	public DataSetService getDataSetService() {
		return dataSetService;
	}


	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}


	public String execute() throws Exception {
		if(dataSetId!=null){
			DataSet dataSet = dataSetService.getDataSet(dataSetId.intValue());
			dataElements = new ArrayList<DataElement>(dataSet.getDataElements());
			return SUCCESS;
		}
		// TODO Auto-generated method stub
		return NONE;
	}

	

}
