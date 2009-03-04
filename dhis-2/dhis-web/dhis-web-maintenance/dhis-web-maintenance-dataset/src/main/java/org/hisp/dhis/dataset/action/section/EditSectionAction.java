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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;

import com.opensymphony.xwork.Action;

public class EditSectionAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SectionService sectionService;

    private DataSetService dataSetService;

    public void setSectionService( SectionService sectionService )
    {
        this.sectionService = sectionService;
    }

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------

    private Integer sectionId;

    private Section section;

    private List<DataElement> selectedList = new ArrayList<DataElement>();

    private DataSet dataSet;

    private Collection<DataElement> dataElementOfDataSet = new ArrayList<DataElement>();

    public Integer getSectionId()
    {
        return sectionId;
    }

    public void setSectionId( Integer sectionId )
    {
        this.sectionId = sectionId;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public List<DataElement> getSelectedList()
    {
        return selectedList;
    }

    public void setSelectedList( List<DataElement> selectedList )
    {
        this.selectedList = selectedList;
    }

    public Collection<DataElement> getDataElementOfDataSet()
    {
        return dataElementOfDataSet;
    }

    public void setDataElementOfDataSet( Collection<DataElement> dataElementOfDataSet )
    {
        this.dataElementOfDataSet = dataElementOfDataSet;
    }

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( DataSet dataSet )
    {
        this.dataSet = dataSet;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        section = sectionService.getSection( sectionId.intValue() );

        selectedList = section.getDataElements();

        dataSet = dataSetService.getDataSet( section.getDataSet().getId() );

        dataElementOfDataSet = dataSet.getDataElements();

        Collection<Section> sections = sectionService.getSectionByDataSet( dataSet );

        for ( Section s : sections )
        {
            dataElementOfDataSet.removeAll( s.getDataElements() );

        }

        return SUCCESS;
    }
}
