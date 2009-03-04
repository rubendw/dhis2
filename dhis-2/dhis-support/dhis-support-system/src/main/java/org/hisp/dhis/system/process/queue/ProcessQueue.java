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

import java.util.Collection;

/**
 * Process queue. The process positions in the queue are collectively defined by
 * each process' {@link ProcessQueueConstraints}. The {@link #getHead()} method
 * is the interesting method. The others should be self-describing.
 * 
 * @see #getHead()
 * @see ProcessQueueConstraints
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessQueue.java 4363 2007-12-21 20:48:44Z torgeilo $
 */
public interface ProcessQueue<P extends ProcessQueueConstraints>
{
    void add( P process );

    void remove( P process );

    /**
     * Returns the head processes. The head processes are collectively defined
     * by each process' {@link ProcessQueueConstraints}. It returns an empty
     * collection if and only if {@link #isEmpty()} returns true. Head will only
     * change when one of the queue altering methods are called ({@link
     * #add(ProcessQueueConstraints)}, {@link #remove(ProcessQueueConstraints)},
     * and {@link #clear()}). Remove processes from the head to make new
     * processes advance in the queue.
     */
    Collection<P> getHead();

    /**
     * Returns all processes in no specific order.
     */
    Collection<P> getAll();

    int size();

    boolean isEmpty();

    void clear();
}
