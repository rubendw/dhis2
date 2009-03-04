package org.hisp.dhis.datamart.resourcetable;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.DataElementCategoryOptionComboName;
import org.hisp.dhis.aggregation.GroupSetStructure;
import org.hisp.dhis.aggregation.OrganisationUnitStructure;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.GroupSetStructureBatchHandler;
import org.hisp.dhis.aggregation.batch.handler.OrganisationUnitStructureBatchHandler;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultResourceTableService.java 5459 2008-06-26 01:12:03Z larshelg $
 */
public class DefaultResourceTableService
    implements ResourceTableService
{
    private static final Log LOG = LogFactory.getLog( DefaultResourceTableService.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataMartStore dataMartStore;

    public void setDataMartStore( DataMartStore dataMartStore )
    {
        this.dataMartStore = dataMartStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }
    
    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService categoryOptionComboService )
    {
        this.categoryOptionComboService = categoryOptionComboService;
    }
    
    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitStructure
    // -------------------------------------------------------------------------
    
    public void generateOrganisationUnitStructures()
    {
        dataMartStore.deleteOrganisationUnitStructures();
        
        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( OrganisationUnitStructureBatchHandler.class );
        
        batchHandler.init();
        
        for ( int i = 0; i < 8; i++ )
        {
            int level = i + 1;
            
            Collection<OrganisationUnit> units = organisationUnitService.getOrganisationUnitsAtLevel( level );
            
            for ( OrganisationUnit unit : units )
            {
                OrganisationUnitStructure structure = new OrganisationUnitStructure();

                structure.setOrganisationUnitId( unit.getId() );
                structure.setLevel( level );
                
                Map<Integer, Integer> identifiers = new HashMap<Integer, Integer>();
                Map<Integer, String> geoCodes = new HashMap<Integer, String>();
                
                for ( int j = level; j > 0; j-- )
                {
                    identifiers.put( j, unit.getId() );
                    geoCodes.put( j, unit.getGeoCode() );
                    
                    unit = unit.getParent();
                }
                
                structure.setIdLevel1( identifiers.get( 1 ) );
                structure.setIdLevel2( identifiers.get( 2 ) );
                structure.setIdLevel3( identifiers.get( 3 ) );
                structure.setIdLevel4( identifiers.get( 4 ) );
                structure.setIdLevel5( identifiers.get( 5 ) );
                structure.setIdLevel6( identifiers.get( 6 ) );
                structure.setIdLevel7( identifiers.get( 7 ) );
                structure.setIdLevel8( identifiers.get( 8 ) );  
                
                structure.setGeoCodeLevel1( geoCodes.get( 1 ) );
                structure.setGeoCodeLevel2( geoCodes.get( 2 ) );
                structure.setGeoCodeLevel3( geoCodes.get( 3 ) );
                structure.setGeoCodeLevel4( geoCodes.get( 4 ) );
                structure.setGeoCodeLevel5( geoCodes.get( 5 ) );
                structure.setGeoCodeLevel6( geoCodes.get( 6 ) );
                structure.setGeoCodeLevel7( geoCodes.get( 7 ) );
                structure.setGeoCodeLevel8( geoCodes.get( 8 ) );
                
                batchHandler.addObject( structure );
            }
        }
        
        batchHandler.flush();
    }
    
    // -------------------------------------------------------------------------
    // GroupSetStructure
    // -------------------------------------------------------------------------

    public void generateGroupSetStructures()
    {
        dataMartStore.deleteGroupSetStructures();
        
        Collection<OrganisationUnitGroupSet> exclusiveGroupSets = organisationUnitGroupService
            .getExclusiveOrganisationUnitGroupSets();

        BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( GroupSetStructureBatchHandler.class );
        
        batchHandler.init();
        
        for ( OrganisationUnitGroupSet groupSet : exclusiveGroupSets )
        {
            Collection<OrganisationUnitGroup> groups = groupSet.getOrganisationUnitGroups();

            for ( OrganisationUnitGroup group : groups )
            {
                Collection<OrganisationUnit> units = group.getMembers();

                for ( OrganisationUnit unit : units )
                {
                    GroupSetStructure groupSetStructure = new GroupSetStructure( unit.getId(), group.getId(), groupSet.getId() );

                    try
                    {
                        batchHandler.addObject( groupSetStructure );
                    }
                    catch ( Exception ex )
                    {
                        LOG.warn( "Error occured - skipping OrganisationUnitGroupSetStructure", ex );
                    }
                }
            }
        }
        
        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryOptionComboName
    // -------------------------------------------------------------------------
    
    public void generateCategoryOptionComboNames()
    {
        dataMartStore.deleteDataElementCategoryOptionComboNames();
        
        Collection<DataElementCategoryOptionCombo> combos = categoryOptionComboService.getAllDataElementCategoryOptionCombos();
        
        for ( DataElementCategoryOptionCombo combo : combos )
        {
            String name = categoryOptionComboService.getOptionNames( combo );
            
            DataElementCategoryOptionComboName entry = new DataElementCategoryOptionComboName( combo.getId(), name );
            
            dataMartStore.addDataElementCategoryOptionComboName( entry );
        }
    }
    
    // -------------------------------------------------------------------------
    // DataMart
    // -------------------------------------------------------------------------

    public int clearDataValues()
    {
        return dataMartStore.deleteAggregatedDataValues();
    }    

    public int clearIndicatorValues()
    {
        return dataMartStore.deleteAggregatedIndicatorValues();
    }

}
