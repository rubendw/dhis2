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

import java.util.Collection;
import java.util.Set;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * Interface for ExpressionService. Defines service functionality for
 * Expressions.
 *
 * @author Margrethe Store
 * @version $Id: ExpressionService.java 5224 2008-05-23 09:35:07Z larshelg $
 */
public interface ExpressionService
{
    String ID = ExpressionService.class.getName();
    
    int VALID = 1;
    int DATAELEMENT_ID_NOT_NUMERIC = -1;
    int CATEGORYOPTIONCOMBO_ID_NOT_NUMERIC = -2;
    int DATAELEMENT_DOES_NOT_EXIST = -3;
    int CATEGORYOPTIONCOMBO_DOES_NOT_EXIST = -4;
    int EXPRESSION_NOT_WELL_FORMED = -5;
    
    /**
     * Adds a new Expression to the database.
     *
     * @param expression The Expression to add.
     * @return The generated identifier for this Expression.
     */
    int addExpression( Expression expression );

    /**
     * Updates an Expression.
     *
     * @param expression The Expression to update.
     */
    void updateExpression( Expression expression );

    /**
     * Deletes an Expression from the database.
     *
     * @param id Identifier of the Expression to delete.
     */
    void deleteExpression( Expression expression );

    /**
     * Get the Expression with the given identifier.
     *
     * @param id The identifier.
     * @return an Expression with the given identifier.
     */
    Expression getExpression( int id );

    /**
     * Gets all Expressions.
     *
     * @return A collection with all Expressions.
     */
    Collection<Expression> getAllExpressions();

    /**
     * Calculates the value of the given Expression.
     * 
     * @param expression The Expression.
     * @param source The Source.
     * @param period The Period.
     * @return The value of the given Expression, or null
     *         if no values are registered for a given combination of 
     *         DataElement, Source, and Period.
     */
    Double getExpressionValue( Expression expression, Period period, Source source );
    
    /**
     * Returns all DataElements included in the given expression string.
     * 
     * @param expression The expression string.
     * @return A Set of DataElements included in the expression string.
     */
    Set<DataElement> getDataElementsInExpression( String expression );
    
    /**
     * Returns all operands included in an expression string. The operand is on
     * the form <data element id>.<category option combo id>.
     * 
     * @param expression The expression string.
     * @return A Set of Strings containing the ids of dataelements and their 
     *         dimensions included in the expression string.
     */
    Set<String> getOperandsInExpression( String expression );
    
    /**
     * Tests whether the expression is valid. Returns a positive value if the
     * expression is valid, or a negative value if not.
     * 
     * @param formula the expression formula.
     * @return VALID if the expression is valid.
     * 		   DATAELEMENT_ID_NOT_NUMERIC if the data element is not a number.
     * 		   CATEGORYOPTIONCOMBO_ID_NOT_NUMERIC if the category option combo id is not a number.
     * 		   DATAELEMENT_DOES_NOT_EXIST if the data element does not exist.
     * 		   CATEGORYOPTIONCOMBO_DOES_NOT_EXIST if the category option combo does not exist.
     */
    int expressionIsValid( String formula );
    
    /**
     * Creates an expression string containing DataElement names and the names of
     * the CategoryOptions in the CategoryOptionCombo from a string consisting
     * of identifiers.
     * 
     * @param expression The expression string.
     * @return An expression string containing DataElement names and the names of
     *         the CategoryOptions in the CategoryOptionCombo.
     * @throws IllegalArgumentException if data element id or category option combo
     * 		   id are not numeric or data element or category option combo do not exist.
     */
    String getExpressionDescription( String expression );

    /**
     * Looks for the existence of calculated dataelement in the give expression.
     * If exists, it will replace this CDE with its equivalent expression.
     * 
     * @param expression The expression string
     * @return expression containing non CDE
     */
    String replaceCDEsWithTheirExpression( String expression );
    
    /**
     * Converts an expression on the form [34] + [23], where the numbers are 
     * IDs of DataElements, to the form 200 + 450, where the numbers are the 
     * values of the DataValues registered for the Period and Source. "0" is
     * included if there is no DataValue registered for the given parameters.
     * 
     * @param expression The expression string.
     * @param period The Period.
     * @param source The Source.
     * @return A numerical expression.
     */    
    String generateExpression( String expression, Period period, Source source );
}
