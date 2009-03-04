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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Abyot Aselefew
 * @version $Id$
 */
public class DataElementCategoryOptionCombo
    implements Serializable
{
    /**
     * The database internal identifier for this DataElementCategoryOption.
     */
    private int id;  
    
    /**
     * 
     */
    private DataElementCategoryCombo categoryCombo;
    
    /**
     * The category options for this DataElementCategoryOptionsCombo.
     */
    private Set<DataElementCategoryOption> categoryOptions = new HashSet<DataElementCategoryOption>();
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementCategoryOptionCombo()
    {
    }
    
    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + ( ( categoryCombo == null ) ? 0 : categoryCombo.hashCode() );
        
        result = prime * result + ( ( categoryOptions == null ) ? 0 : categoryOptions.hashCode() );
        
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
        
        final DataElementCategoryOptionCombo other = (DataElementCategoryOptionCombo) object;
        
        if ( categoryCombo == null )
        {
            if ( other.categoryCombo != null )
            {
                return false;
            }
        }
        else if ( !categoryCombo.equals( other.categoryCombo ) )
        {
            return false;
        }
        
        if ( categoryOptions == null )
        {
            if ( other.categoryOptions != null )
            {
                return false;
            }
        }
        else if ( !categoryOptions.equals( other.categoryOptions ) )
        {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "[" + categoryCombo + ", [" );
        
        Iterator<DataElementCategoryOption> iterator = categoryOptions.iterator();
        
        while ( iterator.hasNext() )
        {
            buffer.append( iterator.next().toString() );
            
            if ( iterator.hasNext() )
            {
                buffer.append( ", " );
            }
        }
        
        buffer.append( "]]" );
        
        return buffer.toString();
    }

    /**
     * Tests whether two objects compare on a name basis. The default equals method
     * becomes unusable in conjunction with persistence frameworks that put proxys
     * on associated objects and collections, since it tests the class type which
     * will differ between the proxy and the raw type.
     * 
     * @param object the object to test for equality.
     * @return true if objects are equal, false otherwise.
     */
    public boolean equalsOnName( DataElementCategoryOptionCombo object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( object.getCategoryCombo() == null )
        {
            return false;
        }
        
        if ( object.getCategoryOptions() == null )
        {
            return false;
        }
        
        if ( !categoryCombo.getName().equals( object.getCategoryCombo().getName() ) )
        {
            return false;
        }
        
        if ( categoryOptions.size() != object.getCategoryOptions().size() )
        {
            return false;
        }
        
        Set<String> names1 = new HashSet<String>();
        Set<String> names2 = new HashSet<String>();
        
        for ( DataElementCategoryOption option : categoryOptions )
        {
            names1.add( option.getName() );
        }
        
        for ( DataElementCategoryOption option : object.getCategoryOptions() )
        {
            names2.add( option.getName() );
        }
        
        return names1.equals( names2 );
    }
    
    /**
     * Tests if this object equals to an object in the given Collection on a name basis.
     * 
     * @param categoryOptionCombos the Collection.
     * @return true if the Collection contains this object, false otherwise.
     */
    public DataElementCategoryOptionCombo get( Collection<DataElementCategoryOptionCombo> categoryOptionCombos )
    {
        for ( DataElementCategoryOptionCombo combo : categoryOptionCombos )
        {
            if ( combo.equalsOnName( this ) )
            {
                return combo;
            }
        }
        
        return null;
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }
    
    public void setId( int id )
    {
        this.id = id;
    }
    
    public DataElementCategoryCombo getCategoryCombo()
    {
        return categoryCombo;
    }
    
    public void setCategoryCombo ( DataElementCategoryCombo categoryCombo )
    {
        this.categoryCombo = categoryCombo;
    }
    
    public Set<DataElementCategoryOption> getCategoryOptions()
    {
        return categoryOptions;
    }
    
    public void setCategoryOptions( Set<DataElementCategoryOption> categoryOptions )
    {
        this.categoryOptions = categoryOptions;
    }
}
