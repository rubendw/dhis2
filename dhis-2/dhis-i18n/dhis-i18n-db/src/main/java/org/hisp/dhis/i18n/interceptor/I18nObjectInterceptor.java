package org.hisp.dhis.i18n.interceptor;

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

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hisp.dhis.i18n.I18nService;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: I18nObjectInterceptor.java 4093 2007-11-25 19:26:37Z torgeilo $
 */
public class I18nObjectInterceptor
    implements MethodInterceptor
{
    private static final String ADD = "add";

    private static final String UPDATE = "update";

    private static final String SAVE = "save";

    private static final String DELETE = "delete";

    private static final String GET_ID_METHOD = "getId";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }

    // -------------------------------------------------------------------------
    // MethodInterceptor implementation
    // -------------------------------------------------------------------------

    public Object invoke( MethodInvocation methodInvocation )
        throws Throwable
    {
        String methodName = methodInvocation.getMethod().getName();

        Object object = null;

        if ( methodInvocation.getArguments().length == 1 )
        {
            object = methodInvocation.getArguments()[0];
        }

        if ( object != null )
        {
            if ( methodName.startsWith( ADD ) )
            {
                Object returnValue = methodInvocation.proceed();

                i18nService.addObject( object );

                return returnValue;
            }
            else if ( methodName.startsWith( UPDATE ) )
            {
                i18nService.verify( object );

                return methodInvocation.proceed();
            }
            else if ( methodName.startsWith( DELETE ) )
            {
                Object returnValue = methodInvocation.proceed();

                i18nService.removeObject( object );

                return returnValue;
            }
            else if ( methodName.startsWith( SAVE ) )
            {
                Method getIdMethod = object.getClass().getMethod( GET_ID_METHOD, new Class[0] );

                int id = (Integer) getIdMethod.invoke( object, new Object[0] );

                if ( id == 0 )
                {
                    Object returnValue = methodInvocation.proceed();

                    i18nService.addObject( object );

                    return returnValue;
                }
                else
                {
                    i18nService.verify( object );

                    return methodInvocation.proceed();
                }
            }
        }

        return methodInvocation.proceed();
    }
}
