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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * PeriodType for weekly Periods. A valid weekly Period has startDate set to
 * monday and endDate set to sunday the same week, assuming monday is the first
 * day and sunday is the last day of the week.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: WeeklyPeriodType.java 2976 2007-03-03 22:50:19Z torgeilo $
 */
public class WeeklyPeriodType
    extends CalendarPeriodType
{
    /**
     * The name of the WeeklyPeriodType, which is "Weekly".
     */
    public static final String NAME = "Weekly";

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
        cal.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );

        Date startDate = cal.getTime();

        cal.add( Calendar.DAY_OF_YEAR, 6 );

        return new Period( this, startDate, cal.getTime() );
    }

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public Period getNextPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
        cal.add( Calendar.WEEK_OF_YEAR, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.DAY_OF_YEAR, 6 );

        return new Period( this, startDate, cal.getTime() );
    }

    @Override
    public Period getPreviousPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
        cal.add( Calendar.WEEK_OF_YEAR, -1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.DAY_OF_YEAR, 6 );

        return new Period( this, startDate, cal.getTime() );
    }

    /**
     * Generates weekly Periods for the whole year in which the given Period's
     * startDate exists.
     */
    @Override
    public List<Period> generatePeriods( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );

        // ---------------------------------------------------------------------
        // If the suplied period is the first week of a year where the start
        // date is in the year before, we want to generate weeks for the year
        // of the end date
        // ---------------------------------------------------------------------

        if ( period.getPeriodType().equals( this ) )
        {
            Calendar cal2 = createCalendarInstance( period.getEndDate() );

            if ( cal.get( Calendar.YEAR ) != cal2.get( Calendar.YEAR ) )
            {
                // Use saturday as Calendar has sunday as first day of week.
                cal2.add( Calendar.DAY_OF_WEEK, -1 );

                if ( cal2.get( Calendar.WEEK_OF_YEAR ) == 1 )
                {
                    cal = cal2;
                    cal2 = null;
                }
            }
        }

        // ---------------------------------------------------------------------
        // Generate weeks
        // ---------------------------------------------------------------------

        cal.set( Calendar.WEEK_OF_YEAR, 1 );
        cal.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );

        int firstWeek = cal.get( Calendar.WEEK_OF_YEAR );

        ArrayList<Period> weeks = new ArrayList<Period>();

        Date startDate = cal.getTime();
        cal.add( Calendar.DAY_OF_YEAR, 6 );
        weeks.add( new Period( this, startDate, cal.getTime() ) );
        cal.add( Calendar.DAY_OF_YEAR, 1 );

        while ( cal.get( Calendar.WEEK_OF_YEAR ) != firstWeek )
        {
            startDate = cal.getTime();
            cal.add( Calendar.DAY_OF_YEAR, 6 );
            weeks.add( new Period( this, startDate, cal.getTime() ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return weeks;
    }
}
