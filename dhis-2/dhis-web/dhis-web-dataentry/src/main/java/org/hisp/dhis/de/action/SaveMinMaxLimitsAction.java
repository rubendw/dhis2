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
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: SaveMinMaxLimitsAction.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class SaveMinMaxLimitsAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MinMaxDataElementStore minMaxDataElementStore;

    public void setMinMaxDataElementStore( MinMaxDataElementStore minMaxDataElementStore )
    {
        this.minMaxDataElementStore = minMaxDataElementStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer organisationUnitId;

    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    private Integer dataElementId;

    public void setDataElementId( Integer dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    private Integer minLimit;

    public void setMinLimit( Integer minLimit )
    {
        this.minLimit = minLimit;
    }

    private Integer maxLimit;

    public void setMaxLimit( Integer maxLimit )
    {
        this.maxLimit = maxLimit;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( organisationUnitId );

        DataElement dataElement = dataElementService.getDataElement( dataElementId );

        MinMaxDataElement minMaxDataElement = minMaxDataElementStore.getMinMaxDataElement( organisationUnit,
            dataElement );

        if ( minMaxDataElement == null )
        {
            minMaxDataElement = new MinMaxDataElement( organisationUnit, dataElement, minLimit, maxLimit, false );

            minMaxDataElementStore.addMinMaxDataElement( minMaxDataElement );
        }
        else
        {
            minMaxDataElement.setMin( minLimit );
            minMaxDataElement.setMax( maxLimit );
            minMaxDataElement.setGenerated( false );

            minMaxDataElementStore.updateMinMaxDataElement( minMaxDataElement );
        }

        return SUCCESS;
    }
}
