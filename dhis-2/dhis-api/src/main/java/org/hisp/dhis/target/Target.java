package org.hisp.dhis.target;

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

import org.hisp.dhis.common.MetaObject;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Martin Myrseth
 * @version $Id$
 */
public class Target
    implements Serializable, MetaObject
{	
    private int id;
    
    private Indicator indicator;
    
    private Source source;
    
    private Period period;

    private String name;

    private String shortName;

    private String description;
    
    private double value;
    
    private double currentValue;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Target()
    {
    }
    
    public Target( Indicator indicator, Source source, Period period )
    {
        this.indicator = indicator;
        this.source = source;
        this.period = period;
    }

    public Target( Indicator indicator, Source source, Period period,
        String name, String shortName, String description, double value )
    {
        this.indicator = indicator;
        this.source = source;
        this.period = period;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.value = value;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
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

        if ( !(o instanceof Target) )
        {
            return false;
        }

        final Target other = (Target) o;

        return name.equals( other.getName() );
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

    public Indicator getIndicator()
    {
        return this.indicator;
    }
    
    public void setIndicator( Indicator indicator )
    {
        this.indicator = indicator;
    }
    
    public Source getSource()
    {
        return this.source;
    }
    
    public void setSource( Source source )
    {
        this.source = source;
    }
    
    public Period getPeriod()
    {
        return this.period;
    }
    
    public void setPeriod( Period period )
    {
        this.period = period;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
    
    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
    
    public double getValue()
    {
        return value;
    }
    
    public void setValue( double value )
    {
        this.value = value;
    }
    
    public double getCurrentValue()
    {
        return currentValue;
    }
    
    public int getCurrentStatus()
    {
    	double statusValue = ( currentValue / value ) * 100;
        
    	if ( statusValue > 100 ) 
    	{
    		statusValue = 100;
    	}
        
    	if ( statusValue < 0 )
    	{
    		statusValue = 0;
    	}
   
        return (int) Math.round( statusValue );
    }
    
    public void setCurrentValue( double currentValue )
    {
        this.currentValue = currentValue;
    }
}
