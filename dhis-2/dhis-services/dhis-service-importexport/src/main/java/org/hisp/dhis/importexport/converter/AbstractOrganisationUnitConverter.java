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

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractOrganisationUnitConverter.java 5725 2008-09-18 15:52:54Z larshelg $
 */
public class AbstractOrganisationUnitConverter
    extends AbstractConverter<OrganisationUnit>
{
    protected OrganisationUnitService organisationUnitService;

    protected BatchHandler sourceBatchHandler;
    
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------

    protected void importUnique( OrganisationUnit object )
    {
        int id = sourceBatchHandler.insertObject( object, true );
        
        object.setId( id );
        
        batchHandler.addObject( object );
    }
    
    protected void importMatching( OrganisationUnit object, OrganisationUnit match )
    {
        match.setUuid( object.getUuid() );
        match.setName( object.getName() );
        match.setShortName( object.getShortName() );
        match.setOrganisationUnitCode( object.getOrganisationUnitCode() );
        match.setOpeningDate( object.getClosedDate() );
        match.setClosedDate( object.getClosedDate() );
        match.setActive( object.isActive() );
        match.setComment( object.getComment() );
        match.setGeoCode( object.getGeoCode() );
        
        organisationUnitService.updateOrganisationUnit( match );
    }
    
    protected OrganisationUnit getMatching( OrganisationUnit object )
    {
        OrganisationUnit match = organisationUnitService.getOrganisationUnitByName( object.getName() );
        
        if ( match == null )
        {
            match = organisationUnitService.getOrganisationUnitByShortName( object.getShortName() );
        }
        if ( match == null )
        {
            match = organisationUnitService.getOrganisationUnitByCode( object.getOrganisationUnitCode() );
        }
        
        return match;
    }
    
    protected boolean isIdentical( OrganisationUnit object, OrganisationUnit existing )
    {
        if ( !object.getName().equals( existing.getName() ) )
        {
            return false;
        }
        if ( !object.getShortName().equals( existing.getShortName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getOrganisationUnitCode(), existing.getOrganisationUnitCode() ) || ( isNotNull( object.getOrganisationUnitCode(), existing.getOrganisationUnitCode() ) && !object.getOrganisationUnitCode().equals( existing.getOrganisationUnitCode() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getOpeningDate(), existing.getOpeningDate() ) || ( isNotNull( object.getOpeningDate(), existing.getOpeningDate() ) && !object.getOpeningDate().equals( existing.getOpeningDate() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getClosedDate(), existing.getClosedDate() ) || ( isNotNull( object.getClosedDate(), existing.getClosedDate() ) && !object.getClosedDate().equals( existing.getClosedDate() ) ) )
        {
            return false;
        }
        if ( object.isActive() != existing.isActive() )
        {
            return false;
        }
        if ( !isSimiliar( object.getComment(), existing.getComment() ) || ( isNotNull( object.getComment(), existing.getComment() ) && !object.getComment().equals( existing.getComment() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getGeoCode(), existing.getGeoCode() ) || ( isNotNull( object.getGeoCode(), existing.getGeoCode() ) && !object.getGeoCode().equals( existing.getGeoCode() ) ) )
        {
            return false;
        }
        
        return true;
    }
}
