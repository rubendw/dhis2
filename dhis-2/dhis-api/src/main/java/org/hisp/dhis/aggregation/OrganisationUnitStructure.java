package org.hisp.dhis.aggregation;

import java.io.Serializable;

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
 * @version $Id: OrganisationUnitStructure.java 2694 2007-01-12 19:19:46Z torgeilo $
 */
public class OrganisationUnitStructure
    implements Serializable
{
    private int id;
    
    private int organisationUnitId;
    
    private int level;
    
    private Integer idLevel1;    
    private Integer idLevel2;
    private Integer idLevel3;
    private Integer idLevel4;    
    private Integer idLevel5;
    private Integer idLevel6;
    private Integer idLevel7;
    private Integer idLevel8;
    
    private String geoCodeLevel1;
    private String geoCodeLevel2;
    private String geoCodeLevel3;
    private String geoCodeLevel4;
    private String geoCodeLevel5;
    private String geoCodeLevel6;
    private String geoCodeLevel7;
    private String geoCodeLevel8;

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    public OrganisationUnitStructure()
    {        
    }
    
    // ----------------------------------------------------------------------
    // Getters & setters
    // ----------------------------------------------------------------------

    public int getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( int organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }
    
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
    }

    public Integer getIdLevel1()
    {
        return idLevel1;
    }

    public void setIdLevel1( Integer idLevel1 )
    {
        this.idLevel1 = idLevel1;
    }

    public Integer getIdLevel2()
    {
        return idLevel2;
    }

    public void setIdLevel2( Integer idLevel2 )
    {
        this.idLevel2 = idLevel2;
    }

    public Integer getIdLevel3()
    {
        return idLevel3;
    }

    public void setIdLevel3( Integer idLevel3 )
    {
        this.idLevel3 = idLevel3;
    }

    public Integer getIdLevel4()
    {
        return idLevel4;
    }

    public void setIdLevel4( Integer idLevel4 )
    {
        this.idLevel4 = idLevel4;
    }

    public Integer getIdLevel5()
    {
        return idLevel5;
    }

    public void setIdLevel5( Integer idLevel5 )
    {
        this.idLevel5 = idLevel5;
    }

    public Integer getIdLevel6()
    {
        return idLevel6;
    }

    public void setIdLevel6( Integer idLevel6 )
    {
        this.idLevel6 = idLevel6;
    }

    public Integer getIdLevel7()
    {
        return idLevel7;
    }

    public void setIdLevel7( Integer idLevel7 )
    {
        this.idLevel7 = idLevel7;
    }

    public Integer getIdLevel8()
    {
        return idLevel8;
    }

    public void setIdLevel8( Integer idLevel8 )
    {
        this.idLevel8 = idLevel8;
    }

    public String getGeoCodeLevel1()
    {
        return geoCodeLevel1;
    }

    public void setGeoCodeLevel1( String geoCodeLevel1 )
    {
        this.geoCodeLevel1 = geoCodeLevel1;
    }

    public String getGeoCodeLevel2()
    {
        return geoCodeLevel2;
    }

    public void setGeoCodeLevel2( String geoCodeLevel2 )
    {
        this.geoCodeLevel2 = geoCodeLevel2;
    }

    public String getGeoCodeLevel3()
    {
        return geoCodeLevel3;
    }

    public void setGeoCodeLevel3( String geoCodeLevel3 )
    {
        this.geoCodeLevel3 = geoCodeLevel3;
    }

    public String getGeoCodeLevel4()
    {
        return geoCodeLevel4;
    }

    public void setGeoCodeLevel4( String geoCodeLevel4 )
    {
        this.geoCodeLevel4 = geoCodeLevel4;
    }

    public String getGeoCodeLevel5()
    {
        return geoCodeLevel5;
    }

    public void setGeoCodeLevel5( String geoCodeLevel5 )
    {
        this.geoCodeLevel5 = geoCodeLevel5;
    }

    public String getGeoCodeLevel6()
    {
        return geoCodeLevel6;
    }

    public void setGeoCodeLevel6( String geoCodeLevel6 )
    {
        this.geoCodeLevel6 = geoCodeLevel6;
    }

    public String getGeoCodeLevel7()
    {
        return geoCodeLevel7;
    }

    public void setGeoCodeLevel7( String geoCodeLevel7 )
    {
        this.geoCodeLevel7 = geoCodeLevel7;
    }

    public String getGeoCodeLevel8()
    {
        return geoCodeLevel8;
    }

    public void setGeoCodeLevel8( String geoCodeLevel8 )
    {
        this.geoCodeLevel8 = geoCodeLevel8;
    }
}
