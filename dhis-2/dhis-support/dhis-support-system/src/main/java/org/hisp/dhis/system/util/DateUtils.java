package org.hisp.dhis.system.util;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DateUtils
{
    public static final double DAYS_IN_YEAR = 365.0;
    
    /**
     * Formats a Date to the IXF date format which is YYYY-MM-DD'T'HH:MM:SS.
     * @param date the Date to parse.
     * @return A formatted date string.
     */
    public static String getLongDateString( Date date )
    {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern( "yyyy-MM-dd'T'HH:mm:ss" );
        
        return date != null ? format.format( date ) : null;
    }
    
    /**
     * Formats the current Date to the IXF date format which is YYYY-MM-DD'T'HH:MM:SS.
     * @return A formatted date string.
     */
    public static String getLongDateString()
    {
        return getLongDateString( Calendar.getInstance().getTime() );
    }
    
    /**
     * Formats a Date to the format YYYY-MM-DD.
     * @param date the Date to parse.
     * @return A formatted date string.
     */
    public static String getMediumDateString( Date date )
    {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern( "yyyy-MM-dd" );
        
        return date != null ? format.format( date ) : null;
    }
    
    /**
     * Formats the current Date to the format YYYY-MM-DD.
     * @return A formatted date string.
     */
    public static String getMediumDateString()
    {
        return getMediumDateString( Calendar.getInstance().getTime() );
    }
    
    /**
     * Parses a date from a String on the format YYYY-MM-DD.
     * @param dateString the String to parse.
     * @return a Date based on the given String.
     */
    public static Date getMediumDate( String dateString )
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern( "yyyy-MM-dd" );
        
            return dateString != null ? format.parse( dateString ) : null;
        }
        catch ( ParseException ex )
        {
            throw new RuntimeException( "Failed to parse medium date", ex ); 
        }
    }
        
    /**
     * Tests if the given base date is between the given start date and end date, including the dates themselves.
     * 
     * @param baseDate the date used as base for the test. 
     * @param startDate the start date.
     * @param endDate the end date.
     * @return <code>true</code> if the base date is between the start date and end date, <code>false</code> otherwise.
     */
    public static boolean between( Date baseDate, Date startDate, Date endDate )
    {
        if ( startDate.equals( endDate ) || endDate.before( startDate ) )
        {
            return false;
        }
        
        if ( ( startDate.before( baseDate ) || startDate.equals( baseDate ) ) && ( endDate.after( baseDate ) || endDate.equals( baseDate ) ) )
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Tests if the given base date is strictly between the given start date and end date.
     * 
     * @param baseDate the date used as base for the test. 
     * @param startDate the start date.
     * @param endDate the end date.
     * @return <code>true</code> if the base date is between the start date and end date, <code>false</code> otherwise.
     */
    public static boolean strictlyBetween( Date baseDate, Date startDate, Date endDate )
    {
        if ( startDate.equals( endDate ) || endDate.before( startDate ) )
        {
            return false;
        }
        
        if ( startDate.before( baseDate ) && endDate.after( baseDate ) )
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns the number of days between the start and end-date. The value
     * is rounded off to the floor value and does not take daylight saving time
     * into account.
     * 
     * @param startDate the start-date.
     * @param endDate the end-date.
     * @return the number of days between the start and end-date.
     */
    public static long getDays( Date startDate, Date endDate )
    {
        long msPrDay = 86400000;
        long ms = endDate.getTime() - startDate.getTime();
        
        return ms / msPrDay;
    }
    
    /**
     * Calculates the number of days between the start and end-date.
     * 
     * @param startDate the start date.
     * @param endDate the end date.
     * @return the number of days between the start and end date.
     */
    public static int daysBetween( Date startDate, Date endDate )
    {
        Days days = Days.daysBetween( new DateTime( startDate ), new DateTime( endDate ) );
        
        return days.getDays();
    }
    
    /**
     * Calculates the number of days between Epoch and the given date.
     * @param date the date.
     * @return the number of days between Epoch and the given date.
     */
    public static int daysSince1900( Date date )
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.clear();
        calendar.set( 1900, 0, 1 );
        
        return daysBetween( calendar.getTime(), date );
    }
    
    /**
     * Returns Epoch date, ie. 01/01/1970.
     * @return Epoch date, ie. 01/01/1970.
     */
    public static Date getEpoch()
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.clear();
        calendar.set( 1970, 0, 1 );
        
        return calendar.getTime();        
    }
}
