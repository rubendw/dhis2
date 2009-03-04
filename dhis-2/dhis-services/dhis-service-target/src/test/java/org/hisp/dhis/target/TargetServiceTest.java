package org.hisp.dhis.target;

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

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Alexander Ottesen
 * @version $Id: TargetServiceTest.java 4222 2007-12-06 12:52:02Z alexao $
 */
public class TargetServiceTest
    extends DhisConvenienceTest
{
    private TargetService targetService;
    
    private IndicatorService indicatorService;
    
    private PeriodService periodService;
    
    private OrganisationUnitService organisationUnitService;
    
    private Calendar calendar = Calendar.getInstance();
    
    // -------------------------------------------------------------------------
    // Supporting data
    // -------------------------------------------------------------------------
    
    private Indicator indicatorA;
    
    private Indicator indicatorB;
    
    private Indicator indicatorC;
    
    private Indicator indicatorD;
    
    private IndicatorType type;
    
    private Period periodA;
    
    private Period periodB;
    
    private Period periodC;
    
    private Period periodD;
    
    private OrganisationUnit orgUnitA;
    
    private OrganisationUnit orgUnitB;
    
    private OrganisationUnit orgUnitC;
    
    private OrganisationUnit orgUnitD;
    
    private Date getDay( int day )
    {
        calendar.clear();
        calendar.set( Calendar.DAY_OF_YEAR, day );

        return calendar.getTime();
    }
    
    public void setUpTest()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Allow access to all persistence implementations needed
        // ---------------------------------------------------------------------
        
        targetService = (TargetService) getBean( TargetService.ID );
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        periodService = (PeriodService) getBean( PeriodService.ID );
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
     
        // ---------------------------------------------------------------------
        // Populate the testing environment
        // ---------------------------------------------------------------------
        
        type = new IndicatorType( "IndicatorType", 100 );
        indicatorService.addIndicatorType( type );

        indicatorA = createIndicator( 'A', type );
        indicatorA.setNumerator( "4" );
        indicatorA.setDenominator( "100" );
        
        indicatorB = createIndicator( 'B', type );
        indicatorB.setNumerator( "3" );
        indicatorB.setDenominator( "150" );
        
        indicatorC = createIndicator( 'C', type );
        indicatorC.setNumerator( "9" );
        indicatorC.setDenominator( "300" );
        
        indicatorD = createIndicator( 'D', type );
        indicatorD.setNumerator( "1" );
        indicatorD.setDenominator( "100" );
        
        indicatorService.addIndicator( indicatorA );
        indicatorService.addIndicator( indicatorB );
        indicatorService.addIndicator( indicatorC );
        indicatorService.addIndicator( indicatorD );
        
        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();
        periodA = new Period( periodType, getDay( 5 ), getDay( 6 ) );
        periodB = new Period( periodType, getDay( 6 ), getDay( 7 ) );
        periodC = new Period( periodType, getDay( 7 ), getDay( 8 ) );
        periodD = new Period( periodType, getDay( 8 ), getDay( 9 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        
        orgUnitA = new OrganisationUnit( "OrganisationUnit A", "orgUnitA", "orgUnitA",
            new Date(), new Date(), true, "Organisation Unit A" );
        orgUnitB = new OrganisationUnit( "OrganisationUnit B", "orgUnitB", "orgUnitB",
            new Date(), new Date(), true, "Organisation Unit B" );
        orgUnitC = new OrganisationUnit( "OrganisationUnit C", "orgUnitC", "orgUnitC",
            new Date(), new Date(), true, "Organisation Unit C" );
        orgUnitD = new OrganisationUnit( "OrganisationUnit D", "orgUnitD", "orgUnitD",
            new Date(), new Date(), true, "Organisation Unit D" );
    
        organisationUnitService.addOrganisationUnit( orgUnitA );
        organisationUnitService.addOrganisationUnit( orgUnitB );
        organisationUnitService.addOrganisationUnit( orgUnitC );
        organisationUnitService.addOrganisationUnit( orgUnitD );
    }
    
    public void testAddTarget()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10.0 );
        Target targetB = new Target( indicatorB, orgUnitA, periodB, "targetB",
            "B", "Target B", 20.0 );
        Target targetC = new Target( indicatorC, orgUnitB, periodB, "targetC",
            "C", "Target C", 30.0 );
        Target targetD = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "D", "Target D", 40.0 );
        
        int idA = targetService.addTarget( targetA );
        int idB = targetService.addTarget( targetB );
        int idC = targetService.addTarget( targetC );
        
        try
        {            
            targetService.addTarget( targetD );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception e )
        {
            // Expected
        }
        
        targetA = targetService.getTarget( idA );
        assertNotNull( targetA );
        assertEquals( indicatorA.getId(), targetA.getIndicator().getId() );
        assertEquals( orgUnitA.getId(), targetA.getSource().getId() );
        assertEquals( periodA.getId(), targetA.getPeriod().getId() );
        assertEquals( "targetA", targetA.getName() );
        assertEquals( "A", targetA.getShortName() );
        assertEquals( "Target A", targetA.getDescription() );
        assertEquals( 10.0, targetA.getValue() );
        assertEquals( 4.0, targetA.getCurrentValue() );
        
        targetB = targetService.getTarget( idB );
        assertNotNull( targetB );
        assertEquals( indicatorB.getId(), targetB.getIndicator().getId() );
        assertEquals( orgUnitA.getId(), targetB.getSource().getId() );
        assertEquals( periodB.getId(), targetB.getPeriod().getId() );
        assertEquals( "targetB", targetB.getName() );
        assertEquals( "B", targetB.getShortName() );
        assertEquals( "Target B", targetB.getDescription() );
        assertEquals( 20.0, targetB.getValue() );
        assertEquals( 2.0, targetB.getCurrentValue() );
        
        targetC = targetService.getTarget( idC );
        assertNotNull( targetC );
        assertEquals( indicatorC.getId(), targetC.getIndicator().getId() );
        assertEquals( orgUnitB.getId(), targetC.getSource().getId() );
        assertEquals( periodB.getId(), targetC.getPeriod().getId() );
        assertEquals( "targetC", targetC.getName() );
        assertEquals( "C", targetC.getShortName() );
        assertEquals( "Target C", targetC.getDescription() );
        assertEquals( 30.0, targetC.getValue() );
        assertEquals( 3.0, targetC.getCurrentValue() );
    }
    
    public void testAddTargetToMultipleOrgUnits()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10.0 );
        Target targetB = new Target( indicatorB, orgUnitA, periodB, "targetB",
            "B", "Target B", 20.0 );
        Target targetC = new Target( indicatorC, orgUnitB, periodB, "targetC",
            "C", "Target C", 30.0 );
        Target targetD = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "D", "Target D", 40.0 );
        
        int idA = targetService.addTarget( targetA );
        int idB = targetService.addTarget( targetB );
        int idC = targetService.addTarget( targetC );        
        
        try
        {            
            targetService.addTarget( targetD );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception e )
        {
            // Expected
        }
        
        targetA = targetService.getTarget( idA );
        assertNotNull( targetA );
        assertEquals( indicatorA.getId(), targetA.getIndicator().getId() );
        assertEquals( orgUnitA.getId(), targetA.getSource().getId() );
        assertEquals( periodA.getId(), targetA.getPeriod().getId() );
        assertEquals( "targetA", targetA.getName() );
        assertEquals( "A", targetA.getShortName() );
        assertEquals( "Target A", targetA.getDescription() );
        assertEquals( 10.0, targetA.getValue() );
        assertEquals( 4.0, targetA.getCurrentValue() );
        
        targetB = targetService.getTarget( idB );
        assertNotNull( targetB );
        assertEquals( indicatorB.getId(), targetB.getIndicator().getId() );
        assertEquals( orgUnitA.getId(), targetB.getSource().getId() );
        assertEquals( periodB.getId(), targetB.getPeriod().getId() );
        assertEquals( "targetB", targetB.getName() );
        assertEquals( "B", targetB.getShortName() );
        assertEquals( "Target B", targetB.getDescription() );
        assertEquals( 20.0, targetB.getValue() );
        assertEquals( 2.0, targetB.getCurrentValue() );
        
        targetC = targetService.getTarget( idC );
        assertNotNull( targetC );
        assertEquals( indicatorC.getId(), targetC.getIndicator().getId() );
        assertEquals( orgUnitB.getId(), targetC.getSource().getId() );
        assertEquals( periodB.getId(), targetC.getPeriod().getId() );
        assertEquals( "targetC", targetC.getName() );
        assertEquals( "C", targetC.getShortName() );
        assertEquals( "Target C", targetC.getDescription() );
        assertEquals( 30.0, targetC.getValue() );
        assertEquals( 3.0, targetC.getCurrentValue() );
    }
    
    public void testDeleteTarget()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10.0 );
        Target targetB = new Target( indicatorB, orgUnitA, periodB, "targetB",
            "B", "Target B", 20.0 );
        Target targetC = new Target( indicatorC, orgUnitB, periodB, "targetC",
            "C", "Target C", 30.0 );
        Target targetD = new Target( indicatorB, orgUnitD, periodA, "targetD",
            "D", "Target D", 40.0 );
        
        int idA = targetService.addTarget( targetA );
        int idB = targetService.addTarget( targetB );
        int idC = targetService.addTarget( targetC );
        int idD = targetService.addTarget( targetD );
        
        assertNotNull( targetService.getTarget( idA ) );
        assertNotNull( targetService.getTarget( idB ) );
        assertNotNull( targetService.getTarget( idC ) );
        assertNotNull( targetService.getTarget( idD ) );
        
        targetService.deleteTarget( targetA );
        assertNull( targetService.getTarget( idA ) );
        assertNotNull( targetService.getTarget( idB ) );
        assertNotNull( targetService.getTarget( idC ) );
        assertNotNull( targetService.getTarget( idD ) );
        
        targetService.deleteTarget( targetB );
        assertNull( targetService.getTarget( idA ) );
        assertNull( targetService.getTarget( idB ) );
        assertNotNull( targetService.getTarget( idC ) );
        assertNotNull( targetService.getTarget( idD ) );
        
        targetService.deleteTarget( targetC );
        assertNull( targetService.getTarget( idA ) );
        assertNull( targetService.getTarget( idB ) );
        assertNull( targetService.getTarget( idC ) );
        assertNotNull( targetService.getTarget( idD ) );
        
        targetService.deleteTarget( targetD );
        assertNull( targetService.getTarget( idA ) );
        assertNull( targetService.getTarget( idB ) );
        assertNull( targetService.getTarget( idC ) );
        assertNull( targetService.getTarget( idD ) );
    }
    
    public void testGetAllTargets()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10.0 );
        Target targetB = new Target( indicatorB, orgUnitA, periodB, "targetB",
            "B", "Target B", 20.0 );
        Target targetC = new Target( indicatorC, orgUnitB, periodB, "targetC",
            "C", "Target C", 30.0 );
        Target targetD = new Target( indicatorB, orgUnitD, periodA, "targetD",
            "D", "Target D", 40.0 );
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<Target> targets = targetService.getAllTargets();
        assertNotNull( targets );
        assertEquals( 4, targets.size() );
    }
    
    public void testGetTargetByName()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10.0 );
        Target targetB = new Target( indicatorB, orgUnitA, periodB, "targetB",
            "B", "Target B", 20.0 );
        Target targetC = new Target( indicatorC, orgUnitB, periodB, "targetC",
            "C", "Target C", 30.0 );
        Target targetD = new Target( indicatorB, orgUnitD, periodA, "targetD",
            "D", "Target D", 40.0 );
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Target testA = targetService.getTargetByName( "targetA" );
        Target testB = targetService.getTargetByName( "targetB" );
        Target testC = targetService.getTargetByName( "targetC" );
        Target testD = targetService.getTargetByName( "targetD" );
        
        assertEquals( targetA, testA );
        assertEquals( targetB, testB );
        assertEquals( targetC, testC );
        assertEquals( targetD, testD );
    }
    
    public void testGetTargetByShortName()
    {
        Target targetA = new Target(indicatorA, orgUnitA, periodA, "targetA-test", 
            "targetA", "targetA-description-test", 10.0);
        Target targetB = new Target(indicatorB, orgUnitB, periodB, "targetB-test", 
            "targetB", "targetB-description-test", 20.0);
        Target targetC = new Target(indicatorC, orgUnitC, periodC, "targetC-test", 
            "targetC", "targetC-description-test", 30.0);
        Target targetD = new Target(indicatorD, orgUnitD, periodD, "targetD-test", 
            "targetD", "targetD-description-test", 40.0);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Target testA = targetService.getTargetByShortName( "targetA" );
        Target testB = targetService.getTargetByShortName( "targetB" );
        Target testC = targetService.getTargetByShortName( "targetC" );
        Target testD = targetService.getTargetByShortName( "targetD" );
        
        assertEquals( targetA, testA );
        assertEquals( targetB, testB );
        assertEquals( targetC, testC );
        assertEquals( targetD, testD );
    }
    
    public void testGetTargetsIndicator()
    {
        Target targetA = new Target(indicatorA, orgUnitA, periodA, "targetA-test", 
            "targetA", "targetA-description-test", 10.0);
        Target targetB = new Target(indicatorB, orgUnitB, periodB, "targetB-test", 
            "targetB", "targetB-description-test", 20.0);
        Target targetC = new Target(indicatorA, orgUnitC, periodC, "targetC-test", 
            "targetC", "targetC-description-test", 30.0);
        Target targetD = new Target(indicatorB, orgUnitD, periodD, "targetD-test", 
            "targetD", "targetD-description-test", 40.0);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<Target> targetsA = targetService.getTargets( indicatorA );
        Collection<Target> targetsB = targetService.getTargets( indicatorB );
        
        assertTrue( targetsA.contains( targetA ) );
        assertTrue( targetsA.contains( targetC ) );
        assertTrue( targetsB.contains( targetB ) );
        assertTrue( targetsB.contains( targetD ) );
        assertFalse( targetsA.contains( targetD ) );
    }
    
    public void testGetTargetsPeriod()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitB, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorC, orgUnitC, periodA, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitD, periodB, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<Target> targetsA = targetService.getTargets( periodA );
        Collection<Target> targetsB = targetService.getTargets( periodB );
        
        assertTrue( targetsA.contains( targetA ) );
        assertTrue( targetsA.contains( targetC ) );
        assertTrue( targetsB.contains( targetB ) );
        assertTrue( targetsB.contains( targetD ) );
        assertFalse( targetsA.contains( targetD ) );
    }
    
    public void testGetTargetsSource()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitB, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorC, orgUnitA, periodC, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitB, periodD, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<Target> targetsA = targetService.getTargets( orgUnitA );
        Collection<Target> targetsB = targetService.getTargets( orgUnitB );
        
        assertTrue( targetsA.contains( targetA ) );
        assertTrue( targetsA.contains( targetC ) );
        assertTrue( targetsB.contains( targetB ) );
        assertTrue( targetsB.contains( targetD ) );
        assertFalse( targetsA.contains( targetD ) );
    }
    
    public void testGetTargetsIndicatorPeriod()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitB, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorA, orgUnitC, periodA, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorB, orgUnitD, periodB, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
       
        Collection<Target> targetsA = targetService.getTargets( indicatorA, periodA );
        Collection<Target> targetsB = targetService.getTargets( indicatorB, periodB );
        
        assertTrue( targetsA.contains( targetA ) );
        assertTrue( targetsA.contains( targetC ) );
        assertTrue( targetsB.contains( targetB ) );
        assertTrue( targetsB.contains( targetD ) );
        assertFalse( targetsA.contains( targetB ) );
    }
    
    public void testGetTargetsIndicatorSource()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitB, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorA, orgUnitA, periodC, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorB, orgUnitB, periodD, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
       
        Collection<Target> targetsA = targetService.getTargets( indicatorA, orgUnitA );
        Collection<Target> targetsB = targetService.getTargets( indicatorB, orgUnitB );
        
        assertTrue( targetsA.contains( targetA ) );
        assertTrue( targetsA.contains( targetC ) );
        assertTrue( targetsB.contains( targetB ) );
        assertTrue( targetsB.contains( targetD ) );
        assertFalse( targetsA.contains( targetB ) );
    }
    
    public void testGetTargetsPeriodSource()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitB, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorC, orgUnitA, periodA, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitB, periodB, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
       
        Collection<Target> targetsA = targetService.getTargets( periodA, orgUnitA );
        Collection<Target> targetsB = targetService.getTargets( periodB, orgUnitB );
        
        assertTrue( targetsA.contains( targetA ) );
        assertTrue( targetsA.contains( targetC ) );
        assertTrue( targetsB.contains( targetB ) );
        assertTrue( targetsB.contains( targetD ) );
        assertFalse( targetsA.contains( targetB ) );
    }
    
    public void testGetTargetsSourceCollection()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitB, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorC, orgUnitC, periodC, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitD, periodD, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<OrganisationUnit> sources = new ArrayList<OrganisationUnit>();
        sources.add( orgUnitA );
        sources.add( orgUnitB );
        
        Collection<Target> targets = targetService.getTargets( sources );
        
        assertTrue( targets.contains( targetA ) );
        assertTrue( targets.contains( targetB ) );
        assertFalse( targets.contains( targetC ) );
        assertFalse( targets.contains( targetD ) );
    }
    
    public void testGetTargetsIndicatorSourceCollection()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorA, orgUnitA, periodB, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorC, orgUnitA, periodB, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitD, periodD, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<OrganisationUnit> sources = new ArrayList<OrganisationUnit>();
        sources.add( orgUnitA );
        sources.add( orgUnitB );
        
        Collection<Target> targets = targetService.getTargets( indicatorA, sources );
        
        assertTrue( targets.contains( targetA ) );
        assertTrue( targets.contains( targetB ) );
        assertFalse( targets.contains( targetC ) );
        assertFalse( targets.contains( targetD ) );
    }
    
    public void testGetTargetsPeriodSourceCollection()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorB, orgUnitA, periodA, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorA, orgUnitA, periodC, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitD, periodD, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<OrganisationUnit> sources = new ArrayList<OrganisationUnit>();
        sources.add( orgUnitA );
        sources.add( orgUnitB );
        
        Collection<Target> targets = targetService.getTargets( periodA, sources );
        
        assertTrue( targets.contains( targetA ) );
        assertTrue( targets.contains( targetB ) );
        assertFalse( targets.contains( targetC ) );
        assertFalse( targets.contains( targetD ) );
    }
    
    public void testGetTargetsIndicatorPeriodSourceCollection()
    {
        Target targetA = new Target( indicatorA, orgUnitA, periodA, "targetA",
            "A", "Target A", 10 );
        Target targetB = new Target( indicatorA, orgUnitB, periodA, "targetB",
            "B", "Target B", 10 );
        Target targetC = new Target( indicatorA, orgUnitA, periodC, "targetC",
            "C", "Target C", 10);
        Target targetD = new Target( indicatorD, orgUnitD, periodD, "targetD",
            "D", "Target D", 10);
        
        targetService.addTarget( targetA );
        targetService.addTarget( targetB );
        targetService.addTarget( targetC );
        targetService.addTarget( targetD );
        
        Collection<OrganisationUnit> sources = new ArrayList<OrganisationUnit>();
        sources.add( orgUnitA );
        sources.add( orgUnitB );
        
        Collection<Target> targets = targetService.getTargets( indicatorA, periodA, sources );
        
        assertTrue( targets.contains( targetA ) );
        assertTrue( targets.contains( targetB ) );
        assertFalse( targets.contains( targetC ) );
        assertFalse( targets.contains( targetD ) );
    }
        
    public void testCompareTargetToIndicator()
    {
        Target targetA = new Target(indicatorA, orgUnitA, periodA, "targetA", 
            "A", "Target A", 4.0);
        Target targetB = new Target(indicatorB, orgUnitB, periodB, "targetB", 
            "B", "Target B", 2.0);
        Target targetC = new Target( indicatorC, orgUnitC, periodC, "targetC",
            "C", "Target C", 3.0);
        Target targetD = new Target( indicatorD, orgUnitD, periodD, "targetD",
            "D", "Target D", 0.5);
        
        int idA = targetService.addTarget( targetA );
        int idB = targetService.addTarget( targetB );
        int idC = targetService.addTarget( targetC );
        int idD = targetService.addTarget( targetD );
        
        targetA = targetService.getTarget( idA );
        targetB = targetService.getTarget( idB );
        targetC = targetService.getTarget( idC );
        targetD = targetService.getTarget( idD );
        
        assertEquals(targetService.compareTargetToIndicator( targetA ), 0.0);
        assertEquals(targetService.compareTargetToIndicator( targetB ), 0.0);
        assertEquals(targetService.compareTargetToIndicator( targetC ),0.0);
        assertEquals(targetService.compareTargetToIndicator( targetD ), -0.5);
    }
}
