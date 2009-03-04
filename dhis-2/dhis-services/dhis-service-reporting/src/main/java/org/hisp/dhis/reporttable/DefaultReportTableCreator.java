package org.hisp.dhis.reporttable;

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

import static org.hisp.dhis.system.util.ConversionUtils.getIdentifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.GenericBatchHandler;
import org.hisp.dhis.common.MetaObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.datamart.DataMartService;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriodType;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.reporttable.jdbc.ReportTableManager;
import org.hisp.dhis.completeness.DataSetCompletenessExportService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultReportTableCreator
    extends ReportTableInternalProcess
{
    private static final Log log = LogFactory.getLog( DefaultReportTableCreator.class );
    
    private static final String NULL_REPLACEMENT = "0.0";
    
    // ---------------------------------------------------------------------
    // Dependencies
    // ---------------------------------------------------------------------

    private ReportTableManager reportTableManager;
    
    public void setReportTableManager( ReportTableManager reportTableManager )
    {
        this.reportTableManager = reportTableManager;
    }
    
    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }
    
    private BatchHandlerFactory batchHandlerFactory;
    
    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }
    
    private DataMartService dataMartService;
    
    public void setDataMartService( DataMartService dataMartService )
    {
        this.dataMartService = dataMartService;
    }
    
    private DataMartStore dataMartStore;

    public void setDataMartStore( DataMartStore dataMartStore )
    {
        this.dataMartStore = dataMartStore;
    }
    
    private DataSetCompletenessExportService completenessExportService;
    
    public void setCompletenessExportService( DataSetCompletenessExportService completenessExportService )
    {
        this.completenessExportService = completenessExportService;
    }

    public void createReportTable( ReportTable reportTable, boolean doDataMart )
    {
        log.info( "Process started for report table: '" + reportTable.getName() + "'" );
        
        setProgress( 10, "aggregating_data" );
        
        reportTableService.saveOrUpdateReportTable( reportTable );
        
        // ---------------------------------------------------------------------
        // Exporting relevant data to data mart
        // ---------------------------------------------------------------------

        if ( doDataMart )
        {
            String mode = reportTable.getMode();
            
            if ( mode.equals( ReportTable.MODE_DATAELEMENTS ) || mode.equals( ReportTable.MODE_INDICATORS ) )
            {
                dataMartService.export( getIdentifiers( DataElement.class, reportTable.getDataElements() ),
                    getIdentifiers( Indicator.class, reportTable.getIndicators() ),
                    getIdentifiers( Period.class, reportTable.getAllPeriods() ),
                    getIdentifiers( OrganisationUnit.class, reportTable.getUnits() ) );
            }
            else if ( mode.equals( ReportTable.MODE_DATASETS ) )
            {
                completenessExportService.exportDataSetCompleteness( getIdentifiers( DataSet.class, reportTable.getDataSets() ),
                    getIdentifiers( Period.class, reportTable.getAllPeriods() ),
                    getIdentifiers( OrganisationUnit.class, reportTable.getUnits() ),
                    reportTable.getId() );
            }
        }
        
        // ---------------------------------------------------------------------
        // Creating report table
        // ---------------------------------------------------------------------
        
        setProgress( 50, "creating_report_datasource" );
        
        reportTableManager.createReportTable( reportTable );

        // ---------------------------------------------------------------------
        // Updating existingt table name after deleting the database table
        // ---------------------------------------------------------------------
        
        reportTable.updateExistingTableName();
        
        reportTableService.saveOrUpdateReportTable( reportTable );
        
        log.info( "Created report table" );
        
        // ---------------------------------------------------------------------
        // Populating report table
        // ---------------------------------------------------------------------

        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class );

        batchHandler.setTableName( reportTable.getTableName() );
        
        batchHandler.init();
        
        for ( MetaObject metaObject : reportTable.getReportIndicators() )
        {
            for ( Period period : reportTable.getReportPeriods() )
            {
                for ( OrganisationUnit unit : reportTable.getReportUnits() )
                {
                    List<String> values = new ArrayList<String>();
                    
                    Map<String, Double> map = reportTableManager.getAggregatedValueMap( reportTable, metaObject, period, unit );

                    // ---------------------------------------------------------
                    // Identifier
                    // ---------------------------------------------------------

                    if ( reportTable.getIndexColumns().contains( ReportTable.INDICATOR_ID ) )
                    {
                        values.add( String.valueOf( metaObject.getId() ) );
                    }
                    
                    if ( reportTable.getIndexColumns().contains( ReportTable.DATAELEMENT_ID ) )
                    {
                        values.add( String.valueOf( metaObject.getId() ) );
                    }
                    
                    if ( reportTable.getIndexColumns().contains( ReportTable.DATASET_ID ) )
                    {
                        values.add( String.valueOf( metaObject.getId() ) );
                    }
                    
                    if ( reportTable.getIndexColumns().contains( ReportTable.PERIOD_ID ) )
                    {
                        values.add( String.valueOf( period.getId() ) );
                    }
                    
                    if ( reportTable.getIndexColumns().contains( ReportTable.ORGANISATIONUNIT_ID ) )
                    {
                        values.add( String.valueOf( unit.getId() ) );
                    }

                    // ---------------------------------------------------------
                    // Name
                    // ---------------------------------------------------------

                    if ( reportTable.getIndexNameColumns().contains( ReportTable.INDICATOR_NAME ) )
                    {
                        values.add( metaObject.getShortName() );
                    }
                    
                    if ( reportTable.getIndexNameColumns().contains( ReportTable.DATAELEMENT_NAME ) )
                    {
                        values.add( metaObject.getShortName() );
                    }

                    if ( reportTable.getIndexNameColumns().contains( ReportTable.DATASET_NAME ) )
                    {
                        values.add( metaObject.getShortName() );
                    }
                    
                    if ( reportTable.getIndexNameColumns().contains( ReportTable.PERIOD_NAME ) )
                    {
                        values.add( getPeriodName( reportTable, period ) );
                    }
                    
                    if ( reportTable.getIndexNameColumns().contains( ReportTable.ORGANISATIONUNIT_NAME ) )
                    {
                        values.add( unit.getShortName() );
                    }
                    
                    values.add( reportTable.getReportingMonthName() );

                    // ---------------------------------------------------------
                    // Values
                    // ---------------------------------------------------------

                    for ( String identifier : reportTable.getCrossTabIdentifiers() )
                    {
                        values.add( parseAndReplaceNull( map.get( identifier ) ) );
                    }
                    
                    batchHandler.addObject( values );
                }
            }
        }
        
        batchHandler.flush();       

        setProgress( 100, "process_done" );        
        
        log.info( "Populated report table: '" + reportTable.getTableName() + "'" );
    }
    
    public void removeReportTable( ReportTable reportTable )
    {
        reportTableManager.removeReportTable( reportTable );
    }
    
    @Override
    public void deleteRelativePeriods()
    {
        dataMartStore.deleteRelativePeriods();
        
        log.info( "Deleted relative periods" );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private static String parseAndReplaceNull( Double value )
    {
        return value != null ? String.valueOf( value ) : NULL_REPLACEMENT;
    }
    
    private String getPeriodName( ReportTable reportTable, Period period )
    {
        if ( period.getPeriodType().getName().equals( RelativePeriodType.NAME ) )
        {
            return period.getName();
        }
        else
        {
            return reportTable.getI18nFormat().formatPeriod( period );
        }
    }
}
