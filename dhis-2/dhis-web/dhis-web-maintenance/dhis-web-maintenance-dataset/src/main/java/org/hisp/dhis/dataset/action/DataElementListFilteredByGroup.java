package org.hisp.dhis.dataset.action;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.Action;

/**
 * @author Kristian
 * @version $Id: DataElementListFilteredByGroup.java 3575 2007-09-28 17:22:01Z larshelg $
 */
public class DataElementListFilteredByGroup
    implements Action
{
    private String dataElementGroupId;

    private String selectedDataElements[];

    private List<DataElement> dataElements;

    private Integer dataSetId;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
    }

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public void setDataElementGroupId( String dataElementGroupId )
    {
        this.dataElementGroupId = dataElementGroupId;
    }

    public void setSelectedDataElements( String[] selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    public String getDataElementGroupId()
    {
        return dataElementGroupId;
    }

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if (  dataElementGroupId == null || dataElementGroupId.equals( "ALL" ) )
        {
            dataElements = new ArrayList<DataElement>( dataElementService.getAllActiveDataElements() );
        }
        else
        {
            DataElementGroup dataElementGroup = dataElementService.getDataElementGroup( Integer
                .parseInt( dataElementGroupId ) );

            dataElements = new ArrayList<DataElement>( dataElementGroup.getMembers() );
        }

        if ( selectedDataElements != null && selectedDataElements.length > 0 )
        {
            Iterator<DataElement> iter = dataElements.iterator();

            while ( iter.hasNext() )
            {
                DataElement dataElement = iter.next();

                for ( int i = 0; i < selectedDataElements.length; i++ )
                {
                    if ( dataElement.getId() == Integer.parseInt( selectedDataElements[i] ) )
                    {
                        iter.remove();
                    }
                }
            }
        }

        if ( dataSetId != null )
        {
            DataSet dataSet = dataSetService.getDataSet( dataSetId );

            dataElements.removeAll( dataSet.getDataElements() );
        }

        Collections.sort( dataElements, dataElementComparator );

        dataElements = displayPropertyHandler.handleDataElements( dataElements );

        return SUCCESS;
    }
}
