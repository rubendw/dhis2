package org.hisp.dhis.gis.action.management;

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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.gis.action.configuration.GISConfigurationManagerService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: UploadSVGFileAction.java 24-08-2008 16:06:00 $
 */
public class UploadSVGFileAction
    implements Action
{
    private static final Log log = LogFactory.getLog( UploadSVGFileAction.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private GISConfigurationManagerService gisConfigurationManagerService;

    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }

    private I18n i18n;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private File upload;

    public void setUpload( File upload )
    {
        this.upload = upload;
    }

    private String fileName;

    public void setUploadFileName( String fileName )
    {
        this.fileName = fileName;
    }

    private String contentType;

    public void setUploadContentType( String contentType )
    {
        this.contentType = contentType;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        File directory = gisConfigurationManagerService.getGISMapDirectory();

        if ( upload != null )
        {

            try
            {
                FileReader in = new FileReader( upload );

                FileWriter out = new FileWriter( new File( directory, fileName ) );

                int c;

                while ( (c = in.read()) != -1 )
                    out.write( c );

                in.close();
                out.close();

            }
            catch ( IOException e )
            {
                e.printStackTrace();
                return NONE;
            }

        }

        return SUCCESS;
    }

}
