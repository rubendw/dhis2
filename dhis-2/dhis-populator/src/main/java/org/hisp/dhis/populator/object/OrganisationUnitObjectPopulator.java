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

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;

/**
 * @rule id = name; shortName; unitCode; openingDate; closingDate; active; comment
 * @rule id = name; parentId; shortName; unitCode; openingDate; closingDate; active; comment
 * @author Torgeir Lorange Ostby
 * @version $Id: OrganisationUnitObjectPopulator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class OrganisationUnitObjectPopulator
    extends ObjectPopulator
{
    private static final Log LOG = LogFactory.getLog( OrganisationUnitObjectPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // ObjectPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String id = PopulatorUtils.getRuleId( rule );

        List<String> arguments = PopulatorUtils.getRuleArguments( rule );

        if ( arguments.size() == 7 )
        {
            String name = arguments.get( 0 );
            String shortName = arguments.get( 1 );
            String unitCode = arguments.get( 2 );
            Date openingDate = PopulatorUtils.toDate( arguments.get( 3 ) );
            Date closingDate = PopulatorUtils.toDate( arguments.get( 4 ) );
            boolean active = PopulatorUtils.toBoolean( arguments.get( 5 ) );
            String comment = arguments.get( 6 );

            LOG.debug( "organisationUnitStore.addOrganisationUnit( " + name + ", " + shortName + ", " + unitCode + ", "
                + openingDate + ", " + closingDate + ", " + active + ", " + comment + " )" );

            OrganisationUnit organisationUnit = new OrganisationUnit( name, shortName, unitCode, openingDate,
                closingDate, active, comment );

            int internalId = organisationUnitService.addOrganisationUnit( organisationUnit );

            addIdMapping( id, internalId );
        }
        else if ( arguments.size() == 8 )
        {
            String name = arguments.get( 0 );
            int parentId = getInternalId( arguments.get( 1 ) );
            String shortName = arguments.get( 2 );
            String unitCode = arguments.get( 3 );
            Date openingDate = PopulatorUtils.toDate( arguments.get( 4 ) );
            Date closingDate = PopulatorUtils.toDate( arguments.get( 5 ) );
            boolean active = PopulatorUtils.toBoolean( arguments.get( 6 ) );
            String comment = arguments.get( 7 );

            LOG.debug( "organisationUnitStore.addOrganisationUnit( " + name + ", " + parentId + ", " + shortName + ", "
                + unitCode + ", " + openingDate + ", " + closingDate + ", " + active + ", " + comment + " )" );

            OrganisationUnit parentUnit = organisationUnitService.getOrganisationUnit( parentId );

            OrganisationUnit organisationUnit = new OrganisationUnit( name, parentUnit, shortName, unitCode,
                openingDate, closingDate, active, comment );

            int internalId = organisationUnitService.addOrganisationUnit( organisationUnit );

            addIdMapping( id, internalId );
        }
        else
        {
            throw new PopulatorException( "Wrong number of arguments, must be 7 or 8 {" + rule + "}" );
        }
    }
}
