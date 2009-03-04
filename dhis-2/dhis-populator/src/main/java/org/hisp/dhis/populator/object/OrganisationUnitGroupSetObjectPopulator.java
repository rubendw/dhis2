package org.hisp.dhis.populator.object;

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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;

/**
 * @rule id = name; description; compulsory; exclusive
 * @author Torgeir Lorange Ostby
 * @version $Id: OrganisationUnitGroupSetObjectPopulator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class OrganisationUnitGroupSetObjectPopulator
    extends ObjectPopulator
{
    private static final Log LOG = LogFactory.getLog( OrganisationUnitGroupSetObjectPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    // -------------------------------------------------------------------------
    // ObjectPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String id = PopulatorUtils.getRuleId( rule );

        List<String> arguments = PopulatorUtils.getRuleArguments( rule );

        if ( arguments.size() != 4 )
        {
            throw new PopulatorException( "Wrong number of arguments, must be 4 {" + rule + "}" );
        }

        String name = arguments.get( 0 );
        String description = arguments.get( 1 );
        boolean compulsory = PopulatorUtils.toBoolean( arguments.get( 2 ) );
        boolean exclusive = PopulatorUtils.toBoolean( arguments.get( 3 ) );

        OrganisationUnitGroupSet groupSet = new OrganisationUnitGroupSet( name, description, compulsory, exclusive );

        LOG.debug( "organisationUnitStore.addOrganisationUnitGroupSet( " + name + ", " + description + ", "
            + compulsory + ", " + exclusive + " )" );

        int internalId = organisationUnitGroupService.addOrganisationUnitGroupSet( groupSet );

        addIdMapping( id, internalId );
    }
}
