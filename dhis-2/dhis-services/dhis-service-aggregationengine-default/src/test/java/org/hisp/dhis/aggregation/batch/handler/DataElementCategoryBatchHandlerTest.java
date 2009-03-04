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
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementCategoryBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private DataElementCategory categoryA;
    private DataElementCategory categoryB;
    private DataElementCategory categoryC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryBatchHandler.class );
        
        batchHandler.init();
        
        categoryA = new DataElementCategory( "categoryA" );
        categoryB = new DataElementCategory( "categoryB" );
        categoryC = new DataElementCategory( "categoryC" );
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
        batchHandler.addObject( categoryA );
        batchHandler.addObject( categoryB );
        batchHandler.addObject( categoryC );
        
        batchHandler.flush();
        
        Collection<DataElementCategory> categories = categoryService.getAllDataElementCategories();
        
        assertTrue( categories.contains( categoryA ) );
        assertTrue( categories.contains( categoryB ) );
        assertTrue( categories.contains( categoryC ) );        
    }
    
    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( categoryA, true );
        int idB = batchHandler.insertObject( categoryB, true );
        int idC = batchHandler.insertObject( categoryC, true );
        
        assertNotNull( categoryService.getDataElementCategory( idA ) );
        assertNotNull( categoryService.getDataElementCategory( idB ) );
        assertNotNull( categoryService.getDataElementCategory( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = categoryService.addDataElementCategory( categoryA );
        
        categoryA.setName( "updatedName" );
        
        batchHandler.updateObject( categoryA );
        
        assertEquals( "updatedName", categoryService.getDataElementCategory( id ).getName() );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = categoryService.addDataElementCategory( categoryA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "categoryA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        categoryService.addDataElementCategory( categoryA );
        
        assertTrue( batchHandler.objectExists( categoryA ) );
        
        assertFalse( batchHandler.objectExists( categoryB ) );
    }
}
