package org.hisp.dhis.aggregation;

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

import java.util.Date;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

import static org.hisp.dhis.expression.Expression.SEPARATOR;

/**
 * @author Lars Helge Overland
 * @version $Id: AggregationServiceTest.java 4753 2008-03-14 12:48:50Z larshelg $
 */
public class AggregationServiceTest
    extends DhisConvenienceTest
{
    private AggregationService aggregationService;

    private DataValueService dataValueService;

    private PeriodService periodService;

    private DataElementService dataElementService;

    private DataElementCategoryComboService categoryComboService;
    
    private IndicatorService indicatorService;

    private OrganisationUnitService organisationUnitService;

    private StatementManager statementManager;

    private DataElementCategoryCombo categoryCombo;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    public void setUpTest()
        throws Exception
    {
        aggregationService = (AggregationService) getBean( AggregationService.ID );

        dataValueService = (DataValueService) getBean( DataValueService.ID );

        periodService = (PeriodService) getBean( PeriodService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );             
        
        categoryOptionCombo = categoryCombo.getOptionCombos().iterator().next();
        
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        statementManager = (StatementManager) getBean( StatementManager.ID );
        
        statementManager.initialise();
    }
    
    public void tearDownTest() throws Exception
    {
        statementManager.destroy();
    }

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    public void testSumIntDataElementAggregatedValue()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement dataElementA = createDataElement( 'A', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement dataElementB = createDataElement( 'B', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Date feb01 = getDate( 2005, 2, 1 );
        Date feb10 = getDate( 2005, 2, 10 );
        Date feb15 = getDate( 2005, 2, 15 );
        Date feb20 = getDate( 2005, 2, 20 );
        Date mar01 = getDate( 2005, 3, 1 );
        Date mar17 = getDate( 2005, 3, 17 );
        Date apr01 = getDate( 2005, 4, 1 );
        Date apr16 = getDate( 2005, 4, 16 );
        Date may01 = getDate( 2005, 5, 1 );
        Date may16 = getDate( 2005, 5, 16 );
        Date jun01 = getDate( 2005, 6, 1 );
        Date jun10 = getDate( 2005, 6, 10 );
        Date jun15 = getDate( 2005, 6, 15 );
        Date jun20 = getDate( 2005, 6, 20 );

        Period periodA = createPeriod( periodType, mar01, apr01 );
        Period periodB = createPeriod( periodType, apr01, may01 );
        Period periodC = createPeriod( periodType, may01, jun01 );
        Period periodD = createPeriod( periodType, feb10, feb20 );
        Period periodE = createPeriod( periodType, jun10, jun20 );
        Period periodF = createPeriod( periodType, mar17, may16 );
        Period periodG = createPeriod( periodType, apr16, may16 );

        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
        periodService.addPeriod( periodF );
        periodService.addPeriod( periodG );

        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        OrganisationUnit unitA = createOrganisationUnit( 'A' );
        OrganisationUnit unitB = createOrganisationUnit( 'B', unitA );
        OrganisationUnit unitC = createOrganisationUnit( 'C', unitA );
        OrganisationUnit unitD = createOrganisationUnit( 'D', unitB );
        OrganisationUnit unitE = createOrganisationUnit( 'E', unitB );
        OrganisationUnit unitF = createOrganisationUnit( 'F', unitB );
        OrganisationUnit unitG = createOrganisationUnit( 'G' );
        OrganisationUnit unitH = createOrganisationUnit( 'H', unitF );
        OrganisationUnit unitI = createOrganisationUnit( 'I', unitF );

        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );
        organisationUnitService.addOrganisationUnit( unitC );
        organisationUnitService.addOrganisationUnit( unitD );
        organisationUnitService.addOrganisationUnit( unitE );
        organisationUnitService.addOrganisationUnit( unitF );
        organisationUnitService.addOrganisationUnit( unitG );

        organisationUnitService.addOrganisationUnitHierarchy( feb01 );

        organisationUnitService.addOrganisationUnit( unitH );

        organisationUnitService.addOrganisationUnitHierarchy( apr01 );

        organisationUnitService.addOrganisationUnit( unitI );

        organisationUnitService.addOrganisationUnitHierarchy( may01 );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "5", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "7", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitC, "8", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodD, unitC, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodE, unitF, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodG, unitF, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodE, unitG, "8", categoryOptionCombo ) ); // won't count
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitH, "5", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodD, unitH, "20", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitI, "15", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitI, "12", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitI, "17", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodF, unitC, "40", categoryOptionCombo ) );

        double aggregatedValue1 = aggregationService.getAggregatedDataValue( dataElementA, categoryOptionCombo, feb15, jun15, unitA );

        assertEquals( aggregatedValue1, 100.0 );
        
        double aggregatedValue2 = aggregationService.getAggregatedDataValue( dataElementA, categoryOptionCombo, feb15, jun15, unitB );

        assertEquals( aggregatedValue2, 47.0 );
        
        double aggregatedValue3 = aggregationService.getAggregatedDataValue( dataElementB, categoryOptionCombo, feb01, mar01, unitA );

        assertEquals( aggregatedValue3, AggregationService.NO_VALUES_REGISTERED );
    }

    public void testAverageIntDataElementAggregatedValue()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement dataElementA = createDataElement( 'A', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_AVERAGE );
        DataElement dataElementB = createDataElement( 'B', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_AVERAGE );        

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Date feb01 = getDate( 2005, 2, 1 );
        Date mar01 = getDate( 2005, 3, 1 );
        Date mar17 = getDate( 2005, 3, 17 );
        Date apr01 = getDate( 2005, 4, 1 );
        Date apr16 = getDate( 2005, 4, 16 );
        Date may01 = getDate( 2005, 5, 1 );
        Date may16 = getDate( 2005, 5, 16 );
        Date may31 = getDate( 2005, 5, 31 );
        Date jun01 = getDate( 2005, 6, 1 );
        Date jun15 = getDate( 2005, 6, 15 );
        Date jul01 = getDate( 2005, 7, 1 );

        Period periodA = createPeriod( periodType, mar01, apr01 );
        Period periodB = createPeriod( periodType, apr01, may01 );
        Period periodC = createPeriod( periodType, may01, may31 );
        Period periodD = createPeriod( periodType, jun01, jul01 );
        Period periodE = createPeriod( periodType, apr01, apr16 );
        Period periodF = createPeriod( periodType, may01, may16 );

        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
        periodService.addPeriod( periodF );

        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        OrganisationUnit unitA = createOrganisationUnit( 'A' );
        OrganisationUnit unitB = createOrganisationUnit( 'B', unitA );
        OrganisationUnit unitC = createOrganisationUnit( 'C', unitA );
        OrganisationUnit unitD = createOrganisationUnit( 'D', unitB );
        OrganisationUnit unitE = createOrganisationUnit( 'E', unitB );
        OrganisationUnit unitF = createOrganisationUnit( 'F', unitB );
        OrganisationUnit unitG = createOrganisationUnit( 'G' );
        OrganisationUnit unitH = createOrganisationUnit( 'H', unitF );
        OrganisationUnit unitI = createOrganisationUnit( 'I', unitF );
        
        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );
        organisationUnitService.addOrganisationUnit( unitC );
        organisationUnitService.addOrganisationUnit( unitD );
        organisationUnitService.addOrganisationUnit( unitE );
        organisationUnitService.addOrganisationUnit( unitF );
        organisationUnitService.addOrganisationUnit( unitG );

        organisationUnitService.addOrganisationUnitHierarchy( feb01 );

        organisationUnitService.addOrganisationUnit( unitH );

        organisationUnitService.addOrganisationUnitHierarchy( apr01 );

        organisationUnitService.addOrganisationUnit( unitI );

        organisationUnitService.addOrganisationUnitHierarchy( may01 );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "140", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitE, "80", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitE, "120", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodD, unitI, "20", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodE, unitB, "140", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodF, unitB, "20", categoryOptionCombo ) );

        double aggregatedValue1 = aggregationService.getAggregatedDataValue( dataElementA, categoryOptionCombo, mar17, jun15, unitA );

        assertEquals( 220.0, aggregatedValue1 );
        
        double aggregatedValue2 = aggregationService.getAggregatedDataValue( dataElementB, categoryOptionCombo, apr01, may01, unitA );

        assertEquals( aggregatedValue2, AggregationService.NO_VALUES_REGISTERED );
    }

    public void testSumBoolDataElementAggregatedValue()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement dataElementA = createDataElement( 'A', DataElement.TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement dataElementB = createDataElement( 'B', DataElement.TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_SUM );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Date feb01 = getDate( 2005, 2, 1 );
        Date feb10 = getDate( 2005, 2, 10 );
        Date feb15 = getDate( 2005, 2, 15 );
        Date feb20 = getDate( 2005, 2, 20 );
        Date mar01 = getDate( 2005, 3, 1 );
        Date mar17 = getDate( 2005, 3, 17 );
        Date apr01 = getDate( 2005, 4, 1 );
        Date apr16 = getDate( 2005, 4, 16 );
        Date may01 = getDate( 2005, 5, 1 );
        Date may16 = getDate( 2005, 5, 16 );
        Date jun01 = getDate( 2005, 6, 1 );
        Date jun10 = getDate( 2005, 6, 10 );
        Date jun15 = getDate( 2005, 6, 15 );
        Date jun20 = getDate( 2005, 6, 20 );

        Period periodA = createPeriod( periodType, mar01, apr01 );
        Period periodB = createPeriod( periodType, apr01, may01 );
        Period periodC = createPeriod( periodType, may01, jun01 );
        Period periodD = createPeriod( periodType, feb10, feb20 );
        Period periodE = createPeriod( periodType, jun10, jun20 );
        Period periodF = createPeriod( periodType, mar17, may16 );
        Period periodG = createPeriod( periodType, apr16, may16 );

        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
        periodService.addPeriod( periodF );
        periodService.addPeriod( periodG );

        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        OrganisationUnit unitA = createOrganisationUnit( 'A' );
        OrganisationUnit unitB = createOrganisationUnit( 'B', unitA );
        OrganisationUnit unitC = createOrganisationUnit( 'C', unitA );
        OrganisationUnit unitD = createOrganisationUnit( 'D', unitB );
        OrganisationUnit unitE = createOrganisationUnit( 'E', unitB );
        OrganisationUnit unitF = createOrganisationUnit( 'F', unitB );
        OrganisationUnit unitG = createOrganisationUnit( 'G' );
        OrganisationUnit unitH = createOrganisationUnit( 'H', unitF );
        OrganisationUnit unitI = createOrganisationUnit( 'I', unitF );

        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );
        organisationUnitService.addOrganisationUnit( unitC );
        organisationUnitService.addOrganisationUnit( unitD );
        organisationUnitService.addOrganisationUnit( unitE );
        organisationUnitService.addOrganisationUnit( unitF );
        organisationUnitService.addOrganisationUnit( unitG );

        organisationUnitService.addOrganisationUnitHierarchy( feb01 );

        organisationUnitService.addOrganisationUnit( unitH );

        organisationUnitService.addOrganisationUnitHierarchy( apr01 );

        organisationUnitService.addOrganisationUnit( unitI );

        organisationUnitService.addOrganisationUnitHierarchy( may01 );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        final String TRUE = "true";
        final String FALSE = "false";

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, FALSE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitC, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodD, unitC, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodE, unitF, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodF, unitC, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodG, unitF, TRUE, categoryOptionCombo ) );

        double aggregatedValue1 = aggregationService.getAggregatedDataValue( dataElementA, categoryOptionCombo, feb15, jun15, unitA );

        assertEquals( aggregatedValue1, 5.0 );
        
        double aggregatedValue3 = aggregationService.getAggregatedDataValue( dataElementB, categoryOptionCombo, feb01, mar01, unitA );

        assertEquals( aggregatedValue3, AggregationService.NO_VALUES_REGISTERED );
    }

    public void testAverageBoolDataElementAggregatedValue()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement dataElementA = createDataElement( 'A', DataElement.TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_AVERAGE );
        DataElement dataElementB = createDataElement( 'B', DataElement.TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_AVERAGE );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Date feb01 = getDate( 2005, 2, 1 );
        Date mar01 = getDate( 2005, 3, 1 );
        Date mar17 = getDate( 2005, 3, 17 );
        Date apr01 = getDate( 2005, 4, 1 );
        Date apr16 = getDate( 2005, 4, 16 );
        Date may01 = getDate( 2005, 5, 1 );
        Date may16 = getDate( 2005, 5, 16 );
        Date jun01 = getDate( 2005, 6, 1 );
        Date jun15 = getDate( 2005, 6, 15 );
        Date jul01 = getDate( 2005, 7, 1 );

        Period periodA = createPeriod( periodType, mar01, apr01 );
        Period periodB = createPeriod( periodType, apr01, may01 );
        Period periodC = createPeriod( periodType, may01, jun01 );
        Period periodD = createPeriod( periodType, jun01, jul01 );
        Period periodE = createPeriod( periodType, apr01, apr16 );
        Period periodF = createPeriod( periodType, may01, may16 );

        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
        periodService.addPeriod( periodF );

        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        OrganisationUnit unitA = createOrganisationUnit( 'A' );
        OrganisationUnit unitB = createOrganisationUnit( 'B', unitA );
        OrganisationUnit unitC = createOrganisationUnit( 'C', unitA );
        OrganisationUnit unitD = createOrganisationUnit( 'D', unitB );
        OrganisationUnit unitE = createOrganisationUnit( 'E', unitB );
        OrganisationUnit unitF = createOrganisationUnit( 'F', unitB );
        OrganisationUnit unitG = createOrganisationUnit( 'G' );
        OrganisationUnit unitH = createOrganisationUnit( 'H', unitF );
        OrganisationUnit unitI = createOrganisationUnit( 'I', unitF );

        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );
        organisationUnitService.addOrganisationUnit( unitC );
        organisationUnitService.addOrganisationUnit( unitD );
        organisationUnitService.addOrganisationUnit( unitE );
        organisationUnitService.addOrganisationUnit( unitF );
        organisationUnitService.addOrganisationUnit( unitG );

        organisationUnitService.addOrganisationUnitHierarchy( feb01 );

        organisationUnitService.addOrganisationUnit( unitH );

        organisationUnitService.addOrganisationUnitHierarchy( apr01 );

        organisationUnitService.addOrganisationUnit( unitI );

        organisationUnitService.addOrganisationUnitHierarchy( may01 );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        final String TRUE = "true";
        final String FALSE = "false";

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitE, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodC, unitE, FALSE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodD, unitF, FALSE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodE, unitB, TRUE, categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodF, unitB, FALSE, categoryOptionCombo ) );

        double aggregatedValue1 = aggregationService.getAggregatedDataValue( dataElementA, categoryOptionCombo, mar17, jun15, unitA );

        assertEquals( aggregatedValue1, 0.5 );
        
        double aggregatedValue2 = aggregationService.getAggregatedDataValue( dataElementB, categoryOptionCombo, apr01, may01, unitA );

        assertEquals( aggregatedValue2, AggregationService.NO_VALUES_REGISTERED );
    }

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    public void testIndicatorAggregatedValue()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        DataElement elementA = createDataElement( 'A', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement elementB = createDataElement( 'B', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement elementC = createDataElement( 'C', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement elementD = createDataElement( 'D', DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM );
        
        int dataElementIdA = dataElementService.addDataElement( elementA );
        int dataElementIdB = dataElementService.addDataElement( elementB );
        int dataElementIdC = dataElementService.addDataElement( elementC );
        int dataElementIdD = dataElementService.addDataElement( elementD );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Date feb01 = getDate( 2005, 2, 1 );
        Date mar01 = getDate( 2005, 3, 1 );
        Date apr01 = getDate( 2005, 4, 1 );
        Date may01 = getDate( 2005, 5, 1 );
        Date jun01 = getDate( 2005, 6, 1 );

        Period periodA = createPeriod( periodType, feb01, mar01 );
        Period periodB = createPeriod( periodType, apr01, may01 );
        
        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        OrganisationUnit unit1 = createOrganisationUnit( 'A' );
        
        organisationUnitService.addOrganisationUnit( unit1 );

        organisationUnitService.addOrganisationUnitHierarchy( feb01 );

        String suffix = SEPARATOR + categoryOptionCombo.getId();
        
        // ---------------------------------------------------------------------
        // Setup IndicatorType
        // ---------------------------------------------------------------------

        int factor = 100;
        
        IndicatorType indicatorType = new IndicatorType( "indicatorTypeName", factor );
        
        indicatorService.addIndicatorType( indicatorType );

        // ---------------------------------------------------------------------
        // Setup Indicators
        // ---------------------------------------------------------------------

        // ---------------------------------------------------------------------
        // Numerator: ( ( [1] + [2] - 2 ) / ( [3] * [4] ) )
        // ---------------------------------------------------------------------

        String numerator = "( ( ["
            + dataElementIdA + suffix + "] + [" 
            + dataElementIdB + suffix + "] - 2 ) / ( [" 
            + dataElementIdC + suffix + "] * ["
            + dataElementIdD + suffix + "] ) )";

        // ---------------------------------------------------------------------
        // Denominator: [1] - 8 + [2] + 2
        // ---------------------------------------------------------------------

        String denominator = "[" 
            + dataElementIdA + suffix + "] - 8 + ["
            + dataElementIdB + suffix + "] + 2";

        Indicator indicator = createIndicator( 'A', indicatorType );
        
        indicator.setNumerator( numerator );
        indicator.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        indicator.setDenominator( denominator );
        indicator.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorService.addIndicator( indicator );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( elementA, periodA, unit1, "12", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( elementB, periodA, unit1, "14", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( elementC, periodA, unit1, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( elementD, periodA, unit1, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( elementC, periodB, unit1, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( elementD, periodB, unit1, "5", categoryOptionCombo ) );

        // ---------------------------------------------------------------------
        // No data registered for element 2, will be replaced by 0
        // ---------------------------------------------------------------------

        double aggregatedValue1 = aggregationService.getAggregatedIndicatorValue( indicator, feb01, mar01, unit1 );

        // ---------------------------------------------------------------------
        // ((12+14-2)/(2*3)) / (12-8+14+2) = 4 / 20 = 0.2
        // ---------------------------------------------------------------------

        assertEquals( aggregatedValue1, 20.0 );

        double aggregatedNumeratorValue = aggregationService.getAggregatedNumeratorValue( 
            indicator, feb01, mar01, unit1 );

        assertEquals( aggregatedNumeratorValue, 4.0 );

        double aggregatedDenominatorValue = aggregationService.getAggregatedDenominatorValue( 
            indicator, feb01, mar01, unit1 );

        assertEquals( aggregatedDenominatorValue, 20.0 );

        double aggregatedValue2 = aggregationService.getAggregatedIndicatorValue( indicator, apr01, may01, unit1 );

        // ---------------------------------------------------------------------
        // ((0+0-2)/(2*5)) / (0-8+0+2)
        // ---------------------------------------------------------------------

        assertEquals( aggregatedValue2, 3.333, 0.1 );
        
        double aggregatedValue3 = aggregationService.getAggregatedIndicatorValue( indicator, may01, jun01, unit1 );

        // ---------------------------------------------------------------------
        // ((0+0-2)/(0*0)) / (0-8+0+2)
        // ---------------------------------------------------------------------

        assertEquals( aggregatedValue3, AggregationService.NO_VALUES_REGISTERED );
    }
}
