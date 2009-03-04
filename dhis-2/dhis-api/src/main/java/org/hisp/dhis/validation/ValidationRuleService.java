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

import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Margrethe Store
 * @version $Id: ValidationRuleService.java 5434 2008-06-18 18:57:59Z larshelg $
 */
public interface ValidationRuleService
{
    String ID = ValidationRuleService.class.getName();

    // -------------------------------------------------------------------------
    // ValidationRule business logic
    // -------------------------------------------------------------------------

    /**
     * Validate DataValues.
     * 
     * @param startDate the start date.
     * @param endDate the end date.
     * @param sources a collection of Sources.
     * @return a collection of ValidationResults for each validation violation. 
     */
    Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<? extends Source> sources );
    
    /**
     * Validate DataValues.
     * 
     * @param startDate the start date.
     * @param endDate the end date.
     * @param sources a collection of Sources.
     * @param group a group of ValidationRules.
     * @return a collection of ValidationResults for each validation violation. 
     */
    Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<? extends Source> sources, ValidationRuleGroup group );
    
    /**
     * Validate DataValues.
     * 
     * @param dataSet the DataSet.
     * @param period the Period.
     * @param source the Source.
     * @return a collection of ValidationResults for each validation violation. 
     */
    Collection<ValidationResult> validate( DataSet dataSet, Period period, Source source );

    /**
     * Validate DataValues.
     * 
     * @param startDate the start date.
     * @param endDate the end date.
     * @param source the Source.
     * @return a collection of ValidationResults for each validation violation. 
     */
    Collection<ValidationResult> validate( Date startDate, Date endDate, Source source );

    // -------------------------------------------------------------------------
    // ValidationRule
    // -------------------------------------------------------------------------

    /**
     * Adds a ValidationRule to the database.
     * 
     * @param validationRule the ValidationRule to add.
     * @return the generated unique identifier for the ValidationRule.
     */
    int addValidationRule( ValidationRule validationRule );

    /**
     * Delete a validation rule with the given identifiers from the database.
     * 
     * @param validationRule the ValidationRule to delete.
     */
    void deleteValidationRule( ValidationRule validationRule );

    /**
     * Update a validation rule with the given identifiers.
     * 
     * @param validationRule the ValidationRule to update.
     */
    void updateValidationRule( ValidationRule validationRule );

    /**
     * Get ValidationRule with the given identifier.
     * 
     * @param id the unique identifier of the ValidationRule.
     * @return the ValidationRule or null if it doesn't exist.
     */
    ValidationRule getValidationRule( int id );

    /**
     * Get all validation rules.
     * 
     * @return a Collection of ValidationRule or null if it there are no validation rules.
     */    
    Collection<ValidationRule> getAllValidationRules();

    /**
     * Get a validation rule with the given name.
     * 
     * @param name the name of the validation rule.
     */
    ValidationRule getValidationRuleByName( String name );
    
    // -------------------------------------------------------------------------
    // ValidationRuleGroup
    // -------------------------------------------------------------------------

    /**
     * Adds a ValidationRuleGroup to the database.
     * 
     * @param validationRuleGroup the ValidationRuleGroup to add.
     * @return the generated unique identifier for the ValidationRuleGroup.
     */
    int addValidationRuleGroup( ValidationRuleGroup validationRuleGroup );

    /**
     * Delete a ValidationRuleGroup with the given identifiers from the database.
     * 
     * @param id the ValidationRuleGroup to delete.
     */
    void deleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup );

    /**
     * Update a ValidationRuleGroup with the given identifiers.
     * 
     * @param validationRule the ValidationRule to update.
     */
    void updateValidationRuleGroup( ValidationRuleGroup validationRuleGroup );

    /**
     * Get ValidationRuleGroup with the given identifier.
     * 
     * @param id the unique identifier of the ValidationRuleGroup.
     * @return the ValidationRule or null if it doesn't exist.
     */
    ValidationRuleGroup getValidationRuleGroup( int id );

    /**
     * Get all ValidationRuleGroups.
     * 
     * @return a Collection of ValidationRuleGroup or null if it there are no ValidationRuleGroups.
     */    
    Collection<ValidationRuleGroup> getAllValidationRuleGroups();
    
    /**
     * Get a ValidationRuleGroup with the given name.
     * 
     * @param name the name of the ValidationRuleGroup.
     */
    ValidationRuleGroup getValidationRuleGroupByName( String name );    
}
