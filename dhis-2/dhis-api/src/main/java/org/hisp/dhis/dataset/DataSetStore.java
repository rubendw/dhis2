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

import java.util.Collection;

import org.hisp.dhis.source.Source;

/**
 * @author Kristian Nordal
 * @version $Id: DataSetStore.java 5607 2008-08-28 11:09:22Z larshelg $
 */
public interface DataSetStore
{
    String ID = DataSetStore.class.getName();

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    /**
     * Adds a DataSet.
     * 
     * @param dataSet The DataSet to add.
     * @return The generated unique identifier for this DataSet.
     */
    int addDataSet( DataSet dataSet );

    /**
     * Updates a DataSet.
     * 
     * @param dataSet The DataSet to update.
     */
    void updateDataSet( DataSet dataSet );

    /**
     * Deletes a DataSet.
     * 
     * @param dataSet The DataSet to delete.
     */
    void deleteDataSet( DataSet dataSet );

    /**
     * Get a DataSet
     * 
     * @param id The unique identifier for the DataSet to get.
     * @return The DataSet with the given id or null if it does not exist.
     */
    DataSet getDataSet( int id );

    /**
     * Returns the DataSet with the given name.
     * 
     * @param name The name.
     * @return The DataSet with the given name.
     */
    DataSet getDataSetByName( String name );

    /**
     * Returns the DataSet with the given short name.
     * 
     * @param shortName The short name.
     * @return The DataSet with the given short name.
     */
    DataSet getDataSetByShortName( String shortName );
    
    /**
     * Returns all DataSets associated with the specified source.
     */
    Collection<DataSet> getDataSetsBySource( Source source );

    /**
     * Get all DataSets.
     * 
     * @return A collection containing all DataSets.
     */
    Collection<DataSet> getAllDataSets();

    // -------------------------------------------------------------------------
    // FrequencyOverrideAssociation
    // -------------------------------------------------------------------------

    /**
     * Adds a FrequencyOverrideAssociation.
     * 
     * @param frequencyOverrideAssociation The FrequencyOverrideAssociation to
     *        add.
     */
    void addFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation );

    /**
     * Updates a FrequencyOverrideAssociation.
     * 
     * @param frequencyOverrideAssociation The FrequencyOverrideAssociation to
     *        update.
     */
    void updateFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation );

    /**
     * Removes a FrequencyOverrideAssociation.
     * 
     * @param frequencyOverrideAssociation The FrequencyOverrideAssociation to
     *        remove.
     */
    void removeFrequencyOverrideAssociation( FrequencyOverrideAssociation frequencyOverrideAssociation );

    /**
     * Retrieves a FrequencyOverrideAssociation.
     * 
     * @param dataSet The DataSet referred to by the
     *        FrequencyOverrideAssociation.
     * @param source The Source referred to by the FrequencyOverrideAssociation.
     * @return The FrequencyOverrideAssociation for the given DataSet and
     *         Source.
     */
    FrequencyOverrideAssociation getFrequencyOverrideAssociation( DataSet dataSet, Source source );

    /**
     * Retrieves FrequencyOverrideAssociations for a given DataSet.
     * 
     * @param dataSet The DataSet.
     * @return A collection of FrequencyOverrideAssociations for the given
     *         DataSet.
     */
    Collection<FrequencyOverrideAssociation> getFrequencyOverrideAssociationsByDataSet( DataSet dataSet );

    /**
     * Retrieves FrequencyOverrideAssociations for a given Source.
     * 
     * @param source The Source.
     * @return A collection of FrequencyOverrideAssociations for the given
     *         Source.
     */
    Collection<FrequencyOverrideAssociation> getFrequencyOverrideAssociationsBySource( Source source );
}
