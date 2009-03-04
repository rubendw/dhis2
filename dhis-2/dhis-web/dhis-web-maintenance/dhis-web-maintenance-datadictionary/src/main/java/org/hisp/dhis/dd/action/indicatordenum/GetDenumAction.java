package org.hisp.dhis.dd.action.indicatordenum;

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

import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementGroupNameComparator;
import org.hisp.dhis.expression.ExpressionService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Oeverland
 * @version $id$
 */
public class GetDenumAction
    extends ActionSupport
{
    private String formula;

    public String getFormula()
    {
        return formula;
    }

    public void setFormula( String formula )
    {
        this.formula = formula;
    }

    private String description;

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String aggregationOperator;

    public String getAggregationOperator()
    {
        return aggregationOperator;
    }
    
    public void setAggregationOperator( String aggregationOperator )
    {
        this.aggregationOperator = aggregationOperator;
    }

    private String type;

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }    

    private String textualFormula;

    public String getTextualFormula()
    {
        return textualFormula;
    }
    
    private List<Operand> operands = new ArrayList<Operand>();

    public List<Operand> getOperands()
    {
        return operands;
    }

    private List<DataElementGroup> dataElementGroups;

    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }
    
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int ALL = 0;

    public int getALL()
    {
        return ALL;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    public String execute()
    {
        textualFormula = expressionService.getExpressionDescription( formula );
        
        dataElementGroups = new ArrayList<DataElementGroup>( 
            dataElementService.getAllDataElementGroups() );
        
        Collections.sort( dataElementGroups, new DataElementGroupNameComparator() );

        if ( aggregationOperator == null || aggregationOperator.trim().length() == 0 )
        {
            aggregationOperator = DataElement.AGGREGATION_OPERATOR_SUM;
        }
        
        return SUCCESS;
    }
}
