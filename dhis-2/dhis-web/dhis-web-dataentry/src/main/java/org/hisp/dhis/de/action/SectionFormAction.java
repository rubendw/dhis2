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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;
import org.hisp.dhis.dataset.comparator.SectionOrderComparator;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.de.comments.StandardCommentsManager;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork.Action;

/**
 * @author Tri
 * @version $Id: FormAction.java 4089 2007-11-24 13:30:44Z larshelg $
 */
public class SectionFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SectionService sectionService;

    private DataValueService dataValueService;

    public void setSectionService( SectionService sectionService )
    {
        this.sectionService = sectionService;
    }

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private StandardCommentsManager standardCommentsManager;

    public void setStandardCommentsManager( StandardCommentsManager standardCommentsManager )
    {
        this.standardCommentsManager = standardCommentsManager;
    }

    private MinMaxDataElementStore minMaxDataElementStore;

    public void setMinMaxDataElementStore( MinMaxDataElementStore minMaxDataElementStore )
    {
        this.minMaxDataElementStore = minMaxDataElementStore;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<Section> sections;

    public List<Section> getSections()
    {
        return sections;
    }

    private Map<Integer, DataValue> dataValueMap;

    public Map<Integer, DataValue> getDataValueMap()
    {
        return dataValueMap;
    }

    private Map<CalculatedDataElement, Integer> calculatedValueMap;

    public Map<CalculatedDataElement, Integer> getCalculatedValueMap()
    {
        return calculatedValueMap;
    }

    private List<String> standardComments;

    public List<String> getStandardComments()
    {
        return standardComments;
    }

    private Map<String, String> dataElementTypeMap;

    public Map<String, String> getDataElementTypeMap()
    {
        return dataElementTypeMap;
    }

    private Map<Integer, MinMaxDataElement> minMaxMap;

    public Map<Integer, MinMaxDataElement> getMinMaxMap()
    {
        return minMaxMap;
    }

    private Integer integer = new Integer( 0 );

    public Integer getInteger()
    {
        return integer;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer selectedDataSetId;

    public void setSelectedDataSetId( Integer selectedDataSetId )
    {
        this.selectedDataSetId = selectedDataSetId;
    }

    public Integer getSelectedDataSetId()
    {
        return selectedDataSetId;
    }

    private Integer selectedPeriodIndex;

    public void setSelectedPeriodIndex( Integer selectedPeriodIndex )
    {
        this.selectedPeriodIndex = selectedPeriodIndex;
    }

    public Integer getSelectedPeriodIndex()
    {
        return selectedPeriodIndex;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        DataSet dataSet = selectedStateManager.getSelectedDataSet();

        Period period = selectedStateManager.getSelectedPeriod();

        Collection<DataElement> dataElements = dataSet.getDataElements();

        if ( dataElements.size() == 0 )
        {
            return SUCCESS;
        }

        // ---------------------------------------------------------------------
        // Get the min/max values
        // ---------------------------------------------------------------------

        Collection<MinMaxDataElement> minMaxDataElements = minMaxDataElementStore.getMinMaxDataElements(
            organisationUnit, dataElements );

        minMaxMap = new HashMap<Integer, MinMaxDataElement>( minMaxDataElements.size() );

        for ( MinMaxDataElement minMaxDataElement : minMaxDataElements )
        {
            minMaxMap.put( minMaxDataElement.getDataElement().getId(), minMaxDataElement );
        }

        // ---------------------------------------------------------------------
        // Order the DataElements
        // ---------------------------------------------------------------------

        sections = (List<Section>) sectionService.getSectionByDataSet( dataSet );

        Collections.sort( sections, new SectionOrderComparator() );

        // ---------------------------------------------------------------------
        // Get the DataValues and create a map
        // ---------------------------------------------------------------------

        Collection<DataValue> dataValues = dataValueService.getDataValues( organisationUnit, period, dataElements );

        dataValueMap = new HashMap<Integer, DataValue>( dataValues.size() );

        for ( DataValue dataValue : dataValues )
        {
            dataValueMap.put( dataValue.getDataElement().getId(), dataValue );
        }

        // ---------------------------------------------------------------------
        // Prepare values for unsaved CalculatedDataElements
        // ---------------------------------------------------------------------

        CalculatedDataElement cde;

        calculatedValueMap = new HashMap<CalculatedDataElement, Integer>();

        Map<DataElement, Integer> factorMap;

        DataValue dataValue;
        int factor;
        int value = 0;

        for ( DataElement dataElement : dataElements )
        {
            if ( !(dataElement instanceof CalculatedDataElement) )
            {
                continue;
            }

            cde = (CalculatedDataElement) dataElement;

            if ( cde.isSaved() )
            {
                continue;
            }

            factorMap = dataElementService.getDataElementFactors( cde );

            for ( DataElement cdeElement : cde.getExpression().getDataElementsInExpression() )
            {
                factor = factorMap.get( cdeElement );
                dataValue = dataValueMap.get( cdeElement.getId() );

                if ( dataValue != null )
                {
                    value += Integer.parseInt( dataValue.getValue() ) * factor;
                }

            }

            calculatedValueMap.put( cde, value );

            value = 0;
        }

        // ---------------------------------------------------------------------
        // Make the standard comments available
        // ---------------------------------------------------------------------

        standardComments = standardCommentsManager.getStandardComments();

        // ---------------------------------------------------------------------
        // Make the DataElement types available
        // ---------------------------------------------------------------------

        dataElementTypeMap = new HashMap<String, String>();
        dataElementTypeMap.put( DataElement.TYPE_BOOL, i18n.getString( "yes_no" ) );
        dataElementTypeMap.put( DataElement.TYPE_INT, i18n.getString( "number" ) );
        dataElementTypeMap.put( DataElement.TYPE_STRING, i18n.getString( "text" ) );

        return SUCCESS;
    }
}
