package org.hisp.dhis.importexport.pdf.converter;

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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.importexport.pdf.util.PDFPrintUtil;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.system.util.PDFUtils;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class OrganisationUnitConverter
    extends PDFUtils 
        implements PDFConverter 
{
    public OrganisationUnitConverter()
    {   
    }
    
    // -------------------------------------------------------------------------
    // PDFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( Document document, ExportParams params )
    {
        PDFPrintUtil.printOrganisationUnitFrontPage( document, params );
        
        I18n i18n = params.getI18n();
        I18nFormat format = params.getFormat();
        
        for ( OrganisationUnit unit : params.getOrganisationUnits() )
        {
            PdfPTable table = getPdfPTable( true, 0.40f, 0.60f );
            
            table.addCell( getHeader3Cell( unit.getName(), 2 ) );
            
            table.addCell( getCell( 2, 15 ) );            

            table.addCell( getItalicCell( i18n.getString( "short_name" ), 1 ) );
            table.addCell( getTextCell( unit.getShortName() ) );

            table.addCell( getItalicCell( i18n.getString( "code" ), 1 ) );
            table.addCell( getTextCell( unit.getOrganisationUnitCode() ) );

            table.addCell( getItalicCell( i18n.getString( "opening_date" ), 1 ) );
            table.addCell( getTextCell( format.formatDate( unit.getOpeningDate() ) ) );

            table.addCell( getItalicCell( i18n.getString( "closed_date" ), 1 ) );
            table.addCell( getTextCell( format.formatDate( unit.getClosedDate() ) ) );

            table.addCell( getItalicCell( i18n.getString( "active" ), 1 ) );
            table.addCell( getTextCell( i18n.getString( String.valueOf( unit.isActive() ) ) ) );

            table.addCell( getItalicCell( i18n.getString( "comment" ), 1 ) );
            table.addCell( getTextCell( unit.getComment() ) );

            table.addCell( getCell( 2, 30 ) );
            
            addTableToDocument( document, table );     
        }
    }
}
