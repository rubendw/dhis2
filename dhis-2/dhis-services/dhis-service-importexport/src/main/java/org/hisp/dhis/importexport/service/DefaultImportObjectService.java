package org.hisp.dhis.importexport.service;

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

import static org.hisp.dhis.expression.Expression.SEPARATOR;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.importexport.GroupMemberAssociation;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportDataValue;
import org.hisp.dhis.importexport.ImportDataValueService;
import org.hisp.dhis.importexport.ImportObject;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.importexport.ImportObjectStore;
import org.hisp.dhis.importexport.mapping.GroupMemberAssociationVerifier;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.importexport.mapping.ObjectMappingGenerator;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableStore;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultImportObjectService.java 5833 2008-10-08 14:00:02Z larshelg $
 */
public class DefaultImportObjectService<T>
    implements ImportObjectService
{
    private Log log = LogFactory.getLog( DefaultImportObjectService.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ImportObjectStore importObjectStore;
    
    public void setImportObjectStore( ImportObjectStore importObjectStore )
    {
        this.importObjectStore = importObjectStore;
    }
    
    private ImportDataValueService importDataValueService;

    public void setImportDataValueService( ImportDataValueService importDataValueService )
    {
        this.importDataValueService = importDataValueService;
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
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
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
    
    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
    }
    
    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
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
    // ImportObjectService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // ImportObject operations
    // -------------------------------------------------------------------------

    public int addImportObject( ImportObjectStatus status, Class<?> clazz, GroupMemberType groupMemberType, Object object )
    {
        // TODO Derive class from object
        
        ImportObject importObject = new ImportObject( status, clazz.getName(), groupMemberType, object );
        
        return importObjectStore.addImportObject( importObject );
    }

    public int addImportObject( ImportObjectStatus status, Class<?> clazz, Object object, Object compareObject )
    {
        ImportObject importObject = new ImportObject( status, clazz.getName(), object, compareObject );
        
        return importObjectStore.addImportObject( importObject );
    }

    public int addImportObject( ImportObjectStatus status, Class<?> clazz, GroupMemberType groupMemberType, Object object, Object compareObject )
    {
        ImportObject importObject = new ImportObject( status, clazz.getName(), groupMemberType, object, compareObject );
        
        return importObjectStore.addImportObject( importObject );
    }
    
    public ImportObject getImportObject( int id )
    {
        return importObjectStore.getImportObject( id );
    }

    public Collection<ImportObject> getImportObjects( Class<?> clazz )
    {
        return importObjectStore.getImportObjects( clazz );
    }
    
    public Collection<ImportObject> getImportObjects( ImportObjectStatus status, Class<?> clazz )
    {
        return importObjectStore.getImportObjects( status, clazz );
    }
    
    public Collection<ImportObject> getImportObjects( GroupMemberType groupMemberType )
    {
        return importObjectStore.getImportObjects( groupMemberType );
    }
    
    public void deleteImportObject( int importObjectId )
    {
        ImportObject importObject = importObjectStore.getImportObject( importObjectId );
        
        if ( importObject != null )
        {
            importObjectStore.deleteImportObject( importObject );
        }
    }

    public void deleteImportObjects( Class<?> clazz )
    {
        importObjectStore.deleteImportObjects( clazz );
    }
    
    public void deleteImportObjects()
    {
        importObjectStore.deleteImportObjects();        
        importDataValueService.deleteImportDataValues();
    }
    
    //TODO Refactor: this code is not modular and is error-prone in terms of cascading deletion of associated objects
    
    public void cascadeDeleteImportObject( int importObjectId )
    {
        ImportObject importObject = importObjectStore.getImportObject( importObjectId );
        
        if ( importObject != null )
        {
            if ( importObject.getClassName().equals( DataElement.class.getName() ) )
            {
                DataElement element = (DataElement) importObject.getObject();            
                
                deleteMemberAssociations( GroupMemberType.DATAELEMENTGROUP, element.getId() );                
                deleteMemberAssociations( GroupMemberType.DATASET, element.getId() );                
                deleteMemberAssociations( GroupMemberType.DATADICTIONARY_DATAELEMENT, element.getId() );                
                deleteMemberAssociations( GroupMemberType.REPORTTABLE_DATAELEMENT, element.getId() );
                
                deleteIndicatorsContainingDataElement( element.getId() );
                
                importDataValueService.deleteImportDataValuesByDataElement( element.getId() );
            }
            else if ( importObject.getClassName().equals( DataElementGroup.class.getName() ) )
            {
                DataElementGroup group = (DataElementGroup) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.DATAELEMENTGROUP, group.getId() );
            }
            else if ( importObject.getClassName().equals( IndicatorType.class.getName() ) )
            {
                IndicatorType type = (IndicatorType) importObject.getObject();
                
                deleteIndicatorsWithIndicatorType( type.getId() );
            }
            else if ( importObject.getClassName().equals( Indicator.class.getName() ) )
            {
                Indicator indicator = (Indicator) importObject.getObject();
                
                deleteMemberAssociations( GroupMemberType.INDICATORGROUP, indicator.getId() );                
                deleteMemberAssociations( GroupMemberType.DATADICTIONARY_INDICATOR, indicator.getId() );                
                deleteMemberAssociations( GroupMemberType.REPORTTABLE_INDICATOR, indicator.getId() );
            }
            else if ( importObject.getClassName().equals( IndicatorGroup.class.getName() ) )
            {
                IndicatorGroup group = (IndicatorGroup) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.INDICATORGROUP, group.getId() );
            }
            else if ( importObject.getClassName().equals( DataDictionary.class.getName() ) )
            {
                DataDictionary dictionary = (DataDictionary) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.DATADICTIONARY_DATAELEMENT, dictionary.getId() );                
                deleteGroupAssociations( GroupMemberType.DATADICTIONARY_INDICATOR, dictionary.getId() );
            }
            else if ( importObject.getClassName().equals( DataSet.class.getName() ) )
            {
                DataSet dataSet = (DataSet) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.DATASET, dataSet.getId() );
                
                deleteCompleteDataSetRegistrationsByDataSet( dataSet.getId() );
            }
            else if ( importObject.getClassName().equals( OrganisationUnit.class.getName() ) )
            {
                OrganisationUnit unit = (OrganisationUnit) importObject.getObject();
                
                deleteMemberAssociations( GroupMemberType.ORGANISATIONUNITGROUP, unit.getId() );                
                deleteGroupAssociations( GroupMemberType.ORGANISATIONUNITRELATIONSHIP, unit.getId() );                
                deleteMemberAssociations( GroupMemberType.ORGANISATIONUNITRELATIONSHIP, unit.getId() );                
                deleteMemberAssociations( GroupMemberType.REPORTTABLE_ORGANISATIONUNIT, unit.getId() );
                
                deleteCompleteDataSetRegistrationsBySource( unit.getId() );
                
                importDataValueService.deleteImportDataValuesBySource( unit.getId() );
            }
            else if ( importObject.getClassName().equals( OrganisationUnitGroup.class.getName() ) )
            {
                OrganisationUnitGroup group = (OrganisationUnitGroup) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.ORGANISATIONUNITGROUP, group.getId() );                
                deleteMemberAssociations( GroupMemberType.ORGANISATIONUNITGROUPSET, group.getId() );
            }
            else if ( importObject.getClassName().equals( OrganisationUnitGroupSet.class.getName() ) )
            {
                OrganisationUnitGroupSet groupSet = (OrganisationUnitGroupSet) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.ORGANISATIONUNITGROUPSET, groupSet.getId() );
            }
            else if ( importObject.getClassName().equals( ReportTable.class.getName() ) )
            {
                ReportTable reportTable = (ReportTable) importObject.getObject();
                
                deleteGroupAssociations( GroupMemberType.REPORTTABLE_DATAELEMENT, reportTable.getId() );
                deleteGroupAssociations( GroupMemberType.REPORTTABLE_INDICATOR, reportTable.getId() );
                deleteGroupAssociations( GroupMemberType.REPORTTABLE_PERIOD, reportTable.getId() );
                deleteGroupAssociations( GroupMemberType.REPORTTABLE_ORGANISATIONUNIT, reportTable.getId() );
            }
        }
        
        deleteImportObject( importObjectId );
    }
    
    public void cascadeDeleteImportObjects( Class<?> clazz )
    {
        importObjectStore.deleteImportObjects( clazz );
        
        if ( clazz.equals( DataElement.class ) )
        {
            importObjectStore.deleteImportObjects( DataElementCategoryOptionCombo.class );            
            importObjectStore.deleteImportObjects( DataElementCategoryCombo.class );            
            importObjectStore.deleteImportObjects( DataElementCategory.class );            
            importObjectStore.deleteImportObjects( DataElementCategoryOption.class );
            importObjectStore.deleteImportObjects( GroupMemberType.CATEGORY_CATEGORYOPTION );
            importObjectStore.deleteImportObjects( GroupMemberType.CATEGORYCOMBO_CATEGORY );
            
            importObjectStore.deleteImportObjects( GroupMemberType.DATAELEMENTGROUP );
            
            importObjectStore.deleteImportObjects( DataSet.class );
            importObjectStore.deleteImportObjects( GroupMemberType.DATASET );
            importObjectStore.deleteImportObjects( CompleteDataSetRegistration.class );
            
            importObjectStore.deleteImportObjects( Indicator.class );
            importObjectStore.deleteImportObjects( GroupMemberType.INDICATORGROUP );
            importObjectStore.deleteImportObjects( GroupMemberType.DATADICTIONARY_INDICATOR ); 
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_INDICATOR );
            
            importObjectStore.deleteImportObjects( GroupMemberType.DATADICTIONARY_DATAELEMENT );
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_DATAELEMENT );
            
            importDataValueService.deleteImportDataValues();
        }
        else if ( clazz.equals( DataElementGroup.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.DATAELEMENTGROUP );
        }
        else if ( clazz.equals( IndicatorType.class ) )
        {
            importObjectStore.deleteImportObjects( Indicator.class );
            importObjectStore.deleteImportObjects( GroupMemberType.INDICATORGROUP );
            importObjectStore.deleteImportObjects( GroupMemberType.DATADICTIONARY_INDICATOR );
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_INDICATOR );
        }
        else if ( clazz.equals( Indicator.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.INDICATORGROUP );
            importObjectStore.deleteImportObjects( GroupMemberType.DATADICTIONARY_INDICATOR );            
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_INDICATOR );            
        }
        else if ( clazz.equals( IndicatorGroup.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.INDICATORGROUP );
        }
        else if ( clazz.equals( DataDictionary.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.DATADICTIONARY_DATAELEMENT );            
            importObjectStore.deleteImportObjects( GroupMemberType.DATADICTIONARY_INDICATOR );            
        }
        else if ( clazz.equals( DataSet.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.DATASET );
            importObjectStore.deleteImportObjects( CompleteDataSetRegistration.class );
        }
        else if ( clazz.equals( OrganisationUnit.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.ORGANISATIONUNITGROUP );            
            importObjectStore.deleteImportObjects( GroupMemberType.ORGANISATIONUNITRELATIONSHIP );            
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_ORGANISATIONUNIT );
            
            importObjectStore.deleteImportObjects( CompleteDataSetRegistration.class );
            
            importDataValueService.deleteImportDataValues();
        }
        else if ( clazz.equals( OrganisationUnitGroup.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.ORGANISATIONUNITGROUP );   
        }
        else if ( clazz.equals( OrganisationUnitGroupSet.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.ORGANISATIONUNITGROUPSET );
        }
        else if ( clazz.equals( ReportTable.class ) )
        {
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_DATAELEMENT );
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_INDICATOR );
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_PERIOD );
            importObjectStore.deleteImportObjects( GroupMemberType.REPORTTABLE_ORGANISATIONUNIT );
        }
    }
    
    // -------------------------------------------------------------------------
    // Object
    // -------------------------------------------------------------------------
    
    public void matchObject( int importObjectId, int existingObjectId )
    {
        ImportObject importObject = importObjectStore.getImportObject( importObjectId );
        
        Object object = importObject.getObject();

        // ---------------------------------------------------------------------
        // Updates the name of the import object to the name of the existing
        // object.
        // ---------------------------------------------------------------------
        
        if ( object.getClass().equals( DataElement.class ) )
        {
            DataElement element = (DataElement) object;
            
            element.setName( dataElementService.getDataElement( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( DataElementGroup.class ) )
        {
            DataElementGroup group = (DataElementGroup) object;
            
            group.setName( dataElementService.getDataElementGroup( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( IndicatorType.class ) )
        {
            IndicatorType type = (IndicatorType) object;
            
            type.setName( indicatorService.getIndicatorType( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( Indicator.class ) )
        {
            Indicator indicator = (Indicator) object;
            
            indicator.setName( indicatorService.getIndicator( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( IndicatorGroup.class ) )
        {
            IndicatorGroup group = (IndicatorGroup) object;
            
            group.setName( indicatorService.getIndicatorGroup( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( DataDictionary.class ) )
        {
            DataDictionary dictionary = (DataDictionary) object;
            
            dictionary.setName( dataDictionaryService.getDataDictionary( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( DataSet.class ) )
        {
            DataSet dataSet = (DataSet) object;
            
            dataSet.setName( dataSetService.getDataSet( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( OrganisationUnit.class ) )
        {
            OrganisationUnit unit = (OrganisationUnit) object;
            
            unit.setName( organisationUnitService.getOrganisationUnit( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( OrganisationUnitGroup.class ) )
        {
            OrganisationUnitGroup group = (OrganisationUnitGroup) object;
            
            group.setName( organisationUnitGroupService.getOrganisationUnitGroup( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( OrganisationUnitGroupSet.class ) )
        {
            OrganisationUnitGroupSet groupSet = (OrganisationUnitGroupSet) object;
            
            groupSet.setName( organisationUnitGroupService.getOrganisationUnitGroupSet( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( ValidationRule.class ) )
        {
            ValidationRule validationRule = (ValidationRule) object;
            
            validationRule.setName( validationRuleService.getValidationRule( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( ReportTable.class ) )
        {
            ReportTable reportTable = (ReportTable) object;
            
            reportTable.setName( reportTableStore.getReportTable( existingObjectId ).getName() );
        }
        else if ( object.getClass().equals( DataValue.class ) )
        {
            DataValue dataValue = (DataValue) object;
            
            object = updateDataValue( dataValue, dataValueService.getDataValue( dataValue.getSource(), dataValue.getDataElement(), dataValue.getPeriod() ) );
        }

        // ---------------------------------------------------------------------
        // Sets the status of the import object to match, these objects will
        // later be ignored on import all but is needed for matching of
        // associations.
        // ---------------------------------------------------------------------
        
        importObject.setStatus( ImportObjectStatus.MATCH );
        
        importObjectStore.updateImportObject( importObject );
    }
    
    // -------------------------------------------------------------------------
    // Import
    // -------------------------------------------------------------------------
    
    public void importAll()
    {
        importCategoryOptions();
        importCategories();
        importCategoryCombos();
        importCategoryOptionCombos();
        importCategoryCategoryOptionAssociations();
        importCategoryComboCategoryAssociations();
        importDataElements();
        importDataElementGroups();
        importDataElementGroupMembers();
        importIndicatorTypes();
        importIndicators();
        importIndicatorGroups();
        importIndicatorGroupMembers();
        importDataDictionaries();
        importDataDictionaryDataElements();
        importDataDictionaryIndicators();
        importDataSets();
        importDataSetMembers();
        importOrganisationUnits();
        importOrganisationUnitRelationships();
        importOrganisationUnitGroups();
        importOrganisationUnitGroupMembers();
        importOrganisationUnitGroupSets();
        importOrganisationUnitGroupSetMembers();
        importValidationRules();
        importPeriods();
        importReportTables();
        importReportTableDataElements();
        importReportTableIndicators();
        importReportTablePeriods();
        importReportTableOrganisationUnits();
        importCompleteDataSetRegistrations();
        importDataValues();
        
        NameMappingUtil.clearMapping();
    }

    // -------------------------------------------------------------------------
    // Import - object supportive methods
    // -------------------------------------------------------------------------

    private void importCategoryOptions()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryOptionBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElementCategoryOption.class );
        
        for ( ImportObject importObject : importObjects )
        {
            DataElementCategoryOption object = (DataElementCategoryOption) importObject.getObject();
            
            NameMappingUtil.addCategoryOptionMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataElementCategoryOption compareObject = (DataElementCategoryOption) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( DataElementCategoryOption.class );
        
        log.info( "Imported DataElementCategoryOptions" );
    }
    
    private void importCategories()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElementCategory.class );

        for ( ImportObject importObject : importObjects )
        {
            DataElementCategory object = (DataElementCategory) importObject.getObject();
            
            NameMappingUtil.addCategoryMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataElementCategory compareObject = (DataElementCategory) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }            

            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }

        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( DataElementCategory.class );
        
        log.info( "Imported DataElementCategories" );
    }
    
    private void importCategoryCombos()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryComboBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElementCategoryCombo.class );

        for ( ImportObject importObject : importObjects )
        {
            DataElementCategoryCombo object = (DataElementCategoryCombo) importObject.getObject();
            
            NameMappingUtil.addCategoryComboMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataElementCategoryCombo compareObject = (DataElementCategoryCombo) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();

        importObjectStore.deleteImportObjects( DataElementCategoryCombo.class );
        
        log.info( "Imported DataElementCategoryCombos" );
    }
    
    private void importCategoryOptionCombos()
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElementCategoryOptionCombo.class );

        Map<Object, Integer> categoryComboMapping = objectMappingGenerator.getCategoryComboMapping( false );
        Map<Object, Integer> categoryOptionMapping = objectMappingGenerator.getCategoryOptionMapping( false );
        
        for ( ImportObject importObject : importObjects )
        {
            DataElementCategoryOptionCombo object = (DataElementCategoryOptionCombo) importObject.getObject();
            
            int categoryOptionComboId = object.getId();
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataElementCategoryOptionCombo compareObject = (DataElementCategoryOptionCombo) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            int categoryComboId = categoryComboMapping.get( object.getCategoryCombo().getId() );
            
            object.setCategoryCombo( categoryComboService.getDataElementCategoryCombo( categoryComboId ) );
            
            Set<DataElementCategoryOption> categoryOptions = new HashSet<DataElementCategoryOption>();
            
            for ( DataElementCategoryOption categoryOption : object.getCategoryOptions() )
            {
                int categoryOptionId = categoryOptionMapping.get( categoryOption.getId() );
                
                categoryOptions.add( categoryOptionService.getDataElementCategoryOption( categoryOptionId ) );
            }
            
            object.setCategoryOptions( categoryOptions );

            NameMappingUtil.addCategoryOptionComboMapping( categoryOptionComboId, object );
            
            if ( importObject.getStatus() == ImportObjectStatus.NEW )
            {
                categoryOptionComboService.addDataElementCategoryOptionCombo( object );
            }
            else if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                categoryOptionComboService.updateDataElementCategoryOptionCombo( object );
            }
        }

        importObjectStore.deleteImportObjects( DataElementCategoryOptionCombo.class );
        
        log.info( "Imported DataElementCategoryOptionCombos" );
    }

    private void importCategoryCategoryOptionAssociations()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( CategoryCategoryOptionAssociationBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.CATEGORY_CATEGORYOPTION, 
            objectMappingGenerator.getCategoryMapping( false ), 
            objectMappingGenerator.getCategoryOptionMapping( false ) );
        
        log.info( "Imported CategoryCategoryOption associations" );
    }
    
    private void importCategoryComboCategoryAssociations()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( CategoryComboCategoryAssociationBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.CATEGORYCOMBO_CATEGORY, 
            objectMappingGenerator.getCategoryComboMapping( false ),
            objectMappingGenerator.getCategoryMapping( false ) );
        
        log.info( "Imported CategoryComboCategory associations" );
    }
    
    private void importDataElements()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementBatchHandler.class );
        BatchHandler extendedDataElementBatchHandler = batchHandlerFactory.createBatchHandler( ExtendedDataElementBatchHandler.class );
        
        Map<Object, Integer> categoryComboMapping = objectMappingGenerator.getCategoryComboMapping( false );
        
        batchHandler.init();
        extendedDataElementBatchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElement.class );
        
        for ( ImportObject importObject : importObjects )
        {
            DataElement object = (DataElement) importObject.getObject();

            NameMappingUtil.addDataElementMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.NEW )
            {
                ExtendedDataElement extendedDataElement = object.getExtended();
                
                if ( extendedDataElement != null )
                {
                    int id = extendedDataElementBatchHandler.insertObject( extendedDataElement, true );
                    
                    extendedDataElement.setId( id );
                    object.setExtended( extendedDataElement );
                }
            }
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                // TODO update extended ?
                
                DataElement compareObject = (DataElement) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            object.getCategoryCombo().setId( categoryComboMapping.get( object.getCategoryCombo().getId() ) );
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        extendedDataElementBatchHandler.flush();
        
        importObjectStore.deleteImportObjects( DataElement.class );
        
        log.info( "Imported DataElements" );
    }
    
    private void importDataElementGroups()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupBatchHandler.class );

        batchHandler.init();
            
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElementGroup.class );

        for ( ImportObject importObject : importObjects )
        {
            DataElementGroup object = (DataElementGroup) importObject.getObject();

            NameMappingUtil.addDataElementGroupMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataElementGroup compareObject = (DataElementGroup) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( DataElementGroup.class );
        
        log.info( "Imported DataElementGroups" );
    }
    
    private void importDataElementGroupMembers()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupMemberBatchHandler.class );

        importGroupMemberAssociation( batchHandler, GroupMemberType.DATAELEMENTGROUP,
            objectMappingGenerator.getDataElementGroupMapping( false ),
            objectMappingGenerator.getDataElementMapping( false ) );
        
        log.info( "Imported DataElementGroup members" );
    }

    private void importIndicatorTypes()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorTypeBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( IndicatorType.class );

        for ( ImportObject importObject : importObjects )
        {   
            IndicatorType object = (IndicatorType) importObject.getObject();

            NameMappingUtil.addIndicatorTypeMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                IndicatorType compareObject = (IndicatorType) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( IndicatorType.class );
        
        log.info( "Imported IndicatorTypes" );
    }    
    
    private void importIndicators()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorBatchHandler.class );
        BatchHandler extendedDataElementBatchHandler = batchHandlerFactory.createBatchHandler( ExtendedDataElementBatchHandler.class );
        
        Map<Object, Integer> indicatorTypeMapping = objectMappingGenerator.getIndicatorTypeMapping( false );
        
        batchHandler.init();
        extendedDataElementBatchHandler.init();
                
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( Indicator.class );

        for ( ImportObject importObject : importObjects )
        {
            Indicator object = (Indicator) importObject.getObject();

            NameMappingUtil.addIndicatorMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.NEW )
            {
                ExtendedDataElement extendedIndicator = object.getExtended();
                
                if ( extendedIndicator != null )
                {
                    int id = extendedDataElementBatchHandler.insertObject( extendedIndicator, true );
                    
                    extendedIndicator.setId( id );
                    object.setExtended( extendedIndicator );
                }
            }
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                // TODO update extended ?
                
                Indicator compareObject = (Indicator) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            object.getIndicatorType().setId( indicatorTypeMapping.get( object.getIndicatorType().getId() ) );
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        extendedDataElementBatchHandler.flush();
        
        importObjectStore.deleteImportObjects( Indicator.class );
        
        log.info( "Imported Indicators" );
    }    
    
    private void importIndicatorGroups()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( IndicatorGroup.class );

        for ( ImportObject importObject : importObjects )
        {   
            IndicatorGroup object = (IndicatorGroup) importObject.getObject();

            NameMappingUtil.addIndicatorGroupMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                IndicatorGroup compareObject = (IndicatorGroup) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );          
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( IndicatorGroup.class );
        
        log.info( "Imported IndicatorGroups" );
    }

    private void importIndicatorGroupMembers()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupMemberBatchHandler.class );

        importGroupMemberAssociation( batchHandler, GroupMemberType.INDICATORGROUP,
            objectMappingGenerator.getIndicatorGroupMapping( false ),
            objectMappingGenerator.getIndicatorMapping( false ) );
        
        log.info( "Imported IndicatorGroup members" );
    }
    
    private void importDataDictionaries()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataDictionary.class );
        
        for ( ImportObject importObject : importObjects )
        {
            DataDictionary object = (DataDictionary) importObject.getObject();
            
            NameMappingUtil.addDataDictionaryMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataDictionary compareObject = (DataDictionary) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( DataDictionary.class );
        
        log.info( "Imported DataDictionaries" );
    }
    
    private void importDataSets()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetBatchHandler.class );

        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataSet.class );
        
        for ( ImportObject importObject : importObjects )
        {
            DataSet object = (DataSet) importObject.getObject();

            NameMappingUtil.addDataSetMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                DataSet compareObject = (DataSet) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );         
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( DataSet.class );
        
        log.info( "Imported DataSets" );
    }
    
    private void importDataDictionaryDataElements()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryDataElementBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.DATADICTIONARY_DATAELEMENT, 
            objectMappingGenerator.getDataDictionaryMapping( false ),
            objectMappingGenerator.getDataElementMapping( false ) );
        
        log.info( "Imported DataDictionary DataElements" );
    }

    private void importDataDictionaryIndicators()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryIndicatorBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.DATADICTIONARY_INDICATOR, 
            objectMappingGenerator.getDataDictionaryMapping( false ),
            objectMappingGenerator.getIndicatorMapping( false ) );
        
        log.info( "Imported DataDictionary Indicators" );
    }

    private void importDataSetMembers()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetMemberBatchHandler.class );

        importGroupMemberAssociation( batchHandler, GroupMemberType.DATASET,
            objectMappingGenerator.getDataSetMapping( false ), 
            objectMappingGenerator.getDataElementMapping( false ) );
        
        log.info( "Imported DataSet members" );
    }
    
    private void importOrganisationUnits()
    {
        BatchHandler sourceBatchHandler = batchHandlerFactory.createBatchHandler( SourceBatchHandler.class );
        BatchHandler organisationUnitBatchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );

        sourceBatchHandler.init();
        organisationUnitBatchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( OrganisationUnit.class );
        
        for ( ImportObject importObject : importObjects )
        {
            OrganisationUnit object = (OrganisationUnit) importObject.getObject();

            NameMappingUtil.addOrganisationUnitMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.NEW )
            {
                int id = sourceBatchHandler.insertObject( object, true );
                
                object.setId( id );
            }
            else if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                OrganisationUnit compareObject = (OrganisationUnit) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( organisationUnitBatchHandler, importObject );
        }
        
        sourceBatchHandler.flush();
        organisationUnitBatchHandler.flush();
        
        importObjectStore.deleteImportObjects( OrganisationUnit.class );
        
        log.info( "Imported OrganisationUnits" );
    }
    
    private void importOrganisationUnitRelationships()
    {
        Map<Object, Integer> organisationUnitMapping = objectMappingGenerator.getOrganisationUnitMapping( false );
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );

        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( GroupMemberType.ORGANISATIONUNITRELATIONSHIP );
        
        for ( ImportObject importObject : importObjects )
        {
            GroupMemberAssociation object = (GroupMemberAssociation) importObject.getObject();
            
            OrganisationUnit child = organisationUnitService.getOrganisationUnit( organisationUnitMapping.get( object.getMemberId() ) );
            
            OrganisationUnit parent = organisationUnitService.getOrganisationUnit( organisationUnitMapping.get( object.getGroupId() ) );
            
            child.setParent( parent );
            
            batchHandler.updateObject( child );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( GroupMemberType.ORGANISATIONUNITRELATIONSHIP );
        
        log.info( "Imported OrganisationUnit relationships" );
    }

    private void importOrganisationUnitGroups()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupBatchHandler.class );

        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( OrganisationUnitGroup.class );
        
        for ( ImportObject importObject : importObjects )
        {
            OrganisationUnitGroup object = (OrganisationUnitGroup) importObject.getObject();

            NameMappingUtil.addOrganisationUnitGroupMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                OrganisationUnitGroup compareObject = (OrganisationUnitGroup) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( OrganisationUnitGroup.class );
        
        log.info( "Imported OrganisationUnitGroups" );
    }
    
    private void importOrganisationUnitGroupMembers()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupMemberBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.ORGANISATIONUNITGROUP,
            objectMappingGenerator.getOrganisationUnitGroupMapping( false ),
            objectMappingGenerator.getOrganisationUnitMapping( false ) );
        
        log.info( "Imported OrganissationUnitGroup members" );
    }

    private void importOrganisationUnitGroupSets()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetBatchHandler.class );

        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( OrganisationUnitGroupSet.class );
        
        for ( ImportObject importObject : importObjects )
        {
            OrganisationUnitGroupSet object = (OrganisationUnitGroupSet) importObject.getObject();

            NameMappingUtil.addGroupSetMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                OrganisationUnitGroupSet compareObject = (OrganisationUnitGroupSet) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );            
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( OrganisationUnitGroupSet.class );
        
        log.info( "Imported OrganisationUnitGroupSets" );
    }

    private void importOrganisationUnitGroupSetMembers()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetMemberBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.ORGANISATIONUNITGROUPSET,
            objectMappingGenerator.getOrganisationUnitGroupSetMapping( false ),
            objectMappingGenerator.getOrganisationUnitGroupMapping( false ) );
        
        log.info( "Imported OrganisationUnitGroupSet members" );
    }

    //TODO BatchHandler
    
    private void importValidationRules()
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( ValidationRule.class );
        
        for ( ImportObject importObject : importObjects )
        {
            ValidationRule object = (ValidationRule) importObject.getObject();
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                ValidationRule compare = (ValidationRule) importObject.getCompareObject();
                
                validationRuleService.updateValidationRule( compare ); // Reload because of Expression
                
                object = updateValidationRule( compare, object );
                
                expressionService.updateExpression( object.getLeftSide() );
                expressionService.updateExpression( object.getRightSide() );
                                
                validationRuleService.updateValidationRule( object );
            }
            else if ( importObject.getStatus() == ImportObjectStatus.NEW )
            {
                expressionService.addExpression( object.getLeftSide() );
                expressionService.addExpression( object.getRightSide() );
                                
                validationRuleService.addValidationRule( object );
            }
        }
        
        importObjectStore.deleteImportObjects( ValidationRule.class );
        
        log.info( "Imported ValidationRules" );
    }
    
    private void importPeriods()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( Period.class );
        
        for ( ImportObject importObject : importObjects )
        {
            Period period = (Period) importObject.getObject();
            
            NameMappingUtil.addPeriodMapping( period.getId(), period );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( Period.class );
        
        log.info( "Imported Periods" );
    }

    private void importReportTables()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableBatchHandler.class );
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( ReportTable.class );
        
        for ( ImportObject importObject : importObjects )
        {
            ReportTable object = (ReportTable) importObject.getObject();
            
            NameMappingUtil.addReportTableMapping( object.getId(), object.getName() );
            
            if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
            {
                ReportTable compareObject = (ReportTable) importObject.getCompareObject();
                
                object.setId( compareObject.getId() );
            }
            
            importObject.setObject( object );
            
            addOrUpdateObject( batchHandler, importObject );
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( ReportTable.class );
        
        log.info( "Imported ReportTables" );
    }

    private void importReportTableDataElements()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableDataElementBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.REPORTTABLE_DATAELEMENT,
            objectMappingGenerator.getReportTableMapping( false ),
            objectMappingGenerator.getDataElementMapping( false ) );
        
        log.info( "Imported ReportTable DataElements" );
    }

    private void importReportTableIndicators()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableIndicatorBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.REPORTTABLE_INDICATOR,
            objectMappingGenerator.getReportTableMapping( false ),
            objectMappingGenerator.getIndicatorMapping( false ) );
        
        log.info( "Imported ReportTable Indicators" );
    }

    private void importReportTablePeriods()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTablePeriodBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.REPORTTABLE_PERIOD,
            objectMappingGenerator.getReportTableMapping( false ),
            objectMappingGenerator.getPeriodMapping( false ) );
        
        log.info( "Imported ReportTable Periods" );
    }

    private void importReportTableOrganisationUnits()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableOrganisationUnitBatchHandler.class );
        
        importGroupMemberAssociation( batchHandler, GroupMemberType.REPORTTABLE_ORGANISATIONUNIT,
            objectMappingGenerator.getReportTableMapping( false ),
            objectMappingGenerator.getOrganisationUnitMapping( false ) );
        
        log.info( "Imported ReportTable OrganisationUnits" );
    }
    
    private void importCompleteDataSetRegistrations()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( CompleteDataSetRegistrationBatchHandler.class );

        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( CompleteDataSetRegistration.class );

        Map<Object, Integer> dataSetMapping = objectMappingGenerator.getDataSetMapping( false );
        Map<Object, Integer> periodMapping = objectMappingGenerator.getPeriodMapping( false );
        Map<Object, Integer> sourceMapping = objectMappingGenerator.getOrganisationUnitMapping( false );
        
        for ( ImportObject importObject : importObjects )
        {
            CompleteDataSetRegistration registration = (CompleteDataSetRegistration) importObject.getObject();
            
            registration.getDataSet().setId( dataSetMapping.get( registration.getDataSet().getId() ) );
            registration.getPeriod().setId( periodMapping.get( registration.getPeriod().getId() ) );
            registration.getSource().setId( sourceMapping.get( registration.getSource().getId() ) );
            
            // -----------------------------------------------------------------
            // Must check for existing registrations since this cannot be done
            // during preview
            // -----------------------------------------------------------------
            
            if ( !batchHandler.objectExists( registration ) )
            {
                batchHandler.addObject( registration );
            }
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( CompleteDataSetRegistration.class );
        
        log.info( "Imported CompleteDataSetRegistrations" );
    }
    
    private void importDataValues()
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataValueBatchHandler.class );
        
        batchHandler.init();
        
        Map<Object, Integer> dataElementMapping = objectMappingGenerator.getDataElementMapping( false );
        Map<Object, Integer> periodMapping = objectMappingGenerator.getPeriodMapping( false );
        Map<Object, Integer> sourceMapping = objectMappingGenerator.getOrganisationUnitMapping( false );
        Map<Object, Integer> categoryOptionComboMapping = objectMappingGenerator.getCategoryOptionComboMapping( false );
        
        Collection<ImportDataValue> importValues = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        for ( ImportDataValue importValue : importValues )
        {
            DataValue value = importValue.getDataValue();
            
            value.getDataElement().setId( dataElementMapping.get( value.getDataElement().getId() ) );
            value.getPeriod().setId( periodMapping.get( value.getPeriod().getId() ) );
            value.getSource().setId( sourceMapping.get( value.getSource().getId() ) );
            value.getOptionCombo().setId( categoryOptionComboMapping.get( value.getOptionCombo().getId() ) );
            
            // -----------------------------------------------------------------
            // Must check for existing datavalues since this cannot be done
            // during preview
            // -----------------------------------------------------------------
            
            if ( !batchHandler.objectExists( value ) )
            {
                batchHandler.addObject( value );
            }
        }
        
        batchHandler.flush();
        
        importDataValueService.deleteImportDataValues();
        
        log.info( "Imported DataValues" );
    }
    
    // -------------------------------------------------------------------------
    // Import - general supportive methods
    // -------------------------------------------------------------------------

    private boolean containsIdentifier( String formula, int identifier )
    {
        if ( formula != null )
        {        
            Pattern pattern = Pattern.compile( "(\\[\\d+\\" + SEPARATOR + "\\d+\\])" );
            Matcher matcher = pattern.matcher( formula );
            
            while ( matcher.find() )
            {
                String match = matcher.group();
                
                match = match.replaceAll( "[\\[\\]]", "" );
                
                String matchId = match.substring( 0, match.indexOf( SEPARATOR ) );                
                
                if ( matchId.equals( String.valueOf( identifier ) ) )
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void addOrUpdateObject( BatchHandler batchHandler, ImportObject importObject )
    {
        if ( importObject.getStatus() == ImportObjectStatus.NEW )
        {
            batchHandler.addObject( importObject.getObject() );
        }
        else if ( importObject.getStatus() == ImportObjectStatus.UPDATE )
        {
            batchHandler.updateObject( importObject.getObject() );
        }

        // ---------------------------------------------------------------------
        // Ignoring ImportObjects of type MATCH
        // ---------------------------------------------------------------------
    }
    
    private void importGroupMemberAssociation( BatchHandler batchHandler, GroupMemberType type,
        Map<Object, Integer> groupMapping, Map<Object, Integer> memberMapping )
    {
        GroupMemberAssociationVerifier.clear();
        
        batchHandler.init();
        
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( type );
        
        for ( ImportObject importObject : importObjects )
        {
            GroupMemberAssociation object = (GroupMemberAssociation) importObject.getObject();

            object.setGroupId( groupMapping.get( object.getGroupId() ) );
            object.setMemberId( memberMapping.get( object.getMemberId() ) );
            
            if ( GroupMemberAssociationVerifier.isUnique( object, type ) && !batchHandler.objectExists( object ) )
            {
                batchHandler.addObject( object );
            }            
        }
        
        batchHandler.flush();
        
        importObjectStore.deleteImportObjects( type );
    }

    // -------------------------------------------------------------------------
    // Update - supportive methods
    // -------------------------------------------------------------------------
        
    private ValidationRule updateValidationRule( ValidationRule original, ValidationRule update )
    {
        original.setName( update.getName() );
        original.setDescription( update.getDescription() );
        original.setType( update.getType() );
        original.setOperator( update.getOperator() );
        original.getLeftSide().setExpression( update.getLeftSide().getExpression() );
        original.getLeftSide().setDescription( update.getLeftSide().getDescription()  );
        original.getLeftSide().setDataElementsInExpression( update.getLeftSide().getDataElementsInExpression() );
        original.getRightSide().setExpression( update.getRightSide().getExpression() );
        original.getRightSide().setDescription( update.getRightSide().getDescription() );
        original.getRightSide().setDataElementsInExpression( update.getRightSide().getDataElementsInExpression() );
        
        return original;
    }
    
    private DataValue updateDataValue( DataValue original, DataValue update )
    {
        original.setDataElement( update.getDataElement() );
        original.setPeriod( update.getPeriod() );
        original.setSource( update.getSource() );
        original.setValue( update.getValue() );
        original.setStoredBy( update.getStoredBy() );
        original.setTimestamp( update.getTimestamp() );
        original.setComment( update.getComment() );
        original.setOptionCombo( update.getOptionCombo() );
        
        return original;
    }

    // -------------------------------------------------------------------------
    // Cascade delete - supportive methods
    // -------------------------------------------------------------------------
    
    private void deleteMemberAssociations( GroupMemberType groupMemberType, int memberId )
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( groupMemberType );
        
        for ( ImportObject importObject : importObjects )
        {
            GroupMemberAssociation association = (GroupMemberAssociation) importObject.getObject();
            
            if ( association.getMemberId() == memberId )
            {
                importObjectStore.deleteImportObject( importObject );
            }
        }
    }

    private void deleteGroupAssociations( GroupMemberType groupMemberType, int groupId )
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( groupMemberType);
        
        for ( ImportObject importObject : importObjects )
        {
            GroupMemberAssociation association = (GroupMemberAssociation) importObject.getObject();
            
            if ( association.getGroupId() == groupId )
            {
                importObjectStore.deleteImportObject( importObject );
            }
        }   
    }
    
    private void deleteIndicatorsContainingDataElement( int dataElementId )
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( Indicator.class );
        
        for ( ImportObject importObject : importObjects )
        {
            Indicator indicator = (Indicator) importObject.getObject();
            
            if ( containsIdentifier( indicator.getNumerator(), dataElementId ) || containsIdentifier( indicator.getDenominator(), dataElementId ) )
            {
                importObjectStore.deleteImportObject( importObject );
                
                deleteMemberAssociations( GroupMemberType.INDICATORGROUP, indicator.getId() );
                
                deleteMemberAssociations( GroupMemberType.DATADICTIONARY_INDICATOR, indicator.getId() );
                
                deleteMemberAssociations( GroupMemberType.REPORTTABLE_INDICATOR, indicator.getId() );
            }   
        }   
    }
    
    private void deleteIndicatorsWithIndicatorType( int indicatorTypeId )
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( Indicator.class );
        
        for ( ImportObject importObject : importObjects )
        {
            Indicator indicator = (Indicator) importObject.getObject();
            
            if ( indicator.getIndicatorType().getId() == indicatorTypeId )
            {
                importObjectStore.deleteImportObject( importObject );
                
                deleteMemberAssociations( GroupMemberType.INDICATORGROUP, indicator.getId() );
                
                deleteMemberAssociations( GroupMemberType.DATADICTIONARY_INDICATOR, indicator.getId() );
                
                deleteMemberAssociations( GroupMemberType.REPORTTABLE_INDICATOR, indicator.getId() );
            }
        }
    }
    
    private void deleteCompleteDataSetRegistrationsByDataSet( int dataSetId )
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( CompleteDataSetRegistration.class );
        
        for ( ImportObject importObject : importObjects )
        {
            CompleteDataSetRegistration registration = (CompleteDataSetRegistration) importObject.getObject();
            
            if ( registration.getDataSet().getId() == dataSetId )
            {
                importObjectStore.deleteImportObject( importObject );
            }
        }
    }

    private void deleteCompleteDataSetRegistrationsBySource( int sourceId )
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( CompleteDataSetRegistration.class );
        
        for ( ImportObject importObject : importObjects )
        {
            CompleteDataSetRegistration registration = (CompleteDataSetRegistration) importObject.getObject();
            
            if ( registration.getSource().getId() == sourceId )
            {
                importObjectStore.deleteImportObject( importObject );
            }
        }
    }
}
