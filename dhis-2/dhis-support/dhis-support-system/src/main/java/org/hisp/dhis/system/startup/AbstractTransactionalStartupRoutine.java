package org.hisp.dhis.system.startup;

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

import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * Superclass which wraps the execute method in a transaction, and closes the
 * Hibernate session after the StartupRoutine is finished. This class should be
 * used by all StartupRoutines which need transactions. It can be configured to
 * not start a transaction is more fine grained controll is needed by calling
 * <code>setStartTransaction( false )</code>.
 * 
 * @author <a href="mailto:torgeilo@gmail.com">Torgeir Lorange Ostby</a>
 * @version $Id: AbstractTransactionalStartupRoutine.java 3217 2007-04-02 08:54:21Z torgeilo $
 */
public abstract class AbstractTransactionalStartupRoutine
    extends AbstractStartupRoutine
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected TransactionManager transactionManager;

    public final void setTransactionManager( TransactionManager transactionManager )
    {
        this.transactionManager = transactionManager;
    }

    private HibernateSessionManager sessionManager;

    public final void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private boolean startTransaction = true;

    public void setStartTransaction( boolean startTransaction )
    {
        this.startTransaction = startTransaction;
    }

    // -------------------------------------------------------------------------
    // Redirect method
    // -------------------------------------------------------------------------

    protected abstract void executeRoutine()
        throws Exception;

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public final void execute()
        throws Exception
    {
        try
        {
            if ( startTransaction )
            {
                transactionManager.enter();

                try
                {
                    executeRoutine();

                    transactionManager.leave();
                }
                catch ( Exception e )
                {
                    transactionManager.abort();

                    throw e;
                }
            }
            else
            {
                executeRoutine();
            }
        }
        finally
        {
            sessionManager.closeCurrentSession();
        }
    }
}
