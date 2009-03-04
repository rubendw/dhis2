package org.hisp.dhis.reporttable;

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

import static org.hisp.dhis.reporttable.ReportTable.SEPARATOR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.AggregatedDataValueBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.AggregatedIndicatorValueBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetCompletenessResultBatchHandler;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.mock.MockI18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.reporttable.jdbc.ReportTableManager;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTableManagerTest
    extends DhisConvenienceTest
{
    private ReportTableManager reportTableManager;

    private BatchHandlerFactory batchHandlerFactory;

    private List<DataElement> dataElements;
    private List<Indicator> indicators;
    private List<DataSet> dataSets;
    private List<Period> periods;
    private List<Period> relativePeriods;
    private List<OrganisationUnit> units;

    private PeriodType periodType;

    private DataElement dataElementA;
    private DataElement dataElementB;
        
    private IndicatorType indicatorType;
    
    private Indicator indicatorA;
    private Indicator indicatorB;
    
    private DataSet dataSetA;
    private DataSet dataSetB;
    
    private Period periodA;
    private Period periodB;
    private Period periodC;
    private Period periodD;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;

    private RelativePeriods relatives;
        
    private I18nFormat i18nFormat;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
        throws Exception
    {
        reportTableManager = (ReportTableManager) getBean( ReportTableManager.ID );

        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        dataElements = new ArrayList<DataElement>();
        indicators = new ArrayList<Indicator>();
        dataSets = new ArrayList<DataSet>();
        periods = new ArrayList<Period>();
        relativePeriods = new ArrayList<Period>();
        units = new ArrayList<OrganisationUnit>();
        
        periodType = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        indicatorType = createIndicatorType( 'A' );

        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        
        dataElementA.setId( 'A' );
        dataElementB.setId( 'B' );
        
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        
        indicatorA = createIndicator( 'A', indicatorType );
        indicatorB = createIndicator( 'B', indicatorType );
        
        indicatorA.setId( 'A' );
        indicatorB.setId( 'B' );
        
        indicators.add( indicatorA );
        indicators.add( indicatorB );
        
        dataSetA = createDataSet( 'A', periodType );
        dataSetB = createDataSet( 'B', periodType );
        
        dataSetA.setId( 'A' );
        dataSetB.setId( 'B' );
        
        dataSets.add( dataSetA );
        dataSets.add( dataSetB );
        
        periodA = createPeriod( periodType, getDate( 2008, 1, 1 ), getDate( 2008, 1, 31 ) );
        periodB = createPeriod( periodType, getDate( 2008, 2, 1 ), getDate( 2008, 2, 28 ) );
        
        periodA.setId( 'A' );
        periodB.setId( 'B' );
        
        periods.add( periodA );
        periods.add( periodB );

        periodC = createPeriod( periodType, getDate( 2008, 3, 1 ), getDate( 2008, 3, 31 ) );
        periodD = createPeriod( periodType, getDate( 2008, 2, 1 ), getDate( 2008, 4, 30 ) );
        
        periodC.setId( 'C' );
        periodD.setId( 'D' );
        
        periodC.setName( RelativePeriods.REPORTING_MONTH );
        periodD.setName( RelativePeriods.LAST_3_MONTHS );
        
        relativePeriods.add( periodC );
        relativePeriods.add( periodD );
        
        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B' );
        
        unitA.setId( 'A' );
        unitB.setId( 'B' );
        
        units.add( unitA );
        units.add( unitB );

        relatives = new RelativePeriods();
        
        relatives.setReportingMonth( true );
        relatives.setLast3Months( true );
        
        i18nFormat = new MockI18nFormat();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testCreateReportTable()
    {
        ReportTable reportTable = new ReportTable( "Immunization", ReportTable.MODE_INDICATORS, 
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        reportTableManager.createReportTable( reportTable );
        
        reportTable = new ReportTable( "Immunization", ReportTable.MODE_DATAELEMENTS, 
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );

        reportTableManager.createReportTable( reportTable );
        
        reportTable = new ReportTable( "Immunization", ReportTable.MODE_DATAELEMENTS, 
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        reportTableManager.createReportTable( reportTable );
    }
    
    public void testRemoveReportTable()
    {
        ReportTable reportTable = new ReportTable( "Immunization", ReportTable.MODE_INDICATORS, 
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        reportTableManager.removeReportTable( reportTable );

        reportTable = new ReportTable( "Immunization", ReportTable.MODE_DATAELEMENTS, 
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        reportTableManager.removeReportTable( reportTable );

        reportTable = new ReportTable( "Immunization", ReportTable.MODE_DATAELEMENTS, 
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        reportTableManager.removeReportTable( reportTable );
    }
    
    public void testGetAggregatedValueMapForIndicator()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( AggregatedIndicatorValueBatchHandler.class );
        
        batchHandler.init();
        
        batchHandler.addObject( new AggregatedIndicatorValue( 'A', 'A', 8, 'A', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'A', 'A', 8, 'B', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'A', 'B', 8, 'A', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'A', 'B', 8, 'B', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'B', 'A', 8, 'A', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'B', 'A', 8, 'B', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'B', 'B', 8, 'A', 8, 1, 10, 20, 2 ) );
        batchHandler.addObject( new AggregatedIndicatorValue( 'B', 'B', 8, 'B', 8, 1, 10, 20, 2 ) );
        
        batchHandler.flush();
        
        ReportTable reportTable = new ReportTable( "Immunization", ReportTable.MODE_INDICATORS,
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        Map<String, Double> map = reportTableManager.getAggregatedValueMap( reportTable, null, null, unitA );
        
        assertNotNull( map );
        assertEquals( 4, map.entrySet().size() );
        
        Map<String, Double> reference = new HashMap<String, Double>();
        
        reference.put( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ), 10.0 );
        reference.put( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ), 10.0 );
        reference.put( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ), 10.0 );
        reference.put( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ), 10.0 );
        
        assertEquals( reference, map );
    }
    
    public void testGetAggregatedValueForDataElement()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( AggregatedDataValueBatchHandler.class );
        
        batchHandler.init();
        
        batchHandler.addObject( new AggregatedDataValue( 'A', 1, 'A', 1, 'A', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'A', 1, 'A', 1, 'B', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'A', 1, 'B', 1, 'A', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'A', 1, 'B', 1, 'B', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'B', 1, 'A', 1, 'A', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'B', 1, 'A', 1, 'B', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'B', 1, 'B', 1, 'A', 2, 10.0 ) );
        batchHandler.addObject( new AggregatedDataValue( 'B', 1, 'B', 1, 'B', 2, 10.0 ) );
        
        batchHandler.flush();        

        ReportTable reportTable = new ReportTable( "Immunization", ReportTable.MODE_DATAELEMENTS,
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        Map<String, Double> map = reportTableManager.getAggregatedValueMap( reportTable, null, null, unitA );
        
        assertNotNull( map );
        assertEquals( 4, map.entrySet().size() );
        
        Map<String, Double> reference = new HashMap<String, Double>();
        
        reference.put( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ), 10.0 );
        reference.put( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ), 10.0 );
        reference.put( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ), 10.0 );
        reference.put( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ), 10.0 );
        
        assertEquals( reference, map );
    }

    public void testGetAggregatedValueForDataSet()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetCompletenessResultBatchHandler.class );
        
        batchHandler.init();
        
        batchHandler.addObject( new DataSetCompletenessResult( 'A', 'A', "PeriodA", 'A', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'A', 'A', "PeriodA", 'B', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'A', 'B', "PeriodA", 'A', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'A', 'B', "PeriodA", 'B', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'B', 'A', "PeriodA", 'A', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'B', 'A', "PeriodA", 'B', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'B', 'B', "PeriodA", 'A', 1, "NameA", 20, 10, 5 ) );
        batchHandler.addObject( new DataSetCompletenessResult( 'B', 'B', "PeriodA", 'B', 1, "NameA", 20, 10, 5 ) );
        
        batchHandler.flush();        

        ReportTable reportTable = new ReportTable( "Immunization", ReportTable.MODE_DATASETS,
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        Map<String, Double> map = reportTableManager.getAggregatedValueMap( reportTable, null, null, unitA );
        
        assertNotNull( map );
        assertEquals( 4, map.entrySet().size() );
        
        Map<String, Double> reference = new HashMap<String, Double>();
        
        reference.put( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ), 50.0 );
        reference.put( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ), 50.0 );
        reference.put( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ), 50.0 );
        reference.put( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ), 50.0 );
        
        assertEquals( reference, map );
    }
}
