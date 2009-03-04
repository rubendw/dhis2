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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;

import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.reporting.dataset.utils.FileUtils;
import com.opensymphony.webwork.ServletActionContext;

/**
 * @author Nguyen Dang Quang
 * @author Lars Helge Overland
 * @version $Id: DesignGenerator.java 4456 2008-01-28 13:46:13Z larshelg $
 */
public class JasperDesignGenerator
    implements DesignGenerator
{
    private static final String FONT_JASPER = "Arial";
    private static final String FONT_PDF = "arial.ttf";
    private static final String PDF_ENCODING ="Cp1251";
    private static final String CHARSET = "UTF-8";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocationManager locationManager;
        
    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }

    // -------------------------------------------------------------------------
    // DesignGenerator implementation
    // -------------------------------------------------------------------------

    public void generateDesign( String reportName, Collection<String> textFieldNames, 
        Collection<String> staticTextNames, int designTemplateNumber, int chartTemplateNumber )
            throws GeneratorException
    {	
        StringBuffer content = new StringBuffer();        
        ServletContext context = ServletActionContext.getServletContext();
        
        try
        {
            // -----------------------------------------------------------------
            // Gets the report template
            // -----------------------------------------------------------------

            InputStreamReader streamReader = new InputStreamReader( context.getResourceAsStream( "/dhis-web-reporting/template/template.xml" ) );
            BufferedReader bufferedReader = new BufferedReader( streamReader );
            
            Pattern pattern1 = Pattern.compile( "name=\"template\"" );
            Pattern pattern2 = Pattern.compile( "<import value=\"net.sf.jasperreports.engine.data.*\" />" );
            Pattern pattern3 = Pattern.compile( "<pageHeader>" );
            Pattern pattern4 = Pattern.compile( "<summary>" );
            String line = null;
            
            while ( ( line = bufferedReader.readLine() ) != null )
            {
                Matcher matcher1 = pattern1.matcher( line );
                Matcher matcher2 = pattern2.matcher( line );
                Matcher matcher3 = pattern3.matcher( line );
                Matcher matcher4 = pattern4.matcher( line );
                
                if ( matcher1.find() )
                {
                    line = "\t\t name=\"" + reportName + "\"";
                }
                else if ( matcher2.find() )
                {
                    line += "\n" + getParameters( textFieldNames );
                }
                else if ( designTemplateNumber == 1 && matcher3.find() )
                {
                    line += "\n" + bufferedReader.readLine();
                    line += "\n" + getDesignTemplate1( textFieldNames, staticTextNames, FONT_JASPER, FONT_PDF );
                }
                else if ( designTemplateNumber == 2 && matcher3.find() )
                {
                    line += "\n" + bufferedReader.readLine();
                    line += "\n" + getDesignTemplate2( textFieldNames, staticTextNames, FONT_JASPER, FONT_PDF );
                }
                else if ( chartTemplateNumber == 1 && matcher4.find() )
                {
                    line += "\n" + bufferedReader.readLine();
                    line += "\n" + getChart1();
                }
                else if ( chartTemplateNumber == 2 && matcher4.find() )
                {
                    line += "\n" + bufferedReader.readLine();
                    line += "\n" + getChart2();
                }
                
                content.append( line + "\n" );
            }
            
            bufferedReader.close();
            streamReader.close();
        }          
        catch ( IOException ex )
        {   
            throw new GeneratorException( "Failed to read templatefile", ex );
        }    
        
        writeFile( content.toString(), reportName );
    }
    
    // -------------------------------------------------------------------------
    // Report code creator methods
    // -------------------------------------------------------------------------
    
    /**
     * @param reportElements reportElements
     */
    private String getParameters( Collection<String> reportElements )
    {
        StringBuffer buffer = new StringBuffer();
        
        Iterator<String> iterator = reportElements.iterator();
        
        while( iterator.hasNext() )
        {
            String elementName = iterator.next().toString();
            
            elementName = filterSpecialCharacters( elementName );
            
            buffer.append( "\t<parameter name=\"" + elementName +  "\" isForPrompting=\"false\" class=\"java.lang.String\">\n" );
            buffer.append( "\t\t<parameterDescription><![CDATA[" + elementName +  "]]></parameterDescription>\n" );
            buffer.append( "\t</parameter>\n" );
        }
        
        return buffer.toString();    
    }
    
    /**
     * @param x Horisontal position of field
     * @param y Vertical position of field
     * @param width Field width
     * @param height Field height
     * @param id Field id
     * @param font Text font
     * @param pdfFont Text font for PDF file
     */
    private String getTextField( int x, int y, int width, int height, String id, String font, String pdfFont )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "\t\t\t\t<textField isStretchWithOverflow=\"false\" pattern=\"\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n" );
        buffer.append( "\t\t\t\t\t<reportElement\n" );
        buffer.append( "\t\t\t\t\t\tmode=\"Transparent\"\n" );
        buffer.append( "\t\t\t\t\t\tx=\"" + x + "\"\n" );
        buffer.append( "\t\t\t\t\t\ty=\"" + y + "\"\n" );
        buffer.append( "\t\t\t\t\t\twidth=\"" + width + "\"\n" );
        buffer.append( "\t\t\t\t\t\theight=\"" + height + "\"\n" );
        buffer.append( "\t\t\t\t\t\tforecolor=\"#000000\"\n" );
        buffer.append( "\t\t\t\t\t\tbackcolor=\"#FFFFFF\"\n" );
        buffer.append( "\t\t\t\t\t\tkey=\"textField\"\n" );
        buffer.append( "\t\t\t\t\t\tstretchType=\"NoStretch\"\n" );
        buffer.append( "\t\t\t\t\t\tpositionType=\"FixRelativeToTop\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintRepeatedValues=\"true\"\n" );
        buffer.append( "\t\t\t\t\t\tisRemoveLineWhenBlank=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintInFirstWholeBand=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintWhenDetailOverflows=\"false\"/>\n" );
        buffer.append( "\t\t\t\t\t<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n" );
        buffer.append( "\t\t\t\t\t<textElement textAlignment=\"Right\" verticalAlignment=\"Top\" rotation=\"None\" lineSpacing=\"Single\">\n" );
        buffer.append( "\t\t\t\t\t\t<font fontName=\"" + font + "\" pdfFontName=\"" + pdfFont + "\" size=\"10\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"true\" pdfEncoding =\"" + PDF_ENCODING + "\" isStrikeThrough=\"false\" />\n" );
        buffer.append( "\t\t\t\t\t</textElement>\n" );
        buffer.append( "\t\t\t\t<textFieldExpression   class=\"java.lang.String\"><![CDATA[$P{" + id + "}]]></textFieldExpression>\n" );
        buffer.append( "\t\t\t\t</textField>\n" );
        
        return buffer.toString();
    }
    
    /**
     * @param x: Horisontal position of field
     * @param y: Vertical position of field
     * @param width: Field width
     * @param height: Field height
     * @param name: Field name
     * @param font: Text font
     * @param pdfFont: Text font for PDF file
     */
    private String getStaticText( int x, int y, int width, int height, String name, String font, String pdfFont )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "\t\t\t\t<staticText>\n" );
        buffer.append( "\t\t\t\t\t<reportElement\n" );
        buffer.append( "\t\t\t\t\t\tmode=\"Transparent\"\n" );
        buffer.append( "\t\t\t\t\t\tx=\"" + x + "\"\n" );
        buffer.append( "\t\t\t\t\t\ty=\"" + y + "\"\n" );
        buffer.append( "\t\t\t\t\t\twidth=\"" + width + "\"\n" );
        buffer.append( "\t\t\t\t\t\theight=\"" + height + "\"\n" );
        buffer.append( "\t\t\t\t\t\tforecolor=\"#000000\"\n" );
        buffer.append( "\t\t\t\t\t\tbackcolor=\"#FFFFFF\"\n" );
        buffer.append( "\t\t\t\t\t\tkey=\"staticText-4\"\n" );
        buffer.append( "\t\t\t\t\t\tstretchType=\"NoStretch\"\n" );
        buffer.append( "\t\t\t\t\t\tpositionType=\"FixRelativeToTop\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintRepeatedValues=\"true\"\n" );
        buffer.append( "\t\t\t\t\t\tisRemoveLineWhenBlank=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintInFirstWholeBand=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintWhenDetailOverflows=\"false\"/>\n" );
        buffer.append( "\t\t\t\t\t<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n" );
        buffer.append( "\t\t\t\t\t<textElement textAlignment=\"Left\" verticalAlignment=\"Top\" rotation=\"None\" lineSpacing=\"Single\">\n" );
        buffer.append( "\t\t\t\t\t\t<font fontName=\"" + font + "\" pdfFontName=\"" + pdfFont + "\" size=\"10\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"true\" pdfEncoding =\"" + PDF_ENCODING + "\" isStrikeThrough=\"false\" />\n" );
        buffer.append( "\t\t\t\t\t</textElement>\n" );
        buffer.append( "\t\t\t\t<text><![CDATA[" + name + "]]></text>\n" );
        buffer.append( "\t\t\t\t</staticText>\n" );
        
        return buffer.toString();
    }

    /**
     * @param x: Horisontal position of field
     * @param y: Vertical position of field
     * @param width: Chart width
     * @param height: Chart height
     */
    private String get3DPieChart( int x, int y, int width, int height )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "\t\t\t\t<pie3DChart>\n" );
        buffer.append( "\t\t\t\t\t<chart isShowLegend=\"false\"  hyperlinkTarget=\"Self\" >\n" );
        buffer.append( "\t\t\t\t\t<reportElement\n" );
        buffer.append( "\t\t\t\t\t\tmode=\"Transparent\"\n" );
        buffer.append( "\t\t\t\t\t\tx=\"" + x + "\"\n" );
        buffer.append( "\t\t\t\t\t\ty=\"" + y + "\"\n" );
        buffer.append( "\t\t\t\t\t\twidth=\"" + width + "\"\n" );
        buffer.append( "\t\t\t\t\t\theight=\"" + height + "\"\n" );
        buffer.append( "\t\t\t\t\t\tforecolor=\"#000000\"\n" );
        buffer.append( "\t\t\t\t\t\tbackcolor=\"#FFFFFF\"\n" );
        buffer.append( "\t\t\t\t\t\tkey=\"element-1\"\n" );
        buffer.append( "\t\t\t\t\t\tstretchType=\"NoStretch\"\n" );
        buffer.append( "\t\t\t\t\t\tpositionType=\"FixRelativeToTop\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintRepeatedValues=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisRemoveLineWhenBlank=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintInFirstWholeBand=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintWhenDetailOverflows=\"false\"/>\n" );
        buffer.append( "\t\t\t\t\t<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n" );
        buffer.append( "\t\t\t\t\t</chart>\n" );
        buffer.append( "\t\t\t\t\t<pieDataset>\n" );
        buffer.append( "\t\t\t\t\t\t<dataset />\n" );
        buffer.append( "\t\t\t\t\t\t<keyExpression><![CDATA[$F{name}]]></keyExpression>\n" );
        buffer.append( "\t\t\t\t\t\t<valueExpression><![CDATA[$F{value}]]></valueExpression>\n" );
        buffer.append( "\t\t\t\t\t\t<labelExpression><![CDATA[$F{name}]]></labelExpression>\n" );
        buffer.append( "\t\t\t\t\t</pieDataset>\n" );
        buffer.append( "\t\t\t\t\t<pie3DPlot >\n" );
        buffer.append( "\t\t\t\t\t\t<plot backgroundAlpha=\"0.7\" foregroundAlpha=\"0.7\" />\n" );
        buffer.append( "\t\t\t\t\t</pie3DPlot>\n" );
        buffer.append( "\t\t\t\t</pie3DChart>" );
        
        return buffer.toString();
    }
    
    /**
     * @param x: Horisontal position of field
     * @param y: Vertical position of field
     * @param width: Chart width
     * @param height: Chart height
     */
    private String get3DBarChart( int x, int y, int width, int height )
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( "\t\t\t\t<bar3DChart>\n" );
        buffer.append( "\t\t\t\t\t<chart isShowLegend=\"false\"  hyperlinkTarget=\"Self\" >\n" );
        buffer.append( "\t\t\t\t\t<reportElement\n" );
        buffer.append( "\t\t\t\t\t\tmode=\"Transparent\"\n" );
        buffer.append( "\t\t\t\t\t\tx=\"" + x + "\"\n" );
        buffer.append( "\t\t\t\t\t\ty=\"" + y + "\"\n" );
        buffer.append( "\t\t\t\t\t\twidth=\"" + width + "\"\n" );
        buffer.append( "\t\t\t\t\t\theight=\"" + height + "\"\n" );
        buffer.append( "\t\t\t\t\t\tforecolor=\"#000000\"\n" );
        buffer.append( "\t\t\t\t\t\tbackcolor=\"#FFFFFF\"\n" );
        buffer.append( "\t\t\t\t\t\tkey=\"element-1\"\n" );
        buffer.append( "\t\t\t\t\t\tstretchType=\"NoStretch\"\n" );
        buffer.append( "\t\t\t\t\t\tpositionType=\"FixRelativeToTop\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintRepeatedValues=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisRemoveLineWhenBlank=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintInFirstWholeBand=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintWhenDetailOverflows=\"false\"/>\n" );
        buffer.append( "\t\t\t\t\t<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>\n" );
        buffer.append( "\t\t\t\t\t</chart>\n" );
        buffer.append( "\t\t\t\t\t<categoryDataset>\n" );
        buffer.append( "\t\t\t\t\t\t<dataset />\n" );
        buffer.append( "\t\t\t\t\t\t<categorySeries>\n" );
        buffer.append( "\t\t\t\t\t\t\t<seriesExpression><![CDATA[$F{name}]]></seriesExpression>\n" );
        buffer.append( "\t\t\t\t\t\t\t<categoryExpression><![CDATA[$F{name}]]></categoryExpression>\n" );
        buffer.append( "\t\t\t\t\t\t\t<valueExpression><![CDATA[$F{value}]]></valueExpression>\n" );
        buffer.append( "\t\t\t\t\t\t</categorySeries>\n" );
        buffer.append( "\t\t\t\t\t</categoryDataset>\n" );
        buffer.append( "\t\t\t\t\t<bar3DPlot >\n" );
        buffer.append( "\t\t\t\t\t\t<plot />\n" );
        buffer.append( "\t\t\t\t\t</bar3DPlot>\n" );
        buffer.append( "\t\t\t\t</bar3DChart>" );
        
        return buffer.toString();
    }
    
    private String getChart1()
    {
        return get3DPieChart( 1, 1, 534, 140 );
    }
    
    private String getChart2()
    {
        return get3DBarChart( 1, 1, 534, 140 );
    }
    
    private String getDesignTemplate1( Collection<String> textFieldNames, Collection<String> staticTextNames, String font, String pdfFont )
    {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator1 = textFieldNames.iterator();
        Iterator<String> iterator2 = staticTextNames.iterator();
        
        int y = 1;
        final int fieldHeight = 18;
        final int lineHeight = 18;
        
        while( iterator1.hasNext() && iterator2.hasNext() )
        {
            buffer.append( getTextField( 340, y, 78, fieldHeight, String.valueOf( iterator1.next() ), font, pdfFont ) );
            buffer.append( getStaticText( 8, y, 321, fieldHeight, String.valueOf( iterator2.next() ), font, pdfFont ) );
            
            if ( y >= 500 ) { break; }
            
            y += lineHeight;            
        }
        
        return buffer.toString();
    }
    
    private String getDesignTemplate2( Collection<String> textFieldNames, Collection<String> staticTextNames, String font, String pdfFont )
    {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator1 = textFieldNames.iterator();
        Iterator<String> iterator2 = staticTextNames.iterator();
        
        int y = 1;
        final int fieldHeight = 18;
        final int lineHeight = 18;
        
        while( iterator1.hasNext() && iterator2.hasNext() )
        {
            buffer.append( getTextField( 216, y, 44, fieldHeight, String.valueOf( iterator1.next() ), font, pdfFont ) );
            buffer.append( getStaticText( 8, y, 206, fieldHeight, String.valueOf( iterator2.next() ), font, pdfFont ) );
            
            if ( iterator1.hasNext() && iterator2.hasNext() )
            {
                buffer.append( getTextField( 482, y, 44, fieldHeight, String.valueOf( iterator1.next() ), font, pdfFont ) );
                buffer.append( getStaticText( 274, y, 206, fieldHeight, String.valueOf( iterator2.next() ), font, pdfFont ) );
            }
            
            if ( y >= 500 ) { break; }
            
            y += lineHeight;            
        }
        
        return buffer.toString();
    }

    private void writeFile( String content, String reportName )
        throws GeneratorException
    {   
        Writer out = null;
        
        try
        {
            File file = locationManager.getFileForWriting( FileUtils.getJRXMLFileName( reportName ), FileUtils.JRXML_DIR );
            
            out = new OutputStreamWriter( new FileOutputStream( file, false ), CHARSET );
            
            out.write( content );
        }
        catch ( LocationManagerException lex )
        {
            throw new GeneratorException( "Failed to get file for writing", lex );
        }
        catch ( Exception ex )
        {
            throw new GeneratorException( "Failed to write to file", ex );            
        }
        finally
        {
            if ( out != null )
            {
                try
                {
                    out.flush();
                }
                catch ( Exception ex )
                {   
                }
                try
                {
                    out.close();
                }
                catch ( Exception ex )
                {   
                }
            }
        }
    }

    private String filterSpecialCharacters( String elementName )
    {
        elementName = elementName.replaceAll( "&", "&amp;" );
	elementName = elementName.replaceAll( "<", "&lt;" );
	elementName = elementName.replaceAll( ">", "&gt;" );
        
	return elementName;
    }
}
