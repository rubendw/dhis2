package org.hisp.dhis.aggregation;

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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.system.util.UUIdUtils;
import org.hisp.dhis.transaction.TransactionManager;

/**
 * @author Lars Helge Overland
 * @version $Id: AggregationStoreTest.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class AggregationStoreTest
    extends DhisSpringTest
{
    private TransactionManager transactionManager;

    private AggregationStore aggregationStore;

    private DataElementService dataElementService;
    
    private DataElementCategoryOptionComboService categoryOptionComboService;

    private PeriodService periodService;

    private SourceStore sourceStore;

    private DataValueService dataValueService;    
    
    private Calendar calendar;
    
    private DataElementCategoryOptionCombo optionCombo;
    
    private StatementManager statementManager;

    public void setUpTest()
        throws Exception
    {
        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        aggregationStore = (AggregationStore) getBean( AggregationStore.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );

        periodService = (PeriodService) getBean( PeriodService.ID );

        sourceStore = (SourceStore) getBean( SourceStore.ID );

        dataValueService = (DataValueService) getBean( DataValueService.ID );

        calendar = Calendar.getInstance();

        optionCombo = new DataElementCategoryOptionCombo();
        
        categoryOptionComboService.addDataElementCategoryOptionCombo( optionCombo );
        
        statementManager = (StatementManager) getBean( StatementManager.ID );
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private Date getDay( int day )
    {
        calendar.clear();
        calendar.set( Calendar.DAY_OF_YEAR, day );

        return calendar.getTime();
    }
    
    private Collection<Integer> getPeriodIds( Collection<Period> periods )
    {
        Collection<Integer> periodIds = new ArrayList<Integer>();
        
        for ( Period period : periods )
        {
            periodIds.add( period.getId() );
        }
        
        return periodIds;
    }

    private DataElement createDataElement( char uniqueCharacter )
    {
        DataElement dataElement = new DataElement();

        dataElement.setUuid( UUIdUtils.getUUId() );
        dataElement.setName( "DataElement" + uniqueCharacter );
        dataElement.setAlternativeName( "AlternativeName" + uniqueCharacter );
        dataElement.setShortName( "DE" + uniqueCharacter );
        dataElement.setCode( "Code" + uniqueCharacter );
        dataElement.setDescription( "DataElementDescription" + uniqueCharacter );
        dataElement.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement.setType( DataElement.TYPE_INT );

        return dataElement;
    }

    // -------------------------------------------------------------------------
    // AggregationStore test
    // -------------------------------------------------------------------------

    public void testRetrieveDataValuesDataElementSourcesDates()
        throws Exception
    {
        DataElement dataElementA = createDataElement( 'A' );
        
        PeriodType periodType = periodService.getAllPeriodTypes().iterator().next();
        
        Period periodA = new Period( periodType, getDay( 5 ), getDay( 6 ) );
        Period periodB = new Period( periodType, getDay( 6 ), getDay( 7 ) );
        Period periodC = new Period( periodType, getDay( 7 ), getDay( 8 ) );
        Period periodD = new Period( periodType, getDay( 8 ), getDay( 9 ) );
        Period periodE = new Period( periodType, getDay( 9 ), getDay( 10 ) );
        Period periodF = new Period( periodType, getDay( 5 ), getDay( 7 ) );
        Period periodG = new Period( periodType, getDay( 8 ), getDay( 10 ) );
        Period periodH = new Period( periodType, getDay( 5 ), getDay( 10 ) );

        Source sourceA = new OrganisationUnit( "nameA", null, "shortNameA", "codeA", null, null, false, null );
        Source sourceB = new OrganisationUnit( "nameB", null, "shortNameB", "codeB", null, null, false, null );
        Source sourceC = new OrganisationUnit( "nameC", null, "shortNameC", "codeC", null, null, false, null );

        DataValue dataValueA = new DataValue( dataElementA, periodA, sourceA, optionCombo );
        dataValueA.setValue( "1" );
        DataValue dataValueB = new DataValue( dataElementA, periodB, sourceB, optionCombo );
        dataValueB.setValue( "2" );
        DataValue dataValueC = new DataValue( dataElementA, periodC, sourceC, optionCombo );
        dataValueC.setValue( "3" );
        DataValue dataValueD = new DataValue( dataElementA, periodD, sourceA, optionCombo );
        dataValueD.setValue( "4" );
        DataValue dataValueE = new DataValue( dataElementA, periodE, sourceB, optionCombo );
        dataValueE.setValue( "5" );
        DataValue dataValueF = new DataValue( dataElementA, periodF, sourceC, optionCombo );
        dataValueF.setValue( "6" );
        DataValue dataValueG = new DataValue( dataElementA, periodG, sourceA, optionCombo );
        dataValueG.setValue( "7" );
        DataValue dataValueH = new DataValue( dataElementA, periodH, sourceB, optionCombo );
        dataValueH.setValue( "8" );
        
        transactionManager.enter();

        dataElementService.addDataElement( dataElementA );

        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );
        periodService.addPeriod( periodC );
        periodService.addPeriod( periodD );
        periodService.addPeriod( periodE );
        periodService.addPeriod( periodF );
        periodService.addPeriod( periodG );
        periodService.addPeriod( periodH );

        sourceStore.addSource( sourceA );
        sourceStore.addSource( sourceB );
        sourceStore.addSource( sourceC );

        dataValueService.addDataValue( dataValueA );
        dataValueService.addDataValue( dataValueB );
        dataValueService.addDataValue( dataValueC );
        dataValueService.addDataValue( dataValueD );
        dataValueService.addDataValue( dataValueE );
        dataValueService.addDataValue( dataValueF );
        dataValueService.addDataValue( dataValueG );
        dataValueService.addDataValue( dataValueH );

        transactionManager.leave();

        Date startDate = getDay( 6 );
        Date endDate = getDay( 9 );

        Collection<Integer> sources = new HashSet<Integer>();
        sources.add( new Integer( sourceA.getId() ) );
        sources.add( new Integer( sourceB.getId() ) );
        sources.add( new Integer( sourceC.getId() ) );

        Collection<Integer> periods = getPeriodIds( periodService.getIntersectingPeriods( startDate, endDate ) );
        
        statementManager.initialise();
        
        Collection<DataValue> dataValues3 = aggregationStore.getDataValues( sources, dataElementA.getId(), optionCombo.getId(), periods );

        assertEquals( dataValues3.size(), 6 );

        Collection<DataValue> dataValues4 = aggregationStore.getDataValues( sourceA.getId(), dataElementA.getId(), optionCombo.getId(), periods );

        assertEquals( dataValues4.size(), 2 );        

        statementManager.destroy();
    }
}
