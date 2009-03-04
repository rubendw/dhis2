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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.common.MetaObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTable
    implements Serializable
{
    public static final String DATAELEMENT_ID = "dataelementid";
    public static final String DATAELEMENT_NAME = "dataelementname";
    public static final String INDICATOR_ID = "indicatorid";
    public static final String INDICATOR_NAME = "indicatorname";
    public static final String DATASET_ID = "datasetid";
    public static final String DATASET_NAME = "datasetname";
    public static final String PERIOD_ID = "periodid";
    public static final String PERIOD_NAME = "periodname";
    public static final String ORGANISATIONUNIT_ID = "organisationunitid";
    public static final String ORGANISATIONUNIT_NAME = "organisationunitname";
    
    public static final String SEPARATOR = "_";
    
    public static final String MODE_DATAELEMENTS = "dataelements";
    public static final String MODE_INDICATORS = "indicators";
    public static final String MODE_DATASETS = "datasets";
    
    private static final String EMPTY_REPLACEMENT = "_";    
    private static final String TABLE_PREFIX = "_report_";

    // -------------------------------------------------------------------------
    // Persisted properties
    // -------------------------------------------------------------------------

    private int id;
    
    private String name;

    private String tableName;
    
    private String existingTableName;
    
    private String mode;

    private List<DataElement> dataElements = new ArrayList<DataElement>();
    
    private List<Indicator> indicators = new ArrayList<Indicator>();
    
    private List<DataSet> dataSets = new ArrayList<DataSet>();
    
    private List<Period> periods = new ArrayList<Period>();
    
    private List<OrganisationUnit> units = new ArrayList<OrganisationUnit>();

    private boolean doIndicators;
    
    private boolean doPeriods;
    
    private boolean doUnits;

    private RelativePeriods relatives;

    private ReportParams reportParams;
    
    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    private List<Period> relativePeriods = new ArrayList<Period>();
    
    private List<Period> allPeriods = new ArrayList<Period>();
    
    private List<MetaObject> crossTabIndicators = new ArrayList<MetaObject>();
    
    private List<Period> crossTabPeriods = new ArrayList<Period>();
    
    private List<OrganisationUnit> crossTabUnits = new ArrayList<OrganisationUnit>();
    
    private List<MetaObject> reportIndicators = new ArrayList<MetaObject>();
    
    private List<Period> reportPeriods = new ArrayList<Period>();
    
    private List<OrganisationUnit> reportUnits = new ArrayList<OrganisationUnit>();
    
    private List<String> indexColumns = new ArrayList<String>();
    
    private List<String> indexNameColumns = new ArrayList<String>();
    
    private List<String> selectColumns = new ArrayList<String>();
    
    private List<String> crossTabColumns = new ArrayList<String>();
    
    private List<String> crossTabIdentifiers = new ArrayList<String>();
        
    private I18nFormat i18nFormat;
    
    private String reportingMonthName;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructor for persistence purposes.
     */
    public ReportTable()
    {   
    }
    
    /**
     * Constructor for testing purposes.
     * 
     * @param name the name.
     * @param tableName the table name.
     */
    public ReportTable( String name, String tableName )
    {
        this.name = name;
        this.tableName = tableName;
    }

    /**
     * ReportTable constructor.
     * 
     * @param name the name.
     * @param mode the mode.
     * @param dataElements the data elements.
     * @param indicators the indicators.
     * @param dataSets the datasets.
     * @param periods the periods. These periods cannot have the name property set.
     * @param relativePeriods the relative periods. These periods must have the name property set. Not persisted.
     * @param units the organisation units.
     * @param doIndicators indicating whether indicators should be crosstabulated.
     * @param doPeriods indicating whether periods should be crosstabulated.
     * @param doUnits indicating whether organisation units should be crosstabulated.
     * @param relatives the relative periods.
     * @param i18nFormat the i18n format. Not persisted.
     * @param reportingMonthName the reporting month name. Not persisted.
     */
    public ReportTable( String name,
        String mode,        
        List<DataElement> dataElements,
        List<Indicator> indicators,
        List<DataSet> dataSets,
        List<Period> periods,
        List<Period> relativePeriods,
        List<OrganisationUnit> units,
        boolean doIndicators,
        boolean doPeriods,
        boolean doUnits,
        RelativePeriods relatives,
        ReportParams reportParams,
        I18nFormat i18nFormat,
        String reportingMonthName )
    {
        this.name = name;
        this.tableName = generateTableName( name );
        this.existingTableName = generateTableName( name );
        this.mode = mode;
        this.dataElements = dataElements;
        this.indicators = indicators;
        this.dataSets = dataSets;
        this.periods = periods;
        this.relativePeriods = relativePeriods;
        this.units = units;
        this.doIndicators = doIndicators;
        this.doPeriods = doPeriods;
        this.doUnits = doUnits;
        this.relatives = relatives;
        this.reportParams = reportParams;
        this.i18nFormat = i18nFormat;
        this.reportingMonthName = reportingMonthName;
        
        init();
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public void init()
    {
        if ( nonEmptyLists( dataElements, indicators, dataSets ) > 1 )
        {
            throw new IllegalArgumentException( "ReportTable cannot contain both dataelements, indicators, and datasets" );
        }
        
        // ---------------------------------------------------------------------
        // Init tableName and allPeriods
        // ---------------------------------------------------------------------

        this.tableName = generateTableName( name );
        
        allPeriods.addAll( periods );
        allPeriods.addAll( relativePeriods );
        
        // ---------------------------------------------------------------------
        // Init indexColumns and selectColumns
        // ---------------------------------------------------------------------

        if ( doIndicators )
        {
            crossTabIndicators = new ArrayList<MetaObject>();
            crossTabIndicators.addAll( indicators );
            crossTabIndicators.addAll( dataElements );
            crossTabIndicators.addAll( dataSets );
            reportIndicators.add( null );
            selectColumns.add( getIdentifier( mode ) );
        }
        else
        {
            crossTabIndicators.add( null );
            reportIndicators = new ArrayList<MetaObject>();
            reportIndicators.addAll( indicators );
            reportIndicators.addAll( dataElements );
            reportIndicators.addAll( dataSets );
            indexColumns.add( getIdentifier( mode ) );
            indexNameColumns.add( getName( mode ) );
        }
        
        if ( doPeriods )
        {
            crossTabPeriods = new ArrayList<Period>( periods );
            crossTabPeriods.addAll( relativePeriods );
            reportPeriods.add( null );
            selectColumns.add( PERIOD_ID );
        }
        else
        {
            crossTabPeriods.add( null );
            reportPeriods = new ArrayList<Period>( periods );
            reportPeriods.addAll( relativePeriods );
            indexColumns.add( PERIOD_ID );
            indexNameColumns.add( PERIOD_NAME );
        }
        
        if ( doUnits )
        {
            crossTabUnits = new ArrayList<OrganisationUnit>( units );
            reportUnits.add( null );
            selectColumns.add( ORGANISATIONUNIT_ID );            
        }
        else
        {
            crossTabUnits.add( null );
            reportUnits = new ArrayList<OrganisationUnit>( units );
            indexColumns.add( ORGANISATIONUNIT_ID );
            indexNameColumns.add( ORGANISATIONUNIT_NAME );
        }

        // ---------------------------------------------------------------------
        // Init crossTabColumns and crossTabIdentifiers
        // ---------------------------------------------------------------------

        for ( MetaObject indicator : crossTabIndicators )
        {
            for ( Period period : crossTabPeriods )
            {
                for ( OrganisationUnit unit : crossTabUnits )
                {
                    String columnName = getColumnName( indicator, period, unit );
                    String columnIdentifier = getColumnIdentifier( indicator, period, unit );
                    
                    crossTabColumns.add( columnName );
                    crossTabIdentifiers.add( columnIdentifier );
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

    public void updateExistingTableName()
    {
        this.existingTableName = generateTableName( name );
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String generateTableName( String name )
    {
        return TABLE_PREFIX + databaseEncode( name );
    }

    private String getIdentifier( String mode )
    {
        if ( mode == null || mode.equals( MODE_INDICATORS ) )
        {
            return INDICATOR_ID;
        }
        else if ( mode.equals( MODE_DATAELEMENTS ) )
        {
            return DATAELEMENT_ID;
        }
        else if ( mode.equals( MODE_DATASETS ) )
        {
            return DATASET_ID;
        }
        
        return null;
    }
    
    private String getName( String mode )
    {
        if ( mode == null || mode.equals( MODE_INDICATORS ) )
        {
            return INDICATOR_NAME;
        }
        else if ( mode.equals( MODE_DATAELEMENTS ) )
        {
            return DATAELEMENT_NAME;
        }
        else if ( mode.equals( MODE_DATASETS ) )
        {
            return DATASET_NAME;
        }
        
        return null;
    }
    
    private int nonEmptyLists( List<? extends MetaObject>... lists )
    {
        int nonEmpty = 0;
        
        for ( List<? extends MetaObject> list : lists )
        {
            if ( list != null && list.size() > 0 )
            {
                ++nonEmpty;
            }
        }
        
        return nonEmpty;
    }
    
    private String getColumnName( MetaObject metaObject, Period period, OrganisationUnit unit )
    {
        StringBuffer buffer = new StringBuffer();
        
        if ( metaObject != null )
        {
            buffer.append( databaseEncode( metaObject.getShortName() ) + SEPARATOR );
        }
        if ( period != null )
        {
            String periodName = period.getName() != null ? period.getName() : i18nFormat.formatPeriod( period );
            
            buffer.append( databaseEncode( periodName ) + SEPARATOR );
        }
        if ( unit != null )
        {
            buffer.append( databaseEncode( unit.getShortName() ) + SEPARATOR );
        }
        
        return buffer.length() > 0 ? buffer.substring( 0, buffer.lastIndexOf( SEPARATOR ) ) : buffer.toString();
    }
    
    private String getColumnIdentifier( MetaObject metaObject, Period period, OrganisationUnit unit )
    {
        StringBuffer buffer = new StringBuffer();

        if ( metaObject != null )
        {
            buffer.append( metaObject.getId() + SEPARATOR );
        }
        if ( period != null )
        {
            buffer.append( period.getId() + SEPARATOR );
        }
        if ( unit != null )
        {
            buffer.append( unit.getId() + SEPARATOR );
        }
        
        return buffer.length() > 0 ? buffer.substring( 0, buffer.lastIndexOf( SEPARATOR ) ) : buffer.toString();
    }
    
    private String databaseEncode( String string )
    {
        if ( string != null )
        {
            string = string.toLowerCase();
            
            string = string.replaceAll( " ", EMPTY_REPLACEMENT );
            string = string.replaceAll( "<", EMPTY_REPLACEMENT + "lt" + EMPTY_REPLACEMENT );
            string = string.replaceAll( ">", EMPTY_REPLACEMENT + "gt" + EMPTY_REPLACEMENT );
            
            StringBuffer buffer = new StringBuffer();
            
            Pattern pattern = Pattern.compile( "[a-zA-Z0-9_]" );
            
            Matcher matcher = pattern.matcher( string );
            
            while ( matcher.find() )
            {
                buffer.append( matcher.group() );
            }
            
            string = buffer.toString();
            
            string = string.replaceAll( EMPTY_REPLACEMENT + "+", EMPTY_REPLACEMENT );
            
            if ( string.length() > 255 )
            {
                string = string.substring( 0, 255 );
            }
        }
        
        return string;
    }

    // -------------------------------------------------------------------------
    // Equals and hashCode
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + ( ( name == null ) ? 0 : name.hashCode() );
        
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
        
        final ReportTable other = (ReportTable) object;
        
        return name.equals( other.getName() );
    }
    
    // -------------------------------------------------------------------------
    // Get- and set-methods for persisted properties
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName( String name )
    {
        this.name = name;
    }

    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }
    
    public String getExistingTableName()
    {
        return existingTableName;
    }

    public void setExistingTableName( String existingTableName )
    {
        this.existingTableName = existingTableName;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode( String mode )
    {
        this.mode = mode;
    }

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( List<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    public List<Period> getPeriods()
    {
        return periods;
    }

    public List<DataSet> getDataSets()
    {
        return dataSets;
    }

    public void setDataSets( List<DataSet> dataSets )
    {
        this.dataSets = dataSets;
    }

    public void setPeriods( List<Period> periods )
    {
        this.periods = periods;
    }

    public List<OrganisationUnit> getUnits()
    {
        return units;
    }

    public void setUnits( List<OrganisationUnit> units )
    {
        this.units = units;
    }

    public boolean isDoIndicators()
    {
        return doIndicators;
    }

    public void setDoIndicators( boolean doIndicators )
    {
        this.doIndicators = doIndicators;
    }

    public boolean isDoPeriods()
    {
        return doPeriods;
    }

    public void setDoPeriods( boolean doPeriods )
    {
        this.doPeriods = doPeriods;
    }

    public boolean isDoUnits()
    {
        return doUnits;
    }

    public void setDoUnits( boolean doUnits )
    {
        this.doUnits = doUnits;
    }

    public RelativePeriods getRelatives()
    {
        return relatives;
    }

    public void setRelatives( RelativePeriods relatives )
    {
        this.relatives = relatives;
    }

    public ReportParams getReportParams()
    {
        return reportParams;
    }

    public void setReportParams( ReportParams reportParams )
    {
        this.reportParams = reportParams;
    }

    // -------------------------------------------------------------------------
    // Get- and set-methods for transient properties
    // -------------------------------------------------------------------------

    public List<Period> getRelativePeriods()
    {
        return relativePeriods;
    }

    public void setRelativePeriods( List<Period> relativePeriods )
    {
        this.relativePeriods = relativePeriods;
    }

    public List<Period> getAllPeriods()
    {
        return allPeriods;
    }

    public I18nFormat getI18nFormat()
    {
        return i18nFormat;
    }

    public void setI18nFormat( I18nFormat format )
    {
        i18nFormat = format;
    }

    public List<MetaObject> getReportIndicators()
    {
        return reportIndicators;
    }

    public List<Period> getReportPeriods()
    {
        return reportPeriods;
    }

    public List<OrganisationUnit> getReportUnits()
    {
        return reportUnits;
    }
    
    public List<String> getIndexColumns()
    {
        return indexColumns;
    }

    public List<String> getIndexNameColumns()
    {
        return indexNameColumns;
    }

    public List<String> getSelectColumns()
    {
        return selectColumns;
    }

    public List<String> getCrossTabColumns()
    {
        return crossTabColumns;
    }

    public List<String> getCrossTabIdentifiers()
    {
        return crossTabIdentifiers;
    }

    public String getReportingMonthName()
    {
        return reportingMonthName;
    }

    public void setReportingMonthName( String reportingMonthName )
    {
        this.reportingMonthName = reportingMonthName;
    }
}
