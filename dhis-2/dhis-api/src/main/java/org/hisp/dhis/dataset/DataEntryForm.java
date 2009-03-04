package org.hisp.dhis.dataset;

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

public class DataEntryForm
    implements Serializable
{
    /**
     * The unique identifier for this DataEntryForm
     */
    private int id;

    /**
     * Name of DataEntryForm. Required and unique.
     */
    private String name;

    /**
     * HTML Code of DataEntryForm
     */
    private String htmlCode;

    /**
     * The dataSet indicating the list of dataelements that this DataEntryForm
     * should be used
     */
    private DataSet dataSet;

    // -------------------------------------------------------------------------
    // Contructors
    // -------------------------------------------------------------------------

    public DataEntryForm()
    {
    }

    public DataEntryForm( String name )
    {
        this.name = name;
    }

    public DataEntryForm( String name, DataSet dataSet )
    {
        this.name = name;
        this.dataSet = dataSet;
    }

    public DataEntryForm( String name, String htmlCode )
    {
        this.name = name;
        this.htmlCode = htmlCode;
    }

    public DataEntryForm( String name, String htmlCode, DataSet dataSet )
    {
        this.name = name;
        this.htmlCode = htmlCode;
        this.dataSet = dataSet;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
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

        if ( !(o instanceof DataEntryForm) )
        {
            return false;
        }

        final DataEntryForm other = (DataEntryForm) o;

        return name.equals( other.getName() );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( DataSet dataSet )
    {
        this.dataSet = dataSet;
    }

    public String getHtmlCode()
    {
        return htmlCode;
    }

    public void setHtmlCode( String htmlCode )
    {
        this.htmlCode = htmlCode;
    }

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

}
