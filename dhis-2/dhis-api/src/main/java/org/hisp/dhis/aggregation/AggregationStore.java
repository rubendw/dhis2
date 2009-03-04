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

import java.util.Collection;

import org.hisp.dhis.datavalue.DataValue;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public interface AggregationStore
{
    String ID = AggregationStore.class.getName();

    // ----------------------------------------------------------------------
    // DataValue
    // ----------------------------------------------------------------------
    
    /**
     * Gets all DataValues for the given parameters. This method can be used in conjunction with the
     * StatementInterceptor, which creates a database connection and statement which is reused for multiple
     * invocations of this method.
     * 
     * @param sourceIds a collection of source ids.
     * @param dataElementId the dataElementId.
     * @param optionComboId the optionComboId.
     * @param periodIds a collection of period ids.
     * @return collection of DataValues for the given parameters.
     */
    Collection<DataValue> getDataValues( Collection<Integer> sourceIds, int dataElementId, int optionComboId, Collection<Integer> periodIds );
    
    /**
     * Gets all DataValues for the given parameters. This method can be used in conjunction with the
     * StatementInterceptor, which creates a database connection and statement which is reused for multiple
     * invocations of this method.
     * 
     * @param sourceId the Source id.
     * @param dataElementId the DataElement id.
     * @param optionComboId the optionComboId.
     * @param periodIds a collection of period ids.
     * @return collection of DataValues for the given parameters.
     */
    Collection<DataValue> getDataValues( int sourceId, int dataElementId, int optionComboId, Collection<Integer> periodIds );
    
    /**
     * Generates a collection of strings on the form <data element id>-<period id>-<source id>
     * for each data value in the database.
     * 
     * @return A collection of strings.
     */
    Collection<String> getDataValueIdentifiers();
}
