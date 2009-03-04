package org.hisp.dhis.dd.action.category;

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
import java.util.Set;

import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrder;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrderService;

import com.opensymphony.xwork.Action;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class UpdateDataElementCategoryAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementCategoryService dataElementCategoryService;

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    private DataElementCategoryOptionService dataElementCategoryOptionService;

    public void setDataElementCategoryOptionService( DataElementCategoryOptionService dataElementCategoryOptionService )
    {
        this.dataElementCategoryOptionService = dataElementCategoryOptionService;
    }

    private DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService;

    public void setDataElementDimensionColumnOrderService(
        DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService )
    {
        this.dataElementDimensionColumnOrderService = dataElementDimensionColumnOrderService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer dataElementCategoryId;

    public void setDataElementCategoryId( Integer dataElementCategoryId )
    {
        this.dataElementCategoryId = dataElementCategoryId;
    }

    private String nameField;

    public void setNameField( String nameField )
    {
        this.nameField = nameField;
    }

    private Collection<String> selectedList = new HashSet<String>();

    public void setSelectedList( Collection<String> selectedList )
    {
        this.selectedList = selectedList;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Update data element category
        // ---------------------------------------------------------------------

        DataElementCategory dataElementCategory = dataElementCategoryService
            .getDataElementCategory( dataElementCategoryId );

        if ( !dataElementCategory.getName().equals( nameField ) )
        {
            dataElementCategory.setName( nameField );
        }

        Set<DataElementCategoryOption> updatedCategoryOptions = new HashSet<DataElementCategoryOption>();

        for ( String id : selectedList )
        {
            DataElementCategoryOption dataElementCategoryOption = dataElementCategoryOptionService
                .getDataElementCategoryOption( Integer.parseInt( id ) );

            updatedCategoryOptions.add( dataElementCategoryOption );

        }

        if( ! dataElementCategory.getCategoryOptions().containsAll(updatedCategoryOptions) )
        {
        	dataElementCategory.setCategoryOptions( updatedCategoryOptions );
        }     

        dataElementCategoryService.updateDataElementCategory( dataElementCategory );

        
        int displayOrder = 1;
        
        DataElementDimensionColumnOrder columnOrder = null;

        for ( String id : selectedList )
        {
            DataElementCategoryOption dataElementCategoryOption = dataElementCategoryOptionService
                .getDataElementCategoryOption( Integer.parseInt( id ) );

            columnOrder = dataElementDimensionColumnOrderService.getDataElementDimensionColumnOrder(
                dataElementCategory, dataElementCategoryOption );

            if ( columnOrder == null )
            {
                columnOrder = new DataElementDimensionColumnOrder( dataElementCategory, dataElementCategoryOption,
                    displayOrder );
                dataElementDimensionColumnOrderService.addDataElementDimensionColumnOrder( columnOrder );
            }
            else
            {
                columnOrder.setDisplayOrder( displayOrder );
                dataElementDimensionColumnOrderService.updateDataElementDimensionColumnOrder( columnOrder );
            }

            displayOrder++;

        }

        return SUCCESS;
    }
}
