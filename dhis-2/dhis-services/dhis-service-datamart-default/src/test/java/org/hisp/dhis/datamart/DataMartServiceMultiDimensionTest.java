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
import java.util.Iterator;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
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
 * @version $Id$
 */
public class DataMartServiceMultiDimensionTest
    extends DhisConvenienceTest
{
    private final String T = "true";
    private final String F = "false";
    
    private DataMartInternalProcess dataMartInternalProcess;

    private DataMartStore dataMartStore;

    private DataElementCategoryOption categoryOptionA;
    private DataElementCategoryOption categoryOptionB;
    
    private DataElementCategory categoryA;
    
    private DataElementCategoryCombo categoryComboA;
    
    private DataElementCategoryOptionCombo categoryOptionComboA;
    private DataElementCategoryOptionCombo categoryOptionComboB;  
    
    private Collection<Integer> dataElementIds;
    private Collection<Integer> indicatorIds;
    private Collection<Integer> periodIds;
    private Collection<Integer> organisationUnitIds;
    
    private DataElement dataElementA;
    private DataElement dataElementB;
    
    private Period periodA;
    private Period periodB;
    private Period periodC;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;
    
    @Override
    public void setUpTest()
    {   
        // ---------------------------------------------------------------------
        // Dependencies
        // ---------------------------------------------------------------------
        
        dataMartInternalProcess = (DataMartInternalProcess) getBean( DataMartInternalProcess.ID );

        dataMartStore = (DataMartStore) getBean( DataMartStore.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        
        dataValueService = (DataValueService) getBean( DataValueService.ID );

        // ---------------------------------------------------------------------
        // Setup identifier Collections
        // ---------------------------------------------------------------------

        dataElementIds = new HashSet<Integer>();
        indicatorIds = new HashSet<Integer>();
        periodIds = new HashSet<Integer>();
        organisationUnitIds = new HashSet<Integer>();
        
        // ---------------------------------------------------------------------
        // Setup Dimensions
        // ---------------------------------------------------------------------

        categoryOptionA = new DataElementCategoryOption( "Male" );
        categoryOptionB = new DataElementCategoryOption( "Female" );
        
        categoryOptionService.addDataElementCategoryOption( categoryOptionA );
        categoryOptionService.addDataElementCategoryOption( categoryOptionB );

        categoryA = new DataElementCategory( "Gender" );
        categoryA.getCategoryOptions().add( categoryOptionA );
        categoryA.getCategoryOptions().add( categoryOptionB );

        categoryService.addDataElementCategory( categoryA );

        categoryComboA = new DataElementCategoryCombo( "Gender" );
        categoryComboA.getCategories().add( categoryA );        
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        categoryOptionComboService.generateOptionCombos( categoryComboA );

        Iterator<DataElementCategoryOptionCombo> categoryOptionCombos = categoryOptionComboService.getAllDataElementCategoryOptionCombos().iterator();
        
        categoryOptionCombos.next(); // Omit default
        categoryOptionComboA = categoryOptionCombos.next();
        categoryOptionComboB = categoryOptionCombos.next();     
        
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        dataElementA = createDataElement( 'A', categoryComboA );
        dataElementB = createDataElement( 'B', categoryComboA );

        dataElementIds.add( dataElementService.addDataElement( dataElementA ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementB ) );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();
        
        Date jul01 = getDate( 2005, 7, 1 );
        Date jul31 = getDate( 2005, 7, 31 );
        Date aug01 = getDate( 2005, 8, 1 );
        Date aug31 = getDate( 2005, 8, 31 );
        
        periodA = createPeriod( periodType, jul01, jul31 );
        periodB = createPeriod( periodType, aug01, aug31 );
        periodC = createPeriod( periodType, jul01, aug31 );
        
        periodIds.add( periodService.addPeriod( periodA ) );
        periodIds.add( periodService.addPeriod( periodB ) );
        periodIds.add( periodService.addPeriod( periodC ) );
        
        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B', unitA );
        unitC = createOrganisationUnit( 'C', unitA );
        
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitA ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitB ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitC ) );

        organisationUnitService.addOrganisationUnitHierarchy( new Date() ); //TODO
    }
    
    public void testSumIntDataElementDataMart()
    {
        dataElementA.setType( DataElement.TYPE_INT );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );        
        dataElementService.updateDataElement( dataElementA );
        
        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, "40", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, "20", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "90", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "10", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "25", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "20", categoryOptionComboB ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, "40", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, "80", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "70", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "30", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "65", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "40", categoryOptionComboB ) );
        
        // ---------------------------------------------------------------------
        // Test
        // ---------------------------------------------------------------------

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );
        
        assertEquals( 90.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitB ) );
        assertEquals( 70.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitB ) );
        assertEquals( 160.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitB ) );
        assertEquals( 10.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitB ) );
        assertEquals( 30.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitB ) );
        assertEquals( 40.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitB ) );

        assertEquals( 25.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitC ) );
        assertEquals( 65.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitC ) );
        assertEquals( 90.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitC ) );
        assertEquals( 20.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitC ) );
        assertEquals( 40.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitC ) );
        assertEquals( 60.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitC ) );

        assertEquals( 155.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitA ) );
        assertEquals( 175.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitA ) );
        assertEquals( 330.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitA ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitA ) );
        assertEquals( 150.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitA ) );
        assertEquals( 200.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitA ) );
    }
    
    public void testAverageIntDataElementDataMart()
    {
        dataElementA.setType( DataElement.TYPE_INT );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );        
        dataElementService.updateDataElement( dataElementA );
        
        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, "40", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, "20", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "90", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "10", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "25", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "20", categoryOptionComboB ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, "40", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, "80", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "70", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "30", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "65", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "40", categoryOptionComboB ) );
        
        // ---------------------------------------------------------------------
        // Test
        // ---------------------------------------------------------------------

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        assertEquals( 90.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitB ) );
        assertEquals( 70.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitB ) );
        assertEquals( 80.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitB ) );
        assertEquals( 10.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitB ) );
        assertEquals( 30.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitB ) );
        assertEquals( 20.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitB ) );

        assertEquals( 25.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitC ) );
        assertEquals( 65.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitC ) );
        assertEquals( 45.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitC ) );
        assertEquals( 20.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitC ) );
        assertEquals( 40.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitC ) );
        assertEquals( 30.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitC ) );

        assertEquals( 155.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitA ) );
        assertEquals( 175.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitA ) );
        assertEquals( 165.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitA ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitA ) );
        assertEquals( 150.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitA ) );
        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitA ) );
    }
    
    public void testSumBoolDataElement()
    {
        dataElementA.setType( DataElement.TYPE_BOOL );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );        
        dataElementService.updateDataElement( dataElementA );
        
        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, T, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, F, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, T, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, F, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, F, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, T, categoryOptionComboB ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, T, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, T, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, F, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, T, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, F, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, F, categoryOptionComboB ) );
        
        // ---------------------------------------------------------------------
        // Test
        // ---------------------------------------------------------------------

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitB ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitB ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitB ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitB ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitB ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitB ) );

        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitC ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitC ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitC ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitC ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitC ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitC ) );

        assertEquals( 2.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitA ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitA ) );
        assertEquals( 3.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitA ) );
        assertEquals( 1.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitA ) );
        assertEquals( 2.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitA ) );
        assertEquals( 3.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitA ) );
    }
    
    public void testAverageBoolDataElement()
    {
        dataElementA.setType( DataElement.TYPE_BOOL );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );        
        dataElementService.updateDataElement( dataElementA );
        
        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, T, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitA, F, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, T, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, F, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, F, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, T, categoryOptionComboB ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, T, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitA, T, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, F, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, T, categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, F, categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, F, categoryOptionComboB ) );
        
        // ---------------------------------------------------------------------
        // Test
        // ---------------------------------------------------------------------

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );

        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitB ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitB ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitB ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitB ) );
        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitB ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitB ) );

        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitC ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitC ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitC ) );
        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitC ) );
        assertEquals( 0.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitC ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitC ) );

        assertEquals( 66.7, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodA, unitA ) );
        assertEquals( 33.3, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodB, unitA ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboA, periodC, unitA ) );
        assertEquals( 33.3, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodA, unitA ) );
        assertEquals( 66.7, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodB, unitA ) );
        assertEquals( 50.0, dataMartStore.getAggregatedValue( dataElementA, categoryOptionComboB, periodC, unitA ) );
    }
    
    public void testIndicator()
    {
        dataElementA.setType( DataElement.TYPE_INT );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );        
        dataElementService.updateDataElement( dataElementA );
        
        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "9", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "3", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "1", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "5", categoryOptionComboB ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "3", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "2", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "7", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "9", categoryOptionComboB ) );
        
        // ---------------------------------------------------------------------
        // Setup Indicators
        // ---------------------------------------------------------------------

        IndicatorType indicatorType = createIndicatorType( 'A' ); // Factor = 100
        
        indicatorService.addIndicatorType( indicatorType );
        
        Indicator indicatorA = createIndicator( 'A', indicatorType );

        String suffixA = Expression.SEPARATOR + categoryOptionComboA.getId();
        String suffixB = Expression.SEPARATOR + categoryOptionComboB.getId();
        
        indicatorA.setNumerator( "[" + dataElementA.getId() + suffixA + "]*[" + dataElementA.getId() + suffixB + "]" );
        indicatorA.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorA.setDenominator( "1" );
        indicatorA.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorIds.add( indicatorService.addIndicator( indicatorA ) );

        // ---------------------------------------------------------------------
        // Test
        // ---------------------------------------------------------------------

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );
        
        assertEquals( 2700.0, dataMartStore.getAggregatedValue( indicatorA, periodA, unitB ) );
        assertEquals( 600.0, dataMartStore.getAggregatedValue( indicatorA, periodB, unitB ) );
        assertEquals( 6000.0, dataMartStore.getAggregatedValue( indicatorA, periodC, unitB ) );
        
        assertEquals( 500.0, dataMartStore.getAggregatedValue( indicatorA, periodA, unitC ) );
        assertEquals( 6300.0, dataMartStore.getAggregatedValue( indicatorA, periodB, unitC ) );
        assertEquals( 11200.0, dataMartStore.getAggregatedValue( indicatorA, periodC, unitC ) );
        
        assertEquals( 8000.0, dataMartStore.getAggregatedValue( indicatorA, periodA, unitA ) );
        assertEquals( 11000.0, dataMartStore.getAggregatedValue( indicatorA, periodB, unitA ) );
        assertEquals( 38000.0, dataMartStore.getAggregatedValue( indicatorA, periodC, unitA ) );   
    }

    public void testAnnualizedIndicator()
    {
        dataElementA.setType( DataElement.TYPE_INT );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );        
        dataElementService.updateDataElement( dataElementA );
        
        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "9", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitB, "3", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "1", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, unitC, "5", categoryOptionComboB ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "3", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitB, "2", categoryOptionComboB ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "7", categoryOptionComboA ) );
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, unitC, "9", categoryOptionComboB ) );
        
        // ---------------------------------------------------------------------
        // Setup Indicators
        // ---------------------------------------------------------------------

        IndicatorType indicatorType = createIndicatorType( 'A' ); // Factor = 100
        
        indicatorService.addIndicatorType( indicatorType );
        
        Indicator indicatorA = createIndicator( 'A', indicatorType );

        indicatorA.setAnnualized( true );
        
        String suffixA = Expression.SEPARATOR + categoryOptionComboA.getId();
        String suffixB = Expression.SEPARATOR + categoryOptionComboB.getId();
        
        indicatorA.setNumerator( "[" + dataElementA.getId() + suffixA + "]*[" + dataElementA.getId() + suffixB + "]" );
        indicatorA.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorA.setDenominator( "1" );
        indicatorA.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicatorIds.add( indicatorService.addIndicator( indicatorA ) );

        // ---------------------------------------------------------------------
        // Test
        // ---------------------------------------------------------------------

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );
        
        assertEquals( 31790.3, dataMartStore.getAggregatedValue( indicatorA, periodA, unitB ) );
        assertEquals( 7064.5, dataMartStore.getAggregatedValue( indicatorA, periodB, unitB ) );
        assertEquals( 35322.6, dataMartStore.getAggregatedValue( indicatorA, periodC, unitB ) );
        
        assertEquals( 5887.1, dataMartStore.getAggregatedValue( indicatorA, periodA, unitC ) );
        assertEquals( 74177.4, dataMartStore.getAggregatedValue( indicatorA, periodB, unitC ) );
        assertEquals( 65935.5, dataMartStore.getAggregatedValue( indicatorA, periodC, unitC ) );
        
        assertEquals( 94193.5, dataMartStore.getAggregatedValue( indicatorA, periodA, unitA ) );
        assertEquals( 129516.1, dataMartStore.getAggregatedValue( indicatorA, periodB, unitA ) );
        assertEquals( 223709.7, dataMartStore.getAggregatedValue( indicatorA, periodC, unitA ) );        
    }
}
