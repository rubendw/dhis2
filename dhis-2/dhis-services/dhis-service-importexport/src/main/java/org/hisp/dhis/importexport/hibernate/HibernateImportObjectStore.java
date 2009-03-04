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
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObject;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.importexport.ImportObjectStore;

/**
 * @author Lars Helge Overland
 * @version $Id: HibernateImportObjectStore.java 5793 2008-10-02 14:14:00Z larshelg $
 */
public class HibernateImportObjectStore
    implements ImportObjectStore
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
    // ImportObjectStore implementation
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // ImportObject
    // ----------------------------------------------------------------------

    public int addImportObject( ImportObject importObject )
    {
        Session session = sessionManager.getCurrentSession();
        
        return (Integer) session.save( importObject );
    }
    
    public void updateImportObject( ImportObject importObject )
    {
        Session session = sessionManager.getCurrentSession();
        
        session.saveOrUpdate( importObject );
    }
    
    public ImportObject getImportObject( int id )
    {
        Session session = sessionManager.getCurrentSession();
                
        return (ImportObject) session.get( ImportObject.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ImportObject> getImportObjects( Class<?> clazz )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( ImportObject.class );
        
        criteria.add( Restrictions.eq( "className", clazz.getName() ) );
        
        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<ImportObject> getImportObjects( ImportObjectStatus status, Class<?> clazz )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( ImportObject.class );
        
        criteria.add( Restrictions.eq( "status", status ) );
        criteria.add( Restrictions.eq( "className", clazz.getName() ) );
        
        return criteria.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<ImportObject> getImportObjects( GroupMemberType groupMemberType )
    {
        Session session = sessionManager.getCurrentSession();
        
        Criteria criteria = session.createCriteria( ImportObject.class );
        
        criteria.add( Restrictions.eq( "groupMemberType", groupMemberType ) );
        
        return criteria.list();
    }
    
    public void deleteImportObject( ImportObject importObject )
    {
        Session session = sessionManager.getCurrentSession();
        
        session.delete( importObject );
    }
    
    @SuppressWarnings( "unchecked" )
    public void deleteImportObjects( Class<?> clazz )
    {
        Session session = sessionManager.getCurrentSession();
        
        String hql = "from ImportObject where className = :className";
        
        Query query = session.createQuery( hql );
        
        query.setString( "className", clazz.getName() );
        
        Collection<ImportObject> importObjects = query.list();
        
        for ( ImportObject importObject : importObjects )
        {
            session.evict( importObject );
        }
        
        hql = "delete " + hql;
        
        query = session.createQuery( hql );
        
        query.setString( "className", clazz.getName() );
        
        query.executeUpdate();
    }

    @SuppressWarnings( "unchecked" )
    public void deleteImportObjects( GroupMemberType groupMemberType )
    {
        Session session = sessionManager.getCurrentSession();

        String hql = "from ImportObject where groupMemberType = :groupMemberType";
        
        Query query = session.createQuery( hql );
        
        query.setParameter( "groupMemberType", groupMemberType );

        Collection<ImportObject> importObjects = query.list();
        
        for ( ImportObject importObject : importObjects )
        {
            session.evict( importObject );
        }
        
        hql = "delete " + hql;
        
        query = session.createQuery( hql );
        
        query.setParameter( "groupMemberType", groupMemberType );
        
        query.executeUpdate();
    }
    
    @SuppressWarnings( "unchecked" )
    public void deleteImportObjects()
    {
        Session session = sessionManager.getCurrentSession();

        String hql = "from ImportObject";
        
        Collection<ImportObject> importObjects = session.createQuery( hql ).list();

        for ( ImportObject importObject : importObjects )
        {
            session.evict( importObject );
        }
        
        hql = "delete " + hql;
        
        session.createQuery( hql ).executeUpdate();
    }
}
