package org.hisp.dhis.dashboard.sa.action;

/*
 * Copyright (c) 2004-2007, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.dashboard.util.DashBoardService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;

public class GenerateSurveyAnalysisDataAction
    implements Action
{

    /* Dependencies */
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DashBoardService dashBoardService;
    
    public void setDashBoardService( DashBoardService dashBoardService )
    {
        this.dashBoardService = dashBoardService;
    }

    private AggregationService aggregationService;

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    /* Parameters */
    private Period startPeriod;

    private Period endPeriod;

    private OrganisationUnit selectedOrgUnit;

    private List<Period> selectedPeriodList;
    
    private List<Double> targetList;

    public List<Double> getTargetList()
    {
        return targetList;
    }

    private Integer indicatorListSize;
    public Integer getIndicatorListSize()
    {
        return indicatorListSize;
    }
    
    
    private List<Object> selectedServiceList;

    public List<Object> getSelectedServiceList()
    {
        return selectedServiceList;
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

    String chartTitle = "Service : ";

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

    List<String> numeratorDEList;

    public List<String> getNumeratorDEList()
    {
        return numeratorDEList;
    }

    List<String> denominatorDEList;

    public List<String> getDenominatorDEList()
    {
        return denominatorDEList;
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

    List<List<Double>> dataList;

    public List<List<Double>> getDataList()
    {
        return dataList;
    }

    List<String> xseriesList;

    public List<String> getXseriesList()
    {
        return xseriesList;
    }

    List<String> yseriesList;

    public List<String> getYseriesList()
    {
        return yseriesList;
    }

    /* Input Parameters */
    private List<String> selectedDataElements;

    public void setSelectedDataElements( List<String> selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }

    private List<String> selectedIndicators;

    public void setSelectedIndicators( List<String> selectedIndicators )
    {
        this.selectedIndicators = selectedIndicators;
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

    private String riRadio;

    public void setRiRadio( String riRadio )
    {
        this.riRadio = riRadio;
    }

    public String getRiRadio()
    {
        return riRadio;
    }
    
    private String selectedButton;

    public String getSelectedButton()
    {
        return selectedButton;
    }

    public void setSelectedButton( String selectedButton )
    {
        this.selectedButton = selectedButton;
    }

    private List<String> orgUnitListCB;

    public void setOrgUnitListCB( List<String> orgUnitListCB )
    {
        this.orgUnitListCB = orgUnitListCB;
    }

    private String facilityLB;

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    public String execute()
        throws Exception
    {
        dataList = new ArrayList<List<Double>>();
        targetList = new ArrayList<Double>();

        // OrgUnit Related Info
        selectedOrgUnit = new OrganisationUnit();
        selectedOrgUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitListCB.get( 0 ) ) );

        // Service Related Info
        int count1 = 0;
        numeratorDEList = new ArrayList<String>();
        denominatorDEList = new ArrayList<String>();
        selectedServiceList = new ArrayList<Object>();
        xseriesList = new ArrayList<String>();
        yseriesList = new ArrayList<String>();

        
        if ( selectedIndicators == null || selectedIndicators.get( 0 ).equals( "EMPTY_PLACEHOLDER_VALUE" ))
        {
            indicatorListSize = new Integer(0);
        }
        else
        {            
            indicatorListSize = new Integer(selectedIndicators.size());
            System.out.println("First Indicator : "+ selectedIndicators.get( 0 ));
            Iterator indicatorIterator = selectedIndicators.iterator();
            while ( indicatorIterator.hasNext() )
            {
                int serviceID = Integer.parseInt( (String) indicatorIterator.next() );
                Indicator indicator = indicatorService.getIndicator( serviceID );
                selectedServiceList.add( indicator );
                chartTitle += indicator.getName() + ", ";
                numeratorDEList.add( getIndicatorDataElements( indicator.getNumerator() ) );
                denominatorDEList.add( getIndicatorDataElements( indicator.getDenominator() ) );
                count1++;
            } // while loop end
        }
        
        if(selectedDataElements == null || selectedDataElements.get( 0 ).equals( "EMPTY_PLACEHOLDER_VALUE" ))
        {
            ;
        }
        else
        {
            Iterator deIterator = selectedDataElements.iterator();
            while ( deIterator.hasNext() )
            {
                int serviceID = Integer.parseInt( (String) deIterator.next() );
                DataElement dataElement = dataElementService.getDataElement( serviceID );
                selectedServiceList.add( dataElement );
                chartTitle += dataElement.getShortName() + ", ";
            }
        }


        // Period Related Info
        startPeriod = periodStore.getPeriod( sDateLB );
        endPeriod = periodStore.getPeriod( eDateLB );

        /*
        int monthlyPeriodTypeId = 0;
        Collection periodTypes = periodStore.getAllPeriodTypes();
        PeriodType monthlyPeriodType = null;
        Iterator iter = periodTypes.iterator();
        while ( iter.hasNext() )
        {
            PeriodType periodType = (PeriodType) iter.next();
            if ( periodType.getName().toLowerCase().trim().equals( "monthly" ) )
            {
                monthlyPeriodType = periodType;
                break;
            }
        }
        if ( monthlyPeriodType != null )
        {
            System.out.println( "Monthly Period id : " + monthlyPeriodType.getId() );
            monthlyPeriodTypeId = monthlyPeriodType.getId();
        }
        else
        {
            System.out.println( "Monthly Period Type is NULL" );
        }
        */
        selectedPeriodList = dashBoardService.getMonthlyPeriods( startPeriod.getStartDate(), endPeriod.getEndDate() );

        if ( facilityLB.equals( "random" ) )
        {
            chartTitle += "\n Facility : --- \nPeriod : " + startPeriod.getStartDate() + " To "
                + endPeriod.getEndDate();
        }
        else
        {
            chartTitle += "\n Facility : " + selectedOrgUnit.getShortName() + "\nPeriod : "
                + startPeriod.getStartDate() + " To " + endPeriod.getEndDate();
        }

        data1 = getServiceValuesByFacility();
        xAxis_Title = "Facilities";
        yAxis_Title = "Value";
        
        count1 = 0;
        while ( count1 != categories1.length )
        {
            xseriesList.add( categories1[count1] );
            count1++;
        }
        // if(selectedButton.equals("ViewSummary")) return "ViewSummary";

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
    }// execute end


    /*
     * Returns the period aggregated values for the children of selected orgunit
     * and list of selected services for ex:- PeriodList is
     * jan2006,feb2006,mar2006 ServiceList is service1 and service2 then it
     * returns the period aggregated values for service1 - child1, child2,
     * child3 service2 - child2, child2, child3 for the selected orgunit
     */
    public Double[][] getServiceValuesByFacility()
    {
        int countForServiceList = 0;
        int countForChildOrgUnitList = 0;
        int noOfPeriods = selectedPeriodList.size();

        Indicator ind = new Indicator();
        DataElement dElement = new DataElement();

        List<OrganisationUnit> childOrgUnitList = new ArrayList<OrganisationUnit>();
        if ( facilityLB.equals( "children" ) )
        {
            childOrgUnitList = dashBoardService.getAllChildren( selectedOrgUnit );
        }
        else
        {
            Iterator orgUnitIterator = orgUnitListCB.iterator();
            while ( orgUnitIterator.hasNext() )
            {
                OrganisationUnit o = organisationUnitService.getOrganisationUnit( Integer
                    .parseInt( (String) orgUnitIterator.next() ) );
                childOrgUnitList.add( o );
            }
        }
        Iterator serviceListIterator = selectedServiceList.iterator();
        Double[][] serviceValues = new Double[selectedServiceList.size()][childOrgUnitList.size()];

        data2 = new Double[selectedServiceList.size()][childOrgUnitList.size()];
        series1 = new String[selectedServiceList.size()];
        series2 = new String[selectedServiceList.size()];
        categories1 = new String[childOrgUnitList.size()];
        categories2 = new String[childOrgUnitList.size()];
        
        while ( serviceListIterator.hasNext() )
        {
            List<Double> dataValues = new ArrayList<Double>();
            if ( countForServiceList < indicatorListSize.intValue() )
            {
                riRadio = "indicatorsRadio";
                ind = (Indicator) serviceListIterator.next();
                //System.out.println( ind.getName() );
                series1[countForServiceList] = ind.getName();
                series2[countForServiceList] = " ";
                yseriesList.add( ind.getName() );
            }
            else
            {
                riRadio = "dataElementsRadio";
                dElement = (DataElement) serviceListIterator.next();
                series1[countForServiceList] = dElement.getAlternativeName();
                series2[countForServiceList] = " ";
                yseriesList.add( dElement.getName() );
            }
            
            Iterator childOrgUnitListIterator = childOrgUnitList.iterator();
            countForChildOrgUnitList = 0;
            while ( childOrgUnitListIterator.hasNext() )
            {
                OrganisationUnit childOrgUnit = (OrganisationUnit) childOrgUnitListIterator.next();
                if ( riRadio.equals( "indicatorsRadio" ) )
                {
                    serviceValues[countForServiceList][countForChildOrgUnitList] = aggregationService
                        .getAggregatedIndicatorValue( ind, startPeriod.getStartDate(), endPeriod.getEndDate(),
                            childOrgUnit )
                        / noOfPeriods;
                }
                else
                {
                    double aggDataValue = 0.0;
                    DataElementCategoryCombo dataElementCategoryCombo = dElement.getCategoryCombo();

                    List<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
                        dataElementCategoryCombo.getOptionCombos() );

                    Iterator<DataElementCategoryOptionCombo> optionComboIterator = optionCombos.iterator();
                    while ( optionComboIterator.hasNext() )
                    {                        
                        DataElementCategoryOptionCombo decoc = (DataElementCategoryOptionCombo) optionComboIterator.next();

                        aggDataValue = aggregationService.getAggregatedDataValue( dElement, decoc, startPeriod.getStartDate(), endPeriod.getEndDate(), childOrgUnit );
                        if(aggDataValue == -1) aggDataValue = 0.0;
                        serviceValues[countForServiceList][countForChildOrgUnitList] += aggDataValue;
                    }                    
                }
                serviceValues[countForServiceList][countForChildOrgUnitList] = Math
                    .round( serviceValues[countForServiceList][countForChildOrgUnitList] * Math.pow( 10, 2 ) )
                    / Math.pow( 10, 2 );
                if ( serviceValues[countForServiceList][countForChildOrgUnitList] == -1 )
                    serviceValues[countForServiceList][countForChildOrgUnitList] = 0.0;
                categories1[countForChildOrgUnitList] = childOrgUnit.getShortName();
                categories2[countForChildOrgUnitList] = childOrgUnit.getShortName();
                data2[countForServiceList][countForChildOrgUnitList] = 0.0;
                dataValues.add( serviceValues[countForServiceList][countForChildOrgUnitList] );
                countForChildOrgUnitList++;
            }// childOrgUnitList loop end
            dataList.add( dataValues );
            countForServiceList++;
        } // serviceList loop end
        return serviceValues;
    }// getServiceValuesByFacility end

    /*
     * Returns a String which contains all the DataElement Names of Numerator or
     * Denominator
     */
    public String getIndicatorDataElements( String tempSD )
    {
        char[] tempCD = tempSD.toCharArray();
        String deNames = "";
        String temp1 = "";
        int flag = 0;
        try
        {
            for ( int i = 0; i < tempCD.length; i++ )
            {
                if ( tempCD[i] == '[' )
                {
                    flag = 1;
                    temp1 = "";
                }
                else if ( tempCD[i] == ']' )
                {
                    flag = 2;
                    int itemp = Integer.parseInt( temp1 );
                    DataElement de = dataElementService.getDataElement( itemp );
                    deNames += de.getAlternativeName() + ",<br>";
                }
                else if ( flag == 1 )
                    temp1 += tempCD[i];
            }// for end
        } // try block end
        catch ( Exception e )
        {
            return null;
        }

        return deNames;
    }// end function getIndicatorDataElements

    
    
}// class end
