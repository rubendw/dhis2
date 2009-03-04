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

import java.util.Date;

/**
 * @author Lars Helge Overland
 * @version $Id: Dhis14RoutineDataValue.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class Dhis14RoutineDataValue
{
    private int dataElementId;
    
    private int periodId;
    
    private int organisationUnitId;
    
    private Integer value;
    
    private String storedBy;
    
    private Date timeStamp;
    
    private String comment;

    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------
    
    public Dhis14RoutineDataValue()
    {   
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId( int periodId )
    {
        this.periodId = periodId;
    }

    public int getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( int organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

    public String getStoredBy()
    {
        return storedBy;
    }

    public void setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
    }
    
    public Date getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp( Date timeStamp )
    {
        this.timeStamp = timeStamp;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }
}
