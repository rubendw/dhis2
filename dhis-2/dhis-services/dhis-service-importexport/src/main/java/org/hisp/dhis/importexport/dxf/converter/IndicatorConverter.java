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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractIndicatorConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorConverter.java 5801 2008-10-03 06:41:02Z larshelg $
 */
public class IndicatorConverter
    extends AbstractIndicatorConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "indicators";
    public static final String ELEMENT_NAME = "indicator";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_UUID = "uuid";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ALTERNATIVE_NAME = "alternativeName";
    private static final String FIELD_SHORT_NAME = "shortName";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_ANNUALIZED = "annualized";
    private static final String FIELD_INDICATOR_TYPE = "indicatorType";
    private static final String FIELD_NUMERATOR = "numerator";
    private static final String FIELD_NUMERATOR_DESCRIPTION = "numeratorDescription";
    private static final String FIELD_NUMERATOR_AGGREGATION_OPERATOR = "numeratorAggregationOperator";
    private static final String FIELD_DENOMINATOR = "denominator";
    private static final String FIELD_DENOMINATOR_DESCRIPTION = "denominatorDescription";
    private static final String FIELD_DENOMINATOR_AGGREGATION_OPERATOR = "denominatorAggregationOperator";
    
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private Map<Object, Integer> indicatorTypeMapping;
    
    private Map<Object, Integer> dataElementMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public IndicatorConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param indicatorService the indicatorService to use.
     * @param importObjectService the importObjectService to use.
     */
    public IndicatorConverter( BatchHandler batchHandler, ImportObjectService importObjectService, 
        IndicatorService indicatorService, Map<Object, Integer> indicatorTypeMapping, 
        Map<Object, Integer> dataElementMapping )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.indicatorService = indicatorService;
        this.indicatorTypeMapping = indicatorTypeMapping;
        this.dataElementMapping = dataElementMapping;
    }    

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<Indicator> indicators = params.getIndicators();
        
        if ( indicators != null && indicators.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( Indicator indicator : indicators )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_ID, String.valueOf( indicator.getId() ) );
                writer.writeElement( FIELD_UUID, indicator.getUuid() );
                writer.writeElement( FIELD_NAME, indicator.getName() );
                writer.writeElement( FIELD_ALTERNATIVE_NAME, indicator.getAlternativeName() );
                writer.writeElement( FIELD_SHORT_NAME, indicator.getShortName() );
                writer.writeElement( FIELD_CODE, indicator.getCode() );
                writer.writeElement( FIELD_DESCRIPTION, indicator.getDescription() );
                writer.writeElement( FIELD_ANNUALIZED, String.valueOf( indicator.getAnnualized() ) );
                writer.writeElement( FIELD_INDICATOR_TYPE, String.valueOf( indicator.getIndicatorType().getId() ) );
                writer.writeElement( FIELD_NUMERATOR, indicator.getNumerator() );
                writer.writeElement( FIELD_NUMERATOR_DESCRIPTION, indicator.getNumeratorDescription() );
                writer.writeElement( FIELD_NUMERATOR_AGGREGATION_OPERATOR, indicator.getNumeratorAggregationOperator() );
                writer.writeElement( FIELD_DENOMINATOR, indicator.getDenominator() );
                writer.writeElement( FIELD_DENOMINATOR_DESCRIPTION, indicator.getDenominatorDescription() );
                writer.writeElement( FIELD_DENOMINATOR_AGGREGATION_OPERATOR, indicator.getDenominatorAggregationOperator() );
                            
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            Indicator indicator = new Indicator();
            
            IndicatorType type = new IndicatorType();
            indicator.setIndicatorType( type );
            
            reader.moveToStartElement( FIELD_ID );
            indicator.setId( Integer.parseInt( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_UUID );            
            indicator.setUuid( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_NAME );
            indicator.setName( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_ALTERNATIVE_NAME );
            indicator.setAlternativeName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_SHORT_NAME );
            indicator.setShortName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_CODE );
            indicator.setCode( reader.getElementValue() );

            reader.moveToStartElement( FIELD_DESCRIPTION );
            indicator.setDescription( reader.getElementValue() );

            reader.moveToStartElement( FIELD_ANNUALIZED );
            indicator.setAnnualized( Boolean.parseBoolean( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_INDICATOR_TYPE );
            indicator.getIndicatorType().setId( indicatorTypeMapping.get( Integer.parseInt( reader.getElementValue() ) ) );
            
            reader.moveToStartElement( FIELD_NUMERATOR );
            indicator.setNumerator( convertFormula( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_NUMERATOR_DESCRIPTION );
            indicator.setNumeratorDescription( reader.getElementValue() );

            reader.moveToStartElement( FIELD_NUMERATOR_AGGREGATION_OPERATOR );
            indicator.setNumeratorAggregationOperator( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_DENOMINATOR );
            indicator.setDenominator( convertFormula( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_DENOMINATOR_DESCRIPTION );
            indicator.setDenominatorDescription( reader.getElementValue() );

            reader.moveToStartElement( FIELD_DENOMINATOR_AGGREGATION_OPERATOR );
            indicator.setDenominatorAggregationOperator( reader.getElementValue() );
            
            NameMappingUtil.addIndicatorMapping( indicator.getId(), indicator.getName() );
            
            read( indicator, Indicator.class, GroupMemberType.NONE, params );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    //TODO move to util class
    
    private String convertFormula( String formula )
        throws IllegalArgumentException
    {
        if ( formula != null )
        {
            // ---------------------------------------------------------------------
            // Extract DataElement identifiers on the form "[123]" from the formula
            // ---------------------------------------------------------------------
    
            Pattern pattern = Pattern.compile( "\\[\\d+\\]" );
            Matcher matcher = pattern.matcher( formula );
            
            StringBuffer convertedFormula = new StringBuffer();
            
            while ( matcher.find() )
            {
                String replaceString = matcher.group();
    
                // -----------------------------------------------------------------
                // Remove brackets to get identifier
                // -----------------------------------------------------------------
    
                String id = replaceString.replaceAll( "[\\[\\]]", "" );
    
                // -----------------------------------------------------------------
                // Parse identifier to int
                // -----------------------------------------------------------------
    
                int dataElementId = -1;
                
                try
                {
                    dataElementId = Integer.parseInt( id );
                }
                catch ( NumberFormatException ex )
                {
                    throw new IllegalArgumentException( "Illegal identifier in formula - " + replaceString, ex );
                }
    
                // -----------------------------------------------------------------
                // Get identifier from the DataElement in the database
                // -----------------------------------------------------------------
    
                int convertedDataElementId = dataElementMapping.get( dataElementId );
                
                // -----------------------------------------------------------------
                // Put brackets back on
                // -----------------------------------------------------------------
    
                replaceString = "[" + convertedDataElementId + "]";
                
                matcher.appendReplacement( convertedFormula, replaceString );
            }
            
            matcher.appendTail( convertedFormula );
            
            return convertedFormula.toString();
        }
        
        return null;
    }
}

