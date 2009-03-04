package org.hisp.dhis.dataelement.hibernate;

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
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrder;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrderStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class HibernateDataElementDimensionColumnOrderStore
    implements DataElementDimensionColumnOrderStore
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
    // DataElementDimensionColumnOrder
    // -------------------------------------------------------------------------

    public void addDataElementDimensionColumnOrder( DataElementDimensionColumnOrder dataElementDimensionColumnOrder )
    {
        Session session = sessionManager.getCurrentSession();

        session.save( dataElementDimensionColumnOrder );
    }

    @SuppressWarnings( "unchecked" )
    public void deleteDataElementDimensionColumnOrder( DataElementDimensionColumnOrder dataElementDimensionColumnOrder )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( dataElementDimensionColumnOrder );
    }

    public int deleteDataElementDimensionColumnOrder( DataElementCategoryOption categoryOption )
    {
        String hql = "delete from DataElementDimensionColumnOrder d where d.categoryOption = :categoryOption";
        
        Query query = sessionManager.getCurrentSession().createQuery( hql );
        
        query.setEntity( "categoryOption", categoryOption );
        
        return query.executeUpdate();
    }

    public int deleteDataElementDimensionColumnOrder( DataElementCategory category )
    {
        String hql = "delete from DataElementDimensionColumnOrder d where d.category = :category";
        
        Query query = sessionManager.getCurrentSession().createQuery( hql );
        
        query.setEntity( "category", category );
        
        return query.executeUpdate();
    }

    public DataElementDimensionColumnOrder getDataElementDimensionColumnOrder( DataElementCategory category,
        DataElementCategoryOption categoryOption )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementDimensionColumnOrder.class );
        criteria.add( Restrictions.eq( "category", category ) );
        criteria.add( Restrictions.eq( "categoryOption", categoryOption ) );

        return (DataElementDimensionColumnOrder) criteria.uniqueResult();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<DataElementDimensionColumnOrder> getDataElementDimensionColumnOrders( DataElementCategory category )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementDimensionColumnOrder.class );
        criteria.add( Restrictions.eq( "category", category ) );

        return criteria.list();
    }

    public void updateDataElementDimensionColumnOrder( DataElementDimensionColumnOrder dataElementDimensionColumnOrder )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( dataElementDimensionColumnOrder );
    }
}
