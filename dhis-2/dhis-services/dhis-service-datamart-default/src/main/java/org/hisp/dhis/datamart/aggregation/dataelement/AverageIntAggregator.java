package org.hisp.dhis.datamart.aggregation.dataelement;

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

import static org.hisp.dhis.system.util.MathUtils.getDays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.CrossTabDataValue;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: AverageIntAggregator.java 5516 2008-08-04 12:42:05Z larshelg $
 */
public class AverageIntAggregator
    extends DataElementAggregator
{
    private static final Log log = LogFactory.getLog( AverageIntAggregator.class );
    
    public Map<Operand, Double> getAggregatedValues( Collection<Operand> operands, Period period, OrganisationUnit unit )
    {
        OrganisationUnitHierarchy hierarchy = aggregationCache.getLatestOrganisationUnitHierarchy();
        
        Collection<Integer> unitIds = aggregationCache.getChildren( hierarchy, unit.getId() );
        
        Map<Operand, Double> values = new HashMap<Operand, Double>(); // <Operand, total value>
        
        for ( Integer unitId : unitIds )
        {
            Collection<CrossTabDataValue> crossTabValues = 
                getCrossTabDataValues( operands, period.getStartDate(), period.getEndDate(), unitId, hierarchy );
            
            Map<Operand, Double[]> entries = getAggregate( crossTabValues, period.getStartDate(), 
                period.getEndDate(), period.getStartDate(), period.getEndDate() ); // <Operand, [total value, total relevant days]>
            
            for ( Entry<Operand, Double[]> entry : entries.entrySet() ) 
            {
                if ( entry.getValue() != null && entry.getValue()[ 1 ] > 0 )
                {
                    double average = entry.getValue()[ 0 ] / entry.getValue()[ 1 ];
                    
                    double existingAverage = values.containsKey( entry.getKey() ) ? values.get( entry.getKey() ) : 0;
                    
                    values.put( entry.getKey(), average + existingAverage );
                }
            }
        }  
        
        return values;
    }
    
    protected Collection<CrossTabDataValue> getCrossTabDataValues( 
        Collection<Operand> operands, Date startDate, Date endDate, int parentId, OrganisationUnitHierarchy hierarchy )
    {
        Collection<Period> periods = aggregationCache.getIntersectingPeriods( startDate, endDate );
        
        Collection<Integer> periodIds = new ArrayList<Integer>();
        
        for ( Period period : periods )
        {
            periodIds.add( period.getId() );
        }
        
        return dataMartStore.getCrossTabDataValues( operands, periodIds, parentId );
    }
    
    protected Map<Operand, Double[]> getAggregate( 
        Collection<CrossTabDataValue> crossTabValues, Date startDate, Date endDate, Date aggregationStartDate, Date aggregationEndDate )
    {
        Map<Operand, Double[]> totalSums = new HashMap<Operand, Double[]>(); // <Operand, [total value, total relevant days]>
        
        for ( CrossTabDataValue crossTabValue : crossTabValues )
        {
            Period period = aggregationCache.getPeriod( crossTabValue.getPeriodId() );
            
            Date currentStartDate = period.getStartDate();
            Date currentEndDate = period.getEndDate();
            
            for ( Entry<Operand, String> entry : crossTabValue.getValueMap().entrySet() ) // <Operand, value>
            {
                if ( entry.getValue() != null )
                {
                    double value = 0;
                    
                    try
                    {
                        value = Double.parseDouble( entry.getValue() );
                    }
                    catch ( NumberFormatException ex )
                    {
                        log.warn( "Value skipped, not numeric: '" + entry.getValue() + 
                            "', for data element with id: '" + entry.getKey() +
                            "', for period with id: '" + crossTabValue.getPeriodId() +
                            "', for source with id: '" + crossTabValue.getSourceId() + "'" );
                    }
                                        
                    double relevantDays = 0;
                    
                    if ( currentStartDate.compareTo( startDate ) >= 0 && currentEndDate.compareTo( endDate ) <= 0 )
                    {
                        relevantDays = getDays( currentEndDate ) - getDays( currentStartDate );
                    }
                    else if ( currentStartDate.compareTo( startDate ) <= 0 && currentEndDate.compareTo( startDate ) >= 0
                        && currentEndDate.compareTo( endDate ) <= 0 )
                    {
                        relevantDays = getDays( currentEndDate ) - getDays( startDate );
                    }
                    else if ( currentStartDate.compareTo( startDate ) >= 0 && currentStartDate.compareTo( endDate ) <= 0
                        && currentEndDate.compareTo( endDate ) >= 0 )
                    {
                        relevantDays = getDays( endDate ) - getDays( currentStartDate );
                    }
                    else if ( currentStartDate.compareTo( startDate ) <= 0 && currentEndDate.compareTo( endDate ) >= 0 )
                    {
                        relevantDays = getDays( endDate ) - getDays( startDate );
                    }
                    
                    value = value * relevantDays;
                    
                    double existingValue = totalSums.containsKey( entry.getKey() ) ? totalSums.get( entry.getKey() )[ 0 ] : 0;
                    double existingRelevantDays = totalSums.containsKey( entry.getKey() ) ? totalSums.get( entry.getKey() )[ 1 ] : 0;
                    
                    Double[] values = { ( value + existingValue ), ( relevantDays + existingRelevantDays ) };
                    
                    totalSums.put( entry.getKey(), values );
                }
            }
        }                    
        
        return totalSums;
    }
}