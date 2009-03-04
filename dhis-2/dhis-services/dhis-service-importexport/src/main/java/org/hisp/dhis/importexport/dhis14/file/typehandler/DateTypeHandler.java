package org.hisp.dhis.importexport.dhis14.file.typehandler;

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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author Lars Helge Overland
 * @version $Id: DateTypeHandler.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class DateTypeHandler
    implements TypeHandlerCallback
{
    // -------------------------------------------------------------------------
    // TypeHandlerCallback implementation
    // -------------------------------------------------------------------------

    public Object getResult( ResultGetter getter )
        throws SQLException
    {
        int days = getter.getInt();
        
        if ( days != 0 )
        {
            Calendar cal = Calendar.getInstance();
    
            // ------------------------------------------------------------
            // JDBC date is represented as an Integer, and returns number 
            // of days since 01/01/1900 minus two.
            // ------------------------------------------------------------
    
            cal.clear();
            cal.set( 1900, 0, 1 );
            cal.add( Calendar.DATE, days - 2 );
            
            Date date = cal.getTime();
            
            return date;
        }
        else
        {
            throw new RuntimeException( "Failed to get period" );
        }
    }
    
    public void setParameter( ParameterSetter setter, Object parameter )
        throws SQLException
    {
        // Not in use
    }
    
    public Object valueOf( String result )
    {
        if ( result != null )
        {
            int days = Integer.parseInt( result );
        
            Calendar cal = Calendar.getInstance();
            
            cal.clear();
            cal.set( 1900, 0, 1 );
            cal.add( Calendar.DATE, days - 2 );
            
            Date date = cal.getTime();
            
            return date;
        }
        else
        {
            throw new RuntimeException( "Failed to get period" );
        }
    }
}
