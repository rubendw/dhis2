package org.hisp.dhis.importexport.dhis14.util;

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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.period.DailyPeriodType;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.OnChangePeriodType;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.SixMonthlyPeriodType;
import org.hisp.dhis.period.TwoYearlyPeriodType;
import org.hisp.dhis.period.WeeklyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: Dhis14ObjectMappingUtil.java 4699 2008-03-11 14:56:56Z larshelg $
 */
public class Dhis14ObjectMappingUtil
{
    private static Map<Integer, PeriodType> periodTypeMap;
    
    private static Map<Integer, String> dataElementTypeMap;
    
    static
    {
        periodTypeMap = new HashMap<Integer, PeriodType>();
        
        periodTypeMap.put( 1, new MonthlyPeriodType() );
        periodTypeMap.put( 2, new QuarterlyPeriodType() );
        periodTypeMap.put( 3, new YearlyPeriodType() );
        periodTypeMap.put( 4, new OnChangePeriodType() );
        periodTypeMap.put( 5, new DailyPeriodType() ); // Should be Survey, which is un-supported
        periodTypeMap.put( 6, new DailyPeriodType() ); 
        periodTypeMap.put( 7, new WeeklyPeriodType() );
        periodTypeMap.put( 8, new SixMonthlyPeriodType() );
        periodTypeMap.put( 9, new TwoYearlyPeriodType() );
    }
    
    static
    {
        dataElementTypeMap = new HashMap<Integer, String>();
        
        dataElementTypeMap.put( 3, DataElement.TYPE_INT );
        dataElementTypeMap.put( 4, DataElement.TYPE_STRING );
        dataElementTypeMap.put( 5, DataElement.TYPE_BOOL );
    }

    public static Map<Integer, PeriodType> getPeriodTypeMap()
    {
        return periodTypeMap;
    }
        
    public static Map<Integer, String> getDataElementTypeMap()
    {
        return dataElementTypeMap;
    }
}
