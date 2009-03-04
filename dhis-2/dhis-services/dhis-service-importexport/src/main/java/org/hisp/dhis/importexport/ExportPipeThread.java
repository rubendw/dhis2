package org.hisp.dhis.importexport;

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
import java.util.zip.ZipOutputStream;

import org.amplecode.staxwax.writer.XMLWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.util.StreamUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ExportPipeThread
    extends Thread
{
    protected static final Log log = LogFactory.getLog( ExportPipeThread.class );
    
    private static final String VERSION = "1.0";
    
    private List<XMLConverter> xsdConverters = new ArrayList<XMLConverter>();
    private List<XMLConverter> xmlConverters = new ArrayList<XMLConverter>();

    // -------------------------------------------------------------------------
    // Parameters
    // -------------------------------------------------------------------------
    
    protected ZipOutputStream zipOutputStream;

    public void setZipOutputStream( ZipOutputStream zipOutputStream )
    {
        this.zipOutputStream = zipOutputStream;
    }
    
    protected ExportParams params;

    public void setParams( ExportParams params )
    {
        this.params = params;
    }
    
    protected XMLWriter writer;

    public void setWriter( XMLWriter writer )
    {
        this.writer = writer;
    }
    
    private String encoding;

    public void setEncoding( String encoding )
    {
        this.encoding = encoding;
    }
    
    private String rootName;

    public void setRootName( String rootName )
    {
        this.rootName = rootName;
    }
    
    private String[] rootProperties = new String[0];

    public void setRootProperties( String[] rootProperties )
    {
        this.rootProperties = rootProperties;
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public ExportPipeThread()
    {   
    }
    
    // -------------------------------------------------------------------------
    // PipeThread methods
    // -------------------------------------------------------------------------
    
    public void registerXSDConverter( XMLConverter converter )
    {
        this.xsdConverters.add( converter );
    }
    
    public void registerXMLConverter( XMLConverter converter )
    {
        this.xmlConverters.add( converter );
    }

    // -------------------------------------------------------------------------
    // Thread implementation
    // -------------------------------------------------------------------------

    public void run()
    {
        try
        {
            log.info( "Export started" );
            
            openDocument( writer );

            // -----------------------------------------------------------------
            // XSD
            // -----------------------------------------------------------------
            
            beforeXSD( writer );
            
            for ( XMLConverter converter : xsdConverters )
            {
                converter.write( writer, params );
            }
            
            afterXSD( writer );

            // -----------------------------------------------------------------
            // XML
            // -----------------------------------------------------------------
            
            beforeXML( writer );
            
            for ( XMLConverter converter : xmlConverters )
            {
                converter.write( writer, params );
            }
            
            afterXML( writer );
            
            closeDocument( writer );
            
            log.info( "Export done" );
        }
        finally
        {
            StreamUtils.finishZipEntry( zipOutputStream );
            
            StreamUtils.closeOutputStream( zipOutputStream );
        }
    }

    // -------------------------------------------------------------------------
    // Overridable methods
    // -------------------------------------------------------------------------
    
    @SuppressWarnings( "unused" )
    protected void beforeXSD( XMLWriter writer )
    {        
    }
    
    @SuppressWarnings( "unused" )
    protected void afterXSD( XMLWriter writer )
    {   
    }
    
    @SuppressWarnings( "unused" )
    protected void beforeXML( XMLWriter writer )
    {   
    }
    
    @SuppressWarnings( "unused" )
    protected void afterXML( XMLWriter writer )
    {   
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private void openDocument( XMLWriter writer )
    {
        writer.openDocument( encoding, VERSION );
        
        writer.openElement( rootName, rootProperties );
    }

    private void closeDocument( XMLWriter writer )
    {
        writer.closeElement( rootName );
        
        writer.closeDocument( encoding );
    }
}
