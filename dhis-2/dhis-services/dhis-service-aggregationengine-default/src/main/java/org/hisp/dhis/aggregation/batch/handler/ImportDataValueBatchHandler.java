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
import org.hisp.dhis.importexport.ImportDataValue;

/**
 * @author Lars Helge Overland
 * @version $Id: ImportDataValueBatchHandler.java 5062 2008-05-01 18:10:35Z larshelg $
 */
public class ImportDataValueBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
 
    public ImportDataValueBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
        
        hasSinglePrimaryKey = false;
    }

    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "importdatavalue";
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
        throw new UnsupportedOperationException( "ImportDataValue has no single unique identifier" );
    }
    
    protected String getUniquenessStatement( Object object )
    {
        ImportDataValue importDataValue = new ImportDataValue();
        
        Map<String, String> fieldMap = new HashMap<String, String>();
        
        fieldMap.put( "dataelementid", String.valueOf( importDataValue.getDataElementId() ) );
        fieldMap.put( "categoryoptioncomboid", String.valueOf( importDataValue.getCategoryOptionComboId() ) );
        fieldMap.put( "periodid", String.valueOf( importDataValue.getPeriodId() ) );
        fieldMap.put( "sourceid", String.valueOf( importDataValue.getSourceId() ) );
        
        return statementBuilder.getValueStatement( tableName, "dataelementid", fieldMap, true );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "dataelementid" );
        statementBuilder.setColumn( "categoryoptioncomboid" );
        statementBuilder.setColumn( "periodid" );
        statementBuilder.setColumn( "sourceid" );
        statementBuilder.setColumn( "value" );
        statementBuilder.setColumn( "storedby" );
        statementBuilder.setColumn( "lastupdated" );
        statementBuilder.setColumn( "comment" );
        statementBuilder.setColumn( "status" );
    }
    
    protected void addValues( Object object )
    {
        ImportDataValue value = (ImportDataValue) object;
        
        statementBuilder.setInt( value.getDataElementId() );
        statementBuilder.setInt( value.getCategoryOptionComboId() );
        statementBuilder.setInt( value.getPeriodId() );
        statementBuilder.setInt( value.getSourceId() );
        statementBuilder.setString( value.getValue() );
        statementBuilder.setString( value.getStoredBy() );
        statementBuilder.setDate( value.getTimestamp() );
        statementBuilder.setString( value.getComment() );
        statementBuilder.setString( value.getStatus() );
    }
}
