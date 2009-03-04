package org.hisp.dhis.aggregation.batch.handler;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.builder.StatementBuilder;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.aggregation.batch.factory.IdentifierExtractorFactory;
import org.hisp.dhis.aggregation.batch.factory.StatementBuilderFactory;
import org.hisp.dhis.aggregation.batch.identifier.IdentifierExtractor;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractBatchHandler.java 5808 2008-10-03 15:18:48Z larshelg $
 */
public abstract class AbstractBatchHandler
    implements BatchHandler
{
    private static final Log log = LogFactory.getLog( AbstractBatchHandler.class );
    
    private JDBCConfiguration configuration;
    
    private Connection connection;
    
    private Statement statement;
    
    protected StatementBuilder statementBuilder;
    
    private IdentifierExtractor identifierExtractor;
    
    protected StringBuffer sqlBuffer;
    
    private final int maxLength = 200000;
    
    private int statementCount = 0;
    
    private Collection<Integer> identifiers;

    protected boolean hasSinglePrimaryKey = true; // Should be overriden by subclasses
    
    protected String tableName;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    @SuppressWarnings( "unused" )
    private AbstractBatchHandler()
    {   
    }
    
    protected AbstractBatchHandler( JDBCConfiguration configuration )
    {
        this.configuration = configuration;
        this.statementBuilder = StatementBuilderFactory.createStatementBuilder( configuration.getDialect() );
        this.identifierExtractor = IdentifierExtractorFactory.createIdentifierExtractor( configuration.getDialect() );
        
        setTableName();
    }

    // -------------------------------------------------------------------------
    // BatchHandler implementation
    // -------------------------------------------------------------------------
    
    public void init()
    {
        try
        {
            Class.forName( configuration.getDriverClass() );
            
            connection = DriverManager.getConnection( 
                configuration.getConnectionUrl(),
                configuration.getUsername(),
                configuration.getPassword() );
            
            statement = connection.createStatement();
            
            sqlBuffer = new StringBuffer( maxLength );
            
            statementCount = 0;
            
            identifiers = new ArrayList<Integer>();
            
            openSqlStatement();
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create statement", ex );
        }
    }
    
    public final int insertObject( Object object, boolean returnGeneratedIdentifier )
    {
        try
        {
            sqlBuffer = new StringBuffer();
            
            statementCount = 0;
            
            openSqlStatement();
            
            addSqlStatement( object );
            
            sqlBuffer.deleteCharAt( sqlBuffer.length() - 1 ); 
            
            statement.executeUpdate( sqlBuffer.toString() );
            
            return returnGeneratedIdentifier ? identifierExtractor.extract( statement ) : 0;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to insert " + object.getClass().getName(), ex );
        }
    }
    
    public final void addObject( Object object )
    {
        addSqlStatement( object );
        
        statementCount++;
        
        if ( sqlBuffer.length() >= maxLength )
        {
            try
            {   
                sqlBuffer.deleteCharAt( sqlBuffer.length() - 1 );
                
                statement.executeUpdate( sqlBuffer.toString() );
                
                if ( hasSinglePrimaryKey )
                {
                    identifiers.addAll( identifierExtractor.extract( statement, statementCount ) );
                }
                
                sqlBuffer = new StringBuffer();
                
                statementCount = 0;
                
                openSqlStatement();
            }
            catch ( SQLException ex )
            {
                log.debug( "SQL statement leading to exception: '" + sqlBuffer.toString() + "'" );
                
                throw new RuntimeException( "Failed to add objects", ex );
            }
        }
    }
    
    public final void updateObject( Object object )
    {
        try
        {
            String sql = getUpdateSqlStatement( object );
            
            statement.executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to update object", ex );
        }
    }

    public final boolean objectExists( Object objectName )
    {
        try
        {
            String sql = getUniquenessStatement( objectName );
            
            ResultSet resultSet = statement.executeQuery( sql );   
            
            return resultSet.next();
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to check uniqueness of object", ex );            
        }
    }
    
    public int getObjectIdentifier( Object objectName )
    {
        try
        {
            String sql = getIdentifierStatement( objectName );
            
            ResultSet resultSet = statement.executeQuery( sql );  
            
            return resultSet.next() ? resultSet.getInt( 1 ) : 0;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get object identifier", ex );            
        }        
    }
    
    public final Collection<Integer> flush()
    {
        try
        {
            if ( sqlBuffer.length() > 2 && statementCount != 0 )
            {
                sqlBuffer.deleteCharAt( sqlBuffer.length() - 1 );
                
                statement.executeUpdate( sqlBuffer.toString() );
                
                if ( hasSinglePrimaryKey )
                {
                    identifiers.addAll( identifierExtractor.extract( statement, statementCount ) );
                }
                
                statementCount = 0;
            }
            
            return identifiers;
        }
        catch ( SQLException ex )
        {
            log.debug( "SQL statement leading to exception: '" + sqlBuffer.toString() + "'" );
            
            throw new RuntimeException( "Failed to flush BatchHandler", ex );
        }
        finally
        {
            if ( statement != null )
            {
                try
                {
                    statement.close();
                }
                catch ( SQLException statementEx )
                {
                    statementEx.printStackTrace();
                }
            }
            
            if ( connection != null )
            {
                try
                {
                    connection.close();
                }
                catch ( SQLException connectionEx )
                {
                    connectionEx.printStackTrace();
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void addSqlStatement( Object object )
    {
        addValues( object );
        
        sqlBuffer.append( statementBuilder.getInsertStatementValues() );
    }

    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }
    
    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract void setTableName();
    
    protected abstract void openSqlStatement();
    
    protected abstract String getUpdateSqlStatement( Object object );
    
    protected abstract String getIdentifierStatement( Object objectName );
    
    protected abstract String getUniquenessStatement( Object object );
    
    protected abstract void addColumns();
    
    protected abstract void addValues( Object object );
}
