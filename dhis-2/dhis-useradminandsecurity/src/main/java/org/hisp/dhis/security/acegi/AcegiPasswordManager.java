package org.hisp.dhis.security.acegi;

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
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.hisp.dhis.security.PasswordManager;
import org.hisp.dhis.security.UsernameSaltSource;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AcegiPasswordManager.java 3109 2007-03-19 17:05:21Z torgeilo $
 */
public class AcegiPasswordManager
    implements PasswordManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PasswordEncoder passwordEncoder;

    public void setPasswordEncoder( PasswordEncoder passwordEncoder )
    {
        this.passwordEncoder = passwordEncoder;
    }

    private AuthenticationManager authenticationManager;

    public void setAuthenticationManager( AuthenticationManager authenticationManager )
    {
        this.authenticationManager = authenticationManager;
    }

    private UsernameSaltSource usernameSaltSource;

    public void setUsernameSaltSource( UsernameSaltSource saltSource )
    {
        this.usernameSaltSource = saltSource;
    }

    // -------------------------------------------------------------------------
    // PasswordManager implementation
    // -------------------------------------------------------------------------

    public final String encodePassword( String username, String password )
    {
        return passwordEncoder.encodePassword( password, usernameSaltSource.getSalt( username ) );
    }

    public final boolean isPasswordValid( String username, String password )
    {
        UserDetails userDetails = new org.acegisecurity.userdetails.User( username, password, true, true, true, true,
            null );

        Authentication authentication = new UsernamePasswordAuthenticationToken( userDetails, userDetails.getPassword() );

        try
        {
            authentication = authenticationManager.authenticate( authentication );
        }
        catch ( AuthenticationException e )
        {
            return false;
        }

        return true;
    }
}
