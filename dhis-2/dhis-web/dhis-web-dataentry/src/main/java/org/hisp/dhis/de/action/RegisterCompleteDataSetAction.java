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
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.i18n.I18nFormat;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class RegisterCompleteDataSetAction
    implements Action
{
    private static final Log log = LogFactory.getLog( RegisterCompleteDataSetAction.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CompleteDataSetRegistrationService registrationService;

    public void setRegistrationService( CompleteDataSetRegistrationService registrationService )
    {
        this.registrationService = registrationService;
    }
    
    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String date;

    public void setDate( String date )
    {
        this.date = date;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        CompleteDataSetRegistration registration = new CompleteDataSetRegistration();
        
        registration.setDataSet( selectedStateManager.getSelectedDataSet() );
        registration.setPeriod( selectedStateManager.getSelectedPeriod() );
        registration.setSource( selectedStateManager.getSelectedOrganisationUnit() );
        registration.setDate( ( date != null && date.trim().length() > 0 ) ? format.parseDate( date ) : new Date() );
                
        registrationService.saveCompleteDataSetRegistration( registration );
        
        log.info( "DataSet registered as complete: " + registration );
        
        return SUCCESS;
    }
}
