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
import java.util.Date;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CompleteDataSetRegistrationBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
        
    private DataSet dataSetA;
    private DataSet dataSetB;
    
    private Period periodA;
    private Period periodB;
    
    private OrganisationUnit unitA;
    
    private Date dateA;
    private Date dateB;
    
    private CompleteDataSetRegistration registrationA;
    private CompleteDataSetRegistration registrationB;
    private CompleteDataSetRegistration registrationC;
    private CompleteDataSetRegistration registrationD;
    

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        dataSetService = (DataSetService) getBean( DataSetService.ID );
        periodService = (PeriodService) getBean( PeriodService.ID );
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        completeDataSetRegistrationService = (CompleteDataSetRegistrationService) getBean( CompleteDataSetRegistrationService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( CompleteDataSetRegistrationBatchHandler.class );
        
        dataSetA = createDataSet( 'A', new MonthlyPeriodType() );
        dataSetB = createDataSet( 'B', new MonthlyPeriodType() );
        
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );
        
        periodA = createPeriod( new MonthlyPeriodType(), getDate( 2000, 1, 1 ), getDate( 2000, 1, 31 ) );
        periodB = createPeriod( new MonthlyPeriodType(), getDate( 2000, 2, 1 ), getDate( 2000, 2, 28 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );        
        
        unitA = createOrganisationUnit( 'A' );
        
        organisationUnitService.addOrganisationUnit( unitA );
        
        dateA = getDate( 2000, 1, 15 );
        dateB = getDate( 2000, 2, 15 );
        
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, unitA, dateA );
        registrationB = new CompleteDataSetRegistration( dataSetA, periodB, unitA, dateB );
        registrationC = new CompleteDataSetRegistration( dataSetB, periodA, unitA, dateA );
        registrationD = new CompleteDataSetRegistration( dataSetB, periodB, unitA, dateB );
        
        batchHandler.init();
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
        batchHandler.addObject( registrationA );
        batchHandler.addObject( registrationB );
        batchHandler.addObject( registrationC );
        batchHandler.addObject( registrationD );
        
        batchHandler.flush();
        
        Collection<CompleteDataSetRegistration> registrations = completeDataSetRegistrationService.getAllCompleteDataSetRegistrations();
        
        assertNotNull( registrations );
        assertEquals( 4, registrations.size() );
        
        assertTrue( registrations.contains( registrationA ) );
        assertTrue( registrations.contains( registrationB ) );
        assertTrue( registrations.contains( registrationC ) );
        assertTrue( registrations.contains( registrationD ) );
    }
    
    public void testInsertObject()
    {
        batchHandler.insertObject( registrationA, false );
        batchHandler.insertObject( registrationB, false );
        batchHandler.insertObject( registrationC, false );
        batchHandler.insertObject( registrationD, false );
        
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, unitA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodB, unitA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodA, unitA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, unitA ) );
    }
    
    public void testUpdateObject()
    {
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        
        registrationA.setDate( dateB );
        
        batchHandler.updateObject( registrationA );
        
        registrationA = completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, unitA );
        
        assertEquals( dateB, registrationA.getDate() );
    }
    
    public void testObjectExists()
    {
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        
        assertTrue( batchHandler.objectExists( registrationA ) );
        assertTrue( batchHandler.objectExists( registrationB ) );
        
        assertFalse( batchHandler.objectExists( registrationC ) );
        assertFalse( batchHandler.objectExists( registrationD ) );
    }
}
