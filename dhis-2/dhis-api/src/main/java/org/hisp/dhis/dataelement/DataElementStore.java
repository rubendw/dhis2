package org.hisp.dhis.dataelement;

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

import org.hisp.dhis.hierarchy.HierarchyViolationException;

/**
 * Defines the functionality for persisting DataElements and DataElementGroups.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: DataElementStore.java 5243 2008-05-25 10:18:58Z larshelg $
 */
public interface DataElementStore
{
    String ID = DataElementStore.class.getName();

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    /**
     * Adds a DataElement.
     * 
     * @param dataElement the DataElement to add.
     * @return a generated unique id of the added DataElement.
     */
    int addDataElement( DataElement dataElement );

    /**
     * Updates a DataElement.
     * 
     * @param dataElement the DataElement to update.
     */
    void updateDataElement( DataElement dataElement );

    /**
     * Deletes a DataElement. The DataElement is also removed from any
     * DataElementGroups it is a member of. It is not possible to delete a
     * DataElement with children.
     * 
     * @param dataElement the DataElement to delete.
     * @throws HierarchyViolationException if the DataElement has children.
     */
    void deleteDataElement( DataElement dataElement )
        throws HierarchyViolationException;

    /**
     * Returns a DataElement.
     * 
     * @param id the id of the DataElement to return.
     * @return the DataElement with the given id, or null if no match.
     */
    DataElement getDataElement( int id );
    
    /**
     * Returns the DataElement with the given UUID.
     * 
     * @param uuid the UUID.
     * @return the DataElement with the given UUID, or null if no match.
     */
    DataElement getDataElement( String uuid );

    /**
     * Returns a DataElement with a given name.
     * 
     * @param name the name of the DataElement to return.
     * @return the DataElement with the given name, or null if no match.
     */
    DataElement getDataElementByName( String name );

    /**
     * Returns a DataElement with a given alternative name.
     * 
     * @param alternativeName the alternative name of the DataElement to return.
     * @return the DataElement with the given alternative name, or null if no
     *         match.
     */
    DataElement getDataElementByAlternativeName( String alternativeName );

    /**
     * Returns a DataElement with a given short name.
     * 
     * @param shortName the short name of the DataElement to return.
     * @return the DataElement with the given short name, or null if no match.
     */
    DataElement getDataElementByShortName( String shortName );

    /**
     * Returns a DataElement with a given code.
     * 
     * @param code the code of the DataElement to return.
     * @return the DataElement with the given code, or null if no match.
     */
    DataElement getDataElementByCode( String code );

    /**
     * Returns all DataElements.
     * 
     * @return a collection of all DataElements, or an empty collection if there
     *         are no DataElements.
     */
    Collection<DataElement> getAllDataElements();

    /**
     * Returns all DataElements with types that are possible to aggregate. The
     * types are currently INT and BOOL.
     * 
     * @return all DataElements with types that are possible to aggregate.
     */
    Collection<DataElement> getAggregateableDataElements();
    
    /**
     * Returns all active DataElements.
     * 
     * @return a collection of all active DataElements, or an empty collection
     *         if there are no active DataElements.
     */
    Collection<DataElement> getAllActiveDataElements();

    /**
     * Returns all DataElements with a given aggregantion operator.
     * 
     * @param aggregationOperator the aggregation operator of the DataElements
     *        to return.
     * @return a collection of all DataElements with the given aggregation
     *         operator, or an empty collection if no DataElements have the
     *         aggregation operator.
     */
    Collection<DataElement> getDataElementsByAggregationOperator( String aggregationOperator );

    /**
     * Returns all DataElements with the given type.
     * 
     * @param type the type.
     * @return all DataElements with the given type.
     */
    Collection<DataElement> getDataElementsByType( String type );
    
    // -------------------------------------------------------------------------
    // Calculated Data Elements
    // -------------------------------------------------------------------------

    /**
     * Returns a CalclulatedDataElement which contains a given dataElement
     * 
     * @paran dataElement the DataElement which is contained by the 
     *          CalculatedDataElement to return.
     * @return a CalculatedDataElement which contains the given DataElement, or
     *          null if the DataElement is not part of a CalculatedDataElement.
     */
    CalculatedDataElement getCalculatedDataElementByDataElement( DataElement dataElement );
    
    /**
     * Returns CalculatedDataElements which contain any of the given DataElements
     * @param dataElements Collection of DataElements which can be contained by 
     *          the returned CalculatedDataElements
     * @return a collection of CalculatedDataElements which contain any of the 
     *          given DataElements, or an empty collection if no 
     *          CalculatedDataElements contain any of the DataElements.
     */
    Collection<CalculatedDataElement> getCalculatedDataElementsByDataElements( Collection<DataElement> dataElements );
    
    /**
     * Returns all CalculatedDataElements
     * @return a collection of all CalculatedDataElements, or an empty collection
     *          if there are no CalculcatedDataELements
     */
    Collection<CalculatedDataElement> getAllCalculatedDataElements();
    
    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    /**
     * Adds a DataElementGroup.
     * 
     * @param dataElementGroup the DataElementGroup to add.
     * @return a generated unique id of the added DataElementGroup.
     */
    int addDataElementGroup( DataElementGroup dataElementGroup );

    /**
     * Updates a DataElementGroup.
     * 
     * @param dataElementGroup the DataElementGroup to update.
     */
    void updateDataElementGroup( DataElementGroup dataElementGroup );

    /**
     * Deletes a DataElementGroup.
     * 
     * @param dataElementGroup the DataElementGroup to delete.
     */
    void deleteDataElementGroup( DataElementGroup dataElementGroup );

    /**
     * Returns a DataElementGroup.
     * 
     * @param id the id of the DataElementGroup to return.
     * @return the DataElementGroup with the given id, or null if no match.
     */
    DataElementGroup getDataElementGroup( int id );

    /**
     * Returns the DataElementGroup with the given UUID.
     * 
     * @param id the UUID.
     * @return the DataElementGroup with the given uuid, or null if no match.
     */
    DataElementGroup getDataElementGroup( String uuid );

    /**
     * Returns a DataElementGroup with a given name.
     * 
     * @param name the name of the DataElementGroup to return.
     * @return the DataElementGroup with the given name, or null if no match.
     */
    DataElementGroup getDataElementGroupByName( String name );

    /**
     * Returns all DataElementGroups.
     * 
     * @return a collection of all DataElementGroups, or an empty collection if
     *         no DataElementGroups exist.
     */
    Collection<DataElementGroup> getAllDataElementGroups();

}
