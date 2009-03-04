package org.hisp.dhis.dd.action.indicator;

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

import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AddIndicatorAction.java 5164 2008-05-16 12:23:58Z larshelg $
 */
public class AddIndicatorAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }    
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String alternativeName;

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }

    private String code;

    public void setCode( String code )
    {
        this.code = code;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }
    
    private boolean annualized;

    public void setAnnualized( boolean annualized )
    {
        this.annualized = annualized;
    }

    private Integer indicatorTypeId;

    public void setIndicatorTypeId( Integer indicatorTypeId )
    {
        this.indicatorTypeId = indicatorTypeId;
    }
    
    private String numerator;

    public void setNumerator( String numerator )
    {
        this.numerator = numerator;
    }
    
    private String numeratorDescription;

    public void setNumeratorDescription( String numeratorDescription )
    {
        this.numeratorDescription = numeratorDescription;
    }

    private String numeratorAggregationOperator;

    public void setNumeratorAggregationOperator( String numeratorAggregationOperator )
    {
        this.numeratorAggregationOperator = numeratorAggregationOperator;
    }

    private String denominator;

    public void setDenominator( String denominator )
    {
        this.denominator = denominator;
    }

    private String denominatorDescription;

    public void setDenominatorDescription( String denominatorDescription )
    {
        this.denominatorDescription = denominatorDescription;
    }

    private String denominatorAggregationOperator;

    public void setDenominatorAggregationOperator( String denominatorAggregationOperator )
    {
        this.denominatorAggregationOperator = denominatorAggregationOperator;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        IndicatorType indicatorType = indicatorService.getIndicatorType( indicatorTypeId );

        if ( alternativeName != null && alternativeName.trim().length() == 0 )
        {
            alternativeName = null;
        }

        if ( code != null && code.trim().length() == 0 )
        {
            code = null;
        }
        
        if ( description != null && description.trim().length() == 0 )
        {
            description = null;
        }
        
        numerator = expressionService.replaceCDEsWithTheirExpression( numerator );
        
        denominator = expressionService.replaceCDEsWithTheirExpression( denominator );        
        
        Indicator indicator = new Indicator();
        
        indicator.setName( name );
        indicator.setAlternativeName( alternativeName );
        indicator.setShortName( shortName );
        indicator.setCode( code );
        indicator.setDescription( description );
        indicator.setAnnualized( annualized );
        indicator.setIndicatorType( indicatorType );
        indicator.setNumerator( numerator );
        indicator.setNumeratorDescription( numeratorDescription );
        indicator.setNumeratorAggregationOperator( numeratorAggregationOperator );
        indicator.setDenominator( denominator );
        indicator.setDenominatorDescription( denominatorDescription );
        indicator.setDenominatorAggregationOperator( denominatorAggregationOperator );
        
        indicatorService.addIndicator( indicator );

        return SUCCESS;
    }
}
