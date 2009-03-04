package org.hisp.dhis.i18n.action;

/*
 * Copyright (c) 2004-2005, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may
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
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.i18n.locale.LocaleManager;
import org.hisp.dhis.i18n.locale.LocaleManagerException;

import com.opensymphony.xwork.Action;

/**
 * @author Oyvind Brucker
 */
public class I18nAction implements Action
{
    private String className;

    private String objectId;

    private String returnUrl;

    private Map<String, String> translations = new Hashtable<String, String>();

    private Map<String, String> referenceTranslations = new Hashtable<String, String>();

    private Map<String, String> propertyLabels = new Hashtable<String, String>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nService i18nService;

    public void setI18nService( I18nService i18nService )
    {
        this.i18nService = i18nService;
    }

    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    public void setClassName( String className )
    {
        this.className = className;
    }

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }

    public void setReturnUrl( String returnUrl )
    {
        this.returnUrl = returnUrl;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    public String getClassName()
    {
        return className;
    }

    public String getObjectId()
    {
        return objectId;
    }


    public String getReturnUrl()
    {
        return returnUrl;
    }

    public Collection<Locale> getAvailableLocales()
        throws Exception
    {
        return i18nService.getAvailableLocales();
    }

    public List<String> getPropertyNames()
    {
        return i18nService.getPropertyNames( className );
    }

    public Map<String, String> getPropertyNamesLabel()
    {
        return propertyLabels;
    }

    public Map<String, String> getReferenceTranslations()
    {
        return referenceTranslations;
    }

    public Map<String, String> getTranslations()
    {
        return translations;
    }

    public Locale getCurrentLocale()
    {
        try
        {
            return localeManager.getCurrentLocale();
        }
        catch ( LocaleManagerException e )
        {
            return localeManager.getFallbackLocale();
        }
    }

    public Locale getCurrentRefLocale()
    {
        return localeManager.getFallbackLocale();
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        propertyLabels = i18nService.getPropertyNamesLabel( className );

        translations = i18nService.getTranslations( className, Integer.parseInt( objectId ), getCurrentLocale() );

        referenceTranslations =
            i18nService.getTranslations( className, Integer.parseInt( objectId ), getCurrentRefLocale() );

        /**
         * Fill in empty strings for null values
         */

        for ( String property : getPropertyNames() )
        {
            if ( translations.get( property ) == null )
            {
                translations.put( property, "" );
            }
            if ( referenceTranslations.get( property ) == null )
            {
                referenceTranslations.put( property, "" );
            }
        }
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------
}
