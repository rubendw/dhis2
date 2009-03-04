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
public class DataElementCategoryComboServiceTest
    extends DhisConvenienceTest
{
    private DataElementCategoryCombo categoryComboA;
    private DataElementCategoryCombo categoryComboB;
    private DataElementCategoryCombo categoryComboC;
    
    private DataElementCategory categoryA;
    private DataElementCategory categoryB;
    private DataElementCategory categoryC;
    
    private Set<DataElementCategory> categories;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        
        categories = new HashSet<DataElementCategory>();
        
        categoryA = new DataElementCategory( "CategoryA" );
        categoryB = new DataElementCategory( "CategoryB" );
        categoryC = new DataElementCategory( "CategoryC" );
        
        categoryService.addDataElementCategory( categoryA );
        categoryService.addDataElementCategory( categoryB );
        categoryService.addDataElementCategory( categoryC );
        
        categories.add( categoryA );
        categories.add( categoryB );
        categories.add( categoryC );        
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGet()
    {
        categoryComboA = new DataElementCategoryCombo( "CategoryComboA", categories );
        categoryComboB = new DataElementCategoryCombo( "CategoryComboB", categories );
        categoryComboC = new DataElementCategoryCombo( "CategoryComboC", categories );
        
        int idA = categoryComboService.addDataElementCategoryCombo( categoryComboA );
        int idB = categoryComboService.addDataElementCategoryCombo( categoryComboB );
        int idC = categoryComboService.addDataElementCategoryCombo( categoryComboC );
        
        assertEquals( categoryComboA, categoryComboService.getDataElementCategoryCombo( idA ) );
        assertEquals( categoryComboB, categoryComboService.getDataElementCategoryCombo( idB ) );
        assertEquals( categoryComboC, categoryComboService.getDataElementCategoryCombo( idC ) );
        
        assertEquals( categories, categoryComboService.getDataElementCategoryCombo( idA ).getCategories() );
        assertEquals( categories, categoryComboService.getDataElementCategoryCombo( idB ).getCategories() );
        assertEquals( categories, categoryComboService.getDataElementCategoryCombo( idC ).getCategories() );        
    }
    
    public void testDelete()
    {
        categoryComboA = new DataElementCategoryCombo( "CategoryComboA", categories );
        categoryComboB = new DataElementCategoryCombo( "CategoryComboB", categories );
        categoryComboC = new DataElementCategoryCombo( "CategoryComboC", categories );
        
        int idA = categoryComboService.addDataElementCategoryCombo( categoryComboA );
        int idB = categoryComboService.addDataElementCategoryCombo( categoryComboB );
        int idC = categoryComboService.addDataElementCategoryCombo( categoryComboC );
        
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idA ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idB ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idC ) );
        
        categoryComboService.deleteDataElementCategoryCombo( categoryComboA );

        assertNull( categoryComboService.getDataElementCategoryCombo( idA ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idB ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idC ) );
        
        categoryComboService.deleteDataElementCategoryCombo( categoryComboB );

        assertNull( categoryComboService.getDataElementCategoryCombo( idA ) );
        assertNull( categoryComboService.getDataElementCategoryCombo( idB ) );
        assertNotNull( categoryComboService.getDataElementCategoryCombo( idC ) );        
    }
    
    public void testGetAll()
    {
        categoryComboA = new DataElementCategoryCombo( "CategoryComboA", categories );
        categoryComboB = new DataElementCategoryCombo( "CategoryComboB", categories );
        categoryComboC = new DataElementCategoryCombo( "CategoryComboC", categories );
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        categoryComboService.addDataElementCategoryCombo( categoryComboB );
        categoryComboService.addDataElementCategoryCombo( categoryComboC );
        
        Collection<DataElementCategoryCombo> categoryCombos = categoryComboService.getAllDataElementCategoryCombos();
        
        assertEquals( 4, categoryCombos.size() ); // Including default
        assertTrue( categoryCombos.contains( categoryComboA ) );
        assertTrue( categoryCombos.contains( categoryComboB ) );
        assertTrue( categoryCombos.contains( categoryComboC ) );        
    }
}
