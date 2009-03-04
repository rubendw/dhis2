package org.hisp.dhis.system.process.queue;

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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Optionally thread-safe implementation of {@link ProcessQueue} based on a
 * {@link List}. All methods redirect to the list except the {@link #getHead()}
 * method. After any of the queue altering methods are called, the next time the
 * {@link #getHead()} method is called the head is recalculated and cached.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ListProcessQueue.java 4371 2007-12-22 23:52:13Z torgeilo $
 */
public final class ListProcessQueue<P extends ProcessQueueConstraints>
    implements ProcessQueue<P>
{
    private List<P> queue = new LinkedList<P>();

    private Set<P> head;

    // -------------------------------------------------------------------------
    // ProcessQueue
    // -------------------------------------------------------------------------

    public void add( P process )
    {
        queue.add( process );
        head = null;
    }

    public void remove( P process )
    {
        queue.remove( process );
        head = null;
    }

    public Collection<P> getHead()
    {
        if ( head == null )
        {
            head = findHead();
        }

        return head;
    }

    public Collection<P> getAll()
    {
        return queue;
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public int size()
    {
        return queue.size();
    }

    public void clear()
    {
        queue.clear();
        head = null;
    }

    // -------------------------------------------------------------------------
    // Support
    // -------------------------------------------------------------------------

    private Set<P> findHead()
    {
        HashSet<P> head = new HashSet<P>();
        HashSet<String> touchedGroups = new HashSet<String>();
        HashSet<String> lockedGroups = new HashSet<String>();

        for ( P process : queue )
        {
            if ( process.isSerialToAll() )
            {
                if ( head.isEmpty() )
                {
                    head.add( process );
                }

                break;
            }
            else if ( process.isSerialToGroup() )
            {
                if ( !touchedGroups.contains( process.getGroup() ) )
                {
                    head.add( process );
                }

                touchedGroups.add( process.getGroup() );
                lockedGroups.add( process.getGroup() );
            }
            else
            {
                if ( process.getGroup() == null || !lockedGroups.contains( process.getGroup() ) )
                {
                    head.add( process );

                    if ( process.getGroup() != null )
                    {
                        touchedGroups.add( process.getGroup() );
                    }
                }
            }
        }

        return head;
    }

    // -------------------------------------------------------------------------
    // Synchronized version
    // -------------------------------------------------------------------------

    /**
     * Returns a ListProcessQueue wrapped in a synchronizing proxy, making the
     * queue thread-safe.
     */
    @SuppressWarnings( "unchecked" )
    public static <P extends ProcessQueueConstraints> ProcessQueue<P> newSynchronizedInstance()
    {
        return (ProcessQueue<P>) Proxy.newProxyInstance( ListProcessQueue.class.getClassLoader(),
            new Class[] { ProcessQueue.class }, new SynchronizedInvocationHandler( new ListProcessQueue() ) );
    }

    private static class SynchronizedInvocationHandler
        implements InvocationHandler
    {
        private final Object target;

        public SynchronizedInvocationHandler( Object target )
        {
            this.target = target;
        }

        public synchronized Object invoke( Object proxy, Method method, Object[] args )
            throws Throwable
        {
            return method.invoke( target, args );
        }
    }
}
