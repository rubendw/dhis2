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
import org.hisp.dhis.period.DailyPeriodType;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.SixMonthlyPeriodType;
import org.hisp.dhis.period.TwoYearlyPeriodType;
import org.hisp.dhis.period.WeeklyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: PeriodTypeConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class PeriodTypeConverter
    implements XMLConverter
{
    private static final String ELEMENT_NAME = "DataPeriodType";
    
    private static final String FIELD_ID = "DataPeriodTypeID";
    private static final String FIELD_NAME = "DataPeriodTypeName";
    private static final String FIELD_NAME_ENG = "DataPeriodTypeNameEng";
    private static final String FIELD_SORT_ORDER = "SortOrder";
    private static final String FIELD_ANNUALISATION_FACTOR = "AnnualisationFactor";
    private static final String FIELD_UPPER_RANGE_DAY_COUNT = "UpperRangeDayCount";
    private static final String FIELD_FIRST_DAY_OF_WEEK = "FirstDayOfWeekID";
    private static final String FIELD_FIRST_WEEK_OF_YEAR = "FirstWeekOfYearID";
    private static final String FIELD_SUPPORTED = "Supported";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public PeriodTypeConverter()
    {   
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        // ---------------------------------------------------------------------
        // Daily
        // ---------------------------------------------------------------------
        
        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 1 ) );
        writer.writeElement( FIELD_NAME, DailyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, DailyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 1 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 365 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 0 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 0 ) );
        
        writer.closeElement( ELEMENT_NAME );
        
        // ---------------------------------------------------------------------
        // Weekly
        // ---------------------------------------------------------------------

        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 2 ) );
        writer.writeElement( FIELD_NAME, WeeklyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, WeeklyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 2 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 52 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 7 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 0 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 1 ) );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Monthly
        // ---------------------------------------------------------------------

        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 3 ) );
        writer.writeElement( FIELD_NAME, MonthlyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, MonthlyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 3 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 12 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 31 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 0 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 1 ) );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Quarterly
        // ---------------------------------------------------------------------

        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 4 ) );
        writer.writeElement( FIELD_NAME, QuarterlyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, QuarterlyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 4 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 4 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 92 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 0 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 0 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 1 ) );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Six-Monthly
        // ---------------------------------------------------------------------

        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 5 ) );
        writer.writeElement( FIELD_NAME, SixMonthlyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, SixMonthlyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 5 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 2 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 183 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 0 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 0 ) );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Yearly
        // ---------------------------------------------------------------------

        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 6 ) );
        writer.writeElement( FIELD_NAME, YearlyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, YearlyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 6 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 1 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 365 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 1 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 1 ) );
        
        writer.closeElement( ELEMENT_NAME );

        // ---------------------------------------------------------------------
        // Two-Yearly
        // ---------------------------------------------------------------------

        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 7 ) );
        writer.writeElement( FIELD_NAME, TwoYearlyPeriodType.NAME );
        writer.writeElement( FIELD_NAME_ENG, TwoYearlyPeriodType.NAME );
        writer.writeElement( FIELD_SORT_ORDER, String.valueOf( 7 ) );
        writer.writeElement( FIELD_ANNUALISATION_FACTOR, String.valueOf( 0.5 ) );
        writer.writeElement( FIELD_UPPER_RANGE_DAY_COUNT, String.valueOf( 721 ) );
        writer.writeElement( FIELD_FIRST_DAY_OF_WEEK, String.valueOf( 1 ) );
        writer.writeElement( FIELD_FIRST_WEEK_OF_YEAR, String.valueOf( 0 ) );
        writer.writeElement( FIELD_SUPPORTED, String.valueOf( 0 ) );
        
        writer.closeElement( ELEMENT_NAME );
    }

    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
