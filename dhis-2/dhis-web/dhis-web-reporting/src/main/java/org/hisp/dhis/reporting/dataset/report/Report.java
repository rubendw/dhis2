package org.hisp.dhis.reporting.dataset.report;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lars Helge Overland
 * @version $Id: Report.java 5138 2008-05-12 15:05:47Z larshelg $
 */
public class Report
    implements Serializable
{
    private List<Element> reportElements; 
    private List<Element> chartElements;
    private int designTemplate;
    private int chartTemplate;
    private int reportType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    public Report()
    {
        this.reportElements = new ArrayList<Element>();
        this.chartElements = new ArrayList<Element>();
        this.designTemplate = 2;
        this.chartTemplate = 0;
        this.reportType = 0;
    }
    
    public Report( int reportType )
    {
        this.reportElements = new ArrayList<Element>();
        this.chartElements = new ArrayList<Element>();
        this.designTemplate = 2;
        this.chartTemplate = 0;
        this.reportType = reportType;
    }
    
    public Report( List<Element> reportElements, List<Element> chartElements, 
        int designTemplate, int chartTemplate, int reportType )
    {
        this.reportElements = reportElements;
        this.chartElements = chartElements;
        this.designTemplate = designTemplate;
        this.chartTemplate = chartTemplate;
        this.reportType = reportType;
    }    

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------
    
    public List<Element> getChartElements()
    {
        return chartElements;
    }

    public void setChartElements( List<Element> chartElements )
    {
        this.chartElements = chartElements;
    }

    public List<Element> getReportElements()
    {
        return reportElements;
    }

    public void setReportElements( List<Element> reportElements )
    {
        this.reportElements = reportElements;
    }

    public int getChartTemplate()
    {
        return chartTemplate;
    }

    public void setChartTemplate( int chartTemplate )
    {
        this.chartTemplate = chartTemplate;
    }

    public int getDesignTemplate()
    {
        return designTemplate;
    }

    public void setDesignTemplate( int designTemplate )
    {
        this.designTemplate = designTemplate;
    }    

    public int getReportType()
    {
        return reportType;
    }

    public void setReportType( int reportType )
    {
        this.reportType = reportType;
    }

    // -------------------------------------------------------------------------
    // Equals & hashCode
    // -------------------------------------------------------------------------
    
    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + ( ( chartElements == null ) ? 0 : chartElements.hashCode() );
        result = PRIME * result + chartTemplate;
        result = PRIME * result + designTemplate;
        result = PRIME * result + ( ( reportElements == null ) ? 0 : reportElements.hashCode() );
        result = PRIME * result + reportType;
        
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        
        if ( obj == null )
        {
            return false;
        }
        
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        
        final Report other = (Report) obj;
        
        if ( chartElements == null )
        {
            if ( other.chartElements != null )
            {
                return false;
            }
        }
        else if ( !chartElements.equals( other.chartElements ) )
        {
            return false;
        }
        
        if ( chartTemplate != other.chartTemplate )
        {
            return false;
        }
        
        if ( designTemplate != other.designTemplate )
        {
            return false;
        }
        
        if ( reportElements == null )
        {
            if ( other.reportElements != null )
            {
                return false;
            }
        }
        else if ( !reportElements.equals( other.reportElements ) )
        {
            return false;
        }
        
        if ( reportType != other.reportType )
        {
            return false;
        }
        
        return true;
    }
}
