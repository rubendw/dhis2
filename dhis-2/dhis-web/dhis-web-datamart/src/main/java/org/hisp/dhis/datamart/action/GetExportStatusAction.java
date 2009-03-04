package org.hisp.dhis.datamart.action;

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

import org.hisp.dhis.datamart.util.InternalProcessUtil;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.system.process.InternalProcess;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.system.process.PercentageBoundedProgressState;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetExportStatusAction
    extends ActionSupport
{
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
            
                setOutput( state.getPercentageCompleted(), i18n.getString( state.getStatusMessage() ) );
            }
            else
            {
                setOutput( percentageCompleted, i18n.getString( "export_process_not_initialized" ) );
            }
        }
        else
        {
            setOutput( percentageCompleted, i18n.getString( "no_export_process_running" ) );
        }
        
        return SUCCESS;        
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void setOutput( int percentageCompleted, String statusMessage )
    {
        this.percentageCompleted = percentageCompleted;
        this.statusMessage = statusMessage;
    }
}
