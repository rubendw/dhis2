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

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CodecUtils
{
    private static final Log log = LogFactory.getLog( CodecUtils.class );
    
    private static final String CHARSET = "8859_1";

    private static final Base64 codec = new Base64();

    /**
     * Encrypts a string with Base64 encoding.
     * 
     * @param string the string to encrypt.
     */
    public static String encryptBase64( String string )
    {
        try
        {
            byte[] data = codec.encode( string.getBytes( CHARSET ) );
            
            return new String( data, CHARSET );
        }
        catch ( UnsupportedEncodingException e )
        {
            log.warn( "Could not encrypt string because of unsupported encoding: " + CHARSET );
            
            return null;
        }
    }

    /**
     * Decrypts a string with Base64 encoding.
     * 
     * @param string the string to decrypt.
     */
    public static String decryptBase64( String string )
    {
        try
        {
            byte[] data = codec.decode( string.getBytes( CHARSET ) );
            
            return new String( data, CHARSET );
        }
        catch ( UnsupportedEncodingException e )
        {
            log.warn( "Could not decrypt string because of unsupported encoding: " + CHARSET );
            
            return null;
        }
    }
}
