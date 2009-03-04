package org.hisp.dhis.system.process.test;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.system.process.InternalProcess;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.system.process.InternalProcess.State;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessTest.java 4399 2008-01-14 14:20:59Z torgeilo $
 */
public class ProcessTest
    extends DhisSpringTest
{
    private InternalProcessCoordinator coordinator;

    @Override
    public void setUpTest()
        throws Exception
    {
        coordinator = (InternalProcessCoordinator) getBean( InternalProcessCoordinator.ID );
    }

    public void testProcessEndWaitLock()
    {
        System.out.println();
        System.out.println( "|----------------------------------------------------------|" );
        System.out.println( "| Timeout test:                                            |" );
        System.out.println( "| - If the test doesn't continue in a few milliseconds     |" );
        System.out.println( "|   then the test has failed and you need to press ctrl-c! |" );

        (new ProcessEndWaitLock()).waitForProcessEnd( 5 );

        System.out.println( "| - Completed successfully! :-)                            |" );
        System.out.println( "|----------------------------------------------------------|" );
        System.out.println();
    }

    public void testStateAndInterrupt()
        throws Exception
    {
        SleepingInternalProcess p1 = coordinator.newProcess( "d", "test" );
        SleepingInternalProcess p2 = coordinator.newProcess( "d", "test" );

        ProcessEndWaitLock lock1 = new ProcessEndWaitLock();
        p1.addInternalProcessListener( lock1 );

        coordinator.requestProcessExecution( p1 );
        coordinator.requestProcessExecution( p2 );

        assertTrue( p1.getState().isStarted() );
        assertFalse( p1.getState().isFinished() );
        assertFalse( p1.getState().isInterrupted() );
        assertNull( p1.getState().getResult() );
        assertNotNull( p1.getState().getStartTime() );
        assertNull( p1.getState().getEndTime() );

        assertFalse( p2.getState().isStarted() );
        assertFalse( p2.getState().isFinished() );
        assertFalse( p2.getState().isInterrupted() );
        assertNull( p2.getState().getResult() );
        assertNull( p2.getState().getStartTime() );
        assertNull( p2.getState().getEndTime() );

        coordinator.interruptOrCancelProcess( p2.getId() );

        assertFalse( p2.getState().isStarted() );
        assertFalse( p2.getState().isFinished() );
        assertTrue( p2.getState().isInterrupted() );
        assertNull( p2.getState().getResult() );
        assertNull( p2.getState().getStartTime() );
        assertNull( p2.getState().getEndTime() );

        coordinator.removeEndedProcess( p2.getId() );

        coordinator.interruptOrCancelProcess( p1.getId() );

        lock1.waitForProcessEnd( 10000 );

        assertTrue( p1.getState().isStarted() );
        assertTrue( p1.getState().isFinished() );
        assertTrue( p1.getState().isInterrupted() );
        assertEquals( InternalProcess.State.INTERRUPTED, p1.getState().getResult() );
        assertNotNull( p1.getState().getStartTime() );
        assertNotNull( p1.getState().getEndTime() );

        coordinator.removeEndedProcess( p1.getId() );
    }

    public void testQueue()
        throws Exception
    {
        SleepingInternalProcess p1 = coordinator.newProcess( "c", "test" );
        SleepingInternalProcess p2 = coordinator.newProcess( "a", "test" );
        SleepingInternalProcess p3 = coordinator.newProcess( "b", "test" );
        SleepingInternalProcess p4 = coordinator.newProcess( "a", "test" );
        SleepingInternalProcess p5 = coordinator.newProcess( "c", "test" );
        SleepingInternalProcess p6 = coordinator.newProcess( "a", "test" );
        SleepingInternalProcess p7 = coordinator.newProcess( "a", "test" );
        SleepingInternalProcess p8 = coordinator.newProcess( "b", "test" );

        assertEquals( "c", p1.getType() );
        assertEquals( "a", p2.getType() );
        assertEquals( "b", p3.getType() );
        assertEquals( "a", p4.getType() );
        assertEquals( "c", p5.getType() );
        assertEquals( "a", p6.getType() );
        assertEquals( "a", p7.getType() );
        assertEquals( "b", p8.getType() );

        assertEquals( "test", p1.getOwner() );
        assertEquals( "test", p2.getOwner() );
        assertEquals( "test", p3.getOwner() );
        assertEquals( "test", p4.getOwner() );
        assertEquals( "test", p5.getOwner() );
        assertEquals( "test", p6.getOwner() );
        assertEquals( "test", p7.getOwner() );
        assertEquals( "test", p8.getOwner() );

        ProcessEndWaitLock lock1 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock2 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock3 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock4 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock5 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock6 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock7 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock8 = new ProcessEndWaitLock();

        p1.addInternalProcessListener( lock1 );
        p2.addInternalProcessListener( lock2 );
        p3.addInternalProcessListener( lock3 );
        p4.addInternalProcessListener( lock4 );
        p5.addInternalProcessListener( lock5 );
        p6.addInternalProcessListener( lock6 );
        p7.addInternalProcessListener( lock7 );
        p8.addInternalProcessListener( lock8 );

        coordinator.requestProcessExecution( p1 );
        coordinator.requestProcessExecution( p2 );
        coordinator.requestProcessExecution( p3 );
        coordinator.requestProcessExecution( p4 );
        coordinator.requestProcessExecution( p5 );
        coordinator.requestProcessExecution( p6 );
        coordinator.requestProcessExecution( p7 );
        coordinator.requestProcessExecution( p8 );

        try
        {
            coordinator.requestProcessExecution( p1 );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        lock1.waitForProcessEnd( 100 );
        lock2.waitForProcessEnd( 100 );
        lock3.waitForProcessEnd( 100 );
        lock4.waitForProcessEnd( 100 );
        lock5.waitForProcessEnd( 100 );
        lock6.waitForProcessEnd( 100 );
        lock7.waitForProcessEnd( 100 );
        lock8.waitForProcessEnd( 100 );

        assertTrue( p1.getState().isFinished() );
        assertTrue( p2.getState().isFinished() );
        assertTrue( p3.getState().isFinished() );
        assertTrue( p4.getState().isFinished() );
        assertTrue( p5.getState().isFinished() );
        assertTrue( p6.getState().isFinished() );
        assertTrue( p7.getState().isFinished() );
        assertTrue( p8.getState().isFinished() );

        assertEquals( State.SUCCESS, p1.getState().getResult() );
        assertEquals( State.SUCCESS, p2.getState().getResult() );
        assertEquals( State.SUCCESS, p3.getState().getResult() );
        assertEquals( State.SUCCESS, p4.getState().getResult() );
        assertEquals( State.SUCCESS, p5.getState().getResult() );
        assertEquals( State.SUCCESS, p6.getState().getResult() );
        assertEquals( State.SUCCESS, p7.getState().getResult() );
        assertEquals( State.SUCCESS, p8.getState().getResult() );

        assertTrue( startTime( p1 ) <= endTime( p1 ) );
        assertTrue( startTime( p2 ) <= endTime( p2 ) );
        assertTrue( startTime( p3 ) <= endTime( p3 ) );
        assertTrue( startTime( p4 ) <= endTime( p4 ) );
        assertTrue( startTime( p5 ) <= endTime( p5 ) );
        assertTrue( startTime( p6 ) <= endTime( p6 ) );
        assertTrue( startTime( p7 ) <= endTime( p7 ) );
        assertTrue( startTime( p8 ) <= endTime( p8 ) );

        assertTrue( endTime( p1 ) <= startTime( p2 ) );
        assertTrue( endTime( p2 ) <= startTime( p3 ) );
        assertTrue( endTime( p3 ) <= startTime( p4 ) );
        assertTrue( endTime( p4 ) <= startTime( p5 ) );

        assertTrue( endTime( p5 ) <= startTime( p6 ) );
        assertTrue( endTime( p5 ) <= startTime( p7 ) );

        assertTrue( endTime( p6 ) <= startTime( p8 ) );
        assertTrue( endTime( p7 ) <= startTime( p8 ) );
    }

    private static final long startTime( InternalProcess p )
    {
        return p.getState().getStartTime().getTime();
    }

    private static final long endTime( InternalProcess p )
    {
        return p.getState().getEndTime().getTime();
    }
}
