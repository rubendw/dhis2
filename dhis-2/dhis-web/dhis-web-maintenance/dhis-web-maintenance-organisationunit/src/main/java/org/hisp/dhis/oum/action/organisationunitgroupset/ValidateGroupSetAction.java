package org.hisp.dhis.oum.action.organisationunitgroupset;

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
import java.util.Collection;
import java.util.Iterator;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidateGroupSetAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private boolean compulsory;

    public void setCompulsory( boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    private boolean exclusive;

    public void setExclusive( boolean exclusive )
    {
        this.exclusive = exclusive;
    }

    private Collection<String> selectedGroups;

    public void setSelectedGroups( Collection<String> selectedGroups )
    {
        this.selectedGroups = selectedGroups;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        // ---------------------------------------------------------------------
        // Validate values
        // ---------------------------------------------------------------------

        if ( name == null )
        {
            message = i18n.getString( "specify_a_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "specify_a_name" );

                return INPUT;
            }

            OrganisationUnitGroupSet match = organisationUnitGroupService.getOrganisationUnitGroupSetByName( name );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }
        }

        if ( description == null )
        {
            message = i18n.getString( "specify_a_description" );

            return INPUT;
        }
        else
        {
            description = description.trim();

            if ( description.length() == 0 )
            {
                message = i18n.getString( "specify_a_description" );

                return INPUT;
            }
        }

        if ( compulsory && (selectedGroups == null || selectedGroups.size() == 0) )
        {
            message = i18n.getString( "compulsory_must_have_member" );

            return INPUT;
        }

        // --------------------------------------------------------------------------------
        // When adding or updating an exclusive group set any unit in the
        // selected groups
        // can not be a member of more than one group
        // --------------------------------------------------------------------------------

        if ( exclusive && selectedGroups != null && selectedGroups.size() > 0 )
        {
            Collection<OrganisationUnit> allUnitsInGroupSet = new ArrayList<OrganisationUnit>();

            for ( String groupId : selectedGroups )
            {
                OrganisationUnitGroup group = organisationUnitGroupService.getOrganisationUnitGroup( Integer
                    .parseInt( groupId ) );

                allUnitsInGroupSet.addAll( group.getMembers() );
            }

            Iterator<OrganisationUnit> unitIterator = allUnitsInGroupSet.iterator();

            while ( unitIterator.hasNext() )
            {
                OrganisationUnit unit = unitIterator.next();

                unitIterator.remove();

                if ( allUnitsInGroupSet.contains( unit ) )
                {
                    message = i18n.getString( "the_group_set_can_not_be_creat_bec_it_is_exc_and" ) + " "
                        + unit.getShortName() + " " + i18n.getString( "is_a_member_of_more_than_one_selected_group" );

                    return INPUT;
                }
            }
        }

        message = "Everything's ok";

        return SUCCESS;
    }
}
