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
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorTypeBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class IndicatorTypeBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private IndicatorType indicatorTypeA;
    private IndicatorType indicatorTypeB;
    private IndicatorType indicatorTypeC;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( IndicatorTypeBatchHandler.class );

        batchHandler.init();
        
        indicatorTypeA = createIndicatorType( 'A' );
        indicatorTypeB = createIndicatorType( 'B' );
        indicatorTypeC = createIndicatorType( 'C' );
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
        batchHandler.addObject( indicatorTypeA );
        batchHandler.addObject( indicatorTypeB );
        batchHandler.addObject( indicatorTypeC );

        batchHandler.flush();
        
        Collection<IndicatorType> indicatorTypes = indicatorService.getAllIndicatorTypes();
        
        assertTrue( indicatorTypes.contains( indicatorTypeA ) );
        assertTrue( indicatorTypes.contains( indicatorTypeB ) );
        assertTrue( indicatorTypes.contains( indicatorTypeC ) );
    }

    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( indicatorTypeA, true );
        int idB = batchHandler.insertObject( indicatorTypeB, true );
        int idC = batchHandler.insertObject( indicatorTypeC, true );
        
        assertNotNull( indicatorService.getIndicatorType( idA ) );
        assertNotNull( indicatorService.getIndicatorType( idB ) );
        assertNotNull( indicatorService.getIndicatorType( idC ) );
    }    

    public void testUpdateObject()
    {
        int id = indicatorService.addIndicatorType( indicatorTypeA );
        
        indicatorTypeA.setName( "UpdatedName" );
        
        batchHandler.updateObject( indicatorTypeA );
        
        assertEquals( indicatorService.getIndicatorType( id ).getName(), "UpdatedName" );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = indicatorService.addIndicatorType( indicatorTypeA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "IndicatorTypeA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        indicatorService.addIndicatorType( indicatorTypeA );
        
        assertTrue( batchHandler.objectExists( indicatorTypeA ) );
        
        assertFalse( batchHandler.objectExists( indicatorTypeB ) );
    }
}
