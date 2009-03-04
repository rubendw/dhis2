package org.hisp.dhis.aggregation.batch.builder;

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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: MySQLStatementBuilder.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public class MySQLStatementBuilder
    extends AbstractStatementBuilder
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public MySQLStatementBuilder()
    {
        super();
    }

    // -------------------------------------------------------------------------
    // AbstractStatementBuilder implementation
    // -------------------------------------------------------------------------
 
    public String getInsertStatementOpening( String table )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "INSERT INTO " + table + " (" );
        
        for ( String column : columns )
        {
            buffer.append( column + SEPARATOR );
        }
        
        if ( columns.size() > 0 )
        {
            buffer.deleteCharAt( buffer.length() - 1 );
        }
        
        buffer.append( BRACKET_END + " VALUES " );
        
        columns.clear();
        
        return buffer.toString();
    }
    
    public String getNoColumnInsertStatementOpening( String table )
    {
        String sql = "INSERT INTO " + table + " VALUES ";
        
        return sql;
    }
    
    public String getInsertStatementValues()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( BRACKET_START );
        
        for ( String value : values )
        {
            buffer.append( value + SEPARATOR );
        }
        
        if ( values.size() > 0 )
        {
            buffer.deleteCharAt( buffer.length() - 1 );
        }
        
        buffer.append( BRACKET_END + SEPARATOR );
        
        values.clear();
        
        return buffer.toString();
    }
    
    public String getUpdateStatement( String table )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "UPDATE " + table + " SET " );

        Iterator<String> columnIterator = columns.iterator();
        Iterator<String> valueIterator = values.iterator();
        
        while ( columnIterator.hasNext() )
        {
            buffer.append( columnIterator.next() + "=" + valueIterator.next() + SEPARATOR );
        }
        
        if ( columns.size() > 0 && values.size() > 0 )
        {
            buffer.deleteCharAt( buffer.length() - 1 );
        }
        
        buffer.append( " WHERE " + identifierColumnName + "=" + identifierColumnValue );
        
        columns.clear();
        values.clear();
        
        return buffer.toString();
    }
    
    public String getValueStatement( String table, String returnField, String compareField, String value )
    {
        String sql = "SELECT " + returnField + " FROM " + table + " WHERE " + compareField + " = '" + sqlEncode( value ) + "'";
        
        return sql;
    }
    
    public String getValueStatement( String table, String returnField1, String returnField2, String compareField1, String value1, String compareField2, String value2 )
    {
        String sql = "SELECT " + returnField1 + ", " + returnField2 + " FROM " + table + " WHERE " + compareField1 + "='" + sqlEncode( value1 ) + "' AND " + compareField2 + "='" + value2 + "'";
        
        return sql;
    }
    
    public String getValueStatement( String table, String returnField, Map<String, String> fieldMap, boolean union )
    {
        String operator = union ? " AND " : " OR ";
        
        String sql = "SELECT " + returnField + " FROM " + table + " WHERE ";
        
        for ( Entry<String, String> entry : fieldMap.entrySet() )
        {
            sql += entry.getKey() + "='" + sqlEncode( entry.getValue() ) + "'" + operator;
        }
        
        sql = sql.substring( 0, sql.length() - operator.length() );
        
        return sql;
    }

    public String getDoubleColumnType()
    {
        String type = "DOUBLE";
        
        return type;
    }
    
    public String getPeriodIdentifierStatement( Period period )
    {
        String sql = 
            "SELECT periodid FROM period WHERE periodtypeid=" + period.getPeriodType().getId() + " " + 
            "AND startdate='" + getDateString( period.getStartDate() ) + "' " +
            "AND enddate='" + getDateString( period.getEndDate() ) + "'";
        
        return sql;
    }    

    public String getCreateAggregatedDataValueTable()
    {
        String sql = "CREATE TABLE aggregateddatavalue ( " +
            "dataelementid INTEGER, " +
            "categoryoptioncomboid INTEGER, " +
            "periodid INTEGER, " +
            "organisationunitid INTEGER, " +
            "periodtypeid INTEGER, " +
            "level INTEGER, " +
            "value DOUBLE );";
        
        return sql;
    }
    
    public String getCreateAggregatedIndicatorTable()
    {
        String sql = "CREATE TABLE aggregatedindicatorvalue ( " +
            "indicatorid INTEGER, " +
            "periodid INTEGER, " +
            "organisationunitid INTEGER, " +
            "periodtypeid INTEGER, " +
            "level INTEGER, " +
            "annualized VARCHAR( 10 ), " +
            "factor DOUBLE, " +
            "value DOUBLE, " +
            "numeratorvalue DOUBLE, " +
            "denominatorvalue DOUBLE );";
        
        return sql;
    }

    public String getCreateDataSetCompletenessTable()
    {
        String sql = "CREATE TABLE aggregateddatasetcompleteness ( " +
            "datasetid INTEGER, " +
            "periodid INTEGER, " +
            "periodname VARCHAR( 30 ), " +
            "organisationunitid INTEGER, " +
            "reporttableid INTEGER, " +
            "sources INTEGER, " +
            "registrations INTEGER, " +
            "registrationsOnTime INTEGER, " +
            "value DOUBLE, " +
            "valueOnTime DOUBLE );";
        
        return sql;
    }

    public String getCreateDataValueIndex()
    {
        String sql =
            "CREATE INDEX crosstab " +
            "ON datavalue ( periodid, sourceid );";
        
        return sql;
    }

    public String getDeleteRelativePeriods()
    {
        String sql =
            "DELETE FROM period " +
            "USING period, periodtype " +
            "WHERE period.periodtypeid = periodtype.periodtypeid " +
            "AND periodtype.name = '" + RelativePeriodType.NAME + "';";
        
        return sql;
    }

    public String getDeleteZeroDataValues()
    {
        String sql = 
            "DELETE FROM datavalue " +
            "USING datavalue, dataelement " +
            "WHERE datavalue.dataelementid = dataelement.dataelementid " +
            "AND dataelement.aggregationtype = 'sum' " +
            "AND ( datavalue.value = '0' OR datavalue.value = '0.0' )";
        
        return sql;
    }
}
