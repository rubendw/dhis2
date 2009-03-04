package org.hisp.dhis.dataset;

/*
 * Copyright (c) 2004-2007, University of Oslo All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the HISP project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultCompleteDataSetRegistrationService
    implements CompleteDataSetRegistrationService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CompleteDataSetRegistrationStore completeDataSetRegistrationStore;

    public void setCompleteDataSetRegistrationStore( CompleteDataSetRegistrationStore completeDataSetRegistrationStore )
    {
        this.completeDataSetRegistrationStore = completeDataSetRegistrationStore;
    }
    
    // -------------------------------------------------------------------------
    // CompleteDataSetRegistrationService
    // -------------------------------------------------------------------------

    public void saveCompleteDataSetRegistration( CompleteDataSetRegistration registration )
    {
        completeDataSetRegistrationStore.saveCompleteDataSetRegistration( registration );
    }
    public void deleteCompleteDataSetRegistration( CompleteDataSetRegistration registration )
    {
        completeDataSetRegistrationStore.deleteCompleteDataSetRegistration( registration );
    }

    public CompleteDataSetRegistration getCompleteDataSetRegistration( DataSet dataSet, Period period, Source source )
    {
        return completeDataSetRegistrationStore.getCompleteDataSetRegistration( dataSet, period, source );
    }
    
    public Collection<CompleteDataSetRegistration> getAllCompleteDataSetRegistrations()
    {
        return completeDataSetRegistrationStore.getAllCompleteDataSetRegistrations();
    }    

    public Collection<CompleteDataSetRegistration> getCompleteDataSetRegistrations( 
        Collection<DataSet> dataSets, Collection<? extends Source> sources, Collection<Period> periods )
    {
        return completeDataSetRegistrationStore.getCompleteDataSetRegistrations( dataSets, sources, periods );
    }    

    @SuppressWarnings( "unchecked" )
    public int getCompleteDataSetRegistrationsForDataSet( DataSet dataSet, Collection<? extends Source> sources, Period period )
    {
        Collection<? extends Source> intersectingSources = CollectionUtils.intersection( sources, dataSet.getSources() );
        
        if ( intersectingSources == null || intersectingSources.size() == 0 )
        {
            return 0;
        }        
        
        return completeDataSetRegistrationStore.getCompleteDataSetRegistrations( dataSet, intersectingSources, period ).size();
    }
    
    @SuppressWarnings( "unchecked" )
    public int getCompleteDataSetRegistrationsForDataSet( DataSet dataSet, Collection<? extends Source> sources, Period period, Date deadline )
    {
        Collection<? extends Source> intersectingSources = CollectionUtils.intersection( sources, dataSet.getSources() );
        
        if ( intersectingSources == null || intersectingSources.size() == 0 )
        {
            return 0;
        }
        
        return completeDataSetRegistrationStore.getCompleteDataSetRegistrations( dataSet, intersectingSources, period, deadline ).size();
    }
    
    public void deleteCompleteDataSetRegistrations( DataSet dataSet )
    {
        completeDataSetRegistrationStore.deleteCompleteDataSetRegistrations( dataSet );
    }
}
