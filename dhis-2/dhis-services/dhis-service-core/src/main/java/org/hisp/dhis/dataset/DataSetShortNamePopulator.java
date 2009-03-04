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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;
import org.hisp.dhis.system.util.TextUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataSetShortNamePopulator
    extends AbstractStartupRoutine
{
    private Log log = LogFactory.getLog( DataSetShortNamePopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetService dataSetService;
    
    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // StartupRoutine implementation
    // -------------------------------------------------------------------------

    public void execute()
        throws Exception
    {
        for ( DataSet dataSet: dataSetService.getAllDataSets() )
        {
            if ( dataSet.getShortName() == null || dataSet.getShortName().trim().length() == 0 )
            {
                dataSet.setShortName( TextUtils.subString( dataSet.getName(), 0, 20 ) );
                
                try
                {
                    dataSetService.updateDataSet( dataSet );
                }
                catch ( Exception ex )
                {
                    log.warn( "Could not set short name of DataSet: '" + dataSet.getName() + "', please do it manually" );
                }
            }
        }
        
        log.info( "Populated DataSet short names" );
    }
}
