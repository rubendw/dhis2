package org.hisp.dhis.aggregation.batch.statement;

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

import java.sql.Connection;
import java.sql.DriverManager;

import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfigurationProvider;

/**
 * @author Lars Helge Overland
 * @version $Id: JDBCStatementManager.java 5714 2008-09-17 13:05:36Z larshelg $
 */
public class JDBCStatementManager
    implements StatementManager
{
    private ThreadLocal<StatementHolder> holderTag = new ThreadLocal<StatementHolder>();
    private ThreadLocal<StatementHolder> internalHolderTag = new ThreadLocal<StatementHolder>();
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JDBCConfigurationProvider configurationProvider;

    public void setConfigurationProvider( JDBCConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }
    
    // -------------------------------------------------------------------------
    // StatementManager implementation
    // -------------------------------------------------------------------------

    public void initialise()
    {
        Connection connection = getConnection();

        StatementHolder holder = new StatementHolder( connection, true );
        
        holderTag.set( holder );
    }
    
    public void initialiseInternal()
    {
        Connection connection = getInternalConnection();

        StatementHolder holder = new StatementHolder( connection, true );
        
        internalHolderTag.set( holder );
    }

    public StatementHolder getHolder()
    {
        StatementHolder holder = holderTag.get();
        
        if ( holder != null )
        {
            return holder;
        }
        
        return new StatementHolder( getConnection(), false );        
    }
    
    public StatementHolder getInternalHolder()
    {
        StatementHolder holder = internalHolderTag.get();
        
        if ( holder != null )
        {
            return holder;
        }
        
        return new StatementHolder( getInternalConnection(), false );
    }

    public void destroy()
    {
        StatementHolder holder = holderTag.get();
        
        if ( holder != null )
        {
            holder.close();
        
            holderTag.remove();
        }
    }
    
    public void destroyInternal()
    {
        StatementHolder holder = internalHolderTag.get();
        
        if ( holder != null )
        {
            holder.close();
        
            internalHolderTag.remove();
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Connection getConnection()
    {
        try
        {
            JDBCConfiguration configuration = configurationProvider.getConfiguration();
            
            Class.forName( configuration.getDriverClass() );
            
            Connection connection = DriverManager.getConnection( 
                configuration.getConnectionUrl(),
                configuration.getUsername(),
                configuration.getPassword() );
            
            return connection;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create connection", ex );
        }
    }

    private Connection getInternalConnection()
    {
        try
        {
            JDBCConfiguration configuration = configurationProvider.getInternalConfiguration();
            
            Class.forName( configuration.getDriverClass() );
            
            Connection connection = DriverManager.getConnection( 
                configuration.getConnectionUrl(),
                configuration.getUsername(),
                configuration.getPassword() );
            
            return connection;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create internall connection", ex );
        }
    }
}
