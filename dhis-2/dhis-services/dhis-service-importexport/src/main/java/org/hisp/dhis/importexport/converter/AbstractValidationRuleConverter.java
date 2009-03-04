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

import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractValidationRuleConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class AbstractValidationRuleConverter
    extends AbstractConverter<ValidationRule>
{
    protected ValidationRuleService validationRuleService;

    protected ExpressionService expressionService;
        
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------

    protected void importUnique( ValidationRule object )
    {
        expressionService.addExpression( object.getLeftSide() );
        expressionService.addExpression( object.getRightSide() );
        
        validationRuleService.addValidationRule( object );        
    }
    
    protected void importMatching( ValidationRule object, ValidationRule match )
    {
        match.setName( object.getName() );
        match.setDescription( object.getDescription() );
        match.setType( object.getType() );
        match.setOperator( object.getOperator() );
        match.getLeftSide().setExpression( object.getLeftSide().getExpression() );
        match.getLeftSide().setDescription( object.getLeftSide().getDescription() );
        match.getLeftSide().setDataElementsInExpression( object.getLeftSide().getDataElementsInExpression() );
        match.getRightSide().setExpression( object.getRightSide().getExpression() );
        match.getRightSide().setDescription( object.getRightSide().getDescription() );
        match.getRightSide().setDataElementsInExpression( object.getRightSide().getDataElementsInExpression() );
        
        expressionService.updateExpression( match.getLeftSide() );
        expressionService.updateExpression( match.getRightSide() );
        
        validationRuleService.updateValidationRule( match );
    }
    
    protected ValidationRule getMatching( ValidationRule object )
    {
        return validationRuleService.getValidationRuleByName( object.getName() );
    }    

    protected boolean isIdentical( ValidationRule object, ValidationRule existing )
    {
        if ( !object.getName().equals( existing.getName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDescription(), existing.getDescription() ) || ( isNotNull( object.getDescription(), existing.getDescription() ) && !object.getDescription().equals( existing.getDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getType(), existing.getType() ) || ( isNotNull( object.getType(), existing.getType() ) && !object.getType().equals( existing.getType() ) ) )
        {
            return false;
        }
        if ( !object.getOperator().equals( existing.getOperator() ) )
        {
            return false;
        }
        
        return true;
    }
}
