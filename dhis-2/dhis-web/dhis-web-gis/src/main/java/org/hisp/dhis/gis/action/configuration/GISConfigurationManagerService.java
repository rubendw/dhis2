package org.hisp.dhis.gis.action.configuration;

import java.io.File;

public interface GISConfigurationManagerService
{
    final String MAP_DIR = "map";

    final String TEMP_DIR = "temp";
    
    public abstract File getGISDirectory(); 
    
    public abstract File getGISMapDirectory();
    
    public abstract File getGISTempDirectory();
    
    public abstract String getIndicatorFrom();
    
    public abstract boolean isNULL(String key);

}
