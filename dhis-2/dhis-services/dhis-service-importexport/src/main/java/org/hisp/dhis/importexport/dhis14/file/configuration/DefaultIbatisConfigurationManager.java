package org.hisp.dhis.importexport.dhis14.file.configuration;

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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.hisp.dhis.external.configuration.ConfigurationManager;
import org.hisp.dhis.external.configuration.NoConfigurationFoundException;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.importexport.IbatisConfiguration;
import org.hisp.dhis.importexport.IbatisConfigurationManager;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultIbatisConfigurationManager.java 5710 2008-09-16 20:16:49Z larshelg $
 */
public class DefaultIbatisConfigurationManager
    implements IbatisConfigurationManager
{
    private static final String KEY_CONNECTION_URL_DATABASE = "ibatis.connection.url.database";
    private static final String KEY_PASSWORD = "ibatis.connection.password";
    private static final String KEY_USERNAME = "ibatis.connection.username";
    private static final String KEY_LEVELS = "ibatis.levels";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private String configDir;

    public void setConfigDir( String configDir )
    {
        this.configDir = configDir;
    }
    
    private String configFile;

    public void setConfigFile( String configFile )
    {
        this.configFile = configFile;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }

    private ConfigurationManager<IbatisConfiguration> configurationManager;
    
    public void setConfigurationManager( ConfigurationManager<IbatisConfiguration> configurationManager )
    {
        this.configurationManager = configurationManager;
    }

    // -------------------------------------------------------------------------
    // IbatisConfigurationManager implementation
    // -------------------------------------------------------------------------

    public Properties getPropertiesConfiguration()
    {
        try
        {
            IbatisConfiguration configuration = getIbatisConfiguration();
            
            Properties properties = new Properties();
            
            properties.put( KEY_CONNECTION_URL_DATABASE, configuration.getDataFile() );
            properties.put( KEY_USERNAME, configuration.getUserName() );
            properties.put( KEY_PASSWORD, configuration.getPassword() );
            properties.put( KEY_LEVELS, configuration.getLevels() );
            
            return properties;
        }
        catch ( NoConfigurationFoundException ex )
        {
            throw new RuntimeException( "No configuration found for Ibatis", ex );
        }
    }
    
    public IbatisConfiguration getIbatisConfiguration()
        throws NoConfigurationFoundException
    {
        try
        {
            InputStream in = locationManager.getInputStream( configFile, configDir );
            
            return configurationManager.getConfiguration( in, IbatisConfiguration.class );
        }
        catch ( LocationManagerException ex )
        {
            throw new NoConfigurationFoundException( "No configuration file found" );
        }
    }

    public void setIbatisConfiguration( IbatisConfiguration configuration )
    {
        try
        {
            OutputStream out = locationManager.getOutputStream( configFile, configDir );
            
            configurationManager.setConfiguration( configuration, out );
        }
        catch ( LocationManagerException ex )
        {
            throw new RuntimeException( "Failed to set configuration", ex );
        }
    }    
}
