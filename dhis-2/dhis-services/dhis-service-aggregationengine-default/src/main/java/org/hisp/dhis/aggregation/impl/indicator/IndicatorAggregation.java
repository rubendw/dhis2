package org.hisp.dhis.aggregation.impl.indicator;

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

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.aggregation.impl.cache.AggregationCache;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;

import static org.hisp.dhis.expression.Expression.SEPARATOR;
import static org.hisp.dhis.system.util.DateUtils.DAYS_IN_YEAR;
import static org.hisp.dhis.system.util.DateUtils.getDays;
import static org.hisp.dhis.system.util.MathUtils.calculateExpression;
import static org.hisp.dhis.system.util.MathUtils.INVALID;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorAggregation.java 5280 2008-05-28 10:10:29Z larshelg $
 */
public class IndicatorAggregation
{
    private static final String NULL_REPLACEMENT = "0";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private AggregationCache aggregationCache;

    public void setAggregationCache( AggregationCache aggregationCache )
    {
        this.aggregationCache = aggregationCache;
    }    
    
    private DataElementService dataElementService;
    
    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;
    
    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }
    
    // -------------------------------------------------------------------------
    // Indicator aggregation
    // -------------------------------------------------------------------------
    
    public double getAggregatedIndicatorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        double numeratorValue = calculateExpression( generateExpression( indicator.getNumerator(), startDate,
            endDate, organisationUnit ) );
        
        double denominatorValue = calculateExpression( generateExpression( indicator.getDenominator(),
            startDate, endDate, organisationUnit ) );
        
        if ( numeratorValue == INVALID || denominatorValue == INVALID || denominatorValue == 0.0 )
        {
            return AggregationService.NO_VALUES_REGISTERED;
        }
        else
        {
            int factor = indicator.getIndicatorType().getFactor();

            double annualizationFactor = getAnnualizationFactor( indicator, startDate, endDate );
            
            double aggregatedValue = ( numeratorValue / denominatorValue ) * factor * annualizationFactor;

            return aggregatedValue;
        }
    }

    public double getAggregatedNumeratorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        return calculateExpression( generateExpression( indicator.getNumerator(), startDate,
            endDate, organisationUnit ) );
    }

    public double getAggregatedDenominatorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        return calculateExpression( generateExpression( indicator.getDenominator(),
            startDate, endDate, organisationUnit ) );
    }

    // -------------------------------------------------------------------------
    // Supportive methods for Indicator aggregation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns the annualized value.
     */
    private double getAnnualizationFactor( Indicator indicator, Date startDate, Date endDate )
    {
        double factor = 1.0;
        
        if ( indicator.getAnnualized() != null && indicator.getAnnualized() )
        {
            long days = getDays( startDate, endDate ) + 1;
            
            factor = DAYS_IN_YEAR / days;
        }
        
        return factor;
    }
    
    /**
     * Converts an expression on the form<br>
     * [34] + [23], where the numbers are IDs of DataElements, to the form<br>
     * 200 + 450, where the numbers are the values of the DataValues registered
     * for the Period and source.
     * 
     * @return The generated expression
     */
    private String generateExpression( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit )
    {    	
        try
        {        	
        	Pattern pattern = Pattern.compile( "(\\[\\d+\\.\\d+\\])" );
            
            Matcher matcher = pattern.matcher( formula );
            StringBuffer buffer = new StringBuffer();            
            
            while ( matcher.find() )
            {
                String replaceString = matcher.group();
                
                replaceString = replaceString.replaceAll( "[\\[\\]]", "" );

                String dataElementIdString = replaceString.substring( 0, replaceString.indexOf( SEPARATOR ) );                
                String optionComboIdString = replaceString.substring( replaceString.indexOf( SEPARATOR ) + 1, replaceString.length() );
                
                int dataElementId = Integer.parseInt( dataElementIdString );
                int optionComboId = Integer.parseInt( optionComboIdString );             
                
                DataElement dataElement = dataElementService.getDataElement( dataElementId );
                
                DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId );

                double aggregatedValue = aggregationCache.getAggregatedDataValue( dataElement, optionCombo, startDate, endDate, organisationUnit );                
                
                if ( aggregatedValue == AggregationService.NO_VALUES_REGISTERED )
                {
                    replaceString = NULL_REPLACEMENT;
                }
                else
                {
                    replaceString = String.valueOf( aggregatedValue );
                }

                matcher.appendReplacement( buffer, replaceString );
            }

            matcher.appendTail( buffer );

            return buffer.toString();
        }
        catch ( NumberFormatException ex )
        {
            throw new RuntimeException( "Illegal DataElement id", ex );
        }
    }
}
