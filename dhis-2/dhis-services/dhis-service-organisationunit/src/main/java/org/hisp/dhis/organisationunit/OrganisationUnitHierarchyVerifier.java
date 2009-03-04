package org.hisp.dhis.organisationunit;

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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;

/**
 * This class verifies that there exists at least one OrganisationUnitHierarchy
 * object in the database. This object is automatically created with OrganisationUnits
 * but may be left out when doing manual imports.
 * 
 * @author Lars Helge Overland
 * @version $Id$
 */
public class OrganisationUnitHierarchyVerifier
    extends AbstractStartupRoutine 
{
    private static final Log log = LogFactory.getLog( OrganisationUnitHierarchyVerifier.class );
    
    public static Date START_OF_TIME;
    
    static
    {
        Calendar cal = Calendar.getInstance();
        
        cal.set( 1970, Calendar.JANUARY, 1 );
        
        START_OF_TIME = cal.getTime();
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public void execute()
        throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        
        Date today = calendar.getTime();
        
        Collection<OrganisationUnitHierarchy> hierarchies = 
            organisationUnitService.getOrganisationUnitHierarchies( START_OF_TIME, today );
        
        if ( hierarchies == null || hierarchies.size() == 0 )
        {
            organisationUnitService.addOrganisationUnitHierarchy( START_OF_TIME );
            
            log.info( "Added organistion unit hierarchy" );
        }
    }
}
