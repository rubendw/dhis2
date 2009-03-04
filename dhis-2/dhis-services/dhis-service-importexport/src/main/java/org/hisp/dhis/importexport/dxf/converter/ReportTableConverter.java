package org.hisp.dhis.importexport.dxf.converter;

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

import java.util.Collection;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractReportTableConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.reporttable.RelativePeriods;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTableConverter
    extends AbstractReportTableConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "reportTables";
    public static final String ELEMENT_NAME = "reportTable";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_TABLE_NAME = "tableName";
    
    private static final String FIELD_DO_INDICATORS = "doIndicators";
    private static final String FIELD_DO_PERIODS = "doPeriods";
    private static final String FIELD_DO_ORGANISATION_UNITS = "doOrganisationUnits";
    
    private static final String FIELD_REPORTING_MONTH = "reportingMonth";
    private static final String FIELD_LAST_3_MONTHS = "last3Months";
    private static final String FIELD_LAST_6_MONTHS = "last6Months";
    private static final String FIELD_LAST_9_MONTHS = "last9Months";
    private static final String FIELD_LAST_12_MONTHS = "last12Months";
    private static final String FIELD_SO_FAR_THIS_YEAR = "soFarThisYear";
    private static final String FIELD_LAST_3_TO_6_MONTHS = "last3To6Months";
    private static final String FIELD_LAST_6_TO_9_MONTHS = "last6To9Months";
    private static final String FIELD_LAST_9_TO_12_MONTHS = "last9To12Months";
        
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public ReportTableConverter()
    {   
    }

    /**
     * Constructor for read operations.
     * 
     * @param batchHandler BatchHandler
     * @param reportTableStore ReportTableStore
     * @param importObjectService ImportObjectService
     */
    public ReportTableConverter( BatchHandler batchHandler, 
        ReportTableStore reportTableStore,
        ImportObjectService importObjectService )
    {
        this.batchHandler = batchHandler;
        this.reportTableStore = reportTableStore;
        this.importObjectService = importObjectService;
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<ReportTable> reportTables = params.getReportTables();
        
        if ( reportTables != null && reportTables.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( ReportTable reportTable : reportTables )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_ID, String.valueOf( reportTable.getId() ) );
                writer.writeElement( FIELD_NAME, reportTable.getName() );
                writer.writeElement( FIELD_TABLE_NAME, reportTable.getTableName() );
                
                writer.writeElement( FIELD_DO_INDICATORS, String.valueOf( reportTable.isDoIndicators() ) );
                writer.writeElement( FIELD_DO_PERIODS, String.valueOf( reportTable.isDoPeriods() ) );
                writer.writeElement( FIELD_DO_ORGANISATION_UNITS, String.valueOf( reportTable.isDoUnits() ) );
                
                writer.writeElement( FIELD_REPORTING_MONTH, String.valueOf( reportTable.getRelatives().isReportingMonth() ) );
                writer.writeElement( FIELD_LAST_3_MONTHS, String.valueOf( reportTable.getRelatives().isLast3Months() ) );
                writer.writeElement( FIELD_LAST_6_MONTHS, String.valueOf( reportTable.getRelatives().isLast6Months() ) );
                writer.writeElement( FIELD_LAST_9_MONTHS, String.valueOf( reportTable.getRelatives().isLast9Months() ) );
                writer.writeElement( FIELD_LAST_12_MONTHS, String.valueOf( reportTable.getRelatives().isLast12Months() ) );
                writer.writeElement( FIELD_SO_FAR_THIS_YEAR, String.valueOf( reportTable.getRelatives().isSoFarThisYear() ) );
                writer.writeElement( FIELD_LAST_3_TO_6_MONTHS, String.valueOf( reportTable.getRelatives().isLast3To6Months() ) );
                writer.writeElement( FIELD_LAST_6_TO_9_MONTHS, String.valueOf( reportTable.getRelatives().isLast6To9Months() ) );
                writer.writeElement( FIELD_LAST_9_TO_12_MONTHS, String.valueOf( reportTable.getRelatives().isLast9To12Months() ) );
                                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            ReportTable reportTable = new ReportTable();
            
            RelativePeriods relatives = new RelativePeriods();
            reportTable.setRelatives( relatives );
            
            reader.moveToStartElement( FIELD_ID );
            reportTable.setId( Integer.parseInt( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_NAME );
            reportTable.setName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_TABLE_NAME );
            reportTable.setTableName( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_DO_INDICATORS );
            reportTable.setDoIndicators( Boolean.parseBoolean( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_DO_PERIODS );
            reportTable.setDoPeriods( Boolean.parseBoolean( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_DO_ORGANISATION_UNITS );
            reportTable.setDoUnits( Boolean.parseBoolean( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_REPORTING_MONTH );
            reportTable.getRelatives().setReportingMonth( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_LAST_3_MONTHS );
            reportTable.getRelatives().setLast3Months( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_LAST_6_MONTHS );
            reportTable.getRelatives().setLast6Months( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_LAST_9_MONTHS );
            reportTable.getRelatives().setLast9Months( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_LAST_12_MONTHS );
            reportTable.getRelatives().setLast12Months( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_SO_FAR_THIS_YEAR );
            reportTable.getRelatives().setSoFarThisYear( Boolean.parseBoolean( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_LAST_3_TO_6_MONTHS );
            reportTable.getRelatives().setLast3To6Months( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_LAST_6_TO_9_MONTHS );
            reportTable.getRelatives().setLast6To9Months( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_LAST_9_TO_12_MONTHS );
            reportTable.getRelatives().setLast9To12Months( Boolean.parseBoolean( reader.getElementValue() ) );
            
            NameMappingUtil.addReportTableMapping( reportTable.getId(), reportTable.getName() );
            
            read( reportTable, ReportTable.class, GroupMemberType.NONE, params );
        }        
    }
}
