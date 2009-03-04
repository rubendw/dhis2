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
import java.util.Date;
import java.util.HashSet;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Nguyen Hong Duc
 * @version $Id: UserStoreTest.java 5724 2008-09-18 14:37:01Z larshelg $
 */
public class UserStoreTest
    extends DhisSpringTest
{
    private UserStore userStore;

    private TransactionManager transactionManager;

    private OrganisationUnitService organisationUnitService;

    public void setUpTest()
        throws Exception
    {
        userStore = (UserStore) getBean( UserStore.ID );

        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
    }

    public void testBasicUser()
        throws Exception
    {
        OrganisationUnit unit1 = new OrganisationUnit( "name1", "shortName1", "organisationUnitCode1", new Date(),
            new Date(), true, "comment" );
        OrganisationUnit unit2 = new OrganisationUnit( "name2", "shortName2", "organisationUnitCode2", new Date(),
            new Date(), true, "comment" );
        
        Collection<OrganisationUnit> units1 = new HashSet<OrganisationUnit>();       
        
        units1.add(unit1);
        units1.add(unit2);

        organisationUnitService.addOrganisationUnit( unit1 );
        organisationUnitService.addOrganisationUnit( unit2 );
        
        String userName = "User";
        User user = new User();
        user.setSurname( userName );
        user.setFirstName( userName );

        // Test addUser
        int id = userStore.addUser( user );
        assertEquals( userStore.getUser( id ).getSurname(), userName );
        assertEquals( userStore.getUser( id ).getFirstName(), userName );
        assertEquals( 1, userStore.getAllUsers().size(), 1 );
        assertEquals( 0, userStore.getUsersByOrganisationUnit( unit1 ).size() );
        assertEquals( 0, userStore.getUsersByOrganisationUnit( unit2 ).size() );
        assertEquals( 1, userStore.getUsersWithoutOrganisationUnit().size() );

        // Test updateUser
        user.setSurname( "User1" );
        user.setOrganisationUnits( units1 );
        userStore.updateUser( user );
        
        assertEquals( userStore.getUser( id ).getSurname(), "User1" );
        assertEquals( 1, userStore.getUsersByOrganisationUnit( unit1 ).size() );
        assertEquals( 1, userStore.getUsersByOrganisationUnit( unit2 ).size() );
        assertEquals( 0, userStore.getUsersWithoutOrganisationUnit().size() );

        // Test getUser
        assertEquals( userStore.getUser( user.getId() ).getSurname(), "User1" );
        assertEquals( userStore.getUser( user.getId() ).getFirstName(), userName );
        transactionManager.enter();
        assertEquals( 2, userStore.getUser( user.getId() ).getOrganisationUnits().size() );
        transactionManager.leave();
        assertEquals( userStore.getUser( user.getId() ).getId(), 1 );

        // Test getAllUsers
        User user2 = new User();
        Collection<OrganisationUnit> units2 = new HashSet<OrganisationUnit>();        
        units2.add(unit2);
        
        user2.setSurname( "User2" );
        user2.setFirstName( "User2" );
        user2.setOrganisationUnits( units2 );
        userStore.addUser( user2 );

        assertEquals( userStore.getAllUsers().size(), 2 );
        for ( int i = 1; i <= userStore.getAllUsers().size(); i++ )
        {
            // System.out.println( "User" + i );
            assertEquals( userStore.getUser( i ).getSurname(), "User" + i );
        }
        assertEquals( 1, userStore.getUsersByOrganisationUnit( unit1 ).size() );
        assertEquals( 2, userStore.getUsersByOrganisationUnit( unit2 ).size() );
        assertEquals( 0, userStore.getUsersWithoutOrganisationUnit().size() );

        // Test deleteUser
        User user3 = new User();
        user3.setSurname( "User3" );
        user3.setFirstName( "User3" );
        OrganisationUnit unit3 = new OrganisationUnit( "name3", "shortName3", "organisationUnitCode3", new Date(),
            new Date(), true, "comment" );        
        organisationUnitService.addOrganisationUnit( unit3 );
        Collection<OrganisationUnit> units3 = new HashSet<OrganisationUnit>();        
        units3.add(unit3);
        
        user.setOrganisationUnits( units3 );
        userStore.addUser( user3 );

        assertEquals( userStore.getAllUsers().size(), 3 );
        // delete User3
        assertEquals( userStore.getUser( user3.getId() ).getSurname(), "User3" );
        userStore.deleteUser( user3 );
        assertEquals( userStore.getAllUsers().size(), 2 );
    }

    public void testBasicUserCredentials()
        throws Exception
    {
        // Test addUserCredentials
        String username = "user";
        String password = "password";
        String someone = "someone";
        String iloveyou = "iloveyou";

        User user = new User();
        user.setSurname( username );
        user.setFirstName( username );
        userStore.addUser( user );

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser( user );
        userCredentials.setUsername( username );
        userCredentials.setPassword( password );

        userStore.addUserCredentials( userCredentials );

        transactionManager.enter();
        assertEquals( userStore.getUserCredentials( user ).getUser().getId(), user.getId() );
        transactionManager.leave();
        assertEquals( userStore.getUserCredentials( user ).getUsername(), username );
        assertEquals( userStore.getUserCredentials( user ).getPassword(), password );

        // Test updateUserCredentials
        userCredentials.setUser( user );
        userCredentials.setUsername( someone );
        userCredentials.setPassword( iloveyou );

        userStore.updateUserCredentials( userCredentials );
        assertEquals( userStore.getUserCredentials( user ).getUsername(), someone );
        assertEquals( userStore.getUserCredentials( user ).getPassword(), iloveyou );

        // Test getUserCredentials
        assertEquals( userStore.getUserCredentials( user ).getUsername(), someone );
        assertEquals( userStore.getUserCredentials( user ).getPassword(), iloveyou );

        // Test getUserCredentialsByUsername
        // System.out.println( userStore.getUserCredentialsByUsername( someone
        // ).getPassword() );
        assertEquals( userStore.getUserCredentialsByUsername( someone ).getPassword(), userCredentials.getPassword() );
        assertEquals( userStore.getUserCredentialsByUsername( someone ).getClass(), userCredentials.getClass() );

        // Test deleteUserCredentials
        // Before delete
        assertNotNull( userStore.getUserCredentials( user ) );
        userStore.deleteUserCredentials( userStore.getUserCredentials( user ) );
        // After delete
        assertNull( userStore.getUserCredentials( user ) );
    }

    public void testBasicUserAuthorityGroup()
        throws Exception
    {
        String name = "UserAuthorityGroup";
        String name1 = "UserAuthorityGroup1";
        String name2 = "UserAuthorityGroup2";

        // Test addUserAuthorityGroup
        UserAuthorityGroup userAuthorityGroup = new UserAuthorityGroup();
        userAuthorityGroup.setName( name );
        userStore.addUserAuthorityGroup( userAuthorityGroup );
        transactionManager.enter();
        assertEquals( userStore.getUserAuthorityGroup( userAuthorityGroup.getId() ).getName(), name );
        transactionManager.leave();

        // Test updateUserAuthorityGroup
        userAuthorityGroup.setName( name1 );
        userStore.updateUserAuthorityGroup( userAuthorityGroup );
        assertEquals( userAuthorityGroup.getName(), name1 );

        // Test getUserAuthorityGroup
        assertEquals( userStore.getUserAuthorityGroup( userAuthorityGroup.getId() ).getName(), name1 );
        assertEquals( userStore.getUserAuthorityGroup( userAuthorityGroup.getId() ).getClass(), userAuthorityGroup
            .getClass() );

        // Test getAllUserAuthorityGroups
        UserAuthorityGroup userAuthorityGroup2 = new UserAuthorityGroup();
        userAuthorityGroup2.setName( name2 );
        userStore.addUserAuthorityGroup( userAuthorityGroup2 );

        assertEquals( userStore.getAllUserAuthorityGroups().size(), 2 );
        for ( int i = 1; i <= userStore.getAllUserAuthorityGroups().size(); i++ )
        {
            // System.out.println( "UserAuthorityGroup" + i );
            assertEquals( userStore.getUserAuthorityGroup( i ).getName(), "UserAuthorityGroup" + i );
        }

        // Test deleteUserAuthorityGroup
        assertEquals( userStore.getAllUserAuthorityGroups().size(), 2 );
        userStore.deleteUserAuthorityGroup( userAuthorityGroup2 );
        assertEquals( userStore.getAllUserAuthorityGroups().size(), 1 );
    }

    public void testBasicUserSettings()
        throws Exception
    {
        String name = "name";
        String value = "value";
        String value1 = "value1";

        // Test addUserSetting
        String userName = "User";
        User user = new User();
        user.setSurname( userName );
        user.setFirstName( userName );
        userStore.addUser( user );

        UserSetting userSetting = new UserSetting();
        userSetting.setUser( user );
        userSetting.setName( name );
        userSetting.setValue( value );

        userStore.addUserSetting( userSetting );
        assertEquals( userStore.getUserSetting( user, name ).getName(), userSetting.getName() );

        // Test updateUserSetting
        userSetting.setValue( value1 );
        // System.out.println( userSetting.getName() );
        userStore.updateUserSetting( userSetting );
        // System.out.println( userSetting.getName() );
        assertEquals( value1, userStore.getUserSetting( user, name ).getValue() );

        // Test getUserSetting
        assertEquals( userStore.getUserSetting( userSetting.getUser(), name ).getName(), name );
        transactionManager.enter();
        assertEquals( userStore.getUserSetting( userSetting.getUser(), name ).getUser().getId(), user.getId() );
        transactionManager.leave();
        assertEquals( userStore.getUserSetting( userSetting.getUser(), name ).getValue(), value1 );

        // Test getAllUserSettings
        assertEquals( userStore.getAllUserSettings( user ).size(), 1 );
        for ( int i = 1; i <= userStore.getAllUserSettings( user ).size(); i++ )
        {
            // System.out.println( "UserSettings" + i );
            assertEquals( userStore.getUserSetting( user, name ).getValue(), "value" + i );
        }

        // Test deleteUserSetting
        assertEquals( userStore.getAllUserSettings( user ).size(), 1 );
        userStore.deleteUserSetting( userStore.getUserSetting( user, name ) );
        assertEquals( userStore.getAllUserSettings( user ).size(), 0 );
    }
}
