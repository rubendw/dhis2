package org.hisp.dhis.validation;

/*
 * Copyright (c) 2004-2005, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may
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

import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Margrethe Store
 * @version $Id: ValidationResult.java 5277 2008-05-27 15:48:42Z larshelg $
 */
public class ValidationResult
    implements Serializable
{
    private Source source;
    
    private Period period;
    
    private ValidationRule validationRule;
    
    private double leftsideValue;
    
    private double rightsideValue;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------     

    public ValidationResult()
    {      
    }
    
    public ValidationResult( Period period, Source source, ValidationRule validationRule,
        double leftsideValue, double rightsideValue )
    {
        this.source = source;
        this.period = period;
        this.validationRule = validationRule;
        this.leftsideValue = leftsideValue;
        this.rightsideValue = rightsideValue;
    }

    // -------------------------------------------------------------------------
    // Equals, hashCode and toString
    // -------------------------------------------------------------------------     

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + ( ( period == null ) ? 0 : period.hashCode() );
        result = PRIME * result + ( ( source == null ) ? 0 : source.hashCode() );
        result = PRIME * result + ( ( validationRule == null ) ? 0 : validationRule.hashCode() );
        
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        final ValidationResult other = (ValidationResult) object;
        
        if ( period == null )
        {
            if ( other.period != null )
            {
                return false;
            }
        }
        else if ( !period.equals( other.period ) )
        {
            return false;
        }
        
        if ( source == null )
        {
            if ( other.source != null )
            {
                return false;
            }
        }
        else if ( !source.equals( other.source ) )
        {
            return false;
        }
        
        if ( validationRule == null )
        {
            if ( other.validationRule != null )
            {
                return false;
            }
        }
        else if ( !validationRule.equals( other.validationRule ) )
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return source + " - " + period + " - " + validationRule + " - " + leftsideValue + " - " + rightsideValue;
    }
    
    // -------------------------------------------------------------------------
    // Set and get methods
    // -------------------------------------------------------------------------     

    public Source getSource()
    {
        return source;
    }

    public void setSource( Source source )
    {
        this.source = source;
    }
    
    public Period getPeriod()
    {
        return period;
    }

    public void setPeriod( Period period )
    {
        this.period = period;
    }

    public ValidationRule getValidationRule()
    {
        return validationRule;
    }

    public void setValidationRule( ValidationRule validationRule )
    {
        this.validationRule = validationRule;
    }

    public double getLeftsideValue()
    {
        return leftsideValue;
    }

    public void setLeftsideValue( double leftsideValue )
    {
        this.leftsideValue = leftsideValue;
    }

    public double getRightsideValue()
    {
        return rightsideValue;
    }

    public void setRightsideValue( double rightsideValue )
    {
        this.rightsideValue = rightsideValue;
    }
}
