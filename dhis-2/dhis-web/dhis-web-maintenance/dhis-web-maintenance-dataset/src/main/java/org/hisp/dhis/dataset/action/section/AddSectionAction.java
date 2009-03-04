package org.hisp.dhis.dataset.action.section;

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

import com.opensymphony.xwork.Action;
import java.util.*;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementNameComparator;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;

public class AddSectionAction
    implements Action
{
    private DataSetService dataSetService;

    private DataElementService dataElementService;

    private SectionService sectionService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer dataSetId;

    private String sectionName;

    private String sectionLabel;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<String> selectedList = new ArrayList<String>();

    private List<DataElement> dataElements = new ArrayList<DataElement>();

    private DataSet dataset;

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    public void setSectionName( String sectionName )
    {
        this.sectionName = sectionName;
    }

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    public List<String> getSelectedList()
    {
        return selectedList;
    }

    public void setSelectedList( List<String> selectedList )
    {
        this.selectedList = selectedList;
    }

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    public void setSectionService( SectionService sectionService )
    {
        this.sectionService = sectionService;
    }

    public void setSectionLabel( String sectionLabel )
    {
        this.sectionLabel = sectionLabel;
    }

    public Integer getDataSetId()
    {
        return dataSetId;
    }

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public DataSet getDataset()
    {
        return dataset;
    }

    public void setDataset( DataSet dataset )
    {
        this.dataset = dataset;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( dataSetId == null )
        {
            return INPUT;

        }
        else
        {
            dataset = dataSetService.getDataSet( dataSetId.intValue() );

            dataElements = new ArrayList<DataElement>( dataset.getDataElements() );

            Collection<Section> sections = sectionService.getSectionByDataSet( dataset );

            for ( Section section : sections )
            {
                dataElements.removeAll( section.getDataElements() );
            }

            Collections.sort( dataElements, new DataElementNameComparator() );
        }

        if ( sectionName == null )
        {
            return INPUT;
        }
        else
        {
            sectionName = sectionName.trim();
            if ( sectionName.length() == 0 )
            {
                return INPUT;
            }
        }

        if ( sectionLabel == null )
        {
            return INPUT;
        }
        else
        {
            sectionLabel = sectionLabel.trim();
            if ( sectionLabel.length() == 0 )
            {
                return INPUT;
            }
        }
        if ( selectedList.size() == 0 )
        {
            return INPUT;
        }

        Section section = new Section();

        section.setName( sectionName );

        section.setLabel( sectionLabel );

        section.setSortOrder( 0 );

        section.setDataSet( dataSetService.getDataSet( dataSetId.intValue() ) );

        for ( String id : selectedList )
        {
            DataElement dataElement = dataElementService.getDataElement( Integer.parseInt( id ) );
            section.addDataElement( dataElement );
        }

        sectionService.addSection( section );

        return SUCCESS;
    }
}
