package org.hisp.dhis.dashboard.ga.action.charts;

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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.dashboard.util.DBConnection;
import org.hisp.dhis.dashboard.util.DashBoardService;
import org.hisp.dhis.dashboard.util.SurveyData;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitShortNameComparator;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;

public class GenerateChartDataAction
    implements Action
{

    /* Dependencies */

    private TargetService targetService;

    public void setTargetService( TargetService targetService )
    {
        this.targetService = targetService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService(
        DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

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

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
    }

    private Comparator<Indicator> indicatorComparator;

    public void setIndicatorComparator( Comparator<Indicator> indicatorComparator )
    {
        this.indicatorComparator = indicatorComparator;
    }

    // -------------------------------------------------------------------------
    // DisplayPropertyHandler
    // -------------------------------------------------------------------------

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }

    // --------------------------------------------------------------------------
    // Parameters
    // --------------------------------------------------------------------------
    private HttpSession session;
    
    public HttpSession getSession()
    {
        return session;
    }

    
    private Period startPeriod;

    private Period endPeriod;

    private OrganisationUnit selectedOrgUnit;

    private OrganisationUnitGroup selectedOrgUnitGroup;

    private List<Period> selectedPeriodList;

    private Map<String, Double> targetValues;

    private List<Double> targetList;

    public List<Double> getTargetList()
    {
        return targetList;
    }

    private List<DataElementCategoryOptionCombo> selectedOptionComboList;

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

    List<List<String>> dataList;

    public List<List<String>> getDataList()
    {
        return dataList;
    }

    List<List<String>> numList;
    List<List<String>> denList;
    
    
    
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

    Map<Indicator, List<Target>> indicatorTargetList;

    public Map<Indicator, List<Target>> getIndicatorTargetList()
    {
        return indicatorTargetList;
    }

    Map<Indicator, List<SurveyData>> indicatorSurveyList;
    
    
    /* Input Parameters */
    private String ougSetCB;

    public void setOugSetCB( String ougSetCB )
    {
        this.ougSetCB = ougSetCB;
    }

    private String deSelection;

    public void setDeSelection( String deSelection )
    {
        this.deSelection = deSelection;
    }

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

    private String categoryLB;

    public String getCategoryLB()
    {
        return categoryLB;
    }

    public void setCategoryLB( String categoryLB )
    {
        this.categoryLB = categoryLB;
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

    public List<List<String>> getNumList()
    {
        return numList;
    }

    public List<List<String>> getDenList()
    {
        return denList;
    }

    public Map<Indicator, List<SurveyData>> getIndicatorSurveyList()
    {
        return indicatorSurveyList;
    }

    public String execute()
        throws Exception
    {
        statementManager.initialise();

        indicatorTargetList = new HashMap<Indicator, List<Target>>();
        indicatorSurveyList = new HashMap<Indicator, List<SurveyData>>();
        dataList = new ArrayList<List<String>>();
        numList = new ArrayList<List<String>>();
        denList = new ArrayList<List<String>>();
        targetList = new ArrayList<Double>();
        selectedOptionComboList = new ArrayList<DataElementCategoryOptionCombo>();

        // OrgUnit Related Info
        if ( ougSetCB == null )
        {
            selectedOrgUnit = new OrganisationUnit();
            selectedOrgUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitListCB.get( 0 ) ) );
        }
        else
        {
            selectedOrgUnitGroup = new OrganisationUnitGroup();
            selectedOrgUnitGroup = organisationUnitGroupService.getOrganisationUnitGroup( Integer
                .parseInt( orgUnitListCB.get( 0 ) ) );
        }

        // Service Related Info
        int count1 = 0;
        numeratorDEList = new ArrayList<String>();
        denominatorDEList = new ArrayList<String>();
        // selectedServiceList = new ArrayList<Object>();
        xseriesList = new ArrayList<String>();
        yseriesList = new ArrayList<String>();

        List<Indicator> li1 = new ArrayList<Indicator>();

        if ( riRadio.equals( "indicatorsRadio" ) )
        {
            Iterator indicatorIterator = selectedIndicators.iterator();
            while ( indicatorIterator.hasNext() )
            {
                int serviceID = Integer.parseInt( (String) indicatorIterator.next() );
                Indicator indicator = indicatorService.getIndicator( serviceID );
                // selectedServiceList.add( indicator );
                li1.add( indicator );
                chartTitle += indicator.getName() + ", ";

                // numeratorDEList.add( getIndicatorDataElements(
                // indicator.getNumerator() ) );
                // denominatorDEList.add( getIndicatorDataElements(
                // indicator.getDenominator() ) );
                numeratorDEList.add( indicator.getNumeratorDescription() );
                denominatorDEList.add( indicator.getDenominatorDescription() );
                count1++;
            } // while loop end
            Collections.sort( li1, indicatorComparator );
            selectedServiceList = new ArrayList<Object>( li1 );
        }
        else
        {
            List<DataElement> li2 = new ArrayList<DataElement>();
            if ( deSelection == null )
            {
                System.out.println( "deOptionValue is null" );
                return null;
            }
            else
            {
                System.out.println( "deOptionValue : " + deSelection );
            }
            if ( deSelection.equalsIgnoreCase( "optioncombo" ) )
            {
                Iterator deIterator = selectedDataElements.iterator();
                while ( deIterator.hasNext() )
                {
                    String serviceId = (String) deIterator.next();
                    String partsOfServiceId[] = serviceId.split( ":" );
                    int dataElementId = Integer.parseInt( partsOfServiceId[0] );
                    DataElement dataElement = dataElementService.getDataElement( dataElementId );
                    // selectedServiceList.add( dataElement );
                    li2.add( dataElement );
                    int optionComboId = Integer.parseInt( partsOfServiceId[1] );
                    DataElementCategoryOptionCombo decoc = dataElementCategoryOptionComboService
                        .getDataElementCategoryOptionCombo( optionComboId );
                    selectedOptionComboList.add( decoc );
                    chartTitle += dataElement.getName() + " : "
                        + dataElementCategoryOptionComboService.getOptionNames( decoc ) + ", ";
                }
            }
            else
            {
                Iterator deIterator = selectedDataElements.iterator();
                while ( deIterator.hasNext() )
                {
                    int serviceID = Integer.parseInt( (String) deIterator.next() );
                    DataElement dataElement = dataElementService.getDataElement( serviceID );
                    // selectedServiceList.add( dataElement );
                    li2.add( dataElement );
                    chartTitle += dataElement.getName() + ", ";
                }
            }
            Collections.sort( li2, dataElementComparator );
            selectedServiceList = new ArrayList<Object>( li2 );
        }

        // TargetValues
        targetValues = new HashMap<String, Double>();
        /*
         * if(riRadio.equals( "indicatorsRadio" )) { getTargetValues(); }
         */

        /* Hack to Intialize Indicator Targets to Zeros */
        if ( riRadio.equals( "indicatorsRadio" ) )
        {
            Iterator serviceIterator = selectedServiceList.iterator();
            while ( serviceIterator.hasNext() )
            {
                Indicator indicator = (Indicator) serviceIterator.next();
                targetValues.put( "" + indicator.getId(), new Double( 0.0 ) );
                targetList.add( targetValues.get( "" + indicator.getId() ) );
            }
        }

        // Period Related Info
        startPeriod = periodStore.getPeriod( sDateLB );
        endPeriod = periodStore.getPeriod( eDateLB );

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
            // System.out.println( "Monthly Period id : " +
            // monthlyPeriodType.getId() );
            monthlyPeriodTypeId = monthlyPeriodType.getId();
        }
        /*
         * else { System.out.println( "Monthly Period Type is NULL" ); }
         */

        selectedPeriodList = dashBoardService.getMonthlyPeriods( startPeriod.getStartDate(), endPeriod.getEndDate() );

        if ( categoryLB.equals( "facility" ) && facilityLB.equals( "random" ) )
        {
            chartTitle += "\n OrganisationUnit : --- \nPeriod : " + startPeriod.getStartDate() + " To "
                + endPeriod.getEndDate();
        }
        else
        {
            if ( ougSetCB == null )
            {
                chartTitle += "\n OrganisationUnit : " + selectedOrgUnit.getShortName() + "\nPeriod : "
                    + startPeriod.getStartDate() + " To " + endPeriod.getEndDate();
            }
            else
            {
                chartTitle += "\n OrganisationUnit : --- \nPeriod : "
                    + startPeriod.getStartDate() + " To " + endPeriod.getEndDate();
            }
        }

        if ( categoryLB.equals( "period" ) )
        {
            data1 = getServiceValuesByPeriod();
            xAxis_Title = "Time Line";
        }
        else
        {
            data1 = getServiceValuesByFacility();
            xAxis_Title = "Facilities";
        }

        if ( riRadio.equals( "indicatorsRadio" ) )
        {
            yAxis_Title = "Percentage/Rate";
        }
        else
        {
            yAxis_Title = "Value";
        }

        count1 = 0;
        while ( count1 != categories1.length )
        {
            xseriesList.add( categories1[count1] );
            count1++;
        }
        
        // if(selectedButton.equals("ViewSummary")) return "ViewSummary";

        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest req = (HttpServletRequest) ctx.get( ServletActionContext.HTTP_REQUEST );

        session = req.getSession();
        session.setAttribute( "data1", data1 );
        session.setAttribute( "data2", data2 );
        session.setAttribute( "series1", series1 );
        session.setAttribute( "categories1", categories1 );
        session.setAttribute( "series2", series2 );
        session.setAttribute( "categories2", categories2 );
        session.setAttribute( "chartTitle", chartTitle );
        session.setAttribute( "xAxisTitle", xAxis_Title );
        session.setAttribute( "yAxisTitle", yAxis_Title );

        statementManager.destroy();

        return SUCCESS;
    }// execute end

    /*
     * Returns the values for selected services by period wise for ex:- the
     * periods are jan-2006,feb-2006,mar-2006 and the services are service1 and
     * service2 then it returns the values for service1 -
     * jan2006,feb2006,mar2006 and service2 - jan2006,feb2006,mar2006 for the
     * selected orgunit
     */
    public Double[][] getServiceValuesByPeriod()
    {
        Double[][] serviceValues = new Double[selectedServiceList.size()][selectedPeriodList.size()];
        data2 = new Double[selectedServiceList.size()][selectedPeriodList.size()];

        int countForServiceList = 0;
        int countForPeriodList = 0;
        int targetFlag = 0;
        Indicator ind = new Indicator();
        DataElement dElement = new DataElement();
        DataElementCategoryOptionCombo decoc = new DataElementCategoryOptionCombo();
        Period p = new Period();

        /*
         * if ( selectedServiceList == null ) System.out.println( "Service List
         * is empty" ); if ( selectedPeriodList == null ) System.out.println(
         * "Period List is Empty" );
         */
        series1 = new String[selectedServiceList.size()];
        series2 = new String[selectedServiceList.size()];
        categories1 = new String[selectedPeriodList.size()];
        categories2 = new String[selectedPeriodList.size()];
        Iterator serviceListIterator = selectedServiceList.iterator();
        while ( serviceListIterator.hasNext() )
        {
            List<String> numValues = new ArrayList<String>();
            List<String> denValues = new ArrayList<String>();
            List<String> dataValues = new ArrayList<String>();
            List<Target> targetList = new ArrayList<Target>();
            List<SurveyData> surveyList = new ArrayList<SurveyData>();

            if ( riRadio.equals( "indicatorsRadio" ) )
            {
                ind = (Indicator) serviceListIterator.next();
                // System.out.println( ind.getName() );
                series1[countForServiceList] = ind.getName();
                series2[countForServiceList] = ind.getName() + "(Target)";
                yseriesList.add( ind.getName() );

                List<Period> tempPeriodList = new ArrayList<Period>( periodStore.getIntersectingPeriods( startPeriod
                    .getStartDate(), endPeriod.getEndDate() ) );
                Iterator tempPeriodListIterator = tempPeriodList.iterator();
                while ( tempPeriodListIterator.hasNext() )
                {
                    Period tempPeriod = (Period) tempPeriodListIterator.next();
                    if ( ougSetCB == null )
                    {
                        List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( ind, tempPeriod,
                            selectedOrgUnit ) );
                        List<SurveyData> tempSurvey = new ArrayList<SurveyData>( getSurveyList(ind, selectedOrgUnit));
                        surveyList = new ArrayList<SurveyData>();
                        if ( tempTarget != null )
                            targetList.addAll( tempTarget );
                        if(tempSurvey !=null && tempSurvey.size() > 0)
                            surveyList.addAll( tempSurvey );
                    }
                    else
                    {
                        List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( ind, tempPeriod,
                            selectedOrgUnitGroup.getMembers().iterator().next() ) );
                        if ( tempTarget != null )
                            targetList.addAll( tempTarget );
                        List<SurveyData> tempSurvey = new ArrayList<SurveyData>( getSurveyList(ind, selectedOrgUnitGroup.getMembers().iterator().next()));
                        surveyList = new ArrayList<SurveyData>();
                        if(tempSurvey !=null && tempSurvey.size() > 0)
                            surveyList.addAll( tempSurvey );                        
                    }

                }
                indicatorTargetList.put( ind, targetList );
                indicatorSurveyList.put( ind, surveyList );
            }
            else
            {
                dElement = (DataElement) serviceListIterator.next();
                if ( deSelection.equalsIgnoreCase( "optioncombo" ) )
                {
                    decoc = selectedOptionComboList.get( countForServiceList );
                    /*if ( dElement.getAlternativeName() != null )
                    {
                        series1[countForServiceList] = dElement.getAlternativeName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc );
                        series2[countForServiceList] = dElement.getAlternativeName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc ) + " (Target)";
                    }
                    else*/
                    {
                        series1[countForServiceList] = dElement.getName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc );
                        series2[countForServiceList] = dElement.getName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc ) + " (Target)";
                    }
                    yseriesList.add( dElement.getName() + " : "
                        + dataElementCategoryOptionComboService.getOptionNames( decoc ) );
                }
                else
                {
                    /*
                    if ( dElement.getAlternativeName() != null )
                    {
                        series1[countForServiceList] = dElement.getAlternativeName();
                        series2[countForServiceList] = dElement.getAlternativeName() + " (Target)";
                    }
                    else*/
                    {
                        series1[countForServiceList] = dElement.getName();
                        series2[countForServiceList] = dElement.getName() + " (Target)";
                    }
                    yseriesList.add( dElement.getName() );
                }
            }
            Iterator periodListIterator = selectedPeriodList.iterator();
            countForPeriodList = 0;
            while ( periodListIterator.hasNext() )
            {
                double numVal = 0.0;
                double denVal = 0.0;

                p = (Period) periodListIterator.next();
                /*
                 * if ( p == null ) System.out.println( "In Period While Loop :
                 * but it is Empty" ); else System.out.println( "In Period While
                 * Loop : but it is not Empty" );
                 */
                if ( riRadio.equals( "indicatorsRadio" ) )
                {

                    if ( ougSetCB == null )
                    {
                        serviceValues[countForServiceList][countForPeriodList] = aggregationService
                            .getAggregatedIndicatorValue( ind, p.getStartDate(), p.getEndDate(), selectedOrgUnit );
                        numVal = aggregationService.getAggregatedNumeratorValue( ind, p.getStartDate(), p.getEndDate(), selectedOrgUnit );
                        denVal = aggregationService.getAggregatedDenominatorValue( ind, p.getStartDate(), p.getEndDate(), selectedOrgUnit );
                    }
                    else
                    {
                        double aggValue = 0.0;
                        List<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>( selectedOrgUnitGroup
                            .getMembers() );
                        Iterator<OrganisationUnit> orgUnitsIterator = orgUnits.iterator();
                        while ( orgUnitsIterator.hasNext() )
                        {
                            OrganisationUnit ou = (OrganisationUnit) orgUnitsIterator.next();
                            double tempd = aggregationService.getAggregatedIndicatorValue( ind, p.getStartDate(), p
                                .getEndDate(), ou );
                            double tempnum = aggregationService.getAggregatedNumeratorValue( ind, p.getStartDate(), p
                                .getEndDate(), ou );
                            double tempden = aggregationService.getAggregatedDenominatorValue( ind, p.getStartDate(), p
                                .getEndDate(), ou );
                            if(tempd == -1) tempd = 0.0;
                            if(tempnum == -1) tempnum = 0.0;
                            if(tempden == -1) tempden = 0.0;
                            
                            aggValue += tempd;
                            numVal += tempnum;
                            denVal += tempden;
                        }
                        serviceValues[countForServiceList][countForPeriodList] = aggValue / orgUnits.size();
                        numVal /= orgUnits.size();
                        denVal /= orgUnits.size();
                    }
                }
                else
                {
                    if ( deSelection.equalsIgnoreCase( "optioncombo" ) )
                    {
                        if ( ougSetCB == null )
                        {
                            serviceValues[countForServiceList][countForPeriodList] = aggregationService
                                .getAggregatedDataValue( dElement, decoc, p.getStartDate(), p.getEndDate(),
                                    selectedOrgUnit );
                        }
                        else
                        {
                            double aggValue = 0.0;
                            List<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>( selectedOrgUnitGroup
                                .getMembers() );
                            Iterator<OrganisationUnit> orgUnitsIterator = orgUnits.iterator();
                            while ( orgUnitsIterator.hasNext() )
                            {
                                OrganisationUnit ou = (OrganisationUnit) orgUnitsIterator.next();
                                double tempd = aggregationService.getAggregatedDataValue( dElement, decoc, p
                                    .getStartDate(), p.getEndDate(), ou );
                                if ( tempd == -1 )
                                    tempd = 0.0;
                                aggValue += tempd;
                            }
                            serviceValues[countForServiceList][countForPeriodList] = aggValue;
                        }
                    }
                    else
                    {
                        double aggDataValue = 0.0;
                        serviceValues[countForServiceList][countForPeriodList] = 0.0;
                        DataElementCategoryCombo dataElementCategoryCombo = dElement.getCategoryCombo();

                        List<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
                            dataElementCategoryCombo.getOptionCombos() );

                        Iterator<DataElementCategoryOptionCombo> optionComboIterator = optionCombos.iterator();
                        while ( optionComboIterator.hasNext() )
                        {
                            DataElementCategoryOptionCombo decoc1 = (DataElementCategoryOptionCombo) optionComboIterator
                                .next();

                            if ( ougSetCB == null )
                            {
                                aggDataValue = aggregationService.getAggregatedDataValue( dElement, decoc1, p
                                    .getStartDate(), p.getEndDate(), selectedOrgUnit );
                            }
                            else
                            {
                                List<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>( selectedOrgUnitGroup
                                    .getMembers() );
                                Iterator<OrganisationUnit> orgUnitsIterator = orgUnits.iterator();
                                while ( orgUnitsIterator.hasNext() )
                                {
                                    OrganisationUnit ou = (OrganisationUnit) orgUnitsIterator.next();
                                    double tempd = aggregationService.getAggregatedDataValue( dElement, decoc1, p
                                        .getStartDate(), p.getEndDate(), ou );
                                    if ( tempd == -1 )
                                        tempd = 0.0;
                                    aggDataValue += tempd;
                                }
                            }
                            if ( aggDataValue == -1 )
                                aggDataValue = 0.0;
                            serviceValues[countForServiceList][countForPeriodList] += aggDataValue;
                        }
                    }
                }
                serviceValues[countForServiceList][countForPeriodList] = Math
                    .round( serviceValues[countForServiceList][countForPeriodList] * Math.pow( 10, 2 ) )
                    / Math.pow( 10, 2 );
                numVal = Math.round( numVal * Math.pow( 10, 2 ) ) / Math.pow( 10, 2 );
                denVal = Math.round( denVal * Math.pow( 10, 2 ) ) / Math.pow( 10, 2 );
                
                if ( serviceValues[countForServiceList][countForPeriodList] == -1 )
                    serviceValues[countForServiceList][countForPeriodList] = 0.0;
                categories1[countForPeriodList] = p.getStartDate().toString();
                categories2[countForPeriodList] = p.getStartDate().toString();

                if ( riRadio.equals( "indicatorsRadio" ) )
                    data2[countForServiceList][countForPeriodList] = (targetValues.get( "" + ind.getId() ))
                        .doubleValue();
                else
                    data2[countForServiceList][countForPeriodList] = 0.0;

                if ( riRadio.equals( "dataElementsRadio" ) )
                {
                    if ( dElement.getType().equalsIgnoreCase( "int" ) )
                        dataValues.add( "" + serviceValues[countForServiceList][countForPeriodList] );
                    else
                        dataValues.add( " " );
                }
                else
                {
                    dataValues.add( "" + serviceValues[countForServiceList][countForPeriodList] );
                    numValues.add(""+ numVal);
                    denValues.add(""+ denVal);
                }

                countForPeriodList++;
            }// periodList loop end
            dataList.add( dataValues );
            numList.add( numValues );
            denList.add( denValues );
            countForServiceList++;
        } // serviceList loop end
        return serviceValues;
    }// getServiceValues method end

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
        // int noOfPeriods = selectedPeriodList.size();
        int noOfPeriods = 1;

        Indicator ind = new Indicator();
        DataElement dElement = new DataElement();
        DataElementCategoryOptionCombo decoc = new DataElementCategoryOptionCombo();

        List<Object> childOrgUnitList = new ArrayList<Object>();
        if ( facilityLB.equals( "children" ) )
        {
            if ( ougSetCB == null )
            {
                childOrgUnitList = new ArrayList<Object>( dashBoardService.getAllChildren( selectedOrgUnit ) );                
            }
            else
            {
                //childOrgUnitList = new ArrayList<Object>( selectedOrgUnitGroup.getMembers() );
                childOrgUnitList = new ArrayList<Object>();
                Iterator orgUnitGroupIte = orgUnitListCB.iterator();
                while ( orgUnitGroupIte.hasNext() )
                {
                    OrganisationUnitGroup oug = organisationUnitGroupService.getOrganisationUnitGroup( Integer
                        .parseInt( (String) orgUnitGroupIte.next() ) );
                    List<OrganisationUnit> tempOUList = new ArrayList<OrganisationUnit>(oug.getMembers());
                    Collections.sort( tempOUList, new OrganisationUnitShortNameComparator() );
                    childOrgUnitList.addAll( tempOUList );
                }
            }
        }
        else
        {
            if ( ougSetCB == null )
            {
                Iterator orgUnitIterator = orgUnitListCB.iterator();
                while ( orgUnitIterator.hasNext() )
                {
                    OrganisationUnit o = organisationUnitService.getOrganisationUnit( Integer
                        .parseInt( (String) orgUnitIterator.next() ) );
                    childOrgUnitList.add( o );
                }
            }
            else
            {
                Iterator orgUnitGroupIte = orgUnitListCB.iterator();
                while ( orgUnitGroupIte.hasNext() )
                {
                    OrganisationUnitGroup oug = organisationUnitGroupService.getOrganisationUnitGroup( Integer
                        .parseInt( (String) orgUnitGroupIte.next() ) );
                    childOrgUnitList.add( oug );
                }
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
            int noOfChildren = 1;
            
            List<String> numValues = new ArrayList<String>();
            List<String> denValues = new ArrayList<String>();
            List<String> dataValues = new ArrayList<String>();
            List<Target> targetList = new ArrayList<Target>();
            List<SurveyData> surveyList = new ArrayList<SurveyData>();
            if ( riRadio.equals( "indicatorsRadio" ) )
            {
                ind = (Indicator) serviceListIterator.next();
                // System.out.println( ind.getName() );
                series1[countForServiceList] = ind.getName();
                series2[countForServiceList] = ind.getName() + "(Target)";
                yseriesList.add( ind.getName() );

                List<Period> tempPeriodList = new ArrayList<Period>( periodStore.getIntersectingPeriods( startPeriod
                    .getStartDate(), endPeriod.getEndDate() ) );
                Iterator tempPeriodListIterator = tempPeriodList.iterator();
                while ( tempPeriodListIterator.hasNext() )
                {
                    Period tempPeriod = (Period) tempPeriodListIterator.next();
                    if ( ougSetCB == null )
                    {
                        if ( facilityLB.equals( "children" ) )
                        {
                            List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( ind, tempPeriod,
                                selectedOrgUnit ) );
                            if ( tempTarget != null )
                                targetList.addAll( tempTarget );
                            List<SurveyData> tempSurvey = new ArrayList<SurveyData>( getSurveyList(ind, selectedOrgUnit));
                            surveyList = new ArrayList<SurveyData>();
                            if(tempSurvey !=null && tempSurvey.size() > 0)
                                surveyList.addAll( tempSurvey );
                        }
                        else
                        {
                            List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( ind, tempPeriod,
                                (OrganisationUnit) childOrgUnitList.iterator().next() ) );
                            if ( tempTarget != null )
                                targetList.addAll( tempTarget );
                            List<SurveyData> tempSurvey = new ArrayList<SurveyData>( getSurveyList(ind, (OrganisationUnit) childOrgUnitList.iterator().next()));
                            surveyList = new ArrayList<SurveyData>();
                            if(tempSurvey !=null && tempSurvey.size() > 0)
                                surveyList.addAll( tempSurvey );
                        }
                    }
                    else
                    {
                        if ( facilityLB.equals( "children" ) )
                        {
                            List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( ind, tempPeriod,
                                selectedOrgUnitGroup.getMembers().iterator().next() ) );
                            if ( tempTarget != null )
                                targetList.addAll( tempTarget );
                            
                            List<SurveyData> tempSurvey = new ArrayList<SurveyData>( getSurveyList(ind, selectedOrgUnitGroup.getMembers().iterator().next()));
                            surveyList = new ArrayList<SurveyData>();
                            if(tempSurvey !=null && tempSurvey.size() > 0)
                                surveyList.addAll( tempSurvey );

                        }
                        else
                        {
                            OrganisationUnitGroup oug = (OrganisationUnitGroup) childOrgUnitList.iterator().next();
                            List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( ind, tempPeriod,
                                oug.getMembers().iterator().next() ) );
                            if ( tempTarget != null )
                                targetList.addAll( tempTarget );
                            
                            List<SurveyData> tempSurvey = new ArrayList<SurveyData>( getSurveyList(ind, oug.getMembers().iterator().next()));
                            surveyList = new ArrayList<SurveyData>();
                            if(tempSurvey !=null && tempSurvey.size() > 0)
                                surveyList.addAll( tempSurvey );
                            
                        }

                    }
                }
                indicatorTargetList.put( ind, targetList );
                indicatorSurveyList.put( ind, surveyList );

            }
            else
            {                
                
                dElement = (DataElement) serviceListIterator.next();
                                                
                if ( deSelection.equalsIgnoreCase( "optioncombo" ) )
                {
                    decoc = selectedOptionComboList.get( countForServiceList );
                    /*if ( dElement.getAlternativeName() != null )
                    {
                        series1[countForServiceList] = dElement.getAlternativeName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc );
                        series2[countForServiceList] = dElement.getAlternativeName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc ) + " (Target)";
                    }
                    else*/
                    {
                        series1[countForServiceList] = dElement.getName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc );
                        series2[countForServiceList] = dElement.getName() + " : "
                            + dataElementCategoryOptionComboService.getOptionNames( decoc ) + " (Target)";
                    }
                    yseriesList.add( dElement.getName() + " : "
                        + dataElementCategoryOptionComboService.getOptionNames( decoc ) );
                }
                else
                {
                    /*if ( dElement.getAlternativeName() != null )
                    {
                        series1[countForServiceList] = dElement.getAlternativeName();
                        series2[countForServiceList] = dElement.getAlternativeName() + "(Target)";
                    }
                    else*/
                    {
                        series1[countForServiceList] = dElement.getName();
                        series2[countForServiceList] = dElement.getName() + "(Target)";
                    }
                    yseriesList.add( dElement.getName() );
                }
            }

            double numVal = 0.0;
            double denVal = 0.0;
            Iterator childOrgUnitListIterator = childOrgUnitList.iterator();
            countForChildOrgUnitList = 0;
            while ( childOrgUnitListIterator.hasNext() )
            {
                OrganisationUnit childOrgUnit = new OrganisationUnit();
                OrganisationUnitGroup childOrgUnitGroup = new OrganisationUnitGroup();

                if ( ougSetCB == null || facilityLB.equals( "children" ) )
                {
                    childOrgUnit = (OrganisationUnit) childOrgUnitListIterator.next();
                }
                else
                {
                    childOrgUnitGroup = (OrganisationUnitGroup) childOrgUnitListIterator.next();
                }

                if ( riRadio.equals( "indicatorsRadio" ) )
                {
                    /*
                     * if ( ind.getIndicatorType().getFactor() == 1200 ) { //
                     * TODO } else noOfPeriods = 1;
                     */
                    if ( ougSetCB == null || facilityLB.equals( "children" ) )
                    {
                        serviceValues[countForServiceList][countForChildOrgUnitList] = aggregationService
                            .getAggregatedIndicatorValue( ind, startPeriod.getStartDate(), endPeriod.getEndDate(),
                                childOrgUnit )
                            / noOfPeriods;
                        numVal = aggregationService.getAggregatedNumeratorValue( ind, startPeriod.getStartDate(), endPeriod.getEndDate(),
                            childOrgUnit )/ noOfPeriods;
                        denVal = aggregationService.getAggregatedDenominatorValue( ind, startPeriod.getStartDate(), endPeriod.getEndDate(),
                            childOrgUnit )/ noOfPeriods;
                        
                    }
                    else
                    {
                        double aggValue = 0.0;
                        List<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>( childOrgUnitGroup
                            .getMembers() );
                        Iterator<OrganisationUnit> orgUnitsIterator = orgUnits.iterator();
                        while ( orgUnitsIterator.hasNext() )
                        {
                            OrganisationUnit ou = (OrganisationUnit) orgUnitsIterator.next();
                            double tempd = aggregationService.getAggregatedIndicatorValue( ind, startPeriod
                                .getStartDate(), endPeriod.getEndDate(), ou );
                            double tempnum = aggregationService.getAggregatedNumeratorValue( ind, startPeriod
                                .getStartDate(), endPeriod.getEndDate(), ou );
                            double tempden = aggregationService.getAggregatedDenominatorValue( ind, startPeriod
                                .getStartDate(), endPeriod.getEndDate(), ou );
                            if ( tempd == -1 ) tempd = 0.0;
                            if( tempnum == -1 ) tempnum = 0.0;
                            if( tempden == -1 ) tempden = 0.0;
                            aggValue += tempd;
                            numVal += numVal;
                            denVal += denVal;
                        }
                        serviceValues[countForServiceList][countForChildOrgUnitList] = aggValue / noOfPeriods;
                        serviceValues[countForServiceList][countForChildOrgUnitList] /= orgUnits.size();
                        numVal /= noOfPeriods;
                        numVal /= orgUnits.size();
                        denVal /= noOfPeriods;
                        denVal /= orgUnits.size();
                    }
                }
                else
                {

                    DataElementGroup deg = dataElementService.getDataElementGroupByName( "Annual State Baseline data (%)" );                
                    if(deg != null && deg.getMembers().contains( dElement ))
                    {
                        if ( facilityLB.equals( "children" ) )
                        {
                            noOfChildren = 1;
                        }
                        else
                        {                        
                            if ( ougSetCB == null )
                            {
                                noOfChildren = dashBoardService.getAllChildren( (OrganisationUnit) childOrgUnit ).size();
                            }
                            else
                            {
                                noOfChildren = childOrgUnitGroup.getMembers().size();
                            }                        
                        }
                    }

                    if ( deSelection.equalsIgnoreCase( "optioncombo" ) )
                    {
                        if ( ougSetCB == null || facilityLB.equals( "children" ) )
                        {
                            serviceValues[countForServiceList][countForChildOrgUnitList] = aggregationService
                                .getAggregatedDataValue( dElement, decoc, startPeriod.getStartDate(), endPeriod
                                    .getEndDate(), childOrgUnit );
                        }
                        else
                        {
                            double aggValue = 0.0;
                            List<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>( childOrgUnitGroup
                                .getMembers() );
                            Iterator<OrganisationUnit> orgUnitsIterator = orgUnits.iterator();
                            while ( orgUnitsIterator.hasNext() )
                            {
                                OrganisationUnit ou = (OrganisationUnit) orgUnitsIterator.next();
                                double tempd = aggregationService.getAggregatedDataValue( dElement, decoc, startPeriod
                                    .getStartDate(), endPeriod.getEndDate(), ou );
                                if ( tempd == -1 )
                                    tempd = 0.0;
                                aggValue += tempd;
                            }
                            serviceValues[countForServiceList][countForChildOrgUnitList] = aggValue;
                        }
                    }
                    else
                    {
                        double aggDataValue = 0.0;
                        serviceValues[countForServiceList][countForChildOrgUnitList] = 0.0;

                        DataElementCategoryCombo dataElementCategoryCombo = dElement.getCategoryCombo();

                        List<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
                            dataElementCategoryCombo.getOptionCombos() );

                        Iterator<DataElementCategoryOptionCombo> optionComboIterator = optionCombos.iterator();
                        while ( optionComboIterator.hasNext() )
                        {
                            DataElementCategoryOptionCombo decoc1 = (DataElementCategoryOptionCombo) optionComboIterator
                                .next();
                            if ( ougSetCB == null || facilityLB.equals( "children" ) )
                            {
                                aggDataValue = aggregationService.getAggregatedDataValue( dElement, decoc1, startPeriod
                                    .getStartDate(), endPeriod.getEndDate(), childOrgUnit );
                            }
                            else
                            {
                                List<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>( childOrgUnitGroup
                                    .getMembers() );
                                Iterator<OrganisationUnit> orgUnitsIterator = orgUnits.iterator();
                                while ( orgUnitsIterator.hasNext() )
                                {
                                    OrganisationUnit ou = (OrganisationUnit) orgUnitsIterator.next();
                                    double tempd = aggregationService.getAggregatedDataValue( dElement, decoc1,
                                        startPeriod.getStartDate(), endPeriod.getEndDate(), ou );
                                    if ( tempd == -1 )
                                        tempd = 0.0;
                                    aggDataValue += tempd;
                                }
                            }
                            if ( aggDataValue == -1 )
                                aggDataValue = 0.0;
                            serviceValues[countForServiceList][countForChildOrgUnitList] += aggDataValue;
                        }
                    }
                }
                serviceValues[countForServiceList][countForChildOrgUnitList] /= noOfChildren;
                serviceValues[countForServiceList][countForChildOrgUnitList] = Math
                    .round( serviceValues[countForServiceList][countForChildOrgUnitList] * Math.pow( 10, 2 ) )
                    / Math.pow( 10, 2 );
            
                numVal = Math.round( numVal * Math.pow( 10, 2 ) ) / Math.pow( 10, 2 );
                denVal = Math.round( denVal * Math.pow( 10, 2 ) ) / Math.pow( 10, 2 );
            
                if ( serviceValues[countForServiceList][countForChildOrgUnitList] == -1 )
                    serviceValues[countForServiceList][countForChildOrgUnitList] = 0.0;
                if ( ougSetCB == null || facilityLB.equals( "children" ) )
                {
                    categories1[countForChildOrgUnitList] = childOrgUnit.getShortName();
                    categories2[countForChildOrgUnitList] = childOrgUnit.getShortName();
                }
                else
                {
                    categories1[countForChildOrgUnitList] = childOrgUnitGroup.getName();
                    categories2[countForChildOrgUnitList] = childOrgUnitGroup.getName();
                }
                if ( riRadio.equals( "indicatorsRadio" ) )
                    data2[countForServiceList][countForChildOrgUnitList] = (targetValues.get( "" + ind.getId() ))
                        .doubleValue();
                else
                    data2[countForServiceList][countForChildOrgUnitList] = 0.0;

                if ( riRadio.equals( "dataElementsRadio" ) )
                {
                    if ( dElement.getType().equalsIgnoreCase( "int" ) )
                        dataValues.add( "" + serviceValues[countForServiceList][countForChildOrgUnitList] );
                    else
                        dataValues.add( " " );
                }
                else
                {
                    dataValues.add( "" + serviceValues[countForServiceList][countForChildOrgUnitList] );
                    numValues.add( ""+numVal );
                    denValues.add( ""+denVal );
                }
                countForChildOrgUnitList++;
            }// childOrgUnitList loop end
            dataList.add( dataValues );
            numList.add( numValues );
            denList.add( denValues );
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
                    /*if ( de.getAlternativeName() != null )
                    {
                        deNames += de.getAlternativeName() + ",<br>";
                    }
                    else*/
                    {
                        deNames += de.getName() + ",<br>";
                    }

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

    /*
     * Assigns the Indicator Target values in the list.
     */
    public void getTargetValues()
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try
        {
            DBConnection dbc = new DBConnection();
            con = dbc.openConnection();

            String query = "SELECT target FROM indicator WHERE indicatorid = ?";
            pst = con.prepareStatement( query );
            Iterator serviceIterator = selectedServiceList.iterator();
            while ( serviceIterator.hasNext() )
            {
                Indicator indicator = (Indicator) serviceIterator.next();
                pst.setInt( 1, indicator.getId() );
                rs = pst.executeQuery();
                if ( rs.next() )
                {
                    targetValues.put( "" + indicator.getId(), new Double( rs.getDouble( 1 ) ) );
                }
                else
                {
                    targetValues.put( "" + indicator.getId(), new Double( 0.0 ) );
                }
                targetList.add( targetValues.get( "" + indicator.getId() ) );
                // System.out.println( " Target Value For Indiactor " +
                // indicator.getName() + " is : "
                // + targetValues.get( "" + indicator.getId() ) );
            }// while loop end
        } // try block end
        catch ( Exception e )
        {
            System.out.println( "Exception while getting Target Values : " + e.getMessage() );
        }
        finally
        {
            try
            {
                if ( rs != null )
                    rs.close();
                if ( pst != null )
                    pst.close();
                if ( con != null )
                    con.close();
            }
            catch ( Exception e )
            {
                System.out.println( "Exception while closing connection : " + e.getMessage() );
            }
        }// finally block end

    } // getTargetValues end

    
    private List<SurveyData> getSurveyList(Indicator ind, OrganisationUnit selectedOrgUnit)
    {
        List<SurveyData> resultList = new ArrayList<SurveyData>();
        
        String path = System.getProperty( "user.home" ) + File.separator + "dhis" + File.separator + "db"
        + File.separator + "surveyDataMapping.xml";
        try
        {
            String newpath = System.getenv( "USER_HOME" );
            if ( newpath != null )
            {
                path = newpath + File.separator + "dhis" + File.separator + "db" + File.separator + "surveyDataMapping.xml";
            }
        }
        catch ( NullPointerException npe )
        {
            // do nothing, but we might be using this somewhere without
            // USER_HOME set, which will throw a NPE
        }

        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse( new File( path ) );
            if ( doc == null )
            {
                // System.out.println( "There is no DECodes related XML file in
                // the user home" );
                return null;
            }

            NodeList listOfSurveys = doc.getElementsByTagName( "survey" );
            int totalSurveys = listOfSurveys.getLength();

            for ( int s = 0; s < totalSurveys; s++ )
            {
                Element surveyElement = (Element) listOfSurveys.item( s );
                int indId = Integer.parseInt(surveyElement.getAttribute( "indicatorid" ));
                int ouId = Integer.parseInt(surveyElement.getAttribute( "orgunitid" ));
                String dlhs = surveyElement.getAttribute( "dlhs" );
                String nfhs = surveyElement.getAttribute( "nfhs" );
                if(indId == ind.getId() && ouId==selectedOrgUnit.getId())
                {
                    if(dlhs.equalsIgnoreCase( "na" ))
                    {
                    
                    }
                    else
                    {
                        SurveyData sd = new SurveyData("DLHS", dlhs);
                        resultList.add( sd );
                    }
                    if(nfhs.equalsIgnoreCase( "na" ))
                    {
                    
                    }
                    else
                    {
                        SurveyData sd = new SurveyData("NFHS", nfhs);
                        resultList.add( sd );
                    }                    
                }            
            }// end of for loop with s var
        }// try block end
        catch ( SAXParseException err )
        {
            System.out.println( "** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId() );
            System.out.println( " " + err.getMessage() );
        }
        catch ( SAXException e )
        {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
        }
        
        return resultList;
    }
    


}// class end
