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

import org.hisp.dhis.DhisConvenienceTest;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementCategoryOptionStoreTest
    extends DhisConvenienceTest
{
    private DataElementCategoryOptionStore categoryOptionStore;
    
    private DataElementCategoryOption categoryOptionA;
    private DataElementCategoryOption categoryOptionB;
    private DataElementCategoryOption categoryOptionC;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        categoryOptionStore = (DataElementCategoryOptionStore) getBean( DataElementCategoryOptionStore.ID );    
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGet()
    {
        categoryOptionA = new DataElementCategoryOption( "CategoryOptionA" );
        categoryOptionB = new DataElementCategoryOption( "CategoryOptionB" );
        categoryOptionC = new DataElementCategoryOption( "CategoryOptionC" );
        
        int idA = categoryOptionStore.addDataElementCategoryOption( categoryOptionA );
        int idB = categoryOptionStore.addDataElementCategoryOption( categoryOptionB );
        int idC = categoryOptionStore.addDataElementCategoryOption( categoryOptionC );
        
        assertEquals( categoryOptionA, categoryOptionStore.getDataElementCategoryOption( idA ) );
        assertEquals( categoryOptionB, categoryOptionStore.getDataElementCategoryOption( idB ) );
        assertEquals( categoryOptionC, categoryOptionStore.getDataElementCategoryOption( idC ) );
    }
    
    public void testDelete()
    {
        categoryOptionA = new DataElementCategoryOption( "CategoryOptionA" );
        categoryOptionB = new DataElementCategoryOption( "CategoryOptionB" );
        categoryOptionC = new DataElementCategoryOption( "CategoryOptionC" );

        int idA = categoryOptionStore.addDataElementCategoryOption( categoryOptionA );
        int idB = categoryOptionStore.addDataElementCategoryOption( categoryOptionB );
        int idC = categoryOptionStore.addDataElementCategoryOption( categoryOptionC );
        
        assertNotNull( categoryOptionStore.getDataElementCategoryOption( idA ) );
        assertNotNull( categoryOptionStore.getDataElementCategoryOption( idB ) );
        assertNotNull( categoryOptionStore.getDataElementCategoryOption( idC ) );
        
        categoryOptionStore.deleteDataElementCategoryOption( categoryOptionA );

        assertNull( categoryOptionStore.getDataElementCategoryOption( idA ) );
        assertNotNull( categoryOptionStore.getDataElementCategoryOption( idB ) );
        assertNotNull( categoryOptionStore.getDataElementCategoryOption( idC ) );

        categoryOptionStore.deleteDataElementCategoryOption( categoryOptionB );

        assertNull( categoryOptionStore.getDataElementCategoryOption( idA ) );
        assertNull( categoryOptionStore.getDataElementCategoryOption( idB ) );
        assertNotNull( categoryOptionStore.getDataElementCategoryOption( idC ) );
    }
    
    public void testGetAll()
    {
        categoryOptionA = new DataElementCategoryOption( "CategoryOptionA" );
        categoryOptionB = new DataElementCategoryOption( "CategoryOptionB" );
        categoryOptionC = new DataElementCategoryOption( "CategoryOptionC" );

        categoryOptionStore.addDataElementCategoryOption( categoryOptionA );
        categoryOptionStore.addDataElementCategoryOption( categoryOptionB );
        categoryOptionStore.addDataElementCategoryOption( categoryOptionC );
        
        Collection<DataElementCategoryOption> categoryOptions = categoryOptionStore.getAllDataElementCategoryOptions();
        
        assertEquals( 4, categoryOptions.size() ); // Including default
        assertTrue( categoryOptions.contains( categoryOptionA ) );
        assertTrue( categoryOptions.contains( categoryOptionB ) );
        assertTrue( categoryOptions.contains( categoryOptionC ) );        
    }
}
