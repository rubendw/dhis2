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
import java.util.Map;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractDataValueConverter;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataConverter
    extends AbstractDataValueConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "indicator";
    public static final String ELEMENT_NAME = "data";
    
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_PERIOD = "timePeriod-key";
    private static final String FIELD_SOURCE = "source-key";
    private static final String FIELD_COMMENT = "comment";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------
    
    private Collection<DataValue> dataValues;
    
    private DataElement dataElement;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private Map<Object, Integer> periodMapping;
    
    private Map<Object, Integer> sourceMapping;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    /**
     * Constructor for write operations.s
     */
    public DataConverter( Collection<DataValue> dataValues )
    {
        this.dataValues = dataValues;
    }
    
    /**
     * Constructor for read operations;
     */
    public DataConverter( BatchHandler batchHandler,
        ImportObjectService importObjectService,
        ImportParams params,
        DataElement dataElement,
        DataElementCategoryOptionCombo categoryOptionCombo,
        Map<Object, Integer> periodMapping,
        Map<Object, Integer> sourceMapping )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.params = params;
        this.dataElement = dataElement;
        this.categoryOptionCombo = categoryOptionCombo;
        this.periodMapping = periodMapping;
        this.sourceMapping = sourceMapping;
    }

    // -------------------------------------------------------------------------
    // IXFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        if ( params.isIncludeDataValues() )
        {
            for ( DataValue value : dataValues )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_VALUE, String.valueOf( value.getValue() ) );
                
                writer.writeElement( FIELD_PERIOD, String.valueOf( value.getPeriod().getId() ) );
                
                OrganisationUnit unit = (OrganisationUnit) value.getSource();
                
                writer.writeElement( FIELD_SOURCE, unit.getUuid() );
                
                writer.writeElement( FIELD_COMMENT, value.getComment() );
                
                writer.closeElement( ELEMENT_NAME );
            }
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            DataValue value = new DataValue();

            value.setDataElement( dataElement );
            
            value.setOptionCombo( categoryOptionCombo );

            Period period = new Period();          
            value.setPeriod( period );
            
            Source source = new OrganisationUnit();
            value.setSource( source );

            reader.moveToStartElement( FIELD_VALUE );
            value.setValue( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_PERIOD );                        
            value.getPeriod().setId( periodMapping.get( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_SOURCE );
            value.getSource().setId( sourceMapping.get( reader.getElementValue() ) );
            
            read( value, DataValue.class, GroupMemberType.NONE, params );
        }
    }
}
