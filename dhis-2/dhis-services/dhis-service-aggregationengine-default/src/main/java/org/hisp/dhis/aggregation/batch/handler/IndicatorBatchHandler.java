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
import org.hisp.dhis.indicator.Indicator;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorBatchHandler.java 5811 2008-10-03 18:36:11Z larshelg $
 */
public class IndicatorBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
 
    public IndicatorBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
    }

    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "indicator";
    }
    
    protected void openSqlStatement()
    {
        statementBuilder.setAutoIncrementColumnIndex( 0 );
        statementBuilder.setAutoIncrementColumnName( "indicatorid" );
        
        addColumns();
        
        sqlBuffer.append( statementBuilder.getInsertStatementOpening( tableName ) );
    }
    
    protected String getUpdateSqlStatement( Object object )
    {
        Indicator indicator = (Indicator) object;
        
        statementBuilder.setIdentifierColumnName( "indicatorid" );
        statementBuilder.setIdentifierColumnValue( indicator.getId() );
        
        addColumns();
        
        addValues( object );
        
        return statementBuilder.getUpdateStatement( tableName );
    }
    
    protected String getIdentifierStatement( Object objectName )
    {
        return statementBuilder.getValueStatement( tableName, "indicatorid", "name", String.valueOf( objectName ) );
    }    

    protected String getUniquenessStatement( Object object )
    {
        Indicator indicator = (Indicator) object;
        
        Map<String, String> fieldMap = new HashMap<String, String>();
        
        fieldMap.put( "name", indicator.getName() );
        fieldMap.put( "shortname", indicator.getShortName() );
        fieldMap.put( "code", indicator.getCode() );
        fieldMap.put( "alternativename", indicator.getAlternativeName() );
        
        return statementBuilder.getValueStatement( tableName, "indicatorid", fieldMap, false );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "uuid" );
        statementBuilder.setColumn( "name" );
        statementBuilder.setColumn( "alternativename" );
        statementBuilder.setColumn( "shortname" );
        statementBuilder.setColumn( "code" );
        statementBuilder.setColumn( "description" );
        statementBuilder.setColumn( "annualized" );
        statementBuilder.setColumn( "indicatortypeid" );
        statementBuilder.setColumn( "numerator" );
        statementBuilder.setColumn( "numeratordescription" );
        statementBuilder.setColumn( "numeratoraggregationtype" );
        statementBuilder.setColumn( "denominator" );
        statementBuilder.setColumn( "denominatordescription" );
        statementBuilder.setColumn( "denominatoraggregationtype" );
        statementBuilder.setColumn( "extendeddataelementid" );
    }
    
    protected void addValues( Object object )
    {
        Indicator indicator = (Indicator) object;
        
        statementBuilder.setString( indicator.getUuid() );
        statementBuilder.setString( indicator.getName() );
        statementBuilder.setString( indicator.getAlternativeName() );
        statementBuilder.setString( indicator.getShortName() );
        statementBuilder.setString( indicator.getCode() );
        statementBuilder.setString( indicator.getDescription() );
        statementBuilder.setBoolean( indicator.getAnnualized() );
        statementBuilder.setInt( indicator.getIndicatorType().getId() );
        statementBuilder.setString( indicator.getNumerator() );
        statementBuilder.setString( indicator.getNumeratorDescription() );
        statementBuilder.setString( indicator.getNumeratorAggregationOperator() );
        statementBuilder.setString( indicator.getDenominator() );
        statementBuilder.setString( indicator.getDenominatorDescription() );
        statementBuilder.setString( indicator.getDenominatorAggregationOperator() );
        statementBuilder.setInt( indicator.getExtended() != null ? indicator.getExtended().getId() : null );
    }
}
