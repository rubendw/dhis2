package org.hisp.dhis.aggregation;

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

import java.util.Date;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Lars Helge Overland
 * @version $Id: AggregationService.java 4712 2008-03-12 10:32:45Z larshelg $
 */
public interface AggregationService
{
    String ID = AggregationService.class.getName();

    static final double NO_VALUES_REGISTERED = -1.0;

    /**
     * Calculates the aggregated value based on the aggregation operator defined
     * in the given dataelement.
     * 
     * @param dataElement the DataElement to aggregate over.
     * @param startDate the start date of the aggregation period.
     * @param endDate the end date of the aggregation period.
     * @param organisationUnit the OrganisationUnit to aggregate over.
     * @return the aggregated value.
     * @throws AggregationStoreException
     */
    double getAggregatedDataValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, Date startDate, Date endDate,
        OrganisationUnit organisationUnit );

    /**
     * Calculates the aggregated value of the given indicator.
     * 
     * @param indicator the Indicator to aggregate over.
     * @param startDate the start date of the aggregation period.
     * @param endDate the end date of the aggregation period.
     * @param organisationUnit the OrganisationUnit to aggregate over.
     * @return the aggregated value.
     * @throws AggregationStoreException
     */
    double getAggregatedIndicatorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit );

    /**
     * Calculates the aggregated value of the numerator of the given indicator.
     * 
     * @param indicator the indicator to aggregate over.
     * @param startDate the start date of the aggregation period.
     * @param endDate the end date of the aggregation period.
     * @param organisationUnit the OrganisationUnit to aggregate over.
     * @return the aggregated value.
     * @throws AggregationStoreException
     */
    double getAggregatedNumeratorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit );

    /**
     * Calculates the aggregated value of the denominator of the given
     * indicator.
     * 
     * @param indicator the indicator to aggregate over.
     * @param startDate the start date of the aggregation period.
     * @param endDate the end date of the aggregation period.
     * @param organisationUnit the OrganisationUnit to aggregate over.
     * @return the aggregated value.
     * @throws AggregationStoreException
     */
    double getAggregatedDenominatorValue( Indicator indicator, Date startDate, Date endDate,
        OrganisationUnit organisationUnit );
}
