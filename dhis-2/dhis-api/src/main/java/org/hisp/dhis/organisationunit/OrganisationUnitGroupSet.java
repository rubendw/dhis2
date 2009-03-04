package org.hisp.dhis.organisationunit;

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
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.organisationunit.OrganisationUnitGroup;

/**
 * @author Kristian Nordal
 * @version $Id: OrganisationUnitGroupSet.java 1905 2006-09-23 14:34:55Z torgeilo $
 */
public class OrganisationUnitGroupSet
    implements Serializable
{
    private int id;

    private String name;

    private String description;

    private Set<OrganisationUnitGroup> organisationUnitGroups = new HashSet<OrganisationUnitGroup>();

    private boolean compulsory;

    private boolean exclusive;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitGroupSet()
    {
    }

    public OrganisationUnitGroupSet( String name, String description, boolean compulsory, boolean exclusive )
    {
        this.name = name;
        this.description = description;
        this.compulsory = compulsory;
        this.exclusive = exclusive;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof OrganisationUnitGroupSet) )
        {
            return false;
        }

        final OrganisationUnitGroupSet other = (OrganisationUnitGroupSet) o;

        return name.equals( other.getName() );
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

    public boolean isCompulsory()
    {
        return compulsory;
    }

    public void setCompulsory( boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    public boolean isExclusive()
    {
        return exclusive;
    }

    public void setExclusive( boolean exclusive )
    {
        this.exclusive = exclusive;
    }

    public Set<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups( Set<OrganisationUnitGroup> organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }
}
