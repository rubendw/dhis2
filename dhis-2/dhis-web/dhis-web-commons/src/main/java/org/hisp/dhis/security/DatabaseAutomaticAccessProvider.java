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

import java.util.HashSet;

import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserCredentials;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DatabaseAutomaticAccessProvider.java 3513 2007-08-04 16:16:40Z torgeilo $
 */
public class DatabaseAutomaticAccessProvider
    extends AbstractAutomaticAccessProvider
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PasswordManager passwordManager;

    public void setPasswordManager( PasswordManager passwordManager )
    {
        this.passwordManager = passwordManager;
    }

    // -------------------------------------------------------------------------
    // AdminAccessManager implementation
    // -------------------------------------------------------------------------

    public void initialise()
    {
        // ---------------------------------------------------------------------
        // Assumes no UserAuthorityGroup called "Superuser" in database
        // ---------------------------------------------------------------------

        String username = "admin";
        String password = "district";

        User user = new User();
        user.setFirstName( username );
        user.setSurname( username );

        userStore.addUser( user );

        UserAuthorityGroup userAuthorityGroup = new UserAuthorityGroup();
        userAuthorityGroup.setName( "Superuser" );
        userAuthorityGroup.setAuthorities( new HashSet<String>( getAuthorities() ) );

        userStore.addUserAuthorityGroup( userAuthorityGroup );

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername( username );
        userCredentials.setPassword( passwordManager.encodePassword( username, password ) );
        userCredentials.setUser( user );
        userCredentials.getUserAuthorityGroups().add( userAuthorityGroup );

        userStore.addUserCredentials( userCredentials );
    }

    public void access()
    {
    }
}
