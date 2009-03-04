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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.expression.Expression;

/**
 * @author Kristian Nordal
 * @version $Id: ValidationRule.java 5434 2008-06-18 18:57:59Z larshelg $
 */
public class ValidationRule
    implements Serializable
{
    public static final String TYPE_STATISTICAL = "statistical";
    public static final String TYPE_ABSOLUTE = "absolute";
    
    public static final String OPERATOR_EQUAL = "equal_to";
    public static final String OPERATOR_NOT_EQUAL = "not_equal_to";
    public static final String OPERATOR_GREATER = "greater_than";
    public static final String OPERATOR_GREATER_EQUAL = "greater_than_or_equal_to";
    public static final String OPERATOR_LESSER = "less_than";
    public static final String OPERATOR_LESSER_EQUAL = "less_than_or_equal_to";
    
    private static Map<String, String> operatorMap = new HashMap<String, String>();
    
    private int id;
    
    private String name;
    
    private String description;
    
    private String type;

    private String operator;
    
    private Expression leftSide;
    
    private Expression rightSide;
    
    // -------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------- 
    
    public ValidationRule()
    {
    }

    public ValidationRule( String name, String description, String type, 
        String operator, Expression leftSide, Expression rightSide )
    {
        this.name = name;
        this.description = description;
        this.type = type;
        this.operator = operator;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // ------------------------------------------------------------------------- 
    
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( !( object instanceof ValidationRule ) )
        {
            return false;
        }

        final ValidationRule validationRule = (ValidationRule) object;

        return name.equals( validationRule.getName() );
    }

    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Set and get methods
    // -------------------------------------------------------------------------  

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
    
    public String getOperator()
    {
        return operator;
    }

    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Expression getLeftSide()
    {
        return leftSide;
    }

    public void setLeftSide( Expression leftSide )
    {
        this.leftSide = leftSide;
    }

    public Expression getRightSide()
    {
        return rightSide;
    }

    public void setRightSide( Expression rightSide )
    {
        this.rightSide = rightSide;
    }

    // -------------------------------------------------------------------------
    // Operator
    // -------------------------------------------------------------------------  

    static
    {
        operatorMap.put( OPERATOR_EQUAL, "==" );
        operatorMap.put( OPERATOR_NOT_EQUAL, "!=" );
        operatorMap.put( OPERATOR_GREATER, ">" );
        operatorMap.put( OPERATOR_GREATER_EQUAL, ">=" );
        operatorMap.put( OPERATOR_LESSER, "<" );
        operatorMap.put( OPERATOR_LESSER_EQUAL, "<=" );
    }
    
    public String getMathematicalOperator()
    {
        return operatorMap.get( operator );
    }
}
