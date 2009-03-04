package org.hisp.dhis.importexport.pdf.util;

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

import java.util.Calendar;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.system.util.PDFUtils;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Lars Helge Overland
 * @version $Id: PDFPrintUtil.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class PDFPrintUtil
    extends PDFUtils
{
    private static final Font TEXT = new Font( Font.HELVETICA, 9, Font.NORMAL );
    private static final Font HEADER2 = new Font( Font.HELVETICA, 16, Font.BOLD );
    
    public static void printDataElementFrontPage( Document document, ExportParams exportParams )
    {
        if ( exportParams.getDataElements().size() > 0 )
        {
            I18n i18n = exportParams.getI18n();
            
            String title = i18n.getString( "data_elements" );
            
            printFrontPage( document, exportParams, title );
        }
    }
    
    public static void printIndicatorFrontPage( Document document, ExportParams exportParams )
    {
        if ( exportParams.getIndicators().size() > 0 )
        {
            I18n i18n = exportParams.getI18n();
            
            String title = i18n.getString( "indicators" );
            
            printFrontPage( document, exportParams, title );
        }
    }

    public static void printDataElementConceptFrontPage( Document document, ExportParams exportParams )
    {
        if ( exportParams.getIndicators().size() > 0 )
        {
            I18n i18n = exportParams.getI18n();
            
            String title = i18n.getString( "data_element_concepts" );
            
            printFrontPage( document, exportParams, title );
        }
    }

    public static void printOrganisationUnitHierarchyFrontPage( Document document, ExportParams exportParams )
    {
        if ( exportParams.getOrganisationUnits().size() > 0 )
        {
            I18n i18n = exportParams.getI18n();
            
            String title = i18n.getString( "organisation_unit_hierarchy" );
            
            printFrontPage( document, exportParams, title );
        }
    }
    
    public static void printOrganisationUnitFrontPage( Document document, ExportParams exportParams )
    {
        if ( exportParams.getOrganisationUnits().size() > 0 )
        {
            I18n i18n = exportParams.getI18n();
            
            String title = i18n.getString( "organisation_units" );
            
            printFrontPage( document, exportParams, title );
        }
    }
    
    public static void printDocumentFrontPage( Document document, ExportParams exportParams )
    {
        I18n i18n = exportParams.getI18n();
        
        String title = i18n.getString( "data_dictionary" );
        
        printFrontPage( document, exportParams, title );
    }
    
    private static void printFrontPage( Document document, ExportParams exportParams, String title )
    {
        I18n i18n = exportParams.getI18n();
        I18nFormat format = exportParams.getFormat();
        
        PdfPTable table = getPdfPTable( true, 1.00f );
        
        table.addCell( getCell( i18n.getString( "district_health_information_software_2" ), 1, TEXT, ALIGN_CENTER ) );
        
        table.addCell( getCell( 1, 40 ) );

        table.addCell( getCell( title, 1, HEADER2, ALIGN_CENTER ) );

        table.addCell( getCell( 1, 40 ) );
        
        String date = format.formatDate( Calendar.getInstance().getTime() );
        
        table.addCell( getCell( date, 1, TEXT, ALIGN_CENTER ) );
        
        addTableToDocument( document, table );
        
        moveToNewPage( document );
    }
}
