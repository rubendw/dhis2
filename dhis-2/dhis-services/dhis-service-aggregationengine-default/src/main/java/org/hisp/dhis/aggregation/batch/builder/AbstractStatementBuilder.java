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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractStatementBuilder.java 5493 2008-07-23 21:57:40Z larshelg $
 */
public abstract class AbstractStatementBuilder
    implements StatementBuilder
{
    protected final String QUOTE = "'";
    protected final String NULL = "null";
    protected final String TRUE = "true";
    protected final String FALSE = "false";
    protected final String SEPARATOR = ",";
    protected final String BRACKET_START = "(";
    protected final String BRACKET_END = ")";

    protected Integer autoIncrementColumnIndex;
    protected String autoIncrementColumnName;
    protected String identifierColumnName;
    protected Integer identifierColumnValue;
    
    protected List<String> columns;
    protected List<String> values;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public AbstractStatementBuilder()
    {
        columns = new ArrayList<String>();
        values = new ArrayList<String>();
    }

    // -------------------------------------------------------------------------
    // StatementBuilder implementation
    // -------------------------------------------------------------------------

    public void setAutoIncrementColumnIndex( int index )
    {
        this.autoIncrementColumnIndex = index;
    }
    
    public void setAutoIncrementColumnName( String name )
    {
        this.autoIncrementColumnName = name;
    }
    
    public void setIdentifierColumnName( String identifierColumnName )
    {
        this.identifierColumnName = identifierColumnName;
    }

    public void setIdentifierColumnValue( Integer identifierColumnValue )
    {
        this.identifierColumnValue = identifierColumnValue;
    }

    public void setColumn( String column )
    {
        columns.add( column );
    }
    
    public void setString( String value )
    {
        values.add( value != null ? QUOTE + sqlEncode( value ) + QUOTE : NULL );
    }

    public void setInt( Integer value )
    {
        values.add( String.valueOf( value ) );
    }
    
    public void setDouble( Double value )
    {
        values.add( String.valueOf( value ) );
    }
    
    public void setBoolean( boolean value )
    {
        values.add( value ? TRUE : FALSE );
    }
    
    public void setDate( Date value )
    {
        values.add( value != null ? QUOTE + getDateString( value ) + QUOTE : NULL );
    }
    
    protected String getDateString( Date date )
    {
        Calendar cal = Calendar.getInstance();
        
        cal.setTime( date );
        
        int year = cal.get( Calendar.YEAR );
        int month = cal.get( Calendar.MONTH ) + 1;
        int day = cal.get( Calendar.DAY_OF_MONTH );
        
        String yearString = String.valueOf( year );
        String monthString = month < 10 ? "0" + month : String.valueOf( month );
        String dayString = day < 10 ? "0" + day : String.valueOf( day );
        
        return yearString + "-" + monthString + "-" + dayString;
    }
    
    protected String sqlEncode( String string )
    {
        if ( string != null )
        {
            string = string.endsWith( "\\" ) ? string.substring( 0, string.length() - 1 ) : string;
            string = string.replaceAll( QUOTE, "\\\\" + QUOTE );
            
            return string;
        }
        
        return null;
    }
}
