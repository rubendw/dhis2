package org.hisp.dhis.populator;

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

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.hisp.dhis.system.startup.AbstractTransactionalStartupRoutine;
import org.hisp.dhis.system.util.SystemUtils;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: AbstractPopulatorEngine.java 3331 2007-06-01 07:29:02Z torgeilo $
 */
public abstract class AbstractPopulatorEngine
    extends AbstractTransactionalStartupRoutine
{
    private static final String USERNAME = "populator";

    // -------------------------------------------------------------------------
    // Execute redirect
    // -------------------------------------------------------------------------

    protected abstract void executeEngine()
        throws PopulatorEngineException;

    // -------------------------------------------------------------------------
    // StartupRoutine implementation
    // -------------------------------------------------------------------------

    public final void executeRoutine()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Check permission to populate
        // ---------------------------------------------------------------------

        if ( !SystemUtils.isRunningForUse() )
        {
            return;
        }

        // ---------------------------------------------------------------------
        // Get security permissions
        // ---------------------------------------------------------------------

        org.acegisecurity.userdetails.User user = new org.acegisecurity.userdetails.User( USERNAME, "", true, true,
            true, true, new GrantedAuthority[0] );

        Authentication authentication = new TestingAuthenticationToken( user, user.getPassword(), user.getAuthorities() );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        // ---------------------------------------------------------------------\
        // Execute engine
        // ---------------------------------------------------------------------

        executeEngine();

        // ---------------------------------------------------------------------
        // Clear security permissions
        // ---------------------------------------------------------------------

        SecurityContextHolder.clearContext();
    }
}
