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
 * PeriodType for monthly Periods. A valid monthly Period has startDate set to
 * the first day of a calendar month, and endDate set to the last day of the
 * same month.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: MonthlyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class MonthlyPeriodType
    extends CalendarPeriodType
{
    /**
     * The name of the MonthlyPeriodType, which is "Monthly".
     */
    public static final String NAME = "Monthly";

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
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        Date startDate = cal.getTime();

        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return new Period( this, startDate, cal.getTime() );
    }

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public Period getNextPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.DAY_OF_MONTH, 1 );
        cal.add( Calendar.MONTH, 1 );

        Date startDate = cal.getTime();

        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return new Period( this, startDate, cal.getTime() );
    }

    @Override
    public Period getPreviousPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.DAY_OF_MONTH, 1 );
        cal.add( Calendar.MONTH, -1 );

        Date startDate = cal.getTime();

        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return new Period( this, startDate, cal.getTime() );
    }

    /**
     * Generates monthly Periods for the whole year in which the given Period's
     * startDate exists.
     */
    @Override
    public List<Period> generatePeriods( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.MONTH, Calendar.JANUARY );
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        int year = cal.get( Calendar.YEAR );

        ArrayList<Period> months = new ArrayList<Period>();

        while ( cal.get( Calendar.YEAR ) == year )
        {
            Date startDate = cal.getTime();
            cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );
            months.add( new Period( this, startDate, cal.getTime() ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return months;
    }
}
