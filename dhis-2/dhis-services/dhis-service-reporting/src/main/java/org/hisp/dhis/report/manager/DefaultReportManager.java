package org.hisp.dhis.report.manager;

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

import org.hisp.dhis.external.configuration.ConfigurationManager;
import org.hisp.dhis.external.configuration.NoConfigurationFoundException;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.report.ReportManager;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultReportManager
    implements ReportManager
{
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private String reportConfigDir;

    public void setReportConfigDir( String reportConfigDir )
    {
        this.reportConfigDir = reportConfigDir;
    }
    
    private String reportConfigFile;

    public void setReportConfigFile( String reportConfigFile )
    {
        this.reportConfigFile = reportConfigFile;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }

    private ConfigurationManager<ReportConfiguration> configurationManager;
    
    public void setConfigurationManager( ConfigurationManager<ReportConfiguration> configurationManager )
    {
        this.configurationManager = configurationManager;
    }

    // -------------------------------------------------------------------------
    // ReportManager implementation
    // -------------------------------------------------------------------------

    public void setConfiguration( ReportConfiguration configuration )
    {
        try
        {
            OutputStream out = locationManager.getOutputStream( reportConfigFile, reportConfigDir );
            
            configurationManager.setConfiguration( configuration, out );
        }
        catch ( LocationManagerException ex )
        {
            throw new RuntimeException( "Failed to set configuration", ex );
        }        
    }
    
    public ReportConfiguration getConfiguration()
        throws NoConfigurationFoundException
    {
        try
        {
            InputStream in = locationManager.getInputStream( reportConfigFile, reportConfigDir );
            
            return configurationManager.getConfiguration( in, ReportConfiguration.class );
        }
        catch ( LocationManagerException ex )
        {
            throw new NoConfigurationFoundException( "No configuration file found" );
        }
    }
}
