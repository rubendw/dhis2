package org.hisp.dhis.datamart;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.aggregation.DataElementCategoryOptionComboName;
import org.hisp.dhis.aggregation.GroupSetStructure;
import org.hisp.dhis.aggregation.OrganisationUnitStructure;

/**
 * @author Lars Helge Overland
 * @version $Id: DataMartStoreTest.java 5514 2008-08-04 10:48:07Z larshelg $
 */
public class DataMartStoreTest
    extends DhisSpringTest
{
    private DataMartStore dataMartStore;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
        throws Exception
    {
        dataMartStore = (DataMartStore) getBean( DataMartStore.ID );
    }
    
    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testOrganisationUnitStructure()
    {
        OrganisationUnitStructure structure1 = new OrganisationUnitStructure();
        OrganisationUnitStructure structure2 = new OrganisationUnitStructure();
        OrganisationUnitStructure structure3 = new OrganisationUnitStructure();
        
        structure1.setIdLevel1( 1 );
        structure2.setIdLevel1( 1 );
        structure2.setIdLevel2( 2 );
        structure3.setIdLevel1( 1 );
        structure3.setIdLevel2( 2 );
        structure3.setIdLevel3( 3 );

        structure1.setGeoCodeLevel1( "A" );
        structure2.setGeoCodeLevel1( "A" );
        structure2.setGeoCodeLevel1( "B" );
        structure3.setGeoCodeLevel1( "A" );
        structure3.setGeoCodeLevel1( "B" );
        structure3.setGeoCodeLevel1( "C" );
        
        dataMartStore.addOrganisationUnitStructure( structure1 );
        dataMartStore.addOrganisationUnitStructure( structure2 );
        dataMartStore.addOrganisationUnitStructure( structure3 );
                
        assertEquals( 3, dataMartStore.getOrganisationUnitStructures().size() );

        assertEquals( 3, dataMartStore.deleteOrganisationUnitStructures() );

        assertEquals( 0, dataMartStore.getOrganisationUnitStructures().size() );
    }

    public void testGroupSetStructure()
    {
        GroupSetStructure structure1 = new GroupSetStructure( 1, 1, 1 );
        GroupSetStructure structure2 = new GroupSetStructure( 2, 2, 2 );
        GroupSetStructure structure3 = new GroupSetStructure( 3, 3, 3 );

        dataMartStore.addGroupSetStructure( structure1 );
        dataMartStore.addGroupSetStructure( structure2 );
        dataMartStore.addGroupSetStructure( structure3 );

        assertEquals( 3, dataMartStore.getGroupSetStructures().size() );

        assertEquals( 3, dataMartStore.deleteGroupSetStructures() );
        
        assertEquals( 0, dataMartStore.getGroupSetStructures().size() );
    }
    
    public void testDataElementCategoryOptionComboName()
    {
        DataElementCategoryOptionComboName name1 = new DataElementCategoryOptionComboName( 1, "A" );
        DataElementCategoryOptionComboName name2 = new DataElementCategoryOptionComboName( 2, "B" );
        DataElementCategoryOptionComboName name3 = new DataElementCategoryOptionComboName( 3, "C" );
        
        dataMartStore.addDataElementCategoryOptionComboName( name1 );
        dataMartStore.addDataElementCategoryOptionComboName( name2 );
        dataMartStore.addDataElementCategoryOptionComboName( name3 );
        
        assertEquals( 3, dataMartStore.getDataElementCategoryOptionComboNames().size() );
        
        assertEquals( 3, dataMartStore.deleteDataElementCategoryOptionComboNames() );

        assertEquals( 0, dataMartStore.getDataElementCategoryOptionComboNames().size() );
    }    
}
