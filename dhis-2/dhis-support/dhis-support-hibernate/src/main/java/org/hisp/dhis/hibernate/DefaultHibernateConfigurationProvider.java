package org.hisp.dhis.hibernate;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.springframework.util.ResourceUtils;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultHibernateConfigurationProvider.java 3644 2007-10-15
 *          16:13:31Z torgeilo $
 */
public class DefaultHibernateConfigurationProvider
    implements HibernateConfigurationProvider
{
    private static final Log LOG = LogFactory.getLog( DefaultHibernateConfigurationProvider.class );

    private Configuration configuration = null;

    private static final String MAPPING_RESOURCES_ROOT = "org/hisp/dhis/";

    // -------------------------------------------------------------------------
    // Property resources
    // -------------------------------------------------------------------------

    private String defaultPropertiesFile = "hibernate-default.properties";

    private String regularPropertiesFile = "hibernate.properties";

    private String testPropertiesFile = "hibernate-test.properties";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }
    
    // -------------------------------------------------------------------------
    // Initialise
    // -------------------------------------------------------------------------

    public void initialise()
        throws Exception
    {
        Configuration configuration = new Configuration();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // ---------------------------------------------------------------------
        // Add mapping resources
        // ---------------------------------------------------------------------

        Enumeration<URL> resources = classLoader.getResources( MAPPING_RESOURCES_ROOT );

        while ( resources.hasMoreElements() )
        {
            URL resource = resources.nextElement();

            if ( ResourceUtils.isJarURL( resource ) )
            {
                URL jarFile = ResourceUtils.extractJarFileURL( resource );

                File file = ResourceUtils.getFile( jarFile );

                LOG.debug( "Adding jar in which to search for hbm.xml files: " + file.getAbsolutePath() );

                configuration.addJar( file );
            }
            else
            {
                File file = ResourceUtils.getFile( resource );

                LOG.debug( "Adding directory in which to search for hbm.xml files: " + file.getAbsolutePath() );

                configuration.addDirectory( file );
            }
        }

        // ---------------------------------------------------------------------
        // Add default properties
        // ---------------------------------------------------------------------

        Properties defaultProperties = loadProperties( defaultPropertiesFile, classLoader );

        configuration.addProperties( defaultProperties );

        // ---------------------------------------------------------------------
        // Choose the properties file to look for
        // ---------------------------------------------------------------------

        String propertiesFile;

        String testing = System.getProperty( "org.hisp.dhis.test", "false" );

        if ( testing.equals( "true" ) )
        {
            propertiesFile = testPropertiesFile;
        }
        else
        {
            propertiesFile = regularPropertiesFile;
        }

        // ---------------------------------------------------------------------
        // Add custom properties from classpath
        // ---------------------------------------------------------------------

        Properties customProperties = getProperties( propertiesFile, classLoader );

        if ( customProperties != null )
        {
            configuration.addProperties( customProperties );
        }

        // ---------------------------------------------------------------------
        // Add custom properties from file system
        // ---------------------------------------------------------------------
        
        try
        {
            File customFile = locationManager.getFileForReading( propertiesFile );
        
            configuration.addProperties( getProperties( customFile ) );   
        }
        catch ( LocationManagerException ex )
        {
            LOG.info( "Could not read external configuration from file system" );
        }
        
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // HibernateConfigurationProvider implementation
    // -------------------------------------------------------------------------

    public void setEventListener( String type, Object listener )
    {
        configuration.setListener( type, listener );
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    // -------------------------------------------------------------------------
    // Supporting methods
    // -------------------------------------------------------------------------

    /**
     * Loads properties file from classpath.
     */
    private Properties getProperties( String propertiesFile, ClassLoader classLoader )
        throws IOException
    {
        try
        {
            return loadProperties( propertiesFile, classLoader );
        }
        catch ( FileNotFoundException e )
        {
            return null;
        }
        catch ( SecurityException e )
        {
            LOG.warn( "SecurityException: Not permitted to read properties file: " + propertiesFile );

            return null;
        }
    }

    private Properties loadProperties( String propertiesFile, ClassLoader classLoader )
        throws IOException
    {
        URL resourceURL = classLoader.getResource( propertiesFile );

        if ( resourceURL == null )
        {
            throw new FileNotFoundException( "Properties file not found: " + propertiesFile );
        }

        LOG.info( "Hibernate properties file found: " + resourceURL.toExternalForm() );

        InputStream inputStream = resourceURL.openStream();

        Properties properties = new Properties();

        try
        {
            properties.load( inputStream );
        }
        catch ( IOException e )
        {
            throw e;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( IOException ee )
            {
                LOG.error( "Failed to close input stream", ee );
            }
        }

        return properties;
    }

    /**
     * Loads properties file from file.
     */
    private Properties getProperties( File file )
        throws IOException
    {
        try
        {
            if ( !file.exists() )
            {
                return null;
            }
        }
        catch ( SecurityException e )
        {
            LOG.warn( "SecurityException: Not permitted to read properties file: " + file.getPath() );

            return null;
        }

        LOG.info( "Hibernate properties file found: " + file.getPath() );

        InputStream inputStream = new FileInputStream( file );

        Properties properties = new Properties();

        try
        {
            properties.load( inputStream );
        }
        catch ( IOException e )
        {
            throw e;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( IOException ee )
            {
                LOG.error( "Failed to close input stream", ee );
            }
        }

        return properties;
    }
}
