package org.hisp.dhis.dd.action.categorycombo;

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
import java.util.HashSet;

import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class ValidateDataElementCategoryComboAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }

    private DataElementCategoryService dataElementCategoryService;

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer dataElementCategoryComboId;

    public void setDataElementCategoryComboId( Integer dataElementCategoryComboId )
    {
        this.dataElementCategoryComboId = dataElementCategoryComboId;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Collection<String> selectedCategories = new HashSet<String>();

    public void setSelectedCategories( Collection<String> selectedCategories )
    {
        this.selectedCategories = selectedCategories;
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

            DataElementCategoryCombo match = dataElementCategoryComboService.getDataElementCategoryComboByName( name );

            if ( match != null && (dataElementCategoryComboId == null || match.getId() != dataElementCategoryComboId) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }

            for ( String id : selectedCategories )
            {
                DataElementCategory dataElementCategory = dataElementCategoryService.getDataElementCategory( Integer
                    .parseInt( id ) );

                if ( dataElementCategory.getCategoryOptions().size() <= 0 )
                {
                    message = i18n.getString( "no_option_assigned" );

                    return INPUT;
                }

            }
        }

        return SUCCESS;
    }
}
