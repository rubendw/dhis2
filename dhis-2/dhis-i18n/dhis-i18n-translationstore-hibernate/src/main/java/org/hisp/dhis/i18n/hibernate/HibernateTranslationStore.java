package org.hisp.dhis.i18n.hibernate;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.i18n.Translation;
import org.hisp.dhis.i18n.TranslationStore;

/**
 * @author Oyvind Brucker
 */
public class HibernateTranslationStore
    implements TranslationStore
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
    // Translation
    // -------------------------------------------------------------------------

    public void addTranslation( Translation translation )
    {
        Session session = sessionManager.getCurrentSession();

        session.save( translation );
    }

    public void updateTranslation( Translation translation )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( translation );
    }

    public Translation getTranslation( String className, int id, Locale locale, String property )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Translation.class );

        criteria.add( Restrictions.eq( "className", className ) );
        criteria.add( Restrictions.eq( "id", id ) );
        criteria.add( Restrictions.eq( "locale", locale.toString() ) );
        criteria.add( Restrictions.eq( "property", property ) );

        return (Translation) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Translation> getTranslations( String className, int id, Locale locale )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Translation.class );

        criteria.add( Restrictions.eq( "className", className ) );
        criteria.add( Restrictions.eq( "id", id ) );
        criteria.add( Restrictions.eq( "locale", locale.toString() ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Translation> getTranslations( String className, Locale locale )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Translation.class );

        criteria.add( Restrictions.eq( "className", className ) );
        criteria.add( Restrictions.eq( "locale", locale.toString() ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Translation> getAllTranslations()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( Translation.class );

        return criteria.list();
    }

    public void deleteTranslation( Translation translation )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( translation );
    }

    @SuppressWarnings( "unchecked" )
    public void deleteTranslations( String className, int id )
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "from Translation t where t.className = :className and t.id = :id" );

        query.setString( "className", className );
        query.setInteger( "id", id );

        List<Object> objlist = query.list();

        for ( Object object : objlist )
        {
            session.delete( object );
        }
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Locale> getAvailableLocales()
    {
        Session session = sessionManager.getCurrentSession();

        List<Object> objlist = session.createQuery( "select distinct translation.locale from Translation translation" )
            .list();

        Collection<Locale> locales = new ArrayList<Locale>();

        for ( Object object : objlist )
        {
            Locale locale = getLocale( object.toString() );

            locales.add( locale );
        }

        return locales;
    }

    /**
     * Creates a Locale object based on the input String
     *
     * @param localestr String to parse
     * @return A locale object or null if not valid
     */
    private Locale getLocale( String localestr )
    {
        String[] parts = localestr.split( "_" );

        Locale thisLocale;

        if ( parts.length == 1 )
        {
            thisLocale = new Locale( parts[0] );
        }
        else if ( parts.length == 2 )
        {
            thisLocale = new Locale( parts[0], parts[1] );
        }
        else if ( parts.length == 3 )
        {
            thisLocale = new Locale( parts[0], parts[1], parts[2] );
        }
        else
        {
            return null;
        }

        return thisLocale;
    }
}
