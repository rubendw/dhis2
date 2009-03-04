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
 * PeriodType for daily Periods. A valid daily Period has equal startDate and
 * endDate.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: DailyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class DailyPeriodType
    extends CalendarPeriodType
{
    /**
     * The name of the DailyPeriodType, which is "Daily".
     */
    public static final String NAME = "Daily";

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
        Date date = createCalendarInstance().getTime();

        return new Period( this, date, date );
    }

    @Override
    public Period createPeriod( Date date )
    {
        Date fixedDate = createCalendarInstance( date ).getTime();

        return new Period( this, fixedDate, fixedDate );
    }

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public Period getNextPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.add( Calendar.DAY_OF_YEAR, 1 );

        Date date = cal.getTime();

        return new Period( this, date, date );
    }

    @Override
    public Period getPreviousPeriod( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.add( Calendar.DAY_OF_YEAR, -1 );

        Date date = cal.getTime();

        return new Period( this, date, date );
    }

    /**
     * Generates daily Periods for the whole month in which the given Period's
     * startDate exists.
     */
    @Override
    public List<Period> generatePeriods( Period period )
    {
        Calendar cal = createCalendarInstance( period.getStartDate() );
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        int month = cal.get( Calendar.MONTH );

        ArrayList<Period> days = new ArrayList<Period>();

        while ( cal.get( Calendar.MONTH ) == month )
        {
            Date date = cal.getTime();

            days.add( new Period( this, date, date ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return days;
    }
}
