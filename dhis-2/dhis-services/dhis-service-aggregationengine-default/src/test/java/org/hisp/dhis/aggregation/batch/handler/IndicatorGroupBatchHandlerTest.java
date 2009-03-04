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
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorGroupBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class IndicatorGroupBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private IndicatorGroup groupA;
    private IndicatorGroup groupB;
    private IndicatorGroup groupC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupBatchHandler.class );

        batchHandler.init();
        
        groupA = createIndicatorGroup( 'A' );
        groupB = createIndicatorGroup( 'B' );
        groupC = createIndicatorGroup( 'C' );
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
        batchHandler.addObject( groupA );
        batchHandler.addObject( groupB );
        batchHandler.addObject( groupC );

        batchHandler.flush();
        
        Collection<IndicatorGroup> groups = indicatorService.getAllIndicatorGroups();
        
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
        assertTrue( groups.contains( groupC ) );
    }

    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( groupA, true );
        int idB = batchHandler.insertObject( groupB, true );
        int idC = batchHandler.insertObject( groupC, true );
        
        assertNotNull( indicatorService.getIndicatorGroup( idA ) );
        assertNotNull( indicatorService.getIndicatorGroup( idB ) );
        assertNotNull( indicatorService.getIndicatorGroup( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = indicatorService.addIndicatorGroup( groupA );
        
        groupA.setName( "UpdatedName" );
        
        batchHandler.updateObject( groupA );
        
        assertEquals( indicatorService.getIndicatorGroup( id ).getName(), "UpdatedName" );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = indicatorService.addIndicatorGroup( groupA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "IndicatorGroupA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        indicatorService.addIndicatorGroup( groupA );
        
        assertTrue( batchHandler.objectExists( groupA ) );
        
        assertFalse( batchHandler.objectExists( groupB ) );
    }
}
