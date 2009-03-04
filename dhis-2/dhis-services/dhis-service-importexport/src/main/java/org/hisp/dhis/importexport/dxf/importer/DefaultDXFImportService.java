package org.hisp.dhis.importexport.dxf.importer;

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
import java.util.zip.ZipInputStream;

import org.amplecode.staxwax.factory.XMLFactory;
import org.amplecode.staxwax.reader.XMLReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.CategoryCategoryOptionAssociationBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.CategoryComboCategoryAssociationBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.CompleteDataSetRegistrationBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataDictionaryBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataDictionaryDataElementBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataDictionaryIndicatorBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementCategoryBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementCategoryComboBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementCategoryOptionBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementGroupMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetMemberBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataValueBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ExtendedDataElementBatchHandler;
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
import org.hisp.dhis.aggregation.batch.handler.ReportTableBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ReportTableDataElementBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ReportTableIndicatorBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ReportTableOrganisationUnitBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ReportTablePeriodBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.SourceBatchHandler;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.importexport.ImportInternalProcess;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.dxf.converter.CategoryCategoryOptionAssociationConverter;
import org.hisp.dhis.importexport.dxf.converter.CategoryComboCategoryAssociationConverter;
import org.hisp.dhis.importexport.dxf.converter.CompleteDataSetRegistrationConverter;
import org.hisp.dhis.importexport.dxf.converter.DataDictionaryConverter;
import org.hisp.dhis.importexport.dxf.converter.DataDictionaryDataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.DataDictionaryIndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryComboConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryOptionComboConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementCategoryOptionConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementGroupConverter;
import org.hisp.dhis.importexport.dxf.converter.DataElementGroupMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.DataSetConverter;
import org.hisp.dhis.importexport.dxf.converter.DataSetMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.DataValueConverter;
import org.hisp.dhis.importexport.dxf.converter.ExtendedDataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.ExtendedIndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.GroupSetConverter;
import org.hisp.dhis.importexport.dxf.converter.GroupSetMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorGroupConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorGroupMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.IndicatorTypeConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitGroupConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitGroupMemberConverter;
import org.hisp.dhis.importexport.dxf.converter.OrganisationUnitRelationshipConverter;
import org.hisp.dhis.importexport.dxf.converter.PeriodConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableDataElementConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableIndicatorConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTableOrganisationUnitConverter;
import org.hisp.dhis.importexport.dxf.converter.ReportTablePeriodConverter;
import org.hisp.dhis.importexport.dxf.converter.ValidationRuleConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.importexport.mapping.ObjectMappingGenerator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.reporttable.ReportTableStore;
import org.hisp.dhis.system.util.StreamUtils;
import org.hisp.dhis.validation.ValidationRuleService;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultDXFImportService.java 5801 2008-10-03 06:41:02Z larshelg $
 */
