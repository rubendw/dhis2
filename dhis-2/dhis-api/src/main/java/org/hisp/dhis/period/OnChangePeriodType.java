package org.hisp.dhis.period;

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

import java.util.Calendar;
import java.util.Date;

/**
 * PeriodType for on-change Periods.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: OnChangePeriodType.java 3640 2007-10-15 12:05:12Z torgeilo $
 */
public class OnChangePeriodType
    extends PeriodType
{
    private static final int INSURMOUNTABLE_YEAR = 9999;

    /**
     * The name of the OnChangePeriodType, which is "OnChange".
     */
    public static final String NAME = "OnChange";

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public Period createPeriod()
    {
        return createPeriod( createCalendarInstance() );
    }

    @Override
    public Period createPeriod( Date date )
    {
        return createPeriod( createCalendarInstance( date ) );
    }

    private Period createPeriod( Calendar cal )
    {
        Date startDate = cal.getTime();

        cal.set( Calendar.YEAR, INSURMOUNTABLE_YEAR );
        cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return new Period( this, startDate, cal.getTime() );
    }

    // -------------------------------------------------------------------------
    // OnChange functionality
    // -------------------------------------------------------------------------

    /**
     * Sets the end date of the given Period to today - 1 day.
     * 
     * @param period the Period to change.
     */
    public static void setEndDateYesterday( Period period )
    {
        Calendar cal = createCalendarInstance();
        cal.add( Calendar.DAY_OF_YEAR, -1 );

        period.setEndDate( cal.getTime() );
    }

    /**
     * Sets the end date of the given Period to 9999/12/31.
     * 
     * @param period the Period to change.
     */
    public static void setEndDateInfinite( Period period )
    {
        Calendar cal = createCalendarInstance();
        cal.set( Calendar.YEAR, INSURMOUNTABLE_YEAR );
        cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        period.setEndDate( cal.getTime() );
    }
}
