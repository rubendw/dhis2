package org.hisp.dhis.importexport.dhis14.xml.converter;

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

import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: DataTypeConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class DataTypeConverter
    implements XMLConverter
{
    private static final String ELEMENT_NAME = "DataType";
    
    private static final String FIELD_ID = "DataTypeID";
    private static final String FIELD_NAME = "DataTypeName";
    private static final String FIELD_DISPLAY = "DataTypeDisplay";
    private static final String FIELD_ML = "DataType_ML";

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DataTypeConverter()
    {   
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        // ---------------------------------------------------------------------
        // Number
        // ---------------------------------------------------------------------
     
        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 1 ) );
        writer.writeElement( FIELD_NAME, "Number" );
        writer.writeElement( FIELD_DISPLAY, "Number" );
        writer.writeElement( FIELD_ML, new String() );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Text
        // ---------------------------------------------------------------------
     
        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 2 ) );
        writer.writeElement( FIELD_NAME, "String" );
        writer.writeElement( FIELD_DISPLAY, "Text" );
        writer.writeElement( FIELD_ML, new String() );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Yes/No
        // ---------------------------------------------------------------------
     
        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 2 ) );
        writer.writeElement( FIELD_NAME, "YesNo" );
        writer.writeElement( FIELD_DISPLAY, "Yes/No" );
        writer.writeElement( FIELD_ML, new String() );
        
        writer.closeElement( ELEMENT_NAME );
    }

    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
