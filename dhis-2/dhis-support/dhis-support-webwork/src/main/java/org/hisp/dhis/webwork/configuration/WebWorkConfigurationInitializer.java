package org.hisp.dhis.webwork.configuration;

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

import java.net.URL;
import java.util.Enumeration;

import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.ConfigurationProvider;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: WebWorkConfigurationInitializer.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class WebWorkConfigurationInitializer
{
    private static final String CONFIG_PATH = "xwork.xml";

    public void init()
        throws Exception
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> configURLs = classLoader.getResources( CONFIG_PATH );

        while ( configURLs.hasMoreElements() )
        {
            URL url = configURLs.nextElement();

            ConfigurationProvider configurationProvider = new UrlXmlConfigurationProvider( url.toExternalForm() );

            ConfigurationManager.addConfigurationProvider( configurationProvider );
        }
    }
}
