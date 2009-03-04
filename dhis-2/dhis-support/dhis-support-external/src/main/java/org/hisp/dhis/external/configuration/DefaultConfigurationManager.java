package org.hisp.dhis.external.configuration;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultConfigurationManager<T>
    implements ConfigurationManager<T>
{
    private static final String CONFIG_NAME = "configuration";
    
    public void setConfiguration( T configuration, OutputStream out )
    {
        try
        {
            XStream xStream = getXStream( configuration.getClass() );
            
            xStream.toXML( configuration, out );
        }
        finally
        {
            closeOutputStream( out );
        }
    }
    
    @SuppressWarnings( "unchecked" )
    public T getConfiguration( InputStream in, Class clazz )
    {
        T object = null;
        
        try
        {
            XStream xStream = getXStream( clazz );
        
            object = (T) xStream.fromXML( in );
        }
        finally
        {
            closeInputStream( in );
        }
        
        return object;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private XStream getXStream( Class<?> clazz )
    {
        XStream xStream = new XStream();
        
        xStream.alias( CONFIG_NAME, clazz );
        
        return xStream;
    }
    
    private static void closeInputStream( InputStream in )
    {
        if ( in != null )
        {
            try
            {
                in.close();
            }
            catch ( IOException ex )
            {
                ex.printStackTrace();
            }
        }
    }
    
    private static void closeOutputStream( OutputStream out )
    {
        if ( out != null )
        {
            try
            {
                out.flush();
            }
            catch ( IOException ex )
            {
                ex.printStackTrace();
            }
            
            try
            {
                out.close();
            }
            catch ( IOException ex )
            {
                ex.printStackTrace();
            }
        }
    }
}
