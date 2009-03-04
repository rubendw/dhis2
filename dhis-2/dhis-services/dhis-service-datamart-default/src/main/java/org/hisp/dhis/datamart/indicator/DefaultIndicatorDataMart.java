package org.hisp.dhis.datamart.indicator;

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

import static org.hisp.dhis.datamart.util.ParserUtil.generateExpression;
import static org.hisp.dhis.system.util.DateUtils.DAYS_IN_YEAR;
import static org.hisp.dhis.system.util.MathUtils.calculateExpression;
import static org.hisp.dhis.system.util.MathUtils.getRounded;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.AggregatedIndicatorValueBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.aggregation.dataelement.DataElementAggregator;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.DateUtils;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultIndicatorDataMart.java 5519 2008-08-05 09:00:31Z larshelg $
 */
public class DefaultIndicatorDataMart
    implements IndicatorDataMart
{
    private static final int DECIMALS = 1;
    
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }

    private DataElementAggregator sumIntAggregator;

    public void setSumIntAggregator( DataElementAggregator sumIntDataElementAggregator )
    {
        this.sumIntAggregator = sumIntDataElementAggregator;
    }

    private DataElementAggregator averageIntAggregator;

    public void setAverageIntAggregator( DataElementAggregator averageIntDataElementAggregator )
    {
        this.averageIntAggregator = averageIntDataElementAggregator;
    }

    // -------------------------------------------------------------------------
    // IndicatorDataMart implementation
    // -------------------------------------------------------------------------
    
    public int exportIndicatorValues( Collection<Integer> indicatorIds, Collection<Integer> periodIds, 
        Collection<Integer> organisationUnitIds, Collection<Operand> operands )
    {        
        Collection<Indicator> indicators = getIndicators( indicatorIds );
        
        Collection<Period> periods = getPeriods( periodIds );

        Collection<OrganisationUnit> organisationUnits = getOrganisationUnits( organisationUnitIds );

        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( AggregatedIndicatorValueBatchHandler.class );

        batchHandler.init();
        
        int count = 0;
        
        for ( OrganisationUnit unit : organisationUnits )
        {
            int level = organisationUnitService.getLevelOfOrganisationUnit( unit );
            
            for ( Period period : periods )
            {
                Map<Operand, Double> sumIntValueMap = sumIntAggregator.getAggregatedValues( operands, period, unit );
                
                Map<Operand, Double> averageIntValueMap = averageIntAggregator.getAggregatedValues( operands, period, unit );
                
                Map<String, Map<Operand, Double>> valueMapMap = new HashMap<String, Map<Operand, Double>>();
                
                valueMapMap.put( DataElement.AGGREGATION_OPERATOR_SUM, sumIntValueMap );
                valueMapMap.put( DataElement.AGGREGATION_OPERATOR_AVERAGE, averageIntValueMap );

                PeriodType periodType = period.getPeriodType();
                
                for ( Indicator indicator : indicators )
                {
                    // ---------------------------------------------------------
                    // Numerator
                    // ---------------------------------------------------------

                    Map<Operand, Double> numeratorValueMap = valueMapMap.get( indicator.getNumeratorAggregationOperator() );
                    
                    double numeratorValue = calculateExpression( generateExpression( indicator.getNumerator(), numeratorValueMap ) );
                    
                    // ---------------------------------------------------------
                    // Denominator
                    // ---------------------------------------------------------

                    Map<Operand, Double> denominatorValueMap = valueMapMap.get( indicator.getDenominatorAggregationOperator() );
                    
                    double denominatorValue = calculateExpression( generateExpression( indicator.getDenominator(), denominatorValueMap ) );

                    // ---------------------------------------------------------
                    // AggregatedIndicatorValue
                    // ---------------------------------------------------------

                    if ( denominatorValue != 0 )
                    {
                        double annualizationFactor = getAnnualizationFactor( indicator, period );
                        
                        double factor = indicator.getIndicatorType().getFactor();
                        
                        double aggregatedValue = ( numeratorValue / denominatorValue ) * factor * annualizationFactor;
                        
                        double annualizedFactor = factor * annualizationFactor;

                        AggregatedIndicatorValue indicatorValue = new AggregatedIndicatorValue();
                        
                        indicatorValue.setIndicatorId( indicator.getId() );
                        indicatorValue.setPeriodId( period.getId() );
                        indicatorValue.setPeriodTypeId( periodType.getId() );
                        indicatorValue.setOrganisationUnitId( unit.getId() );
                        indicatorValue.setLevel( level );
                        indicatorValue.setAnnualized( getAnnualizationString( indicator.getAnnualized() ) );
                        indicatorValue.setFactor( annualizedFactor );
                        indicatorValue.setValue( getRounded( aggregatedValue, DECIMALS ) );
                        indicatorValue.setNumeratorValue( getRounded( numeratorValue, DECIMALS ) );
                        indicatorValue.setDenominatorValue( getRounded( denominatorValue, DECIMALS ) );
                        
                        batchHandler.addObject( indicatorValue );
                        
                        count++;
                    }
                }
            }
        }
        
        batchHandler.flush();
        
        return count;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private double getAnnualizationFactor( Indicator indicator, Period period )
    {
        double factor = 1.0;
        
        if ( indicator.getAnnualized() != null && indicator.getAnnualized() )
        {
            int daysInPeriod = DateUtils.daysBetween( period.getStartDate(), period.getEndDate() ) + 1;
            
            factor = DAYS_IN_YEAR / daysInPeriod;
        }
        
        return factor;
    }
    
    private String getAnnualizationString( Boolean annualized )
    {
        return ( annualized == null || !annualized ) ? FALSE : TRUE;
    }
    
    // -------------------------------------------------------------------------
    // Id-to-object methods
    // -------------------------------------------------------------------------

    private Collection<Indicator> getIndicators( Collection<Integer> indicatorIds )
    {
        Set<Indicator> indicators = new HashSet<Indicator>();
        
        for ( Integer id : indicatorIds )
        {
            indicators.add( indicatorService.getIndicator( id ) );
        }
        
        return indicators;
    }
    
    private Collection<Period> getPeriods( Collection<Integer> periodIds )
    {
        Set<Period> periods = new HashSet<Period>();
        
        for ( Integer id : periodIds )
        {
            periods.add( periodService.getPeriod( id ) );
        }
        
        return periods;
    }
    
    private Collection<OrganisationUnit> getOrganisationUnits( Collection<Integer> organisationUnitIds )
    {
        Set<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();
        
        for ( Integer id : organisationUnitIds )
        {
            organisationUnits.add( organisationUnitService.getOrganisationUnit( id ) );
        }
        
        return organisationUnits;
    }
}
