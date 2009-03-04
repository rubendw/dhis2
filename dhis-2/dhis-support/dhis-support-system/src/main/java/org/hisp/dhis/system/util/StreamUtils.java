package org.hisp.dhis.system.util;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class StreamUtils
{
    private static final String LINE_BREAK = "\n";
    private static final String ENCODING_UTF = "UTF8";
    
    /**
     * Loads a resorce from the classpath defined by the name parameter.
     * 
     * @param name the name of the resource.
     * @return an InputStream.
     */
    public static InputStream loadResource( String name )
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
        return classLoader.getResourceAsStream( name );
    }

    /**
     * Writes the content of the StringBuffer to the file.
     * 
     * @param file the file to write to.
     * @param content the content to write.
     * @throws IOException
     */
    public static void writeContent( File file, StringBuffer content )
        throws IOException
    {
        BufferedWriter writer = new BufferedWriter( 
            new OutputStreamWriter( new FileOutputStream( file ), ENCODING_UTF ) );
        
        try
        {
            writer.write( content.toString() );
        }
        finally
        {
            try
            {
                writer.flush();
            }
            catch ( Exception ex )
            {   
            }
            
            try
            {
                writer.close();
            }
            catch ( Exception ex )
            {   
            }
        }
    }
    
    /**
     * Reads the content of the file to a StringBuffer. Each line is compared to
     * the keys of the argument map. If a line is matched, the line is replaced with
     * the keys corresponding value.
     * 
     * @param file the file to read from.
     * @param replaceMap a map containing keys to be matched and values with replacements.
     * @return a StringBuffer with the content of the file replaced according to the Map.
     * @throws IOException
     */
    public static StringBuffer readContent( File file, Map<String, String> replaceMap )
        throws IOException
    {
        StringBuffer content = new StringBuffer();
        
        BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ), ENCODING_UTF ) );
        
        String line = null;
        
        try
        {   
            while ( ( line = reader.readLine() ) != null )
            {
                if ( replaceMap != null )
                {
                    for ( Entry<String, String> entry : replaceMap.entrySet() )
                    {
                        if ( line.contains( entry.getKey() ) )
                        {
                            line = entry.getValue();
                        }
                    }
                }
                
                content.append( line + LINE_BREAK );
            }
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch ( Exception ex )
            {   
            }
        }
        
        return content;
    }
    
    /**
     * Closes the given Reader.
     * 
     * @param reader the Reader to close.
     */
    public static void closeReader( Reader reader )
    {
        if ( reader != null )
        {
            try
            {
                reader.close();
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Closes the given Writer.
     * 
     * @param writer the Writer to close.
     */
    public static void closeWriter( Writer writer )
    {
        if ( writer != null )
        {
            try
            {
                writer.flush();
            }
            catch ( IOException ex )
            {
                ex.printStackTrace();
            }
            
            try
            {
                writer.close();
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Closes the given InputStream.
     * 
     * @param in the InputStream to close.
     */
    public static void closeInputStream( InputStream in )
    {
        if ( in != null )
        {
            try
            {
                in.close();
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Closes and flushes the given OutputStream.
     * 
     * @param out the OutputStream to close.
     */
    public static void closeOutputStream( OutputStream out )
    {
        if ( out != null )
        {
            try
            {
                out.flush();
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }
            
            try
            {
                out.close();
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Reads the next ZIP file entry from the ZipInputStream and positions the 
     * stream at the beginning of the entry data.
     * 
     * @param in the ZipInputStream to read from.
     */
    public static void getNextZipEntry( ZipInputStream in )
    {
        try
        {
            in.getNextEntry();
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get next entry in ZIP-file", ex );
        }
    }
    
    /**
     * Finishes writing the contents of the ZIP output stream without closing the underlying stream.
     * 
     * @param out the ZipOutputStream to write to.
     */
    public static void finishZipEntry( ZipOutputStream out )
    {
        try
        {
            out.finish();
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to finish ZipOutputStream", ex );
        }
    }
}
