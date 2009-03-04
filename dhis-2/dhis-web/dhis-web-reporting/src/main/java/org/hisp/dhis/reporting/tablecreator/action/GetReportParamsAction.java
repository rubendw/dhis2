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

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.reporttable.ReportParams;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportStore;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetReportParamsAction
    implements Action
{
    private static final int AVAILABLE_REPORTING_MONTHS = 12;
    private static final String MODE_REPORT = "report";
    private static final String MODE_REPORT_TABLE = "table";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String mode;

    public String getMode()
    {
        return mode;
    }

    public void setMode( String mode )
    {
        this.mode = mode;
    }
    
    private String url;

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private ReportParams reportParams;

    public ReportParams getReportParams()
    {
        return reportParams;
    }
    
    private List<OrganisationUnitLevel> levels;

    public List<OrganisationUnitLevel> getLevels()
    {
        return levels;
    }
    
    private SortedMap<Integer, String> reportingPeriods = new TreeMap<Integer, String>();

    public SortedMap<Integer, String> getReportingPeriods()
    {
        return reportingPeriods;
    }
    
    private Report report;

    public Report getReport()
    {
        return report;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        if ( mode != null && id != null )
        {
            reportParams = getReportParams( mode, id );
            
            if ( reportParams.isParamParentOrganisationUnit() || reportParams.isParamOrganisationUnit() )
            {
                levels = organisationUnitService.getOrganisationUnitLevels();      
            }
            
            if ( reportParams.isParamReportingMonth() )
            {
                MonthlyPeriodType periodType = new MonthlyPeriodType();
                
                Calendar cal = PeriodType.createCalendarInstance();
                
                for ( int i = 0; i < AVAILABLE_REPORTING_MONTHS; i++ )
                {
                    int month = i + 1;
    
                    cal.add( Calendar.MONTH, -1 );
                    
                    Period period = periodType.createPeriod( cal.getTime() );
                    
                    String periodName = format.formatPeriod( period );
                    
                    reportingPeriods.put( month, periodName );
                }                
            }
        }
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * This method will assemble a ReportParams object. If in report table mode,
     * the ReportParams of the report table will be returned. If in report mode,
     * the value of each report param in the ReportParams object will be true
     * if any of the report params in the set of report tables in the report are
     * true.
     * 
     * @param mode the mode
     * @param id the id of the report table or the report
     * @return a ReportParams object.
     */
    private ReportParams getReportParams( String mode, Integer id )
    {
        ReportParams params = new ReportParams();

        if ( mode.equals( MODE_REPORT_TABLE ) )
        {
            params = reportTableService.getReportTable( id ).getReportParams();
        }
        else if ( mode.equals( MODE_REPORT ) )
        {
            Report report = reportStore.getReport( id );
            
            for ( ReportTable reportTable : report.getReportTables() )
            {                
                if ( reportTable.getReportParams() != null && reportTable.getReportParams().isParamReportingMonth() )
                {
                    params.setParamReportingMonth( true );
                }
                
                if ( reportTable.getReportParams() != null && reportTable.getReportParams().isParamOrganisationUnit() )
                {
                    params.setParamOrganisationUnit( true );
                }
                
                if ( reportTable.getReportParams() != null && reportTable.getReportParams().isParamParentOrganisationUnit() )
                {
                    params.setParamParentOrganisationUnit( true );
                }
            }
        }
        
        return params != null ? params : new ReportParams();
    }
}