public class DefaultDXFImportService
    extends ImportInternalProcess
{
    private final Log log = LogFactory.getLog( DefaultDXFImportService.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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
    
    private DataElementCategoryService categoryService;

    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }
    
    private DataElementCategoryOptionService categoryOptionService;

    public void setCategoryOptionService( DataElementCategoryOptionService categoryOptionService )
    {
        this.categoryOptionService = categoryOptionService;
    }

    private DataElementCategoryComboService categoryComboService;
    
    public void setCategoryComboService( DataElementCategoryComboService categoryComboService )
    {
        this.categoryComboService = categoryComboService;
    }
    
    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService categoryOptionComboService )
    {
        this.categoryOptionComboService = categoryOptionComboService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    private DataDictionaryService dataDictionaryService;

    public void setDataDictionaryService( DataDictionaryService dataDictionaryService )
    {
        this.dataDictionaryService = dataDictionaryService;
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
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }

    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
    }
    
    private ReportTableStore reportTableStore;

    public void setReportTableStore( ReportTableStore reportTableStore )
    {
        this.reportTableStore = reportTableStore;
    }
    
    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }
    
    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }

    private ObjectMappingGenerator objectMappingGenerator;

    public void setObjectMappingGenerator( ObjectMappingGenerator objectMappingGenerator )
    {
        this.objectMappingGenerator = objectMappingGenerator;
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DefaultDXFImportService()
    {
        super();
    }
    
    // -------------------------------------------------------------------------
    // DXFImportService implementation
    // -------------------------------------------------------------------------

    public void importData( ImportParams params, InputStream inputStream )
    {
        if ( params.isPreview() )
        {
            importObjectService.deleteImportObjects();
        }
        
        ZipInputStream zipIn = new ZipInputStream ( inputStream );
        
        StreamUtils.getNextZipEntry( zipIn );
        
        XMLReader reader = XMLFactory.getXMLReader( zipIn );
        
        while ( reader.next() )
        {
            if ( reader.isStartElement( DataElementCategoryOptionConverter.COLLECTION_NAME ) )
            {
                setProgress( 0, "importing_data_element_category_options" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryOptionBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataElementCategoryOptionConverter( batchHandler,
                    importObjectService,
                    categoryOptionService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataElementCategoryOptions" );
            }
            else if ( reader.isStartElement( DataElementCategoryConverter.COLLECTION_NAME ) )
            {
                setProgress( 2, "importing_data_element_categories" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataElementCategoryConverter( batchHandler,
                    importObjectService, 
                    categoryService );

                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataElementCategories" );                
            }
            else if ( reader.isStartElement( DataElementCategoryComboConverter.COLLECTION_NAME ) )
            {
                setProgress( 4, "importing_data_element_category_combos" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryComboBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataElementCategoryComboConverter( batchHandler,
                    importObjectService,
                    categoryComboService );

                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataElementCategoryCombos" );                
            }
            else if ( reader.isStartElement( DataElementCategoryOptionComboConverter.COLLECTION_NAME ) )
            {
                setProgress( 6, "importing_data_element_category_option_combos" );
                
                XMLConverter converter = new DataElementCategoryOptionComboConverter( importObjectService,
                    objectMappingGenerator.getCategoryComboMapping( params.isPreview() ),
                    objectMappingGenerator.getCategoryOptionMapping( params.isPreview() ),
                    categoryOptionComboService,
                    categoryOptionService,
                    categoryComboService );

                converter.read( reader, params );
                
                log.info( "Imported DataElementCategoryOptionCombos" );       
            }
            else if ( reader.isStartElement( CategoryCategoryOptionAssociationConverter.COLLECTION_NAME ) )
            {
                setProgress( 8, "importing_data_element_category_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( CategoryCategoryOptionAssociationBatchHandler.class );

                batchHandler.init();
                
                XMLConverter converter = new CategoryCategoryOptionAssociationConverter( batchHandler,
                    importObjectService,
                    objectMappingGenerator.getCategoryMapping( params.isPreview() ),
                    objectMappingGenerator.getCategoryOptionMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported CategoryCategoryOption associations" );
            }
            else if ( reader.isStartElement( CategoryComboCategoryAssociationConverter.COLLECTION_NAME ) )
            {
                setProgress( 10, "importing_data_element_category_combo_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( CategoryComboCategoryAssociationBatchHandler.class );

                batchHandler.init();
                
                XMLConverter converter = new CategoryComboCategoryAssociationConverter( batchHandler,
                    importObjectService,
                    objectMappingGenerator.getCategoryComboMapping( params.isPreview() ),
                    objectMappingGenerator.getCategoryMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported CategoryComboCategory associations" );
            }
            else if ( reader.isStartElement( DataElementConverter.COLLECTION_NAME ) )
            {                
                setProgress( 12, "importing_data_elements" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementBatchHandler.class );
                
                batchHandler.init();
                                
                XMLConverter converter = new DataElementConverter( batchHandler, 
                    importObjectService,
                    objectMappingGenerator.getCategoryComboMapping( params.isPreview() ),
                    dataElementService );
                
                converter.read( reader, params );                

                batchHandler.flush();  
                
                log.info( "Imported DataElements" );
            }
            else if ( reader.isStartElement( ExtendedDataElementConverter.COLLECTION_NAME ) )
            {                
                setProgress( 14, "importing_data_elements" );

                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementBatchHandler.class );
                
                BatchHandler extendedDataElementBatchHandler = batchHandlerFactory.createBatchHandler( ExtendedDataElementBatchHandler.class );
                
                extendedDataElementBatchHandler.init();
                
                batchHandler.init();
                
                XMLConverter converter = new ExtendedDataElementConverter( batchHandler,
                    extendedDataElementBatchHandler, 
                    importObjectService,
                    objectMappingGenerator.getCategoryComboMapping( params.isPreview() ),
                    dataElementService );
                
                converter.read( reader, params );
                
                extendedDataElementBatchHandler.flush();
                
                batchHandler.flush();
                
                log.info( "Imported ExtendedDataElements" );
            }
            else if ( reader.isStartElement( DataElementGroupConverter.COLLECTION_NAME ) )
            {                
                setProgress( 16, "importing_data_element_groups" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataElementGroupConverter( batchHandler, importObjectService, dataElementService );
                                
                converter.read( reader, params );

                batchHandler.flush();
                
                log.info( "Imported DataElementGroups" );
            }
            else if ( reader.isStartElement( DataElementGroupMemberConverter.COLLECTION_NAME ) )
            {                
                setProgress( 18, "importing_data_element_group_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupMemberBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataElementGroupMemberConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ),
                    objectMappingGenerator.getDataElementGroupMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataElementGroup members" );
            }
            else if ( reader.isStartElement( IndicatorTypeConverter.COLLECTION_NAME ) )
            {                
                setProgress( 20, "importing_indicator_types" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorTypeBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new IndicatorTypeConverter( batchHandler, importObjectService, indicatorService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported IndicatorTypes" );
            }
            else if ( reader.isStartElement( IndicatorConverter.COLLECTION_NAME ) )
            {                
                setProgress( 22, "importing_indicators" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new IndicatorConverter( batchHandler, importObjectService, indicatorService,
                    objectMappingGenerator.getIndicatorTypeMapping( params.isPreview() ), 
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ) );
                                
                converter.read( reader, params );

                batchHandler.flush();
                
                log.info( "Imported Indicators" );
            }
            else if ( reader.isStartElement( ExtendedIndicatorConverter.COLLECTION_NAME ) )
            {
                setProgress( 24, "importing_indicators" );

                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorBatchHandler.class );
                
                BatchHandler extendedDataElementBatchHandler = batchHandlerFactory.createBatchHandler( ExtendedDataElementBatchHandler.class );
                
                extendedDataElementBatchHandler.init();
                
                batchHandler.init();
                
                XMLConverter converter = new ExtendedIndicatorConverter( batchHandler,
                    extendedDataElementBatchHandler,
                    importObjectService, 
                    indicatorService,
                    objectMappingGenerator.getIndicatorTypeMapping( params.isPreview() ), 
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ) );
                
                converter.read( reader, params );

                extendedDataElementBatchHandler.flush();
                
                batchHandler.flush();
                
                log.info( "Imported ExtendedIndicators" );
            }
            else if ( reader.isStartElement( IndicatorGroupConverter.COLLECTION_NAME ) )
            {
                setProgress( 26, "importing_indicator_groups" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new IndicatorGroupConverter( batchHandler, importObjectService, indicatorService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported IndicatorGroups" );
            }
            else if ( reader.isStartElement( IndicatorGroupMemberConverter.COLLECTION_NAME ) )
            {
                setProgress( 28, "importing_indicator_group_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupMemberBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new IndicatorGroupMemberConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getIndicatorMapping( params.isPreview() ),
                    objectMappingGenerator.getIndicatorGroupMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported IndicatorGroup members" );
            }
            else if ( reader.isStartElement( DataDictionaryConverter.COLLECTION_NAME ) )
            {
                setProgress( 28, "importing_data_dictionaries" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataDictionaryConverter( batchHandler, 
                    importObjectService,
                    dataDictionaryService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataDictionaries" );
            }
            else if ( reader.isStartElement( DataDictionaryDataElementConverter.COLLECTION_NAME ) )
            {
                setProgress( 28, "importing_data_dictionary_data_elements" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryDataElementBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataDictionaryDataElementConverter( batchHandler,
                    importObjectService,
                    objectMappingGenerator.getDataDictionaryMapping( params.isPreview() ),
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataDictionary DataElements" );
            }
            else if ( reader.isStartElement( DataDictionaryIndicatorConverter.COLLECTION_NAME ) )
            {
                setProgress( 28, "importing_data_dictionary_indicators" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryIndicatorBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataDictionaryIndicatorConverter( batchHandler,
                    importObjectService,
                    objectMappingGenerator.getDataDictionaryMapping( params.isPreview() ),
                    objectMappingGenerator.getIndicatorMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataDictionary Indicators" );
            }
            else if ( reader.isStartElement( DataSetConverter.COLLECTION_NAME ) )
            {
                setProgress( 30, "importing_data_sets" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataSetConverter( batchHandler, importObjectService, dataSetService,
                    objectMappingGenerator.getPeriodTypeMapping() );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataSets" );
            }
            else if ( reader.isStartElement( DataSetMemberConverter.COLLECTION_NAME ) )
            {
                setProgress( 32, "importing_data_set_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetMemberBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new DataSetMemberConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ),
                    objectMappingGenerator.getDataSetMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported DataSet members" );
            }
            else if ( reader.isStartElement( OrganisationUnitConverter.COLLECTION_NAME ) )
            {
                setProgress( 34, "importing_organisation_units" );
                
                BatchHandler sourceBatchHandler = batchHandlerFactory.createBatchHandler( SourceBatchHandler.class );
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );
                
                sourceBatchHandler.init();
                batchHandler.init();
                
                XMLConverter converter = new OrganisationUnitConverter( batchHandler, sourceBatchHandler,
                    importObjectService, organisationUnitService );
                
                converter.read( reader, params );
                
                sourceBatchHandler.flush();
                batchHandler.flush();
                
                log.info( "Imported OrganisationUnits" );
            }
            else if ( reader.isStartElement( OrganisationUnitRelationshipConverter.COLLECTION_NAME ) )
            {
                setProgress( 56, "importing_organisation_unit_relationships" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new OrganisationUnitRelationshipConverter( batchHandler, 
                    importObjectService,
                    organisationUnitService,
                    objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported OrganisationUnit relationships" );
            }
            else if ( reader.isStartElement( OrganisationUnitGroupConverter.COLLECTION_NAME ) )
            {
                setProgress( 58, "importing_organisation_unit_groups" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new OrganisationUnitGroupConverter( batchHandler, importObjectService, organisationUnitGroupService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported OrganisationUnitGroups" );
            }
            else if ( reader.isStartElement( OrganisationUnitGroupMemberConverter.COLLECTION_NAME ) )
            {
                setProgress( 60, "importing_organisation_unit_group_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupMemberBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new OrganisationUnitGroupMemberConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ),
                    objectMappingGenerator.getOrganisationUnitGroupMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported OrganisationUnitGroup members" );
            }
            else if ( reader.isStartElement( GroupSetConverter.COLLECTION_NAME ) )
            {
                setProgress( 62, "importing_organisation_unit_group_sets" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new GroupSetConverter( batchHandler, importObjectService, organisationUnitGroupService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported OrganisationUnitGroupSets" );
            }
            else if ( reader.isStartElement( GroupSetMemberConverter.COLLECTION_NAME ) )
            {
                setProgress( 64, "importing_organisation_unit_group_set_members" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetMemberBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new GroupSetMemberConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getOrganisationUnitGroupMapping( params.isPreview() ),
                    objectMappingGenerator.getOrganisationUnitGroupSetMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported OrganisationUnitGroupSet members" );
            }
            else if ( reader.isStartElement( ValidationRuleConverter.COLLECTION_NAME ) )
            {
                setProgress( 66, "importing_validation_rules" );
                
                XMLConverter converter = new ValidationRuleConverter( importObjectService, 
                    validationRuleService, 
                    expressionService,
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                log.info( "Imported ValidationRules" );
            }
            else if ( reader.isStartElement( PeriodConverter.COLLECTION_NAME ) )
            {
                setProgress( 68, "importing_periods" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new PeriodConverter( batchHandler, importObjectService, periodService,
                    objectMappingGenerator.getPeriodTypeMapping() );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported Periods" );
            }
            else if ( reader.isStartElement( ReportTableConverter.COLLECTION_NAME ) )
            {
                setProgress( 70, "importing_report_tables" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new ReportTableConverter( batchHandler, reportTableStore, importObjectService );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported ReportTables" );
            }
            else if ( reader.isStartElement( ReportTableDataElementConverter.COLLECTION_NAME ) )
            {
                setProgress( 70, "importing_report_table_dataelements" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableDataElementBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new ReportTableDataElementConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getReportTableMapping( params.isPreview() ),
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported ReportTable DataElements" );
            }
            else if ( reader.isStartElement( ReportTableIndicatorConverter.COLLECTION_NAME ) )
            {
                setProgress( 70, "importing_report_table_indicators" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableIndicatorBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new ReportTableIndicatorConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getReportTableMapping( params.isPreview() ),
                    objectMappingGenerator.getIndicatorMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported ReportTable Indicators" );
            }
            else if ( reader.isStartElement( ReportTablePeriodConverter.COLLECTION_NAME ) )
            {
                setProgress( 70, "importing_report_table_periods" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTablePeriodBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new ReportTablePeriodConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getReportTableMapping( params.isPreview() ),
                    objectMappingGenerator.getPeriodMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported ReportTable Periods" );
            }
            else if ( reader.isStartElement( ReportTableOrganisationUnitConverter.COLLECTION_NAME ) )
            {
                setProgress( 70, "importing_report_table_organisation_units" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableOrganisationUnitBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new ReportTableOrganisationUnitConverter( batchHandler, importObjectService,
                    objectMappingGenerator.getReportTableMapping( params.isPreview() ),
                    objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported ReportTable OrganisationUnits" );
            }
            else if ( reader.isStartElement( CompleteDataSetRegistrationConverter.COLLECTION_NAME ) && params.isDataValues() )
            {
                setProgress( 72, "importing_complete_data_set_registrations" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( CompleteDataSetRegistrationBatchHandler.class );
                
                batchHandler.init();
                
                XMLConverter converter = new CompleteDataSetRegistrationConverter( batchHandler, importObjectService, params,
                    objectMappingGenerator.getDataSetMapping( params.isPreview() ),
                    objectMappingGenerator.getPeriodMapping( params.isPreview() ),
                    objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                log.info( "Imported CompleteDataSetRegistrations" );
            }
            else if ( reader.isStartElement( DataValueConverter.COLLECTION_NAME ) && params.isDataValues() )
            {
                setProgress( 75, "importing_data_values" );
                
                BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataValueBatchHandler.class );
                
                BatchHandler importDataValueBatchHandler = batchHandlerFactory.createBatchHandler( ImportDataValueBatchHandler.class );
                
                batchHandler.init();
                
                importDataValueBatchHandler.init();
                
                XMLConverter converter = new DataValueConverter( batchHandler, 
                    importDataValueBatchHandler,                    
                    dataValueService,
                    importObjectService,
                    params,
                    objectMappingGenerator.getDataElementMapping( params.isPreview() ),
                    objectMappingGenerator.getPeriodMapping( params.isPreview() ),
                    objectMappingGenerator.getOrganisationUnitMapping( params.isPreview() ),
                    objectMappingGenerator.getCategoryOptionComboMapping( params.isPreview() ) );
                
                converter.read( reader, params );
                
                batchHandler.flush();
                
                importDataValueBatchHandler.flush();
                
                log.info( "Imported DataValues" );
            }
        }
        
        setProgress( 100, "import_process_done" );
        
        NameMappingUtil.clearMapping();
        
        StreamUtils.closeInputStream( zipIn );
    }
}
