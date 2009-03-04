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

import java.util.Date;
import java.util.Map;

import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: StatementBuilder.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public interface StatementBuilder
{
    /**
     * Sets the index of the auto increment column of the statement.
     * 
     * @param index the index.
     */
    void setAutoIncrementColumnIndex( int index );
    
    /**
     * Sets the name of the auto increment column of the statement.
     * 
     * @param name the name.
     */
    void setAutoIncrementColumnName( String name );
    
    /**
     * Sets the name of the identifier column of the current object.
     * 
     * @param identifierColumnName the name of the identifier column.
     */
    void setIdentifierColumnName( String identifierColumnName );
    
    /**
     * Sets the value of identifier column of the current object.
     * 
     * @param identifierColumnValue the value of the identifier column.
     */    
    void setIdentifierColumnValue( Integer identifierColumnValue );
    
    /**
     * Adds a column to the column list of the statement.
     * 
     * @param column the column name.
     */
    void setColumn( String column );
    
    /**
     * Adds a string to the value list of the statement.
     * 
     * @param value the string value.
     */
    void setString( String value );
    
    /**
     * Adds an int to the value list of the statement.
     * 
     * @param value the int value.
     */
    void setInt( Integer value );
    
    /**
     * Adds a double to the value list of the statement.
     * 
     * @param value the double value.
     */
    void setDouble( Double value );

    /**
     * Adds a boolean to the value list of the statement.
     * 
     * @param value the boolean value.
     */
    void setBoolean( boolean value );
    
    /**
     * Adds a date to the value list of the statement.
     * 
     * @param value the date.
     */
    void setDate( Date value );
    
    /**
     * Creates the opening of an insert SQL statement. The columns are added with
     * the <code>setColumn( String )</code> method. The returned String is on the
     * form <blockquote>INSERT INTO table ( column1, column2 ) VALUES </blockquote>.
     * 
     * @param table the table to use in the SQL statement.
     * @return the opening of an insert SQL statement.
     */
    String getInsertStatementOpening( String table );
    
    /**
     * Creates the opening of an insert SQL statement with no columns defined.
     * 
     * @param table the table to use in the SQL statement.
     * @return the opening of an insert SQL statement.
     */
    String getNoColumnInsertStatementOpening( String table );
    
    /**
     * Creates a value list for an insert SQL statement. The values are added with
     * the various <code>setFoo( .. )</code> methods. The returned String is on the
     * form <blockquote>( value1, value2 )</blockquote>.
     * 
     * @return a value list for an insert SQL statement.
     */
    String getInsertStatementValues();
    
    /**
     * Creates an update SQL statement. The columns are added with the
     * <code>setColumn(..)</code> method, the values are added with the
     * various <code>setFoo(..)</code> methods.
     * 
     * @param table the name of the table.
     * @return
     */
    String getUpdateStatement( String table );
    
    /**
     * Creates a SELECT statement for the given parameters.
     * 
     * @param table the table to use in the statement.
     * @param returnField the table field to return from the query.
     * @param compareField the table field to compare with the value.
     * @param value the value to compare with the compareField.
     * @return a SELECT statement for the given parameters.
     */
    String getValueStatement( String table, String returnField, String compareField, String value );
    
    /**
     * Creates a SELECT statement for the given parameters.
     * 
     * @param table the table to use in the statement.
     * @param returnField1 the first table field to return from the query.
     * @param returnField2 the second table field to return from the query.
     * @param compareField1 the first table field to compare with the value.
     * @param value1 the value to compare with the first compareField.
     * @param compareField2 the second table field to compare with the value.
     * @param value2 the value to compare with the second compareField.
     * @return a SELECT statement for the given parameters.
     */
    String getValueStatement( String table, String returnField1, String returnField2, String compareField1, String value1, String compareField2, String value2 );
    
    /**
     * Creates a SELECT statement for the given parameters.
     * 
     * @param table the table to use in the statement.
     * @param returnField the table field to return from the query.
     * @param fieldMap the map of fields and values to compare.
     * @param union true for AND, false for OR
     * @return a SELECT statement for the given parameters.
     */
    String getValueStatement( String table, String returnField, Map<String, String> fieldMap, boolean union );
    
    /**
     * Returns the name of a double column type.
     * @return the name of a double column type.
     */
    String getDoubleColumnType();
    
    /**
     * Creates a SELECT statement returning the identifier of the given Period.
     * 
     * @param period the Period to use in the statement. 
     * @return a SELECT statement returning the identifier of the given Period.
     */
    String getPeriodIdentifierStatement( Period period );
    
    /**
     * Creates a create table statement fot the aggregated datavalue table.
     * @return a create table statement fot the aggregated datavalue table.
     */
    String getCreateAggregatedDataValueTable();
    
    /**
     * Creates a create table statement for the aggregated indicatorvalue table.
     * @return a create table statement for the aggregated indicatorvalue table.
     */    
    String getCreateAggregatedIndicatorTable();

    /**
     * Creates a create table statement for the aggregated datasetcompleteness table.
     * @return a create table statement for the aggregated datasetcompleteness table.
     */
    String getCreateDataSetCompletenessTable();
    
    /**
     * Creates a create index statement for the datavalue table.
     * @return a create index statement for the datavalue table.
     */
    String getCreateDataValueIndex();
    
    /**
     * Creates a delete periods of type relative statement.
     * @return a delete periods of type relative statement.
     */
    String getDeleteRelativePeriods();
    
    /**
     * Creates a delete datavalue statement.
     * @return a delete datavalue statement.
     */
    String getDeleteZeroDataValues();
}
