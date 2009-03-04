package org.hisp.dhis.reporting.completeness.workbook;

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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.reporting.completeness.util.OutputGenerator;
import org.hisp.dhis.system.util.DateUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataCompletenessWorkbookGenerator
    implements OutputGenerator
{
    private final static int MARGIN_LEFT = 1;
        
    public void generateOutput( Collection<DataSetCompletenessResult> results, OutputStream out, I18n i18n, OrganisationUnit unit, DataSet dataSet )
    {
        WritableCellFormat documentTitle = new WritableCellFormat( new WritableFont( WritableFont.TAHOMA, 15, WritableFont.NO_BOLD, false ) );
        WritableCellFormat subTitle = new WritableCellFormat( new WritableFont( WritableFont.TAHOMA, 13, WritableFont.NO_BOLD, false ) );
        WritableCellFormat columnHeader = new WritableCellFormat( new WritableFont( WritableFont.TAHOMA, 11, WritableFont.NO_BOLD, true ) );
        WritableCellFormat text = new WritableCellFormat( new WritableFont( WritableFont.ARIAL, 11, WritableFont.NO_BOLD, false ) );            
        
        try
        {
            WritableWorkbook workbook = Workbook.createWorkbook( out );
        
            WritableSheet sheet = workbook.createSheet( "Data completeness", 0 );

            String dataSetName = dataSet != null ? " - " + dataSet.getName() : new String();
            
            sheet.addCell( new Label( MARGIN_LEFT, 1, i18n.getString( "data_completeness_report" ) + " - " + unit.getName() + dataSetName, documentTitle ) );
            
            sheet.addCell( new Label( MARGIN_LEFT, 3, i18n.getString( "district_health_information_software" ) + " - " + DateUtils.getMediumDateString(), subTitle ) );
            
            int row = 5;
            
            sheet.addCell( new Label( MARGIN_LEFT + 0, row, i18n.getString( "name" ), columnHeader ) );
            sheet.addCell( new Label( MARGIN_LEFT + 1, row, i18n.getString( "actual" ), columnHeader ) );
            sheet.addCell( new Label( MARGIN_LEFT + 2, row, i18n.getString( "target" ), columnHeader ) );
            sheet.addCell( new Label( MARGIN_LEFT + 3, row, i18n.getString( "percent" ), columnHeader ) );
            sheet.addCell( new Label( MARGIN_LEFT + 4, row, i18n.getString( "on_time" ), columnHeader ) );
            sheet.addCell( new Label( MARGIN_LEFT + 5, row, i18n.getString( "percent" ), columnHeader ) );
            
            row = 7;
            
            for ( DataSetCompletenessResult result : results )
            {
                sheet.addCell( new Label( MARGIN_LEFT + 0, row, result.getName(), text ) );
                sheet.addCell( new Number( MARGIN_LEFT + 1, row, result.getRegistrations(), text ) );
                sheet.addCell( new Number( MARGIN_LEFT + 2, row, result.getSources(), text ) );
                sheet.addCell( new Number( MARGIN_LEFT + 3, row, result.getPercentage(), text ) );
                sheet.addCell( new Number( MARGIN_LEFT + 4, row, result.getRegistrationsOnTime(), text ) );
                sheet.addCell( new Number( MARGIN_LEFT + 5, row, result.getPercentageOnTime(), text ) );
                
                row++;
            }

            workbook.write();
            
            workbook.close();
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to create workbook", ex );
        }
        catch ( RowsExceededException ex )
        {
            throw new RuntimeException( "Rows exceeded", ex );
        }
        catch ( WriteException ex )
        {
            throw new RuntimeException( "Write failed", ex );
        }
    }
}
