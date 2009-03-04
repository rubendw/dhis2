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

import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * Abstract {@link InternalProcess} which wrappes the process execution in a
 * transaction and finally closes the session. If manual transaction management
 * is required, use the {@link #setWrapInTransaction(boolean)} method with
 * argument {@code false} to avoid a transaction from being opened. Note that
 * the session is closed after the {@link #doExecute()} method has returned, not
 * after {@link #postNormalExecution()} or {@link #postExecution()}.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: AbstractTransactionalInternalProcess.java 4371 2007-12-22 23:52:13Z torgeilo $
 */
public abstract class AbstractTransactionalInternalProcess
    extends InternalProcess
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected HibernateSessionManager sessionManager;

    public final void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    protected TransactionManager transactionManager;

    public final void setTransactionManager( TransactionManager transactionManager )
    {
        this.transactionManager = transactionManager;
    }

    private boolean wrapInTransaction = true;

    /**
     * Default is {@link true}.
     */
    public void setWrapInTransaction( boolean wrapInTransaction )
    {
        this.wrapInTransaction = wrapInTransaction;
    }

    // -------------------------------------------------------------------------
    // InternalProcess
    // -------------------------------------------------------------------------

    /**
     * Delegates process execution to {@link #doExecute()}.
     */
    protected final String execute()
        throws Exception
    {
        String result;

        try
        {
            if ( wrapInTransaction )
            {
                transactionManager.enter();

                try
                {
                    result = doExecute();

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
                result = doExecute();
            }
        }
        finally
        {
            sessionManager.closeCurrentSession();
        }

        return result;
    }

    /**
     * The process method which must be implemented by subclasses.
     */
    protected abstract String doExecute()
        throws Exception;
}
