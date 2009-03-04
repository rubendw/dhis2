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

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.i18n.util.LocaleUtils;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;

/**
 * @author Oyvind Brucker
 */
public class TranslateAction implements Action
{
    private String className;

    private String id;

    private String loc;

    private String returnUrl;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nService i18nService;

    public void setI18nService( I18nService i18nService )
    {
        this.i18nService = i18nService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    public void setClassName( String className )
    {
        this.className = className;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public void setLoc( String locale )
    {
        this.loc = locale;
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

    public String getId()
    {
        return id;
    }

    public String getLocale()
    {
        return loc;
    }

    public String getReturnUrl()
    {
        return returnUrl;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();

        Map<String, String> translations = new Hashtable<String, String>();

        List<String> propertyNames = i18nService.getPropertyNames( className );

        for ( String propertyName : propertyNames )
        {
            String[] translation = request.getParameterValues( propertyName );

            if ( translation != null && translation.length > 0 && translation[0].length() > 0 )
            {
                translations.put( propertyName, translation[0] );
            }
        }

        Locale thisLocale = LocaleUtils.getLocale( loc );

        if ( thisLocale != null && !loc.equals("heading"))
        {
            i18nService.updateTranslation( className, Integer.parseInt( id ), thisLocale, translations );
        }

        return SUCCESS;
    }
}
