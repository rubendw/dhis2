package org.hisp.dhis.dd.action.indicator;

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

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.datadictionary.comparator.DataDictionaryNameComparator;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.comparator.IndicatorGroupNameComparator;
import org.hisp.dhis.options.datadictionary.DataDictionaryModeManager;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: GetIndicatorListAction.java 5573 2008-08-22 03:39:55Z
 *          ch_bharath1 $
 */
public class GetIndicatorListAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataDictionaryModeManager dataDictionaryModeManager;

    public void setDataDictionaryModeManager( DataDictionaryModeManager dataDictionaryModeManager )
    {
        this.dataDictionaryModeManager = dataDictionaryModeManager;
    }

    private DataDictionaryService dataDictionaryService;

    public void setDataDictionaryService( DataDictionaryService dataDictionaryService )
    {
        this.dataDictionaryService = dataDictionaryService;
    }

    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

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
    // Output
    // -------------------------------------------------------------------------

    private List<Indicator> indicators;

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    private DataDictionary dataDictionary;

    public DataDictionary getDataDictionary()
    {
        return dataDictionary;
    }

    private List<DataDictionary> dataDictionaries;

    public List<DataDictionary> getDataDictionaries()
    {
        return dataDictionaries;
    }

    private List<IndicatorGroup> indicatorGroups;

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String indicatorGroupId;

    public String getIndicatorGroupId()
    {
        return indicatorGroupId;
    }

    public void setIndicatorGroupId( String indicatorGroupId )
    {
        this.indicatorGroupId = indicatorGroupId;
    }

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Action implemantation
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public String execute()
        throws Exception
    {
        indicatorGroups = new ArrayList<IndicatorGroup>( indicatorService.getAllIndicatorGroups() );

        Collections.sort( indicatorGroups, new IndicatorGroupNameComparator() );

        dataDictionaries = new ArrayList<DataDictionary>( dataDictionaryService.getAllDataDictionaries() );

        Collections.sort( dataDictionaries, new DataDictionaryNameComparator() );

        dataDictionaryModeManager.setCurrentDataDictionary( id );

        List<Indicator> dataDictionaryMembers = new ArrayList<Indicator>();

        List<Indicator> indicatorGroupMembers = new ArrayList<Indicator>();

        if ( id != null && id > 0 )
        {
            dataDictionary = dataDictionaryService.getDataDictionary( id );

            dataDictionaryMembers = new ArrayList<Indicator>( dataDictionary.getIndicators() );

            if ( indicatorGroupId != null && Integer.parseInt( indicatorGroupId ) > 0 )
            {
                IndicatorGroup indGroup = indicatorService.getIndicatorGroup( Integer.parseInt( indicatorGroupId ) );

                if ( indGroup != null )
                {
                    indicatorGroupMembers = new ArrayList<Indicator>( indGroup.getMembers() );

                    indicators = new ArrayList<Indicator>( CollectionUtils.intersection( 
                        dataDictionaryMembers, indicatorGroupMembers ) );
                }
            }
            else
            {
                indicators = new ArrayList<Indicator>( dataDictionaryMembers );
            }
        }
        else
        {
            if ( indicatorGroupId != null && Integer.parseInt( indicatorGroupId ) > 0 )
            {
                IndicatorGroup indGroup = indicatorService.getIndicatorGroup( Integer.parseInt( indicatorGroupId ) );

                if ( indGroup != null )
                {
                    indicators = new ArrayList<Indicator>( indGroup.getMembers() );
                }
            }
            else
            {
                indicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );
            }
        }

        Collections.sort( indicators, indicatorComparator );

        indicators = displayPropertyHandler.handleIndicators( indicators );

        return SUCCESS;
    }

}
