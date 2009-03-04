package org.hisp.dhis.options.datadictionary;

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

import org.hisp.dhis.user.NoCurrentUserException;
import org.hisp.dhis.user.UserSettingService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultDataDictionaryModeManager
    implements DataDictionaryModeManager
{
    private static final String SETTING_NAME_DATADICTIONARY_MODE = "currentDataDictionaryMode";
    
    private static final String SETTING_NAME_DATADICTIONARY = "currentDataDictionary";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }

    // -------------------------------------------------------------------------
    // DataDictionaryModeManager implementation
    // -------------------------------------------------------------------------

    public void setCurrentDataDictionaryMode( String dataDictionaryMode )
    {
        try
        {
            userSettingService.saveUserSetting( SETTING_NAME_DATADICTIONARY_MODE, dataDictionaryMode );
        }
        catch ( NoCurrentUserException e )
        {   
        }
    }
    
    public String getCurrentDataDictionaryMode()
    {
        return (String) userSettingService.getUserSetting( SETTING_NAME_DATADICTIONARY_MODE, DATADICTIONARY_MODE_REGULAR );
    }
    
    public List<String> getDataDictionaryModes()
    {
        List<String> settings = new ArrayList<String>();
        
        settings.add( DATADICTIONARY_MODE_REGULAR );
        settings.add( DATADICTIONARY_MODE_EXTENDED );
        
        return settings;
    }
    
    public void setCurrentDataDictionary( Integer id )
    {
        try
        {
            userSettingService.saveUserSetting( SETTING_NAME_DATADICTIONARY, id );
        }
        catch ( NoCurrentUserException e )
        {   
        }
    }
    
    public Integer getCurrentDataDictionary()
    {
        return (Integer) userSettingService.getUserSetting( SETTING_NAME_DATADICTIONARY, -1 );
    }
}
