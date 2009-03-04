package org.hisp.dhis.validation;

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
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;

/**
 * @author Lars Helge Overland
 * @version $Id: ValidationRuleStoreTest.java 3679 2007-10-22 18:25:18Z larshelg $
 */
public class ValidationRuleStoreTest
    extends DhisConvenienceTest
{    
    private ValidationRuleStore validationRuleStore;

    private ExpressionService expressionService;
    
    private DataElement dataElementA;
    private DataElement dataElementB;
    private DataElement dataElementC;
    private DataElement dataElementD;

    private Set<DataElement> dataElements;

    private Expression expressionA;
    private Expression expressionB;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
        throws Exception
    {       
        validationRuleStore = (ValidationRuleStore) getBean( ValidationRuleStore.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        expressionService = (ExpressionService) getBean ( ExpressionService.ID );
        
        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        dataElementC = createDataElement( 'C' );
        dataElementD = createDataElement( 'D' );
        
        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        dataElementService.addDataElement( dataElementC );
        dataElementService.addDataElement( dataElementD );        

        dataElements = new HashSet<DataElement>();

        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        dataElements.add( dataElementC );
        dataElements.add( dataElementD );
                
        expressionA = new Expression( "expressionA", "descriptionA", dataElements );
        expressionB = new Expression( "expressionB", "descriptionB", dataElements );
        
        expressionService.addExpression( expressionB );
        expressionService.addExpression( expressionA );
    }
    
    // -------------------------------------------------------------------------
    // ValidationRule
    // -------------------------------------------------------------------------

    public void testAddGetValidationRule()
    {
        ValidationRule validationRule = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );
        
        int id = validationRuleStore.addValidationRule( validationRule );
        
        validationRule = validationRuleStore.getValidationRule( id );
        
        assertEquals( validationRule.getName(), "ValidationRuleA" );
        assertEquals( validationRule.getDescription(), "DescriptionA" );
        assertEquals( validationRule.getType(), ValidationRule.TYPE_ABSOLUTE );
        assertEquals( validationRule.getOperator(), ValidationRule.OPERATOR_EQUAL );
        assertNotNull( validationRule.getLeftSide().getExpression() );
        assertNotNull( validationRule.getRightSide().getExpression() );
    }
    
    public void testUpdateValidationRule()
    {
        ValidationRule validationRule = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );
        
        int id = validationRuleStore.addValidationRule( validationRule );
        
        validationRule = validationRuleStore.getValidationRule( id );
        
        assertEquals( validationRule.getName(), "ValidationRuleA" );
        assertEquals( validationRule.getDescription(), "DescriptionA" );
        assertEquals( validationRule.getType(), ValidationRule.TYPE_ABSOLUTE );
        assertEquals( validationRule.getOperator(), ValidationRule.OPERATOR_EQUAL );
        
        validationRule.setName( "ValidationRuleB" );
        validationRule.setDescription( "DescriptionB" );
        validationRule.setType( ValidationRule.TYPE_STATISTICAL );
        validationRule.setOperator( ValidationRule.OPERATOR_GREATER );
        
        validationRuleStore.updateValidationRule( validationRule );

        validationRule = validationRuleStore.getValidationRule( id );
        
        assertEquals( validationRule.getName(), "ValidationRuleB" );
        assertEquals( validationRule.getDescription(), "DescriptionB" );
        assertEquals( validationRule.getType(), ValidationRule.TYPE_STATISTICAL );
        assertEquals( validationRule.getOperator(), ValidationRule.OPERATOR_GREATER );
    }
    
    public void testDeleteValidationRule()
    {
        ValidationRule validationRuleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );
        ValidationRule validationRuleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );

        int idA = validationRuleStore.addValidationRule( validationRuleA );
        int idB = validationRuleStore.addValidationRule( validationRuleB );
        
        assertNotNull( validationRuleStore.getValidationRule( idA ) );
        assertNotNull( validationRuleStore.getValidationRule( idB ) );
        
        validationRuleStore.deleteValidationRule( validationRuleA );

        assertNull( validationRuleStore.getValidationRule( idA ) );
        assertNotNull( validationRuleStore.getValidationRule( idB ) );
                
        validationRuleStore.deleteValidationRule( validationRuleB );
        
        assertNull( validationRuleStore.getValidationRule( idA ) );
        assertNull( validationRuleStore.getValidationRule( idB ) );
    }
    
    public void testGetAllValidationRules()
    {
        ValidationRule validationRuleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );
        ValidationRule validationRuleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );

        validationRuleStore.addValidationRule( validationRuleA );
        validationRuleStore.addValidationRule( validationRuleB );
        
        Collection<ValidationRule> rules = validationRuleStore.getAllValidationRules();
        
        assertTrue( rules.size() == 2 );
        assertTrue( rules.contains( validationRuleA ) );
        assertTrue( rules.contains( validationRuleB ) );        
    }
    
    public void testGetValidationRuleByName()
    {
        ValidationRule validationRuleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );
        ValidationRule validationRuleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );

        int id = validationRuleStore.addValidationRule( validationRuleA );
        validationRuleStore.addValidationRule( validationRuleB );
        
        ValidationRule rule = validationRuleStore.getValidationRuleByName( "ValidationRuleA" );
        
        assertEquals( rule.getId(), id );
        assertEquals( rule.getName(), "ValidationRuleA" ); 
    }

    // -------------------------------------------------------------------------
    // ValidationRuleGroup
    // -------------------------------------------------------------------------

    public void testAddValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleStore.addValidationRule( ruleA );
        validationRuleStore.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        int idA = validationRuleStore.addValidationRuleGroup( groupA );
        int idB = validationRuleStore.addValidationRuleGroup( groupB );
        
        assertEquals( groupA, validationRuleStore.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleStore.getValidationRuleGroup( idB ) );
    }

    public void testUpdateValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleStore.addValidationRule( ruleA );
        validationRuleStore.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        int idA = validationRuleStore.addValidationRuleGroup( groupA );
        int idB = validationRuleStore.addValidationRuleGroup( groupB );
        
        assertEquals( groupA, validationRuleStore.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleStore.getValidationRuleGroup( idB ) );
        
        ruleA.setName( "UpdatedValidationRuleA" );
        ruleB.setName( "UpdatedValidationRuleB" );
        
        validationRuleStore.updateValidationRuleGroup( groupA );
        validationRuleStore.updateValidationRuleGroup( groupB );

        assertEquals( groupA, validationRuleStore.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleStore.getValidationRuleGroup( idB ) );        
    }

    public void testDeleteValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleStore.addValidationRule( ruleA );
        validationRuleStore.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        int idA = validationRuleStore.addValidationRuleGroup( groupA );
        int idB = validationRuleStore.addValidationRuleGroup( groupB );
        
        assertNotNull( validationRuleStore.getValidationRuleGroup( idA ) );
        assertNotNull( validationRuleStore.getValidationRuleGroup( idB ) );
        
        validationRuleStore.deleteValidationRuleGroup( groupA );

        assertNull( validationRuleStore.getValidationRuleGroup( idA ) );
        assertNotNull( validationRuleStore.getValidationRuleGroup( idB ) );
        
        validationRuleStore.deleteValidationRuleGroup( groupB );

        assertNull( validationRuleStore.getValidationRuleGroup( idA ) );
        assertNull( validationRuleStore.getValidationRuleGroup( idB ) );
    }

    public void testGetAllValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleStore.addValidationRule( ruleA );
        validationRuleStore.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        validationRuleStore.addValidationRuleGroup( groupA );
        validationRuleStore.addValidationRuleGroup( groupB );
        
        Collection<ValidationRuleGroup> groups = validationRuleStore.getAllValidationRuleGroups();
        
        assertEquals( 2, groups.size() );
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
    }

    public void testGetValidationRuleGroupByName()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleStore.addValidationRule( ruleA );
        validationRuleStore.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        validationRuleStore.addValidationRuleGroup( groupA );
        validationRuleStore.addValidationRuleGroup( groupB );
        
        ValidationRuleGroup groupByName = validationRuleStore.getValidationRuleGroupByName( groupA.getName() );
        
        assertEquals( groupA, groupByName );
    }
}
