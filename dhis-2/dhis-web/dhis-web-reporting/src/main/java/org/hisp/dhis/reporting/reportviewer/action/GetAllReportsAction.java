package org.hisp.dhis.reporting.reportviewer.action;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportManager;
import org.hisp.dhis.report.ReportStore;
import org.hisp.dhis.report.comparator.ReportComparator;
import org.hisp.dhis.report.manager.ReportConfiguration;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetAllReportsAction
    implements Action
{
    private static final String SEPARATOR = "/";
    private static final String PORT_SEPARATOR = ":";    
    private static final String PROTOCOL = "http://";
    private static final String BASE_QUERY = "frameset?__report=";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportManager reportManager;

    public void setReportManager( ReportManager reportManager )
    {
        this.reportManager = reportManager;
    }
    
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }
    
    private UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }
    
    private ReportStore reportStore;

    public void setReportStore( ReportStore reportStore )
    {
        this.reportStore = reportStore;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<Report> reports = new ArrayList<Report>();

    public List<Report> getReports()
    {
        return reports;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() 
        throws Exception
    {
        ReportConfiguration config = reportManager.getConfiguration();
        
        String birtHome = config.getHome();
        
        String birtDirectory = config.getDirectory();

        HttpServletRequest request = ServletActionContext.getRequest();
        
        String server = request.getServerName();
        
        int port = request.getServerPort();
        
        String baseUrl = PROTOCOL + server + PORT_SEPARATOR + port + SEPARATOR;
        
        String birtURL = baseUrl + birtDirectory + SEPARATOR + BASE_QUERY;
        
        if ( birtHome != null && birtDirectory != null )
        {
            for ( Report report : getUserReports() )
            {
                String url = birtURL + report.getDesign();
                
                report.setUrl( url );
                
                reports.add( report );
            }
        }
        
        Collections.sort( reports, new ReportComparator() );
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Collection<Report> getUserReports()
    {
        if ( currentUserService.currentUserIsSuper() )
        {
            return reportStore.getAllReports();
        }
        else
        {
            Collection<Report> reports = new HashSet<Report>();
            
            UserCredentials credentials = userStore.getUserCredentials( currentUserService.getCurrentUser() );
            
            for ( UserAuthorityGroup group : credentials.getUserAuthorityGroups() )
            {
                reports.addAll( group.getReports() );
            }
            
            return reports;
        }
    }
}
