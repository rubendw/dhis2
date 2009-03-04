package org.hisp.dhis.datamart.hibernate;

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
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.datamart.DataMartExport;
import org.hisp.dhis.datamart.DataMartExportStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class HibernateDataMartExportStore
    implements DataMartExportStore
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
    // DataMartExportStore implementation
    // -------------------------------------------------------------------------

    public void saveDataMartExport( DataMartExport export )
    {
        sessionManager.getCurrentSession().saveOrUpdate( export );
    }

    public DataMartExport getDataMartExport( int id )
    {
        return (DataMartExport) sessionManager.getCurrentSession().get( DataMartExport.class, new Integer( id ) );
    }
    
    public void deleteDataMartExport( DataMartExport export )
    {
        sessionManager.getCurrentSession().delete( export );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataMartExport> getAllDataMartExports()
    {
        return sessionManager.getCurrentSession().createCriteria( DataMartExport.class ).list();
    }
    
    public DataMartExport getDataMartExportByName( String name )
    {
        Criteria criteria = sessionManager.getCurrentSession().createCriteria( DataMartExport.class );
        
        criteria.add( Restrictions.eq( "name", name ) );
        
        return (DataMartExport) criteria.uniqueResult();
    }
}
