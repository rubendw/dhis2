package org.hisp.dhis.expression;

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

import static org.hisp.dhis.expression.Expression.SEPARATOR;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

/**
 * @author Abyot Aselefew
 * @version $Id$
 */
public class MultiDimensionExpressionUpgrader
    extends AbstractStartupRoutine
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ExpressionService expressionService;
    
    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }
    
    private IndicatorService indicatorService;
    
    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
     
    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService categoryOptionComboService )
    {
        this.categoryOptionComboService = categoryOptionComboService;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public void execute()
        throws Exception
    {
        DataElementCategoryOptionCombo defaultOptionCombo = categoryOptionComboService.getDefaultDataElementCategoryOptionCombo();
                
        int defaultId = defaultOptionCombo.getId();

        Collection<Expression> expressions = expressionService.getAllExpressions();
        
        Collection<Indicator> indicators = indicatorService.getAllIndicators();
    
        // ---------------------------------------------------------------------
        // Any expression containing an operand of the form [dataElementId] will
        // be upgraded to the form [dataElementId.defaultOptionComboId] 
        // ---------------------------------------------------------------------
        
		for ( Expression expression : expressions )
    	{
			String expressionString = upgradeExpression( expression.getExpression(), defaultId );
			
			if ( expressionString != null )
			{
				expression.setExpression( expressionString );
				
				if( expression.getDescription() == null )
                {
					expression.setDescription( expressionService.getExpressionDescription( expressionString ) );
                }
	            
	            expressionService.updateExpression( expression );
			}			
        }
        
        for( Indicator indicator : indicators )
		{
            String numerator = upgradeExpression( indicator.getNumerator(), defaultId );
			
			String denominator = upgradeExpression( indicator.getDenominator(), defaultId );
            
			if ( numerator != null )
			{
				indicator.setNumerator( numerator );				
			}
            
			if ( denominator != null )
			{				
				indicator.setDenominator( denominator );				
			}		
			
			if ( numerator != null || denominator != null )
			{
				indicatorService.updateIndicator( indicator );
			}
		}
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String upgradeExpression( String expression, int defaultOptionComboId )
    {
        StringBuffer buffer = new StringBuffer();

        boolean matched = false;
        
        if ( expression != null )
        {
        	Matcher matcher = getMatcher( "(\\[\\d+\\])", expression );
            
            while ( matcher.find() )
            {
                matched = true;
                
                String replaceString = matcher.group();
                
                replaceString = replaceString.substring( 0, replaceString.length() - 1 ) + SEPARATOR + defaultOptionComboId + "]";                
                
                matcher.appendReplacement( buffer, replaceString );
            }
            
            matcher.appendTail( buffer );
        }
        
        return matched ? buffer.toString() : null;
    }
    
    private Matcher getMatcher( String regex, String expression )
    {
        Pattern pattern = Pattern.compile( regex );
        
        return pattern.matcher( expression );
    }
}
