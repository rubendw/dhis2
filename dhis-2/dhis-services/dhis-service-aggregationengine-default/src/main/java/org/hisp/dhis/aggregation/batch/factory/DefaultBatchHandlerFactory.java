package org.hisp.dhis.aggregation.batch.factory;

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

import java.lang.reflect.Constructor;

import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfigurationProvider;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultBatchHandlerFactory.java 5797 2008-10-02 15:40:29Z larshelg $
 */
public class DefaultBatchHandlerFactory
    implements BatchHandlerFactory
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private JDBCConfigurationProvider configurationProvider;

    public void setConfigurationProvider( JDBCConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }
    
    // -------------------------------------------------------------------------
    // BatchHandlerFactory implementation
    // -------------------------------------------------------------------------
    
    public BatchHandler createBatchHandler( Class<? extends BatchHandler> clazz )
    {
        try
        {
            JDBCConfiguration configuration = configurationProvider.getConfiguration();
            
            return createBatchHandler( clazz, configuration );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get BatchHandler", ex );
        }
    }

    public BatchHandler createInternalBatchHandler( Class<? extends BatchHandler> clazz )
    {
        try
        {
            JDBCConfiguration configuration = configurationProvider.getInternalConfiguration();
            
            return createBatchHandler( clazz, configuration );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get BatchHandler", ex );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private BatchHandler createBatchHandler( Class<? extends BatchHandler> clazz, JDBCConfiguration configuration )
    {
        try
        {
            Class<?>[] argumentClasses = new Class<?>[] { JDBCConfiguration.class };
            
            Constructor<? extends BatchHandler> constructor = clazz.getConstructor( argumentClasses );
            
            Object[] arguments = new Object[] { configuration };
            
            BatchHandler batchHandler = constructor.newInstance( arguments );
            
            return batchHandler;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get BatchHandler", ex );
        }
    }    
}
