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
import java.util.Map;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractDataElementConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;

/**
 * @author Lars Helge Overland
 * @version $Id: DataElementConverter.java 5727 2008-09-18 17:48:54Z larshelg $
 */
public class DataElementConverter
    extends AbstractDataElementConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "dataElements";
    public static final String ELEMENT_NAME = "dataElement";

    private static final String FIELD_ID = "id";
    private static final String FIELD_UUID = "uuid";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ALTERNATIVE_NAME = "alternativeName";
    private static final String FIELD_SHORT_NAME = "shortName";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_ACTIVE = "active";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_AGGREGATION_OPERATOR = "aggregationOperator";
    private static final String FIELD_CATEGORY_COMBO = "categoryCombo";

    private Map<Object, Integer> categoryComboMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DataElementConverter()
    {
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param importObjectService the importObjectService to use.
     * @param categoryComboMapping the categoryComboMapping to use.
     * @param dataElementService the dataElementService to use.
     */
    public DataElementConverter( BatchHandler batchHandler, 
        ImportObjectService importObjectService,
        Map<Object, Integer> categoryComboMapping,
        DataElementService dataElementService )
    {        
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.categoryComboMapping = categoryComboMapping;
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<DataElement> elements = params.getDataElements();
        
        if ( elements != null && elements.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( DataElement element : elements )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_ID, String.valueOf( element.getId() ) );
                writer.writeElement( FIELD_UUID, element.getUuid() );
                writer.writeElement( FIELD_NAME, element.getName() );
                writer.writeElement( FIELD_ALTERNATIVE_NAME, element.getAlternativeName() );
                writer.writeElement( FIELD_SHORT_NAME, element.getShortName() );
                writer.writeElement( FIELD_CODE, element.getCode() );
                writer.writeElement( FIELD_DESCRIPTION, element.getDescription() );
                writer.writeElement( FIELD_ACTIVE, String.valueOf( element.isActive() ) );
                writer.writeElement( FIELD_TYPE, element.getType() );
                writer.writeElement( FIELD_AGGREGATION_OPERATOR, element.getAggregationOperator() );
                writer.writeElement( FIELD_CATEGORY_COMBO, String.valueOf( element.getCategoryCombo().getId() ) );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            DataElement element = new DataElement();
            
            DataElementCategoryCombo categoryCombo = new DataElementCategoryCombo();
            element.setCategoryCombo( categoryCombo );
            
            reader.moveToStartElement( FIELD_ID );
            element.setId( Integer.parseInt( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_UUID );            
            element.setUuid( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_NAME );
            element.setName( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_ALTERNATIVE_NAME );
            element.setAlternativeName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_SHORT_NAME );
            element.setShortName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_CODE );
            element.setCode( reader.getElementValue() );

            reader.moveToStartElement( FIELD_DESCRIPTION );
            element.setDescription( reader.getElementValue() );

            reader.moveToStartElement( FIELD_ACTIVE );
            element.setActive( Boolean.parseBoolean( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_TYPE );
            element.setType( reader.getElementValue() );

            reader.moveToStartElement( FIELD_AGGREGATION_OPERATOR );
            element.setAggregationOperator( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_CATEGORY_COMBO );
            element.getCategoryCombo().setId( categoryComboMapping.get( Integer.parseInt( reader.getElementValue() ) ) );
            
            NameMappingUtil.addDataElementMapping( element.getId(), element.getName() );
            
            read( element, DataElement.class, GroupMemberType.NONE, params );
        }
    }
}
