package org.hisp.dhis.dataelement;

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
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class DefaultDataElementCategoryService
    implements DataElementCategoryService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementCategoryStore dataElementCategoryStore;

    public void setDataElementCategoryStore( DataElementCategoryStore dataElementCategoryStore )
    {
        this.dataElementCategoryStore = dataElementCategoryStore;
    }

    private DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService;

    public void setDataElementDimensionColumnOrderService(
        DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService )
    {
        this.dataElementDimensionColumnOrderService = dataElementDimensionColumnOrderService;
    }

    // -------------------------------------------------------------------------
    // DataElementCategory
    // -------------------------------------------------------------------------

    public int addDataElementCategory( DataElementCategory dataElementCategory )
    {
        return dataElementCategoryStore.addDataElementCategory( dataElementCategory );
    }

    public void deleteDataElementCategory( DataElementCategory dataElementCategory )
    {
        dataElementCategoryStore.deleteDataElementCategory( dataElementCategory );
    }

    public Collection<DataElementCategory> getAllDataElementCategories()
    {
        return dataElementCategoryStore.getAllDataElementCategories();
    }

    public DataElementCategory getDataElementCategory( int id )
    {
        return dataElementCategoryStore.getDataElementCategory( id );

    }

    public DataElementCategory getDataElementCategoryByName( String name )
    {
        return dataElementCategoryStore.getDataElementCategoryByName( name );
    }

    public void updateDataElementCategory( DataElementCategory dataElementCategory )
    {
        dataElementCategoryStore.updateDataElementCategory( dataElementCategory );
    }

    public Collection<DataElementCategoryOption> getOrderedOptions( DataElementCategory category )
    {
        Map<Integer, DataElementCategoryOption> optionsMap = new TreeMap<Integer, DataElementCategoryOption>();

        for ( DataElementCategoryOption option : category.getCategoryOptions() )
        {
            DataElementDimensionColumnOrder columnOrder = dataElementDimensionColumnOrderService
                .getDataElementDimensionColumnOrder( category, option );

            if ( columnOrder != null )
            {
                optionsMap.put( columnOrder.getDisplayOrder(), option );
            }
            else
            {
                optionsMap.put( option.getId(), option );
            }
        }

        return optionsMap.values();
    }
}
