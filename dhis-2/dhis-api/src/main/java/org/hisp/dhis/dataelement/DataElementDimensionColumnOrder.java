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
public class DataElementDimensionColumnOrder
    implements Serializable
{	     
    private DataElementCategory category;
    
    private DataElementCategoryOption categoryOption;
    
    private int displayOrder;  

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementDimensionColumnOrder()
    {
    }
    
    public DataElementDimensionColumnOrder( DataElementCategory category, DataElementCategoryOption categoryOption, int displayOrder )
    {
    	this.category = category;
    	this.categoryOption = categoryOption;
    	this.displayOrder = displayOrder;
    } 
    
    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = result * prime + category.hashCode();
        result = result * prime + categoryOption.hashCode();       

        return result;
    }
    
    @Override   
    public boolean equals( Object object )
    {
        if( this == object )
        {
            return true;
        }
        
        if( ( object == null) || ( object.getClass() != this.getClass() ) )
        {
            return false;
        }
        
        final DataElementDimensionColumnOrder other = (DataElementDimensionColumnOrder) object;
        
        return ( category == other.category && categoryOption == other.categoryOption && 
            displayOrder == other.displayOrder );            
    }
    
    // ------------------------------------------------------------------------
    // Getters and setters
    // ------------------------------------------------------------------------

    public DataElementCategory getCategory() 
    {
        return category;
    }
    
    public void setCategory( DataElementCategory category )
    {
        this.category = category;
    }
    
    public DataElementCategoryOption getCategoryOption() 
    {
        return categoryOption;
    }
    
    public void setCategoryOption( DataElementCategoryOption categoryOption )
    {
        this.categoryOption = categoryOption;
    }
    
    public int getDisplayOrder() 
    {
        return displayOrder;
    }
    
    public void setDisplayOrder( int displayOrder )
    {
        this.displayOrder = displayOrder;
    }  
    
}

