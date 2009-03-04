package org.hisp.dhis.dd.action.datadictionary;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

import com.opensymphony.xwork.Action;

import static org.hisp.dhis.system.util.TextUtils.nullIfEmpty;;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class AddDataDictionaryAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataDictionaryService dataDictionaryService;

    public void setDataDictionaryService( DataDictionaryService dataDictionaryService )
    {
        this.dataDictionaryService = dataDictionaryService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }
    
    private String region;

    public void setRegion( String region )
    {
        this.region = region;
    }

    private Collection<String> selectedDataElements;

    public void setSelectedDataElements( Collection<String> groupMembers )
    {
        this.selectedDataElements = groupMembers;
    }
    
    private Collection<String> selectedIndicators;

    public void setSelectedIndicators( Collection<String> indicators )
    {
        this.selectedIndicators = indicators;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        DataDictionary dictionary = new DataDictionary();
        
        dictionary.setName( name);
        dictionary.setDescription( nullIfEmpty( description ) );
        dictionary.setRegion( nullIfEmpty( region ) );
        
        if ( selectedDataElements != null )
        {
            Set<DataElement> members = new HashSet<DataElement>( selectedDataElements.size() );

            for ( String id : selectedDataElements )
            {
                members.add( dataElementService.getDataElement( Integer.parseInt( id ) ) );
            }

            dictionary.setDataElements( members );
        }
        
        if ( selectedIndicators != null )
        {
            Set<Indicator> members = new HashSet<Indicator>( selectedIndicators.size() );
            
            for ( String id : selectedIndicators )
            {
                members.add( indicatorService.getIndicator( Integer.parseInt( id ) ) );
            }
            
            dictionary.setIndicators( members );
        }

        dataDictionaryService.saveDataDictionary( dictionary );
        
        return SUCCESS;
    }
}
