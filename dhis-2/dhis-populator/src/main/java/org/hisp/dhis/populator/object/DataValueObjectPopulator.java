package org.hisp.dhis.populator.object;

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

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;

/**
 * @rule id = dataElementId; periodId; sourceId; value; storedBy; timestamp; comment
 * @author Torgeir Lorange Ostby
 * @version $Id: DataValueObjectPopulator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class DataValueObjectPopulator
    extends ObjectPopulator
{
    private static final Log LOG = LogFactory.getLog( DataValueObjectPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    public PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    public SourceStore sourceStore;

    public void setSourceStore( SourceStore sourceStore )
    {
        this.sourceStore = sourceStore;
    }

    private ObjectPopulator dataElementObjectPopulator;

    public void setDataElementObjectPopulator( ObjectPopulator dataElementObjectPopulator )
    {
        this.dataElementObjectPopulator = dataElementObjectPopulator;
    }

    private ObjectPopulator periodObjectPopulator;

    public void setPeriodObjectPopulator( ObjectPopulator periodObjectPopulator )
    {
        this.periodObjectPopulator = periodObjectPopulator;
    }

    private ObjectPopulator organisationUnitObjectPopulator;

    public void setOrganisationUnitObjectPopulator( ObjectPopulator organisationUnitObjectPopulator )
    {
        this.organisationUnitObjectPopulator = organisationUnitObjectPopulator;
    }

    // -------------------------------------------------------------------------
    // ObjectPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        List<String> arguments = PopulatorUtils.getRuleArguments( rule );

        if ( arguments.size() != 7 )
        {
            throw new PopulatorException( "Wrong number of arguments, must be 7 {" + rule + "}" );
        }

        int dataElementId = dataElementObjectPopulator.getInternalId( arguments.get( 0 ) );
        int periodId = periodObjectPopulator.getInternalId( arguments.get( 1 ) );
        int sourceId = organisationUnitObjectPopulator.getInternalId( arguments.get( 2 ) );
        String value = arguments.get( 3 );
        String storedBy = arguments.get( 4 );
        Date timestamp = PopulatorUtils.toDate( arguments.get( 5 ) );
        String comment = arguments.get( 6 );

        LOG.debug( "dataValueService.addDataValue( " + dataElementId + ", " + periodId + ", " + sourceId + ", " + value
            + ", " + storedBy + ", " + timestamp + ", " + comment + " )" );

        DataElement dataElement = dataElementService.getDataElement( dataElementId );
        Period period = periodService.getPeriod( periodId );
        Source source = sourceStore.getSource( sourceId );

        dataValueService
            .addDataValue( new DataValue( dataElement, period, source, value, storedBy, timestamp, comment ) );
    }
}
