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
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;
import org.hisp.dhis.populator.object.ObjectPopulator;

/**
 * @rule DataSetId = DataElementId; DataElementId; ...
 * @author Torgeir Lorange Ostby
 * @version $Id: DataSetElementMemberPopulator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class DataSetElementMemberPopulator
    implements MemberPopulator
{
    private static final Log LOG = LogFactory.getLog( DataSetElementMemberPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private ObjectPopulator dataElementObjectPopulator;

    public void setDataElementObjectPopulator( ObjectPopulator dataElementObjectPopulator )
    {
        this.dataElementObjectPopulator = dataElementObjectPopulator;
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
        String dataSetId = PopulatorUtils.getRuleId( rule );

        int internalDataSetId = dataSetObjectPopulator.getInternalId( dataSetId );

        DataSet dataSet = dataSetStore.getDataSet( internalDataSetId );

        if ( dataSet == null )
        {
            throw new PopulatorException( "DataSet with id " + internalDataSetId + " not found {" + rule + "}" );
        }

        for ( String dataElementId : PopulatorUtils.getRuleArguments( rule ) )
        {
            int internalDataElementId = dataElementObjectPopulator.getInternalId( dataElementId );

            LOG.debug( "DataSet: " + internalDataSetId + ", DataElement: " + internalDataElementId );

            DataElement dataElement = dataElementService.getDataElement( internalDataElementId );

            if ( dataElement == null )
            {
                throw new PopulatorException( "DataElement with id " + internalDataElementId + " doesn't exist {"
                    + rule + "}" );
            }

            dataSet.getDataElements().add( dataElement );
        }

        dataSetStore.updateDataSet( dataSet );
    }
}