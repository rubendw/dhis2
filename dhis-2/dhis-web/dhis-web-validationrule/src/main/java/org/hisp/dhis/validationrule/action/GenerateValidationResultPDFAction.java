package org.hisp.dhis.validationrule.action;

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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collection;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.util.SessionUtils;
import org.hisp.dhis.validation.ValidationResult;
import org.hisp.dhis.validationrule.util.OutputGeneratorPipeThread;
import org.hisp.dhis.validationrule.util.ValidationResultPDFGenerator;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GenerateValidationResultPDFAction
    extends ActionSupport
{
    private static final String KEY_VALIDATIONRESULT = "validationResult";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }
    
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private InputStream inputStream;

    public InputStream getInputStream()
    {
        return inputStream;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public String execute()
        throws Exception
    {
        Collection<ValidationResult> results = (Collection<ValidationResult>) SessionUtils.
            getSessionVar( KEY_VALIDATIONRESULT );
        
        if ( results != null )
        {
            // -----------------------------------------------------------------
            // Pipes are input/output pairs. Data written on the output stream 
            // shows up on the input stream at the other end of the pipe. 
            // -----------------------------------------------------------------
            
            PipedOutputStream out = new PipedOutputStream();
            
            PipedInputStream in = new PipedInputStream( out );
            
            OutputGeneratorPipeThread thread = new OutputGeneratorPipeThread();
            
            thread.setValidationResults( results );
            thread.setOutputStream( out );
            thread.setI18n( i18n );
            thread.setFormat( format );
            thread.setOutputGenerator( new ValidationResultPDFGenerator() );
            
            thread.start();
            
            inputStream = new BufferedInputStream( in );
            
            return SUCCESS;
        }
        
        return NONE;
    }
}
