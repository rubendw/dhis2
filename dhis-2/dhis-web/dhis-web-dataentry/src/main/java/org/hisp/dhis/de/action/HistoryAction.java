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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.de.history.DataElementHistory;
import org.hisp.dhis.de.history.HistoryRetriever;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: HistoryAction.java 4778 2008-03-20 13:21:56Z abyot $
 */
public class HistoryAction
    implements Action
{
    private static final int HISTORY_LENGTH = 13;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HistoryRetriever historyRetriever;

    public void setHistoryRetriever( HistoryRetriever historyRetriever )
    {
        this.historyRetriever = historyRetriever;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }
    
    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer dataElementId;

    public void setDataElementId( Integer dataElementId )
    {
        this.dataElementId = dataElementId;
    }
    
    private Integer optionComboId;
    
    public void setOptionComboId( Integer optionComboId )
    {
    	this.optionComboId = optionComboId;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private DataElementHistory dataElementHistory;

    public DataElementHistory getDataElementHistory()
    {
        return dataElementHistory;
    }
    
    private Boolean isHistoryValid = true;
    
    public Boolean getIsHistoryValid()
    {
    	return isHistoryValid;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        DataElement dataElement = dataElementService.getDataElement( dataElementId );
        
        DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId );
        
        if ( optionCombo == null )
        {
            String defaultName = DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME;
            
        	optionCombo = dataElementCategoryComboService.getDataElementCategoryComboByName( defaultName ).getOptionCombos().iterator().next();
        }

        if ( dataElement == null )
        {
            throw new Exception( "DataElement doesn't exist: " + dataElementId );
        }

        Period period = selectedStateManager.getSelectedPeriod();

        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        dataElementHistory = historyRetriever.getHistory( dataElement, optionCombo, organisationUnit, period, HISTORY_LENGTH );
        
        if( dataElementHistory == null )
        {
        	isHistoryValid = false;
        }

        return SUCCESS;
    }
}
