package org.hisp.dhis.oum.action.organisationunitgroup;

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

import java.util.Collection;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.oust.manager.SelectionTreeManager;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ValidateOrganisationUnitGroupAction.java 1898 2006-09-22 12:06:56Z torgeilo $
 */
public class ValidateOrganisationUnitGroupAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
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

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
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
            message = i18n.getString( "please_specify_a_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "please_specify_a_name" );

                return INPUT;
            }

            OrganisationUnitGroup match = organisationUnitGroupService.getOrganisationUnitGroupByName( name );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "name_is_already_in_use" );

                return INPUT;
            }
        }

        // ---------------------------------------------------------------------
        // When updating a group which is a member of an exclusive group set,
        // any selected units can not be a member of another group in the
        // group set.
        // ---------------------------------------------------------------------

        // No need to check this if it's a new organisations group.
        if ( id == null )
        {
            message = "Everything's ok";

            return SUCCESS;
        }

        OrganisationUnitGroup organisationUnitGroup = organisationUnitGroupService.getOrganisationUnitGroup( id
            .intValue() );

        Collection<OrganisationUnitGroupSet> exclusiveGroupSets = organisationUnitGroupService
            .getExclusiveOrganisationUnitGroupSetsContainingGroup( organisationUnitGroup );

        if ( exclusiveGroupSets != null && exclusiveGroupSets.size() > 0 )
        {
            Collection<OrganisationUnit> selectedUnits = selectionTreeManager.getSelectedOrganisationUnits();

            for ( OrganisationUnitGroupSet groupSet : exclusiveGroupSets )
            {
                for ( OrganisationUnitGroup group : groupSet.getOrganisationUnitGroups() )
                {
                    for ( OrganisationUnit unit : selectedUnits )
                    {
                        if ( group.getMembers().contains( unit ) && group.getId() != id )
                        {
                            message = unit.getShortName() + " "
                                + i18n.getString( "can_not_be_a_member_because_member_of" ) + " " + group.getName()
                                + " " + i18n.getString( "which_is_a_member_of_the_same_exclusive_group_set" ) + " "
                                + groupSet.getName() + " " + i18n.getString( "as_the_current_group" );

                            return INPUT;
                        }
                    }
                }
            }
        }

        message = "Everything's ok";

        return SUCCESS;
    }
}
