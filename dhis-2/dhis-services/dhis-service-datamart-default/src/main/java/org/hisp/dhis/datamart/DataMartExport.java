package org.hisp.dhis.datamart;

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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataMartExport
    implements Serializable
{
    private int id;
    
    private String name;
    
    private Set<DataElement> dataElements = new HashSet<DataElement>();
    
    private Set<Indicator> indicators = new HashSet<Indicator>();
    
    private Set<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();
    
    private Set<Period> periods = new HashSet<Period>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataMartExport()
    {   
    }

    public DataMartExport( String name, Set<DataElement> dataElements, Set<Indicator> indicators,
        Set<OrganisationUnit> organisationUnits, Set<Period> periods )
    {
        this.name = name;
        this.dataElements = dataElements;
        this.indicators = indicators;
        this.organisationUnits = organisationUnits;
        this.periods = periods;
    }

    // -------------------------------------------------------------------------
    // Getters & setters
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

    public Set<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( Set<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public Set<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( Set<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    public Set<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Set<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    public Set<Period> getPeriods()
    {
        return periods;
    }

    public void setPeriods( Set<Period> periods )
    {
        this.periods = periods;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        
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
        
        final DataMartExport other = (DataMartExport) object;
        
        return name.equals( other.name );
    }
}
