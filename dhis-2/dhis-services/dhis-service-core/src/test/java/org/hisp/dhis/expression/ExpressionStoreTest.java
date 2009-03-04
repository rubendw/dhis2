package org.hisp.dhis.expression;

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
import java.util.Set;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ExpressionStoreTest
    extends DhisConvenienceTest
{
    private ExpressionStore expressionStore;
    
    private int dataElementIdA;    
    private int dataElementIdB;    
    private int dataElementIdC;    
    private int dataElementIdD;
    
    private String expressionA;
    private String expressionB;
    
    private String descriptionA;
    private String descriptionB;
    
    private Set<DataElement> dataElements = new HashSet<DataElement>();
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
        throws Exception
    {
        expressionStore = (ExpressionStore) getBean( ExpressionStore.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );        
        
        dataElementIdA = dataElementService.addDataElement( dataElementA );
        dataElementIdB = dataElementService.addDataElement( dataElementB );
        dataElementIdC = dataElementService.addDataElement( dataElementC );
        dataElementIdD = dataElementService.addDataElement( dataElementD );
        
        expressionA = "[" + dataElementIdA + "] + [" + dataElementIdB + "]";
        expressionB = "[" + dataElementIdC + "] - [" + dataElementIdD + "]";
        
        descriptionA = "Expression A";
        descriptionB = "Expression B";
                
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        dataElements.add( dataElementC );
        dataElements.add( dataElementD );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testAddGetExpression()
    {
        Expression expr = new Expression( expressionA, descriptionA, dataElements );
        
        int id = expressionStore.addExpression( expr );
        
        expr = expressionStore.getExpression( id );
        
        assertEquals( expr.getExpression(), expressionA );
        assertEquals( expr.getDescription(), descriptionA );
        assertEquals( expr.getDataElementsInExpression(), dataElements );
    }
    
    public void testUpdateExpression()
    {
        Expression expr = new Expression( expressionA, descriptionA, dataElements );
        
        int id = expressionStore.addExpression( expr );
        
        expr = expressionStore.getExpression( id );
        
        assertEquals( expr.getExpression(), expressionA );
        assertEquals( expr.getDescription(), descriptionA );
        
        expr.setExpression( expressionB );
        expr.setDescription( descriptionB );

        expressionStore.updateExpression( expr );

        expr = expressionStore.getExpression( id );
        
        assertEquals( expr.getExpression(), expressionB );
        assertEquals( expr.getDescription(), descriptionB );
    }
    
    public void testDeleteExpression()
    {
        Expression exprA = new Expression( expressionA, descriptionA, dataElements );
        Expression exprB = new Expression( expressionB, descriptionB, dataElements );
        
        int idA = expressionStore.addExpression( exprA );
        int idB = expressionStore.addExpression( exprB );
        
        assertNotNull( expressionStore.getExpression( idA ) );
        assertNotNull( expressionStore.getExpression( idB ) );
        
        expressionStore.deleteExpression( exprA );

        assertNull( expressionStore.getExpression( idA ) );
        assertNotNull( expressionStore.getExpression( idB ) );

        expressionStore.deleteExpression( exprB );

        assertNull( expressionStore.getExpression( idA ) );
        assertNull( expressionStore.getExpression( idB ) );
    }
    
    public void testGetAllExpressions()
    {
        Expression exprA = new Expression( expressionA, descriptionA, dataElements );
        Expression exprB = new Expression( expressionB, descriptionB, dataElements );
        
        expressionStore.addExpression( exprA );
        expressionStore.addExpression( exprB );
        
        Collection<Expression> expressions = expressionStore.getAllExpressions();
                
        assertTrue( expressions.size() == 2 );
        assertTrue( expressions.contains( exprA ) );
        assertTrue( expressions.contains( exprB ) );
    }
}
