package org.hisp.dhis.gis.action.configuration;

import java.io.File;

import org.hisp.dhis.gis.GISConfiguration;
import org.hisp.dhis.gis.GISConfigurationService;

public class DefaultGISConfigurationManagerService
    implements GISConfigurationManagerService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private GISConfigurationService gisConfigurationService;

    public void setGisConfigurationService( GISConfigurationService gisConfigurationService )
    {
        this.gisConfigurationService = gisConfigurationService;
    }

    // -------------------------------------------------------------------------
    // Implement
    // -------------------------------------------------------------------------

    public boolean isNULL( String key )
    {
        GISConfiguration gisConfiguration = gisConfigurationService.get( key );
        if ( gisConfiguration == null )
            return true;
        if ( gisConfiguration.getValue() == null )
            return true;
        if ( gisConfiguration.getValue() == "" )
            return true;
        return false;
    }

    public File getGISDirectory()
    {
        String value = gisConfigurationService.getValue( GISConfiguration.KEY_DIRECTORY );

        if ( value != null )
        {
            return new File( value );
        }

        return null;

    }

    public File getGISMapDirectory()
    {
        if ( getGISDirectory() != null )
        {
            return new File( getGISDirectory(), MAP_DIR );
        }
        return null;

    }

    public File getGISTempDirectory()
    {

        if ( getGISDirectory() != null )
        {
            return new File( getGISDirectory(), TEMP_DIR );
        }
        return null;
    }

    public String getIndicatorFrom()
    {

        return gisConfigurationService.getValue( GISConfiguration.KEY_GETINDICATOR );
    }

}
