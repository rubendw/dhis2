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

import java.util.Collection;

/**
 * Defines methods for working with OrganisationUnitGroups and
 * OrganisationUnitGroupSets.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: OrganisationUnitGroupService.java 3286 2007-05-07 18:05:21Z larshelg $
 */
public interface OrganisationUnitGroupService
{
    String ID = OrganisationUnitGroupService.class.getName();

    // -------------------------------------------------------------------------
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    /**
     * Adds an OrganisationUnitGroup.
     * 
     * @param organisationUnitGroup the OrganisationUnitGroup to add.
     * @return a generated unique id of the added OrganisationUnitGroup.
     */
    int addOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup );

    /**
     * Updates an OrganisationUnitGroup.
     * 
     * @param organisationUnitGroup the OrganisationUnitGroup to update.
     */
    void updateOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup );

    /**
     * Deletes an OrganisationUnitGroup.
     * 
     * @param organisationUnitGroup the OrganisationUnitGroup to delete.
     */
    void deleteOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup );

    /**
     * Returns an OrganisationUnitGroup.
     * 
     * @param id the id of the OrganisationUnitGroup.
     * @return the OrganisationGroup with the given id, or null if no match.
     */
    OrganisationUnitGroup getOrganisationUnitGroup( int id );

    /**
     * Returns the OrganisationUnitGroup with the given UUID.
     * 
     * @param id the UUID of the OrganisationUnitGroup.
     * @return the OrganisationGroup with the given UUID, or null if no match.
     */
    OrganisationUnitGroup getOrganisationUnitGroup( String uuid );

    /**
     * Returns an OrganisationUnitGroup with a given name.
     * 
     * @param name the name of the OrganisationUnitGroup.
     * @return the OrganisationUnitGroup with the given name, or null if no
     *         match.
     */
    OrganisationUnitGroup getOrganisationUnitGroupByName( String name );

    /**
     * Returns all OrganisationUnitGroups.
     * 
     * @return a collection of all the OrganisationUnitGroups, or an empty
     *         collection if no OrganisationUnitGroup exists.
     */
    Collection<OrganisationUnitGroup> getAllOrganisationUnitGroups();

    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSet
    // -------------------------------------------------------------------------

    /**
     * Adds an OrganisationUnitGroupSet.
     * 
     * @param organisationUnitGroupSet the OrganisationUnitGroupSet to add.
     * @return the generated unique id of the added OrganisationUnitGroupSet.
     */
    int addOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet );

    /**
     * Updates an OrganisationUnitGroupSet.
     * 
     * @param organisationUnitGroupSet the OrganisationUnitGroupSet to update.
     */
    void updateOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet );

    /**
     * Deletes an OrganisationUnitGroupSet.
     * 
     * @param organisationUnitGroupSet the OrganisationUnitGroupSet to delete.
     */
    void deleteOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet );

    /**
     * Returns an OrganisationUnitGroupSet.
     * 
     * @param id the id of the OrganisationUnitGroupSet to return.
     * @return the OrganisationUnitGroupSet with the given id, or null if no
     *         match.
     */
    OrganisationUnitGroupSet getOrganisationUnitGroupSet( int id );

    /**
     * Returns an OrganisationUnitGroupSet with a given name.
     * 
     * @param name the name of the OrganisationUnitGroupSet to return.
     * @return the OrganisationUnitGroupSet with the given name, or null if no
     *         match.
     */
    OrganisationUnitGroupSet getOrganisationUnitGroupSetByName( String name );

    /**
     * Returns all OrganisationUnitGroupSets.
     * 
     * @return a collection of all OrganisationUnitGroupSets, or an empty
     *         collection if no OrganisationUnitGroupSet exists.
     */
    Collection<OrganisationUnitGroupSet> getAllOrganisationUnitGroupSets();

    /**
     * Returns all compulsory OrganisationUnitGroupSets.
     * 
     * @return a collection of all compulsory OrganisationUnitGroupSets, or an
     *         empty collection if there are no compulsory
     *         OrganisationUnitGroupSets.
     */
    Collection<OrganisationUnitGroupSet> getCompulsoryOrganisationUnitGroupSets();

    /**
     * Returns all exclusive OrganisationUnitGroupSets.
     * 
     * @return a collection of all exclusive OrganisationUnitGroupSets, or an
     *         empty collection if there are no exclusive
     *         OrganisationUnitGroupSets.
     */
    Collection<OrganisationUnitGroupSet> getExclusiveOrganisationUnitGroupSets();

    /**
     * Returns a collection of all exclusive OrganisationUnitGroupSets
     * containing a given OrganisationUnitGroup.
     * 
     * @param organisationUnitGroup the OrganisationUnitGroup to look for.
     * @return a collection of all exclusive OrganisationUnitGroupSets
     *         containing the given OrganisationUnitGroup, or an empty
     *         collection if no such OrganisationUnitGroupSet exists.
     */
    Collection<OrganisationUnitGroupSet> getExclusiveOrganisationUnitGroupSetsContainingGroup(
        OrganisationUnitGroup organisationUnitGroup );
}
