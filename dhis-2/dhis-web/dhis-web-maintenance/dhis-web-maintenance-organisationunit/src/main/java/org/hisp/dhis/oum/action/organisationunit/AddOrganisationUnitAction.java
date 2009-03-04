package org.hisp.dhis.oum.action.organisationunit;

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

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AddOrganisationUnitAction.java 1898 2006-09-22 12:06:56Z torgeilo $
 */
public class AddOrganisationUnitAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

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
    // Input
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String organisationUnitCode;

    public void setOrganisationUnitCode( String organisationUnitCode )
    {
        this.organisationUnitCode = organisationUnitCode;
    }

    private String openingDate;

    public void setOpeningDate( String openingDate )
    {
        this.openingDate = openingDate;
    }

    private String comment;

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( organisationUnitCode != null && organisationUnitCode.trim().length() == 0 )
        {
            organisationUnitCode = null;
        }

        if ( comment != null && comment.trim().length() == 0 )
        {
            comment = null;
        }

        Date date = format.parseDate( openingDate );

        // ---------------------------------------------------------------------
        // Get parent
        // ---------------------------------------------------------------------

        OrganisationUnit parent = selectionManager.getSelectedOrganisationUnit();

        if ( parent == null )
        {
            // -----------------------------------------------------------------
            // If no unit is selected, the parent is the parent of the roots in
            // the tree
            // -----------------------------------------------------------------

            parent = selectionManager.getRootOrganisationUnitsParent();
        }

        // ---------------------------------------------------------------------
        // Create organisation unit
        // ---------------------------------------------------------------------

        OrganisationUnit organisationUnit = new OrganisationUnit( name, shortName, 
            organisationUnitCode, date, null, true, comment );
        
        organisationUnit.setParent( parent );

        organisationUnitService.addOrganisationUnit( organisationUnit );

        organisationUnitService.addOrganisationUnitHierarchy( date );

        return SUCCESS;
    }
}
