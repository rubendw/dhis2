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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.reporting.dataset.utils.FileUtils;

import com.opensymphony.webwork.ServletActionContext;

/**
 * @author Abyout Asalefew
 * @version $Id$
 */
public class JasperTabularDesignGenerator
    implements TabularDesignGenerator
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
    // TabularDesignGenerator implementation
    // -------------------------------------------------------------------------

    public void generateDesign( String reportName, Collection<String> headerParameters, 
        Collection<String> columnFields, Collection<DataElementCategory> orderedCategories, 
            Integer numberOfColumns, Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap )
                throws GeneratorException
    {   
    	StringBuffer content = new StringBuffer();        
        ServletContext context = ServletActionContext.getServletContext();
        
        try
        {
            // -----------------------------------------------------------------------
            // Gets the report template
            // -----------------------------------------------------------------------

            InputStreamReader streamReader = new InputStreamReader( context.getResourceAsStream( "/dhis-web-reporting/template/tabular_template.xml" ) );
            BufferedReader bufferedReader = new BufferedReader( streamReader );
            
            Pattern pattern1 = Pattern.compile( "name=\"template\"" );            
            Pattern pattern2 = Pattern.compile( "<parameter name=\"Period\" isForPrompting=\"false\" class=\"java.lang.String\"/>" );            
            Pattern pattern3 = Pattern.compile( "<field name=\"tabularRow\" class=\"java.lang.String\"/>" );
            Pattern pattern4 = Pattern.compile( "<pageHeader>" );
            Pattern pattern5 = Pattern.compile( "</columnHeader>" );            
            
            String line = null;
            
            while ( ( line = bufferedReader.readLine() ) != null )
            {
                Matcher matcher1 = pattern1.matcher( line );
                Matcher matcher2 = pattern2.matcher( line );
                Matcher matcher3 = pattern3.matcher( line );
                Matcher matcher4 = pattern4.matcher( line );
                Matcher matcher5 = pattern5.matcher( line );
                
                if ( matcher1.find() )
                {
                    line = "\t\t name=\"" + reportName + "\"";
                }
                else if ( matcher2.find() )
                {
                	line += "\n" + putHeaderParameters( headerParameters );                    
                }
                
                else if( matcher3.find() )
                {
                	line += "\n" + putColumnFields( columnFields );
                }
                else if ( matcher4.find() )
                {
                	line += "\n" + bufferedReader.readLine();
                	
                    line += "\n" + getDesignTemplateTabularHeader( orderedCategories, numberOfColumns, orderedOptionsMap );
                }              
                
                else if ( matcher5.find() )
                {
                	line += "\n" + bufferedReader.readLine();
                	line += "\n" + bufferedReader.readLine();
                	
                    line += "\n" + getDesignTemplateTabularCells( columnFields );
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
    
    // -----------------------------------------------------------------------
    // Report code creator methods
    // -----------------------------------------------------------------------
    
    /**
     * @param reportElements reportElements
     */
    private String putHeaderParameters( Collection<String> reportElements )
    {
        StringBuffer buffer = new StringBuffer();
        
        Iterator<String> iterator = reportElements.iterator();
        
        while( iterator.hasNext() )
        {
            String elementName = iterator.next().toString();
            
            elementName = filterSpecialCharacters( elementName );
            
            buffer.append( "\t<parameter name=\"" + elementName +  "\" isForPrompting=\"false\" class=\"java.lang.String\"/>\n" );         
            
        }
        
        return buffer.toString();    
    }    
    
    private String putColumnFields( Collection<String> reportElements )
    {
        StringBuffer buffer = new StringBuffer();
        
        Iterator<String> iterator = reportElements.iterator();
        
        while( iterator.hasNext() )
        {
            String elementName = iterator.next().toString();
            
            elementName = filterSpecialCharacters( elementName );
            
            buffer.append( "\t<field name=\"" + elementName +  "\" class=\"java.lang.String\"/>\n" );          
            
        }
        
        return buffer.toString();    
    }
    
    /**
     * @param x: Horisontal position of field
     * @param y: Vertical position of field
     * @param width: Chart width
     * @param height: Chart height
     */   
    private String getDesignTemplateTabularHeader( Collection<DataElementCategory> orderedCategories, Integer numberOfColumns, Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap )
    {
        StringBuffer buffer = new StringBuffer();           
        
        int smallCellWidth = 700 / numberOfColumns;      
        
        int cellHeight = 30;       
        
        int y = 229 - ( 30 * ( orderedCategories.size() - 1) );     
        
        boolean border = true;
        
        int catColSpan = numberOfColumns;
        
        for( DataElementCategory cat : orderedCategories )
        {
        	int x = 130;
        	
        	catColSpan = catColSpan / cat.getCategoryOptions().size();       	
        	
        	int thisCellWidth  = smallCellWidth * catColSpan;
        	
        	Collection<DataElementCategoryOption> options = orderedOptionsMap.get(cat.getId());
			
			for( DataElementCategoryOption option : options )
			{				
				buffer.append( putTableHeader( x, y, thisCellWidth, cellHeight, option.getName(), border ) );				
				x += thisCellWidth;				
			}
			
			y += cellHeight;
        }
        
        return buffer.toString();
    }
    
    private String getDesignTemplateTabularCells( Collection<String> textFieldNames )
    {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = textFieldNames.iterator();    
        
        int cellWidth = 700 / textFieldNames.size();
        
        int cellHeight = 16;
        
        int x = 130;
        
        int count = 1;
        
        boolean border = false;
        while( iterator.hasNext() )
        {
        	if( count == textFieldNames.size() )
        	{
        		border = true;        		
        	}
        		
        	buffer.append( putTableCell( x, 0, cellWidth, cellHeight, String.valueOf( iterator.next() ), border ) );        	
            
            if ( x >= 842 ) { break; }
            
            x += cellWidth;
            
            count++;
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
    private String putTableHeader( int x, int y, int width, int height, String id, boolean border )
    {
        StringBuffer buffer = new StringBuffer();
        
        String rBorder = border ? "1Point" : "None";            
       		
        buffer.append( "\t\t\t\t<textField isStretchWithOverflow=\"false\" pattern=\"\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n" );
        buffer.append( "\t\t\t\t\t<reportElement\n" );
        buffer.append( "\t\t\t\t\t\tmode=\"Opaque\"\n" );
        buffer.append( "\t\t\t\t\t\tx=\"" + x + "\"\n" );
        buffer.append( "\t\t\t\t\t\ty=\"" + y + "\"\n" );
        buffer.append( "\t\t\t\t\t\twidth=\"" + width + "\"\n" );
        buffer.append( "\t\t\t\t\t\theight=\"" + height + "\"\n" );
        buffer.append( "\t\t\t\t\t\tbackcolor=\"#666666\"\n" );
        buffer.append( "\t\t\t\t\t\tkey=\"textField\"\n" );
        buffer.append( "\t\t\t\t\t\tstretchType=\"NoStretch\"\n" );
        buffer.append( "\t\t\t\t\t\tpositionType=\"FixRelativeToTop\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintRepeatedValues=\"true\"\n" );
        buffer.append( "\t\t\t\t\t\tisRemoveLineWhenBlank=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintInFirstWholeBand=\"false\"\n" );
        buffer.append( "\t\t\t\t\t\tisPrintWhenDetailOverflows=\"false\"/>\n" );
        buffer.append( "\t\t\t\t\t<box topBorder=\"1Point\" topBorderColor=\"#000000\" leftBorder=\"1Point\" leftBorderColor=\"#000000\" rightBorder=\"" + rBorder + "\" rightBorderColor=\"#000000\" bottomBorder=\"1Point\" bottomBorderColor=\"#000000\"/>\n" );
        buffer.append( "\t\t\t\t\t<textElement textAlignment=\"Center\">\n" );
        buffer.append( "\t\t\t\t\t\t<font fontName=\"" + FONT_JASPER + "\" pdfFontName=\"" + FONT_PDF + "\" size=\"10\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"true\" pdfEncoding =\"" + PDF_ENCODING + "\" isStrikeThrough=\"false\" />\n" );
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
    private String putTableCell( int x, int y, int width, int height, String name, boolean border )
    {    	    	
        StringBuffer buffer = new StringBuffer();
        
        String rBorder = "None";
        
        if ( border )
        	rBorder = "1Point";
        
        buffer.append( "\t\t\t\t<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n" );
        buffer.append( "\t\t\t\t\t<reportElement\n" );        
        buffer.append( "\t\t\t\t\t\tx=\"" + x + "\"\n" );
        buffer.append( "\t\t\t\t\t\ty=\"" + y + "\"\n" );
        buffer.append( "\t\t\t\t\t\twidth=\"" + width + "\"\n" );
        buffer.append( "\t\t\t\t\t\theight=\"" + height + "\"\n" );   
        buffer.append( "\t\t\t\t\t\tkey=\"textField\"\n" );        
        buffer.append( "\t\t\t\t\t\tstretchType=\"RelativeToBandHeight\">\n" );       
        buffer.append( "\t\t\t\t\t\t<printWhenExpression><![CDATA[new Boolean (!$F{" + name + "}.equalsIgnoreCase( \"null\" ))]]></printWhenExpression>\n" );
        buffer.append( "\t\t\t\t\t</reportElement>\n" );        
        buffer.append( "\t\t\t\t\t<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"Thin\" leftBorderColor=\"#000000\" rightBorder=\"" + rBorder + "\" rightBorderColor=\"#000000\" bottomBorder=\"Thin\" bottomBorderColor=\"#000000\"/>\n" );
        buffer.append( "\t\t\t\t\t<textElement textAlignment=\"Center\">\n" );
        buffer.append( "\t\t\t\t\t\t<font fontName=\"" + FONT_JASPER + "\" pdfFontName=\"" + FONT_PDF + "\" size=\"10\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"true\" pdfEncoding =\"" + PDF_ENCODING + "\" isStrikeThrough=\"false\" />\n" );
        buffer.append( "\t\t\t\t\t</textElement>\n" );        						
        buffer.append( "\t\t\t\t<textFieldExpression   class=\"java.lang.String\"><![CDATA[$F{" + name + "}]]></textFieldExpression>\n" );        
        buffer.append( "\t\t\t\t</textField>\n" );
        
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
