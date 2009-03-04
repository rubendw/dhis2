package org.hisp.dhis.dataadmin.action.maintenance;

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

import org.hisp.dhis.completeness.DataSetCompletenessStore;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.datamart.resourcetable.ResourceTableService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class PerformMaintenanceAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }    

    private ResourceTableService resourceTableService;

    public void setResourceTableService( ResourceTableService resourceTableService )
    {
        this.resourceTableService = resourceTableService;
    }
    
    private DataSetCompletenessStore completenessStore;

    public void setCompletenessStore( DataSetCompletenessStore completenessStore )
    {
        this.completenessStore = completenessStore;
    }
    
    private DataMartStore dataMartStore;

    public void setDataMartStore( DataMartStore dataMartStore )
    {
        this.dataMartStore = dataMartStore;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private boolean hierarchyHistory;

    public void setHierarchyHistory( boolean hierarchyHistory )
    {
        this.hierarchyHistory = hierarchyHistory;
    }

    private boolean aggregatedDataValues;

    public void setAggregatedDataValues( boolean aggregatedDataValues )
    {
        this.aggregatedDataValues = aggregatedDataValues;
    }

    private boolean aggregatedIndicatorValues;

    public void setAggregatedIndicatorValues( boolean aggregatedIndicatorValues )
    {
        this.aggregatedIndicatorValues = aggregatedIndicatorValues;
    }

    private boolean zeroValues;

    public void setZeroValues( boolean zeroValues )
    {
        this.zeroValues = zeroValues;
    }
    
    private boolean dataSetCompleteness;

    public void setDataSetCompleteness( boolean dataSetCompleteness )
    {
        this.dataSetCompleteness = dataSetCompleteness;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute() 
        throws Exception
    {
        if ( hierarchyHistory )
        {
            organisationUnitService.clearOrganisationUnitHierarchyHistory();
        }
        
        if ( aggregatedDataValues )
        {
            resourceTableService.clearDataValues();
        }
        
        if ( aggregatedIndicatorValues )
        {
            resourceTableService.clearIndicatorValues();
        }
        
        if ( zeroValues )
        {
            dataMartStore.deleteZeroDataValues();
        }
        
        if ( dataSetCompleteness )
        {
            completenessStore.deleteDataSetCompleteness();
        }
        
        return SUCCESS;
    }
}
