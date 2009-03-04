package org.hisp.dhis.target;

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

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Alexander Ottesen
 * @version $Id: DefaultTargetService.java 4235 2007-12-06 21:19:08Z alexao $
 */
public class DefaultTargetService
    implements TargetService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private TargetStore targetStore;
    
    public void setTargetStore( TargetStore targetStore )
    {
        this.targetStore = targetStore;
    }
    
    private AggregationService aggregationService;
    
    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }  
    
    // -------------------------------------------------------------------------
    // TargetService implementation
    // -------------------------------------------------------------------------

    public int addTarget( Target target )
    {
        return targetStore.addTarget( target );
    }

    public void addTargets( Collection<Target> targets )
    {
        for ( Target target : targets )
        {
            this.addTarget( target );
        }
    }
    
    public void deleteTarget( Target target )
    {
        targetStore.deleteTarget( target );
    }

    public Target getTarget( int id )
    {
    	return addAggregatedCurrent( targetStore.getTarget( id ) );
    }
    
    public Collection<Target> getAllTargets()
    {
        Collection<Target> targets = targetStore.getAllTargets();
        
        return aggregateCurrent( targets );
    }

    public Collection<Target> getTargets( Indicator indicator, Period period, Source source )
    {
        return targetStore.getTargets( indicator, period, source );
    }

    public Target getTargetByName( String name )
    {
        return addAggregatedCurrent( targetStore.getTargetByName( name ) );
    }

    public Target getTargetByShortName( String shortName )
    {
        return addAggregatedCurrent( targetStore.getTargetByShortName( shortName ) );
    }

    public Collection<Target> getTargets( Indicator indicator )
    {
        return aggregateCurrent( targetStore.getTargets( indicator ) );
    }

    public Collection<Target> getTargets( Period period )
    {
        return aggregateCurrent( targetStore.getTargets( period ) );
    }

    public Collection<Target> getTargets( Source source )
    {    	
        return aggregateCurrent( targetStore.getTargets( source ) );
    }

    public Collection<Target> getTargets( Indicator indicator, Period period )
    {
        return aggregateCurrent( targetStore.getTargets( indicator, period ) );
    }

    public Collection<Target> getTargets( Indicator indicator, Source source )
    {
        return aggregateCurrent( targetStore.getTargets( indicator, source ) );
    }

    public Collection<Target> getTargets( Period period, Source source )
    {
        return aggregateCurrent( targetStore.getTargets( period, source ) );
    }

    public Collection<Target> getTargets( Collection<OrganisationUnit> sources )
    {
        Collection<Target> targets = new ArrayList<Target>();
        
        for ( Source source : sources )
        {        	
            targets.addAll( getTargets( source ) );
        }
        
        return targets;
    }

    public Collection<Target> getTargets( Indicator indicator, Collection<OrganisationUnit> sources )
    {
        Collection<Target> targets = new ArrayList<Target>();
        
        for ( Source source : sources )
        {
            targets.addAll( getTargets( indicator, source ) );
        }
        
        return targets;
    }

    public Collection<Target> getTargets( Period period, Collection<OrganisationUnit> sources )
    {
        Collection<Target> targets = new ArrayList<Target>();
        
        for ( Source source : sources )
        {
            targets.addAll( getTargets( period, source ) );
        }
        
        return targets;
    }

    public Collection<Target> getTargets( Indicator indicator, Period period, Collection<OrganisationUnit> sources )
    {
        Collection<Target> targets = new ArrayList<Target>();
        
        for ( Source source : sources )
        {   
            targets.addAll( getTargets( indicator, period, source ) );
        }
        
        return targets;
    }
    
    public void updateTarget( Target target )
    {
        targetStore.updateTarget( target );
    }
    
    // -------------------------------------------------------------------------
    // Target operations
    // -------------------------------------------------------------------------
    
    public double compareTargetToIndicator( Target target )
    {
        return target.getValue() - target.getCurrentValue();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    /**
     * Aggregates a value for a target's indicator and returns it.
     */
    private double aggregateCurrent( Target target )
    {
        if ( target != null )        	
        {           	
            Indicator indicator = target.getIndicator();
            
            Period period = target.getPeriod();           
            
            OrganisationUnit unit = ( OrganisationUnit ) target.getSource();           
            
            return aggregationService.getAggregatedIndicatorValue( indicator, period.getStartDate(), period.getEndDate(), unit );
        }
        else
        {
            return 0.0; //TODO ?
        }
    }
    
    /**
     * Adds aggregated values to all targets in a set and returns the set.
     */
    private Collection<Target> aggregateCurrent( Collection<Target> targets )
    {
        for ( Target target : targets )
        {
            target.setCurrentValue( aggregateCurrent( target ) );
        }
        
        return targets;
    }
    
    /**
     * Adds an aggregated value to the target.
     */
    private Target addAggregatedCurrent( Target target )
    {
        if ( target != null )
        {
            target.setCurrentValue( aggregateCurrent( target ) );
            
            return target;
        }
        
        return null;
    }
}
