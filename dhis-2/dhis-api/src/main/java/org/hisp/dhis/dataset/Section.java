package org.hisp.dhis.dataset;

/*
 * Copyright (c) 2004-2007, University of Oslo All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the HISP project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;

public class Section
    implements Serializable
{
    private int id;

    private String name;

    private String label;

    private DataSet dataSet;

    private List<DataElement> dataElements = new ArrayList<DataElement>();

    private int sortOrder;

    public Section()
    {
    }

    public Section( int id, DataSet dataSet, List<DataElement> dataElements )
    {
        this.id = id;
        this.dataSet = dataSet;
        this.dataElements = dataElements;
    }

    public Section( String name, DataSet dataSet )
    {
        this.name = name;
        this.dataSet = dataSet;
    }

    public Section( int id, String name, DataSet dataSet, List<DataElement> dataElements )
    {
        this.id = id;
        this.name = name;
        this.dataSet = dataSet;
        this.dataElements = dataElements;
    }

    public int getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

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

        if ( !(o instanceof Section) )
        {
            return false;
        }
        final Section other = (Section) o;
        return name.equals( other.getName() );
    }

    // ------------------------------------------------------------------
    // getter and setter
    // ------------------------------------------------------------------
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

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( DataSet dataSet )
    {
        this.dataSet = dataSet;
    }

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public void addDataElement( DataElement dataElement )
    {
        this.dataElements.add( dataElement );
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }
}
