package org.hisp.dhis.validation;

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

import static org.hisp.dhis.system.util.MathUtils.expressionIsTrue;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.source.Source;

/**
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id
 */
public class DefaultValidationRuleService
    implements ValidationRuleService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ValidationRuleStore validationRuleStore;
    
    public void setValidationRuleStore( ValidationRuleStore validationRuleStore )
    {
        this.validationRuleStore = validationRuleStore;
    }
    
    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // ValidationRule business logic
    // -------------------------------------------------------------------------
    
    public Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<? extends Source> sources )
    {    	         
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();
        
        for ( Period period : periodService.getIntersectingPeriods( startDate, endDate ) )
        {        	
            for ( Source source : sources )
            {
                for ( DataSet dataSet : getRelevantDataSets( source ) )
                {
                    validationViolations.addAll( validate( period, source, getRelevantValidationRules( dataSet ) ) );
                }
            }
        }
        
        return validationViolations;
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<? extends Source> sources, ValidationRuleGroup group )
    {
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        for ( Period period : periodService.getIntersectingPeriods( startDate, endDate ) )
        {               
            for ( Source source : sources )
            {
                for ( DataSet dataSet : getRelevantDataSets( source ) )
                {
                    Collection<ValidationRule> relevantRules = CollectionUtils.intersection( getRelevantValidationRules( dataSet ), group.getMembers() );
                    
                    validationViolations.addAll( validate( period, source, relevantRules ) );
                }
            }
        }
        
        return validationViolations;
    }
    
    public Collection<ValidationResult> validate( Date startDate, Date endDate, Source source )
    {
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();
        
        for ( Period period : periodService.getIntersectingPeriods( startDate, endDate ) )
        {
            for ( DataSet dataSet : getRelevantDataSets( source ) )
            {
                validationViolations.addAll( validate( period, source, getRelevantValidationRules( dataSet ) ) );
            }
        }
        
        return validationViolations;
    }
    
    public Collection<ValidationResult> validate( DataSet dataSet, Period period, Source source )
    {
        return validate( period, source, getRelevantValidationRules( dataSet ) );
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Collection<ValidationResult> validate( Period period, Source source, Collection<ValidationRule> validationRules )
    {
        Collection<ValidationResult> validationResults = new HashSet<ValidationResult>();
        
        for ( ValidationRule validationRule : validationRules )
        {
            Double leftSide = expressionService.getExpressionValue( validationRule.getLeftSide(), period, source );
            Double rightSide = expressionService.getExpressionValue( validationRule.getRightSide(), period, source );
            
            if ( leftSide != null && rightSide != null )
            {
                boolean violation = !expressionIsTrue( leftSide, validationRule.getMathematicalOperator(), rightSide );
                
                if ( violation )
                {
                    validationResults.add( new ValidationResult( period, source, validationRule, leftSide, rightSide ) );
                }
            }
        }
        
        return validationResults;
    }

    private Collection<ValidationRule> getRelevantValidationRules( DataSet dataSet )
    {
        Collection<ValidationRule> rules = validationRuleStore.getAllValidationRules();
        
        return getRelevantValidationRules( dataSet, rules );
    }
    
    private Collection<ValidationRule> getRelevantValidationRules( DataSet dataSet, Collection<ValidationRule> validationRules )
    {           
        Collection<ValidationRule> relevantValidationRules = new HashSet<ValidationRule>();
        
        for ( ValidationRule validationRule : validationRules )
        {               
            for ( DataElement dataElement : dataSet.getDataElements() )
            {   
                if ( validationRule.getLeftSide().getDataElementsInExpression().contains( dataElement ) ||
                    validationRule.getRightSide().getDataElementsInExpression().contains( dataElement ) )
                {
                    relevantValidationRules.add( validationRule );
                }
            }
        }
        
        return relevantValidationRules;
    }
    
    private Collection<DataSet> getRelevantDataSets( Source source )
    {
        Collection<DataSet> relevantDataSets = new HashSet<DataSet>();
        
        for ( DataSet dataSet : dataSetService.getAllDataSets() )
        {
            if ( dataSet.getSources().contains( source ) )
            {
                relevantDataSets.add( dataSet );
            }
        }
        
        return relevantDataSets;
    }
    
    // -------------------------------------------------------------------------
    // ValidationRule CRUD operations
    // -------------------------------------------------------------------------
   
    public int addValidationRule( ValidationRule validationRule )
    {
        return validationRuleStore.addValidationRule( validationRule );
    }

    public void deleteValidationRule( ValidationRule validationRule )
    {
        validationRuleStore.deleteValidationRule( validationRule );        
    }

    public Collection<ValidationRule> getAllValidationRules()
    {
        return validationRuleStore.getAllValidationRules();
    }

    public ValidationRule getValidationRule( int id )
    {
        return validationRuleStore.getValidationRule( id );
    }

    public ValidationRule getValidationRuleByName( String name )
    {
        return validationRuleStore.getValidationRuleByName( name );
    }

    public void updateValidationRule( ValidationRule validationRule )
    {
        validationRuleStore.updateValidationRule( validationRule );
    }

    // -------------------------------------------------------------------------
    // ValidationRuleGroup CRUD operations
    // -------------------------------------------------------------------------

    public int addValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        return validationRuleStore.addValidationRuleGroup( validationRuleGroup );
    }

    public void deleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        validationRuleStore.deleteValidationRuleGroup( validationRuleGroup );
    }

    public void updateValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        validationRuleStore.updateValidationRuleGroup( validationRuleGroup );
    }

    public ValidationRuleGroup getValidationRuleGroup( int id )
    {
        return validationRuleStore.getValidationRuleGroup( id );
    }

    public Collection<ValidationRuleGroup> getAllValidationRuleGroups()
    {
        return validationRuleStore.getAllValidationRuleGroups();
    }
    
    public ValidationRuleGroup getValidationRuleGroupByName( String name )
    {
        return validationRuleStore.getValidationRuleGroupByName( name );
    }  
}
