package org.hisp.dhis.importexport.pdf.exporter;

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

import java.io.OutputStream;

import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.importexport.pdf.util.PDFPrintUtil;
import org.hisp.dhis.system.util.PDFUtils;
import org.hisp.dhis.system.util.StreamUtils;

import com.lowagie.text.Document;

/**
 * @author Lars Helge Overland
 * @version $Id: PDFPipeThread.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class PDFPipeThread
    extends Thread
{
    private OutputStream outputStream;

    public void setOutputStream( OutputStream outputStream )
    {
        this.outputStream = outputStream;
    }
    
    private ExportParams exportParams;

    public void setExportParams( ExportParams exportParams )
    {
        this.exportParams = exportParams;
    }
    
    private PDFConverter dataElementConverter;

    public void setDataElementConverter( PDFConverter dataElementConverter )
    {
        this.dataElementConverter = dataElementConverter;
    }
    
    private PDFConverter indicatorConverter;

    public void setIndicatorConverter( PDFConverter indicatorConverter )
    {
        this.indicatorConverter = indicatorConverter;
    }

    private PDFConverter extendedDataElementConverter;

    public void setExtendedDataElementConverter( PDFConverter extendedDataElementConverter )
    {
        this.extendedDataElementConverter = extendedDataElementConverter;
    }    

    private PDFConverter organisationUnitHierarchyConverter;

    public void setOrganisationUnitHierarchyConverter( PDFConverter organisationUnitHierarchyConverter )
    {
        this.organisationUnitHierarchyConverter = organisationUnitHierarchyConverter;
    }

    private PDFConverter organisationUnitConverter;

    public void setOrganisationUnitConverter( PDFConverter organisationUnitConverter )
    {
        this.organisationUnitConverter = organisationUnitConverter;
    }
    
    // -------------------------------------------------------------------------
    // Thread implementation
    // -------------------------------------------------------------------------
    
    public void run()
    {
        Document document = null;
        
        try
        {
            document = PDFUtils.openDocument( outputStream );
            
            PDFPrintUtil.printDocumentFrontPage( document, exportParams );
            
            if ( exportParams.isExtendedMode() )
            {
                extendedDataElementConverter.write( document, exportParams );
            }
            else
            {            
                dataElementConverter.write( document, exportParams );
            
                indicatorConverter.write( document, exportParams );
            }

            organisationUnitHierarchyConverter.write( document, exportParams );
            
            organisationUnitConverter.write( document, exportParams );
            
            PDFUtils.closeDocument( document );            
        }
        finally
        {
            StreamUtils.closeOutputStream( outputStream );
        }
    }
}
