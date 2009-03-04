package org.hisp.dhis.user;

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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nguyen Hong Duc
 * @version $Id: UserCredentials.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class UserCredentials
    implements Serializable
{
    private int id;

    /**
     * Required and unique.
     */
    private User user;

    /**
     * Required and unique.
     */
    private String username;

    /**
     * Required.
     */
    private String password;

    private Set<UserAuthorityGroup> userAuthorityGroups = new HashSet<UserAuthorityGroup>();

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return username.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof UserCredentials) )
        {
            return false;
        }

        final UserCredentials other = (UserCredentials) o;

        return username.equals( other.getUsername() );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public Set<UserAuthorityGroup> getUserAuthorityGroups()
    {
        return userAuthorityGroups;
    }

    public void setUserAuthorityGroups( Set<UserAuthorityGroup> userAuthorityGroups )
    {
        this.userAuthorityGroups = userAuthorityGroups;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }
}
