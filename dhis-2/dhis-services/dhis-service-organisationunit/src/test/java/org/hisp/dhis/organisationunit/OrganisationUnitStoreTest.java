package org.hisp.dhis.organisationunit;

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
import org.hisp.dhis.system.session.SessionUtil;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class OrganisationUnitStoreTest
    extends DhisConvenienceTest
{
    private OrganisationUnitStore organisationUnitStore;
    
    private SessionUtil sessionUtil;
    
    public void setUpTest()
    {
        organisationUnitStore = (OrganisationUnitStore) getBean( OrganisationUnitStore.ID );
        
        sessionUtil = (SessionUtil) getBean( SessionUtil.ID );
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    public void testAddGetOrganisationUnitLevel()
    {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel( 1, "National" );
        OrganisationUnitLevel levelB = new OrganisationUnitLevel( 2, "District" );
        
        int idA = organisationUnitStore.addOrganisationUnitLevel( levelA );
        int idB = organisationUnitStore.addOrganisationUnitLevel( levelB );
        
        assertEquals( levelA, organisationUnitStore.getOrganisationUnitLevel( idA ) );
        assertEquals( levelB, organisationUnitStore.getOrganisationUnitLevel( idB ) );        
    }
    
    public void testGetOrganisationUnitLevels()
    {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel( 1, "National" );
        OrganisationUnitLevel levelB = new OrganisationUnitLevel( 2, "District" );
        
        organisationUnitStore.addOrganisationUnitLevel( levelA );
        organisationUnitStore.addOrganisationUnitLevel( levelB );
        
        Collection<OrganisationUnitLevel> actual = organisationUnitStore.getOrganisationUnitLevels();
        
        assertNotNull( actual );
        assertEquals( 2, actual.size() );
        assertTrue( actual.contains( levelA ) );
        assertTrue( actual.contains( levelB ) );
    }
    
    public void testRemoveOrganisationUnitLevel()
    {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel( 1, "National" );
        OrganisationUnitLevel levelB = new OrganisationUnitLevel( 2, "District" );
        
        int idA = organisationUnitStore.addOrganisationUnitLevel( levelA );
        int idB = organisationUnitStore.addOrganisationUnitLevel( levelB );
        
        assertNotNull( organisationUnitStore.getOrganisationUnitLevel( idA ) );
        assertNotNull( organisationUnitStore.getOrganisationUnitLevel( idB ) );
        
        organisationUnitStore.deleteOrganisationUnitLevel( levelA );

        assertNull( organisationUnitStore.getOrganisationUnitLevel( idA ) );
        assertNotNull( organisationUnitStore.getOrganisationUnitLevel( idB ) );

        organisationUnitStore.deleteOrganisationUnitLevel( levelB );

        assertNull( organisationUnitStore.getOrganisationUnitLevel( idA ) );
        assertNull( organisationUnitStore.getOrganisationUnitLevel( idB ) );        
    }
    
    public void testRemoveOrganisationUnitLevels()
    {
        OrganisationUnitLevel levelA = new OrganisationUnitLevel( 1, "National" );
        OrganisationUnitLevel levelB = new OrganisationUnitLevel( 2, "District" );
        
        int idA = organisationUnitStore.addOrganisationUnitLevel( levelA );
        int idB = organisationUnitStore.addOrganisationUnitLevel( levelB );

        assertNotNull( organisationUnitStore.getOrganisationUnitLevel( idA ) );
        assertNotNull( organisationUnitStore.getOrganisationUnitLevel( idB ) );
        
        organisationUnitStore.deleteOrganisationUnitLevels();
        
        sessionUtil.clearCurrentSession();

        assertNull( organisationUnitStore.getOrganisationUnitLevel( idA ) );
        assertNull( organisationUnitStore.getOrganisationUnitLevel( idB ) ); 
    }
}
