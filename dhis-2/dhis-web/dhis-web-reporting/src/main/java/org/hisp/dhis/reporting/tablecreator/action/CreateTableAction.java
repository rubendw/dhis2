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
import java.util.Date;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.reporttable.RelativePeriods;
import org.hisp.dhis.reporttable.ReportParams;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.reporttable.ReportTableInternalProcess;
import org.hisp.dhis.reporting.tablecreator.util.InternalProcessUtil;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.system.util.CollectionConversionUtils;
import org.hisp.dhis.user.CurrentUserService;

import static org.hisp.dhis.system.util.ConversionUtils.getIntegerCollection;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CreateTableAction
    implements Action
{
    private static final String PROCTYPE_REPORTTABLE = "ReportTable";
    
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
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer tableId;

    public void setTableId( Integer id )
    {
        this.tableId = id;
    }
    
    private String tableName;

    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }
    
    private boolean skipDataMart;

    public void setSkipDataMart( boolean skipDataMart )
    {
        this.skipDataMart = skipDataMart;
    }
    
    private String mode;

    public void setMode( String mode )
    {
        this.mode = mode;
    }
    
    private boolean doIndicators;

    public void setDoIndicators( boolean doIndicators )
    {
        this.doIndicators = doIndicators;
    }

    private boolean doPeriods;

    public void setDoPeriods( boolean doPeriods )
    {
        this.doPeriods = doPeriods;
    }

    private boolean doOrganisationUnits;

    public void setDoOrganisationUnits( boolean doUnits )
    {
        this.doOrganisationUnits = doUnits;
    }

    private List<String> selectedDataElements = new ArrayList<String>();

    public void setSelectedDataElements( List<String> selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }
    
    private List<String> selectedIndicators = new ArrayList<String>();

    public void setSelectedIndicators( List<String> selectedIndicators )
    {
        this.selectedIndicators = selectedIndicators;
    }

    private List<String> selectedDataSets = new ArrayList<String>();

    public void setSelectedDataSets( List<String> selectedDataSets )
    {
        this.selectedDataSets = selectedDataSets;
    }
    
    private List<String> selectedPeriods = new ArrayList<String>();

    public void setSelectedPeriods( List<String> selectedPeriods )
    {
        this.selectedPeriods = selectedPeriods;
    }

    private List<String> selectedOrganisationUnits = new ArrayList<String>();

    public void setSelectedOrganisationUnits( List<String> selectedOrganisationUnits )
    {
        this.selectedOrganisationUnits = selectedOrganisationUnits;
    }

    private Integer reportingPeriod;

    public void setReportingPeriod( Integer reportingPeriod )
    {
        this.reportingPeriod = reportingPeriod;
    }
    
    private boolean reportingMonth;

    public void setReportingMonth( boolean reportingMonth )
    {
        this.reportingMonth = reportingMonth;
    }

    private boolean last3Months;

    public void setLast3Months( boolean last3Months )
    {
        this.last3Months = last3Months;
    }

    private boolean last6Months;

    public void setLast6Months( boolean last6Months )
    {
        this.last6Months = last6Months;
    }
    
    private boolean last9Months;

    public void setLast9Months( boolean last9Months )
    {
        this.last9Months = last9Months;
    }
    
    private boolean last12Months;

    public void setLast12Months( boolean last12Months )
    {
        this.last12Months = last12Months;
    }
    
    private boolean soFarThisYear;

    public void setSoFarThisYear( boolean soFarThisYear )
    {
        this.soFarThisYear = soFarThisYear;
    }

    private boolean soFarThisFinancialYear;

    public void setSoFarThisFinancialYear( boolean soFarThisFinancialYear )
    {
        this.soFarThisFinancialYear = soFarThisFinancialYear;
    }
    
    private boolean last3To6Months;

    public void setLast3To6Months( boolean last3To6Months )
    {
        this.last3To6Months = last3To6Months;
    }

    private boolean last6To9Months;

    public void setLast6To9Months( boolean last6To9Months )
    {
        this.last6To9Months = last6To9Months;
    }

    private boolean last9To12Months;

    public void setLast9To12Months( boolean last9To12Months )
    {
        this.last9To12Months = last9To12Months;
    }
    
    private boolean last12IndividualMonths;

    public void setLast12IndividualMonths( boolean last12IndividualMonths )
    {
        this.last12IndividualMonths = last12IndividualMonths;
    }
    
    private boolean individualMonthsThisYear;

    public void setIndividualMonthsThisYear( boolean individualMonthsThisYear )
    {
        this.individualMonthsThisYear = individualMonthsThisYear;
    }

    private boolean individualQuartersThisYear;

    public void setIndividualQuartersThisYear( boolean individualQuartersThisYear )
    {
        this.individualQuartersThisYear = individualQuartersThisYear;
    }
    
    private boolean paramReportingMonth;

    public void setParamReportingMonth( boolean paramReportingMonth )
    {
        this.paramReportingMonth = paramReportingMonth;
    }

    private boolean paramParentOrganisationUnit;

    public void setParamParentOrganisationUnit( boolean paramParentOrganisationUnit )
    {
        this.paramParentOrganisationUnit = paramParentOrganisationUnit;
    }
    
    private boolean paramOrganisationUnit;

    public void setParamOrganisationUnit( boolean paramOrganisationUnit )
    {
        this.paramOrganisationUnit = paramOrganisationUnit;
    }
        
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        ReportTable reportTable = getReportTable();
        
        String owner = currentUserService.getCurrentUsername();
        
        ReportTableInternalProcess reportTableProcess = internalProcessCoordinator.newProcess( PROCTYPE_REPORTTABLE, owner );
        
        reportTableProcess.addReportTable( reportTable );
        reportTableProcess.setDoDataMart( !skipDataMart );
        
        String id = internalProcessCoordinator.requestProcessExecution( reportTableProcess );
        
        InternalProcessUtil.setCurrentRunningProcess( id );
        
        return SUCCESS;
    }
    
    public String save()
        throws Exception
    {
        ReportTable reportTable = getReportTable();
        
        reportTableService.saveReportTable( reportTable );
        
        return SUCCESS;
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private ReportTable getReportTable()
        throws Exception
    {
        List<DataElement> dataElements = new CollectionConversionUtils<DataElement>().getList( 
            dataElementService.getDataElements( getIntegerCollection( selectedDataElements ) ) );
        
        List<Indicator> indicators = new CollectionConversionUtils<Indicator>().getList( 
            indicatorService.getIndicators( getIntegerCollection( selectedIndicators ) ) );
        
        List<DataSet> dataSets = new CollectionConversionUtils<DataSet>().getList( 
            dataSetService.getDataSets( getIntegerCollection( selectedDataSets ) ) );
        
        List<Period> periods = new CollectionConversionUtils<Period>().getList( 
            periodService.getPeriods( getIntegerCollection( selectedPeriods ) ) );
        
        List<OrganisationUnit> organisationUnits = new CollectionConversionUtils<OrganisationUnit>().getList( 
            organisationUnitService.getOrganisationUnits( getIntegerCollection( selectedOrganisationUnits ) ) );

        RelativePeriods relatives = new RelativePeriods();
        
        relatives.setReportingMonth( reportingMonth );
        relatives.setLast3Months( last3Months );
        relatives.setLast6Months( last6Months );
        relatives.setLast9Months( last9Months );
        relatives.setLast12Months( last12Months );
        relatives.setSoFarThisYear( soFarThisYear );
        relatives.setSoFarThisFinancialYear( soFarThisFinancialYear );
        relatives.setLast3To6Months( last3To6Months );
        relatives.setLast6To9Months( last6To9Months );
        relatives.setLast9To12Months( last9To12Months );
        relatives.setLast12IndividualMonths( last12IndividualMonths );
        relatives.setIndividualMonthsThisYear( individualMonthsThisYear );
        relatives.setIndividualQuartersThisYear( individualQuartersThisYear );
        
        List<Period> relativePeriods = reportTableService.getRelativePeriods( relatives, reportingPeriod );
        
        ReportParams reportParams = new ReportParams();
        
        reportParams.setParamReportingMonth( paramReportingMonth );
        reportParams.setParamParentOrganisationUnit( paramParentOrganisationUnit );
        reportParams.setParamOrganisationUnit( paramOrganisationUnit );
        
        Date date = reportTableService.getDateFromPreviousMonth( reportingPeriod );
        
        MonthlyPeriodType periodType = new MonthlyPeriodType();
        
        Period period = periodType.createPeriod( date );
        
        String reportingMonthName = format.formatPeriod( period );
        
        ReportTable reportTable = null;
        
        if ( tableId == null )
        {
            reportTable = new ReportTable( tableName, mode,
                dataElements, indicators, dataSets, periods, relativePeriods, organisationUnits, 
                doIndicators, doPeriods, doOrganisationUnits, relatives, reportParams, 
                format, reportingMonthName );
        }
        else
        {
            reportTable = reportTableService.getReportTable( tableId );
            
            reportTable.setName( tableName );
            reportTable.setDataElements( dataElements );
            reportTable.setIndicators( indicators );
            reportTable.setDataSets( dataSets );
            reportTable.setPeriods( periods );
            reportTable.setRelativePeriods( relativePeriods );
            reportTable.setUnits( organisationUnits );
            reportTable.setDoIndicators( doIndicators );
            reportTable.setDoPeriods( doPeriods );
            reportTable.setDoUnits( doOrganisationUnits );
            reportTable.setRelatives( relatives );
            reportTable.setReportParams( reportParams );
            reportTable.setI18nFormat( format );
            reportTable.setReportingMonthName( reportingMonthName );
            
            reportTable.init();
        }
        
        return reportTable;
    }
}
