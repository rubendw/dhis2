package org.hisp.dhis.validation.hibernate;

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
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleGroup;
import org.hisp.dhis.validation.ValidationRuleStore;

/**
 * @author Margrethe Store
 * @version $Id: HibernateValidationRuleStore.java 3676 2007-10-22 17:30:12Z larshelg $
 */
public class HibernateValidationRuleStore
    implements ValidationRuleStore
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
    // ValidationRule
    // -------------------------------------------------------------------------

    public int addValidationRule( ValidationRule validationRule )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( validationRule );
    }

    public void deleteValidationRule( ValidationRule validationRule )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( validationRule );
    }

    public void updateValidationRule( ValidationRule validationRule )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( validationRule );
    }
    
    public ValidationRule getValidationRule( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (ValidationRule) session.get( ValidationRule.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ValidationRule> getAllValidationRules()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( ValidationRule.class ).list();
    }

    public ValidationRule getValidationRuleByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( ValidationRule.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (ValidationRule) criteria.uniqueResult();
    }

    // -------------------------------------------------------------------------
    // ValidationRuleGroup
    // -------------------------------------------------------------------------
    
    public int addValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( validationRuleGroup );
    }

    public void deleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.delete( validationRuleGroup );
    }

    public void updateValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        Session session = sessionManager.getCurrentSession();

        session.update( validationRuleGroup );
    }
    
    public ValidationRuleGroup getValidationRuleGroup( int id )
    {
        Session session = sessionManager.getCurrentSession();

        return (ValidationRuleGroup) session.get( ValidationRuleGroup.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ValidationRuleGroup> getAllValidationRuleGroups()
    {
        Session session = sessionManager.getCurrentSession();

        return session.createCriteria( ValidationRuleGroup.class ).list();
    }

    public ValidationRuleGroup getValidationRuleGroupByName( String name )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( ValidationRuleGroup.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (ValidationRuleGroup) criteria.uniqueResult();
    }
}
