package org.hisp.dhis.importexport.ixf.util;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.period.DailyPeriodType;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.WeeklyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: MappingUtil.java 5307 2008-05-30 16:08:45Z larshelg $
 */
public class IXFMappingUtil
{
    private static Map<String, String> periodTypeNameToIXFMapping;
    
    private static Map<String, String> periodTypeNameFromIXFMapping;
    
    private static Map<String, PeriodType> periodTypeFromIXFMapping;
    
    static
    {
        periodTypeNameToIXFMapping = new HashMap<String, String>();
        
        periodTypeNameToIXFMapping.put( YearlyPeriodType.NAME, "Year" );
        periodTypeNameToIXFMapping.put( MonthlyPeriodType.NAME, "Month" );
        periodTypeNameToIXFMapping.put( WeeklyPeriodType.NAME, "Week" );
        periodTypeNameToIXFMapping.put( DailyPeriodType.NAME, "Day" );
        
        periodTypeNameFromIXFMapping = new HashMap<String, String>();
        
        periodTypeNameFromIXFMapping.put( "Year", YearlyPeriodType.NAME );
        periodTypeNameFromIXFMapping.put( "Month", MonthlyPeriodType.NAME );
        periodTypeNameFromIXFMapping.put( "Week", WeeklyPeriodType.NAME );
        periodTypeNameFromIXFMapping.put( "Day", DailyPeriodType.NAME );
        
        periodTypeFromIXFMapping = new HashMap<String, PeriodType>();
        
        periodTypeFromIXFMapping.put( "Year", new YearlyPeriodType() );
        periodTypeFromIXFMapping.put( "Month", new MonthlyPeriodType() );
        periodTypeFromIXFMapping.put( "Week", new WeeklyPeriodType() );
        periodTypeFromIXFMapping.put( "Day", new DailyPeriodType() );
    }
    
    public static String getPeriodTypeToIXF( String type )
    {
        return periodTypeNameToIXFMapping.get( type );
    }
    
    public static String getPeriodTypeFromIXF( String type )
    {
        return periodTypeNameFromIXFMapping.get( type );
    }
    
    public static Period getPeriodFromIXF( String periodicity, Date startDate )
    {
        return periodTypeFromIXFMapping.get( periodicity ).createPeriod( startDate );
    }
    
    /**
     * Returns the IXF equivalent textual Indicator type based on the given IndicatorType.
     * @param type the IndicatorType.
     * @return the IXF equivalent textual Indicator type based on the given IndicatorType.
     */
    public static String getIndicatorType( IndicatorType type )
    {
        if ( type.getFactor() == 100 )
        {
            return "percent";
        }
        else
        {
            return "count";
        }
    }
}
