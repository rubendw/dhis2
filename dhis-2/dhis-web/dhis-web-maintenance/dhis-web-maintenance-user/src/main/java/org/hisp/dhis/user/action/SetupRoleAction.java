package org.hisp.dhis.user.action;

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
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportStore;
import org.hisp.dhis.report.comparator.ReportComparator;
import org.hisp.dhis.security.authority.SystemAuthoritiesProvider;

import com.opensymphony.xwork.Action;

/**
 * @author Thanh Nguyen
 * @version $Id: SetupRoleAction.java 5701 2008-09-14 20:34:46Z larshelg $
 */
public class SetupRoleAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private ReportStore reportStore;

    public void setReportStore( ReportStore reportStore )
    {
        this.reportStore = reportStore;
    }
    
    private SystemAuthoritiesProvider authoritiesProvider;

    public void setAuthoritiesProvider( SystemAuthoritiesProvider authoritiesProvider )
    {
        this.authoritiesProvider = authoritiesProvider;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataSet> availableDataSets;

    public List<DataSet> getAvailableDataSets()
    {
        return availableDataSets;
    }
    
    private List<Report> availableReports;

    public List<Report> getAvailableReports()
    {
        return availableReports;
    }
    
    private List<String> availableAuthorities;

    public List<String> getAvailableAuthorities()
    {
        return availableAuthorities;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        availableDataSets = new ArrayList<DataSet>( dataSetService.getAllDataSets() );
        
        availableReports = new ArrayList<Report>( reportStore.getAllReports() );
        
        Collections.sort( availableReports, new ReportComparator() );
        
        availableAuthorities = new ArrayList<String>( authoritiesProvider.getSystemAuthorities() );

        Collections.sort( availableAuthorities );

        return SUCCESS;
    }
}
