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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.reporttable.ReportTable;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTableBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
 
    public ReportTableBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
    }

    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "reporttable";
    }
    
    protected void openSqlStatement()
    {
        statementBuilder.setAutoIncrementColumnIndex( 0 );
        statementBuilder.setAutoIncrementColumnName( "reporttableid" );
        
        addColumns();
        
        sqlBuffer.append( statementBuilder.getInsertStatementOpening( tableName ) );
    }
    
    protected String getUpdateSqlStatement( Object object )
    {
        ReportTable reportTable = (ReportTable) object;
        
        statementBuilder.setIdentifierColumnName( "reporttableid" );
        statementBuilder.setIdentifierColumnValue( reportTable.getId() );
        
        addColumns();
        
        addValues( object );
        
        return statementBuilder.getUpdateStatement( tableName );
    }
    
    protected String getIdentifierStatement( Object objectName )
    {
        return statementBuilder.getValueStatement( tableName, "reporttableid", "name", String.valueOf( objectName ) );
    }
    
    protected String getUniquenessStatement( Object object )
    {
        ReportTable reportTable = (ReportTable) object;
        
        Map<String, String> fieldMap = new HashMap<String, String>();
        
        fieldMap.put( "name", reportTable.getName() );
        
        return statementBuilder.getValueStatement( tableName, "reporttableid", fieldMap, false );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "name" );
        statementBuilder.setColumn( "tablename" );
        statementBuilder.setColumn( "doindicators" );
        statementBuilder.setColumn( "doperiods" );
        statementBuilder.setColumn( "dounits" );
        statementBuilder.setColumn( "reportingmonth" );
        statementBuilder.setColumn( "last3months" );
        statementBuilder.setColumn( "last6months" );
        statementBuilder.setColumn( "last9months" );
        statementBuilder.setColumn( "last12months" );
        statementBuilder.setColumn( "sofarthisyear" );
    }
    
    protected void addValues( Object object )
    {
        ReportTable reportTable = (ReportTable) object;
        
        statementBuilder.setString( reportTable.getName() );
        statementBuilder.setString( reportTable.getTableName() );
        statementBuilder.setBoolean( reportTable.isDoIndicators() );
        statementBuilder.setBoolean( reportTable.isDoPeriods() );
        statementBuilder.setBoolean( reportTable.isDoUnits() );
        statementBuilder.setBoolean( reportTable.getRelatives().isReportingMonth() );
        statementBuilder.setBoolean( reportTable.getRelatives().isLast3Months() );
        statementBuilder.setBoolean( reportTable.getRelatives().isLast6Months() );
        statementBuilder.setBoolean( reportTable.getRelatives().isLast9Months() );
        statementBuilder.setBoolean( reportTable.getRelatives().isLast12Months() );
        statementBuilder.setBoolean( reportTable.getRelatives().isSoFarThisYear() );
    }
}
