package org.hisp.dhis.importexport;

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
 * @version $Id$
 */
public class ImportObject
    implements Serializable
{
    private int id;

    private ImportObjectStatus status;
    
    private String className;
    
    private GroupMemberType groupMemberType;
    
    private Object object;
    
    private Object compareObject;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ImportObject()
    {
    }
    
    public ImportObject( ImportObjectStatus status, String className, Object object )
    {
        this.status = status;
        this.className = className;
        this.groupMemberType = GroupMemberType.NONE;
        this.object = object;
        this.compareObject = null;
    }

    public ImportObject( ImportObjectStatus status, String className, GroupMemberType groupMemberType, Object object )
    {
        this.status = status;
        this.className = className;
        this.groupMemberType = groupMemberType;
        this.object = object;
        this.compareObject = null;
    }
    
    public ImportObject( ImportObjectStatus status, String className, Object object, Object compareObject )
    {
        this.status = status;
        this.className = className;
        this.groupMemberType = GroupMemberType.NONE;
        this.object = object;
        this.compareObject = compareObject;
    }

    public ImportObject( ImportObjectStatus status, String className, GroupMemberType groupMemberType, Object object, Object compareObject )
    {
        this.status = status;
        this.className = className;
        this.groupMemberType = groupMemberType;
        this.object = object;
        this.compareObject = compareObject;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + className.hashCode();
        
        result = PRIME * result + object.hashCode();
        
        return result;
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

        if ( !( o instanceof ImportObject ) )
        {
            return false;
        }

        final ImportObject other = (ImportObject) o;

        return status.equals( other.getStatus() ) && className.equals( other.getClassName() ) &&
            groupMemberType.equals( other.getGroupMemberType() ) && object.equals( other.getObject() );
    }
    
    @Override
    public String toString()
    {
        String string = "[Internal id: " + id + ", status: " + status.name() + 
            ", class name: " + className + ", group member type: " + groupMemberType.name() + "]";
        
        return string;
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

    public ImportObjectStatus getStatus()
    {
        return status;
    }

    public void setStatus( ImportObjectStatus status )
    {
        this.status = status;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName( String className )
    {
        this.className = className;
    }

    public GroupMemberType getGroupMemberType()
    {
        return groupMemberType;
    }

    public void setGroupMemberType( GroupMemberType groupMemberType )
    {
        this.groupMemberType = groupMemberType;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject( Object object )
    {
        this.object = object;
    }

    public Object getCompareObject()
    {
        return compareObject;
    }

    public void setCompareObject( Object compareObject )
    {
        this.compareObject = compareObject;
    }
}

