package org.hisp.dhis.reporttable;

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

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.system.process.AbstractStatementInternalProcess;
import org.hisp.dhis.system.process.PercentageBoundedProgressState;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public abstract class ReportTableInternalProcess
    extends AbstractStatementInternalProcess
    implements ReportTableCreator
{
    public static String ID = "internal-process-ReportTable";
    
    private List<ReportTable> reportTables = new ArrayList<ReportTable>();
    
    public void setReportTables( List<ReportTable> reportTables )
    {
        this.reportTables = reportTables;
    }

    private boolean doDataMart;
        
    public void setDoDataMart( boolean doDataMart )
    {
        this.doDataMart = doDataMart;
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ReportTableInternalProcess()
    {
        this.setStateClass( PercentageBoundedProgressState.class );
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addReportTable( ReportTable table )
    {
        this.reportTables.add( table );
    }
    
    // -------------------------------------------------------------------------
    // AbstractStatementInternalProcess implementation
    // -------------------------------------------------------------------------

    public String executeStatements()
    {
        for ( ReportTable reportTable : reportTables )
        {
            createReportTable( reportTable, doDataMart );
        }
        
        setProgress( true );
        
        deleteRelativePeriods();
        
        return State.SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract void deleteRelativePeriods();
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    protected final void setProgress( int percentageCompleted, String statusMessage )
    {
        PercentageBoundedProgressState state = (PercentageBoundedProgressState) getState();

        if ( state != null )
        {
            state.setPercentageCompleted( percentageCompleted );
            state.setStatusMessage( statusMessage );
        }
    }
    
    protected final void setProgress( boolean complete )
    {
        PercentageBoundedProgressState state = (PercentageBoundedProgressState) getState();

        if ( state != null )
        {
            state.setComplete( complete );
        }
    }
}
