package org.hisp.dhis.order.store;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.order.DataElementOrder;

import com.thoughtworks.xstream.XStream;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: FileDataElementOrderStore.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class FileDataElementOrderStore
    implements DataElementOrderStore
{
    private static final String ALIAS_DATAELEMENTORDER = "dataElementOrder";

    private static final String DIR = "de";
    private static final String FILE_NAME = "dataElementOrder.xml";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }
    
    // -------------------------------------------------------------------------
    // DataElementOrderStore implementation
    // -------------------------------------------------------------------------

    public void addDataElementOrder( DataElementOrder dataElementOrder ) throws DataElementOrderStoreException
    {
        Map<String, DataElementOrder> orderMap = loadOrderMap();

        orderMap.put( getUniqueKey( dataElementOrder ), dataElementOrder );

        saveOrderMap( orderMap );
    }

    public void updateDataElementOrder( DataElementOrder dataElementOrder ) throws DataElementOrderStoreException
    {
        addDataElementOrder( dataElementOrder );
    }

    public DataElementOrder getDataElementOrder( DataSet dataSet ) throws DataElementOrderStoreException
    {
        Map<String, DataElementOrder> orderMap = loadOrderMap();

        DataElementOrder dataElementOrder = orderMap.get( getUniqueKey( dataSet ) );

        return dataElementOrder;
    }

    public void deleteDataElementOrder( DataElementOrder dataElementOrder ) throws DataElementOrderStoreException
    {
        Map<String, DataElementOrder> orderMap = loadOrderMap();

        orderMap.remove( getUniqueKey( dataElementOrder ) );

        saveOrderMap( orderMap );
    }

    // -------------------------------------------------------------------------
    // File access methods
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private Map<String, DataElementOrder> loadOrderMap() throws DataElementOrderStoreException
    {
        Reader reader;
        
        try
        {
            File targetFile = locationManager.getFileForReading( FILE_NAME, DIR );
            
            reader = new BufferedReader( new FileReader( targetFile ) );
        }
        catch ( LocationManagerException ex )
        {
            return new HashMap<String, DataElementOrder>();
        }
        catch ( FileNotFoundException ex )
        {
            return new HashMap<String, DataElementOrder>();
        }

        XStream xStream = getXStream();
        
        Map<String, DataElementOrder> orderMap = (Map<String, DataElementOrder>) xStream.fromXML( reader );

        try
        {
            reader.close();
        }
        catch ( IOException e )
        {
            throw new DataElementOrderStoreException( "Failed to close writer", e );
        }

        return orderMap;
    }

    private void saveOrderMap( Map<String, DataElementOrder> orderMap ) throws DataElementOrderStoreException
    {
        Writer writer;
        
        try
        {
            File targetFile = locationManager.getFileForWriting( FILE_NAME, DIR );
            
            writer = new BufferedWriter( new FileWriter( targetFile ) );
        }
        catch ( LocationManagerException ex )
        {
            throw new DataElementOrderStoreException( "Failed to get target file for reading", ex );
        }
        catch ( IOException ex )
        {
            throw new DataElementOrderStoreException( "Failed to create writer for target file", ex );
        }

        XStream xStream = getXStream();
        xStream.toXML( orderMap, writer );

        try
        {
            writer.close();
        }
        catch ( IOException e )
        {
            throw new DataElementOrderStoreException( "Failed to close writer", e );
        }
    }
    
    private XStream getXStream()
    {
        XStream xStream = new XStream();
        
        xStream.alias( ALIAS_DATAELEMENTORDER, DataElementOrder.class );
        
        return xStream;
    }

    // -------------------------------------------------------------------------
    // These methods return the same key based on different sources
    // -------------------------------------------------------------------------

    private static String getUniqueKey( DataElementOrder dataElementOrder )
    {
        return String.valueOf( dataElementOrder.getDataSetId() );
    }

    private static String getUniqueKey( DataSet dataSet )
    {
        return String.valueOf( dataSet.getId() );
    }
}
