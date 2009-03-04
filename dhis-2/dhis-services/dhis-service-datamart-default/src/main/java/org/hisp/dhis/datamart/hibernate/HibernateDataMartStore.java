package org.hisp.dhis.datamart.hibernate;

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

import static org.hisp.dhis.system.util.TextUtils.getCommaDelimitedString;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hisp.dhis.aggregation.DataElementCategoryOptionComboName;
import org.hisp.dhis.aggregation.GroupSetStructure;
import org.hisp.dhis.aggregation.OrganisationUnitStructure;
import org.hisp.dhis.aggregation.batch.builder.StatementBuilder;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfigurationProvider;
import org.hisp.dhis.aggregation.batch.factory.StatementBuilderFactory;
import org.hisp.dhis.aggregation.batch.statement.JDBCStatementManager;
import org.hisp.dhis.aggregation.batch.statement.StatementHolder;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.CrossTabDataValue;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.datamart.crosstab.jdbc.CrossTabStore;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.hibernate.HibernateSessionManager;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: HibernateDataMartStore.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public class HibernateDataMartStore
    implements DataMartStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }
    
    private JDBCStatementManager statementManager;

    public void setStatementManager( JDBCStatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    private JDBCConfigurationProvider configurationProvider;

    public void setConfigurationProvider( JDBCConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }

    // -------------------------------------------------------------------------
    // AggregatedDataValue
    // -------------------------------------------------------------------------
    
    public double getAggregatedValue( DataElement dataElement, Period period, OrganisationUnit organisationUnit )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "SELECT value " +
                "FROM aggregateddatavalue " +
                "WHERE dataelementid = " + dataElement.getId() + " " +
                "AND periodid = " + period.getId() + " " +
                "AND organisationunitid = " + organisationUnit.getId();
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return resultSet.next() ? resultSet.getDouble( 1 ) : -1;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public double getAggregatedValue( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo, Period period, OrganisationUnit organisationUnit )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "SELECT value " +
                "FROM aggregateddatavalue " +
                "WHERE dataelementid = " + dataElement.getId() + " " +
                "AND categoryoptioncomboid = " + categoryOptionCombo.getId() + " " +
                "AND periodid = " + period.getId() + " " +
                "AND organisationunitid = " + organisationUnit.getId();
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return resultSet.next() ? resultSet.getDouble( 1 ) : -1;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public int deleteAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "DELETE FROM aggregateddatavalue " +
                "WHERE dataelementid IN ( " + getCommaDelimitedString( dataElementIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete aggregated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public int deleteAggregatedDataValues()
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = "DELETE FROM aggregateddatavalue";
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete aggregated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public Collection<String> getAggregatedDataValueIdentifiers() //TODO remove?
    {
        int min = 0;
        
        final int limit = 10000;
        
        Collection<String> identifiers = new HashSet<String>();
        
        Collection<String> temp = null;
        
        while ( ( temp = getAggregatedDataValueIdentifiers( min, limit ) ).size() > 0 )
        {
            identifiers.addAll( temp );
            
            min += limit;
        }
        
        return identifiers;
    }
    
    private Collection<String> getAggregatedDataValueIdentifiers( int min, int limit )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = 
                "SELECT dataelementid, periodid, organisationunitid " +
                "FROM aggregateddatavalue " +
                "ORDER BY dataelementid, periodid, organisationunitid " +
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
            throw new RuntimeException( "Failed to get AggregatedDataValue identifiers", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    // -------------------------------------------------------------------------
    // AggregatedIndicatorValue
    // -------------------------------------------------------------------------

    public double getAggregatedValue( Indicator indicator, Period period, OrganisationUnit organisationUnit )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "SELECT value " +
                "FROM aggregatedindicatorvalue " +
                "WHERE indicatorid = " + indicator.getId() + " " +
                "AND periodid = " + period.getId() + " " +
                "AND organisationunitid = " + organisationUnit.getId();
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return resultSet.next() ? resultSet.getDouble( 1 ) : -1;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated indicator value", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public int deleteAggregatedIndicatorValues( Collection<Integer> indicatorIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "DELETE FROM aggregatedindicatorvalue " +
                "WHERE indicatorid IN ( " + getCommaDelimitedString( indicatorIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete aggregated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public int deleteAggregatedIndicatorValues()
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = "DELETE FROM aggregatedindicatorvalue";
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete aggregated indicator values", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public Collection<String> getAggregatedIndicatorValueIdentifiers() //TODO remove?
    {
        int min = 0;
        
        final int limit = 10000;
        
        Collection<String> identifiers = new HashSet<String>();
        
        Collection<String> temp = null;
        
        while ( ( temp = getAggregatedIndicatorValueIdentifiers( min, limit ) ).size() > 0 )
        {
            identifiers.addAll( temp );
            
            min += limit;
        }
        
        return identifiers;
    }
    
    private Collection<String> getAggregatedIndicatorValueIdentifiers( int min, int limit )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = 
                "SELECT indicatorid, periodid, organisationunitid " +
                "FROM aggregatedindicatorvalue " +
                "ORDER BY indicatorid, periodid, organisationunitid " +
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
            throw new RuntimeException( "Failed to get AggregatedIndicatorValue identifiers", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitStructure
    // -------------------------------------------------------------------------

    public int addOrganisationUnitStructure( OrganisationUnitStructure structure )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( structure );
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnitStructure> getOrganisationUnitStructures()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( OrganisationUnitStructure.class );

        return criteria.list();
    }

    public int deleteOrganisationUnitStructures()
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "DELETE FROM OrganisationUnitStructure" );

        return query.executeUpdate();
    }

    // -------------------------------------------------------------------------
    // GroupSetStructure
    // -------------------------------------------------------------------------

    public int addGroupSetStructure( GroupSetStructure structure )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( structure );
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<GroupSetStructure> getGroupSetStructures()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( GroupSetStructure.class );

        return criteria.list();
    }

    public int deleteGroupSetStructures()
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "DELETE FROM GroupSetStructure" );

        return query.executeUpdate();
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryOptionComboName
    // -------------------------------------------------------------------------

    public int addDataElementCategoryOptionComboName( DataElementCategoryOptionComboName name )
    {
        Session session = sessionManager.getCurrentSession();

        return (Integer) session.save( name );
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<DataElementCategoryOptionComboName> getDataElementCategoryOptionComboNames()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( DataElementCategoryOptionComboName.class );

        return criteria.list();
    }
    
    public int deleteDataElementCategoryOptionComboNames()
    {
        Session session = sessionManager.getCurrentSession();

        Query query = session.createQuery( "DELETE FROM DataElementCategoryOptionComboName" );

        return query.executeUpdate();
    }
    
    // -------------------------------------------------------------------------
    // DataValue
    // -------------------------------------------------------------------------

    public Collection<DataValue> getDataValues( int dataElementId, Collection<Integer> periodIds, Collection<Integer> sourceIds )
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
                    "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                    "AND sourceid IN ( " + getCommaDelimitedString( sourceIds ) + " ) " +
                    "GROUP BY periodid";
                
                ResultSet resultSet = holder.getStatement().executeQuery( sql );
                
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
                throw new RuntimeException( "Failed to get DataValues", ex );
            }
            finally
            {
                holder.close();
            }
        }
        
        return new ArrayList<DataValue>();
    }

    public Map<Operand, String> getDataValueMap( int periodId, int sourceId )
    {
        StatementHolder holder = statementManager.getHolder();
            
        try
        {
            String sql =
                "SELECT dataelementid, categoryoptioncomboid, value " +
                "FROM datavalue " +
                "WHERE periodid = " + periodId + " " +
                "AND sourceid = " + sourceId;
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            Map<Operand, String> map = new HashMap<Operand, String>();
            
            while ( resultSet.next() )
            {
                Operand operand = new Operand( resultSet.getInt( 1 ), resultSet.getInt( 2 ), null );
                
                map.put( operand, resultSet.getString( 3 ) );
            }
            
            return map;
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

    // -------------------------------------------------------------------------
    // CrossTabDataValue
    // -------------------------------------------------------------------------

    public Collection<CrossTabDataValue> getCrossTabDataValues( Collection<Operand> operands, Collection<Integer> periodIds, Collection<Integer> sourceIds )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql =
                "SELECT * " +
                "FROM datavaluecrosstab " +
                "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND sourceid IN ( " + getCommaDelimitedString( sourceIds ) + " )";
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return getCrossTabDataValues( resultSet, operands );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get CrossTabDataValues", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public Collection<CrossTabDataValue> getCrossTabDataValues( Collection<Operand> operands, Collection<Integer> periodIds, int sourceId )
    {
        StatementHolder holder = statementManager.getHolder();
        
        try
        {
            String sql = 
                "SELECT * " +
                "FROM datavaluecrosstab " +
                "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND sourceid = " + sourceId;
            
            ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return getCrossTabDataValues( resultSet, operands );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get CrossTabDataValues", ex );
        }
        finally
        {
            holder.close();
        }
    }

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    public int deleteRelativePeriods()
    {
        StatementHolder holder = statementManager.getHolder();
        
        JDBCConfiguration configuration = configurationProvider.getConfiguration();
        
        StatementBuilder builder = StatementBuilderFactory.createStatementBuilder( configuration.getDialect() );
        
        try
        {
            String sql = builder.getDeleteRelativePeriods();
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete relative periods", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public int deleteZeroDataValues()
    {
        StatementHolder holder = statementManager.getHolder();
        
        JDBCConfiguration configuration = configurationProvider.getConfiguration();
        
        StatementBuilder builder = StatementBuilderFactory.createStatementBuilder( configuration.getDialect() );
        
        try
        {
            String sql = builder.getDeleteZeroDataValues();
            
            return holder.getStatement().executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to delete zero data values", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Converts a ResultSet into a collection of CrossTabDataValues.
     */
    private Collection<CrossTabDataValue> getCrossTabDataValues( ResultSet resultSet, Collection<Operand> operands )
        throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        
        Collection<CrossTabDataValue> values = new ArrayList<CrossTabDataValue>();
        
        Map<Integer, Operand> operandMap = getOperandMap( metaData );

        while ( resultSet.next() )
        {
            CrossTabDataValue value = new CrossTabDataValue();
            
            value.setPeriodId( resultSet.getInt( 1 ) );
            value.setSourceId( resultSet.getInt( 2 ) );
            
            for ( int i = 0; i < metaData.getColumnCount() - 2; i++ )
            {
                int index = i + 3;
                
                Operand operand = operandMap.get( index );
                
                if ( operands.contains( operand ) )
                {
                    value.getValueMap().put( operand, resultSet.getString( index ) );
                }
            }
            
            values.add( value );
        }
        
        return values;
    }
    
    /**
     * Returns a map containing column index as key and data element id as value.
     */
    private Map<Integer, Operand> getOperandMap( ResultSetMetaData metaData )
        throws SQLException
    {
        Map<Integer, Operand> map = new HashMap<Integer, Operand>();
        
        for ( int i = 0; i < metaData.getColumnCount() - 2; i++ )
        {
            int index = i + 3;
            
            String columnName = metaData.getColumnName( index );
            
            String operand = columnName.toLowerCase().replace( CrossTabStore.COLUMN_PREFIX, "" );
            
            int dataElementId = Integer.parseInt( operand.substring( 0, operand.indexOf( CrossTabStore.SEPARATOR ) ) );
            int categoryOptionComboId = Integer.parseInt( operand.substring( operand.indexOf( CrossTabStore.SEPARATOR ) + 1 ) );
            
            map.put( index, new Operand( dataElementId, categoryOptionComboId ) );
        }
        
        return map;
    }
}
