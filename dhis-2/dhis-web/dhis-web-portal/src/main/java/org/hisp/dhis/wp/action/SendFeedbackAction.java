package org.hisp.dhis.wp.action;

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

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class SendFeedbackAction
    implements Action
{
    private static final String LINEBREAK = "\n";
    private static final String DOUBLE_LINEBREAK = "\n\n";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String message;
    
    public void setMessage( String message )
    {
        this.message = message;
    }
    
    private String stackTrace;
    
    public void setStackTrace( String stackTrace )
    {
        this.stackTrace = stackTrace;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        SimpleEmail mail = new SimpleEmail();
        
        try
        {
            mail.setAuthenticator( new DefaultAuthenticator( "dhis2userfeedback", "dhis2dhis2" ) );
            
            mail.setHostName( "smtp.gmail.com" );
            mail.addTo( "dhis2-userfeedback@googlegroups.com" );
            mail.setFrom( "dhis2-userfeedback@hisp.info" );
            mail.setSubject( "DHIS 2 User Feedback" );
            mail.setMsg( getMessage( message, stackTrace ) );
            
            mail.setSSL( true );
            mail.setSslSmtpPort( "465" );
            
            mail.send();
        }
        catch ( EmailException ex )
        {
            throw new RuntimeException( "Failed to send mail", ex );
        }
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String getMessage( String message, String stackTrace )
    {
        StringBuffer buffer = new StringBuffer();
        
        User user = currentUserService.getCurrentUser();
        
        String userName = currentUserService.getCurrentUsername();
        
        buffer.append( "Sender" + DOUBLE_LINEBREAK );
        
        buffer.append( "Name: " + format( user.getFirstName() ) + " " + format( user.getSurname() + LINEBREAK ) );        
        buffer.append( "Email: " + format( user.getEmail() ) + LINEBREAK );
        buffer.append( "User name: " + format( userName ) + LINEBREAK  );
        
        buffer.append( DOUBLE_LINEBREAK + "Message" + DOUBLE_LINEBREAK );

        buffer.append( format( message ) + LINEBREAK );
        
        buffer.append( DOUBLE_LINEBREAK + "Error message" + DOUBLE_LINEBREAK );
        
        buffer.append( format( stackTrace ) );
        
        return buffer.toString();
    }
    
    private String format( String string )
    {
        return string != null ? string : new String();
    }
}
