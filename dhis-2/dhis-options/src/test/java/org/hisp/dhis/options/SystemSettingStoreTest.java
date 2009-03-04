package org.hisp.dhis.options;

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

import org.hisp.dhis.DhisSpringTest;

/**
 * @author Stian Strandli
 * @version $Id: SystemSettingStoreTest.java 4866 2008-04-11 10:40:35Z larshelg $
 */
public class SystemSettingStoreTest
    extends DhisSpringTest
{
    SystemSettingStore systemSettingStore;

    SystemSetting settingA;

    SystemSetting settingB;

    SystemSetting settingC;

    public void setUpTest()
        throws Exception
    {
        systemSettingStore = (SystemSettingStore) getBean( SystemSettingStore.ID );

        settingA = new SystemSetting();
        settingA.setName( "Setting1" );
        settingA.setValue( new String( "Value1" ) );

        settingB = new SystemSetting();
        settingB.setName( "Setting2" );
        settingB.setValue( new String( "Value2" ) );

        settingC = new SystemSetting();
        settingC.setName( "Setting3" );
        settingC.setValue( new String( "Value3" ) );
    }

    public void testAddSystemSetting()
    {
        systemSettingStore.addSystemSetting( settingA );
        systemSettingStore.addSystemSetting( settingB );
        systemSettingStore.addSystemSetting( settingC );

        SystemSetting s = systemSettingStore.getSystemSetting( settingA.getName() );
        assertNotNull( s );
        assertEquals( "Setting1", s.getName() );
        assertEquals( "Value1", s.getValue() );

        settingA.setValue( new String( "Value1.1" ) );
        systemSettingStore.addSystemSetting( settingA );

        s = systemSettingStore.getSystemSetting( settingA.getName() );
        assertNotNull( s );
        assertEquals( "Setting1", s.getName() );
        assertEquals( "Value1.1", s.getValue() );        
    }
    
    public void testUpdateSystemSetting()
    {
        systemSettingStore.addSystemSetting( settingA );
        
        settingA = systemSettingStore.getSystemSetting( "Setting1" );
        
        assertEquals( "Value1", (String) settingA.getValue() );
        
        settingA.setValue( new String( "Value2" ) );
        
        systemSettingStore.updateSystemSetting( settingA );

        settingA = systemSettingStore.getSystemSetting( "Setting1" );
        
        assertEquals( "Value2", (String) settingA.getValue() );
    }

    public void testDeleteSystemSetting()
    {
        systemSettingStore.addSystemSetting( settingA );
        systemSettingStore.addSystemSetting( settingB );
        systemSettingStore.addSystemSetting( settingC );

        systemSettingStore.deleteSystemSetting( systemSettingStore.getSystemSetting( settingA.getName() ) );

        assertNull( systemSettingStore.getSystemSetting( settingA.getName() ) );
        assertEquals( 2, systemSettingStore.getAllSystemSettings().size() );
    }

    public void testGetSystemSetting()
    {
        systemSettingStore.addSystemSetting( settingA );
        systemSettingStore.addSystemSetting( settingB );

        SystemSetting s = systemSettingStore.getSystemSetting( "Setting1" );
        assertNotNull( s );
        assertEquals( "Setting1", s.getName() );
        assertEquals( "Value1", s.getValue() );

        s = systemSettingStore.getSystemSetting( "Setting3" );
        assertNull( s );
    }

    public void testGetAllSystemSettings()
    {
        Collection<SystemSetting> m = systemSettingStore.getAllSystemSettings();
        assertNotNull( m );
        assertEquals( 0, m.size() );

        systemSettingStore.addSystemSetting( settingA );
        systemSettingStore.addSystemSetting( settingB );

        m = systemSettingStore.getAllSystemSettings();
        assertNotNull( m );
        assertEquals( 2, m.size() );
    }
}
