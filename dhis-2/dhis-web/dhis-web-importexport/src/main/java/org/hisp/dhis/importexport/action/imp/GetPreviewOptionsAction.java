package org.hisp.dhis.importexport.action.imp;

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

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.importexport.ImportDataValueService;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.importexport.action.util.ClassMapUtil;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.validation.ValidationRule;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetPreviewOptionsAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ImportObjectService importObjectService;

    public void setImportObjectService( ImportObjectService importObjectService )
    {
        this.importObjectService = importObjectService;
    }
    
    private ImportDataValueService importDataValueService;

    public void setImportDataValueService( ImportDataValueService importDataValueService )
    {
        this.importDataValueService = importDataValueService;
    }
        
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private String type;

    public String getType()
    {
        return type;
    }

    public void setType( String elementType )
    {
        this.type = elementType;
    }
    
    private String status;

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String elementStatus )
    {
        this.status = elementStatus;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Integer newDataElements;

    public Integer getNewDataElements()
    {
        return newDataElements;
    }
    
    private Integer newExtendedDataElements;

    public Integer getNewExtendedDataElements()
    {
        return newExtendedDataElements;
    }

    private Integer newDataElementGroups;

    public Integer getNewDataElementGroups()
    {
        return newDataElementGroups;
    }
    
    private Integer newIndicatorTypes;

    public Integer getNewIndicatorTypes()
    {
        return newIndicatorTypes;
    }

    private Integer newIndicators;

    public Integer getNewIndicators()
    {
        return newIndicators;
    }

    private Integer newIndicatorGroups;

    public Integer getNewIndicatorGroups()
    {
        return newIndicatorGroups;
    }
    
    private Integer newDataDictionaries;

    public Integer getNewDataDictionaries()
    {
        return newDataDictionaries;
    }
    
    private Integer newDataSets;

    public Integer getNewDataSets()
    {
        return newDataSets;
    }

    private Integer newOrganisationUnits;

    public Integer getNewOrganisationUnits()
    {
        return newOrganisationUnits;
    }

    private Integer newOrganisationUnitGroups;

    public Integer getNewOrganisationUnitGroups()
    {
        return newOrganisationUnitGroups;
    }

    private Integer newOrganisationUnitGroupSets;

    public Integer getNewOrganisationUnitGroupSets()
    {
        return newOrganisationUnitGroupSets;
    }
    
    private Integer newValidationRules;

    public Integer getNewValidationRules()
    {
        return newValidationRules;
    }
    
    private Integer newReportTables;

    public Integer getNewReportTables()
    {
        return newReportTables;
    }
    
    private Integer newDataValues;

    public Integer getNewDataValues()
    {
        return newDataValues;
    }

    private Integer updateDataElements;

    public Integer getUpdateDataElements()
    {
        return updateDataElements;
    }
    
    private Integer updateExtendedDataElements;

    public Integer getUpdateExtendedDataElements()
    {
        return updateExtendedDataElements;
    }

    private Integer updateDataElementGroups;

    public Integer getUpdateDataElementGroups()
    {
        return updateDataElementGroups;
    }
    
    private Integer updateIndicatorTypes;

    public Integer getUpdateIndicatorTypes()
    {
        return updateIndicatorTypes;
    }

    private Integer updateIndicators;

    public Integer getUpdateIndicators()
    {
        return updateIndicators;
    }

    private Integer updateIndicatorGroups;

    public Integer getUpdateIndicatorGroups()
    {
        return updateIndicatorGroups;
    }
    
    private Integer updateDataDictionaries;

    public Integer getUpdateDataDictionaries()
    {
        return updateDataDictionaries;
    }
    
    private Integer updateDataSets;

    public Integer getUpdateDataSets()
    {
        return updateDataSets;
    }

    private Integer updateOrganisationUnits;

    public Integer getUpdateOrganisationUnits()
    {
        return updateOrganisationUnits;
    }
    
    private Integer updateOrganisationUnitGroups;

    public Integer getUpdateOrganisationUnitGroups()
    {
        return updateOrganisationUnitGroups;
    }

    private Integer updateOrganisationUnitGroupSets;

    public Integer getUpdateOrganisationUnitGroupSets()
    {
        return updateOrganisationUnitGroupSets;
    }
    
    private Integer updateValidationRules;

    public Integer getUpdateValidationRules()
    {
        return updateValidationRules;
    }
    
    private Integer updateReportTables;

    public Integer getUpdateReportTables()
    {
        return updateReportTables;
    }
    
    private Integer updateDataValues;

    public Integer getUpdateDataValues()
    {
        return updateDataValues;
    }
    
    private Collection<? extends Object> objects;

    public Collection<? extends Object> getObjects()
    {
        return objects;
    }
    
    public Collection<? extends Object> importObjects;

    public Collection<? extends Object> getImportObjects()
    {
        return importObjects;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        newDataElements = importObjectService.getImportObjects( ImportObjectStatus.NEW, DataElement.class ).size();
        newExtendedDataElements = importObjectService.getImportObjects( ImportObjectStatus.NEW, ExtendedDataElement.class  ).size();
        newDataElementGroups = importObjectService.getImportObjects( ImportObjectStatus.NEW, DataElementGroup.class ).size();
        newIndicatorTypes = importObjectService.getImportObjects( ImportObjectStatus.NEW, IndicatorType.class ).size();
        newIndicators = importObjectService.getImportObjects( ImportObjectStatus.NEW, Indicator.class ).size();
        newIndicatorGroups = importObjectService.getImportObjects( ImportObjectStatus.NEW, IndicatorGroup.class ).size();
        newDataDictionaries = importObjectService.getImportObjects( ImportObjectStatus.NEW, DataDictionary.class ).size();
        newDataSets = importObjectService.getImportObjects( ImportObjectStatus.NEW, DataSet.class ).size();
        newOrganisationUnits = importObjectService.getImportObjects( ImportObjectStatus.NEW, OrganisationUnit.class ).size();
        newOrganisationUnitGroups = importObjectService.getImportObjects( ImportObjectStatus.NEW, OrganisationUnitGroup.class ).size();
        newOrganisationUnitGroupSets = importObjectService.getImportObjects( ImportObjectStatus.NEW, OrganisationUnitGroupSet.class ).size();
        newValidationRules = importObjectService.getImportObjects( ImportObjectStatus.NEW, ValidationRule.class ).size();
        newReportTables = importObjectService.getImportObjects( ImportObjectStatus.NEW, ReportTable.class ).size();
        newDataValues = importDataValueService.getImportDataValues( ImportObjectStatus.NEW ).size();
        
        updateDataElements = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, DataElement.class ).size();
        updateExtendedDataElements = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, ExtendedDataElement.class ).size();
        updateDataElementGroups = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, DataElementGroup.class ).size();
        updateIndicatorTypes = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, IndicatorType.class ).size();
        updateIndicators = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, Indicator.class ).size();
        updateIndicatorGroups = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, IndicatorGroup.class ).size();
        updateDataDictionaries = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, DataDictionary.class ).size();
        updateDataSets = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, DataSet.class ).size();
        updateOrganisationUnits = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, OrganisationUnit.class ).size();
        updateOrganisationUnitGroups = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, OrganisationUnitGroup.class ).size();
        updateOrganisationUnitGroupSets = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, OrganisationUnitGroupSet.class ).size();
        updateValidationRules = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, ValidationRule.class ).size();
        updateReportTables = importObjectService.getImportObjects( ImportObjectStatus.UPDATE, ReportTable.class ).size();
        updateDataValues = importDataValueService.getImportDataValues( ImportObjectStatus.UPDATE ).size();
        
        if ( type != null && status != null )
        {
            importObjects = importObjectService.getImportObjects( ImportObjectStatus.valueOf( status ), ClassMapUtil.getClass( type ) );            
        }
                       
        return SUCCESS;
    }
}
