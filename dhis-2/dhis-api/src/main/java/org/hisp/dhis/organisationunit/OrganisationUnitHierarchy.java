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

import java.util.Date;
import java.util.Map;

/**
 * The purpose of the OrganisationUnitHierarchy object is to store the parent-child relationship of the
 * registered organisation units together with a timestamp. The parent-child relationships are
 * stored in a Map, where the key column stores the organisation unit id and the value column
 * stores the id of the parent organisation unit.
 * 
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitHierarchy.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class OrganisationUnitHierarchy
{
    private int id;
    
    private Date date;
    
    private Map<Integer, Integer> structure;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    public OrganisationUnitHierarchy()
    {
    }
    
    public OrganisationUnitHierarchy( Date date, Map<Integer, Integer> structure )
    {
        this.date = date;
        this.structure = structure;
    }
    
    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------
        
    public int getId()
    {
        return id;
    }
    
    public void setId( int id )
    {
        this.id = id;
    }

    public Map<Integer, Integer> getStructure()
    {
        return structure;
    }

    public void setStructure( Map<Integer, Integer> structure )
    {
        this.structure = structure;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
