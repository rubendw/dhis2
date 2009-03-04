package org.hisp.dhis.security.intercept;

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

import org.acegisecurity.intercept.AbstractSecurityInterceptor;
import org.acegisecurity.intercept.InterceptorStatusToken;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.hisp.dhis.security.ActionAccessResolver;
import org.hisp.dhis.security.authority.RequiredAuthoritiesProvider;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: WebWorkSecurityInterceptor.java 5797 2008-10-02 15:40:29Z larshelg $
 */
public class WebWorkSecurityInterceptor
    extends AbstractSecurityInterceptor
    implements Interceptor
{
    private static final String KEY_ACTION_ACCESS_RESOLVER = "auth";

    private ThreadLocal<ObjectDefinitionSource> definitionSourceTag = new ThreadLocal<ObjectDefinitionSource>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private RequiredAuthoritiesProvider requiredAuthoritiesProvider;

    public void setRequiredAuthoritiesProvider( RequiredAuthoritiesProvider requiredAuthoritiesProvider )
    {
        this.requiredAuthoritiesProvider = requiredAuthoritiesProvider;
    }

    private ActionAccessResolver actionAccessResolver;

    public void setActionAccessResolver( ActionAccessResolver actionAccessResolver )
    {
        this.actionAccessResolver = actionAccessResolver;
    }

    // -------------------------------------------------------------------------
    // WebWork Interceptor
    // -------------------------------------------------------------------------

    public void init()
    {
    }

    public void destroy()
    {
    }

    public String intercept( ActionInvocation invocation )
        throws Exception
    {
        Object result = null;
        ActionConfig actionConfig = invocation.getProxy().getConfig();
        definitionSourceTag.set( requiredAuthoritiesProvider.createObjectDefinitionSource( actionConfig ) );

        InterceptorStatusToken token = beforeInvocation( actionConfig );

        addActionAccessResolver( invocation );

        try
        {
            result = invocation.invoke();
        }
        finally
        {
            result = afterInvocation( token, result );

            definitionSourceTag.remove();
        }

        if ( result != null )
        {
            return result.toString();
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // Acegi Security Interceptor
    // -------------------------------------------------------------------------

    @Override
    public Class<?> getSecureObjectClass()
    {
        return ActionConfig.class;
    }

    @Override
    public ObjectDefinitionSource obtainObjectDefinitionSource()
    {
        ObjectDefinitionSource definitionSource = definitionSourceTag.get();

        if ( definitionSource != null )
        {
            return definitionSource;
        }

        // ---------------------------------------------------------------------
        // ObjectDefinitionSource required, but we are not inside an
        // invocation. Returning an empty dummy.
        // ---------------------------------------------------------------------

        return new SingleObjectDefinitionSource( new ActionConfig() );
    }

    // -------------------------------------------------------------------------
    // ActionAccessResolver
    // -------------------------------------------------------------------------

    private void addActionAccessResolver( ActionInvocation invocation )
    {
        Map<String, Object> accessResolverMap = new HashMap<String, Object>( 1 );
        accessResolverMap.put( KEY_ACTION_ACCESS_RESOLVER, actionAccessResolver );
        invocation.getStack().push( accessResolverMap );
    }
}
