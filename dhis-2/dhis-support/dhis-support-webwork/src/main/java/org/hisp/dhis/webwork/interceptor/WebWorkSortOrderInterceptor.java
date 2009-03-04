package org.hisp.dhis.webwork.interceptor;

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

import java.util.Comparator;
import java.util.Map;

import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.options.sortorder.SortOrderManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * @author Lars Helge Overland
 * @version $Id: WebWorkSortOrderInterceptor.java 3796 2007-10-30 16:21:08Z larshelg $
 */
public class WebWorkSortOrderInterceptor
    implements Interceptor
{
    private static final String KEY_DATAELEMENT_COMPARATOR = "dataElementComparator";

    private static final String KEY_INDICATOR_COMPARATOR = "indicatorComparator";
    
    private static final String KEY_ORGANISATIONUNIT_COMPARATOR = "organisationUnitComparator";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SortOrderManager sortOrderManager;

    public void setSortOrderManager( SortOrderManager sortOrderManager )
    {
        this.sortOrderManager = sortOrderManager;
    }

    // -------------------------------------------------------------------------
    // Interface implementation
    // -------------------------------------------------------------------------

    public void destroy()
    {
    }

    public void init()
    {
    }

    public String intercept( ActionInvocation actionInvocation )
        throws Exception
    {
        Comparator<DataElement> dataElementComparator = sortOrderManager.getCurrentDataElementSortOrderComparator();

        Comparator<Indicator> indicatorComparator = sortOrderManager.getCurrentIndicatorSortOrderComparator();
        
        Comparator<OrganisationUnit> organisationUnitComparator = sortOrderManager.getCurrentOrganisationUnitSortOrderComparator();
        
        Action action = (Action) actionInvocation.getAction();
        Map contextMap = actionInvocation.getInvocationContext().getContextMap();

        try
        {
            Ognl.setValue( KEY_DATAELEMENT_COMPARATOR, contextMap, action, dataElementComparator );
        }
        catch ( NoSuchPropertyException e )
        {
        }
                
        try
        {
            Ognl.setValue( KEY_INDICATOR_COMPARATOR, contextMap, action, indicatorComparator );
        }
        catch ( NoSuchPropertyException e )
        {
        }

        try
        {
            Ognl.setValue( KEY_ORGANISATIONUNIT_COMPARATOR, contextMap, action, organisationUnitComparator );
        }
        catch ( NoSuchPropertyException e )
        {
        }

        return actionInvocation.invoke();
    }
}
