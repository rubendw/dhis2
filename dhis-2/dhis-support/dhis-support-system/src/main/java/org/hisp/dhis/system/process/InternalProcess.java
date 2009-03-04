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

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.process.queue.ProcessQueueConstraintsImpl;

/**
 * Superclass for internal processes. Subclass processes should check the
 * {@link State#isInterrupted()} method more or less frequently to see if it
 * should abort execution.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: InternalProcess.java 4399 2008-01-14 14:20:59Z torgeilo $
 */
public abstract class InternalProcess
    extends ProcessQueueConstraintsImpl
    implements Runnable
{
    private final Log log = LogFactory.getLog( InternalProcess.class );

    private Set<InternalProcessListener> listeners = new HashSet<InternalProcessListener>( 2 );

    private Class<? extends State> stateClass = State.class;

    private Thread thread;

    private String type;

    private String owner;

    private String id;

    private State state;

    // -------------------------------------------------------------------------
    // Listener methods
    // -------------------------------------------------------------------------

    /**
     * Add a process listener.
     */
    public final void addInternalProcessListener( InternalProcessListener listener )
    {
        listeners.add( listener );
    }

    /**
     * Remove a process listener.
     */
    public final void removeInternalProcessListener( InternalProcessListener listener )
    {
        listeners.remove( listener );
    }

    private void fireInternalProcessEnded()
    {
        /*
         * Make a copy so that listeners can remove themselves from the set
         * while looping them.
         */
        final Set<InternalProcessListener> copy = new HashSet<InternalProcessListener>( listeners );

        final InternalProcessEvent event = new InternalProcessEvent( this );

        for ( InternalProcessListener listener : copy )
        {
            listener.internalProcessFinished( event );
        }
    }

    // -------------------------------------------------------------------------
    // Run
    // -------------------------------------------------------------------------

    /**
     * INTERNAL USE ONLY!
     */
    public final void run()
    {
        try
        {
            preExecution();

            state.result = execute();

            if ( state.result == null )
            {
                state.result = State.UNKNOWN;
            }

            postNormalExecution();
        }
        catch ( InterruptedException e )
        {
            log.warn( "Internal process was interrupted", e );

            state.result = State.INTERRUPTED;
        }
        catch ( Throwable t )
        {
            log.error( "Internal process threw exception", t );

            state.result = State.EXCEPTION;
        }

        try
        {
            postExecution();
        }
        catch ( Exception e )
        {
            log.error( "Post-execution threw exception", e );

            // Ignore
        }

        state.endTime = System.currentTimeMillis();

        log.debug( "Internal process ended" );

        fireInternalProcessEnded();
    }

    // -------------------------------------------------------------------------
    // Execution methods
    // -------------------------------------------------------------------------

    /**
     * Overridable method which is called right before the {@link #execute()}
     * method.
     */
    protected void preExecution()
    {
    }

    /**
     * The process method which must be implemented by subclasses.
     */
    protected abstract String execute()
        throws Exception;

    /**
     * Overridable method which is called after the {@link #execute()} method
     * has finished successfully.
     */
    protected void postNormalExecution()
    {
    }

    /**
     * Overridable method which is called after the {@link #execute()} method
     * has finished, regardless of result.
     */
    protected void postExecution()
    {
    }

    // -------------------------------------------------------------------------
    // Control methods
    // -------------------------------------------------------------------------

    /**
     * Sets the process type. Used by the InternalProcessCoordinator upon
     * process loading. INTERNAL USE ONLY!
     */
    /* package */final void setType( String type )
    {
        this.type = type;
    }

    /**
     * Sets the owner of the process. Used by the InternalProcessCoordinator.
     * INTERNAL USE ONLY!
     */
    /* package */
    final void setOwner( String owner )
    {
        this.owner = owner;
    }

    /**
     * Prepares the process for execution. INTERNAL USE ONLY!
     */
    /* package */final void initialize( String id )
    {
        this.id = id;

        state = newInstance( stateClass );
    }

    /**
     * Starts the process. INTERNAL USE ONLY!
     */
    /* package */final void start()
    {
        thread = new Thread( this );
        thread.start();

        log.debug( "Starting internal process" );

        state.startTime = System.currentTimeMillis();
        state.started = true;
    }

    /**
     * Interrupts the thread. INTERNAL USE ONLY!
     */
    /* package */final void interrupt()
    {
        state.interrupted = true;

        if ( thread != null )
        {
            thread.interrupt();
        }
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    /**
     * Sets the state class. The class must have a constructor without
     * arguments. Default state class is {@link State}.
     */
    public final void setStateClass( Class<? extends State> stateClass )
    {
        this.stateClass = stateClass;
    }

    /**
     * Returns the process type.
     */
    public final String getType()
    {
        return type;
    }

    /**
     * Returns the process owner.
     */
    public final String getOwner()
    {
        return owner;
    }

    /**
     * Returns the process ID. The ID is set upon initialization.
     */
    public final String getId()
    {
        return id;
    }

    /**
     * Returns the state. The state is set upon initialization.
     */
    public final State getState()
    {
        return state;
    }

    // -------------------------------------------------------------------------
    // Class instantiator
    // -------------------------------------------------------------------------

    private static final <T> T newInstance( Class<T> clazz )
    {
        try
        {
            Constructor<T> constructor = clazz.getConstructor( new Class[] {} );
            return constructor.newInstance( new Object[] {} );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Failed to instantiate " + clazz.getName(), e );
        }
    }

    // -------------------------------------------------------------------------
    // HashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public final boolean equals( Object o )
    {
        return this == o;
    }

    @Override
    public final int hashCode()
    {
        if ( id == null )
        {
            return 0;
        }

        return id.hashCode();
    }

    // -------------------------------------------------------------------------
    // State class
    // -------------------------------------------------------------------------

    /**
     * Default state class. Can be extended if more state information is
     * desired.
     * 
     * @author Torgeir Lorange Ostby
     * @version $Id: InternalProcess.java 4399 2008-01-14 14:20:59Z torgeilo $
     */
    public static class State
    {
        public static final String INTERRUPTED = "interrupted";

        public static final String EXCEPTION = "exception";

        public static final String SUCCESS = "success";

        public static final String ERROR = "error";

        public static final String UNKNOWN = "unknown";

        // ---------------------------------------------------------------------
        // State
        // ---------------------------------------------------------------------

        private boolean interrupted;

        private boolean started;

        private String result;

        private Long startTime;

        private Long endTime;

        // ---------------------------------------------------------------------
        // Getters
        // ---------------------------------------------------------------------

        /**
         * Returns true if the process was started.
         */
        public final boolean isStarted()
        {
            return started;
        }

        /**
         * Returns true if the process was interrupted.
         */
        public final boolean isInterrupted()
        {
            return interrupted;
        }

        /**
         * Returns true if the process has finished. A process which is never
         * started is never finished.
         */
        public final boolean isFinished()
        {
            return endTime != null;
        }

        /**
         * Returns the result. Returns null if the process has not finished.
         */
        public final String getResult()
        {
            return result;
        }

        /**
         * Returns the start time, or null if the process has not started.
         */
        public final Date getStartTime()
        {
            if ( startTime == null )
            {
                return null;
            }

            return new Date( startTime );
        }

        /**
         * Returns the end time, or null if the process has not ended.
         */
        public final Date getEndTime()
        {
            if ( endTime == null )
            {
                return null;
            }

            return new Date( endTime );
        }

        /**
         * Returns the amount of milliseconds the process has been running. If
         * the process is not started zero is returned.
         */
        public final long getRunningTimeMillis()
        {
            if ( startTime == null )
            {
                return 0;
            }

            if ( endTime == null )
            {
                return System.currentTimeMillis() - startTime;
            }

            return endTime - startTime;
        }
    }
}
