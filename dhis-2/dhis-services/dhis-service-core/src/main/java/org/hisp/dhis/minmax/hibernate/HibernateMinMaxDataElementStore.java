package org.hisp.dhis.minmax.hibernate;

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
import java.util.Collections;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementStore;
import org.hisp.dhis.source.Source;

/**
 * @author Kristian Nordal
 * @version $Id: HibernateMinMaxDataElementStore.java 5012 2008-04-24 21:14:40Z larshelg $
 */
public class HibernateMinMaxDataElementStore
    implements MinMaxDataElementStore
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
    // Implementation
    // -------------------------------------------------------------------------

    public int addMinMaxDataElement( MinMaxDataElement minMaxDataElement )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( minMaxDataElement );
    }

    public void delMinMaxDataElement( int id )
    {
        Session session = sessionManager.getCurrentSession();

        MinMaxDataElement minMaxDataElement = (MinMaxDataElement) session.get( MinMaxDataElement.class,
            new Integer( id ) );

        session.delete( minMaxDataElement );
    }

    public void updateMinMaxDataElement( MinMaxDataElement minMaxDataElement )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( minMaxDataElement );
    }

    public MinMaxDataElement getMinMaxDataElement( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (MinMaxDataElement) session.get( MinMaxDataElement.class, new Integer( id ) );
    }

    public MinMaxDataElement getMinMaxDataElement( Source source, DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( MinMaxDataElement.class );
        criteria.add( Expression.eq( "source", source ) );
        criteria.add( Expression.eq( "dataElement", dataElement ) );

        return (MinMaxDataElement) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<MinMaxDataElement> getMinMaxDataElements( Source source,
        Collection<DataElement> dataElements )
    {
        if ( dataElements.size() == 0 )
        {
            return Collections.emptySet();
        }

        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( MinMaxDataElement.class );
        criteria.add( Expression.eq( "source", source ) );
        criteria.add( Expression.in( "dataElement", dataElements ) );

        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<MinMaxDataElement> getAllMinMaxDataElements()
    {
        Session session = sessionManager.getCurrentSession();
        
        return session.createCriteria( MinMaxDataElement.class ).list();
    }
}
