package org.hisp.dhis.reporting.dataset.utils;

/*
 * Copyright (c) 2004-2007, Lars Helge �verland
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
import java.util.Map.Entry;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class XMLUtils
{
    private static final String EMPTY = "";
    
    private static Map<String, String> escapeCharacters;
    
    static
    {
        escapeCharacters = new HashMap<String, String>();
        
        escapeCharacters.put( "&", "&amp;" );
        escapeCharacters.put( "<", "&lt;" );
        escapeCharacters.put( ">", "&gt;" );
        escapeCharacters.put( "\"", "&quot;" );
        escapeCharacters.put( "'", "&apos;" );
    }

    /**
     * Encodes a string to be appropriate for use in an XML file.
     *  
     * @param value the string.
     * @return the encoded string.
     */
    public static String encode( String value )
    {
        if ( value == null )
        {
            return EMPTY;
        }
        
        for ( Entry<String, String> entry : escapeCharacters.entrySet() )
        {
            value = value.replaceAll( entry.getKey(), entry.getValue() );
        }
        
        return value;
    }
    
    /**
     * Decodes a string used in an XML file.
     * 
     * @param value the string.
     * @return the decoded string.
     */
    public static String decode( String value )
    {
        for ( Entry<String, String> entry : escapeCharacters.entrySet() )
        {
            value = value.replaceAll( entry.getValue(), entry.getKey() );
        }
        
        return value;
    }
}
