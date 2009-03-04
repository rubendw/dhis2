package org.hisp.dhis.user.action;

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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ValidateUserAction.java 3816 2007-11-02 23:00:19Z larshelg $
 */
public class ValidateUserAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String username;

    public void setUsername( String username )
    {
        this.username = username;
    }

    private String surname;

    public void setSurname( String surname )
    {
        this.surname = surname;
    }

    private String firstName;

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    private String rawPassword;

    public void setRawPassword( String rawPassword )
    {
        this.rawPassword = rawPassword;
    }

    private String retypePassword;

    public void setRetypePassword( String retypePassword )
    {
        this.retypePassword = retypePassword;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( id == null )
        {
            if ( username == null )
            {
                message = i18n.getString( "specify_username" );

                return INPUT;
            }
            else
            {
                username = username.trim();

                if ( username.length() == 0 )
                {
                    message = i18n.getString( "specify_username" );

                    return INPUT;
                }

                UserCredentials match = userStore.getUserCredentialsByUsername( username );

                if ( match != null )
                {
                    message = i18n.getString( "username_in_use" );

                    return INPUT;
                }
            }
        }

        if ( id == null && ( rawPassword == null || rawPassword.trim().length() == 0 ) )
        {
            message = i18n.getString( "specify_raw_password" );

            return INPUT;

        }

        if ( id == null && ( retypePassword == null || retypePassword.trim().length() == 0 ) )
        {
            message = i18n.getString( "specify_retype_password" );

            return INPUT;

        }

        if ( rawPassword != null && rawPassword.trim().length() != 0 && !rawPassword.equals( retypePassword ) )
        {
            message = i18n.getString( "password_un_matched" );

            return INPUT;
        }

        if ( surname == null || surname.trim().length() == 0 )
        {
            message = i18n.getString( "specify_surname" );

            return INPUT;
        }

        if ( firstName == null || firstName.trim().length() == 0 )
        {
            message = i18n.getString( "specify_first_name" );

            return INPUT;
        }

        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }
}
