package org.hisp.dhis.webportal.interceptor;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: WebWorkPortalParamsInterceptor.java 4559 2008-02-09 18:47:24Z torgeilo $
 */
public class WebWorkPortalParamsInterceptor
    extends AroundInterceptor
{
    // -------------------------------------------------------------------------
    // External configuration
    // -------------------------------------------------------------------------

    private Set<String> standardParams = new HashSet<String>();

    public void setStandardParams( Set<String> standardParams )
    {
        this.standardParams = standardParams;
    }

    private Set<String> commaSeparatedParams = new HashSet<String>();

    public void setCommaSeparatedParams( Set<String> commaSeparatedParams )
    {
        this.commaSeparatedParams = commaSeparatedParams;
    }

    // -------------------------------------------------------------------------
    // AroundInterceptor implementation
    // -------------------------------------------------------------------------

    @Override
    protected void after( ActionInvocation actionInvocation, String result )
        throws Exception
    {
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected void before( ActionInvocation actionInvocation )
        throws Exception
    {
        ActionConfig actionConfig = actionInvocation.getProxy().getConfig();

        final Map<String, String> staticParams = actionConfig.getParams();

        if ( staticParams == null )
        {
            return;
        }

        // ---------------------------------------------------------------------
        // Push the specified static parameters onto the value stack
        // ---------------------------------------------------------------------

        Map<String, Object> matches = new HashMap<String, Object>();

        for ( Map.Entry<String, String> entry : staticParams.entrySet() )
        {
            if ( standardParams.contains( entry.getKey() ) )
            {
                matches.put( entry.getKey(), entry.getValue() );
            }
            else if ( commaSeparatedParams.contains( entry.getKey() ) )
            {
                String[] values = entry.getValue().split( "," );

                for ( int i = 0; i < values.length; i++ )
                {
                    values[i] = values[i].trim();
                }
                
                matches.put( entry.getKey(), values );
            }
        }

        actionInvocation.getStack().push( matches );
    }
}
