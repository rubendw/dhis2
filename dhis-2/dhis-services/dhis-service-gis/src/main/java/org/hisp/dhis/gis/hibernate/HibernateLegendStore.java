package org.hisp.dhis.gis.hibernate;

/* Copyright (c) 2004-2007, University of Oslo
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
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.LegendSet;
import org.hisp.dhis.gis.LegendStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.indicator.Indicator;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public class HibernateLegendStore
    implements LegendStore
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
    // LegendStore implementation
    // -------------------------------------------------------------------------

    public void addLegend( Legend legend )
    {
        Session session = sessionManager.getCurrentSession();

        session.save( legend );
    }

    public void deleteLegend( Legend legend )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( legend );
    }

    public Legend getLegend( int legendId )
    {
        Session session = sessionManager.getCurrentSession();

        return (Legend) session.get( Legend.class, new Integer( legendId ) );
    }

    @SuppressWarnings( "unchecked" )
    public Set<Legend> getLegendByMaxMin( double max, double min )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Legend.class );
        criteria.add( Restrictions.eq( "max", max ) );
        criteria.add( Restrictions.eq( "min", min ) );

        return (Set<Legend>) criteria.list();
    }

    public void updateLegend( Legend legend )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( legend );

    }

    @SuppressWarnings( "unchecked" )
    public Set<Legend> getAllLegend()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Legend.class );

        return new HashSet<Legend>( criteria.list() );
    }

    public Legend getLegendByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Legend.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (Legend) criteria.uniqueResult();
    }

    public void addLegendSet( LegendSet legendSet )
    {
        Session session = sessionManager.getCurrentSession();

        session.save( legendSet );
    }

    public void deleteLegendSet( LegendSet legendSet )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( legendSet );
    }

    @SuppressWarnings( "unchecked" )
    public Set<LegendSet> getAllLegendSet()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( LegendSet.class );

        return new HashSet<LegendSet>( criteria.list() );
    }

    public LegendSet getLegendSet( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (LegendSet) session.get( LegendSet.class, new Integer( id ) );
    }

    public LegendSet getLegendSet( String name )
    {
        Session session = sessionManager.getCurrentSession();
        Criteria criteria = session.createCriteria( LegendSet.class );
        criteria.add( Restrictions.eq( "name", name ) );
        return (LegendSet) criteria.uniqueResult();
    }

    public LegendSet getLegendSet( Indicator indicator )
    {
        Set<LegendSet> legendSets = getAllLegendSet();

        for ( LegendSet legendSet : legendSets )
        {
            for ( Indicator temp : legendSet.getIndicators() )
            {
                if ( temp.equals( indicator ) )
                {
                    return legendSet;
                }
            }
        }

        return null;
    }

    public void updateLegendSet( LegendSet legendSet )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( legendSet );
    }
}
