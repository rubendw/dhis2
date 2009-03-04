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

import java.util.Iterator;

import org.hisp.dhis.reporttable.ReportTable;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetReportTableDataStatement
    extends ReportTableStatement
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public GetReportTableDataStatement( ReportTable reportTable )
    {
        super( reportTable );
    }

    // -------------------------------------------------------------------------
    // ReportTableStatement implementation
    // -------------------------------------------------------------------------

    @Override
    protected void init( ReportTable reportTable )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "SELECT value, " );
        
        Iterator<String> selectColumns = reportTable.getSelectColumns().iterator();
        
        while( selectColumns.hasNext() )
        {
            buffer.append( selectColumns.next() + ( selectColumns.hasNext() ? SEPARATOR : SPACE ) );                
        }
        
        buffer.append( "FROM " + getDataTable( reportTable.getMode() ) + SPACE );
        
        Iterator<String> indexColumns = reportTable.getIndexColumns().iterator();
        
        String indexColumn = indexColumns.next();
        
        buffer.append( "WHERE " + indexColumn + "='" + QUERY_PARAM_ID + indexColumn + "' " );
        
        while ( indexColumns.hasNext() )
        {
            indexColumn = indexColumns.next();
            
            buffer.append( "AND " + indexColumn + "='" + QUERY_PARAM_ID + indexColumn + "' " );
        }
        
        statement = buffer.toString();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String getDataTable( String mode )
    {
        if ( mode == null || mode.equals( ReportTable.MODE_INDICATORS ) )
        {
            return "aggregatedindicatorvalue";
        }
        else if ( mode.equals( ReportTable.MODE_DATAELEMENTS ) )
        {
            return "aggregateddatavalue";
        }
        else if ( mode.equals( ReportTable.MODE_DATASETS ) )
        {
            return "aggregateddatasetcompleteness";
        }
        
        return null;
    }
}
