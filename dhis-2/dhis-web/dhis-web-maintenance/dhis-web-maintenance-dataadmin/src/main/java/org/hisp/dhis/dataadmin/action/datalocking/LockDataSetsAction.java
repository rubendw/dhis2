package org.hisp.dhis.dataadmin.action.datalocking;

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
import java.util.Collection;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;

import com.opensymphony.xwork.Action;

/**
 * @author Jan Henrik Overland
 * @author Lars Helge Overland
 * @version $Id$
 */
public class LockDataSetsAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private PeriodService periodService;
    
    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Collection<String> lockedDataSets = new ArrayList<String>();

    public void setLockedDataSets( Collection<String> lockedDataSets )
    {
        this.lockedDataSets = lockedDataSets;
    }

    private Collection<String> unlockedDataSets = new ArrayList<String>();

    public void setUnlockedDataSets( Collection<String> unlockedDataSets )
    {
        this.unlockedDataSets = unlockedDataSets;
    }
    
    private Integer periodId;
    
    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        Period period = periodService.getPeriod( periodId );
        
        for ( String id : lockedDataSets )
        {
            DataSet dataSet = dataSetService.getDataSet( Integer.parseInt( id ) );
            dataSet.getLockedPeriods().add( period );
            dataSetService.updateDataSet( dataSet );
        }
        
        for ( String id : unlockedDataSets )
        {
            DataSet dataSet = dataSetService.getDataSet( Integer.parseInt( id ) );
            dataSet.getLockedPeriods().remove( period );
            dataSetService.updateDataSet( dataSet ); 
        }
        
        return SUCCESS;
    }
}
