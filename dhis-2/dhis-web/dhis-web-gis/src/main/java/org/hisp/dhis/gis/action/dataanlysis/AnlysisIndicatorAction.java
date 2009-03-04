package org.hisp.dhis.gis.action.dataanlysis;

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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.gis.FeatureService;
import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.comparator.LegendComparator;
import org.hisp.dhis.gis.LegendService;
import org.hisp.dhis.gis.LegendSet;

import org.hisp.dhis.gis.ext.BagSession;
import org.hisp.dhis.gis.ext.Feature;
import org.hisp.dhis.gis.ext.FeatureValueComparator;
import org.hisp.dhis.gis.hibernate.JDBCIndicatorService;
import org.hisp.dhis.gis.state.SelectionManager;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: AnlysisIndicatorAction.java 28-08-2008 16:06:00 $
 */
public class AnlysisIndicatorAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private FeatureService featureService;

    private OrganisationUnitSelectionManager selectionManager;

    private SelectionManager selectionGISManager;

    private StatementManager statementManager;

    private JDBCIndicatorService jdbcIndicatorService;

    private IndicatorService indicatorService;

    private AggregationService aggregationService;

    private LegendService legendService;

    private PeriodService periodService;

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer indicatorId;

    private Integer periodId;

    private Double valueBegin;

    private Double valueEnd;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<Feature> features = new ArrayList<Feature>();

    private LegendSet legendSet;

    // -------------------------------------------------------------------------
    // Getter & setter
    // -------------------------------------------------------------------------

    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    public void setSelectionGISManager( SelectionManager selectionGISManager )
    {
        this.selectionGISManager = selectionGISManager;
    }

    public void setAggregationService( AggregationService aggregationService )
    {

        this.aggregationService = aggregationService;
    }

    public void setLegendService( LegendService legendService )
    {
        this.legendService = legendService;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    public List<Feature> getFeatures()
    {
        return features;
    }

    public LegendSet getLegendSet()
    {
        return legendSet;
    }

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    public void setJdbcIndicatorService( JDBCIndicatorService jdbcIndicatorService )
    {
        this.jdbcIndicatorService = jdbcIndicatorService;
    }

    public void setIndicatorId( Integer indicatorId )
    {
        this.indicatorId = indicatorId;
    }

    public void setFeatureService( FeatureService featureService )
    {
        this.featureService = featureService;
    }

    public void setValueBegin( Double valueBegin )
    {
        this.valueBegin = valueBegin;
    }

    public void setValueEnd( Double valueEnd )
    {
        this.valueEnd = valueEnd;
    }

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private void autoFixLegendSet( double maxValue )
    {

        Legend maxLegend = Collections.max( legendSet.getLegends(), new LegendComparator() );

        if ( maxValue > maxLegend.getMax() )
        {
            NumberFormat formatter = new DecimalFormat( "#0.00" );

            if ( maxLegend.getAutoCreateMax() == Legend.AUTO_CREATE_MAX )
            {

                maxLegend.setMax( Double.parseDouble( formatter.format( maxValue ) ) );

            }
            else
            {
                int red = new Integer( (int) (Math.random() * 255) ).intValue();
                int green = new Integer( (int) (Math.random() * 255) ).intValue();
                int blue = new Integer( (int) (Math.random() * 255) ).intValue();

                String color = (Integer.toHexString( red ) + Integer.toHexString( green ) + Integer.toHexString( blue ))
                    .toUpperCase();

                Legend legendNew = new Legend( "Fix", color, maxLegend.getMax(), Double.parseDouble( formatter
                    .format( maxValue ) ) );

                legendSet.addLegend( legendNew );
            }
        }

    }

    private LegendSet createLegendSet( double min, double max )
    {

        NumberFormat formatter = new DecimalFormat( "#0.00" );

        double section = (max - min) / 5;

        Legend l1 = new Legend( "00FFFF", Double.parseDouble( formatter.format( min ) ), Double.parseDouble( formatter
            .format( min + section ) ) );
        min += section;
        Legend l2 = new Legend( "00CCFF", Double.parseDouble( formatter.format( min ) ), Double.parseDouble( formatter
            .format( min + section ) ) );
        min += section;
        Legend l3 = new Legend( "0066FF", Double.parseDouble( formatter.format( min ) ), Double.parseDouble( formatter
            .format( min + section ) ) );
        min += section;
        Legend l4 = new Legend( "0000FF", Double.parseDouble( formatter.format( min ) ), Double.parseDouble( formatter
            .format( min + section ) ) );
        min += section;
        Legend l5 = new Legend( "3300CC", Double.parseDouble( formatter.format( min ) ), Double.parseDouble( formatter
            .format( min + section ) ) );

        LegendSet legendSet = new LegendSet( "Default" );
        legendSet.addLegend( l1 );
        legendSet.addLegend( l2 );
        legendSet.addLegend( l3 );
        legendSet.addLegend( l4 );
        legendSet.addLegend( l5 );

        return legendSet;

    }

    public String execute()
        throws Exception
    {

        statementManager.initialise();

        OrganisationUnit organisationUnit = selectionManager.getSelectedOrganisationUnit();

        Indicator indicator = indicatorService.getIndicator( new Integer( indicatorId ).intValue() );

        Period period = periodService.getPeriod( periodId.intValue() );

        selectionGISManager.setSelectedIndicator( indicator );

        selectionGISManager.setPeriod( period );

        System.out.println( "1" );

        for ( OrganisationUnit org : organisationUnit.getChildren() )
        {

            double indicatorValue = jdbcIndicatorService.getIndicatorvalue( org, period, indicator );

            if ( indicatorValue < 0.0 )
            {

                indicatorValue = 0;
            }

            if ( valueBegin != null && valueEnd != null )
            {

                if ( valueBegin <= indicatorValue && valueEnd >= indicatorValue )
                {

                    org.hisp.dhis.gis.Feature feature = featureService.get( org );

                    features.add( new Feature( feature, indicatorValue, "#CCCCCC" ) );

                }

            }
            else
            {

                org.hisp.dhis.gis.Feature feature = featureService.get( org );

                features.add( new Feature( feature, indicatorValue, "#CCCCCC" ) );
            }

        }

        double max = Collections.max( features, new FeatureValueComparator() ).getAggregatedDataValue();

        double min = Collections.min( features, new FeatureValueComparator() ).getAggregatedDataValue();

        legendSet = legendService.getLegendSet( indicator );

        if ( legendSet == null )
        {

            legendSet = createLegendSet( min, max );

        }
        else
        {

            autoFixLegendSet( max );
        }

        legendSet.sortLegend( new LegendComparator() );

        for ( Feature feature : features )
        {
            for ( Legend legend : legendSet.getLegends() )
            {

                if ( feature.getAggregatedDataValue() >= legend.getMin()
                    && feature.getAggregatedDataValue() <= legend.getMax() )
                {

                    feature.setColor( "#" + legend.getColor() );

                }
            }

            NumberFormat formatter = new DecimalFormat( "#0.00" );

            feature.setAggregatedDataValue( new Double( formatter.format( feature.getAggregatedDataValue() ) )
                .doubleValue() );

        }

        selectionGISManager.setSeletedBagSession( new BagSession( indicator, period.getStartDate().toString(), period
            .getEndDate().toString(), features, legendSet ) );

        valueBegin = valueEnd = null;

        statementManager.destroy();

        return SUCCESS;
    }

}
