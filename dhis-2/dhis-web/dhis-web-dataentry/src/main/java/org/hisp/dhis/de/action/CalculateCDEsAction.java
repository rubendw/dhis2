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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.de.state.StatefulDataValueSaver;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork.Action;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class CalculateCDEsAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private StatefulDataValueSaver statefulDataValueSaver;

    public void setStatefulDataValueSaver( StatefulDataValueSaver statefulDataValueSaver )
    {
        this.statefulDataValueSaver = statefulDataValueSaver;
    }
    
    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }
    
    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Map<Integer, String> cdeValueMap;

    public Map<Integer, String> getCdeValueMap()
    {
        return cdeValueMap;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();
        
        Period period = selectedStateManager.getSelectedPeriod();
        
        Collection<DataElement> dataElements = selectedStateManager.getSelectedDataSet().getDataElements();
        
        if ( dataElements.size() > 0 )
        {           	
            Collection<CalculatedDataElement> cdes = dataElementService
            	.getCalculatedDataElementsByDataElements( dataElements );
        	
            //Look for the existence of CDEs in the form itself
            Iterator<DataElement> iterator = dataElements.iterator();            

            while ( iterator.hasNext() )
            {
                DataElement dataElement = iterator.next();

                if ( dataElement instanceof CalculatedDataElement )
                {                	
                	cdes.add( (CalculatedDataElement) dataElement );  
                }
            }            
        	
            cdeValueMap = new HashMap<Integer, String>();

            String value = null;

            for ( CalculatedDataElement cde : cdes )
            {        		
    		value = expressionService.getExpressionValue( cde.getExpression(), period, organisationUnit ).toString();        			
    		
    		if ( value == null )
    		{
    		    continue;
    		}

    		// Should the value be updated in Data Entry?
    		if ( dataElements.contains( cde ) )
    		{        		
    		    cdeValueMap.put( cde.getId(), value );        			
    		}

    		// Should the value be saved to the database?
    		if ( cde.isSaved() )
    		{        			
    		    DataElementCategoryCombo catCombo = dataElementCategoryComboService.
    		        getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );
    		    DataElementCategoryOptionCombo optionCombo = catCombo.getOptionCombos().iterator().next();
    		    statefulDataValueSaver.saveValue( cde.getId(), optionCombo.getId(), "" + value );
    		}

    		value = null;
            }
        }

        return SUCCESS;
    }
}
