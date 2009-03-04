package org.hisp.dhis.user;

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


import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Nguyen Hong Duc
 * @version $Id: User.java 5554 2008-08-20 09:18:38Z abyot $
 */
public class User
    implements Serializable
{
    private int id;

    /**
     * Required.
     */
    private String surname;

    /**
     * Required.
     */
    private String firstName;

    private String email;

    /**
     * All OrgUnits where the user could belong
     */
    private Collection<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();
 
    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    /**
     * No real candidate key here. Doing the best we can.
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = result * prime + surname.hashCode();
        result = result * prime + firstName.hashCode();
        result = result * prime + (email != null ? email.hashCode() : 0);      

        return result;
    }

    /**
     * No real candidate key here. Doing the best we can.
     */
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

        if ( !(o instanceof User) )
        {
            return false;
        }

        final User other = (User) o;

        return surname.equals( other.getSurname() )
            && firstName.equals( other.getFirstName() )
            && ((email == null && other.getEmail() == null) || email.equals( other.getEmail() ));
            
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

    public String getSurname()
    {
        return surname;
    }

    public void setSurname( String surname )
    {
        this.surname = surname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }
    
    public Collection<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Collection<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }
    
}
