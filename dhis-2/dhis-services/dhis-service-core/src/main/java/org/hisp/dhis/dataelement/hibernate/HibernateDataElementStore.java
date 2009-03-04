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
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.hierarchy.HierarchyViolationException;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: HibernateDataElementStore.java 5243 2008-05-25 10:18:58Z
 *          larshelg $
 */
public class HibernateDataElementStore
    implements DataElementStore
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
    // DataElement
    // -------------------------------------------------------------------------

    public int addDataElement( DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( dataElement );
    }

    public void updateDataElement( DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( dataElement );
    }

    @SuppressWarnings( "unchecked" )
    public void deleteDataElement( DataElement dataElement )
        throws HierarchyViolationException
    {
        if ( !dataElement.getChildren().isEmpty() )
        {
            throw new HierarchyViolationException( "Not allowed to delete DataElements with children" );
        }

        Session session = sessionManager.getCurrentSession();

        session.delete( dataElement );
    }

    public DataElement getDataElement( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (DataElement) session.get( DataElement.class, id );
    }

    public DataElement getDataElement( String uuid )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "uuid", uuid ) );

        return (DataElement) criteria.uniqueResult();
    }

    public DataElement getDataElementByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (DataElement) criteria.uniqueResult();
    }

    public DataElement getDataElementByAlternativeName( String alternativeName )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "alternativeName", alternativeName ) );

        return (DataElement) criteria.uniqueResult();
    }

    public DataElement getDataElementByShortName( String shortName )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "shortName", shortName ) );

        return (DataElement) criteria.uniqueResult();
    }

    public DataElement getDataElementByCode( String code )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "code", code ) );

        return (DataElement) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElement> getAllDataElements()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElement> getAggregateableDataElements()
    {
        Session session = sessionManager.getCurrentSession();

        Set<String> types = new HashSet<String>();

        types.add( DataElement.TYPE_INT );
        types.add( DataElement.TYPE_BOOL );

        Criteria criteria = session.createCriteria( DataElement.class );

        criteria.add( Restrictions.in( "type", types ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElement> getAllActiveDataElements()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "active", true ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElement> getDataElementsByAggregationOperator( String aggregationOperator )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "aggregationOperator", aggregationOperator ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElement> getDataElementsByType( String type )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElement.class );
        criteria.add( Restrictions.eq( "type", type ) );

        return criteria.list();
    }

    // -------------------------------------------------------------------------
    // CalculatedDataElement
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public Collection<CalculatedDataElement> getAllCalculatedDataElements()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( CalculatedDataElement.class );

        return criteria.list();
    }

    public CalculatedDataElement getCalculatedDataElementByDataElement( DataElement dataElement )
    {
        Session session = sessionManager.getCurrentSession();

        Set<Integer> dataElementIds = new HashSet<Integer>();
        dataElementIds.add( dataElement.getId() );

        Criteria criteria = session.createCriteria( CalculatedDataElement.class ).createCriteria( "expression" )
            .createCriteria( "dataElementsInExpression" ).add( Restrictions.in( "id", dataElementIds ) );

        return (CalculatedDataElement) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<CalculatedDataElement> getCalculatedDataElementsByDataElements(
        Collection<DataElement> dataElements )
    {
        Session session = sessionManager.getCurrentSession();

        Set<Integer> dataElementIds = new HashSet<Integer>();

        for ( DataElement dataElement : dataElements )
        {
            dataElementIds.add( dataElement.getId() );
        }

        Criteria criteria = session.createCriteria( CalculatedDataElement.class ).createCriteria( "expression" )
            .createCriteria( "dataElementsInExpression" ).add( Restrictions.in( "id", dataElementIds ) );

        return new HashSet<CalculatedDataElement>( criteria.list() );

    }

    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    public int addDataElementGroup( DataElementGroup dataElementGroup )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( dataElementGroup );
    }

    public void updateDataElementGroup( DataElementGroup dataElementGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( dataElementGroup );
    }

    public void deleteDataElementGroup( DataElementGroup dataElementGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( dataElementGroup );
    }

    public DataElementGroup getDataElementGroup( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (DataElementGroup) session.get( DataElementGroup.class, id );
    }

    public DataElementGroup getDataElementGroup( String uuid )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementGroup.class );
        criteria.add( Restrictions.eq( "uuid", uuid ) );

        return (DataElementGroup) criteria.uniqueResult();
    }

    public DataElementGroup getDataElementGroupByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementGroup.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (DataElementGroup) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElementGroup> getAllDataElementGroups()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementGroup.class );

        return criteria.list();
    }
}
