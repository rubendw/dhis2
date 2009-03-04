package org.hisp.dhis.external.configuration;

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

import java.io.InputStream;
import java.io.OutputStream;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ConfigurationManagerTest
    extends DhisSpringTest
{
    private ConfigurationManager<DummyConfiguration> configurationManager;
    
    private LocationManager locationManager;
    
    private DummyConfiguration configurationA;
    private DummyConfiguration configurationB;
    
    @SuppressWarnings( "unchecked" )
    public void setUpTest()
    {
        configurationManager = (ConfigurationManager<DummyConfiguration>) getBean( ConfigurationManager.ID );
        
        locationManager = (LocationManager) getBean( LocationManager.ID );
        
        configurationA = new DummyConfiguration( "homeA", "directoryA" );
        configurationB = new DummyConfiguration( "homeB", "directoryB" );
    }
    
    public void testSetGet()
        throws Exception
    {
        try
        {
            OutputStream outA = locationManager.getOutputStream( "safeToDeleteA.xml", "test" );
            OutputStream outB = locationManager.getOutputStream( "safeToDeleteB.xml", "test" );
            
            configurationManager.setConfiguration( configurationA, outA );
            configurationManager.setConfiguration( configurationB, outB );
            
            InputStream inA = locationManager.getInputStream( "safeToDeleteA.xml", "test" );
            InputStream inB = locationManager.getInputStream( "safeToDeleteB.xml", "test" );
            
            DummyConfiguration receivedA = configurationManager.getConfiguration( inA, DummyConfiguration.class );
            DummyConfiguration receivedB = configurationManager.getConfiguration( inB, DummyConfiguration.class );
            
            assertNotNull( receivedA );
            assertNotNull( receivedB );
            
            assertEquals( configurationA, receivedA );
            assertEquals( configurationB, receivedB );
        }
        catch ( LocationManagerException ex )
        {
            // External directory not set
        }
    }
}
