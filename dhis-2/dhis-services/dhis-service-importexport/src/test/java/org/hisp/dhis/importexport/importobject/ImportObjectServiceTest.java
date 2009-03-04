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
import org.hisp.dhis.importexport.GroupMemberAssociation;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObject;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.period.MonthlyPeriodType;

import static org.hisp.dhis.expression.Expression.SEPARATOR;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ImportObjectServiceTest
    extends DhisConvenienceTest
{
    private ImportObjectService importObjectService;
    
    private DataElement dataElementA;    
    private DataElement dataElementB;    
    private DataElement dataElementC;
    
    private DataElementCategoryCombo categoryComboA;
    
    private DataElementGroup dataElementGroupA;    
    private DataElementGroup dataElementGroupB;
    private DataElementGroup dataElementGroupC;
    
    private GroupMemberAssociation associationA; 
    private GroupMemberAssociation associationB; 
    private GroupMemberAssociation associationC;
    private GroupMemberAssociation associationD; 
    private GroupMemberAssociation associationE; 
    private GroupMemberAssociation associationF;
    
    private DataSet dataSetA;
    
    private GroupMemberAssociation associationG;
    private GroupMemberAssociation associationH;
    private GroupMemberAssociation associationI;    
    
    private IndicatorType indicatorTypeA;
    
    private Indicator indicatorA;    
    private Indicator indicatorB;    
    private Indicator indicatorC;
    
    private IndicatorGroup indicatorGroupA;
    private IndicatorGroup indicatorGroupB;

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
        importObjectService = (ImportObjectService) getBean( ImportObjectService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );

        indicatorService = (IndicatorService) getBean( IndicatorService.ID );

        categoryComboA = new DataElementCategoryCombo( "CategoryComboA" );
        
        categoryComboA.setId( 'A' );
        
        dataElementA = createDataElement( 'A', categoryComboA );        
        dataElementB = createDataElement( 'B', categoryComboA );        
        dataElementC = createDataElement( 'C', categoryComboA );
        
        dataElementA.setId( 'A' );
        dataElementB.setId( 'B' );
        dataElementC.setId( 'C' );
        
        dataElementGroupA = createDataElementGroup( 'A' );
        dataElementGroupB = createDataElementGroup( 'B' );
        dataElementGroupC = createDataElementGroup( 'C' );
        
        dataElementGroupA.setId( 'A' );
        dataElementGroupB.setId( 'B' );
        dataElementGroupB.setId( 'C' );

        associationA = new GroupMemberAssociation( 'A', 'A' );
        associationB = new GroupMemberAssociation( 'A', 'B' );        
        associationC = new GroupMemberAssociation( 'A', 'C' );
        associationD = new GroupMemberAssociation( 'B', 'A' );        
        associationE = new GroupMemberAssociation( 'B', 'B' );        
        associationF = new GroupMemberAssociation( 'B', 'C' );
        
        dataSetA = createDataSet( 'A', new MonthlyPeriodType() );
        
        dataSetA.setId( 'A' );
        
        associationG = new GroupMemberAssociation( 'A', 'A' );
        associationH = new GroupMemberAssociation( 'A', 'B' );
        associationI = new GroupMemberAssociation( 'A', 'C' );
                
        indicatorTypeA = createIndicatorType( 'A' );
        
        indicatorTypeA.setId( 'A' );
        
        indicatorA = createIndicator( 'A', indicatorTypeA );        
        indicatorB = createIndicator( 'B', indicatorTypeA );        
        indicatorC = createIndicator( 'C', indicatorTypeA );
        
        indicatorA.setId( 'A' );
        indicatorB.setId( 'B' );
        indicatorC.setId( 'C' );
        
        indicatorGroupA = createIndicatorGroup( 'A' );
        indicatorGroupB = createIndicatorGroup( 'B' );
        
        indicatorGroupA.setId( 'A' );
        indicatorGroupB.setId( 'B' );

        idA = importObjectService.addImportObject( ImportObjectStatus.NEW, DataElement.class, GroupMemberType.NONE, dataElementA );
        idB = importObjectService.addImportObject( ImportObjectStatus.NEW, DataElement.class, GroupMemberType.NONE, dataElementB );
        idC = importObjectService.addImportObject( ImportObjectStatus.UPDATE, DataElement.class, GroupMemberType.NONE, dataElementC );
        idD = importObjectService.addImportObject( ImportObjectStatus.NEW, DataElementGroup.class, GroupMemberType.NONE, dataElementGroupA );
        idE = importObjectService.addImportObject( ImportObjectStatus.UPDATE, DataElementGroup.class, GroupMemberType.NONE, dataElementGroupB );
        idF = importObjectService.addImportObject( ImportObjectStatus.UPDATE, DataElementGroup.class, GroupMemberType.NONE, dataElementGroupC );
        idG = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationA );
        idH = importObjectService.addImportObject( ImportObjectStatus.MATCH, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationB );
        idI = importObjectService.addImportObject( ImportObjectStatus.MATCH, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationC );
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
            assertNull( String.valueOf( id ), importObjectService.getImportObject( id ) );
        }
    }
    
    private void assertNotNulls( Integer... identifiers )
    {
        for ( Integer id : identifiers )
        {
            assertNotNull( String.valueOf( id ), importObjectService.getImportObject( id ) );
        }
    }
    
    // -------------------------------------------------------------------------
    // ImportObject
    // -------------------------------------------------------------------------
    
    public void testAddGetImportObject()
    {
        assertEquals( importObjectService.getImportObject( idA ), ImportObjectStatus.NEW, DataElement.class, GroupMemberType.NONE, dataElementA, null );        
        assertEquals( importObjectService.getImportObject( idC ), ImportObjectStatus.UPDATE, DataElement.class, GroupMemberType.NONE, dataElementC, null );
        assertEquals( importObjectService.getImportObject( idH ), ImportObjectStatus.MATCH, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationB, null );
    }
    
    public void testAddGetImportObjectWithCompareObject()
    {
        int importObjectIdA = importObjectService.addImportObject( ImportObjectStatus.NEW, DataElement.class, dataElementA, dataElementB );
        int importObjectIdB = importObjectService.addImportObject( ImportObjectStatus.UPDATE, DataElement.class, dataElementB, dataElementC );
        int importObjectIdC = importObjectService.addImportObject( ImportObjectStatus.MATCH, DataElement.class, dataElementC, dataElementA );
        
        assertEquals( importObjectService.getImportObject( importObjectIdA ), ImportObjectStatus.NEW, DataElement.class, GroupMemberType.NONE, dataElementA, dataElementB );
        assertEquals( importObjectService.getImportObject( importObjectIdB ), ImportObjectStatus.UPDATE, DataElement.class, GroupMemberType.NONE, dataElementB, dataElementC );
        assertEquals( importObjectService.getImportObject( importObjectIdC ), ImportObjectStatus.MATCH, DataElement.class, GroupMemberType.NONE, dataElementC, dataElementA );
    }

    public void testGetImportObjectsByClass()
    {
        Collection<ImportObject> importObjects = importObjectService.getImportObjects( DataElement.class );
        
        assertEquals( importObjects.size(), 3 );
        
        assertTrue( importObjects.contains( importObjectService.getImportObject( idA ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idB ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idC ) ) );
        
        importObjects = importObjectService.getImportObjects( GroupMemberAssociation.class );

        assertEquals( importObjects.size(), 3 );
        
        assertTrue( importObjects.contains( importObjectService.getImportObject( idG ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idH ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idI ) ) );        
    }

    public void testGetImportObjectsByStatusClass()
    {
        Collection<ImportObject> importObjects = importObjectService.getImportObjects( ImportObjectStatus.NEW, DataElement.class );
        
        assertEquals( importObjects.size(), 2 );
        
        assertTrue( importObjects.contains( importObjectService.getImportObject( idA ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idB ) ) );
        
        importObjects = importObjectService.getImportObjects( ImportObjectStatus.MATCH, GroupMemberAssociation.class );

        assertEquals( importObjects.size(), 2 );
        
        assertTrue( importObjects.contains( importObjectService.getImportObject( idH )  ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idI )  ) );        
    }

    public void testGetImportObjectsByGroupMemberType()
    {
        Collection<ImportObject> importObjects = importObjectService.getImportObjects( GroupMemberType.NONE );
        
        assertEquals( importObjects.size(), 6 );
        
        assertTrue( importObjects.contains( importObjectService.getImportObject( idA ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idB ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idC ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idD ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idE ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idF ) ) );
        
        importObjects = importObjectService.getImportObjects( GroupMemberType.DATAELEMENTGROUP );

        assertEquals( importObjects.size(), 3 );
        
        assertTrue( importObjects.contains( importObjectService.getImportObject( idG ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idH ) ) );
        assertTrue( importObjects.contains( importObjectService.getImportObject( idI ) ) );        
    }
    
    public void testDeleteImportObject()
    {
        assertNotNulls( idA, idB, idC );

        importObjectService.deleteImportObject( idA );

        assertNulls( idA );
        assertNotNulls( idB, idC );

        importObjectService.deleteImportObject( idB );

        assertNulls( idA, idB );
        assertNotNulls( idC );

        importObjectService.deleteImportObject( idC );

        assertNulls( idA, idB, idC );
    }
    
    public void testDeleteImportObjectsByClass()
    {
        assertNotNulls( idA, idB, idC, idD, idE, idF, idG, idH, idI );
        
        importObjectService.deleteImportObjects( DataElement.class );

        assertNotNulls( idD, idE, idF, idG, idH, idI );
        assertNulls( idA, idB, idC );

        importObjectService.deleteImportObjects( DataElementGroup.class );

        assertNotNulls( idG, idH, idI );
        assertNulls( idA, idB, idC, idD, idE, idF );
    }
    
    public void testCascadeDeleteImportObject()
    {
        int idJ = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationD );
        int idK = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationE );
        int idL = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationF );
        
        int idM = importObjectService.addImportObject( ImportObjectStatus.NEW, DataSet.class, GroupMemberType.NONE, dataSetA );
        
        int idN = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATASET, associationG );
        int idO = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATASET, associationH );
        int idP = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATASET, associationI );
        
        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( DataElementGroup.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( DataSet.class ).size(), 1 );
        assertEquals( importObjectService.getImportObjects( GroupMemberAssociation.class ).size(), 9 );
        
        assertNotNulls( idJ, idK, idL, idM, idN, idO, idP );
        
        importObjectService.cascadeDeleteImportObject( idA );

        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( DataElementGroup.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( DataSet.class ).size(), 1 );
        assertEquals( importObjectService.getImportObjects( GroupMemberAssociation.class ).size(), 6 );

        assertNulls( idA, idG, idJ, idN );
        
        importObjectService.cascadeDeleteImportObject( idD );

        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( DataElementGroup.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( DataSet.class ).size(), 1 );
        assertEquals( importObjectService.getImportObjects( GroupMemberAssociation.class ).size(), 4 );
        
        assertNulls( idD, idH, idI );
        
        importObjectService.cascadeDeleteImportObject( idM );

        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( DataElementGroup.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( DataSet.class ).size(), 0 );
        assertEquals( importObjectService.getImportObjects( GroupMemberAssociation.class ).size(), 2 );
        
        assertNulls( idM, idO, idP );
    }
    
    public void testCascadeDeleteIndicatorsContainingDataElement()
    {
        String suffix = SEPARATOR + categoryComboA.getId();
        
        indicatorA.setNumerator( "[" + Integer.valueOf( 'A' ) + suffix + "]+[" + Integer.valueOf( 'B' ) + suffix + "]-[" + Integer.valueOf( 'C' ) + suffix + "]" );
        indicatorA.setDenominator( "[" + Integer.valueOf( 'A' ) + suffix + "]" );

        indicatorB.setNumerator( "[" + Integer.valueOf( 'B' ) + suffix + "]+[" + Integer.valueOf( 'C' ) + suffix + "]" );
        indicatorB.setDenominator( "[" + Integer.valueOf( 'B' ) + suffix + "]" );
        
        indicatorC.setNumerator( "[" + Integer.valueOf( 'A' ) + suffix + "]" );
        indicatorC.setDenominator( "[" + Integer.valueOf( 'A' ) + suffix + "]" );
        
        importObjectService.addImportObject( ImportObjectStatus.NEW, Indicator.class, GroupMemberType.NONE, indicatorA );
        importObjectService.addImportObject( ImportObjectStatus.NEW, Indicator.class, GroupMemberType.NONE, indicatorB );
        importObjectService.addImportObject( ImportObjectStatus.NEW, Indicator.class, GroupMemberType.NONE, indicatorC );
        
        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( Indicator.class ).size(), 3 );
        
        importObjectService.cascadeDeleteImportObject( idA );

        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( Indicator.class ).size(), 1 );

        importObjectService.cascadeDeleteImportObject( idC );

        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 1 );
        assertEquals( importObjectService.getImportObjects( Indicator.class ).size(), 0 );
    }
    
    public void testCascadeDeleteImportObjects()
    {
        int idJ = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationD );
        int idK = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationE );
        int idL = importObjectService.addImportObject( ImportObjectStatus.NEW, GroupMemberAssociation.class, GroupMemberType.DATAELEMENTGROUP, associationF );
        
        int idM = importObjectService.addImportObject( ImportObjectStatus.NEW, DataSet.class, GroupMemberType.NONE, dataSetA );
        
        int idN = importObjectService.addImportObject( ImportObjectStatus.NEW, Indicator.class, GroupMemberType.NONE, indicatorA );
        int idO = importObjectService.addImportObject( ImportObjectStatus.NEW, Indicator.class, GroupMemberType.NONE, indicatorB );
        
        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( DataElementGroup.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( Indicator.class ).size(), 2 );
        assertEquals( importObjectService.getImportObjects( DataSet.class ).size(), 1 );
        assertEquals( importObjectService.getImportObjects( GroupMemberAssociation.class ).size(), 6 );

        assertNotNulls( idJ, idK, idL, idM, idN, idO );
        
        importObjectService.cascadeDeleteImportObjects( DataElement.class );

        assertEquals( importObjectService.getImportObjects( DataElement.class ).size(), 0 );
        assertEquals( importObjectService.getImportObjects( DataElementGroup.class ).size(), 3 );
        assertEquals( importObjectService.getImportObjects( Indicator.class ).size(), 0 );
        assertEquals( importObjectService.getImportObjects( DataSet.class ).size(), 0 );
        assertEquals( importObjectService.getImportObjects( GroupMemberAssociation.class ).size(), 0 );

        assertNulls( idA, idB, idC, idG, idH, idI, idJ, idK, idL, idN, idO );
    }

    // -------------------------------------------------------------------------
    // Object
    // -------------------------------------------------------------------------
    
    public void testMatchObject()
    {
        indicatorService.addIndicatorType( indicatorTypeA );
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        int existingObjectIdA = dataElementService.addDataElement( dataElementA );
        int existingObjectIdB = indicatorService.addIndicator( indicatorA );
        
        int importObjectIdA = importObjectService.addImportObject( ImportObjectStatus.NEW, DataElement.class, dataElementB, null );
        int importObjectIdB = importObjectService.addImportObject( ImportObjectStatus.UPDATE, Indicator.class, indicatorB, indicatorA );
        
        importObjectService.matchObject( importObjectIdA, existingObjectIdA );
        importObjectService.matchObject( importObjectIdB, existingObjectIdB );
        
        ImportObject importObjectA = importObjectService.getImportObject( importObjectIdA );
        ImportObject importObjectB = importObjectService.getImportObject( importObjectIdB );
        
        DataElement existingObjectA = (DataElement) importObjectA.getObject();
        Indicator existingObjectB = (Indicator) importObjectB.getObject();
        
        assertEquals( existingObjectA.getId(), 'B' );
        assertEquals( existingObjectA.getName(), "DataElementA" );
        assertEquals( importObjectA.getStatus(), ImportObjectStatus.MATCH );

        assertEquals( existingObjectB.getId(), 'B' );
        assertEquals( existingObjectB.getName(), "IndicatorA" );
        assertEquals( importObjectB.getStatus(), ImportObjectStatus.MATCH );
    }
}
