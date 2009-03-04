package org.hisp.dhis.de.history;

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
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.CalendarPeriodType;
import org.hisp.dhis.period.Period;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultHistoryRetriever.java 5131 2008-05-11 21:06:23Z larshelg $
 */
public class DefaultHistoryRetriever
    implements HistoryRetriever
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MinMaxDataElementStore minMaxDataElementStore;

    public void setMinMaxDataElementStore( MinMaxDataElementStore minMaxDataElementStore )
    {
        this.minMaxDataElementStore = minMaxDataElementStore;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    // -------------------------------------------------------------------------
    // HistoryRetriever implementation
    // -------------------------------------------------------------------------

    public DataElementHistory getHistory( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, OrganisationUnit organisationUnit,
        Period lastPeriod, int historyLength )
        throws HistoryRetrieverException
    {
        if ( !dataElement.getType().equals( DataElement.TYPE_INT ) )
        {	
            return null;
        	
            /*throw new HistoryRetrieverException( "DataElement is not of type " + DataElement.TYPE_INT + ": "
                + dataElement.getShortName() ) ; */
        }

        // ---------------------------------------------------------------------
        // Initialise history
        // ---------------------------------------------------------------------

        DataElementHistory history = new DataElementHistory();
        history.setDataElement( dataElement );
        history.setOptionCombo( optionCombo );
        history.setOrganisationUnit( organisationUnit );
        history.setHistoryLength( historyLength );
        addMinMaxLimits( organisationUnit, dataElement, history );

        // ---------------------------------------------------------------------
        // Create history points
        // ---------------------------------------------------------------------

        List<Period> periods = getPeriods( lastPeriod, historyLength );

        double max = 1;
        double average = 0;
        double total = 0;
        int count = 0;

        if ( history.getMaxLimit() != null )
        {
            max = Math.max( max, history.getMaxLimit() );
        }

        for ( Period period : periods )
        {
            DataElementHistoryPoint historyPoint = new DataElementHistoryPoint();
            historyPoint.setPeriod( period );

            Double value = getValue( dataElement, optionCombo, organisationUnit, period );

            if ( value != null )
            {
                historyPoint.setValue( value );
            }

            if ( historyPoint.getValue() != null )
            {
                max = Math.max( max, historyPoint.getValue() );
                total += historyPoint.getValue();
                average = total / ++count;
            }

            historyPoint.setAverage( average );

            history.getHistoryPoints().add( historyPoint );
        }

        history.setMaxHistoryValue( max );

        // get the maxValue
        double maxValue = getMaxValue( history );

        // if there was any entred values, set minValue and maxValue
        if ( maxValue != Double.NEGATIVE_INFINITY )
        {
            history.setMaxValue( maxValue );

            double minValue = getMinValue( history );
            history.setMinValue( minValue );
        }

        return history;
    }
    
    public DataElementHistory getHistory( DataElement dataElement, OrganisationUnit organisationUnit,
            Period lastPeriod, int historyLength )
        throws HistoryRetrieverException
        {
            if ( !dataElement.getType().equals( DataElement.TYPE_INT ) )
            {
                throw new HistoryRetrieverException( "DataElement is not of type " + DataElement.TYPE_INT + ": "
                    + dataElement.getShortName() );
            }

            // ---------------------------------------------------------------------
            // Initialise history
            // ---------------------------------------------------------------------

            DataElementHistory history = new DataElementHistory();
            history.setDataElement( dataElement );            
            history.setOrganisationUnit( organisationUnit );
            history.setHistoryLength( historyLength );
            addMinMaxLimits( organisationUnit, dataElement, history );

            // ---------------------------------------------------------------------
            // Create history points
            // ---------------------------------------------------------------------

            List<Period> periods = getPeriods( lastPeriod, historyLength );

            double max = 1;
            double average = 0;
            double total = 0;
            int count = 0;

            if ( history.getMaxLimit() != null )
            {
                max = Math.max( max, history.getMaxLimit() );
            }

            for ( Period period : periods )
            {
                DataElementHistoryPoint historyPoint = new DataElementHistoryPoint();
                historyPoint.setPeriod( period );

                Double value = getValue( dataElement, organisationUnit, period );

                if ( value != null )
                {
                    historyPoint.setValue( value );
                }

                if ( historyPoint.getValue() != null )
                {
                    max = Math.max( max, historyPoint.getValue() );
                    total += historyPoint.getValue();
                    average = total / ++count;
                }

                historyPoint.setAverage( average );

                history.getHistoryPoints().add( historyPoint );
            }

            history.setMaxHistoryValue( max );

            // get the maxValue
            double maxValue = getMaxValue( history );

            // if there was any entred values, set minValue and maxValue
            if ( maxValue != Double.NEGATIVE_INFINITY )
            {
                history.setMaxValue( maxValue );

                double minValue = getMinValue( history );
                history.setMinValue( minValue );
           }

            return history;
        }


    private void addMinMaxLimits( OrganisationUnit organisationUnit, DataElement dataElement, DataElementHistory history )
    {
        MinMaxDataElement minMaxDataElement = minMaxDataElementStore.getMinMaxDataElement( organisationUnit,
            dataElement );

        if ( minMaxDataElement != null )
        {
            history.setMaxLimit( minMaxDataElement.getMax() );
            history.setMinLimit( minMaxDataElement.getMin() );
        }
    }

    /**
     * Finds the lowest value entered in the periode given by
     * history.historyLenght.
     * 
     * @param history DataElementHistory
     * @return the lowest Double value entred. If no values are entred,
     *         Double.MAX_VALUE is returned
     */
    private Double getMinValue( DataElementHistory history )
    {
        double value = Double.MAX_VALUE;
        List<DataElementHistoryPoint> historyPoints = history.getHistoryPoints();

        for ( DataElementHistoryPoint DEPoint : historyPoints )
        {
            if ( DEPoint.getValue() != null )
            {
                if ( DEPoint.getValue() < value )
                {
                    value = DEPoint.getValue();
                }
            }
        }

        return value;
    }

    /**
     * Finds the highest value entered in the periode given by
     * history.historyLenght.
     * 
     * @param history DataElementHistory
     * @return the highest entred value. If no value is entred
     *         Double.NEGATIVE_INFINITY is returned
     */
    private Double getMaxValue( DataElementHistory history )
    {
        double value = Double.NEGATIVE_INFINITY;
        List<DataElementHistoryPoint> historyPoints = history.getHistoryPoints();

        for ( DataElementHistoryPoint DEPoint : historyPoints )
        {
            if ( DEPoint.getValue() != null )
            {
                if ( DEPoint.getValue() > value )
                {
                    value = DEPoint.getValue();
                }
            }
        }

        return value;
    }

    private List<Period> getPeriods( Period lastPeriod, int historyLength )
    {
        List<Period> periods = new ArrayList<Period>( historyLength );

        CalendarPeriodType periodType = (CalendarPeriodType) lastPeriod.getPeriodType();

        Period period = lastPeriod;

        for ( int i = 0; i < historyLength; ++i )
        {
            periods.add( period );
            period = periodType.getPreviousPeriod( period );
        }

        Collections.reverse( periods );

        return periods;
    }

    private Double getValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, OrganisationUnit organisationUnit, Period period )
        throws HistoryRetrieverException
    {
        DataValue dataValue = dataValueService.getDataValue( organisationUnit, dataElement, period, optionCombo );

        if ( dataValue != null )
        {
            if ( dataValue.getValue() != null )
            {
                return parseValue( dataValue.getValue() );

            }
        }

        return null;
    }
    
    private Double getValue( DataElement dataElement, OrganisationUnit organisationUnit, Period period )
    throws HistoryRetrieverException
    {
    	DataValue dataValue = dataValueService.getDataValue( organisationUnit, dataElement, period );

    	if ( dataValue != null )
    	{
    		if ( dataValue.getValue() != null )
    		{
    			return parseValue( dataValue.getValue() );

    		}
    	}

    	return null;
    }


    private Double parseValue( String value )
        throws HistoryRetrieverException
    {
        try
        {
            return Double.parseDouble( value );
        }
        catch ( NumberFormatException e )
        {
            throw new HistoryRetrieverException( "Failed to parse double: " + value, e );
        }
    }
}
