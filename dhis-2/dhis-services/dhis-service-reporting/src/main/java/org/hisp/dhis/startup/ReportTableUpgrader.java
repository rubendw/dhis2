package org.hisp.dhis.startup;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;

/**
 * The purpose of this startup routine is simply to make existing report tables
 * compatible with the new logic and can be removed after some time (included
 * at 26.09.2008).
 * 
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTableUpgrader
    extends AbstractStartupRoutine
{
    private static final Log log = LogFactory.getLog( ReportTableUpgrader.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }

    // -------------------------------------------------------------------------
    // AbstractStartupRoutine implementation
    // -------------------------------------------------------------------------

    public void execute()
        throws Exception
    {
        for ( ReportTable table : reportTableService.getAllReportTables() )
        {
            if ( table.getExistingTableName() == null || table.getExistingTableName().trim().length() == 0 )
            {
                table.setExistingTableName( table.getTableName() );
                
                reportTableService.saveOrUpdateReportTable( table );
            }
        }
        
        log.info( "Upgraded report tables" );
    }
}
