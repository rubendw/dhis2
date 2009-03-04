package org.hisp.dhis.importexport.converter;

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

import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.importexport.ImportParams;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractAggregatedIndicatorValueConverter.java 4825 2008-04-08 12:46:49Z larshelg $
 */
public class AbstractAggregatedIndicatorValueConverter
    extends AbstractConverter<AggregatedIndicatorValue>
{
    protected ImportParams params;
    
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------

    protected void importUnique( AggregatedIndicatorValue object )
    {        
        batchHandler.addObject( object );
    }

    protected void importMatching( AggregatedIndicatorValue object, AggregatedIndicatorValue match )
    {
        // Do nothing
    }

    protected AggregatedIndicatorValue getMatching( AggregatedIndicatorValue object )
    {
        return !params.isSkipCheckMatching() ? ( batchHandler.objectExists( object ) ? object : null ) : null;
    }

    protected boolean isIdentical( AggregatedIndicatorValue object, AggregatedIndicatorValue match )
    {
        return true;
    }
}
