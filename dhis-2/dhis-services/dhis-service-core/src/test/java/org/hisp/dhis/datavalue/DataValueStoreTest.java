package org.hisp.dhis.datavalue;

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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementStore;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DataValueStoreTest.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public class DataValueStoreTest
    extends DhisConvenienceTest
{
    private DataValueStore dataValueStore;

    private DataElementStore dataElementStore;
    
    private PeriodStore periodStore;

    private SourceStore sourceStore;

    private TransactionManager transactionManager;
    
    private Calendar calendar = Calendar.getInstance();

    // -------------------------------------------------------------------------
    // Supporting data
    // -------------------------------------------------------------------------

    private DataElement dataElementA;

    private DataElement dataElementB;

    private DataElement dataElementC;

    private DataElement dataElementD;

    private DataElementCategoryOptionCombo optionCombo;
    
    private Period periodA;

    private Period periodB;

    private Period periodC;

    private Period periodD;

    private Source sourceA;

    private Source sourceB;

    private Source sourceC;

    private Source sourceD;

    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        dataValueStore = (DataValueStore) getBean( DataValueStore.ID );
        
        dataElementStore = (DataElementStore) getBean( DataElementStore.ID );

        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        periodStore = (PeriodStore) getBean( PeriodStore.ID );
        
        sourceStore = (SourceStore) getBean( SourceStore.ID );
        
        transactionManager = (TransactionManager) getBean( TransactionManager.ID );
        
        // ---------------------------------------------------------------------
        // Add supporting data
        // ---------------------------------------------------------------------

        dataElementA = new DataElement( "NameA", "AlternativeNameA", "ShortNameA", "CodeA", "DescriptionA", true,
            DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );
        dataElementB = new DataElement( "NameB", "AlternativeNameB", "ShortNameB", "CodeB", "DescriptionB", true,
            DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );
        dataElementC = new DataElement( "NameC", "AlternativeNameC", "ShortNameC", "CodeC", "DescriptionC", true,
            DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );
        dataElementD = new DataElement( "NameD", "AlternativeNameD", "ShortNameD", "CodeD", "DescriptionD", true,
            DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );

        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
        dataElementStore.addDataElement( dataElementD );

        PeriodType periodType = periodStore.getAllPeriodTypes().iterator().next();
        periodA = new Period( periodType, getDay( 5 ), getDay( 6 ) );
        periodB = new Period( periodType, getDay( 6 ), getDay( 7 ) );
        periodC = new Period( periodType, getDay( 7 ), getDay( 8 ) );
        periodD = new Period( periodType, getDay( 8 ), getDay( 9 ) );

        periodStore.addPeriod( periodA );
        periodStore.addPeriod( periodB );
        periodStore.addPeriod( periodC );
        periodStore.addPeriod( periodD );

        sourceA = new DummySource( "SourceA" );
        sourceB = new DummySource( "SourceB" );
        sourceC = new DummySource( "SourceC" );
        sourceD = new DummySource( "SourceD" );

        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );
        sourceStore.addSource( sourceD );        

        optionCombo = new DataElementCategoryOptionCombo();
        
        categoryOptionComboService.addDataElementCategoryOptionCombo( optionCombo );
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private Date getDay( int day )
    {
        calendar.clear();
        calendar.set( Calendar.DAY_OF_YEAR, day );

        return calendar.getTime();
    }

    // -------------------------------------------------------------------------
    // Basic DataValue
    // -------------------------------------------------------------------------

    public void testAddDataValue()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceA, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );

        try
        {
            // Should give unique constraint violation
            dataValueStore.addDataValue( dataValueD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        transactionManager.enter();

        dataValueA = dataValueStore.getDataValue( sourceA, dataElementA, periodA );
        assertNotNull( dataValueA );
        assertEquals( sourceA.getId(), dataValueA.getSource().getId() );
        assertEquals( dataElementA, dataValueA.getDataElement() );
        assertEquals( periodA, dataValueA.getPeriod() );
        assertEquals( "1", dataValueA.getValue() );

        transactionManager.leave();
        transactionManager.enter();

        dataValueB = dataValueStore.getDataValue( sourceA, dataElementB, periodA );
        assertNotNull( dataValueB );
        assertEquals( sourceA.getId(), dataValueB.getSource().getId() );
        assertEquals( dataElementB, dataValueB.getDataElement() );
        assertEquals( periodA, dataValueB.getPeriod() );
        assertEquals( "2", dataValueB.getValue() );

        transactionManager.leave();
        transactionManager.enter();

        dataValueC = dataValueStore.getDataValue( sourceA, dataElementC, periodC );
        assertNotNull( dataValueC );
        assertEquals( sourceA.getId(), dataValueC.getSource().getId() );
        assertEquals( dataElementC, dataValueC.getDataElement() );
        assertEquals( periodC, dataValueC.getPeriod() );
        assertEquals( "3", dataValueC.getValue() );

        transactionManager.leave();
    }

    public void testUpdataDataValue()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceB, optionCombo );
        dataValueB.setValue( "2" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );

        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementB, periodA ) );

        dataValueA.setValue( "5" );
        dataValueStore.updateDataValue( dataValueA );

        dataValueA = dataValueStore.getDataValue( sourceA, dataElementA, periodA );
        assertNotNull( dataValueA );
        assertEquals( "5", dataValueA.getValue() );

        dataValueB = dataValueStore.getDataValue( sourceB, dataElementB, periodA );
        assertNotNull( dataValueB );
        assertEquals( "2", dataValueB.getValue() );
    }

    public void testDeleteAndGetDataValue()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementD, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValue( dataValueA );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValue( dataValueB );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValue( dataValueC );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValue( dataValueD );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );
    }

    public void testDeleteDataValuesBySource()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementD, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesBySource( sourceA );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesBySource( sourceB );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesBySource( sourceC );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesBySource( sourceD );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );
    }

    public void testDeleteDataValuesByDataElement()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementD, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesByDataElement( dataElementA );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesByDataElement( dataElementB );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNotNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesByDataElement( dataElementC );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNotNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );

        dataValueStore.deleteDataValuesByDataElement( dataElementD );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementA, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceA, dataElementB, periodA ) );
        assertNull( dataValueStore.getDataValue( sourceD, dataElementC, periodC ) );
        assertNull( dataValueStore.getDataValue( sourceB, dataElementD, periodC ) );
    }

    // -------------------------------------------------------------------------
    // Collections of DataValues
    // -------------------------------------------------------------------------

    public void testGetAllDataValues()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementD, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );
    
        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );
        
        Collection<DataValue> dataValues = dataValueStore.getAllDataValues();
        assertNotNull( dataValues );
        assertEquals( 4, dataValues.size() );
    }   
        
    public void testGetDataValuesSourcePeriod()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementD, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        Collection<DataValue> dataValues = dataValueStore.getDataValues( sourceA, periodA );
        assertNotNull( dataValues );
        assertEquals( 2, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sourceB, periodC );
        assertNotNull( dataValues );
        assertEquals( 1, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sourceB, periodD );
        assertNotNull( dataValues );
        assertEquals( 0, dataValues.size() );
    }

    public void testGetDataValuesSourceDataElement()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementD, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        Collection<DataValue> dataValues = dataValueStore.getDataValues( sourceA, dataElementA );
        assertNotNull( dataValues );
        assertEquals( 1, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sourceA, dataElementB );
        assertNotNull( dataValues );
        assertEquals( 1, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sourceA, dataElementC );
        assertNotNull( dataValues );
        assertEquals( 0, dataValues.size() );
    }

    public void testGetDataValuesSourcesDataElement()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementA, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        Collection<Source> sources = new HashSet<Source>();
        sources.add( sourceA );
        sources.add( sourceB );

        Collection<DataValue> dataValues = dataValueStore.getDataValues( sources, dataElementA );
        assertNotNull( dataValues );
        assertEquals( 2, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sources, dataElementB );
        assertNotNull( dataValues );
        assertEquals( 1, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sources, dataElementC );
        assertNotNull( dataValues );
        assertEquals( 0, dataValues.size() );
    }

    public void testGetDataValuesSourcePeriodDataElements()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementB, periodA, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementC, periodC, sourceD, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementA, periodC, sourceB, optionCombo );
        dataValueD.setValue( "4" );

        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );

        Collection<DataElement> dataElements = new HashSet<DataElement>();
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );

        Collection<DataValue> dataValues = dataValueStore.getDataValues( sourceA, periodA, dataElements );
        assertNotNull( dataValues );
        assertEquals( 2, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sourceB, periodC, dataElements );
        assertNotNull( dataValues );
        assertEquals( 1, dataValues.size() );

        dataValues = dataValueStore.getDataValues( sourceD, periodC, dataElements );
        assertNotNull( dataValues );
        assertEquals( 0, dataValues.size() );
    }
    
    public void testGetDataValuesDataElementPeriodsSources()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceB, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementA, periodB, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementA, periodA, sourceC, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementB, periodB, sourceD, optionCombo );
        dataValueD.setValue( "4" );
        
        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );
        
        Collection<Period> periods = new HashSet<Period>();
        periods.add( periodA );
        periods.add( periodB );

        Collection<Source> sources = new HashSet<Source>();
        sources.add( sourceA );
        sources.add( sourceB );
        
        Collection<DataValue> dataValues = dataValueStore.getDataValues( dataElementA, periods, sources );
        
        assertEquals( dataValues.size(), 2 );
        assertTrue( dataValues.contains( dataValueA ) );
        assertTrue( dataValues.contains( dataValueB ) );
    }

    public void testGetDataValuesOptionComboDataElementPeriodsSources()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceB, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementA, periodB, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementA, periodA, sourceC, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementB, periodB, sourceD, optionCombo );
        dataValueD.setValue( "4" );
        
        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );
        
        Collection<Period> periods = new HashSet<Period>();
        periods.add( periodA );
        periods.add( periodB );

        Collection<Source> sources = new HashSet<Source>();
        sources.add( sourceA );
        sources.add( sourceB );
        
        Collection<DataValue> dataValues = dataValueStore.getDataValues( dataElementA, optionCombo, periods, sources );
        
        assertEquals( dataValues.size(), 2 );
        assertTrue( dataValues.contains( dataValueA ) );
        assertTrue( dataValues.contains( dataValueB ) );
    }
    
    public void testGetDataValuesDataElementsPeriodsSourcesFirstResultMaxResults()
        throws Exception
    {
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementA, periodB, sourceB, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementB, periodC, sourceC, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementB, periodD, sourceD, optionCombo );
        dataValueD.setValue( "4" );
        DataValue dataValueE = new DataValue( dataElementC, periodA, sourceA, optionCombo );
        dataValueC.setValue( "5" );
        DataValue dataValueF = new DataValue( dataElementC, periodB, sourceB, optionCombo );
        dataValueD.setValue( "6" );
        DataValue dataValueG = new DataValue( dataElementD, periodC, sourceC, optionCombo );
        dataValueC.setValue( "7" );
        DataValue dataValueH = new DataValue( dataElementD, periodD, sourceD, optionCombo );
        dataValueD.setValue( "8" );
        
        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );
        dataValueStore.addDataValue( dataValueE );
        dataValueStore.addDataValue( dataValueF );
        dataValueStore.addDataValue( dataValueG );
        dataValueStore.addDataValue( dataValueH );
        
        Collection<DataElement> dataElements = new HashSet<DataElement>();
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        dataElements.add( dataElementC );
        
        Collection<Period> periods = new HashSet<Period>();
        periods.add( periodA );
        periods.add( periodB );
        periods.add( periodC );

        Collection<Source> sources = new HashSet<Source>();
        sources.add( sourceA );
        sources.add( sourceB );
        sources.add( sourceC );
        
        Collection<DataValue> dataValues = dataValueStore.getDataValues( dataElements, periods, sources, 0, 10 );
        
        assertEquals( dataValues.size(), 5 );
        assertTrue( dataValues.contains( dataValueA ) );
        assertTrue( dataValues.contains( dataValueB ) );
        assertTrue( dataValues.contains( dataValueC ) );
        assertTrue( dataValues.contains( dataValueE ) );
        assertTrue( dataValues.contains( dataValueF ) );        

        dataValues = dataValueStore.getDataValues( dataElements, periods, sources, 1, 3 );
        
        assertEquals( dataValues.size(), 3 );        
        assertTrue( dataValues.contains( dataValueB ) );
        assertTrue( dataValues.contains( dataValueC ) );
        assertTrue( dataValues.contains( dataValueE ) );        
    }
}
