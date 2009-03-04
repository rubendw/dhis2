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
import java.util.HashSet;

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
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataSetServiceTest
    extends DhisConvenienceTest
{
    private PeriodStore periodStore;

    private DataSetService dataSetService;

    private SourceStore sourceStore;

    private PeriodType periodType;

    public void setUpTest()
        throws Exception
    {
        dataSetService = (DataSetService) getBean( DataSetService.ID );

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
        
        int idA = dataSetService.addDataSet( dataSetA );
        int idB = dataSetService.addDataSet( dataSetB );       
        
        dataSetA = dataSetService.getDataSet( idA );
        dataSetB = dataSetService.getDataSet( idB );
        
        assertEquals( idA, dataSetA.getId() );
        assertEquals( 'A', dataSetA );
        
        assertEquals( idB, dataSetB.getId() );
        assertEquals( 'B', dataSetB );
    }
    
    public void testUpdateDataSet()
    {
        DataSet dataSet = createDataSet( 'A', periodType );
        
        int id = dataSetService.addDataSet( dataSet );
        
        dataSet = dataSetService.getDataSet( id );
        
        assertEquals( 'A', dataSet );
        
        dataSet.setName( "DataSetB" );
        
        dataSetService.updateDataSet( dataSet );
        
        dataSet = dataSetService.getDataSet( id );
        
        assertEquals( dataSet.getName(), "DataSetB" );
    }
    
    public void testDeleteAndGetDataSet()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );
        
        int idA = dataSetService.addDataSet( dataSetA );
        int idB = dataSetService.addDataSet( dataSetB );
        
        assertNotNull( dataSetService.getDataSet( idA ) );
        assertNotNull( dataSetService.getDataSet( idB ) );
        
        dataSetService.deleteDataSet( dataSetService.getDataSet( idA ) );
        
        assertNull( dataSetService.getDataSet( idA ) );
        assertNotNull( dataSetService.getDataSet( idB ) );

        dataSetService.deleteDataSet( dataSetService.getDataSet( idB ) );
        
        assertNull( dataSetService.getDataSet( idA ) );
        assertNull( dataSetService.getDataSet( idB ) );        
    }
    
    public void testGetDataSetByName()
        throws Exception
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );
    
        int idA = dataSetService.addDataSet( dataSetA );
        int idB = dataSetService.addDataSet( dataSetB );
    
        assertEquals( dataSetService.getDataSetByName( "DataSetA" ).getId(), idA );
        assertEquals( dataSetService.getDataSetByName( "DataSetB" ).getId(), idB );
        assertNull( dataSetService.getDataSetByName( "DataSetC" ) );
    }

    public void testGetDataSetByShortName()
        throws Exception
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        int idA = dataSetService.addDataSet( dataSetA );
        int idB = dataSetService.addDataSet( dataSetB );

        assertEquals( dataSetService.getDataSetByShortName( "ShortNameA" ).getId(), idA );
        assertEquals( dataSetService.getDataSetByShortName( "ShortNameB" ).getId(), idB );
        assertNull( dataSetService.getDataSetByShortName( "ShortNameC" ) );
    }

    public void testGetAllDataSets()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );
    
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );
        
        Collection<DataSet> dataSets = dataSetService.getAllDataSets();
        
        assertEquals( dataSets.size(), 2 );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetB ) );            
    }

    public void testGetDataSetsBySource()
    {
        Source sourceA = new DummySource( "A" );
        Source sourceB = new DummySource( "B" );
        Source sourceC = new DummySource( "C" );
        
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );
        
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );
        DataSet dataSetC = createDataSet( 'C', periodType );
        
        dataSetA.getSources().add( sourceA );
        dataSetA.getSources().add( sourceB );
        
        dataSetB.getSources().add( sourceB );
        dataSetB.getSources().add( sourceC );        

        dataSetC.getSources().add( sourceA );
        dataSetC.getSources().add( sourceC );
        
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );
        dataSetService.addDataSet( dataSetC );
        
        Collection<DataSet> dataSets = dataSetService.getDataSetsBySource( sourceA );
        
        assertEquals( 2, dataSets.size() );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetC ) );
        
        dataSets = dataSetService.getDataSetsBySource( sourceB );        

        assertEquals( 2, dataSets.size() );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetB ) );

        dataSets = dataSetService.getDataSetsBySource( sourceC );
        
        assertEquals( 2, dataSets.size() );
        assertTrue( dataSets.contains( dataSetB ) );
        assertTrue( dataSets.contains( dataSetC ) );        
    }

    public void testGetDataSetsBySources()
    {
        Source sourceA = new DummySource( "A" );
        Source sourceB = new DummySource( "B" );
        Source sourceC = new DummySource( "C" );
        Source sourceD = new DummySource( "D" );
        
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );
        sourceStore.addSource( sourceD );
        
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );
        DataSet dataSetC = createDataSet( 'C', periodType );
        DataSet dataSetD = createDataSet( 'D', periodType );
        
        dataSetA.getSources().add( sourceA );
        dataSetA.getSources().add( sourceB );
        
        dataSetB.getSources().add( sourceB );
        dataSetB.getSources().add( sourceC );        

        dataSetC.getSources().add( sourceC );
        
        dataSetD.getSources().add( sourceD );
        
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );
        dataSetService.addDataSet( dataSetC );
        dataSetService.addDataSet( dataSetD );
        
        Collection<Source> sources = new HashSet<Source>();
        
        sources.add( sourceA );
        sources.add( sourceD );        
        
        Collection<DataSet> dataSets = dataSetService.getDataSetsBySources( sources );
        
        assertEquals( 2, dataSets.size() );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetD ) );
        
        sources.clear();
        
        sources.add( sourceB );
        sources.add( sourceC );

        dataSets = dataSetService.getDataSetsBySources( sources );
        
        assertEquals( 3, dataSets.size() );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetB ) );
        assertTrue( dataSets.contains( dataSetC ) );        
    }

    public void testGetSourcesAssociatedWithDataSet()
    {
        Source sourceA = new DummySource( "A" );
        Source sourceB = new DummySource( "B" );
        Source sourceC = new DummySource( "C" );
        Source sourceD = new DummySource( "D" );
        Source sourceE = new DummySource( "E" );
        Source sourceF = new DummySource( "F" );
        
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );
        sourceStore.addSource( sourceD );
        sourceStore.addSource( sourceE );
        sourceStore.addSource( sourceF );
        
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );
        
        dataSetA.getSources().add( sourceA );
        dataSetA.getSources().add( sourceB );
        dataSetA.getSources().add( sourceC );

        dataSetB.getSources().add( sourceC );
        dataSetB.getSources().add( sourceD );
        dataSetB.getSources().add( sourceE );       
        
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );

        Collection<Source> sources = new HashSet<Source>();
        
        sources.add( sourceA );
        sources.add( sourceB );
        sources.add( sourceD );
        sources.add( sourceE );
        
        assertEquals( 2, dataSetService.getSourcesAssociatedWithDataSet( dataSetA, sources ) );
        assertEquals( 2, dataSetService.getSourcesAssociatedWithDataSet( dataSetB, sources ) );
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

        dataSetService.addDataSet( dataSet1 );
        dataSetService.addDataSet( dataSet2 );

        Source source1 = new DummySource( "Source1" );
        Source source2 = new DummySource( "Source2" );
        sourceStore.addSource( source1 );
        sourceStore.addSource( source2 );

        FrequencyOverrideAssociation association = new FrequencyOverrideAssociation( dataSet1, source1, periodType3 );
        
        dataSetService.addFrequencyOverrideAssociation( association );        
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source1 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 0 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source2 ).size(), 0 );
        
        dataSetService.addFrequencyOverrideAssociation( new FrequencyOverrideAssociation( dataSet1, source2, periodType3 ) );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 2 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source1 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 0 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source2 ).size(), 1 );
        
        dataSetService.addFrequencyOverrideAssociation( new FrequencyOverrideAssociation( dataSet2, source1, periodType3 ) );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 2 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source1 ).size(), 2 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source2 ).size(), 1 );
        
        dataSetService.removeFrequencyOverrideAssociation( association );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet1 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source1 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsByDataSet( dataSet2 ).size(), 1 );
        assertEquals( dataSetService.getFrequencyOverrideAssociationsBySource( source2 ).size(), 1 );
    }
}
