package org.hisp.dhis.oum.action.organisationunit;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: GetOrganisationUnitListAction.java 1898 2006-09-22 12:06:56Z torgeilo $
 */
public class GetOrganisationUnitListAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    private Comparator<OrganisationUnit> organisationUnitComparator;

    public void setOrganisationUnitComparator( Comparator<OrganisationUnit> organisationUnitComparator )
    {
        this.organisationUnitComparator = organisationUnitComparator;
    }
    
    // -------------------------------------------------------------------------
    // DisplayPropertyHandler
    // -------------------------------------------------------------------------

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }    
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        Collection<OrganisationUnit> selectedUnits = selectionManager.getSelectedOrganisationUnits();

        if ( selectedUnits.isEmpty() )
        {
            organisationUnits.addAll( selectionManager.getRootOrganisationUnits() );
        }
        else
        {
            for ( OrganisationUnit selectedUnit : selectedUnits )
            {
                organisationUnits.addAll( selectedUnit.getChildren() );
            }
        }
        
        Collections.sort( organisationUnits, organisationUnitComparator );
        
        organisationUnits = displayPropertyHandler.handleOrganisationUnits( organisationUnits );
        
        return SUCCESS;
    }
}
