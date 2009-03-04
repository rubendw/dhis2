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
 * @version $Id: DataRootXSDConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class DataRootXSDConverter
    implements XMLConverter
{
    private static final String DHIS_VERSION = "1.4.0.101";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DataRootXSDConverter()
    {   
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        writer.openElement( "xsd:element", "name", "dataroot", "dhis-version", DHIS_VERSION, "dhis-application", "DHIS_CORE" );
        
        writer.openElement( "xsd:complexType" );
        
        writer.openElement( "xsd:sequence" );
        
        writer.writeElement( "xsd:element", "", "ref", "DataElement", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "DataElementAndIndicatorGroup", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "DataElementGroupMember", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "Indicator", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "IndicatorType", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "IndicatorGroupMember", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "DataPeriodType", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "UserName", "minOccurs", "0", "maxOccurs", "unbounded" );
        writer.writeElement( "xsd:element", "", "ref", "DataType", "minOccurs", "0", "maxOccurs", "unbounded" );
        
        writer.closeElement( "xsd:sequence" );
        
        writer.closeElement( "xsd:complexType" );
        
        writer.closeElement( "xsd:element" );
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented
    }
}
