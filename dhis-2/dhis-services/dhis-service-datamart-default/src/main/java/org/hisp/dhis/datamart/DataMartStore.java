package org.hisp.dhis.datamart;

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
import java.util.Map;

import org.hisp.dhis.aggregation.DataElementCategoryOptionComboName;
import org.hisp.dhis.aggregation.GroupSetStructure;
import org.hisp.dhis.aggregation.OrganisationUnitStructure;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: DataMartStore.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public interface DataMartStore
{
    String ID = DataMartStore.class.getName();

    // ----------------------------------------------------------------------
    // AggregatedDataValue
    // ----------------------------------------------------------------------
    
    /**
     * Gets the aggregated value from the datamart table for the given parameters.
     * 
     * @param dataElement The DataElement.
     * @param period The Period.
     * @param organisationUnit The OrganisationUnit.
     * @return the aggregated value, or -1 if no value exists.
     */
    double getAggregatedValue( DataElement dataElement, Period period, OrganisationUnit organisationUnit );
    
    /**
     * Gets the aggregated value from the datamart table for the given parameters.
     * 
     * @param dataElement The DataElement.
     * @param categoryOptionCombo The DataElementCategoryOptionCombo.
     * @param period The Period.
     * @param organisationUnit The OrganisationUnit.
     * @return the aggregated value, or -1 if no value exists.
     */
    double getAggregatedValue( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo, Period period, OrganisationUnit organisationUnit );
    
    /**
     * Deletes AggregatedDataValues registered for the given parameters.
     * 
     * @param dataElementIds a collection of DataElement identifiers.
     * @param periodIds a collection of Period identifiers.
     * @param organisationUnitIds a collection of OrganisationUnit identifiers.
     * @return the number of deleted AggregatedDataValues.
     */
    int deleteAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds );

    /**
     * Deletes all AggregatedDataValues.
     * 
     * @return the number of deleted AggregatedDataValues.
     * @throws AggregationStoreException
     */
    int deleteAggregatedDataValues();

    /**
     * Generates a collection of strings on the form <data element id>-<period id>-<source id>
     * for each aggregated data value in the database.
     * 
     * @return A collection of strings.
     */
    Collection<String> getAggregatedDataValueIdentifiers();
    
    // ----------------------------------------------------------------------
    // AggregatedIndicatorValue
    // ----------------------------------------------------------------------

    /**
     * Gets the aggregated value from the datamart table for the given parameters.
     * 
     * @param indicator The Indicator.
     * @param period The Period.
     * @param organisationUnit The OrganisationUnit.
     * @return the aggregated value, or -1 if no value exists.
     */
    double getAggregatedValue( Indicator indicator, Period period, OrganisationUnit unit );
    
    /**
     * Deletes AggregatedIndicatorValue registered for the given parameters.
     * 
     * @param indicatorIds a collection of Indicator identifiers.
     * @param periodIds a collection of Period identifiers.
     * @param organisationUnitIds a collection of OrganisationUnit identifiers.
     * @return the number of deleted AggregatedIndicatorValues.
     */
    int deleteAggregatedIndicatorValues( Collection<Integer> indicatorIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds );
    
    /**
     * Deletes all AggregatedIndicatorValue.
     * 
     * @return the number of deleted AggregatedIndicatorValues.
     * @throws AggregationStoreException
     */
    int deleteAggregatedIndicatorValues();
    
    /**
     * Generates a collection of strings on the form <indicator id>-<period id>-<source id>
     * for each aggregated indicator value in the database.
     * 
     * @return A collection of strings.
     */
    Collection<String> getAggregatedIndicatorValueIdentifiers();
    
    // ----------------------------------------------------------------------
    // OrganisationUnitStructure
    // ----------------------------------------------------------------------
    
    /**
     * Adds a OrganisationUnitStructure.
     * 
     * @param structure the OrganisationUnitStructure to add.
     * @return the generated unique identifier for the inserted object.
     */
    int addOrganisationUnitStructure( OrganisationUnitStructure structure );
    
    /**
     * Gets all OrganisationUnitStructures.
     * 
     * @return a collection of all OrganisationUnitStructures.
     */
    Collection<OrganisationUnitStructure> getOrganisationUnitStructures();
    
    /**
     * Deletes all OrganisationUnitStructures.
     * 
     * @return the number of deleted OrganisationUnitStructures.
     */
    int deleteOrganisationUnitStructures();

    // ----------------------------------------------------------------------
    // OrganisationUnitGroupSetStructure
    // ----------------------------------------------------------------------
    
    /**
     * Adds a GroupSetStructure.
     * 
     * @param structure the GroupSetStructure to add.
     * @return the generated unique identifier for the inserted object.
     */
    int addGroupSetStructure( GroupSetStructure structure );
    
    /**
     * Gets all GroupSetStructure.
     * 
     * @return a collection of all GroupSetStructures.
     */
    Collection<GroupSetStructure> getGroupSetStructures();
    
    /**
     * Deletes all GroupSetStructures.
     * 
     * @return the number of deletede GroupSetStructures.
     */
    int deleteGroupSetStructures();

    // -------------------------------------------------------------------------
    // DataElementCategoryOptionComboName
    // -------------------------------------------------------------------------

    /**
     * Adds a CategoryOptionComboName.
     * 
     * @param name the CategoryOptionComboName.
     */
    int addDataElementCategoryOptionComboName( DataElementCategoryOptionComboName name );
    
    /**
     * Gets all CategoryOptionComboNames.
     * 
     * @return a Collection of CategoryOptionComboNames.
     */
    Collection<DataElementCategoryOptionComboName> getDataElementCategoryOptionComboNames();
    
    /**
     * Deletes all CategoryOptionComboNames.
     * 
     * @return the number of deleted CategoryOptionComboNames.
     */
    int deleteDataElementCategoryOptionComboNames();
    
    // ----------------------------------------------------------------------
    // DataValue
    // ----------------------------------------------------------------------
    
    /**
     * Gets all DataValues for the given DataElement identifer, collection of Period identifers, and collection of Sources
     * grouped by Period.
     * 
     * @param dataElementId the DataElement identifier.
     * @param periodIds the collection of Period identifiers.
     * @param sourceIds the collection of Source identifiers.
     * @return collection of DataValues.
     */
    Collection<DataValue> getDataValues( int dataElementId, Collection<Integer> periodIds, Collection<Integer> sourceIds );
    
    /**
     * Gets a Map with entries containing Operand and value for all DataValues registered for the given Period and Source.
     * 
     * @param periodId the Period identifier.
     * @param sourceId the Source identifier.
     * @return map of data values.
     */
    Map<Operand, String> getDataValueMap( int periodId, int sourceId );
    
    /**
     * Gets all CrossTabDataValues for the given collection of period ids and source ids.
     * 
     * @param dataElementIds the dataelement identifiers.
     * @param periodIds the period identifiers.
     * @param sourceIds the source identifiers.
     * @return collection of CrossTabDataValues.
     */
    Collection<CrossTabDataValue> getCrossTabDataValues( Collection<Operand> operands, Collection<Integer> periodIds, Collection<Integer> sourceIds );

    /**
     * Gets all CrossTabDataValues for the given collection of period ids and the source id.
     * 
     * @param dataElementIds the dataelement identifiers.
     * @param periodIds the period identifiers.
     * @param sourceId the source identifier.
     * @return collection of CrossTabDataValues.
     */
    Collection<CrossTabDataValue> getCrossTabDataValues( Collection<Operand> operands, Collection<Integer> periodIds, int sourceId );
    
    /**
     * Delets periods of type relative.
     * 
     * @return the number of affected rows.
     */
    int deleteRelativePeriods();
    
    /**
     * Deletes data values registered with 0 as value and associated with
     * data elements with sum as aggregation operator.
     * 
     * @return the number of affected rows.
     */
    int deleteZeroDataValues();
}
