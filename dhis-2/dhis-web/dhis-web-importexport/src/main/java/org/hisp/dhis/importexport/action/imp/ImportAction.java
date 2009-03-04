package org.hisp.dhis.importexport.action.imp;

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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.importexport.ImportInternalProcess;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.ImportStrategy;
import org.hisp.dhis.importexport.action.util.FileUtil;
import org.hisp.dhis.importexport.action.util.InternalProcessUtil;
import org.hisp.dhis.options.datadictionary.DataDictionaryModeManager;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ImportAction
    extends ActionSupport
{
    private static final String IMPORT_INTERNAL_PROCESS_ID_POSTFIX = "ImportService";
    
    private static final Log log = LogFactory.getLog( ImportAction.class );
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String dataDictionaryMode;

    public void setDataDictionaryMode( String dataDictionaryMode )
    {
        this.dataDictionaryMode = dataDictionaryMode;
    }
    
    private boolean preview;

    public void setPreview( boolean preview )
    {
        this.preview = preview;
    }
    
    private String incomingRecords;

    public void setIncomingRecords( String incomingRecords )
    {
        this.incomingRecords = incomingRecords;
    }
    
    private boolean dataValues;

    public void setDataValues( boolean dataValues )
    {
        this.dataValues = dataValues;
    }
    
    private boolean skipCheckMatching;

    public void setSkipCheckMatching( boolean skipCheckMatching )
    {
        this.skipCheckMatching = skipCheckMatching;
    }
    
    private String lastUpdated;

    public void setLastUpdated( String lastUpdated )
    {
        this.lastUpdated = lastUpdated;
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

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }    
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private InternalProcessCoordinator internalProcessCoordinator;

    public void setInternalProcessCoordinator( InternalProcessCoordinator internalProcessCoordinator )
    {
        this.internalProcessCoordinator = internalProcessCoordinator;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }
    
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        String importFormat = InternalProcessUtil.getCurrentRunningProcessImportFormat();
        
        InputStream in = null;

        // ---------------------------------------------------------------------
        // Validation
        // ---------------------------------------------------------------------

        if ( !importFormat.equals( "DHIS14FILE" ) )
        {
            if ( file == null )
            {
                message = i18n.getString( "specify_file" );
                
                return SUCCESS;
            }
            
            if ( !FileUtil.isAllowed( contentType ) )
            {
                message = i18n.getString( "file_type_not_allowed" );
                
                return SUCCESS;
            }
            
            in = new BufferedInputStream( new FileInputStream( file ) );
            
            log.info( "Content-type: " + contentType + ", filename: " + file.getCanonicalPath() );
        }

        // ---------------------------------------------------------------------
        // Import parameters
        // ---------------------------------------------------------------------

        ImportParams params = new ImportParams();
        
        ImportStrategy strategy = ImportStrategy.valueOf( incomingRecords );

        params.setPreview( preview );
        params.setExtendedMode( dataDictionaryMode.equals( DataDictionaryModeManager.DATADICTIONARY_MODE_EXTENDED ) );        
        params.setImportStrategy( strategy );
        params.setDataValues( dataValues );
        params.setSkipCheckMatching( skipCheckMatching );
        params.setLastUpdated( ( lastUpdated != null && lastUpdated.trim().length() > 0 ) ? DateUtils.getMediumDate( lastUpdated ) : null );

        // ---------------------------------------------------------------------
        // Process
        // ---------------------------------------------------------------------

        String type = importFormat + IMPORT_INTERNAL_PROCESS_ID_POSTFIX;
        
        String owner = currentUserService.getCurrentUsername();
        
        ImportInternalProcess importProcess = internalProcessCoordinator.newProcess( type, owner );
        
        importProcess.setImportParams( params );
        importProcess.setInputStream( in );
        
        String id = internalProcessCoordinator.requestProcessExecution( importProcess );
        
        InternalProcessUtil.setCurrentRunningProcess( id );
        InternalProcessUtil.setCurrentRunningProcessType( preview ? InternalProcessUtil.TYPE_PREVIEW : InternalProcessUtil.TYPE_IMPORT );
        InternalProcessUtil.setCurrentImportFileName( fileName );
        
        return SUCCESS;
    }
}
