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
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;

/**
 * @author Lars Helge Overland
 * @version $Id: GroupSetBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class GroupSetBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private OrganisationUnitGroupSet groupSetA;
    private OrganisationUnitGroupSet groupSetB;
    private OrganisationUnitGroupSet groupSetC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        organisationUnitGroupService = (OrganisationUnitGroupService) getBean( OrganisationUnitGroupService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( GroupSetBatchHandler.class );

        batchHandler.init();
        
        groupSetA = createOrganisationUnitGroupSet( 'A' );
        groupSetB = createOrganisationUnitGroupSet( 'B' );
        groupSetC = createOrganisationUnitGroupSet( 'C' );
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
        batchHandler.addObject( groupSetA );
        batchHandler.addObject( groupSetB );
        batchHandler.addObject( groupSetC );

        batchHandler.flush();
        
        Collection<OrganisationUnitGroupSet> groupSets = organisationUnitGroupService.getAllOrganisationUnitGroupSets();
        
        assertTrue( groupSets.add( groupSetA ) );
        assertTrue( groupSets.add( groupSetB ) );
        assertTrue( groupSets.add( groupSetC ) );
    }
    
    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( groupSetA, true );
        int idB = batchHandler.insertObject( groupSetB, true );
        int idC = batchHandler.insertObject( groupSetC, true );
        
        assertNotNull( organisationUnitGroupService.getOrganisationUnitGroupSet( idA ) );
        assertNotNull( organisationUnitGroupService.getOrganisationUnitGroupSet( idB ) );
        assertNotNull( organisationUnitGroupService.getOrganisationUnitGroupSet( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = organisationUnitGroupService.addOrganisationUnitGroupSet( groupSetA );
        
        groupSetA.setName( "UpdatedName" );
        
        batchHandler.updateObject( groupSetA );
        
        assertEquals( organisationUnitGroupService.getOrganisationUnitGroupSet( id ).getName(), "UpdatedName" );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = organisationUnitGroupService.addOrganisationUnitGroupSet( groupSetA );
        
        int retrievedId = batchHandler.getObjectIdentifier( "OrganisationUnitGroupSetA" );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        organisationUnitGroupService.addOrganisationUnitGroupSet( groupSetA );
        
        assertTrue( batchHandler.objectExists( groupSetA ) );
        
        assertFalse( batchHandler.objectExists( groupSetB ) );
    }
}
