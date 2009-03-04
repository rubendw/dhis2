package org.hisp.dhis.oust.action;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitNameComparator;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ExpandSubtreeAction.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class ExpandSubtreeAction
    implements Action
{
    private static final Log LOG = LogFactory.getLog( ExpandSubtreeAction.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private int parentId;

    public void setParentId( int organisationUnitId )
    {
        this.parentId = organisationUnitId;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnit> parents = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getParents()
    {
        return parents;
    }

    private Map<OrganisationUnit, List<OrganisationUnit>> childrenMap = new HashMap<OrganisationUnit, List<OrganisationUnit>>();

    public Map<OrganisationUnit, List<OrganisationUnit>> getChildrenMap()
    {
        return childrenMap;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        try
        {
            OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentId );

            if ( parent == null )
            {
                throw new RuntimeException( "OrganisationUnit with id " + parentId + " doesn't exist" );
            }

            addParentWithChildren( parent );
        }
        catch ( Exception e )
        {
            LOG.error( e.getMessage(), e );

            throw e;
        }

        return SUCCESS;
    }

    private void addParentWithChildren( OrganisationUnit parent )
        throws Exception
    {
        List<OrganisationUnit> children = getChildren( parent );

        parents.add( parent );

        childrenMap.put( parent, children );

        for ( OrganisationUnit child : children )
        {
            boolean hasChildren = child.getChildren().size() > 0; // Dirty
            // loading

            LOG.debug( "OrganisationUnit " + child.getId() + " has children = " + hasChildren );
        }
    }

    private final List<OrganisationUnit> getChildren( OrganisationUnit parent )
    {
        List<OrganisationUnit> children = new ArrayList<OrganisationUnit>( parent.getChildren() );

        Collections.sort( children, new OrganisationUnitNameComparator() );

        return children;
    }
}
