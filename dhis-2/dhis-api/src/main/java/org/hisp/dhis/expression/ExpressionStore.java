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

/**
 * Interface for ExpressionStore. Defines the functionality for persisting
 * Expressions.
 * 
 * @author Margrethe Store
 * @version $Id: ExpressionStore.java 3676 2007-10-22 17:30:12Z larshelg $
 */
public interface ExpressionStore
{
    String ID = ExpressionStore.class.getName();
    
    /**
     * Adds a new Expression to the database.
     * 
     * @param expression The new Expression to add.
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
     * Gets the Expression with the given identifier.
     * 
     * @param id The identifier.
     * @return An Expression with the given identifier.
     */
    Expression getExpression( int id );

    /**
     * Gets all Expression.
     * 
     * @return A collection with all the Expressions.
     */
    Collection<Expression> getAllExpressions();
}
