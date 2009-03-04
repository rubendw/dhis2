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

import java.util.Collection;
import java.util.HashSet;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.hisp.dhis.security.authority.SystemAuthoritiesProvider;
import org.hisp.dhis.user.UserStore;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AbstractAutomaticAccessProvider.java 3516 2007-08-05 11:45:54Z torgeilo $
 */
public abstract class AbstractAutomaticAccessProvider
    implements AutomaticAccessProvider
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

    private SystemAuthoritiesProvider systemAuthoritiesProvider;

    public void setSystemAuthoritiesProvider( SystemAuthoritiesProvider systemAuthoritiesProvider )
    {
        this.systemAuthoritiesProvider = systemAuthoritiesProvider;
    }

    // -------------------------------------------------------------------------
    // Overrides
    // -------------------------------------------------------------------------

    protected abstract void initialise();

    public final void init()
    {
        if ( isEnabled() )
        {
            initialise();
        }
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    protected boolean isEnabled()
    {
        return userStore.getAllUsers().size() == 0;
    }

    protected Collection<String> getAuthorities()
    {
        //return systemAuthoritiesProvider.getSystemAuthorities();

        HashSet<String> authorities = new HashSet<String>( 1 );
        authorities.add( "ALL" );
        return authorities;
    }

    protected GrantedAuthority[] getGrantedAuthorities()
    {
        Collection<String> systemAuthorities = getAuthorities();

        GrantedAuthority[] grantedAuthorities = new GrantedAuthority[systemAuthorities.size()];

        int i = 0;

        for ( String authority : systemAuthorities )
        {
            grantedAuthorities[i++] = new GrantedAuthorityImpl( authority );
        }

        return grantedAuthorities;
    }
}
