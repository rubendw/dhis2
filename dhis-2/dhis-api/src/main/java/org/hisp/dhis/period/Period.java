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
import java.util.Date;

/**
 * @author Kristian Nordal
 * @version $Id: Period.java 5277 2008-05-27 15:48:42Z larshelg $
 */
public class Period
    implements Serializable
{
    private int id;

    /**
     * Required.
     */
    private PeriodType periodType;

    /**
     * Required. Must be unique together with endDate.
     */
    private Date startDate;

    /**
     * Required. Must be unique together with startDate.
     */
    private Date endDate;
    
    /**
     * Convenience property.
     */
    private transient String name;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Period()
    {
    }

    public Period( PeriodType periodType, Date startDate, Date endDate )
    {
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        int prime = 31;
        int result = 1;

        result = result * prime + startDate.hashCode();
        result = result * prime + endDate.hashCode();
        result = result * prime + periodType.hashCode();
        
        return result;
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

        if ( !(o instanceof Period) )
        {
            return false;
        }

        final Period other = (Period) o;

        return startDate.equals( other.getStartDate() ) && 
            endDate.equals( other.getEndDate() ) && 
            periodType.equals( other.getPeriodType() );
    }

    @Override
    public String toString()
    {
        return "[" + periodType.getName() + ": " + startDate + " - " + endDate + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public PeriodType getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType( PeriodType periodType )
    {
        this.periodType = periodType;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
