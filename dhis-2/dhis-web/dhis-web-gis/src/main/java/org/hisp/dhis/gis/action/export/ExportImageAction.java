package org.hisp.dhis.gis.action.export;

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
import org.hisp.dhis.gis.action.configuration.GISConfigurationManagerService;
import org.hisp.dhis.gis.action.management.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: ExportImageAction.java 28-04-2008 16:06:00 $
 */
public class ExportImageAction
    implements Action
{

    private GISConfigurationManagerService gisConfigurationManagerService;

    private FileFeatureStore fileFeatureStore;

    private String mapFileName;

    private String outputImage;

    private InputStream inputStream;

    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public void setInputStream( InputStream inputStream )
    {
        this.inputStream = inputStream;
    }

    public void setMapFileName( String mapFileName )
    {
        this.mapFileName = mapFileName;
    }

    public String getOutputImage()
    {
        return outputImage;
    }

    public void setOutputImage( String outputImage )
    {
        this.outputImage = outputImage;
    }

    public void setFileFeatureStore( FileFeatureStore fileFeatureStore )
    {
        this.fileFeatureStore = fileFeatureStore;
    }

    public String execute()
        throws Exception
    {

        if ( fileFeatureStore.createSVGTempFile( this.mapFileName ) )
        {
            // fileFeatureStore.convert();

            File directory = gisConfigurationManagerService.getGISTempDirectory();

            String svg = directory.getAbsolutePath() + File.separator + "temp.svg";

            String jarpath = "\"" + gisConfigurationManagerService.getGISDirectory() + File.separator + "batik-1.7"
                + File.separator + "batik-rasterizer.jar\" ";

            String jarrun = "java -jar " + jarpath + " -w 1024  -h 1024 -q 0.9999 -m image/jpeg " + "\"" + svg + "\"";

            Process pro = Runtime.getRuntime().exec( jarrun );

            pro.waitFor();

            pro.destroy();

            File outputXLSStream = new File( directory.getAbsolutePath() + File.separator + "temp.jpg" );

            inputStream = new BufferedInputStream( new FileInputStream( outputXLSStream ) );

            outputImage = mapFileName.replace( ".svg", "" ) + "_" + new Date().getDay() + new Date().getMonth()
                + new Date().getYear() + ".jpg";

            outputXLSStream.deleteOnExit();

            return SUCCESS;
        }

        return ERROR;

    }

}
