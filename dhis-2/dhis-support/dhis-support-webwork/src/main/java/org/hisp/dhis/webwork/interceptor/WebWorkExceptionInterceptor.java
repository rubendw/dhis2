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

import java.util.HashMap;
import java.util.Map;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.InsufficientAuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: WebWorkExceptionInterceptor.java 3071 2007-03-15 13:22:19Z torgeilo $
 */
public class WebWorkExceptionInterceptor
    implements Interceptor
{
    private static final Log LOG = LogFactory.getLog( WebWorkExceptionInterceptor.class );

    public static final String EXCEPTION_RESULT_KEY = "onExceptionReturn";

    public static final String EXCEPTION_RESULT_DEFAULT = "exceptionDefault";

    public static final String EXCEPTION_RESULT_PLAIN_TEXT = "plainTextErrorResult";

    public static final String EXCEPTION_RESULT_ACCESS_DENIED = "accessDenied";

    public static final String TEMPLATE_KEY_EXCEPTION = "exception";

    public static final String TEMPLATE_KEY_SHOW_STACK_TRACE = "showStackTrace";

    // -------------------------------------------------------------------------
    // Show stack trace parameter. Defaults to true
    // -------------------------------------------------------------------------

    private boolean showStackTrace = true;

    public void setShowStackTrace( boolean showStackTrace )
    {
        this.showStackTrace = showStackTrace;
    }

    // -------------------------------------------------------------------------
    // Interface implementation
    // -------------------------------------------------------------------------

    public void destroy()
    {
    }

    public void init()
    {
    }

    public String intercept( ActionInvocation actionInvocation )
    {
        try
        {
            return actionInvocation.invoke();
        }
        catch ( Exception e )
        {
            // -----------------------------------------------------------------
            // Save exception etc. to value stack
            // -----------------------------------------------------------------

            Map<String, Object> parameterMap = new HashMap<String, Object>( 3 );
            parameterMap.put( TEMPLATE_KEY_EXCEPTION, e );
            parameterMap.put( TEMPLATE_KEY_SHOW_STACK_TRACE, showStackTrace );
            actionInvocation.getStack().push( parameterMap );

            // -----------------------------------------------------------------
            // Find and return result name
            // -----------------------------------------------------------------

            Map params = actionInvocation.getProxy().getConfig().getParams();
            String exceptionResultName = (String) params.get( EXCEPTION_RESULT_KEY );

            if ( e instanceof AccessDeniedException || e instanceof InsufficientAuthenticationException )
            {
                if ( EXCEPTION_RESULT_PLAIN_TEXT.equals( exceptionResultName ) )
                {
                    // Access denied as plain text
                    return EXCEPTION_RESULT_PLAIN_TEXT;
                }

                // Access denied as nice page
                return EXCEPTION_RESULT_ACCESS_DENIED;
            }

            LOG.error( "Error while executing action", e );

            if ( exceptionResultName != null )
            {
                return exceptionResultName;
            }
            else
            {
                return EXCEPTION_RESULT_DEFAULT;
            }
        }
    }
}
