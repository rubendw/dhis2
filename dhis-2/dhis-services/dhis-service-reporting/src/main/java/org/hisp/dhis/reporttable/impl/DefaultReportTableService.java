package org.hisp.dhis.reporttable.impl;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.RelativePeriodType;
import org.hisp.dhis.reporttable.RelativePeriods;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.reporttable.ReportTableStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultReportTableService
    implements ReportTableService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportTableStore reportTableStore;
    
    public void setReportTableStore( ReportTableStore reportTableStore )
    {
        this.reportTableStore = reportTableStore;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // ReportTableService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public List<Period> getRelativePeriods( RelativePeriods relatives, int months )
    {
        List<Period> relativePeriods = new ArrayList<Period>();
        
        Date date = getDateFromPreviousMonth( months );
        
        if ( relatives.isReportingMonth() )
        {
            Period period = periodService.getRelativePeriod( date, -1 );
            period.setName( RelativePeriods.REPORTING_MONTH );
            relativePeriods.add( period );
        }
        if ( relatives.isLast3Months() )
        {
            Period period = periodService.getRelativePeriod( date, -3 );
            period.setName( RelativePeriods.LAST_3_MONTHS );
            relativePeriods.add( period );            
        }
        if ( relatives.isLast6Months() )
        {
            Period period = periodService.getRelativePeriod( date, -6 );
            period.setName( RelativePeriods.LAST_6_MONTHS );
            relativePeriods.add( period );
        }
        if ( relatives.isLast9Months() )
        {
            Period period = periodService.getRelativePeriod( date, -9 );
            period.setName( RelativePeriods.LAST_9_MONTHS );
            relativePeriods.add( period );
        }
        if ( relatives.isLast12Months() )
        {
            Period period = periodService.getRelativePeriod( date, -12 );
            period.setName( RelativePeriods.LAST_12_MONTHS );
            relativePeriods.add( period );
        }
        if ( relatives.isSoFarThisYear() )
        {
            MonthlyPeriodType periodType = new MonthlyPeriodType();            
            Period period = new Period();
            
            period.setPeriodType( new RelativePeriodType() );
            period.setStartDate( getStartDateOfYear( date ) );            
            period.setEndDate( periodType.createPeriod( date ).getEndDate() );
            
            period = savePeriod( period );
            period.setName( RelativePeriods.SO_FAR_THIS_YEAR );
            relativePeriods.add( period );
        }
        if ( relatives.isSoFarThisFinancialYear() )
        {
            MonthlyPeriodType periodType = new MonthlyPeriodType();            
            Period period = new Period();
            
            period.setPeriodType( new RelativePeriodType() );
            period.setStartDate( getStartDateOfFinancialYear( date ) );            
            period.setEndDate( periodType.createPeriod( date ).getEndDate() );
            
            period = savePeriod( period );
            period.setName( RelativePeriods.SO_FAR_THIS_FINANCIAL_YEAR );
            relativePeriods.add( period );
        }
        if ( relatives.isLast3To6Months() )
        {
            Period period = periodService.getRelativePeriod( date, -6, -3 );
            period.setName( RelativePeriods.LAST_3_TO_6_MONTHS );
            relativePeriods.add( period );
        }
        if ( relatives.isLast6To9Months() )
        {
            Period period = periodService.getRelativePeriod( date, -9, -6 );
            period.setName( RelativePeriods.LAST_6_TO_9_MONTHS );
            relativePeriods.add( period );
        }
        if ( relatives.isLast9To12Months() )
        {
            Period period = periodService.getRelativePeriod( date, -12, -9 );
            period.setName( RelativePeriods.LAST_9_TO_12_MONTHS );
            relativePeriods.add( period );
        }
        if ( relatives.isLast12IndividualMonths() )
        {
            for ( int i = 0; i < 12; i++ )
            {
                int periodNumber = i - 12;
                
                Period period = periodService.getRelativePeriod( date, periodNumber, periodNumber + 1 );
                period.setName( RelativePeriods.PREVIOUS_MONTH_NAMES[i] );
                relativePeriods.add( period );
            }
        }
        if ( relatives.isIndividualMonthsThisYear() )
        {
            MonthlyPeriodType periodType = new MonthlyPeriodType();
            
            Period period = new Period();
            period.setStartDate( date );
            
            List<Period> periods = periodType.generatePeriods( period );
            
            for ( int i = 0; i < 12; i++ )
            {
                Period month = periods.get( i );
                month.setPeriodType( new RelativePeriodType() );
                month = savePeriod( month );
                month.setName( RelativePeriods.MONTHS_THIS_YEAR[i] );                
                relativePeriods.add( month );
            }            
        }
        if ( relatives.isIndividualQuartersThisYear() )
        {
            QuarterlyPeriodType periodType = new QuarterlyPeriodType();
            
            Period period = new Period();
            period.setStartDate( date );
            
            List<Period> periods = periodType.generatePeriods( period );
            
            for ( int i = 0; i < 4; i++ )
            {
                Period quarter = periods.get( i );
                quarter.setPeriodType( new RelativePeriodType() );
                quarter = savePeriod( quarter );
                quarter.setName( RelativePeriods.QUARTERS_THIS_YEAR[i] );
                relativePeriods.add( quarter );
            }
        }
        
        return relativePeriods;
    }

    public Date getDateFromPreviousMonth( int months )
    {
        Calendar cal = PeriodType.createCalendarInstance();
        
        cal.add( Calendar.MONTH, months * -1 );
        
        return cal.getTime();
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    public int saveReportTable( ReportTable reportTable )
    {
        return reportTableStore.saveReportTable( reportTable );
    }
    
    public void saveOrUpdateReportTable( ReportTable reportTable )
    {
        reportTableStore.saveOrUpdateReportTable( reportTable );
    }
    
    public void deleteReportTable( ReportTable reportTable )
    {
        reportTableStore.deleteReportTable( reportTable );
    }
    
    public ReportTable getReportTable( int id )
    {
        return reportTableStore.getReportTable( id );
    }
    
    public Collection<ReportTable> getAllReportTables()
    {
        return reportTableStore.getAllReportTables();
    }
    
    public Collection<ReportTable> getReportTables( Collection<Integer> reportTables )
    {
        Collection<ReportTable> objects = new HashSet<ReportTable>();
        
        for ( Integer id : reportTables )
        {
            objects.add( getReportTable( id ) );
        }
        
        return objects;
    }
    
    public ReportTable getReportTableByName( String name )
    {
        return reportTableStore.getReportTableByName( name );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Date getStartDateOfFinancialYear( Date date )
    {
        Calendar cal = PeriodType.createCalendarInstance( date );

        cal.set( Calendar.MONTH, 3 );
        cal.set( Calendar.DAY_OF_MONTH, 1 );

        if ( date.before( cal.getTime() ) )
        {
            cal.add( Calendar.YEAR, -1 );
        }
        
        return cal.getTime();
        
        //TODO Create system setting for start month, this can be different from place to place
    }
    
    private Date getStartDateOfYear( Date date )
    {
        Calendar cal = PeriodType.createCalendarInstance( date );
        
        cal.set( Calendar.MONTH, 0 );
        cal.set( Calendar.DAY_OF_MONTH, 1 );
        
        return cal.getTime();
    }
    
    private Period savePeriod( Period period )
    {
        Period persistedPeriod = periodService.getPeriod( period.getStartDate(), period.getEndDate(), period.getPeriodType() );
        
        if ( persistedPeriod == null )
        {
            periodService.addPeriod( period );
        }
        else
        {
            period = persistedPeriod;
        }
        
        return period;
    }
}
