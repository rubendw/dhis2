package org.hisp.dhis.completeness.jdbc;

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

import static org.hisp.dhis.system.util.TextUtils.getCommaDelimitedString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.hisp.dhis.aggregation.batch.statement.JDBCStatementManager;
import org.hisp.dhis.aggregation.batch.statement.StatementHolder;
import org.hisp.dhis.completeness.DataSetCompletenessStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class JDBCDataSetCompletenessStore
    implements DataSetCompletenessStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JDBCStatementManager statementManager;

    public void setStatementManager( JDBCStatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    // -------------------------------------------------------------------------
    // DataSetCompletenessStore
    // -------------------------------------------------------------------------

    public double getPercentage( int dataSetId, int periodId, int organisationUnitId )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "SELECT value " +
                "FROM aggregateddatasetcompleteness " +
                "WHERE datasetid = " + dataSetId + " " +
                "AND periodid = " + periodId + " " +
                "AND organisationunitid = " + organisationUnitId;
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return resultSet.next() ? resultSet.getDouble( 1 ) : -1;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get datasetcompleteness", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public int deleteDataSetCompleteness( Collection<Integer> dataSetIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = 
                "DELETE FROM aggregateddatasetcompleteness " +
                "WHERE datasetid IN ( " + getCommaDelimitedString( dataSetIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete datasetcompleteness", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public void deleteDataSetCompleteness()
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = "DELETE FROM aggregateddatasetcompleteness";
            
            holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete datasetcompleteness", ex );
        }
        finally
        {
            holder.close();
        }
    }
}
