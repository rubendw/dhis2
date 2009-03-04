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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;

import com.opensymphony.xwork.Action;

public class SortOrderSection
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

    private Integer dataSetId;

    private List<String> selectedList = new ArrayList<String>();;

    private DataSet dataset;

    private Set<Section> sections = new HashSet<Section>();

    public Set<Section> getSections()
    {
        return sections;
    }

    public DataSet getDataset()
    {
        return dataset;
    }

    public Integer getDataSetId()
    {
        return dataSetId;
    }

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    public void setSelectedList( List<String> selectedList )
    {
        this.selectedList = selectedList;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        Collection<Section> temps = sectionService.getAllSections();

        if ( dataSetId != null )
        {
            dataset = dataSetService.getDataSet( dataSetId.intValue() );

            for ( Section section : temps )
            {
                if ( section.getDataSet().getId() == dataset.getId() )
                {

                    sections.add( section );
                }
            }

            return INPUT;
        }

        if ( selectedList.size() == 0 )
        {
            return INPUT;
        }
        
        int i = 0;
        
        for ( String id : selectedList )
        {
            Section temp = sectionService.getSection( Integer.parseInt( id ) );
            temp.setSortOrder( i );
            System.out.println( temp.getSortOrder() );
            sectionService.updateSection( temp );
            i++;
        }

        return SUCCESS;
    }
}
