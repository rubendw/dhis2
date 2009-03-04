package org.hisp.dhis.aggregation.impl.cache;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.organisationunit.OrganisationUnitStore;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;

/**
 * @author Lars Helge Overland
 * @version $Id: MemoryAggregationCache.java 5280 2008-05-28 10:10:29Z larshelg $
 */
public class MemoryAggregationCache
    implements AggregationCache
{
    private Map<String, Collection<Integer>> childrenCache = new HashMap<String, Collection<Integer>>();

    private Map<String, Collection<OrganisationUnitHierarchy>> hierarchyCache = new HashMap<String, Collection<OrganisationUnitHierarchy>>();
    
    private Map<String, Period> periodCache = new HashMap<String, Period>();
    
    private Map<String, Collection<Integer>> periodIdCache = new HashMap<String, Collection<Integer>>();
    
    private static final String SEPARATOR = "-";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitStore organisationUnitStore;

    public void setOrganisationUnitStore( OrganisationUnitStore organisationUnitStore )
    {
        this.organisationUnitStore = organisationUnitStore;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private AggregationService aggregationService;

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    // -------------------------------------------------------------------------
    // AggregationCache implementation
    // -------------------------------------------------------------------------

    public Collection<Integer> getChildren( OrganisationUnitHierarchy hierarchy, int parentId )
    {
        String key = hierarchy.getId() + SEPARATOR + parentId;

        Collection<Integer> children = childrenCache.get( key );

        if ( children != null )
        {
            return children;
        }

        children = organisationUnitStore.getChildren( hierarchy, parentId );
        
        childrenCache.put( key, children );
        
        return children;
    }

    public Collection<OrganisationUnitHierarchy> getOrganisationUnitHierarchies( Date startDate, Date endDate )
    {
        String key = startDate.toString() + SEPARATOR + endDate.toString();

        Collection<OrganisationUnitHierarchy> hierarchies = hierarchyCache.get( key );

        if ( hierarchies != null )
        {
            return hierarchies;
        }

        hierarchies = organisationUnitStore.getOrganisationUnitHierarchies( startDate, endDate );
        
        hierarchyCache.put( key, hierarchies );
        
        return hierarchies;
    }
    
    public Period getPeriod( int periodId )
    {
        String key = String.valueOf( periodId );
        
        Period period = periodCache.get( key );
        
        if ( period != null )
        {
            return period;
        }
        
        period = periodService.getPeriod( periodId );
        
        periodCache.put( key, period );
        
        return period;
    }
    
    public Collection<Integer> getPeriodIds( Date startDate, Date endDate )
    {
        String key = startDate.toString() + SEPARATOR + endDate.toString();
        
        Collection<Integer> periodIds = periodIdCache.get( key );
        
        if ( periodIds != null )
        {
            return periodIds;
        }
        
        Collection<Period> periods = periodService.getIntersectingPeriods( startDate, endDate );
        
        periodIds = new ArrayList<Integer>();
        
        for ( Period period : periods )
        {
            periodIds.add( period.getId() );
        }
        
        periodIdCache.put( key, periodIds );
        
        return periodIds;
    }    
    
    public double getAggregatedDataValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, Date startDate, Date endDate, OrganisationUnit organisationUnit )
    {
        //String key = dataElement.getId() + SEPARATOR + optionCombo.getId() + "-" + startDate.toString() + "-" + endDate.toString() + "-" + organisationUnit.getId();
        
        /*Double value = aggregatedValueCache.get( key );
        
        if ( value != null )
        {
            return value.doubleValue();
        }*/
        
        Double value = aggregationService.getAggregatedDataValue( dataElement, optionCombo, startDate, endDate, organisationUnit );
        
        /*if ( value != AggregationService.NO_VALUES_REGISTERED )
        {
            aggregatedValueCache.put( key, value );
        }*/
        
        return value.doubleValue();
    }
}
