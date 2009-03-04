package org.hisp.dhis.importexport;

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

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GroupMemberAssociation
    implements Serializable
{
    private int groupId;
    
    private int memberId;
    
    private int sortOrder;
    
    private AssociationType associationType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unused" )
    private GroupMemberAssociation()
    {   
    }
    
    public GroupMemberAssociation( AssociationType associationType )
    {
        this.associationType = associationType;
    }

    public GroupMemberAssociation( int groupId, int memberId )
    {
        this.groupId = groupId;
        this.memberId = memberId;
        this.associationType = AssociationType.SET;
    }

    public GroupMemberAssociation( int groupId, int memberId, int sortOrder )
    {
        this.groupId = groupId;
        this.memberId = memberId;
        this.sortOrder = sortOrder;
        this.associationType = AssociationType.LIST;
    }
    
    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {        
        return 31 * groupId * memberId;
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

        if ( !(o instanceof GroupMemberAssociation) )
        {
            return false;
        }

        final GroupMemberAssociation other = (GroupMemberAssociation) o;

        return groupId == other.getGroupId() && 
            memberId == other.getMemberId() && sortOrder == other.getSortOrder();
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId( int groupId )
    {
        this.groupId = groupId;
    }

    public int getMemberId()
    {
        return memberId;
    }

    public void setMemberId( int memberId )
    {
        this.memberId = memberId;
    }

    public int getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    public AssociationType getAssociationType()
    {
        return associationType;
    }

    public void setAssociationType( AssociationType associationType )
    {
        this.associationType = associationType;
    }    
}
