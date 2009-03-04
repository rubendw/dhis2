package org.hisp.dhis.datavalue.hibernate;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.source.Source;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: HibernateDataValueStore.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public class HibernateDataValueStore
    implements DataValueStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    private PeriodStore periodStore;

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
    // Basic DataValue
    // -------------------------------------------------------------------------

    public void addDataValue( DataValue dataValue )
    {
        dataValue.setPeriod( reloadPeriodForceAdd( dataValue.getPeriod() ) );

        Session session = sessionManager.getCurrentSession();

        session.save( dataValue );
    }

    public void updateDataValue( DataValue dataValue )
    {
        dataValue.setPeriod( reloadPeriodForceAdd( dataValue.getPeriod() ) );

        Session session = sessionManager.getCurrentSession();

        session.update( dataValue );
    }

    public void deleteDataValue( DataValue dataValue )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( dataValue );
    }

    public int deleteDataValuesBySource( Source source )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "delete DataValue where source = :source" );
        query.setEntity( "source", source );

        return query.executeUpdate();
    }
    
    public int deleteDataValuesByDataElement( DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "delete DataValue where dataElement = :dataElement" );
        query.setEntity( "dataElement", dataElement );

        return query.executeUpdate();    
    }
    
    public DataValue getDataValue( Source source, DataElement dataElement, Period period )
    {
        Session session = sessionManager.getCurrentSession();

        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return null;
        }

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );

        return (DataValue) criteria.uniqueResult();
    }
    
    public DataValue getDataValue( Source source, DataElement dataElement, Period period, DataElementCategoryOptionCombo optionCombo )
    {
        Session session = sessionManager.getCurrentSession();

        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return null;
        }

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.eq( "optionCombo", optionCombo ) );

        return (DataValue) criteria.uniqueResult();
    }

    // -------------------------------------------------------------------------
    // Collections of DataValues
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getAllDataValues()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Source source, Period period )
    {
        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return Collections.emptySet();
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Source source, DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Collection<? extends Source> sources, DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.in( "source", sources ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Source source, Period period, Collection<DataElement> dataElements )
    {
        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return Collections.emptySet();
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.in( "dataElement", dataElements ) );

        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Source source, Period period, Collection<DataElement> dataElements, Collection<DataElementCategoryOptionCombo> optionCombos  )
    {
        Period storedPeriod = reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return Collections.emptySet();
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.in( "dataElement", dataElements ) );
        criteria.add( Restrictions.in( "optionCombo", optionCombos ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement, Collection<Period> periods,
        Collection<? extends Source> sources )
    {
        Collection<Period> storedPeriods = new ArrayList<Period>();

        for ( Period period : periods )
        {
            Period storedPeriod = reloadPeriod( period );

            if ( storedPeriod != null )
            {
                storedPeriods.add( storedPeriod );
            }
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.in( "period", storedPeriods ) );
        criteria.add( Restrictions.in( "source", sources ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, 
        Collection<Period> periods, Collection<? extends Source> sources )
    {
        Collection<Period> storedPeriods = new ArrayList<Period>();

        for ( Period period : periods )
        {
            Period storedPeriod = reloadPeriod( period );

            if ( storedPeriod != null )
            {
                storedPeriods.add( storedPeriod );
            }
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.eq( "optionCombo", optionCombo ) );
        criteria.add( Restrictions.in( "period", storedPeriods ) );
        criteria.add( Restrictions.in( "source", sources ) );

        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Collection<DataElement> dataElements, Collection<Period> periods,
        Collection<? extends Source> sources, int firstResult, int maxResults )
    {
        Collection<Period> storedPeriods = new ArrayList<Period>();

        for ( Period period : periods )
        {
            Period storedPeriod = reloadPeriod( period );

            if ( storedPeriod != null )
            {
                storedPeriods.add( storedPeriod );
            }
        }

        if ( storedPeriods.size() == 0 )
        {
            return Collections.emptySet();
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );

        criteria.add( Restrictions.in( "dataElement", dataElements ) );
        criteria.add( Restrictions.in( "period", storedPeriods ) );
        criteria.add( Restrictions.in( "source", sources ) );

        if ( maxResults != 0 )
        {
            criteria.addOrder( Order.asc( "dataElement" ) );
            criteria.addOrder( Order.asc( "period" ) );
            criteria.addOrder( Order.asc( "source" ) );
    
            criteria.setFirstResult( firstResult );
            criteria.setMaxResults( maxResults );
        }

        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Collection<DataElementCategoryOptionCombo> optionCombos )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );        
        criteria.add( Restrictions.in( "optionCombo", optionCombos ) );

        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );        
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );

        return criteria.list();
    }
}
