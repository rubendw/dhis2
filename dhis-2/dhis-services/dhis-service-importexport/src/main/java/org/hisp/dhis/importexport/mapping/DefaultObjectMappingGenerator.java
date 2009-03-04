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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataDictionaryBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementCategoryBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementCategoryComboBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementCategoryOptionBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataElementGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataSetBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.GroupSetBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.IndicatorTypeBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.OrganisationUnitBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.OrganisationUnitGroupBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.PeriodBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.ReportTableBatchHandler;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.LoggingHashMap;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultObjectMappingGenerator.java 5824 2008-10-07 18:00:24Z larshelg $
 */
public class DefaultObjectMappingGenerator
    implements ObjectMappingGenerator
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }
    
    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }
    
    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService categoryOptionComboService )
    {
        this.categoryOptionComboService = categoryOptionComboService;
    }

    // -------------------------------------------------------------------------
    // DataElementCategory
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getCategoryMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getCategoryMap(), preview );
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryCombo
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getCategoryComboMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryComboBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getCategoryComboMap(), preview );
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryOption
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getCategoryOptionMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementCategoryOptionBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getCategoryOptionMap(), preview );
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryOptionCombo
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getCategoryOptionComboMapping( boolean preview )
    {
        return getMapping( NameMappingUtil.getCategoryOptionComboMap(), preview );
    }
    
    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getDataElementMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getDataElementMap(), preview );
    }

    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getDataElementGroupMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataElementGroupBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getDataElementGroupMap(), preview );
    }

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getIndicatorMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorBatchHandler.class );
                
        return getMapping( batchHandler, NameMappingUtil.getIndicatorMap(), preview );
    }

    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getIndicatorGroupMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorGroupBatchHandler.class );
                
        return getMapping( batchHandler, NameMappingUtil.getIndicatorGroupMap(), preview );
    }

    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getIndicatorTypeMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( IndicatorTypeBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getIndicatorTypeMap(), preview );
    }

    // -------------------------------------------------------------------------
    // DataDictionary
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getDataDictionaryMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataDictionaryBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getDataDictionaryMap(), preview );
    }
    
    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getDataSetMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataSetBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getDataSetMap(), preview );
    }

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getOrganisationUnitMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitBatchHandler.class );

        return getMapping( batchHandler, NameMappingUtil.getOrganisationUnitMap(), preview );
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getOrganisationUnitGroupMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitGroupBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getOrganisationUnitGroupMap(), preview );
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSet
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getOrganisationUnitGroupSetMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getGroupSetMap(), preview );
    }

    // -------------------------------------------------------------------------
    // ReportTable
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getReportTableMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( ReportTableBatchHandler.class );
        
        return getMapping( batchHandler, NameMappingUtil.getReportTableMap(), preview );
    }
    
    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    public Map<Object, Integer> getPeriodMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
        
        batchHandler.init();
        
        Map<Object, Integer> periodMap = new LoggingHashMap<Object, Integer>();
        
        Map<Object, Period> mapping = NameMappingUtil.getPeriodMap();
        
        if ( mapping != null )
        {
            for ( Map.Entry<Object, Period> map : mapping.entrySet() )
            {
                int identifier = preview ? getKey( map.getKey() ) : batchHandler.getObjectIdentifier( map.getValue() );
                
                verifyIdentifier( identifier, preview, map.getValue().toString() );
                
                periodMap.put( map.getKey(), identifier );
            }
            
            verifyMap( mapping, periodMap );
        }
        
        batchHandler.flush();
        
        return periodMap;
    }

    // -------------------------------------------------------------------------
    // OnChangePeriod
    // -------------------------------------------------------------------------

    public Map<Period, Integer> getOnChangePeriodMapping( boolean preview )
    {
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( PeriodBatchHandler.class );
        
        batchHandler.init();
        
        Map<Period, Integer> onChangePeriodMap = new LoggingHashMap<Period, Integer>();
        
        Set<Period> mapping = NameMappingUtil.getOnChangePeriodSet();
        
        if ( mapping != null )
        {
            for ( Period period : mapping )
            {
                int identifier = preview ? period.getId() : batchHandler.getObjectIdentifier( period );
                
                verifyIdentifier( identifier, preview, period.toString() );
                
                onChangePeriodMap.put( period, identifier );
            }
        }
        
        batchHandler.flush();
        
        return onChangePeriodMap;
    }
    
    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    public Map<String, Integer> getPeriodTypeMapping()
    {
        Map<String, Integer> periodTypeMap = new LoggingHashMap<String, Integer>();
        
        Collection<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        
        for ( PeriodType type : periodTypes )
        {
            periodTypeMap.put( type.getName(), type.getId() );
        }
        
        return periodTypeMap;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Map<Object, Integer> getMapping( BatchHandler batchHandler, Map<Object, String> nameMap, boolean preview )
    {
        batchHandler.init();

        Map<Object, Integer> identifierMap = new LoggingHashMap<Object, Integer>();
        
        if ( nameMap != null )
        {
            for ( Map.Entry<Object, String> nameMapEntry : nameMap.entrySet() )
            {
                int identifier = preview ? getKey( nameMapEntry.getKey() ) : batchHandler.getObjectIdentifier( nameMapEntry.getValue() );
                
                verifyIdentifier( identifier, preview, nameMapEntry.getValue() );

                identifierMap.put( nameMapEntry.getKey(), identifier );
            }
            
            verifyMap( nameMap, identifierMap );
        }
        
        batchHandler.flush();
        
        return identifierMap;
    }
    
    private Map<Object, Integer> getMapping( Map<Object, DataElementCategoryOptionCombo> categoryOptionComboMap, boolean preview )
    {
        Map<Object, Integer> identifierMap = new LoggingHashMap<Object, Integer>();
        
        if ( categoryOptionComboMap != null )
        {
            for ( Map.Entry<Object, DataElementCategoryOptionCombo> map : categoryOptionComboMap.entrySet() )
            {
                int identifier = 0;
                
                if ( preview )
                {
                    identifier = getKey( map.getKey() );
                }
                else
                {
                    DataElementCategoryOptionCombo temp = map.getValue();
                    
                    DataElementCategoryOptionCombo categoryOptionCombo = 
                        categoryOptionComboService.getDataElementCategoryOptionCombo( temp );
                    
                    if ( categoryOptionCombo == null )
                    {
                        throw new RuntimeException( "DataElementCategoryOptionCombo does not exist: " + temp );
                    }
                    
                    identifier = categoryOptionCombo.getId(); 
                }
                
                verifyIdentifier( identifier, preview, "[DataElementCategoryOptionCombo]" );
                
                identifierMap.put( map.getKey(), identifier );
            }
            
            verifyMap( categoryOptionComboMap, identifierMap );
        }
        
        return identifierMap;
    }
    
    private void verifyIdentifier( Integer identifier, boolean preview, String name )
    {
        if ( identifier == 0 && !preview )
        {
            throw new RuntimeException( "The object named '" + name + "' does not exist" );
        }        
    }
    
    private void verifyMap( Map<? extends Object, ? extends Object> nameMap, Map<? extends Object, ? extends Object> identifierMap )
    {
        if ( nameMap.size() != identifierMap.size() )
        {
            throw new RuntimeException( "The name mapping contains duplicate names" );
        }
    }
    
    /**
     * Return the Integer value of the Object argument. If the Object is not of
     * type Integer, -1 is returned.
     */
    private int getKey( Object key )
    {
        int value = -1;
        
        try
        {
            value = Integer.parseInt( String.valueOf( key ) );
        }
        catch ( NumberFormatException ex )
        {
            // Object is not of type Integer
        }
        
        return value;
    }
}
