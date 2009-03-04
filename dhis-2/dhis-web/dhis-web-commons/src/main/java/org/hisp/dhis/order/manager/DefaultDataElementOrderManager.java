package org.hisp.dhis.order.manager;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.order.DataElementOrder;
import org.hisp.dhis.order.store.DataElementOrderStore;
import org.hisp.dhis.order.store.DataElementOrderStoreException;
import org.hisp.dhis.util.CollectionUtils;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultDataElementOrderManager.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class DefaultDataElementOrderManager
    implements DataElementOrderManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementOrderStore dataElementOrderStore;

    public void setDataElementOrderStore( DataElementOrderStore dataElementOrderStore )
    {
        this.dataElementOrderStore = dataElementOrderStore;
    }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private Comparator<DataElement> defaultComparator;

    public void setDefaultComparator( Comparator<DataElement> defaultComparator )
    {
        this.defaultComparator = defaultComparator;
    }

    private Collection<Comparator<DataElement>> availableComparators;

    public void setAvailableComparators( Collection<Comparator<DataElement>> availableComparators )
    {
        this.availableComparators = availableComparators;
    }

    // -------------------------------------------------------------------------
    // DataElementOrderManager implementation
    // -------------------------------------------------------------------------

    public Comparator<DataElement> getDefaultComparator()
        throws DataElementOrderManagerException
    {
        return defaultComparator;
    }

    public Collection<Comparator<DataElement>> getAvailableComparators()
        throws DataElementOrderManagerException
    {
        return availableComparators;
    }

    public List<DataElement> getOrderedDataElements( DataSet dataSet )
        throws DataElementOrderManagerException
    {
        DataElementOrder dataElementOrder = null;

        // ---------------------------------------------------------------------
        // Search the data set for stored order
        // ---------------------------------------------------------------------

        try
        {
            dataElementOrder = dataElementOrderStore.getDataElementOrder( dataSet );
        }
        catch ( DataElementOrderStoreException e )
        {
            throw new DataElementOrderManagerException( "Failed to get data element order", e );
        }

        // ---------------------------------------------------------------------
        // No stored order, use default
        // ---------------------------------------------------------------------

        if ( dataElementOrder == null )
        {
            List<DataElement> dataElements = new ArrayList<DataElement>( dataSet.getDataElements() );

            Collections.sort( dataElements, getDefaultComparator() );

            return dataElements;
        }

        // ---------------------------------------------------------------------
        // Stored order
        // ---------------------------------------------------------------------

        else
        {
            Map<Integer, DataElement> idMap;

            try
            {
                idMap = CollectionUtils.createMap( dataSet.getDataElements(), "getId" );
            }
            catch ( Exception e )
            {
                throw new DataElementOrderManagerException( "Failed to create id map", e );
            }

            // -----------------------------------------------------------------
            // Order according to the stored order
            // -----------------------------------------------------------------

            List<DataElement> result = new ArrayList<DataElement>( idMap.size() );

            for ( Integer id : dataElementOrder.getOrder() )
            {
                if ( idMap.containsKey( id ) )
                {
                    result.add( idMap.get( id ) );

                    idMap.remove( id );
                }
            }

            // -----------------------------------------------------------------
            // Order remaining data elements by default order
            // -----------------------------------------------------------------

            List<DataElement> remainingDataElements = new ArrayList<DataElement>( idMap.values() );

            Collections.sort( remainingDataElements, getDefaultComparator() );

            result.addAll( remainingDataElements );

            return result;
        }
    }

    public void setDataElementOrder( DataSet dataSet, List<Integer> dataElementIds )
        throws DataElementOrderManagerException
    {
        DataElementOrder dataElementOrder;

        try
        {
            dataElementOrder = dataElementOrderStore.getDataElementOrder( dataSet );
        }
        catch ( DataElementOrderStoreException e )
        {
            throw new DataElementOrderManagerException( "Failed to get data element order", e );
        }

        if ( dataElementOrder == null )
        {
            dataElementOrder = new DataElementOrder();
            dataElementOrder.setDataSetId( dataSet.getId() );
            dataElementOrder.setOrder( dataElementIds );

            try
            {
                dataElementOrderStore.addDataElementOrder( dataElementOrder );
            }
            catch ( DataElementOrderStoreException e )
            {
                throw new DataElementOrderManagerException( "Failed to add data element order", e );
            }
        }
        else
        {
            dataElementOrder.setOrder( dataElementIds );

            try
            {
                dataElementOrderStore.updateDataElementOrder( dataElementOrder );
            }
            catch ( DataElementOrderStoreException e )
            {
                throw new DataElementOrderManagerException( "Failed to update data element order", e );
            }
        }
    }

    public void setDataElementOrder( DataSet dataSet, Comparator<DataElement> comparator )
        throws DataElementOrderManagerException
    {
        List<DataElement> dataElements = new ArrayList<DataElement>( dataSet.getDataElements() );

        Collections.sort( dataElements, comparator );

        setDataElementOrder( dataSet, getIdList( dataElements ) );
    }

    public void moveDataElementUp( DataSet dataSet, Integer dataElementId )
        throws DataElementOrderManagerException
    {
        List<DataElement> list = getOrderedDataElements( dataSet );

        List<Integer> idList = getIdList( list );

        int index = idList.indexOf( dataElementId );

        if ( index > 0 )
        {
            Integer tmp = idList.get( index - 1 );
            idList.set( index - 1, dataElementId );
            idList.set( index, tmp );
        }

        setDataElementOrder( dataSet, idList );
    }

    public void moveDataElementDown( DataSet dataSet, Integer dataElementId )
        throws DataElementOrderManagerException
    {
        List<DataElement> list = getOrderedDataElements( dataSet );

        List<Integer> idList = getIdList( list );

        int index = idList.indexOf( dataElementId );

        if ( index < idList.size() - 1 )
        {
            Integer tmp = idList.get( index + 1 );
            idList.set( index + 1, dataElementId );
            idList.set( index, tmp );
        }

        setDataElementOrder( dataSet, idList );
    }

    public void resetDataElementOrder( DataSet dataSet )
        throws DataElementOrderManagerException
    {
        DataElementOrder dataElementOrder;

        try
        {
            dataElementOrder = dataElementOrderStore.getDataElementOrder( dataSet );
        }
        catch ( DataElementOrderStoreException e )
        {
            throw new DataElementOrderManagerException( "Failed to get data element order", e );
        }

        if ( dataElementOrder != null )
        {
            try
            {
                dataElementOrderStore.deleteDataElementOrder( dataElementOrder );
            }
            catch ( DataElementOrderStoreException e )
            {
                throw new DataElementOrderManagerException( "Failed to delete data element order", e );
            }
        }
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private List<Integer> getIdList( List<DataElement> dataElements )
        throws DataElementOrderManagerException
    {
        try
        {
            return CollectionUtils.createList( dataElements, "getId" );
        }
        catch ( Exception e )
        {
            throw new DataElementOrderManagerException( "Failed to create id list", e );
        }
    }
}
