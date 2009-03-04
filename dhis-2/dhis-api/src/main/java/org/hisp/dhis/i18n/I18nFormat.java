package org.hisp.dhis.i18n;

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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.hisp.dhis.period.Period;

/**
 * @author Pham Thi Thuy
 * @author Nguyen Dang Quang
 * @version $Id: I18nFormat.java 2974 2007-03-03 22:11:13Z torgeilo $
 */
public class I18nFormat
{
    private ResourceBundle resourceBundle;

    public I18nFormat( ResourceBundle resourceBundle )
    {
        this.resourceBundle = resourceBundle;
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    private DateFormatSymbols dateFormatSymbols;

    public void init()
    {
        String[] months = { "month.january", "month.february", "month.march", "month.april", "month.may", "month.june",
            "month.july", "month.august", "month.september", "month.october", "month.november", "month.december" };
        String[] shortMonths = { "month.short.january", "month.short.february", "month.short.march",
            "month.short.april", "month.short.may", "month.short.june", "month.short.july", "month.short.august",
            "month.short.september", "month.short.october", "month.short.november", "month.short.december" };
        String[] weekdays = { "weekday.sunday", "weekday.monday", "weekday.tuesday", "weekday.wednesday",
            "weekday.thursday", "weekday.friday", "weekday.saturday" };
        String[] shortWeekdays = { "weekday.short.sunday", "weekday.short.monday", "weekday.short.tuesday",
            "weekday.short.wednesday", "weekday.short.thursday", "weekday.short.friday", "weekday.short.saturday" };

        for ( int i = 0; i < 12; ++i )
        {
            months[i] = resourceBundle.getString( months[i] );
            shortMonths[i] = resourceBundle.getString( shortMonths[i] );
        }

        for ( int i = 0; i < 7; ++i )
        {
            weekdays[i] = resourceBundle.getString( weekdays[i] );
            shortWeekdays[i] = resourceBundle.getString( shortWeekdays[i] );
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormatSymbols = dateFormat.getDateFormatSymbols();

        dateFormatSymbols.setMonths( months );
        dateFormatSymbols.setShortMonths( shortMonths );
        dateFormatSymbols.setWeekdays( weekdays );
        dateFormatSymbols.setShortWeekdays( shortWeekdays );
    }

    // -------------------------------------------------------------------------
    // Format methods
    // -------------------------------------------------------------------------

    public Date parseDate( String date )
    {
        if ( date == null )
        {
            return null;
        }

        return commonParsing( date, resourceBundle.getString( "format.date" ) );
    }

    public Date parseTime( String time )
    {
        if ( time == null )
        {
            return null;
        }

        return commonParsing( time, resourceBundle.getString( "format.time" ) );
    }

    public Date parseDateTime( String dateTime )
    {
        if ( dateTime == null )
        {
            return null;
        }

        return commonParsing( dateTime, resourceBundle.getString( "format.datetime" ) );
    }

    public String formatDate( Date date )
    {
        if ( date == null )
        {
            return null;
        }

        return commonFormatting( date, resourceBundle.getString( "format.date" ) );
    }

    public String formatTime( Date date )
    {
        if ( date == null )
        {
            return null;
        }

        return commonFormatting( date, resourceBundle.getString( "format.time" ) );
    }

    public String formatDateTime( Date date )
    {
        if ( date == null )
        {
            return null;
        }

        return commonFormatting( date, resourceBundle.getString( "format.datetime" ) );
    }

    public String formatPeriod( Period period )
    {
        String typeName = period.getPeriodType().getName();

        String keyStartDate = "format." + typeName + ".startDate";
        String keyEndDate = "format." + typeName + ".endDate";

        String startDate = commonFormatting( period.getStartDate(), resourceBundle.getString( keyStartDate ) );
        String endDate = commonFormatting( period.getEndDate(), resourceBundle.getString( keyEndDate ) );

        return Character.toUpperCase( startDate.charAt( 0 ) ) + startDate.substring( 1 ) + endDate;
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private Date commonParsing( String input, String pattern )
    {
        DateFormat dateFormat = new SimpleDateFormat( pattern, dateFormatSymbols );

        Date parsedDate;

        try
        {
            parsedDate = dateFormat.parse( input );
        }
        catch ( ParseException e )
        {
            return null;
        }

        if ( !commonFormatting( parsedDate, pattern ).equals( input ) )
        {
            return null;
        }

        return parsedDate;
    }

    private String commonFormatting( Date date, String pattern )
    {
        DateFormat dateFormat = new SimpleDateFormat( pattern, dateFormatSymbols );

        return dateFormat.format( date );
    }
}
