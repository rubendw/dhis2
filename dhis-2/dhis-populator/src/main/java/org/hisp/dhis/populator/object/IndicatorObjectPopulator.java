package org.hisp.dhis.populator.object;

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
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;

/**
 * @author Oyvind Brucker
 * @rule id = name; alternativeName; shortName; code; description;
 *       indicatorType; numerator; numeratorDescription;
 *       numeratorAggregationOperator; denominator; denominatorDescription;
 *       denominatorAggregationOperator
 */
public class IndicatorObjectPopulator
    extends ObjectPopulator
{
    private static final Log LOG = LogFactory.getLog( IndicatorObjectPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    private ObjectPopulator indicatorTypeObjectPopulator;

    public void setIndicatorTypeObjectPopulator( IndicatorTypeObjectPopulator indicatorTypeObjectPopulator )
    {
        this.indicatorTypeObjectPopulator = indicatorTypeObjectPopulator;
    }

    private ObjectPopulator dataElementObjectPopulator;

    public void setDataElementObjectPopulator( ObjectPopulator dataElementObjectPopulator )
    {
        this.dataElementObjectPopulator = dataElementObjectPopulator;
    }

    // -------------------------------------------------------------------------
    // ObjectPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String id = PopulatorUtils.getRuleId( rule );

        List<String> arguments = PopulatorUtils.getRuleArguments( rule );

        if ( arguments.size() != 12 )
        {
            throw new PopulatorException( "Wrong number of arguments, must be 12 {" + rule + "}" );
        }

        String name = arguments.get( 0 );
        String alternativeName = arguments.get( 1 );
        String shortName = arguments.get( 2 );
        String code = arguments.get( 3 );
        String description = arguments.get( 4 );
        String indicatorTypeIdString = arguments.get( 5 );
        String numerator = arguments.get( 6 );
        String numeratorDescription = arguments.get( 7 );
        String numeratorOperatorString = arguments.get( 8 );
        String denominator = arguments.get( 9 );
        String denominatorDescription = arguments.get( 10 );
        String denominatorOperatorString = arguments.get( 11 );
        int internalId;

        int indicatorTypeId = indicatorTypeObjectPopulator.getInternalId( indicatorTypeIdString );

        IndicatorType indicatorType = indicatorService.getIndicatorType( indicatorTypeId );

        numerator = replacePopulatorIds( numerator );

        String numeratorOperator = getAggregationOperator( numeratorOperatorString );

        denominator = replacePopulatorIds( denominator );

        String denominatorOperator = getAggregationOperator( denominatorOperatorString );

        Indicator indicator = new Indicator( name, alternativeName, shortName, code, description, false, indicatorType,
            numerator, numeratorDescription, numeratorOperator, denominator, denominatorDescription,
            denominatorOperator );

        LOG.debug( "Adding indicator: " + name + ", " + alternativeName + ", " + shortName + ", " + code + ", "
            + description + ", " + "Indicatortype: " + indicatorType.getName() + ", " + numerator + ", "
            + numeratorDescription + ", " + numeratorOperator + ", " + denominator + ", " + denominatorDescription
            + ", " + denominatorOperator + ", " );

        internalId = indicatorService.addIndicator( indicator );

        addIdMapping( id, internalId );
    }

    // -------------------------------------------------------------------------
    // Util methods
    // -------------------------------------------------------------------------

    /**
     * Replaces the populator id (ie: $de1) with the "real" internal id
     * 
     * @param xator the numerator or denominator formula to process
     * @return the modified string
     * @throws PopulatorException if the dataelement cant be resolved
     */
    private String replacePopulatorIds( String xator )
        throws PopulatorException
    {
        ArrayList<String> ids = new ArrayList<String>();

        StringTokenizer stringTokenizer = new StringTokenizer( xator, "[" );

        while ( stringTokenizer.hasMoreTokens() )
        {
            String token = stringTokenizer.nextToken();
            if ( token.startsWith( "$" ) )
            {
                int endPos = token.indexOf( "]" );
                if ( endPos != -1 )
                {
                    token = token.substring( 0, endPos );
                }
                if ( !ids.contains( token ) )
                {
                    ids.add( token );
                }
            }

        }

        for ( String id : ids )
        {
            int dataElementId = dataElementObjectPopulator.getInternalId( id );
            // Need to escape the first character in the regex ($)
            xator = xator.replaceAll( "\\" + id, String.valueOf( dataElementId ) );
        }

        return xator;
    }

    private String getAggregationOperator( String operatorString )
        throws PopulatorException
    {
        if ( operatorString.equals( "sum" ) )
        {
            return DataElement.AGGREGATION_OPERATOR_SUM;
        }
        else if ( operatorString.equals( "average" ) )
        {
            return DataElement.AGGREGATION_OPERATOR_AVERAGE;
        }
        else if ( operatorString.equals( "count" ) )
        {
            return DataElement.AGGREGATION_OPERATOR_COUNT;
        }
        else
        {
            throw new PopulatorException( "Invalid aggregation operator: " + operatorString
                + ", valid operators are SUM and AVERAGE" );
        }
    }
}
