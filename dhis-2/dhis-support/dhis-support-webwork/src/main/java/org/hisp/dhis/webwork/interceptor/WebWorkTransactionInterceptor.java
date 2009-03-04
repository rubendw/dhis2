package org.hisp.dhis.webwork.interceptor;

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
import org.hisp.dhis.transaction.TransactionManager;
import org.hisp.dhis.transaction.TransactionType;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * @author Kristian Nordal
 * @version $Id: WebWorkTransactionInterceptor.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class WebWorkTransactionInterceptor
    implements Interceptor
{
    private static final Log LOG = LogFactory.getLog( WebWorkTransactionInterceptor.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private TransactionManager transactionManager;

    public void setTransactionManager( TransactionManager transactionManager )
    {
        this.transactionManager = transactionManager;
    }

    // -------------------------------------------------------------------------
    // Interceptor implementation
    // -------------------------------------------------------------------------

    public void destroy()
    {
    }

    public void init()
    {
    }

    public String intercept( ActionInvocation actionInvocation )
        throws Exception
    {
        String result;

        transactionManager.enter( TransactionType.READ_ONLY );

        try
        {
            result = actionInvocation.invoke();
        }
        catch ( Exception e )
        {
            try
            {
                transactionManager.abort();
            }
            catch ( Exception ee )
            {
                LOG.error( "Error while aborting transaction", ee );
            }

            throw e;
        }

        transactionManager.leave();

        return result;
    }
}
