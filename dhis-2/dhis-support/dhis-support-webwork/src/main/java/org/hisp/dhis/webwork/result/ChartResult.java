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

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;

public class ChartResult 
    implements Result
{    
    private int width;

    public void setWidth( int width )
    {
        this.width = width;
    }
    
    private int height;

    public void setHeight( int height )
    {
        this.height = height;
    }

    public void execute( ActionInvocation invocation )
        throws Exception
    {
        JFreeChart chart = (JFreeChart) invocation.getStack().findValue( "chart" );
        
        HttpServletResponse response = ServletActionContext.getResponse();
        
        OutputStream outputStream = response.getOutputStream();
        
        ChartUtilities.writeChartAsPNG( outputStream, chart, width, height );
        
        outputStream.flush();
    }
}
