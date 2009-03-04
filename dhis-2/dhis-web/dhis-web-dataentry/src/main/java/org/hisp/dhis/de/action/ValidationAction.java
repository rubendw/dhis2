package org.hisp.dhis.de.action;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.validation.ValidationResult;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

import com.opensymphony.xwork.Action;

/**
 * @author Margrethe Store
 * @version $Id: ValidationAction.java 5426 2008-06-16 04:33:05Z larshelg $
 */
public class ValidationAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
    }    
    
    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }
   
    private ExpressionService expressionService;
    
    public void setExpressionService (ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------
    
    private List<ValidationResult> results;
    
    public List<ValidationResult> getResults()
    {
        return results;
    }
    
    private Map<Integer, Expression> leftsideMap;
    
    public Map<Integer, Expression> getLeftsideMap()
    {
        return leftsideMap;
    }
        
    private Map<Integer, Expression> rightsideMap;
    
    public Map<Integer, Expression> getRightsideMap()
    {
        return rightsideMap;
    }
    
    private Map<Integer, String> leftsideFormulaMap;
    
    public Map<Integer, String> getLeftsideFormulaMap()
    {
        return leftsideFormulaMap;
    }
    
    private Map<Integer, String> rightsideFormulaMap;
    
    public Map<Integer, String> getRightsideFormulaMap()
    {
        return rightsideFormulaMap;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute() 
        throws Exception
    {
        OrganisationUnit orgUnit = selectedStateManager.getSelectedOrganisationUnit();        
              
        Period period = selectedStateManager.getSelectedPeriod();        
        
        DataSet dataSet = selectedStateManager.getSelectedDataSet();       

        results = new ArrayList<ValidationResult>( validationRuleService.validate( dataSet, period, orgUnit ) );
        
        if ( results.size() > 0 )
        {
            leftsideMap = new HashMap<Integer, Expression>( results.size() );
            rightsideMap = new HashMap<Integer, Expression>( results.size() );

            leftsideFormulaMap = new HashMap<Integer, String>( results.size() );
            rightsideFormulaMap = new HashMap<Integer, String>( results.size() );
            
            for ( ValidationResult result : results )
            {
                ValidationRule rule = result.getValidationRule();
                int id = rule.getId();

                Expression leftside = rule.getLeftSide();
                Expression rightside = rule.getRightSide();
                
                leftsideMap.put( id, leftside );
                rightsideMap.put( id, rightside );
                
                leftsideFormulaMap.put( id,  expressionService.getExpressionDescription( leftside.getExpression() ) );
                rightsideFormulaMap.put( id, expressionService.getExpressionDescription( rightside.getExpression() ) );
            }
        }
        
        return SUCCESS;
    }
}
