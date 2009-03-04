package org.hisp.dhis.gis.util;

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class FileUtils
{

   
    public static List<String> getListFile( File directory )
    {
        List<String> result = new ArrayList<String>();

        if ( directory != null )
        {
            for ( File f : directory.listFiles( new SVGFileFilter() ) )
            {
                if ( !(f.getName().equals( "example.svg" ) || f.getName().equals( "temp.svg" )) )
                {
                    result.add( f.getName() );
                }

            }
        }

        return result;
    }

    public static boolean copyFile( String input, String output )
    {
        File inputFile = new File( input );
        File outputFile = new File( output );

        try
        {
            FileReader in = new FileReader( inputFile );
            FileWriter out = new FileWriter( outputFile );

            int c;

            while ( (c = in.read()) != -1 )
                out.write( c );

            in.close();
            out.close();

        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public static boolean copyFile( File input, File output )
    {

        try
        {
            FileReader in = new FileReader( input );
            FileWriter out = new FileWriter( output );

            int c;

            while ( (c = in.read()) != -1 )
                out.write( c );

            in.close();
            out.close();

        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public static boolean delete( String path )
    {
        new File( path ).delete();
        return true;
    }

    public static boolean isExist( String fileName )
    {
        try
        {
            new FileReader( fileName );
            return true;
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            return false;
        }
    }
}
