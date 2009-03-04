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
import java.util.List;

import org.hisp.dhis.DhisConvenienceTest;
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

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTableTest
    extends DhisConvenienceTest
{
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
        dataElements = new ArrayList<DataElement>();
        indicators = new ArrayList<Indicator>();
        dataSets = new ArrayList<DataSet>();
        periods = new ArrayList<Period>();
        relativePeriods = new ArrayList<Period>();
        units = new ArrayList<OrganisationUnit>();
        
        periodType = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        
        dataElementA.setId( 'A' );
        dataElementB.setId( 'B' );
        
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        
        indicatorType = createIndicatorType( 'A' );

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

    public void testIndicatorReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_INDICATORS,
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );
        
        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 2, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( selectColumns.contains( ReportTable.PERIOD_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );        
        assertEquals( 8, crossTabColumns.size() );
        
        assertTrue( crossTabColumns.contains( "shortnamea_reporting_month" ) );
        assertTrue( crossTabColumns.contains( "shortnamea_last3_months" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_reporting_month" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_last3_months" ) );
        
        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 8, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'C' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'D' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'C' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'D' ) ) );
        
        assertTrue( reportTable.getReportIndicators().contains( null ) );
        assertTrue( reportTable.getReportPeriods().contains( null ) );
        assertTrue( reportTable.getReportUnits().size() == 2 );
    }
    
    public void testIndicatorReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_INDICATORS,
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            false, false, true, relatives, null, i18nFormat, "january_2000" );

        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );        

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.INDICATOR_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );        

        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 1, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );
        assertEquals( 2, crossTabColumns.size() );
        assertTrue( crossTabColumns.contains( "shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnameb" ) );

        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 2, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( String.valueOf( Integer.valueOf( 'A' ) ) ) );
        assertTrue( crossTabIdentifiers.contains( String.valueOf( Integer.valueOf( 'B' ) ) ) );

        assertTrue( reportTable.getReportIndicators().size() == 2 );
        assertTrue( reportTable.getReportPeriods().size() == 4 );
        assertTrue( reportTable.getReportUnits().contains( null ) );
    }
    
    public void testIndicatorReportTableC()
    {        
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_INDICATORS, 
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, false, true, relatives, null, i18nFormat, "january_2000" );

        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 2, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( selectColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );        
        assertEquals( 4, crossTabColumns.size() );
        
        assertTrue( crossTabColumns.contains( "shortnamea_shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnamea_shortnameb" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_shortnameb" ) );

        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 4, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );

        assertTrue( reportTable.getReportIndicators().contains( null ) );
        assertTrue( reportTable.getReportPeriods().size() == 4 );
        assertTrue( reportTable.getReportUnits().contains( null ) );
    }

    public void testDataElementReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_DATAELEMENTS,
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );
        
        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 2, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.DATAELEMENT_ID ) );
        assertTrue( selectColumns.contains( ReportTable.PERIOD_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );        
        assertEquals( 8, crossTabColumns.size() );
        
        assertTrue( crossTabColumns.contains( "shortnamea_reporting_month" ) );
        assertTrue( crossTabColumns.contains( "shortnamea_last3_months" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_reporting_month" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_last3_months" ) );
        
        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 8, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'C' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'D' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'C' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'D' ) ) );
        
        assertTrue( reportTable.getReportIndicators().contains( null ) );
        assertTrue( reportTable.getReportPeriods().contains( null ) );
        assertTrue( reportTable.getReportUnits().size() == 2 );
    }
    
    public void testDataElementReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_DATAELEMENTS,
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, 
            false, false, true, relatives, null, i18nFormat, "january_2000" );

        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.DATAELEMENT_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );        

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.DATAELEMENT_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );        

        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 1, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );
        assertEquals( 2, crossTabColumns.size() );
        
        assertTrue( crossTabColumns.contains( "shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnameb" ) );

        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 2, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( String.valueOf( Integer.valueOf( 'A' ) ) ) );
        assertTrue( crossTabIdentifiers.contains( String.valueOf( Integer.valueOf( 'B' ) ) ) );

        assertTrue( reportTable.getReportIndicators().size() == 2 );
        assertTrue( reportTable.getReportPeriods().size() == 4 );
        assertTrue( reportTable.getReportUnits().contains( null ) );
    }
    
    public void testDataElementReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_DATAELEMENTS, 
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, 
            true, false, true, relatives, null, i18nFormat, "january_2000" );

        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 2, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.DATAELEMENT_ID ) );
        assertTrue( selectColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );        
        assertEquals( 4, crossTabColumns.size() );
        
        assertTrue( crossTabColumns.contains( "shortnamea_shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnamea_shortnameb" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_shortnameb" ) );

        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 4, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );

        assertTrue( reportTable.getReportIndicators().contains( null ) );
        assertTrue( reportTable.getReportPeriods().size() == 4 );
        assertTrue( reportTable.getReportUnits().contains( null ) );
    }

    public void testDataSetReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_DATASETS,
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, 
            true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );
        
        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 2, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.DATASET_ID ) );
        assertTrue( selectColumns.contains( ReportTable.PERIOD_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );        
        assertEquals( 8, crossTabColumns.size() );        
        assertTrue( crossTabColumns.contains( "shortnamea_reporting_month" ) );
        assertTrue( crossTabColumns.contains( "shortnamea_last3_months" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_reporting_month" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_last3_months" ) );
        
        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 8, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'C' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'D' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'C' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'D' ) ) );
        
        assertTrue( reportTable.getReportIndicators().contains( null ) );
        assertTrue( reportTable.getReportPeriods().contains( null ) );
        assertTrue( reportTable.getReportUnits().size() == 2 );
    }
    
    public void testDataSetReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_DATASETS,
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, 
            false, false, true, relatives, null, i18nFormat, "january_2000" );

        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.DATASET_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );        

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.DATASET_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );        

        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 1, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );
        assertEquals( 2, crossTabColumns.size() );
        assertTrue( crossTabColumns.contains( "shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnameb" ) );

        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 2, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( String.valueOf( Integer.valueOf( 'A' ) ) ) );
        assertTrue( crossTabIdentifiers.contains( String.valueOf( Integer.valueOf( 'B' ) ) ) );

        assertTrue( reportTable.getReportIndicators().size() == 2 );
        assertTrue( reportTable.getReportPeriods().size() == 4 );
        assertTrue( reportTable.getReportUnits().contains( null ) );
    }
    
    public void testDataSetReportTableC()
    {        
        ReportTable reportTable = new ReportTable( "Embezzlement", ReportTable.MODE_DATASETS, 
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, 
            true, false, true, relatives, null, i18nFormat, "january_2000" );

        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<String> selectColumns = reportTable.getSelectColumns();
        
        assertNotNull( selectColumns );
        assertEquals( 2, selectColumns.size() );
        assertTrue( selectColumns.contains( ReportTable.DATASET_ID ) );
        assertTrue( selectColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> crossTabColumns = reportTable.getCrossTabColumns();
        
        assertNotNull( crossTabColumns );        
        assertEquals( 4, crossTabColumns.size() );
        
        assertTrue( crossTabColumns.contains( "shortnamea_shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnamea_shortnameb" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_shortnamea" ) );
        assertTrue( crossTabColumns.contains( "shortnameb_shortnameb" ) );

        List<String> crossTabIdentifiers = reportTable.getCrossTabIdentifiers();
        
        assertNotNull( crossTabIdentifiers );
        assertEquals( 4, crossTabIdentifiers.size() );
        
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'A' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'A' ) ) );
        assertTrue( crossTabIdentifiers.contains( Integer.valueOf( 'B' ) + SEPARATOR + Integer.valueOf( 'B' ) ) );

        assertTrue( reportTable.getReportIndicators().contains( null ) );
        assertTrue( reportTable.getReportPeriods().size() == 4 );
        assertTrue( reportTable.getReportUnits().contains( null ) );
    }
}
