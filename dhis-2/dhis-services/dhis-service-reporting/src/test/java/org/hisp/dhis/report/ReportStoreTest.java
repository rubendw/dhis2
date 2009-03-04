package org.hisp.dhis.report;

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
import java.util.Set;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportStoreTest
    extends DhisSpringTest
{
    private ReportStore reportStore;
    
    private ReportTableStore reportTableStore;
    
    private ReportTable reportTableA;
    
    private Set<ReportTable> reportTables;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        reportStore = (ReportStore) getBean( ReportStore.ID );
        
        reportTableStore = (ReportTableStore) getBean( ReportTableStore.ID );
        
        reportTableA = new ReportTable( "ReportTableA", "ReportTableA" );

        reportTables = new HashSet<ReportTable>();
        reportTables.add( reportTableA );
        
        reportTableStore.saveReportTable( reportTableA );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testSaveGet()
    {
        Report reportA = new Report( "ReportA", "DesignA", reportTables );
        Report reportB = new Report( "ReportB", "DesignB", reportTables );
        
        int idA = reportStore.saveReport( reportA );
        int idB = reportStore.saveReport( reportB );
        
        assertEquals( reportA, reportStore.getReport( idA ) );
        assertEquals( reportB, reportStore.getReport( idB ) );
    }
    
    public void testSaveGetUpdate()
    {
        Report reportA = new Report( "ReportA", "DesignA", reportTables );
        Report reportB = new Report( "ReportB", "DesignB", reportTables );
        
        int idA = reportStore.saveReport( reportA );
        int idB = reportStore.saveReport( reportB );
        
        assertEquals( reportA, reportStore.getReport( idA ) );
        assertEquals( reportB, reportStore.getReport( idB ) );
        
        reportA.setDesign( "UpdatedDesignA" );
        reportB.setDesign( "UpdatedDesignB" );
        
        int updatedIdA = reportStore.saveReport( reportA );
        int updatedIdB = reportStore.saveReport( reportB );
        
        assertEquals( idA, updatedIdA );
        assertEquals( idB, updatedIdB );
        
        assertEquals( "UpdatedDesignA", reportStore.getReport( updatedIdA ).getDesign() );
        assertEquals( "UpdatedDesignB", reportStore.getReport( updatedIdB ).getDesign() );
    }
    
    public void testDelete()
    {
        Report reportA = new Report( "ReportA", "DesignA", reportTables );
        Report reportB = new Report( "ReportB", "DesignB", reportTables );
        
        int idA = reportStore.saveReport( reportA );
        int idB = reportStore.saveReport( reportB );
        
        assertNotNull( reportStore.getReport( idA ) );
        assertNotNull( reportStore.getReport( idB ) );
        
        reportStore.deleteReport( reportA );

        assertNull( reportStore.getReport( idA ) );
        assertNotNull( reportStore.getReport( idB ) );

        reportStore.deleteReport( reportB );

        assertNull( reportStore.getReport( idA ) );
        assertNull( reportStore.getReport( idB ) );
    }
    
    public void testGetAll()
    {
        Report reportA = new Report( "ReportA", "DesignA", reportTables );
        Report reportB = new Report( "ReportB", "DesignB", reportTables );
        
        reportStore.saveReport( reportA );
        reportStore.saveReport( reportB );
        
        Collection<Report> reports = reportStore.getAllReports();
        
        assertNotNull( reports );
        assertEquals( 2, reports.size() );
        assertTrue( reports.contains( reportA ) );
        assertTrue( reports.contains( reportB ) );        
    }
}
