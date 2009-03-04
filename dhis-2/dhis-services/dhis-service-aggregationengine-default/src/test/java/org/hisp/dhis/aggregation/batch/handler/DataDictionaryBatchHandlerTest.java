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
import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataDictionaryBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private DataDictionary dataDictionaryA;
    private DataDictionary dataDictionaryB;
    private DataDictionary dataDictionaryC;    

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        dataDictionaryService = (DataDictionaryService) getBean( DataDictionaryService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryBatchHandler.class );

        batchHandler.init();
        
        dataDictionaryA = createDataDictionary( 'A' );
        dataDictionaryB = createDataDictionary( 'B' );
        dataDictionaryC = createDataDictionary( 'C' );
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
        batchHandler.addObject( dataDictionaryA );
        batchHandler.addObject( dataDictionaryB );
        batchHandler.addObject( dataDictionaryC );
        
        batchHandler.flush();
        
        Collection<DataDictionary> dataDictionaries = dataDictionaryService.getAllDataDictionaries();
        
        assertTrue( dataDictionaries.contains( dataDictionaryA ) );
        assertTrue( dataDictionaries.contains( dataDictionaryB ) );
        assertTrue( dataDictionaries.contains( dataDictionaryC ) );
    }
    
    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( dataDictionaryA, true );
        int idB = batchHandler.insertObject( dataDictionaryB, true );
        int idC = batchHandler.insertObject( dataDictionaryC, true );
        
        assertNotNull( dataDictionaryService.getDataDictionary( idA ) );
        assertNotNull( dataDictionaryService.getDataDictionary( idB ) );
        assertNotNull( dataDictionaryService.getDataDictionary( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = dataDictionaryService.saveDataDictionary( dataDictionaryA );
        
        dataDictionaryA.setName( "UpdatedName" );
        
        batchHandler.updateObject( dataDictionaryA );
        
        assertEquals( dataDictionaryService.getDataDictionary( id ).getName(), "UpdatedName" );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = dataDictionaryService.saveDataDictionary( dataDictionaryA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "DataDictionaryA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        dataDictionaryService.saveDataDictionary( dataDictionaryA );
        
        assertTrue( batchHandler.objectExists( dataDictionaryA ) );
        
        assertFalse( batchHandler.objectExists( dataDictionaryB ) );
    }
}
