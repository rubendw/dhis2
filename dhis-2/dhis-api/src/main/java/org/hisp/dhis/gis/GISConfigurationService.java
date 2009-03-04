package org.hisp.dhis.gis;

import java.util.Collection;

public interface GISConfigurationService
{
   public void add( String key, String value );

    public void delete( String key );

    public void update( String key, String value );

    public GISConfiguration get( String key );

    public Collection<GISConfiguration> getALL();

    public String getValue( String key );   
   

}
