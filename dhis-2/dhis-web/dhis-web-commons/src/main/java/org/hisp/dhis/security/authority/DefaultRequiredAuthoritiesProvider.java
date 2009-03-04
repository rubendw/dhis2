package org.hisp.dhis.security.authority;

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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.hisp.dhis.security.intercept.SingleObjectDefinitionSource;

import com.opensymphony.xwork.config.entities.ActionConfig;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultRequiredAuthoritiesProvider.java 3160 2007-03-24 20:15:06Z torgeilo $
 */
public class DefaultRequiredAuthoritiesProvider
    implements RequiredAuthoritiesProvider
{
    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private String requiredAuthoritiesKey;

    public void setRequiredAuthoritiesKey( String requiredAuthoritiesKey )
    {
        this.requiredAuthoritiesKey = requiredAuthoritiesKey;
    }

    private Set<String> globalAttributes = Collections.emptySet();

    public void setGlobalAttributes( Set<String> globalAttributes )
    {
        this.globalAttributes = globalAttributes;
    }

    // -------------------------------------------------------------------------
    // RequiredAuthoritiesProvider implementation
    // -------------------------------------------------------------------------

    public ObjectDefinitionSource createObjectDefinitionSource( ActionConfig actionConfig )
    {
        return createObjectDefinitionSource( actionConfig, actionConfig );
    }

    public ObjectDefinitionSource createObjectDefinitionSource( ActionConfig actionConfig, Object object )
    {
        Collection<String> requiredAuthorities = getRequiredAuthorities( actionConfig );

        ConfigAttributeDefinition attributes = new ConfigAttributeDefinition();

        for ( String requiredAuthority : requiredAuthorities )
        {
            attributes.addConfigAttribute( new SecurityConfig( requiredAuthority ) );
        }

        for ( String globalAttribute : globalAttributes )
        {
            attributes.addConfigAttribute( new SecurityConfig( globalAttribute ) );
        }

        return new SingleObjectDefinitionSource( object, attributes );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<String> getRequiredAuthorities( ActionConfig actionConfig )
    {
        final Map<String, String> staticParams = actionConfig.getParams();

        if ( staticParams == null || !staticParams.containsKey( requiredAuthoritiesKey ) )
        {
            return Collections.emptySet();
        }

        final String param = staticParams.get( requiredAuthoritiesKey );

        HashSet<String> requiredAuthorities = new HashSet<String>();

        StringTokenizer t = new StringTokenizer( param, "\t\n\r ," );

        while ( t.hasMoreTokens() )
        {
            requiredAuthorities.add( t.nextToken() );
        }

        return requiredAuthorities;
    }
}
