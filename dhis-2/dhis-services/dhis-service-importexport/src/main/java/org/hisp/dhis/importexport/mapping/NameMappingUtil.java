package org.hisp.dhis.importexport.mapping;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: NameMappingUtil.java 5727 2008-09-18 17:48:54Z larshelg $
 */
public class NameMappingUtil
{
    private static ThreadLocal<Map<Object, String>> categoryMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> categoryOptionMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> categoryComboMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, DataElementCategoryOptionCombo>> categoryOptionComboMap = new ThreadLocal<Map<Object, DataElementCategoryOptionCombo>>();    
    private static ThreadLocal<Map<Object, String>> dataElementMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> dataElementGroupMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> indicatorMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> indicatorGroupMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> indicatorTypeMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> dataDictionaryMap = new ThreadLocal<Map<Object,String>>();
    private static ThreadLocal<Map<Object, Period>> periodMap = new ThreadLocal<Map<Object, Period>>();
    private static ThreadLocal<Map<Object, String>> dataSetMap = new ThreadLocal<Map<Object,String>>();
    private static ThreadLocal<Map<Object, String>> organisationUnitMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> organisationUnitGroupMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> organisationUnitGroupSetMap = new ThreadLocal<Map<Object, String>>();
    private static ThreadLocal<Map<Object, String>> reportTableMap = new ThreadLocal<Map<Object,String>>();
    private static ThreadLocal<Set<Period>> onChangePeriodSet = new ThreadLocal<Set<Period>>();

    // -------------------------------------------------------------------------
    // Control
    // -------------------------------------------------------------------------
    
    public static void clearMapping()
    {
        categoryMap.remove();
        categoryOptionMap.remove();
        categoryComboMap.remove();
        categoryOptionComboMap.remove();
        dataElementMap.remove();
        dataElementGroupMap.remove();
        indicatorMap.remove();
        indicatorGroupMap.remove();
        indicatorTypeMap.remove();
        dataDictionaryMap.remove();
        periodMap.remove();
        dataSetMap.remove();
        organisationUnitMap.remove();
        organisationUnitGroupMap.remove();
        organisationUnitGroupSetMap.remove();
        reportTableMap.remove();
        onChangePeriodSet.remove();
    }

    // -------------------------------------------------------------------------
    // Category
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataElementCategory identifier as key and name as value.
     */
    public static void addCategoryMapping( Object categoryId, String categoryName )
    {
        put( categoryMap, categoryId, categoryName );
    }
    
