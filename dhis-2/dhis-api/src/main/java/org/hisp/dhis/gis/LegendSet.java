package org.hisp.dhis.gis;

/*
 * Copyright (c) 2004-2007, University of Oslo All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the HISP project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.indicator.Indicator;

public class LegendSet
    implements Serializable
{
    private int id;

    private String name;

    private List<Legend> legends = new ArrayList<Legend>();

    private Set<Indicator> indicators = new HashSet<Indicator>();

    public LegendSet()
    {
    }

    public LegendSet( String name )
    {
        this.name = name;
    }

    public void addLegend( Legend legend )
    {
        this.legends.add( legend );
    }

    public void addIndicator( Indicator indicator )
    {
        this.indicators.add( indicator );
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<Legend> getLegends()
    {
        return legends;
    }

    public void setLegends( List<Legend> legends )
    {
        this.legends = legends;
    }

    public Set<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( Set<Indicator> indicators )
    {
        this.indicators = indicators;
    }
    
    public void sortLegend(Comparator<Legend> comparator){
    	Collections.sort(this.legends, comparator);	
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        
        final LegendSet other = (LegendSet) obj;
        if ( id != other.id )
            return false;
        
        return true;
    }
}
