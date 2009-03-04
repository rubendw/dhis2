package org.hisp.dhis.organisationunit.hibernate;

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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupStore;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: HibernateOrganisationUnitGroupStore.java 3286 2007-05-07 18:05:21Z larshelg $
 */
public class HibernateOrganisationUnitGroupStore
    implements OrganisationUnitGroupStore
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
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    public int addOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( organisationUnitGroup );
    }

    public void updateOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( organisationUnitGroup );
    }

    public void deleteOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( organisationUnitGroup );
    }

    public OrganisationUnitGroup getOrganisationUnitGroup( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (OrganisationUnitGroup) session.get( OrganisationUnitGroup.class, id );
    }

    public OrganisationUnitGroup getOrganisationUnitGroup( String uuid )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( OrganisationUnitGroup.class );
        criteria.add( Restrictions.eq( "uuid", uuid ) );
        
        return (OrganisationUnitGroup) criteria.uniqueResult();                
    }
    
    public OrganisationUnitGroup getOrganisationUnitGroupByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitGroup.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (OrganisationUnitGroup) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnitGroup> getAllOrganisationUnitGroups()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitGroup.class );

        return criteria.list();
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSet
    // -------------------------------------------------------------------------

    public int addOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( organisationUnitGroupSet );
    }

    public void updateOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( organisationUnitGroupSet );
    }

    public void deleteOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( organisationUnitGroupSet );
    }

    public OrganisationUnitGroupSet getOrganisationUnitGroupSet( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (OrganisationUnitGroupSet) session.get( OrganisationUnitGroupSet.class, id );
    }

    public OrganisationUnitGroupSet getOrganisationUnitGroupSetByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitGroupSet.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (OrganisationUnitGroupSet) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnitGroupSet> getAllOrganisationUnitGroupSets()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitGroupSet.class );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnitGroupSet> getCompulsoryOrganisationUnitGroupSets()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitGroupSet.class );
        criteria.add( Restrictions.eq( "compulsory", true ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnitGroupSet> getExclusiveOrganisationUnitGroupSets()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitGroupSet.class );
        criteria.add( Restrictions.eq( "exclusive", true ) );

        return criteria.list();
    }
}
