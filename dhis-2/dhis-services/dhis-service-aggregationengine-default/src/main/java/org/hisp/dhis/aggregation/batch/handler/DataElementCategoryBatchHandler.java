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
import org.hisp.dhis.dataelement.DataElementCategory;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementCategoryBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DataElementCategoryBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
    }
    
    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "dataelementcategory";
    }
    
    protected void openSqlStatement()
    {
        statementBuilder.setAutoIncrementColumnIndex( 0 );
        statementBuilder.setAutoIncrementColumnName( "categoryid" );
        
        addColumns();
        
        sqlBuffer.append( statementBuilder.getInsertStatementOpening( tableName ) );
    }
    
    protected String getUpdateSqlStatement( Object object )
    {
        DataElementCategory category = (DataElementCategory) object;
        
        statementBuilder.setIdentifierColumnName( "categoryid" );
        statementBuilder.setIdentifierColumnValue( category.getId() );
        
        addColumns();
        
        addValues( object );
        
        return statementBuilder.getUpdateStatement( tableName );
    }
    
    protected String getIdentifierStatement( Object objectName )
    {
        return statementBuilder.getValueStatement( tableName, "categoryid", "name", String.valueOf( objectName ) );
    }
    
    protected String getUniquenessStatement( Object object )
    {
        DataElementCategory category = (DataElementCategory) object;

        Map<String, String> fieldMap = new HashMap<String, String>();
        
        fieldMap.put( "name", category.getName() );
        
        return statementBuilder.getValueStatement( tableName, "categoryid", fieldMap, false );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "name" );
    }
    
    protected void addValues( Object object )
    {
        DataElementCategory category = (DataElementCategory) object;
        
        statementBuilder.setString( category.getName() );
    }
}
