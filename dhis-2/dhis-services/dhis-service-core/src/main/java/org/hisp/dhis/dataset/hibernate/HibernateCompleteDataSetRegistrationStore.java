package org.hisp.dhis.dataset.hibernate;

/*
 * Copyright (c) 2004-2007, University of Oslo All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the HISP project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Collection;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationStore;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class HibernateCompleteDataSetRegistrationStore
    implements CompleteDataSetRegistrationStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Period reloadPeriod( Period period )
    {
        Period persistedPeriod = periodService.getPeriod( 
            period.getStartDate(), period.getEndDate(), period.getPeriodType() );
        
        if ( persistedPeriod != null )
        {
            return persistedPeriod;
        }
        
        periodService.addPeriod( period );
        
        return period;
    }
    
    // -------------------------------------------------------------------------
    // DataSetCompleteRegistrationStore implementation
    // -------------------------------------------------------------------------

    public void saveCompleteDataSetRegistration( CompleteDataSetRegistration registration )
    {
        registration.setPeriod( reloadPeriod( registration.getPeriod() ) );
        
        sessionManager.getCurrentSession().save( registration );
    }
    
    public CompleteDataSetRegistration getCompleteDataSetRegistration( DataSet dataSet, Period period, Source source )
    {
        period = reloadPeriod( period );
        
        Criteria criteria = sessionManager.getCurrentSession().createCriteria( CompleteDataSetRegistration.class );
        
        criteria.add( Restrictions.eq( "dataSet", dataSet ) );
        criteria.add( Restrictions.eq( "period", period ) );
        criteria.add( Restrictions.eq( "source", source ) );
        
        return (CompleteDataSetRegistration) criteria.uniqueResult();
    }
    
    public void deleteCompleteDataSetRegistration( CompleteDataSetRegistration registration )
    {
        sessionManager.getCurrentSession().delete( registration );
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<CompleteDataSetRegistration> getCompleteDataSetRegistrations( 
        DataSet dataSet, Collection<? extends Source> sources, Period period )
    {
        period = reloadPeriod( period );
        
        Criteria criteria = sessionManager.getCurrentSession().createCriteria( CompleteDataSetRegistration.class );
        
        criteria.add( Restrictions.eq( "dataSet", dataSet ) );
        criteria.add( Restrictions.in( "source", sources ) );
        criteria.add( Restrictions.eq( "period", period ) );
        
        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<CompleteDataSetRegistration> getAllCompleteDataSetRegistrations()
    {
        return sessionManager.getCurrentSession().createCriteria( CompleteDataSetRegistration.class ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<CompleteDataSetRegistration> getCompleteDataSetRegistrations( 
        Collection<DataSet> dataSets, Collection<? extends Source> sources, Collection<Period> periods )
    {
        for ( Period period : periods )
        {
            period = reloadPeriod( period );
        }        
        
        Criteria criteria = sessionManager.getCurrentSession().createCriteria( CompleteDataSetRegistration.class );
        
        criteria.add( Restrictions.in( "dataSet", dataSets ) );
        criteria.add( Restrictions.in( "source", sources ) );
        criteria.add( Restrictions.in( "period", periods ) );
        
        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<CompleteDataSetRegistration> getCompleteDataSetRegistrations( 
        DataSet dataSet, Collection<? extends Source> sources, Period period, Date deadline )
    {
        period = reloadPeriod( period );
        
        Criteria criteria = sessionManager.getCurrentSession().createCriteria( CompleteDataSetRegistration.class );
        
        criteria.add( Restrictions.eq( "dataSet", dataSet ) );
        criteria.add( Restrictions.in( "source", sources ) );
        criteria.add( Restrictions.eq( "period", period ) );
        criteria.add( Restrictions.le( "date", deadline ) );
        
        return criteria.list();
    }
    
    public void deleteCompleteDataSetRegistrations( DataSet dataSet )
    {
        String hql = "delete from CompleteDataSetRegistration c where c.dataSet = :dataSet";
        
        Query query = sessionManager.getCurrentSession().createQuery( hql );
        
        query.setEntity( "dataSet", dataSet );
        
        query.executeUpdate();
    }
}
