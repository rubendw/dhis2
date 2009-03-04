package org.hisp.dhis.dataelement;

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
 * @author Abyot Asalefew
 * @version $Id$
 */
public class Operand
    implements Serializable, Comparable<Operand>
{
    public static final String SEPARATOR = ".";

    private String id;

    private int dataElementId;

    private int optionComboId;

    private String operandName;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Operand()
    {
    }
    
    public Operand( int dataElementId, int optionComboId )
    {
        this.id = dataElementId + SEPARATOR + optionComboId;
        this.dataElementId = dataElementId;
        this.optionComboId = optionComboId;
    }

    public Operand( int dataElementId, int optionComboId, String operandName )
    {
        this.id = dataElementId + SEPARATOR + optionComboId;
        this.dataElementId = dataElementId;
        this.optionComboId = optionComboId;
        this.operandName = operandName;
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getOperandName()
    {
        return operandName;
    }

    public void setOperandName( String operandName )
    {
        this.operandName = operandName;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getOptionComboId()
    {
        return optionComboId;
    }

    public void setOptionComboId( int optionComboId )
    {
        this.optionComboId = optionComboId;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + dataElementId;
        result = prime * result + optionComboId;
        
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
        
        final Operand other = (Operand) object;
        
        return dataElementId == other.dataElementId && optionComboId == other.optionComboId;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "[DataElementId: " + dataElementId + ", CategoryOptionComboId: " + optionComboId + "]";
    }
    
    // -------------------------------------------------------------------------
    // compareTo
    // -------------------------------------------------------------------------

    public int compareTo( Operand other )
    {
        if ( this.getDataElementId() != other.getDataElementId() )
        {
            return this.getDataElementId() - other.getDataElementId();
        }
        
        return this.getOptionComboId() - other.getOptionComboId();
    }
}
