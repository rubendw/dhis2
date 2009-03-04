package org.hisp.dhis.importexport.dhis14.file.sqlmap;

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

import java.io.Reader;
import java.util.Properties;

import org.hisp.dhis.importexport.IbatisConfigurationManager;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * @author Lars Helge Overland
 * @version $Id: IbatisSqlMapClientProvider.java 5517 2008-08-04 14:59:27Z larshelg $
 */
public class IbatisSqlMapClientProvider
    implements SqlMapClientProvider
{
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private static final String KEY_CONNECTION_URL = "ibatis.connection.url";
    private static final String KEY_DATABASE_URL = "ibatis.connection.url.database";
    private static final String VALUE_CONNECTION_URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    
    private String configurationFile;

    public void setConfigurationFile( String configurationFile )
    {
        this.configurationFile = configurationFile;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IbatisConfigurationManager configurationManager;

    public void setConfigurationManager( IbatisConfigurationManager configurationManager )
    {
        this.configurationManager = configurationManager;
    }

    // -------------------------------------------------------------------------
    // SqlMapClientProvider implementation
    // -------------------------------------------------------------------------

    public SqlMapClient getSqlMapClient()
    {
        try
        {
            Reader reader = Resources.getResourceAsReader( configurationFile );
            
            SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient( reader, getDatabaseProperties() );
            
            return sqlMapClient;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create SqlMapClient", ex );
        }
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Properties getDatabaseProperties()
    {
        Properties properties = configurationManager.getPropertiesConfiguration();
            
        properties.setProperty( KEY_CONNECTION_URL, VALUE_CONNECTION_URL + 
            properties.getProperty( KEY_DATABASE_URL ) );

        return properties;
    }
}
