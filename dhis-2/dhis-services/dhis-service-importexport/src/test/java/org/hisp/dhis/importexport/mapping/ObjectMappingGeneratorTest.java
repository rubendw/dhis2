package org.hisp.dhis.importexport.mapping;

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

import java.util.Map;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementService;
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
public class ObjectMappingGeneratorTest
    extends DhisConvenienceTest 
{
    private ObjectMappingGenerator objectMappingGenerator;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        objectMappingGenerator = (ObjectMappingGenerator) getBean( ObjectMappingGenerator.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
    }
    
    public void tearDownTest()
    {
        NameMappingUtil.clearMapping();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testGetDataElementMapping()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        
        dataElementA.setId( 'A' );
        dataElementB.setId( 'B' );
        
        NameMappingUtil.addDataElementMapping( dataElementA.getId(), dataElementA.getName() );
        NameMappingUtil.addDataElementMapping( dataElementB.getId(), dataElementB.getName() );
        
        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );
        
        Map<Object, Integer> mapping = objectMappingGenerator.getDataElementMapping( false );
        
        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( idA ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( idB ) );
        
        mapping = objectMappingGenerator.getDataElementMapping( true );

        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( 'A' ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( 'B' ) );
    }
    
    public void testGetCategoryOptionComboMapping()
    {
        DataElementCategoryCombo categoryComboA = new DataElementCategoryCombo( "CategoryComboA" );
        
        categoryComboService.addDataElementCategoryCombo( categoryComboA );
        
        DataElementCategoryOption categoryOptionA = new DataElementCategoryOption( "CategoryOptionA" );
        DataElementCategoryOption categoryOptionB = new DataElementCategoryOption( "CategoryOptionB" );
        DataElementCategoryOption categoryOptionC = new DataElementCategoryOption( "CategoryOptionC" );
        DataElementCategoryOption categoryOptionD = new DataElementCategoryOption( "CategoryOptionD" );
        
        categoryOptionService.addDataElementCategoryOption( categoryOptionA );
        categoryOptionService.addDataElementCategoryOption( categoryOptionB );
        categoryOptionService.addDataElementCategoryOption( categoryOptionC );
        categoryOptionService.addDataElementCategoryOption( categoryOptionD );
        
        DataElementCategoryOptionCombo categoryOptionComboA = new DataElementCategoryOptionCombo();
        categoryOptionComboA.setId( 'A' );
        categoryOptionComboA.setCategoryCombo( categoryComboA );
        categoryOptionComboA.getCategoryOptions().add( categoryOptionA );
        categoryOptionComboA.getCategoryOptions().add( categoryOptionB );

        DataElementCategoryOptionCombo categoryOptionComboB = new DataElementCategoryOptionCombo();
        categoryOptionComboB.setId( 'B' );
        categoryOptionComboB.setCategoryCombo( categoryComboA );
        categoryOptionComboB.getCategoryOptions().add( categoryOptionC );
        categoryOptionComboB.getCategoryOptions().add( categoryOptionD );
        
        NameMappingUtil.addCategoryOptionComboMapping( categoryOptionComboA.getId(), categoryOptionComboA );
        NameMappingUtil.addCategoryOptionComboMapping( categoryOptionComboB.getId(), categoryOptionComboB );        

        int idA = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboA );
        int idB = categoryOptionComboService.addDataElementCategoryOptionCombo( categoryOptionComboB );
        
        Map<Object, Integer> mapping = objectMappingGenerator.getCategoryOptionComboMapping( false );

        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( idA ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( idB ) );

        mapping = objectMappingGenerator.getCategoryOptionComboMapping( true );

        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( 'A' ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( 'B' ) );
    }
    
    public void testGetPeriodMapping()
    {        
        PeriodType periodTypeA = periodService.getPeriodTypeByName( MonthlyPeriodType.NAME );
        
        Period periodA = createPeriod( periodTypeA, getDate( 1, 0, 2000 ), getDate( 31, 0, 2000 ) );
        Period periodB = createPeriod( periodTypeA, getDate( 1, 1, 2000 ), getDate( 28, 1, 2000 ) );
        
        periodA.setId( 'A' );
        periodB.setId( 'B' );
        
        NameMappingUtil.addPeriodMapping( periodA.getId(), periodA );
        NameMappingUtil.addPeriodMapping( periodB.getId(), periodB );

        int idA = periodService.addPeriod( periodA );
        int idB = periodService.addPeriod( periodB );
        
        Map<Object, Integer> mapping = objectMappingGenerator.getPeriodMapping( false );

        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( idA ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( idB ) );
        
        mapping = objectMappingGenerator.getPeriodMapping( true );
        
        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( 'A' ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( 'B' ) );
    }
    
    public void testOrganisationUnitMapping()
    {
        OrganisationUnit organisationUnitA = createOrganisationUnit( 'A' );
        OrganisationUnit organisationUnitB = createOrganisationUnit( 'B' );
        
        organisationUnitA.setId( 'A' );
        organisationUnitB.setId( 'B' );        

        NameMappingUtil.addOrganisationUnitMapping( organisationUnitA.getId(), organisationUnitA.getName() );
        NameMappingUtil.addOrganisationUnitMapping( organisationUnitB.getId(), organisationUnitB.getName() );

        int idA = organisationUnitService.addOrganisationUnit( organisationUnitA );
        int idB = organisationUnitService.addOrganisationUnit( organisationUnitB );

        Map<Object, Integer> mapping = objectMappingGenerator.getOrganisationUnitMapping( false );

        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( idA ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( idB ) );
        
        mapping = objectMappingGenerator.getOrganisationUnitMapping( true );
        
        assertEquals( mapping.get( Integer.valueOf( 'A' ) ), Integer.valueOf( 'A' ) );
        assertEquals( mapping.get( Integer.valueOf( 'B' ) ), Integer.valueOf( 'B' ) );        
    }
    
    public void testOrganisationUnitMappingWithUUID()
    {
        OrganisationUnit organisationUnitA = createOrganisationUnit( 'A' );
        OrganisationUnit organisationUnitB = createOrganisationUnit( 'B' );
        
        NameMappingUtil.addOrganisationUnitMapping( organisationUnitA.getUuid(), organisationUnitA.getName() );
        NameMappingUtil.addOrganisationUnitMapping( organisationUnitB.getUuid(), organisationUnitB.getName() );

        int idA = organisationUnitService.addOrganisationUnit( organisationUnitA );
        int idB = organisationUnitService.addOrganisationUnit( organisationUnitB );

        Map<Object, Integer> mapping = objectMappingGenerator.getOrganisationUnitMapping( false );

        assertEquals( mapping.get( organisationUnitA.getUuid() ), Integer.valueOf( idA ) );
        assertEquals( mapping.get( organisationUnitB.getUuid() ), Integer.valueOf( idB ) );
        
        mapping = objectMappingGenerator.getOrganisationUnitMapping( true );
        
        assertEquals( mapping.get( organisationUnitA.getUuid() ), Integer.valueOf( -1 ) );
        assertEquals( mapping.get( organisationUnitB.getUuid() ), Integer.valueOf( -1 ) );
    }
}
