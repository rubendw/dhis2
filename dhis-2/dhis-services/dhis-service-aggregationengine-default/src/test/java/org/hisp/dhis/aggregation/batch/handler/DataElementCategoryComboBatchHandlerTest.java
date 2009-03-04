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
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementCategoryComboBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private DataElementCategoryCombo categoryComboA;
    private DataElementCategoryCombo categoryComboB;
    private DataElementCategoryCombo categoryComboC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryComboBatchHandler.class );

        batchHandler.init();
        
        categoryComboA = new DataElementCategoryCombo( "categoryComboA" );
        categoryComboB = new DataElementCategoryCombo( "categoryComboB" );
        categoryComboC = new DataElementCategoryCombo( "categoryComboC" );
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
        batchHandler.addObject( categoryComboA );
        batchHandler.addObject( categoryComboB );
        batchHandler.addObject( categoryComboC );
        
        batchHandler.flush();
        
        Collection<DataElementCategoryCombo> categoryCombos = categoryComboService.getAllDataElementCategoryCombos();
        
        assertTrue( categoryCombos.contains( categoryComboA  ) );
        assertTrue( categoryCombos.contains( categoryComboB  ) );
        assertTrue( categoryCombos.contains( categoryComboC  ) );
    }
    
    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( categoryComboA, true );
        int idB = batchHandler.insertObject( categoryComboB, true );
        int idC = batchHandler.insertObject( categoryComboC, true );
        
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idA ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idB ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        categoryComboA.setName( "UpdatedName" );
        
        batchHandler.updateObject( categoryComboA );
        
        assertEquals( "UpdatedName", categoryComboService.getDataElementCategoryCombo( id ).getName() );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "categoryComboA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        assertTrue( batchHandler.objectExists( categoryComboA ) );
        
        assertFalse( batchHandler.objectExists( categoryComboB ) );
    }
}
