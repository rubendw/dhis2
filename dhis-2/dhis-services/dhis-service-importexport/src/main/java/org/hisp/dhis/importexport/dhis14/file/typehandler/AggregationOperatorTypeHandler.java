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

import org.hisp.dhis.dataelement.DataElement;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author Lars Helge Overland
 * @version $Id: AggregationOperatorTypeHandler.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class AggregationOperatorTypeHandler
    implements TypeHandlerCallback
{
    private static final String JDBC_SUM = "Sum";
    private static final String JDBC_AVERAGE = "Avg";
    private static final String JDBC_COUNT = "Count";
    
    static final Integer NON_EXISTING = new Integer( -1 );
    
    // -------------------------------------------------------------------------
    // TypeHandlerCallback implementation
    // -------------------------------------------------------------------------

    public Object getResult( ResultGetter getter )
        throws SQLException
    {   
        String result = getter.getString();
        
        if ( result != null )
        {
            if ( result.equalsIgnoreCase( JDBC_SUM ) )
            {
                return DataElement.AGGREGATION_OPERATOR_SUM;
            }
            else if ( result.equalsIgnoreCase( JDBC_AVERAGE ) )
            {
                return DataElement.AGGREGATION_OPERATOR_AVERAGE;
            }
            else if ( result.equalsIgnoreCase( JDBC_COUNT ) )
            {
                return DataElement.AGGREGATION_OPERATOR_COUNT;
            }
            else
            {
                throw new RuntimeException( "Illegal aggregation operator: " + result );
            }
        }
        else
        {
            throw new RuntimeException( "Aggregation operator is not present" );
        }
    }
    
    public void setParameter( ParameterSetter setter, Object parameter )
        throws SQLException
    {
        // Not in use
    }
    
    public Object valueOf( String result )
    {
        if ( result == null )
        {
            return NON_EXISTING;
        }        
        else if ( result.equalsIgnoreCase( JDBC_SUM ) )
        {
            return DataElement.AGGREGATION_OPERATOR_SUM;
        }
        else if ( result.equalsIgnoreCase( JDBC_AVERAGE ) )
        {
            return DataElement.AGGREGATION_OPERATOR_AVERAGE;
        }
        else if ( result.equalsIgnoreCase( JDBC_COUNT ) )
        {
            return DataElement.AGGREGATION_OPERATOR_COUNT;
        }
        else
        {
            throw new RuntimeException( "Illegal aggregation operator: " + result );
        }
    }
}
