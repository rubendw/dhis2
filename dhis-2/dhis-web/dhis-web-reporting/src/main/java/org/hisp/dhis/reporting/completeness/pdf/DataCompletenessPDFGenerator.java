package org.hisp.dhis.reporting.completeness.pdf;

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

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.reporting.completeness.util.OutputGenerator;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.system.util.PDFUtils;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataCompletenessPDFGenerator
    extends PDFUtils
        implements OutputGenerator
{
    public void generateOutput( Collection<DataSetCompletenessResult> results, OutputStream out, I18n i18n, OrganisationUnit unit, DataSet dataSet )
    {
        Document document = openDocument( out );
        
        PdfPTable table = getPdfPTable( true, 0.501f, 0.10f, 0.10f, 0.10f, 0.10f, 0.10f );
        
        table.setHeaderRows( 1 );

        String dataSetName = dataSet != null ? " - " + dataSet.getName() : new String();
        
        table.addCell( getHeader3Cell( i18n.getString( "data_completeness_report" ) + " - " + unit.getName() + dataSetName, 6 ) );

        table.addCell( getCell( 6, 8 ) );

        table.addCell( getTextCell( i18n.getString( "district_health_information_software" ) + " - " + DateUtils.getMediumDateString(), 6 ) );

        table.addCell( getCell( 6, 15 ) );
        
        table.addCell( getItalicCell( i18n.getString( "name" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "actual" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "target" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "percent" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "on_time" ), 1 ) );
        table.addCell( getItalicCell( i18n.getString( "percent" ), 1 ) );

        table.addCell( getCell( 6, 8 ) );
        
        if ( results != null )
        {
            for ( DataSetCompletenessResult result : results )
            {
                table.addCell( getTextCell( result.getName() ) );
                table.addCell( getTextCell( String.valueOf( result.getRegistrations() ) ) );
                table.addCell( getTextCell( String.valueOf( result.getSources() ) ) );
                table.addCell( getTextCell( String.valueOf( result.getPercentage() ) ) );
                table.addCell( getTextCell( String.valueOf( result.getRegistrationsOnTime() ) ) );
                table.addCell( getTextCell( String.valueOf( result.getPercentageOnTime() ) ) );
            }
        }        
        
        addTableToDocument( document, table );
        
        closeDocument( document );
    }
}
