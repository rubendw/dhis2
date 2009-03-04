package org.hisp.dhis.system.util;

import java.util.Collection;

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

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class TextUtils
{
    /**
     * Gets the sub string of the given string. If the beginIndex is larger than
     * the length of the string, the empty string is returned. If the beginIndex +
     * the length is larger than the length of the string, the part of the string
     * following the beginIndex is returned.
     * 
     * @param string the string.
     * @param beginIndex the zero-based begin index.
     * @param length the length of the sub string starting at the begin index.
     * @return the sub string of the given string.
     */
    public static String subString( String string, int beginIndex, int length )
    {
        int endIndex = beginIndex + length;
        
        if ( beginIndex >= string.length()  )
        {
            return new String();
        }
        
        if ( endIndex > string.length() )
        {
            return string.substring( beginIndex, string.length() );
        }
        
        return string.substring( beginIndex, endIndex );
    }

    /**
     * Transforms a collection of Integers into a comma delimited String.
     * 
     * @param elements the collection of Integers
     * @return a comma delimited String.
     */
    public static String getCommaDelimitedString( Collection<Integer> elements )
    {
        if ( elements != null && elements.size() > 0 )
        {
            StringBuffer buffer = new StringBuffer();        
        
            for ( Integer element : elements )
            {
                buffer.append( element.toString() + ", " );
            }
            
            return buffer.substring( 0, buffer.length() - ", ".length() );
        }
        
        return null;
    }
    
    /**
     * Returns null if the given string is not null and contains no charachters,
     * the string itselft otherwise.
     * 
     * @param string the string.
     * @return null if the given string is not null and contains no charachters,
     *         the string itself otherwise.
     */
    public static String nullIfEmpty( String string )
    {
        return string != null && string.trim().length() == 0 ? null : string;
    }
}
