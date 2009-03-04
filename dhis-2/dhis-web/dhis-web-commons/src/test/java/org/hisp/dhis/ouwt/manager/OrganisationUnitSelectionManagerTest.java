package org.hisp.dhis.ouwt.manager;

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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: OrganisationUnitSelectionManagerTest.java 3200 2007-03-29 11:51:17Z torgeilo $
 */
public class OrganisationUnitSelectionManagerTest
    extends DhisSpringTest
{
    private OrganisationUnitService organisationUnitService;

    private TransactionManager transactionManager;

    private OrganisationUnitSelectionManager selectionManager;

    public void setUpTest()
        throws Exception
    {
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        // ---------------------------------------------------------------------
        // Have to replace the session part with a dummy
        // ---------------------------------------------------------------------

        SessionReplacerOUSManager selectionManager = new SessionReplacerOUSManager();
        selectionManager.setOrganisationUnitService( organisationUnitService );
        this.selectionManager = selectionManager;

        // ---------------------------------------------------------------------
        // Add a couple of org units
        // ---------------------------------------------------------------------

        transactionManager.enter();
        OrganisationUnit parentUnit = new OrganisationUnit( "OrganisationUnit1", "OrgUnit1", "OU1", new Date(),
            new Date(), true, "Comment1" );
        organisationUnitService.addOrganisationUnit( parentUnit );
        OrganisationUnit childUnit = new OrganisationUnit( "OrganisationUnit2", parentUnit, "OrgUnit2", "OU2",
            new Date(), new Date(), true, "Comment2" );
        parentUnit.getChildren().add( childUnit );
        organisationUnitService.addOrganisationUnit( childUnit );
        transactionManager.leave();
    }

    public void testRoot()
        throws Exception
    {
        transactionManager.enter();
        OrganisationUnit rootUnit = getSingleRootOrganisationUnit();
        OrganisationUnit child = (OrganisationUnit) rootUnit.getChildren().iterator().next();
        transactionManager.leave();

        assertNotNull( rootUnit );

        transactionManager.enter();
        setSingleRootOrganisationUnitParent( child );
        assertEquals( child.getId(), getSingleRootOrganisationUnit().getId() );
        transactionManager.leave();

        transactionManager.enter();
        selectionManager.resetRootOrganisationUnits();
        assertEquals( rootUnit.getId(), getSingleRootOrganisationUnit().getId() );
        transactionManager.leave();
    }

    public void testSelection()
        throws Exception
    {
        transactionManager.enter();
        assertTrue( selectionManager.getSelectedOrganisationUnits().isEmpty() );
        transactionManager.leave();

        transactionManager.enter();
        OrganisationUnit rootUnit = getSingleRootOrganisationUnit();
        OrganisationUnit child = (OrganisationUnit) rootUnit.getChildren().iterator().next();
        transactionManager.leave();

        transactionManager.enter();
        setSingleSelectedOrganisationUnit( child );
        assertEquals( child.getId(), getSingleSelectedOrganisationUnit().getId() );
        transactionManager.leave();

        transactionManager.enter();
        selectionManager.clearSelectedOrganisationUnits();
        assertTrue( selectionManager.getSelectedOrganisationUnits().isEmpty() );
        transactionManager.leave();
    }

    public void testParentChildPaths()
        throws Exception
    {
        transactionManager.enter();
        OrganisationUnit rootUnit = getSingleRootOrganisationUnit();
        OrganisationUnit child = (OrganisationUnit) rootUnit.getChildren().iterator().next();
        transactionManager.leave();

        transactionManager.enter();
        setSingleRootOrganisationUnitParent( rootUnit );
        setSingleSelectedOrganisationUnit( child );
        assertEquals( rootUnit.getId(), getSingleRootOrganisationUnit().getId() );
        assertEquals( child.getId(), getSingleSelectedOrganisationUnit().getId() );
        transactionManager.leave();

        transactionManager.enter();
        setSingleRootOrganisationUnitParent( child );
        assertEquals( child.getId(), getSingleRootOrganisationUnit().getId() );
        assertNull( getSingleSelectedOrganisationUnit() );
        transactionManager.leave();

        transactionManager.enter();
        setSingleSelectedOrganisationUnit( rootUnit );
        //assertTrue( selectionManager.getSelectedOrganisationUnits().size() == 0 );
        transactionManager.leave();

        transactionManager.enter();
        setSingleRootOrganisationUnitParent( rootUnit );
        setSingleSelectedOrganisationUnit( rootUnit );
        assertEquals( rootUnit.getId(), getSingleRootOrganisationUnit().getId() );
        assertEquals( rootUnit.getId(), getSingleSelectedOrganisationUnit().getId() );
        transactionManager.leave();

        transactionManager.enter();
        setSingleRootOrganisationUnitParent( child );
        assertEquals( child.getId(), getSingleRootOrganisationUnit().getId() );
        assertNull( getSingleSelectedOrganisationUnit() );
        transactionManager.leave();
    }

    private void setSingleRootOrganisationUnitParent( OrganisationUnit unit )
        throws Exception
    {
        OrganisationUnit parent = unit.getParent();

        if ( parent == null )
        {
            selectionManager.resetRootOrganisationUnits();
        }
        else
        {
            selectionManager.setRootOrganisationUnitsParent( parent );
        }
    }

    private void setSingleSelectedOrganisationUnit( OrganisationUnit unit )
        throws Exception
    {
        Set<OrganisationUnit> selectedUnits = new HashSet<OrganisationUnit>( 1 );
        selectedUnits.add( unit );
        selectionManager.setSelectedOrganisationUnits( selectedUnits );
    }

    private OrganisationUnit getSingleRootOrganisationUnit()
        throws Exception
    {
        Collection<OrganisationUnit> rootUnits = getRootOrganisationUnits();

        return rootUnits.iterator().next();
    }

    private OrganisationUnit getSingleSelectedOrganisationUnit()
        throws Exception
    {
        Collection<OrganisationUnit> selectedUnits = selectionManager.getSelectedOrganisationUnits();

        if ( selectedUnits.isEmpty() )
        {
            return null;
        }
        return selectedUnits.iterator().next();
    }

    private Collection<OrganisationUnit> getRootOrganisationUnits()
        throws Exception
    {
        return selectionManager.getRootOrganisationUnits();
    }
}
