package org.hisp.dhis.importexport.dhis14.file.importer;

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

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementGroupMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataValueBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.GroupSetBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.GroupSetMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ImportDataValueBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorGroupMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorTypeBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.OrganisationUnitBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.OrganisationUnitGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.OrganisationUnitGroupMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.PeriodBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.SourceBatchHandler;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.importexport.ImportInternalProcess;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.dhis14.file.query.QueryManager;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.DataElementGroupMemberRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.DataElementGroupRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.DataElementRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.DataSetMemberRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.DataSetRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.GroupSetMemberRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.GroupSetRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.IndicatorGroupMemberRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.IndicatorGroupRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.IndicatorRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.IndicatorTypeRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.OnChangePeriodRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.OrganisationUnitGroupMemberRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.OrganisationUnitGroupRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.OrganisationUnitRelationshipRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.OrganisationUnitRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.PeriodRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.RoutineDataValueRowHandler;
import org.hisp.dhis.importexport.dhis14.file.rowhandler.SemiPermanentDataValueRowHandler;
import org.hisp.dhis.importexport.dhis14.util.OnChangePeriodUtil;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.importexport.mapping.ObjectMappingGenerator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.util.DateUtils;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultDhis14FileImportService.java 5517 2008-08-04 14:59:27Z larshelg $
 */
