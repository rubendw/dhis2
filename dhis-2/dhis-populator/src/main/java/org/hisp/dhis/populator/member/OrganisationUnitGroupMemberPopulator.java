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
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;
import org.hisp.dhis.populator.object.ObjectPopulator;

/**
 * @rule OrgUnitGroupId = OrgUnitId; OrgUnitId; ...
 * @author Torgeir Lorange Ostby
 * @version $Id: OrganisationUnitGroupMemberPopulator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class OrganisationUnitGroupMemberPopulator
    implements MemberPopulator
{
    private static final Log LOG = LogFactory.getLog( OrganisationUnitGroupMemberPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private ObjectPopulator organisationUnitObjectPopulator;

    public void setOrganisationUnitObjectPopulator( ObjectPopulator organisationUnitObjectPopulator )
    {
        this.organisationUnitObjectPopulator = organisationUnitObjectPopulator;
    }

    private ObjectPopulator organisationUnitGroupObjectPopulator;

    public void setOrganisationUnitGroupObjectPopulator( ObjectPopulator organisationUnitGroupObjectPopulator )
    {
        this.organisationUnitGroupObjectPopulator = organisationUnitGroupObjectPopulator;
    }

    // -------------------------------------------------------------------------
    // MemberPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String organisationUnitGroup = PopulatorUtils.getRuleId( rule );

        int organisationUnitGroupId = organisationUnitGroupObjectPopulator.getInternalId( organisationUnitGroup );

        for ( String organisationUnit : PopulatorUtils.getRuleArguments( rule ) )
        {
            int organisationUnitId = organisationUnitObjectPopulator.getInternalId( organisationUnit );

            LOG.debug( "dataElementService.addDataElementGroupMember( " + organisationUnitGroupId + ", "
                + organisationUnitId + " )" );

            OrganisationUnitGroup group = organisationUnitGroupService
                .getOrganisationUnitGroup( organisationUnitGroupId );

            OrganisationUnit unit = organisationUnitService.getOrganisationUnit( organisationUnitId );

            group.getMembers().add( unit );

            organisationUnitGroupService.updateOrganisationUnitGroup( group );
        }
    }
}
