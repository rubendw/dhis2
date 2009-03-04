package org.hisp.dhis.importexport.dhis14.object;

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

/**
 * @author Lars Helge Overland
 * @version $Id: Dhis14OrganisationUnitStructure.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class Dhis14OrganisationUnitStructure
{
    private String organisationUnitName;
    
    private int level;
    
    private String nameLevel1;
    
    private String nameLevel2;
    
    private String nameLevel3;
    
    private String nameLevel4;
    
    private String nameLevel5;
    
    private String nameLevel6;
    
    private String nameLevel7;
    
    private String nameLevel8;
    
    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------
    
    public Dhis14OrganisationUnitStructure()
    {        
    }

    // ----------------------------------------------------------------------
    // Getters & setters
    // ----------------------------------------------------------------------

    public String getOrganisationUnitName()
    {
        return organisationUnitName;
    }

    public void setOrganisationUnitName( String organisationUnitName )
    {
        this.organisationUnitName = organisationUnitName;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
    }

    public String getNameLevel1()
    {
        return nameLevel1;
    }

    public void setNameLevel1( String nameLevel1 )
    {
        this.nameLevel1 = nameLevel1;
    }

    public String getNameLevel2()
    {
        return nameLevel2;
    }

    public void setNameLevel2( String nameLevel2 )
    {
        this.nameLevel2 = nameLevel2;
    }

    public String getNameLevel3()
    {
        return nameLevel3;
    }

    public void setNameLevel3( String nameLevel3 )
    {
        this.nameLevel3 = nameLevel3;
    }

    public String getNameLevel4()
    {
        return nameLevel4;
    }

    public void setNameLevel4( String nameLevel4 )
    {
        this.nameLevel4 = nameLevel4;
    }

    public String getNameLevel5()
    {
        return nameLevel5;
    }

    public void setNameLevel5( String nameLevel5 )
    {
        this.nameLevel5 = nameLevel5;
    }

    public String getNameLevel6()
    {
        return nameLevel6;
    }

    public void setNameLevel6( String nameLevel6 )
    {
        this.nameLevel6 = nameLevel6;
    }

    public String getNameLevel7()
    {
        return nameLevel7;
    }

    public void setNameLevel7( String nameLevel7 )
    {
        this.nameLevel7 = nameLevel7;
    }

    public String getNameLevel8()
    {
        return nameLevel8;
    }

    public void setNameLevel8( String nameLevel8 )
    {
        this.nameLevel8 = nameLevel8;
    }
}
