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
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidationRuleGroup
    implements Serializable
{
    private int id;
    
    private String name;
    
    private String description;
    
    private Set<ValidationRule> members = new HashSet<ValidationRule>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------     

    public ValidationRuleGroup()
    {   
    }
    
    public ValidationRuleGroup( String name, String description, Set<ValidationRule> members )
    {
        this.name = name;
        this.description = description;
        this.members = members;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------     

    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( !( object instanceof ValidationRuleGroup ) )
        {
            return false;
        }

        final ValidationRuleGroup validationRuleGroup = (ValidationRuleGroup) object;

        return name.equals( validationRuleGroup.getName() );
    }

    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
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

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public Set<ValidationRule> getMembers()
    {
        return members;
    }

    public void setMembers( Set<ValidationRule> members )
    {
        this.members = members;
    }
}
