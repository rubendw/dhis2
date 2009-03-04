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
public class DataElementCategoryServiceTest
    extends DhisConvenienceTest
{
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
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );

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
        
        int idA = categoryService.addDataElementCategory( categoryA );
        int idB = categoryService.addDataElementCategory( categoryB );
        int idC = categoryService.addDataElementCategory( categoryC );
        
        assertEquals( categoryA, categoryService.getDataElementCategory( idA ) );
        assertEquals( categoryB, categoryService.getDataElementCategory( idB ) );
        assertEquals( categoryC, categoryService.getDataElementCategory( idC ) );
        
        assertEquals( categoryOptions, categoryService.getDataElementCategory( idA ).getCategoryOptions() );
        assertEquals( categoryOptions, categoryService.getDataElementCategory( idB ).getCategoryOptions() );
        assertEquals( categoryOptions, categoryService.getDataElementCategory( idC ).getCategoryOptions() );        
    }
    
    public void testDelete()
    {
        categoryA = new DataElementCategory( "CategoryA", categoryOptions );
        categoryB = new DataElementCategory( "CategoryB", categoryOptions );
        categoryC = new DataElementCategory( "CategoryC", categoryOptions );
        
        int idA = categoryService.addDataElementCategory( categoryA );
        int idB = categoryService.addDataElementCategory( categoryB );
        int idC = categoryService.addDataElementCategory( categoryC );
        
        assertNotNull( categoryService.getDataElementCategory( idA ) );
        assertNotNull( categoryService.getDataElementCategory( idB ) );
        assertNotNull( categoryService.getDataElementCategory( idC ) );
        
        categoryService.deleteDataElementCategory( categoryA );

        assertNull( categoryService.getDataElementCategory( idA ) );
        assertNotNull( categoryService.getDataElementCategory( idB ) );
        assertNotNull( categoryService.getDataElementCategory( idC ) );

        categoryService.deleteDataElementCategory( categoryB );

        assertNull( categoryService.getDataElementCategory( idA ) );
        assertNull( categoryService.getDataElementCategory( idB ) );
        assertNotNull( categoryService.getDataElementCategory( idC ) );        
    }
    
    public void testGetAll()
    {
        categoryA = new DataElementCategory( "CategoryA", categoryOptions );
        categoryB = new DataElementCategory( "CategoryB", categoryOptions );
        categoryC = new DataElementCategory( "CategoryC", categoryOptions );

        categoryService.addDataElementCategory( categoryA );
        categoryService.addDataElementCategory( categoryB );
        categoryService.addDataElementCategory( categoryC );
        
        Collection<DataElementCategory> categories = categoryService.getAllDataElementCategories();
        
        assertEquals( 4, categories.size() ); // Including default
        assertTrue( categories.contains( categoryA ) );
        assertTrue( categories.contains( categoryB ) );
        assertTrue( categories.contains( categoryC ) );        
    }
}
