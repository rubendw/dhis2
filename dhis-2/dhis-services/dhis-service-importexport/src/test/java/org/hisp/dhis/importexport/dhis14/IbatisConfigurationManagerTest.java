package org.hisp.dhis.importexport.dhis14;

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

import java.util.Properties;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.importexport.IbatisConfiguration;
import org.hisp.dhis.importexport.IbatisConfigurationManager;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class IbatisConfigurationManagerTest
    extends DhisSpringTest
{
    private static final String KEY_CONNECTION_URL_DATABASE = "ibatis.connection.url.database";
    private static final String KEY_PASSWORD = "ibatis.connection.password";
    private static final String KEY_USERNAME = "ibatis.connection.username";
    private static final String KEY_LEVELS = "ibatis.levels";   
    
    private IbatisConfigurationManager configurationManager;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        configurationManager = (IbatisConfigurationManager) getBean( IbatisConfigurationManager.ID );
        
        setDependency( configurationManager, "configDir", "test", String.class );
        setDependency( configurationManager, "configFile", "testIbatisConfiguration.xml", String.class );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testSetGetIbatisConfiguration()
        throws Exception
    {
        IbatisConfiguration config = new IbatisConfiguration();
        
        config.setDataFile( "dataFile" );
        config.setUserName( "userName" );
        config.setPassword( "password" );
        config.setLevels( "8" );
        
        configurationManager.setIbatisConfiguration( config );
        
        IbatisConfiguration receivedConfig = configurationManager.getIbatisConfiguration();
        
        assertEquals( config, receivedConfig );
    }
    
    public void testGetPropertiesConfiguration()
        throws Exception
    {
        IbatisConfiguration config = new IbatisConfiguration();
        
        config.setDataFile( "dataFile" );
        config.setUserName( "userName" );
        config.setPassword( "password" );
        config.setLevels( "8" );
        
        configurationManager.setIbatisConfiguration( config );
        
        Properties properties = configurationManager.getPropertiesConfiguration();
        
        assertEquals( properties.getProperty( KEY_CONNECTION_URL_DATABASE ), "dataFile" );
        assertEquals( properties.getProperty( KEY_USERNAME ), "userName" );
        assertEquals( properties.getProperty( KEY_PASSWORD ), "password" );
        assertEquals( properties.getProperty( KEY_LEVELS ), "8" );
    } 
}
