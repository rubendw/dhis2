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
import org.hisp.dhis.dataelement.DataElementStore;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueStore;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.system.util.UUIdUtils;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: PeriodStoreTest.java 5472 2008-07-01 14:35:35Z larshelg $
 */
public class PeriodStoreTest
    extends DhisSpringTest
{
    private PeriodStore periodStore;
    
    private DataElementStore dataElementStore;

    private DataElementCategoryOptionComboService categoryOptionComboService;
    
    private SourceStore sourceStore;
    
    private DataValueStore dataValueStore;

    private TransactionManager transactionManager;

    private Calendar calendar;

    private DataElementCategoryOptionCombo optionCombo;
    
    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        periodStore = (PeriodStore) getBean( PeriodStore.ID );
        
        dataElementStore = (DataElementStore) getBean( DataElementStore.ID );

        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        sourceStore = (SourceStore) getBean( SourceStore.ID );
        
        dataValueStore = (DataValueStore) getBean( DataValueStore.ID );

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

    private DataElement createDataElement( char uniqueCharacter )
    {
        DataElement dataElement = new DataElement();

        dataElement.setUuid( UUIdUtils.getUUId() );
        dataElement.setName( "DataElement" + uniqueCharacter );
        dataElement.setAlternativeName( "AlternativeName" + uniqueCharacter );
        dataElement.setShortName( "DE" + uniqueCharacter );
        dataElement.setCode( "Code" + uniqueCharacter );
        dataElement.setDescription( "DataElementDescription" + uniqueCharacter );
        dataElement.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement.setType( DataElement.TYPE_INT );

        return dataElement;
    }
    
    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    public void testAddPeriod()
        throws Exception
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        int idA = periodStore.addPeriod( periodA );
        int idB = periodStore.addPeriod( periodB );
        int idC = periodStore.addPeriod( periodC );

        transactionManager.leave();

        try
        {
            // Should give unique constraint violation.
            periodStore.addPeriod( periodD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected.
        }

        transactionManager.enter();

        periodA = periodStore.getPeriod( idA );
        assertNotNull( periodA );
        assertEquals( idA, periodA.getId() );
        assertEquals( periodTypeA, periodA.getPeriodType() );
        assertEquals( getDay( 0 ), periodA.getStartDate() );
        assertEquals( getDay( 1 ), periodA.getEndDate() );

        periodB = periodStore.getPeriod( idB );
        assertNotNull( periodB );
        assertEquals( idB, periodB.getId() );
        assertEquals( periodTypeA, periodB.getPeriodType() );
        assertEquals( getDay( 1 ), periodB.getStartDate() );
        assertEquals( getDay( 2 ), periodB.getEndDate() );

        periodC = periodStore.getPeriod( idC );
        assertNotNull( periodC );
        assertEquals( idC, periodC.getId() );
        assertEquals( periodTypeB, periodC.getPeriodType() );
        assertEquals( getDay( 2 ), periodC.getStartDate() );
        assertEquals( getDay( 3 ), periodC.getEndDate() );

        transactionManager.leave();
    }

    public void testDeleteAndGetPeriod()
        throws Exception
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        int idA = periodStore.addPeriod( periodA );
        int idB = periodStore.addPeriod( periodB );
        int idC = periodStore.addPeriod( periodC );
        int idD = periodStore.addPeriod( periodD );

        assertNotNull( periodStore.getPeriod( idA ) );
        assertNotNull( periodStore.getPeriod( idB ) );
        assertNotNull( periodStore.getPeriod( idC ) );
        assertNotNull( periodStore.getPeriod( idD ) );

        periodStore.deletePeriod( periodA );
        assertNull( periodStore.getPeriod( idA ) );
        assertNotNull( periodStore.getPeriod( idB ) );
        assertNotNull( periodStore.getPeriod( idC ) );
        assertNotNull( periodStore.getPeriod( idD ) );

        periodStore.deletePeriod( periodB );
        assertNull( periodStore.getPeriod( idA ) );
        assertNull( periodStore.getPeriod( idB ) );
        assertNotNull( periodStore.getPeriod( idC ) );
        assertNotNull( periodStore.getPeriod( idD ) );

        periodStore.deletePeriod( periodC );
        assertNull( periodStore.getPeriod( idA ) );
        assertNull( periodStore.getPeriod( idB ) );
        assertNull( periodStore.getPeriod( idC ) );
        assertNotNull( periodStore.getPeriod( idD ) );

        periodStore.deletePeriod( periodD );
        assertNull( periodStore.getPeriod( idA ) );
        assertNull( periodStore.getPeriod( idB ) );
        assertNull( periodStore.getPeriod( idC ) );
        assertNull( periodStore.getPeriod( idD ) );

        transactionManager.leave();
    }

    public void testGetPeriod()
        throws Exception
    {
        transactionManager.enter();
        
        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        Period periodE = new Period( periodTypeA, getDay( 3 ), getDay( 4 ) );
        int idA = periodStore.addPeriod( periodA );
        int idB = periodStore.addPeriod( periodB );
        int idC = periodStore.addPeriod( periodC );
        int idD = periodStore.addPeriod( periodD );
        int idE = periodStore.addPeriod( periodE );

        periodA = periodStore.getPeriod( getDay( 0 ), getDay( 1 ), periodTypeA );
        assertNotNull( periodA );
        assertEquals( idA, periodA.getId() );
        assertEquals( periodTypeA, periodA.getPeriodType() );
        assertEquals( getDay( 0 ), periodA.getStartDate() );
        assertEquals( getDay( 1 ), periodA.getEndDate() );
        
        periodB = periodStore.getPeriod( getDay( 1 ), getDay( 2 ), periodTypeA );
        assertNotNull( periodB );
        assertEquals( idB, periodB.getId() );
        assertEquals( periodTypeA, periodB.getPeriodType() );
        assertEquals( getDay( 1 ), periodB.getStartDate() );
        assertEquals( getDay( 2 ), periodB.getEndDate() );

        periodC = periodStore.getPeriod( getDay( 2 ), getDay( 3 ), periodTypeB );
        assertNotNull( periodC );
        assertEquals( idC, periodC.getId() );
        assertEquals( periodTypeB, periodC.getPeriodType() );
        assertEquals( getDay( 2 ), periodC.getStartDate() );
        assertEquals( getDay( 3 ), periodC.getEndDate() );

        periodD = periodStore.getPeriod( getDay( 3 ), getDay( 4 ), periodTypeB );
        assertNotNull( periodD );
        assertEquals( idD, periodD.getId() );
        assertEquals( periodTypeB, periodD.getPeriodType() );
        assertEquals( getDay( 3 ), periodD.getStartDate() );
        assertEquals( getDay( 4 ), periodD.getEndDate() );

        periodE = periodStore.getPeriod( getDay( 3 ), getDay( 4 ), periodTypeA );
        assertNotNull( periodE );
        assertEquals( idE, periodE.getId() );
        assertEquals( periodTypeA, periodE.getPeriodType() );
        assertEquals( getDay( 3 ), periodE.getStartDate() );
        assertEquals( getDay( 4 ), periodE.getEndDate() );
        
        assertNull( periodStore.getPeriod( getDay( 1 ), getDay( 2 ), periodTypeB ) );
        assertNull( periodStore.getPeriod( getDay( 2 ), getDay( 3 ), periodTypeA ) );
        assertNull( periodStore.getPeriod( getDay( 0 ), getDay( 5 ), periodTypeB ) );
        assertNull( periodStore.getPeriod( getDay( 4 ), getDay( 3 ), periodTypeB ) );
        assertNull( periodStore.getPeriod( getDay( 5 ), getDay( 6 ), periodTypeA ) );
        
        transactionManager.leave();
    }
    
    public void testGetAllPeriods()
        throws Exception
    {
        PeriodType periodType = periodStore.getAllPeriodTypes().iterator().next();

        Period periodA = new Period( periodType, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodType, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodType, getDay( 2 ), getDay( 3 ) );
        
        periodStore.addPeriod( periodA );
        periodStore.addPeriod( periodB );
        periodStore.addPeriod( periodC );
        
        Collection<Period> periods = periodStore.getAllPeriods();
        
        assertNotNull( periods );
        assertEquals( 3, periods.size() );
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );        
    }
    
    public void testGetPeriodsBetweenDates()
        throws Exception
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        periodStore.addPeriod( periodA );
        periodStore.addPeriod( periodB );
        periodStore.addPeriod( periodC );
        periodStore.addPeriod( periodD );

        Collection<Period> periods = periodStore.getPeriodsBetweenDates( getDay( 0 ), getDay( 0 ) );
        assertNotNull( periods );
        assertEquals( 0, periods.size() );

        periods = periodStore.getPeriodsBetweenDates( getDay( 0 ), getDay( 1 ) );
        assertNotNull( periods );
        assertEquals( 1, periods.size() );
        assertEquals( periodA, periods.iterator().next() );

        periods = periodStore.getPeriodsBetweenDates( getDay( 1 ), getDay( 4 ) );
        assertNotNull( periods );
        assertEquals( 3, periods.size() );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );

        periods = periodStore.getPeriodsBetweenDates( getDay( 0 ), getDay( 5 ) );
        assertNotNull( periods );
        assertEquals( 4, periods.size() );
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );

        transactionManager.leave();
    }

    public void testGetIntersectingPeriodsByPeriodType()
        throws Exception
    {
    	transactionManager.enter();
        
    	PeriodType ypt = PeriodType.getPeriodTypeByName( "Yearly" );
    	
    	Date jan2006 = getDate(1, 1, 2006);
    	Date dec2006 = getDate(31, 12, 2006);
    	Date jan2007 = getDate(1, 1, 2007);
    	Date dec2007 = getDate(31, 12, 2007);
    	
    	Period periodA = new Period(ypt, jan2006, dec2006);
    	Period periodB = new Period(ypt, jan2007, dec2007);    	
    	periodStore.addPeriod(periodA);
    	periodStore.addPeriod(periodB);    	
    	
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
    	
    	periodStore.addPeriod(periodC);
    	periodStore.addPeriod(periodD);
    	periodStore.addPeriod(periodE);
    	periodStore.addPeriod(periodF);
    	periodStore.addPeriod(periodG);
    	periodStore.addPeriod(periodH);
    	periodStore.addPeriod(periodI);
    	periodStore.addPeriod(periodJ);
    	periodStore.addPeriod(periodK);
    	periodStore.addPeriod(periodL);
    	periodStore.addPeriod(periodM);
    	periodStore.addPeriod(periodN);
    	
    	Collection<Period> periodsA = periodStore.getIntersectingPeriodsByPeriodType(ypt, getDate(1,6,2006), getDate(30,11,2006)); 
    	assertNotNull( periodsA );
    	assertEquals( 1, periodsA.size() );
    	
    	Collection<Period> periodsB = periodStore.getIntersectingPeriodsByPeriodType(mpt, getDate(1,6,2006), getDate(30,11,2006));    	
    	assertNotNull( periodsB );
    	assertEquals( 6, periodsB.size() );
    	
    	transactionManager.leave();
    }
    
    public void testGetIntersectingPeriods()
        throws Exception
    {
        PeriodType type = periodStore.getAllPeriodTypes().iterator().next();
        
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
        
        periodStore.addPeriod( periodA );
        periodStore.addPeriod( periodB );
        periodStore.addPeriod( periodC );
        periodStore.addPeriod( periodD );
        periodStore.addPeriod( periodE );
        periodStore.addPeriod( periodF );
        periodStore.addPeriod( periodG );
        periodStore.addPeriod( periodH );
        periodStore.addPeriod( periodI );
        periodStore.addPeriod( periodJ );
        
        Collection<Period> periods = periodStore.getIntersectingPeriods( getDay( 4 ), getDay( 10 ) );
        
        assertEquals( periods.size(), 6 );
        
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );
        assertTrue( periods.contains( periodE ) );
        assertTrue( periods.contains( periodH ) );
        assertTrue( periods.contains( periodI ) );
        assertTrue( periods.contains( periodJ ) );
    }
    
    public void testGetPeriodsByPeriodType()
        throws Exception
    {
        transactionManager.enter();

        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();

        Period periodA = new Period( periodTypeA, getDay( 0 ), getDay( 1 ) );
        Period periodB = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodC = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        periodStore.addPeriod( periodA );
        periodStore.addPeriod( periodB );
        periodStore.addPeriod( periodC );
        periodStore.addPeriod( periodD );

        Collection<Period> periodsARef = new HashSet<Period>();
        periodsARef.add( periodA );
        periodsARef.add( periodB );
        periodsARef.add( periodC );

        Collection<Period> periodsA = periodStore.getPeriodsByPeriodType( periodTypeA );
        assertNotNull( periodsA );
        assertEquals( periodsARef.size(), periodsA.size() );
        assertTrue( periodsA.containsAll( periodsARef ) );

        Collection<Period> periodsB = periodStore.getPeriodsByPeriodType( periodTypeB );
        assertNotNull( periodsB );
        assertEquals( 1, periodsB.size() );
        assertEquals( periodD, periodsB.iterator().next() );

        Collection<Period> periodsC = periodStore.getPeriodsByPeriodType( periodTypeC );
        assertNotNull( periodsC );
        assertEquals( 0, periodsC.size() );

        transactionManager.leave();
    }

    public void testGetPeriodsWithAssociatedDataValues()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );   
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
    
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
    
        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
     
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );
        
        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        dataValueStore.addDataValue( dataValueD );
        dataValueStore.addDataValue( dataValueE );
        dataValueStore.addDataValue( dataValueF );
        dataValueStore.addDataValue( dataValueG );
        dataValueStore.addDataValue( dataValueH );
        dataValueStore.addDataValue( dataValueI );
        
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
        
        Collection<Period> periods = periodStore.getPeriods( jan, dataElements1, sources1 );
        
        assertEquals( periods.size(), 7 );

        periods = periodStore.getPeriods( feb, dataElements1, sources1 );
        
        assertEquals( periods.size(), 3 );
        
        periods = periodStore.getPeriods( mar, dataElements1, sources1 );

        assertEquals( periods.size(), 1 );
        
        periods = periodStore.getPeriods( apr, dataElements1, sources1 );

        assertEquals( periods.size(), 1 );
        
        periods = periodStore.getPeriods( may, dataElements1, sources1 );

        assertEquals( periods.size(), 0 );
        
        periods = periodStore.getPeriods( jan, dataElements1, sources2 );
        
        assertEquals( periods.size(), 0 );
        
        periods = periodStore.getPeriods( feb, dataElements2, sources1 );

        assertEquals( periods.size(), 0 );
        
        periods = periodStore.getPeriods( mar, dataElements2, sources2 );
    }
    
    public void testPrunePeriods()
        throws Exception
    {
        DataElement dataElementA = new DataElement( "NameA", "AlternativeNameA", "ShortNameA", "CodeA", "DescriptionA",
            true, DataElement.TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM, null );
    
        PeriodType periodType = periodStore.getAllPeriodTypes().iterator().next();
        
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
    
        dataElementStore.addDataElement( dataElementA );
    
        periodStore.addPeriod( periodA );
        periodStore.addPeriod( periodB );
        periodStore.addPeriod( periodC );
        periodStore.addPeriod( periodD );
        periodStore.addPeriod( periodE );
    
        sourceStore.addSource( sourceA );
    
        dataValueStore.addDataValue( dataValueA );
        dataValueStore.addDataValue( dataValueB );
        dataValueStore.addDataValue( dataValueC );
        
        transactionManager.leave();

        Collection<Period> periods = periodStore.getPeriodsByPeriodType( periodType );
        
        assertEquals( periods.size(), 5 );

        transactionManager.enter();
    
        int count = periodStore.prunePeriods();

        assertEquals( count, 2 );

        transactionManager.leave();

        periods = periodStore.getPeriodsByPeriodType( periodType );
        
        assertEquals( periods.size(), 3 );
    }    

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    public void testAddPeriodType()
        throws Exception
    {
        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType refA = it.next();
        PeriodType refB = it.next();
        PeriodType refC = it.next();

        periodStore.deletePeriodType( refA );
        periodStore.deletePeriodType( refB );
        periodStore.deletePeriodType( refC );

        int idA = periodStore.addPeriodType( refA );
        int idB = periodStore.addPeriodType( refB );
        int idC = periodStore.addPeriodType( refC );

        PeriodType periodTypeA = periodStore.getPeriodType( idA );
        assertNotNull( periodTypeA );
        assertEquals( idA, periodTypeA.getId() );
        assertEquals( refA.getName(), periodTypeA.getName() );

        PeriodType periodTypeB = periodStore.getPeriodType( idB );
        assertNotNull( periodTypeB );
        assertEquals( idB, periodTypeB.getId() );
        assertEquals( refB.getName(), periodTypeB.getName() );

        PeriodType periodTypeC = periodStore.getPeriodType( idC );
        assertNotNull( periodTypeC );
        assertEquals( idC, periodTypeC.getId() );
        assertEquals( refC.getName(), periodTypeC.getName() );
    }

    public void testDeleteAndGetPeriodType()
        throws Exception
    {
        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();
        PeriodType periodTypeD = it.next();

        int idA = periodTypeA.getId();
        int idB = periodTypeB.getId();
        int idC = periodTypeC.getId();
        int idD = periodTypeD.getId();

        assertNotNull( periodStore.getPeriodType( idA ) );
        assertNotNull( periodStore.getPeriodType( idB ) );
        assertNotNull( periodStore.getPeriodType( idC ) );
        assertNotNull( periodStore.getPeriodType( idD ) );

        assertNotNull( periodStore.getPeriodType( periodTypeA.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeB.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeC.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeD.getClass() ) );

        periodStore.deletePeriodType( periodTypeA );
        assertNull( periodStore.getPeriodType( idA ) );
        assertNotNull( periodStore.getPeriodType( idB ) );
        assertNotNull( periodStore.getPeriodType( idC ) );
        assertNotNull( periodStore.getPeriodType( idD ) );
        
        assertNull( periodStore.getPeriodType( periodTypeA.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeB.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeC.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeD.getClass() ) );

        periodStore.deletePeriodType( periodTypeB );
        assertNull( periodStore.getPeriodType( idA ) );
        assertNull( periodStore.getPeriodType( idB ) );
        assertNotNull( periodStore.getPeriodType( idC ) );
        assertNotNull( periodStore.getPeriodType( idD ) );
        
        assertNull( periodStore.getPeriodType( periodTypeA.getClass() ) );
        assertNull( periodStore.getPeriodType( periodTypeB.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeC.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeD.getClass() ) );

        periodStore.deletePeriodType( periodTypeC );
        assertNull( periodStore.getPeriodType( idA ) );
        assertNull( periodStore.getPeriodType( idB ) );
        assertNull( periodStore.getPeriodType( idC ) );
        assertNotNull( periodStore.getPeriodType( idD ) );
        
        assertNull( periodStore.getPeriodType( periodTypeA.getClass() ) );
        assertNull( periodStore.getPeriodType( periodTypeB.getClass() ) );
        assertNull( periodStore.getPeriodType( periodTypeC.getClass() ) );
        assertNotNull( periodStore.getPeriodType( periodTypeD.getClass() ) );

        periodStore.deletePeriodType( periodTypeD );
        assertNull( periodStore.getPeriodType( idA ) );
        assertNull( periodStore.getPeriodType( idB ) );
        assertNull( periodStore.getPeriodType( idC ) );
        assertNull( periodStore.getPeriodType( idD ) );
        
        assertNull( periodStore.getPeriodType( periodTypeA.getClass() ) );
        assertNull( periodStore.getPeriodType( periodTypeB.getClass() ) );
        assertNull( periodStore.getPeriodType( periodTypeC.getClass() ) );
        assertNull( periodStore.getPeriodType( periodTypeD.getClass() ) );
    }
}
