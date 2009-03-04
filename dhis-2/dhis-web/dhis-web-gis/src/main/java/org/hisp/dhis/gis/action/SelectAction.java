package org.hisp.dhis.gis.action;

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
import java.util.*;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.gis.FeatureService;
import org.hisp.dhis.gis.GISConfiguration;
import org.hisp.dhis.gis.MapFile;
import org.hisp.dhis.gis.action.configuration.*;
import org.hisp.dhis.gis.state.SelectionManager;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.comparator.IndicatorNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.comparator.PeriodComparator;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: SelectAction.java 28-01-2008 16:06:00 $
 */

public class SelectAction
    implements Action
{

    private OrganisationUnitSelectionManager selectionManager;

    private DataSetService dataSetService;

    private IndicatorService indicatorService;

    private FeatureService featureService;

    private SelectionManager selectionGISManager;

    private GISConfigurationManagerService gisConfigurationManagerService;

    private PeriodService periodService;

    /*------------------------------------
     * Input & Output
     *------------------------------------*/

    private List<PeriodType> periodTypes;

    private List<Period> periods;

    private List<IndicatorGroup> indicatorGroups = new ArrayList<IndicatorGroup>();

    private List<Indicator> indicators = new ArrayList<Indicator>();

    private OrganisationUnit organisationUnit;

    private MapFile mapFile;

    private AggregationService aggregationService;

    private Integer indicatorGroupId;

    private Integer indicatorId;

    private String getIndicatorFrom;

    private PeriodType periodType;

    private Period period;

    private String startDate;

    private String endDate;

    /*------------------------------------
     * Geter & Setter
     *------------------------------------*/

    public String getStartDate()
    {
        return startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setSelectionGISManager( SelectionManager selectionGISManager )
    {
        this.selectionGISManager = selectionGISManager;
    }

    public AggregationService getAggregationService()
    {
        return aggregationService;
    }

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    public DataSetService getDataSetService()
    {
        return dataSetService;
    }

    public List<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }

    public List<Period> getPeriods()
    {
        return periods;
    }

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    public void setFeatureService( FeatureService featureService )
    {
        this.featureService = featureService;
    }

    public MapFile getMapFile()
    {
        return mapFile;
    }

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    public Integer getIndicatorGroupId()
    {
        return indicatorGroupId;
    }

    public Integer getIndicatorId()
    {
        return indicatorId;
    }

    public String getGetIndicatorFrom()
    {
        return getIndicatorFrom;
    }

    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    public PeriodType getPeriodType()
    {
        return periodType;
    }

    public Period getPeriod()
    {
        return period;
    }

    public String execute()
        throws Exception
    {

        if ( gisConfigurationManagerService.isNULL( GISConfiguration.KEY_DIRECTORY )
            || gisConfigurationManagerService.isNULL( GISConfiguration.KEY_GETINDICATOR ) )
        {
            return "configuration";
        }

        organisationUnit = selectionManager.getSelectedOrganisationUnit();

        if ( organisationUnit != null )
        {

            mapFile = featureService.getMapFile( organisationUnit );

        }

        if ( mapFile == null )
        {
            mapFile = new MapFile();
            mapFile.setMapFile( "example.svg" );
        }

        indicatorGroups = new ArrayList<IndicatorGroup>( indicatorService.getAllIndicatorGroups() );
        
        IndicatorGroup indicatorGroupTemp = selectionGISManager.getSelectedIndicatorGroup();

        if ( indicatorGroupTemp != null )
        {

            indicators = new ArrayList<Indicator>( indicatorGroupTemp.getMembers() );

            Collections.sort( indicators, new IndicatorNameComparator() );

            indicatorGroupId = indicatorGroupTemp.getId();

            Indicator indiacatorTemp = selectionGISManager.getSelectedIndicator();

            if ( indiacatorTemp != null )
            {

                indicatorId = indiacatorTemp.getId();
            }

        }

        getIndicatorFrom = gisConfigurationManagerService.getIndicatorFrom();

        if ( getIndicatorFrom.equals( GISConfiguration.AggregatedIndicatorValue ) )
        {

            periodTypes = periodService.getAllPeriodTypes();

            periodType = selectionGISManager.getPeriodTypeSelected();

            if ( periodType != null )
            {

                periods = new ArrayList<Period>( periodService.getPeriodsByPeriodType( periodType ) );

            }
            else
            {
                periods = new ArrayList<Period>( periodService.getAllPeriods() );

            }

            Collections.sort( periods, new PeriodComparator() );
        }
        else
        {
            startDate = selectionGISManager.getSelectedStartDate();

            endDate = selectionGISManager.getSelectedEndtDate();
        }

        return SUCCESS;
    }

}
