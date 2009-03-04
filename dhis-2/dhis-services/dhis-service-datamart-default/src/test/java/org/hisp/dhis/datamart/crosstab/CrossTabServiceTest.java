package org.hisp.dhis.datamart.crosstab;

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

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.CrossTabDataValue;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.WeeklyPeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: CrossTabServiceTest.java 5510 2008-07-30 16:30:27Z larshelg $
 */
public class CrossTabServiceTest
    extends DhisConvenienceTest
{
    private CrossTabService crossTabService;
    
    private DataMartStore dataMartStore;
    
    private Iterator<Period> generatedPeriods;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        crossTabService = (CrossTabService) getBean( CrossTabService.ID );
        
        dataMartStore = (DataMartStore) getBean( DataMartStore.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        
        dataValueService = (DataValueService) getBean( DataValueService.ID );
        
        Calendar calendar = Calendar.getInstance();
        
        calendar.set( 2007, 0, 1 );
        
        WeeklyPeriodType periodType = (WeeklyPeriodType) periodService.getPeriodTypeByName( WeeklyPeriodType.NAME );
        
        Period period = createPeriod( periodType, calendar.getTime(), calendar.getTime() );

        generatedPeriods = periodType.generatePeriods( period ).iterator();        
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testPopulateCrossTabTable()
    {
        // ---------------------------------------------------------------------
        // Setting up test data
        // ---------------------------------------------------------------------

        DataElementCategoryOption categoryOptionA = new DataElementCategoryOption( "Male" );
        DataElementCategoryOption categoryOptionB = new DataElementCategoryOption( "Female" );
        
        categoryOptionService.addDataElementCategoryOption( categoryOptionA );
        categoryOptionService.addDataElementCategoryOption( categoryOptionB );
        
        DataElementCategory categoryA = new DataElementCategory( "Gender" );
        categoryA.getCategoryOptions().add( categoryOptionA );
        categoryA.getCategoryOptions().add( categoryOptionB );
        
        categoryService.addDataElementCategory( categoryA );
        
        DataElementCategoryCombo categoryComboA = new DataElementCategoryCombo( "Gender" );
        categoryComboA.getCategories().add( categoryA );        
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        categoryOptionComboService.generateOptionCombos( categoryComboA );
        
        Collection<DataElementCategoryOptionCombo> categoryOptionCombos = categoryOptionComboService.getAllDataElementCategoryOptionCombos();
                
        Character[] characters = { 'A', 'B', 'C', 'D', 'E' };
        
        Collection<Integer> periodIds = new HashSet<Integer>();
        Collection<Integer> organisationUnitIds = new HashSet<Integer>();
        
        Collection<DataElement> dataElements = new HashSet<DataElement>();
        Collection<Period> periods = new HashSet<Period>();
        Collection<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();
        
        for ( Character character : characters )
        {
            DataElement dataElement = createDataElement( character, categoryComboA );
            Period period = generatedPeriods.next();
            OrganisationUnit organisationUnit = createOrganisationUnit( character );
            
            dataElements.add( dataElement );
            periods.add( period );
            organisationUnits.add( organisationUnit );
            
            dataElementService.addDataElement( dataElement );
            periodIds.add( periodService.addPeriod( period ) );
            organisationUnitIds.add( organisationUnitService.addOrganisationUnit( organisationUnit ) );
        }
        
        Collection<Operand> operands = categoryOptionComboService.getOperands( dataElements );
        
        for ( DataElement dataElement : dataElements )
        {
            for ( DataElementCategoryOptionCombo categoryOptionCombo : categoryOptionCombos )
            {
                for ( Period period : periods )
                {
                    for ( OrganisationUnit organisationUnit : organisationUnits )
                    {
                        dataValueService.addDataValue( createDataValue( dataElement, period, organisationUnit, "10", categoryOptionCombo ) );
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // Populating crosstab table
        // ---------------------------------------------------------------------

        crossTabService.populateCrossTabTable( operands, periodIds, organisationUnitIds, null );

        // ---------------------------------------------------------------------
        // Asserting retrieved crosstab values
        // ---------------------------------------------------------------------

        Collection<CrossTabDataValue> values = dataMartStore.getCrossTabDataValues( operands, periodIds, organisationUnitIds );
        
        assertNotNull( values );
        
        assertEquals( 25, values.size() );
        
        for ( CrossTabDataValue crossTabValue : values )
        {
            assertTrue( crossTabValue.getPeriodId() != 0 );
            assertTrue( crossTabValue.getSourceId() != 0 );
            
            assertNotNull( crossTabValue.getValueMap() );
            
            assertEquals( 10, crossTabValue.getValueMap().size() );
            
            for ( String value : crossTabValue.getValueMap().values() )
            {
                assertEquals( "10", value );
            }
        }
    }
}

