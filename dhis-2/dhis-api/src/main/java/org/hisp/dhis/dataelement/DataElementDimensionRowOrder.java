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
public class DataElementDimensionRowOrder
    implements Serializable
{	
    private DataElementCategoryCombo categoryCombo;
    
    private DataElementCategory category;
    
    private int displayOrder;  

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementDimensionRowOrder()
    {
    }
    
    public DataElementDimensionRowOrder( DataElementCategoryCombo categoryCombo, DataElementCategory category, int displayOrder )
    {
    	this.categoryCombo = categoryCombo;
    	this.category = category;
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

        result = result * prime + categoryCombo.hashCode();
        result = result * prime + category.hashCode();       

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
        
        final DataElementDimensionRowOrder other = (DataElementDimensionRowOrder) object;
        
        return ( categoryCombo == other.categoryCombo && category == other.category && 
            displayOrder == other.displayOrder );            
    }
    
    // ------------------------------------------------------------------------
    // Getters and setters
    // ------------------------------------------------------------------------

    public DataElementCategoryCombo getCategoryCombo() 
    {
        return categoryCombo;
    }
    
    public void setCategoryCombo( DataElementCategoryCombo categoryCombo )
    {
        this.categoryCombo = categoryCombo;
    }
    
    public DataElementCategory getCategory() 
    {
        return category;
    }
    
    public void setCategory( DataElementCategory category )
    {
        this.category = category;
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

