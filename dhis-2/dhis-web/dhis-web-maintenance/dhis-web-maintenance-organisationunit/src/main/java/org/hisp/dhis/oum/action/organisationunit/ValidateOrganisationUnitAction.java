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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ValidateOrganisationUnitAction.java 1898 2006-09-22 12:06:56Z torgeilo $
 */
public class ValidateOrganisationUnitAction
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

    private String closedDate;

    public void setClosedDate( String closedDate )
    {
        this.closedDate = closedDate;
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

            OrganisationUnit match = organisationUnitService.getOrganisationUnitByName( name );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }
        }

        if ( shortName == null )
        {
            message = i18n.getString( "specify_a_short_name" );

            return INPUT;
        }
        else
        {
            shortName = shortName.trim();

            OrganisationUnit match = organisationUnitService.getOrganisationUnitByShortName( shortName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "short_name_in_use" );

                return INPUT;
            }
        }

        if ( organisationUnitCode != null && organisationUnitCode.trim().length() == 0 )
        {
            organisationUnitCode = null;
        }
        else
        {
            organisationUnitCode = organisationUnitCode.trim();

            OrganisationUnit match = organisationUnitService.getOrganisationUnitByCode( organisationUnitCode );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "org_unit_code_in_use" );

                return INPUT;
            }
        }

        Date oDate;

        if ( openingDate == null || openingDate.trim().length() == 0 )
        {
            message = i18n.getString( "specify_an_opening_date" );

            return INPUT;
        }
        else
        {
            oDate = format.parseDate( openingDate.trim() );

            if ( oDate == null )
            {
                message = i18n.getString( "enter_a_valid_opening_date" );

                return INPUT;
            }
        }

        if ( closedDate == null || closedDate.trim().length() == 0 )
        {
        }
        else
        {
            OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( id.intValue() );

            if ( organisationUnit.getChildren().size() != 0 )
            {
                message = i18n.getString( "org_unit_with_children_cannot_be_closed" );

                return INPUT;
            }

            Date cDate = format.parseDate( closedDate.trim() );

            if ( cDate == null )
            {
                message = i18n.getString( "enter_a_valid_opening_date" );

                return INPUT;
            }

            if ( cDate.before( oDate ) )
            {
                message = i18n.getString( "closed_date_cannot_be_before_opening_date" );

                return INPUT;
            }
        }

        message = "Everything's ok";

        return SUCCESS;
    }
}
