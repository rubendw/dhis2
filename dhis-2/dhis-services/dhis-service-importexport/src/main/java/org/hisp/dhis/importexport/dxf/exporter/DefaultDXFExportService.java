package org.hisp.dhis.importexport.dxf.exporter;

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
import org.hisp.dhis.importexport.dxf.converter.CategoryCategoryOptionAssociationConverter;
import org.hisp.dhis.importexport.dxf.converter.CategoryComboCategoryAssociationConverter;
import org.hisp.dhis.importexport.dxf.converter.CompleteDataSetRegistrationConverter;
import org.hisp.dhis.importexport.dxf.converter.DataDictionaryConverter;
import org.hisp.dhis.importexport.dxf.converter.DataDictionaryDataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.DataDictionaryIndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryComboConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryOptionComboConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryOptionConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementGroupConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementGroupMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.DataSetConverter;
import org.hisp.dhis.importexport.dxf.converter.DataSetMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.DataValueConverter;
import org.hisp.dhis.importexport.dxf.converter.ExtendedDataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.ExtendedIndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.GroupSetConverter;
import org.hisp.dhis.importexport.dxf.converter.GroupSetMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorGroupConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorGroupMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorTypeConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitGroupConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitGroupMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitRelationshipConverter;
import org.hisp.dhis.importexport.dxf.converter.PeriodConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableIndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableOrganisationUnitConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTablePeriodConverter;
import org.hisp.dhis.importexport.dxf.converter.ValidationRuleConverter;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultDXFExportService.java 5800 2008-10-02 18:41:41Z larshelg $
 */
public class DefaultDXFExportService
    implements ExportService
{
    private static final String ZIP_ENTRY_NAME = "Export.xml";
    private static final String ROOT_NAME = "dxf";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
        
    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    // -------------------------------------------------------------------------
    // ExportService implementation
    // -------------------------------------------------------------------------

    public InputStream exportData( ExportParams params )
    {
        try
        {
            // -----------------------------------------------------------------
            // Pipes are input/output pairs. Data written on the output stream 
            // shows up on the input stream at the other end of the pipe. 
            // -----------------------------------------------------------------
            
            PipedOutputStream out = new PipedOutputStream();
            
            PipedInputStream in = new PipedInputStream( out );
            
            ZipOutputStream zipOut = new ZipOutputStream( out );
            
            zipOut.putNextEntry( new ZipEntry( ZIP_ENTRY_NAME ) );

            XMLWriter writer = XMLFactory.getXMLWriter( zipOut );
            
            // -----------------------------------------------------------------
            // Writes to one end of the pipe 
            // -----------------------------------------------------------------
            
            ExportPipeThread thread = new ExportPipeThread();
            
            thread.setZipOutputStream( zipOut );
            thread.setParams( params );
            thread.setWriter( writer );
            thread.setRootName( ROOT_NAME );
            
            thread.registerXMLConverter( new DataElementCategoryOptionConverter() );
            thread.registerXMLConverter( new DataElementCategoryConverter() );
            thread.registerXMLConverter( new DataElementCategoryComboConverter() );
            thread.registerXMLConverter( new DataElementCategoryOptionComboConverter() );
            
            thread.registerXMLConverter( new CategoryCategoryOptionAssociationConverter() );
            thread.registerXMLConverter( new CategoryComboCategoryAssociationConverter() );
            
            thread.registerXMLConverter( params.isExtendedMode() ? new ExtendedDataElementConverter() : new DataElementConverter() );
            thread.registerXMLConverter( new DataElementGroupConverter() );
            thread.registerXMLConverter( new DataElementGroupMemberConverter() );
            
            thread.registerXMLConverter( new IndicatorTypeConverter() );
            thread.registerXMLConverter( params.isExtendedMode() ? new ExtendedIndicatorConverter() : new IndicatorConverter() );
            thread.registerXMLConverter( new IndicatorGroupConverter() );
            thread.registerXMLConverter( new IndicatorGroupMemberConverter() );
            
            thread.registerXMLConverter( new DataDictionaryConverter() );
            thread.registerXMLConverter( new DataDictionaryDataElementConverter() );
            thread.registerXMLConverter( new DataDictionaryIndicatorConverter() );
                        
            thread.registerXMLConverter( new DataSetConverter() );
            thread.registerXMLConverter( new DataSetMemberConverter() );
            
            thread.registerXMLConverter( new OrganisationUnitConverter() );
            thread.registerXMLConverter( new OrganisationUnitRelationshipConverter() );
            thread.registerXMLConverter( new OrganisationUnitGroupConverter() );
            thread.registerXMLConverter( new OrganisationUnitGroupMemberConverter() );
            
            thread.registerXMLConverter( new GroupSetConverter() );
            thread.registerXMLConverter( new GroupSetMemberConverter() );
            
            thread.registerXMLConverter( new ValidationRuleConverter() );
            
            thread.registerXMLConverter( new PeriodConverter() );
            
            thread.registerXMLConverter( new ReportTableConverter() );
            thread.registerXMLConverter( new ReportTableIndicatorConverter() );
            thread.registerXMLConverter( new ReportTablePeriodConverter() );
            thread.registerXMLConverter( new ReportTableOrganisationUnitConverter() );
            
            thread.registerXMLConverter( new CompleteDataSetRegistrationConverter() );
            
            thread.registerXMLConverter( new DataValueConverter( dataValueService ) );
            
            thread.start();
            
            // -----------------------------------------------------------------
            // Reads at the other end of the pipe 
            // -----------------------------------------------------------------
            
            InputStream bis = new BufferedInputStream( in );
            
            return bis;
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Error occured during export to stream", ex );
        }
    }
}
