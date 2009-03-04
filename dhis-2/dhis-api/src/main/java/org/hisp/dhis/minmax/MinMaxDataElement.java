package org.hisp.dhis.minmax;

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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.source.Source;

/**
 * @author Kristian Nordal
 * @version $Id: MinMaxDataElement.java 5180 2008-05-20 15:09:18Z larshelg $
 */
public class MinMaxDataElement
    implements Serializable
{
    private int id;

    private Source source;

    private DataElement dataElement;

    private int min;

    private int max;

    private boolean generated;

    // -------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------- 
    
    public MinMaxDataElement()
    {
    }

    public MinMaxDataElement( Source source, DataElement dataElement, int min, int max,
        boolean generated )
    {
        this.source = source;
        this.dataElement = dataElement;
        this.min = min;
        this.max = max;
        this.generated = generated;
    }

    // -------------------------------------------------------------------------
    // Equals and hashCode
    // ------------------------------------------------------------------------- 

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + max;
        
        result = PRIME * result + min;
        
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        
        if ( obj == null )
        {
            return false;
        }
        
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        
        final MinMaxDataElement other = (MinMaxDataElement) obj;
        
        if ( max != other.max )
        {
            return false;
        }
        
        if ( min != other.min )
        {
            return false;
        }
        
        return true;
    }
    
    // -------------------------------------------------------------------------
    // Setters and getters
    // ------------------------------------------------------------------------- 
    
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public Source getSource()
    {
        return source;
    }

    public void setSource( Source source )
    {
        this.source = source;
    }

    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    public int getMin()
    {
        return min;
    }

    public void setMin( int min )
    {
        this.min = min;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax( int max )
    {
        this.max = max;
    }

    public boolean isGenerated()
    {
        return generated;
    }

    public void setGenerated( boolean generated )
    {
        this.generated = generated;
    }
}
