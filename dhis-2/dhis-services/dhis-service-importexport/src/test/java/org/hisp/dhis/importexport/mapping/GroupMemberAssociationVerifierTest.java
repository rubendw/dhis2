package org.hisp.dhis.importexport.mapping;

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

import org.hisp.dhis.importexport.AssociationType;
import org.hisp.dhis.importexport.GroupMemberAssociation;
import org.hisp.dhis.importexport.GroupMemberType;

import junit.framework.TestCase;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GroupMemberAssociationVerifierTest
    extends TestCase
{
    public void testGroupMemberAssociationSet()
    {
        GroupMemberAssociationVerifier.clear();
        
        GroupMemberAssociation associationA = new GroupMemberAssociation( 1, 1 );
        GroupMemberAssociation associationB = new GroupMemberAssociation( 1, 2 );
        GroupMemberAssociation associationC = new GroupMemberAssociation( 1, 3 );
        GroupMemberAssociation associationD = new GroupMemberAssociation( 1, 1 );
        GroupMemberAssociation associationE = new GroupMemberAssociation( 1, 2 );
        GroupMemberAssociation associationF = new GroupMemberAssociation( 1, 4 );
        
        GroupMemberType typeA = GroupMemberType.DATAELEMENTGROUP;
        
        assertEquals( AssociationType.SET, associationA.getAssociationType() );
        
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationA, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationB, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationC, typeA ) );
        assertFalse( GroupMemberAssociationVerifier.isUnique( associationD, typeA ) );
        assertFalse( GroupMemberAssociationVerifier.isUnique( associationE, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationF, typeA ) );
    }
    
    public void testGroupMemberAssociationList()
    {
        GroupMemberAssociationVerifier.clear();
        
        GroupMemberAssociation associationA = new GroupMemberAssociation( 1, 1, 0 );
        GroupMemberAssociation associationB = new GroupMemberAssociation( 1, 2, 1 );
        GroupMemberAssociation associationC = new GroupMemberAssociation( 1, 3, 2 );
        GroupMemberAssociation associationD = new GroupMemberAssociation( 1, 1, 1 );
        GroupMemberAssociation associationE = new GroupMemberAssociation( 1, 2, 2 );
        GroupMemberAssociation associationF = new GroupMemberAssociation( 1, 3, 3 );
        
        GroupMemberType typeA = GroupMemberType.REPORTTABLE_INDICATOR;

        assertEquals( AssociationType.LIST, associationA.getAssociationType() );
        
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationA, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationB, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationC, typeA ) );
        assertFalse( GroupMemberAssociationVerifier.isUnique( associationD, typeA ) );
        assertFalse( GroupMemberAssociationVerifier.isUnique( associationE, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationF, typeA ) );
    }
    
    public void testGroupMemberAssocation()
    {
        GroupMemberAssociationVerifier.clear();
        
        GroupMemberAssociation associationA = new GroupMemberAssociation( 1, 1 );
        GroupMemberAssociation associationB = new GroupMemberAssociation( 1, 2 );
        GroupMemberAssociation associationC = new GroupMemberAssociation( 1, 3 );
        GroupMemberAssociation associationD = new GroupMemberAssociation( 1, 2 );
        
        GroupMemberAssociation associationE = new GroupMemberAssociation( 1, 1, 1 );
        GroupMemberAssociation associationF = new GroupMemberAssociation( 1, 2, 2 );
        GroupMemberAssociation associationG = new GroupMemberAssociation( 1, 3, 3 );
        GroupMemberAssociation associationH = new GroupMemberAssociation( 1, 4, 2 );

        GroupMemberType typeA = GroupMemberType.REPORTTABLE_INDICATOR;        

        assertEquals( AssociationType.SET, associationA.getAssociationType() );
        assertEquals( AssociationType.LIST, associationE.getAssociationType() );
        
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationA, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationB, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationC, typeA ) );
        assertFalse( GroupMemberAssociationVerifier.isUnique( associationD, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationE, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationF, typeA ) );
        assertTrue( GroupMemberAssociationVerifier.isUnique( associationG, typeA ) );
        assertFalse( GroupMemberAssociationVerifier.isUnique( associationH, typeA ) );
    }
}
