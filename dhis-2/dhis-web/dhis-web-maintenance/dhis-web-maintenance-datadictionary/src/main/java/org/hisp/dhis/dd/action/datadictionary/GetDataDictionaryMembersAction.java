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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetDataDictionaryMembersAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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
    
    private DataDictionaryService dataDictionaryService;

    public void setDataDictionaryService( DataDictionaryService dataDictionaryService )
    {
        this.dataDictionaryService = dataDictionaryService;
    }    

    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
    }
    
    private Comparator<Indicator> indicatorComparator;

    public void setIndicatorComparator( Comparator<Indicator> indicatorComparator )
    {
        this.indicatorComparator = indicatorComparator;
    }

    // -------------------------------------------------------------------------
    // DisplayPropertyHandler
    // -------------------------------------------------------------------------

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }    
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataElement> selectedDataElements = new ArrayList<DataElement>() ;

    public List<DataElement> getSelectedDataElements()
    {
        return selectedDataElements;
    }

    private List<DataElement> availableDataElements = new ArrayList<DataElement>();

    public List<DataElement> getAvailableDataElements()
    {
        return availableDataElements;
    }

    private List<Indicator> selectedIndicators = new ArrayList<Indicator>();

    public List<Indicator> getSelectedIndicators()
    {
        return selectedIndicators;
    }
    
    private List<Indicator> availableIndicators = new ArrayList<Indicator>();

    public List<Indicator> getAvailableIndicators()
    {
        return availableIndicators;
    }

    private DataDictionary dataDictionary;

    public DataDictionary getDataDictionary()
    {
        return dataDictionary;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Get selected elements
        // ---------------------------------------------------------------------

        if ( id != null )
        {
            dataDictionary = dataDictionaryService.getDataDictionary( id );
            
            selectedDataElements = new ArrayList<DataElement>( dataDictionary.getDataElements() );
            Collections.sort( selectedDataElements, dataElementComparator );            
            displayPropertyHandler.handleDataElements( selectedDataElements );
            
            selectedIndicators = new ArrayList<Indicator>( dataDictionary.getIndicators() );            
            Collections.sort( selectedIndicators, indicatorComparator );            
            displayPropertyHandler.handleIndicators( selectedIndicators );
        }
        
        // ---------------------------------------------------------------------
        // Get available elements
        // ---------------------------------------------------------------------

        availableDataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );        
        availableDataElements.removeAll( selectedDataElements );
        Collections.sort( availableDataElements, dataElementComparator );        
        displayPropertyHandler.handleDataElements( availableDataElements );
        
        availableIndicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );        
        availableIndicators.removeAll( selectedIndicators );        
        Collections.sort( availableIndicators, indicatorComparator );        
        displayPropertyHandler.handleIndicators( availableIndicators );
        
        return SUCCESS;
    }
}
