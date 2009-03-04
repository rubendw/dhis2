package org.hisp.dhis.indicator;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class IndicatorServiceTest
    extends DhisSpringTest
{
    private TransactionManager transactionManager;

    private IndicatorService indicatorService;

    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private Indicator createIndicator( char uniqueCharacter, IndicatorType type )
    {
        Indicator indicator = new Indicator();
        
        indicator.setName( "Indicator" + uniqueCharacter );
        indicator.setAlternativeName( "AlternativeName" + uniqueCharacter );
        indicator.setShortName( "ShortName" + uniqueCharacter );
        indicator.setCode( "Code" + uniqueCharacter );
        indicator.setDescription( "Description" + uniqueCharacter );
        indicator.setIndicatorType( type );
        indicator.setNumerator( "Numerator" );
        indicator.setNumeratorDescription( "NumeratorDescription" );
        indicator.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        indicator.setDenominator( "Denominator" );
        indicator.setDenominatorDescription( "DenominatorDescription" );
        indicator.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        return indicator;
    }
    
    private void assertEquals( char uniqueCharacter, Indicator indicator )
    {
        assertEquals( "Indicator" + uniqueCharacter, indicator.getName() );
        assertEquals( "AlternativeName" + uniqueCharacter, indicator.getAlternativeName() );
        assertEquals( "ShortName" + uniqueCharacter, indicator.getShortName() );
        assertEquals( "Code" + uniqueCharacter, indicator.getCode() );
        assertEquals( "Description" + uniqueCharacter, indicator.getDescription() );
    }
    
    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    public void testAddIndicatorType()
        throws Exception
    {
        transactionManager.enter();
        
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        IndicatorType typeC = new IndicatorType( "IndicatorTypeA", 100 );
        
        int idA = indicatorService.addIndicatorType( typeA );
        int idB = indicatorService.addIndicatorType( typeB );
        
        transactionManager.leave();

        try
        {
            indicatorService.addIndicatorType( typeC );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception ex )
        {
        }

        transactionManager.enter();

        typeA = indicatorService.getIndicatorType( idA );
        assertNotNull( typeA );
        assertEquals( idA, typeA.getId() );
        
        typeB = indicatorService.getIndicatorType( idB );
        assertNotNull( typeB );
        assertEquals( idB, typeB.getId() );
        
        transactionManager.leave();
    }
    
    public void testUpdateIndicatorType()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        int idA = indicatorService.addIndicatorType( typeA );
        typeA = indicatorService.getIndicatorType( idA );
        assertEquals( typeA.getName(), "IndicatorTypeA" );
        
        typeA.setName( "IndicatorTypeB" );
        indicatorService.updateIndicatorType( typeA );
        typeA = indicatorService.getIndicatorType( idA );
        assertNotNull( typeA );
        assertEquals( typeA.getName(), "IndicatorTypeB" );
    }
    
    public void testGetAndDeleteIndicatorType()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        
        int idA = indicatorService.addIndicatorType( typeA );
        int idB = indicatorService.addIndicatorType( typeB );
        
        assertNotNull( indicatorService.getIndicatorType( idA ) );
        assertNotNull( indicatorService.getIndicatorType( idB ) );
        
        indicatorService.deleteIndicatorType( typeA );

        assertNull( indicatorService.getIndicatorType( idA ) );
        assertNotNull( indicatorService.getIndicatorType( idB ) );

        indicatorService.deleteIndicatorType( typeB );

        assertNull( indicatorService.getIndicatorType( idA ) );
        assertNull( indicatorService.getIndicatorType( idB ) );        
    }
    
    public void testGetAllIndicatorTypes()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        
        indicatorService.addIndicatorType( typeA );
        indicatorService.addIndicatorType( typeB );
        
        Collection<IndicatorType> types = indicatorService.getAllIndicatorTypes();
        
        assertEquals( types.size(), 2 );
        assertTrue( types.contains( typeA ) );
        assertTrue( types.contains( typeB ) );
    }
    
    public void testGetIndicatorTypeByName()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        
        int idA = indicatorService.addIndicatorType( typeA );
        int idB = indicatorService.addIndicatorType( typeB );
        
        assertNotNull( indicatorService.getIndicatorType( idA ) );
        assertNotNull( indicatorService.getIndicatorType( idB ) );
        
        typeA = indicatorService.getIndicatorTypeByName( "IndicatorTypeA" );
        assertNotNull( typeA );
        assertEquals( typeA.getId(), idA );
        
        IndicatorType typeC = indicatorService.getIndicatorTypeByName( "IndicatorTypeC" );
        assertNull( typeC );
    }
    
    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    public void testAddIndicatorGroup()
        throws Exception
    {
        transactionManager.enter();
        
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        IndicatorGroup groupC = new IndicatorGroup( "IndicatorGroupA" );
        
        int idA = indicatorService.addIndicatorGroup( groupA );
        int idB = indicatorService.addIndicatorGroup( groupB );
        
        transactionManager.leave();

        try
        {
            indicatorService.addIndicatorGroup( groupC );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception ex )
        {
        }

        transactionManager.enter();

        groupA = indicatorService.getIndicatorGroup( idA );
        assertNotNull( groupA );
        assertEquals( idA, groupA.getId() );
        
        groupB = indicatorService.getIndicatorGroup( idB );
        assertNotNull( groupB );
        assertEquals( idB, groupB.getId() );
        
        transactionManager.leave();
    }
    
    public void testUpdateIndicatorGroup()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        int idA = indicatorService.addIndicatorGroup( groupA );
        groupA = indicatorService.getIndicatorGroup( idA );
        assertEquals( groupA.getName(), "IndicatorGroupA" );
        
        groupA.setName( "IndicatorGroupB" );
        indicatorService.updateIndicatorGroup( groupA );
        groupA = indicatorService.getIndicatorGroup( idA );
        assertNotNull( groupA );
        assertEquals( groupA.getName(), "IndicatorGroupB" );
    }
    
    public void testGetAndDeleteIndicatorGroup()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        
        int idA = indicatorService.addIndicatorGroup( groupA );
        int idB = indicatorService.addIndicatorGroup( groupB );
        
        assertNotNull( indicatorService.getIndicatorGroup( idA ) );
        assertNotNull( indicatorService.getIndicatorGroup( idB ) );
        
        indicatorService.deleteIndicatorGroup( groupA );

        assertNull( indicatorService.getIndicatorGroup( idA ) );
        assertNotNull( indicatorService.getIndicatorGroup( idB ) );

        indicatorService.deleteIndicatorGroup( groupB );

        assertNull( indicatorService.getIndicatorGroup( idA ) );
        assertNull( indicatorService.getIndicatorGroup( idB ) );        
    }
    
    public void testGetIndicatorGroupByUUID()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        int id = indicatorService.addIndicatorGroup( groupA );
        
        groupA = indicatorService.getIndicatorGroup( id );
        String uuid = groupA.getUuid();
        
        IndicatorGroup groupB = indicatorService.getIndicatorGroup( uuid );
        
        assertEquals( groupA, groupB );
    }
    
    public void testGetAllIndicatorGroups()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        
        indicatorService.addIndicatorGroup( groupA );
        indicatorService.addIndicatorGroup( groupB );
        
        Collection<IndicatorGroup> groups = indicatorService.getAllIndicatorGroups();
        
        assertEquals( groups.size(), 2 );
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
    }
    
    public void testGetIndicatorGroupByName()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        
        int idA = indicatorService.addIndicatorGroup( groupA );
        int idB = indicatorService.addIndicatorGroup( groupB );
        
        assertNotNull( indicatorService.getIndicatorGroup( idA ) );
        assertNotNull( indicatorService.getIndicatorGroup( idB ) );
        
        groupA = indicatorService.getIndicatorGroupByName( "IndicatorGroupA" );
        assertNotNull( groupA );
        assertEquals( groupA.getId(), idA );
        
        IndicatorGroup groupC = indicatorService.getIndicatorGroupByName( "IndicatorGroupC" );
        assertNull( groupC );
    }

    public void testGetGroupsContainingIndicator() throws Exception
    {
        transactionManager.enter();

        IndicatorType indicatorType = new IndicatorType( "indicatorTypeName", 100 );
        indicatorService.addIndicatorType( indicatorType );

        transactionManager.leave();
        transactionManager.enter();

        Indicator indicator1 = createIndicator( 'A', indicatorType );
        Indicator indicator2 = createIndicator( 'B', indicatorType );
        Indicator indicator3 = createIndicator( 'C', indicatorType );
        Indicator indicator4 = createIndicator( 'D', indicatorType );

        indicatorService.addIndicator( indicator1 );
        indicatorService.addIndicator( indicator2 );
        indicatorService.addIndicator( indicator3 );
        indicatorService.addIndicator( indicator4 );

        transactionManager.leave();
        transactionManager.enter();

        IndicatorGroup indicatorGroup1 = new IndicatorGroup( "indicatorGroupName1" );
        IndicatorGroup indicatorGroup2 = new IndicatorGroup( "indicatorGroupName2" );
        IndicatorGroup indicatorGroup3 = new IndicatorGroup( "indicatorGroupName3" );
        IndicatorGroup indicatorGroup4 = new IndicatorGroup( "indicatorGroupName4" );

        Set<Indicator> members1 = new HashSet<Indicator>();
        Set<Indicator> members2 = new HashSet<Indicator>();

        members1.add( indicator1 );
        members1.add( indicator2 );
        members2.add( indicator1 );
        members2.add( indicator3 );
        members2.add( indicator4 );

        indicatorGroup1.setMembers( members1 );
        indicatorGroup2.setMembers( members2 );
        indicatorGroup3.setMembers( members1 );
        indicatorGroup4.setMembers( members2 );

        indicatorService.addIndicatorGroup( indicatorGroup1 );
        indicatorService.addIndicatorGroup( indicatorGroup2 );
        indicatorService.addIndicatorGroup( indicatorGroup3 );
        indicatorService.addIndicatorGroup( indicatorGroup4 );

        transactionManager.leave();
        
        Collection<IndicatorGroup> groups1 = indicatorService.getGroupsContainingIndicator( indicator1 );
        
        assertTrue( groups1.size() == 4 );

        Collection<IndicatorGroup> groups2 = indicatorService.getGroupsContainingIndicator( indicator2 );
        
        assertTrue( groups2.size() == 2 );
        assertTrue( groups2.contains( indicatorGroup1 ) );
        assertTrue( groups2.contains( indicatorGroup3 ) );
    }
    
    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    public void testAddIndicator()
        throws Exception
    {
        transactionManager.enter();
        
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );
        
        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );
        Indicator indicatorC = createIndicator( 'A', type );
        
        int idA = indicatorService.addIndicator( indicatorA );
        int idB = indicatorService.addIndicator( indicatorB );
        
        transactionManager.leave();

        try
        {
            indicatorService.addIndicator( indicatorC );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception ex )
        {
        }

        transactionManager.enter();

        indicatorA = indicatorService.getIndicator( idA );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        indicatorB = indicatorService.getIndicator( idB );
        assertNotNull( indicatorB );
        assertEquals( 'B', indicatorB );
        
        transactionManager.leave();
    }
    
    public void testUpdateIndicator()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        int idA = indicatorService.addIndicator( indicatorA );
        indicatorA = indicatorService.getIndicator( idA );
        assertEquals( 'A', indicatorA );
        
        indicatorA.setName( "IndicatorB" );
        indicatorService.updateIndicator( indicatorA );
        indicatorA = indicatorService.getIndicator( idA );
        assertNotNull( indicatorA );
        assertEquals( indicatorA.getName(), "IndicatorB" );
    }
    
    public void testGetAndDeleteIndicator()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        int idA = indicatorService.addIndicator( indicatorA );
        int idB = indicatorService.addIndicator( indicatorB );
        
        assertNotNull( indicatorService.getIndicator( idA ) );
        assertNotNull( indicatorService.getIndicator( idB ) );
        
        indicatorService.deleteIndicator( indicatorA );

        assertNull( indicatorService.getIndicator( idA ) );
        assertNotNull( indicatorService.getIndicator( idB ) );

        indicatorService.deleteIndicator( indicatorB );

        assertNull( indicatorService.getIndicator( idA ) );
        assertNull( indicatorService.getIndicator( idB ) );        
    }
    
    public void testGetIndicatorByUUID()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );
        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );        
        int idA = indicatorService.addIndicator( indicatorA );
        
        indicatorA = indicatorService.getIndicator( idA );
        String uuid = indicatorA.getUuid();
        
        Indicator indicatorB = indicatorService.getIndicator( uuid );
        
        assertEquals( indicatorA, indicatorB );
    }
    
    public void testGetAllIndicators()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        indicatorService.addIndicator( indicatorA );
        indicatorService.addIndicator( indicatorB );
        
        Collection<Indicator> indicators = indicatorService.getAllIndicators();
        
        assertEquals( indicators.size(), 2 );
        assertTrue( indicators.contains( indicatorA ) );
        assertTrue( indicators.contains( indicatorB ) );
    }
    
    public void testGetIndicatorByName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        int idA = indicatorService.addIndicator( indicatorA );
        int idB = indicatorService.addIndicator( indicatorB );
        
        assertNotNull( indicatorService.getIndicator( idA ) );
        assertNotNull( indicatorService.getIndicator( idB ) );
        
        indicatorA = indicatorService.getIndicatorByName( "IndicatorA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorService.getIndicatorByName( "IndicatorC" );
        assertNull( indicatorC );
    }    

    public void testGetIndicatorByAlternativeName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        int idA = indicatorService.addIndicator( indicatorA );
        int idB = indicatorService.addIndicator( indicatorB );
        
        assertNotNull( indicatorService.getIndicator( idA ) );
        assertNotNull( indicatorService.getIndicator( idB ) );
        
        indicatorA = indicatorService.getIndicatorByAlternativeName( "AlternativeNameA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorService.getIndicatorByAlternativeName( "AlternativeNameC" );
        assertNull( indicatorC );
    }
    
    public void testGetIndicatorByShortName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );
    
        int idA = indicatorService.addIndicator( indicatorA );
        int idB = indicatorService.addIndicator( indicatorB );
        
        assertNotNull( indicatorService.getIndicator( idA ) );
        assertNotNull( indicatorService.getIndicator( idB ) );
        
        indicatorA = indicatorService.getIndicatorByShortName( "ShortNameA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorService.getIndicatorByShortName( "ShortNameC" );
        assertNull( indicatorC );
    }    

    public void testGetIndicatorByCodeName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorService.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );
    
        int idA = indicatorService.addIndicator( indicatorA );
        int idB = indicatorService.addIndicator( indicatorB );
        
        assertNotNull( indicatorService.getIndicator( idA ) );
        assertNotNull( indicatorService.getIndicator( idB ) );
        
        indicatorA = indicatorService.getIndicatorByCode( "CodeA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorService.getIndicatorByCode( "CodeC" );
        assertNull( indicatorC );
    }
}
