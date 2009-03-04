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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorStore;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.system.util.UUIdUtils;
import org.hisp.dhis.transaction.TransactionManager;

import java.util.*;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorStoreTest.java 3286 2007-05-07 18:05:21Z larshelg $
 */
public class IndicatorStoreTest
    extends DhisSpringTest
{
    private TransactionManager transactionManager;

    private IndicatorStore indicatorStore;

    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        indicatorStore = (IndicatorStore) getBean( IndicatorStore.ID );
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private Indicator createIndicator( char uniqueCharacter, IndicatorType type )
    {
        Indicator indicator = new Indicator();
        
        indicator.setUuid( UUIdUtils.getUUId() );
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
        
        int idA = indicatorStore.addIndicatorType( typeA );
        int idB = indicatorStore.addIndicatorType( typeB );
        
        transactionManager.leave();

        try
        {
            indicatorStore.addIndicatorType( typeC );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception ex )
        {
        }

        transactionManager.enter();

        typeA = indicatorStore.getIndicatorType( idA );
        assertNotNull( typeA );
        assertEquals( idA, typeA.getId() );
        
        typeB = indicatorStore.getIndicatorType( idB );
        assertNotNull( typeB );
        assertEquals( idB, typeB.getId() );
        
        transactionManager.leave();
    }
    
    public void testUpdateIndicatorType()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        int idA = indicatorStore.addIndicatorType( typeA );
        typeA = indicatorStore.getIndicatorType( idA );
        assertEquals( typeA.getName(), "IndicatorTypeA" );
        
        typeA.setName( "IndicatorTypeB" );
        indicatorStore.updateIndicatorType( typeA );
        typeA = indicatorStore.getIndicatorType( idA );
        assertNotNull( typeA );
        assertEquals( typeA.getName(), "IndicatorTypeB" );
    }
    
    public void testGetAndDeleteIndicatorType()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        
        int idA = indicatorStore.addIndicatorType( typeA );
        int idB = indicatorStore.addIndicatorType( typeB );
        
        assertNotNull( indicatorStore.getIndicatorType( idA ) );
        assertNotNull( indicatorStore.getIndicatorType( idB ) );
        
        indicatorStore.deleteIndicatorType( typeA );

        assertNull( indicatorStore.getIndicatorType( idA ) );
        assertNotNull( indicatorStore.getIndicatorType( idB ) );

        indicatorStore.deleteIndicatorType( typeB );

        assertNull( indicatorStore.getIndicatorType( idA ) );
        assertNull( indicatorStore.getIndicatorType( idB ) );        
    }
    
    public void testGetAllIndicatorTypes()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        
        indicatorStore.addIndicatorType( typeA );
        indicatorStore.addIndicatorType( typeB );
        
        Collection<IndicatorType> types = indicatorStore.getAllIndicatorTypes();
        
        assertEquals( types.size(), 2 );
        assertTrue( types.contains( typeA ) );
        assertTrue( types.contains( typeB ) );
    }
    
    public void testGetIndicatorTypeByName()
        throws Exception
    {
        IndicatorType typeA = new IndicatorType( "IndicatorTypeA", 100 );
        IndicatorType typeB = new IndicatorType( "IndicatorTypeB", 1 );
        
        int idA = indicatorStore.addIndicatorType( typeA );
        int idB = indicatorStore.addIndicatorType( typeB );
        
        assertNotNull( indicatorStore.getIndicatorType( idA ) );
        assertNotNull( indicatorStore.getIndicatorType( idB ) );
        
        typeA = indicatorStore.getIndicatorTypeByName( "IndicatorTypeA" );
        assertNotNull( typeA );
        assertEquals( typeA.getId(), idA );
        
        IndicatorType typeC = indicatorStore.getIndicatorTypeByName( "IndicatorTypeC" );
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
        
        int idA = indicatorStore.addIndicatorGroup( groupA );
        int idB = indicatorStore.addIndicatorGroup( groupB );
        
        transactionManager.leave();

        try
        {
            indicatorStore.addIndicatorGroup( groupC );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception ex )
        {
        }

        transactionManager.enter();

        groupA = indicatorStore.getIndicatorGroup( idA );
        assertNotNull( groupA );
        assertEquals( idA, groupA.getId() );
        
        groupB = indicatorStore.getIndicatorGroup( idB );
        assertNotNull( groupB );
        assertEquals( idB, groupB.getId() );
        
        transactionManager.leave();
    }
    
    public void testUpdateIndicatorGroup()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        int idA = indicatorStore.addIndicatorGroup( groupA );
        groupA = indicatorStore.getIndicatorGroup( idA );
        assertEquals( groupA.getName(), "IndicatorGroupA" );
        
        groupA.setName( "IndicatorGroupB" );
        indicatorStore.updateIndicatorGroup( groupA );
        groupA = indicatorStore.getIndicatorGroup( idA );
        assertNotNull( groupA );
        assertEquals( groupA.getName(), "IndicatorGroupB" );
    }
    
    public void testGetAndDeleteIndicatorGroup()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        
        int idA = indicatorStore.addIndicatorGroup( groupA );
        int idB = indicatorStore.addIndicatorGroup( groupB );
        
        assertNotNull( indicatorStore.getIndicatorGroup( idA ) );
        assertNotNull( indicatorStore.getIndicatorGroup( idB ) );
        
        indicatorStore.deleteIndicatorGroup( groupA );

        assertNull( indicatorStore.getIndicatorGroup( idA ) );
        assertNotNull( indicatorStore.getIndicatorGroup( idB ) );

        indicatorStore.deleteIndicatorGroup( groupB );

        assertNull( indicatorStore.getIndicatorGroup( idA ) );
        assertNull( indicatorStore.getIndicatorGroup( idB ) );        
    }
    
    public void testGetIndicatorGroupByUUID()
        throws Exception
    {
        String uuid = UUIdUtils.getUUId();
        
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        groupA.setUuid( uuid );
        
        indicatorStore.addIndicatorGroup( groupA );
        
        groupA = indicatorStore.getIndicatorGroup( uuid );
        
        assertNotNull( groupA );
        assertEquals( groupA.getUuid(), uuid );
    }
    
    public void testGetAllIndicatorGroups()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        
        indicatorStore.addIndicatorGroup( groupA );
        indicatorStore.addIndicatorGroup( groupB );
        
        Collection<IndicatorGroup> groups = indicatorStore.getAllIndicatorGroups();
        
        assertEquals( groups.size(), 2 );
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
    }
    
    public void testGetIndicatorGroupByName()
        throws Exception
    {
        IndicatorGroup groupA = new IndicatorGroup( "IndicatorGroupA" );
        IndicatorGroup groupB = new IndicatorGroup( "IndicatorGroupB" );
        
        int idA = indicatorStore.addIndicatorGroup( groupA );
        int idB = indicatorStore.addIndicatorGroup( groupB );
        
        assertNotNull( indicatorStore.getIndicatorGroup( idA ) );
        assertNotNull( indicatorStore.getIndicatorGroup( idB ) );
        
        groupA = indicatorStore.getIndicatorGroupByName( "IndicatorGroupA" );
        assertNotNull( groupA );
        assertEquals( groupA.getId(), idA );
        
        IndicatorGroup groupC = indicatorStore.getIndicatorGroupByName( "IndicatorGroupC" );
        assertNull( groupC );
    }
    
    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    public void testAddIndicator()
        throws Exception
    {
        transactionManager.enter();
        
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );
        
        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );
        Indicator indicatorC = createIndicator( 'A', type );
        
        int idA = indicatorStore.addIndicator( indicatorA );
        int idB = indicatorStore.addIndicator( indicatorB );
        
        transactionManager.leave();

        try
        {
            indicatorStore.addIndicator( indicatorC );
            fail( "Expected unique constraint exception" );
        }
        catch ( Exception ex )
        {
        }

        transactionManager.enter();

        indicatorA = indicatorStore.getIndicator( idA );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        indicatorB = indicatorStore.getIndicator( idB );
        assertNotNull( indicatorB );
        assertEquals( 'B', indicatorB );
        
        transactionManager.leave();
    }
    
    public void testUpdateIndicator()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        int idA = indicatorStore.addIndicator( indicatorA );
        indicatorA = indicatorStore.getIndicator( idA );
        assertEquals( 'A', indicatorA );
        
        indicatorA.setName( "IndicatorB" );
        indicatorStore.updateIndicator( indicatorA );
        indicatorA = indicatorStore.getIndicator( idA );
        assertNotNull( indicatorA );
        assertEquals( indicatorA.getName(), "IndicatorB" );
    }
    
    public void testGetAndDeleteIndicator()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        int idA = indicatorStore.addIndicator( indicatorA );
        int idB = indicatorStore.addIndicator( indicatorB );
        
        assertNotNull( indicatorStore.getIndicator( idA ) );
        assertNotNull( indicatorStore.getIndicator( idB ) );
        
        indicatorStore.deleteIndicator( indicatorA );

        assertNull( indicatorStore.getIndicator( idA ) );
        assertNotNull( indicatorStore.getIndicator( idB ) );

        indicatorStore.deleteIndicator( indicatorB );

        assertNull( indicatorStore.getIndicator( idA ) );
        assertNull( indicatorStore.getIndicator( idB ) );        
    }
    
    public void testGetIndicatorByUUID()
        throws Exception
    {
        String uuid = UUIdUtils.getUUId();
        
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );
        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        indicatorA.setUuid( uuid );
        
        indicatorStore.addIndicator( indicatorA );
        
        indicatorA = indicatorStore.getIndicator( uuid );
        
        assertNotNull( indicatorA );
        assertEquals( indicatorA.getUuid(), uuid );
    }
    
    public void testGetAllIndicators()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        indicatorStore.addIndicator( indicatorA );
        indicatorStore.addIndicator( indicatorB );
        
        Collection<Indicator> indicators = indicatorStore.getAllIndicators();
        
        assertEquals( indicators.size(), 2 );
        assertTrue( indicators.contains( indicatorA ) );
        assertTrue( indicators.contains( indicatorB ) );
    }
    
    public void testGetIndicatorByName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        int idA = indicatorStore.addIndicator( indicatorA );
        int idB = indicatorStore.addIndicator( indicatorB );
        
        assertNotNull( indicatorStore.getIndicator( idA ) );
        assertNotNull( indicatorStore.getIndicator( idB ) );
        
        indicatorA = indicatorStore.getIndicatorByName( "IndicatorA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorStore.getIndicatorByName( "IndicatorC" );
        assertNull( indicatorC );
    }    

    public void testGetIndicatorByAlternativeName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );

        int idA = indicatorStore.addIndicator( indicatorA );
        int idB = indicatorStore.addIndicator( indicatorB );
        
        assertNotNull( indicatorStore.getIndicator( idA ) );
        assertNotNull( indicatorStore.getIndicator( idB ) );
        
        indicatorA = indicatorStore.getIndicatorByAlternativeName( "AlternativeNameA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorStore.getIndicatorByAlternativeName( "AlternativeNameC" );
        assertNull( indicatorC );
    }
    
    public void testGetIndicatorByShortName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );
    
        int idA = indicatorStore.addIndicator( indicatorA );
        int idB = indicatorStore.addIndicator( indicatorB );
        
        assertNotNull( indicatorStore.getIndicator( idA ) );
        assertNotNull( indicatorStore.getIndicator( idB ) );
        
        indicatorA = indicatorStore.getIndicatorByShortName( "ShortNameA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorStore.getIndicatorByShortName( "ShortNameC" );
        assertNull( indicatorC );
    }    

    public void testGetIndicatorByCodeName()
        throws Exception
    {
        IndicatorType type = new IndicatorType( "IndicatorType", 100 );

        indicatorStore.addIndicatorType( type );
        
        Indicator indicatorA = createIndicator( 'A', type );
        Indicator indicatorB = createIndicator( 'B', type );
    
        int idA = indicatorStore.addIndicator( indicatorA );
        int idB = indicatorStore.addIndicator( indicatorB );
        
        assertNotNull( indicatorStore.getIndicator( idA ) );
        assertNotNull( indicatorStore.getIndicator( idB ) );
        
        indicatorA = indicatorStore.getIndicatorByCode( "CodeA" );
        assertNotNull( indicatorA );
        assertEquals( 'A', indicatorA );
        
        Indicator indicatorC = indicatorStore.getIndicatorByCode( "CodeC" );
        assertNull( indicatorC );
    }
}
