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

import org.hisp.dhis.user.UserSetting;

/**
 * The main interface for working with user settings. Implementation need to get
 * the current user from {@link CurrentUserService}.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: UserSettingService.java 2869 2007-02-20 14:26:09Z andegje $
 */
public interface UserSettingService
{
    String ID = UserSettingService.class.getName();

    /**
     * Saves the name/value pair as a user setting connected to the currently
     * logged in user.
     * 
     * @param name the name/handle of the value.
     * @param value the value to store.
     * @throws NoCurrentUserException if there is no current user.
     */
    void saveUserSetting( String name, Serializable value )
        throws NoCurrentUserException;

    /**
     * Returns the value of the user setting specified by the given name.
     * 
     * @param name the name of the user setting.
     * @return the value corresponding to the named user setting, or null if
     *         there is no match.
     * @throws NoCurrentUserException if there is no current user.
     */
    Serializable getUserSetting( String name )
        throws NoCurrentUserException;

    /**
     * Returns the value of the user setting specified by the given name. If
     * there is no current user or the user setting doesn't exist, the specified
     * default value is returned.
     * 
     * @param name the name of the user setting.
     * @param defaultValue the value to return if there is no current user or no
     *        user setting correspoinding to the given name.
     * @return the value corresponding to the names user setting, or the default
     *         value if there is no current user or matching user setting.
     */
    Serializable getUserSetting( String name, Serializable defaultValue );

    /**
     * Returns all user settings belonging to the current user.
     * 
     * @return all user settings belonging to the current user.
     * @throws NoCurrentUserException if there is no current user.
     */
    Collection<UserSetting> getAllUserSettings()
        throws NoCurrentUserException;

    /**
     * Deletes the user setting with the given name.
     * 
     * @param name the name of the user setting to delete.
     * @throws NoCurrentUserException if there is no current user.
     */
    void deleteUserSetting( String name )
        throws NoCurrentUserException;
}
