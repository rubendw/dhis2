package org.hisp.dhis.importexport;

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

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.validation.ValidationRule;

/**
 * @author Lars Helge Overland
 * @version $Id: ExportParams.java 5727 2008-09-18 17:48:54Z larshelg $
 */
public class ExportParams
{
    private boolean includeDataValues;
    
    private boolean extendedMode;
    
    private Collection<DataElementCategory> categories = new ArrayList<DataElementCategory>();
    
    private Collection<DataElementCategoryOption> categoryOptions = new ArrayList<DataElementCategoryOption>();
    
    private Collection<DataElementCategoryCombo> categoryCombos = new ArrayList<DataElementCategoryCombo>();

    private Collection<DataElementCategoryOptionCombo> categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();

    private Collection<DataElement> dataElements = new ArrayList<DataElement>();
    
    private Collection<DataElementGroup> dataElementGroups = new ArrayList<DataElementGroup>();
    
    private Collection<Indicator> indicators = new ArrayList<Indicator>();
    
    private Collection<IndicatorGroup> indicatorGroups = new ArrayList<IndicatorGroup>();
    
    private Collection<IndicatorType> indicatorTypes = new ArrayList<IndicatorType>();
    
    private Collection<DataDictionary> dataDictionaries = new ArrayList<DataDictionary>();
    
    private Collection<DataSet> dataSets = new ArrayList<DataSet>();
    
    private Collection<CompleteDataSetRegistration> completeDataSetRegistrations = new ArrayList<CompleteDataSetRegistration>();
    
    private Collection<Period> periods = new ArrayList<Period>();
    
    private Collection<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();
    
    private Collection<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>();
    
    private Collection<OrganisationUnitGroupSet> organisationUnitGroupSets = new ArrayList<OrganisationUnitGroupSet>();
    
    private Collection<ValidationRule> validationRules = new ArrayList<ValidationRule>();
    
    private Collection<ReportTable> reportTables = new ArrayList<ReportTable>();
    
    private I18n i18n;
    
    private I18nFormat format;
    
    // -------------------------------------------------------------------------
    // Constructur
    // -------------------------------------------------------------------------
    
    public ExportParams()
    {   
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    public boolean isIncludeDataValues()
    {
        return includeDataValues;
    }

    public void setIncludeDataValues( boolean includeDataValues )
    {
        this.includeDataValues = includeDataValues;
    }

    public boolean isExtendedMode()
    {
        return extendedMode;
    }

    public void setExtendedMode( boolean extendedMode )
    {
        this.extendedMode = extendedMode;
    }

    public Collection<DataElementCategory> getCategories()
    {
        return categories;
    }

    public void setCategories( Collection<DataElementCategory> categories )
    {
        this.categories = categories;
    }

    public Collection<DataElementCategoryOption> getCategoryOptions()
    {
        return categoryOptions;
    }

    public void setCategoryOptions( Collection<DataElementCategoryOption> categoryOptions )
    {
        this.categoryOptions = categoryOptions;
    }

    public Collection<DataElementCategoryCombo> getCategoryCombos()
    {
        return categoryCombos;
    }

    public void setCategoryCombos( Collection<DataElementCategoryCombo> categoryCombos )
    {
        this.categoryCombos = categoryCombos;
    }

    public Collection<DataElementCategoryOptionCombo> getCategoryOptionCombos()
    {
        return categoryOptionCombos;
    }

    public void setCategoryOptionCombos( Collection<DataElementCategoryOptionCombo> categoryOptionCombos )
    {
        this.categoryOptionCombos = categoryOptionCombos;
    }

    public Collection<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( Collection<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public Collection<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    public void setDataElementGroups( Collection<DataElementGroup> dataElementGroups )
    {
        this.dataElementGroups = dataElementGroups;
    }

    public Collection<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( Collection<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    public Collection<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    public void setIndicatorGroups( Collection<IndicatorGroup> indicatorGroups )
    {
        this.indicatorGroups = indicatorGroups;
    }

    public Collection<IndicatorType> getIndicatorTypes()
    {
        return indicatorTypes;
    }

    public void setIndicatorTypes( Collection<IndicatorType> indicatorTypes )
    {
        this.indicatorTypes = indicatorTypes;
    }

    public Collection<DataDictionary> getDataDictionaries()
    {
        return dataDictionaries;
    }

    public void setDataDictionaries( Collection<DataDictionary> dataDictionaries )
    {
        this.dataDictionaries = dataDictionaries;
    }

    public Collection<DataSet> getDataSets()
    {
        return dataSets;
    }

    public void setDataSets( Collection<DataSet> dataSets )
    {
        this.dataSets = dataSets;
    }

    public Collection<CompleteDataSetRegistration> getCompleteDataSetRegistrations()
    {
        return completeDataSetRegistrations;
    }

    public void setCompleteDataSetRegistrations( Collection<CompleteDataSetRegistration> completeDataSetRegistrations )
    {
        this.completeDataSetRegistrations = completeDataSetRegistrations;
    }

    public Collection<Period> getPeriods()
    {
        return periods;
    }

    public void setPeriods( Collection<Period> periods )
    {
        this.periods = periods;
    }
    
    public Collection<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Collection<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    public Collection<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups( Collection<OrganisationUnitGroup> organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }

    public Collection<OrganisationUnitGroupSet> getOrganisationUnitGroupSets()
    {
        return organisationUnitGroupSets;
    }

    public void setOrganisationUnitGroupSets( Collection<OrganisationUnitGroupSet> organisationUnitGroupSets )
    {
        this.organisationUnitGroupSets = organisationUnitGroupSets;
    }

    public Collection<ValidationRule> getValidationRules()
    {
        return validationRules;
    }

    public void setValidationRules( Collection<ValidationRule> validationRules )
    {
        this.validationRules = validationRules;
    }

    public Collection<ReportTable> getReportTables()
    {
        return reportTables;
    }

    public void setReportTables( Collection<ReportTable> reportTables )
    {
        this.reportTables = reportTables;
    }

    public I18n getI18n()
    {
        return i18n;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }
    
    public I18nFormat getFormat()
    {
        return format;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }
}
