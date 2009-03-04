package org.hisp.dhis.dataset;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Bharath
 * @version $Id$
 */
public class DataEntryFormStoreTest
    extends DhisSpringTest
{
    private PeriodStore periodStore;

    private DataSetStore dataSetStore;

    private DataEntryFormStore dataEntryFormStore;

    private PeriodType periodType;

    public void setUpTest()
        throws Exception
    {
        dataSetStore = (DataSetStore) getBean( DataSetStore.ID );

        periodStore = (PeriodStore) getBean( PeriodStore.ID );

        dataEntryFormStore = (DataEntryFormStore) getBean( DataEntryFormStore.ID );

        periodType = periodStore.getAllPeriodTypes().iterator().next();
    }

    // -------------------------------------------------------------------------
    // DataEntryForm
    // -------------------------------------------------------------------------

    public void testAddDataEntryForm()
    {
        DataSet dataSetA = new DataSet( "DataSet-A", periodType );

        dataSetStore.addDataSet( dataSetA );

        DataEntryForm dataEntryFormA = new DataEntryForm( "DataEntryForm-A", dataSetA );

        int dataEntryFormAid = dataEntryFormStore.addDataEntryForm( dataEntryFormA );

        dataEntryFormA = dataEntryFormStore.getDataEntryForm( dataEntryFormAid );

        assertEquals( dataEntryFormAid, dataEntryFormA.getId() );
        assertEquals( "DataEntryForm-A", dataEntryFormA.getName() );
    }

    public void testUpdateDataEntryForm()
    {
        DataSet dataSetA = new DataSet( "DataSet-A", periodType );

        dataSetStore.addDataSet( dataSetA );

        DataEntryForm dataEntryForm = new DataEntryForm( "DataEntryForm-A", dataSetA );

        int id = dataEntryFormStore.addDataEntryForm( dataEntryForm );

        dataEntryForm = dataEntryFormStore.getDataEntryForm( id );

        assertEquals( "DataEntryForm-A", dataEntryForm.getName() );

        dataEntryForm.setName( "DataEntryForm-X" );

        dataEntryFormStore.updateDataEntryForm( dataEntryForm );

        dataEntryForm = dataEntryFormStore.getDataEntryForm( id );

        assertEquals( dataEntryForm.getName(), "DataEntryForm-X" );
    }

    public void testDeleteAndGetDataEntryForm()
    {
        DataSet dataSetA = new DataSet( "DataSet-A", periodType );

        dataSetStore.addDataSet( dataSetA );

        DataEntryForm dataEntryForm = new DataEntryForm( "DataEntryForm-A", dataSetA );

        int id = dataEntryFormStore.addDataEntryForm( dataEntryForm );

        dataEntryForm = dataEntryFormStore.getDataEntryForm( id );

        assertNotNull( dataEntryFormStore.getDataEntryForm( id ) );

        dataEntryFormStore.deleteDataEntryForm( dataEntryFormStore.getDataEntryForm( id ) );

        assertNull( dataEntryFormStore.getDataEntryForm( id ) );
    }

    public void testGetDataEntryFormByName()
        throws Exception
    {
        DataSet dataSetA = new DataSet( "DataSet-A", periodType );

        dataSetStore.addDataSet( dataSetA );

        DataEntryForm dataEntryForm = new DataEntryForm( "DataEntryForm-A", dataSetA );

        int id = dataEntryFormStore.addDataEntryForm( dataEntryForm );

        dataEntryForm = dataEntryFormStore.getDataEntryForm( id );

        assertEquals( dataEntryFormStore.getDataEntryFormByName( "DataEntryForm-A" ), dataEntryForm );
        assertNull( dataEntryFormStore.getDataEntryFormByName( "DataEntryForm-X" ) );
    }

    public void testGetDataEntryFormByDataSet()
        throws Exception
    {
        DataSet dataSetA = new DataSet( "DataSet-A", periodType );

        int dataSetAid = dataSetStore.addDataSet( dataSetA );

        DataEntryForm dataEntryForm = new DataEntryForm( "DataEntryForm-A", dataSetA );

        dataEntryFormStore.addDataEntryForm( dataEntryForm );

        DataSet dataSetB = new DataSet( "DataSet-B", periodType );

        dataSetStore.addDataSet( dataSetB );

        assertEquals( dataEntryFormStore.getDataEntryFormByDataSet( dataSetA ).getId(), dataSetAid );
        assertNull( dataEntryFormStore.getDataEntryFormByDataSet( dataSetB ) );
    }

    public void testGetAllDataEntryForms()
    {
        DataSet dataSetA = new DataSet( "DataSet-A", periodType );
        DataSet dataSetB = new DataSet( "DataSet-B", periodType );

        dataSetStore.addDataSet( dataSetA );
        dataSetStore.addDataSet( dataSetB );

        DataEntryForm dataEntryFormA = new DataEntryForm( "DataEntryForm-A", dataSetA );
        DataEntryForm dataEntryFormB = new DataEntryForm( "DataEntryForm-B", dataSetB );

        dataEntryFormStore.addDataEntryForm( dataEntryFormA );
        dataEntryFormStore.addDataEntryForm( dataEntryFormB );

        Collection<DataEntryForm> dataEntryForms = dataEntryFormStore.getAllDataEntryForms();

        assertEquals( dataEntryForms.size(), 2 );
        assertTrue( dataEntryForms.contains( dataEntryFormA ) );
        assertTrue( dataEntryForms.contains( dataEntryFormB ) );
    }
}
