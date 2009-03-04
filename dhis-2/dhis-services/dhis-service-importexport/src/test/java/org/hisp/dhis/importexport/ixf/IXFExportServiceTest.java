package org.hisp.dhis.importexport.ixf;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.importexport.ExportService;
import org.hisp.dhis.importexport.ixf.mock.MockIXFConfigurationManager;
import org.hisp.dhis.importexport.util.ImportExportUtils;
import org.hisp.dhis.system.util.StreamUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class IXFExportServiceTest
    extends DhisSpringTest
{
    private ExportService exportService;
    
    private InputStream referenceStream;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        exportService = (ExportService) getBean( "org.hisp.dhis.importexport.IXFExportService" );

        setDependency( exportService, "configurationManager", new MockIXFConfigurationManager() );

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        referenceStream = classLoader.getResourceAsStream( "ixf_export.zip" );
    }
    
    public void tearDownTest()
    {
        StreamUtils.closeInputStream( referenceStream );
    }
    
    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    public void testExport()
    {
        InputStream generatedStream = exportService.exportData( ImportExportUtils.getExportParams( 3, true ) );
        
        //ImportExportUtils.generateFile( generatedStream ); //Generate reference file
        
        int[] ignoredBytePos = { 284, 285, 286, 287, 289, 290, 292, 293, 295, 296, 298, 299, 301, 302 }; //Timestamp tag

        assertTrue( ImportExportUtils.streamsAreEqual( referenceStream, generatedStream, ignoredBytePos ) );
    }
}
