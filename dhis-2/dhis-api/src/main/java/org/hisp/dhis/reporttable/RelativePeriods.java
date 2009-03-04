package org.hisp.dhis.reporttable;

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

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class RelativePeriods
    implements Serializable
{
    public static final String REPORTING_MONTH = "reporting_month";
    
    public static final String LAST_3_MONTHS = "last3_months";
    public static final String LAST_6_MONTHS = "last6_months";
    public static final String LAST_9_MONTHS = "last9_months";
    public static final String LAST_12_MONTHS = "last12_months";
    public static final String SO_FAR_THIS_YEAR = "so_far_this_year";
    public static final String SO_FAR_THIS_FINANCIAL_YEAR = "so_far_this_financial_year";
    
    public static final String LAST_3_TO_6_MONTHS = "last3_6_months";
    public static final String LAST_6_TO_9_MONTHS = "last6_9_months";
    public static final String LAST_9_TO_12_MONTHS = "last9_12_months";
    
    public static final String[] PREVIOUS_MONTH_NAMES = { 
        "previous_month_1",
        "previous_month_2",
        "previous_month_3",
        "previous_month_4",
        "previous_month_5",
        "previous_month_6",
        "previous_month_7",
        "previous_month_8",
        "previous_month_9",
        "previous_month_10",
        "previous_month_11",
        "previous_month_12" };
    
    public static final String[] MONTHS_THIS_YEAR = {
        "january",
        "february",
        "march",
        "april",
        "may",
        "june",
        "july",
        "august",
        "september",
        "october",
        "november",
        "december" };
    
    public static final String[] QUARTERS_THIS_YEAR = {
        "quarter1",
        "quarter2",
        "quarter3",
        "quarter4" };
    
    private Boolean reportingMonth;
    
    private Boolean last3Months;
    
    private Boolean last6Months;
    
    private Boolean last9Months;
    
    private Boolean last12Months;
    
    private Boolean soFarThisYear;
    
    private Boolean soFarThisFinancialYear;
    
    private Boolean last3To6Months;
    
    private Boolean last6To9Months;
    
    private Boolean last9To12Months;
    
    private Boolean last12IndividualMonths;
    
    private Boolean individualMonthsThisYear;
    
    private Boolean individualQuartersThisYear;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public RelativePeriods()
    {   
    }

    public RelativePeriods( boolean reportingMonth, boolean last3Months,
        boolean last6Months, boolean last9Months, boolean last12Months, boolean soFarThisYear, boolean soFarThisFinancialYear,
        boolean last3To6Months, boolean last6To9Months, boolean last9To12Months,
        boolean last12IndividualMonths, boolean individualMonthsThisYear, boolean individualQuartersThisYear )
    {
        this.reportingMonth = reportingMonth;
        this.last3Months = last3Months;
        this.last6Months = last6Months;
        this.last9Months = last9Months;
        this.last12Months = last12Months;
        this.soFarThisYear = soFarThisYear;
        this.soFarThisFinancialYear = soFarThisFinancialYear;
        this.last3To6Months = last3To6Months;
        this.last6To9Months = last6To9Months;
        this.last9To12Months = last9To12Months;
        this.last12IndividualMonths = last12IndividualMonths;
        this.individualMonthsThisYear = individualMonthsThisYear;
        this.individualQuartersThisYear = individualQuartersThisYear;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean isReportingMonth()
    {
        return reportingMonth != null && reportingMonth;
    }
    
    public boolean isLast3Months()
    {
        return last3Months != null && last3Months;
    }
    
    public boolean isLast6Months()
    {
        return last6Months != null && last6Months;
    }
    
    public boolean isLast9Months()
    {
        return last9Months != null && last9Months;
    }
    
    public boolean isLast12Months()
    {
        return last12Months != null && last12Months;
    }
    
    public boolean isSoFarThisYear()
    {
        return soFarThisYear != null && soFarThisYear;
    }
    
    public boolean isSoFarThisFinancialYear()
    {
        return soFarThisFinancialYear != null && soFarThisFinancialYear;
    }
    
    public boolean isLast3To6Months()
    {
        return last3To6Months != null && last3To6Months;
    }
    
    public boolean isLast6To9Months()
    {
        return last6To9Months != null && last6To9Months;
    }
    
    public boolean isLast9To12Months()
    {
        return last9To12Months != null && last9To12Months;
    }
    
    public boolean isLast12IndividualMonths()
    {
        return last12IndividualMonths != null && last12IndividualMonths;
    }
    
    public boolean isIndividualMonthsThisYear()
    {
        return individualMonthsThisYear != null && individualMonthsThisYear;
    }
    
    public boolean isIndividualQuartersThisYear()
    {
        return individualQuartersThisYear != null && individualQuartersThisYear;
    }
        
    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    public Boolean getReportingMonth()
    {
        return reportingMonth;
    }

    public void setReportingMonth( Boolean reportingMonth )
    {
        this.reportingMonth = reportingMonth;
    }
    
    public Boolean getLast3Months()
    {
        return last3Months;
    }

    public void setLast3Months( Boolean last3Months )
    {
        this.last3Months = last3Months;
    }

    public Boolean getLast6Months()
    {
        return last6Months;
    }

    public void setLast6Months( Boolean last6Months )
    {
        this.last6Months = last6Months;
    }

    public Boolean getLast9Months()
    {
        return last9Months;
    }

    public void setLast9Months( Boolean last9Months )
    {
        this.last9Months = last9Months;
    }

    public Boolean getLast12Months()
    {
        return last12Months;
    }

    public void setLast12Months( Boolean last12Months )
    {
        this.last12Months = last12Months;
    }

    public Boolean getSoFarThisYear()
    {
        return soFarThisYear;
    }

    public void setSoFarThisYear( Boolean soFarThisYear )
    {
        this.soFarThisYear = soFarThisYear;
    }

    public Boolean getSoFarThisFinancialYear()
    {
        return soFarThisFinancialYear;
    }

    public void setSoFarThisFinancialYear( Boolean soFarThisFinancialYear )
    {
        this.soFarThisFinancialYear = soFarThisFinancialYear;
    }

    public Boolean getLast3To6Months()
    {
        return last3To6Months;
    }

    public void setLast3To6Months( Boolean last3To6Months )
    {
        this.last3To6Months = last3To6Months;
    }

    public Boolean getLast6To9Months()
    {
        return last6To9Months;
    }

    public void setLast6To9Months( Boolean last6To9Months )
    {
        this.last6To9Months = last6To9Months;
    }

    public Boolean getLast9To12Months()
    {
        return last9To12Months;
    }

    public void setLast9To12Months( Boolean last9To12Months )
    {
        this.last9To12Months = last9To12Months;
    }

    public Boolean getLast12IndividualMonths()
    {
        return last12IndividualMonths;
    }

    public void setLast12IndividualMonths( Boolean last12IndividualMonths )
    {
        this.last12IndividualMonths = last12IndividualMonths;
    }

    public Boolean getIndividualMonthsThisYear()
    {
        return individualMonthsThisYear;
    }

    public void setIndividualMonthsThisYear( Boolean individualMonthsThisYear )
    {
        this.individualMonthsThisYear = individualMonthsThisYear;
    }

    public Boolean getIndividualQuartersThisYear()
    {
        return individualQuartersThisYear;
    }

    public void setIndividualQuartersThisYear( Boolean individualQuartersThisYear )
    {
        this.individualQuartersThisYear = individualQuartersThisYear;
    }

    // -------------------------------------------------------------------------
    // Equals, hashCode, and toString
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        String toString = "[Reporting month: " + reportingMonth +
            ", Last 3 months: " + last3Months +
            ", Last 6 months: " + last6Months +
            ", Last 9 months: " + last9Months +
            ", Last 12 months: " + last12Months +
            ", So far this year: " + soFarThisYear + 
            ", So far this financial year: " + soFarThisFinancialYear +
            ", Last 3 to 6 months: " + last3To6Months +
            ", Last 6 to 9 months: " + last6To9Months +
            ", Last 9 to 12 months: " + last9To12Months + 
            ", Last 12 Individual months: " + last12IndividualMonths + 
            ", Individual months this year: " + individualMonthsThisYear +
            ", Individual quarters this year: " + individualQuartersThisYear + "]";
        
        return toString;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = 1;
        
        result = prime * result + ( ( last12Months == null ) ? 0 : last12Months.hashCode() );
        result = prime * result + ( ( last3Months == null ) ? 0 : last3Months.hashCode() );
        result = prime * result + ( ( last3To6Months == null ) ? 0 : last3To6Months.hashCode() );
        result = prime * result + ( ( last6Months == null ) ? 0 : last6Months.hashCode() );
        result = prime * result + ( ( last6To9Months == null ) ? 0 : last6To9Months.hashCode() );
        result = prime * result + ( ( last9Months == null ) ? 0 : last9Months.hashCode() );
        result = prime * result + ( ( last9To12Months == null ) ? 0 : last9To12Months.hashCode() );
        result = prime * result + ( ( reportingMonth == null ) ? 0 : reportingMonth.hashCode() );
        result = prime * result + ( ( soFarThisYear == null ) ? 0 : soFarThisYear.hashCode() );
        result = prime * result + ( ( soFarThisFinancialYear == null ) ? 0 : soFarThisFinancialYear.hashCode() );
        result = prime * result + ( ( last12IndividualMonths == null ) ? 0 : last12IndividualMonths.hashCode() );
        result = prime * result + ( ( individualMonthsThisYear == null ) ? 0 : individualMonthsThisYear.hashCode() );
        result = prime * result + ( ( individualQuartersThisYear == null ) ? 0 : individualQuartersThisYear.hashCode() );
        
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        final RelativePeriods other = (RelativePeriods) object;
        
        if ( last12Months == null )
        {
            if ( other.last12Months != null )
            {
                return false;
            }
        }
        else if ( !last12Months.equals( other.last12Months ) )
        {
            return false;
        }
        
        if ( last3Months == null )
        {
            if ( other.last3Months != null )
            {
                return false;
            }
        }
        else if ( !last3Months.equals( other.last3Months ) )
        {
            return false;
        }
        
        if ( last3To6Months == null )
        {
            if ( other.last3To6Months != null )
            {
                return false;
            }
        }
        else if ( !last3To6Months.equals( other.last3To6Months ) )
        {
            return false;
        }
        
        if ( last6Months == null )
        {
            if ( other.last6Months != null )
            {
                return false;
            }
        }
        else if ( !last6Months.equals( other.last6Months ) )
        {
            return false;
        }
        
        if ( last6To9Months == null )
        {
            if ( other.last6To9Months != null )
            {
                return false;
            }
        }
        else if ( !last6To9Months.equals( other.last6To9Months ) )
        {
            return false;
        }
        
        if ( last9Months == null )
        {
            if ( other.last9Months != null )
            {
                return false;
            }
        }
        else if ( !last9Months.equals( other.last9Months ) )
        {
            return false;
        }
        
        if ( last9To12Months == null )
        {
            if ( other.last9To12Months != null )
            {
                return false;
            }
        }
        else if ( !last9To12Months.equals( other.last9To12Months ) )
        {
            return false;
        }
        
        if ( reportingMonth == null )
        {
            if ( other.reportingMonth != null )
            {
                return false;
            }
        }
        else if ( !reportingMonth.equals( other.reportingMonth ) )
        {
            return false;
        }
        
        if ( soFarThisYear == null )
        {
            if ( other.soFarThisYear != null )
            {
                return false;
            }
        }
        else if ( !soFarThisYear.equals( other.soFarThisYear ) )
        {
            return false;
        }

        if ( soFarThisFinancialYear == null )
        {
            if ( other.soFarThisFinancialYear != null )
            {
                return false;
            }
        }
        else if ( !soFarThisFinancialYear.equals( other.soFarThisFinancialYear ) )
        {
            return false;
        }
        
        if ( last12IndividualMonths == null )
        {
            if ( other.last12IndividualMonths != null )
            {
                return false;
            }
        }
        else if ( !last12IndividualMonths.equals( other.last12IndividualMonths ) )
        {
            return false;
        }

        if ( individualMonthsThisYear == null )
        {
            if ( other.individualMonthsThisYear != null )
            {
                return false;
            }
        }
        else if ( !individualMonthsThisYear.equals( other.individualMonthsThisYear ) )
        {
            return false;
        }

        if ( individualQuartersThisYear == null )
        {
            if ( other.individualQuartersThisYear != null )
            {
                return false;
            }
        }
        else if ( !individualQuartersThisYear.equals( other.individualQuartersThisYear ) )
        {
            return false;
        }
        
        return true;
    }
}
