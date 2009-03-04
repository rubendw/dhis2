package org.hisp.dhis.reporttable.statement;

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

import org.hisp.dhis.aggregation.batch.builder.StatementBuilder;
import org.hisp.dhis.aggregation.batch.factory.StatementBuilderFactory;
import org.hisp.dhis.aggregation.batch.factory.StatementDialect;
import org.hisp.dhis.reporttable.ReportTable;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public abstract class ReportTableStatement
{
    protected static final String NUMERIC_COLUMN_TYPE = "INTEGER NOT NULL";
    protected static final String SHORT_TEXT_COLUMN_TYPE = "VARCHAR (15)";
    protected static final String LONG_TEXT_COLUMN_TYPE = "VARCHAR (40)";
    protected static final String REPORTING_MONTH_COLUMN_NAME = "reporting_month_name";
    protected static final String SPACE = " ";
    protected static final String SEPARATOR = ", ";
    protected static final String QUERY_PARAM_ID = ":";

    protected StatementBuilder statementBuilder;
    
    protected String statement;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unused" )
    private ReportTableStatement()
    {
    }
    
    public ReportTableStatement( ReportTable reportTable )
    {
        init( reportTable );
    }
    
    public ReportTableStatement( ReportTable reportTable, StatementDialect dialect )
    {
        statementBuilder = StatementBuilderFactory.createStatementBuilder( dialect );
        
        init( reportTable );
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

    public String getStatement()
    {
        return statement;
    }
    
    public void setString( String param, String value )
    {
        statement = statement.replace( QUERY_PARAM_ID + param, value );
    }
    
    public void setInt( String param, Integer value )
    {        
        statement = statement.replace( QUERY_PARAM_ID + param, String.valueOf( value ) );
    }
    
    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract void init( ReportTable reportTable );
}
