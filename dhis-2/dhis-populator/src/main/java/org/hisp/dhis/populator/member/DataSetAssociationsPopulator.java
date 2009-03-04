package org.hisp.dhis.populator.member;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;
import org.hisp.dhis.populator.object.ObjectPopulator;

/**
 * @rule DataSetId = OrgUnitId; OrgUnitId; ...
 * @author Torgeir Lorange Ostby
 * @version $Id: DataSetAssociationsPopulator.java 3648 2007-10-15 22:47:45Z larshelg $
 */
public class DataSetAssociationsPopulator
    implements MemberPopulator
{
    private static final Log LOG = LogFactory.getLog( DataSetAssociationsPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private ObjectPopulator organisationUnitObjectPopulator;

    public void setOrganisationUnitObjectPopulator( ObjectPopulator organisationUnitObjectPopulator )
    {
        this.organisationUnitObjectPopulator = organisationUnitObjectPopulator;
    }

    private DataSetStore dataSetStore;

    public void setDataSetStore( DataSetStore dataSetStore )
    {
        this.dataSetStore = dataSetStore;
    }

    private ObjectPopulator dataSetObjectPopulator;

    public void setDataSetObjectPopulator( ObjectPopulator dataSetObjectPopulator )
    {
        this.dataSetObjectPopulator = dataSetObjectPopulator;
    }

    // -------------------------------------------------------------------------
    // MemberPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String organisationUnitGroup = PopulatorUtils.getRuleId( rule );

        int dataSetId = dataSetObjectPopulator.getInternalId( organisationUnitGroup );

        for ( String organisationUnit : PopulatorUtils.getRuleArguments( rule ) )
        {
            int organisationUnitId = organisationUnitObjectPopulator.getInternalId( organisationUnit );

            LOG.debug( "dataSetStore.addDataSetToSource( " + dataSetId + ", " + organisationUnitId + " )" );

            DataSet dataSet = dataSetStore.getDataSet( dataSetId );

            OrganisationUnit unit = organisationUnitService.getOrganisationUnit( organisationUnitId );

            if ( dataSet.getSources().add( unit ) )
            {
                dataSetStore.updateDataSet( dataSet );
            }
        }
    }
}
