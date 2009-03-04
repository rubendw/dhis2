package org.hisp.dhis.security.authority;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: FileSystemAuthoritiesProvider.java 3160 2007-03-24 20:15:06Z torgeilo $
 */
public class FileSystemAuthoritiesProvider
    implements SystemAuthoritiesProvider
{
    private static final Log LOG = LogFactory.getLog( FileSystemAuthoritiesProvider.class );

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private String authoritiesFilePath;

    public void setAuthoritiesFilePath( String authoritiesFilePath )
    {
        this.authoritiesFilePath = authoritiesFilePath;
    }

    // -------------------------------------------------------------------------
    // Load authorities from all files matching authoritiesFilePath.
    // -------------------------------------------------------------------------

    public Collection<String> loadAuthorities()
        throws IOException
    {
        if ( authoritiesFilePath == null )
        {
            throw new IllegalStateException( "Authorities file path not specified" );
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources( authoritiesFilePath );

        if ( !resources.hasMoreElements() )
        {
            LOG.warn( "No resources found for path: '" + authoritiesFilePath + "'" );

            return Collections.emptySet();
        }

        HashSet<String> systemAuthorities = new HashSet<String>();

        while ( resources.hasMoreElements() )
        {
            InputStream in = resources.nextElement().openStream();
            BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );

            String line;

            while ( (line = reader.readLine()) != null )
            {
                line = line.trim();

                if ( line.length() > 0 )
                {
                    systemAuthorities.add( line );
                }
            }

            reader.close();
            in.close();
        }

        return systemAuthorities;
    }

    // -------------------------------------------------------------------------
    // SystemAuthoritiesProvider implementation
    // -------------------------------------------------------------------------

    public Collection<String> getSystemAuthorities()
    {
        try
        {
            return loadAuthorities();
        }
        catch ( IOException e )
        {
            LOG.error( "Failed to load authorities files", e );
        }

        return Collections.emptySet();
    }
}
