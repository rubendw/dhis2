package org.hisp.dhis.source.hibernate;

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

import org.hibernate.Session;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: HibernateSourceStore.java 2919 2007-02-26 18:56:19Z margrsto $
 */
public class HibernateSourceStore
    implements SourceStore
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
    // SourceStore implementation
    // -------------------------------------------------------------------------

    public <S extends Source> int addSource( S source )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( source );
    }

    public <S extends Source> void updateSource( S source )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( source );
    }

    public <S extends Source> void deleteSource( S source )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( source );
    }

    @SuppressWarnings( "unchecked" )
    public <S extends Source> S getSource( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (S) session.get( Source.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public <S extends Source> Collection<S> getAllSources( Class<S> clazz )
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( clazz ).list();
    }

    @SuppressWarnings( "unchecked" )
    public <S extends Source> Collection<S> getAllSources()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( Source.class ).list();
    }
}
