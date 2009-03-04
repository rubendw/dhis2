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
import org.hisp.dhis.importexport.converter.AbstractIndicatorTypeConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorTypeConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class IndicatorTypeConverter
    extends AbstractIndicatorTypeConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "indicatorTypes";
    public static final String ELEMENT_NAME = "indicatorType";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_FACTOR = "factor";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public IndicatorTypeConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param indicatorService the indicatorService to use.
     * @param importObjectService the importObjectService to use.
     */
    public IndicatorTypeConverter( BatchHandler batchHandler, 
        ImportObjectService importObjectService, 
        IndicatorService indicatorService )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.indicatorService = indicatorService;
    }    

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<IndicatorType> indicatorTypes = params.getIndicatorTypes();
        
        if ( indicatorTypes != null && indicatorTypes.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( IndicatorType type : indicatorTypes )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_ID, String.valueOf( type.getId() ) );
                writer.writeElement( FIELD_NAME, type.getName() );
                writer.writeElement( FIELD_FACTOR, String.valueOf( type.getFactor() ) );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            IndicatorType type = new IndicatorType();

            reader.moveToStartElement( FIELD_ID );
            type.setId( Integer.parseInt( reader.getElementValue() ) );
                     
            reader.moveToStartElement( FIELD_NAME );
            type.setName( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_FACTOR );
            type.setFactor( Integer.parseInt( reader.getElementValue() ) );
            
            NameMappingUtil.addIndicatorTypeMapping( type.getId(), type.getName() );
            
            read( type, IndicatorType.class, GroupMemberType.NONE, params );
        }
    }
}
