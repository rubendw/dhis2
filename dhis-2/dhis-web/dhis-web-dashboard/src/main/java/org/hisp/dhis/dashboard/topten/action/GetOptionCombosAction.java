package org.hisp.dhis.dashboard.topten.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;

import com.opensymphony.xwork.Action;

public class GetOptionCombosAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetService dataSetService;
    
    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    private int dataSetId;

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }    

    private List<String> optionComboIds;

    public List<String> getOptionComboIds()
    {
        return optionComboIds;
    }

    private List<String> optionComboNames;

    public List<String> getOptionComboNames()
    {
        return optionComboNames;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------
    public String execute()
        throws Exception
    {
        optionComboIds = new ArrayList<String>();
        optionComboNames = new ArrayList<String>();

        DataSet dataSet = dataSetService.getDataSet( dataSetId );
        List<DataElement> dataElementList = new ArrayList<DataElement>(dataSet.getDataElements());
        
        if(dataElementList!=null)
        {
            DataElement dataElement = (DataElement) dataElementList.iterator().next();
            DataElementCategoryCombo dataElementCategoryCombo = dataElement.getCategoryCombo();

            List<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
                dataElementCategoryCombo.getOptionCombos() );

            Iterator<DataElementCategoryOptionCombo> optionComboIterator = optionCombos.iterator();
            while ( optionComboIterator.hasNext() )
            {
                String optionComboName = "";
                DataElementCategoryOptionCombo decoc = (DataElementCategoryOptionCombo) optionComboIterator.next();
                List<DataElementCategoryOption> categoryOptions = new ArrayList<DataElementCategoryOption>( decoc
                    .getCategoryOptions() );
                Iterator<DataElementCategoryOption> categoryOptionsIterator = categoryOptions.iterator();
                while ( categoryOptionsIterator.hasNext() )
                {
                    DataElementCategoryOption categoryOption = categoryOptionsIterator.next();
                    optionComboName += categoryOption.getName() + " ";
                }
                //System.out.println( optionComboName );
                optionComboNames.add( optionComboName );
                optionComboIds.add( "" + decoc.getId() );
            }
        }

        return SUCCESS;
    }

}

  
