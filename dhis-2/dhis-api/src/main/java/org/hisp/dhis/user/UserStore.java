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

import java.util.Collection;

import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Nguyen Hong Duc
 * @version $Id: UserStore.java 5724 2008-09-18 14:37:01Z larshelg $
 */
public interface UserStore
{
    String ID = UserStore.class.getName();

    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    int addUser( User user );

    void updateUser( User user );

    User getUser( int id );

    Collection<User> getAllUsers();

    Collection<User> getUsersByOrganisationUnit( OrganisationUnit organisationUnit );

    Collection<User> getUsersWithoutOrganisationUnit();

    void deleteUser( User user );

    // -------------------------------------------------------------------------
    // UserCredentials
    // -------------------------------------------------------------------------

    User addUserCredentials( UserCredentials userCredentials );

    void updateUserCredentials( UserCredentials userCredentials );

    UserCredentials getUserCredentials( User user );

    UserCredentials getUserCredentialsByUsername( String username );
    
    Collection<UserCredentials> getAllUserCredentials();

    void deleteUserCredentials( UserCredentials userCredentials );

    // -------------------------------------------------------------------------
    // UserAuthorityGroup
    // -------------------------------------------------------------------------

    int addUserAuthorityGroup( UserAuthorityGroup userAuthorityGroup );

    void updateUserAuthorityGroup( UserAuthorityGroup userAuthorityGroup );

    UserAuthorityGroup getUserAuthorityGroup( int id );
    
    UserAuthorityGroup getUserAuthorityGroupByName( String name );

    Collection<UserAuthorityGroup> getAllUserAuthorityGroups();

    void deleteUserAuthorityGroup( UserAuthorityGroup userAuthorityGroup );

    // -------------------------------------------------------------------------
    // UserSettings
    // -------------------------------------------------------------------------

    void addUserSetting( UserSetting userSetting );

    void updateUserSetting( UserSetting userSetting );

    UserSetting getUserSetting( User user, String name );

    Collection<UserSetting> getAllUserSettings( User user );

    void deleteUserSetting( UserSetting userSetting );
}
