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
public class DataElementCategoryOptionComboServiceTest
    extends DhisConvenienceTest
{
    private DataElementCategory categoryA;
    private DataElementCategory categoryB;
        
    private DataElementCategoryCombo categoryComboA;
    private DataElementCategoryCombo categoryComboB;
    
    private DataElementCategoryOption categoryOptionA;
    private DataElementCategoryOption categoryOptionB;
    private DataElementCategoryOption categoryOptionC;
    private DataElementCategoryOption categoryOptionD;
    
    private DataElementCategoryOptionCombo categoryOptionComboA;
    private DataElementCategoryOptionCombo categoryOptionComboB;
    private DataElementCategoryOptionCombo categoryOptionComboC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        categoryOptionA = new DataElementCategoryOption( "Male" );
        categoryOptionB = new DataElementCategoryOption( "Female" );
        categoryOptionC = new DataElementCategoryOption( "0-20" );
        categoryOptionD = new DataElementCategoryOption( "20-100" );

        categoryOptionService.addDataElementCategoryOption( categoryOptionA );
        categoryOptionService.addDataElementCategoryOption( categoryOptionB );
        categoryOptionService.addDataElementCategoryOption( categoryOptionC );
        categoryOptionService.addDataElementCategoryOption( categoryOptionD );
                
        categoryA = new DataElementCategory( "Gender" );
        categoryB = new DataElementCategory( "Agegroup" );
        
        categoryA.getCategoryOptions().add( categoryOptionA );
        categoryA.getCategoryOptions().add( categoryOptionB );        
        categoryB.getCategoryOptions().add( categoryOptionC );
        categoryB.getCategoryOptions().add( categoryOptionD );
        
        categoryService.addDataElementCategory( categoryA );
        categoryService.addDataElementCategory( categoryB );
        
        categoryComboA = new DataElementCategoryCombo( "GenderAgegroup" );
        categoryComboB = new DataElementCategoryCombo( "Gender" );
        
        categoryComboA.getCategories().add( categoryA );
        categoryComboA.getCategories().add( categoryB );
        categoryComboB.getCategories().add( categoryA );
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        categoryComboService.addDataElementCategoryCombo( categoryComboB ); 
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGetDataElementCategoryOptionCombo()
    {
        categoryOptionComboA = new DataElementCategoryOptionCombo();
        
        Set<DataElementCategoryOption> categoryOptions = new HashSet<DataElementCategoryOption>();
        
        categoryOptions.add( categoryOptionA );
        categoryOptions.add( categoryOptionB );        
        
        categoryOptionComboA.setCategoryCombo( categoryComboA );
        categoryOptionComboA.setCategoryOptions( categoryOptions );        
        
        int id = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboA );
        
        categoryOptionComboA = categoryOptionComboService.getDataElementCategoryOptionCombo( id );
        
        assertNotNull( categoryOptionComboA );
        assertEquals( categoryComboA, categoryOptionComboA.getCategoryCombo() );
        assertEquals( categoryOptions, categoryOptionComboA.getCategoryOptions() );
    }
    
    public void testUpdateGetDataElementCategoryOptionCombo()
    {
        categoryOptionComboA = new DataElementCategoryOptionCombo();
        
        Set<DataElementCategoryOption> categoryOptions = new HashSet<DataElementCategoryOption>();
        
        categoryOptions.add( categoryOptionA );
        categoryOptions.add( categoryOptionB );        
        
        categoryOptionComboA.setCategoryCombo( categoryComboA );
        categoryOptionComboA.setCategoryOptions( categoryOptions );        
        
        int id = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboA );
        
        categoryOptionComboA = categoryOptionComboService.getDataElementCategoryOptionCombo( id );
        
        assertNotNull( categoryOptionComboA );
        assertEquals( categoryComboA, categoryOptionComboA.getCategoryCombo() );
        assertEquals( categoryOptions, categoryOptionComboA.getCategoryOptions() );
        
        categoryOptionComboA.setCategoryCombo( categoryComboB );
        
        categoryOptionComboService.updateDataElementCategoryOptionCombo( categoryOptionComboA );
        
        categoryOptionComboA = categoryOptionComboService.getDataElementCategoryOptionCombo( id );
        
        assertNotNull( categoryOptionComboA );
        assertEquals( categoryComboB, categoryOptionComboA.getCategoryCombo() );
        assertEquals( categoryOptions, categoryOptionComboA.getCategoryOptions() );
    }
    
    public void testDeleteDataElementCategoryOptionCombo()
    {
        categoryOptionComboA = new DataElementCategoryOptionCombo();
        categoryOptionComboB = new DataElementCategoryOptionCombo();
        categoryOptionComboC = new DataElementCategoryOptionCombo();
        
        int idA = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboA );
        int idB = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboB );
        int idC = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboC );
        
        assertNotNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idA ) );
        assertNotNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idB ) );
        assertNotNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idC ) );
        
        categoryOptionComboService.deleteDataElementCategoryOptionCombo( categoryOptionComboService.getDataElementCategoryOptionCombo( idA ) );

        assertNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idA ) );
        assertNotNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idB ) );
        assertNotNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idC ) );

        categoryOptionComboService.deleteDataElementCategoryOptionCombo( categoryOptionComboService.getDataElementCategoryOptionCombo( idB ) );

        assertNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idA ) );
        assertNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idB ) );
        assertNotNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idC ) );

        categoryOptionComboService.deleteDataElementCategoryOptionCombo( categoryOptionComboService.getDataElementCategoryOptionCombo( idC ) );

        assertNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idA ) );
        assertNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idB ) );
        assertNull( categoryOptionComboService.getDataElementCategoryOptionCombo( idC ) );
    }
    
    public void testGetAllDataElementCategoryOptionCombos()
    {
        categoryOptionComboA = new DataElementCategoryOptionCombo();
        categoryOptionComboB = new DataElementCategoryOptionCombo();
        categoryOptionComboC = new DataElementCategoryOptionCombo();
        
        categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboA );
        categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboB );
        categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboC );
        
        Collection<DataElementCategoryOptionCombo> categoryOptionCombos = 
            categoryOptionComboService.getAllDataElementCategoryOptionCombos();
        
        assertNotNull( categoryOptionCombos );
        assertEquals( 4, categoryOptionCombos.size() ); // Including default category option combo
    }
}
