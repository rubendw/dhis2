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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
public class GetDataElementCategoryAction
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
    // Input/output
    // -------------------------------------------------------------------------

    private Integer dataElementCategoryId;

    public void setDataElementCategoryId( Integer dataElementCategoryId )
    {
        this.dataElementCategoryId = dataElementCategoryId;
    }

    private DataElementCategory dataElementCategory;

    public DataElementCategory getDataElementCategory()
    {
        return dataElementCategory;
    }

    private Collection<DataElementCategoryOption> dataElementCategoryOptions = new ArrayList<DataElementCategoryOption>();

    public Collection<DataElementCategoryOption> getDataElementCategoryOptions()
    {
        return dataElementCategoryOptions;
    }

    private List<DataElementCategoryOption> allDataElementCategoryOptions;

    public List<DataElementCategoryOption> getAllDataElementCategoryOptions()
    {
        return allDataElementCategoryOptions;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        dataElementCategory = dataElementCategoryService.getDataElementCategory( dataElementCategoryId );

        List<DataElementCategoryOption> options = new ArrayList<DataElementCategoryOption>( dataElementCategory
            .getCategoryOptions() );

        Map<Integer, DataElementCategoryOption> map = new TreeMap<Integer, DataElementCategoryOption>();

        boolean storedDisplayOrder = true;

        DataElementDimensionColumnOrder columnOrder = null;

        for ( DataElementCategoryOption option : options )
        {
            columnOrder = dataElementDimensionColumnOrderService.getDataElementDimensionColumnOrder(
                dataElementCategory, option );

            if ( columnOrder == null )
            {

                storedDisplayOrder = false;
                break;
            }

            map.put( columnOrder.getDisplayOrder(), option );
        }

        if ( storedDisplayOrder == false )
        {
            dataElementCategoryOptions = options;
        }
        else
        {
            dataElementCategoryOptions = map.values();
        }

        allDataElementCategoryOptions = new ArrayList<DataElementCategoryOption>( dataElementCategoryOptionService
            .getAllDataElementCategoryOptions() );

        Iterator<DataElementCategoryOption> categoryOptionIterator = allDataElementCategoryOptions.iterator();

        while ( categoryOptionIterator.hasNext() )
        {
            DataElementCategoryOption option = categoryOptionIterator.next();

            if ( dataElementCategoryOptions.contains( option ) )
            {
                categoryOptionIterator.remove();
            }
        }

        return SUCCESS;
    }
}
