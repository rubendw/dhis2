package org.hisp.dhis.dataset;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CompleteDataSetRegistrationServiceTest
    extends DhisConvenienceTest
{
    private CompleteDataSetRegistration registrationA;
    private CompleteDataSetRegistration registrationB;
    private CompleteDataSetRegistration registrationC;
    private CompleteDataSetRegistration registrationD;
    private CompleteDataSetRegistration registrationE;
    private CompleteDataSetRegistration registrationF;
    private CompleteDataSetRegistration registrationG;
    private CompleteDataSetRegistration registrationH;
    private CompleteDataSetRegistration registrationI;
    private CompleteDataSetRegistration registrationJ;
    private CompleteDataSetRegistration registrationK;
    private CompleteDataSetRegistration registrationL;
    
    private DataSet dataSetA;
    private DataSet dataSetB;
    private DataSet dataSetC;
    
    private Period periodA;
    private Period periodB;
    
    private Source sourceA;
    private Source sourceB;
    private Source sourceC;

    private Date onTimeA;
    private Date onTimeB;
    private Date deadlineA;
    private Date deadlineB;
    private Date tooLateA;
    private Date tooLateB;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        completeDataSetRegistrationService = (CompleteDataSetRegistrationService) getBean( CompleteDataSetRegistrationService.ID );
        
        dataSetService = (DataSetService) getBean( DataSetService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );

        sourceStore = (SourceStore) getBean( SourceStore.ID );

        sourceA = new DummySource( "SourceA" );
        sourceB = new DummySource( "SourceB" );
        sourceC = new DummySource( "SourceC" );
        
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB ); 
        sourceStore.addSource( sourceC );    
        
        periodA = createPeriod( new MonthlyPeriodType(), getDate( 2000, 1, 1 ), getDate( 2000, 1, 31 ) );
        periodB = createPeriod( new MonthlyPeriodType(), getDate( 2000, 2, 1 ), getDate( 2000, 2, 28 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );  

        dataSetA = createDataSet( 'A', new MonthlyPeriodType() );
        dataSetB = createDataSet( 'B', new MonthlyPeriodType() );
        dataSetC = createDataSet( 'C', new MonthlyPeriodType() );
        
        dataSetA.getSources().add( sourceA );
        dataSetA.getSources().add( sourceB );
        dataSetB.getSources().add( sourceA );
        dataSetB.getSources().add( sourceB );
        dataSetC.getSources().add( sourceA );
        dataSetC.getSources().add( sourceB );        
        
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );
        dataSetService.addDataSet( dataSetC );

        onTimeA = getDate( 2000, 1, 10 );
        onTimeB = getDate( 2000, 2, 10 );
        deadlineA = getDate( 2000, 1, 15 );
        deadlineB = getDate( 2000, 2, 15 );
        tooLateA = getDate( 2000, 1, 25 );
        tooLateB = getDate( 2000, 2, 25 );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testSaveGet()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, new Date() );
        registrationB = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, new Date() );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        
        assertEquals( registrationA, completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, sourceA ) );
        assertEquals( registrationB, completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, sourceA ) );
        
        registrationC = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, new Date() );
        
        try
        {
            completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationC );
            
            fail();
        }
        catch ( Exception ex )
        {
            // Expected unique constraint exception
        }
    }
    
    public void testDelete()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, new Date() );
        registrationB = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, new Date() );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, sourceA ) );

        completeDataSetRegistrationService.deleteCompleteDataSetRegistration( registrationA );
        
        assertNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, sourceA ) );

        completeDataSetRegistrationService.deleteCompleteDataSetRegistration( registrationB );
        
        assertNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, sourceA ) );
        assertNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, sourceA ) );        
    }

    public void testGetAll()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, new Date() );
        registrationB = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, new Date() );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        
        Collection<CompleteDataSetRegistration> registrations = completeDataSetRegistrationService.getAllCompleteDataSetRegistrations();
        
        assertEquals( 2, registrations.size() );
        assertTrue( registrations.contains( registrationA ) );
        assertTrue( registrations.contains( registrationB ) );
    }

    public void testGetDataSetsSourcesPeriods()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, new Date() );
        registrationB = new CompleteDataSetRegistration( dataSetB, periodA, sourceA, new Date() );
        registrationC = new CompleteDataSetRegistration( dataSetA, periodB, sourceA, new Date() );
        registrationD = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, new Date() );
        registrationE = new CompleteDataSetRegistration( dataSetA, periodA, sourceB, new Date() );
        registrationF = new CompleteDataSetRegistration( dataSetB, periodA, sourceB, new Date() );
        registrationG = new CompleteDataSetRegistration( dataSetA, periodB, sourceB, new Date() );
        registrationH = new CompleteDataSetRegistration( dataSetB, periodB, sourceB, new Date() );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationC );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationD );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationE );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationF );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationG );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationH );
        
        Collection<DataSet> dataSets = new ArrayList<DataSet>();
        
        dataSets.add( dataSetB );
        
        Collection<Source> sources = new ArrayList<Source>();

        sources.add( sourceA );
        sources.add( sourceB );
        
        Collection<Period> periods = new ArrayList<Period>();
        
        periods.add( periodA );
        
        Collection<CompleteDataSetRegistration> registrations = completeDataSetRegistrationService.
            getCompleteDataSetRegistrations( dataSets, sources, periods );
        
        assertNotNull( registrations );
        assertEquals( 2, registrations.size() );
        assertTrue( registrations.contains( registrationB ) );
        assertTrue( registrations.contains( registrationF ) );        
    }
    
    public void testGetDataSetSourcesPeriod()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, new Date() );
        registrationB = new CompleteDataSetRegistration( dataSetB, periodA, sourceA, new Date() );
        registrationC = new CompleteDataSetRegistration( dataSetA, periodB, sourceA, new Date() );
        registrationD = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, new Date() );        
        registrationE = new CompleteDataSetRegistration( dataSetA, periodA, sourceB, new Date() );
        registrationF = new CompleteDataSetRegistration( dataSetB, periodA, sourceB, new Date() );
        registrationG = new CompleteDataSetRegistration( dataSetA, periodB, sourceB, new Date() );
        registrationH = new CompleteDataSetRegistration( dataSetB, periodB, sourceB, new Date() );
        
        registrationI = new CompleteDataSetRegistration( dataSetA, periodA, sourceC, new Date() );
        registrationJ = new CompleteDataSetRegistration( dataSetB, periodA, sourceC, new Date() );
        registrationK = new CompleteDataSetRegistration( dataSetA, periodB, sourceC, new Date() );
        registrationL = new CompleteDataSetRegistration( dataSetB, periodB, sourceC, new Date() );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationC );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationD );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationE );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationF );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationG );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationH );        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationI );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationJ );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationK );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationL );
        
        Collection<Source> sources = new ArrayList<Source>();
        
        sources.add( sourceA );
        sources.add( sourceB );
        sources.add( sourceC );
                
        assertEquals( 2, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetA, sources, periodA ) );
        assertEquals( 2, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetB, sources, periodA ) );
        assertEquals( 2, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetA, sources, periodB ) );
        assertEquals( 2, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetB, sources, periodB ) );
        assertEquals( 0, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetC, sources, periodA ) );
        assertEquals( 0, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetC, sources, periodB ) );        
    }

    public void testGetDataSetSourcesPeriodDate()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, onTimeA );
        registrationB = new CompleteDataSetRegistration( dataSetB, periodA, sourceA, onTimeA );
        registrationC = new CompleteDataSetRegistration( dataSetA, periodB, sourceA, onTimeB );
        registrationD = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, onTimeB );        
        registrationE = new CompleteDataSetRegistration( dataSetA, periodA, sourceB, tooLateA );
        registrationF = new CompleteDataSetRegistration( dataSetB, periodA, sourceB, tooLateA );
        registrationG = new CompleteDataSetRegistration( dataSetA, periodB, sourceB, tooLateB );
        registrationH = new CompleteDataSetRegistration( dataSetB, periodB, sourceB, tooLateB );
        
        registrationI = new CompleteDataSetRegistration( dataSetA, periodA, sourceC, onTimeA );
        registrationJ = new CompleteDataSetRegistration( dataSetB, periodA, sourceC, onTimeA );
        registrationK = new CompleteDataSetRegistration( dataSetA, periodB, sourceC, onTimeB );
        registrationL = new CompleteDataSetRegistration( dataSetB, periodB, sourceC, onTimeB );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationC );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationD );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationE );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationF );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationG );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationH );        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationI );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationJ );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationK );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationL );
        
        Collection<Source> sources = new ArrayList<Source>();
        
        sources.add( sourceA );
        sources.add( sourceB );
        sources.add( sourceC );
                
        assertEquals( 1, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetA, sources, periodA, deadlineA ) );
        assertEquals( 1, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetB, sources, periodA, deadlineA ) );
        assertEquals( 1, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetA, sources, periodB, deadlineB ) );
        assertEquals( 1, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetB, sources, periodB, deadlineB ) );
        assertEquals( 0, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetC, sources, periodA, deadlineA ) );
        assertEquals( 0, completeDataSetRegistrationService.getCompleteDataSetRegistrationsForDataSet( dataSetC, sources, periodB, deadlineB ) );        
    }

    public void testDeleteByDataSet()
    {
        registrationA = new CompleteDataSetRegistration( dataSetA, periodA, sourceA, onTimeA );
        registrationB = new CompleteDataSetRegistration( dataSetA, periodB, sourceA, onTimeA );
        registrationC = new CompleteDataSetRegistration( dataSetB, periodA, sourceA, onTimeA );
        registrationD = new CompleteDataSetRegistration( dataSetB, periodB, sourceA, onTimeA );
        
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationA );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationB );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationC );
        completeDataSetRegistrationService.saveCompleteDataSetRegistration( registrationD );
        
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodB, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodA, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, sourceA ) );
        
        completeDataSetRegistrationService.deleteCompleteDataSetRegistrations( dataSetA );

        assertNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodA, sourceA ) );
        assertNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetA, periodB, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodA, sourceA ) );
        assertNotNull( completeDataSetRegistrationService.getCompleteDataSetRegistration( dataSetB, periodB, sourceA ) );   
    }
}
