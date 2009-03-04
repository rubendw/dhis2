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

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;

public class DualAxisChart
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

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart( "", // chart
            // title
            "Category", // domain axis label
            "Value", // range axis label
            dataset1, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips?
            false // URL generator? Not required...
            );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        /* TITLE */
        final TextTitle mainTitle = new TextTitle( "Dual Axis Chart" );
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

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        // plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation( AxisLocation.BOTTOM_OR_RIGHT );

        // customise the range axis...
        final NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
        rangeAxis1.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );
        rangeAxis1.setAutoRangeIncludesZero( true );
        rangeAxis1.setUpperMargin( 0.15 );
        rangeAxis1.setLowerMargin( 0.15 );
        rangeAxis1.setLabel( yAxis_Title );

        final CategoryDataset dataset2 = getDataset( data1, series1, categories1 );
        plot.setDataset( 1, dataset2 );
        plot.mapDatasetToRangeAxis( 1, 1 );
        // customise the range axis...

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions( CategoryLabelPositions.DOWN_45 );
        domainAxis.setLabel( xAxis_Title );

        // final ValueAxis axis2 = new NumberAxis("Secondary");

        final NumberAxis rangeAxis2 = (NumberAxis) plot.getRangeAxis();
        rangeAxis2.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );
        rangeAxis2.setAutoRangeIncludesZero( true );
        rangeAxis2.setUpperMargin( 0.30 );
        rangeAxis2.setLowerMargin( 0.15 );
        rangeAxis2.setLabel( yAxis_Title );
        plot.setRangeAxis( 1, rangeAxis2 );

        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setToolTipGenerator( new StandardCategoryToolTipGenerator() );
        renderer2.setItemLabelGenerator( new StandardCategoryItemLabelGenerator() );
        renderer2.setItemLabelsVisible( true );

        plot.setRenderer( 1, renderer2 );
        plot.setDatasetRenderingOrder( DatasetRenderingOrder.REVERSE );
        // OPTIONAL CUSTOMISATION COMPLETED.
      
        return chart;
    }// getChartViewer end

}
