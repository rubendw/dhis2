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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrder;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrderService;

import com.opensymphony.xwork.Action;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class GetDataElementCategoryComboAction
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

    private DataElementDimensionRowOrderService dataElementDimensionRowOrderService;

    public void setDataElementDimensionRowOrderService(
        DataElementDimensionRowOrderService dataElementDimensionRowOrderService )
    {
        this.dataElementDimensionRowOrderService = dataElementDimensionRowOrderService;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer dataElementCategoryComboId;

    public void setDataElementCategoryComboId( Integer dataElementCategoryComboId )
    {
        this.dataElementCategoryComboId = dataElementCategoryComboId;
    }

    private DataElementCategoryCombo dataElementCategoryCombo;

    public DataElementCategoryCombo getDataElementCategoryCombo()
    {
        return dataElementCategoryCombo;
    }

    private Collection<DataElementCategory> dataElementCategories = new ArrayList<DataElementCategory>();

    public Collection<DataElementCategory> getDataElementCategories()
    {
        return dataElementCategories;
    }

    private List<DataElementCategory> allDataElementCategories;

    public List<DataElementCategory> getAllDataElementCategories()
    {
        return allDataElementCategories;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        dataElementCategoryCombo = dataElementCategoryComboService
            .getDataElementCategoryCombo( dataElementCategoryComboId );

        List<DataElementCategory> cateogries = new ArrayList<DataElementCategory>( dataElementCategoryCombo
            .getCategories() );

        Map<Integer, DataElementCategory> temp = new TreeMap<Integer, DataElementCategory>();

        boolean storedDisplayOrder = true;
        
        DataElementDimensionRowOrder rowOrder = null;
        
        for ( DataElementCategory category : cateogries )
        {
            rowOrder = dataElementDimensionRowOrderService.getDataElementDimensionRowOrder( dataElementCategoryCombo,
                category );

            if ( rowOrder == null )
            {
                storedDisplayOrder = false;
                break;
            }

            temp.put( rowOrder.getDisplayOrder(), category );
        }

        if ( storedDisplayOrder == false )
        {
            dataElementCategories = cateogries;
        }
        else
        {
            dataElementCategories = temp.values();
        }

        allDataElementCategories = new ArrayList<DataElementCategory>( dataElementCategoryService
            .getAllDataElementCategories() );

        Iterator<DataElementCategory> categoryIterator = allDataElementCategories.iterator();

        while ( categoryIterator.hasNext() )
        {
            DataElementCategory category = categoryIterator.next();

            if ( dataElementCategories.contains( category ) )
            {
                categoryIterator.remove();
            }
        }

        return SUCCESS;
    }
}