    /**
     * Returns a Map with all DataElementCategory identifier and name entries.
     */
    public static Map<Object, String> getCategoryMap()
    {
        return categoryMap.get() != null ? new HashMap<Object, String>( categoryMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // CategoryOption
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataElementCategoryOption identifier as key and name as value.
     */
    public static void addCategoryOptionMapping( Object categoryOptionId, String categoryOptionName )
    {
        put( categoryOptionMap, categoryOptionId, categoryOptionName );
    }
    
    /**
     * Returns a Map with all DataElementCategoryOption identifier and name entries.
     */
    public static Map<Object, String> getCategoryOptionMap()
    {
        return categoryOptionMap.get() != null ? new HashMap<Object, String>( categoryOptionMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // CategoryCombo
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataElementCategoryCombo identifier as key and name as value.
     */
    public static void addCategoryComboMapping( Object categoryComboId, String categoryComboName )
    {
        put( categoryComboMap, categoryComboId, categoryComboName );
    }
    
    /**
     * Returns a Map with all DataElementCategoryCombo identifier and name entries.
     */
    public static Map<Object, String> getCategoryComboMap()
    {
        return categoryComboMap.get() != null ? new HashMap<Object, String>( categoryComboMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // CategoryOptionCombo
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataElementCategoryOptionCombo identifier as key
     * and the DataElementCategoryOptionCombo as value.
     */
    public static void addCategoryOptionComboMapping( Object categoryOptionComboId, DataElementCategoryOptionCombo categoryOptionCombo )
    {
        Map<Object, DataElementCategoryOptionCombo> map = categoryOptionComboMap.get();
        
        if ( map == null )
        {
            map = new HashMap<Object, DataElementCategoryOptionCombo>();
        }
        
        if ( !map.containsKey( categoryOptionComboId ) )
        {
            map.put( categoryOptionComboId, categoryOptionCombo );
            
            categoryOptionComboMap.set( map );
        }
    }
    
    /**
     * Returns a Map with all DataElementCategoryOption identifier and
     * DataElementCategoryOptionCombo entries.
     */
    public static Map<Object, DataElementCategoryOptionCombo> getCategoryOptionComboMap()
    {
        return categoryOptionComboMap.get() != null ? new HashMap<Object, DataElementCategoryOptionCombo>( categoryOptionComboMap.get() ) : new HashMap<Object, DataElementCategoryOptionCombo>();
    }

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataElement identifier as key and name as value.
     */
    public static void addDataElementMapping( Object dataElementId, String dataElementName )
    {
        put( dataElementMap, dataElementId, dataElementName );
    }
    
    /**
     * Returns a map with all DataElement identifier and name entries.
     */
    public static Map<Object, String> getDataElementMap()
    {
        return dataElementMap.get() != null ? new HashMap<Object, String>( dataElementMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataElementGroup identifier as key and name as value.
     */
    public static void addDataElementGroupMapping( Object groupId, String groupName )
    {
        put( dataElementGroupMap, groupId, groupName );
    }
    
    /**
     * Returns a map with all DataElementGroup identifier and name entries. 
     */
    public static Map<Object, String> getDataElementGroupMap()
    {
        return dataElementGroupMap.get() != null ? new HashMap<Object, String>( dataElementGroupMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with Indicator identifier as key and name as value.
     */
    public static void addIndicatorMapping( Object indicatorId, String indicatorName )
    {
        put( indicatorMap, indicatorId, indicatorName );
    }
    
    /**
     * Returns a map with all Indicator identifier and name entries.
     */
    public static Map<Object, String> getIndicatorMap()
    {
        return indicatorMap.get() != null ? new HashMap<Object, String>( indicatorMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with IndicatorGroup identifier as key and name as value.
     */
    public static void addIndicatorGroupMapping( Object groupId, String groupName )
    {
        put( indicatorGroupMap, groupId, groupName );
    }
    
    /**
     * Returns a map with all Indicator identifier and name entries.
     */
    public static Map<Object, String> getIndicatorGroupMap()
    {
        return indicatorGroupMap.get() != null ? new HashMap<Object, String>( indicatorGroupMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with IndicatorType identifier as key and name as value.
     */
    public static void addIndicatorTypeMapping( Object typeId, String typeName )
    {
        put( indicatorTypeMap, typeId, typeName );
    }
    
    /**
     * Returns a map with all IndicatorType identifier and name entries.
     */
    public static Map<Object, String> getIndicatorTypeMap()
    {
        return indicatorTypeMap.get() != null ? new HashMap<Object, String>( indicatorTypeMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // DataDictionary
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataDictionary identifier as key and name as value.
     */
    public static void addDataDictionaryMapping( Object dictionaryId, String dictionaryName )
    {
        put( dataDictionaryMap, dictionaryId, dictionaryName );
    }
    
    /**
     * Returns a map with all DataDictionary identifier and name entries.
     */
    public static Map<Object, String> getDataDictionaryMap()
    {
        return dataDictionaryMap.get() != null ? new HashMap<Object, String>( dataDictionaryMap.get() ) : new HashMap<Object, String>();
    }
    
    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with Period identifier as key and the period as value.
     */
    public static void addPeriodMapping( Object periodId, Period period )
    {
        Map<Object, Period> map = periodMap.get();
        
        if ( map == null )
        {
            map = new HashMap<Object, Period>();
        }
        
        map.put( periodId, period );
        
        periodMap.set( map );
    }
        
    /**
     * Returns a map with all Period identifier and period entries.
     */
    public static Map<Object, Period> getPeriodMap()
    {
        return periodMap.get() != null ? new HashMap<Object, Period>( periodMap.get() ) : new HashMap<Object, Period>();
    }

    // -------------------------------------------------------------------------
    // OnChangePeriod
    // -------------------------------------------------------------------------
    
    /**
     * Adds the on-change period to a set.
     */    
    public static void addOnChangePeriodMapping( Period period )
    {
        Set<Period> set = onChangePeriodSet.get();
        
        if ( set == null )
        {
            set = new HashSet<Period>();
        }
        
        set.add( period );
        
        onChangePeriodSet.set( set );
    }
    
    /**
     * Returns a set with all distinct on-change periods.
     */
    public static Set<Period> getOnChangePeriodSet()
    {
        return onChangePeriodSet.get() != null ? new HashSet<Period>( onChangePeriodSet.get() ) : new HashSet<Period>();
    }
    
    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with DataSet identifier as key and name as value.
     */
    public static void addDataSetMapping( Object dataSetId, String dataSetName )
    {
        put( dataSetMap, dataSetId, dataSetName );
    }
    
    /**
     * Returns a map with all DataSet identifier and name entries.
     */
    public static Map<Object, String> getDataSetMap()
    {
        return dataSetMap.get() != null ? new HashMap<Object, String>( dataSetMap.get() ) : new HashMap<Object, String>();
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with OrganisationUnit identifier as key and name as value.
     */
    public static void addOrganisationUnitMapping( Object organisationUnitId, String organisationUnitName )
    {
        put( organisationUnitMap, organisationUnitId, organisationUnitName );
    }
    
    /**
     * Returns a map with all OrganisationUnit identifier and name entries.
     */
    public static Map<Object, String> getOrganisationUnitMap()
    {
        return organisationUnitMap.get() != null ? new HashMap<Object, String>( organisationUnitMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    /**
     * Adds a map entry with OrganisationUnitGroup identifier as key and name as value.
     */
    public static void addOrganisationUnitGroupMapping( Object groupId, String groupName )
    {
        put( organisationUnitGroupMap, groupId, groupName );
    }
    
    /**
     * Returns a map with all OrganisationUnitGroup identifier and name entries.
     */
    public static Map<Object, String> getOrganisationUnitGroupMap()
    {
        return organisationUnitGroupMap.get() != null ? new HashMap<Object, String>( organisationUnitGroupMap.get() ) : new HashMap<Object, String>();
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSet
    // -------------------------------------------------------------------------
    
    /**
     * Adds a map entry with OrganisationUnitGroupSet identifier as key and name as value.
     */
    public static void addGroupSetMapping( Object groupSetId, String groupSetName )
    {
        put( organisationUnitGroupSetMap, groupSetId, groupSetName );
    }
        
    /**
     * Returns a map with all OrganisationUnitGroupSet identifier and name entries.
     */
    public static Map<Object, String> getGroupSetMap()
    {
        return organisationUnitGroupSetMap.get() != null ? new HashMap<Object, String>( organisationUnitGroupSetMap.get() ) : new HashMap<Object, String>();
    }

    // -------------------------------------------------------------------------
    // ReportTable
    // -------------------------------------------------------------------------
    
    /**
     * Adds a map entry with ReportTable identifier as key and name as value.
     */
    public static void addReportTableMapping( Object reportTableId, String reportTableName )
    {
        put( reportTableMap, reportTableId, reportTableName );
    }
    
    /**
     * Returns a map with all ReportTable identifier and name entries.
     */
    public static Map<Object, String> getReportTableMap()
    {
        return reportTableMap.get() != null ? new HashMap<Object, String>( reportTableMap.get() ) : new HashMap<Object, String>();
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private static void put( ThreadLocal<Map<Object, String>> threadLocal, Object key, String value )
    {
        Map<Object, String> map = threadLocal.get();
        
        if ( map == null )
        {
            map = new HashMap<Object, String>();
        }
        
        if ( !map.containsKey( key ) )
        {
            map.put( key, value );
        
            threadLocal.set( map );
        }
    }
}
