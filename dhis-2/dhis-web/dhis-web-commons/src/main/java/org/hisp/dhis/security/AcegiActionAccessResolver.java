package org.hisp.dhis.security;

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

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.InsufficientAuthenticationException;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.security.authority.RequiredAuthoritiesProvider;

import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.PackageConfig;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AcegiActionAccessResolver.java 3160 2007-03-24 20:15:06Z torgeilo $
 */
public class AcegiActionAccessResolver
    implements ActionAccessResolver
{
    private static final Log LOG = LogFactory.getLog( AcegiActionAccessResolver.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private RequiredAuthoritiesProvider requiredAuthoritiesProvider;

    public void setRequiredAuthoritiesProvider( RequiredAuthoritiesProvider requiredAuthoritiesProvider )
    {
        this.requiredAuthoritiesProvider = requiredAuthoritiesProvider;
    }

    private AccessDecisionManager accessDecisionManager;

    public void setAccessDecisionManager( AccessDecisionManager accessDecisionManager )
    {
        this.accessDecisionManager = accessDecisionManager;
    }

    // -------------------------------------------------------------------------
    // ActionAccessResolver implementation
    // -------------------------------------------------------------------------

    public boolean hasAccess( String module, String name )
    {
        // ---------------------------------------------------------------------
        // Get ObjectDefinitionSource
        // ---------------------------------------------------------------------

        Configuration config = ConfigurationManager.getConfiguration();

        PackageConfig packageConfig = config.getPackageConfig( module );

        if ( packageConfig == null )
        {
            throw new IllegalArgumentException( "Module doesn't exist: '" + module + "'" );
        }

        ActionConfig actionConfig = (ActionConfig) packageConfig.getActionConfigs().get( name );

        if ( actionConfig == null )
        {
            throw new IllegalArgumentException( "Module " + module + " doesn't have an action named: '" + name + "'" );
        }

        ObjectDefinitionSource objectDefinitionSource = requiredAuthoritiesProvider
            .createObjectDefinitionSource( actionConfig );

        // ---------------------------------------------------------------------
        // Test access
        // ---------------------------------------------------------------------

        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication authentication = securityContext.getAuthentication();

        try
        {
            if ( objectDefinitionSource.getAttributes( actionConfig ) != null )
            {
                if ( authentication == null || !authentication.isAuthenticated() )
                {
                    return false;
                }

                accessDecisionManager.decide( authentication, actionConfig, objectDefinitionSource
                    .getAttributes( actionConfig ) );
            }

            LOG.debug( "Access to [" + module + ", " + name + "]: TRUE" );

            return true;
        }
        catch ( AccessDeniedException e )
        {
            LOG.debug( "Access to [" + module + ", " + name + "]: FALSE (access denied)" );

            return false;
        }
        catch ( InsufficientAuthenticationException e )
        {
            LOG.debug( "Access to [" + module + ", " + name + "]: FALSE (insufficient authentication)" );

            return false;
        }
    }
}
