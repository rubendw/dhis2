package org.hisp.dhis.dataset.action;

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

import java.util.Collection;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.oust.manager.SelectionTreeManager;

import com.opensymphony.xwork.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: SelectLevelAction.java 4524 2008-02-04 18:48:53Z larshelg $
 */
public class SelectLevelAction
    implements Action
{
    private static final int FIRST_LEVEL = 1;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer level;

    public void setLevel( Integer level )
    {
        this.level = level;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Integer selectLevel;

    public Integer getSelectLevel()
    {
        return selectLevel;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        Collection<OrganisationUnit> rootUnits = selectionTreeManager.getRootOrganisationUnits();

        Collection<OrganisationUnit> selectedUnits = selectionTreeManager.getSelectedOrganisationUnits();

        for ( OrganisationUnit rootUnit : rootUnits )
        {
            selectLevel( rootUnit, FIRST_LEVEL, selectedUnits );
        }

        selectionTreeManager.setSelectedOrganisationUnits( selectedUnits );

        selectLevel = level;
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void selectLevel( OrganisationUnit orgUnit, int currentLevel, Collection<OrganisationUnit> selectedUnits )
    {
        if ( currentLevel == level )
        {
            selectedUnits.add( orgUnit );
        }
        else
        {
            for ( OrganisationUnit child : orgUnit.getChildren() )
            {
                selectLevel( child, currentLevel + 1, selectedUnits );
            }
        }
    }
}
