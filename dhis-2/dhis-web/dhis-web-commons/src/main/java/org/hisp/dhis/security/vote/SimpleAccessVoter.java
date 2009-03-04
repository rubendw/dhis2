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

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple AccessDecisionVoter which grants access if a specified required
 * authority is among the configAttributes.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SimpleAccessVoter.java 3526 2007-08-20 12:05:56Z torgeilo $
 */
public class SimpleAccessVoter
    implements AccessDecisionVoter
{
    private static final Log LOG = LogFactory.getLog( SimpleAccessVoter.class );

    private String requiredAuthority;

    public void setRequiredAuthority( String requiredAuthority )
    {
        this.requiredAuthority = requiredAuthority;
    }

    // -------------------------------------------------------------------------
    // Interface implementation
    // -------------------------------------------------------------------------

    public boolean supports( ConfigAttribute configAttribute )
    {
        return configAttribute != null && configAttribute.getAttribute() != null
            && configAttribute.getAttribute().equals( requiredAuthority );
    }

    public boolean supports( Class clazz )
    {
        return true;
    }

    public int vote( Authentication authentication, Object object, ConfigAttributeDefinition definition )
    {
        for ( GrantedAuthority authority : authentication.getAuthorities() )
        {
            if ( authority.getAuthority().equals( requiredAuthority ) )
            {
                LOG.debug( "ACCESS GRANTED [" + object.toString() + "]" );

                return ACCESS_GRANTED;
            }
        }

        LOG.debug( "ACCESS DENIED [" + object.toString() + "]" );

        return ACCESS_DENIED;
    }
}
