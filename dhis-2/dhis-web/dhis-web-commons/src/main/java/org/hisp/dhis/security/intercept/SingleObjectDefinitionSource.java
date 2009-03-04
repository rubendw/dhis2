package org.hisp.dhis.security.intercept;

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

import java.util.Iterator;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.ObjectDefinitionSource;

/**
 * Generic ObjectDefinitionSource for one single object.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SingleObjectDefinitionSource.java 3131 2007-03-21 20:01:13Z torgeilo $
 */
public class SingleObjectDefinitionSource
    implements ObjectDefinitionSource
{
    private Object object;

    private ConfigAttributeDefinition attributes;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public SingleObjectDefinitionSource( Object object )
    {
        this.object = object;
    }

    public SingleObjectDefinitionSource( Object object, ConfigAttributeDefinition attributes )
    {
        this.object = object;

        if ( attributes.size() > 0 )
        {
            this.attributes = attributes;
        }
    }

    // -------------------------------------------------------------------------
    // ObjectDefinitionSource implementation
    // -------------------------------------------------------------------------

    public ConfigAttributeDefinition getAttributes( Object object )
        throws IllegalArgumentException
    {
        if ( !supports( object.getClass() ) )
        {
            throw new IllegalArgumentException( "Illegal type of object: " + object.getClass() );
        }

        if ( object.equals( this.object ) )
        {
            return attributes;
        }

        return null;
    }

    public Iterator getConfigAttributeDefinitions()
    {
        return null;
    }

    @SuppressWarnings( "unchecked" )
    public boolean supports( Class clazz )
    {
        return clazz.isAssignableFrom( object.getClass() );
    }
}
