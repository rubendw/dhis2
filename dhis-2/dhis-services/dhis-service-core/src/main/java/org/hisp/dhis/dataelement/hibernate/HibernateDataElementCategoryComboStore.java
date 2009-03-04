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
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;

/**
 * @author Jan Henrik Overland
 * @version $Id$
 */
public class HibernateDataElementCategoryComboStore
    implements DataElementCategoryComboStore
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
    // DataElementCategoryCombo
    // -------------------------------------------------------------------------

    public int addDataElementCategoryCombo( DataElementCategoryCombo dataElementCategoryCombo )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( dataElementCategoryCombo );
    }

    public void updateDataElementCategoryCombo( DataElementCategoryCombo dataElementCategoryCombo )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( dataElementCategoryCombo );
    }

    @SuppressWarnings( "unchecked" )
    public void deleteDataElementCategoryCombo( DataElementCategoryCombo dataElementCategoryCombo )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( dataElementCategoryCombo );
    }

    public DataElementCategoryCombo getDataElementCategoryCombo( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (DataElementCategoryCombo) session.get( DataElementCategoryCombo.class, id );
    }

    public DataElementCategoryCombo getDataElementCategoryComboByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementCategoryCombo.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (DataElementCategoryCombo) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElementCategoryCombo> getAllDataElementCategoryCombos()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementCategoryCombo.class );

        return criteria.list();
    }
}
