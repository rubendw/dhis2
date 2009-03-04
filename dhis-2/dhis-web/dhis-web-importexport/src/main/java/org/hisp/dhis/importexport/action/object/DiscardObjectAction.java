package org.hisp.dhis.importexport.action.object;

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

import java.util.Collection;

import org.hisp.dhis.importexport.ImportObjectService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DiscardObjectAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Collection<String> id;

    public void setId( Collection<String> id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ImportObjectService importObjectService;

    public void setImportObjectService( ImportObjectService importObjectService )
    {
        this.importObjectService = importObjectService;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        int count = 0;
        
        if ( id != null )
        {
            for ( String identifier : id )
            {
                importObjectService.cascadeDeleteImportObject( Integer.parseInt( identifier ) );
                
                count++;
            }
            
            message = String.valueOf( count );
            
            return SUCCESS;
        }
        
        return ERROR;
    }
}
