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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The superclass of all PeriodTypes.
 * 
 * @author Kristian Nordal
 * @version $Id: PeriodType.java 5297 2008-05-29 16:49:33Z larshelg $
 */
public abstract class PeriodType
    implements Serializable
{
    // -------------------------------------------------------------------------
    // Available PeriodTypes
    // -------------------------------------------------------------------------

    private static List<PeriodType> periodTypes;

    private static Map<String, PeriodType> periodTypeMap;

    static
    {
        periodTypes = new ArrayList<PeriodType>();
        periodTypes.add( new DailyPeriodType() );
        periodTypes.add( new WeeklyPeriodType() );
        periodTypes.add( new MonthlyPeriodType() );
        periodTypes.add( new QuarterlyPeriodType() );
        periodTypes.add( new SixMonthlyPeriodType() );
        periodTypes.add( new YearlyPeriodType() );
        periodTypes.add( new TwoYearlyPeriodType() );
        periodTypes.add( new OnChangePeriodType() );
        periodTypes.add( new RelativePeriodType() );
        //periodTypes.add( new SurveyPeriodType() );

        periodTypeMap = new HashMap<String, PeriodType>();

        for ( PeriodType periodType : periodTypes )
        {
            periodTypeMap.put( periodType.getName(), periodType );
        }
    }

    /**
     * Returns all available PeriodTypes in their natural order.
     * 
     * @return all available PeriodTypes in their natural order.
     */
    public static List<PeriodType> getAvailablePeriodTypes()
    {
        return new ArrayList<PeriodType>( periodTypes );
    }

    /**
     * Returns a PeriodType with a given name.
     * 
     * @param name the name of the PeriodType to return.
     * @return the PeriodType with the given name or null if no such PeriodType
     *         exists.
     */
    public static PeriodType getPeriodTypeByName( String name )
    {
        return periodTypeMap.get( name );
    }

    // -------------------------------------------------------------------------
    // Persistable
    // -------------------------------------------------------------------------

    private int id;

    public final void setId( int id )
    {
        this.id = id;
    }

    public final int getId()
    {
        return id;
    }

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    /**
     * Returns a unique name for the PeriodType.
     * 
     * @return a unique name for the PeriodType. E.g. "Monthly".
     */
    public abstract String getName();

    /**
     * Creates a valid Period based on the current date. E.g. if today is
     * January 5. 2007, a monthly PeriodType should return January 2007.
     * 
     * @return a valid Period based on the current date.
     */
    public abstract Period createPeriod();

    /**
     * Creates a valid Period based on the given date. E.g. the given date is
     * February 10. 2007, a monthly PeriodType should return February 2007.
     * 
     * @param date the date which is contained by the created period.
     * @return the valid Period based on the given date
     */
    public abstract Period createPeriod( Date date );

    // -------------------------------------------------------------------------
    // Calendar support
    // -------------------------------------------------------------------------

    /**
     * Returns an instance of a Calendar without any time of day, with the
     * current date.
     * 
     * @return an instance of a Calendar without any time of day.
     */
    public static final Calendar createCalendarInstance()
    {
        Calendar calendar = new GregorianCalendar();

        clearTimeOfDay( calendar );

        return calendar;
    }

    /**
     * Returns an instance of a Calendar without any time of day, with the given
     * date.
     * 
     * @param date the date of the Calendar.
     * @return an instance of a Calendar without any time of day.
     */
    public static final Calendar createCalendarInstance( Date date )
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime( date );

        clearTimeOfDay( calendar );

        return calendar;
    }

    /**
     * Clears the time of day in a Calendar instance.
     * 
     * @param calendar the Calendar to fix.
     */
    public static final void clearTimeOfDay( Calendar calendar )
    {
        calendar.clear( Calendar.MILLISECOND );
        calendar.clear( Calendar.SECOND );
        calendar.clear( Calendar.MINUTE );
        calendar.set( Calendar.HOUR_OF_DAY, 0 );
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof PeriodType) )
        {
            return false;
        }

        final PeriodType other = (PeriodType) o;

        return getName().equals( other.getName() );
    }
    
    @Override
    public String toString()
    {
        return "[" + getName() + "]";
    }
}
