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
import org.hisp.dhis.completeness.DataSetCompletenessResult;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataSetCompletenessResultBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
 
    public DataSetCompletenessResultBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
        
        hasSinglePrimaryKey = false;
    }

    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "aggregateddatasetcompleteness";
    }
    
    protected void openSqlStatement()
    {
        addColumns();
        
        sqlBuffer.append( statementBuilder.getInsertStatementOpening( tableName ) );
    }
    
    protected String getUpdateSqlStatement( Object object )
    {
        addColumns();
        
        addValues( object );
        
        return statementBuilder.getUpdateStatement( tableName );
    }
    
    protected String getIdentifierStatement( Object objectName )
    {
        throw new UnsupportedOperationException( "DataSetCompleteness has no single unique identifier" );
    }
    
    protected String getUniquenessStatement( Object object )
    {
        DataSetCompletenessResult result = (DataSetCompletenessResult) object;
        
        Map<String, String> fieldMap = new HashMap<String, String>();
        
        fieldMap.put( "datasetid", String.valueOf( result.getDataSetId() ) );
        fieldMap.put( "periodid", String.valueOf( result.getPeriodId() ) );
        fieldMap.put( "organisationunitid", String.valueOf( result.getOrganisationUnitId() ) );
        fieldMap.put( "reporttableid", String.valueOf( result.getReportTableId() ) );
        
        return statementBuilder.getValueStatement( tableName, "datasetid", fieldMap, false );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "datasetid" );
        statementBuilder.setColumn( "periodid" );
        statementBuilder.setColumn( "periodname" );
        statementBuilder.setColumn( "organisationunitid" );
        statementBuilder.setColumn( "reporttableid" );
        statementBuilder.setColumn( "sources" );
        statementBuilder.setColumn( "registrations" );
        statementBuilder.setColumn( "registrationsOnTime" );
        statementBuilder.setColumn( "value" );
        statementBuilder.setColumn( "valueOnTime" );
    }
    
    protected void addValues( Object object )
    {
        DataSetCompletenessResult result = (DataSetCompletenessResult) object;
        
        statementBuilder.setInt( result.getDataSetId() );
        statementBuilder.setInt( result.getPeriodId() );
        statementBuilder.setString( result.getPeriodName() );
        statementBuilder.setInt( result.getOrganisationUnitId() );
        statementBuilder.setInt( result.getReportTableId() );
        statementBuilder.setInt( result.getSources() );
        statementBuilder.setInt( result.getRegistrations() );
        statementBuilder.setInt( result.getRegistrationsOnTime() );
        statementBuilder.setDouble( result.getPercentage() );
        statementBuilder.setDouble( result.getPercentageOnTime() );
    }
}
