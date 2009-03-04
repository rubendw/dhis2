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
 * @version $Id: IndicatorXSDConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class IndicatorXSDConverter
    extends AbstractXSDConverter implements XMLConverter
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public IndicatorXSDConverter()
    {   
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        writer.openElement( "xsd:element", "name", "Indicator" );
        
        writeAnnotation( writer );
        
        writer.openElement( "xsd:complexType" );
        
        writer.openElement( "xsd:sequence" );
        
        writeInteger( writer, "IndicatorID", 1, true );
        
        writeInteger( writer, "SortOrder", 1, true );
        
        writeText( writer, "IndiacatorCode", 0, false, 15 );
        
        writeText( writer, "IndicatorName", 1, true, 230 );
        
        writeText( writer, "IndicatorShort", 1, true, 25 );
        
        writeText( writer, "IndicatorDOS", 1, true, 8 );
        
        writeText( writer, "IndicatorPrompt", 1, true, 230 );
        
        writeLongInteger( writer, "ValidFrom", 1, true );

        writeLongInteger( writer, "ValidTo", 1, true );
        
        writeMemo( writer, "IndicatorDescription", 0, false, 536870910 );
        
        writeMemo( writer, "Comment", 0, false, 536870910 );
        
        writeInteger( writer, "Selected", 1, true );
        
        writeInteger( writer, "IndicatorTypeID", 1, true );
        
        writeInteger( writer, "DataPeriodTypeID", 1, true );
        
        writeInteger( writer, "Annualised", 1, true );
        
        writeMemo( writer, "IndiactorNumerator", 0, false, 536870910 );
        
        writeInteger( writer, "IndicatorNumeratorAggregateStartLevel", 1, true );
        
        writeInteger( writer, "IndicatorNumeratorTimeLag", 0, false );
        
        writeText( writer, "IndiactorNumeratorDescription", 0, false, 100 );

        writeMemo( writer, "IndiactorDenominator", 0, false, 536870910 );
        
        writeInteger( writer, "IndicatorDenominatorAggregateStartLevel", 1, true );
        
        writeInteger( writer, "IndicatorDenominatorTimeLag", 0, false );
        
        writeText( writer, "IndiactorDenominatorDescription", 0, false, 100 );
        
        writeInteger( writer, "LastUserID", 1, true );
        
        writeDateTime( writer, "LastUpdated", 1, true );
        
        writeText( writer, "IndicatorNameAlt1", 0, false, 230 );

        writeText( writer, "IndicatorShortAlt1", 0, false, 25 );

        writeText( writer, "IndicatorNameAlt2", 0, false, 230 );

        writeText( writer, "IndicatorShortAlt2", 0, false, 25 );

        writeText( writer, "IndicatorNameAlt3", 0, false, 230 );

        writeText( writer, "IndicatorShortAlt3", 0, false, 25 );
        
        writer.closeElement( "xsd:sequence" );
        
        writer.closeElement( "xsd:complexType" );
        
        writer.closeElement( "xsd:element" );
    }

    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
