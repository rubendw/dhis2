package org.hisp.dhis.oum.action.hierarchy;

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

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: MoveOrganisationUnitAction.java 1898 2006-09-22 12:06:56Z torgeilo $
 */
public class MoveOrganisationUnitAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer organisationUnitToMoveId;

    public void setOrganisationUnitToMoveId( Integer organisationUnitToMoveId )
    {
        this.organisationUnitToMoveId = organisationUnitToMoveId;
    }

    private Integer newParentOrganisationUnitId;

    public void setNewParentOrganisationUnitId( Integer newParentOrganisationUnitId )
    {
        this.newParentOrganisationUnitId = newParentOrganisationUnitId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Get parent
        // ---------------------------------------------------------------------

        OrganisationUnit newParent;

        if ( newParentOrganisationUnitId != null )
        {
            newParent = organisationUnitService.getOrganisationUnit( newParentOrganisationUnitId.intValue() );
        }
        else
        {
            newParent = selectionManager.getRootOrganisationUnitsParent();
        }

        // ---------------------------------------------------------------------
        // Update unit to move
        // ---------------------------------------------------------------------

        OrganisationUnit unitToMove = organisationUnitService.getOrganisationUnit( organisationUnitToMoveId.intValue() );

        unitToMove.setParent( newParent );

        organisationUnitService.updateOrganisationUnit( unitToMove );

        organisationUnitService.addOrganisationUnitHierarchy( new Date() );

        return SUCCESS;
    }
}
