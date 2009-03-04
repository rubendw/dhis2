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

import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

import com.opensymphony.xwork.ActionSupport;

/** 
 * @author Margrethe Store
 * @version $Id: ValidateValidationRuleAction.java 3868 2007-11-08 15:11:12Z larshelg $
 */
public class ValidateValidationRuleAction
    extends ActionSupport
{
    private static final String NONE = "none";
    
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

    public void setName( String validationName )
    {
        this.name = validationName;
    }

    private String operator;

    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    private String leftSideExpression;

    public void setLeftSideExpression( String leftFormula )
    {
        this.leftSideExpression = leftFormula;
    }

    private String rightSideExpression;

    public void setRightSideExpression( String rightFormula )
    {
        this.rightSideExpression = rightFormula;
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

            ValidationRule match = validationRuleService.getValidationRuleByName( name );

            if ( match != null && ( id == null || match.getId() != id ) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }
        }

        if ( operator == null || operator.equals( NONE ) )
        {
            message = i18n.getString( "specify_operator" );
            
            return INPUT;
        }

        if ( leftSideExpression == null || leftSideExpression.trim().length() == 0 )
        {
            message = i18n.getString( "specify_left_side" );
            
            return INPUT;
        }

        Set<DataElement> leftSideDataElements = expressionService.getDataElementsInExpression( leftSideExpression );
        
        for ( DataElement element : leftSideDataElements )
        {
            if ( !element.getType().equals( DataElement.TYPE_INT ) )
            {
                message = i18n.getString( "dataelements_left_must_be_type_integers" );
                
                return INPUT;
            }
        }

        if ( rightSideExpression == null || rightSideExpression.trim().length() == 0 )
        {
            message = i18n.getString( "specify_right_side" );
            
            return INPUT;
        }
        
        Set<DataElement> rightSideDataElements = expressionService.getDataElementsInExpression( rightSideExpression  );

        for ( DataElement element : rightSideDataElements )
        {
            if ( !element.getType().equals( DataElement.TYPE_INT ) )
            {
                message = i18n.getString( "dataelements_right_must_be_type_integers" );
                
                return INPUT;
            }
        }
        
        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }
}
