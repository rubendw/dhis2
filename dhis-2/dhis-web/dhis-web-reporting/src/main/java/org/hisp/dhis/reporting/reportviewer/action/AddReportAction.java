package org.hisp.dhis.reporting.reportviewer.action;

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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfigurationProvider;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportManager;
import org.hisp.dhis.report.ReportStore;
import org.hisp.dhis.report.manager.ReportConfiguration;
import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.system.util.CollectionConversionUtils;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.StreamUtils;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id: UploadDesignAction.java 5207 2008-05-22 12:16:36Z larshelg $
 */
public class AddReportAction
    extends ActionSupport
{
    private static final Log log = LogFactory.getLog( AddReportAction.class );
    
    private static final String START_TAG_DRIVER_CLASS = "<property name=\"odaDriverClass\">";
    private static final String START_TAG_URL = "<property name=\"odaURL\">";
    private static final String START_TAG_USER_NAME = "<property name=\"odaUser\">";
    private static final String START_TAG_PASSWORD = "<encrypted-property name=\"odaPassword\" encryptionID=\"base64\">";
    private static final String END_TAG_DRIVER_CLASS = "</property>";
    private static final String END_TAG_URL = "</property>";
    private static final String END_TAG_USER_NAME = "</property>";
    private static final String END_TAG_PASSWORD = "</encrypted-property>";    
	
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportManager reportManager;

    public void setReportManager( ReportManager reportManager )
    {
        this.reportManager = reportManager;
    }
    
    private ReportStore reportStore;

    public void setReportStore( ReportStore reportStore )
    {
        this.reportStore = reportStore;
    }    

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }
    
    private JDBCConfigurationProvider configurationProvider;

    public void setConfigurationProvider( JDBCConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }
    
    // -----------------------------------------------------------------------
    // I18n
    // -----------------------------------------------------------------------

    protected I18n i18n;
    
    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Collection<String> selectedReportTables;

    public void setSelectedReportTables( Collection<String> selectedReportTables )
    {
        this.selectedReportTables = selectedReportTables;
    }
    
    private File file;

    public void setUpload( File file )
    {
        this.file = file;
    }
    
    private String fileName;
    
    public void setUploadFileName( String fileName )
    {
        this.fileName = fileName;
    }
    
    private String contentType;
    
    public void setUploadContentType( String contentType )
    {
    	this.contentType = contentType;
    }
    
    private String currentDesign;

    public void setCurrentDesign( String currentDesign )
    {
        this.currentDesign = currentDesign;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;
    
    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Set fileName to the current design file name in case of update
        // ---------------------------------------------------------------------

        if ( fileName == null && currentDesign != null )
        {
            fileName = currentDesign;
        }
        
        // ---------------------------------------------------------------------
        // Validation
        // ---------------------------------------------------------------------

        if ( name == null || name.trim().length() == 0 )
        {
            message = i18n.getString( "specify_name" );
            
            return ERROR;
        }
        
        if ( fileName == null || fileName.trim().length() == 0 )
        {
            message = i18n.getString( "select_file" );
            
            return ERROR;
        }
        
        // ---------------------------------------------------------------------
        // Design file upload
        // ---------------------------------------------------------------------

    	log.info( "Upload file name: " + fileName + ", content type: " + contentType );
    	
        ReportConfiguration reportConfig = reportManager.getConfiguration();
        
        String birtHome = reportConfig.getHome();
        
        File design = new File( birtHome, fileName );
        
        log.info( "New file: " + design.getAbsolutePath() );

        // ---------------------------------------------------------------------
        // Updating database properties in design file
        // ---------------------------------------------------------------------

        if ( file != null )
        {
            JDBCConfiguration jdbcConfig = configurationProvider.getConfiguration();
            
            String encryptedPassword = CodecUtils.encryptBase64( jdbcConfig.getPassword() );
            
            Map<String, String> replaceMap = new HashMap<String, String>();
            
            replaceMap.put( START_TAG_DRIVER_CLASS, START_TAG_DRIVER_CLASS + jdbcConfig.getDriverClass() + END_TAG_DRIVER_CLASS );
            replaceMap.put( START_TAG_URL, START_TAG_URL + jdbcConfig.getConnectionUrl() + END_TAG_URL );
            replaceMap.put( START_TAG_USER_NAME, START_TAG_USER_NAME + jdbcConfig.getUsername() + END_TAG_USER_NAME );
            replaceMap.put( START_TAG_PASSWORD, START_TAG_PASSWORD + encryptedPassword + END_TAG_PASSWORD );
    
            StringBuffer in = StreamUtils.readContent( file, replaceMap );
    
            StreamUtils.writeContent( design, in );
        }
        
        // ---------------------------------------------------------------------
        // Create and save report
        // ---------------------------------------------------------------------

        Report report = ( id == null ) ? new Report() : reportStore.getReport( id );
        
        report.setName( name );
        report.setDesign( fileName );
        report.setReportTables( selectedReportTables != null ? new CollectionConversionUtils<ReportTable>().getSet( 
            reportTableService.getReportTables( ConversionUtils.getIntegerCollection( selectedReportTables ) ) ) : null );
        
        reportStore.saveReport( report );
        
        return SUCCESS;
    }
}
