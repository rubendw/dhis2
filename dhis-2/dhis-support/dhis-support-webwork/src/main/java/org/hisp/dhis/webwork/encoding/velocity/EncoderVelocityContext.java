package org.hisp.dhis.webwork.encoding.velocity;

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

import org.apache.velocity.VelocityContext;

import com.opensymphony.util.TextUtils;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: EncoderVelocityContext.java 5824 2008-10-07 18:00:24Z larshelg $
 */
public class EncoderVelocityContext
    extends VelocityContext
{
    public static final String KEY = "encoder";

    private static final Encoder ENCODER = new Encoder();

    // -------------------------------------------------------------------------
    // Override VelocityContext methods
    // -------------------------------------------------------------------------

    @Override
    public Object internalGet( String key )
    {
        if ( KEY.equals( key ) )
        {
            return ENCODER;
        }

        return super.internalGet( key );
    }

    @Override
    public boolean containsKey( Object key )
    {
        return KEY.equals( key ) || super.containsKey( key );
    }

    // -------------------------------------------------------------------------
    // Encoder class
    // -------------------------------------------------------------------------

    public static class Encoder
    {
        private static final String ESCAPE_JS = "\\\\";

        private static final String QUOTE_JS = "'";

        // ---------------------------------------------------------------------
        // Public encode methods
        // ---------------------------------------------------------------------

        public String htmlEncode( Object object )
        {
            return xmlEncode( object );
        }

        public String xmlEncode( Object object )
        {
            if ( object == null )
            {
                return null;
            }

            return defaultEncode( object.toString() );
        }

        public String jsEncode( Object object )
        {
            if ( object == null )
            {
                return null;
            }

            String tmp = defaultEncode( object.toString() );
            tmp = jsEscape( tmp, QUOTE_JS );

            return tmp;
        }

        public String jsEscape( Object object, String quoteChar )
        {
            if ( object == null )
            {
                return null;
            }

            String tmp = object.toString();

            tmp = tmp.replaceAll( ESCAPE_JS, ESCAPE_JS + ESCAPE_JS );
            tmp = tmp.replaceAll( quoteChar, ESCAPE_JS + quoteChar );

            return tmp;
        }

        // ---------------------------------------------------------------------
        // Default encode method
        // ---------------------------------------------------------------------

        private String defaultEncode( String string )
        {
            return TextUtils.htmlEncode( string );
        }
    }
}
