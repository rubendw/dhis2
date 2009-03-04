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

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.process.InternalProcessEvent;
import org.hisp.dhis.system.process.InternalProcessListener;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessEndWaitLock.java 4372 2007-12-23 00:37:47Z torgeilo $
 */
public final class ProcessEndWaitLock
    implements InternalProcessListener
{
    private final Log log = LogFactory.getLog( ProcessEndWaitLock.class );

    private static int runner = 0;

    private final int id;

    private TimerTask timerTask;

    private boolean notified;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProcessEndWaitLock()
    {
        id = ++runner;
    }

    // -------------------------------------------------------------------------
    // Synchronizers
    // -------------------------------------------------------------------------

    public synchronized void waitForProcessEnd( int timeout )
    {
        if ( notified )
        {
            log.info( "[n" + id + "] No waiting, process already ended" );

            return;
        }

        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                log.info( "[n" + id + "] Timeout!" );

                doNotify();
            }
        };

        (new Timer()).schedule( timerTask, timeout );

        log.info( "[n" + id + "] Timer started, waiting..." );

        try
        {
            wait();
        }
        catch ( InterruptedException e )
        {
            log.error( "[n" + id + "] Interrupted!", e );

            if ( timerTask != null )
            {
                timerTask.cancel();
                timerTask = null;
            }
        }
    }

    private synchronized void doNotify()
    {
        if ( timerTask != null )
        {
            timerTask.cancel();
            timerTask = null;
        }

        notify();
        notified = true;
    }

    // -------------------------------------------------------------------------
    // InternalProcessListener
    // -------------------------------------------------------------------------

    public void internalProcessFinished( InternalProcessEvent e )
    {
        log.info( "[n" + id + "] Process ended" );

        doNotify();
    }
}
