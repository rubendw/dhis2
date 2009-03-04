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
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidateRoleAction
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

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }
    
    private String description;

    public void setDescription( String description )
    {
        this.description = description;
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
            if ( name == null )
            {
                message = i18n.getString( "specify_name" );

                return INPUT;
            }
            else
            {
                name = name.trim();

                if ( name.length() == 0 )
                {
                    message = i18n.getString( "specify_name" );

                    return INPUT;
                }

                UserAuthorityGroup match = userStore.getUserAuthorityGroupByName( name );

                if ( match != null )
                {
                    message = i18n.getString( "name_in_use" );

                    return INPUT;
                }
            }
        }

        if ( id == null && ( description == null || description.trim().length() == 0 ) )
        {
            message = i18n.getString( "specify_description" );

            return INPUT;
        }
        
        message = i18n.getString( "everything_is_ok" );
        
        return SUCCESS;
    }
}
