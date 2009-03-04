package org.hisp.dhis.importexport.importobject;

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

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.importexport.GroupMemberAssociation;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObject;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.importexport.ImportObjectStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ImportObjectStoreTest
    extends DhisConvenienceTest
{
    private ImportObjectStore importObjectStore;
    
    private DataElement dataElementA;
    private DataElement dataElementB;
    private DataElement dataElementC;
    
    private DataElementGroup dataElementGroupA;
    private DataElementGroup dataElementGroupB;
    private DataElementGroup dataElementGroupC;
    
    private GroupMemberAssociation associationA; 
    private GroupMemberAssociation associationB; 
    private GroupMemberAssociation associationC; 
    
    private ImportObject importObjectA;
    private ImportObject importObjectB;
    private ImportObject importObjectC;
    private ImportObject importObjectD;
    private ImportObject importObjectE;
    private ImportObject importObjectF;
    private ImportObject importObjectG;
    private ImportObject importObjectH;
    private ImportObject importObjectI;
    
    private int idA;
    private int idB;
    private int idC;
    private int idD;
    private int idE;
    private int idF;
    private int idG;
    private int idH;
    private int idI;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        importObjectStore = (ImportObjectStore) getBean( ImportObjectStore.ID );

        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        dataElementC = createDataElement( 'C' );
        
        dataElementGroupA = createDataElementGroup( 'A' );
        dataElementGroupB = createDataElementGroup( 'B' );
        dataElementGroupC = createDataElementGroup( 'C' );
        
        associationA = new GroupMemberAssociation( 'A', 'A' );        
        associationB = new GroupMemberAssociation( 'B', 'B' );        
        associationC = new GroupMemberAssociation( 'C', 'C' );        
        
        importObjectA = new ImportObject( ImportObjectStatus.NEW, DataElement.class.getName(), GroupMemberType.NONE, dataElementA );
        importObjectB = new ImportObject( ImportObjectStatus.NEW, DataElement.class.getName(), GroupMemberType.NONE, dataElementB );
        importObjectC = new ImportObject( ImportObjectStatus.UPDATE, DataElement.class.getName(), GroupMemberType.NONE, dataElementC );
        importObjectD = new ImportObject( ImportObjectStatus.NEW, DataElementGroup.class.getName(), GroupMemberType.NONE, dataElementGroupA );
        importObjectE = new ImportObject( ImportObjectStatus.UPDATE, DataElementGroup.class.getName(), GroupMemberType.NONE, dataElementGroupB );
        importObjectF = new ImportObject( ImportObjectStatus.UPDATE, DataElementGroup.class.getName(), GroupMemberType.NONE, dataElementGroupC );
        importObjectG = new ImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class.getName(), GroupMemberType.DATAELEMENTGROUP, associationA );
        importObjectH = new ImportObject( ImportObjectStatus.MATCH, GroupMemberAssociation.class.getName(), GroupMemberType.DATAELEMENTGROUP, associationB );
        importObjectI = new ImportObject( ImportObjectStatus.MATCH, GroupMemberAssociation.class.getName(), GroupMemberType.DATAELEMENTGROUP, associationC );
        
        idA = importObjectStore.addImportObject( importObjectA );
        idB = importObjectStore.addImportObject( importObjectB );
        idC = importObjectStore.addImportObject( importObjectC );
        idD = importObjectStore.addImportObject( importObjectD );
        idE = importObjectStore.addImportObject( importObjectE );
        idF = importObjectStore.addImportObject( importObjectF );
        idG = importObjectStore.addImportObject( importObjectG );
        idH = importObjectStore.addImportObject( importObjectH );
        idI = importObjectStore.addImportObject( importObjectI );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void assertEquals( ImportObject importObject, ImportObjectStatus status, Class<?> clazz, GroupMemberType type, Object object, Object compareObject )
    {
        assertEquals( importObject.getStatus(), status );
        assertEquals( importObject.getClassName(), clazz.getName() );
        assertEquals( importObject.getGroupMemberType(), type );
        assertEquals( importObject.getObject(), object );
        assertEquals( importObject.getCompareObject(), compareObject );
    }
    
    private void assertNulls( Integer... identifiers )
    {
        for ( Integer id : identifiers )
        {
            assertNull( importObjectStore.getImportObject( id ) );
        }
    }
    
    private void assertNotNulls( Integer... identifiers )
    {
        for ( Integer id : identifiers )
        {
            assertNotNull( importObjectStore.getImportObject( id ) );
        }
    }
    
    // -------------------------------------------------------------------------
    // ImportObject
    // -------------------------------------------------------------------------
    
    public void testAddGetImportObject()
    {
        assertEquals( importObjectStore.getImportObject( idA ), ImportObjectStatus.NEW, DataElement.class, GroupMemberType.NONE, dataElementA, null );        
        assertEquals( importObjectStore.getImportObject( idC ), ImportObjectStatus.UPDATE, DataElement.class, GroupMemberType.NONE, dataElementC, null );
        assertEquals( importObjectStore.getImportObject( idH ), ImportObjectStatus.MATCH, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationB, null );
    }
    
    public void testUpdateImportObject()
    {
        assertEquals( importObjectStore.getImportObject( idA ), ImportObjectStatus.NEW, DataElement.class, GroupMemberType.NONE, dataElementA, null );        
        assertEquals( importObjectStore.getImportObject( idC ), ImportObjectStatus.UPDATE, DataElement.class, GroupMemberType.NONE, dataElementC, null );
        assertEquals( importObjectStore.getImportObject( idH ), ImportObjectStatus.MATCH, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationB, null );
        
        importObjectA.setClassName( DataElementGroup.class.getName() );
        importObjectC.setStatus( ImportObjectStatus.MATCH );
        importObjectH.setObject( dataElementA );
        
        importObjectStore.updateImportObject( importObjectA );
        importObjectStore.updateImportObject( importObjectC );
        importObjectStore.updateImportObject( importObjectH );
        
        assertEquals( importObjectStore.getImportObject( idA ), ImportObjectStatus.NEW, DataElementGroup.class, GroupMemberType.NONE, dataElementA, null );        
        assertEquals( importObjectStore.getImportObject( idC ), ImportObjectStatus.MATCH, DataElement.class, GroupMemberType.NONE, dataElementC, null );
        assertEquals( importObjectStore.getImportObject( idH ), ImportObjectStatus.MATCH, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, dataElementA, null );        
    }
    
    public void testGetImportObjectsByClass()
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( DataElement.class );
        
        assertEquals( importObjects.size(), 3 );
        
        assertTrue( importObjects.contains( importObjectA ) );
        assertTrue( importObjects.contains( importObjectB ) );
        assertTrue( importObjects.contains( importObjectC ) );
        
        importObjects = importObjectStore.getImportObjects( GroupMemberAssociation.class );

        assertEquals( importObjects.size(), 3 );
        
        assertTrue( importObjects.contains( importObjectG ) );
        assertTrue( importObjects.contains( importObjectH ) );
        assertTrue( importObjects.contains( importObjectI ) );        
    }

    public void testGetImportObjectsByStatusClass()
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( ImportObjectStatus.NEW, DataElement.class );
        
        assertEquals( importObjects.size(), 2 );
        
        assertTrue( importObjects.contains( importObjectA ) );
        assertTrue( importObjects.contains( importObjectB ) );
        
        importObjects = importObjectStore.getImportObjects( ImportObjectStatus.MATCH, GroupMemberAssociation.class );

        assertEquals( importObjects.size(), 2 );
        
        assertTrue( importObjects.contains( importObjectH ) );
        assertTrue( importObjects.contains( importObjectI ) );        
    }

    public void testGetImportObjectsByGroupMemberType()
    {
        Collection<ImportObject> importObjects = importObjectStore.getImportObjects( GroupMemberType.NONE );
        
        assertEquals( importObjects.size(), 6 );
        
        assertTrue( importObjects.contains( importObjectA ) );
        assertTrue( importObjects.contains( importObjectB ) );
        assertTrue( importObjects.contains( importObjectC ) );
        assertTrue( importObjects.contains( importObjectD ) );
        assertTrue( importObjects.contains( importObjectE ) );
        assertTrue( importObjects.contains( importObjectF ) );
        
        importObjects = importObjectStore.getImportObjects( GroupMemberType.DATAELEMENTGROUP );

        assertEquals( importObjects.size(), 3 );
        
        assertTrue( importObjects.contains( importObjectG ) );
        assertTrue( importObjects.contains( importObjectH ) );
        assertTrue( importObjects.contains( importObjectI ) );        
    }

    public void testDeleteImportObject()
    {
        assertNotNulls( idA, idB, idC );
        
        importObjectStore.deleteImportObject( importObjectA );
        
        assertNulls( idA );
        assertNotNulls( idB, idC );

        importObjectStore.deleteImportObject( importObjectB );
        
        assertNulls( idA, idB );
        assertNotNulls( idC );

        importObjectStore.deleteImportObject( importObjectC );
        
        assertNulls( idA, idB, idC );
    }

    public void testDeleteImportObjectsByClass()
    {
        assertNotNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );

        assertEquals( importObjectStore.getImportObjects( DataElement.class ).size(), 3 );
        assertEquals( importObjectStore.getImportObjects( DataElementGroup.class ).size(), 3 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberAssociation.class ).size(), 3 );

        importObjectStore.deleteImportObjects( DataElement.class );
        
        assertNotNulls( idD, idE, idF, idG, idH, idI );
        assertNulls( idA, idB, idC );

        assertEquals( importObjectStore.getImportObjects( DataElement.class ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( DataElementGroup.class ).size(), 3 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberAssociation.class ).size(), 3 );

        importObjectStore.deleteImportObjects( DataElementGroup.class );
        
        assertNotNulls( idG, idH, idI );
        assertNulls( idA, idB, idC, idD, idE, idF );

        assertEquals( importObjectStore.getImportObjects( DataElement.class ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( DataElementGroup.class ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberAssociation.class ).size(), 3 );

        importObjectStore.deleteImportObjects( GroupMemberAssociation.class );
        
        assertNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );

        assertEquals( importObjectStore.getImportObjects( DataElement.class ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( DataElementGroup.class ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberAssociation.class ).size(), 0 );
    }

    public void testDeleteImportObjectsByGroupMemberType()
    {
        assertNotNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );

        assertEquals( importObjectStore.getImportObjects( GroupMemberType.NONE ).size(), 6 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberType.DATAELEMENTGROUP ).size(), 3 );

        importObjectStore.deleteImportObjects( GroupMemberType.NONE );
        
        assertNotNulls( idG, idH, idI );
        assertNulls( idA, idB, idC, idD, idE, idF );
        
        assertEquals( importObjectStore.getImportObjects( GroupMemberType.NONE ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberType.DATAELEMENTGROUP ).size(), 3 );

        importObjectStore.deleteImportObjects( GroupMemberType.DATAELEMENTGROUP );
        
        assertNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );

        assertEquals( importObjectStore.getImportObjects( GroupMemberType.NONE ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberType.DATAELEMENTGROUP ).size(), 0 );
    }
    
    public void testDeleteAllImportObjects()
    {
        assertNotNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );

        assertEquals( importObjectStore.getImportObjects( GroupMemberType.NONE ).size(), 6 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberType.DATAELEMENTGROUP ).size(), 3 );

        importObjectStore.deleteImportObjects();

        assertNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );

        assertEquals( importObjectStore.getImportObjects( GroupMemberType.NONE ).size(), 0 );
        assertEquals( importObjectStore.getImportObjects( GroupMemberType.DATAELEMENTGROUP ).size(), 0 );
    }
}
