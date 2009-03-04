package org.hisp.dhis.dataset.hibernate;

/*
 * Copyright (c) 2004-2007, University of Oslo All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the HISP project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;

/**
 * @author Tri
 * @version $Id$
 */
public class HibernateSectionStore
    implements SectionStore
{
    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    public HibernateSessionManager getSessionManager()
    {
        return sessionManager;
    }

    public int addSection( Section section )
    {
        Session session = sessionManager.getCurrentSession();
        return (Integer) session.save( section );
    }

    public void deleteSection( Section section )
    {
        Session session = sessionManager.getCurrentSession();
        session.delete( section );
    }

    @SuppressWarnings("unchecked")
    public Collection<Section> getAllSections()
    {
        Session session = sessionManager.getCurrentSession();
        return session.createCriteria( Section.class ).list();
    }

    public Section getSection( int id )
    {
        Session session = sessionManager.getCurrentSession();
        return (Section) session.get( Section.class, new Integer( id ) );
    }

    public Section getSectionByName( String name )
    {
        Session session = sessionManager.getCurrentSession();
        Criteria criteria = session.createCriteria( Section.class );
        criteria.add( Restrictions.eq( "name", name ) );
        return (Section) criteria.uniqueResult();
    }

    public void updateSection( Section section )
    {
        Session session = sessionManager.getCurrentSession();
        session.update( section );
    }

    @SuppressWarnings("unchecked")
    public Collection<Section> getSectionByDataSet( DataSet dataSet )
    {
        Session session = sessionManager.getCurrentSession();        
        List<Section> sections = session.createQuery( "from Section as sec where sec.dataSet = ?" ).setEntity( 0,
            dataSet ).list();
        return sections;
    }
}
