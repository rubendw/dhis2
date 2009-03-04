package org.hisp.dhis.system.util;

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ConversionUtils
{
    /**
     * Creates a collection of the identifiers of the objects passed as argument.
     * 
     * @param clazz the clazz of the argument objects.
     * @param objects for which to get the identifiers.
     */
    @SuppressWarnings( "unchecked" )
    public static Collection<Integer> getIdentifiers( Class clazz, Collection<? extends Object> objects )
    {
        try
        {
            Collection<Integer> identifiers = new ArrayList<Integer>();
            
            Method method = clazz.getMethod( "getId", new Class[ 0 ] );
            
            for ( Object object : objects )
            {
                Integer identifier = (Integer) method.invoke( object, new Object[ 0 ] );
                
                identifiers.add( identifier );
            }
            
            return identifiers;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to convert objects", ex );
        }
    }
    
    /**
     * Creates a collection of Integers out of a collection of Strings.
     * 
     * @param strings the collection of Strings.
     * @return a collection of Integers.
     */
    public static Collection<Integer> getIntegerCollection( Collection<String> strings )
    {
        Collection<Integer> integers = new ArrayList<Integer>();
        
        if ( strings == null )
        {
            return integers;
        }
        
        for ( String string : strings )
        {
            integers.add( Integer.valueOf( string ) );
        }
        
        return integers;
    }
}
