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
import java.util.List;

/**
 * @author Lars Helge Overland
 * @version $Id: IXFConfiguration.java 5484 2008-07-02 13:29:43Z larshelg $
 */
public class IXFConfiguration
    implements Serializable
{
    public static final String SOURCE = "District Health Information System 2";
    public static final String SOURCE_KEY = "25E2917E-FA2A-440C-A00B-49F8E609035F";

    private String source;
    
    private String sourceKey;

    private String comment;
    
    private IXFCountry country;
    
    private List<String> levelNames;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public IXFConfiguration()
    {   
    }

    public IXFConfiguration(  String comment, IXFCountry country, List<String> levelNames )
    {
        this.source = SOURCE;
        this.sourceKey = SOURCE_KEY;
        this.comment = comment;
        this.country = country;
        this.levelNames = levelNames;
    }

    public IXFConfiguration( String source, String sourceKey, String comment, IXFCountry country, List<String> levelNames )
    {
        this.source = source;
        this.sourceKey = sourceKey;
        this.comment = comment;
        this.country = country;
        this.levelNames = levelNames;
    }

    // -------------------------------------------------------------------------
    // equals and hashCode
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + ( ( comment == null ) ? 0 : comment.hashCode() );
        result = prime * result + ( ( country == null ) ? 0 : country.hashCode() );
        result = prime * result + ( ( levelNames == null ) ? 0 : levelNames.hashCode() );
        result = prime * result + ( ( source == null ) ? 0 : source.hashCode() );
        result = prime * result + ( ( sourceKey == null ) ? 0 : sourceKey.hashCode() );
        
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
        
        final IXFConfiguration other = (IXFConfiguration) object;
        
        if ( comment == null )
        {
            if ( other.comment != null )
            {
                return false;
            }
        }
        else if ( !comment.equals( other.comment ) )
        {
            return false;
        }
        
        if ( country == null )
        {
            if ( other.country != null )
            {
                return false;
            }
        }
        else if ( !country.equals( other.country ) )
        {
            return false;
        }
        
        if ( levelNames == null )
        {
            if ( other.levelNames != null )
            {
                return false;
            }
        }
        else if ( !levelNames.equals( other.levelNames ) )
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
        
        if ( sourceKey == null )
        {
            if ( other.sourceKey != null )
            {
                return false;
            }
        }
        else if ( !sourceKey.equals( other.sourceKey ) )
        {
            return false;
        }
        
        return true;
    }    
    
    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    public String getSource()
    {
        return source;
    }

    public void setSource( String source )
    {
        this.source = source;
    }
    
    public String getSourceKey()
    {
        return sourceKey;
    }

    public void setSourceKey( String sourceKey )
    {
        this.sourceKey = sourceKey;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public IXFCountry getCountry()
    {
        return country;
    }

    public void setCountry( IXFCountry country )
    {
        this.country = country;
    }

    public List<String> getLevelNames()
    {
        return levelNames;
    }

    public void setLevelNames( List<String> levelNames )
    {
        this.levelNames = levelNames;
    }
}
