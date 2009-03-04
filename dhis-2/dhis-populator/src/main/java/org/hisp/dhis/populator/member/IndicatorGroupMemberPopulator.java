package org.hisp.dhis.populator.member;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;
import org.hisp.dhis.populator.object.ObjectPopulator;

/**
 * @rule IndicatorGroupId = IndicatorId; IndicatorId; ...
 * @author Oyvind Brucker
 * @version $Id$
 */
public class IndicatorGroupMemberPopulator
    implements MemberPopulator
{
    private static final Log LOG = LogFactory.getLog( IndicatorGroupMemberPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    private ObjectPopulator indicatorObjectPopulator;

    public void setIndicatorObjectPopulator( ObjectPopulator indicatorObjectPopulator )
    {
        this.indicatorObjectPopulator = indicatorObjectPopulator;
    }

    private ObjectPopulator indicatorGroupObjectPopulator;

    public void setIndicatorGroupObjectPopulator( ObjectPopulator indicatorGroupObjectPopulator )
    {
        this.indicatorGroupObjectPopulator = indicatorGroupObjectPopulator;
    }

    // -------------------------------------------------------------------------
    // MemberPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String indicatorGroup = PopulatorUtils.getRuleId( rule );

        int indicatorGroupId = indicatorGroupObjectPopulator.getInternalId( indicatorGroup );

        for ( String indicator : PopulatorUtils.getRuleArguments( rule ) )
        {
            int indicatorId = indicatorObjectPopulator.getInternalId( indicator );

            LOG.debug( "indicatorStore.addIndicatorGroupMember( " + indicatorGroupId + ", " + indicatorId + " )" );

            IndicatorGroup group = indicatorService.getIndicatorGroup( indicatorGroupId );

            Indicator in = indicatorService.getIndicator( indicatorId );

            group.getMembers().add( in );

            indicatorService.updateIndicatorGroup( group );
        }
    }
}
