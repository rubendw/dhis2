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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.expression.Expression;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @author Hans S. Toemmerholt
 * @version $Id: UpdateDataElementAction.java 5798 2008-10-02 18:01:34Z larshelg $
 */
public class UpdateDataElementAction
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

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String alternativeName;

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String code;

    public void setCode( String code )
    {
        this.code = code;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private Boolean active;

    public void setActive( Boolean active )
    {
        this.active = active;
    }

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private String aggregationOperator;

    public void setAggregationOperator( String aggregationOperator )
    {
        this.aggregationOperator = aggregationOperator;
    }
    
    private String saved;
    
    public void setSaved( String saved )
    {
        this.saved = saved;
    }
    
    private List<String> dataElementIds;

    public void setDataElementIds( List<String> dataElementIds )
    {
        this.dataElementIds = dataElementIds;
    }

    private List<String> factors;

    public void setFactors( List<String> factors )
    {
        this.factors = factors;
    }
    
    private Integer selectedCategoryComboId;
    
    public void setSelectedCategoryComboId( Integer selectedCategoryComboId )
    {
        this.selectedCategoryComboId = selectedCategoryComboId;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {    	
    	// ---------------------------------------------------------------------
        // Prepare values
        // ---------------------------------------------------------------------

        if ( alternativeName != null && alternativeName.trim().length() == 0 )
        {
            alternativeName = null;
        }

        if ( code != null && code.trim().length() == 0 )
        {
            code = null;
        }

        if ( description != null && description.trim().length() == 0 )
        {
            description = null;
        }       
        
        // ---------------------------------------------------------------------
        // Update data element
        // ---------------------------------------------------------------------

        DataElement dataElement = dataElementService.getDataElement( id );
        DataElementCategoryCombo categoryCombo = dataElementCategoryComboService.getDataElementCategoryCombo( selectedCategoryComboId );

        dataElement.setName( name );
        dataElement.setAlternativeName( alternativeName );
        dataElement.setShortName( shortName );
        dataElement.setCode( code );
        dataElement.setDescription( description );
        dataElement.setActive( active );
        dataElement.setType( type );
        dataElement.setAggregationOperator( aggregationOperator );
        dataElement.setCategoryCombo( categoryCombo );        

        // ---------------------------------------------------------------------
        // Calculated data element
        // ---------------------------------------------------------------------

        if ( dataElement instanceof CalculatedDataElement )
        {        	
            CalculatedDataElement calculatedDataElement = (CalculatedDataElement) dataElement;
            
            Expression expression = calculatedDataElement.getExpression();
            
            Set<DataElement> expressionDataElements = new HashSet<DataElement>();
            
            String expressionString = new String();

            for ( int i = 0; i < dataElementIds.size(); i++ )
            {
            	String operandId = dataElementIds.get( i ) ;
                
                String dataElementIdStr = operandId.substring( 0, operandId.indexOf('.') );                    
                
            	DataElement expressionDataElement = dataElementService.getDataElement( Integer.parseInt(dataElementIdStr) );                      
                
                if ( expressionDataElement == null )
                {
                    continue;
                }

                int factor = Integer.parseInt( factors.get( i ) );
                
                expressionString += " + [" + operandId + "] * " + factor;

                expressionDataElements.add( expressionDataElement );
            }

            if ( expressionString.length() > 3 )
            {
                expressionString = expressionString.substring( 3 );
            }
            
            expression.setExpression( expressionString );
            
            expression.setDataElementsInExpression( expressionDataElements );            
            
            calculatedDataElement.setSaved( saved != null );        
        }       
        
        dataElementService.updateDataElement( dataElement );

        return SUCCESS;
    }
}
