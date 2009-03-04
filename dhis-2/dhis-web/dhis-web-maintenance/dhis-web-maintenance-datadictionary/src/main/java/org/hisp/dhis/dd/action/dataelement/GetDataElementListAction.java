package org.hisp.dhis.dd.action.dataelement;

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
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementGroupNameComparator;
import org.hisp.dhis.options.datadictionary.DataDictionaryModeManager;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: GetDataElementListAction.java 5573 2008-08-22 03:39:55Z
 *          ch_bharath1 $
 */
public class GetDataElementListAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
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

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
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

    private List<DataElement> dataElements = new ArrayList<DataElement>();

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    private List<DataElementGroup> dataElementGroups;

    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    private List<DataDictionary> dataDictionaries;

    public List<DataDictionary> getDataDictionaries()
    {
        return dataDictionaries;
    }

    private DataDictionary dataDictionary;

    public DataDictionary getDataDictionary()
    {
        return dataDictionary;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String dataElementGroupId;

    public void setDataElementGroupId( String dataElementGroupId )
    {
        this.dataElementGroupId = dataElementGroupId;
    }

    public String getDataElementGroupId()
    {
        return dataElementGroupId;
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
        dataElementGroups = new ArrayList<DataElementGroup>( dataElementService.getAllDataElementGroups() );

        Collections.sort( dataElementGroups, new DataElementGroupNameComparator() );

        dataDictionaries = new ArrayList<DataDictionary>( dataDictionaryService.getAllDataDictionaries() );

        Collections.sort( dataDictionaries, new DataDictionaryNameComparator() );

        dataDictionaryModeManager.setCurrentDataDictionary( id );

        List<DataElement> dataDictionaryMembers = new ArrayList<DataElement>();

        List<DataElement> dataElementGroupMembers = new ArrayList<DataElement>();

        if ( id != null && id > 0 )
        {
            dataDictionary = dataDictionaryService.getDataDictionary( id );

            dataDictionaryMembers = new ArrayList<DataElement>( dataDictionary.getDataElements() );

            if ( dataElementGroupId != null && Integer.parseInt( dataElementGroupId ) > 0 )
            {
                DataElementGroup group = dataElementService.getDataElementGroup( Integer
                    .parseInt( dataElementGroupId ) );

                if ( group != null )
                {
                    dataElementGroupMembers = new ArrayList<DataElement>( group.getMembers() );

                    dataElements = new ArrayList<DataElement>( CollectionUtils.intersection( 
                        dataDictionaryMembers, dataElementGroupMembers ) );
                }
            }
            else
            {
                dataElements.addAll( dataDictionaryMembers );
            }
        }
        else
        {
            if ( dataElementGroupId != null && Integer.parseInt( dataElementGroupId ) > 0 )
            {
                DataElementGroup group = dataElementService.getDataElementGroup( Integer
                    .parseInt( dataElementGroupId ) );

                if ( group != null )
                {
                    dataElements = new ArrayList<DataElement>( group.getMembers() );
                }
            }
            else
            {
                dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );
            }
        }

        Collections.sort( dataElements, dataElementComparator );

        dataElements = displayPropertyHandler.handleDataElements( dataElements );

        return SUCCESS;
    }
}