public class DefaultDhis14FileImportService
    extends ImportInternalProcess
{
    private final Log log = LogFactory.getLog( DefaultDhis14FileImportService.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private QueryManager queryManager;

    public void setQueryManager( QueryManager queryManager )
    {
        this.queryManager = queryManager;
    }

    private ObjectMappingGenerator objectMappingGenerator;

    public void setObjectMappingGenerator( ObjectMappingGenerator objectMappingGenerator )
    {
        this.objectMappingGenerator = objectMappingGenerator;
    }
    
    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }
    
    private ImportObjectService importObjectService;

    public void setImportObjectService( ImportObjectService importObjectService )
    {
        this.importObjectService = importObjectService;
    }
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementCategoryComboService categoryComboService;
    
    public void setCategoryComboService( DataElementCategoryComboService categoryComboService )
    {
        this.categoryComboService = categoryComboService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
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
    
    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }
    
    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DefaultDhis14FileImportService()
    {
        super();
    }
    
    // -------------------------------------------------------------------------
    // ImportInternalProcess implementation
    // -------------------------------------------------------------------------

    public void importData( ImportParams params, InputStream inputStream )
    {
        if ( params.isPreview() )
        {
            importObjectService.deleteImportObjects();
        }
        
        importDataElements( params );        
        importIndicatorTypes( params );
        importIndicators( params );
        importDataElementGroups( params );
        importDataElementGroupMembers( params );
        importIndicatorGroups( params );
        importIndicatorGroupMembers( params );
        
        importDataSets( params );
        importDataSetMembers( params );

        importOrganisationUnits( params );            
        importOrganisationUnitGroups( params );
        importOrganisationUnitGroupMembers( params );
        importGroupSets( params );
        importGroupSetMembers( params );
        importOrganisationUnitRelationships( params );
        importOrganisationUnitHierarchy();

        if ( params.isDataValues() )
        {
            importPeriods( params );
            importRoutineDataValues( params );

            importOnChangePeriods( params );
            importSemiPermanentDataValues( params );
        }
        
        setProgress( 100, "import_process_done" );
        
        NameMappingUtil.clearMapping();
    }
    
    // -------------------------------------------------------------------------
    // DataElement and Indicator
    // -------------------------------------------------------------------------

    private void importDataElements( ImportParams params )
    {
        setProgress( 0, "importing_data_elements" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementBatchHandler.class );
        
        DataElementCategoryCombo categoryCombo = categoryComboService.
            getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );
        
        RowHandler rowHandler = new DataElementRowHandler( batchHandler,
            importObjectService,
            dataElementService, 
            params,
            categoryCombo );

        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getDataElements", rowHandler );

        batchHandler.flush();
        
        log.info( "Imported DataElements" );
    }
    
    private void importIndicatorTypes( ImportParams params )
    {
        setProgress( 3, "importing_indicator_types" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorTypeBatchHandler.class );
        
        RowHandler rowHandler = new IndicatorTypeRowHandler( batchHandler,
            importObjectService,
            indicatorService,
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getIndicatorTypes", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported IndicatorTypes" );
    }
    
    private void importIndicators( ImportParams params )
    {
        setProgress( 6, "importing_indicators" );
        
        BatchHandler indicatorBatchHandler = batchHandlerFactory.createBatchHandler( IndicatorBatchHandler.class );
        BatchHandler dataElementBatchHandler = batchHandlerFactory.createBatchHandler( DataElementBatchHandler.class );
        BatchHandler indicatorTypeBatchHandler = batchHandlerFactory.createBatchHandler( IndicatorTypeBatchHandler.class );
        
        RowHandler rowHandler = new IndicatorRowHandler( indicatorBatchHandler,
            importObjectService,
            indicatorService,
            objectMappingGenerator.getIndicatorTypeMapping( params.isPreview() ), 
            objectMappingGenerator.getDataElementMapping( params.isPreview() ),
            params );
        
        indicatorBatchHandler.init();
        dataElementBatchHandler.init();
        indicatorTypeBatchHandler.init();
        
        queryManager.queryWithRowhandler( "getIndicators", rowHandler );
        
        indicatorBatchHandler.flush();
        dataElementBatchHandler.flush();
        indicatorTypeBatchHandler.flush();
        
        log.info( "Imported Indicators" );
    }
    
    private void importDataElementGroups( ImportParams params )
    {
        setProgress( 9, "importing_data_element_groups" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupBatchHandler.class );
        
        RowHandler rowHandler = new DataElementGroupRowHandler( batchHandler,
            importObjectService,
            dataElementService, 
            params );
        
        batchHandler.init();        
        
        queryManager.queryWithRowhandler( "getDataElementGroups", rowHandler );
        
        batchHandler.flush();        
        
        log.info( "Imported DataElementGroups" );
    }
    
    private void importIndicatorGroups( ImportParams params )
    {
        setProgress( 12, "importing_indicator_groups" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupBatchHandler.class );
        
        RowHandler rowHandler = new IndicatorGroupRowHandler( batchHandler,
            importObjectService,
            indicatorService, 
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getIndicatorGroups", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported IndicatorGroups" );
    }
    
    private void importDataElementGroupMembers( ImportParams params )
    {
        setProgress( 15, "importing_data_element_group_members" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupMemberBatchHandler.class );
        
        RowHandler rowHandler = new DataElementGroupMemberRowHandler( batchHandler,
            importObjectService,
            objectMappingGenerator.getDataElementMapping( params.isPreview() ),
            objectMappingGenerator.getDataElementGroupMapping( params.isPreview() ),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getDataElementGroupMembers", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported DataElementGroup members" );
    }

    private void importIndicatorGroupMembers( ImportParams params )
    {
        setProgress( 18, "importing_indicator_group_members" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupMemberBatchHandler.class );
        
        RowHandler rowHandler = new IndicatorGroupMemberRowHandler( batchHandler,
            importObjectService,
            objectMappingGenerator.getIndicatorMapping( params.isPreview() ),
            objectMappingGenerator.getIndicatorGroupMapping( params.isPreview() ),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getIndicatorGroupMembers", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported IndicatorGroup members" );
    }
    
    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    private void importDataSets( ImportParams params )
    {
        setProgress( 21, "importing_data_sets" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetBatchHandler.class );
        
        RowHandler rowHandler = new DataSetRowHandler( batchHandler,
            importObjectService,
            dataSetService,
            objectMappingGenerator.getPeriodTypeMapping(),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getDataSets", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported DataSets" );
    }
    
    private void importDataSetMembers( ImportParams params )
    {
        setProgress( 24, "importing_data_set_members" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetMemberBatchHandler.class );
        
        RowHandler rowHandler = new DataSetMemberRowHandler( batchHandler,
            importObjectService,
            objectMappingGenerator.getDataElementMapping( params.isPreview() ), 
            objectMappingGenerator.getDataSetMapping( params.isPreview() ),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getDataSetMembers", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported DataSet members" );
    }

    // -------------------------------------------------------------------------
    // OrganisatonUnit
    // -------------------------------------------------------------------------

    private void importOrganisationUnits( ImportParams params )
    {
        setProgress( 27, "importing_organisation_units" );
        
        BatchHandler sourceBatchHandler = batchHandlerFactory.createBatchHandler( SourceBatchHandler.class );
        BatchHandler organisationUnitBatchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );
        
        RowHandler rowHandler = new OrganisationUnitRowHandler( organisationUnitBatchHandler, 
            sourceBatchHandler,
            importObjectService,
            organisationUnitService,
            params );
        
        sourceBatchHandler.init();
        organisationUnitBatchHandler.init();
        
        queryManager.queryWithRowhandler( "getOrganisationUnits", rowHandler );
        
        sourceBatchHandler.flush();
        organisationUnitBatchHandler.flush();
        
        log.info( "Imported OrganisationUnits" );       
    }
    
    private void importOrganisationUnitGroups( ImportParams params )
    {
        setProgress( 36, "importing_organisation_unit_groups" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupBatchHandler.class );
        
        RowHandler rowHandler = new OrganisationUnitGroupRowHandler( batchHandler,
            importObjectService,
            organisationUnitGroupService,
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getOrganisationUnitGroups", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported OrganisationUnitGroups" );
    }
    
    private void importOrganisationUnitGroupMembers( ImportParams params )
    {
        setProgress( 39, "importing_organisation_unit_group_members" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupMemberBatchHandler.class );
        
        RowHandler rowHandler = new OrganisationUnitGroupMemberRowHandler( batchHandler,
            importObjectService,
            objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ),
            objectMappingGenerator.getOrganisationUnitGroupMapping( params.isPreview() ),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getOrganisationUnitGroupMembers", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported OrganisationUnitGroup members" );
    }
    
    private void importGroupSets( ImportParams params )
    {
        setProgress( 42, "importing_organisation_unit_group_sets" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetBatchHandler.class );
        
        RowHandler rowHandler = new GroupSetRowHandler( batchHandler, 
            importObjectService,
            organisationUnitGroupService,
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getOrganisationUnitGroupSets", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported OrganisationUnitGroupSets" );  
    }
    
    private void importGroupSetMembers( ImportParams params )
    {
        setProgress( 45, "importing_organisation_unit_group_set_members" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetMemberBatchHandler.class );
        
        RowHandler rowHandler = new GroupSetMemberRowHandler( batchHandler,
            importObjectService,
            objectMappingGenerator.getOrganisationUnitGroupMapping( params.isPreview() ),
            objectMappingGenerator.getOrganisationUnitGroupSetMapping( params.isPreview() ),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getOrganisationUnitGroupSetMembers", rowHandler );
        
        batchHandler.flush();
                
        log.info( "Imported OrganisationUnitGroupSet members" );
    }

    private void importOrganisationUnitRelationships( ImportParams params )
    {
        setProgress( 51, "importing_organisation_unit_relationships" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );
        
        RowHandler rowHandler = new OrganisationUnitRelationshipRowHandler( batchHandler,
            importObjectService,
            organisationUnitService,
            objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getOrganisationUnitRelationships", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported OrganisationUnitRelationships" );
    }
    
    private void importOrganisationUnitHierarchy()
    {
        setProgress( 54, "importing_organisation_unit_hierarchy" );
        
        organisationUnitService.addOrganisationUnitHierarchy( DateUtils.getEpoch() );
        
        log.info( "Imported OrganisationUnitHierarchy" );
    }

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    private void importPeriods( ImportParams params )
    {   
        setProgress( 57, "importing_periods" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
        
        RowHandler rowHandler = new PeriodRowHandler( batchHandler, 
            importObjectService,
            periodService,
            objectMappingGenerator.getPeriodTypeMapping(),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getPeriods", rowHandler );
        
        batchHandler.flush();
        
        log.info( "Imported Periods" );
    }

    // -------------------------------------------------------------------------
    // RoutineDataValue
    // -------------------------------------------------------------------------

    private void importRoutineDataValues( ImportParams params )
    {
        setProgress( 60, "importing_routine_data_values" );

        DataElementCategoryCombo categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );
        
        DataElementCategoryOptionCombo categoryOptionCombo = categoryCombo.getOptionCombos().iterator().next();
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataValueBatchHandler.class );
        
        BatchHandler importDataValueBatchHandler = batchHandlerFactory.createBatchHandler( ImportDataValueBatchHandler.class );
                
        RowHandler rowHandler = new RoutineDataValueRowHandler( batchHandler,
            importDataValueBatchHandler,
            dataValueService,
            objectMappingGenerator.getDataElementMapping( params.isPreview() ),
            objectMappingGenerator.getPeriodMapping( params.isPreview() ),
            objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ),
            categoryOptionCombo,
            params );
        
        batchHandler.init();
        
        importDataValueBatchHandler.init();
        
        if ( params.getLastUpdated() == null )
        {
            queryManager.queryWithRowhandler( "getRoutineDataValues", rowHandler );
        }
        else
        {            
            queryManager.queryWithRowhandler( "getRoutineDataValuesLastUpdated", rowHandler, params.getLastUpdated() );
        }
        
        batchHandler.flush();
        
        importDataValueBatchHandler.flush();
        
        log.info( "Imported RoutineDataValues" );
    }

    // -------------------------------------------------------------------------
    // OnChangePeriod
    // -------------------------------------------------------------------------

    private void importOnChangePeriods( ImportParams params )
    {
        setProgress( 70, "importing_on_change_periods" );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
        
        RowHandler rowHandler = new OnChangePeriodRowHandler( batchHandler,
            importObjectService,
            periodService,
            objectMappingGenerator.getPeriodTypeMapping(),
            params );
        
        batchHandler.init();
        
        queryManager.queryWithRowhandler( "getOnChangePeriods", rowHandler );
        
        batchHandler.flush();
        
        OnChangePeriodUtil.clear();
        
        log.info( "Imported OnChangePeriods" );
    }

    // -------------------------------------------------------------------------
    // SemiPermanentDataValue
    // -------------------------------------------------------------------------

    private void importSemiPermanentDataValues( ImportParams params )
    {
        setProgress( 73, "importing_semi_permanent_data_values" );

        DataElementCategoryCombo categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );
        
        DataElementCategoryOptionCombo categoryOptionCombo = categoryCombo.getOptionCombos().iterator().next();
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataValueBatchHandler.class );

        BatchHandler importDataValueBatchHandler = batchHandlerFactory.createBatchHandler( ImportDataValueBatchHandler.class );
        
        RowHandler rowHandler = new SemiPermanentDataValueRowHandler( batchHandler,
            importDataValueBatchHandler,
            dataValueService,
            objectMappingGenerator.getDataElementMapping( params.isPreview() ),
            objectMappingGenerator.getPeriodTypeMapping(),
            objectMappingGenerator.getOnChangePeriodMapping( params.isPreview() ),
            objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ),
            categoryOptionCombo,
            params );
        
        batchHandler.init();
        
        importDataValueBatchHandler.init();
        
        if ( params.getLastUpdated() == null )
        {
            queryManager.queryWithRowhandler( "getSemiPermanentDataValues", rowHandler );
        }
        else
        {
            queryManager.queryWithRowhandler( "getSemiPermanentDataValuesLastUpdated", rowHandler, params.getLastUpdated() );
        }
        
        batchHandler.flush();
        
        importDataValueBatchHandler.flush();
        
        log.info( "Imported SemiPermanentDataValues" );
    }    
}
