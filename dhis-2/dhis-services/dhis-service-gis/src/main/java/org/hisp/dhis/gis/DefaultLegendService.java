package org.hisp.dhis.gis;

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

import java.util.Set;

import org.hisp.dhis.gis.LegendService;
import org.hisp.dhis.indicator.Indicator;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public class DefaultLegendService
    implements LegendService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LegendStore legendStore;

    public void setLegendStore( LegendStore legendStore )
    {
        this.legendStore = legendStore;
    }

    // -------------------------------------------------------------------------
    // LegendService implementation
    // -------------------------------------------------------------------------

    public LegendStore getLegendStore()
    {
        return legendStore;
    }

    public void addLegend( Legend legend )
    {
        legendStore.addLegend( legend );
    }

    public void deleteLegend( Legend legend )
    {
        legendStore.deleteLegend( legend );
    }

    public Legend getLegend( int legendId )
    {
        return legendStore.getLegend( legendId );
    }

    public Set<Legend> getLegendByMaxMin( double max, double min )
    {
        return legendStore.getLegendByMaxMin( max, min );
    }

    public void updateLegend( Legend legend )
    {
        legendStore.updateLegend( legend );
    }

    public Set<Legend> getAllLegend()
    {
        return legendStore.getAllLegend();
    }

    public Legend getLegendByName( String arg0 )
    {
        return legendStore.getLegendByName( arg0 );
    }

    public void addLegendSet( LegendSet legendSet )
    {
        legendStore.addLegendSet( legendSet );
    }

    public void deleteLegendSet( LegendSet legendSet )
    {
        legendStore.deleteLegendSet( legendSet );
    }

    public Set<LegendSet> getAllLegendSet()
    {
        return legendStore.getAllLegendSet();
    }

    public LegendSet getLegendSet( int id )
    {
        return legendStore.getLegendSet( id );
    }

    public LegendSet getLegendSet( String name )
    {
        return legendStore.getLegendSet( name );
    }

    public LegendSet getLegendSet( Indicator indicator )
    {
        return legendStore.getLegendSet( indicator );
    }

    public void updateLegendSet( LegendSet legendSet )
    {
        legendStore.updateLegendSet( legendSet );
    }
}
