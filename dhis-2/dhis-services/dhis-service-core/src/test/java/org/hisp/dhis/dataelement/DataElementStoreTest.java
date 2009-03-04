package org.hisp.dhis.dataelement;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.hierarchy.HierarchyViolationException;
import org.hisp.dhis.system.util.UUIdUtils;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DataElementStoreTest.java 5742 2008-09-26 11:37:35Z larshelg $
 */
public class DataElementStoreTest
    extends DhisSpringTest
{
    private DataElementStore dataElementStore;
    
    private TransactionManager transactionManager;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        dataElementStore = (DataElementStore) getBean( DataElementStore.ID );

        transactionManager = (TransactionManager) getBean( TransactionManager.ID );
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private DataElement createDataElement( char uniqueCharacter )
    {
        DataElement dataElement = new DataElement();

        setDataElementFields(dataElement, uniqueCharacter);

        return dataElement;
    }

    private DataElement setDataElementFields( DataElement dataElement, char uniqueCharacter )
    {
        dataElement.setUuid( UUIdUtils.getUUId() );
        dataElement.setName( "DataElement" + uniqueCharacter );
        dataElement.setAlternativeName( "AlternativeName" + uniqueCharacter );
        dataElement.setShortName( "DE" + uniqueCharacter );
        dataElement.setCode( "Code" + uniqueCharacter );
        dataElement.setDescription( "DataElementDescription" + uniqueCharacter );
        dataElement.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement.setType( DataElement.TYPE_INT );
        return dataElement;
    }
    
    private void assertEquals( char uniqueCharacter, DataElement dataElement )
    {
        assertEquals( "DataElement" + uniqueCharacter, dataElement.getName() );
        assertEquals( "AlternativeName" + uniqueCharacter, dataElement.getAlternativeName() );
        assertEquals( "DE" + uniqueCharacter, dataElement.getShortName() );
        assertEquals( "Code" + uniqueCharacter, dataElement.getCode() );
        assertEquals( "DataElementDescription" + uniqueCharacter, dataElement.getDescription() );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddDataElement()
    {
        transactionManager.enter();

        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'A' );

        int idA = dataElementStore.addDataElement( dataElementA );
        int idB = dataElementStore.addDataElement( dataElementB );
        int idC = dataElementStore.addDataElement( dataElementC );

        transactionManager.leave();

        try
        {
            // Should give unique constraint violation
            dataElementStore.addDataElement( dataElementD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        transactionManager.enter();

        dataElementA = dataElementStore.getDataElement( idA );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementStore.getDataElement( idB );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        dataElementC = dataElementStore.getDataElement( idC );
        assertNotNull( dataElementC );
        assertEquals( idC, dataElementC.getId() );
        assertEquals( 'C', dataElementC );

        transactionManager.leave();
    }

    public void testUpdateDataElement()
    {
        DataElement dataElementA = createDataElement( 'A' );
        int idA = dataElementStore.addDataElement( dataElementA );
        dataElementA = dataElementStore.getDataElement( idA );
        assertEquals( DataElement.TYPE_INT, dataElementA.getType() );

        dataElementA.setType( DataElement.TYPE_BOOL );
        dataElementStore.updateDataElement( dataElementA );
        dataElementA = dataElementStore.getDataElement( idA );
        assertNotNull( dataElementA.getType() );
        assertEquals( DataElement.TYPE_BOOL, dataElementA.getType() );
    }

    public void testDeleteAndGetDataElement()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        int idA = dataElementStore.addDataElement( dataElementA );
        int idB = dataElementStore.addDataElement( dataElementB );
        int idC = dataElementStore.addDataElement( dataElementC );
        int idD = dataElementStore.addDataElement( dataElementD );

        assertNotNull( dataElementStore.getDataElement( idA ) );
        assertNotNull( dataElementStore.getDataElement( idB ) );
        assertNotNull( dataElementStore.getDataElement( idC ) );
        assertNotNull( dataElementStore.getDataElement( idD ) );

        transactionManager.enter();

        dataElementA.setParent( dataElementB );
        dataElementB.getChildren().add( dataElementA );
        dataElementStore.updateDataElement( dataElementA );

        transactionManager.leave();
        transactionManager.enter();

        dataElementA = dataElementStore.getDataElement( idA );
        dataElementB = dataElementStore.getDataElement( idB );

        try
        {
            dataElementStore.deleteDataElement( dataElementB );
            fail();
        }
        catch ( HierarchyViolationException e )
        {
            // Expected
        }

        transactionManager.leave();
        transactionManager.enter();

        dataElementA.setParent( null );
        dataElementStore.updateDataElement( dataElementA );

        transactionManager.leave();
        transactionManager.enter();

        dataElementA = dataElementStore.getDataElement( idA );
        dataElementB = dataElementStore.getDataElement( idB );
        dataElementC = dataElementStore.getDataElement( idC );
        dataElementD = dataElementStore.getDataElement( idD );

        dataElementStore.deleteDataElement( dataElementA );
        assertNull( dataElementStore.getDataElement( idA ) );
        assertNotNull( dataElementStore.getDataElement( idB ) );
        assertNotNull( dataElementStore.getDataElement( idC ) );
        assertNotNull( dataElementStore.getDataElement( idD ) );

        dataElementStore.deleteDataElement( dataElementB );
        assertNull( dataElementStore.getDataElement( idA ) );
        assertNull( dataElementStore.getDataElement( idB ) );
        assertNotNull( dataElementStore.getDataElement( idC ) );
        assertNotNull( dataElementStore.getDataElement( idD ) );

        dataElementStore.deleteDataElement( dataElementC );
        assertNull( dataElementStore.getDataElement( idA ) );
        assertNull( dataElementStore.getDataElement( idB ) );
        assertNull( dataElementStore.getDataElement( idC ) );
        assertNotNull( dataElementStore.getDataElement( idD ) );

        dataElementStore.deleteDataElement( dataElementD );
        assertNull( dataElementStore.getDataElement( idA ) );
        assertNull( dataElementStore.getDataElement( idB ) );
        assertNull( dataElementStore.getDataElement( idC ) );
        assertNull( dataElementStore.getDataElement( idD ) );

        transactionManager.leave();
    }
    
    public void testGetDataElementByUUID()
    {
        String uuid = UUIdUtils.getUUId();
        
        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setUuid( uuid );
        
        dataElementStore.addDataElement( dataElementA );
        
        dataElementA = dataElementStore.getDataElement( uuid );
        
        assertNotNull( dataElementA );
        assertEquals( dataElementA.getUuid(), uuid );
    }

    public void testGetDataElementByName()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementStore.addDataElement( dataElementA );
        int idB = dataElementStore.addDataElement( dataElementB );

        dataElementA = dataElementStore.getDataElementByName( "DataElementA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementStore.getDataElementByName( "DataElementB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementStore.getDataElementByName( "DataElementC" );
        assertNull( dataElementC );
    }

    public void testGetDataElementByAlternativeName()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementStore.addDataElement( dataElementA );
        int idB = dataElementStore.addDataElement( dataElementB );

        dataElementA = dataElementStore.getDataElementByAlternativeName( "AlternativeNameA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementStore.getDataElementByAlternativeName( "AlternativeNameB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementStore.getDataElementByAlternativeName( "AlternativeNameC" );
        assertNull( dataElementC );
    }

    public void testGetDataElementByShortName()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementStore.addDataElement( dataElementA );
        int idB = dataElementStore.addDataElement( dataElementB );

        dataElementA = dataElementStore.getDataElementByShortName( "DEA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementStore.getDataElementByShortName( "DEB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementStore.getDataElementByShortName( "DEC" );
        assertNull( dataElementC );
    }

    public void testGetDataElementByCode()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementStore.addDataElement( dataElementA );
        int idB = dataElementStore.addDataElement( dataElementB );

        dataElementA = dataElementStore.getDataElementByCode( "CodeA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementStore.getDataElementByCode( "CodeB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementStore.getDataElementByCode( "CodeC" );
        assertNull( dataElementC );
    }

    public void testGetAllDataElements()
    {
        assertEquals( 0, dataElementStore.getAllDataElements().size() );

        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
        dataElementStore.addDataElement( dataElementD );

        Collection<DataElement> dataElementsRef = new HashSet<DataElement>();
        dataElementsRef.add( dataElementA );
        dataElementsRef.add( dataElementB );
        dataElementsRef.add( dataElementC );
        dataElementsRef.add( dataElementD );

        transactionManager.enter();

        Collection<DataElement> dataElements = dataElementStore.getAllDataElements();
        assertNotNull( dataElements );
        assertEquals( dataElementsRef.size(), dataElements.size() );
        assertTrue( dataElements.containsAll( dataElementsRef ) );

        transactionManager.leave();
    }

    public void testGetAggregateableDataElements()
    {
        assertEquals( 0, dataElementStore.getAggregateableDataElements().size() );

        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );
        
        dataElementA.setType( DataElement.TYPE_INT );
        dataElementB.setType( DataElement.TYPE_BOOL );
        dataElementC.setType( DataElement.TYPE_STRING );
        dataElementD.setType( DataElement.TYPE_INT );

        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
        dataElementStore.addDataElement( dataElementD );

        Collection<DataElement> dataElementsRef = new HashSet<DataElement>();
        dataElementsRef.add( dataElementA );
        dataElementsRef.add( dataElementB );
        dataElementsRef.add( dataElementD );

        transactionManager.enter();

        Collection<DataElement> dataElements = dataElementStore.getAggregateableDataElements();
        assertNotNull( dataElements );
        assertEquals( dataElementsRef.size(), dataElements.size() );
        assertTrue( dataElements.containsAll( dataElementsRef ) );

        transactionManager.leave();
    }
    
    public void testGetAllActiveDataElements()
    {
        assertEquals( 0, dataElementStore.getAllActiveDataElements().size() );

        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setActive( true );
        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setActive( true );
        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setActive( true );
        DataElement dataElementD = createDataElement( 'D' );

        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
        dataElementStore.addDataElement( dataElementD );

        Collection<DataElement> dataElementsRef = new HashSet<DataElement>();
        dataElementsRef.add( dataElementA );
        dataElementsRef.add( dataElementB );
        dataElementsRef.add( dataElementC );

        transactionManager.enter();

        assertEquals( dataElementsRef.size() + 1, dataElementStore.getAllDataElements().size() );

        Collection<DataElement> dataElements = dataElementStore.getAllActiveDataElements();
        assertNotNull( dataElements );
        assertEquals( dataElementsRef.size(), dataElements.size() );
        assertTrue( dataElements.containsAll( dataElementsRef ) );

        transactionManager.leave();
    }

    public void testGetDataElementsByAggregationOperator()
    {
        assertEquals( 0, dataElementStore.getDataElementsByAggregationOperator(
            DataElement.AGGREGATION_OPERATOR_AVERAGE ).size() );
        assertEquals( 0, dataElementStore.getDataElementsByAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM )
            .size() );

        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement dataElementD = createDataElement( 'D' );
        dataElementD.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );

        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
        dataElementStore.addDataElement( dataElementD );

        assertEquals( 1, dataElementStore.getDataElementsByAggregationOperator(
            DataElement.AGGREGATION_OPERATOR_AVERAGE ).size() );
        assertEquals( 3, dataElementStore.getDataElementsByAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM )
            .size() );
    }

    public void testGetDataElementsByType()
    {
        assertEquals( 0, dataElementStore.getDataElementsByType( DataElement.TYPE_INT ).size() );
        assertEquals( 0, dataElementStore.getDataElementsByType( DataElement.TYPE_BOOL ).size() );

        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setType( DataElement.TYPE_INT );
        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setType( DataElement.TYPE_BOOL );
        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setType( DataElement.TYPE_BOOL );
        DataElement dataElementD = createDataElement( 'D' );
        dataElementD.setType( DataElement.TYPE_BOOL );

        dataElementStore.addDataElement( dataElementA );
        dataElementStore.addDataElement( dataElementB );
        dataElementStore.addDataElement( dataElementC );
        dataElementStore.addDataElement( dataElementD );

        assertEquals( 1, dataElementStore.getDataElementsByType( DataElement.TYPE_INT ).size() );
        assertEquals( 3, dataElementStore.getDataElementsByType( DataElement.TYPE_BOOL ).size() );
    }

    // -------------------------------------------------------------------------
    // CalculatedDataElements
    // -------------------------------------------------------------------------

    public void testCalculatedDataElements()
    {
        DataElement deA = createDataElement('A');
        DataElement deB = createDataElement('B');
        DataElement deC = createDataElement('C');
        DataElement deD = createDataElement('D');
        DataElement deE = createDataElement('E');

        int deIdA = dataElementStore.addDataElement(deA);
        int deIdB = dataElementStore.addDataElement(deB);
        int deIdC = dataElementStore.addDataElement(deC);
        int deIdD = dataElementStore.addDataElement(deD);
        dataElementStore.addDataElement(deE);
        
        CalculatedDataElement cdeX = (CalculatedDataElement) setDataElementFields( new CalculatedDataElement (), 'X' );
        CalculatedDataElement cdeY = (CalculatedDataElement) setDataElementFields( new CalculatedDataElement (), 'Y' );

        Set<DataElement> dataElementsX = new HashSet<DataElement> ();
        dataElementsX.add(deA);
        dataElementsX.add(deB);
        Expression expressionX = new Expression ( "["+deIdA+"] * 2 + ["+deIdB+"] * 3", "foo", dataElementsX );
        cdeX.setExpression(expressionX);
        cdeX.setSaved(true);
        dataElementStore.addDataElement(cdeX);
        
        Set<DataElement> dataElementsY = new HashSet<DataElement> ();
        dataElementsY.add(deC);
        dataElementsY.add(deD);
        Expression expressionY = new Expression ( "["+deIdC+"] * 2 + ["+deIdD+"] * 3", "foo", dataElementsY );
        cdeY.setExpression(expressionY);
        cdeY.setSaved(true);
        dataElementStore.addDataElement(cdeY);
        
        Collection<CalculatedDataElement> cdes = dataElementStore.getAllCalculatedDataElements();
        assertEquals( 2, cdes.size() );
        
        //CalculatedDataElement cde;
        CalculatedDataElement cde = dataElementStore.getCalculatedDataElementByDataElement( deA );
        assertNotNull(cde);
        assertEquals("DataElementX", cde.getName() );
        
        cde = dataElementStore.getCalculatedDataElementByDataElement( deE );
        assertNull(cde);
        
        Set<DataElement> dataElements = new HashSet<DataElement> ();
        dataElements.add(deA);
        cdes = dataElementStore.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 1, cdes.size() );
        assertEquals("DataElementX", cdes.iterator().next().getName());
        
        dataElements.add(deC);
        cdes = dataElementStore.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 2, cdes.size() );

        Iterator<CalculatedDataElement> iterator = cdes.iterator();
        assertEquals( iterator.next().getName(), "DataElementX" );
        assertEquals( iterator.next().getName(), "DataElementY" );
        
        //Make sure the results are unique
        dataElements.add(deB);
        cdes = dataElementStore.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 2, cdes.size() );

        iterator = cdes.iterator();
        assertEquals( iterator.next().getName(), "DataElementX" );
        assertEquals( iterator.next().getName(), "DataElementY" );

        //Check that no other data elements are returned
        dataElements.add(deE);
        cdes = dataElementStore.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 2, cdes.size() );

    }
    
    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    public void testAddDataElementGroup()
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );
        DataElementGroup dataElementGroupD = new DataElementGroup( "DataElementGroupA" );
        
        int idA = dataElementStore.addDataElementGroup( dataElementGroupA );
        int idB = dataElementStore.addDataElementGroup( dataElementGroupB );
        int idC = dataElementStore.addDataElementGroup( dataElementGroupC );

        try
        {
            // Should give unique constraint violation
            dataElementStore.addDataElementGroup( dataElementGroupD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        dataElementGroupA = dataElementStore.getDataElementGroup( idA );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupA", dataElementGroupA.getName() );

        dataElementGroupB = dataElementStore.getDataElementGroup( idB );
        assertNotNull( dataElementGroupB );
        assertEquals( idB, dataElementGroupB.getId() );
        assertEquals( "DataElementGroupB", dataElementGroupB.getName() );

        dataElementGroupC = dataElementStore.getDataElementGroup( idC );
        assertNotNull( dataElementGroupC );
        assertEquals( idC, dataElementGroupC.getId() );
        assertEquals( "DataElementGroupC", dataElementGroupC.getName() );
    }

    public void testUpdateDataElementGroup()
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );

        int idA = dataElementStore.addDataElementGroup( dataElementGroupA );
        int idB = dataElementStore.addDataElementGroup( dataElementGroupB );
        int idC = dataElementStore.addDataElementGroup( dataElementGroupC );

        dataElementGroupA = dataElementStore.getDataElementGroup( idA );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupA", dataElementGroupA.getName() );

        dataElementGroupA.setName( "DataElementGroupAA" );
        dataElementStore.updateDataElementGroup( dataElementGroupA );

        dataElementGroupA = dataElementStore.getDataElementGroup( idA );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupAA", dataElementGroupA.getName() );

        dataElementGroupB = dataElementStore.getDataElementGroup( idB );
        assertNotNull( dataElementGroupB );
        assertEquals( idB, dataElementGroupB.getId() );
        assertEquals( "DataElementGroupB", dataElementGroupB.getName() );

        dataElementGroupC = dataElementStore.getDataElementGroup( idC );
        assertNotNull( dataElementGroupC );
        assertEquals( idC, dataElementGroupC.getId() );
        assertEquals( "DataElementGroupC", dataElementGroupC.getName() );
    }

    public void testDeleteAndGetDataElementGroup()
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );
        DataElementGroup dataElementGroupD = new DataElementGroup( "DataElementGroupD" );

        int idA = dataElementStore.addDataElementGroup( dataElementGroupA );
        int idB = dataElementStore.addDataElementGroup( dataElementGroupB );
        int idC = dataElementStore.addDataElementGroup( dataElementGroupC );
        int idD = dataElementStore.addDataElementGroup( dataElementGroupD );

        assertNotNull( dataElementStore.getDataElementGroup( idA ) );
        assertNotNull( dataElementStore.getDataElementGroup( idB ) );
        assertNotNull( dataElementStore.getDataElementGroup( idC ) );
        assertNotNull( dataElementStore.getDataElementGroup( idD ) );

        dataElementStore.deleteDataElementGroup( dataElementGroupA );
        assertNull( dataElementStore.getDataElementGroup( idA ) );
        assertNotNull( dataElementStore.getDataElementGroup( idB ) );
        assertNotNull( dataElementStore.getDataElementGroup( idC ) );
        assertNotNull( dataElementStore.getDataElementGroup( idD ) );

        dataElementStore.deleteDataElementGroup( dataElementGroupB );
        assertNull( dataElementStore.getDataElementGroup( idA ) );
        assertNull( dataElementStore.getDataElementGroup( idB ) );
        assertNotNull( dataElementStore.getDataElementGroup( idC ) );
        assertNotNull( dataElementStore.getDataElementGroup( idD ) );

        dataElementStore.deleteDataElementGroup( dataElementGroupC );
        assertNull( dataElementStore.getDataElementGroup( idA ) );
        assertNull( dataElementStore.getDataElementGroup( idB ) );
        assertNull( dataElementStore.getDataElementGroup( idC ) );
        assertNotNull( dataElementStore.getDataElementGroup( idD ) );

        dataElementStore.deleteDataElementGroup( dataElementGroupD );
        assertNull( dataElementStore.getDataElementGroup( idA ) );
        assertNull( dataElementStore.getDataElementGroup( idB ) );
        assertNull( dataElementStore.getDataElementGroup( idC ) );
        assertNull( dataElementStore.getDataElementGroup( idD ) );
    }

    public void testGetDataElementGroupByUUID()
    {
        String uuid = UUIdUtils.getUUId();
        
        DataElementGroup groupA = new DataElementGroup( "groupA" );
        groupA.setUuid( uuid );
        
        dataElementStore.addDataElementGroup( groupA );
        
        groupA = dataElementStore.getDataElementGroup( uuid );
        
        assertNotNull( groupA );
        assertEquals( groupA.getUuid(), uuid );
    }

    public void testGetDataElementGroupByName()
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        int idA = dataElementStore.addDataElementGroup( dataElementGroupA );
        int idB = dataElementStore.addDataElementGroup( dataElementGroupB );

        assertNotNull( dataElementStore.getDataElementGroup( idA ) );
        assertNotNull( dataElementStore.getDataElementGroup( idB ) );

        dataElementGroupA = dataElementStore.getDataElementGroupByName( "DataElementGroupA" );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupA", dataElementGroupA.getName() );

        dataElementGroupB = dataElementStore.getDataElementGroupByName( "DataElementGroupB" );
        assertNotNull( dataElementGroupB );
        assertEquals( idB, dataElementGroupB.getId() );
        assertEquals( "DataElementGroupB", dataElementGroupB.getName() );

        DataElementGroup dataElementGroupC = dataElementStore.getDataElementGroupByName( "DataElementGroupC" );
        assertNull( dataElementGroupC );
    }

    public void testGetAllDataElementGroups()
        throws Exception
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        dataElementStore.addDataElementGroup( dataElementGroupA );
        dataElementStore.addDataElementGroup( dataElementGroupB );

        Collection<DataElementGroup> groups = dataElementStore.getAllDataElementGroups();
        
        assertTrue( groups.size() == 2 );
        assertTrue( groups.contains( dataElementGroupA ) );
        assertTrue( groups.contains( dataElementGroupB ) );
    }

}
