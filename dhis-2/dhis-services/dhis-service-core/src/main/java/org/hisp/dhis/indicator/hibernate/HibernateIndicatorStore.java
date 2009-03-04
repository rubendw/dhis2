package org.hisp.dhis.indicator.hibernate;

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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorStore;
import org.hisp.dhis.indicator.IndicatorType;

/**
 * @author Lars Helge Overland
 * @version $Id: HibernateIndicatorStore.java 3287 2007-05-08 00:26:53Z larshelg $
 */
public class HibernateIndicatorStore 
    implements IndicatorStore
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
    // Indicator
    // -------------------------------------------------------------------------

    public int addIndicator( Indicator indicator )
    {        
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( indicator );
    }

    public void updateIndicator( Indicator indicator )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( indicator );
    }

    public void deleteIndicator( Indicator indicator )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( indicator );
    }

    public Indicator getIndicator( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (Indicator) session.get( Indicator.class, new Integer( id ) );
    }
    
    public Indicator getIndicator( String uuid )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Indicator.class );
        criteria.add( Restrictions.eq( "uuid", uuid ) );
        
        return (Indicator) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public Collection<Indicator> getAllIndicators()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createQuery( "from Indicator" ).list();
    }

    public Indicator getIndicatorByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from Indicator i where i.name = :name" );

        query.setString( "name", name );

        return (Indicator) query.uniqueResult();
    }

    public Indicator getIndicatorByShortName( String shortName )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from Indicator i where i.shortName = :shortName" );

        query.setString( "shortName", shortName );

        return (Indicator) query.uniqueResult();
    }

    public Indicator getIndicatorByAlternativeName( String alternativeName )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from Indicator i where i.alternativeName = :alternativeName" );

        query.setString( "alternativeName", alternativeName );

        return (Indicator) query.uniqueResult();
    }

    public Indicator getIndicatorByCode( String code )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from Indicator i where i.code = :code" );

        query.setString( "code", code );

        return (Indicator) query.uniqueResult();
    }

    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    public int addIndicatorType( IndicatorType indicatorType )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( indicatorType );
    }

    public void updateIndicatorType( IndicatorType indicatorType )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( indicatorType );
    }

    public void deleteIndicatorType( IndicatorType indicatorType )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( indicatorType );
    }

    public IndicatorType getIndicatorType( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (IndicatorType) session.get( IndicatorType.class, new Integer( id ) );
    }

    @SuppressWarnings("unchecked")
    public Collection<IndicatorType> getAllIndicatorTypes()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createQuery( "from IndicatorType" ).list();
    }

    public IndicatorType getIndicatorTypeByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from IndicatorType i where i.name = :name" );

        query.setString( "name", name );

        return (IndicatorType) query.uniqueResult();
    }

    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    public int addIndicatorGroup( IndicatorGroup indicatorGroup )
    {        
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( indicatorGroup );
    }

    public void updateIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        Session session = sessionManager.getCurrentSession();
        
        session.update( indicatorGroup );
    }

    public void deleteIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( indicatorGroup );
    }

    public IndicatorGroup getIndicatorGroup( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (IndicatorGroup) session.get( IndicatorGroup.class, new Integer( id ) );
    }
    
    public IndicatorGroup getIndicatorGroup( String uuid )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( IndicatorGroup.class );
        criteria.add( Restrictions.eq( "uuid", uuid ) );
        
        return (IndicatorGroup) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public Collection<IndicatorGroup> getAllIndicatorGroups()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createQuery( "from IndicatorGroup" ).list();
    }

    public IndicatorGroup getIndicatorGroupByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from IndicatorGroup i where i.name = :name" );

        query.setString( "name", name );

        return (IndicatorGroup) query.uniqueResult();
    }
}
