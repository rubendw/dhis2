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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.reporting.dataset.report.ChartElement;
import org.hisp.dhis.reporting.dataset.utils.FileUtils;
import com.opensymphony.webwork.ServletActionContext;

/**
 * @author Lars Helge Overland
 * @version $Id: ReportGenerator.java 4103 2007-11-26 14:20:07Z larshelg $
 */
public class JasperReportGenerator
    implements ReportGenerator
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------         

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }
    
    // -------------------------------------------------------------------------
    // ReportGenerator implementation
    // -------------------------------------------------------------------------         
    
    public String generateReportPreview( String reportName, SortedMap<String, String> reportElements, Collection<ChartElement> chartElements )
        throws GeneratorException
    {
        try
        {
            JasperPrint jasperPrint = getJasperPrint( reportName, reportElements, chartElements );
            
            StringBuffer buffer = new StringBuffer();
            
            // -----------------------------------------------------------------
            // Preview
            // -----------------------------------------------------------------   
                        
            JRHtmlExporter previewExporter = new JRHtmlExporter();
            previewExporter.setParameter( JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE );
            previewExporter.setParameter( JRHtmlExporterParameter.OUTPUT_STRING_BUFFER, buffer );
            previewExporter.setParameter( JRHtmlExporterParameter.JASPER_PRINT, jasperPrint );
            previewExporter.exportReport();
            
            return stripJasperHtmlBuffer( buffer );
        }
        catch ( JRException ex )
        {
            ex.printStackTrace();           
            
            throw new GeneratorException( "Failed to generate report preview", ex );
        }
    }
    
    public void generateReportFile( String reportName, SortedMap<String, String> reportElements, Collection<ChartElement> chartElements )
        throws GeneratorException
    {
        try
        {
            JasperPrint jasperPrint = getJasperPrint( reportName, reportElements, chartElements );
                 
            // -----------------------------------------------------------------------
            // PDF export to file
            // -----------------------------------------------------------------------            
            
            File file = locationManager.getFileForWriting( reportName + FileUtils.PDF_EXT, FileUtils.PDF_DIR );
            
            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setParameter( JRPdfExporterParameter.OUTPUT_FILE, file );
            pdfExporter.setParameter( JRPdfExporterParameter.JASPER_PRINT, jasperPrint );
            pdfExporter.exportReport();
        }
        catch ( LocationManagerException ex )
        {
            throw new GeneratorException( "Failed to get file for writing", ex );
        }
        catch ( JRException ex )
        {
            ex.printStackTrace();           
            
            throw new GeneratorException( "Failed to generate PDF report file", ex );
        }
    }
    
    public InputStream generateReportStream( String reportName, SortedMap<String, String> reportElements, Collection<ChartElement> chartElements )
        throws GeneratorException
    {
        try
        {
            JasperPrint jasperPrint = getJasperPrint( reportName, reportElements, chartElements );
            
            // -----------------------------------------------------------------------
            // PDF export to stream
            // -----------------------------------------------------------------------            
                        
            byte[] report = JasperExportManager.exportReportToPdf( jasperPrint );
            
            InputStream stream = new ByteArrayInputStream( report );
                                  
            return stream;
        }
        catch ( JRException ex )
        {
            ex.printStackTrace();           
            
            throw new GeneratorException( "Failed to generate report stream", ex );
        }
    }       

    // -----------------------------------------------------------------------
    // Supportive methods
    // -----------------------------------------------------------------------            
    
    /**
     * Generates a JasperPrint.
     * @param reportName Name of the report
     * @param reportElements The elements to include in the report
     * @param chartElements The elements to include in the chart
     * @return A JasperPrint containing the elements given in the parameters
     * @throws GeneratorException
     */
    private JasperPrint getJasperPrint( String reportName, SortedMap<String, String> reportElements, Collection<ChartElement> chartElements )
        throws GeneratorException
    {
        ServletContext context = ServletActionContext.getServletContext();
        
        System.setProperty( "jasper.reports.compile.class.path", 
            context.getRealPath( "/WEB-INF/lib" ) +
            System.getProperty( "path.separator" ) + 
            context.getRealPath( "/WEB-INF/classes" ) ); 
        
        JRDataSource dataSource = null;
        
        if ( chartElements == null || chartElements.size() == 0 )
        {
            dataSource = new JREmptyDataSource();
        }
        else
        {
            dataSource = new JRBeanCollectionDataSource( chartElements );
        }
        
        try
        {
            File file = locationManager.getFileForReading( FileUtils.getJRXMLFileName( reportName ), FileUtils.JRXML_DIR );
            
            // -----------------------------------------------------------------------
            // Load, compile and fill report
            // -----------------------------------------------------------------------         
            
            JasperDesign jasperDesign = JRXmlLoader.load( file );
            JasperReport jasperReport = JasperCompileManager.compileReport( jasperDesign );
            JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, reportElements, dataSource );
            
            return jasperPrint;
        }
        catch ( LocationManagerException ex )
        {
            throw new GeneratorException( "Failed to get file for reading", ex );
        }
        catch ( JRException ex )
        {
            ex.printStackTrace();           
            
            throw new GeneratorException( "Failed to generate jasper print", ex );
        }
    }

    /**
     * Strips the html buffer and leaves only the content inside the body tag. This makes it possible
     * to display and use the buffer inside another html page.
     * @param buffer The buffer to strip
     * @return Stripped html string
     * @throws GeneratorException
     */
    private String stripJasperHtmlBuffer( StringBuffer buffer )
        throws GeneratorException
    {
        try
        {
            StringReader reader = new StringReader( buffer.toString() );
            BufferedReader bufferedReader = new BufferedReader( reader );
            
            Pattern startPattern = Pattern.compile( "<body" );
            Pattern endPattern = Pattern.compile( "</body>" );
            Pattern imgPattern = Pattern.compile( "<img src=" );
            
            StringBuffer content = new StringBuffer();
            String line = null;
            boolean read = false;
            
            while ( ( line = bufferedReader.readLine() ) != null )
            {
                Matcher startMatcher = startPattern.matcher( line );
                Matcher endMatcher = endPattern.matcher( line );
                Matcher imgMatcher = imgPattern.matcher( line );
                
                if ( startMatcher.find() )
                {
                    read = true;
                    line = bufferedReader.readLine();
                }
                else if ( endMatcher.find() )
                {
                    break;
                }
                
                if ( read && !imgMatcher.find() )
                {
                    content.append( line + "\n" );
                }
            }
            
            return content.toString();
        }
        catch ( IOException ex )
        {
            throw new GeneratorException( "Failed to strip buffer", ex );
        }
    }
}
