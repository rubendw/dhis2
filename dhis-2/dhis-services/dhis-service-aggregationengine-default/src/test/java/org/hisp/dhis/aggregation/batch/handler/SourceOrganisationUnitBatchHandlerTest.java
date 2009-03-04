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
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

/**
 * @author Lars Helge Overland
 * @version $Id: SourceOrganisationUnitBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class SourceOrganisationUnitBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler sourceBatchHandler;
    
    private BatchHandler organisationUnitbatchHandler;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        sourceBatchHandler = batchHandlerFactory.createBatchHandler( SourceBatchHandler.class );
        
        organisationUnitbatchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );

        sourceBatchHandler.init();
        
        organisationUnitbatchHandler.init();
        
        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B' );
        unitC = createOrganisationUnit( 'C' );
    }
    
    public void tearDownTest()
    {
        sourceBatchHandler.flush();
        
        organisationUnitbatchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testAddObject()
    {
        int idA = sourceBatchHandler.insertObject( unitA, true );
        int idB = sourceBatchHandler.insertObject( unitB, true );
        int idC = sourceBatchHandler.insertObject( unitC, true );
        
        unitA.setId( idA );
        unitB.setId( idB );
        unitC.setId( idC );        
        
        organisationUnitbatchHandler.addObject( unitA );
        organisationUnitbatchHandler.addObject( unitB );
        organisationUnitbatchHandler.addObject( unitC );
        
        organisationUnitbatchHandler.flush();
        
        Collection<OrganisationUnit> units = organisationUnitService.getAllOrganisationUnits();
        
        assertTrue( units.contains( unitA ) );
        assertTrue( units.contains( unitB ) );
        assertTrue( units.contains( unitC ) );
    }    

    public void testInsertObject()
    {
        int idA = sourceBatchHandler.insertObject( unitA, true );
        int idB = sourceBatchHandler.insertObject( unitB, true );
        int idC = sourceBatchHandler.insertObject( unitC, true );
        
        unitA.setId( idA );
        unitB.setId( idB );
        unitC.setId( idC );
        
        organisationUnitbatchHandler.insertObject( unitA, true );
        organisationUnitbatchHandler.insertObject( unitB, true );
        organisationUnitbatchHandler.insertObject( unitC, true );
        
        assertNotNull( organisationUnitService.getOrganisationUnit( idA ) );
        assertNotNull( organisationUnitService.getOrganisationUnit( idB ) );
        assertNotNull( organisationUnitService.getOrganisationUnit( idC ) );
    }    

    public void testUpdateObject()
    {
        int id = organisationUnitService.addOrganisationUnit( unitA );
        
        unitA.setName( "UpdatedName" );
        
        organisationUnitbatchHandler.updateObject( unitA );
        
        assertEquals( organisationUnitService.getOrganisationUnit( id ).getName(), "UpdatedName" );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = organisationUnitService.addOrganisationUnit( unitA );
        
        int retrievedId = organisationUnitbatchHandler.getObjectIdentifier( "OrganisationUnitA" );

        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        organisationUnitService.addOrganisationUnit( unitA );
        
        assertTrue( organisationUnitbatchHandler.objectExists( unitA ) );
        
        assertFalse( organisationUnitbatchHandler.objectExists( unitB ) );
    }
}
