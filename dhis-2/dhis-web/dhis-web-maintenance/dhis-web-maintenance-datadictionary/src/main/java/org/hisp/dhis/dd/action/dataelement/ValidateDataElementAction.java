package org.hisp.dhis.dd.action.dataelement;

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
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @author Hans S. Toemmerholt
 * @version $Id: ValidateDataElementAction.java 2869 2007-02-20 14:26:09Z
 *          andegje $
 */
public class ValidateDataElementAction
    implements Action
{
	// -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
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

    private String alternativeName;

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String code;

    public void setCode( String code )
    {
        this.code = code;
    }

    // For Calculated Data Elements

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private String calculated;

    public void setCalculated( String calculated )
    {
        this.calculated = calculated;
    }

    private Collection<String> dataElementIds;

    public void setDataElementIds( Collection<String> dataElementIds )
    {
        this.dataElementIds = dataElementIds;
    }
    
    private Integer selectedCategoryComboId;
    
    public void setSelectedCategoryComboId( Integer selectedCategoryComboId )
    {
        this.selectedCategoryComboId = selectedCategoryComboId;
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
        // Validating DataElement fields
        // ----------------------------------------------------------------------

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

            DataElement match = dataElementService.getDataElementByName( name );

            if ( match != null && (id == null || match.getId() != id) )
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

            DataElement match = dataElementService.getDataElementByShortName( shortName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "short_name_in_use" );

                return INPUT;
            }
        }

        if ( alternativeName != null && alternativeName.trim().length() != 0 )
        {
            DataElement match = dataElementService.getDataElementByAlternativeName( alternativeName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "alternative_name_in_use" );

                return INPUT;
            }
        }

        if ( code != null && code.trim().length() != 0 )
        {
            DataElement match = dataElementService.getDataElementByCode( code );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "code_in_use" );

                return INPUT;
            }
        }       
        
        if ( selectedCategoryComboId == null )
        {
            message = i18n.getString( "select_categorycombo" );

            return INPUT;
        }

        // ---------------------------------------------------------------------
        // Validating CalculatedDataElement fields
        // ---------------------------------------------------------------------

        if ( calculated != null && calculated.equals( "on" ) )
        {
            if ( type != null && (!type.equals( DataElement.TYPE_INT )) )
            {
                message = i18n.getString( "cde_must_be_number" );

                return INPUT;
            }
            
            if ( dataElementIds != null && dataElementIds.size() > 1 )
            {
                DataElement dataElement;
                DataElementCategoryOptionCombo optionCombo;
                
                message = "";
                
                for ( String operandId : dataElementIds )
                {
                    String dataElementIdStr = operandId.substring( 0, operandId.indexOf('.') );
                    String optionComboIdStr = operandId.substring( operandId.indexOf('.')+1, operandId.length() );                    
                    
                    dataElement = dataElementService.getDataElement( Integer.parseInt(dataElementIdStr) );
                    optionCombo = dataElementCategoryOptionComboService.getDataElementCategoryOptionCombo( Integer.parseInt( optionComboIdStr ) );
                    
                    if ( !dataElement.getType().equals( DataElement.TYPE_INT ) || optionCombo == null )
                    {
                        message += dataElement.getName() + ", ";
                    }
                }
                
                if ( ! message.equals("") )
                {
                    message = i18n.getString( "cde_data_elements_must_be_numbers" ) + ": " +message.substring(0, message.length()-2);
                    return INPUT;
                }                
            }
            else
            {
                message = i18n.getString( "cde_must_have_data_elements" );

                return INPUT;
            }
        }

        // ---------------------------------------------------------------------
        // Validation success
        // ---------------------------------------------------------------------

        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }
}
