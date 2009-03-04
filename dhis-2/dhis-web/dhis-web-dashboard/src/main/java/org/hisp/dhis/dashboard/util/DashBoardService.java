package org.hisp.dhis.dashboard.util;

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

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;

public class DashBoardService
{
    /* Dependencies */
    private PeriodStore periodStore;
    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }
    
    
    public List<Period> getMonthlyPeriods(Date start, Date end)
    {
        List<Period> periodList = new ArrayList<Period>(periodStore.getPeriodsBetweenDates( start, end ));
        PeriodType monthlyPeriodType = getPeriodTypeObject("monthly");
        
        List<Period> monthlyPeriodList = new ArrayList<Period>();
        Iterator it = periodList.iterator();
        while(it.hasNext())
        {
            Period period = (Period) it.next();
            if(period.getPeriodType().getId() == monthlyPeriodType.getId())
            {
                monthlyPeriodList.add( period );
            }
        }
        return monthlyPeriodList;
    }
    
    /*
    public List<Period> getMonthlyPeriods(Date start, Date end) 
    {
        PeriodType monthlyPeriodType = getPeriodTypeObject("monthly");
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(end);
        boolean januaryIsFirst = false;
        if (cal.get(Calendar.MONTH) == Calendar.JANUARY) 
        {
            januaryIsFirst = true;
        }
        List<Period> periods = new ArrayList<Period>();
        while (cal.get(Calendar.MONTH) != cal1.get(Calendar.MONTH)) {
                Period period = getPeriodByMonth(cal.get(Calendar.MONTH), cal
                                .get(Calendar.YEAR), monthlyPeriodType);
                if (period != null) {
                        periods.add(period);
                }
                cal.roll(Calendar.MONTH, true);
                if (!januaryIsFirst && cal.get(Calendar.MONTH) == 0) {
                        cal.roll(Calendar.YEAR, true);
                }
        }
        periods.add(getPeriodByMonth(cal1.get(Calendar.MONTH), cal1
                        .get(Calendar.YEAR), monthlyPeriodType));
        return periods;
    }
    */

    /*
     * Returns the Period Object of the given date
     * For ex:- if the month is 3, year is 2006 and periodType Object of type Monthly then
     * it returns the corresponding Period Object
     */
    public Period getPeriodByMonth( int month, int year, PeriodType periodType )
    {
        int monthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        Calendar cal = Calendar.getInstance();
        cal.set( year, month, 1, 0, 0, 0 );
        Date firstDay = new Date( cal.getTimeInMillis() );

        if ( periodType.getName().equalsIgnoreCase( "Monthly" ) )
        {
            cal.set( year, month, 1, 0, 0, 0 );
            if ( year % 4 == 0 && month == 1)
            {
                cal.set( Calendar.DAY_OF_MONTH, monthDays[month] + 1 );
            }
            else
            {
                cal.set( Calendar.DAY_OF_MONTH, monthDays[month] );
            }
        }
        else if ( periodType.getName().equalsIgnoreCase( "Yearly" ) )
        {
            cal.set( year, Calendar.DECEMBER, 31 );
        }

        Date lastDay = new Date( cal.getTimeInMillis() );

        Period newPeriod = periodStore.getPeriod( firstDay, lastDay, periodType );

        return newPeriod;
    }
    
    
    /*
     * Returns the PeriodType Object based on the Period Type Name
     * For ex:- if we pass name as Monthly then it returns the PeriodType Object 
     * for Monthly PeriodType
     * If there is no such PeriodType returns null
     */
    public PeriodType getPeriodTypeObject(String periodTypeName)
    {        
        Collection periodTypes = periodStore.getAllPeriodTypes();
        PeriodType periodType = null;
        Iterator iter = periodTypes.iterator();
        while ( iter.hasNext() )
        {
            PeriodType tempPeriodType = (PeriodType) iter.next();
            if ( tempPeriodType.getName().toLowerCase().trim().equals( periodTypeName ) )
            {
                periodType = tempPeriodType;
                break;
            }
        }
        if ( periodType == null )
        {
            System.out.println( "No Such PeriodType" );
            return null;
        }        
        return periodType;
    }

    /*
     * Returns the child tree of the selected Orgunit
     */
    public List<OrganisationUnit> getAllChildren(OrganisationUnit selecteOU) 
    {
        List<OrganisationUnit> ouList = new ArrayList<OrganisationUnit>();
        
        Iterator it = selecteOU.getChildren().iterator();
        while (it.hasNext()) 
        {
        	OrganisationUnit orgU = (OrganisationUnit) it.next();
            ouList.add(orgU);
        }
        return ouList;
    }

    
    
    public String createDataTable(String orgUnitInfo, String deInfo, String periodInfo)
    {
        Connection con = (new DBConnection()).openConnection();

        Statement st1 = null;
        Statement st2 = null;
        
        String dataTableName = "data" + UUID.randomUUID().toString();
        dataTableName = dataTableName.replaceAll( "-", "" );
                
        String query = "DROP TABLE IF EXISTS "+dataTableName;
        
        try
        {
            st1 = con.createStatement();
            st2 = con.createStatement();
            
            st1.executeUpdate( query );
                        
            System.out.println("Table "+dataTableName+" dropped Successfully (if exists) ");
            
            query = "CREATE table "+ dataTableName +" AS " +
                    " SELECT sourceid,dataelementid,periodid,value FROM datavalue " + 
                    " WHERE dataelementid in (" + deInfo + ") AND " + 
                    " sourceid in (" + orgUnitInfo + ") AND " +
                    " periodid in (" + periodInfo + ")";
                    
                    
            st2.executeUpdate( query );
            
            System.out.println("Table "+dataTableName+" created Successfully");
        } // try block end
        catch ( Exception e )
        {
            System.out.println("SQL Exception : "+e.getMessage());     
            return null;
        }
        finally
        {
            try
            {
                if(st1 != null) st1.close();
                if(st2 != null) st2.close();
                if(con != null) con.close();
            }
            catch( Exception e )
            {
                System.out.println("SQL Exception : "+e.getMessage());
                return null;
            }
        }// finally block end

        return dataTableName;                
    }
    

    public String createDataTableForComments(String orgUnitInfo, String deInfo, String periodInfo)
    {
        Connection con = (new DBConnection()).openConnection();

        Statement st1 = null;
        Statement st2 = null;
        
        String dataTableName = "data" + UUID.randomUUID().toString();
        dataTableName = dataTableName.replaceAll( "-", "" );
                
        String query = "DROP TABLE IF EXISTS "+dataTableName;
        
        try
        {
            st1 = con.createStatement();
            st2 = con.createStatement();
            
            st1.executeUpdate( query );
            
            System.out.println("TABLE NAME : "+dataTableName);
            System.out.println("Table dropped Successfully (if exists) ");
            
            query = "CREATE table "+ dataTableName +" AS " +
                    " SELECT sourceid,dataelementid,periodid,value,comment FROM datavalue " + 
                    " WHERE dataelementid in (" + deInfo + ") AND " + 
                    " sourceid in (" + orgUnitInfo + ") AND " +
                    " periodid in (" + periodInfo + ") AND "+
                    " commnet IS NOT NULL";                    
                    
            st2.executeUpdate( query );
            
            System.out.println("Table created Successfully");
        } // try block end
        catch ( Exception e )
        {
            System.out.println("SQL Exception : "+e.getMessage());     
            return null;
        }
        finally
        {
            try
            {
                if(st1 != null) st1.close();
                if(st2 != null) st2.close();
                if(con != null) con.close();
            }
            catch( Exception e )
            {
                System.out.println("SQL Exception : "+e.getMessage());
                return null;
            }
        }// finally block end

        return dataTableName;                
    }

    public void deleteDataTable(String dataTableName)
    {
        Connection con = (new DBConnection()).openConnection();

        Statement st1 = null;        
                       
        String query = "DROP TABLE IF EXISTS "+dataTableName;
        
        try
        {
            st1 = con.createStatement();            
            st1.executeUpdate( query );
            System.out.println("Table "+dataTableName+" dropped Successfully");
        } // try block end
        catch ( Exception e )
        {
            System.out.println("SQL Exception : "+e.getMessage());                        
        }
        finally
        {
            try
            {                
                if(st1 != null) st1.close();                
                if(con != null) con.close();
            }
            catch( Exception e )
            {
                System.out.println("SQL Exception : "+e.getMessage());                
            }
        }// finally block end        
    }

    
    
    
} // class end
