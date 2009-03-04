package org.hisp.dhis.importexport.dhis14.xml.converter.xsd;

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

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;

/**
 * @author Lars Helge Overland
 * @version $Id: DataElementXSDConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class DataElementXSDConverter
    extends AbstractXSDConverter implements XMLConverter
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DataElementXSDConverter()
    {   
    }
    
    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        writer.openElement( "xsd:element", "name", "DataElement" );
        
        writeAnnotation( writer );
        
        writer.openElement( "xsd:complexType" );
        
        writer.openElement( "xsd:sequence" );
        
        writeInteger( writer, "DataElementID", 1, true );
        
        writeInteger( writer, "SortOrder", 1, true );
        
        writeText( writer, "DataElementCode", 0, false, 15 );
        
        writeText( writer, "DataElementName", 1, true, 230 );

        writeText( writer, "DataElementShort", 1, true, 25 );

        writeText( writer, "DataElementDOS", 1, true, 8 );

        writeText( writer, "DataElementPrompt", 1, true, 230 );

        writeInteger( writer, "MetaDataElement", 1, true );
        
        writeInteger( writer, "DataTypeID", 1, true );
        
        writeInteger( writer, "DataPeriodTypeID", 1, true );
        
        writeLongInteger( writer, "ValidFrom", 1, true );

        writeLongInteger( writer, "ValidTo", 1, true );
        
        writeMemo( writer, "DataElementDescription", 0, false, 536870910 );
        
        writeMemo( writer, "Comment", 0, false, 536870910 );
        
        writeInteger( writer, "Calculated", 1, true );
        
        writeLongInteger( writer, "CalculatedValidFrom", 0, false );

        writeLongInteger( writer, "CalculatedValidTo", 0, false );
        
        writeInteger( writer, "SaveCalculated", 0, false );
        
        writeInteger( writer, "AggregateStartLevel", 1, true );
        
        writeText( writer, "AggregateOperator", 1, true, 6 );
        
        writeInteger( writer, "Selected", 1, true );
        
        writeInteger( writer, "LastUserID", 1, true );
        
        writeDateTime( writer, "LastUpdated", 1, true );
        
        writeText( writer, "DataElementNameAlt1", 0, false, 230 );

        writeText( writer, "DataElementShortAlt1", 0, false, 25 );

        writeText( writer, "DataElementNameAlt2", 0, false, 230 );

        writeText( writer, "DataElementShortAlt2", 0, false, 25 );

        writeText( writer, "DataElementNameAlt3", 0, false, 230 );

        writeText( writer, "DataElementShortAlt3", 0, false, 25 );
        
        writer.closeElement( "xsd:sequence" );
        
        writer.closeElement( "xsd:complexType" );
        
        writer.closeElement( "xsd:element" );
    }    

    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
