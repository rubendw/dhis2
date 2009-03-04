package org.hisp.dhis.completeness;

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
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetCompletenessResultBatchHandler;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.completeness.cache.DataSetCompletenessCache;
import org.hisp.dhis.system.process.InternalProcess;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataSetCompletenessInternalProcess
    extends InternalProcess implements DataSetCompletenessExportService
{
    public static final String ID = "internal-process-DataSetCompleteness";
    
    private static final Log log = LogFactory.getLog( DataSetCompletenessInternalProcess.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetCompletenessService completenessService;

    public void setCompletenessService( DataSetCompletenessService completenessService )
    {
        this.completenessService = completenessService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private DataSetCompletenessCache completenessCache;

    public void setCompletenessCache( DataSetCompletenessCache completenessCache )
    {
        this.completenessCache = completenessCache;
    }
    
    private DataSetCompletenessStore completenessStore;

    public void setCompletenessStore( DataSetCompletenessStore completenessStore )
    {
        this.completenessStore = completenessStore;
    }
    
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private Collection<Integer> dataSetIds;

    public void setDataSetIds( Collection<Integer> dataSetIds )
    {
        this.dataSetIds = dataSetIds;
    }

    private Collection<Integer> periodIds;

    public void setPeriodIds( Collection<Integer> periodIds )
    {
        this.periodIds = periodIds;
    }

    private Collection<Integer> organisationUnitIds;

    public void setOrganisationUnitIds( Collection<Integer> organisationUnitIds )
    {
        this.organisationUnitIds = organisationUnitIds;
    }
    
    private Integer reportTableId;

    public void setReportTableId( Integer reportTableId )
    {
        this.reportTableId = reportTableId;
    }
    
    // -------------------------------------------------------------------------
    // AbstractTransactionalInternalProcess implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        exportDataSetCompleteness( dataSetIds, periodIds, organisationUnitIds, reportTableId );
        
        return State.SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Process methods
    // -------------------------------------------------------------------------

    public void exportDataSetCompleteness( Collection<Integer> dataSetIds, 
        Collection<Integer> periodIds, Collection<Integer> organisationUnitIds, Integer reportTableId )
    {
        log.info( "Data completeness export process started" );
        
        completenessStore.deleteDataSetCompleteness( dataSetIds, periodIds, organisationUnitIds );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetCompletenessResultBatchHandler.class );
        
        batchHandler.init();
        
        Collection<Period> periods = periodService.getPeriods( periodIds );
        Collection<OrganisationUnit> units = organisationUnitService.getOrganisationUnits( organisationUnitIds );
        Collection<DataSet> dataSets = dataSetService.getDataSets( dataSetIds );
        
        for ( Period period : periods )
        {
            Collection<Period> intersectingPeriods = periodService.getIntersectingPeriods( period.getStartDate(), period.getEndDate() );
            
            for ( OrganisationUnit unit : units )
            {
                for ( DataSet dataSet : dataSets )
                {
                    DataSetCompletenessResult aggregatedResult = new DataSetCompletenessResult();
                    
                    aggregatedResult.setDataSetId( dataSet.getId() );
                    aggregatedResult.setPeriodId( period.getId() );
                    aggregatedResult.setPeriodName( period.getName() );
                    aggregatedResult.setOrganisationUnitId( unit.getId() );
                    aggregatedResult.setReportTableId( reportTableId );
                    
                    for ( Period intersectingPeriod : intersectingPeriods )
                    {
                        if ( intersectingPeriod.getPeriodType().equals( dataSet.getPeriodType() ) )
                        {
                            Date deadline = completenessCache.getDeadline( intersectingPeriod );
                            
                            DataSetCompletenessResult result = completenessService.getDataSetCompleteness( intersectingPeriod, deadline, unit, dataSet );
                            
                            aggregatedResult.incrementSources( result.getSources() );
                            aggregatedResult.incrementRegistrations( result.getRegistrations() );
                            aggregatedResult.incrementRegistrationsOnTime( result.getRegistrationsOnTime() );
                        }
                    }
                    
                    if ( aggregatedResult.getSources() > 0 )
                    {
                        batchHandler.addObject( aggregatedResult );
                    }
                }
            }
            
            log.info( "Exported data completeness for period " + period.getId() );
        }
        
        completenessCache.clear();
        
        batchHandler.flush();
        
        log.info( "Export process done" );
    }
}
