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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: PeriodBatchHandlerTest.java 4949 2008-04-21 07:59:54Z larshelg $
 */
public class PeriodBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private PeriodType periodType;
    
    private Date dateA;
    private Date dateB;
    private Date dateC;
    private Date dateD;
    
    private Period periodA;
    private Period periodB;
    private Period periodC;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        periodType = periodService.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        Calendar calendar = Calendar.getInstance();
        
        calendar.clear();
        
        calendar.set( 2000, 0, 1 );
        
        dateA = calendar.getTime();

        calendar.set( 2000, 1, 1 );
        
        dateB = calendar.getTime();

        calendar.set( 2000, 2, 1 );
        
        dateC = calendar.getTime();

        calendar.set( 2000, 3, 1 );
        
        dateD = calendar.getTime();
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
        
        batchHandler.init();
        
        periodA = createPeriod( periodType, dateA, dateB );
        periodB = createPeriod( periodType, dateB, dateC );
        periodC = createPeriod( periodType, dateC, dateD );
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
        batchHandler.addObject( periodA );
        batchHandler.addObject( periodB );
        batchHandler.addObject( periodC );

        batchHandler.flush();
        
        Collection<Period> periods = periodService.getPeriodsByPeriodType( periodType );
        
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
    }    

    public void testInsertObject()
    {
        int idA = batchHandler.insertObject( periodA, true );
        int idB = batchHandler.insertObject( periodB, true );
        int idC = batchHandler.insertObject( periodC, true );
        
        assertNotNull( periodService.getPeriod( idA ) );
        assertNotNull( periodService.getPeriod( idB ) );
        assertNotNull( periodService.getPeriod( idC ) );
    }
    
    public void testUpdateObject()
    {
        int id = periodService.addPeriod( periodA );
                
        periodA.setStartDate( dateA );
        
        batchHandler.updateObject( periodA );
        
        assertEquals( periodService.getPeriod( id ).getStartDate(), dateA );
    }
    
    public void testGetObjectIdentifier()
    {
        int referenceId = periodService.addPeriod( periodA );
        
        int retrievedId = batchHandler.getObjectIdentifier( periodA );
        
        assertEquals( referenceId, retrievedId );
    }
    
    public void testObjectExists()
    {
        periodService.addPeriod( periodA );
        
        assertTrue( batchHandler.objectExists( periodA ) );
        
        assertFalse( batchHandler.objectExists( periodB ) );
    }
}
