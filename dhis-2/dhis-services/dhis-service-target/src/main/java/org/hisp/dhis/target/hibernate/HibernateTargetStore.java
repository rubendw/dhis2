package org.hisp.dhis.target.hibernate;

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
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetStore;

/**
 * @author Martin Myrseth
 * @version $Id$
 */
public class HibernateTargetStore
    implements TargetStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }
    
    private PeriodStore periodStore; //TODO service

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }
    
    // -------------------------------------------------------------------------
    // Support methods for reloading periods
    // -------------------------------------------------------------------------

    private final Period reloadPeriod( Period period )
    {
        Session session = sessionManager.getCurrentSession();

        if ( session.contains( period ) )
        {
            return period; // Already in session, no reload needed
        }

        return periodStore.getPeriod( period.getStartDate(), period.getEndDate(), period.getPeriodType() );
    }

    private final Period reloadPeriodForceAdd( Period period )
    {
        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            periodStore.addPeriod( period );

            return period;
        }

        return storedPeriod;
    }

    // -------------------------------------------------------------------------
    // TargetStore implementation
    // -------------------------------------------------------------------------

    public int addTarget( Target target )
    {
        target.setPeriod( reloadPeriodForceAdd( target.getPeriod() ) );
        
        Session session = sessionManager.getCurrentSession();
        
        return (Integer) session.save( target );
    }

    public void deleteTarget( Target target )
    {
        Session session = sessionManager.getCurrentSession();
        
        session.delete( target );
    }
    
    public Target getTarget( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (Target) session.get( Target.class, id );
    }

    public void updateTarget( Target target )
    {
        target.setPeriod( reloadPeriodForceAdd( target.getPeriod() ) );
        
        Session session = sessionManager.getCurrentSession();
        
        session.update( target );
    }
    
    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Indicator indicator, Period period, Source source ) 
    {
        Session session = sessionManager.getCurrentSession();

        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return null;
        }
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "indicator", indicator ) );
        criteria.add( Restrictions.eq( "period", period ) );
        criteria.add( Restrictions.eq( "source", source ) );
        
        return criteria.list();
    }

    public Target getTargetByName( String name ) 
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "name", name ) );
        
        return (Target) criteria.uniqueResult();
    }

    public Target getTargetByShortName( String shortName ) 
    {
        Session session = sessionManager.getCurrentSession();
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "shortName", shortName ) );
        return (Target) criteria.uniqueResult();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<Target> getAllTargets()
    {
        Session session = sessionManager.getCurrentSession();
        
        return session.createCriteria( Target.class ).list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Indicator indicator )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "indicator", indicator ) );
        
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Period period )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "period", period ) );
        
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Source source )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "source", source ) );
        
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Indicator indicator, Period period )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "indicator", indicator) );
        criteria.add( Restrictions.eq( "period", period ) );
        
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Indicator indicator, Source source )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "indicator", indicator) );
        criteria.add( Restrictions.eq( "source", source ) );
        
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Target> getTargets( Period period, Source source )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Target.class );
        criteria.add( Restrictions.eq( "period", period ) );
        criteria.add( Restrictions.eq( "source", source ) );
        
        return criteria.list();
    }
}
