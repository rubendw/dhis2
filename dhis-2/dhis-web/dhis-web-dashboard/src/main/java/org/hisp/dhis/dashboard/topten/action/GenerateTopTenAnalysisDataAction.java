package org.hisp.dhis.dashboard.topten.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;

public class GenerateTopTenAnalysisDataAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private AggregationService aggregationService;

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;
    
    public void setDataElementCategoryOptionComboService(
        DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }
    
    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------
    private Period startPeriod;

    private Period endPeriod;
    
    private OrganisationUnit selectedOrgUnit;
    
    private Map<String, String> optionComboNames;

    private String reportTitle;
    
    public String getReportTitle()
    {
        return reportTitle;
    }
    
    private List<String> dataList;
    
    public List<String> getDataList()
    {
        return dataList;
    }

    private List<String> diseaseList;
    
    public List<String> getDiseaseList()
    {
        return diseaseList;
    }
    
    String chartTitle;

    public String getChartTitle()
    {
        return chartTitle;
    }

    String xAxis_Title;

    public String getXAxis_Title()
    {
        return xAxis_Title;
    }

    String yAxis_Title;

    public String getYAxis_Title()
    {
        return yAxis_Title;
    }

    private String[] series1;

    public String[] getSeries1()
    {
        return series1;
    }

    private String[] categories1;

    public String[] getCategories1()
    {
        return categories1;
    }

    private String[] series2;

    public String[] getSeries2()
    {
        return series2;
    }

    private String[] categories2;

    public String[] getCategories2()
    {
        return categories2;
    }
    
    Double data1[][];

    public Double[][] getData1()
    {
        return data1;
    }

    Double data2[][];

    public Double[][] getData2()
    {
        return data2;
    }


    /* Input Parameters */

    private List<String> availableDataSets;

    public void setAvailableDataSets( List<String> availableDataSets )
    {
        this.availableDataSets = availableDataSets;
    }

    private List<String> availableOptionCombos;

    public void setAvailableOptionCombos( List<String> availableOptionCombos )
    {
        this.availableOptionCombos = availableOptionCombos;
    }

    private int ouIDTB;

    public void setOuIDTB( int ouIDTB )
    {
        this.ouIDTB = ouIDTB;
    }

    private int sDateLB;

    public void setSDateLB( int dateLB )
    {
        sDateLB = dateLB;
    }

    private int eDateLB;

    public void setEDateLB( int dateLB )
    {
        eDateLB = dateLB;
    }

    public String execute()
        throws Exception
    {
        // Intialization
        optionComboNames = new HashMap<String,String>();
        dataList =  new ArrayList<String>();
        diseaseList = new ArrayList<String>();
        List<Double> completeValues = new ArrayList<Double>();
        
        
        // OrgUnit Related Info
        selectedOrgUnit = new OrganisationUnit();
        selectedOrgUnit = organisationUnitService.getOrganisationUnit( ouIDTB );
        chartTitle = "Facility : " + selectedOrgUnit.getShortName();
        reportTitle = "TOP TEN DISEASE REPORT <br> Facililty : "+selectedOrgUnit.getShortName();
        
        // Period Related Info
        startPeriod = periodStore.getPeriod( sDateLB );
        endPeriod = periodStore.getPeriod( eDateLB );
        chartTitle += "\nPeriod : "+startPeriod.getStartDate()+ " To "+endPeriod.getEndDate();
        reportTitle += "<br>Period : "+startPeriod.getStartDate()+ " To "+endPeriod.getEndDate()+"<br>";
        
        // DataSet Related Info
        int dataSetId = Integer.parseInt( (String) availableDataSets.iterator().next());
        DataSet dataSet = dataSetService.getDataSet( dataSetId );
        List<DataElement> dataElementList = new ArrayList<DataElement>(dataSet.getDataElements());
        List<DataElementCategoryOptionCombo> optionComboList = new ArrayList<DataElementCategoryOptionCombo>();
        reportTitle += "DataSet : "+dataSet.getName()+"<br>OptionCombo : ";
        
        double[] sortedValues = new double[dataElementList.size()];
        DataElement[] dataElementNames = new DataElement[dataElementList.size()];
                                           
        Iterator<String> optionComboIterator = availableOptionCombos.iterator();
        while(optionComboIterator.hasNext())
        {
            int optionComboId = Integer.parseInt( (String ) optionComboIterator.next());
            DataElementCategoryOptionCombo dcoc = dataElementCategoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId );
            
            String optionComboName = "";
            
            List<DataElementCategoryOption> categoryOptions = new ArrayList<DataElementCategoryOption>( dcoc
                .getCategoryOptions() );
            Iterator<DataElementCategoryOption> categoryOptionsIterator = categoryOptions.iterator();
            while ( categoryOptionsIterator.hasNext() )
            {
                DataElementCategoryOption categoryOption = categoryOptionsIterator.next();
                optionComboName += categoryOption.getName() + " ";
            }            
            
            reportTitle += optionComboName+", ";
            optionComboNames.put( ""+dcoc.getId(), optionComboName );                        
            optionComboList.add( dcoc );
        }

        int count = 0;
        
        
        Iterator<DataElement> dataElementListIterator =  dataElementList.iterator();
        while(dataElementListIterator.hasNext())
        {
            DataElement dataElement = (DataElement) dataElementListIterator.next();
            
            double aggDataValue = 0.0;
            
            Iterator<DataElementCategoryOptionCombo> optionComboListIterator = optionComboList.iterator();
            while ( optionComboListIterator.hasNext() )
            {
                DataElementCategoryOptionCombo decoc = (DataElementCategoryOptionCombo) optionComboListIterator.next();
                double tempValue = aggregationService.getAggregatedDataValue( dataElement, decoc, startPeriod.getStartDate(), endPeriod.getEndDate(), selectedOrgUnit );
                if ( tempValue != -1 ) aggDataValue += tempValue;
            }
            //System.out.println("DataElementid : "+dataElement.getId()+"AggValue : "+aggDataValue);
            for(int i=0; i < count ; i++)
            {
                if(aggDataValue > sortedValues[i])
                {
                    double temp = aggDataValue;
                    aggDataValue = sortedValues[i];
                    sortedValues[i] = temp;
                    
                    DataElement tempDE = dataElement;
                    dataElement = dataElementNames[i];
                    dataElementNames[i] = tempDE;                    
                }
            }
            
            sortedValues[count] = aggDataValue;
            dataElementNames[count] = dataElement;            
            count++;
            //completeValues.add( aggDataValue );            
        }        
        
        xAxis_Title = "Diseases";
        yAxis_Title = "Values";
        
        series1 = new String[1];
        series2 = new String[1];
        
        series1[0] = startPeriod.getStartDate()+" TO "+endPeriod.getEndDate();
        series2[0] = selectedOrgUnit.getName();                        
        
        if(sortedValues.length < 10)
        {
            data1 = new Double[1][sortedValues.length];
            data2 = new Double[1][sortedValues.length];
            categories1 = new String[sortedValues.length];
            categories2 = new String[sortedValues.length];
        }
        else
        {
            data1 = new Double[1][10];
            data2 = new Double[1][10];
            categories1 = new String[10];
            categories2 = new String[10];
        }
        
        int j=0;
        while( j<categories1.length)
        {
            data1[0][j] = sortedValues[j];
            data2[0][j] = 0.0;
            dataList.add( String.valueOf( sortedValues[j]) );
            
            categories1[j] = dataElementNames[j].getName();
            categories2[j] = dataElementNames[j].getName();
            
            diseaseList.add( dataElementNames[j].getName() );
            j++;
        }
        
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest req = (HttpServletRequest) ctx.get( ServletActionContext.HTTP_REQUEST );

        HttpSession session = req.getSession();
        session.setAttribute( "data1", data1 );
        session.setAttribute( "data2", data2 );
        session.setAttribute( "series1", series1 );
        session.setAttribute( "categories1", categories1 );
        session.setAttribute( "series2", series2 );
        session.setAttribute( "categories2", categories2 );
        session.setAttribute( "chartTitle", chartTitle );
        session.setAttribute( "xAxisTitle", xAxis_Title );
        session.setAttribute( "yAxisTitle", yAxis_Title );

        
        return SUCCESS;
    }

    

   

  

}
