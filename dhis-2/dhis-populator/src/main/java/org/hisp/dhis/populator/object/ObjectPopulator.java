package org.hisp.dhis.populator.object;

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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.populator.Populator;
import org.hisp.dhis.populator.PopulatorException;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ObjectPopulator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public abstract class ObjectPopulator
    implements Populator
{
    private Map<String, Integer> idMap = new HashMap<String, Integer>();

    // -------------------------------------------------------------------------
    // To be implemented by subclasses
    // -------------------------------------------------------------------------

    public abstract void executeRule( String rule ) throws PopulatorException;

    // -------------------------------------------------------------------------
    // Methods for ID mapping
    // -------------------------------------------------------------------------

    public int getInternalId( String id ) throws PopulatorException
    {
        Integer internalId = idMap.get( id );

        if ( internalId == null )
        {
            throw new PopulatorException( "ID doesn't exist: " + id );
        }

        return internalId.intValue();
    }

    protected void addIdMapping( String id, int internalId ) throws PopulatorException
    {
        if ( idMap.containsKey( id ) )
        {
            throw new PopulatorException( "ID already mapped: " + id );
        }

        idMap.put( id, internalId );
    }
}
