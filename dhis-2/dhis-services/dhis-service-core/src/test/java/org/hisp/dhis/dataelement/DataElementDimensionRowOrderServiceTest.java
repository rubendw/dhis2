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
public class DataElementDimensionRowOrderServiceTest
    extends DhisConvenienceTest
{
    private DataElementCategory categoryA;
    private DataElementCategory categoryB;
    
    private DataElementCategoryCombo categoryComboA;
    private DataElementCategoryCombo categoryComboB;    

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        dimensionRowOrderService = (DataElementDimensionRowOrderService) getBean( DataElementDimensionRowOrderService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );

        categoryA = new DataElementCategory( "CategoryA" );
        categoryB = new DataElementCategory( "CategoryB" );
        
        categoryService.addDataElementCategory( categoryA );
        categoryService.addDataElementCategory( categoryB );
        
        categoryComboA = new DataElementCategoryCombo( "CategoryComboA" );
        categoryComboB = new DataElementCategoryCombo( "CategoryComboB" );
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        categoryComboService.addDataElementCategoryCombo( categoryComboB );        
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGet()
    {
        DataElementDimensionRowOrder dimensionRowOrderA = new DataElementDimensionRowOrder( categoryComboA, categoryA, 1 );
        DataElementDimensionRowOrder dimensionRowOrderB = new DataElementDimensionRowOrder( categoryComboA, categoryB, 1 );
        DataElementDimensionRowOrder dimensionRowOrderC = new DataElementDimensionRowOrder( categoryComboB, categoryB, 1 );
        
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderA );
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderB );
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderC );
        
        assertEquals( dimensionRowOrderA, dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboA, categoryA ) );
        assertEquals( dimensionRowOrderB, dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboA, categoryB ) );
        assertEquals( dimensionRowOrderC, dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboB, categoryB ) );        
    }
    
    public void testDeleteByCategoryCombo()
    {
        DataElementDimensionRowOrder dimensionRowOrderA = new DataElementDimensionRowOrder( categoryComboA, categoryA, 1 );
        DataElementDimensionRowOrder dimensionRowOrderB = new DataElementDimensionRowOrder( categoryComboA, categoryB, 1 );
        DataElementDimensionRowOrder dimensionRowOrderC = new DataElementDimensionRowOrder( categoryComboB, categoryB, 1 );
        
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderA );
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderB );
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderC );
        
        dimensionRowOrderService.deleteDataElementDimensionRowOrder( categoryComboA );
        
        assertNull( dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboA, categoryA ) );
        assertNull( dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboA, categoryB ) );
        assertNotNull( dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboB, categoryB ) );    
    }
    
    public void testDeleteByCategory()
    {
        DataElementDimensionRowOrder dimensionRowOrderA = new DataElementDimensionRowOrder( categoryComboA, categoryA, 1 );
        DataElementDimensionRowOrder dimensionRowOrderB = new DataElementDimensionRowOrder( categoryComboA, categoryB, 1 );
        DataElementDimensionRowOrder dimensionRowOrderC = new DataElementDimensionRowOrder( categoryComboB, categoryB, 1 );
        
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderA );
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderB );
        dimensionRowOrderService.addDataElementDimensionRowOrder( dimensionRowOrderC );
        
        dimensionRowOrderService.deleteDataElementDimensionRowOrder( categoryA );
        
        assertNull( dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboA, categoryA ) );
        assertNotNull( dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboA, categoryB ) );
        assertNotNull( dimensionRowOrderService.getDataElementDimensionRowOrder( categoryComboB, categoryB ) );        
    }
}
