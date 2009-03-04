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

import static org.hisp.dhis.system.util.ConversionUtils.getIntegerCollection;

import java.util.Collection;

import org.hisp.dhis.datamart.DataMartInternalProcess;
import org.hisp.dhis.datamart.util.InternalProcessUtil;
import org.hisp.dhis.system.process.InternalProcessCoordinator;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id: ExportAction.java 5590 2008-08-25 11:42:46Z larshelg $
 */
public class ExportAction
    extends ActionSupport
{
    private static final String PROCESS_TYPE = "DataMart";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private InternalProcessCoordinator internalProcessCoordinator;
    
    public void setInternalProcessCoordinator( InternalProcessCoordinator processCoordinator )
    {
        this.internalProcessCoordinator = processCoordinator;
    }
    
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }
    
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private final int ALL = 0;
    
    public int getALL()
    {
        return ALL;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private Collection<String> selectedDataElements;
    
    public void setSelectedDataElements( Collection<String> selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }

    private Collection<String> selectedIndicators;

    public void setSelectedIndicators( Collection<String> selectedIndicators )
    {
        this.selectedIndicators = selectedIndicators;
    }

    private Collection<String> selectedPeriods; 
    
    public void setSelectedPeriods( Collection<String> selectedPeriods )
    {
        this.selectedPeriods = selectedPeriods;
    }

    private Collection<String> selectedOrganisationUnits;
    
    public void setSelectedOrganisationUnits( Collection<String> selectedOrganisationUnits )
    {
        this.selectedOrganisationUnits = selectedOrganisationUnits;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() 
        throws Exception
    {        
        String owner = currentUserService.getCurrentUsername();
        
        DataMartInternalProcess process = internalProcessCoordinator.newProcess( PROCESS_TYPE, owner );
        
        process.setDataElementIds( getIntegerCollection( selectedDataElements ) );
        process.setIndicatorIds( getIntegerCollection( selectedIndicators ) );
        process.setPeriodIds( getIntegerCollection( selectedPeriods ) );
        process.setOrganisationUnitIds( getIntegerCollection( selectedOrganisationUnits ) );
        
        String id = internalProcessCoordinator.requestProcessExecution( process );
        
        InternalProcessUtil.setCurrentRunningProcess( id );
        
        return SUCCESS;
    }
}
