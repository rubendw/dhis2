package org.hisp.dhis.importexport.action.ixf;

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
import java.util.List;

import org.hisp.dhis.importexport.ixf.config.IXFConfiguration;
import org.hisp.dhis.importexport.ixf.config.IXFConfigurationManager;
import org.hisp.dhis.importexport.ixf.config.IXFCountry;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class SetIXFConfigurationAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private String comment;

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    private String countryKey;

    public void setCountryKey( String countryKey )
    {
        this.countryKey = countryKey;
    }
    
    private List<String> levelNames;

    public void setLevelNames( List<String> levelNames )
    {
        this.levelNames = levelNames;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private IXFConfigurationManager configurationManager;

    public void setConfigurationManager( IXFConfigurationManager configurationManager )
    {
        this.configurationManager = configurationManager;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // -------------------------------------------------------------------------
        // Switching from XWork to Java List implementation
        // -------------------------------------------------------------------------

        List<String> javaLevelNames = new ArrayList<String>();
        
        javaLevelNames.addAll( levelNames );
        
        IXFCountry country = configurationManager.getCountry( countryKey );
        
        IXFConfiguration config = new IXFConfiguration();

        config.setComment( comment );
        config.setCountry( country );
        config.setLevelNames( javaLevelNames );
        
        configurationManager.setConfiguration( config );
        
        return SUCCESS;
    }
}
