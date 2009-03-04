package org.hisp.dhis.gis.hibernate;

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
import org.hisp.dhis.gis.Feature;
import org.hisp.dhis.gis.FeatureStore;
import org.hisp.dhis.gis.MapFile;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public class HibernateFeatureStore
    implements FeatureStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public HibernateSessionManager getSessionManager()
    {
        return sessionManager;
    }

    // -------------------------------------------------------------------------
    // FeatureStore implementation
    // -------------------------------------------------------------------------

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    public void add( Feature feature )
    {
        Session session = sessionManager.getCurrentSession();
        session.save( feature );
    }

    public void delete( Feature feature )
    {
        Session session = sessionManager.getCurrentSession();
        session.delete( feature );
    }

    public Feature get( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (Feature) session.get( Feature.class, new Integer( id ) );
    }

    public Feature get( String featureCode )
    {
        Session session = sessionManager.getCurrentSession();
        Criteria criteria = session.createCriteria( Feature.class );
        criteria.add( Restrictions.eq( "featureCode", featureCode ) );
        return (Feature) criteria.uniqueResult();
    }

    public Feature get( OrganisationUnit organisationUnit )
    {
        Session session = sessionManager.getCurrentSession();

        return (Feature) session.createQuery( "from Feature as f where f.organisationUnit = ?" ).setEntity( 0,
            organisationUnit ).uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Feature> getAll()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( Feature.class ).list();
    }

    public void update( Feature feature )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( feature );
    }

    public void delete( String featureCode )
    {
        Session session = sessionManager.getCurrentSession();

        session.createQuery( "delete Feature as f where f.featureCode = ?" ).setEntity( 0, featureCode );
    }

    public void addMapFile( MapFile arg0 )
    {
        Session session = sessionManager.getCurrentSession();

        session.save( arg0 );
    }

    public void deleteMapFile( MapFile arg0 )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( arg0 );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<MapFile> getAllMapFile()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( MapFile.class ).list();
    }

    public MapFile getMapFile( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (MapFile) session.get( MapFile.class, new Integer( id ) );
    }

    public MapFile getMapFile( OrganisationUnit organisationUnit )
    {
        Session session = sessionManager.getCurrentSession();

        return (MapFile) session.createQuery( "from MapFile as f where f.organisationUnit = ?" ).setEntity( 0,
            organisationUnit ).uniqueResult();
    }

    public void updateMapFile( MapFile arg0 )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( arg0 );
    }
}
