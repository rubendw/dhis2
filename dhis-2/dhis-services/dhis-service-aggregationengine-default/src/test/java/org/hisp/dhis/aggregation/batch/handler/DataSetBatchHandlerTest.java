package org.hisp.dhis.aggregation.batch.handler;

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
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: DataSetBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class DataSetBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private DataSet dataSetA;
    private DataSet dataSetB;
    private DataSet dataSetC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        dataSetService = (DataSetService) getBean( DataSetService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( DataSetBatchHandler.class );

        batchHandler.init();
        
        PeriodType periodType = periodService.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        dataSetA = createDataSet( 'A', periodType );
        dataSetB = createDataSet( 'B', periodType );
        dataSetC = createDataSet( 'C', periodType );        
    }

    public void tearDownTest()
    {
        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testAddObject()
    {
        batchHandler.addObject( dataSetA );
        batchHandler.addObject( dataSetB );
        batchHandler.addObject( dataSetC );

        batchHandler.flush();
        
        Collection<DataSet> dataSets = dataSetService.getAllDataSets();
        
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetB ) );
        assertTrue( dataSets.contains( dataSetC ) );
    }
    
    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( dataSetA, true );
        int idB = batchHandler.insertObject( dataSetB, true );
        int idC = batchHandler.insertObject( dataSetC, true );
        
        assertNotNull( dataSetService.getDataSet( idA ) );
        assertNotNull( dataSetService.getDataSet( idB ) );
        assertNotNull( dataSetService.getDataSet( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = dataSetService.addDataSet( dataSetA );
        
        dataSetA.setName( "UpdatedName" );
        
        batchHandler.updateObject( dataSetA );
        
        assertEquals( dataSetService.getDataSet( id ).getName(), "UpdatedName" );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = dataSetService.addDataSet( dataSetA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "DataSetA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        dataSetService.addDataSet( dataSetA );
        
        assertTrue( batchHandler.objectExists( dataSetA ) );
        
        assertFalse( batchHandler.objectExists( dataSetB ) );
    }
}
