package org.hisp.dhis.gis;

import java.util.Collection;

public class DefaultGISConfigurationService
    implements GISConfigurationService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private GISConfigurationStore gisConfigurationStore;

    public void setGisConfigurationStore( GISConfigurationStore gisConfigurationStore )
    {
        this.gisConfigurationStore = gisConfigurationStore;
    }

    public void add( String arg0, String arg1 )
    {
        gisConfigurationStore.add( arg0, arg1 );
    }

    public void delete( String arg0 )
    {
        gisConfigurationStore.delete( arg0 );

    }

    public GISConfiguration get( String arg0 )
    {
        // TODO Auto-generated method stub
        return gisConfigurationStore.get( arg0 );
    }

    public Collection<GISConfiguration> getALL()
    {
        // TODO Auto-generated method stub
        return gisConfigurationStore.getALL();
    }

    public String getValue( String arg0 )
    {
        // TODO Auto-generated method stub
        return gisConfigurationStore.getValue( arg0 );
    }

    public void update( String arg0, String arg1 )
    {
        gisConfigurationStore.update( arg0, arg1 );
    }

}
