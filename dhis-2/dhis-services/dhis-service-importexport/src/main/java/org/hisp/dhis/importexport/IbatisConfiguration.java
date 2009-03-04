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
 * @version $Id: IbatisConfiguration.java 5517 2008-08-04 14:59:27Z larshelg $
 */
public class IbatisConfiguration
    implements Serializable
{
    private String dataFile;

    private String userName;

    private String password;

    private String levels;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public IbatisConfiguration()
    {
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;

        int result = 1;

        result = prime * result + ((dataFile == null) ? 0 : dataFile.hashCode());
        result = prime * result + ((levels == null) ? 0 : levels.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());

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
        
        final IbatisConfiguration other = (IbatisConfiguration) object;
        
        if ( dataFile == null )
        {
            if ( other.dataFile != null )
            {
                return false;
            }
        }
        else if ( !dataFile.equals( other.dataFile ) )
        {
            return false;
        }
        
        if ( levels == null )
        {
            if ( other.levels != null )
            {
                return false;
            }
        }
        else if ( !levels.equals( other.levels ) )
        {
            return false;
        }
        
        if ( password == null )
        {
            if ( other.password != null )
            {
                return false;
            }
        }
        else if ( !password.equals( other.password ) )
        {
            return false;
        }
        
        if ( userName == null )
        {
            if ( other.userName != null )
            {
                return false;
            }
        }
        else if ( !userName.equals( other.userName ) )
        {
            return false;
        }
        
        return true;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public String getDataFile()
    {
        return dataFile;
    }

    public void setDataFile( String dataFile )
    {
        this.dataFile = dataFile;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getLevels()
    {
        return levels;
    }

    public void setLevels( String levels )
    {
        this.levels = levels;
    }
}
