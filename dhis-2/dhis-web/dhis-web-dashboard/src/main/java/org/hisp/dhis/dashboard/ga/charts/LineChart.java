package org.hisp.dhis.dashboard.ga.charts;

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

import java.awt.BasicStroke;
import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;

public class LineChart
    implements StandardChart
{
    private DefaultCategoryDataset getDataset( double[][] data, String[] series, String[] categories )
    {
        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for ( int i = 0; i < series.length; i++ )
        {
            for ( int j = 0; j < categories.length; j++ )
            {
                dataset.addValue( data[i][j], series[i], categories[j] );
            }
        }
        return dataset;
    }

    public JFreeChart getChartViewer( double[][] data1, String[] series1, String[] categories1, double[][] data2,
        String[] series2, String[] categories2, String chartTitle, String xAxis_Title, String yAxis_Title )
    {
        DefaultCategoryDataset dataset1 = getDataset( data1, series1, categories1 );

        // create the first plot...
        final CategoryItemRenderer renderer = new LineAndShapeRenderer();        
        renderer.setToolTipGenerator( new StandardCategoryToolTipGenerator() );
        renderer.setItemLabelGenerator( new StandardCategoryItemLabelGenerator() );
        renderer.setItemLabelsVisible( true );
        

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset( dataset1 );
        plot.setRenderer( renderer );

        plot.setDomainAxis( new CategoryAxis( xAxis_Title ) );
        plot.setRangeAxis( new NumberAxis( yAxis_Title ) );

        plot.setOrientation( PlotOrientation.VERTICAL );
        plot.setRangeGridlinesVisible( true );
        plot.setDomainGridlinesVisible( true );
        

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );
        rangeAxis.setAutoRangeIncludesZero( true );
        rangeAxis.setUpperMargin( 0.15 );
        rangeAxis.setLowerMargin( 0.15 );

        final CategoryDataset dataset2 = getDataset( data2, series2, categories2 );

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setSeriesStroke( 0, new BasicStroke( 2.0f ) );
        renderer2.setSeriesStroke( 1, new BasicStroke( 2.0f ) );
        plot.setDataset( 1, dataset2 );
        plot.setRenderer( 1, renderer2 );
        plot.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );

        final JFreeChart chart = new JFreeChart( plot );

        /* TITLE */
        final TextTitle mainTitle = new TextTitle( "Line Chart" );
        mainTitle.setFont( new Font( "times", Font.BOLD, 13 ) );
        mainTitle.setPosition( RectangleEdge.TOP );
        mainTitle.setVerticalAlignment( VerticalAlignment.BOTTOM );
        chart.addSubtitle( mainTitle );

        /* SUB TITLE */
        final TextTitle subtitle = new TextTitle( chartTitle );
        subtitle.setFont( new Font( "times", Font.BOLD, 13 ) );
        subtitle.setPosition( RectangleEdge.TOP );
        subtitle.setVerticalAlignment( VerticalAlignment.BOTTOM );
        chart.addSubtitle( subtitle );

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions( CategoryLabelPositions.UP_45 );

        return chart;
    }// getChartViewer end

}
