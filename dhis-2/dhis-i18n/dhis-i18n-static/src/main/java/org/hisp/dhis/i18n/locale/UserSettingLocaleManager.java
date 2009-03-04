package org.hisp.dhis.i18n.locale;

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
import java.util.List;
import java.util.Locale;

import org.hisp.dhis.user.NoCurrentUserException;
import org.hisp.dhis.user.UserSettingService;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: UserSettingLocaleManager.java 4023 2007-11-15 18:11:14Z torgeilo $
 */
public class UserSettingLocaleManager
    implements LocaleManager
{
    private String userSettingKey;

    public void setUserSettingKey( String userSettingKey )
    {
        this.userSettingKey = userSettingKey;
    }

    private Locale defaultLocale;

    public void setDefaultLocale( Locale defaultLocale )
    {
        this.defaultLocale = defaultLocale;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }

    // -------------------------------------------------------------------------
    // LocaleManager implementation
    // -------------------------------------------------------------------------

    public Locale getCurrentLocale()
        throws LocaleManagerException
    {
        Locale locale = getUserSelectedLocale();

        if ( locale != null )
        {
            return locale;
        }

        if ( defaultLocale != null )
        {
            return defaultLocale;
        }

        return DHIS_STANDARD_LOCALE;
    }

    public void setCurrentLocale( Locale locale )
        throws LocaleManagerException
    {
        try
        {
            userSettingService.saveUserSetting( userSettingKey, locale );
        }
        catch ( NoCurrentUserException e )
        {
            // Just ignore for now. No change compared to previous behaviour.
        }
    }

    public List<Locale> getLocalesOrderedByPriority()
        throws LocaleManagerException
    {
        List<Locale> locales = new ArrayList<Locale>();

        Locale userLocale = getUserSelectedLocale();

        if ( userLocale != null )
        {
            locales.add( userLocale );
        }

        locales.add( DHIS_STANDARD_LOCALE );

        return locales;
    }

    private Locale getUserSelectedLocale()
        throws LocaleManagerException
    {
        return (Locale) userSettingService.getUserSetting( userSettingKey, null );
    }

    public Locale getFallbackLocale()
    {
        return DHIS_STANDARD_LOCALE;
    }
}
