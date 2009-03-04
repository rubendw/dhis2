package org.hisp.dhis.importexport.converter;

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

import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractIndicatorConverter.java 4753 2008-03-14 12:48:50Z larshelg $
 */
public class AbstractIndicatorConverter
    extends AbstractConverter<Indicator>
{
    protected IndicatorService indicatorService;
    
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------
    
    protected void importUnique( Indicator object )
    {
        batchHandler.addObject( object );      
    }
    
    protected void importMatching( Indicator object, Indicator match )
    {
        match.setUuid( object.getUuid() );
        match.setName( object.getName() );
        match.setAlternativeName( object.getAlternativeName() );
        match.setShortName( object.getShortName() );
        match.setCode( object.getCode() );
        match.setDescription( object.getDescription() );
        match.setIndicatorType( object.getIndicatorType() );
        match.setNumerator( object.getNumerator() );
        match.setNumeratorDescription( object.getNumeratorDescription() );
        match.setNumeratorAggregationOperator( object.getNumeratorAggregationOperator() );
        match.setDenominator( object.getDenominator() );
        match.setDenominatorDescription( object.getDenominatorDescription() );
        match.setDenominatorAggregationOperator( object.getDenominatorAggregationOperator() );
        
        indicatorService.updateIndicator( match );                
    }
    
    protected Indicator getMatching( Indicator object )
    {
        Indicator match = indicatorService.getIndicatorByName( object.getName() );
        
        if ( match == null )
        {
            match = indicatorService.getIndicatorByAlternativeName( object.getAlternativeName() );
        }
        if ( match == null )
        {
            match = indicatorService.getIndicatorByShortName( object.getShortName() );
        }
        if ( match == null )
        {
            match = indicatorService.getIndicatorByCode( object.getCode() );
        }
        
        return match;
    }
    
    protected boolean isIdentical( Indicator object, Indicator existing )
    {
        if ( !object.getName().equals( existing.getName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getAlternativeName(), existing.getAlternativeName() ) || ( isNotNull( object.getAlternativeName(), existing.getAlternativeName() ) && !object.getAlternativeName().equals( existing.getAlternativeName() ) ) )
        {
            return false;
        }
        if ( !object.getShortName().equals( existing.getShortName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getCode(), existing.getCode() ) || ( isNotNull( object.getCode(), existing.getCode() ) && !object.getCode().equals( existing.getCode() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDescription(), existing.getDescription() ) || ( isNotNull( object.getDescription(), existing.getDescription() ) && !object.getDescription().equals( existing.getDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getNumeratorDescription(), existing.getNumeratorDescription() ) || ( isNotNull( object.getNumeratorDescription(), existing.getNumeratorDescription() ) && !object.getNumeratorDescription().equals( existing.getNumeratorDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getNumeratorAggregationOperator(), existing.getNumeratorAggregationOperator() ) || ( isNotNull( object.getNumeratorAggregationOperator(), existing.getNumeratorAggregationOperator() ) && !object.getNumeratorAggregationOperator().equals( existing.getNumeratorAggregationOperator() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDenominatorDescription(), existing.getDenominatorDescription() ) || ( isNotNull( object.getDenominatorDescription(), existing.getDenominatorDescription() ) && !object.getDenominatorDescription().equals( existing.getDenominatorDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDenominatorAggregationOperator(), existing.getDenominatorAggregationOperator() ) || ( isNotNull( object.getDenominatorAggregationOperator(), existing.getDenominatorAggregationOperator() ) && !object.getDenominatorAggregationOperator().equals( existing.getDenominatorAggregationOperator() ) ) )
        {
            return false;
        }
        
        return true;
    }
}
