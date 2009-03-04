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
import java.util.Map;

import org.hisp.dhis.webportal.module.ModuleManager;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: WebWorkPortalModuleInterceptor.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class WebWorkPortalModuleInterceptor
    extends AroundInterceptor
{
    private static final String KEY_MODULES = "modules";

    private static final String KEY_CURRENT_MODULE = "currentModule";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ModuleManager moduleManager;

    public void setModuleManager( ModuleManager moduleManager )
    {
        this.moduleManager = moduleManager;
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
    protected void before( ActionInvocation actionInvocation )
        throws Exception
    {
        Map<String, Object> handle = new HashMap<String, Object>( 2 );

        handle.put( KEY_MODULES, moduleManager.getMenuModules() );
        handle.put( KEY_CURRENT_MODULE, moduleManager.getCurrentModule() );

        actionInvocation.getStack().push( handle );
    }
}
