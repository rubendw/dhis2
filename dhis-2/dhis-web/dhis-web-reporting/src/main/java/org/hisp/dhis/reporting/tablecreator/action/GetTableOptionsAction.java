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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementGroupNameComparator;
import org.hisp.dhis.dataelement.comparator.DataElementNameComparator;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dataset.comparator.DataSetNameComparator;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.comparator.IndicatorGroupNameComparator;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.comparator.PeriodComparator;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetTableOptionsAction
    implements Action
{
    private static final int AVAILABLE_REPORTING_MONTHS = 12;
    
    private final int ALL = 0;

    public int getALL()
    {
        return ALL;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
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
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    private Comparator<OrganisationUnit> organisationUnitComparator;

    public void setOrganisationUnitComparator( Comparator<OrganisationUnit> organisationUnitComparator )
    {
        this.organisationUnitComparator = organisationUnitComparator;
    }
    
    private DisplayPropertyHandler displayPropertyHandler;
    
    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
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

    public String getMode()
    {
        return mode;
    }

    public void setMode( String mode )
    {
        this.mode = mode;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataElementGroup> dataElementGroups = new ArrayList<DataElementGroup>();

    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }
    
    private List<DataElement> dataElements = new ArrayList<DataElement>();

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }
    
    private List<IndicatorGroup> indicatorGroups = new ArrayList<IndicatorGroup>();

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }
    
    private List<Indicator> indicators = new ArrayList<Indicator>();

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    private List<DataSet> dataSets = new ArrayList<DataSet>();

    public List<DataSet> getDataSets()
    {
        return dataSets;
    }
    
    private List<PeriodType> periodTypes = new ArrayList<PeriodType>();

    public List<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }

    private List<Period> periods = new ArrayList<Period>();

    public List<Period> getPeriods()
    {
        return periods;
    }
    
    private List<OrganisationUnitLevel> levels = new ArrayList<OrganisationUnitLevel>();

    public List<OrganisationUnitLevel> getLevels()
    {
        return levels;
    }
    
    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }
    
    private ReportTable reportTable;

    public ReportTable getReportTable()
    {
        return reportTable;
    }
    
    private List<DataElement> selectedDataElements = new ArrayList<DataElement>();

    public List<DataElement> getSelectedDataElements()
    {
        return selectedDataElements;
    }
    
    private List<Indicator> selectedIndicators = new ArrayList<Indicator>();

    public List<Indicator> getSelectedIndicators()
    {
        return selectedIndicators;
    }

    private List<DataSet> selectedDataSets = new ArrayList<DataSet>();

    public List<DataSet> getSelectedDataSets()
    {
        return selectedDataSets;
    }    
    
    private List<Period> selectedPeriods = new ArrayList<Period>();

    public List<Period> getSelectedPeriods()
    {
        return selectedPeriods;
    }
    
    private List<OrganisationUnit> selectedOrganisationUnits = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getSelectedOrganisationUnits()
    {
        return selectedOrganisationUnits;
    }
    
    private SortedMap<Integer, String> reportingPeriods = new TreeMap<Integer, String>();

    public SortedMap<Integer, String> getReportingPeriods()
    {
        return reportingPeriods;
    }
    
    private Comparator<Indicator> indicatorComparator;
    
    public void setIndicatorComparator( Comparator<Indicator> indicatorComparator )
    {
        this.indicatorComparator = indicatorComparator;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        // ---------------------------------------------------------------------
        // Available metadata
        // ---------------------------------------------------------------------

        if ( mode != null && mode.equals( ReportTable.MODE_DATAELEMENTS ) )
        {
            dataElementGroups = new ArrayList<DataElementGroup>( dataElementService.getAllDataElementGroups() );
            
            Collections.sort( dataElementGroups, new DataElementGroupNameComparator() );
            
            dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );
            
            Collections.sort( dataElements, new DataElementNameComparator() );
            
            dataElements = displayPropertyHandler.handleDataElements( dataElements );
        }
        else if ( mode != null && mode.equals( ReportTable.MODE_INDICATORS ) )
        {
            indicatorGroups = new ArrayList<IndicatorGroup>( indicatorService.getAllIndicatorGroups() );
            
            Collections.sort( indicatorGroups, new IndicatorGroupNameComparator() );
                    
            indicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );
            
            Collections.sort( indicators, indicatorComparator );
            
            indicators = displayPropertyHandler.handleIndicators( indicators );
        }
        else if ( mode != null && mode.equals( ReportTable.MODE_DATASETS ) )
        {
            dataSets = new ArrayList<DataSet>( dataSetService.getAllDataSets() );
            
            Collections.sort( dataSets, new DataSetNameComparator() );            
        }
        
        periodTypes = periodService.getAllPeriodTypes();
        
        periods = new ArrayList<Period>( periodService.getPeriodsByPeriodType( MonthlyPeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME ) ) );

        Collections.sort( periods, new PeriodComparator() );
        
        levels = organisationUnitService.getOrganisationUnitLevels();
        
        organisationUnits = new ArrayList<OrganisationUnit>( organisationUnitService.getAllOrganisationUnits() );

        organisationUnits = displayPropertyHandler.handleOrganisationUnits( organisationUnits );

        Collections.sort( organisationUnits, organisationUnitComparator );
        
        // ---------------------------------------------------------------------
        // Reporting periods
        // ---------------------------------------------------------------------

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
        
        // ---------------------------------------------------------------------
        // Report table
        // ---------------------------------------------------------------------

        if ( id != null )
        {
            reportTable = reportTableService.getReportTable( id );
            
            dataElements.removeAll( reportTable.getDataElements() );
            
            indicators.removeAll( reportTable.getIndicators() );
            
            dataSets.removeAll( reportTable.getDataSets() );
            
            periods.removeAll( reportTable.getPeriods() );
            
            organisationUnits.removeAll( reportTable.getUnits() );
            
            selectedDataElements = reportTable.getDataElements();
            
            selectedIndicators = reportTable.getIndicators();
            
            selectedDataSets = reportTable.getDataSets();
            
            selectedPeriods = reportTable.getPeriods();
            
            selectedOrganisationUnits = reportTable.getUnits();
        }
        
        return SUCCESS;
    }
}
