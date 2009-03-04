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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.common.MetaObject;
import org.hisp.dhis.source.Source;

/**
 * @author Kristian Nordal
 * @version $Id: OrganisationUnit.java 5540 2008-08-19 10:47:07Z larshelg $
 */
public class OrganisationUnit
    extends Source
    implements Serializable, MetaObject
{
    private String name;
    
    private String uuid;

    private Set<OrganisationUnit> children = new HashSet<OrganisationUnit>();

    private OrganisationUnit parent;

    private String shortName;

    private String organisationUnitCode;
    
    private Date openingDate;

    private Date closedDate;

    private boolean active;

    private String comment;
    
    private String geoCode;
    
    private String latitude;
    
    private String longitude;
    
    private transient int level;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnit()
    {
    }

    /**
     * @param name
     * @param shortName
     * @param organisationUnitCode
     * @param openingDate
     * @param closedDate
     * @param active
     * @param comment
     */
    public OrganisationUnit( String name, String shortName, String organisationUnitCode, Date openingDate,
        Date closedDate, boolean active, String comment )
    {
        this.name = name;
        this.shortName = shortName;
        this.organisationUnitCode = organisationUnitCode;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.active = active;
        this.comment = comment;
    }

    /**
     * @param name
     * @param parent
     * @param shortName
     * @param organisationUnitCode
     * @param openingDate
     * @param closedDate
     * @param active
     * @param comment
     */
    public OrganisationUnit( String name, OrganisationUnit parent, String shortName, String organisationUnitCode,
        Date openingDate, Date closedDate, boolean active, String comment )
    {
        this.name = name;
        this.parent = parent;
        this.shortName = shortName;
        this.organisationUnitCode = organisationUnitCode;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.active = active;
        this.comment = comment;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
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

        if ( !(o instanceof OrganisationUnit) )
        {
            return false;
        }

        final OrganisationUnit other = (OrganisationUnit) o;

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

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid( String uuid )
    {
        this.uuid = uuid;
    }

    public Set<OrganisationUnit> getChildren()
    {
        return children;
    }

    public void setChildren( Set<OrganisationUnit> children )
    {
        this.children = children;
    }

    public OrganisationUnit getParent()
    {
        return parent;
    }

    public void setParent( OrganisationUnit parent )
    {
        this.parent = parent;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    public String getOrganisationUnitCode()
    {
        return organisationUnitCode;
    }

    public void setOrganisationUnitCode( String organisationUnitCode )
    {
        this.organisationUnitCode = organisationUnitCode;
    }

    public Date getOpeningDate()
    {
        return openingDate;
    }

    public void setOpeningDate( Date openingDate )
    {
        this.openingDate = openingDate;
    }

    public Date getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate( Date closedDate )
    {
        this.closedDate = closedDate;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive( boolean active )
    {
        this.active = active;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public String getGeoCode()
    {
        return geoCode;
    }

    public void setGeoCode( String geoCode )
    {
        this.geoCode = geoCode;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude( String latitude )
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude( String longitude )
    {
        this.longitude = longitude;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
    }
}
