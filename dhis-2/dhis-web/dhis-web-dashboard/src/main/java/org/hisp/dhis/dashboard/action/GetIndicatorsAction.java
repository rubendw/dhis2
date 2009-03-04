package org.hisp.dhis.dashboard.action;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id: GetIndicatorsAction.java 3305 2007-05-14 18:55:52Z larshelg $
 */
public class GetIndicatorsAction
    extends ActionSupport
{
    private final int ALL = 0;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    private Comparator<Indicator> indicatorComparator;

    public void setIndicatorComparator( Comparator<Indicator> indicatorComparator )
    {
        this.indicatorComparator = indicatorComparator;
    }

    // -------------------------------------------------------------------------
    // DisplayPropertyHandler
    // -------------------------------------------------------------------------

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }

    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private List<Indicator> indicators;

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( id == ALL )
        {
            indicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );
        }
        else
        {
            IndicatorGroup indicatorGroup = indicatorService.getIndicatorGroup( id );

            if ( indicatorGroup != null )
            {
                indicators = new ArrayList<Indicator>( indicatorGroup.getMembers() );
            }
            else
            {
                indicators = new ArrayList<Indicator>();
            }
        }

        Collections.sort( indicators, indicatorComparator );

        indicators = displayPropertyHandler.handleIndicators( indicators );

        return SUCCESS;
    }
}
