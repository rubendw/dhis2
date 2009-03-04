package org.hisp.dhis.gis.action.export;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date; 
/*
import org.apache.batik.transcoder.image.JPEGTranscoder;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
*/
import org.hisp.dhis.gis.action.configuration.GISConfigurationManagerService;

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

/**
 * @author Tran Thanh Tri
 * @version $Id: ExportToImageAction.java 19-08-2008 16:06:00 $
 */
public class ExportToImageAction
    implements com.opensymphony.xwork.Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private FileFeatureStore fileFeatureStore;

    private GISConfigurationManagerService gisConfigurationManagerService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String mapFileName;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String outputImage;

    private InputStream inputStream;

    // -------------------------------------------------------------------------
    // Getter & Setter
    // -------------------------------------------------------------------------

    public String getOutputImage()
    {
        return outputImage;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public void setFileFeatureStore( FileFeatureStore fileFeatureStore )
    {
        this.fileFeatureStore = fileFeatureStore;
    }

    public void setMapFileName( String mapFileName )
    {
        this.mapFileName = mapFileName;
    }
    
    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }

    public String execute()
        throws Exception
    {
        /*
        if ( fileFeatureStore.createSVGTempFile( this.mapFileName ) )
        {

            File directory = gisConfigurationManagerService.getGISTempDirectory();

            String svg = new File( directory.getAbsolutePath(), "temp.svg" ).toURL().toString();

            JPEGTranscoder t = new JPEGTranscoder();

            // Set the transcoding hints.
            t.addTranscodingHint( JPEGTranscoder.KEY_QUALITY, new Float( .8 ) );
            t.addTranscodingHint( JPEGTranscoder.KEY_HEIGHT, new Float( 300 ) );
            t.addTranscodingHint( JPEGTranscoder.KEY_WIDTH, new Float( 300 ) );

            // Create the transcoder input.

            System.out.println( svg );
            TranscoderInput input = new TranscoderInput( svg );
            System.out.println( SUCCESS );

            // Create the transcoder output.

            OutputStream ostream = new FileOutputStream( directory.getAbsolutePath() + File.separator + "temp.jpg" );

            TranscoderOutput output = new TranscoderOutput( ostream );

            // Save the image.
            t.transcode( input, output );

            // Flush and close the stream.
            ostream.flush();
            ostream.close();

            File outputStream = new File( directory.getAbsolutePath() + File.separator + "temp.jpg" );

            inputStream = new BufferedInputStream( new FileInputStream( outputStream ) );

            outputImage = mapFileName.replace( ".svg", "" ) + "_" + new Date().getDay() + new Date().getMonth()
                + new Date().getYear() + ".jpg";

            outputStream.deleteOnExit();

            // TODO Auto-generated method stub
            return SUCCESS;
        }
        */
        return ERROR;
    }

   

}
