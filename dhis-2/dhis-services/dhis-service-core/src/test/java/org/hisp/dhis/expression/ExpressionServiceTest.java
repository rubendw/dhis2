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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;

import static org.hisp.dhis.expression.Expression.SEPARATOR;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ExpressionServiceTest
    extends DhisConvenienceTest
{
    private ExpressionService expressionService;

    private DataElementCategoryComboService categoryComboService;
        
    private SourceStore sourceStore;
    
    private DataElement dataElementA;
    private DataElement dataElementB;
    private DataElement dataElementC;
    private DataElement dataElementD;
    
    private Period period;
    
    private Source source;
    
    private int dataElementIdA;    
    private int dataElementIdB;    
    private int dataElementIdC;    
    private int dataElementIdD;

    private DataElementCategoryCombo categoryCombo;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private int categoryOptionComboId;
    
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
        expressionService = (ExpressionService) getBean( ExpressionService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );

        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        dataValueService = (DataValueService) getBean( DataValueService.ID );
        
        sourceStore = (SourceStore) getBean( SourceStore.ID );
        
        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        dataElementC = createDataElement( 'C' );
        dataElementD = createDataElement( 'D' );        
        
        dataElementIdA = dataElementService.addDataElement( dataElementA );
        dataElementIdB = dataElementService.addDataElement( dataElementB );
        dataElementIdC = dataElementService.addDataElement( dataElementC );
        dataElementIdD = dataElementService.addDataElement( dataElementD );
        
        categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );             
        
        categoryOptionCombo = categoryCombo.getOptionCombos().iterator().next();
        
        categoryOptionComboId = categoryOptionCombo.getId();
        
        period = new Period( PeriodType.getAvailablePeriodTypes().iterator().next(), new Date(), new Date() );
        
        source = new DummySource( "SourceA" );
        
        sourceStore.addSource( source );
        
        expressionA = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "] + [" + dataElementIdB + SEPARATOR + categoryOptionComboId + "]";
        expressionB = "[" + dataElementIdC + SEPARATOR + categoryOptionComboId + "] - [" + dataElementIdD + SEPARATOR + categoryOptionComboId + "]";
        
        descriptionA = "Expression A";
        descriptionB = "Expression B";
                
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        dataElements.add( dataElementC );
        dataElements.add( dataElementD );

        dataValueService.addDataValue( createDataValue( dataElementA, period, source, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, period, source, "5", categoryOptionCombo ) );        
    }

    // -------------------------------------------------------------------------
    // Business logic tests
    // -------------------------------------------------------------------------
    
    public void testGetExpressionValue()
    {
        Expression expression = new Expression( expressionA, descriptionA, dataElements );
        
        Double value = expressionService.getExpressionValue( expression, period, source );
        
        assertEquals( value, 15.0 );
        
        expression = new Expression( expressionB, descriptionB, dataElements );

        value = expressionService.getExpressionValue( expression, period, source );
        
        assertEquals( value, 0.0 );
    }
    
    public void testGetDataElementsInExpression()
    {
        Set<DataElement> dataElements = expressionService.getDataElementsInExpression( expressionA );
        
        assertTrue( dataElements.size() == 2 );
        assertTrue( dataElements.contains( dataElementA ) );
        assertTrue( dataElements.contains( dataElementB ) );        
    }
    
    public void testGetOperandsInExpression()
    {
        Set<String> operands = expressionService.getOperandsInExpression( expressionA );
        
        assertNotNull( operands );
        assertEquals( 2, operands.size() );
        
        assertTrue( operands.contains( dataElementIdA + SEPARATOR + categoryOptionComboId ) );
        assertTrue( operands.contains( dataElementIdB + SEPARATOR + categoryOptionComboId ) );
    }
    
    public void testExpressionIsValid()
    {
    	assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionA ) );
    	assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionB ) );
    	
    	expressionA = "[" + "foo" + SEPARATOR + categoryOptionComboId + "] + 12";
    	
    	assertEquals( ExpressionService.DATAELEMENT_ID_NOT_NUMERIC, expressionService.expressionIsValid( expressionA ) );
    	
    	expressionA = "[" + dataElementIdA + SEPARATOR + "foo" + "] + 12";
    	
    	assertEquals( ExpressionService.CATEGORYOPTIONCOMBO_ID_NOT_NUMERIC, expressionService.expressionIsValid( expressionA ) );
    	
    	expressionA = "[" + 999 + SEPARATOR + categoryOptionComboId + "] + 12";
    	
    	assertEquals( ExpressionService.DATAELEMENT_DOES_NOT_EXIST, expressionService.expressionIsValid( expressionA ) );
    	
    	expressionA = "[" + dataElementIdA + SEPARATOR + 999 + "] + 12";
    	
    	assertEquals( ExpressionService.CATEGORYOPTIONCOMBO_DOES_NOT_EXIST, expressionService.expressionIsValid( expressionA ) );
        
        expressionA = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "] + ( 12";
        
        assertEquals( ExpressionService.EXPRESSION_NOT_WELL_FORMED, expressionService.expressionIsValid( expressionA ) );
        
        expressionA = "12 x 4";        

        assertEquals( ExpressionService.EXPRESSION_NOT_WELL_FORMED, expressionService.expressionIsValid( expressionA ) );        
    }
    
    public void testGetExpressionDescription()
    {
        String description = expressionService.getExpressionDescription( expressionA );        
        
        assertEquals( description, "DataElementA + DataElementB" );
    }
    
    public void testGenerateExpression()
    {
        String expression = expressionService.generateExpression( expressionA, period, source );
        
        assertEquals( expression, "10 + 5" );
        
        expression = expressionService.generateExpression( expressionB, period, source );
        
        assertEquals( expression, "0 - 0" );
    }
    
    // -------------------------------------------------------------------------
    // CRUD tests
    // -------------------------------------------------------------------------
    
    public void testAddGetExpression()
    {
        Expression expression = new Expression( expressionA, descriptionA, dataElements );
        
        int id = expressionService.addExpression( expression );
        
        expression = expressionService.getExpression( id );
        
        assertEquals( expression.getExpression(), expressionA );
        assertEquals( expression.getDescription(), descriptionA );
        assertEquals( expression.getDataElementsInExpression(), dataElements );
    }
    
    public void testUpdateExpression()
    {
        Expression expression = new Expression( expressionA, descriptionA, dataElements );
        
        int id = expressionService.addExpression( expression );
        
        expression = expressionService.getExpression( id );
        
        assertEquals( expression.getExpression(), expressionA );
        assertEquals( expression.getDescription(), descriptionA );
        
        expression.setExpression( expressionB );
        expression.setDescription( descriptionB );

        expressionService.updateExpression( expression );

        expression = expressionService.getExpression( id );
        
        assertEquals( expression.getExpression(), expressionB );
        assertEquals( expression.getDescription(), descriptionB );
    }
    
    public void testDeleteExpression()
    {
        Expression exprA = new Expression( expressionA, descriptionA, dataElements );
        Expression exprB = new Expression( expressionB, descriptionB, dataElements );
        
        int idA = expressionService.addExpression( exprA );
        int idB = expressionService.addExpression( exprB );
        
        assertNotNull( expressionService.getExpression( idA ) );
        assertNotNull( expressionService.getExpression( idB ) );
        
        expressionService.deleteExpression( exprA );

        assertNull( expressionService.getExpression( idA ) );
        assertNotNull( expressionService.getExpression( idB ) );

        expressionService.deleteExpression( exprB );

        assertNull( expressionService.getExpression( idA ) );
        assertNull( expressionService.getExpression( idB ) );
    }
    
    public void testGetAllExpressions()
    {
        Expression exprA = new Expression( expressionA, descriptionA, dataElements );
        Expression exprB = new Expression( expressionB, descriptionB, dataElements );
        
        expressionService.addExpression( exprA );
        expressionService.addExpression( exprB );
        
        Collection<Expression> expressions = expressionService.getAllExpressions();
                
        assertTrue( expressions.size() == 2 );
        assertTrue( expressions.contains( exprA ) );
        assertTrue( expressions.contains( exprB ) );
    }
}
