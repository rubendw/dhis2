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
import java.util.Set;

import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrder;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrderService;

import com.opensymphony.xwork.Action;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class UpdateDataElementCategoryComboAction
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

    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService(
        DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    private DataElementDimensionRowOrderService dataElementDimensionRowOrderService;

    public void setDataElementDimensionRowOrderService(
        DataElementDimensionRowOrderService dataElementDimensionRowOrderService )
    {
        this.dataElementDimensionRowOrderService = dataElementDimensionRowOrderService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer dataElementCategoryComboId;

    public void setDataElementCategoryComboId( Integer dataElementCategoryComboId )
    {
        this.dataElementCategoryComboId = dataElementCategoryComboId;
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
        DataElementCategoryCombo dataElementCategoryCombo = dataElementCategoryComboService
            .getDataElementCategoryCombo( dataElementCategoryComboId );

        if ( !dataElementCategoryCombo.getName().equals( nameField ) )
        {
            dataElementCategoryCombo.setName( nameField );
        }

        Set<DataElementCategory> updatedCategories = new HashSet<DataElementCategory>();

        for ( String id : selectedList )
        {
            DataElementCategory dataElementCategory = dataElementCategoryService.getDataElementCategory( Integer
                .parseInt( id ) );

            updatedCategories.add( dataElementCategory );

        }

        if( ! dataElementCategoryCombo.getCategories().containsAll( updatedCategories) )
        {
        	dataElementCategoryCombo.setCategories( updatedCategories );

            dataElementCategoryComboService.updateDataElementCategoryCombo( dataElementCategoryCombo );
            
        	dataElementCategoryOptionComboService.generateOptionCombos( dataElementCategoryCombo );

        }       	

        int displayOrder = 1;
        
        DataElementDimensionRowOrder rowOrder = null;

        for ( String id : selectedList )
        {            
            
        	DataElementCategory dataElementCategory = dataElementCategoryService.getDataElementCategory( Integer
                .parseInt( id ) );       	

            rowOrder = dataElementDimensionRowOrderService.getDataElementDimensionRowOrder( dataElementCategoryCombo,
                dataElementCategory );

            if ( rowOrder == null )
            {
                rowOrder = new DataElementDimensionRowOrder( dataElementCategoryCombo, dataElementCategory,
                    displayOrder );
                
                dataElementDimensionRowOrderService.addDataElementDimensionRowOrder( rowOrder );                
                
            }
            else
            {
                rowOrder.setDisplayOrder( displayOrder );
                
                dataElementDimensionRowOrderService.updateDataElementDimensionRowOrder( rowOrder );                
                
            }

            displayOrder++;

        }

        return SUCCESS;
    }
}
