package org.hisp.dhis.importexport.converter;

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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractDataElementConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class AbstractDataElementConverter
    extends AbstractConverter<DataElement>
{
    protected DataElementService dataElementService;
        
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------

    protected void importUnique( DataElement object )
    {
        batchHandler.addObject( object );        
    }

    protected void importMatching( DataElement object, DataElement match )
    {
        match.setUuid( object.getUuid() );
        match.setName( object.getName() );
        match.setShortName( object.getShortName() );
        match.setAlternativeName( object.getAlternativeName() );
        match.setCode( object.getCode() );
        match.setDescription( object.getDescription() );
        match.setActive( object.isActive() );
        match.setType( object.getType() );
        match.setAggregationOperator( object.getAggregationOperator() );
                            
        dataElementService.updateDataElement( match );
    }
    
    protected DataElement getMatching( DataElement object )
    {
        DataElement match = dataElementService.getDataElementByName( object.getName() );

        if ( match == null )
        {
            match = dataElementService.getDataElementByAlternativeName( object.getAlternativeName() );
        }
        if ( match == null )
        {
            match = dataElementService.getDataElementByShortName( object.getShortName() );
        }
        if ( match == null )
        {
            match = dataElementService.getDataElementByCode( object.getCode() );
        }
        
        return match;
    }
    
    protected boolean isIdentical( DataElement object, DataElement existing )
    {
        if ( !object.getName().equals( existing.getName() ) )
        {
            return false;
        }        
        if ( !isSimiliar( object.getAlternativeName(), existing.getAlternativeName() ) || ( isNotNull( object.getAlternativeName(), existing.getAlternativeName() ) && !object.getAlternativeName().equals( existing.getAlternativeName() ) ) )
        {
            return false;
        }
        if ( !object.getShortName().equals( existing.getShortName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getCode(), existing.getCode() ) || ( isNotNull( object.getCode(), existing.getCode() ) && !object.getCode().equals( existing.getCode() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDescription(), existing.getDescription() ) || ( isNotNull( object.getDescription(), existing.getDescription() ) && !object.getDescription().equals( existing.getDescription() ) ) )
        {
            return false;
        }
        if ( object.isActive() != existing.isActive() )
        {
            return false;
        }
        if ( !object.getType().equals( existing.getType() ) )
        {
            return false;
        }
        if ( !object.getAggregationOperator().equals( existing.getAggregationOperator() ) )
        {
            return false;
        }
        
        return true;
    }
}
