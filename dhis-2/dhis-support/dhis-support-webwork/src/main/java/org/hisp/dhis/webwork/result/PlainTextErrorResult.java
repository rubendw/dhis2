package org.hisp.dhis.webwork.result;

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

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.TextParseUtil;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: PlainTextErrorResult.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class PlainTextErrorResult
    implements Result
{
    // -------------------------------------------------------------------------
    // Parameters
    // -------------------------------------------------------------------------

    private boolean parse = true;

    public void setParse( boolean parse )
    {
        this.parse = parse;
    }

    private String message;

    public void setMessage( String message )
    {
        this.message = message;
    }

    // -------------------------------------------------------------------------
    // Result implementation
    // -------------------------------------------------------------------------

    public void execute( ActionInvocation invocation )
        throws Exception
    {
        HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(
            WebWorkStatics.HTTP_RESPONSE );

        response.setContentType( "text/plain; charset=UTF-8" );
        response.setHeader( "Content-Disposition", "inline" );
        response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );

        OgnlValueStack stack = ActionContext.getContext().getValueStack();
        String finalMessage = parse ? TextParseUtil.translateVariables( message, stack ) : message;

        // ---------------------------------------------------------------------
        // Write final message
        // ---------------------------------------------------------------------

        PrintWriter writer = null;

        try
        {
            writer = response.getWriter();
            writer.print( finalMessage );
            writer.flush();
        }
        finally
        {
            if ( writer != null )
            {
                writer.close();
            }
        }
    }
}
