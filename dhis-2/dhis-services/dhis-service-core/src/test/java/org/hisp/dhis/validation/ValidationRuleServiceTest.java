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

import static org.hisp.dhis.expression.Expression.SEPARATOR;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.system.util.MathUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidationRuleServiceTest
    extends DhisConvenienceTest
{    
    private ValidationRuleService validationRuleService;

    private DataElementCategoryComboService categoryComboService;

    private ExpressionService expressionService;
    
    private DataElement dataElementA;
    private DataElement dataElementB;
    private DataElement dataElementC;
    private DataElement dataElementD;
    
    private int dataElementIdA;
    private int dataElementIdB;
    private int dataElementIdC;
    private int dataElementIdD;    

    private Set<DataElement> dataElementsA = new HashSet<DataElement>();
    private Set<DataElement> dataElementsB = new HashSet<DataElement>();
    private Set<DataElement> dataElementsC = new HashSet<DataElement>();

    private DataElementCategoryCombo categoryCombo;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private Expression expressionA;
    private Expression expressionB;
    private Expression expressionC;
    
    private DataSet dataSet;
    
    private Period periodA;
    private Period periodB;
    
    private Source sourceA;
    private Source sourceB;
    
    private Set<Source> sourcesA = new HashSet<Source>();
    
    private ValidationRule validationRuleA;
    private ValidationRule validationRuleB;
    private ValidationRule validationRuleC;
    private ValidationRule validationRuleD;
    
    private ValidationRuleGroup group;
    
    // ----------------------------------------------------------------------
    // Fixture
    // ----------------------------------------------------------------------
    
    public void setUpTest()
        throws Exception
    {       
        validationRuleService = (ValidationRuleService) getBean( ValidationRuleService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        expressionService = (ExpressionService) getBean ( ExpressionService.ID );
        
        dataSetService = (DataSetService) getBean( DataSetService.ID );

        sourceStore = (SourceStore) getBean( SourceStore.ID ); 
        
        dataValueService = (DataValueService) getBean( DataValueService.ID ); 

        periodService = (PeriodService) getBean( PeriodService.ID );
        
        PeriodType periodType = periodService.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        dataElementC = createDataElement( 'C' );
        dataElementD = createDataElement( 'D' );
        
        dataElementIdA = dataElementService.addDataElement( dataElementA );
        dataElementIdB = dataElementService.addDataElement( dataElementB );
        dataElementIdC = dataElementService.addDataElement( dataElementC );
        dataElementIdD = dataElementService.addDataElement( dataElementD );        

        dataElementsA.add( dataElementA );
        dataElementsA.add( dataElementB );
        dataElementsB.add( dataElementC );
        dataElementsB.add( dataElementD );
        dataElementsC.add( dataElementB );
        
        categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );             
        
        categoryOptionCombo = categoryCombo.getOptionCombos().iterator().next();

        String suffix = SEPARATOR + categoryOptionCombo.getId();
        
        expressionA = new Expression( "[" + dataElementIdA + suffix + "] + [" + dataElementIdB + suffix + "]", "descriptionA", dataElementsA );
        expressionB = new Expression( "[" + dataElementIdC + suffix + "] - [" + dataElementIdD + suffix + "]", "descriptionB", dataElementsB );
        expressionC = new Expression( "[" + dataElementIdB + suffix + "] * 2", "descriptionC", dataElementsC );
        
        expressionService.addExpression( expressionA );
        expressionService.addExpression( expressionB );
        expressionService.addExpression( expressionC );
        
        periodA = createPeriod( new MonthlyPeriodType(), getDate( 2000, 3, 1 ), getDate( 2000, 3, 31 ) );
        periodB = createPeriod( new MonthlyPeriodType(), getDate( 2000, 4, 1 ), getDate( 2000, 4, 30 ) );
        
        sourceA = new DummySource( "SourceA" );
        sourceB = new DummySource( "SourceB" );
        
        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        
        sourcesA.add( sourceA );
        sourcesA.add( sourceB );
        
        dataSet = createDataSet( 'A', periodType );
        
        dataSet.getDataElements().add( dataElementA );
        dataSet.getDataElements().add( dataElementB );
        dataSet.getDataElements().add( dataElementC );
        dataSet.getDataElements().add( dataElementD );
        
        dataSet.getSources().add( sourceA );
        dataSet.getSources().add( sourceB );

        dataSetService.addDataSet( dataSet );
        
        validationRuleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, expressionA, expressionB );
        validationRuleB = createValidationRule( 'B', ValidationRule.OPERATOR_GREATER, expressionB, expressionC );
        validationRuleC = createValidationRule( 'C', ValidationRule.OPERATOR_LESSER_EQUAL, expressionB, expressionA );
        validationRuleD = createValidationRule( 'D', ValidationRule.OPERATOR_LESSER, expressionA, expressionC );
        
        group = createValidationRuleGroup( 'A' );
    }

    // ----------------------------------------------------------------------
    // Business logic tests
    // ----------------------------------------------------------------------

    public void testValidateDateDateSources()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceA, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceB, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceB, "4", categoryOptionCombo ) );

        validationRuleService.addValidationRule( validationRuleA ); // Invalid
        validationRuleService.addValidationRule( validationRuleB ); // Invalid
        validationRuleService.addValidationRule( validationRuleC ); // Valid
        validationRuleService.addValidationRule( validationRuleD ); // Valid
        
        Collection<ValidationResult> results = validationRuleService.validate( getDate( 2000, 2, 1 ), getDate( 2000, 6, 1 ), sourcesA );
        
        Collection<ValidationResult> reference = new HashSet<ValidationResult>();
        
        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleA, 3.0, -1.0 ) );

        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleB, -1.0, 4.0 ) );
        
        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), 
                result.getValidationRule().getOperator(), result.getRightsideValue() ) );
        }
        
        assertEquals( results.size(), 8 );
        assertEquals( reference, results );
    }
    
    public void testValidateDateDateSourcesGroup()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceA, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceB, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceB, "4", categoryOptionCombo ) );

        validationRuleService.addValidationRule( validationRuleA ); // Invalid
        validationRuleService.addValidationRule( validationRuleB ); // Invalid
        validationRuleService.addValidationRule( validationRuleC ); // Valid
        validationRuleService.addValidationRule( validationRuleD ); // Valid
        
        group.getMembers().add( validationRuleA );
        group.getMembers().add( validationRuleC );
        
        validationRuleService.addValidationRuleGroup( group );

        Collection<ValidationResult> results = validationRuleService.validate( getDate( 2000, 2, 1 ), getDate( 2000, 6, 1 ), sourcesA, group );
        
        Collection<ValidationResult> reference = new HashSet<ValidationResult>();
        
        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleA, 3.0, -1.0 ) );
        
        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), 
                result.getValidationRule().getOperator(), result.getRightsideValue() ) );
        }
        
        assertEquals( results.size(), 4 );
        assertEquals( reference, results );
    }
    
    public void testValidateDateDateSource()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );
        
        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceA, "4", categoryOptionCombo ) );

        validationRuleService.addValidationRule( validationRuleA );
        validationRuleService.addValidationRule( validationRuleB );
        validationRuleService.addValidationRule( validationRuleC );
        validationRuleService.addValidationRule( validationRuleD );
        
        Collection<ValidationResult> results = validationRuleService.validate( getDate( 2000, 2, 1 ), getDate( 2000, 6, 1 ), sourceA );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleB, -1.0, 4.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), 
                result.getValidationRule().getOperator(), result.getRightsideValue() ) );
        }
        
        assertEquals( results.size(), 4 );
        assertEquals( reference, results );
    }
    
    public void testValidateDataSetPeriodSource()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );

        validationRuleService.addValidationRule( validationRuleA );
        validationRuleService.addValidationRule( validationRuleB );
        validationRuleService.addValidationRule( validationRuleC );
        validationRuleService.addValidationRule( validationRuleD );
        
        Collection<ValidationResult> results = validationRuleService.validate( dataSet, periodA, sourceA );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), 
                result.getValidationRule().getOperator(), result.getRightsideValue() ) );
        }
        
        assertEquals( results.size(), 2 );
        assertEquals( reference, results );
    }
    
    // ----------------------------------------------------------------------
    // CURD functionality tests
    // ----------------------------------------------------------------------
    
    public void testAddGetValidationRule()
    {
        int id = validationRuleService.addValidationRule( validationRuleA );
        
        validationRuleA = validationRuleService.getValidationRule( id );
        
        assertEquals( validationRuleA.getName(), "ValidationRuleA" );
        assertEquals( validationRuleA.getDescription(), "DescriptionA" );
        assertEquals( validationRuleA.getType(), ValidationRule.TYPE_ABSOLUTE );
        assertEquals( validationRuleA.getOperator(), ValidationRule.OPERATOR_EQUAL );
        assertNotNull( validationRuleA.getLeftSide().getExpression() );
        assertNotNull( validationRuleA.getRightSide().getExpression() );
    }
    
    public void testUpdateValidationRule()
    {
        int id = validationRuleService.addValidationRule( validationRuleA );
        
        validationRuleA = validationRuleService.getValidationRule( id );
        
        assertEquals( validationRuleA.getName(), "ValidationRuleA" );
        assertEquals( validationRuleA.getDescription(), "DescriptionA" );
        assertEquals( validationRuleA.getType(), ValidationRule.TYPE_ABSOLUTE );
        assertEquals( validationRuleA.getOperator(), ValidationRule.OPERATOR_EQUAL );
        
        validationRuleA.setName( "ValidationRuleB" );
        validationRuleA.setDescription( "DescriptionB" );
        validationRuleA.setType( ValidationRule.TYPE_STATISTICAL );
        validationRuleA.setOperator( ValidationRule.OPERATOR_GREATER );
        
        validationRuleService.updateValidationRule( validationRuleA );

        validationRuleA = validationRuleService.getValidationRule( id );
        
        assertEquals( validationRuleA.getName(), "ValidationRuleB" );
        assertEquals( validationRuleA.getDescription(), "DescriptionB" );
        assertEquals( validationRuleA.getType(), ValidationRule.TYPE_STATISTICAL );
        assertEquals( validationRuleA.getOperator(), ValidationRule.OPERATOR_GREATER );
    }
    
    public void testDeleteValidationRule()
    {
        int idA = validationRuleService.addValidationRule( validationRuleA );
        int idB = validationRuleService.addValidationRule( validationRuleB );
        
        assertNotNull( validationRuleService.getValidationRule( idA ) );
        assertNotNull( validationRuleService.getValidationRule( idB ) );
        
        validationRuleService.deleteValidationRule( validationRuleA );

        assertNull( validationRuleService.getValidationRule( idA ) );
        assertNotNull( validationRuleService.getValidationRule( idB ) );
                
        validationRuleService.deleteValidationRule( validationRuleB );
        
        assertNull( validationRuleService.getValidationRule( idA ) );
        assertNull( validationRuleService.getValidationRule( idB ) );
    }
    
    public void testGetAllValidationRules()
    {
        validationRuleService.addValidationRule( validationRuleA );
        validationRuleService.addValidationRule( validationRuleB );
        
        Collection<ValidationRule> rules = validationRuleService.getAllValidationRules();
        
        assertTrue( rules.size() == 2 );
        assertTrue( rules.contains( validationRuleA ) );
        assertTrue( rules.contains( validationRuleB ) );        
    }

    public void testGetValidationRuleByName()
    {
        int id = validationRuleService.addValidationRule( validationRuleA );
        validationRuleService.addValidationRule( validationRuleB );
        
        ValidationRule rule = validationRuleService.getValidationRuleByName( "ValidationRuleA" );
        
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
        
        validationRuleService.addValidationRule( ruleA );
        validationRuleService.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        int idA = validationRuleService.addValidationRuleGroup( groupA );
        int idB = validationRuleService.addValidationRuleGroup( groupB );
        
        assertEquals( groupA, validationRuleService.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleService.getValidationRuleGroup( idB ) );
    }

    public void testUpdateValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleService.addValidationRule( ruleA );
        validationRuleService.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        int idA = validationRuleService.addValidationRuleGroup( groupA );
        int idB = validationRuleService.addValidationRuleGroup( groupB );
        
        assertEquals( groupA, validationRuleService.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleService.getValidationRuleGroup( idB ) );
        
        ruleA.setName( "UpdatedValidationRuleA" );
        ruleB.setName( "UpdatedValidationRuleB" );
        
        validationRuleService.updateValidationRuleGroup( groupA );
        validationRuleService.updateValidationRuleGroup( groupB );

        assertEquals( groupA, validationRuleService.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleService.getValidationRuleGroup( idB ) );        
    }

    public void testDeleteValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleService.addValidationRule( ruleA );
        validationRuleService.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        int idA = validationRuleService.addValidationRuleGroup( groupA );
        int idB = validationRuleService.addValidationRuleGroup( groupB );
        
        assertNotNull( validationRuleService.getValidationRuleGroup( idA ) );
        assertNotNull( validationRuleService.getValidationRuleGroup( idB ) );
        
        validationRuleService.deleteValidationRuleGroup( groupA );

        assertNull( validationRuleService.getValidationRuleGroup( idA ) );
        assertNotNull( validationRuleService.getValidationRuleGroup( idB ) );
        
        validationRuleService.deleteValidationRuleGroup( groupB );

        assertNull( validationRuleService.getValidationRuleGroup( idA ) );
        assertNull( validationRuleService.getValidationRuleGroup( idB ) );
    }

    public void testGetAllValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleService.addValidationRule( ruleA );
        validationRuleService.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        validationRuleService.addValidationRuleGroup( groupA );
        validationRuleService.addValidationRuleGroup( groupB );
        
        Collection<ValidationRuleGroup> groups = validationRuleService.getAllValidationRuleGroups();
        
        assertEquals( 2, groups.size() );
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
    }

    public void testGetValidationRuleGroupByName()
    {
        ValidationRule ruleA = createValidationRule( 'A', ValidationRule.OPERATOR_EQUAL, null, null );
        ValidationRule ruleB = createValidationRule( 'B', ValidationRule.OPERATOR_EQUAL, null, null );
        
        validationRuleService.addValidationRule( ruleA );
        validationRuleService.addValidationRule( ruleB );
        
        Set<ValidationRule> rules = new HashSet<ValidationRule>();
        
        rules.add( ruleA );
        rules.add( ruleB );
        
        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );
        
        groupA.setMembers( rules );
        groupB.setMembers( rules );
        
        validationRuleService.addValidationRuleGroup( groupA );
        validationRuleService.addValidationRuleGroup( groupB );
        
        ValidationRuleGroup groupByName = validationRuleService.getValidationRuleGroupByName( groupA.getName() );
        
        assertEquals( groupA, groupByName );
    }
}
