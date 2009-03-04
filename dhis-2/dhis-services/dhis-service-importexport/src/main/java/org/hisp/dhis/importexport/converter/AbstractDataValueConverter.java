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

import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.importexport.ImportParams;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractDataValueConverter.java 5152 2008-05-15 12:30:29Z larshelg $
 */
public class AbstractDataValueConverter
    extends AbstractConverter<DataValue>
{
    protected DataValueService dataValueService;
    
    protected ImportParams params;
    
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------

    protected void importUnique( DataValue object )
    {
        batchHandler.addObject( object );    
    }

    protected void importMatching( DataValue object, DataValue match )
    {
        match.setValue( object.getValue() );
        match.setStoredBy( object.getStoredBy() );
        match.setTimestamp( object.getTimestamp() );
        match.setComment( object.getComment() );
        
        batchHandler.updateObject( match );
    }
    
    protected DataValue getMatching( DataValue object )
    {
        if ( params.isSkipCheckMatching() )
        {
            return null;
        }

        // ---------------------------------------------------------------------
        // Datavalue cannot be compared against existing datavalues during
        // preview since the elements in its composite id have not been mapped
        // ---------------------------------------------------------------------

        if ( params.isPreview() )
        {
            return null;
        }
        
        return batchHandler.objectExists( object ) ? object : null;
    }
    
    protected boolean isIdentical( DataValue object, DataValue existing )
    {
        // ---------------------------------------------------------------------
        // Matching datavalues will not be overwritten
        // ---------------------------------------------------------------------

        return true;
    }
}
