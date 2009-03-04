package org.hisp.dhis.system.process;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.process.InternalProcess.State;
import org.hisp.dhis.system.process.queue.ListProcessQueue;
import org.hisp.dhis.system.process.queue.ProcessQueue;

/**
 * Abstract implementation of the {@link InternalProcessCoordinator} interface
 * which implements all methods except the {@link #newProcess(String)} method.
 * It is thread-safe. Processes which are requested for execution are added to a
 * queue, and processes in the head of the queue are executed in parallel, but
 * not more than the specified maximum of running processes.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: AbstractInternalProcessCoordinator.java 4372 2007-12-23 00:37:47Z torgeilo $
 */
public abstract class AbstractInternalProcessCoordinator
    implements InternalProcessCoordinator, InternalProcessListener
{
    private final Log log = LogFactory.getLog( AbstractInternalProcessCoordinator.class );

    /**
     * Value: {@value}.
     */
    public static final int DEFAULT_MAX_RUNNING_PROCESSES = 10;

    private Map<String, InternalProcess> processIndex = new HashMap<String, InternalProcess>();

    private ProcessQueue<InternalProcess> processQueue = new ListProcessQueue<InternalProcess>();

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private int maxRunningProcesses = DEFAULT_MAX_RUNNING_PROCESSES;

    /**
     * Sets the maximum number of running processes.
     */
    public final void setMaxRunningProcesses( int maxRunningProcesses )
    {
        this.maxRunningProcesses = maxRunningProcesses;
    }

    // -------------------------------------------------------------------------
    // InternalProcessCoordinator
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessCoordinator#requestProcessExecution(org.hisp.dhis.system.process.InternalProcess)
     */
    public final synchronized String requestProcessExecution( InternalProcess internalProcess )
    {
        if ( internalProcess.getId() != null )
        {
            throw new IllegalArgumentException( "Not allowed to reuse internal process objects" );
        }

        final String id = generateId();

        internalProcess.initialize( id );
        internalProcess.addInternalProcessListener( this );

        processIndex.put( id, internalProcess );
        processQueue.add( internalProcess );

        executeHead();

        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessCoordinator#interruptOrCancelProcess(java.lang.String)
     */
    public final synchronized void interruptOrCancelProcess( String id )
    {
        InternalProcess internalProcess = getProcess( id );

        if ( internalProcess != null && !internalProcess.getState().isFinished()
            && !internalProcess.getState().isInterrupted() )
        {
            internalProcess.interrupt();

            if ( !internalProcess.getState().isStarted() )
            {
                processQueue.remove( internalProcess );

                log.debug( "Process removed from queue unstarted" );

                executeHead();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessCoordinator#removeEndedProcess(java.lang.String)
     */
    public final void removeEndedProcess( String id )
    {
        InternalProcess internalProcess = getProcess( id );

        if ( internalProcess != null )
        {
            State state = internalProcess.getState();

            if ( state.isFinished() || ( state.isInterrupted() && !state.isStarted() ) )
            {
                processIndex.remove( id );
            }
            else
            {
                throw new RuntimeException( "Cannot remove an unended internal process" );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessCoordinator#getProcess(java.lang.String)
     */
    public final InternalProcess getProcess( String id )
    {
        return processIndex.get( id );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessCoordinator#getAllProcesses()
     */
    public final Collection<InternalProcess> getAllProcesses()
    {
        return processIndex.values();
    }

    // -------------------------------------------------------------------------
    // InternalProcessListener
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessListener#internalProcessFinished(org.hisp.dhis.system.process.InternalProcessEvent)
     */
    public final synchronized void internalProcessFinished( InternalProcessEvent e )
    {
        InternalProcess internalProcess = e.getSource();
        internalProcess.removeInternalProcessListener( this );

        processQueue.remove( internalProcess );

        executeHead();
    }

    // -------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------

    /**
     * Interrupts remaining processes.
     */
    public final void close()
    {
        for ( InternalProcess process : processIndex.values() )
        {
            interruptOrCancelProcess( process.getId() );
        }
    }

    /**
     * Returns an assumed unique id.
     */
    protected String generateId()
    {
        return UUID.randomUUID().toString();
    }

    private void executeHead()
    {
        int numRunning = 0;
        HashSet<InternalProcess> notStarted = new HashSet<InternalProcess>();

        for ( InternalProcess process : processQueue.getHead() )
        {
            if ( !process.getState().isFinished() )
            {
                if ( process.getState().isStarted() )
                {
                    ++numRunning;
                }
                else
                {
                    notStarted.add( process );
                }
            }
        }

        if ( numRunning < maxRunningProcesses )
        {
            Iterator<InternalProcess> it = notStarted.iterator();

            while ( numRunning < maxRunningProcesses && it.hasNext() )
            {
                it.next().start();
                ++numRunning;
            }
        }
    }
}
