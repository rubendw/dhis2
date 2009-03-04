package org.hisp.dhis.settings.action.user;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.hisp.dhis.i18n.locale.LocaleManager;
import org.hisp.dhis.i18n.resourcebundle.ResourceBundleManager;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: GetAvailableLocalesAction.java 3264 2007-04-25 14:54:05Z stianast $
 */
public class GetAvailableLocalesAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ResourceBundleManager resourceBundleManager;

    public void setResourceBundleManager( ResourceBundleManager resourceBundleManager )
    {
        this.resourceBundleManager = resourceBundleManager;
    }

    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<Locale> availableLocales;

    public List<Locale> getAvailableLocales()
    {
        return availableLocales;
    }

    private Locale currentLocale;

    public Locale getCurrentLocale()
    {
        return currentLocale;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        availableLocales = new ArrayList<Locale>( resourceBundleManager.getAvailableLocales() );

        Collections.sort( availableLocales, new Comparator<Locale>() {
            public int compare( Locale locale0, Locale locale1 )
            {
                return locale0.getDisplayName().compareTo( locale1.getDisplayName() );
            }
        } );

        currentLocale = localeManager.getCurrentLocale();

        return SUCCESS;
    }
}
