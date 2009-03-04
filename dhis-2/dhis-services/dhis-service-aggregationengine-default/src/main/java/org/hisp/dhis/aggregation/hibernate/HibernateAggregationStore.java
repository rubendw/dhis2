package org.hisp.dhis.aggregation.hibernate;

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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.hisp.dhis.aggregation.AggregationStore;
import org.hisp.dhis.aggregation.batch.statement.StatementHolder;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: HibernateAggregationStore.java 4934 2008-04-17 15:49:50Z larshelg $
 */
public class HibernateAggregationStore
    implements AggregationStore
{
    //TODO rename to jdbc
    
    // ----------------------------------------------------------------------
    // Dependencies
    // ----------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    // ----------------------------------------------------------------------
    // DataValue
    // ----------------------------------------------------------------------
    
    public Collection<DataValue> getDataValues( Collection<Integer> sourceIds, int dataElementId, int optionComboId, Collection<Integer> periodIds )
    {
        if ( sourceIds != null && sourceIds.size() > 0 && periodIds != null && periodIds.size() > 0 )
        {
            StatementHolder holder = statementManager.getHolder();
        	
            try
            {
                String sql = 
                    "SELECT periodid, value " +
                    "FROM datavalue " +
                    "WHERE dataelementid = " + dataElementId + " " +
                    "AND categoryoptioncomboid = " + optionComboId + " " +
                    "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                    "AND sourceid IN ( " + getCommaDelimitedString( sourceIds ) + " )";                 
                
                ResultSet resultSet = holder.getStatement().executeQuery( sql );
                
                return getDataValues( resultSet );             
            }
            catch ( Exception ex )
            {
                throw new RuntimeException( "Failed to get DataValues", ex );
            }
            finally
            {
                holder.close();            	
            }
        }
        
        return new ArrayList<DataValue>();
    }
    
    public Collection<DataValue> getDataValues( int sourceId, int dataElementId, int optionComboId, Collection<Integer> periodIds )
    {
        if ( periodIds != null && periodIds.size() > 0 )
        {
            StatementHolder holder = statementManager.getHolder();
            
            try
            {
                String sql = 
                    "SELECT periodid, value " +
                    "FROM datavalue " +
                    "WHERE dataelementid = " + dataElementId + " " +
                    "AND categoryoptioncomboid = " + optionComboId + " " +
                    "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                    "AND sourceid = " + sourceId;

                ResultSet resultSet = holder.getStatement().executeQuery( sql );
                
                return getDataValues( resultSet );
            }
            catch ( SQLException ex )
            {
                throw new RuntimeException( "Failed to get DataValues", ex );
            }
            finally
            {
                holder.close();
            }
        }
        
        return new ArrayList<DataValue>();
    }
    
    public Collection<String> getDataValueIdentifiers()
    {
        int min = 0;
        
        final int limit = 10000;
        
        Collection<String> identifiers = new HashSet<String>();
        
        Collection<String> temp = null;
        
        while ( ( temp = getDataValueIdentifiers( min, limit ) ).size() > 0 )
        {
            identifiers.addAll( temp );
            
            min += limit;
        }
        
        return identifiers;
    }
    
    private Collection<String> getDataValueIdentifiers( int min, int limit )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = 
                "SELECT dataelementid, periodid, sourceid " +
                "FROM datavalue " +
                "ORDER BY dataelementid, periodid, sourceid " +
                "LIMIT " + min + ", " + limit;
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            Collection<String> identifiers = new HashSet<String>();
            
            while ( resultSet.next() )
            {
                identifiers.add( resultSet.getInt( 1 ) + "-" + resultSet.getInt( 2 ) + "-" + resultSet.getInt( 3 ) );
            }
            
            return identifiers;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get DataValue identifiers", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    // ----------------------------------------------------------------------
    // Supportive methods
    // ----------------------------------------------------------------------

    private Collection<DataValue> getDataValues( ResultSet resultSet )
    {
        try
        {
            Collection<DataValue> list = new ArrayList<DataValue>();
            
            while ( resultSet.next() )
            {
                Period period = new Period();
                
                period.setId( Integer.parseInt( resultSet.getString( 1 ) ) );
                
                DataValue dataValue = new DataValue();
                
                dataValue.setPeriod( period );
                dataValue.setValue( resultSet.getString( 2 ) );
                
                list.add( dataValue );
            }
            
            return list;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to transform resultset into collection", ex );
        }
    }   
    
    private String getCommaDelimitedString( Collection<Integer> elements )
    {
        if ( elements != null && elements.size() > 0 )
        {
            StringBuffer sourceSqlBuffer = new StringBuffer();        
        
            for ( Integer element : elements )
            {
                sourceSqlBuffer.append( element.toString() + ", " );
            }
            
            return sourceSqlBuffer.substring( 0, sourceSqlBuffer.length() - ", ".length() );
        }
        
        return null;
    }
}
