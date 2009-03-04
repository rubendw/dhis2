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

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.security.PasswordManager;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AddUserAction.java 5556 2008-08-20 11:36:20Z abyot $
 */
public class AddUserAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    private UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

    private PasswordManager passwordManager;

    public void setPasswordManager( PasswordManager passwordManager )
    {
        this.passwordManager = passwordManager;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String username;

    public void setUsername( String username )
    {
        this.username = username;
    }

    private String rawPassword;

    public void setRawPassword( String rawPassword )
    {
        this.rawPassword = rawPassword;
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

    private String email;

    public void setEmail( String email )
    {
        this.email = email;
    }

    private String passwordUnMatched;

    public String getPasswordUnMatched()
    {
        return this.passwordUnMatched;
    }

    public void setPasswordUnMatched( String passwordUnMatched )
    {
        this.passwordUnMatched = passwordUnMatched;
    }

    private Collection<String> selectedList = new ArrayList<String>();

    public void setSelectedList( Collection<String> selectedList )
    {
        this.selectedList = selectedList;
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

        if ( email != null && email.trim().length() == 0 )
        {
            email = null;
        }

        username = username.trim();

        // ---------------------------------------------------------------------
        // Create userCredentials and user
        // ---------------------------------------------------------------------

        Collection<OrganisationUnit> orgUnits = selectionTreeManager.getSelectedOrganisationUnits();

        User user = new User();
        user.setSurname( surname );
        user.setFirstName( firstName );
        user.setEmail( email );
        user.setOrganisationUnits( orgUnits );

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser( user );
        userCredentials.setUsername( username );
        userCredentials.setPassword( passwordManager.encodePassword( username, rawPassword ) );

        for ( String id : selectedList )
        {
            UserAuthorityGroup group = userStore.getUserAuthorityGroup( Integer.parseInt( id ) );
            userCredentials.getUserAuthorityGroups().add( group );
        }
        
        userStore.addUser( user );
        userStore.addUserCredentials( userCredentials );

        if ( orgUnits.size() > 0 )
        {
            selectionManager.setSelectedOrganisationUnits( orgUnits );
        }

        return SUCCESS;
    }
}
