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
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractDataValueConverter;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.system.util.DateUtils;

/**
 * @author Lars Helge Overland
 * @version $Id: DataValueConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class DataValueConverter
    extends AbstractDataValueConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "dataValues";
    public static final String ELEMENT_NAME = "dataValue";
    
    private static final String FIELD_DATAELEMENT = "dataElement";
    private static final String FIELD_PERIOD = "period";
    private static final String FIELD_SOURCE = "source";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_STOREDBY = "storedBy";
    private static final String FIELD_TIMESTAMP = "timeStamp";
    private static final String FIELD_COMMENT = "comment";
    private static final String FIELD_CATEGORY_OPTION_COMBO = "categoryOptionCombo";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private Map<Object, Integer> dataElementMapping;    
    private Map<Object, Integer> periodMapping;    
    private Map<Object, Integer> sourceMapping;
    private Map<Object, Integer> categoryOptionComboMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DataValueConverter( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;    
    }
    
    /**
     * Constructor for read operations.
     */
    public DataValueConverter( BatchHandler batchHandler,
        BatchHandler importDataValueBatchHandler,
        DataValueService dataValueService,
        ImportObjectService importObjectService,
        ImportParams params,
        Map<Object, Integer> dataElementMapping,
        Map<Object, Integer> periodMapping,
        Map<Object, Integer> sourceMapping,
        Map<Object, Integer> categoryOptionComboMapping )
    {
        this.batchHandler = batchHandler;
        this.importDataValueBatchHandler = importDataValueBatchHandler;
        this.dataValueService = dataValueService;
        this.importObjectService = importObjectService;
        this.params = params;
        this.dataElementMapping = dataElementMapping;
        this.periodMapping = periodMapping;
        this.sourceMapping = sourceMapping;
        this.categoryOptionComboMapping = categoryOptionComboMapping;
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {        
        Collection<DataValue> values = null;
        
        if ( params.isIncludeDataValues() )
        {        
            if ( params.getDataElements().size() > 0 && params.getPeriods().size() > 0 && params.getOrganisationUnits().size() > 0 )
            {
                writer.openElement( COLLECTION_NAME );
                
                for ( DataElement element : params.getDataElements() )
                {
                    values = dataValueService.getDataValues( element, params.getPeriods(), params.getOrganisationUnits() );
                    
                    for ( DataValue value : values )
                    {   
                        writer.openElement( ELEMENT_NAME );
                        
                        writer.writeElement( FIELD_DATAELEMENT, String.valueOf( value.getDataElement().getId() ) );
                        writer.writeElement( FIELD_PERIOD, String.valueOf( value.getPeriod().getId() ) );
                        writer.writeElement( FIELD_SOURCE, String.valueOf( value.getSource().getId() ) );
                        writer.writeElement( FIELD_VALUE, value.getValue() );
                        writer.writeElement( FIELD_STOREDBY, value.getStoredBy() );
                        writer.writeElement( FIELD_TIMESTAMP, DateUtils.getMediumDateString( value.getTimestamp() ) );
                        writer.writeElement( FIELD_COMMENT, value.getComment() );
                        writer.writeElement( FIELD_CATEGORY_OPTION_COMBO, String.valueOf( value.getOptionCombo().getId() ) );
                        
                        writer.closeElement( ELEMENT_NAME );
                    }
                }
                
                writer.closeElement( COLLECTION_NAME );
            }
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {      
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            DataValue value = new DataValue();
            
            DataElement element = new DataElement();
            value.setDataElement( element );

            Period period = new Period();          
            value.setPeriod( period );
            
            Source source = new OrganisationUnit();
            value.setSource( source );

            DataElementCategoryOptionCombo categoryOptionCombo = new DataElementCategoryOptionCombo();
            value.setOptionCombo( categoryOptionCombo );
            
            reader.moveToStartElement( FIELD_DATAELEMENT );  
            String de = reader.getElementValue();
            value.getDataElement().setId( dataElementMapping.get( Integer.parseInt( de ) ) );

            reader.moveToStartElement( FIELD_PERIOD );
            String pe = reader.getElementValue();
            value.getPeriod().setId( periodMapping.get( Integer.parseInt( pe ) ) );
            
            reader.moveToStartElement( FIELD_SOURCE );
            String so = reader.getElementValue();
            value.getSource().setId( sourceMapping.get( Integer.parseInt( so ) ) );
            
            reader.moveToStartElement( FIELD_VALUE );
            value.setValue( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_STOREDBY );
            value.setStoredBy( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_TIMESTAMP );
            value.setTimestamp( DateUtils.getMediumDate( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_COMMENT );
            value.setComment( reader.getElementValue() );
                        
            reader.moveToStartElement( FIELD_CATEGORY_OPTION_COMBO );
            String op = reader.getElementValue();
            value.getOptionCombo().setId( categoryOptionComboMapping.get( Integer.parseInt( op ) ) );
            
            read( value, DataValue.class, GroupMemberType.NONE, params );
        }
    }
}
