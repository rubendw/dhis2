package org.hisp.dhis.dd.action.target;

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

import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetService;

import com.opensymphony.xwork.Action;

/**
 * @author Martin Myrseth
 * @version $id$
 */
public class ShowUpdateTargetFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private TargetService targetService;

    public void setTargetService( TargetService targetService )
    {
        this.targetService = targetService;
    }
    
    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }    

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }    
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private int periodTypeId;
    
    public void setPeriodTypeId( int periodTypeId )
    {
        this.periodTypeId = periodTypeId;
    }
    
    public int getPeriodTypeId()
    {
        return periodTypeId;
    }
    
    private OrganisationUnit orgUnit;
    
    public OrganisationUnit getOrgUnit()
    {
        return orgUnit;
    }

    private ArrayList<Indicator> indicators;
    
    public ArrayList<Indicator> getIndicators()
    {
        return indicators;
    }
    
    private ArrayList<PeriodType> periodTypes;
    
    public ArrayList<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }
    
    private ArrayList<Period> periods;
    
    public ArrayList<Period> getPeriods()
    {
        return periods;
    }
    
    private int targetId;
    
    public void setTargetId( int targetId )
    {
    	this.targetId = targetId;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------
    
    private Target target;
    
    public Target getTarget()
    {
        return target;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute()
        throws Exception        
    {
    	indicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );
        
        periodTypes = new ArrayList<PeriodType>( periodService.getAllPeriodTypes() );
        
        if ( periodTypeId != 0 )
        {
            PeriodType periodType = periodService.getPeriodType( periodTypeId );
            
            periods = new ArrayList<Period>( periodService.getPeriodsByPeriodType( periodType ) );          
            
        }
        
        target = targetService.getTarget( targetId );
        
        return SUCCESS;
    }
}
