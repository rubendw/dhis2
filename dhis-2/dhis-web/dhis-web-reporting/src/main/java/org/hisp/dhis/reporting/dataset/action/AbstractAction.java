package org.hisp.dhis.reporting.dataset.action;

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

import java.io.InputStream;
import java.util.Collection;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.reporting.dataset.dataaccess.ReportDataAccess;
import org.hisp.dhis.reporting.dataset.report.ReportStore;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractAction.java 5556 2008-08-20 11:36:20Z abyot $
 */
public abstract class AbstractAction
    extends ActionSupport
{
    // -----------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------

    protected ReportStore reportStore;

    public void setReportStore( ReportStore reportStore )
    {
        this.reportStore = reportStore;
    }

    protected ReportDataAccess reportDataAccess;

    public void setReportDataAccess( ReportDataAccess reportDataAccess )
    {
        this.reportDataAccess = reportDataAccess;
    }

    protected OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    // -----------------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------------

    protected final String REPORT = "Report";

    protected final String REPORT_TYPE = "ReportType";

    protected final String HTML_BUFFER = "HTML_buffer";

    protected final String START_DATE = "StartDate";

    protected final String END_DATE = "EndDate";

    protected final String DATA_SET = "DataSet";

    protected final int ALL = 0;

    public int getALL()
    {
        return ALL;
    }

    // -----------------------------------------------------------------------
    // Parameters
    // -----------------------------------------------------------------------

    protected String report;

    protected String reportName;

    protected int reportType;

    protected Collection<String> dataElementId;

    protected Collection<String> indicatorId;

    protected int dataElementGroupId;

    protected int indicatorGroupId;

    protected int designTemplate;

    protected int chartTemplate;

    protected String elementId;

    protected String startDate;

    protected String endDate;

    protected String message;

    protected boolean includeChildren;

    protected int organisationUnitId;

    protected int dataSetId;

    protected InputStream inputStream;

    public String getReport()
    {
        return report;
    }

    public void setReport( String report )
    {
        this.report = report;
    }

    public String getReportName()
    {
        return reportName;
    }

    public void setReportName( String reportName )
    {
        this.reportName = reportName;
    }

    public int getReportType()
    {
        return reportType;
    }

    public void setReportType( int reportType )
    {
        this.reportType = reportType;
    }

    public Collection<String> getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( Collection<String> dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public Collection<String> getIndicatorId()
    {
        return indicatorId;
    }

    public void setIndicatorId( Collection<String> indicatorId )
    {
        this.indicatorId = indicatorId;
    }

    public int getDataElementGroupId()
    {
        return dataElementGroupId;
    }

    public void setDataElementGroupId( int dataElementGroupId )
    {
        this.dataElementGroupId = dataElementGroupId;
    }

    public int getIndicatorGroupId()
    {
        return indicatorGroupId;
    }

    public void setIndicatorGroupId( int indicatorGroupId )
    {
        this.indicatorGroupId = indicatorGroupId;
    }

    public int getChartTemplate()
    {
        return chartTemplate;
    }

    public void setChartTemplate( int chartTemplate )
    {
        this.chartTemplate = chartTemplate;
    }

    public int getDesignTemplate()
    {
        return designTemplate;
    }

    public void setDesignTemplate( int designTemplate )
    {
        this.designTemplate = designTemplate;
    }

    public void setElementId( String elementId )
    {
        this.elementId = elementId;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public boolean isIncludeChildren()
    {
        return includeChildren;
    }

    public void setIncludeChildren( boolean includeChildren )
    {
        this.includeChildren = includeChildren;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public int getDataSetId()
    {
        return dataSetId;
    }

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    public void setInputStream( InputStream inputStream )
    {
        this.inputStream = inputStream;
    }

    // -----------------------------------------------------------------------
    // I18n
    // -----------------------------------------------------------------------

    protected I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    protected I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -----------------------------------------------------------------------
    // Organisation Unit Tree
    // -----------------------------------------------------------------------

    protected int getSelectedOrganisationUnitId()
    {
        int organisationUnitId = 0;

        // OrganisationUnit selectedUnit =
        // selectionManager.getSelectedOrganisationUnit();

        OrganisationUnit selectedUnit = selectionTreeManager.getSelectedOrganisationUnit();

        if ( selectedUnit != null )
        {
            organisationUnitId = selectedUnit.getId();
        }

        return organisationUnitId;
    }

    protected void setRootOrganisationUnit()
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser != null && currentUser.getOrganisationUnits().size() > 0 )
        {
            selectionManager.setRootOrganisationUnits( currentUser.getOrganisationUnits() );
        }
    }

    // -----------------------------------------------------------------------
    // Session
    // -----------------------------------------------------------------------

    protected final Object getSessionVar( String name )
    {
        return ActionContext.getContext().getSession().get( name );
    }

    @SuppressWarnings( "unchecked" )
    protected final void setSessionVar( String name, Object value )
    {
        ActionContext.getContext().getSession().put( name, value );
    }

    protected final boolean containsSessionVar( String name )
    {
        return ActionContext.getContext().getSession().containsKey( name );
    }

    protected final void removeSessionVar( String name )
    {
        ActionContext.getContext().getSession().remove( name );
    }

    // -----------------------------------------------------------------------
    // Validation
    // -----------------------------------------------------------------------

    protected boolean reportName()
    {
        if ( reportName == null || reportName.equals( "" ) )
        {
            message = i18n.getString( "error_reportName" );
            return false;
        }

        return true;
    }

    protected boolean report()
    {
        if ( report == null || report.equals( "" ) )
        {
            message = i18n.getString( "error_report" );
            return false;
        }

        return true;
    }

    protected boolean startDate()
    {
        if ( format.parseDate( startDate ) == null )
        {
            message = i18n.getString( "error_startDate" );
            return false;
        }

        return true;
    }

    protected boolean endDate()
    {
        if ( format.parseDate( endDate ) == null )
        {
            message = i18n.getString( "error_endDate" );
            return false;
        }

        return true;
    }

    protected boolean isAfter()
    {
        if ( !format.parseDate( endDate ).after( format.parseDate( startDate ) ) )
        {
            message = i18n.getString( "error_after" );
            return false;
        }

        return true;
    }

    protected boolean indicatorId()
    {
        if ( indicatorId == null )
        {
            message = i18n.getString( "error_indicatorId" );
            return false;
        }

        return true;
    }

    protected boolean dataElementId()
    {
        if ( dataElementId == null )
        {
            message = i18n.getString( "error_dataElementId" );
            return false;
        }

        return true;
    }

    protected boolean organisationUnitId()
    {
        if ( organisationUnitId == 0 )
        {
            message = i18n.getString( "error_organisationUnitId" );
            return false;
        }

        return true;
    }

    protected boolean dataSetId()
    {
        if ( dataSetId == 0 )
        {
            message = i18n.getString( "error_dataSetId" );
            return false;
        }

        return true;
    }
}
