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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ValidateIndicatorAction.java 4015 2007-11-15 14:46:04Z larshelg $
 */
public class ValidateIndicatorAction
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
    
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

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
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        if ( name == null )
        {
            message = i18n.getString( "specify_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "specify_name" );

                return INPUT;
            }

            Indicator match = indicatorService.getIndicatorByName( name );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }
        }

        if ( shortName == null )
        {
            message = i18n.getString( "specify_short_name" );

            return INPUT;
        }
        else
        {
            shortName = shortName.trim();

            if ( shortName.length() == 0 )
            {
                message = i18n.getString( "specify_short_name" );

                return INPUT;
            }

            Indicator match = indicatorService.getIndicatorByShortName( shortName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "short_name_in_use" );

                return INPUT;
            }
        }

        if ( alternativeName != null && alternativeName.trim().length() != 0 )
        {
            Indicator match = indicatorService.getIndicatorByAlternativeName( alternativeName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "alternative_name_in_use" );

                return INPUT;
            }
        }

        if ( code != null && code.trim().length() != 0 )
        {
            Indicator match = indicatorService.getIndicatorByCode( code );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "code_in_use" );

                return INPUT;
            }
        }

        if ( indicatorTypeId == null )
        {
            message = i18n.getString( "choose_indicator_type" );

            return INPUT;
        }
        
        if ( numerator == null )
        {
            message = i18n.getString( "specify_numerator" );
        }
        
        if ( numeratorDescription == null )
        {
            message = i18n.getString( "specify_numerator_description" );
        }
        
        if ( numeratorAggregationOperator == null )
        {
            message = i18n.getString( "specify_numerator_agg_operator" );
        }
        
        if ( denominator == null )
        {
            message = i18n.getString( "specify_denominator" );
        }
        
        if ( denominatorDescription == null )
        {
            message = i18n.getString( "specify_denominator_description" );
        }
        
        if ( denominatorAggregationOperator == null )
        {
            message = i18n.getString( "specify_denominator_agg_operator" );
        }

        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }

}
