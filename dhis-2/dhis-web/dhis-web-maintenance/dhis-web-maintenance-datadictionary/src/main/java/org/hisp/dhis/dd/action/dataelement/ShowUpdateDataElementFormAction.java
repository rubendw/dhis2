package org.hisp.dhis.dd.action.dataelement;

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

import static org.hisp.dhis.expression.Expression.SEPARATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.Operand;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Hans S. Toemmerholt
 * @version $Id: GetDataElementAction.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class ShowUpdateDataElementFormAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private DataElementCategoryComboService dataElementCategoryComboService;    

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
    	this.dataElementCategoryComboService = dataElementCategoryComboService;
    }
    
    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;    

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
    	this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }   

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private DataElement dataElement;

    public DataElement getDataElement()
    {
        return dataElement;
    }

    private CalculatedDataElement calculatedDataElement;

    public CalculatedDataElement getCalculatedDataElement()
    {
        return calculatedDataElement;
    }

    private Collection<DataElementGroup> dataElementGroups;

    public Collection<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }  
    
    private Map<String, Integer> factorMap;

    public Map<String, Integer> getFactorMap()
    {
        return factorMap;
    }
    
    private Collection<Operand> operands = new ArrayList<Operand>();

    public Collection<Operand> getOperands()
    {
        return operands;
    }

    private final int ALL = 0;
    
    public int getALL()
    {
        return ALL;
    }
    
    private List<DataElementCategoryCombo> dataElementCategoryCombos;
    
    public List<DataElementCategoryCombo> getDataElementCategoryCombos()
    {
        return dataElementCategoryCombos;
    }
        
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {    	
    	dataElementCategoryCombos = new ArrayList<DataElementCategoryCombo>( dataElementCategoryComboService.getAllDataElementCategoryCombos() );
    	
        dataElement = dataElementService.getDataElement( id );       
        
        if ( dataElement != null && dataElement instanceof CalculatedDataElement )
        {
            calculatedDataElement = (CalculatedDataElement) dataElement;
            dataElementGroups = dataElementService.getAllDataElementGroups();
            
            Collection<String> operandIds = new ArrayList<String>();
            
            operandIds = dataElementService.getOperandIds( calculatedDataElement );
            factorMap = dataElementService.getOperandFactors( calculatedDataElement );    
            
            for( String operandId : operandIds )
            {
            	String dataElementIdString = operandId.substring( 0, operandId.indexOf( SEPARATOR ) );                
                String optionComboIdString = operandId.substring( operandId.indexOf( SEPARATOR ) + 1, operandId.length() );
    			
    		DataElement dataElement = dataElementService.getDataElement( Integer.parseInt( dataElementIdString ) );
    		DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService.getDataElementCategoryOptionCombo( Integer.parseInt( optionComboIdString ) );    			    			
    			
        	Operand operand = new Operand( dataElement.getId(), optionCombo.getId(), dataElement.getName() + dataElementCategoryOptionComboService.getOptionNames( optionCombo ) );
        		
        	operands.add( operand );
            }
            
        }               

        return SUCCESS;        
    }
}
