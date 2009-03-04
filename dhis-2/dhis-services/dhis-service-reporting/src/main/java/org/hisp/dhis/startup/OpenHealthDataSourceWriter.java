package org.hisp.dhis.startup;

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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.database.DatabaseInfo;
import org.hisp.dhis.system.database.DatabaseInfoProvider;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;
import org.hisp.dhis.system.util.StreamUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class OpenHealthDataSourceWriter
    extends AbstractStartupRoutine
{
    private static final Log log = LogFactory.getLog( OpenHealthDataSourceWriter.class );
    
    private static final String START_TAG = "<DataSourceInfo>";
    private static final String END_TAG = "</DataSourceInfo>";
    private static final String DATASOURCE_FOLDER = File.separator + "WEB-INF" + File.separator + "classes";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private String environmentVariable;

    public void setEnvironmentVariable( String environmentVariable )
    {
        this.environmentVariable = environmentVariable;
    }
    
    private String dataSourceFile;
        
    public void setDataSourceFile( String dataSourceFile )
    {
        this.dataSourceFile = dataSourceFile;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DatabaseInfoProvider databaseInfoProvider;
        
    public void setDatabaseInfoProvider( DatabaseInfoProvider databaseInfoProvider )
    {
        this.databaseInfoProvider = databaseInfoProvider;
    }

    // -------------------------------------------------------------------------
    // AbstractStartupRoutine implementation
    // -------------------------------------------------------------------------

    public void execute()
        throws Exception
    {
        String path = System.getenv( environmentVariable );
        
        if ( path == null )
        {
            log.info( "Environment variable " + environmentVariable + " not set" );
        }
        else
        {
            File file = new File( path + File.separator + DATASOURCE_FOLDER, dataSourceFile );
            
            if ( !file.exists() )
            {
                log.info( "File " + file.getAbsolutePath() + " does not exist" );
            }
            else
            {
                DatabaseInfo info = databaseInfoProvider.getDatabaseInfo();
                
                Map<String, String> replaceMap = new HashMap<String, String>();
                
                String value = START_TAG + 
                    "Provider=mondrian; " +
                    "Jdbc=" + xmlEncode( info.getUrl() ) + "; " +
                    "JdbcDrivers=" + info.getDriverClass() + "; " +
                    "JdbcUser=" + info.getUser() + "; " +
                    "JdbcPassword=" + info.getPassword() + "; " +
                    "PoolNeeded=true" + END_TAG;
                
                replaceMap.put( START_TAG, value );                

                StringBuffer in = StreamUtils.readContent( file, replaceMap );

                StreamUtils.writeContent( file, in );
                
                log.info( "Wrote OpenHealth datasource configuration file" );
            }
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String xmlEncode( String string )
    {
        string = string.replaceAll( "&", "&amp;" );
        string = string.replaceAll( "[<>{}]", "" );
        
        return string;
    }
}
