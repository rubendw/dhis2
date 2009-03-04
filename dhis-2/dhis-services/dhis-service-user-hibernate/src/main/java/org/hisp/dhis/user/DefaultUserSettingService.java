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
import java.util.Collection;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultUserSettingService.java 5724 2008-09-18 14:37:01Z larshelg $
 */
public class DefaultUserSettingService
    implements UserSettingService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

    // -------------------------------------------------------------------------
    // UserSettingService implementation
    // -------------------------------------------------------------------------

    public void saveUserSetting( String name, Serializable value )
        throws NoCurrentUserException
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            throw new NoCurrentUserException( "No current user. User setting not saved: " + name );
        }

        UserSetting userSetting = userStore.getUserSetting( currentUser, name );

        if ( userSetting == null )
        {
            userSetting = new UserSetting();
            userSetting.setUser( currentUser );
            userSetting.setName( name );
            userSetting.setValue( value );

            userStore.addUserSetting( userSetting );
        }
        else
        {
            userSetting.setValue( value );

            userStore.updateUserSetting( userSetting );
        }
    }

    public Serializable getUserSetting( String name )
        throws NoCurrentUserException
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            throw new NoCurrentUserException( "No current user. User setting not returned: " + name );
        }

        UserSetting userSetting = userStore.getUserSetting( currentUser, name );

        if ( userSetting != null )
        {
            return userSetting.getValue();
        }

        return null;
    }

    public Serializable getUserSetting( String name, Serializable defaultValue )
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            return defaultValue;
        }

        UserSetting userSetting = userStore.getUserSetting( currentUser, name );

        if ( userSetting != null )
        {
            return userSetting.getValue();
        }

        return defaultValue;
    }

    public Collection<UserSetting> getAllUserSettings()
        throws NoCurrentUserException
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            throw new NoCurrentUserException( "No current user. User settings not returned" );
        }

        return userStore.getAllUserSettings( currentUser );
    }

    public void deleteUserSetting( String name )
        throws NoCurrentUserException
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            throw new NoCurrentUserException( "No current user. User setting not deleted: " + name );
        }

        userStore.deleteUserSetting( userStore.getUserSetting( currentUser, name ) );
    }
}
