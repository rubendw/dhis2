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
 * PeriodType for six-monthly Periods. A valid six-monthly Period has startDate
 * set to either January 1st or July 1st, and endDate set to the last day of the
 * fifth month after the startDate.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SixMonthlyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class SixMonthlyPeriodType
    extends CalendarPeriodType
{
    /**
     * The name of the SixMonthlyPeriodType, which is "SixMonthly".
     */
    public static final String NAME = "SixMonthly";

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
        cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - cal.get( Calendar.MONTH ) % 6 );
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.MONTH, 5 );
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
        cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - cal.get( Calendar.MONTH ) % 6 + 6 );
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.MONTH, 5 );
        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return new Period( this, startDate, cal.getTime() );
    }

    @Override
    public Period getPreviousPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - cal.get( Calendar.MONTH ) % 6 - 6 );
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.MONTH, 5 );
        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return new Period( this, startDate, cal.getTime() );
    }

    /**
     * Generates six-monthly Periods for a rounded-off 5 years span in which the
     * given Period's startDate exists.
     */
    @Override
    public List<Period> generatePeriods( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.add( Calendar.YEAR, -(cal.get( Calendar.YEAR ) % 5) );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        ArrayList<Period> sixMonths = new ArrayList<Period>();

        for ( int i = 0; i < 10; ++i )
        {
            Date startDate = cal.getTime();
            cal.add( Calendar.MONTH, 5 );
            cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );
            sixMonths.add( new Period( this, startDate, cal.getTime() ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return sixMonths;
    }
}
