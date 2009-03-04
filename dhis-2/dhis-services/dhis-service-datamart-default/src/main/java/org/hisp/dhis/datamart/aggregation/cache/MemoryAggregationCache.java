package org.hisp.dhis.datamart.aggregation.cache;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;

/**
 * @author Lars Helge Overland
 * @version $Id: MemoryAggregationCache.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class MemoryAggregationCache
    implements AggregationCache
{
    private static final String SEPARATOR = "-";
    
    // -------------------------------------------------------------------------
    // Cache
    // -------------------------------------------------------------------------

    private ThreadLocal<OrganisationUnitHierarchy> latestHierarchyCache = new ThreadLocal<OrganisationUnitHierarchy>();
    
    private ThreadLocal<Map<String, Collection<OrganisationUnitHierarchy>>> hierarchyCache = new ThreadLocal<Map<String,Collection<OrganisationUnitHierarchy>>>();
    
    private ThreadLocal<Map<String, Collection<Integer>>> childrenCache = new ThreadLocal<Map<String,Collection<Integer>>>();
    
    private ThreadLocal<Map<String, Collection<Period>>> intersectingPeriodCache = new ThreadLocal<Map<String,Collection<Period>>>();

    private ThreadLocal<Map<String, Period>> periodCache = new ThreadLocal<Map<String,Period>>();
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;
    
    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private PeriodService periodService;
    
    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    // -------------------------------------------------------------------------
    // AggregationCache implementation
    // -------------------------------------------------------------------------

    public OrganisationUnitHierarchy getLatestOrganisationUnitHierarchy()
    {
        OrganisationUnitHierarchy hierarchy = latestHierarchyCache.get();
        
        if ( hierarchy != null )
        {
            return hierarchy;
        }
        
        hierarchy = organisationUnitService.getLatestOrganisationUnitHierarchy();
        
        latestHierarchyCache.set( hierarchy );
        
        return hierarchy;
    }
    
    public Collection<OrganisationUnitHierarchy> getOrganisationUnitHierarchies( Date startDate, Date endDate )
    {
        String key = startDate.toString() + SEPARATOR + endDate.toString();
        
        Map<String, Collection<OrganisationUnitHierarchy>> cache = hierarchyCache.get();
        
        Collection<OrganisationUnitHierarchy> hierarchies = null;
        
        if ( cache != null && ( hierarchies = cache.get( key ) ) != null )
        {
            return hierarchies;
        }
        
        hierarchies = organisationUnitService.getOrganisationUnitHierarchies( startDate, endDate );
        
        cache = ( cache == null ) ? new HashMap<String, Collection<OrganisationUnitHierarchy>>() : cache;
        
        cache.put( key, hierarchies );
        
        hierarchyCache.set( cache );
        
        return hierarchies;
    }
    
    public Collection<Integer> getChildren( OrganisationUnitHierarchy hierarchy, int parentId )
    {
        String key = hierarchy.getId() + SEPARATOR + parentId;
        
        Map<String, Collection<Integer>> cache = childrenCache.get();
        
        Collection<Integer> children = null;
        
        if ( cache != null && ( children = cache.get( key ) ) != null )
        {
            return children;
        }
        
        children = organisationUnitService.getChildren( hierarchy, parentId );
        
        cache = ( cache == null ) ? new HashMap<String, Collection<Integer>>() : cache;
        
        cache.put( key, children );
        
        childrenCache.set( cache );
        
        return children;
    }
    
    public Collection<Period> getIntersectingPeriods( Date startDate, Date endDate )
    {
        String key = startDate.toString() + SEPARATOR + endDate.toString();
        
        Map<String, Collection<Period>> cache = intersectingPeriodCache.get();
        
        Collection<Period> periods = null;
        
        if ( cache != null && ( periods = cache.get( key ) ) != null )
        {
            return periods;
        }
        
        periods = periodService.getIntersectingPeriods( startDate, endDate );
        
        cache = ( cache == null ) ? new HashMap<String, Collection<Period>>() : cache;
        
        cache.put( key, periods );
        
        intersectingPeriodCache.set( cache );
        
        return periods;
    }
    
    public Period getPeriod( int id )
    {
        String key = String.valueOf( id );
        
        Map<String, Period> cache = periodCache.get();
        
        Period period = null;
        
        if ( cache != null && ( period = cache.get( key ) ) != null )
        {
            return period;
        }
        
        period = periodService.getPeriod( id );
        
        cache = ( cache == null ) ? new HashMap<String, Period>() : cache;
        
        cache.put( key, period );
        
        periodCache.set( cache );
        
        return period;
    }
    
    public void clearCache()
    {
        hierarchyCache.remove();
        
        childrenCache.remove();
        
        intersectingPeriodCache.remove();
    }
}
