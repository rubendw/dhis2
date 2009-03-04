package org.hisp.dhis.validationrule.action;

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
import java.util.Comparator;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.comparator.DataElementGroupNameComparator;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id: GetExpressionAction.java 5333 2008-06-04 13:24:21Z larshelg $
 */
public class GetExpressionAction
    extends ActionSupport
{
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
    
    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
    }

    // -------------------------------------------------------------------------
    // DisplayPropertyHandler
    // -------------------------------------------------------------------------

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }
    
    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------
    
    private String side;

    public String getSide()
    {
        return side;
    }

    public void setSide( String side )
    {
        this.side = side;
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
    
    private String expression;

    public String getExpression()
    {
        return expression;
    }

    public void setExpression( String expression )
    {
        this.expression = expression;
    }
    
    private String textualExpression;

    public String getTextualExpression()
    {
        return textualExpression;
    }

    public void setTextualExpression( String textualExpression )
    {
        this.textualExpression = textualExpression;
    }

    private List<DataElement> dataElements;
    
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    private List<DataElementGroup> dataElementGroups;
    
    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------  
    
    public String execute() throws Exception
    {
        dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );
        
        Collections.sort( dataElements, dataElementComparator );

        dataElements = displayPropertyHandler.handleDataElements( dataElements );
        
        dataElementGroups = new ArrayList<DataElementGroup>( dataElementService.getAllDataElementGroups() );

        Collections.sort( dataElementGroups, new DataElementGroupNameComparator() );
        
        return SUCCESS;
    }
}
