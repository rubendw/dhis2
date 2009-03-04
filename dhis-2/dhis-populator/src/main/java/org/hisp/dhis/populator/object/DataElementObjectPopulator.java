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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.populator.PopulatorException;
import org.hisp.dhis.populator.PopulatorUtils;

/**
 * @rule id = name; shortName; description; active; type; aggregationOperator
 * @author Torgeir Lorange Ostby
 * @version $Id: DataElementObjectPopulator.java 3120 2007-03-20 16:35:20Z hanssto $
 */
public class DataElementObjectPopulator
    extends ObjectPopulator
{
    private static final Log LOG = LogFactory.getLog( DataElementObjectPopulator.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // ObjectPopulator implementation
    // -------------------------------------------------------------------------

    public void executeRule( String rule )
        throws PopulatorException
    {
        String id = PopulatorUtils.getRuleId( rule );

        List<String> arguments = PopulatorUtils.getRuleArguments( rule );

        if ( arguments.size() != 6 )
        {
            throw new PopulatorException( "Wrong number of arguments, must be 5 {" + rule + "}" );
        }

        String name = arguments.get( 0 );
        String shortName = arguments.get( 1 );
        String description = arguments.get( 2 );
        boolean active = PopulatorUtils.toBoolean( arguments.get( 3 ) );
        String type = arguments.get( 4 );
        String aggregationOperator = arguments.get( 5 );

        LOG
            .debug( "dataElementService.addDataElement( " + name + ", " + description + ", " + active + ", " + type
                + " )" );

        DataElement dataElement = new DataElement( name, shortName, description, active, type, aggregationOperator,
            null );

        int internalId = dataElementService.addDataElement( dataElement );

        addIdMapping( id, internalId );
    }
}
