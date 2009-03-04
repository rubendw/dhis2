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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.importexport.action.util.InternalProcessUtil;
import org.hisp.dhis.system.process.InternalProcess;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.system.process.PercentageBoundedProgressState;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetImportStatusAction
    extends ActionSupport
{
    private static final String ACTION_INFO = "info";
    private static final String ACTION_PREVIEW = "preview";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private InternalProcessCoordinator internalProcessCoordinator;
    
    public void setInternalProcessCoordinator( InternalProcessCoordinator processCoordinator )
    {
        this.internalProcessCoordinator = processCoordinator;
    }
    
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String actionType;

    public String getActionType()
    {
        return actionType;
    }
    
    private int percentageCompleted;

    public int getPercentageCompleted()
    {
        return percentageCompleted;
    }

    private String statusMessage;

    public String getStatusMessage()
    {
        return statusMessage;
    }
    
    private String fileMessage;

    public String getFileMessage()
    {
        return fileMessage;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() 
        throws Exception
    {
        if ( InternalProcessUtil.processIsRunning() )
        {
            String id = InternalProcessUtil.getCurrentRunningProcess();
            
            InternalProcess process = internalProcessCoordinator.getProcess( id );
            
            if ( process != null )
            {
                PercentageBoundedProgressState state = (PercentageBoundedProgressState) process.getState();
            
                setOutput( null, state.getPercentageCompleted(), i18n.getString( state.getStatusMessage() ), 
                    InternalProcessUtil.getCurrentImportFileName() );
                
                String type = InternalProcessUtil.getCurrentRunningProcessType();
            
                if ( type.equals( InternalProcessUtil.TYPE_PREVIEW ) && state.isFinished() )
                {
                    actionType = ACTION_PREVIEW;
                
                    InternalProcessUtil.setCurrentRunningProcessType( InternalProcessUtil.TYPE_IMPORT );
                }
                else
                {
                    actionType = ACTION_INFO;
                }
            }
            else
            {
                setOutput( ACTION_INFO, 0, i18n.getString( "import_process_not_initialized" ), i18n.getString( "no_current_file" ) );
            }
        }
        else
        {
            setOutput( ACTION_INFO, 0, i18n.getString( "no_import_process_running" ), i18n.getString( "no_current_file" ) );
        }
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void setOutput( String actionType, int percentageCompleted, String statusMessage, String fileMessage )
    {
        this.actionType = actionType;
        this.percentageCompleted = percentageCompleted;
        this.statusMessage = statusMessage;
        this.fileMessage = fileMessage;
    }
}
