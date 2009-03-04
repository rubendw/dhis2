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
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.hierarchy.HierarchyViolationException;
import org.hisp.dhis.system.util.UUIdUtils;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Kristian Nordal
 * @version $Id: DataElementServiceTest.java 5742 2008-09-26 11:37:35Z larshelg $
 */
public class DataElementServiceTest
    extends DhisSpringTest
{
    private DataElementService dataElementService;
    
    private TransactionManager transactionManager;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        dataElementService = (DataElementService) getBean( DataElementService.ID );

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
        assertEquals( DataElement.TYPE_INT, dataElement.getType() );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddDataElement()
        throws Exception
    {
        transactionManager.enter();

        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'A' );

        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );
        int idC = dataElementService.addDataElement( dataElementC );

        transactionManager.leave();

        try
        {
            // Should give unique constraint violation
            dataElementService.addDataElement( dataElementD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        transactionManager.enter();

        dataElementA = dataElementService.getDataElement( idA );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementService.getDataElement( idB );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        dataElementC = dataElementService.getDataElement( idC );
        assertNotNull( dataElementC );
        assertEquals( idC, dataElementC.getId() );
        assertEquals( 'C', dataElementC );

        transactionManager.leave();
    }

    public void testUpdateDataElement()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        int idA = dataElementService.addDataElement( dataElementA );
        dataElementA = dataElementService.getDataElement( idA );
        assertEquals( DataElement.TYPE_INT, dataElementA.getType() );

        dataElementA.setType( DataElement.TYPE_BOOL );
        dataElementService.updateDataElement( dataElementA );
        dataElementA = dataElementService.getDataElement( idA );
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

        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );
        int idC = dataElementService.addDataElement( dataElementC );
        int idD = dataElementService.addDataElement( dataElementD );

        assertNotNull( dataElementService.getDataElement( idA ) );
        assertNotNull( dataElementService.getDataElement( idB ) );
        assertNotNull( dataElementService.getDataElement( idC ) );
        assertNotNull( dataElementService.getDataElement( idD ) );

        transactionManager.enter();

        dataElementA.setParent( dataElementB );
        dataElementB.getChildren().add( dataElementA );
        dataElementService.updateDataElement( dataElementA );

        transactionManager.leave();
        transactionManager.enter();

        dataElementA = dataElementService.getDataElement( idA );
        dataElementB = dataElementService.getDataElement( idB );

        try
        {
            dataElementService.deleteDataElement( dataElementB );
            fail();
        }
        catch ( HierarchyViolationException e )
        {
            // Expected
        }

        transactionManager.leave();
        transactionManager.enter();

        dataElementA.setParent( null );
        dataElementService.updateDataElement( dataElementA );

        transactionManager.leave();
        transactionManager.enter();

        dataElementA = dataElementService.getDataElement( idA );
        dataElementB = dataElementService.getDataElement( idB );
        dataElementC = dataElementService.getDataElement( idC );
        dataElementD = dataElementService.getDataElement( idD );

        dataElementService.deleteDataElement( dataElementA );
        assertNull( dataElementService.getDataElement( idA ) );
        assertNotNull( dataElementService.getDataElement( idB ) );
        assertNotNull( dataElementService.getDataElement( idC ) );
        assertNotNull( dataElementService.getDataElement( idD ) );

        dataElementService.deleteDataElement( dataElementB );
        assertNull( dataElementService.getDataElement( idA ) );
        assertNull( dataElementService.getDataElement( idB ) );
        assertNotNull( dataElementService.getDataElement( idC ) );
        assertNotNull( dataElementService.getDataElement( idD ) );

        dataElementService.deleteDataElement( dataElementC );
        assertNull( dataElementService.getDataElement( idA ) );
        assertNull( dataElementService.getDataElement( idB ) );
        assertNull( dataElementService.getDataElement( idC ) );
        assertNotNull( dataElementService.getDataElement( idD ) );

        dataElementService.deleteDataElement( dataElementD );
        assertNull( dataElementService.getDataElement( idA ) );
        assertNull( dataElementService.getDataElement( idB ) );
        assertNull( dataElementService.getDataElement( idC ) );
        assertNull( dataElementService.getDataElement( idD ) );

        transactionManager.leave();
    }

    public void testGetDataElementByUUID()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        int idA = dataElementService.addDataElement( dataElementA );
        
        dataElementA = dataElementService.getDataElement( idA );
        String uuid = dataElementA.getUuid();
        
        DataElement dataElementB = dataElementService.getDataElement( uuid );
        
        assertEquals( dataElementA, dataElementB );
    }

    public void testGetDataElementByName()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );

        dataElementA = dataElementService.getDataElementByName( "DataElementA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementService.getDataElementByName( "DataElementB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementService.getDataElementByName( "DataElementC" );
        assertNull( dataElementC );
    }

    public void testGetDataElementByAlternativeName()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );

        dataElementA = dataElementService.getDataElementByAlternativeName( "AlternativeNameA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementService.getDataElementByAlternativeName( "AlternativeNameB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementService.getDataElementByAlternativeName( "AlternativeNameC" );
        assertNull( dataElementC );
    }

    public void testGetDataElementByShortName()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );

        dataElementA = dataElementService.getDataElementByShortName( "DEA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementService.getDataElementByShortName( "DEB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementService.getDataElementByShortName( "DEC" );
        assertNull( dataElementC );
    }

    public void testGetDataElementByCode()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );

        dataElementA = dataElementService.getDataElementByCode( "CodeA" );
        assertNotNull( dataElementA );
        assertEquals( idA, dataElementA.getId() );
        assertEquals( 'A', dataElementA );

        dataElementB = dataElementService.getDataElementByCode( "CodeB" );
        assertNotNull( dataElementB );
        assertEquals( idB, dataElementB.getId() );
        assertEquals( 'B', dataElementB );

        DataElement dataElementC = dataElementService.getDataElementByCode( "CodeC" );
        assertNull( dataElementC );
    }

    public void testGetAllDataElements()
        throws Exception
    {
        assertEquals( 0, dataElementService.getAllDataElements().size() );

        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );

        Collection<DataElement> dataElementsRef = new HashSet<DataElement>();
        dataElementsRef.add( dataElementA );
        dataElementsRef.add( dataElementB );
        dataElementsRef.add( dataElementC );
        dataElementsRef.add( dataElementD );

        transactionManager.enter();

        Collection<DataElement> dataElements = dataElementService.getAllDataElements();
        assertNotNull( dataElements );
        assertEquals( dataElementsRef.size(), dataElements.size() );
        assertTrue( dataElements.containsAll( dataElementsRef ) );

        transactionManager.leave();
    }

    public void testGetAggregateableDataElements()
    {
        assertEquals( 0, dataElementService.getAggregateableDataElements().size() );

        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );
        
        dataElementA.setType( DataElement.TYPE_INT );
        dataElementB.setType( DataElement.TYPE_BOOL );
        dataElementC.setType( DataElement.TYPE_STRING );
        dataElementD.setType( DataElement.TYPE_INT );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );

        Collection<DataElement> dataElementsRef = new HashSet<DataElement>();
        dataElementsRef.add( dataElementA );
        dataElementsRef.add( dataElementB );
        dataElementsRef.add( dataElementD );

        transactionManager.enter();

        Collection<DataElement> dataElements = dataElementService.getAggregateableDataElements();
        assertNotNull( dataElements );
        assertEquals( dataElementsRef.size(), dataElements.size() );
        assertTrue( dataElements.containsAll( dataElementsRef ) );

        transactionManager.leave();
    }
    
    public void testGetAllActiveDataElements()
        throws Exception
    {
        assertEquals( 0, dataElementService.getAllActiveDataElements().size() );

        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setActive( true );
        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setActive( true );
        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setActive( true );
        DataElement dataElementD = createDataElement( 'D' );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );

        Collection<DataElement> dataElementsRef = new HashSet<DataElement>();
        dataElementsRef.add( dataElementA );
        dataElementsRef.add( dataElementB );
        dataElementsRef.add( dataElementC );

        transactionManager.enter();

        assertEquals( dataElementsRef.size() + 1, dataElementService.getAllDataElements().size() );

        Collection<DataElement> dataElements = dataElementService.getAllActiveDataElements();
        assertNotNull( dataElements );
        assertEquals( dataElementsRef.size(), dataElements.size() );
        assertTrue( dataElements.containsAll( dataElementsRef ) );

        transactionManager.leave();
    }

    public void testGetDataElementsByAggregationOperator()
        throws Exception
    {
        assertEquals( 0, dataElementService.getDataElementsByAggregationOperator(
            DataElement.AGGREGATION_OPERATOR_AVERAGE ).size() );
        assertEquals( 0, dataElementService.getDataElementsByAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM )
            .size() );

        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        DataElement dataElementD = createDataElement( 'D' );
        dataElementD.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );

        assertEquals( 1, dataElementService.getDataElementsByAggregationOperator(
            DataElement.AGGREGATION_OPERATOR_AVERAGE ).size() );
        assertEquals( 3, dataElementService.getDataElementsByAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM )
            .size() );
    }

    public void testGetDataElementsByType()
    {
        assertEquals( 0, dataElementService.getDataElementsByType( DataElement.TYPE_INT ).size() );
        assertEquals( 0, dataElementService.getDataElementsByType( DataElement.TYPE_BOOL ).size() );

        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setType( DataElement.TYPE_INT );
        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setType( DataElement.TYPE_BOOL );
        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setType( DataElement.TYPE_BOOL );
        DataElement dataElementD = createDataElement( 'D' );
        dataElementD.setType( DataElement.TYPE_BOOL );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );

        assertEquals( 1, dataElementService.getDataElementsByType( DataElement.TYPE_INT ).size() );
        assertEquals( 3, dataElementService.getDataElementsByType( DataElement.TYPE_BOOL ).size() );
    }

    // -------------------------------------------------------------------------
    // CalculatedDataElements
    // -------------------------------------------------------------------------

    public void testCalculatedDataElements()
        throws Exception
    {
        DataElement deA = createDataElement('A');
        DataElement deB = createDataElement('B');
        DataElement deC = createDataElement('C');
        DataElement deD = createDataElement('D');
        DataElement deE = createDataElement('E');

        int deIdA = dataElementService.addDataElement(deA);
        int deIdB = dataElementService.addDataElement(deB);
        int deIdC = dataElementService.addDataElement(deC);
        int deIdD = dataElementService.addDataElement(deD);
        dataElementService.addDataElement(deE);
        
        CalculatedDataElement cdeX = (CalculatedDataElement) setDataElementFields( new CalculatedDataElement (), 'X' );
        CalculatedDataElement cdeY = (CalculatedDataElement) setDataElementFields( new CalculatedDataElement (), 'Y' );

        Set<DataElement> dataElementsX = new HashSet<DataElement> ();
        dataElementsX.add(deA);
        dataElementsX.add(deB);
        Expression expressionX = new Expression ( "["+deIdA+"] * 2 + ["+deIdB+"] * 3", "foo", dataElementsX );
        cdeX.setExpression(expressionX);
        cdeX.setSaved(true);
        dataElementService.addDataElement(cdeX);
        
        Set<DataElement> dataElementsY = new HashSet<DataElement> ();
        dataElementsY.add(deC);
        dataElementsY.add(deD);
        Expression expressionY = new Expression ( "["+deIdC+"] * 2 + ["+deIdD+"] * 3", "foo", dataElementsY );
        cdeY.setExpression(expressionY);
        cdeY.setSaved(true);
        dataElementService.addDataElement(cdeY);
        
        Collection<CalculatedDataElement> cdes = dataElementService.getAllCalculatedDataElements();
        assertEquals( 2, cdes.size() );
        
        //CalculatedDataElement cde;
        CalculatedDataElement cde = dataElementService.getCalculatedDataElementByDataElement( deA );
        assertNotNull(cde);
        assertEquals("DataElementX", cde.getName() );
        
        cde = dataElementService.getCalculatedDataElementByDataElement( deE );
        assertNull(cde);
        
        Set<DataElement> dataElements = new HashSet<DataElement> ();
        dataElements.add(deA);
        cdes = dataElementService.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 1, cdes.size() );
        assertEquals("DataElementX", cdes.iterator().next().getName());
        
        dataElements.add(deC);
        cdes = dataElementService.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 2, cdes.size() );

        Iterator<CalculatedDataElement> iterator = cdes.iterator();
        assertEquals( iterator.next().getName(), "DataElementX" );
        assertEquals( iterator.next().getName(), "DataElementY" );
        
        //Make sure the results are unique
        dataElements.add(deB);
        cdes = dataElementService.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 2, cdes.size() );

        iterator = cdes.iterator();
        assertEquals( iterator.next().getName(), "DataElementX" );
        assertEquals( iterator.next().getName(), "DataElementY" );

        //Check that no other data elements are returned
        dataElements.add(deE);
        cdes = dataElementService.getCalculatedDataElementsByDataElements( dataElements );
        assertEquals( 2, cdes.size() );
        
        Map<DataElement,Integer> factorMap = dataElementService.getDataElementFactors(cdeX);
        assertEquals( 2, factorMap.size() );
        assertTrue(factorMap.keySet().contains(deA));
        assertEquals( new Integer(2), factorMap.get(deA));
        assertTrue(factorMap.keySet().contains(deB));
        assertEquals( new Integer(3), factorMap.get(deB));

    }
    
    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    public void testAddDataElementGroup()
        throws Exception
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );
        DataElementGroup dataElementGroupD = new DataElementGroup( "DataElementGroupA" );

        int idA = dataElementService.addDataElementGroup( dataElementGroupA );
        int idB = dataElementService.addDataElementGroup( dataElementGroupB );
        int idC = dataElementService.addDataElementGroup( dataElementGroupC );

        try
        {
            // Should give unique constraint violation
            dataElementService.addDataElementGroup( dataElementGroupD );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        dataElementGroupA = dataElementService.getDataElementGroup( idA );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupA", dataElementGroupA.getName() );

        dataElementGroupB = dataElementService.getDataElementGroup( idB );
        assertNotNull( dataElementGroupB );
        assertEquals( idB, dataElementGroupB.getId() );
        assertEquals( "DataElementGroupB", dataElementGroupB.getName() );

        dataElementGroupC = dataElementService.getDataElementGroup( idC );
        assertNotNull( dataElementGroupC );
        assertEquals( idC, dataElementGroupC.getId() );
        assertEquals( "DataElementGroupC", dataElementGroupC.getName() );
    }

    public void testUpdateDataElementGroup()
        throws Exception
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );

        int idA = dataElementService.addDataElementGroup( dataElementGroupA );
        int idB = dataElementService.addDataElementGroup( dataElementGroupB );
        int idC = dataElementService.addDataElementGroup( dataElementGroupC );

        dataElementGroupA = dataElementService.getDataElementGroup( idA );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupA", dataElementGroupA.getName() );

        dataElementGroupA.setName( "DataElementGroupAA" );
        dataElementService.updateDataElementGroup( dataElementGroupA );

        dataElementGroupA = dataElementService.getDataElementGroup( idA );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupAA", dataElementGroupA.getName() );

        dataElementGroupB = dataElementService.getDataElementGroup( idB );
        assertNotNull( dataElementGroupB );
        assertEquals( idB, dataElementGroupB.getId() );
        assertEquals( "DataElementGroupB", dataElementGroupB.getName() );

        dataElementGroupC = dataElementService.getDataElementGroup( idC );
        assertNotNull( dataElementGroupC );
        assertEquals( idC, dataElementGroupC.getId() );
        assertEquals( "DataElementGroupC", dataElementGroupC.getName() );
    }

    public void testDeleteAndGetDataElementGroup()
        throws Exception
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );
        DataElementGroup dataElementGroupD = new DataElementGroup( "DataElementGroupD" );

        int idA = dataElementService.addDataElementGroup( dataElementGroupA );
        int idB = dataElementService.addDataElementGroup( dataElementGroupB );
        int idC = dataElementService.addDataElementGroup( dataElementGroupC );
        int idD = dataElementService.addDataElementGroup( dataElementGroupD );

        assertNotNull( dataElementService.getDataElementGroup( idA ) );
        assertNotNull( dataElementService.getDataElementGroup( idB ) );
        assertNotNull( dataElementService.getDataElementGroup( idC ) );
        assertNotNull( dataElementService.getDataElementGroup( idD ) );

        dataElementService.deleteDataElementGroup( dataElementGroupA );
        assertNull( dataElementService.getDataElementGroup( idA ) );
        assertNotNull( dataElementService.getDataElementGroup( idB ) );
        assertNotNull( dataElementService.getDataElementGroup( idC ) );
        assertNotNull( dataElementService.getDataElementGroup( idD ) );

        dataElementService.deleteDataElementGroup( dataElementGroupB );
        assertNull( dataElementService.getDataElementGroup( idA ) );
        assertNull( dataElementService.getDataElementGroup( idB ) );
        assertNotNull( dataElementService.getDataElementGroup( idC ) );
        assertNotNull( dataElementService.getDataElementGroup( idD ) );

        dataElementService.deleteDataElementGroup( dataElementGroupC );
        assertNull( dataElementService.getDataElementGroup( idA ) );
        assertNull( dataElementService.getDataElementGroup( idB ) );
        assertNull( dataElementService.getDataElementGroup( idC ) );
        assertNotNull( dataElementService.getDataElementGroup( idD ) );

        dataElementService.deleteDataElementGroup( dataElementGroupD );
        assertNull( dataElementService.getDataElementGroup( idA ) );
        assertNull( dataElementService.getDataElementGroup( idB ) );
        assertNull( dataElementService.getDataElementGroup( idC ) );
        assertNull( dataElementService.getDataElementGroup( idD ) );
    }

    public void testGetDataElementGroupByUUID()
        throws Exception
    {
        DataElementGroup groupA = new DataElementGroup( "groupA" );
        int idA = dataElementService.addDataElementGroup( groupA );
        
        groupA = dataElementService.getDataElementGroup( idA );
        String uuid = groupA.getUuid();
        
        DataElementGroup groupB = dataElementService.getDataElementGroup( uuid );
        
        assertEquals( groupA, groupB );
    }

    public void testGetDataElementGroupByName()
        throws Exception
    {
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        int idA = dataElementService.addDataElementGroup( dataElementGroupA );
        int idB = dataElementService.addDataElementGroup( dataElementGroupB );

        assertNotNull( dataElementService.getDataElementGroup( idA ) );
        assertNotNull( dataElementService.getDataElementGroup( idB ) );

        dataElementGroupA = dataElementService.getDataElementGroupByName( "DataElementGroupA" );
        assertNotNull( dataElementGroupA );
        assertEquals( idA, dataElementGroupA.getId() );
        assertEquals( "DataElementGroupA", dataElementGroupA.getName() );

        dataElementGroupB = dataElementService.getDataElementGroupByName( "DataElementGroupB" );
        assertNotNull( dataElementGroupB );
        assertEquals( idB, dataElementGroupB.getId() );
        assertEquals( "DataElementGroupB", dataElementGroupB.getName() );

        DataElementGroup dataElementGroupC = dataElementService.getDataElementGroupByName( "DataElementGroupC" );
        assertNull( dataElementGroupC );
    }

    public void testGetAllDataElementGroups()
        throws Exception
    {
    }
    
    public void testGetGroupsContainingDataElement() throws Exception
    {
        transactionManager.enter();
        
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );
        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );
        
        transactionManager.leave();
        transactionManager.enter();
        
        DataElementGroup dataElementGroupA = new DataElementGroup( "DataElementGroupA" );
        DataElementGroup dataElementGroupB = new DataElementGroup( "DataElementGroupB" );
        DataElementGroup dataElementGroupC = new DataElementGroup( "DataElementGroupC" );
        DataElementGroup dataElementGroupD = new DataElementGroup( "DataElementGroupD" );
        
        Set<DataElement> membersA = new HashSet<DataElement>();
        membersA.add( dataElementA );
        membersA.add( dataElementB );
        membersA.add( dataElementC );
        
        Set<DataElement> membersB = new HashSet<DataElement>();
        membersB.add( dataElementC );
        membersB.add( dataElementD );

        dataElementGroupA.setMembers( membersA );
        dataElementGroupB.setMembers( membersB );
        dataElementGroupC.setMembers( membersA );
        dataElementGroupD.setMembers( membersB );
        
        dataElementService.addDataElementGroup( dataElementGroupA );
        dataElementService.addDataElementGroup( dataElementGroupB );
        dataElementService.addDataElementGroup( dataElementGroupC );
        dataElementService.addDataElementGroup( dataElementGroupD );
        
        transactionManager.leave();
        
        Collection<DataElementGroup> groupsA = dataElementService.getGroupsContainingDataElement( dataElementA );
        
        assertTrue( groupsA.size() == 2 );
        assertTrue( groupsA.contains( dataElementGroupA ) );
        assertTrue( groupsA.contains( dataElementGroupC ) );        

        Collection<DataElementGroup> groupsB = dataElementService.getGroupsContainingDataElement( dataElementC );
        
        assertTrue( groupsB.size() == 4 );
    }
}
