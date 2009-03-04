package org.hisp.dhis.dataset;

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

import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultDataSetService.java 5607 2008-08-28 11:09:22Z larshelg $
 */
public class DefaultDataSetService
    implements DataSetService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetStore dataSetStore;

    public void setDataSetStore( DataSetStore dataSetStore )
    {
        this.dataSetStore = dataSetStore;
    }
    
    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    public int addDataSet( DataSet dataSet )
    {
        return dataSetStore.addDataSet( dataSet );
    }

    public void updateDataSet( DataSet dataSet )
    {
        dataSetStore.updateDataSet( dataSet );
    }

    public void deleteDataSet( DataSet dataSet )
    {
        dataSetStore.deleteDataSet( dataSet );
    }

    public DataSet getDataSet( int id )
    {
        return dataSetStore.getDataSet( id );
    }

    public DataSet getDataSetByName( String name )
    {
        return dataSetStore.getDataSetByName( name );
    }

    public DataSet getDataSetByShortName( String shortName )
    {
        return dataSetStore.getDataSetByShortName( shortName );
    }

    public Collection<DataSet> getDataSetsBySource( Source source )
    {
        return dataSetStore.getDataSetsBySource( source );
    }

    public Collection<DataSet> getDataSetsBySources( Collection<? extends Source> sources )
    {
        Set<DataSet> dataSets = new HashSet<DataSet>();
        
        for ( Source source : sources )
        {
            dataSets.addAll( dataSetStore.getDataSetsBySource( source ) );
        }
        
        return dataSets;
    }
    
    public int getSourcesAssociatedWithDataSet( DataSet dataSet, Collection<? extends Source> sources )
    {
        int count = 0;
        
        for ( Source source : sources )
        {
            if ( dataSet.getSources().contains( source ) )
            {
                count++;
            }
        }
        
        return count;
    }
    
    public Collection<DataSet> getAllDataSets()
    {
        return dataSetStore.getAllDataSets();
    }
    
    public Collection<DataSet> getDataSets( Collection<Integer> identifiers )
    {
        Collection<DataSet> objects = new ArrayList<DataSet>();
        
        for ( Integer id : identifiers )
        {
            objects.add( getDataSet( id ) );
        }
        
        return objects;
    }

    public List<DataSet> getAvailableDataSets()
    {
        List<DataSet> availableDataSetList = new ArrayList<DataSet>();
        List<DataSet> dataSetList = new ArrayList<DataSet>( getAllDataSets() );

        for ( DataSet dataSet : dataSetList )
        {            
            DataEntryForm dataEntryForm = dataEntryFormService.getDataEntryFormByDataSet( dataSet );
            
            if ( dataEntryForm == null )
            {
                availableDataSetList.add( dataSet );
            }
        }

        return availableDataSetList;
    }

    public List<DataSet> getAssignedDataSets()
    {
        List<DataSet> assignedDataSetList = new ArrayList<DataSet>();
        List<DataSet> dataSetList = new ArrayList<DataSet>( getAllDataSets() );

        for ( DataSet dataSet : dataSetList )
        {
            DataEntryForm dataEntryForm = dataEntryFormService.getDataEntryFormByDataSet( dataSet );
            
            if ( dataEntryForm != null )
            {
                assignedDataSetList.add( dataSet );
            }
        }

        return assignedDataSetList;
    }
    
    // -------------------------------------------------------------------------
    // FrequencyOverrideAssociation
    // -------------------------------------------------------------------------

    public void addFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation )
    {
        dataSetStore.addFrequencyOverrideAssociation( frequencyOverrideAssociation );
    }

    public void updateFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation )
    {
        dataSetStore.updateFrequencyOverrideAssociation( frequencyOverrideAssociation );
    }

    public void removeFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation )
    {
        dataSetStore.removeFrequencyOverrideAssociation( frequencyOverrideAssociation );
    }

    public FrequencyOverrideAssociation getFrequencyOverrideAssociation( DataSet dataSet, Source source )
    {
        return dataSetStore.getFrequencyOverrideAssociation( dataSet, source );
    }

    public Collection<FrequencyOverrideAssociation> getFrequencyOverrideAssociationsByDataSet( DataSet dataSet )
    {
        return dataSetStore.getFrequencyOverrideAssociationsByDataSet( dataSet );
    }

    public Collection<FrequencyOverrideAssociation> getFrequencyOverrideAssociationsBySource( Source source )
    {
        return dataSetStore.getFrequencyOverrideAssociationsBySource( source );
    }
}
