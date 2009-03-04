package org.hisp.dhis.reporting.dataset.generators;

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
import java.util.Collection;
import java.util.SortedMap;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public interface TabularReportGenerator
{
    /**
     * Generates a Jasper report preview.
     * @param reportName Name of the report
     * @param reportElements The elements to include in the report
     * @param chartElements The elements to include in the chart
     * @return A report in a html string
     * @throws GeneratorException
     */
    String generateReportPreview( String reportName, SortedMap<String, String> reportElements, 
        SortedMap<String, Collection<String>> complexReportElements )
            throws GeneratorException;
    
    /**
     * Generates a Jasper report PDF file.
     * @param reportName Name of the report
     * @param reportElements The elements to include in the report
     * @param chartElements The elements to include in the chart
     * @throws GeneratorException
     */
    void generateReportFile( String reportName, SortedMap<String, String> reportElements, 
        SortedMap<String, Collection<String>> complexReportElements )
            throws GeneratorException;

    /**
     * Generates a Jasper report stream for download.
     * @param reportName Name of the report
     * @param reportElements The elements to include in the report
     * @param chartElements The elements to include in the chart
     * @return A inputstream containg a report
     * @throws GeneratorException
     */
    InputStream generateReportStream( String reportName, SortedMap<String, String> reportElements, 
        SortedMap<String, Collection<String>> complexReportElements )
            throws GeneratorException;
}
