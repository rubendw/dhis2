package org.hisp.dhis.datamart;

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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.dataelement.Operand;

/**
 * @author Lars Helge Overland
 * @version $Id: CrossTabDataValue.java 5514 2008-08-04 10:48:07Z larshelg $
 */
public class CrossTabDataValue
{
    private int periodId;
    
    private int sourceId;
    
    /**
     * Contains Operand (data element id and category option combo id) and data value.
     */
    private Map<Operand, String> valueMap = new HashMap<Operand, String>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    public CrossTabDataValue()
    {   
    }
    
    public CrossTabDataValue( int periodId, int sourceId, Map<Operand, String> valueMap )
    {
        this.periodId = periodId;
        this.sourceId = sourceId;
        this.valueMap = valueMap;
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------
    
    public int getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId( int periodId )
    {
        this.periodId = periodId;
    }

    public int getSourceId()
    {
        return sourceId;
    }

    public void setSourceId( int sourceId )
    {
        this.sourceId = sourceId;
    }

    public Map<Operand, String> getValueMap()
    {
        return valueMap;
    }

    public void setValueMap( Map<Operand, String> valueMap )
    {
        this.valueMap = valueMap;
    }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------
    
    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + periodId;
        result = PRIME * result + sourceId;
        
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
        
        final CrossTabDataValue other = (CrossTabDataValue) object;
        
        return periodId == other.periodId && sourceId == other.sourceId;
    }
    
    @Override
    public String toString()
    {
        String toString = "[period id: " + periodId + ", source id: " + sourceId + "]";
        
        return toString;
    }
}
