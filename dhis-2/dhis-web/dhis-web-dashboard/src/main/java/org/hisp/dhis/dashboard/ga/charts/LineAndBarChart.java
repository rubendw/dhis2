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

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;

public class LineAndBarChart
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

        final CategoryDataset dataset1 = getDataset( data1, series1, categories1 );
        final NumberAxis rangeAxis1 = new NumberAxis( yAxis_Title );
        rangeAxis1.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );
        rangeAxis1.setUpperMargin( 0.15 );
        rangeAxis1.setLowerMargin( 0.15 );

        final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setBaseToolTipGenerator( new StandardCategoryToolTipGenerator() );
        renderer1.setItemLabelGenerator( new StandardCategoryItemLabelGenerator() );
        renderer1.setItemLabelsVisible( true );
        final CategoryPlot subplot1 = new CategoryPlot( dataset1, null, rangeAxis1, renderer1 );
        subplot1.setDomainGridlinesVisible( true );

        final CategoryDataset dataset2 = getDataset( data1, series1, categories1 );
        final NumberAxis rangeAxis2 = new NumberAxis( yAxis_Title );
        rangeAxis2.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );
        rangeAxis2.setUpperMargin( 0.30 );
        rangeAxis1.setLowerMargin( 0.30 );

        final BarRenderer renderer2 = new BarRenderer();
        renderer2.setBaseToolTipGenerator( new StandardCategoryToolTipGenerator() );
        renderer2.setItemLabelGenerator( new StandardCategoryItemLabelGenerator() );
        renderer2.setItemLabelsVisible( true );
        final CategoryPlot subplot2 = new CategoryPlot( dataset2, null, rangeAxis2, renderer2 );
        subplot2.setDomainGridlinesVisible( true );

        final CategoryAxis domainAxis = new CategoryAxis( xAxis_Title );
        domainAxis.setCategoryLabelPositions( CategoryLabelPositions.UP_45 );

        final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot( domainAxis );
        plot.add( subplot1, 2 );
        plot.add( subplot2, 1 );

        final JFreeChart chart = new JFreeChart( "", new Font( "SansSerif", Font.BOLD, 12 ), plot, true );

        /* TITLE */
        final TextTitle mainTitle = new TextTitle( "Combined (Line and Bar) Chart" );
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

        return chart;
    }// getChartViewer end

}
