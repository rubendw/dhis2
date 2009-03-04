package org.hisp.dhis.importexport.action.exp;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ExportService;
import org.hisp.dhis.importexport.ImportDataValueService;
import org.hisp.dhis.importexport.ImportExportServiceManager;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.options.datadictionary.DataDictionaryModeManager;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableStore;
import org.hisp.dhis.validation.ValidationRuleService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class MetaDataExportAction
    extends ActionSupport
{
    private static final String FILENAME = "Export_meta.zip";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }
        
    private ImportExportServiceManager serviceManager;

    public void setServiceManager( ImportExportServiceManager serviceManager )
    {
        this.serviceManager = serviceManager;
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
    
    private ReportTableStore reportTableStore;

    public void setReportTableStore( ReportTableStore reportTableStore )
    {
        this.reportTableStore = reportTableStore;
    }

    private ImportDataValueService importDataValueService;

    public void setImportDataValueService( ImportDataValueService importDataValueService )
    {
        this.importDataValueService = importDataValueService;
    }

    private ImportObjectService importObjectService;

    public void setImportObjectService( ImportObjectService importObjectService )
    {
        this.importObjectService = importObjectService;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private InputStream inputStream;

    public InputStream getInputStream()
    {
        return inputStream;
    }
    
    private String fileName;

    public String getFileName()
    {
        return fileName;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String exportFormat;

    public String getExportFormat()
    {
        return exportFormat;
    }

    public void setExportFormat( String exportFormat )
    {
        this.exportFormat = exportFormat;
    }

    private String dataDictionaryMode;

    public void setDataDictionaryMode( String dataDictionaryMode )
    {
        this.dataDictionaryMode = dataDictionaryMode;
    }
    
    private boolean dataElements;

    public void setDataElements( boolean dataElements )
    {
        this.dataElements = dataElements;
    }

    private boolean dataElementGroups;

    public void setDataElementGroups( boolean dataElementGroups )
    {
        this.dataElementGroups = dataElementGroups;
    }
    
    private boolean dataDictionaries;

    public void setDataDictionaries( boolean dataDictionaries )
    {
        this.dataDictionaries = dataDictionaries;
    }
    
    private boolean dataSets;

    public void setDataSets( boolean dataSets )
    {
        this.dataSets = dataSets;
    }

    private boolean indicators;

    public void setIndicators( boolean indicators )
    {
        this.indicators = indicators;
    }

    private boolean indicatorGroups;

    public void setIndicatorGroups( boolean indicatorGroups )
    {
        this.indicatorGroups = indicatorGroups;
    }

    private boolean organisationUnits;

    public void setOrganisationUnits( boolean organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    private boolean organisationUnitGroups;

    public void setOrganisationUnitGroups( boolean organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }

    private boolean organisationUnitGroupSets;

    public void setOrganisationUnitGroupSets( boolean organisationUnitGroupSets )
    {
        this.organisationUnitGroupSets = organisationUnitGroupSets;
    }
    
    private boolean validationRules;

    public void setValidationRules( boolean validationRules )
    {
        this.validationRules = validationRules;
    }
    
    private boolean reportTables;

    public void setReportTables( boolean reportTables )
    {
        this.reportTables = reportTables;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        importDataValueService.deleteImportDataValues();
        
        importObjectService.deleteImportObjects();
        
        ExportParams params = new ExportParams();

        params.setExtendedMode( dataDictionaryMode != null && dataDictionaryMode.equals( DataDictionaryModeManager.DATADICTIONARY_MODE_EXTENDED ) );
        
        if ( dataElements || dataElementGroups || indicators || dataSets || validationRules || reportTables )
        {
            params.setCategories( categoryService.getAllDataElementCategories() );
            params.setCategoryCombos( categoryComboService.getAllDataElementCategoryCombos() );
            params.setCategoryOptions( categoryOptionService.getAllDataElementCategoryOptions() );
            params.setCategoryOptionCombos( categoryOptionComboService.getAllDataElementCategoryOptionCombos() );
            params.setDataElements( dataElementService.getAllDataElements() );
        }
        
        if ( dataElementGroups )
        {
            params.setDataElementGroups( dataElementService.getAllDataElementGroups() );
        }
        
        if ( indicators || indicatorGroups || reportTables )
        {
            params.setIndicators( indicatorService.getAllIndicators() );
            
            params.setIndicatorTypes( indicatorService.getAllIndicatorTypes() );
        }
        
        if ( indicatorGroups )
        {
            params.setIndicatorGroups( indicatorService.getAllIndicatorGroups() );
        }

        if ( dataDictionaries )
        {
            params.setDataDictionaries( dataDictionaryService.getAllDataDictionaries() );
        }
        
        if ( dataSets )
        {
            params.setDataSets( dataSetService.getAllDataSets() );            
        }
        
        if ( organisationUnits || organisationUnitGroups || reportTables )
        {
            params.setOrganisationUnits( organisationUnitService.getAllOrganisationUnits() );
        }
        
        if ( organisationUnitGroups || organisationUnitGroupSets )
        {
            params.setOrganisationUnitGroups( organisationUnitGroupService.getAllOrganisationUnitGroups() );            
        }
        
        if ( organisationUnitGroupSets )
        {
            params.setOrganisationUnitGroupSets( organisationUnitGroupService.getAllOrganisationUnitGroupSets() );
        }
        
        if ( validationRules )
        {
            params.setValidationRules( validationRuleService.getAllValidationRules() );
        }
        
        if ( reportTables )
        {            
            params.setReportTables( reportTableStore.getAllReportTables() );
            
            params.setPeriods( getPeriodsInReportTables( params.getReportTables() ) );
        }
        
        params.setIncludeDataValues( false );
        
        params.setI18n( i18n );
        params.setFormat( format );
        
        ExportService exportService = serviceManager.getExportService( exportFormat );
        
        inputStream = exportService.exportData( params );
        
        fileName = FILENAME;
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Collection<Period> getPeriodsInReportTables( Collection<ReportTable> reportTables )
    {
        Set<Period> distinctPeriods = new HashSet<Period>();
        
        for ( ReportTable reportTable : reportTables )
        {
            distinctPeriods.addAll( reportTable.getPeriods() );
        }
        
        return distinctPeriods;
    }
}
