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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: SaveCommentAction.java 3113 2007-03-19 20:47:10Z torgeilo $
 */
public class SaveCommentAction
    implements Action
{
    private static final Log LOG = LogFactory.getLog( SaveCommentAction.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private String comment;

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    private int dataElementId;

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    private int statusCode;

    public int getStatusCode()
    {
        return statusCode;
    }

    private Date timestamp;

    public Date getTimestamp()
    {
        return timestamp;
    }

    private String storedBy;

    public String getStoredBy()
    {
        return storedBy;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        Period period = selectedStateManager.getSelectedPeriod();

        DataElement dataElement = dataElementService.getDataElement( dataElementId );

        storedBy = currentUserService.getCurrentUsername();

        if ( storedBy == null )
        {
            storedBy = "[unknown]";
        }

        if ( comment != null && comment.trim().equals( "" ) )
        {
            comment = null;
        }

        // ---------------------------------------------------------------------
        // Update DB
        // ---------------------------------------------------------------------

        DataValue dataValue = dataValueService.getDataValue( organisationUnit, dataElement, period );

        if ( dataValue == null )
        {
            if ( comment != null )
            {
                LOG.debug( "Adding DataValue, comment added" );

                dataValue = new DataValue( dataElement, period, organisationUnit, null, storedBy, new Date(), comment );

                dataValueService.addDataValue( dataValue );
            }
        }
        else
        {
            LOG.debug( "Updating DataValue, comment added/changed" );

            dataValue.setComment( comment );
            dataValue.setTimestamp( new Date() );
            dataValue.setStoredBy( storedBy );

            dataValueService.updateDataValue( dataValue );
        }

        if ( dataValue != null )
        {
            this.timestamp = dataValue.getTimestamp();
            this.storedBy = dataValue.getStoredBy();
        }

        return SUCCESS;
    }
}
