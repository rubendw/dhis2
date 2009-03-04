package org.hisp.dhis.completeness.cache;

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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.hisp.dhis.external.configuration.NoConfigurationFoundException;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.completeness.DataSetCompletenessService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class MemoryDataSetCompletenessCache
    implements DataSetCompletenessCache
{
    private ThreadLocal<Map<Period, Date>> deadlines = new ThreadLocal<Map<Period, Date>>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetCompletenessService completenessService;
    
    public void setCompletenessService( DataSetCompletenessService completenessService )
    {
        this.completenessService = completenessService;
    }

    // -------------------------------------------------------------------------
    // DataSetCompletenessCache implementation
    // -------------------------------------------------------------------------

    public Date getDeadline( Period period )
    {
        Map<Period, Date> cache = deadlines.get();
        
        Date deadline = null;
        
        if ( cache != null && ( deadline = cache.get( period ) ) != null )
        {
            return deadline;
        }

        try
        {
            deadline = completenessService.getConfiguration().getDeadline( period );
        }
        catch ( NoConfigurationFoundException ex )
        {
            // On-time calculations will be disabled
        }
        
        cache = ( cache == null ) ? new HashMap<Period, Date>() : cache;
        
        cache.put( period, deadline );
        
        deadlines.set( cache );
        
        return deadline;
    }
    
    public void clear()
    {
        deadlines.remove();
    }
}
