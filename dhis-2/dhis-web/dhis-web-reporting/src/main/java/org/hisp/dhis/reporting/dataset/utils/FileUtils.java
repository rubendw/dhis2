package org.hisp.dhis.reporting.dataset.utils;

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

import static java.io.File.separator;

/**
 * @author Lars Helge Overland
 * @version $Id: FileUtils.java 5181 2008-05-20 15:15:38Z larshelg $
 */
public class FileUtils
{
    public static final String XML_DIR = "rt" + separator + "design" + separator + "xml" + separator;
    public static final String JRXML_DIR = "rt" + separator + "design" + separator + "jrxml" + separator;
    public static final String PDF_DIR = "rt" + separator + "reports" + separator + "PDF" + separator;
    public static final String HTML_DIR = "rt" + separator + "reports" + separator + "HTML" + separator;
    
    public static final String XML_EXT = ".xml";
    public static final String JRXML_EXT = ".jrxml";
    public static final String PDF_EXT = ".pdf";
    public static final String HTML_EXT = ".html";    
    
    /**
     * Creates a valid Java class name by removing illegal characters.
     * @param input Input string
     * @return Valid Java class name string
     */
    public static String getQualifiedFileName( String name )
    {    
        String pattern = "[ (){}\\[\\]\\'\\+\\*\\/\\-\\\\]";
        String replacement = "";        
        name = name.replaceAll( pattern, replacement );        
        
        return name;
    }
    
    /**
     * Gets the extension of a file.
     * @param file The file
     * @return The extension of the file
     */
    public static String getExtension( File file )
    {
        if ( file.exists() )
        {
            String path = file.getName();
            return path.substring( path.lastIndexOf( "." ) + 1, path.length() );
        }
        
        return null;
    }
    
    /**
     * Gets the extension of a file.
     * @param fileName The file name
     * @return The extension of the file
     */
    public static String getExtension( String fileName )
    {
        return fileName.substring( fileName.lastIndexOf( "." ) + 1, fileName.length() );
    }
    
    /**
     * Gets the basename of a file.
     * @param file The file
     * @return The basename of the file
     */
    public static String getBaseName( File file )
    {
        if ( file.exists() )
        {
            String path = file.getName();
            return path.substring( 0 , path.lastIndexOf( "." ) );
        }
        
        return null;
    }
    
    /**
     * Gets the basename of a file.
     * @param file The file name
     * @return The basename of the file
     */    
    public static String getBaseName( String fileName )
    {
        return fileName.substring( 0 , fileName.lastIndexOf( "." ) );
    }
    
    /**
     * Gets the path to an XML file for the given name
     * @param name the file name
     * @return the path to the xml file
     */
    public static String getXMLFileName( String name )
    {
        return XMLUtils.encode( getQualifiedFileName( name ) + XML_EXT );
    }
    
    /**
     * Gets the path to an JEXML file for the given name
     * @param name the file name
     * @return the path to the xml file
     */
    public static String getJRXMLFileName( String name )
    {
        return XMLUtils.encode( getQualifiedFileName( name ) + JRXML_EXT );
    }
}
