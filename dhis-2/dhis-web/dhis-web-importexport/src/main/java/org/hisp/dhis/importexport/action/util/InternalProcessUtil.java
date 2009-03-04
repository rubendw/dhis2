package org.hisp.dhis.importexport.action.util;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.util.SessionUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class InternalProcessUtil
{
    private static final Log log = LogFactory.getLog( InternalProcessUtil.class );
    
    private static final String DEFAULT_IMPORT_FORMAT = "DXF";
    
    public static final String TYPE_IMPORT = "Import";
    public static final String TYPE_PREVIEW = "Preview";
    
    private static final String KEY_PROCESS_IDENTIFIER = "CurrentRunningImportInternalProcessIdentifier";
    private static final String KEY_PROCESS_TYPE = "CurrentRunningImportInternalProcessType";
    private static final String KEY_PROCESS_IMPORT_FORMAT = "CurrentRunningImportInternalProcessImportFormat";
    private static final String KEY_PROCESS_IMPORT_FILENAME = "CurrentRunningImportFileName";
    
    // -----------------------------------------------------------------------
    // InternalProcess identifier
    // -----------------------------------------------------------------------

    public static String getCurrentRunningProcess()
    {
        return String.valueOf( SessionUtils.getSessionVar( KEY_PROCESS_IDENTIFIER ) );
    }

    public static void setCurrentRunningProcess( String id )
    {
        SessionUtils.setSessionVar( KEY_PROCESS_IDENTIFIER, id );
    }
    
    public static boolean processIsRunning()
    {
        return SessionUtils.containsSessionVar( KEY_PROCESS_IDENTIFIER );
    }
    
    public static void removeCurrentRunningProcess()
    {
        SessionUtils.removeSessionVar( KEY_PROCESS_IDENTIFIER );
    }

    // -----------------------------------------------------------------------
    // InternalProcess type
    // -----------------------------------------------------------------------

    public static void setCurrentRunningProcessType( String type )
    {
        SessionUtils.setSessionVar( KEY_PROCESS_TYPE, type );
    }
    
    public static String getCurrentRunningProcessType()
    {
        return String.valueOf( SessionUtils.getSessionVar( KEY_PROCESS_TYPE ) );
    }

    // -----------------------------------------------------------------------
    // InternalProcess import format
    // -----------------------------------------------------------------------

    public static void setCurrentRunningProcessImportFormat( String importFormat )
    {
        SessionUtils.setSessionVar( KEY_PROCESS_IMPORT_FORMAT, importFormat );
    }
    
    public static String getCurrentRunningProcessImportFormat()
    {
        String importFormat = String.valueOf( SessionUtils.getSessionVar( KEY_PROCESS_IMPORT_FORMAT ) );
        
        if ( importFormat == null )
        {
            log.warn( "Import format not set, using default" );
            
            return DEFAULT_IMPORT_FORMAT;
        }
        
        return importFormat;
    }

    // -----------------------------------------------------------------------
    // InternalProcess import file name
    // -----------------------------------------------------------------------

    public static void setCurrentImportFileName( String fileName )
    {
        SessionUtils.setSessionVar( KEY_PROCESS_IMPORT_FILENAME, fileName );
    }
    
    public static String getCurrentImportFileName()
    {
        return String.valueOf( SessionUtils.getSessionVar( KEY_PROCESS_IMPORT_FILENAME ) );
    }
}
