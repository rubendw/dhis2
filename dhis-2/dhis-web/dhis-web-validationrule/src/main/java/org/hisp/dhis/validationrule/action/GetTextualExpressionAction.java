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

import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id: GetFormulaTextAction.java 3814 2007-11-02 12:11:54Z larshelg $
 */
public class GetTextualExpressionAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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
    // Properties
    // -------------------------------------------------------------------------

    private String expression;

    public void setExpression( String formula )
    {
        this.expression = formula;
    }

    private String textualExpression;

    public String getTextualExpression()
    {
        return textualExpression;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        textualExpression = getExpressionDescription();
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    public String getExpressionDescription()
    {
        String description = null;
        
        if ( expression != null )
        {
            int result = expressionService.expressionIsValid( expression );
            
            if ( result == ExpressionService.VALID )
            {
                description = expressionService.getExpressionDescription( expression );
            }
            else if ( result == ExpressionService.DATAELEMENT_ID_NOT_NUMERIC )
            {
                description = i18n.getString( "dataelement_id_must_be_number" );
            }
            else if ( result == ExpressionService.CATEGORYOPTIONCOMBO_ID_NOT_NUMERIC )
            {
                description = i18n.getString( "category_option_combo_id_must_be_number" );
            }
            else if ( result == ExpressionService.DATAELEMENT_DOES_NOT_EXIST )
            {
                description = i18n.getString( "id_does_not_reference_dataelement" );
            }
            else if ( result == ExpressionService.CATEGORYOPTIONCOMBO_DOES_NOT_EXIST )
            {
                description = i18n.getString( "id_does_not_reference_category_option_combo" );
            }
            else if ( result == ExpressionService.EXPRESSION_NOT_WELL_FORMED )
            {
                description = i18n.getString( "expression_not_well_formed" );
            }
        }
        
        return description;
    }
}
