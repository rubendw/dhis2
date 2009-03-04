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
 * PeriodType for relative periods. Relative periods means periods relative to
 * a date, eg. current month, last three months, last six months, or so far this year.
 * This month will typically be used for reporting purposes and not for data entry.
 * The createPeriod-method will return the current month. The createPeriod( Date )
 * method will return the month in which the date resides.
 * 
 * @author Lars Helge Overland
 * @version $Id$
 */
public class RelativePeriodType
    extends PeriodType
{
    /**
     * The name of the RelativePeriodType, which is "Relative".
     */
    public static final String NAME = "Relative";

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
        return createPeriod( createCalendarInstance().getTime() );
    }

    @Override
    public Period createPeriod( Date date )
    {
        Calendar cal = createCalendarInstance( date );
        
        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMinimum( Calendar.DAY_OF_MONTH ) );
        
        Date startDate = cal.getTime();
        
        cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );
        
        Date endDate = cal.getTime();
        
        return new Period( this, startDate, endDate );
    }
}
