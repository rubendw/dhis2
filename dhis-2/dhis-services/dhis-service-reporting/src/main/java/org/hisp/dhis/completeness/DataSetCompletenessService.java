package org.hisp.dhis.completeness;

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
import java.util.Date;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.external.configuration.NoConfigurationFoundException;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public interface DataSetCompletenessService
{
    String ID = DataSetCompletenessService.class.getName();
    
    /**
     * Returns a Collection of DataSetCompletenessResults. The DataSetCompletenessResult
     * object contains the name of the associated DataSet, the number of
     * sources being associated with the DataSet AND being a child of the given 
     * OrganisationUnit, and the number of CompleteDataSetRegistrations for the
     * mentioned sources. One DataSetCompletenessResult is included for each
     * distinct DataSet associated with any of the given OrganisationUnit
     * or its children.
     * 
     * @param periodId the identifier of the Period.
     * @param organisationUnitId the identifier of the root OrganisationUnit.
     * @return a Collection of DataSetCompletenessResults.
     */
    Collection<DataSetCompletenessResult> getDataSetCompleteness( int periodId, int organisationUnitId );
    
    /**
     * Returns a Collection of DataSetCompletenessResults. The DataSetCompletenessResult
     * object contains the name of the associated OrganisationUnit, the number of
     * sources being associated with the DataSet AND being a child of the OrganisationUnit,
     * and the number of CompleteDataSetRegistrations for the mentioned sources
     * for the DataSet. One DataSetCompletenessResult is included for each child
     * of the OrganisationUnit.
     * 
     * @param periodId the identifier of the Period.
     * @param parentOrganisationUnitId the identifier of the parent OrganisationUnit.
     * @param dataSetId the identifier of the DataSet.
     * @return a Collection of DataSetCompletenessResults.
     */
    Collection<DataSetCompletenessResult> getDataSetCompleteness( int periodId, int parentOrganisationUnitId, int dataSetId );
    
    /**
     * Returns a Collection of DataSetCompletenessResults.
     * 
     * @param period the Period.
     * @param unit the OrganisationUnit.
     * @param dataSet the DataSet.
     * @return a Collection of DataSetCompletenessResults.
     */
    DataSetCompletenessResult getDataSetCompleteness( Period period, Date deadline, OrganisationUnit unit, DataSet dataSet );
    
    /**
     * Sets the configuration for the data completeness functionality.
     * @param configuration the configuration object.
     */
    void setConfiguration( DataSetCompletenessConfiguration configuration );
    
    /**
     * Gets the configuration for the data completeness functionality.
     * @return the configuration for the data completeness functionality.
     * @throws NoConfigurationFoundException
     */
    DataSetCompletenessConfiguration getConfiguration()
        throws NoConfigurationFoundException;
}
