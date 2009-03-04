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
 * PeriodType for two-yearly Periods. A valid two-yearly Period has startDate
 * set to January 1st on an even year (2000, 2002, 2004, etc), and endDate set
 * to the last day of the next year.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: TwoYearlyPeriodType.java 2975 2007-03-03 22:24:36Z torgeilo $
 */
public class TwoYearlyPeriodType
    extends CalendarPeriodType
{
    /**
     * The name of the TwoYearlyPeriodType, which is "TwoYearly".
     */
    public static final String NAME = "TwoYearly";

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
        cal.set( Calendar.YEAR, cal.get( Calendar.YEAR ) - cal.get( Calendar.YEAR ) % 2 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.YEAR, 1 );
        cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return new Period( this, startDate, cal.getTime() );
    }

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public Period getNextPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.YEAR, cal.get( Calendar.YEAR ) - cal.get( Calendar.YEAR ) % 2 + 2 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.YEAR, 1 );
        cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return new Period( this, startDate, cal.getTime() );
    }

    @Override
    public Period getPreviousPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.YEAR, cal.get( Calendar.YEAR ) - cal.get( Calendar.YEAR ) % 2 - 2 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.YEAR, 1 );
        cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return new Period( this, startDate, cal.getTime() );
    }

    /**
     * Generates two-yearly Periods for the rounded-off decade in which the
     * given Period's startDate exists.
     */
    @Override
    public List<Period> generatePeriods( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.add( Calendar.YEAR, -(cal.get( Calendar.YEAR ) % 20) );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        ArrayList<Period> twoYears = new ArrayList<Period>();

        for ( int i = 0; i < 10; ++i )
        {
            Date startDate = cal.getTime();
            cal.add( Calendar.YEAR, 1 );
            cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );
            twoYears.add( new Period( this, startDate, cal.getTime() ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return twoYears;
    }
}
