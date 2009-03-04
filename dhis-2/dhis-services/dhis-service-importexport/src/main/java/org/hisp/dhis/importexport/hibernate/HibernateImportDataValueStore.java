package org.hisp.dhis.importexport.hibernate;

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
import org.hisp.dhis.importexport.ImportDataValue;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.importexport.ImportDataValueStore;
import org.hisp.dhis.importexport.ImportObjectStatus;

/**
 * @author Lars Helge Overland
 * @version $Id: HibernateImportDataValueStore.java 4663 2008-02-29 09:10:33Z larshelg $
 */
public class HibernateImportDataValueStore
    implements ImportDataValueStore
{
    // ----------------------------------------------------------------------
    // Dependencies
    // ----------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    // ----------------------------------------------------------------------
    // ImportDataValueStore implementation
    // ----------------------------------------------------------------------

    public void addImportDataValue( ImportDataValue importDataValue )
    {
        Session session = sessionManager.getCurrentSession();
        
        session.save( importDataValue );
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<ImportDataValue> getImportDataValues( ImportObjectStatus status )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( ImportDataValue.class );
        
        criteria.add( Restrictions.eq( "status", status.name() ) );
        
        return criteria.list();
    }

    public void deleteImportDataValues()
    {
        Session session = sessionManager.getCurrentSession();
        
        String hql = "DELETE FROM ImportDataValue";
        
        session.createQuery( hql ).executeUpdate();
    }
    
    public void deleteImportDataValuesByDataElement( int dataElementId )
    {        
        Session session = sessionManager.getCurrentSession();
        
        String hql = "DELETE FROM ImportDataValue WHERE dataElementId = :dataElementId";
        
        Query query = session.createQuery( hql );
        
        query.setInteger( "dataElementId", dataElementId );
        
        query.executeUpdate();
    }
    
    public void deleteImportDataValuesBySource( int sourceId )
    {
        Session session = sessionManager.getCurrentSession();
        
        String hql = "DELETE FROM ImportDataValue WHERE sourceId = :sourceId";
        
        Query query = session.createQuery( hql );
        
        query.setInteger( "sourceId", sourceId );
        
        query.executeUpdate();
    }
}
