package org.hisp.dhis.aggregation.impl;

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

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.aggregation.impl.dataelement.AbstractDataElementAggregation;
import org.hisp.dhis.aggregation.impl.indicator.IndicatorAggregation;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultAggregationService.java 5116 2008-05-08 10:51:21Z larshelg $
 */
public class DefaultAggregationService
    implements AggregationService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private AbstractDataElementAggregation sumIntDataElementAggregation;

    public void setSumIntDataElementAggregation( AbstractDataElementAggregation sumIntDataElementAggregation )
    {
        this.sumIntDataElementAggregation = sumIntDataElementAggregation;
    }

    private AbstractDataElementAggregation sumBoolDataElementAggregation;

    public void setSumBoolDataElementAggregation( AbstractDataElementAggregation sumBoolDataElementAggregation )
    {
        this.sumBoolDataElementAggregation = sumBoolDataElementAggregation;
    }

    private AbstractDataElementAggregation averageIntDataElementAggregation;

    public void setAverageIntDataElementAggregation( AbstractDataElementAggregation averageIntDataElementAggregation )
    {
        this.averageIntDataElementAggregation = averageIntDataElementAggregation;
    }

    private AbstractDataElementAggregation averageBoolDataElementAggregation;

    public void setAverageBoolDataElementAggregation( AbstractDataElementAggregation averageBoolDataElementAggregation )
    {
        this.averageBoolDataElementAggregation = averageBoolDataElementAggregation;
    }
    
    private IndicatorAggregation indicatorAggregation;

    public void setIndicatorAggregation( IndicatorAggregation indicatorAggregation )
    {
        this.indicatorAggregation = indicatorAggregation;
    }
    
    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    public double getAggregatedDataValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        AbstractDataElementAggregation dataElementAggregation = 
            getInstance( dataElement.getType(), dataElement.getAggregationOperator() );        

        return dataElementAggregation.getAggregatedValue( dataElement, optionCombo, startDate, endDate, organisationUnit );
    }

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    public double getAggregatedIndicatorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        return indicatorAggregation.getAggregatedIndicatorValue( indicator, startDate, endDate, organisationUnit );
    }

    public double getAggregatedNumeratorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        return indicatorAggregation.getAggregatedNumeratorValue( indicator, startDate, endDate, organisationUnit );
    }

    public double getAggregatedDenominatorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit )
    {
        return indicatorAggregation.getAggregatedDenominatorValue( indicator, startDate, endDate, organisationUnit );
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private AbstractDataElementAggregation getInstance( String type, String aggregationOperator )
    {
        if ( type.equals( DataElement.TYPE_INT )
            && aggregationOperator.equals( DataElement.AGGREGATION_OPERATOR_SUM ) )
        {
            return sumIntDataElementAggregation;
        }
        else if ( type.equals( DataElement.TYPE_BOOL )
            && aggregationOperator.equals( DataElement.AGGREGATION_OPERATOR_SUM ) )
        {
            return sumBoolDataElementAggregation;
        }
        else if ( type.equals( DataElement.TYPE_INT )
            && aggregationOperator.equals( DataElement.AGGREGATION_OPERATOR_AVERAGE ) )
        {
            return averageIntDataElementAggregation;
        }
        else if ( type.equals( DataElement.TYPE_BOOL )
            && aggregationOperator.equals( DataElement.AGGREGATION_OPERATOR_AVERAGE ) )
        {
            return averageBoolDataElementAggregation;
        }
        else
        {
            throw new IllegalArgumentException( "Unsupported aggregation operator ("
                + aggregationOperator + ") or data element type (" + type + ")" );
        }
    }
}
