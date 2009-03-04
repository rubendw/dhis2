package org.hisp.dhis.security.vote;

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

import java.util.Collections;
import java.util.List;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.InsufficientAuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AccessDecisionManager which delegates to other AccessDecisionManagers in a
 * logical or fashion. Delegation is stopped at the first positive answer from
 * the delegates, where the order of execution is defined by the list of
 * AccessDecisionManagers. So if the first AccessDecisionManager grants access
 * for a specific target, no other AccessDecisionManager is questioned. 
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: LogicalOrAccessDecisionManager.java 3526 2007-08-20 12:05:56Z torgeilo $
 */
public class LogicalOrAccessDecisionManager
    implements AccessDecisionManager
{
    private static final Log LOG = LogFactory.getLog( LogicalOrAccessDecisionManager.class );

    private List<AccessDecisionManager> accessDecisionManagers = Collections.emptyList();

    public void setAccessDecisionManagers( List<AccessDecisionManager> accessDecisionManagers )
    {
        this.accessDecisionManagers = accessDecisionManagers;
    }

    // -------------------------------------------------------------------------
    // Interface implementation
    // -------------------------------------------------------------------------

    public void decide( Authentication authentication, Object object, ConfigAttributeDefinition definition )
        throws AccessDeniedException, InsufficientAuthenticationException
    {
        AccessDeniedException ade = null;
        InsufficientAuthenticationException iae = null;

        for ( AccessDecisionManager accessDecisionManager : accessDecisionManagers )
        {
            try
            {
                accessDecisionManager.decide( authentication, object, definition );

                LOG.debug( "ACCESS GRANTED [" + object.toString() + "]" );

                return;
            }
            catch ( AccessDeniedException e )
            {
                ade = e;
            }
            catch ( InsufficientAuthenticationException e )
            {
                iae = e;
            }
        }

        LOG.debug( "ACCESS DENIED [" + object.toString() + "]" );

        if ( ade != null )
        {
            throw ade;
        }

        if ( iae != null )
        {
            throw iae;
        }
    }

    public boolean supports( ConfigAttribute configAttribute )
    {
        for ( AccessDecisionManager accessDecisionManager : accessDecisionManagers )
        {
            if ( accessDecisionManager.supports( configAttribute ) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean supports( Class clazz )
    {
        for ( AccessDecisionManager accessDecisionManager : accessDecisionManagers )
        {
            if ( accessDecisionManager.supports( clazz ) )
            {
                return true;
            }
        }

        return false;
    }
}
