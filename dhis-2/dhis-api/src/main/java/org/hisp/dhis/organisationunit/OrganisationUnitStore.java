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
import java.util.Date;

/**
 * Defines methods for persisting OrganisationUnits.
 * 
 * @author Kristian Nordal
 * @version $Id: OrganisationUnitStore.java 5645 2008-09-04 10:01:02Z larshelg $
 */
public interface OrganisationUnitStore
{
    String ID = OrganisationUnitStore.class.getName();

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    /**
     * Returns the OrganisationUnit with the given UUID.
     * 
     * @param uuid the UUID of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given UUID, or null if no match.
     */
    OrganisationUnit getOrganisationUnit( String uuid );

    /**
     * Returns an OrganisationUnit with a given name.
     * 
     * @param name the name of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given name, or null if not match.
     */
    OrganisationUnit getOrganisationUnitByName( String name );

    /**
     * Returns an OrganisationUnit with a given short name.
     * 
     * @param shortName the short name of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given short name, or null if no
     *         match.
     */
    OrganisationUnit getOrganisationUnitByShortName( String shortName );

    /**
     * Returns an OrganisationUnit with a given code.
     * 
     * @param code the code of the OrgananisationUnit to return.
     * @return the OrganisationUnit with the given code, or null if no match.
     */
    OrganisationUnit getOrganisationUnitByCode( String code );

    /**
     * Returns all root OrganisationUnits. A root OrganisationUnit is an
     * OrganisationUnit with no parent/has the parent set to null.
     * 
     * @return a collection containing all root OrganisationUnits, or an empty
     *         collection if there are no OrganisationUnits.
     */
    Collection<OrganisationUnit> getRootOrganisationUnits();

    // -------------------------------------------------------------------------
    // OrganisationUnitHierarchy
    // -------------------------------------------------------------------------

    /**
     * This method is not intended to be used by other modules!
     */
    int addOrganisationUnitHierarchy( Date date );

    /**
     * This method is not intended to be used by other modules!
     */
    OrganisationUnitHierarchy getOrganisationUnitHierarchy( int id );
    
    /**
     * This method is not intended to be used by other modules!
     */
    OrganisationUnitHierarchy getLatestOrganisationUnitHierarchy();
    
    /**
     * This method is not intended to be used by other modules!
     */
    void removeOrganisationUnitHierarchies( Date date );
    
    /**
     * This method is not intended to be used by other modules!
     */
    Collection<OrganisationUnitHierarchy> getOrganisationUnitHierarchies( Date startDate, Date endDate );
    
    /**
     * This method is not intended to be used by other modules!
     */    
    void deleteOrganisationUnitHierarchies();
    
    /**
     * This method is not intended to be used by other modules!
     */
    Collection<Integer> getChildren( OrganisationUnitHierarchy hierarchy, int parentId );

    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    int addOrganisationUnitLevel( OrganisationUnitLevel level );
    
    void updateOrganisationUnitLevel( OrganisationUnitLevel level );
    
    OrganisationUnitLevel getOrganisationUnitLevel( int id );
    
    void deleteOrganisationUnitLevel( OrganisationUnitLevel level );
    
    void deleteOrganisationUnitLevels();
    
    Collection<OrganisationUnitLevel> getOrganisationUnitLevels();
}
