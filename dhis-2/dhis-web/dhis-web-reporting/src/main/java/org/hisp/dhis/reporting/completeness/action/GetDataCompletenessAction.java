package org.hisp.dhis.reporting.completeness.action;

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
import java.util.List;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.completeness.DataSetCompletenessService;
import org.hisp.dhis.completeness.comparator.DataSetCompletenessResultComparator;
import org.hisp.dhis.util.SessionUtils;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetDataCompletenessAction
    implements Action
{
    private static final String KEY_DATA_COMPLETENESS = "dataSetCompletenessResults";
    private static final String KEY_DATA_COMPLETENESS_DATASET = "dataSetCompletenessDataSet";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetCompletenessService completenessService;

    public void setCompletenessService( DataSetCompletenessService completenessService )
    {
        this.completenessService = completenessService;
    }
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer periodId;

    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }
    
    private Integer dataSetId;

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }
    
    private Integer organisationUnitId;

    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataSetCompletenessResult> results;

    public List<DataSetCompletenessResult> getResults()
    {
        return results;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        SessionUtils.removeSessionVar( KEY_DATA_COMPLETENESS );
        SessionUtils.removeSessionVar( KEY_DATA_COMPLETENESS_DATASET );
        
        if ( periodId != null && organisationUnitId != null )
        {
            if ( dataSetId != null )
            {
                // -------------------------------------------------------------
                // Display completeness by DataSets
                // -------------------------------------------------------------

                results = new ArrayList<DataSetCompletenessResult>( completenessService.
                    getDataSetCompleteness( periodId, organisationUnitId, dataSetId ) );
                
                DataSet dataSet = dataSetService.getDataSet( dataSetId );
                
                SessionUtils.setSessionVar( KEY_DATA_COMPLETENESS_DATASET, dataSet );
            }
            else
            {
                // -------------------------------------------------------------
                // Display completeness by child OrganisationUnits for a DataSet
                // -------------------------------------------------------------

                results = new ArrayList<DataSetCompletenessResult>( completenessService.
                    getDataSetCompleteness( periodId, organisationUnitId ) );
            }
            
            Collections.sort( results, new DataSetCompletenessResultComparator() );
            
            SessionUtils.setSessionVar( KEY_DATA_COMPLETENESS, results );
        }
        
        return SUCCESS;
    }
}
