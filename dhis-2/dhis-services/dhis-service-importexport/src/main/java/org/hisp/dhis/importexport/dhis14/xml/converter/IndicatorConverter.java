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

import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.indicator.Indicator;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class IndicatorConverter
    implements XMLConverter
{
    private static final String ELEMENT_NAME = "Indicator";
    
    private static final String FIELD_ID = "IndicatorID";
    private static final String FIELD_SORT_ORDER = "SortOrder";
    private static final String FIELD_NAME = "IndicatorName";
    private static final String FIELD_SHORT_NAME = "ShortName";
    private static final String FIELD_DOS = "IndicatorDOS";
    private static final String FIELD_VALID_FROM = "ValidFrom";
    private static final String FIELD_VALID_TO = "ValidTo";
    private static final String FIELD_DESCRIPTION = "IndicatorDescription";
    private static final String FIELD_SELECTED = "Selected";
    private static final String FIELD_INDICATOR_TYPE = "IndicatorTypeID";
    private static final String FIELD_DATA_PERIOD_TYPE = "DataPeriodTypeID";
    private static final String FIELD_ANNUALISED = "Annualised";
    private static final String FIELD_NUMERATOR = "IndicatorNumerator";
    private static final String FIELD_NUMERATOR_AGG_LEVEL = "IndicatorNumeratorAggregateStartLevel";
    private static final String FIELD_NUMERATOR_TIME_LAG = "IndicatorNumeratorTimeLag";
    private static final String FIELD_NUMERATOR_DESCRIPTION = "IndicatorNumeratorDescription";
    private static final String FIELD_DENOMINATOR = "IndicatorDenominator";
    private static final String FIELD_DENOMINATOR_AGG_LEVEL = "IndicatorDenominatorAggregateStartLevel";
    private static final String FIELD_DENOMINATOR_TIME_LAG = "IndicatorDenominatorTimeLag";
    private static final String FIELD_DENOMINATOR_DESCRIPTION = "IndicatorDenominatorDescription";
    private static final String FIELD_LAST_USER = "LastUserID";
    private static final String FIELD_LAST_UPDATED = "LastUpdated";
    
    private static final int VALID_FROM = 34000;
    private static final int VALID_TO = 2958465;
    private static final int AGG_START_LEVEL = 5;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public IndicatorConverter()
    {   
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<Indicator> indicators = params.getIndicators();
        
        for ( Indicator object : indicators )
        {
            writer.openElement( ELEMENT_NAME );
            
            writer.writeElement( FIELD_ID, String.valueOf( object.getId() ) );
            writer.writeElement( FIELD_SORT_ORDER, String.valueOf( object.getId() ) );
            writer.writeElement( FIELD_NAME, object.getName() );
            writer.writeElement( FIELD_SHORT_NAME, object.getShortName() );
            writer.writeElement( FIELD_DOS, object.getShortName().substring( 0, 8 ) );
            writer.writeElement( FIELD_VALID_FROM, String.valueOf( VALID_FROM ) );
            writer.writeElement( FIELD_VALID_TO, String.valueOf( VALID_TO ) );
            writer.writeElement( FIELD_DESCRIPTION, object.getDescription() );
            writer.writeElement( FIELD_SELECTED, String.valueOf( 0 ) );
            writer.writeElement( FIELD_INDICATOR_TYPE, "" ); //TODO
            writer.writeElement( FIELD_DATA_PERIOD_TYPE, "" ); //TODO
            writer.writeElement( FIELD_ANNUALISED, String.valueOf( 0 ) );
            writer.writeElement( FIELD_NUMERATOR, object.getNumerator() ); //TODO
            writer.writeElement( FIELD_NUMERATOR_AGG_LEVEL, String.valueOf( AGG_START_LEVEL ) );
            writer.writeElement( FIELD_NUMERATOR_TIME_LAG, String.valueOf( 0 ) );
            writer.writeElement( FIELD_NUMERATOR_DESCRIPTION, object.getNumeratorDescription() );
            writer.writeElement( FIELD_DENOMINATOR, object.getDenominator() ); //TODO
            writer.writeElement( FIELD_DENOMINATOR_AGG_LEVEL, String.valueOf( AGG_START_LEVEL ) );
            writer.writeElement( FIELD_DENOMINATOR_TIME_LAG, String.valueOf( 0 ) );
            writer.writeElement( FIELD_DENOMINATOR_DESCRIPTION, object.getNumeratorDescription() );
            writer.writeElement( FIELD_LAST_USER, new String() ); //TODO
            writer.writeElement( FIELD_LAST_UPDATED, new String() ); //TODO
            
            writer.closeElement( ELEMENT_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
