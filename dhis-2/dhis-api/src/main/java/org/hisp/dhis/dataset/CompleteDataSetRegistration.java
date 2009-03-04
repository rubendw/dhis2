package org.hisp.dhis.dataset;

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

import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CompleteDataSetRegistration
    implements Serializable
{
    private DataSet dataSet;

    private Period period;
    
    private Source source;
    
    private Date date;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CompleteDataSetRegistration()
    {   
    }

    public CompleteDataSetRegistration( DataSet dataSet, Period period, Source source, Date date )
    {
        this.dataSet = dataSet;
        this.period = period;
        this.source = source;
        this.date = date;
    }

    // -------------------------------------------------------------------------
    // HashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + ( ( dataSet == null ) ? 0 : dataSet.hashCode() );
        result = prime * result + ( ( period == null ) ? 0 : period.hashCode() );
        result = prime * result + ( ( source == null ) ? 0 : source.hashCode() );
        
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        final CompleteDataSetRegistration other = (CompleteDataSetRegistration) object;
        
        if ( dataSet == null )
        {
            if ( other.dataSet != null )
            {
                return false;
            }
        }
        else if ( !dataSet.equals( other.dataSet ) )
        {
            return false;
        }
        
        if ( period == null )
        {
            if ( other.period != null )
            {
                return false;
            }
        }
        else if ( !period.equals( other.period ) )
        {
            return false;
        }
        
        if ( source == null )
        {
            if ( other.source != null )
            {
                return false;
            }
        }
        else if ( !source.equals( other.source ) )
        {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString()
    {
        String toString = "[" + dataSet + ", " + period + ", " + source + ", " + date + "]";
        
        return toString;
    }
        
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( DataSet dataSet )
    {
        this.dataSet = dataSet;
    }

    public Period getPeriod()
    {
        return period;
    }

    public void setPeriod( Period period )
    {
        this.period = period;
    }
    
    public Source getSource()
    {
        return source;
    }

    public void setSource( Source source )
    {
        this.source = source;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }
}
