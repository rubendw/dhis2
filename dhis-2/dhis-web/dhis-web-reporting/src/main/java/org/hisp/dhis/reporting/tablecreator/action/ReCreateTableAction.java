package org.hisp.dhis.reporting.tablecreator.action;

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
import java.util.Date;
import java.util.List;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.report.ReportStore;
import org.hisp.dhis.reporttable.ReportTableInternalProcess;
import org.hisp.dhis.reporting.tablecreator.util.InternalProcessUtil;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReCreateTableAction
    implements Action
{
    private static final String PROCTYPE_REPORTTABLE = "ReportTable";
    private static final String MODE_REPORT = "report";
    private static final String MODE_REPORT_TABLE = "table";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private InternalProcessCoordinator internalProcessCoordinator;
    
    public void setInternalProcessCoordinator( InternalProcessCoordinator processCoordinator )
    {
        this.internalProcessCoordinator = processCoordinator;
    }
    
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }
    
    private ReportStore reportStore;

    public void setReportStore( ReportStore reportStore )
    {
        this.reportStore = reportStore;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String mode;

    public void setMode( String mode )
    {
        this.mode = mode;
    }
    
    private Integer reportingPeriod;

    public void setReportingPeriod( Integer reportingPeriod )
    {
        this.reportingPeriod = reportingPeriod;
    }

    private Integer parentOrganisationUnitId;

    public void setParentOrganisationUnitId( Integer parentOrganisationUnitId )
    {
        this.parentOrganisationUnitId = parentOrganisationUnitId;
    }

    private Integer organisationUnitId;

    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        String owner = currentUserService.getCurrentUsername();

        ReportTableInternalProcess process = internalProcessCoordinator.newProcess( PROCTYPE_REPORTTABLE, owner );

        process.setDoDataMart( true );
        
        // ---------------------------------------------------------------------
        // Report parameters
        // ---------------------------------------------------------------------
        
        for ( ReportTable reportTable : getReportTables( id, mode ) )
        {
            // -----------------------------------------------------------------
            // Reporting period report parameter / reporting period
            // -----------------------------------------------------------------
    
            Date date = null;
            
            if ( reportTable.getReportParams() != null && reportTable.getReportParams().isParamReportingMonth() )
            {
                reportTable.setRelativePeriods( reportTableService.getRelativePeriods( reportTable.getRelatives(), reportingPeriod ) );            
                date = reportTableService.getDateFromPreviousMonth( reportingPeriod );
            }
            else
            {
                reportTable.setRelativePeriods( reportTableService.getRelativePeriods( reportTable.getRelatives(), -1 ) );            
                date = reportTableService.getDateFromPreviousMonth( -1 );  
            }

            String reportingMonthName = format.formatPeriod( new MonthlyPeriodType().createPeriod( date ) );            
            reportTable.setReportingMonthName( reportingMonthName );
            
            // -----------------------------------------------------------------
            // Parent organisation unit report parameter
            // -----------------------------------------------------------------

            if ( reportTable.getReportParams() != null && reportTable.getReportParams().isParamParentOrganisationUnit() )
            {
                reportTable.setUnits( new ArrayList<OrganisationUnit>( 
                    organisationUnitService.getOrganisationUnit( parentOrganisationUnitId ).getChildren() ) );
            }    

            // -----------------------------------------------------------------
            // Organisation unit report parameter
            // -----------------------------------------------------------------

            if ( reportTable.getReportParams() != null && reportTable.getReportParams().isParamOrganisationUnit() )
            {
                List<OrganisationUnit> organisationUnit = new ArrayList<OrganisationUnit>();
                organisationUnit.add( organisationUnitService.getOrganisationUnit( organisationUnitId ) );
                reportTable.setUnits( organisationUnit );
            }
            
            reportTable.setI18nFormat( format );            
            reportTable.init();            

            process.addReportTable( reportTable );    
        }
        
        // ---------------------------------------------------------------------
        // Internal process execution
        // ---------------------------------------------------------------------
        
        String processId = internalProcessCoordinator.requestProcessExecution( process );

        InternalProcessUtil.setCurrentRunningProcess( processId );
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    /**
     * If report table mode, this method will return the report table with the
     * given identifier. If report mode, this method will return the report
     * tables associated with the report.
     * 
     * @param id the identifier.
     * @param mode the mode.
     */
    private Collection<ReportTable> getReportTables( Integer id, String mode )
    {
        Collection<ReportTable> reportTables = new ArrayList<ReportTable>();
        
        if ( mode.equals( MODE_REPORT_TABLE ) )
        {
            reportTables.add( reportTableService.getReportTable( id ) );
        }
        else if ( mode.equals( MODE_REPORT ) )
        {
            reportTables = reportStore.getReport( id ).getReportTables();
        }
        
        return reportTables;
    }
}
