package org.hisp.dhis.importexport.ixf.config;

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
 * @version $Id: IXFCountry.java 5484 2008-07-02 13:29:43Z larshelg $
 */
public class IXFCountry
    implements Serializable
{
    private String key;

    private String number;
    
    private String name;
    
    private String longName;
    
    private String isoCode;
    
    private String levelNumber;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public IXFCountry()
    {   
    }
    
    public IXFCountry( String key, String number, String name, 
        String longName, String isoCode, String levelNumber )
    {
        this.key = key;
        this.number = number;
        this.name = name;
        this.longName = longName;
        this.isoCode = isoCode;
        this.levelNumber = levelNumber;
    }

    // -------------------------------------------------------------------------
    // equals and hashCode
    // -------------------------------------------------------------------------
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + ( ( isoCode == null) ? 0 : isoCode.hashCode() );
        result = prime * result + ( ( key == null) ? 0 : key.hashCode() );
        result = prime * result + ( ( levelNumber == null) ? 0 : levelNumber.hashCode() );
        result = prime * result + ( ( longName == null) ? 0 : longName.hashCode() );
        result = prime * result + ( ( name == null) ? 0 : name.hashCode() );
        result = prime * result + ( ( number == null) ? 0 : number.hashCode() );
        
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
        
        final IXFCountry other = (IXFCountry) object;
        
        if ( isoCode == null )
        {
            if ( other.isoCode != null )
            {
                return false;
            }
        }
        else if ( !isoCode.equals( other.isoCode ) )
        {
            return false;
        }
        
        if ( key == null )
        {
            if ( other.key != null )
            {
                return false;
            }
        }
        else if ( !key.equals( other.key ) )
        {
            return false;
        }
        
        if ( levelNumber == null )
        {
            if ( other.levelNumber != null )
            {
                return false;
            }
        }
        else if ( !levelNumber.equals( other.levelNumber ) )
        {
            return false;
        }
        
        if ( longName == null )
        {
            if ( other.longName != null )
            {
                return false;
            }
        }
        else if ( !longName.equals( other.longName ) )
        {
            return false;
        }
        
        if ( name == null )
        {
            if ( other.name != null )
            {
                return false;
            }
        }
        else if ( !name.equals( other.name ) )
        {
            return false;
        }
        
        if ( number == null )
        {
            if ( other.number != null )
            {
                return false;
            }
        }
        else if ( !number.equals( other.number ) )
        {
            return false;
        }
        
        return true;
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------
    
    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber( String number )
    {
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getIsoCode()
    {
        return isoCode;
    }

    public void setIsoCode( String isoCode )
    {
        this.isoCode = isoCode;
    }

    public String getLevelNumber()
    {
        return levelNumber;
    }

    public void setLevelNumber( String levelNumber )
    {
        this.levelNumber = levelNumber;
    }
}
