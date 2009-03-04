package org.hisp.dhis.aggregation.batch.configuration;

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

import org.hibernate.cfg.Configuration;
import org.hisp.dhis.aggregation.batch.factory.StatementDialect;
import org.hisp.dhis.hibernate.HibernateConfigurationProvider;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultJDBCConfigurationProvider.java 5714 2008-09-17 13:05:36Z larshelg $
 */
public class DefaultJDBCConfigurationProvider
    implements JDBCConfigurationProvider
{
    private static final String KEY_DIALECT = "hibernate.dialect";
    private static final String DIALECT_MYSQL = "org.hibernate.dialect.MySQLDialect";
    private static final String DIALECT_POSTGRESQL = "org.hibernate.dialect.PostgreSQLDialect";
    private static final String DIALECT_H2 = "org.hibernate.dialect.H2Dialect";
    
    private static final String KEY_DRIVER = "hibernate.connection.driver_class";
    private static final String KEY_CONNECTION_URL = "hibernate.connection.url";
    private static final String KEY_USERNAME = "hibernate.connection.username";
    private static final String KEY_PASSWORD = "hibernate.connection.password";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private HibernateConfigurationProvider configurationProvider;
    
    public void setConfigurationProvider( HibernateConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }
    
    // -------------------------------------------------------------------------
    // JDBCConfigurationProvider implementation
    // -------------------------------------------------------------------------

    public JDBCConfiguration getConfiguration()
    {
        Configuration hibernateConfiguration = configurationProvider.getConfiguration();
        
        JDBCConfiguration config = new JDBCConfiguration();
        
        if ( hibernateConfiguration.getProperty( KEY_DIALECT ).equals( DIALECT_MYSQL ) )
        {
            config.setDialect( StatementDialect.MYSQL );
        }
        else if ( hibernateConfiguration.getProperty( KEY_DIALECT ).equals( DIALECT_POSTGRESQL ) )
        {
            config.setDialect( StatementDialect.POSTGRESQL );
        }
        else if ( hibernateConfiguration.getProperty( KEY_DIALECT ).equals( DIALECT_H2 ) )
        {
            config.setDialect( StatementDialect.H2 );
        }
        
        config.setDriverClass( hibernateConfiguration.getProperty( KEY_DRIVER ) );            
        config.setConnectionUrl( hibernateConfiguration.getProperty( KEY_CONNECTION_URL ) );
        config.setUsername( hibernateConfiguration.getProperty( KEY_USERNAME ) );
        config.setPassword( hibernateConfiguration.getProperty( KEY_PASSWORD ) );
        
        return config;
    }
    
    public JDBCConfiguration getInternalConfiguration()
    {
        JDBCConfiguration config = new JDBCConfiguration();
        
        config.setDialect( StatementDialect.H2 );
        config.setDriverClass( "org.h2.Driver" );
        config.setConnectionUrl( "jdbc:h2:mem:dhis2internal;DB_CLOSE_DELAY=-1" );
        config.setUsername( "sa" );
        config.setPassword( "" );
        
        return config;
    }
}
