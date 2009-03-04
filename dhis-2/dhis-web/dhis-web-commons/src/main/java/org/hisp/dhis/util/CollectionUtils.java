package org.hisp.dhis.util;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: CollectionUtils.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class CollectionUtils
{
    /**
     * Creates a map with the elements of the collection as values using the
     * specified keyMethod to obtain the key from the elements.
     */
    @SuppressWarnings( "unchecked" )
    public static <K, T> Map<K, T> createMap( Collection<T> collection, String keyMethod )
    {
        Map<K, T> map = new HashMap<K, T>( collection.size() );

        if ( collection.isEmpty() )
        {
            return map;
        }

        Class elementClass = collection.iterator().next().getClass();

        Method getKeyMethod;

        try
        {
            getKeyMethod = elementClass.getMethod( keyMethod, new Class[0] );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Failed to get key method", e );
        }

        for ( T element : collection )
        {
            K key;
            try
            {
                key = (K) getKeyMethod.invoke( element, (Object[]) null );
            }
            catch ( Exception e )
            {
                throw new RuntimeException( "Failed to get key", e );
            }

            map.put( key, element );
        }

        return map;
    }

    /**
     * Creates a list of values extracted from the provided list using the
     * specified value method, keeping the order of the provided list.
     */
    @SuppressWarnings( "unchecked" )
    public static <K, T> List<K> createList( List<T> list, String valueMethod )
    {
        List<K> valueList = new ArrayList<K>( list.size() );

        if ( list.isEmpty() )
        {
            return valueList;
        }

        Class elementClass = list.iterator().next().getClass();

        Method getValueMethod;

        try
        {
            getValueMethod = elementClass.getMethod( valueMethod, new Class[0] );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Failed to get key method", e );
        }

        for ( T element : list )
        {
            K value;

            try
            {
                value = (K) getValueMethod.invoke( element, (Object[]) null );
            }
            catch ( Exception e )
            {
                throw new RuntimeException( "Failed to get key", e );
            }

            valueList.add( value );
        }

        return valueList;
    }
}
