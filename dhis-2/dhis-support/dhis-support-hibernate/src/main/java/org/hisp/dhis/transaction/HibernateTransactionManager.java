package org.hisp.dhis.transaction;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hisp.dhis.hibernate.HibernateSessionManager;

/**
 * @author Trygve Laugst&oslash;l
 * @author Torgeir Lorange Ostby
 * @version $Id: HibernateTransactionManager.java 3647 2007-10-15 20:40:00Z torgeilo $
 */
public class HibernateTransactionManager
    implements TransactionManager
{
    private static final Log LOG = LogFactory.getLog( HibernateTransactionManager.class );

    private static final FlushMode FLUSH_MODE_READ_ONLY = FlushMode.MANUAL;

    private static final FlushMode FLUSH_MODE_READ_WRITE = FlushMode.COMMIT;

    private ThreadLocal<State> statePeg = new ThreadLocal<State>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private boolean closeSessionOnCommit = true;

    /**
     * Sets if session should be closed on commit. Default is <code>true</true>.
     * Session is always closed on abort or any other transactional failure.
     */
    public void setCloseSessionOnCommit( boolean closeSessionOnCommit )
    {
        this.closeSessionOnCommit = closeSessionOnCommit;
    }

    // -------------------------------------------------------------------------
    // State class
    // -------------------------------------------------------------------------

    private static class State
    {
        private Transaction transaction;

        private int level;

        private boolean aborted;

        // ---------------------------------------------------------------------
        // Constructors
        // ---------------------------------------------------------------------

        public State( Transaction transaction )
        {
            this.transaction = transaction;
        }

        // ---------------------------------------------------------------------
        // Level methods
        // ---------------------------------------------------------------------

        public void levelUp()
        {
            ++level;
        }

        public void levelDown()
        {
            --level;
        }

        // ---------------------------------------------------------------------
        // Abort methods
        // ---------------------------------------------------------------------

        public void setAborted()
        {
            aborted = true;
        }

        // ---------------------------------------------------------------------
        // Getters
        // ---------------------------------------------------------------------

        public Transaction getTransaction()
        {
            return transaction;
        }

        public int getLevel()
        {
            return level;
        }

        public boolean isAborted()
        {
            return aborted;
        }
    }

    // -------------------------------------------------------------------------
    // TransactionManager implementation
    // -------------------------------------------------------------------------

    public void enter()
    {
        enter( TransactionType.READ_ONLY );
    }

    public void enter( TransactionType transactionType )
    {
        if ( transactionType == null )
        {
            throw new IllegalArgumentException( "TransactionType must be specified, got null" );
        }

        State state = statePeg.get();

        Session session = sessionManager.getCurrentSession();

        if ( state == null )
        {
            if ( TransactionType.READ_WRITE.equals( transactionType ) )
            {
                session.setFlushMode( FLUSH_MODE_READ_WRITE );
            }
            else
            {
                session.setFlushMode( FLUSH_MODE_READ_ONLY );
            }

            Transaction transaction;

            try
            {
                transaction = session.beginTransaction();
            }
            catch ( RuntimeException e )
            {
                sessionManager.closeCurrentSession();

                throw e;
            }

            state = new State( transaction );

            state.levelUp();

            statePeg.set( state );
        }
        else
        {
            if ( state.isAborted() )
            {
                throw new IllegalStateException( "Cannot enter an aborted transaction" );
            }

            state.levelUp();

            if ( TransactionType.READ_WRITE.equals( transactionType )
                && session.getFlushMode().equals( FLUSH_MODE_READ_ONLY ) )
            {
                session.setFlushMode( FLUSH_MODE_READ_WRITE );
            }
        }

        LOG.debug( "enter( " + transactionType + " ) [entered level " + state.getLevel() + "]" );
    }

    public void leave()
    {
        State state = statePeg.get();

        if ( state == null )
        {
            throw new IllegalStateException( "Not inside a transaction" );
        }

        LOG.debug( "leave() [leaving level " + state.getLevel() + "]" );

        state.levelDown();

        if ( state.getLevel() == 0 )
        {
            statePeg.remove();

            if ( !state.isAborted() )
            {
                Transaction transaction = state.getTransaction();

                try
                {
                    transaction.commit();

                    if ( closeSessionOnCommit )
                    {
                        sessionManager.closeCurrentSession();
                    }
                }
                catch ( RuntimeException e )
                {
                    transaction.rollback();

                    sessionManager.closeCurrentSession();

                    throw e;
                }
            }
        }
    }

    public void abort()
    {
        State state = statePeg.get();

        if ( state == null )
        {
            throw new IllegalStateException( "Not inside a transaction" );
        }

        LOG.debug( "abort() [aborting at level " + state.getLevel() + "]" );

        state.levelDown();

        if ( !state.isAborted() )
        {
            state.setAborted();

            Transaction transaction = state.getTransaction();

            transaction.rollback();

            sessionManager.closeCurrentSession();
        }

        if ( state.getLevel() == 0 )
        {
            statePeg.remove();
        }
    }
}
