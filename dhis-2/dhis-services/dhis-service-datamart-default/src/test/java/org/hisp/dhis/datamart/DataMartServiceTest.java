package org.hisp.dhis.datamart;

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

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: DataMartServiceTest.java 5519 2008-08-05 09:00:31Z larshelg $
 */
public class DataMartServiceTest
    extends DhisConvenienceTest
{    
    private final String T = "true";
    private final String F = "false";
    
    private DataMartInternalProcess dataMartInternalProcess;
    
    private DataMartStore dataMartStore;

    private DataElementCategoryCombo categoryCombo;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;

    private Collection<Integer> dataElementIds;
    private Collection<Integer> indicatorIds;
    private Collection<Integer> periodIds;
    private Collection<Integer> organisationUnitIds;
    
    private DataElement dataElementA;
    private DataElement dataElementB;
    
    private Period periodA;
    private Period periodB;
    private Period periodC;
    private Period periodD;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;
    private OrganisationUnit unitD;
    private OrganisationUnit unitE;
    private OrganisationUnit unitF;
    private OrganisationUnit unitG;
    private OrganisationUnit unitH;
    private OrganisationUnit unitI;    
    
    @Override
    public void setUpTest()
    {
        dataMartInternalProcess = (DataMartInternalProcess) getBean( DataMartInternalProcess.ID );

        dataMartStore = (DataMartStore) getBean( DataMartStore.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        dataValueService = (DataValueService) getBean( DataValueService.ID );

        categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );
        
        categoryOptionCombo = categoryOptionComboService.getDefaultDataElementCategoryOptionCombo();
        
        // ---------------------------------------------------------------------
        // Setup identifier Collections
        // ---------------------------------------------------------------------

        dataElementIds = new HashSet<Integer>();
        indicatorIds = new HashSet<Integer>();
        periodIds = new HashSet<Integer>();
        organisationUnitIds = new HashSet<Integer>();
        
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        dataElementA = createDataElement( 'A', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );
        dataElementB = createDataElement( 'B', DataElement.TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );

        dataElementIds.add( dataElementService.addDataElement( dataElementA ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementB ) );
        
        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Date mar01 = getDate( 2005, 3, 1 );
        Date mar31 = getDate( 2005, 3, 31 );
        Date apr01 = getDate( 2005, 4, 1 );
        Date apr30 = getDate( 2005, 4, 30 );
        Date may01 = getDate( 2005, 5, 1 );
        Date may31 = getDate( 2005, 5, 31 );
        
        periodA = createPeriod( periodType, mar01, mar31 );
        periodB = createPeriod( periodType, apr01, apr30 );
        periodC = createPeriod( periodType, may01, may31 );
        periodD = createPeriod( periodType, mar01, may31 );
        
        periodIds.add( periodService.addPeriod( periodA ) );
        periodIds.add( periodService.addPeriod( periodB ) );
        periodIds.add( periodService.addPeriod( periodC ) );
        periodIds.add( periodService.addPeriod( periodD ) );
        
        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B', unitA );
        unitC = createOrganisationUnit( 'C', unitA );
        unitD = createOrganisationUnit( 'D', unitB );
        unitE = createOrganisationUnit( 'E', unitB );
        unitF = createOrganisationUnit( 'F', unitB );
        unitG = createOrganisationUnit( 'G', unitF);
        unitH = createOrganisationUnit( 'H', unitF );
        unitI = createOrganisationUnit( 'I' );

        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitA ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitB ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitC ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitD ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitE ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitF ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitG ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitH ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitI ) );
        
        organisationUnitService.addOrganisationUnitHierarchy( new Date() ); //TODO

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "90", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitD, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitE, "35", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitF, "25", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitG, "20", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitH, "60", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "70", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitD, "40", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitE, "65", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitF, "55", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitG, "20", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitH, "15", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitC, "95", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitD, "40", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitE, "45", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitF, "30", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitG, "50", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitH, "70", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, unitC, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, unitD, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, unitE, F, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, unitG, F, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, unitH, T, categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, unitC, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, unitD, F, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, unitE, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, unitG, F, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, unitH, T, categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementB, periodC, unitC, F, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodC, unitD, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodC, unitE, F, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodC, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodC, unitG, T, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodC, unitH, T, categoryOptionCombo ) );
    }
    
    public void testSumIntDataElementDataMart()
    {
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        dataElementService.updateDataElement( dataElementA );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );
        
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitA ), 240.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitB ), 150.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitC ), 90.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitD ), 10.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitE ), 35.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitF ), 105.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitG ), 20.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitH ), 60.0 );
        
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitA ), 835.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitB ), 580.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitC ), 255.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitD ), 90.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitE ), 145.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitF ), 345.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitG ), 90.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitH ), 145.0 );
    }
    
    public void testAverageIntDataElementDataMart()
    {
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        
        dataElementService.updateDataElement( dataElementA );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitA ), 240.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitB ), 150.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitC ), 90.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitD ), 10.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitE ), 35.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitF ), 105.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitG ), 20.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodA, unitH ), 60.0 );
        
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitA ), 278.5 ); 
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitB ), 193.3 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitC ), 85.2 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitD ), 29.9 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitE ), 48.1 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitF ), 115.3 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitG ), 30.1 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementA, periodD, unitH ), 48.7 );
    }
    
    public void testSumBoolDataElementDataMart()
    {
        dataElementB.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );

        dataElementService.updateDataElement( dataElementB );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitA ), 4.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitB ), 3.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitC ), 1.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitD ), 1.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitE ), 0.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitF ), 2.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitG ), 0.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitH ), 1.0 );
        
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitA ), 12.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitB ), 10.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitC ), 2.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitD ), 2.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitE ), 1.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitF ), 7.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitG ), 1.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitH ), 3.0 );
    }
    
    public void testAverageBoolDataElementDataMart()
    {
        dataElementB.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        
        dataElementService.updateDataElement( dataElementB );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitA ), 66.7 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitB ), 60.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitC ), 100.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitD ), 100.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitE ), 0.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitF ), 66.7 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitG ), 0.0 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodA, unitH ), 100.0 );
        
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitA ), 66.7 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitB ), 66.7 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitC ), 66.3 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitD ), 67.4 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitE ), 32.6 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitF ), 77.9 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitG ), 33.7 );
        assertEquals( dataMartStore.getAggregatedValue( dataElementB, periodD, unitH ), 100.0 );
    }
    
    public void testIndicatorDataMart()
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement dataElementC = createDataElement( 'C', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );
        DataElement dataElementD = createDataElement( 'D', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );
        DataElement dataElementE = createDataElement( 'E', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_AVERAGE, categoryCombo );
        DataElement dataElementF = createDataElement( 'F', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_AVERAGE, categoryCombo );
        
        int idC = dataElementService.addDataElement( dataElementC );
        int idD = dataElementService.addDataElement( dataElementD );
        int idE = dataElementService.addDataElement( dataElementE );
        int idF = dataElementService.addDataElement( dataElementF );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementC, periodA, unitF, "40", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, unitG, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, unitH, "75", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, unitF, "45", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, unitG, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, unitH, "75", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementC, periodC, unitF, "40", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodC, unitG, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodC, unitH, "70", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementD, periodA, unitF, "65", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, unitG, "90", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, unitH, "25", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, unitF, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, unitG, "55", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, unitH, "85", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementD, periodC, unitF, "45", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodC, unitG, "15", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodC, unitH, "25", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementE, periodA, unitF, "1500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodA, unitG, "5500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodA, unitH, "4500", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementE, periodB, unitF, "6500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodB, unitG, "7000", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodB, unitH, "2000", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementE, periodC, unitF, "5500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodC, unitG, "5000", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodC, unitH, "3500", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementF, periodA, unitF, "2500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodA, unitG, "3500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodA, unitH, "4000", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementF, periodB, unitF, "3500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodB, unitG, "1000", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodB, unitH, "3000", categoryOptionCombo ) );        
        dataValueService.addDataValue( createDataValue( dataElementF, periodC, unitF, "2500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodC, unitG, "6000", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodC, unitH, "1500", categoryOptionCombo ) );

        // ---------------------------------------------------------------------
        // Setup Indicators
        // ---------------------------------------------------------------------

        IndicatorType indicatorType = createIndicatorType( 'A' ); // Factor = 100
        
        indicatorService.addIndicatorType( indicatorType );
        
        Indicator indicatorA = createIndicator( 'A', indicatorType );
        
        String suffix = "." + categoryOptionCombo.getId();
        
        indicatorA.setNumerator( "[" + idC + suffix + "]*[" + idD + suffix + "]" );
        indicatorA.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorA.setDenominator( "[" + idE + suffix + "]+[" + idF + suffix + "]" );
        indicatorA.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        
        indicatorIds.add( indicatorService.addIndicator( indicatorA ) );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        // ---------------------------------------------------------------------
        // Assert
        // ---------------------------------------------------------------------
        
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodA, unitF ), 104.7 );
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodA, unitG ), 10.0 );
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodA, unitH ), 22.1 );

        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodD, unitF ), 681.6 );
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodD, unitG ), 51.3 );
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodD, unitH ), 480.6 );
    }

    public void testAnnualizedIndicatorDataMart()
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement dataElementC = createDataElement( 'C', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );
        DataElement dataElementD = createDataElement( 'D', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );
        DataElement dataElementE = createDataElement( 'E', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_AVERAGE, categoryCombo );
        DataElement dataElementF = createDataElement( 'F', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_AVERAGE, categoryCombo );
        
        int idC = dataElementService.addDataElement( dataElementC );
        int idD = dataElementService.addDataElement( dataElementD );
        int idE = dataElementService.addDataElement( dataElementE );
        int idF = dataElementService.addDataElement( dataElementF );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementC, periodC, unitF, "40", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodC, unitG, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodC, unitH, "70", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementD, periodC, unitF, "45", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodC, unitG, "15", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodC, unitH, "25", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementE, periodC, unitF, "5500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodC, unitG, "5000", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementE, periodC, unitH, "3500", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementF, periodC, unitF, "2500", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodC, unitG, "6000", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementF, periodC, unitH, "1500", categoryOptionCombo ) );

        // ---------------------------------------------------------------------
        // Setup Indicators
        // ---------------------------------------------------------------------

        IndicatorType indicatorType = createIndicatorType( 'A' ); // Factor = 100
                
        indicatorService.addIndicatorType( indicatorType );
        
        Indicator indicatorA = createIndicator( 'A', indicatorType );
        
        indicatorA.setAnnualized( true );
        
        String suffix = Expression.SEPARATOR + categoryOptionCombo.getId();
        
        indicatorA.setNumerator( "[" + idC + suffix + "]*[" + idD + suffix + "]" );
        indicatorA.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorA.setDenominator( "[" + idE + suffix + "]+[" + idF + suffix + "]" );
        indicatorA.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        
        indicatorIds.add( indicatorService.addIndicator( indicatorA ) );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        // ---------------------------------------------------------------------
        // Assert
        // ---------------------------------------------------------------------
        
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodC, unitF ), 500.4 );
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodC, unitG ), 16.1 );
        assertEquals( dataMartStore.getAggregatedValue( indicatorA, periodC, unitH ), 412.1 );
    }
}
