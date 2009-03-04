package org.hisp.dhis.datamart.crosstab.jdbc;

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

import java.util.List;

import org.hisp.dhis.aggregation.batch.statement.StatementHolder;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.dataelement.Operand;

/**
 * @author Lars Helge Overland
 * @version $Id: JDBCCrossTabStore.java 5510 2008-07-30 16:30:27Z larshelg $
 */
public class JDBCCrossTabStore
    implements CrossTabStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;
    
    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    // -------------------------------------------------------------------------
    // CrossTabStore implementation
    // -------------------------------------------------------------------------

    public void createCrossTabTable( List<Operand> operands )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = "CREATE TABLE datavaluecrosstab ( " +
                "periodid INTEGER NOT NULL, " + 
                "sourceid INTEGER NOT NULL, ";
            
            for ( Operand operand : operands )
            {
                sql += COLUMN_PREFIX + operand.getDataElementId() + SEPARATOR + operand.getOptionComboId() + " VARCHAR(30), "; 
            }
            
            sql += "PRIMARY KEY ( periodid, sourceid ) );";
            
            holder.getStatement().executeUpdate( sql );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create datavalue crosstab table", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public void dropCrossTabTable()
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = "DROP TABLE IF EXISTS datavaluecrosstab";
            
            holder.getStatement().executeUpdate( sql );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to drop datavalue crosstab table", ex );
        }
        finally
        {
            holder.close();
        }
    }
}
