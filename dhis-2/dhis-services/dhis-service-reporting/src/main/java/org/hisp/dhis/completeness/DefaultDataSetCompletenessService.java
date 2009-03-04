package org.hisp.dhis.completeness;

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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.external.configuration.ConfigurationManager;
import org.hisp.dhis.external.configuration.NoConfigurationFoundException;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultDataSetCompletenessService
    implements DataSetCompletenessService
{
    private static final Log log = LogFactory.getLog( DefaultDataSetCompletenessService.class );
    
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private String configDir;

    public void setConfigDir( String configDir )
    {
        this.configDir = configDir;
    }
    
    private String configFile;

    public void setConfigFile( String configFile )
    {
        this.configFile = configFile;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;
    
    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private CompleteDataSetRegistrationService registrationService;
    
    public void setRegistrationService( CompleteDataSetRegistrationService registrationService )
    {
        this.registrationService = registrationService;
    }

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }

    private ConfigurationManager<DataSetCompletenessConfiguration> configurationManager;
    
    public void setConfigurationManager( ConfigurationManager<DataSetCompletenessConfiguration> configurationManager )
    {
        this.configurationManager = configurationManager;
    }
    
    // -------------------------------------------------------------------------
    // DataSetCompletenessService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // DataSetCompleteness
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public Collection<DataSetCompletenessResult> getDataSetCompleteness( int periodId, int organisationUnitId )
    {
        Period period = periodService.getPeriod( periodId );
        
        Date deadline = null;
        
        try
        {
            deadline = getConfiguration().getDeadline( period );
        }
        catch ( NoConfigurationFoundException ex )
        {
            log.warn( "Disabling on-time completeness calculations because no configuration was found" );
        }
        
        Collection<? extends Source> children = organisationUnitService.getOrganisationUnitWithChildren( organisationUnitId );
        
        Collection<DataSet> dataSets = dataSetService.getDataSetsBySources( children );
        
        Collection<DataSetCompletenessResult> results = new ArrayList<DataSetCompletenessResult>();
        
        for ( DataSet dataSet : dataSets )
        {
            DataSetCompletenessResult result = new DataSetCompletenessResult();
            
            result.setName( dataSet.getName() );
            result.setRegistrations( registrationService.getCompleteDataSetRegistrationsForDataSet( dataSet, children, period ) );
            result.setRegistrationsOnTime( deadline != null ? registrationService.getCompleteDataSetRegistrationsForDataSet( dataSet, children, period, deadline ) : 0 );
            result.setSources( dataSetService.getSourcesAssociatedWithDataSet( dataSet, children ) );
            
            result.setDataSetId( dataSet.getId() );
            result.setPeriodId( periodId );
            result.setOrganisationUnitId( organisationUnitId );
            
            results.add( result );
        }
        
        return results;
    }
    
    public Collection<DataSetCompletenessResult> getDataSetCompleteness( int periodId, int parentOrganisationUnitId, int dataSetId )
    {
        Period period = periodService.getPeriod( periodId );

        Date deadline = null;
        
        try
        {
            deadline = getConfiguration().getDeadline( period );
        }
        catch ( NoConfigurationFoundException ex )
        {
            log.warn( "Disabling on-time completeness calculations because no configuration was found" );
        }
        
        DataSet dataSet = dataSetService.getDataSet( dataSetId );
        
        OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentOrganisationUnitId );
        
        Collection<OrganisationUnit> units = parent.getChildren();
        
        Collection<DataSetCompletenessResult> results = new ArrayList<DataSetCompletenessResult>();
        
        for ( OrganisationUnit unit : units )
        {
            Collection<OrganisationUnit> children = organisationUnitService.getOrganisationUnitWithChildren( unit.getId() );
            
            DataSetCompletenessResult result = new DataSetCompletenessResult();
            
            result.setName( unit.getName() );
            result.setRegistrations( registrationService.getCompleteDataSetRegistrationsForDataSet( dataSet, children, period ) );
            result.setRegistrationsOnTime( deadline != null ? registrationService.getCompleteDataSetRegistrationsForDataSet( dataSet, children, period, deadline ) : 0 );
            result.setSources( dataSetService.getSourcesAssociatedWithDataSet( dataSet, children ) );
            
            result.setDataSetId( dataSetId );
            result.setPeriodId( periodId );
            result.setOrganisationUnitId( unit.getId() );
            
            results.add( result );
        }
        
        return results;
    }

    public DataSetCompletenessResult getDataSetCompleteness( Period period, Date deadline, OrganisationUnit unit, DataSet dataSet )
    {        
        Collection<OrganisationUnit> children = organisationUnitService.getOrganisationUnitWithChildren( unit.getId() );
        
        DataSetCompletenessResult result = new DataSetCompletenessResult();
        
        result.setName( unit.getName() );
        result.setRegistrations( registrationService.getCompleteDataSetRegistrationsForDataSet( dataSet, children, period ) );
        result.setRegistrationsOnTime( deadline != null ? registrationService.getCompleteDataSetRegistrationsForDataSet( dataSet, children, period, deadline ) : 0 );
        result.setSources( dataSetService.getSourcesAssociatedWithDataSet( dataSet, children ) );
        
        result.setDataSetId( dataSet.getId() );
        result.setPeriodId( period.getId() );
        result.setOrganisationUnitId( unit.getId() );
        
        return result;
    }
    
    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    public void setConfiguration( DataSetCompletenessConfiguration configuration )
    {
        try
        {
            OutputStream out = locationManager.getOutputStream( configFile, configDir );
            
            configurationManager.setConfiguration( configuration, out );
        }
        catch ( LocationManagerException ex )
        {
            throw new RuntimeException( "Failed to set configuration", ex );
        }
    }
    
    public DataSetCompletenessConfiguration getConfiguration()
        throws NoConfigurationFoundException
    {
        try
        {
            InputStream in = locationManager.getInputStream( configFile, configDir );
            
            return configurationManager.getConfiguration( in, DataSetCompletenessConfiguration.class );
        }
        catch ( LocationManagerException ex )
        {
            throw new NoConfigurationFoundException( "No configuration file found" );
        }
    }
}
