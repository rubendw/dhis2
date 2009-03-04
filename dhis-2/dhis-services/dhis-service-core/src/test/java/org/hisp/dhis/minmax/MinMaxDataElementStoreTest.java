package org.hisp.dhis.minmax;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.source.DummySource;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;

/**
 * @author Kristian Nordal
 * @version $Id: MinMaxDataElementStoreTest.java 5012 2008-04-24 21:14:40Z larshelg $
 */
public class MinMaxDataElementStoreTest
    extends DhisSpringTest
{
    private SourceStore sourceStore;

    private DataElementService dataElementService;

    private MinMaxDataElementStore minMaxDataElementStore;

    public void setUpTest()
        throws Exception
    {
        sourceStore = (SourceStore) getBean( SourceStore.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        minMaxDataElementStore = (MinMaxDataElementStore) getBean( MinMaxDataElementStore.ID );
    }

    public void tearDownTest()
        throws Exception
    {
        sourceStore = null;

        dataElementService = null;
    }

    public void testBasic()
        throws Exception
    {
        Source source1 = new DummySource("Source1name");
        Source source2 = new DummySource("Source2name");

        sourceStore.addSource( source1 );
        sourceStore.addSource( source2 );

        DataElement dataElement1 = new DataElement();
        dataElement1.setName( "DE1name" );
        dataElement1.setShortName( "DE1sname" );
        dataElement1.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement1.setType( DataElement.TYPE_INT );
        
        DataElement dataElement2 = new DataElement();
        dataElement2.setName( "DE2name" );
        dataElement2.setShortName( "DE2sname" );
        dataElement2.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement2.setType( DataElement.TYPE_INT );

        DataElement dataElement3 = new DataElement();
        dataElement3.setName( "DE3name" );
        dataElement3.setShortName( "DE3sname" );
        dataElement3.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement3.setType( DataElement.TYPE_INT );
        
        DataElement dataElement4 = new DataElement();
        dataElement4.setName( "DE4name" );
        dataElement4.setShortName( "DE4sname" );
        dataElement4.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        dataElement4.setType( DataElement.TYPE_INT );
        
        dataElementService.addDataElement( dataElement1 );
        dataElementService.addDataElement( dataElement2 );
        dataElementService.addDataElement( dataElement3 );
        dataElementService.addDataElement( dataElement4 );

        MinMaxDataElement minMaxDataElement1 = new MinMaxDataElement( source1, dataElement1, 0, 100, false );
        MinMaxDataElement minMaxDataElement2 = new MinMaxDataElement( source2, dataElement2, 0, 100, false );
        MinMaxDataElement minMaxDataElement3 = new MinMaxDataElement( source2, dataElement3, 0, 100, false );
        MinMaxDataElement minMaxDataElement4 = new MinMaxDataElement( source2, dataElement4, 0, 100, false );

        int mmdeid1 = minMaxDataElementStore.addMinMaxDataElement( minMaxDataElement1 );
        minMaxDataElementStore.addMinMaxDataElement( minMaxDataElement2 );
        minMaxDataElementStore.addMinMaxDataElement( minMaxDataElement3 );
        minMaxDataElementStore.addMinMaxDataElement( minMaxDataElement4 );

        // ----------------------------------------------------------------------
        // Assertions
        // ----------------------------------------------------------------------

        assertNotNull( minMaxDataElementStore.getMinMaxDataElement( mmdeid1 ) );

        assertTrue( minMaxDataElementStore.getMinMaxDataElement( mmdeid1 ).getMax() == 100 );

        minMaxDataElement1.setMax( 200 );
        minMaxDataElementStore.updateMinMaxDataElement( minMaxDataElement1 );
        assertTrue( minMaxDataElementStore.getMinMaxDataElement( mmdeid1 ).getMax() == 200 );

        Collection<DataElement> dataElements1 = new HashSet<DataElement>();
        dataElements1.add( dataElement1 );

        Collection<DataElement> dataElements2 = new HashSet<DataElement>();
        dataElements2.add( dataElement2 );
        dataElements2.add( dataElement3 );
        dataElements2.add( dataElement4 );

        assertNotNull( minMaxDataElementStore.getMinMaxDataElement( source1, dataElement1 ) );
        assertNull( minMaxDataElementStore.getMinMaxDataElement( source2, dataElement1 ) );

        assertTrue( minMaxDataElementStore.getMinMaxDataElements( source1, dataElements1 ).size() == 1 );
        assertTrue( minMaxDataElementStore.getMinMaxDataElements( source2, dataElements2 ).size() == 3 );

        minMaxDataElementStore.delMinMaxDataElement( mmdeid1 );

        assertNull( minMaxDataElementStore.getMinMaxDataElement( mmdeid1 ) );
    }
}
