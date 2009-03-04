package org.hisp.dhis.datamart.startup;

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

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.builder.StatementBuilder;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfigurationProvider;
import org.hisp.dhis.aggregation.batch.factory.StatementBuilderFactory;
import org.hisp.dhis.aggregation.batch.statement.StatementHolder;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;

/**
 * @author Lars Helge Overland
 * @version $Id: AggregationTableCreator.java 5610 2008-08-28 16:16:07Z larshelg $
 */
public class AggregationTableCreator
    extends AbstractStartupRoutine
{
    private Log log = LogFactory.getLog( AggregationTableCreator.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    private JDBCConfigurationProvider configurationProvider;

    public void setConfigurationProvider( JDBCConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }

    // -------------------------------------------------------------------------
    // StartupRoutine implementation
    // -------------------------------------------------------------------------

    public void execute()
    {
        StatementHolder holder = statementManager.getHolder(); 
        
        JDBCConfiguration configuration = configurationProvider.getConfiguration();
        
        StatementBuilder builder = StatementBuilderFactory.createStatementBuilder( configuration.getDialect() );
        
        try
        {
            // -----------------------------------------------------------------
            // Aggregated data value table
            // -----------------------------------------------------------------
            
            try
            {                
                holder.getStatement().executeUpdate( builder.getCreateAggregatedDataValueTable() );
                
                log.info( "Created table aggregateddatavalue" );
            }
            catch ( SQLException ex )
            {
                log.info( "Table aggregateddatavalue exists" );
            }
    
            // -----------------------------------------------------------------
            // Aggregated indicator value table
            // -----------------------------------------------------------------
    
            try
            {
                holder.getStatement().executeUpdate( builder.getCreateAggregatedIndicatorTable() );
                
                log.info( "Created table aggregatedindicatorvalue" );
            }
            catch ( SQLException ex )
            {
                log.info( "Table aggregatedindicatorvalue exists" );
            }
            
            // -----------------------------------------------------------------
            // Crosstab index on datavalue table
            // -----------------------------------------------------------------
    
            try
            {
                holder.getStatement().executeUpdate( builder.getCreateDataValueIndex() );
                
                log.info( "Created index crosstab on table datavalue" );
            }
            catch ( SQLException ex )
            {
                log.info( "Index crosstab exists on table datavalue" );
            }
            
            // -----------------------------------------------------------------
            // DataSetCompleteness
            // -----------------------------------------------------------------
    
            try
            {
                holder.getStatement().executeUpdate( builder.getCreateDataSetCompletenessTable() );
                
                log.info( "Created table aggregateddatasetcompleteness" );
            }
            catch ( SQLException ex )
            {
                log.info( "Table aggregateddatasetcompleteness exists" );
            }
        }
        finally
        {        
            holder.close();
        }
    }
}
