package org.hisp.dhis.dataintegrity;

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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;

/**
 * @author Fredrik Fjeld
 * @version $Id$
 */
public interface DataIntegrityService
{
    String ID = DataIntegrityService.class.getName();

    // -------------------------------------------------------------------------
    // DataIntegrityService
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    /**
     * Gets all data elements which are not assigned to any data set.
     */
    Collection<DataElement> getDataElementsWithoutDataSet();

    /**
     * Gets all data elements which are not members of any groups.
     */
    Collection<DataElement> getDataElementsWithoutGroups();

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    /**
     * Gets all data sets which are not assigned to any organisation units.
     */
    Collection<DataSet> getDataSetsNotAssignedToOrganisationUnits();

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    /**
     * Gets all indicators whith blank numerator or denominator.
     */
    Collection<Indicator> getIndicatorsWithBlankFormulas();

    /**
     * Gets all indicators with identical numerator and denominator.
     */
    Collection<Indicator> getIndicatorsWithIdenticalFormulas();

    /**
     * Gets all indicators which are not assigned to any groups.
     */
    Collection<Indicator> getIndicatorsWithoutGroups();
    
    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    /**
     * Gets all organisation units which are related to each other in a cyclic reference.
     */
    Collection<OrganisationUnit> getOrganisationUnitsWithCyclicReferences();

    /**
     * Gets all organisation units with no parents or children.
     */
    Collection<OrganisationUnit> getOrphanedOrganisationUnits();

    /**
     * Gets all organisation units which are not assigned to any groups.
     */
    Collection<OrganisationUnit> getOrganisationUnitsWithoutGroups();

    /**
     * Gets all organisation units which are not members of one or more groups
     * which enter into a compulsory group set.
     */
    Collection<OrganisationUnit> getOrganisationUnitsViolatingCompulsoryGroupSets();

    /**
     * Gets all organisation units which are members of more than one group
     * which enter into an exclusive group set.
     */
    Collection<OrganisationUnit> getOrganisationUnitsViolatingExclusiveGroupSets();

    /**
     * Gets all organisation unit groups which are not assigned to any group set.
     */
    Collection<OrganisationUnitGroup> getOrganisationUnitGroupsWithoutGroupSets();
}
