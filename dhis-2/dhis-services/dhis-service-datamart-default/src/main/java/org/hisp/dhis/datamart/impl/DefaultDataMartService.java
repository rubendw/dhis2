package org.hisp.dhis.datamart.impl;

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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.DataMartInternalProcess;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.datamart.aggregation.cache.AggregationCache;
import org.hisp.dhis.datamart.aggregation.dataelement.DataElementAggregator;
import org.hisp.dhis.datamart.crosstab.CrossTabService;
import org.hisp.dhis.datamart.dataelement.DataElementDataMart;
import org.hisp.dhis.datamart.indicator.IndicatorDataMart;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.TimeUtils;

import static org.hisp.dhis.datamart.util.ParserUtil.getDataElementsInFormula;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultDataMartService.java 5707 2008-09-15 13:26:12Z larshelg $
 */
public class DefaultDataMartService
    extends DataMartInternalProcess
{
    private static final Log log = LogFactory.getLog( DefaultDataMartService.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected AggregationCache aggregationCache;
        
    public void setAggregationCache( AggregationCache aggregationCache )
    {
        this.aggregationCache = aggregationCache;
    }
    
    private DataMartStore dataMartStore;

    public void setDataMartStore( DataMartStore dataMartStore )
    {
        this.dataMartStore = dataMartStore;
    }

    private CrossTabService crossTabService;

    public void setCrossTabService( CrossTabService crossTabService )
    {
        this.crossTabService = crossTabService;
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
    
    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService categoryOptionComboService )
    {
        this.categoryOptionComboService = categoryOptionComboService;
    }
    
    private DataElementDataMart dataElementDataMart;

    public void setDataElementDataMart( DataElementDataMart dataElementDataMart )
    {
        this.dataElementDataMart = dataElementDataMart;
    }

    private IndicatorDataMart indicatorDataMart;

    public void setIndicatorDataMart( IndicatorDataMart indicatorDataMart )
    {
        this.indicatorDataMart = indicatorDataMart;
    }
    
    private DataElementAggregator sumIntAggregator;

    public void setSumIntAggregator( DataElementAggregator sumIntDataElementAggregator )
    {
        this.sumIntAggregator = sumIntDataElementAggregator;
    }

    private DataElementAggregator averageIntAggregator;

    public void setAverageIntAggregator( DataElementAggregator averageIntDataElementAggregator )
    {
        this.averageIntAggregator = averageIntDataElementAggregator;
    }

    private DataElementAggregator sumBoolAggregator;

    public void setSumBoolAggregator( DataElementAggregator sumBooleanDataElementAggregator )
    {
        this.sumBoolAggregator = sumBooleanDataElementAggregator;
    }

    private DataElementAggregator averageBoolAggregator;

    public void setAverageBoolAggregator( DataElementAggregator averageBooleanDataElementAggregator )
    {
        this.averageBoolAggregator = averageBooleanDataElementAggregator;
    }

    // -------------------------------------------------------------------------
    // DataMartInternalProcess implementation
    // -------------------------------------------------------------------------

    public int export( Collection<Integer> dataElementIds, Collection<Integer> indicatorIds,
        Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        int count = 0;
                
        log.info( "Export process started" );
        
        TimeUtils.start();

        setProgress( 0, "deleting_existing_aggregated_data" );

        // ---------------------------------------------------------------------
        // Delete existing aggregated data
        // ---------------------------------------------------------------------

        dataMartStore.deleteAggregatedDataValues( dataElementIds, periodIds, organisationUnitIds );
        
        dataMartStore.deleteAggregatedIndicatorValues( indicatorIds, periodIds, organisationUnitIds );
        
        log.info( "Deleted existing aggregated data" );

        setProgress( 10, "crosstabulating_data" );

        // ---------------------------------------------------------------------
        // Crosstabulate data
        // ---------------------------------------------------------------------

        Collection<Integer> dataElementAndIndicatorIds = new HashSet<Integer>();        
        Collection<Integer> dataElementInIndicatorIds = getDataElementIdentifiers( indicatorIds );
        
        dataElementAndIndicatorIds.addAll( dataElementIds );
        dataElementAndIndicatorIds.addAll( dataElementInIndicatorIds );
        
        Collection<Operand> dataElementAndIndicatorOperands = categoryOptionComboService.getOperands( dataElementService.getDataElements( dataElementAndIndicatorIds ) );
        Collection<Operand> dataElementInIndicatorOperands = categoryOptionComboService.getOperands( dataElementService.getDataElements( dataElementInIndicatorIds ) );
        
        crossTabService.populateCrossTabTable( dataElementAndIndicatorOperands, getIntersectingIdentifiers( periodIds ), 
            getIdentifiersWithChildren( organisationUnitIds ), getTemporaryState( 10, 40 ) );

        clearTemporaryState();
        
        log.info( "Crosstabulated data" );

        Collection<DataElement> dataElements = dataElementService.getDataElements( dataElementIds );
        
        Collection<Operand> sumInt = getOperands( dataElements, DataElement.AGGREGATION_OPERATOR_SUM, DataElement.TYPE_INT );
        Collection<Operand> averageInt = getOperands( dataElements, DataElement.AGGREGATION_OPERATOR_AVERAGE, DataElement.TYPE_INT );
        Collection<Operand> sumBoolean = getOperands( dataElements, DataElement.AGGREGATION_OPERATOR_SUM, DataElement.TYPE_BOOL );
        Collection<Operand> averageBoolean = getOperands( dataElements, DataElement.AGGREGATION_OPERATOR_AVERAGE, DataElement.TYPE_BOOL );

        // ---------------------------------------------------------------------
        // Data element export
        // ---------------------------------------------------------------------

        setProgress( 40, "exporting_data_for_data_elements" );

        if ( sumInt.size() > 0 )
        {
            count += dataElementDataMart.exportDataValues( sumInt, periodIds, organisationUnitIds, sumIntAggregator );
        
            log.info( "Exported values for data elements with sum aggregation operator of type number" );
        }

        setProgress( 50, "exporting_data_for_data_elements" );

        if ( averageInt.size() > 0 )
        {            
            count += dataElementDataMart.exportDataValues( averageInt, periodIds, organisationUnitIds, averageIntAggregator );
        
            log.info( "Exported values for data elements with average aggregation operator of type number" );
        }

        setProgress( 60, "exporting_data_for_data_elements" );

        if ( sumBoolean.size() > 0 )
        {
            count += dataElementDataMart.exportDataValues( sumBoolean, periodIds, organisationUnitIds, sumBoolAggregator );
            
            log.info( "Exported values for data elements with sum aggregation operator of type yes/no" );
        }

        setProgress( 70, "exporting_data_for_data_elements" );

        if ( averageBoolean.size() > 0 )
        {
            count += dataElementDataMart.exportDataValues( averageBoolean, periodIds, organisationUnitIds, averageBoolAggregator );
            
            log.info( "Exported values for data elements with average aggregation operator of type yes/no" );
        }

        setProgress( 80, "exporting_data_for_indicators" );

        // ---------------------------------------------------------------------
        // Indicator export
        // ---------------------------------------------------------------------

        if ( indicatorIds != null && indicatorIds.size() > 0 )
        {
            count += indicatorDataMart.exportIndicatorValues( indicatorIds, periodIds, organisationUnitIds, dataElementInIndicatorOperands );
            
            log.info( "Exported indicator values" );
        }

        crossTabService.dropCrossTabTable();
        
        log.info( "Export process completed: " + TimeUtils.getHMS() );

        setProgress( 100, "export_process_done" );
        
        return count;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns all data element identifiers included in the indicators in the given identifier collection.
     */
    private Set<Integer> getDataElementIdentifiers( Collection<Integer> indicatorIds )
    {
        Set<Integer> identifiers = new HashSet<Integer>();
        
        for ( Integer id : indicatorIds )
        {
            Indicator indicator = indicatorService.getIndicator( id );
            
            identifiers.addAll( getDataElementsInFormula( indicator.getNumerator() ) );
            identifiers.addAll( getDataElementsInFormula( indicator.getDenominator() ) );            
        }
        
        return identifiers;
    }
    
    /**
     * Returns the idenfifiers in given collection including all of its children.
     */
    private Collection<Integer> getIdentifiersWithChildren( Collection<Integer> organisationUnitIds )
    {
        OrganisationUnitHierarchy hierarchy = aggregationCache.getLatestOrganisationUnitHierarchy();
        
        Set<Integer> identifers = new HashSet<Integer>();
        
        for ( Integer id : organisationUnitIds )
        {
            Collection<Integer> children = aggregationCache.getChildren( hierarchy, id );
            
            identifers.addAll( children );
        }
        
        return identifers;
    }
    
    /**
     * Returns the identifiers of the periods in the given collection including all intersecting periods.
     */
    private Collection<Integer> getIntersectingIdentifiers( Collection<Integer> periodIds )
    {
        Set<Integer> identifiers = new HashSet<Integer>();
        
        for ( Integer id : periodIds )
        {
            Period period = periodService.getPeriod( id );
            
            Collection<Period> periods = periodService.getIntersectingPeriods( period.getStartDate(), period.getEndDate() );
            
            identifiers.addAll( ConversionUtils.getIdentifiers( Period.class, periods ) );
        }
        
        return identifiers;
    }
    
    /**
     * Sorts out the data element identifers of the given aggregation operator and the given type.
     */
    private Collection<Operand> getOperands( Collection<DataElement> dataElements, String aggregationOperator, String type )
    {
        Collection<DataElement> section = new ArrayList<DataElement>();
        
        for ( DataElement element : dataElements )
        {
            if ( element.getAggregationOperator().equals( aggregationOperator ) && element.getType().equals( type ) )
            {
                section.add( element );
            }
        }
        
        return categoryOptionComboService.getOperands( section );
    }
}
