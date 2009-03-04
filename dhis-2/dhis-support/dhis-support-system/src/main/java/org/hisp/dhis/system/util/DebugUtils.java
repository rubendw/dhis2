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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DebugUtils
{
    private static final Log log = LogFactory.getLog( DebugUtils.class );
    
    public static final String SEPARATOR = "-";
    
    private static ThreadLocal<Map<String, List<String>>> duplicateMapPeg = new ThreadLocal<Map<String,List<String>>>(); 
    
    public static String logDuplicates( String key, Integer... values )
    {
        StringBuffer buffer = new StringBuffer();
        
        for ( Integer value : values )
        {
            buffer.append( value + SEPARATOR );
        }
        
        String value = buffer.toString();
        
        value = value.substring( 0, value.length() - 1 );
        
        return log( key, value );
    }
    
    public static String logDuplicates( String key, String... values )
    {
        StringBuffer buffer = new StringBuffer();
        
        for ( String value : values )
        {
            buffer.append( value + SEPARATOR );
        }

        String value = buffer.toString();
        
        value = value.substring( 0, value.length() - 1 );
        
        return log( key, value );
    }
    
    private static String log( String key, String value )
    {   
        Map<String, List<String>> duplicateMap = duplicateMapPeg.get();
        
        if ( duplicateMap == null )
        {
            duplicateMap = new HashMap<String, List<String>>();
        }
        
        List<String> list = duplicateMap.get( key );

        if ( list == null )
        {
            list = new ArrayList<String>();
        }
                
        String duplicate = null;
        
        if ( list.contains( value ) )
        {
            log.warn( "Duplicate found: '" + value + "' for key: '" + key + "'" );
            
            duplicate = value;
        }
        
        list.add( value );
        
        duplicateMap.put( key, list );
        
        duplicateMapPeg.set( duplicateMap );

        return duplicate;
    }
    
    public static boolean resetDuplicates( String key )
    {
        Map<String, List<String>> duplicateMap = duplicateMapPeg.get();
        
        if ( duplicateMap != null )
        {
            duplicateMap.remove( key );
        
            duplicateMapPeg.set( duplicateMap );
            
            log.info( "Reset duplicate for key: '" + key + "'" );
            
            return true;
        }
        
        return false;
    }
}
