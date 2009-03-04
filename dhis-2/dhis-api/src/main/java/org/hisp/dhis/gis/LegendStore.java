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

import java.util.Set;

import org.hisp.dhis.indicator.Indicator;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public interface LegendStore
{
    public void addLegend( Legend legend );

    public void deleteLegend( Legend legend );

    public void updateLegend( Legend legend );

    public Legend getLegend( int id );

    public Set<Legend> getLegendByMaxMin( double max, double min );

    public Set<Legend> getAllLegend();

    public Legend getLegendByName( String name );

    public void addLegendSet( LegendSet legendSet );

    public void deleteLegendSet( LegendSet legendSet );

    public void updateLegendSet( LegendSet legendSet );

    public Set<LegendSet> getAllLegendSet();

    public LegendSet getLegendSet( int id );

    public LegendSet getLegendSet( String name );

    public LegendSet getLegendSet( Indicator indicator );
}
