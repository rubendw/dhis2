package org.hisp.dhis.importexport.ixf.converter;

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

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractOrganisationUnitConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import static org.hisp.dhis.system.util.TextUtils.subString;

/**
 * @author Lars Helge Overland
 * @version $Id: SourceConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class SourceConverter
    extends AbstractOrganisationUnitConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "sources";
    public static final String ELEMENT_NAME = "source";
    
    private static final String FIELD_ORG = "org";
    private static final String FIELD_KEY = "key";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public SourceConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param 
     */
    public SourceConverter( BatchHandler batchHandler,
        BatchHandler sourceBatchHandler,
        ImportObjectService importObjectService,
        OrganisationUnitService organisationUnitService )
    {
        this.batchHandler = batchHandler;
        this.sourceBatchHandler = sourceBatchHandler;
        this.importObjectService = importObjectService;
        this.organisationUnitService = organisationUnitService;
    }
    
    // -------------------------------------------------------------------------
    // IXFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<OrganisationUnit> units = params.getOrganisationUnits();
        
        if ( units != null && units.size() > 0 )
        {        
            writer.openElement( COLLECTION_NAME );
            
            for ( OrganisationUnit unit : units )
            {
                writer.openElement( ELEMENT_NAME, FIELD_KEY, unit.getUuid() );
                
                writer.writeElement( FIELD_ORG, unit.getName(), "lang", "en" );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }

    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            OrganisationUnit unit = new OrganisationUnit();

            unit.setUuid( reader.getAttributeValue( FIELD_KEY ) );
            
            reader.moveToStartElement( FIELD_ORG );            
            unit.setName( reader.getElementValue() );
            unit.setShortName( subString( unit.getName(), 0, 20 ) );
            
            NameMappingUtil.addOrganisationUnitMapping( unit.getUuid(), unit.getName() );
            
            read( unit, OrganisationUnit.class, GroupMemberType.NONE, params );            
        }
    }
}
