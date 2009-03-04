package org.hisp.dhis.user.hibernate;

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
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserSetting;
import org.hisp.dhis.user.UserStore;

/**
 * @author Nguyen Hong Duc
 * @version $Id: HibernateUserStore.java 5724 2008-09-18 14:37:01Z larshelg $
 */
public class HibernateUserStore
    implements UserStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    public int addUser( User user )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( user );
    }

    public void updateUser( User user )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( user );
    }

    public User getUser( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (User) session.get( User.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<User> getAllUsers()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createQuery( "from User" ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<User> getUsersByOrganisationUnit( OrganisationUnit organisationUnit )
    {   
        Collection<User> users = getAllUsers();
        
        Iterator<User> iterator = users.iterator();
        
        while( iterator.hasNext() )
        {
            if( ! iterator.next().getOrganisationUnits().contains( organisationUnit ) )
            {
        	iterator.remove();
            }
        }
        
        return users;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<User> getUsersWithoutOrganisationUnit()
    {    	
    	Collection<User> users = getAllUsers();
        
        Iterator<User> iterator = users.iterator();
        
        while( iterator.hasNext() )
        {
            if( iterator.next().getOrganisationUnits().size() > 0 )
            {
        	iterator.remove();
            }
        }
        
        return users;
    }

    public void deleteUser( User user )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( user );
    }

    // -------------------------------------------------------------------------
    // UserCredentials
    // -------------------------------------------------------------------------

    public User addUserCredentials( UserCredentials userCredentials )
    {
        Session session = sessionManager.getCurrentSession();

        int id = (Integer) session.save( userCredentials );

        return getUser( id );
    }

    public void updateUserCredentials( UserCredentials userCredentials )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( userCredentials );
    }

    public UserCredentials getUserCredentials( User user )
    {
        Session session = sessionManager.getCurrentSession();

        return (UserCredentials) session.get( UserCredentials.class, user.getId() );
    }

    public UserCredentials getUserCredentialsByUsername( String username )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from UserCredentials uc where uc.username = :username" );

        query.setString( "username", username );

        return (UserCredentials) query.uniqueResult();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<UserCredentials> getAllUserCredentials()
    {
        Session session = sessionManager.getCurrentSession();
        
        return session.createCriteria( UserCredentials.class ).list();
    }

    public void deleteUserCredentials( UserCredentials userCredentials )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( userCredentials );
    }

    // -------------------------------------------------------------------------
    // UserAuthorityGroup
    // -------------------------------------------------------------------------

    public int addUserAuthorityGroup( UserAuthorityGroup userAuthorityGroup )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( userAuthorityGroup );
    }

    public void updateUserAuthorityGroup( UserAuthorityGroup userAuthorityGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( userAuthorityGroup );
    }

    public UserAuthorityGroup getUserAuthorityGroup( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (UserAuthorityGroup) session.get( UserAuthorityGroup.class, id );
    }
    
    public UserAuthorityGroup getUserAuthorityGroupByName( String name )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( UserAuthorityGroup.class );
        
        criteria.add( Restrictions.eq( "name", name ) );
        
        return (UserAuthorityGroup) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<UserAuthorityGroup> getAllUserAuthorityGroups()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createQuery( "from UserAuthorityGroup" ).list();
    }

    public void deleteUserAuthorityGroup( UserAuthorityGroup userAuthorityGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( userAuthorityGroup );
    }

    // -------------------------------------------------------------------------
    // UserSettings
    // -------------------------------------------------------------------------

    public void addUserSetting( UserSetting userSetting )
    {
        Session session = sessionManager.getCurrentSession();

        session.save( userSetting );
    }

    public void updateUserSetting( UserSetting userSetting )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( userSetting );
    }

    public UserSetting getUserSetting( User user, String name )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from UserSetting us where us.user = :user and us.name = :name" );

        query.setEntity( "user", user );
        query.setString( "name", name );

        return (UserSetting) query.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<UserSetting> getAllUserSettings( User user )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from UserSetting us where us.user = :user" );

        query.setEntity( "user", user );

        return query.list();
    }

    public void deleteUserSetting( UserSetting userSetting )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( userSetting );
    }
}
