package org.hisp.dhis.importexport.ixf.exporter;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.amplecode.staxwax.factory.XMLFactory;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ExportPipeThread;
import org.hisp.dhis.importexport.ExportService;
import org.hisp.dhis.importexport.ixf.config.IXFConfigurationManager;
import org.hisp.dhis.importexport.ixf.converter.CountryConverter;
import org.hisp.dhis.importexport.ixf.converter.DimensionConverter;
import org.hisp.dhis.importexport.ixf.converter.FileMetaDataConverter;
import org.hisp.dhis.importexport.ixf.converter.GeoCodeConverter;
import org.hisp.dhis.importexport.ixf.converter.GeoLevelConverter;
import org.hisp.dhis.importexport.ixf.converter.GeoTypeConverter;
import org.hisp.dhis.importexport.ixf.converter.IndicatorConverter;
import org.hisp.dhis.importexport.ixf.converter.LanguageConverter;
import org.hisp.dhis.importexport.ixf.converter.SourceConverter;
import org.hisp.dhis.importexport.ixf.converter.TimePeriodConverter;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultIXFExportService.java 5375 2008-06-11 16:42:54Z larshelg $
 */
public class DefaultIXFExportService
    implements ExportService
{
    private static final String ZIP_ENTRY_NAME = "Export.xml";
    private static final String ROOT_NAME = "aidsSchema";
    private static final String[] ROOT_PROPERTIES = { "xmlns:xsi", "http://www.w3.org/2001/xmlschema-instance", 
                                  "xmlns", "http://www.unaids.org/ns/cris",
                                  "xmlns:cris", "http://www.unaids.org/ns/cris-ext",
                                  "xmlns:aida", "http://www.idmlinitiative.org/resources/dtds/AIDA22.xsd",
                                  "schemaVersion", "2.0" };
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private IXFConfigurationManager configurationManager;

    public void setConfigurationManager( IXFConfigurationManager configurationManager )
    {
        this.configurationManager = configurationManager;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    // -------------------------------------------------------------------------
    // IXFExportService implementation
    // -------------------------------------------------------------------------

    public InputStream exportData( ExportParams params )
    {
        try
        {
            // -------------------------------------------------------------------------
            // Pipes are input/output pairs. Data written on the output stream shows 
            // up on the input stream at the other end of the pipe. 
            // -------------------------------------------------------------------------
            
            PipedOutputStream out = new PipedOutputStream();
            
            PipedInputStream in = new PipedInputStream( out );

            ZipOutputStream zipOut = new ZipOutputStream( out );
            
            zipOut.putNextEntry( new ZipEntry( ZIP_ENTRY_NAME ) );

            XMLWriter writer = XMLFactory.getXMLWriter( zipOut );
            
            // -------------------------------------------------------------------------
            // Writes to one end of the pipe 
            // -------------------------------------------------------------------------
            
            ExportPipeThread thread = new ExportPipeThread();
            
            thread.setZipOutputStream( zipOut );
            thread.setParams( params );
            thread.setWriter( writer );
            thread.setRootName( ROOT_NAME );
            thread.setRootProperties( ROOT_PROPERTIES );
            
            thread.registerXMLConverter( new FileMetaDataConverter( configurationManager ) );
            thread.registerXMLConverter( new TimePeriodConverter() );
            thread.registerXMLConverter( new SourceConverter() );
            thread.registerXMLConverter( new LanguageConverter() );
            thread.registerXMLConverter( new GeoLevelConverter( configurationManager, organisationUnitService ) );
            thread.registerXMLConverter( new GeoTypeConverter() );
            thread.registerXMLConverter( new CountryConverter( configurationManager ) );
            thread.registerXMLConverter( new GeoCodeConverter() );
            thread.registerXMLConverter( new DimensionConverter() );
            thread.registerXMLConverter( new IndicatorConverter( dataValueService ) );
            
            thread.start();

            // -------------------------------------------------------------------------
            // Reads at the other end of the pipe 
            // -------------------------------------------------------------------------
            
            InputStream bis = new BufferedInputStream( in );
                                    
            return bis;
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Error occured during export to stream", ex );
        }
    }
}
