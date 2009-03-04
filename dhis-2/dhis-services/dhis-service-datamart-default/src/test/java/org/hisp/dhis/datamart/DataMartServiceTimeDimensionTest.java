package org.hisp.dhis.datamart;

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

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;

/**
 * @author Lars Helge Overland
 * @version $Id: DataMartServiceTest.java 5164 2008-05-16 12:23:58Z larshelg $
 */
public class DataMartServiceTimeDimensionTest
    extends DhisConvenienceTest
{
    private DataMartInternalProcess dataMartInternalProcess;

    private DataMartStore dataMartStore;

    private DataElementCategoryCombo categoryCombo;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private Collection<Integer> dataElementIds;
    private Collection<Integer> indicatorIds;
    private Collection<Integer> periodIds;
    private Collection<Integer> organisationUnitIds;
    
    @Override
    public void setUpTest()
    {
        dataMartInternalProcess = (DataMartInternalProcess) getBean( DataMartInternalProcess.ID );
        
        dataMartStore = (DataMartStore) getBean( DataMartStore.ID );

        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        periodService = (PeriodService) getBean( PeriodService.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        dataValueService = (DataValueService) getBean( DataValueService.ID );

        categoryCombo = categoryComboService.getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );
        
        categoryOptionCombo = categoryOptionComboService.getDefaultDataElementCategoryOptionCombo();        

        dataElementIds = new HashSet<Integer>();
        indicatorIds = new HashSet<Integer>();
        periodIds = new HashSet<Integer>();
        organisationUnitIds = new HashSet<Integer>();        
    }
    
    public void testAverageIntDataElement()
    {
        DataElement dataElement = createDataElement( 'A', categoryCombo );
        
        dataElement.setType( DataElement.TYPE_INT );
        dataElement.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_AVERAGE );
        
        dataElementIds.add( dataElementService.addDataElement( dataElement ) );

        Period dataPeriod = createPeriod( new YearlyPeriodType(), getDate( 2000, 1, 1 ), getDate( 2000, 12, 31 ) );
        
        OrganisationUnit unit = createOrganisationUnit( 'A' );
        
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unit ) );

        dataValueService.addDataValue( createDataValue( dataElement, dataPeriod, unit, "100", categoryOptionCombo ) );

        Period periodA = createPeriod( new MonthlyPeriodType(), getDate( 2000, 3, 1 ), getDate( 2000, 3, 31 ) );
        Period periodB = createPeriod( new MonthlyPeriodType(), getDate( 2000, 4, 1 ), getDate( 2000, 4, 30 ) );
        Period periodC = createPeriod( new QuarterlyPeriodType(), getDate( 2000, 3, 1 ), getDate( 2000, 5, 31 ) );
        
        periodIds.add( periodService.addPeriod( periodA ) );
        periodIds.add( periodService.addPeriod( periodB ) );
        periodIds.add( periodService.addPeriod( periodC ) );
        
        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );
        
        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElement, periodA, unit ) );
        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElement, periodB, unit ) );
        assertEquals( 100.0, dataMartStore.getAggregatedValue( dataElement, periodC, unit ) );
    }
    
    public void testSumIntDataElement()
    {
        DataElement dataElement = createDataElement( 'A', categoryCombo );
        
        dataElement.setType( DataElement.TYPE_INT );
        dataElement.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        dataElementIds.add( dataElementService.addDataElement( dataElement ) );        

        Period dataPeriodA = createPeriod( new MonthlyPeriodType(), getDate( 2000, 3, 1 ), getDate( 2000, 3, 31 ) );
        Period dataPeriodB = createPeriod( new MonthlyPeriodType(), getDate( 2000, 4, 1 ), getDate( 2000, 4, 30 ) );
        Period dataPeriodC = createPeriod( new MonthlyPeriodType(), getDate( 2000, 5, 1 ), getDate( 2000, 5, 31 ) );

        OrganisationUnit unit = createOrganisationUnit( 'A' );
        
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unit ) );

        dataValueService.addDataValue( createDataValue( dataElement, dataPeriodA, unit, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElement, dataPeriodB, unit, "20", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElement, dataPeriodC, unit, "30", categoryOptionCombo ) );

        Period periodA = createPeriod( new YearlyPeriodType(), getDate( 2000, 1, 1 ), getDate( 2000, 12, 31 ) );
        
        periodIds.add( periodService.addPeriod( periodA ) );

        dataMartInternalProcess.export( dataElementIds, indicatorIds, periodIds, organisationUnitIds );
        
        assertEquals( 60.0, dataMartStore.getAggregatedValue( dataElement, periodA, unit ) );
    }
}
