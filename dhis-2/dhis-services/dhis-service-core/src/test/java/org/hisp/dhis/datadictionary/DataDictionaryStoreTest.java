package org.hisp.dhis.datadictionary;

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
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataDictionaryStoreTest
    extends DhisConvenienceTest
{
    private DataDictionaryStore dataDictionaryStore;

    private DataElement dataElementA;
    private DataElement dataElementB;
    
    private IndicatorType indicatorType;
    
    private Indicator indicatorA;
    private Indicator indicatorB;
    
    private DataDictionary dataDictionaryA;
    private DataDictionary dataDictionaryB;
        
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        dataDictionaryStore = (DataDictionaryStore) getBean( DataDictionaryStore.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        
        dataDictionaryA = createDataDictionary( 'A' );
        dataDictionaryB = createDataDictionary( 'B' );
        
        dataElementA = createExtendedDataElement( 'A' );
        dataElementB = createExtendedDataElement( 'B' );
        
        indicatorType = createIndicatorType( 'A' );
        
        indicatorService.addIndicatorType( indicatorType );
        
        indicatorA = createExtendedIndicator( 'A', indicatorType );
        indicatorB = createExtendedIndicator( 'B', indicatorType );
    }

    // -------------------------------------------------------------------------
    // DataDictionary tests
    // -------------------------------------------------------------------------
    
    public void testSaveGetDataDictionary()
    {
        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );
        
        indicatorService.addIndicator( indicatorA );
        indicatorService.addIndicator( indicatorB );
                
        dataDictionaryA.getDataElements().add( dataElementA );
        dataDictionaryA.getDataElements().add( dataElementB );
        
        dataDictionaryA.getIndicators().add( indicatorA );
        dataDictionaryA.getIndicators().add( indicatorB );
                
        int id = dataDictionaryStore.saveDataDictionary( dataDictionaryA );
        
        dataDictionaryA = dataDictionaryStore.getDataDictionary( id );
        
        assertEquals( dataDictionaryA.getName(), "DataDictionaryA" );
        assertEquals( dataDictionaryA.getDescription(), "DescriptionA" );
        assertEquals( dataDictionaryA.getRegion(), "RegionA" );
        assertEquals( dataDictionaryA.getDataElements().size(), 2 );
        
        assertTrue( dataDictionaryA.getDataElements().contains( dataElementA ) );
        assertTrue( dataDictionaryA.getDataElements().contains( dataElementB ) );
        
        assertTrue( dataDictionaryA.getIndicators().contains( indicatorA ) );
        assertTrue( dataDictionaryA.getIndicators().contains( indicatorB ) );
                
        dataDictionaryA.setName( "DataDictionaryB" );
        dataDictionaryA.setDescription( "DescriptionB" );
        dataDictionaryA.setRegion( "RegionB" );
        
        dataDictionaryStore.saveDataDictionary( dataDictionaryA );
        
        dataDictionaryA = dataDictionaryStore.getDataDictionary( id );
        
        assertEquals( dataDictionaryA.getName(), "DataDictionaryB" );
        assertEquals( dataDictionaryA.getDescription(), "DescriptionB" );
        assertEquals( dataDictionaryA.getRegion(), "RegionB" );
    }

    public void testGetDataDictionaryByName()
    {
        dataDictionaryStore.saveDataDictionary( dataDictionaryA );
        dataDictionaryStore.saveDataDictionary( dataDictionaryB );
        
        dataDictionaryA = dataDictionaryStore.getDataDictionaryByName( "DataDictionaryA" );
        
        assertNotNull( dataDictionaryA );
        assertEquals( dataDictionaryA.getName(), "DataDictionaryA" );
        assertEquals( dataDictionaryA.getDescription(), "DescriptionA" );
    }

    public void testDeleteDataDictionary()
    {
        int idA = dataDictionaryStore.saveDataDictionary( dataDictionaryA );
        int idB = dataDictionaryStore.saveDataDictionary( dataDictionaryB );
        
        assertNotNull( dataDictionaryStore.getDataDictionary( idA ) );
        assertNotNull( dataDictionaryStore.getDataDictionary( idB ) );
        
        dataDictionaryStore.deleteDataDictionary( dataDictionaryA );

        assertNull( dataDictionaryStore.getDataDictionary( idA ) );
        assertNotNull( dataDictionaryStore.getDataDictionary( idB ) );

        dataDictionaryStore.deleteDataDictionary( dataDictionaryB );

        assertNull( dataDictionaryStore.getDataDictionary( idA ) );
        assertNull( dataDictionaryStore.getDataDictionary( idB ) );
    }
    
    public void testGetAllDataDictionaries()
    {
        dataDictionaryStore.saveDataDictionary( dataDictionaryA );
        dataDictionaryStore.saveDataDictionary( dataDictionaryB );
        
        Collection<DataDictionary> dictionaries = dataDictionaryStore.getAllDataDictionaries();
        
        assertTrue( dictionaries.size() == 2 );
        assertTrue( dictionaries.contains( dataDictionaryA ) );
        assertTrue( dictionaries.contains( dataDictionaryB ) );        
    }

    // -------------------------------------------------------------------------
    // ExtendedDataElement tests
    // -------------------------------------------------------------------------

    public void testAddGetExtendedDataElement()
    {
        int id = dataElementService.addDataElement( dataElementA );
        
        dataElementA = dataElementService.getDataElement( id );
        
        assertEquals( dataElementA.getName(), "DataElementA" );
        assertNotNull( dataElementA.getExtended() );
        assertEquals( dataElementA.getExtended().getMnemonic(), "MnemonicA" );
        assertEquals( dataElementA.getExtended().getDataDomain(), "DataDomainA" );
        assertEquals( dataElementA.getExtended().getLocation(), "LocationA" );
    }
    
    public void testUpdateExtendedDataElement()
    {
        int id = dataElementService.addDataElement( dataElementA );
        
        dataElementA = dataElementService.getDataElement( id );
        
        assertEquals( dataElementA.getName(), "DataElementA" );
        assertNotNull( dataElementA.getExtended() );
        assertEquals( dataElementA.getExtended().getMnemonic(), "MnemonicA" );
        assertEquals( dataElementA.getExtended().getDataDomain(), "DataDomainA" );
        assertEquals( dataElementA.getExtended().getLocation(), "LocationA" );
        
        dataElementA.getExtended().setMnemonic( "MnemonicB" );
        dataElementA.getExtended().setDataDomain( "DataDomainB" );
        dataElementA.getExtended().setLocation( "LocationB" );
        
        dataElementService.updateDataElement( dataElementA );
        
        dataElementA = dataElementService.getDataElement( id );

        assertEquals( dataElementA.getName(), "DataElementA" );
        assertNotNull( dataElementA.getExtended() );
        assertEquals( dataElementA.getExtended().getMnemonic(), "MnemonicB" );
        assertEquals( dataElementA.getExtended().getDataDomain(), "DataDomainB" );
        assertEquals( dataElementA.getExtended().getLocation(), "LocationB" );
    }
    
    public void testDeleteExtendedDataElement()
        throws Exception
    {
        int idA = dataElementService.addDataElement( dataElementA );
        int idB = dataElementService.addDataElement( dataElementB );
        
        assertNotNull( dataElementService.getDataElement( idA ) );
        assertNotNull( dataElementService.getDataElement( idB ) );
        
        dataElementService.deleteDataElement( dataElementA );

        assertNull( dataElementService.getDataElement( idA ) );
        assertNotNull( dataElementService.getDataElement( idB ) );

        dataElementService.deleteDataElement( dataElementB );

        assertNull( dataElementService.getDataElement( idA ) );
        assertNull( dataElementService.getDataElement( idB ) );
    }

    // -------------------------------------------------------------------------
    // ExtendedIndicator tests
    // -------------------------------------------------------------------------

    public void testAddGetExtendedIndicator()
    {
        int id = indicatorService.addIndicator( indicatorA );
        
        indicatorA = indicatorService.getIndicator( id );
        
        assertEquals( indicatorA.getName(), "IndicatorA" );
        assertNotNull( indicatorA.getExtended() );
        assertEquals( indicatorA.getExtended().getMnemonic(), "MnemonicA" );
        assertEquals( indicatorA.getExtended().getDataDomain(), "DataDomainA" );
        assertEquals( indicatorA.getExtended().getLocation(), "LocationA" );
    }
    
    public void testUpdateExtendedIndicator()
    {
        int id = indicatorService.addIndicator( indicatorA );
        
        indicatorA = indicatorService.getIndicator( id );
        
        assertEquals( indicatorA.getName(), "IndicatorA" );
        assertNotNull( indicatorA.getExtended() );
        assertEquals( indicatorA.getExtended().getMnemonic(), "MnemonicA" );
        assertEquals( indicatorA.getExtended().getDataDomain(), "DataDomainA" );
        assertEquals( indicatorA.getExtended().getLocation(), "LocationA" );
        
        indicatorA.getExtended().setMnemonic( "MnemonicB" );
        indicatorA.getExtended().setDataDomain( "DataDomainB" );
        indicatorA.getExtended().setLocation( "LocationB" );
        
        indicatorService.updateIndicator( indicatorA );
        
        indicatorA = indicatorService.getIndicator( id );

        assertEquals( indicatorA.getName(), "IndicatorA" );
        assertNotNull( indicatorA.getExtended() );
        assertEquals( indicatorA.getExtended().getMnemonic(), "MnemonicB" );
        assertEquals( indicatorA.getExtended().getDataDomain(), "DataDomainB" );
        assertEquals( indicatorA.getExtended().getLocation(), "LocationB" );
    }
    
    public void testDeleteExtendedIndicator()
        throws Exception
    {
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
}
