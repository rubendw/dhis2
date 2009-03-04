package org.hisp.dhis.dataelement;

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

import org.hisp.dhis.DhisConvenienceTest;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementDimensionColumnOrderStoreTest
    extends DhisConvenienceTest
{
    private DataElementDimensionColumnOrderStore dimensionColumnOrderStore;
    
    private DataElementCategory categoryA;
    private DataElementCategory categoryB;
    
    private DataElementCategoryOption categoryOptionA;
    private DataElementCategoryOption categoryOptionB; 

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        dimensionColumnOrderStore = (DataElementDimensionColumnOrderStore) getBean( DataElementDimensionColumnOrderStore.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryA = new DataElementCategory( "CategoryA" );
        categoryB = new DataElementCategory( "CategoryB" );
        
        categoryService.addDataElementCategory( categoryA );
        categoryService.addDataElementCategory( categoryB );
                
        categoryOptionA = new DataElementCategoryOption( "CategoryOptionA" );
        categoryOptionB = new DataElementCategoryOption( "CategoryOptionB" );
        
        categoryOptionService.addDataElementCategoryOption( categoryOptionA );
        categoryOptionService.addDataElementCategoryOption( categoryOptionB );        
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGet()
    {
        DataElementDimensionColumnOrder dimensionColumnOrderA = new DataElementDimensionColumnOrder( categoryA, categoryOptionA, 1 );
        DataElementDimensionColumnOrder dimensionColumnOrderB = new DataElementDimensionColumnOrder( categoryA, categoryOptionB, 1 );
        DataElementDimensionColumnOrder dimensionColumnOrderC = new DataElementDimensionColumnOrder( categoryB, categoryOptionB, 1 );
        
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderA );
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderB );
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderC );
        
        assertEquals( dimensionColumnOrderA, dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryA, categoryOptionA ) );
        assertEquals( dimensionColumnOrderB, dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryA, categoryOptionB ) );
        assertEquals( dimensionColumnOrderC, dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryB, categoryOptionB ) );        
    }
    
    public void testDeleteByCategory()
    {
        DataElementDimensionColumnOrder dimensionColumnOrderA = new DataElementDimensionColumnOrder( categoryA, categoryOptionA, 1 );
        DataElementDimensionColumnOrder dimensionColumnOrderB = new DataElementDimensionColumnOrder( categoryA, categoryOptionB, 1 );
        DataElementDimensionColumnOrder dimensionColumnOrderC = new DataElementDimensionColumnOrder( categoryB, categoryOptionB, 1 );
        
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderA );
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderB );
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderC );
        
        dimensionColumnOrderStore.deleteDataElementDimensionColumnOrder( categoryA );
        
        assertNull( dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryA, categoryOptionA ) );
        assertNull( dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryA, categoryOptionB ) );
        assertNotNull( dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryB, categoryOptionB ) );
    }
    
    public void testDeleteByCategoryOption()
    {
        DataElementDimensionColumnOrder dimensionColumnOrderA = new DataElementDimensionColumnOrder( categoryA, categoryOptionA, 1 );
        DataElementDimensionColumnOrder dimensionColumnOrderB = new DataElementDimensionColumnOrder( categoryA, categoryOptionB, 1 );
        DataElementDimensionColumnOrder dimensionColumnOrderC = new DataElementDimensionColumnOrder( categoryB, categoryOptionB, 1 );

        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderA );
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderB );
        dimensionColumnOrderStore.addDataElementDimensionColumnOrder( dimensionColumnOrderC );
        
        dimensionColumnOrderStore.deleteDataElementDimensionColumnOrder( categoryOptionA );
        
        assertNull( dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryA, categoryOptionA ) );
        assertNotNull( dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryA, categoryOptionB ) );
        assertNotNull( dimensionColumnOrderStore.getDataElementDimensionColumnOrder( categoryB, categoryOptionB ) );
    }
}
