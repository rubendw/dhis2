package org.hisp.dhis.aggregation.batch.handler;

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
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataValueBatchHandlerTest
    extends DhisConvenienceTest
{
    private BatchHandlerFactory batchHandlerFactory;
    
    private BatchHandler batchHandler;
    
    private DataElement dataElementA;
    
    private DataElementCategoryOptionCombo categoryOptionComboA;
    
    private PeriodType periodTypeA;
    
    private Period periodA;
    private Period periodB;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    
    private DataValue dataValueA;
    private DataValue dataValueB;
    private DataValue dataValueC;
    private DataValue dataValueD;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------
    
    public void setUpTest()
    {
        dataValueService = (DataValueService) getBean( DataValueService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( BatchHandlerFactory.ID );
        
        batchHandler = batchHandlerFactory.createBatchHandler( DataValueBatchHandler.class );
        
        dataElementA = createDataElement( 'A' );
        
        dataElementService.addDataElement( dataElementA );        
        
        categoryOptionComboA = categoryOptionComboService.getDefaultDataElementCategoryOptionCombo();
        
        periodTypeA = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        periodA = createPeriod( periodTypeA, getDate( 2000, 0, 1 ), getDate( 2000, 0, 31 ) );
        periodB = createPeriod( periodTypeA, getDate( 2000, 1, 1 ), getDate( 2000, 2, 28 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
                
        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B' );
        
        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );        
        
        dataValueA = createDataValue( dataElementA, periodA, unitA, "10", categoryOptionComboA );
        dataValueB = createDataValue( dataElementA, periodA, unitB, "10", categoryOptionComboA );
        dataValueC = createDataValue( dataElementA, periodB, unitA, "10", categoryOptionComboA );
        dataValueD = createDataValue( dataElementA, periodB, unitB, "10", categoryOptionComboA );     
        
        batchHandler.init();
    }
    
    public void tearDownTest()
    {
        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testAddObject()
    {
        batchHandler.addObject( dataValueA );
        batchHandler.addObject( dataValueB );
        batchHandler.addObject( dataValueC );
        batchHandler.addObject( dataValueD );
        
        batchHandler.flush();
        
        Collection<DataValue> values = dataValueService.getAllDataValues();
        
        assertNotNull( values );
        assertEquals( 4, values.size() );
        
        assertTrue( values.contains( dataValueA ) );
        assertTrue( values.contains( dataValueB ) );
        assertTrue( values.contains( dataValueC ) );
        assertTrue( values.contains( dataValueD ) );
    }
    
    public void testInsertObject()
    {
        batchHandler.insertObject( dataValueA, false );
        batchHandler.insertObject( dataValueB, false );
        batchHandler.insertObject( dataValueC, false );
        batchHandler.insertObject( dataValueD, false );
        
        assertNotNull( dataValueService.getDataValue( unitA, dataElementA, periodA, categoryOptionComboA ) );
        assertNotNull( dataValueService.getDataValue( unitB, dataElementA, periodA, categoryOptionComboA ) );
        assertNotNull( dataValueService.getDataValue( unitA, dataElementA, periodB, categoryOptionComboA ) );
        assertNotNull( dataValueService.getDataValue( unitB, dataElementA, periodB, categoryOptionComboA ) );
    }
    
    public void testUpdateObject()
    {
        dataValueService.addDataValue( dataValueA );
        
        dataValueA.setValue( "20" );
        
        batchHandler.updateObject( dataValueA );
        
        dataValueA = dataValueService.getDataValue( unitA, dataElementA, periodA, categoryOptionComboA );
        
        assertEquals( "20", dataValueA.getValue() );
    }
    
    public void testObjectExists()
    {
        dataValueService.addDataValue( dataValueA );
        dataValueService.addDataValue( dataValueC );
        
        assertTrue( batchHandler.objectExists( dataValueA ) );
        assertTrue( batchHandler.objectExists( dataValueC ) );
        
        assertFalse( batchHandler.objectExists( dataValueB ) );
        assertFalse( batchHandler.objectExists( dataValueD ) );
    }
}
