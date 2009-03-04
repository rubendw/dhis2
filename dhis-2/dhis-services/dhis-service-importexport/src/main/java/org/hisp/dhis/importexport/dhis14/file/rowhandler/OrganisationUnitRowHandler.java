package org.hisp.dhis.importexport.dhis14.file.rowhandler;

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
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.converter.AbstractOrganisationUnitConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.util.UUIdUtils;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitRowHandler.java 4691 2008-03-10 17:52:00Z larshelg $
 */
public class OrganisationUnitRowHandler
    extends AbstractOrganisationUnitConverter implements RowHandler
{    
    private ImportParams params;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public OrganisationUnitRowHandler( BatchHandler batchHandler, 
        BatchHandler sourceBatchHandler,
        ImportObjectService importObjectService,
        OrganisationUnitService organisationUnitService,
        ImportParams params )
    {
        this.batchHandler = batchHandler;
        this.sourceBatchHandler = sourceBatchHandler;
        this.importObjectService = importObjectService;
        this.organisationUnitService = organisationUnitService;
        this.params = params;
    }
    
    // -------------------------------------------------------------------------
    // BatchRowHandler implementation
    // -------------------------------------------------------------------------

    public void handleRow( Object object )
    {
        OrganisationUnit unit = (OrganisationUnit) object;
        
        NameMappingUtil.addOrganisationUnitMapping( unit.getId(), unit.getName() );

        unit.setUuid( UUIdUtils.getUUId() );
            
        if ( unit.getOrganisationUnitCode() != null && unit.getOrganisationUnitCode().trim().length() == 0 )
        {
            unit.setOrganisationUnitCode( null );                
        }
        
        read( unit, OrganisationUnit.class, GroupMemberType.NONE, params );
    }
}
