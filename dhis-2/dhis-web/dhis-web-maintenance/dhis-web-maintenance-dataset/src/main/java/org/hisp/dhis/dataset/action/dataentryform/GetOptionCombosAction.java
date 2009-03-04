package org.hisp.dhis.dataset.action.dataentryform;

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
import java.util.Iterator;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;

import com.opensymphony.xwork.Action;

public class GetOptionCombosAction
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
    
    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    private int dataElementId;

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    private List<String> optionComboIds;

    public List<String> getOptionComboIds()
    {
        return optionComboIds;
    }

    private List<String> optionComboNames;

    public List<String> getOptionComboNames()
    {
        return optionComboNames;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------
    public String execute()
        throws Exception
    {
        optionComboIds = new ArrayList<String>();
        optionComboNames = new ArrayList<String>();

        DataElement dataElement = dataElementService.getDataElement( dataElementId );

        DataElementCategoryCombo dataElementCategoryCombo = dataElement.getCategoryCombo();

        List<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
            dataElementCategoryCombo.getOptionCombos() );

        Iterator<DataElementCategoryOptionCombo> optionComboIterator = optionCombos.iterator();
        
        while ( optionComboIterator.hasNext() )
        {
            DataElementCategoryOptionCombo optionCombo = optionComboIterator.next();           
            
            String optionComboName = dataElementCategoryOptionComboService.getOptionNames( optionCombo );
         
            String optionComboId = String.valueOf( optionCombo.getId() );
            
            if ( ( optionComboId != null || optionComboId.trim().length() != 0 ) )
            {
                if ( optionComboName == null || optionComboName.trim().length() == 0 )
                {                       
                    optionComboName = DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME;
                }
            }
            
            optionComboNames.add( optionComboName );
            
            optionComboIds.add( String.valueOf( optionCombo.getId() ) );                                    
        }

        return SUCCESS;
    }
}
