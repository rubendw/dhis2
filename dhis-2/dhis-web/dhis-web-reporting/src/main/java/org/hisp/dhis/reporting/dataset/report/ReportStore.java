package org.hisp.dhis.reporting.dataset.report;

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

import org.hisp.dhis.external.location.LocationManagerException;

/**
 * @author Lars Helge Overland
 * @version $Id: ReportStore.java 5142 2008-05-13 10:53:20Z larshelg $
 */
public interface ReportStore
{
    String ID = ReportStore.class.getName();
    
    // -------------------------------------------------------------------------
    // Report Type
    // -------------------------------------------------------------------------
    
    final static int GENERIC = 0;
    
    final static int ORGUNIT_SPECIFIC = 1;
    
    final static int TABULAR = 2;
    
    // -------------------------------------------------------------------------
    // Report Element Type
    // -------------------------------------------------------------------------
    
    final static String DATAELEMENT = "DataElement";
    
    final static String INDICATOR = "Indicator";   
    
    // -------------------------------------------------------------------------
    // Report
    // -------------------------------------------------------------------------
    
    void addReport( String name, int reportType )
        throws ReportStoreException, LocationManagerException;
    
    Report getReport( String name )
        throws ReportStoreException, LocationManagerException;
    
    boolean deleteReport( String name )
        throws ReportStoreException, LocationManagerException;
    
    int getReportType( String reportName )
        throws ReportStoreException, LocationManagerException;
        
    Collection<String> getAllReports()
        throws ReportStoreException, LocationManagerException;

    Collection<String> getAllDesigns()
        throws ReportStoreException, LocationManagerException;
    
    Collection<String> getDesignsByType( int reportType )
        throws ReportStoreException, LocationManagerException;
    
    boolean isXMLReportExists( String reportName );
    
    boolean isJRXMLReportExists( String reportName );
    
    // -------------------------------------------------------------------------
    // Report Element
    // -------------------------------------------------------------------------
    
    void addReportElement( String reportName, String type, int elementId )
        throws ReportStoreException, LocationManagerException;
    
    void addReportElement( String reportName, String type, int elementId, int organisationUnitId )
        throws ReportStoreException, LocationManagerException;
    
    void removeReportElement( String reportName, String id )
        throws ReportStoreException, LocationManagerException;
    
    Collection<Element> getAllReportElements( String reportName )
        throws ReportStoreException, LocationManagerException;
    
    void moveUpReportElement( String reportName, String id )
        throws ReportStoreException, LocationManagerException;
    
    void moveDownReportElement( String reportName, String id )
        throws ReportStoreException, LocationManagerException;

    // -------------------------------------------------------------------------
    // Chart Element
    // -------------------------------------------------------------------------

    void addChartElement( String reportName, String type, int elementId )
        throws ReportStoreException, LocationManagerException;
    
    void addChartElement( String reportName, String type, int elementId, int organisationUnitId )
        throws ReportStoreException, LocationManagerException;
    
    void removeChartElement( String reportName, String id )
        throws ReportStoreException, LocationManagerException;
    
    Collection<Element> getAllChartElements( String reportName )
        throws ReportStoreException, LocationManagerException;
    
    void moveUpChartElement( String reportName, String id )
        throws ReportStoreException, LocationManagerException;
    
    void moveDownChartElement( String reportName, String id )
        throws ReportStoreException, LocationManagerException;
    
    // -------------------------------------------------------------------------
    // Design Template
    // -------------------------------------------------------------------------
        
    void setDesignTemplate( String reportName, int number )
        throws ReportStoreException, LocationManagerException;
    
    int getDesignTemplate( String reportName )
        throws ReportStoreException, LocationManagerException;

    // -------------------------------------------------------------------------
    // Chart Template
    // -------------------------------------------------------------------------
    
    void setChartTemplate( String reportName, int number )
        throws ReportStoreException, LocationManagerException;
    
    int getChartTemplate( String reportName )
        throws ReportStoreException, LocationManagerException;
}
