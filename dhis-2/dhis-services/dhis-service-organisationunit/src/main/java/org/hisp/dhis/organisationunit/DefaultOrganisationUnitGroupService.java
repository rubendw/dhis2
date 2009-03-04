package org.hisp.dhis.organisationunit;

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

import org.hisp.dhis.system.util.UUIdUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultOrganisationUnitGroupService.java 5017 2008-04-25 09:19:19Z larshelg $
 */
public class DefaultOrganisationUnitGroupService
    implements OrganisationUnitGroupService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitGroupStore organisationUnitGroupStore;

    public void setOrganisationUnitGroupStore( OrganisationUnitGroupStore organisationUnitGroupStore )
    {
        this.organisationUnitGroupStore = organisationUnitGroupStore;
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    public int addOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        if ( organisationUnitGroup.getUuid() == null )
        {
            organisationUnitGroup.setUuid( UUIdUtils.getUUId() );
        }
        
        return organisationUnitGroupStore.addOrganisationUnitGroup( organisationUnitGroup );
    }

    public void updateOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        organisationUnitGroupStore.updateOrganisationUnitGroup( organisationUnitGroup );
    }

    public void deleteOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        organisationUnitGroupStore.deleteOrganisationUnitGroup( organisationUnitGroup );
    }

    public OrganisationUnitGroup getOrganisationUnitGroup( int id )
    {
        return organisationUnitGroupStore.getOrganisationUnitGroup( id );
    }

    public OrganisationUnitGroup getOrganisationUnitGroup( String uuid )
    {
        return organisationUnitGroupStore.getOrganisationUnitGroup( uuid );
    }

    public OrganisationUnitGroup getOrganisationUnitGroupByName( String name )
    {
        return organisationUnitGroupStore.getOrganisationUnitGroupByName( name );
    }

    public Collection<OrganisationUnitGroup> getAllOrganisationUnitGroups()
    {
        return organisationUnitGroupStore.getAllOrganisationUnitGroups();
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSet
    // -------------------------------------------------------------------------

    public int addOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        return organisationUnitGroupStore.addOrganisationUnitGroupSet( organisationUnitGroupSet );
    }

    public void updateOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        organisationUnitGroupStore.updateOrganisationUnitGroupSet( organisationUnitGroupSet );
    }

    public void deleteOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        organisationUnitGroupStore.deleteOrganisationUnitGroupSet( organisationUnitGroupSet );
    }

    public OrganisationUnitGroupSet getOrganisationUnitGroupSet( int id )
    {
        return organisationUnitGroupStore.getOrganisationUnitGroupSet( id );
    }

    public OrganisationUnitGroupSet getOrganisationUnitGroupSetByName( String name )
    {
        return organisationUnitGroupStore.getOrganisationUnitGroupSetByName( name );
    }

    public Collection<OrganisationUnitGroupSet> getAllOrganisationUnitGroupSets()
    {
        return organisationUnitGroupStore.getAllOrganisationUnitGroupSets();
    }

    public Collection<OrganisationUnitGroupSet> getCompulsoryOrganisationUnitGroupSets()
    {
        return organisationUnitGroupStore.getCompulsoryOrganisationUnitGroupSets();
    }

    public Collection<OrganisationUnitGroupSet> getExclusiveOrganisationUnitGroupSets()
    {
        return organisationUnitGroupStore.getExclusiveOrganisationUnitGroupSets();
    }

    public Collection<OrganisationUnitGroupSet> getExclusiveOrganisationUnitGroupSetsContainingGroup(
        OrganisationUnitGroup organisationUnitGroup )
    {
        HashSet<OrganisationUnitGroupSet> result = new HashSet<OrganisationUnitGroupSet>();

        Collection<OrganisationUnitGroupSet> exclusiveGroupSets = getExclusiveOrganisationUnitGroupSets();

        for ( OrganisationUnitGroupSet groupSet : exclusiveGroupSets )
        {
            if ( groupSet.getOrganisationUnitGroups().contains( organisationUnitGroup ) )
            {
                result.add( groupSet );
            }
        }

        return result;
    }
}
