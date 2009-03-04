package org.hisp.dhis.dashboard.aa.action;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.dashboard.util.PeriodStartDateComparator;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementGroupNameComparator;
import org.hisp.dhis.dataelement.comparator.DataElementNameComparator;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.comparator.IndicatorGroupNameComparator;
import org.hisp.dhis.indicator.comparator.IndicatorNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork.ActionSupport;

public class GenerateAnnualAnalyserFormAction
    extends ActionSupport
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private final int ALL = 0;

    private final int DATAVALUE = 1;

    private final int INDICATORVALUE = 2;

    public int getALL()
    {
        return ALL;
    }

    public int getDATAVALUE()
    {
        return DATAVALUE;
    }

    public int getINDICATORVALUE()
    {
        return INDICATORVALUE;
    }

    /* Parameters */
    private List<DataElement> dataElements;

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    private List<DataElementGroup> dataElementGroups;

    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    private List<Indicator> indicators;

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    private List<IndicatorGroup> indicatorGroups;

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    private Collection<OrganisationUnit> organisationUnits;

    public Collection<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private List<Period> yearlyPeriods;

    public List<Period> getYearlyPeriods()
    {
        return yearlyPeriods;
    }

    private SimpleDateFormat simpleDateFormat;

    public SimpleDateFormat getSimpleDateFormat()
    {
        return simpleDateFormat;
    }

    private List<String> monthNames;

    public List<String> getMonthNames()
    {
        return monthNames;
    }

    public String execute()
        throws Exception
    {
        /* OrganisationUnit */
        //organisationUnits = organisationUnitService.getAllOrganisationUnits();

        /* DataElements and Groups */
        dataElements = new ArrayList<DataElement>(dataElementService.getAllDataElements());
        dataElementGroups = new ArrayList<DataElementGroup>(dataElementService.getAllDataElementGroups());
        
        Collections.sort( dataElements, new DataElementNameComparator() );
        Collections.sort( dataElementGroups, new DataElementGroupNameComparator() );

        /* Indicators and Groups */
        indicators = new ArrayList<Indicator>(indicatorService.getAllIndicators());
        indicatorGroups = new ArrayList<IndicatorGroup>(indicatorService.getAllIndicatorGroups());

        Collections.sort( indicators, new IndicatorNameComparator() );
        Collections.sort( indicatorGroups, new IndicatorGroupNameComparator() );
        
        /* Yearly Periods */
        PeriodType yearlyPeriodType = PeriodType.getPeriodTypeByName( "Yearly" );
        yearlyPeriods = new ArrayList<Period>( periodStore.getPeriodsByPeriodType( yearlyPeriodType ) );
        Collections.sort( yearlyPeriods, new PeriodStartDateComparator() );
        simpleDateFormat = new SimpleDateFormat( "yyyy" );

        /* Month Names */
        monthNames = new ArrayList<String>();
        monthNames.add( "Jan" );
        monthNames.add( "Feb" );
        monthNames.add( "Mar" );
        monthNames.add( "Apr" );
        monthNames.add( "May" );
        monthNames.add( "Jun" );
        monthNames.add( "Jul" );
        monthNames.add( "Aug" );
        monthNames.add( "Sep" );
        monthNames.add( "Oct" );
        monthNames.add( "Nov" );
        monthNames.add( "Dec" );

        return SUCCESS;
    }
}
