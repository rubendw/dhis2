package org.hisp.dhis.populator;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: PopulatorUtils.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class PopulatorUtils
{
    public static final String ID_SEPARATOR = "=";

    public static final String ARGUMENTS_SEPARATOR = ";";

    public static final String NULL = "null";

    // -------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------

    public static String getRuleId( String rule ) throws PopulatorException
    {
        int i = rule.indexOf( ID_SEPARATOR );

        if ( i == -1 )
        {
            throw new PopulatorException( "Malformed rule [id = ...] {" + rule + "}" );
        }

        String ruleId = rule.substring( 0, i );

        return ruleId.trim(); // Trimmed
    }

    public static List<String> getRuleArguments( String rule ) throws PopulatorException
    {
        int i = rule.indexOf( ID_SEPARATOR );

        if ( i == -1 )
        {
            throw new PopulatorException( "Malformed rule [id = ...] {" + rule + "}" );
        }

        List<String> arguments = new ArrayList<String>();

        String argumentsString = rule.substring( i + ID_SEPARATOR.length() );

        StringTokenizer t = new StringTokenizer( argumentsString, ARGUMENTS_SEPARATOR );

        while ( t.hasMoreTokens() )
        {
            String argument = t.nextToken().trim(); // Trimmed
            arguments.add( nullConverter( argument ) );
        }

        return arguments;
    }

    public static final String nullConverter( String value )
    {
        if ( value.equals( NULL ) )
        {
            return null;
        }

        return value;
    }

    public static final Date toDate( String dateString ) throws PopulatorException
    {
        if ( dateString == null )
        {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm" );

        try
        {
            return dateFormat.parse( dateString );
        }
        catch ( ParseException e )
        {
            throw new PopulatorException( "Failed to parse date string: " + dateString, e );
        }
    }

    public static final boolean toBoolean( String string )
    {
        return new Boolean( string ).booleanValue();
    }
}
