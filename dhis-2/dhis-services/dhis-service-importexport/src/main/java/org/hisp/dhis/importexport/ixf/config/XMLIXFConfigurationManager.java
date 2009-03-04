package org.hisp.dhis.importexport.ixf.config;

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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.external.configuration.ConfigurationManager;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.system.util.StreamUtils;

import com.thoughtworks.xstream.XStream;

/**
 * @author Lars Helge Overland
 * @version $Id: XMLIXFConfigurationManager.java 5710 2008-09-16 20:16:49Z larshelg $
 */
public class XMLIXFConfigurationManager
    implements IXFConfigurationManager
{
    private static final String COUNTRIES_PATH = "ixf/countries.cris.xml";
    
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

    private ConfigurationManager<IXFConfiguration> configurationManager;
    
    public void setConfigurationManager( ConfigurationManager<IXFConfiguration> configurationManager )
    {
        this.configurationManager = configurationManager;
    }

    // -------------------------------------------------------------------------
    // IXFConfigurationManager implementation
    // -------------------------------------------------------------------------

    public IXFConfiguration getConfiguration()
    {
        try
        {
            InputStream in = locationManager.getInputStream( configFile, configDir );
            
            IXFConfiguration configuration = configurationManager.getConfiguration( in, IXFConfiguration.class );
            
            configuration.setSource( IXFConfiguration.SOURCE );
            configuration.setSourceKey( IXFConfiguration.SOURCE_KEY );
            
            return configuration;
        }
        catch ( LocationManagerException ex )
        {
            return getDefaultConfiguration();
        }
    }

    public void setConfiguration( IXFConfiguration configuration )
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

    public List<IXFCountry> getCountries()
    {
        List<IXFCountry> countries = new ArrayList<IXFCountry>();
        
        XStream xStream = getXStream();
        
        InputStream in = StreamUtils.loadResource( COUNTRIES_PATH );
        
        ObjectInputStream objectStream = null;
        
        try
        {   
            objectStream = xStream.createObjectInputStream( new InputStreamReader( in ) );
            
            while( true )
            {
                IXFCountry country = (IXFCountry) objectStream.readObject();
                
                countries.add( country );
            }
        }
        catch ( EOFException ex )
        {
            StreamUtils.closeInputStream( objectStream );
            
            StreamUtils.closeInputStream( in );
            
            Collections.sort( countries, new IXFCountryLongNameComparator() );
                        
            return countries;
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to create ObjectInputStream", ex );
        }
        catch ( ClassNotFoundException ex )
        {
            throw new RuntimeException( "Failed to read object", ex );
        }
    }
    
    public IXFCountry getCountry( String key )
    {
        List<IXFCountry> countries = getCountries();
        
        for ( IXFCountry country : countries )
        {
            if ( country.getKey().equals( key ) )
            {
                return country;
            }
        }
        
        return null;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private IXFConfiguration getDefaultConfiguration()
    {
        IXFCountry country = new IXFCountry();
        
        country.setKey( "197" );
        country.setNumber( "710" );
        country.setName( "SOUTH AFRICA" );
        country.setLongName( "South Africa" );
        country.setIsoCode( "ZA" );
        country.setLevelNumber( "2" );
        
        List<String> levelNames = new ArrayList<String>();
        
        levelNames.add( "Country" );
        levelNames.add( "Province" );
        levelNames.add( "District" );
        levelNames.add( "Hospital" );
        levelNames.add( "Ward" );
        levelNames.add( "Facility" );
        
        IXFConfiguration config = new IXFConfiguration();
        
        config.setSource( IXFConfiguration.SOURCE );
        config.setSourceKey( IXFConfiguration.SOURCE_KEY );
        config.setComment( "IXF export file generated by DHIS 2" );
        config.setCountry( country );
        config.setLevelNames( levelNames );
        
        return config;
    }

    private XStream getXStream()
    {
        XStream xStream = new XStream();
        
        xStream.alias( "ixfConfiguration", IXFConfiguration.class );
        xStream.alias( "country", IXFCountry.class );
        
        return xStream;
    }
}
