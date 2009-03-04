package org.hisp.dhis.dd.action.target;

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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetService;

import com.opensymphony.xwork.Action;


/**
 * @author abyota
 */
public class ValidateTargetAction
    implements Action
{
	// -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

	private TargetService targetService;

    public void setTargetService( TargetService targetService )
    {
        this.targetService = targetService;
    }

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer targetId;

    public void setTargetId( Integer targetId )
    {
        this.targetId = targetId;
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
    
    private Double value;

    public void setValue( Double value )
    {
        this.value = value;
    }    

    private Integer periodId;
    
    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }

    private Integer indicatorId;

    public void setIndicatorId( Integer indicatorId )
    {
        this.indicatorId = indicatorId;
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
        if ( name == null )
        {
            message = i18n.getString( "specify_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "specify_name" );

                return INPUT;
            }
            
            Target match = targetService.getTargetByName( name );

            if ( match != null && ( targetId == null || match.getId() != targetId ) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }
        }

        if ( shortName == null )
        {
            message = i18n.getString( "specify_short_name" );

            return INPUT;
        }        
        else
        {
            shortName = shortName.trim();

            if ( shortName.length() == 0 )
            {
                message = i18n.getString( "specify_short_name" );

                return INPUT;
            }

            Target match = targetService.getTargetByShortName( name );

            if ( match != null && (targetId == null || match.getId() != targetId) )
            {
                message = i18n.getString( "short_name_in_use" );

                return INPUT;
            }
        }
        
        if ( value == null )
        {
            message = i18n.getString( "specify_value" );
            
            return INPUT;
        }
        
        if ( selectionManager.getSelectedOrganisationUnits().size() == 0 )
        {
            message = i18n.getString( "select_organisation_unit" );
            
            return INPUT;
        }
        
        if ( periodId == null )
        {       		        
            message = i18n.getString( "select_period" );

            return INPUT;
        }
        
        if ( indicatorId == null )
        {       		        
            message = i18n.getString( "select_indicator" );

            return INPUT;
        }

        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }
}
