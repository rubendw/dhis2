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

/**
 * Coordinates the execution of {@link InternalProcess}es.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: InternalProcessCoordinator.java 4372 2007-12-23 00:37:47Z torgeilo $
 */
public interface InternalProcessCoordinator
{
    String ID = InternalProcessCoordinator.class.getName();

    /**
     * Loads an instance of a new process specified by the given type. The
     * process should then be prepared as necessary by the client and requested
     * for execution.
     * 
     * @param type The process type.
     * @param owner The process owner.
     */
    <T extends InternalProcess> T newProcess( String type, String owner );

    /**
     * Adds a prepared process to the execution queue and executes it in turn.
     * 
     * @return a unique process ID.
     */
    String requestProcessExecution( InternalProcess internalProcess );

    /**
     * Removes the process from the execution queue if it has not been started
     * yet, or tries to interrupt it if it is running. If a process aborts on an
     * interrupt depends on the process implementation.
     */
    void interruptOrCancelProcess( String id );

    /**
     * Removes an ended process from the set of ended processes. Should be
     * called by the client when the process has ended. Ended means finished or
     * interrupted before started.
     */
    void removeEndedProcess( String id );

    /**
     * Returns the process with the given ID, or null if it is not found.
     */
    InternalProcess getProcess( String id );

    /**
     * Returns all processes, finished or not, in no particular order.
     */
    Collection<InternalProcess> getAllProcesses();
}
