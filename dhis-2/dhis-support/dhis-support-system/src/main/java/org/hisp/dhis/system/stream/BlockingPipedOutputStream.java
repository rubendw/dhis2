package org.hisp.dhis.system.stream;

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

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: BlockingPipedOutputStream.java 4597 2008-02-16 14:11:24Z torgeilo $
 */
public class BlockingPipedOutputStream
    extends PipedOutputStream
{
    private boolean connected = false;

    private boolean waiting = false;

    private boolean closed = false;

    // -------------------------------------------------------------------------
    // Overrides
    // -------------------------------------------------------------------------

    @Override
    public synchronized void connect( PipedInputStream snk )
        throws IOException
    {
        super.connect( snk );
        connected = true;
        notifyAll();
    }

    @Override
    public void write( byte[] b )
        throws IOException
    {
        if ( !connected )
        {
            block();

            if ( !connected )
            {
                return;
            }
        }

        super.write( b );
    }

    @Override
    public void write( int b )
        throws IOException
    {
        if ( !connected )
        {
            block();

            if ( !connected )
            {
                return;
            }
        }

        super.write( b );
    }

    @Override
    public void write( byte[] b, int off, int len )
        throws IOException
    {
        if ( !connected )
        {
            block();

            if ( !connected )
            {
                return;
            }
        }

        super.write( b, off, len );
    }

    @Override
    public void flush()
        throws IOException
    {
        if ( !connected )
        {
            block();

            if ( !connected )
            {
                return;
            }
        }

        super.flush();
    }

    @Override
    public synchronized void close()
        throws IOException
    {
        connected = false;
        closed = true;

        try
        {
            super.close();
        }
        finally
        {
            notifyAll();
        }
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public boolean isWaiting()
    {
        return waiting;
    }

    public boolean isClosed()
    {
        return closed;
    }

    // -------------------------------------------------------------------------
    // Lock
    // -------------------------------------------------------------------------

    private synchronized void block()
        throws IOException
    {
        if ( closed )
        {
            throw new IOException( "Output stream is closed" );
        }

        if ( waiting )
        {
            throw new IOException( "Someone else is waiting on this pipe" );
        }

        waiting = true;

        try
        {
            wait();
        }
        catch ( InterruptedException e )
        {
            throw new IOException( "Interrupted!" );
        }
        finally
        {
            waiting = false;
        }
    }
}
