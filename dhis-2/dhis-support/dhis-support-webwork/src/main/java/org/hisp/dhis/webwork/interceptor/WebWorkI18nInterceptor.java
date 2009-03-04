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

import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.i18n.I18nManager;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

/**
 * @author Nguyen Dang Quang
 * @version $Id: WebWorkI18nInterceptor.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class WebWorkI18nInterceptor
    extends AroundInterceptor
{
    private static final String KEY_I18N = "i18n";

    private static final String KEY_I18N_FORMAT = "format";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nManager i18nManager;

    public void setI18nManager( I18nManager manager )
    {
        i18nManager = manager;
    }

    // -------------------------------------------------------------------------
    // AroundInterceptor implementation
    // -------------------------------------------------------------------------

    protected void after( ActionInvocation invocation, String result )
        throws Exception
    {
    }

    protected void before( ActionInvocation invocation )
        throws Exception
    {
        Action action = (Action) invocation.getAction();

        I18n i18n = i18nManager.getI18n( action.getClass() );
        I18nFormat i18nFormat = i18nManager.getI18nFormat();

        // ---------------------------------------------------------------------
        // Make the objects available for web templates
        // ---------------------------------------------------------------------

        Map<String, Object> i18nMap = new HashMap<String, Object>( 2 );
        i18nMap.put( KEY_I18N, i18n );
        i18nMap.put( KEY_I18N_FORMAT, i18nFormat );

        invocation.getStack().push( i18nMap );

        // ---------------------------------------------------------------------
        // Set the objects in the action class if the properties exist
        // ---------------------------------------------------------------------

        Map contextMap = invocation.getInvocationContext().getContextMap();

        try
        {
            Ognl.setValue( KEY_I18N, contextMap, action, i18n );
        }
        catch ( NoSuchPropertyException e )
        {
        }

        try
        {
            Ognl.setValue( KEY_I18N_FORMAT, contextMap, action, i18nFormat );
        }
        catch ( NoSuchPropertyException e )
        {
        }
    }
}
