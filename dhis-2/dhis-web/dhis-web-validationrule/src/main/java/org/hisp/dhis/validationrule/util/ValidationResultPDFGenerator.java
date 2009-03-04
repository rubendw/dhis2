package org.hisp.dhis.validationrule.util;

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
import java.util.Collection;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.system.util.PDFUtils;
import org.hisp.dhis.validation.ValidationResult;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidationResultPDFGenerator
    extends PDFUtils
        implements OutputGenerator
{
    // -------------------------------------------------------------------------
    // PDF functionality
    // -------------------------------------------------------------------------

    public void generateOutput( Collection<ValidationResult> results, OutputStream out, I18n i18n, I18nFormat format )
    {
        Document document = openDocument( out );
        
        PdfPTable table = getPdfPTable( true, 0.19f, 0.13f, 0.21f, 0.07f, 0.12f, 0.07f, 0.21f );
        
        table.setHeaderRows( 0 );
        
        table.addCell( getHeader3Cell( i18n.getString( "data_quality_report" ), 7 ) );

        table.addCell( getCell( 7, 8 ) );
        
        table.addCell( getTextCell( i18n.getString( "district_health_information_software" ) + " - " + DateUtils.getMediumDateString(), 7 ) );
        
        table.addCell( getCell( 7, 15 ) );
        
        table.addCell( getItalicCell( i18n.getString( "source" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "period" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "left_side_description" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "value" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "operator" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "value" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "right_side_description" ), 1 ) );
        
        table.addCell( getCell( 7, 8 ) );
        
        if ( results != null )
        {
            for ( ValidationResult validationResult : results )
            {
                OrganisationUnit unit = (OrganisationUnit) validationResult.getSource();
                
                Period period = validationResult.getPeriod();
                
                table.addCell( getTextCell( unit.getName() ) );
                table.addCell( getTextCell( format.formatPeriod( period ) ) );
                table.addCell( getTextCell( validationResult.getValidationRule().getLeftSide().getDescription() ) );
                table.addCell( getTextCell( String.valueOf( validationResult.getLeftsideValue() ) ) );
                table.addCell( getTextCell( i18n.getString( validationResult.getValidationRule().getOperator() ), 1, ALIGN_CENTER ) );
                table.addCell( getTextCell( String.valueOf( validationResult.getRightsideValue() ) ) );
                table.addCell( getTextCell( validationResult.getValidationRule().getRightSide().getDescription() ) );                    
            }
        }
        
        addTableToDocument( document, table );
        
        closeDocument( document );
    }
}
