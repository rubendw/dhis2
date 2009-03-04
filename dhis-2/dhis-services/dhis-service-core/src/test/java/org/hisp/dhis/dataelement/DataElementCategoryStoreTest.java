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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.DhisConvenienceTest;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementCategoryStoreTest
    extends DhisConvenienceTest
{
    private DataElementCategoryStore categoryStore;
    
    private DataElementCategoryOption categoryOptionA;
    private DataElementCategoryOption categoryOptionB;
    private DataElementCategoryOption categoryOptionC;
    
    private DataElementCategory categoryA;
    private DataElementCategory categoryB;
    private DataElementCategory categoryC;
    
    private Set<DataElementCategoryOption> categoryOptions;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryStore = (DataElementCategoryStore) getBean( DataElementCategoryStore.ID );

        categoryOptionA = new DataElementCategoryOption( "CategoryOptionA" );
        categoryOptionB = new DataElementCategoryOption( "CategoryOptionB" );
        categoryOptionC = new DataElementCategoryOption( "CategoryOptionC" );
        
        categoryOptionService.addDataElementCategoryOption( categoryOptionA );
        categoryOptionService.addDataElementCategoryOption( categoryOptionB );
        categoryOptionService.addDataElementCategoryOption( categoryOptionC );
        
        categoryOptions = new HashSet<DataElementCategoryOption>();
        
        categoryOptions.add( categoryOptionA );
        categoryOptions.add( categoryOptionB );
        categoryOptions.add( categoryOptionC );         
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGet()
    {
        categoryA = new DataElementCategory( "CategoryA", categoryOptions );
        categoryB = new DataElementCategory( "CategoryB", categoryOptions );
        categoryC = new DataElementCategory( "CategoryC", categoryOptions );
        
        int idA = categoryStore.addDataElementCategory( categoryA );
        int idB = categoryStore.addDataElementCategory( categoryB );
        int idC = categoryStore.addDataElementCategory( categoryC );
        
        assertEquals( categoryA, categoryStore.getDataElementCategory( idA ) );
        assertEquals( categoryB, categoryStore.getDataElementCategory( idB ) );
        assertEquals( categoryC, categoryStore.getDataElementCategory( idC ) );
        
        assertEquals( categoryOptions, categoryStore.getDataElementCategory( idA ).getCategoryOptions() );
        assertEquals( categoryOptions, categoryStore.getDataElementCategory( idB ).getCategoryOptions() );
        assertEquals( categoryOptions, categoryStore.getDataElementCategory( idC ).getCategoryOptions() );        
    }
    
    public void testDelete()
    {
        categoryA = new DataElementCategory( "CategoryA", categoryOptions );
        categoryB = new DataElementCategory( "CategoryB", categoryOptions );
        categoryC = new DataElementCategory( "CategoryC", categoryOptions );
        
        int idA = categoryStore.addDataElementCategory( categoryA );
        int idB = categoryStore.addDataElementCategory( categoryB );
        int idC = categoryStore.addDataElementCategory( categoryC );
        
        assertNotNull( categoryStore.getDataElementCategory( idA ) );
        assertNotNull( categoryStore.getDataElementCategory( idB ) );
        assertNotNull( categoryStore.getDataElementCategory( idC ) );
        
        categoryStore.deleteDataElementCategory( categoryA );

        assertNull( categoryStore.getDataElementCategory( idA ) );
        assertNotNull( categoryStore.getDataElementCategory( idB ) );
        assertNotNull( categoryStore.getDataElementCategory( idC ) );

        categoryStore.deleteDataElementCategory( categoryB );

        assertNull( categoryStore.getDataElementCategory( idA ) );
        assertNull( categoryStore.getDataElementCategory( idB ) );
        assertNotNull( categoryStore.getDataElementCategory( idC ) );        
    }
    
    public void testGetAll()
    {
        categoryA = new DataElementCategory( "CategoryA", categoryOptions );
        categoryB = new DataElementCategory( "CategoryB", categoryOptions );
        categoryC = new DataElementCategory( "CategoryC", categoryOptions );

        categoryStore.addDataElementCategory( categoryA );
        categoryStore.addDataElementCategory( categoryB );
        categoryStore.addDataElementCategory( categoryC );
        
        Collection<DataElementCategory> categories = categoryStore.getAllDataElementCategories();
        
        assertEquals( 4, categories.size() ); // Including default
        assertTrue( categories.contains( categoryA ) );
        assertTrue( categories.contains( categoryB ) );
        assertTrue( categories.contains( categoryC ) );        
    }
}
