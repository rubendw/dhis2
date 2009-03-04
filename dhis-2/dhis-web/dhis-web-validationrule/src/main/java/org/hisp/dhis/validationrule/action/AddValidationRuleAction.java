package org.hisp.dhis.validationrule.action;

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

import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

import com.opensymphony.xwork.ActionSupport;

/**
 * Adds a new validation rule to the database.
 * 
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id: AddValidationRuleAction.java 3868 2007-11-08 15:11:12Z larshelg $
 */
public class AddValidationRuleAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
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

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String operator;

    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    private String leftSideExpression;

    public void setLeftSideExpression( String leftSideExpression )
    {
        this.leftSideExpression = leftSideExpression;
    }

    private String leftSideDescription;

    public void setLeftSideDescription( String leftSideDescription )
    {
        this.leftSideDescription = leftSideDescription;
    }

    private String rightSideExpression;

    public void setRightSideExpression( String rightSideExpression )
    {
        this.rightSideExpression = rightSideExpression;
    }
    
    private String rightSideDescription;

    public void setRightSideDescription( String rightSideDescription )
    {
        this.rightSideDescription = rightSideDescription;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute()
    {
        Expression leftSide = new Expression();
        
        leftSide.setExpression( leftSideExpression );
        leftSide.setDescription( leftSideDescription );
        leftSide.setDataElementsInExpression( expressionService.getDataElementsInExpression( leftSideExpression ) );
        
        expressionService.addExpression( leftSide );
        
        Expression rightSide = new Expression();
        
        rightSide.setExpression( rightSideExpression );
        rightSide.setDescription( rightSideDescription );
        rightSide.setDataElementsInExpression( expressionService.getDataElementsInExpression( rightSideExpression ) );
        
        ValidationRule validationRule = new ValidationRule();
        
        expressionService.addExpression( rightSide );
        
        validationRule.setName( name );
        validationRule.setDescription( description );
        validationRule.setType( ValidationRule.TYPE_ABSOLUTE );
        validationRule.setOperator( operator );
        validationRule.setLeftSide( leftSide );
        validationRule.setRightSide( rightSide );
        
        validationRuleService.addValidationRule( validationRule );
        
        return SUCCESS;
    }
}
