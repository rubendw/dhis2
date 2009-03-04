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
import java.util.Collection;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportStore;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.xwork.Action;

/**
 * @author Thanh Nguyen
 * @version $Id: AddRoleAction.java 5701 2008-09-14 20:34:46Z larshelg $
 */
public class AddRoleAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

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
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String rolename )
    {
        this.name = rolename;
    }
    
    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }
    
    private Collection<String> selectedList = new ArrayList<String>();

    public void setSelectedList( Collection<String> selectedList )
    {
        this.selectedList = selectedList;
    }

    private Collection<String> selectedListReport = new ArrayList<String>();

    public void setSelectedListReport( Collection<String> selectedListReport )
    {
        this.selectedListReport = selectedListReport;
    }
    
    private Collection<String> selectedListAuthority = new ArrayList<String>();

    public void setSelectedListAuthority( Collection<String> selectedListAuthority )
    {
        this.selectedListAuthority = selectedListAuthority;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        UserAuthorityGroup group = new UserAuthorityGroup();

        group.setName( name );
        group.setDescription( description );

        for ( String id : selectedList )
        {
            DataSet dataSet = dataSetService.getDataSet( Integer.parseInt( id ) );

            group.getDataSets().add( dataSet );
        }
        
        for ( String id : selectedListReport )
        {
            Report report = reportStore.getReport( Integer.parseInt( id ) );
            
            group.getReports().add( report );
        }

        group.getAuthorities().addAll( selectedListAuthority );

        userStore.addUserAuthorityGroup( group );

        return SUCCESS;
    }
}
