package org.hisp.dhis.dataset.hibernate;

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
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.dataset.FrequencyOverrideAssociation;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;

/**
 * @author Kristian Nordal
 * @version $Id: HibernateDataSetStore.java 3303 2007-05-14 13:39:34Z larshelg $
 */
public class HibernateDataSetStore
    implements DataSetStore
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
    // DataSet
    // -------------------------------------------------------------------------

    public int addDataSet( DataSet dataSet )
    {
        PeriodType periodType = periodStore.getPeriodType( dataSet.getPeriodType().getClass() );

        dataSet.setPeriodType( periodType );

        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( dataSet );
    }

    public void updateDataSet( DataSet dataSet )
    {
        PeriodType periodType = periodStore.getPeriodType( dataSet.getPeriodType().getClass() );

        dataSet.setPeriodType( periodType );

        Session session = sessionManager.getCurrentSession();

        session.update( dataSet );
    }

    public void deleteDataSet( DataSet dataSet )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( dataSet );
    }

    public DataSet getDataSet( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (DataSet) session.get( DataSet.class, new Integer( id ) );
    }

    public DataSet getDataSetByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataSet.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (DataSet) criteria.uniqueResult();
    }

    public DataSet getDataSetByShortName( String shortName )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataSet.class );
        criteria.add( Restrictions.eq( "shortName", shortName ) );

        return (DataSet) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataSet> getDataSetsBySource( Source source )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataSet.class );
        criteria.createAlias( "sources", "s" );
        criteria.add( Restrictions.eq( "s.id", source.getId() ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataSet> getAllDataSets()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( DataSet.class ).list();
    }

    // -------------------------------------------------------------------------
    // FrequencyOverrideAssociation
    // -------------------------------------------------------------------------

    public void addFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation )
    {
        PeriodType periodType = PeriodType.getPeriodTypeByName( frequencyOverrideAssociation.getPeriodType().getName() );

        periodType = periodStore.getPeriodType( periodType.getClass() );

        frequencyOverrideAssociation.setPeriodType( periodType );

        Session session = sessionManager.getCurrentSession();

        session.save( frequencyOverrideAssociation );
    }

    public void updateFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation )
    {
        PeriodType periodType = PeriodType.getPeriodTypeByName( frequencyOverrideAssociation.getPeriodType().getName() );

        periodType = periodStore.getPeriodType( periodType.getClass() );

        frequencyOverrideAssociation.setPeriodType( periodType );

        Session session = sessionManager.getCurrentSession();

        session.update( frequencyOverrideAssociation );
    }

    public void removeFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( frequencyOverrideAssociation );
    }

    public FrequencyOverrideAssociation getFrequencyOverrideAssociation( DataSet dataSet, Source source )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( FrequencyOverrideAssociation.class );
        criteria.add( Restrictions.eq( "dataSet", dataSet ) );
        criteria.add( Restrictions.eq( "source", source ) );

        return (FrequencyOverrideAssociation) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<FrequencyOverrideAssociation> getFrequencyOverrideAssociationsByDataSet( DataSet dataSet )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( FrequencyOverrideAssociation.class );
        criteria.add( Restrictions.eq( "dataSet", dataSet ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<FrequencyOverrideAssociation> getFrequencyOverrideAssociationsBySource( Source source )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( FrequencyOverrideAssociation.class );
        criteria.add( Restrictions.eq( "source", source ) );

        return criteria.list();
    }
}
