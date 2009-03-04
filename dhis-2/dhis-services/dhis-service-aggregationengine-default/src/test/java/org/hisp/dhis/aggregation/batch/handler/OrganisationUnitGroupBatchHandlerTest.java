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
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;

/**
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitGroupBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class OrganisationUnitGroupBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private OrganisationUnitGroup groupA;
    private OrganisationUnitGroup groupB;
    private OrganisationUnitGroup groupC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        organisationUnitGroupService = (OrganisationUnitGroupService) getBean( OrganisationUnitGroupService.ID );

        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupBatchHandler.class );

        batchHandler.init();
        
        groupA = createOrganisationUnitGroup( 'A' );
        groupB = createOrganisationUnitGroup( 'B' );
        groupC = createOrganisationUnitGroup( 'C' );
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
        batchHandler.addObject( groupA );
        batchHandler.addObject( groupB );
        batchHandler.addObject( groupC );

        batchHandler.flush();
        
        Collection<OrganisationUnitGroup> groups = organisationUnitGroupService.getAllOrganisationUnitGroups();
        
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
        assertTrue( groups.contains( groupC ) );
    }

    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( groupA, true );
        int idB = batchHandler.insertObject( groupB, true );
        int idC = batchHandler.insertObject( groupC, true );
        
        assertNotNull( organisationUnitGroupService.getOrganisationUnitGroup( idA ) );
        assertNotNull( organisationUnitGroupService.getOrganisationUnitGroup( idB ) );
        assertNotNull( organisationUnitGroupService.getOrganisationUnitGroup( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = organisationUnitGroupService.addOrganisationUnitGroup( groupA );

        groupA.setName( "UpdatedName" );
        
        batchHandler.updateObject( groupA );
        
        assertEquals( organisationUnitGroupService.getOrganisationUnitGroup( id ).getName(), "UpdatedName" );
    }

    public void testGetObjectIdentifier()
    {
        int referenceId = organisationUnitGroupService.addOrganisationUnitGroup( groupA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "OrganisationUnitGroupA" );

        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        organisationUnitGroupService.addOrganisationUnitGroup( groupA );
        
        assertTrue( batchHandler.objectExists( groupA ) );
        
        assertFalse( batchHandler.objectExists( groupB ) );
    }
}
