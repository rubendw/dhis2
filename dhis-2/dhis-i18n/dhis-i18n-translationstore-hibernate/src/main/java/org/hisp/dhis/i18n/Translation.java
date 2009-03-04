package org.hisp.dhis.i18n;

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
 * @author Oyvind Brucker
 */
public class Translation implements Serializable
{
    private String className;

    private int id;

    private String locale;

    private String property;

    private String value;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Translation( String className, int id, String locale, String property, String value )
    {
        this.className = className;
        this.id = id;
        this.locale = locale;
        this.property = property;
        this.value = value;
    }

    public Translation()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public String getClassName()
    {
        return className;
    }

    public void setClassName( String className )
    {
        this.className = className;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale( String locale )
    {
        this.locale = locale;
    }

    public String getProperty()
    {
        return property;
    }

    public void setProperty( String property )
    {
        this.property = property;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = result * prime + className.hashCode();
        result = result * prime + id;
        result = result * prime + locale.hashCode();
        result = result * prime + property.hashCode();
        
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

        if ( !( o instanceof Translation ) )
        {
            return false;
        }

        Translation translation = (Translation) o;

        return className.equals( translation.getClassName() ) && id == translation.getId() &&
            locale.equals( translation.getLocale() ) && property.equals( translation.getProperty());
    }

    @Override
    public String toString()
    {
        return "Translation className=" + className + " id=" + id + " locale=" + locale + " property=" + property +
            " value=" + value;
    }
}