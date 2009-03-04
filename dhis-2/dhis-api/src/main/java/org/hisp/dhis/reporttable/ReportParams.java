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

import java.io.Serializable;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportParams
    implements Serializable
{
    private Boolean paramReportingMonth;
    
    private Boolean paramParentOrganisationUnit;
    
    private Boolean paramOrganisationUnit;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ReportParams()
    {
    }
    
    public ReportParams( boolean paramReportingMonth, boolean paramParentOrganisationUnit, boolean paramOrganisationUnit )
    {
        this.paramReportingMonth = paramReportingMonth;
        this.paramParentOrganisationUnit = paramParentOrganisationUnit;
        this.paramOrganisationUnit = paramOrganisationUnit;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean isParamReportingMonth()
    {
        return paramReportingMonth != null && paramReportingMonth;
    }
    
    public boolean isParamParentOrganisationUnit()
    {
        return paramParentOrganisationUnit != null && paramParentOrganisationUnit;
    }
    
    public boolean isParamOrganisationUnit()
    {
        return paramOrganisationUnit != null && paramOrganisationUnit;
    }
    
    public boolean isSet()
    {
        return isParamReportingMonth() || isParamParentOrganisationUnit() || isParamOrganisationUnit();
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Boolean getParamReportingMonth()
    {
        return paramReportingMonth;
    }

    public void setParamReportingMonth( Boolean paramReportingMonth )
    {
        this.paramReportingMonth = paramReportingMonth;
    }

    public Boolean getParamParentOrganisationUnit()
    {
        return paramParentOrganisationUnit;
    }

    public void setParamParentOrganisationUnit( Boolean paramParentOrganisationUnit )
    {
        this.paramParentOrganisationUnit = paramParentOrganisationUnit;
    }

    public Boolean getParamOrganisationUnit()
    {
        return paramOrganisationUnit;
    }

    public void setParamOrganisationUnit( Boolean paramOrganisationUnit )
    {
        this.paramOrganisationUnit = paramOrganisationUnit;
    }    
}
