package org.hisp.dhis.dashboard.ta.action;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;

import com.opensymphony.xwork.Action;

public class GenerateTabularAnalysisResultAction
    implements Action
{

    /* Dependencies */
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private AggregationService aggregationService;

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    /* Parameters */
    private List<OrganisationUnit> selectedOrgUnitList;

    public List<OrganisationUnit> getSelectedOrgUnitList()
    {
        return selectedOrgUnitList;
    }

    private List<DataElement> selectedDataElementList;

    private Map<DataElement, List<Double>> dataValueResult;

    public Map<DataElement, List<Double>> getDataValueResult()
    {
        return dataValueResult;
    }

    private Period startPeriod;

    public Period getStartPeriod()
    {
        return startPeriod;
    }

    private Period endPeriod;

    public Period getEndPeriod()
    {
        return endPeriod;
    }

    private List<String> orgUnitListCB;

    public void setOrgUnitListCB( List<String> orgUnitListCB )
    {
        this.orgUnitListCB = orgUnitListCB;
    }

    private List<String> selectedDataElements;

    public void setSelectedDataElements( List<String> selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }

    private int sDateLB;

    public void setSDateLB( int dateLB )
    {
        sDateLB = dateLB;
    }

    private int eDateLB;

    public void setEDateLB( int dateLB )
    {
        eDateLB = dateLB;
    }

    public String execute()
        throws Exception
    {
        /* OrganisationUnit Info */
        selectedOrgUnitList = new ArrayList<OrganisationUnit>();
        if ( orgUnitListCB == null )
            System.out.println( "orgunit list is null" );
        Iterator orgUnitIterator = orgUnitListCB.iterator();
        while ( orgUnitIterator.hasNext() )
        {
            OrganisationUnit o = organisationUnitService.getOrganisationUnit( Integer
                .parseInt( (String) orgUnitIterator.next() ) );
            selectedOrgUnitList.add( o );
        }

        /* DataElement Info */
        selectedDataElementList = new ArrayList<DataElement>();
        Iterator dataElementIterator = selectedDataElements.iterator();
        while ( dataElementIterator.hasNext() )
        {
            DataElement de = dataElementService
                .getDataElement( Integer.parseInt( (String) dataElementIterator.next() ) );
            selectedDataElementList.add( de );
        }

        /* Period Info */
        startPeriod = periodStore.getPeriod( sDateLB );
        endPeriod = periodStore.getPeriod( eDateLB );

        /* Result Calculation Part */
        double rowTotal = 0.0;
        dataValueResult = new HashMap<DataElement, List<Double>>();
        dataElementIterator = selectedDataElementList.iterator();
        while ( dataElementIterator.hasNext() )
        {
            DataElement de = (DataElement) dataElementIterator.next();

            List<Double> dataValue = new ArrayList<Double>();
            orgUnitIterator = selectedOrgUnitList.iterator();
            rowTotal = 0.0;
            while ( orgUnitIterator.hasNext() )
            {
                OrganisationUnit ou = (OrganisationUnit) orgUnitIterator.next();
                //System.out.println( de.getAlternativeName() + " : " + startPeriod.getStartDate() + " : "
                    //+ endPeriod.getEndDate() + " : " + ou.getShortName() );
                double aggDataValue = 0.0;
                double d = 0.0;
                DataElementCategoryCombo dataElementCategoryCombo = de.getCategoryCombo();

                List<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
                    dataElementCategoryCombo.getOptionCombos() );

                Iterator<DataElementCategoryOptionCombo> optionComboIterator = optionCombos.iterator();
                while ( optionComboIterator.hasNext() )
                {
                    DataElementCategoryOptionCombo decoc = (DataElementCategoryOptionCombo) optionComboIterator.next();

                    aggDataValue = aggregationService.getAggregatedDataValue( de, decoc, startPeriod.getStartDate(),
                        endPeriod.getEndDate(), ou );
                    if ( aggDataValue == -1 )
                        aggDataValue = 0.0;
                    d += aggDataValue;
                }

                d = Math.round( d * Math.pow( 10, 2 ) ) / Math.pow( 10, 2 );
                rowTotal += d;

                dataValue.add( new Double( d ) );
            }// OrgUnit While End
            rowTotal = Math.round( rowTotal * Math.pow( 10, 2 ) ) / Math.pow( 10, 2 );
            dataValue.add( new Double( rowTotal ) );
            dataValueResult.put( de, dataValue );
        }// DataElement While End

        return SUCCESS;
    }

}// class end
