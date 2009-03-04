package org.hisp.dhis.aggregation;

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

/**
 * @author Lars Helge Overland
 * @version $Id: AggregatedIndicatorValue.java 2694 2007-01-12 19:19:46Z torgeilo $
 */
public class AggregatedIndicatorValue
    implements Serializable
{
    private int indicatorId;
    
    private int periodId;
    
    private int periodTypeId;
    
    private int organisationUnitId;
    
    private int level;
    
    private String annualized;
    
    private double factor;
    
    private double value;
    
    private double numeratorValue;
    
    private double denominatorValue;

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------
    
    public AggregatedIndicatorValue()
    {   
    }
    
    /**
     * @deprecated this constructor is deprecated
     */
    public AggregatedIndicatorValue( int indicatorId, int periodId, int periodTypeId, int organisationUnitId, 
        int level, double value, double numeratorValue, double denominatorValue )
    {
        this.indicatorId = indicatorId;
        this.periodId = periodId;
        this.periodTypeId = periodTypeId;
        this.organisationUnitId = organisationUnitId;
        this.level = level;
        this.value = value;
        this.numeratorValue = numeratorValue;
        this.denominatorValue = denominatorValue;
    }
    
    /**
     * @param indicatorId indicator id
     * @param periodId period id
     * @param periodTypeId period type id
     * @param organisationUnitId organisation unit id
     * @param level level
     * @param factor factor
     * @param value value
     * @param numeratorValue numerator value
     * @param denominatorValue denominator value
     */
    public AggregatedIndicatorValue( int indicatorId, int periodId, int periodTypeId, int organisationUnitId, 
        int level, double factor, double value, double numeratorValue, double denominatorValue )
    {
        this.indicatorId = indicatorId;
        this.periodId = periodId;
        this.periodTypeId = periodTypeId;
        this.organisationUnitId = organisationUnitId;
        this.level = level;
        this.factor = factor;
        this.value = value;
        this.numeratorValue = numeratorValue;
        this.denominatorValue = denominatorValue;
    }

    // ----------------------------------------------------------------------
    // Getters and setters
    // ----------------------------------------------------------------------
    
    public double getDenominatorValue()
    {
        return denominatorValue;
    }

    public void setDenominatorValue( double denominatorValue )
    {
        this.denominatorValue = denominatorValue;
    }

    public int getIndicatorId()
    {
        return indicatorId;
    }

    public void setIndicatorId( int indicatorId )
    {
        this.indicatorId = indicatorId;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
    }

    public String getAnnualized()
    {
        return annualized;
    }

    public void setAnnualized( String annualized )
    {
        this.annualized = annualized;
    }

    public double getFactor()
    {
        return factor;
    }

    public void setFactor( double factor )
    {
        this.factor = factor;
    }

    public double getNumeratorValue()
    {
        return numeratorValue;
    }

    public void setNumeratorValue( double numeratorValue )
    {
        this.numeratorValue = numeratorValue;
    }

    public int getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( int organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    public int getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId( int periodId )
    {
        this.periodId = periodId;
    }

    public int getPeriodTypeId()
    {
        return periodTypeId;
    }

    public void setPeriodTypeId( int periodTypeId )
    {
        this.periodTypeId = periodTypeId;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue( double value )
    {
        this.value = value;
    }

    // ----------------------------------------------------------------------
    // hashCode and equals
    // ----------------------------------------------------------------------

    public int hashCode()
    {
        return indicatorId * periodId * organisationUnitId * 17;
    }
    
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        
        if ( o == null || !( o instanceof AggregatedIndicatorValue ) )
        {
            return false;
        }
        
        AggregatedIndicatorValue that = (AggregatedIndicatorValue) o;
        
        return this.indicatorId == that.getIndicatorId() && 
            this.periodId == that.getPeriodId() && 
            this.organisationUnitId == that.getOrganisationUnitId(); 
    }
}
