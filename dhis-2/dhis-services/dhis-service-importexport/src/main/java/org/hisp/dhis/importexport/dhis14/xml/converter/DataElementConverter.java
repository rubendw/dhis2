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

import java.util.Collection;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.dhis14.xml.typehandler.TypeHandler;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: DataElementConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class DataElementConverter
    implements XMLConverter
{
    private static final String ELEMENT_NAME = "DataElement";
    
    private static final String FIELD_ID = "DataElementID";
    private static final String FIELD_SORT_ORDER = "SortOrder";
    private static final String FIELD_CODE = "DataElementCode";
    private static final String FIELD_NAME = "DataElementName";
    private static final String FIELD_SHORT_NAME = "DataElementShort";
    private static final String FIELD_DOS = "DataElementDOS";
    private static final String FIELD_PROMPT = "DataElementPrompt";
    private static final String FIELD_META = "MetaDataElement";
    private static final String FIELD_DATA_TYPE = "DataTypeID";
    private static final String FIELD_PERIOD_TYPE = "DataPeriodTypeID";
    private static final String FIELD_VALID_FROM = "ValidFrom";
    private static final String FIELD_VALID_TO = "ValidTo";
    private static final String FIELD_DESCRIPTION = "DataElementDescription";
    private static final String FIELD_CALCULATED = "Calculated";
    private static final String FIELD_SAVE_CALCULATED = "SaveCalculated";
    private static final String FIELD_AGGREGATION_START_LEVEL = "AggregateStartLevel";
    private static final String FIELD_AGGREGATION_OPERATOR = "AggregateOperator";
    private static final String FIELD_SELECTED = "Selected";
    private static final String FIELD_LAST_USER = "LastUserID";
    private static final String FIELD_LAST_UPDATED = "LastUpdated";
    private static final String FIELD_NAME_ALT1 = "DataElementNameAlt1";
    private static final String FIELD_SHORT_NAME_ALT1 = "DataElementShortAlt1";
    private static final String FIELD_NAME_ALT2 = "DataElementNameAlt2";
    private static final String FIELD_SHORT_NAME_ALT2 = "DataElementShortAlt2";
    private static final String FIELD_NAME_ALT3 = "DataElementNameAlt3";
    private static final String FIELD_SHORT_NAME_ALT3 = "DataElementShortAlt3";

    private static final int VALID_FROM = 34000;
    private static final int VALID_TO = 2958465;
    private static final int AGG_START_LEVEL = 5;
    private static final String LAST_UPDATED = "2007-01-01";

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DataElementConverter()
    {
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<DataElement> elements = params.getDataElements();
        
        for ( DataElement object : elements )
        {
            writer.openElement( ELEMENT_NAME );
            
            writer.writeElement( FIELD_ID, String.valueOf( object.getId() ) );
            writer.writeElement( FIELD_SORT_ORDER, String.valueOf( object.getId() ) );
            writer.writeElement( FIELD_CODE, object.getCode() );
            writer.writeElement( FIELD_NAME, object.getName() );
            writer.writeElement( FIELD_SHORT_NAME, object.getShortName() );
            writer.writeElement( FIELD_DOS, object.getShortName().substring( 0, 8 ) );
            writer.writeElement( FIELD_PROMPT, object.getName() );
            writer.writeElement( FIELD_META, String.valueOf( 0 ) );
            writer.writeElement( FIELD_DATA_TYPE, String.valueOf( 1 ) );
            writer.writeElement( FIELD_PERIOD_TYPE, String.valueOf( 1 ) );
            writer.writeElement( FIELD_VALID_FROM, String.valueOf( VALID_FROM ) );
            writer.writeElement( FIELD_VALID_TO, String.valueOf( VALID_TO ) );
            writer.writeElement( FIELD_DESCRIPTION, object.getDescription() );
            writer.writeElement( FIELD_CALCULATED, String.valueOf( 0 ) );
            writer.writeElement( FIELD_SAVE_CALCULATED, String.valueOf( 0 ) );
            writer.writeElement( FIELD_AGGREGATION_START_LEVEL, String.valueOf( AGG_START_LEVEL ) );
            writer.writeElement( FIELD_AGGREGATION_OPERATOR, TypeHandler.convertAggregationOperatorTo( object.getAggregationOperator() ) );
            writer.writeElement( FIELD_SELECTED, String.valueOf( 0 ) );
            writer.writeElement( FIELD_LAST_USER, String.valueOf( 1 ) );
            writer.writeElement( FIELD_LAST_UPDATED, LAST_UPDATED );
            writer.writeElement( FIELD_NAME_ALT1, new String() );
            writer.writeElement( FIELD_SHORT_NAME_ALT1, new String() );
            writer.writeElement( FIELD_NAME_ALT2, new String() );
            writer.writeElement( FIELD_SHORT_NAME_ALT2, new String() );
            writer.writeElement( FIELD_NAME_ALT3, new String() );
            writer.writeElement( FIELD_SHORT_NAME_ALT3, new String() );
            
            writer.closeElement( ELEMENT_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
