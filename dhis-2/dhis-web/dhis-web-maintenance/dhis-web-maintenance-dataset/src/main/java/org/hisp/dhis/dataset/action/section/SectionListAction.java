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
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;
import org.hisp.dhis.dataset.comparator.DataSetNameComparator;
import org.hisp.dhis.dataset.comparator.SectionOrderComparator;

import com.opensymphony.xwork.Action;

public class SectionListAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SectionService sectionService;

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    public void setSectionService( SectionService sectionService )
    {
        this.sectionService = sectionService;
    }

    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------

    private List<Section> sections = new ArrayList<Section>();

    private List<DataSet> datasets = new ArrayList<DataSet>();

    private Integer dataSetId;

    public Collection<Section> getSections()
    {
        return sections;
    }

    public void setSections( List<Section> sections )
    {
        this.sections = sections;
    }

    public SectionService getSectionService()
    {
        return sectionService;
    }

    public List<DataSet> getDatasets()
    {
        return datasets;
    }

    public void setDatasets( List<DataSet> datasets )
    {
        this.datasets = datasets;
    }

    public Integer getDataSetId()
    {
        return dataSetId;
    }

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        datasets = new ArrayList<DataSet>( dataSetService.getAllDataSets() );
        
        Collections.sort( datasets, new DataSetNameComparator() );

        Collection<Section> temp = sectionService.getAllSections();

        if ( dataSetId == null )
        {
            sections = new ArrayList<Section>( temp );
        }
        else
        {
            sections = new ArrayList<Section>();
            
            for ( Section section : temp )
            {
                if ( section.getDataSet().getId() == dataSetId.intValue() )
                {
                    sections.add( section );
                }
            }
            
            Collections.sort( sections, new SectionOrderComparator() );
        }

        if ( sections == null )
        {
            sections = new ArrayList<Section>();
        }

        return SUCCESS;
    }
}
