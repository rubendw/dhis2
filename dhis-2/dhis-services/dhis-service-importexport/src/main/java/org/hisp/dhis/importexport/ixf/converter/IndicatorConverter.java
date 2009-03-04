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

import static org.hisp.dhis.system.util.TextUtils.subString;

import java.util.Collection;
import java.util.Map;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractDataElementConverter;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class IndicatorConverter
    extends AbstractDataElementConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "indicators";
    public static final String ELEMENT_NAME = "indicator";
    
    private static final String FIELD_DEFINITION = "definition";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TYPE = "type";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private DataValueService dataValueService;

    private DataElementCategoryCombo categoryCombo;
    
    private BatchHandler dataValueBatchHandler;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private XMLConverter dataConverter;
    
    private Map<Object, Integer> periodMapping;
    
    private Map<Object, Integer> sourceMapping;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     * 
     * @param indicatorService the indicatorService to use.
     * @param dataElementService the dataElementService to use.
     * @param dataValueService the dataValueService to use.
     * @param aggregationStore the aggregationStore to use.
     * @param configurationManager the configurationManager to use.
     */
    public IndicatorConverter( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    /**
     * Constructor for read operations.
     * 
     * @param dataElementService the dataElementService to use.
     * @param importObjectService the importObjectService to use.
     */
    public IndicatorConverter( BatchHandler batchHandler,
        DataElementService dataElementService, 
        ImportObjectService importObjectService,
        DataElementCategoryCombo categoryCombo,
        BatchHandler dataValueBatchHandler,
        DataElementCategoryOptionCombo categoryOptionCombo,
        Map<Object, Integer> periodMapping,
        Map<Object, Integer> sourceMapping )
    {
        this.batchHandler = batchHandler;
        this.dataElementService = dataElementService;
        this.importObjectService = importObjectService;
        this.categoryCombo = categoryCombo;
        this.dataValueBatchHandler = dataValueBatchHandler;
        this.categoryOptionCombo = categoryOptionCombo;
        this.periodMapping = periodMapping;
        this.sourceMapping = sourceMapping;
    }
    
    // -------------------------------------------------------------------------
    // IXFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<DataElement> dataElements = params.getDataElements();
        
        if ( dataElements != null && dataElements.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );

            for ( DataElement element : dataElements )
            {
                writer.openElement( ELEMENT_NAME );
    
                writer.openElement( FIELD_DEFINITION, "key", element.getUuid(), 
                    "keyNameSpace", "http://www.unaids.org/ns/cris-ext" );                
    
                writer.writeElement( FIELD_NAME, element.getName(), "lang", "en" );
    
                if ( element.getDescription() != null )
                {
                    writer.writeElement( FIELD_DESCRIPTION, element.getDescription(), "lang", "en" );
                }
    
                writer.writeElement( FIELD_TYPE, "count" );
                
                writer.closeElement( FIELD_DEFINITION );
    
                // -------------------------------------------------------------
                // Data values are embedded in the Indicator collection
                // -------------------------------------------------------------
                
                Collection<DataValue> values = dataValueService.getDataValues( element, params.getPeriods(),
                    params.getOrganisationUnits() );
                
                dataConverter = new DataConverter( values );
                
                dataConverter.write( writer, params );
                
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
            
            reader.moveToStartElement( FIELD_DEFINITION );
            
            element.setUuid( reader.getAttributeValue( "key" ) );
            
            reader.moveToStartElement( FIELD_NAME );            
            element.setName( reader.getElementValue() );                      
            element.setShortName( subString( element.getName(), 0, 20 ) );
            
            reader.moveToStartElement( FIELD_DESCRIPTION );            
            element.setDescription( reader.getElementValue() );
            
            element.setActive( true );            
            element.setType( DataElement.TYPE_INT );            
            element.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
            
            element.setCategoryCombo( categoryCombo );

            read( element, DataElement.class, GroupMemberType.NONE, params );

            // -----------------------------------------------------------------
            // Data values are embedded in the Indicator collection
            // -----------------------------------------------------------------
            
            dataConverter = new DataConverter( dataValueBatchHandler, 
                importObjectService,
                params,
                element,
                categoryOptionCombo,
                periodMapping,
                sourceMapping );
            
            dataConverter.read( reader, params );
        }
    }

    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------
    
    @Override
    protected void importUnique( DataElement object )
    {
        dataElementService.addDataElement( object );
    }    
}
