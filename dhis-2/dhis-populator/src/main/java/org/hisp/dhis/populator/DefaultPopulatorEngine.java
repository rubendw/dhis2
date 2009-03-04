package org.hisp.dhis.populator;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.transaction.TransactionType;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultPopulatorEngine.java 3217 2007-04-02 08:54:21Z torgeilo $
 */
public class DefaultPopulatorEngine
    extends AbstractPopulatorEngine
    implements BeanFactoryAware
{
    private static Log log = LogFactory.getLog( DefaultPopulatorEngine.class );

    private static final String COMMENT = "#";

    private static final String HEADER_START = "[";

    private static final String HEADER_END = "]";

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private String dataFile;

    public void setDataFile( String dataFile )
    {
        this.dataFile = dataFile;
    }

    // -------------------------------------------------------------------------
    // BeanFactoryAware implementation
    // -------------------------------------------------------------------------

    private BeanFactory beanFactory;

    public void setBeanFactory( BeanFactory beanFactory )
    {
        this.beanFactory = beanFactory;
    }

    // -------------------------------------------------------------------------
    // PopulatorEngine implementation
    // -------------------------------------------------------------------------

    public void executeEngine()
        throws PopulatorEngineException
    {
        log.info( "Populating..." );

        BufferedReader reader = new BufferedReader( getReader( dataFile ) );

        Populator populator = null;

        try
        {
            for ( String line = reader.readLine(); line != null; line = reader.readLine() )
            {
                line = line.trim();

                line = convertUnicode( line );

                // -------------------------------------------------------------
                // Skip empty and commented lines
                // -------------------------------------------------------------

                if ( line.length() == 0 || line.startsWith( COMMENT ) )
                {
                    // Skip
                }

                // -------------------------------------------------------------
                // Recognize section header and get corresponding populator
                // -------------------------------------------------------------

                else if ( line.startsWith( HEADER_START ) && line.endsWith( HEADER_END ) )
                {
                    String beanId = line.substring( HEADER_START.length(), line.length() - HEADER_END.length() );

                    beanId = beanId.trim();

                    log.debug( "beanFactory.getBean( " + beanId + " )" );

                    populator = (Populator) beanFactory.getBean( beanId );

                    if ( populator == null )
                    {
                        throw new PopulatorEngineException( "Populator not found: " + beanId );
                    }
                }

                // -------------------------------------------------------------
                // Execute rule
                // -------------------------------------------------------------

                else
                {
                    if ( populator == null )
                    {
                        throw new PopulatorEngineException( "Missing first header section" );
                    }

                    transactionManager.enter( TransactionType.READ_WRITE );
                    populator.executeRule( line );
                    transactionManager.leave();
                }
            }
        }
        catch ( IOException e )
        {
            throw new PopulatorEngineException( "Failed to read from file: " + dataFile, e );
        }
        catch ( PopulatorException e )
        {
            throw new PopulatorEngineException( "Failed to execute rule", e );
        }
        catch ( PopulatorEngineException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            throw new PopulatorEngineException( "Something failed", e );
        }
        finally
        {
            closeReader( reader );
        }

        log.info( "Done!" );
    }

    protected Reader getReader( String file )
        throws PopulatorEngineException
    {
        // ---------------------------------------------------------------------
        // Prefer a data file from USER_HOME/dhis if exists
        // ---------------------------------------------------------------------

        File externalDir = new File( System.getProperty( "user.home" ), "dhis" );

        File externalFile = new File( externalDir, file );

        if ( externalFile.exists() )
        {
            try
            {
                return new FileReader( externalFile );
            }
            catch ( FileNotFoundException e )
            {
                throw new PopulatorEngineException( "Failed to find a file which has been tested to exist!", e );
            }
        }

        // ---------------------------------------------------------------------
        // Classpath resource
        // ---------------------------------------------------------------------

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        InputStream in = classLoader.getResourceAsStream( file );

        if ( in == null )
        {
            throw new PopulatorEngineException( "Resource file not found: " + file );
        }

        try
        {
            return new InputStreamReader( in, "utf-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new PopulatorEngineException( "Failed to create InputStreamReader", e );
        }
    }

    private void closeReader( Reader reader )
    {
        try
        {
            reader.close();
        }
        catch ( IOException e )
        {
            log.error( "Failed to close reader" );
        }
    }

    private String convertUnicode( String string )
        throws PopulatorException
    {
        StringBuffer buffer = new StringBuffer( string.length() );

        int i = 0;

        while ( i < string.length() )
        {
            if ( string.charAt( i++ ) == '\\' )
            {
                if ( string.charAt( i++ ) == 'u' )
                {
                    int result = 0;

                    for ( int j = 0; j < 4; ++j, ++i )
                    {
                        char c = string.charAt( i );

                        switch ( c )
                        {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            result = (result << 4) + c - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            result = (result << 4) + 10 + c - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            result = (result << 4) + 10 + c - 'A';
                            break;
                        default:
                            throw new PopulatorException( "Illegal unicode encoding" );
                        }
                    }

                    buffer.append( (char) result );
                }
                else
                {
                    buffer.append( string.charAt( i - 1 ) );
                }
            }
            else
            {
                buffer.append( string.charAt( i - 1 ) );
            }
        }

        return buffer.toString();
    }
}
