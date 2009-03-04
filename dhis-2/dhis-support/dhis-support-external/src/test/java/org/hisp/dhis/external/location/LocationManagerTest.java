package org.hisp.dhis.external.location;

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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.hisp.dhis.DhisSpringTest;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class LocationManagerTest
    extends DhisSpringTest
{
    private LocationManager locationManager;
    
    private InputStream in;
    
    private OutputStream out;
    
    private File file;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        locationManager = (LocationManager) getBean( LocationManager.ID );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // InputStream
    // -------------------------------------------------------------------------

    public void testGetInputStream()
    {
        try
        {
            in = locationManager.getInputStream( "test.properties" );
            
            assertNotNull( in );
        }
        catch ( LocationManagerException ex )
        {
            // External directory not set or the file does not exist
        }
        
        try
        {
            in = locationManager.getInputStream( "doesnotexist.properties" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    public void testInputStreamWithDirs()
    {
        try
        {
            in = locationManager.getInputStream( "test.properties", "test", "dir" );

            assertNotNull( in );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set or the file does not exist" );
        }
                
        try
        {
            in = locationManager.getInputStream( "doesnotexist.properties", "does", "not", "exist" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    // -------------------------------------------------------------------------
    // File for reading
    // -------------------------------------------------------------------------

    public void testGetFileForReading()
    {
        try
        {
            file = locationManager.getFileForReading( "test.properties" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "File for reading: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set or the file does not exist" );
        }
        
        try
        {
            file = locationManager.getFileForReading( "doesnotexist.properties" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    public void testGetFileForReadingWithDirs()
    {
        try
        {
            file = locationManager.getFileForReading( "test.properties", "test", "dir" );

            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "File for reading with dirs: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set or the file does not exist" );
        }
                
        try
        {
            file = locationManager.getFileForReading( "doesnotexist.properties", "does", "not", "exist" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    // -------------------------------------------------------------------------
    // OutputStream
    // -------------------------------------------------------------------------

    public void testGetOutputStream()
    {
        try
        {
            out = locationManager.getOutputStream( "test.properties" );
            
            assertNotNull( out );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
    }
    
    public void testGetOutputStreamWithDirs()
    {
        try
        {
            out = locationManager.getOutputStream( "test.properties", "test" );
            
            assertNotNull( out );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
    }

    // -------------------------------------------------------------------------
    // File for writing
    // -------------------------------------------------------------------------

    public void testGetFileForWriting()
    {
        try
        {
            file = locationManager.getFileForWriting( "test.properties" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "File for writing: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
        
        try
        {
            file = locationManager.getFileForWriting( "doesnotexist.properties" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "File for writing which does not exist: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
    }
    
    public void testGetFileForWritingWithDirs()
    {
        try
        {
            file = locationManager.getFileForWriting( "test.properties", "test" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "File for writing with dirs: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
        
        try
        {
            file = locationManager.getFileForWriting( "doesnotexist.properties", "does", "not", "exist" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "File for writing with dirs which does not exist: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
    }

    // -------------------------------------------------------------------------
    // External directory and environment variables
    // -------------------------------------------------------------------------

    public void testGetExternalDirectory()
    {
        try
        {
            file = locationManager.getExternalDirectory();

            assertTrue( file.getAbsolutePath().length() > 0 );
            
            System.out.println( "External directory: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            System.out.println( "External directory not set" );
        }
    }
    
    public void testExternalDirectorySet()
    {        
        boolean set = locationManager.externalDirectorySet();
        
        if ( set )
        {
            System.out.println( "External directory set" );
        }
        else
        {
            System.out.println( "External directory not set" );
        }
    }
    
    public void testGetEnvironmentVariable()
    {
        String env = locationManager.getEnvironmentVariable();
        
        System.out.println( "Environment variable: " + env );
    }
}
