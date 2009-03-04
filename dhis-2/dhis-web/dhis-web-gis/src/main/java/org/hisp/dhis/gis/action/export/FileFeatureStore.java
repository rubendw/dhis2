package org.hisp.dhis.gis.action.export;

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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.hisp.dhis.gis.Legend;

import org.hisp.dhis.gis.action.configuration.*;

import org.hisp.dhis.gis.ext.Title;
import org.hisp.dhis.gis.state.SelectionManager;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class FileFeatureStore
{

    private GISConfigurationManagerService gisConfigurationManagerService;

    private SelectionManager selectionManager;

    public void setSelectionManager( SelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }


    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }


    // -------------------------------------------------------------------------
    // Target file
    // -------------------------------------------------------------------------

    private static final String TEMP_SVG_FILE = "temp.svg";

    private static final String LEGEND_FILE = "legend.svg";
   

    public boolean createSVGTempFile( String mapFileName )
    {

        StringBuffer buffer = new StringBuffer();

        Map<String, String> value = selectionManager.getSeleteBagSession().getFeature();

        ViewBox viewBox = new ViewBox();

        Scanner scanner = null;
        try
        {
            File svgMapFile = new File( gisConfigurationManagerService.getGISMapDirectory(), mapFileName );

            scanner = new Scanner( svgMapFile );
        }
        catch ( FileNotFoundException ex )
        {
            throw new NoSuchElementException( "Can not open this file" );
        }

        while ( scanner.hasNext() )
        {
            String line = (String) scanner.nextLine();
            // String lineCopy = new String(line);
            if ( line.contains( "viewBox" ) )
            {
                if ( line.trim().trim().startsWith( "viewBox" ) )
                {
                    viewBox = new ViewBox( line.trim() );
                    viewBox.changeViewBox();
                    line = viewBox.printViewBox();
                }
                else
                {

                }

            }

            if ( line.trim().startsWith( "<polygon" ) || line.trim().startsWith( "<text" ) )
            {
                String[] array = line.split( " " );
                String id = "";
                String newColor = "";
                String color = "";
                String newText = "";
                for ( int i = 0; i < array.length; i++ )
                {

                    if ( array[i].startsWith( "id" ) )
                    {
                        id = array[i].substring( 4, array[i].length() - 1 );

                        String color_value = value.get( id );

                        if ( color_value == null )
                        {
                            color_value = "#CCCCCC-0.0";
                        }
                        String[] a_color_value = color_value.split( "-" );
                        newColor = a_color_value[0];

                        newColor = "fill=\"" + newColor + "\"";

                        newText = ":" + a_color_value[1] + "</text>";

                        if ( line.contains( "</text>" ) )
                        {
                            line = line.replace( "</text>", newText );
                            // System.out.println(line);
                        }
                    }

                    if ( line.contains( "fill" ) )
                    {
                        if ( !line.contains( "</text>" ) )
                        {
                            if ( array[i].trim().startsWith( "fill" ) )
                            {
                                color = array[i];
                                line = line.replace( color, newColor );
                                break;
                            }
                        }
                    }
                    else
                    {
                        line = line.replace( "/>", newColor + "/>" );
                    }

                }
                if ( !line.trim().startsWith( "</svg>" ) )
                {
                    buffer.append( line + "\n" );
                }

            }
            else
            {
                if ( !line.trim().startsWith( "</svg>" ) )
                {
                    buffer.append( line + "\n" );
                }
            }

        }
        Title title = selectionManager.getSeleteBagSession().getTitle();

        buffer.append( "<g id='title'>" );
        buffer.append( "\n" );
        buffer.append( "<text x='0' y='0' fill='black' font-size='0.25'>Indicator:" + title.getName() + "</text>" );
        buffer.append( "\n" );
        buffer.append( "<text x='0' y='0.3' fill='black' font-size='0.25'>Period:" + title.getStartDate() + " to "
            + title.getEndDate() + "</text>" );
        buffer.append( "\n" );
        buffer.append( "</g>" );
        buffer.append( "\n" );

        buffer.append( "<g id='legend'>" );
        buffer.append( "\n" );
        buffer.append( "<text x='" + (viewBox.width - 1.5) + "' y='0.1' fill='black' font-size='0.25'>Legends</text>" );

        double x = viewBox.width - 1.5;
        double y = 0.3;
        for ( Legend legend : selectionManager.getSeleteBagSession().getLegends() )
        {
            buffer.append( "\n" );
            buffer.append( "<rect x='" + x + "' y='" + y + "' height='0.3' width='0.3' fill='" + legend.getColor()
                + "' stroke='#000000' stroke-width='0.01'/>" );
            buffer.append( "\n" );
            buffer.append( "<text x='" + (x + 0.4) + "' y='" + (y + 0.2) + "' fill='black' font-size='0.2'>"
                + legend.getMin() + " - " + legend.getMax() + "</text>" );
            y += 0.4;
        }
        buffer.append( "\n" );

        buffer.append( "</g>" );

        buffer.append( "</svg>" );

        try
        {
            OutputStream outputStream = new FileOutputStream( new File( gisConfigurationManagerService.getGISTempDirectory(),
                this.TEMP_SVG_FILE ) );
            DataOutputStream s = new DataOutputStream( outputStream );
            s.writeBytes( buffer.toString() );
            outputStream.close();
            scanner.close();

            return true;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }

    }

    public boolean createSVGTempFileNoLegend( String mapFileName )
    {

        StringBuffer buffer = new StringBuffer();

        Map<String, String> value = selectionManager.getSeleteBagSession().getFeature();

        Scanner scanner = null;
        try
        {
            File svgMapFile = new File( gisConfigurationManagerService.getGISMapDirectory(), mapFileName );

            scanner = new Scanner( svgMapFile );
        }
        catch ( FileNotFoundException ex )
        {
            throw new NoSuchElementException( "Can not open this file" );
        }

        while ( scanner.hasNext() )
        {
            String line = (String) scanner.nextLine();

            if ( line.trim().startsWith( "<polygon" ) || line.trim().startsWith( "<text" ) )
            {
                String[] array = line.split( " " );
                String id = "";
                String newColor = "";
                String color = "";
                String newText = "";
                for ( int i = 0; i < array.length; i++ )
                {

                    if ( array[i].startsWith( "id" ) )
                    {
                        id = array[i].substring( 4, array[i].length() - 1 );

                        String color_value = value.get( id );

                        if ( color_value == null )
                        {
                            color_value = "#CCCCCC-0.0";
                        }
                        String[] a_color_value = color_value.split( "-" );
                        newColor = a_color_value[0];

                        newColor = "fill=\"" + newColor + "\"";

                        newText = ":" + a_color_value[1] + "</text>";

                        if ( line.contains( "</text>" ) )
                        {
                            line = line.replace( "</text>", newText );
                            // System.out.println(line);
                        }
                    }

                    if ( line.contains( "fill" ) )
                    {
                        if ( !line.contains( "</text>" ) )
                        {
                            if ( array[i].trim().startsWith( "fill" ) )
                            {
                                color = array[i];
                                line = line.replace( color, newColor );
                                break;
                            }
                        }
                    }
                    else
                    {
                        line = line.replace( "/>", newColor + "/>" );
                    }

                }
                buffer.append( line + "\n" );

            }
            else
            {

                buffer.append( line + "\n" );

            }

        }
        scanner.close();

        try
        {
            OutputStream outputStream = new FileOutputStream( new File( gisConfigurationManagerService.getGISTempDirectory(),
                this.TEMP_SVG_FILE ) );
            DataOutputStream s = new DataOutputStream( outputStream );
            s.writeBytes( buffer.toString() );
            outputStream.close();
            return true;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }

    }

    public boolean createLegends( List<Legend> legends )
    {

        StringBuffer buffer = new StringBuffer( "<?xml version='1.0' encoding='UTF-8'?>" );

        buffer.append( "\n" );

        buffer
            .append( "<svg width='100%' height='100%' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' " );

        buffer.append( "xmlns:attrib='http://www.carto.net/attrib/' viewBox='0 0 1 " + legends.size() + "'>" );

        buffer.append( "\n" );

        int i = 0;

        for ( Legend legend : legends )
        {

            buffer.append( "<rect x='0' y='" + i + "' height='1' width='1' fill='" + legend.getColor()
                + "' stroke='#000000' stroke-width='0.01'/>" );

            buffer.append( "\n" );

            i++;

        }
        buffer.append( "\n" );

        buffer.append( "</svg>" );

        try
        {
            OutputStream outputStream = new FileOutputStream( new File( gisConfigurationManagerService.getGISTempDirectory(),
                this.LEGEND_FILE ) );
            DataOutputStream s = new DataOutputStream( outputStream );
            s.writeBytes( buffer.toString() );
            s.close();
            return true;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }

    }

    class ViewBox
    {
        private double x;

        private double y;

        private double width;

        private double height;

        public ViewBox( double x, double y, double width, double height )
        {
            super();
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public String printViewBox()
        {
            return "viewBox=" + "\"" + x + " " + y + " " + width + " " + height + "\"";
        }

        public void changeViewBox()
        {
            this.x = x;
            this.y = y - 0.5;
            this.width = width + 1;
            this.height = this.height + 1;

        }

        public ViewBox( String viewBox )
        {
            viewBox = viewBox.replace( "viewBox=", "" );
            viewBox = viewBox.replace( "\"", "" );
            String[] array = viewBox.split( " " );
            this.x = new Double( array[0] ).doubleValue();
            this.y = new Double( array[1] ).doubleValue();
            this.width = new Double( array[2] ).doubleValue();
            this.height = new Double( array[3] ).doubleValue();
        }

        public ViewBox()
        {
            // TODO Auto-generated constructor stub
        }
    }

}
