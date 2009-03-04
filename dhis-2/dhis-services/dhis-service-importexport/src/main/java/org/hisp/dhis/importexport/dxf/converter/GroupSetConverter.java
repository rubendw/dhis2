package org.hisp.dhis.importexport.dxf.converter;

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

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractGroupSetConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: GroupSetConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class GroupSetConverter
    extends AbstractGroupSetConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "groupSets";
    public static final String ELEMENT_NAME = "groupSet";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_COMPULSORY = "compulsory";
    private static final String FIELD_EXCLUSIVE = "exclusive";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public GroupSetConverter()
    {   
    }    

    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param organisationUnitGroupService the organisationUnitGroupService to use.
     * @param importObjectService the importObjectService to use.
     */
    public GroupSetConverter( BatchHandler batchHandler, 
        ImportObjectService importObjectService, 
        OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.organisationUnitGroupService = organisationUnitGroupService;
    }
    
    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<OrganisationUnitGroupSet> groupSets = params.getOrganisationUnitGroupSets();
        
        if ( groupSets != null && groupSets.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( OrganisationUnitGroupSet groupSet : groupSets )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_ID, String.valueOf( groupSet.getId() ) );
                writer.writeElement( FIELD_NAME, groupSet.getName() );
                writer.writeElement( FIELD_DESCRIPTION, groupSet.getDescription() );
                writer.writeElement( FIELD_COMPULSORY, String.valueOf( groupSet.isCompulsory() ) );
                writer.writeElement( FIELD_EXCLUSIVE, String.valueOf( groupSet.isExclusive() ) );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            OrganisationUnitGroupSet groupSet = new OrganisationUnitGroupSet();

            reader.moveToStartElement( FIELD_ID );
            groupSet.setId( Integer.parseInt( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_NAME );
            groupSet.setName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_DESCRIPTION );
            groupSet.setDescription( reader.getElementValue() );

            reader.moveToStartElement( FIELD_COMPULSORY );
            groupSet.setCompulsory( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_EXCLUSIVE );
            groupSet.setExclusive( Boolean.parseBoolean( reader.getElementValue() ) );
            
            NameMappingUtil.addGroupSetMapping( groupSet.getId(), groupSet.getName() );
            
            read( groupSet, OrganisationUnitGroupSet.class, GroupMemberType.NONE, params );
        }
    }
}
