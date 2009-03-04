package org.hisp.dhis.importexport;

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

import java.io.InputStream;

import org.hisp.dhis.system.process.AbstractStatementInternalProcess;
import org.hisp.dhis.system.process.PercentageBoundedProgressState;

/**
 * @author Lars Helge Overland
 * @version $Id: ImportInternalProcess.java 5306 2008-05-30 14:48:04Z larshelg $
 */
public abstract class ImportInternalProcess
    extends AbstractStatementInternalProcess
{
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    protected ImportParams params;

    public final void setImportParams( ImportParams params )
    {
        this.params = params;
    }
    
    protected InputStream inputStream;

    public final void setInputStream( InputStream inputStream )
    {
        this.inputStream = inputStream;
    }
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ImportInternalProcess()
    {
        this.setStateClass( PercentageBoundedProgressState.class );
    }

    // -------------------------------------------------------------------------
    // AbstractStatementInternalProcess implementation
    // -------------------------------------------------------------------------

    public String executeStatements()
        throws Exception
    {
        importData( params, inputStream );
        
        return State.SUCCESS;
    }    

    public abstract void importData( ImportParams params, InputStream inputStream );

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    protected final void setProgress( int percentageCompleted, String statusMessage )
    {
        processIsInterrupted();
        
        PercentageBoundedProgressState state = (PercentageBoundedProgressState) getState();

        if ( state != null )
        {
            state.setPercentageCompleted( percentageCompleted );
            state.setStatusMessage( statusMessage );
        }
    }

    private final void processIsInterrupted()
    {
        if ( getState() != null && getState().isStarted() && getState().isInterrupted() )
        {
            PercentageBoundedProgressState state = (PercentageBoundedProgressState) getState();

            state.setStatusMessage( "export_process_cancelled" );
        }
    }    
}
