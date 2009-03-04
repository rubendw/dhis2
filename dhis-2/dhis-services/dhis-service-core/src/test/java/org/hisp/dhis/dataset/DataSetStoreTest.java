package org.hisp.dhis.dataset;

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

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.WeeklyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;

/**
 * @author Kristian Nordal
 * @version $Id: DataSetStoreTest.java 3451 2007-07-09 12:28:19Z torgeilo $
 */
public class DataSetStoreTest
    extends DhisConvenienceTest
{
    private PeriodStore periodStore;

    private DataSetStore dataSetStore;

    private SourceStore sourceStore;
    
    private PeriodType periodType;

    public void setUpTest()
        throws Exception
    {
        dataSetStore = (DataSetStore) getBean( DataSetStore.ID );

        periodStore = (PeriodStore) getBean( PeriodStore.ID );

        sourceStore = (SourceStore) getBean( SourceStore.ID );
        
        periodType = PeriodType.getAvailablePeriodTypes().iterator().next();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void assertEquals( char uniqueCharacter, DataSet dataSet )
    {
        assertEquals( "DataSet" + uniqueCharacter, dataSet.getName() );
        assertEquals( "ShortName" + uniqueCharacter, dataSet.getShortName() );
        assertEquals( periodType, dataSet.getPeriodType() );
    }
    
    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    public void testAddDataSet()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        int idA = dataSetStore.addDataSet( dataSetA );
        int idB = dataSetStore.addDataSet( dataSetB );

        dataSetA = dataSetStore.getDataSet( idA );
        dataSetB = dataSetStore.getDataSet( idB );

        assertEquals( idA, dataSetA.getId() );
        assertEquals( 'A', dataSetA );

        assertEquals( idB, dataSetB.getId() );
        assertEquals( 'B', dataSetB );
    }

    public void testUpdateDataSet()
    {
        DataSet dataSet = createDataSet( 'A', periodType );

        int id = dataSetStore.addDataSet( dataSet );

        dataSet = dataSetStore.getDataSet( id );

        assertEquals( 'A', dataSet );

        dataSet.setName( "DataSetB" );

        dataSetStore.updateDataSet( dataSet );

        dataSet = dataSetStore.getDataSet( id );

        assertEquals( dataSet.getName(), "DataSetB" );
    }

    public void testDeleteAndGetDataSet()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        int idA = dataSetStore.addDataSet( dataSetA );
        int idB = dataSetStore.addDataSet( dataSetB );

        assertNotNull( dataSetStore.getDataSet( idA ) );
        assertNotNull( dataSetStore.getDataSet( idB ) );

        dataSetStore.deleteDataSet( dataSetStore.getDataSet( idA ) );

        assertNull( dataSetStore.getDataSet( idA ) );
        assertNotNull( dataSetStore.getDataSet( idB ) );

        dataSetStore.deleteDataSet( dataSetStore.getDataSet( idB ) );

        assertNull( dataSetStore.getDataSet( idA ) );
        assertNull( dataSetStore.getDataSet( idB ) );
    }

    public void testGetDataSetByName()
        throws Exception
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        int idA = dataSetStore.addDataSet( dataSetA );
        int idB = dataSetStore.addDataSet( dataSetB );

        assertEquals( dataSetStore.getDataSetByName( "DataSetA" ).getId(), idA );
        assertEquals( dataSetStore.getDataSetByName( "DataSetB" ).getId(), idB );
        assertNull( dataSetStore.getDataSetByName( "DataSetC" ) );
    }

    public void testGetDataSetByShortName()
        throws Exception
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        int idA = dataSetStore.addDataSet( dataSetA );
        int idB = dataSetStore.addDataSet( dataSetB );

        assertEquals( dataSetStore.getDataSetByShortName( "ShortNameA" ).getId(), idA );
        assertEquals( dataSetStore.getDataSetByShortName( "ShortNameB" ).getId(), idB );
        assertNull( dataSetStore.getDataSetByShortName( "ShortNameC" ) );
    }

    public void testGetDataSets()
    {
        Source sourceA = new DummySource( "A" );
        sourceStore.addSource( sourceA );
        Source sourceB = new DummySource( "B" );
        sourceStore.addSource( sourceB );
        Source sourceC = new DummySource( "C" );
        sourceStore.addSource( sourceC );
        Source sourceD = new DummySource( "D" );
        sourceStore.addSource( sourceD );

        DataSet dataSetA = new DataSet( "A", new WeeklyPeriodType() );
        dataSetA.getSources().add( sourceA );
        dataSetStore.addDataSet( dataSetA );

        DataSet dataSetB = new DataSet( "B", new WeeklyPeriodType() );
        dataSetB.getSources().add( sourceB );
        dataSetB.getSources().add( sourceC );
        dataSetStore.addDataSet( dataSetB );

        DataSet dataSetC = new DataSet( "C", new WeeklyPeriodType() );
        dataSetC.getSources().add( sourceA );
        dataSetC.getSources().add( sourceB );
        dataSetStore.addDataSet( dataSetC );

        Collection<DataSet> result;

        result = dataSetStore.getDataSetsBySource( sourceA );
        assertEquals( 2, result.size() );
        assertTrue( result.contains( dataSetA ) );
        assertTrue( result.contains( dataSetC ) );

        result = dataSetStore.getDataSetsBySource( sourceB );
        assertEquals( 2, result.size() );
        assertTrue( result.contains( dataSetB ) );
        assertTrue( result.contains( dataSetC ) );

        result = dataSetStore.getDataSetsBySource( sourceC );
        assertEquals( 1, result.size() );
        assertTrue( result.contains( dataSetB ) );

        result = dataSetStore.getDataSetsBySource( sourceD );
        assertEquals( 0, result.size() );
    }

    public void testGetAllDataSets()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        dataSetStore.addDataSet( dataSetA );
        dataSetStore.addDataSet( dataSetB );

        Collection<DataSet> dataSets = dataSetStore.getAllDataSets();

        assertEquals( dataSets.size(), 2 );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetB ) );
    }

    // -------------------------------------------------------------------------
    // FrequencyOverrideAssociation
    // -------------------------------------------------------------------------

    public void testFrequencyOverrideAssociation()
        throws Exception
    {
        PeriodType periodType1 = periodStore.getPeriodType( YearlyPeriodType.class );
        PeriodType periodType2 = periodStore.getPeriodType( MonthlyPeriodType.class );
        PeriodType periodType3 = periodStore.getPeriodType( WeeklyPeriodType.class );

        DataSet dataSet1 = new DataSet( "name1", periodType1 );
        DataSet dataSet2 = new DataSet( "name2", periodType2 );

        dataSetStore.addDataSet( dataSet1 );
        dataSetStore.addDataSet( dataSet2 );

        Source source1 = new DummySource( "Source1" );
        Source source2 = new DummySource( "Source2" );
        sourceStore.addSource( source1 );
        sourceStore.addSource( source2 );

        FrequencyOverrideAssociation association = new FrequencyOverrideAssociation( dataSet1, source1, periodType3 );

        dataSetStore.addFrequencyOverrideAssociation( association );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source1 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 0 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source2 ).size(), 0 );

        dataSetStore
            .addFrequencyOverrideAssociation( new FrequencyOverrideAssociation( dataSet1, source2, periodType3 ) );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 2 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source1 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 0 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source2 ).size(), 1 );

        dataSetStore
            .addFrequencyOverrideAssociation( new FrequencyOverrideAssociation( dataSet2, source1, periodType3 ) );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 2 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source1 ).size(), 2 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source2 ).size(), 1 );

        dataSetStore.removeFrequencyOverrideAssociation( association );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source1 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 1 );
        assertEquals( dataSetStore.getFrequencyOverrideAssociationsBySource( source2 ).size(), 1 );
    }
}
