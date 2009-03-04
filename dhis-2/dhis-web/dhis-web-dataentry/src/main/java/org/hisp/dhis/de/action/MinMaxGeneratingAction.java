package org.hisp.dhis.de.action;

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
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.de.history.DataElementHistory;
import org.hisp.dhis.de.history.HistoryRetriever;
import org.hisp.dhis.order.manager.DataElementOrderManager;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork.Action;

/**
 * @author Margrethe Store
 * @version $Id: MinMaxGeneratingAction.java 5568 2008-08-21 13:47:11Z larshelg $
 */
public class MinMaxGeneratingAction
    implements Action
{
    private static final int HISTORY_LENGTH = 6;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HistoryRetriever historyRetriever;

    public void setHistoryRetriever( HistoryRetriever historyRetriever )
    {
        this.historyRetriever = historyRetriever;
    }

    private MinMaxDataElementStore minMaxDataElementStore;

    public void setMinMaxDataElementStore( MinMaxDataElementStore minMaxDataElementStore )
    {
        this.minMaxDataElementStore = minMaxDataElementStore;
    }

    private DataElementOrderManager dataElementOrderManager;

    public void setDataElementOrderManager( DataElementOrderManager dataElementOrderManager )
    {
        this.dataElementOrderManager = dataElementOrderManager;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<MinMaxDataElement> minMaxDataElements;

    public List<MinMaxDataElement> getMinMaxDataElements()
    {
        return minMaxDataElements;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        minMaxDataElements = new ArrayList<MinMaxDataElement>();

        Period period = selectedStateManager.getSelectedPeriod();

        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        DataSet dataSet = selectedStateManager.getSelectedDataSet();

        List<DataElement> dataElements = new ArrayList<DataElement>( dataElementOrderManager
            .getOrderedDataElements( dataSet ) );

        for ( DataElement dataelement : dataElements )
        {
            if ( dataelement.getType().equals( DataElement.TYPE_INT ) )
            {
                DataElementHistory dataElementHistory = historyRetriever.getHistory( dataelement, organisationUnit,
                    period, HISTORY_LENGTH );

                setMinMaxLimits( dataElementHistory, organisationUnit, dataelement );
            }
        }

        return SUCCESS;
    }

    /**
     * Finds the maximum and the minimum entred value for each dataelement in
     * the given orgunit, and creates min/max limits 10% lower/higher then the
     * entred values.
     * 
     * @param dataElementHistory DateElementHistory
     * @param organisationUnit OrganisationUnit
     * @param dataelement DataElement
     * @throws Exception
     */
    private void setMinMaxLimits( DataElementHistory dataElementHistory, OrganisationUnit organisationUnit,
        DataElement dataelement )
        throws Exception
    {
        MinMaxDataElement minMaxDataElement = minMaxDataElementStore.getMinMaxDataElement( organisationUnit,
            dataelement );

        if ( minMaxDataElement != null )
        {
            if ( !minMaxDataElement.isGenerated() )
            {
                return;
            }
        }

        double maxValue = dataElementHistory.getMaxValue();

        if ( maxValue != Double.NEGATIVE_INFINITY )
        {
            double maxLimit;
            double minLimit;
            double minValue = dataElementHistory.getMinValue();

            if ( maxValue > 0 )
            {
                maxLimit = Math.ceil( maxValue + (maxValue * 0.1) );
                minLimit = Math.floor( minValue - (minValue * 0.1) );
            }
            else if ( maxValue == 0 )
            {
                return;
            }
            else
            {
                maxLimit = Math.ceil( maxValue - (maxValue * 0.1) );
                minLimit = Math.floor( minValue + (minValue * 0.1) );
            }

            if ( minMaxDataElement == null )
            {
                minMaxDataElement = new MinMaxDataElement( organisationUnit, dataelement, (int) minLimit,
                    (int) maxLimit, true );
                
                minMaxDataElementStore.addMinMaxDataElement( minMaxDataElement );
            }
            else
            {
                minMaxDataElement.setMax( (int) maxLimit );
                minMaxDataElement.setMin( (int) minLimit );
                
                minMaxDataElementStore.updateMinMaxDataElement( minMaxDataElement );
            }

            minMaxDataElements.add( minMaxDataElement );
        }
    }
}
