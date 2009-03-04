package org.hisp.dhis.completeness;

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

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriodType;
import org.hisp.dhis.reporttable.ReportTable;

import static org.hisp.dhis.system.util.ConversionUtils.getIdentifiers;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataSetCompletenessInternalProcessTest
    extends DhisConvenienceTest
{
    private DataSetCompletenessInternalProcess process;
    
    private DataSetCompletenessStore completenessStore;

    private DataSetCompletenessService completenessService;

    private CompleteDataSetRegistrationService registrationService;
    
    private PeriodType monthly;
    private PeriodType relative;
    
    private Period periodA;
    private Period periodB;
    private Period periodC;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;

    private DataSet dataSetA;
    
    private Collection<DataSet> dataSets;
    private Collection<Period> periods;
    private Collection<OrganisationUnit> units;
    
    private ReportTable reportTable;
    
    public void setUpTest()
    {
        process = (DataSetCompletenessInternalProcess) getBean( DataSetCompletenessInternalProcess.ID );
        
        completenessStore = (DataSetCompletenessStore) getBean( DataSetCompletenessStore.ID );
        
        completenessService = (DataSetCompletenessService) getBean( DataSetCompletenessService.ID );
        
        periodService = (PeriodService) getBean( PeriodService.ID );
        
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        
        dataSetService = (DataSetService) getBean( DataSetService.ID );
        
        registrationService = (CompleteDataSetRegistrationService) getBean( CompleteDataSetRegistrationService.ID );

        dataSets = new ArrayList<DataSet>();
        periods = new ArrayList<Period>();
        units = new ArrayList<OrganisationUnit>();
        
        monthly = new MonthlyPeriodType();
        relative = new RelativePeriodType();
        
        periodA = createPeriod( monthly, getDate( 2000, 1, 1 ), getDate( 2000, 1, 31 ) );
        periodB = createPeriod( monthly, getDate( 2000, 2, 1 ), getDate( 2000, 2, 28 ) );
        periodC = createPeriod( relative, getDate( 2000, 1, 1 ), getDate( 2000, 2, 28 ) );
        
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        
        periods.add( periodA );
        periods.add( periodB );
        periods.add( periodC );
        
        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B' );
        unitC = createOrganisationUnit( 'C' );
        
        unitB.setParent( unitA );
        unitC.setParent( unitA );
        
        unitA.getChildren().add( unitB );
        unitA.getChildren().add( unitC );
        
        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );
        organisationUnitService.addOrganisationUnit( unitC );

        units.add( unitA );
        units.add( unitB );
        units.add( unitC );
        
        dataSetA = createDataSet( 'A', monthly );
        
        dataSetA.getSources().add( unitA );
        dataSetA.getSources().add( unitB );
        dataSetA.getSources().add( unitC );
        
        dataSetService.addDataSet( dataSetA );
        
        dataSets.add( dataSetA );
        
        reportTable = new ReportTable();
        reportTable.setId( 1 );
    }
    
    public void testExportDataSetCompleteness()
    {
        registrationService.saveCompleteDataSetRegistration( new CompleteDataSetRegistration( dataSetA, periodA, unitB, null ) );
        registrationService.saveCompleteDataSetRegistration( new CompleteDataSetRegistration( dataSetA, periodA, unitC, null ) );
        registrationService.saveCompleteDataSetRegistration( new CompleteDataSetRegistration( dataSetA, periodB, unitB, null ) );
        registrationService.saveCompleteDataSetRegistration( new CompleteDataSetRegistration( dataSetA, periodB, unitA, null ) );
        
        DataSetCompletenessResult resultA = completenessService.getDataSetCompleteness( periodA, null, unitB, dataSetA );
        DataSetCompletenessResult resultB = completenessService.getDataSetCompleteness( periodA, null, unitC, dataSetA );
        DataSetCompletenessResult resultC = completenessService.getDataSetCompleteness( periodA, null, unitA, dataSetA );
        DataSetCompletenessResult resultD = completenessService.getDataSetCompleteness( periodB, null, unitB, dataSetA );
        DataSetCompletenessResult resultE = completenessService.getDataSetCompleteness( periodB, null, unitC, dataSetA );
        DataSetCompletenessResult resultF = completenessService.getDataSetCompleteness( periodB, null, unitA, dataSetA );
                
        assertEquals( new DataSetCompletenessResult( unitB.getName(), 1, 1, 0 ), resultA );
        assertEquals( new DataSetCompletenessResult( unitC.getName(), 1, 1, 0 ), resultB );
        assertEquals( new DataSetCompletenessResult( unitA.getName(), 3, 2, 0 ), resultC );
        assertEquals( new DataSetCompletenessResult( unitB.getName(), 1, 1, 0 ), resultD );
        assertEquals( new DataSetCompletenessResult( unitC.getName(), 1, 0, 0 ), resultE );
        assertEquals( new DataSetCompletenessResult( unitA.getName(), 3, 2, 0 ), resultF );
        
        process.exportDataSetCompleteness( getIdentifiers( DataSet.class, dataSets ),
            getIdentifiers( Period.class, periods ), getIdentifiers( OrganisationUnit.class, units ),  reportTable.getId() );
        
        assertEquals( 100.0, completenessStore.getPercentage( dataSetA.getId(), periodA.getId(), unitB.getId() ) );
        assertEquals( 100.0, completenessStore.getPercentage( dataSetA.getId(), periodA.getId(), unitC.getId() ) );
        assertEquals( 66.7, completenessStore.getPercentage( dataSetA.getId(), periodA.getId(), unitA.getId() ) );
        
        assertEquals( 100.0, completenessStore.getPercentage( dataSetA.getId(), periodB.getId(), unitB.getId() ) );
        assertEquals( 0.0, completenessStore.getPercentage( dataSetA.getId(), periodB.getId(), unitC.getId() ) );
        assertEquals( 66.7, completenessStore.getPercentage( dataSetA.getId(), periodB.getId(), unitA.getId() ) );
        
        assertEquals( 100.0, completenessStore.getPercentage( dataSetA.getId(), periodC.getId(), unitB.getId() ) );
        assertEquals( 50.0, completenessStore.getPercentage( dataSetA.getId(), periodC.getId(), unitC.getId() ) );
        assertEquals( 66.7, completenessStore.getPercentage( dataSetA.getId(), periodC.getId(), unitA.getId() ) );   
    }
}
