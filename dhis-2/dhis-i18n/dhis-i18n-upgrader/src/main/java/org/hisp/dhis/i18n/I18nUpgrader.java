package org.hisp.dhis.i18n;

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
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.i18n.locale.LocaleManager;
import org.hisp.dhis.i18n.locale.LocaleManagerException;
import org.hisp.dhis.i18n.util.LocaleUtils;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.startup.AbstractTransactionalStartupRoutine;
import org.hisp.dhis.system.util.SystemUtils;

/**
 * @author Oyvind Brucker
 */
public class I18nUpgrader
    extends AbstractTransactionalStartupRoutine
{
    private static Log log = LogFactory.getLog( I18nUpgrader.class );

    String ID = I18nUpgrader.class.getName();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataSetStore dataSetStore;

    public void setDataSetStore( DataSetStore dataSetStore )
    {
        this.dataSetStore = dataSetStore;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
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
    
    private DataElementCategoryService dataElementCategoryService;

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }
    
    private DataElementCategoryOptionService dataElementCategoryOptionService;

    public void setDataElementCategoryOptionService( DataElementCategoryOptionService dataElementCategoryOptionService )
    {
        this.dataElementCategoryOptionService = dataElementCategoryOptionService;
    }
    
    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }
    
    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    private TranslationStore translationStore;

    public void setTranslationStore( TranslationStore translationStore )
    {
        this.translationStore = translationStore;
    }

    private I18nService i18nService;

    public void setI18nService( I18nService i18nService )
    {
        this.i18nService = i18nService;
    }

    private Locale alternativeNameLocale = null;

    public void setAlternativeNameLocale( String alternativeNameLocale )
    {
        this.alternativeNameLocale = LocaleUtils.getLocale( alternativeNameLocale );
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public void executeRoutine()
    {
        // ---------------------------------------------------------------------
        // Check permission to run
        // ---------------------------------------------------------------------

        if ( !SystemUtils.isRunningForUse() )
        {
            return;
        }

        // ---------------------------------------------------------------------
        // Check if it should run - if so make the i18n service aware of all objects
        // ---------------------------------------------------------------------

        Collection<Translation> translations = translationStore.getAllTranslations();

        if ( translations == null || translations.isEmpty() )
        {
            Locale orgLocale = null;

            try
            {
                orgLocale = localeManager.getCurrentLocale();

                localeManager.setCurrentLocale( localeManager.getFallbackLocale() );

                String alternativeNameDescription = "";

                if ( alternativeNameLocale != null )
                {
                    alternativeNameDescription = " and " + alternativeNameLocale.getDisplayName()
                        + " for alternative names";
                }

                log.info( "I18n Upgrader running using locale " + localeManager.getFallbackLocale().getDisplayName()
                    + alternativeNameDescription );
            }
            catch ( LocaleManagerException e )
            {
                orgLocale = localeManager.getFallbackLocale();
            }

            // ---------------------------------------------------------------------
            // DataElement
            // ---------------------------------------------------------------------

            Collection<DataElement> dataElements = dataElementService.getAllDataElements();

            if ( dataElements != null && !dataElements.isEmpty() )
            {
                log.info( "I18n Upgrader: DataElement (" + dataElements.size() + ")" );

                for ( DataElement dataElement : dataElements )
                {
                    i18nService.addObject( dataElement );

                    if ( alternativeNameLocale != null && dataElement.getAlternativeName() != null )
                    {
                        i18nService.addTranslation( dataElement, "name", dataElement.getAlternativeName(),
                            alternativeNameLocale );
                    }
                }
            }

            // ---------------------------------------------------------------------
            // DataElementGroup
            // ---------------------------------------------------------------------

            Collection<DataElementGroup> dataElementGroups = dataElementService.getAllDataElementGroups();

            if ( dataElementGroups != null && !dataElementGroups.isEmpty() )
            {
                log.info( "I18n Upgrader: dataElementGroup (" + dataElementGroups.size() + ")" );

                for ( DataElementGroup dataElementGroup : dataElementGroups )
                {
                    i18nService.addObject( dataElementGroup );
                }
            }

            // ---------------------------------------------------------------------
            // Indicator
            // ---------------------------------------------------------------------

            Collection<Indicator> indicators = indicatorService.getAllIndicators();

            if ( indicators != null && !indicators.isEmpty() )
            {
                log.info( "I18n Upgrader: Indicator (" + indicators.size() + ")" );

                for ( Indicator indicator : indicators )
                {
                    i18nService.addObject( indicator );

                    if ( alternativeNameLocale != null && indicator.getAlternativeName() != null )
                    {
                        i18nService.addTranslation( indicator, "name", indicator.getAlternativeName(),
                            alternativeNameLocale );
                    }
                }
            }

            // ---------------------------------------------------------------------
            // IndicatorType
            // ---------------------------------------------------------------------

            Collection<IndicatorType> indicatorTypes = indicatorService.getAllIndicatorTypes();

            if ( indicatorTypes != null && !indicatorTypes.isEmpty() )
            {
                log.info( "I18n Upgrader: indicatorType (" + indicatorTypes.size() + ")" );

                for ( IndicatorType indicatorType : indicatorTypes )
                {
                    i18nService.addObject( indicatorType );
                }
            }

            // ---------------------------------------------------------------------
            // IndicatorGroup
            // ---------------------------------------------------------------------

            Collection<IndicatorGroup> indicatorGroups = indicatorService.getAllIndicatorGroups();

            if ( indicatorGroups != null && !indicatorGroups.isEmpty() )
            {
                log.info( "I18n Upgrader: indicatorGroup (" + indicatorGroups.size() + ")" );

                for ( IndicatorGroup indicatorGroup : indicatorGroups )
                {
                    i18nService.addObject( indicatorGroup );
                }
            }

            // ---------------------------------------------------------------------
            // OrganisationUnit
            // ---------------------------------------------------------------------

            Collection<OrganisationUnit> orgunits = organisationUnitService.getAllOrganisationUnits();

            if ( orgunits != null && !orgunits.isEmpty() )
            {
                log.info( "I18n Upgrader: OrganisationUnit (" + orgunits.size() + ")" );

                for ( OrganisationUnit orgunit : orgunits )
                {
                    i18nService.addObject( orgunit );
                }
            }

            // ---------------------------------------------------------------------
            // OrganisationUnitGroup
            // ---------------------------------------------------------------------

            Collection<OrganisationUnitGroup> orgunitGroups = organisationUnitGroupService
                .getAllOrganisationUnitGroups();

            if ( orgunitGroups != null && !orgunitGroups.isEmpty() )
            {
                log.info( "I18n Upgrader: OrganisationUnitGroup (" + orgunitGroups.size() + ")" );

                for ( OrganisationUnitGroup orgunitGroup : orgunitGroups )
                {
                    i18nService.addObject( orgunitGroup );
                }
            }

            // ---------------------------------------------------------------------
            // OrganisationUnitGroupSet
            // ---------------------------------------------------------------------

            Collection<OrganisationUnitGroupSet> orgunitGroupSets = organisationUnitGroupService
                .getAllOrganisationUnitGroupSets();

            if ( orgunitGroupSets != null && !orgunitGroupSets.isEmpty() )
            {
                log.info( "I18n Upgrader: OrganisationUnitGroupSet (" + orgunitGroupSets.size() + ")" );

                for ( OrganisationUnitGroupSet orgunitGroupSet : orgunitGroupSets )
                {
                    i18nService.addObject( orgunitGroupSet );
                }
            }

            // ---------------------------------------------------------------------
            // DataSet
            // ---------------------------------------------------------------------

            Collection<DataSet> dataSets = dataSetStore.getAllDataSets();

            if ( dataSets != null && !dataSets.isEmpty() )
            {
                log.info( "I18n Upgrader: DataSet (" + dataSets.size() + ")" );

                for ( DataSet dataSet : dataSets )
                {
                    i18nService.addObject( dataSet );
                }
            }

            // ---------------------------------------------------------------------
            // DataDictionary
            // ---------------------------------------------------------------------

            Collection<DataDictionary> dataDictionaries = dataDictionaryService.getAllDataDictionaries();
            
            if ( dataDictionaries != null && !dataDictionaries.isEmpty() )
            {
                log.info( "I81n Upgrader: DataDictionary (" + dataDictionaries.size() + ")" );
                
                for ( DataDictionary dataDictionary : dataDictionaries )
                {
                    i18nService.addObject( dataDictionary );
                }
            }

            try
            {
                localeManager.setCurrentLocale( orgLocale );
            }
            catch ( LocaleManagerException e )
            {
            }
            
            // ---------------------------------------------------------------------
            // DataElementCategory
            // ---------------------------------------------------------------------

            Collection<DataElementCategory> categories = dataElementCategoryService.getAllDataElementCategories();
            
            if ( categories != null && !categories.isEmpty() )
            {
                log.info( "I81n Upgrader: DataElementCategory (" + categories.size() + ")" );
                
                for ( DataElementCategory category : categories )
                {
                    i18nService.addObject( category );
                }
            }

            try
            {
                localeManager.setCurrentLocale( orgLocale );
            }
            catch ( LocaleManagerException e )
            {
            }
            
            // ---------------------------------------------------------------------
            // DataElementCategoryOption
            // ---------------------------------------------------------------------            
            
            Collection<DataElementCategoryOption> categoryOptions = dataElementCategoryOptionService.getAllDataElementCategoryOptions();
            
            if ( categoryOptions != null && !categoryOptions.isEmpty() )
            {
                log.info( "I81n Upgrader: DataElementCategoryOption (" + categoryOptions.size() + ")" );                
                
                for ( DataElementCategoryOption categoryOption : categoryOptions )
                {
                    i18nService.addObject( categoryOption );                    
                }
            }

            try
            {
                localeManager.setCurrentLocale( orgLocale );
            }
            catch ( LocaleManagerException e )
            {
            }
            
            // ---------------------------------------------------------------------
            // DataElementCategoryCombo
            // ---------------------------------------------------------------------

            Collection<DataElementCategoryCombo> categoryCombos = dataElementCategoryComboService.getAllDataElementCategoryCombos();
            
            if ( categoryCombos != null && !categoryCombos.isEmpty() )
            {
                log.info( "I81n Upgrader: DataElementCategoryCombo (" + categoryCombos.size() + ")" );
                
                for ( DataElementCategoryCombo categoryCombo : categoryCombos )
                {
                    i18nService.addObject( categoryCombo );
                }
            }

            try
            {
                localeManager.setCurrentLocale( orgLocale );
            }
            catch ( LocaleManagerException e )
            {
            }
        }
    }
}
