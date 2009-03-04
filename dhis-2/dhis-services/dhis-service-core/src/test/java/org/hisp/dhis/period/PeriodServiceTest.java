package org.hisp.dhis.period;

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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Kristian Nordal
 * @version $Id: PeriodServiceTest.java 5527 2008-08-06 13:00:07Z larshelg $
 */
public class PeriodServiceTest
    extends DhisSpringTest
{
    private PeriodService periodService;
    
    private DataElementService dataElementService;

    private DataElementCategoryOptionComboService categoryOptionComboService;
    
    private SourceStore sourceStore;
    
    private DataValueService dataValueService;

    private TransactionManager transactionManager;

    private Calendar calendar;

    private DataElementCategoryOptionCombo optionCombo;
    
    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );

        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        sourceStore = (SourceStore) getBean( SourceStore.ID );
        
        dataValueService = (DataValueService) getBean( DataValueService.ID );

        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        calendar = Calendar.getInstance();

        optionCombo = new DataElementCategoryOptionCombo();
        
        categoryOptionComboService.addDataElementCategoryOptionCombo( optionCombo );
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private Date getDay( int day )
    {
        calendar.clear();
        calendar.set( Calendar.DAY_OF_YEAR, day );

        return calendar.getTime();
    }
    
    private Date getDate( int day, int month, int year )
    {
    	calendar.clear();
    	calendar.set( Calendar.YEAR, year );
    	calendar.set( Calendar.MONTH, month - 1 );
    	calendar.set( Calendar.DAY_OF_MONTH, day );
    	    	    	
    	return calendar.getTime();
    }


    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    public void testAddPeriod()
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        int idA = periodService.addPeriod( periodA );
        int idB = periodService.addPeriod( periodB );
        int idC = periodService.addPeriod( periodC );

        transactionManager.leave();

        try
        {
            // Should give unique constraint violation.
            periodService.addPeriod( periodD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected.
        }

        transactionManager.enter();

        periodA = periodService.getPeriod( idA );
        assertNotNull( periodA );
        assertEquals( idA, periodA.getId() );
        assertEquals( periodTypeA, periodA.getPeriodType() );
        assertEquals( getDay( 0 ), periodA.getStartDate() );
        assertEquals( getDay( 1 ), periodA.getEndDate() );

        periodB = periodService.getPeriod( idB );
        assertNotNull( periodB );
        assertEquals( idB, periodB.getId() );
        assertEquals( periodTypeA, periodB.getPeriodType() );
        assertEquals( getDay( 1 ), periodB.getStartDate() );
        assertEquals( getDay( 2 ), periodB.getEndDate() );

        periodC = periodService.getPeriod( idC );
        assertNotNull( periodC );
        assertEquals( idC, periodC.getId() );
        assertEquals( periodTypeB, periodC.getPeriodType() );
        assertEquals( getDay( 2 ), periodC.getStartDate() );
        assertEquals( getDay( 3 ), periodC.getEndDate() );

        transactionManager.leave();
    }

    public void testDeleteAndGetPeriod()
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        int idA = periodService.addPeriod( periodA );
        int idB = periodService.addPeriod( periodB );
        int idC = periodService.addPeriod( periodC );
        int idD = periodService.addPeriod( periodD );

        assertNotNull( periodService.getPeriod( idA ) );
        assertNotNull( periodService.getPeriod( idB ) );
        assertNotNull( periodService.getPeriod( idC ) );
        assertNotNull( periodService.getPeriod( idD ) );

        periodService.deletePeriod( periodA );
        assertNull( periodService.getPeriod( idA ) );
        assertNotNull( periodService.getPeriod( idB ) );
        assertNotNull( periodService.getPeriod( idC ) );
        assertNotNull( periodService.getPeriod( idD ) );

        periodService.deletePeriod( periodB );
        assertNull( periodService.getPeriod( idA ) );
        assertNull( periodService.getPeriod( idB ) );
        assertNotNull( periodService.getPeriod( idC ) );
        assertNotNull( periodService.getPeriod( idD ) );

        periodService.deletePeriod( periodC );
        assertNull( periodService.getPeriod( idA ) );
        assertNull( periodService.getPeriod( idB ) );
        assertNull( periodService.getPeriod( idC ) );
        assertNotNull( periodService.getPeriod( idD ) );

        periodService.deletePeriod( periodD );
        assertNull( periodService.getPeriod( idA ) );
        assertNull( periodService.getPeriod( idB ) );
        assertNull( periodService.getPeriod( idC ) );
        assertNull( periodService.getPeriod( idD ) );

        transactionManager.leave();
    }

    public void testGetPeriod()
    {
        transactionManager.enter();
        
        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
    
        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        Period periodE = new Period( periodTypeA, getDay( 3 ), getDay( 4 ) );
        int idA = periodService.addPeriod( periodA );
        int idB = periodService.addPeriod( periodB );
        int idC = periodService.addPeriod( periodC );
        int idD = periodService.addPeriod( periodD );
        int idE = periodService.addPeriod( periodE );
    
        periodA = periodService.getPeriod( getDay( 0 ), getDay( 1 ), periodTypeA );
        assertNotNull( periodA );
        assertEquals( idA, periodA.getId() );
        assertEquals( periodTypeA, periodA.getPeriodType() );
        assertEquals( getDay( 0 ), periodA.getStartDate() );
        assertEquals( getDay( 1 ), periodA.getEndDate() );
        
        periodB = periodService.getPeriod( getDay( 1 ), getDay( 2 ), periodTypeA );
        assertNotNull( periodB );
        assertEquals( idB, periodB.getId() );
        assertEquals( periodTypeA, periodB.getPeriodType() );
        assertEquals( getDay( 1 ), periodB.getStartDate() );
        assertEquals( getDay( 2 ), periodB.getEndDate() );
    
        periodC = periodService.getPeriod( getDay( 2 ), getDay( 3 ), periodTypeB );
        assertNotNull( periodC );
        assertEquals( idC, periodC.getId() );
        assertEquals( periodTypeB, periodC.getPeriodType() );
        assertEquals( getDay( 2 ), periodC.getStartDate() );
        assertEquals( getDay( 3 ), periodC.getEndDate() );
    
        periodD = periodService.getPeriod( getDay( 3 ), getDay( 4 ), periodTypeB );
        assertNotNull( periodD );
        assertEquals( idD, periodD.getId() );
        assertEquals( periodTypeB, periodD.getPeriodType() );
        assertEquals( getDay( 3 ), periodD.getStartDate() );
        assertEquals( getDay( 4 ), periodD.getEndDate() );
    
        periodE = periodService.getPeriod( getDay( 3 ), getDay( 4 ), periodTypeA );
        assertNotNull( periodE );
        assertEquals( idE, periodE.getId() );
        assertEquals( periodTypeA, periodE.getPeriodType() );
        assertEquals( getDay( 3 ), periodE.getStartDate() );
        assertEquals( getDay( 4 ), periodE.getEndDate() );
        
        assertNull( periodService.getPeriod( getDay( 1 ), getDay( 2 ), periodTypeB ) );
        assertNull( periodService.getPeriod( getDay( 2 ), getDay( 3 ), periodTypeA ) );
        assertNull( periodService.getPeriod( getDay( 0 ), getDay( 5 ), periodTypeB ) );
        assertNull( periodService.getPeriod( getDay( 4 ), getDay( 3 ), periodTypeB ) );
        assertNull( periodService.getPeriod( getDay( 5 ), getDay( 6 ), periodTypeA ) );
        
        transactionManager.leave();
    }

    public void testGetAllPeriods()
    {
        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();

        Period periodA = new Period( periodType, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodType, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodType, getDay( 2 ), getDay( 3 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        
        Collection<Period> periods = periodService.getAllPeriods();
        
        assertNotNull( periods );
        assertEquals( 3, periods.size() );
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );        
    }
    
    public void testGetPeriodsBetweenDates()
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );

        Collection<Period> periods = periodService.getPeriodsBetweenDates( getDay( 0 ), getDay( 0 ) );
        assertNotNull( periods );
        assertEquals( 0, periods.size() );

        periods = periodService.getPeriodsBetweenDates( getDay( 0 ), getDay( 1 ) );
        assertNotNull( periods );
        assertEquals( 1, periods.size() );
        assertEquals( periodA, periods.iterator().next() );

        periods = periodService.getPeriodsBetweenDates( getDay( 1 ), getDay( 4 ) );
        assertNotNull( periods );
        assertEquals( 3, periods.size() );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );

        periods = periodService.getPeriodsBetweenDates( getDay( 0 ), getDay( 5 ) );
        assertNotNull( periods );
        assertEquals( 4, periods.size() );
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );

        transactionManager.leave();
    }

    public void testGetIntersectingPeriodsByPeriodType()
    {
    	transactionManager.enter();
        
    	PeriodType ypt = PeriodType.getPeriodTypeByName( "Yearly" );
    	
    	Date jan2006 = getDate(1, 1, 2006);
    	Date dec2006 = getDate(31, 12, 2006);
    	Date jan2007 = getDate(1, 1, 2007);
    	Date dec2007 = getDate(31, 12, 2007);
    	
    	Period periodA = new Period(ypt, jan2006, dec2006);
    	Period periodB = new Period(ypt, jan2007, dec2007);    	
    	periodService.addPeriod(periodA);
    	periodService.addPeriod(periodB);
    	    	
    	PeriodType mpt = PeriodType.getPeriodTypeByName( "Monthly" );
    	
    	Date janstart = getDate(1,1,2006);
    	Date janend = getDate(31,1,2006);
    	Date febstart = getDate(1,2,2006);
    	Date febend = getDate(28,2,2006);
    	Date marstart = getDate(1,3,2006);
    	Date marend = getDate(31,3,2006);
    	Date aprstart = getDate(1,4,2006);
    	Date aprend = getDate(30,4,2006);
    	Date maystart = getDate(1,5,2006);
    	Date mayend = getDate(31,5,2006);
    	Date junstart = getDate(1,6,2006);
    	Date junend = getDate(30,6,2006);
    	Date julstart = getDate(1,7,2006);
    	Date julend = getDate(31,7,2006);
    	Date augstart = getDate(1,8,2006);
    	Date augend = getDate(31,8,2006);
    	Date sepstart = getDate(1,9,2006);
    	Date sepend = getDate(30,9,2006);
    	Date octstart = getDate(1,10,2006);
    	Date octend = getDate(31,10,2006);
    	Date novstart = getDate(1,11,2006);
    	Date novend = getDate(30,11,2006);
    	Date decstart = getDate(1,12,2006);
    	Date decend = getDate(31,12,2006);
    	
    	Period periodC = new Period(mpt,janstart,janend);
    	Period periodD = new Period(mpt,febstart,febend);
    	Period periodE = new Period(mpt,marstart,marend);
    	Period periodF = new Period(mpt,aprstart,aprend);
    	Period periodG = new Period(mpt,maystart,mayend);
    	Period periodH = new Period(mpt,junstart,junend);
    	Period periodI = new Period(mpt,julstart,julend);
    	Period periodJ = new Period(mpt,augstart,augend);
    	Period periodK = new Period(mpt,sepstart,sepend);
    	Period periodL = new Period(mpt,octstart,octend);
    	Period periodM = new Period(mpt,novstart,novend);
    	Period periodN = new Period(mpt,decstart,decend);
    	
    	periodService.addPeriod(periodC);
    	periodService.addPeriod(periodD);
    	periodService.addPeriod(periodE);
    	periodService.addPeriod(periodF);
    	periodService.addPeriod(periodG);
    	periodService.addPeriod(periodH);
    	periodService.addPeriod(periodI);
    	periodService.addPeriod(periodJ);
    	periodService.addPeriod(periodK);
    	periodService.addPeriod(periodL);
    	periodService.addPeriod(periodM);
    	periodService.addPeriod(periodN);
    	
    	Collection<Period> periodsA = periodService.getIntersectingPeriodsByPeriodType(ypt, getDate(1,6,2006), getDate(30,11,2006)); 
    	assertNotNull( periodsA );
    	assertEquals( 1, periodsA.size() );
    	
    	Collection<Period> periodsB = periodService.getIntersectingPeriodsByPeriodType(mpt, getDate(1,6,2006), getDate(30,11,2006));
    	assertNotNull( periodsB );
    	assertEquals( 6, periodsB.size() );
    	
    	transactionManager.leave();
    }    

    public void testGetIntersectingPeriods()
    {
        PeriodType type = periodService.getAllPeriodTypes().iterator().next();
        
        Period periodA = new Period( type, getDay( 0 ), getDay( 2 ) );
        Period periodB = new Period( type, getDay( 2 ), getDay( 4 ) );
        Period periodC = new Period( type, getDay( 4 ), getDay( 6 ) );
        Period periodD = new Period( type, getDay( 6 ), getDay( 8 ) );
        Period periodE = new Period( type, getDay( 8 ), getDay( 10 ) );
        Period periodF = new Period( type, getDay( 10 ), getDay( 12 ) );
        Period periodG = new Period( type, getDay( 12 ), getDay( 14 ) );
        Period periodH = new Period( type, getDay( 2 ), getDay( 6 ) );
        Period periodI = new Period( type, getDay( 8 ), getDay( 12 ) );
        Period periodJ = new Period( type, getDay( 2 ), getDay( 12 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
        periodService.addPeriod( periodF );
        periodService.addPeriod( periodG );
        periodService.addPeriod( periodH );
        periodService.addPeriod( periodI );
        periodService.addPeriod( periodJ );
        
        Collection<Period> periods = periodService.getIntersectingPeriods( getDay( 4 ), getDay( 10 ) );
        
        assertEquals( periods.size(), 6 );
        
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );
        assertTrue( periods.contains( periodE ) );
        assertTrue( periods.contains( periodH ) );
        assertTrue( periods.contains( periodI ) );
        assertTrue( periods.contains( periodJ ) );
    }
    
    public void testGetPeriodsByPeriodType()
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );

        Collection<Period> periodsARef = new HashSet<Period>();
        periodsARef.add( periodA );
        periodsARef.add( periodB );
        periodsARef.add( periodC );

        Collection<Period> periodsA = periodService.getPeriodsByPeriodType( periodTypeA );
        assertNotNull( periodsA );
        assertEquals( periodsARef.size(), periodsA.size() );
        assertTrue( periodsA.containsAll( periodsARef ) );

        Collection<Period> periodsB = periodService.getPeriodsByPeriodType( periodTypeB );
        assertNotNull( periodsB );
        assertEquals( 1, periodsB.size() );
        assertEquals( periodD, periodsB.iterator().next() );

        Collection<Period> periodsC = periodService.getPeriodsByPeriodType( periodTypeC );
        assertNotNull( periodsC );
        assertEquals( 0, periodsC.size() );

        transactionManager.leave();
    }

    public void testGetPeriodsWithAssociatedDataValues()
    {
        DataElement dataElementA = new DataElement( "NameA", "AlternativeNameA", "ShortNameA", "CodeA", "DescriptionA",
            true, DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );        
        DataElement dataElementB = new DataElement( "NameB", "AlternativeNameB", "ShortNameB", "CodeB", "DescriptionB",
            true, DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );        
        DataElement dataElementC = new DataElement( "NameC", "AlternativeNameC", "ShortNameC", "CodeC", "DescriptionC",
            true, DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );
    
        PeriodType quarterly = PeriodType.getPeriodTypeByName( QuarterlyPeriodType.NAME );
        PeriodType monthly = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );
        PeriodType weekly = PeriodType.getPeriodTypeByName( WeeklyPeriodType.NAME );
        
        Period qu1 = new Period( quarterly, getDate( 1, 1, 2008 ), getDate( 31, 3, 2008 ) );
        
        Period jan = new Period( monthly, getDate( 1, 1, 2008 ), getDate( 31, 1, 2008 ) );
        Period feb = new Period( monthly, getDate( 1, 2, 2008 ), getDate( 29, 2, 2008 ) );
        Period mar = new Period( monthly, getDate( 1, 3, 2008 ), getDate( 31, 3, 2008 ) );
        Period apr = new Period( monthly, getDate( 1, 4, 2008 ), getDate( 30, 4, 2008 ) );
        Period may = new Period( monthly, getDate( 1, 5, 2008 ), getDate( 31, 5, 2008 ) );
        
        Period w01 = new Period( weekly, getDate( 31, 12, 2007 ), getDate( 6, 1, 2008 ) );
        Period w02 = new Period( weekly, getDate( 7, 1, 2008 ), getDate( 13, 1, 2008 ) );
        Period w03 = new Period( weekly, getDate( 14, 1, 2008 ), getDate( 20, 1, 2008 ) );
        Period w04 = new Period( weekly, getDate( 21, 1, 2008 ), getDate( 27, 1, 2008 ) );
        Period w05 = new Period( weekly, getDate( 28, 1, 2008 ), getDate( 3, 2, 2008 ) );
                
        Source sourceA = new DummySource( "SourceA" );
        Source sourceB = new DummySource( "SourceB" );
        Source sourceC = new DummySource( "SourceC" );
        
        DataValue dataValueA = new DataValue( dataElementA, jan, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementA, feb, sourceB, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementA, apr, sourceB, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementA, qu1, sourceA, optionCombo );
        dataValueD.setValue( "4" );
        DataValue dataValueE = new DataValue( dataElementB, w01, sourceA, optionCombo );
        dataValueE.setValue( "5" );
        DataValue dataValueF = new DataValue( dataElementB, w02, sourceB, optionCombo );
        dataValueF.setValue( "6" );
        DataValue dataValueG = new DataValue( dataElementB, w03, sourceA, optionCombo );
        dataValueG.setValue( "7" );
        DataValue dataValueH = new DataValue( dataElementB, w04, sourceB, optionCombo );
        dataValueH.setValue( "8" );
        DataValue dataValueI = new DataValue( dataElementB, w05, sourceA, optionCombo );
        dataValueI.setValue( "9" );
        
        transactionManager.enter();
    
        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );

        dataValueService.addDataValue( dataValueA );
        dataValueService.addDataValue( dataValueB );
        dataValueService.addDataValue( dataValueC );
        dataValueService.addDataValue( dataValueD );
        dataValueService.addDataValue( dataValueE );
        dataValueService.addDataValue( dataValueF );
        dataValueService.addDataValue( dataValueG );
        dataValueService.addDataValue( dataValueH );
        dataValueService.addDataValue( dataValueI );
        
        transactionManager.leave();
                
        Collection<DataElement> dataElements1 = new ArrayList<DataElement>();
        
        dataElements1.add( dataElementA );
        dataElements1.add( dataElementB );
        
        Collection<DataElement> dataElements2 = new ArrayList<DataElement>();
        
        dataElements2.add( dataElementC );
        
        Collection<Source> sources1 = new ArrayList<Source>();
        
        sources1.add( sourceA );
        sources1.add( sourceB );
        
        Collection<Source> sources2 = new ArrayList<Source>();
        
        sources2.add( sourceC );
        
        Collection<Period> periods = periodService.getPeriods( jan, dataElements1, sources1 );
        
        assertEquals( periods.size(), 7 );

        periods = periodService.getPeriods( feb, dataElements1, sources1 );
        
        assertEquals( periods.size(), 3 );
        
        periods = periodService.getPeriods( mar, dataElements1, sources1 );

        assertEquals( periods.size(), 1 );
        
        periods = periodService.getPeriods( apr, dataElements1, sources1 );

        assertEquals( periods.size(), 1 );
        
        periods = periodService.getPeriods( may, dataElements1, sources1 );

        assertEquals( periods.size(), 0 );
        
        periods = periodService.getPeriods( jan, dataElements1, sources2 );
        
        assertEquals( periods.size(), 0 );
        
        periods = periodService.getPeriods( feb, dataElements2, sources1 );

        assertEquals( periods.size(), 0 );
        
        periods = periodService.getPeriods( mar, dataElements2, sources2 );
    }
    
    public void testGetBoundaryPeriods()
    {
        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();
        
        Period periodA = new Period( periodType, getDay( 5 ), getDay( 8 ) );
        Period periodB = new Period( periodType, getDay( 8 ), getDay( 11 ) );
        Period periodC = new Period( periodType, getDay( 11 ), getDay( 14 ) );
        Period periodD = new Period( periodType, getDay( 14 ), getDay( 17 ) );
        Period periodE = new Period( periodType, getDay( 17 ), getDay( 20 ) );
        Period periodF = new Period( periodType, getDay( 5 ), getDay( 20 ) );
        
        Collection<Period> periods = new ArrayList<Period>();
        
        periods.add( periodA );
        periods.add( periodB );
        periods.add( periodC );
        periods.add( periodD );
        periods.add( periodE );
        periods.add( periodF );

        Period basePeriod = new Period( periodType, getDay( 9 ), getDay( 15 ) );
        
        Collection<Period> boundaryPeriods = periodService.getBoundaryPeriods( basePeriod, periods );
        
        assertTrue( boundaryPeriods.size() == 3 );
        assertTrue( boundaryPeriods.contains( periodB ) );
        assertTrue( boundaryPeriods.contains( periodD ) );
        assertTrue( boundaryPeriods.contains( periodF ) );
        
        basePeriod = new Period( periodType, getDay( 11 ), getDay( 14 ) );
        
        boundaryPeriods = periodService.getBoundaryPeriods( basePeriod, periods );
        
        assertTrue( boundaryPeriods.size() == 1 );        
        assertTrue( boundaryPeriods.contains( periodF ) );
        
        basePeriod = new Period( periodType, getDay( 2 ), getDay( 5 ) );

        boundaryPeriods = periodService.getBoundaryPeriods( basePeriod, periods );
        
        assertTrue( boundaryPeriods.size() == 0 );
    }
    
    public void testGetInclusivePeriods()
    {
        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();
        
        Period periodA = new Period( periodType, getDay( 5 ), getDay( 8 ) );
        Period periodB = new Period( periodType, getDay( 8 ), getDay( 11 ) );
        Period periodC = new Period( periodType, getDay( 11 ), getDay( 14 ) );
        Period periodD = new Period( periodType, getDay( 14 ), getDay( 17 ) );
        Period periodE = new Period( periodType, getDay( 17 ), getDay( 20 ) );
        Period periodF = new Period( periodType, getDay( 5 ), getDay( 20 ) );
        
        Collection<Period> periods = new ArrayList<Period>();
        
        periods.add( periodA );
        periods.add( periodB );
        periods.add( periodC );
        periods.add( periodD );
        periods.add( periodE );
        periods.add( periodF );

        Period basePeriod = new Period( periodType, getDay( 8 ), getDay( 20 ) );
        
        Collection<Period> inclusivePeriods = periodService.getInclusivePeriods( basePeriod, periods );
        
        assertTrue( inclusivePeriods.size() == 4 );
        assertTrue( inclusivePeriods.contains( periodB ) );
        assertTrue( inclusivePeriods.contains( periodC ) );
        assertTrue( inclusivePeriods.contains( periodD ) );
        assertTrue( inclusivePeriods.contains( periodE ) );
        
        basePeriod = new Period( periodType, getDay( 9 ), getDay( 18 ) );
        
        inclusivePeriods = periodService.getInclusivePeriods( basePeriod, periods );
        
        assertTrue( inclusivePeriods.size() == 2 );
        assertTrue( inclusivePeriods.contains( periodC ) );
        assertTrue( inclusivePeriods.contains( periodD ) );
        
        basePeriod = new Period( periodType, getDay( 2 ), getDay( 5 ) );

        inclusivePeriods = periodService.getInclusivePeriods( basePeriod, periods );
        
        assertTrue( inclusivePeriods.size() == 0 );
    }
        
    public void testPrunePeriods()
    {
        DataElement dataElementA = new DataElement( "NameA", "AlternativeNameA", "ShortNameA", "CodeA", "DescriptionA",
            true, DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );
    
        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();
        
        Period periodA = new Period( periodType, getDay( 5 ), getDay( 6 ) );
        Period periodB = new Period( periodType, getDay( 6 ), getDay( 7 ) );
        Period periodC = new Period( periodType, getDay( 7 ), getDay( 8 ) );
        Period periodD = new Period( periodType, getDay( 8 ), getDay( 9 ) );
        Period periodE = new Period( periodType, getDay( 9 ), getDay( 10 ) );
    
        Source sourceA = new DummySource( "SourceA" );
        
        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementA, periodB, sourceA, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementA, periodC, sourceA, optionCombo );
        dataValueC.setValue( "3" );
        
        transactionManager.enter();
    
        dataElementService.addDataElement( dataElementA );
    
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
    
        sourceStore.addSource( sourceA );
    
        dataValueService.addDataValue( dataValueA );
        dataValueService.addDataValue( dataValueB );
        dataValueService.addDataValue( dataValueC );
        
        transactionManager.leave();

        Collection<Period> periods = periodService.getPeriodsByPeriodType( periodType );
        
        assertEquals( periods.size(), 5 );

        transactionManager.enter();
    
        int count = periodService.prunePeriods();

        assertEquals( count, 2 );

        transactionManager.leave();

        periods = periodService.getPeriodsByPeriodType( periodType );
        
        assertEquals( periods.size(), 3 );
    }

    public void testGetRelativePeriodDateIntInt()
    {
        Date date = getDate( 10, 7, 2000 );

        PeriodType periodType = new RelativePeriodType();
        
        Period period = periodService.getRelativePeriod( date, 3, 6 );
        
        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 10, 2000 ), period.getStartDate() );
        assertEquals( getDate( 31, 12, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 10, 2000 ), getDate( 31, 12, 2000 ), periodType ) );
        
        period = periodService.getRelativePeriod( date, 0, 3 );

        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 7, 2000 ), period.getStartDate() );
        assertEquals( getDate( 30, 9, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 7, 2000 ), getDate( 30, 9, 2000 ), periodType ) );

        period = periodService.getRelativePeriod( date, -3, 0 );

        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 4, 2000 ), period.getStartDate() );
        assertEquals( getDate( 30, 6, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 4, 2000 ), getDate( 30, 6, 2000 ), periodType ) );

        period = periodService.getRelativePeriod( date, -6, -3 );

        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 1, 2000 ), period.getStartDate() );
        assertEquals( getDate( 31, 3, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 1, 2000 ), getDate( 31, 3, 2000 ), periodType ) );
    }
    
    public void testGetRelativePeriodDateInt()
    {
        Date date = getDate( 10, 6, 2000 );

        PeriodType periodType = new RelativePeriodType();
        
        Period period = periodService.getRelativePeriod( date, 3 );
        
        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 6, 2000 ), period.getStartDate() );
        assertEquals( getDate( 31, 8, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 6, 2000 ), getDate( 31, 8, 2000 ), periodType ) );
        
        period = periodService.getRelativePeriod( date, 1 );
        
        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 6, 2000 ), period.getStartDate() );
        assertEquals( getDate( 30, 6, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 6, 2000 ), getDate( 30, 6, 2000 ), periodType ) );
        
        period = periodService.getRelativePeriod( date, -3 );
        
        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 4, 2000 ), period.getStartDate() );
        assertEquals( getDate( 30, 6, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 4, 2000 ), getDate( 30, 6, 2000 ), periodType ) );

        period = periodService.getRelativePeriod( date, -1 );
        
        assertTrue( period.getId() != 0 );
        assertEquals( getDate( 1, 6, 2000 ), period.getStartDate() );
        assertEquals( getDate( 30, 6, 2000 ), period.getEndDate() );
        assertNotNull( periodService.getPeriod( getDate( 1, 6, 2000 ), getDate( 30, 6, 2000 ), periodType ) );

        try
        {
            periodService.getRelativePeriod( date, 0 );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( IllegalArgumentException.class, ex.getClass() );
        }
    }
    
    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    public void testGetAndGetAllPeriodTypes()
        throws Exception
    {
        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();
        PeriodType periodTypeD = it.next();

        assertNotNull( periodService.getPeriodTypeByName( periodTypeA.getName() ) );
        assertNotNull( periodService.getPeriodTypeByName( periodTypeB.getName() ) );
        assertNotNull( periodService.getPeriodTypeByName( periodTypeC.getName() ) );
        assertNotNull( periodService.getPeriodTypeByName( periodTypeD.getName() ) );
    }

    public void testGetPeriodTypeByName()
        throws Exception
    {
        Collection<PeriodType> periodTypes = periodService.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType refA = it.next();
        PeriodType refB = it.next();

        PeriodType periodTypeA = periodService.getPeriodTypeByName( refA.getName() );
        assertNotNull( periodTypeA );
        assertEquals( refA.getName(), periodTypeA.getName() );

        PeriodType periodTypeB = periodService.getPeriodTypeByName( refB.getName() );
        assertNotNull( periodTypeB );
        assertEquals( refB.getName(), periodTypeB.getName() );
    }
}
