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
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractExtendedIndicatorConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.system.util.DateUtils;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: ExtendedIndicatorConverter.java 5896 2008-10-11 20:49:24Z larshelg $
 */
public class ExtendedIndicatorConverter
    extends AbstractExtendedIndicatorConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "extendedIndicators";
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

    private static final String FIELD_MNEMONIC = "mnemonic";
    private static final String FIELD_VERSION = "version";
    private static final String FIELD_CONTEXT = "context";
    private static final String FIELD_SYNONYMS = "synonyms";
    private static final String FIELD_HONONYMS = "hononyms";
    private static final String FIELD_KEYWORDS = "keywords";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_STATUS_DATE = "statusDate";
    private static final String FIELD_DATAELEMENT_TYPE = "dataElementType";
    
    private static final String FIELD_DATA_TYPE = "dataType";
    private static final String FIELD_REPRESENTATIONAL_FORM = "representationalForm";
    private static final String FIELD_REPRESENTATIONAL_LAYOUT = "representationalLayout";
    private static final String FIELD_MINIMUM_SIZE = "minimumSize";
    private static final String FIELD_MAXIMUM_SIZE = "maximumSize";
    private static final String FIELD_DATA_DOMAIN = "dataDomain";
    private static final String FIELD_VALIDATION_RULES = "validationRules";
    private static final String FIELD_RELATED_DATA_REFERENCES = "relatedDataReferences";
    private static final String FIELD_GUIDE_FOR_USE = "guideForUse";
    private static final String FIELD_COLLECTION_METHODS = "collectionMethods";
    
    private static final String FIELD_RESPONSIBLE_AUTHORITY = "responsibleAuthority";
    private static final String FIELD_UPDATE_RULES = "updateRules";
    private static final String FIELD_ACCESS_AUTHORITY = "accessAuthority";
    private static final String FIELD_UPDATE_FREQUENCY = "updateFrequency";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_REPORTING_METHODS = "reportingMethods";
    private static final String FIELD_VERSION_STATUS = "versionStatus";
    private static final String FIELD_PREVIOUS_VERSION_REFERENCES = "previousVersionReferences";
    private static final String FIELD_SOURCE_DOCUMENT = "sourceDocument";
    private static final String FIELD_SOURCE_ORGANISATION = "sourceOrganisation";
    private static final String FIELD_COMMENT = "comment";
    private static final String FIELD_SAVED = "saved";
    private static final String FIELD_LAST_UPDATED = "lastUpdated";
    
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
    public ExtendedIndicatorConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param indicatorService the indicatorService to use.
     * @param importObjectService the importObjectService to use.
     */
    public ExtendedIndicatorConverter( BatchHandler batchHandler,
        BatchHandler extendedDataElementBatchHandler,
        ImportObjectService importObjectService, 
        IndicatorService indicatorService, 
        Map<Object, Integer> indicatorTypeMapping, 
        Map<Object, Integer> dataElementMapping )
    {
        this.batchHandler = batchHandler;
        this.extendedDataElementBatchHandler = extendedDataElementBatchHandler;
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
                if ( indicator.getExtended() == null )
                {
                    indicator.setExtended( new ExtendedDataElement() );
                }
                
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

                writer.writeElement( FIELD_MNEMONIC, indicator.getExtended().getMnemonic() );
                writer.writeElement( FIELD_VERSION, indicator.getExtended().getVersion() );
                writer.writeElement( FIELD_CONTEXT, indicator.getExtended().getContext() );
                writer.writeElement( FIELD_SYNONYMS, indicator.getExtended().getSynonyms() );
                writer.writeElement( FIELD_HONONYMS, indicator.getExtended().getHononyms() );
                writer.writeElement( FIELD_KEYWORDS, indicator.getExtended().getKeywords() );
                writer.writeElement( FIELD_STATUS, indicator.getExtended().getStatus() );
                writer.writeElement( FIELD_STATUS_DATE, DateUtils.getMediumDateString( indicator.getExtended().getStatusDate() ) );
                writer.writeElement( FIELD_DATAELEMENT_TYPE, indicator.getExtended().getDataElementType() );
                
                writer.writeElement( FIELD_DATA_TYPE, indicator.getExtended().getDataType() );
                writer.writeElement( FIELD_REPRESENTATIONAL_FORM, indicator.getExtended().getRepresentationalForm() );
                writer.writeElement( FIELD_REPRESENTATIONAL_LAYOUT, indicator.getExtended().getRepresentationalLayout() );
                writer.writeElement( FIELD_MINIMUM_SIZE, valueOf( indicator.getExtended().getMinimumSize() ) );
                writer.writeElement( FIELD_MAXIMUM_SIZE, valueOf( indicator.getExtended().getMaximumSize() ) );
                writer.writeElement( FIELD_DATA_DOMAIN, indicator.getExtended().getDataDomain() );
                writer.writeElement( FIELD_VALIDATION_RULES, indicator.getExtended().getValidationRules() );
                writer.writeElement( FIELD_RELATED_DATA_REFERENCES, indicator.getExtended().getRelatedDataReferences() );
                writer.writeElement( FIELD_GUIDE_FOR_USE, indicator.getExtended().getGuideForUse() );
                writer.writeElement( FIELD_COLLECTION_METHODS, indicator.getExtended().getCollectionMethods() );
                
                writer.writeElement( FIELD_RESPONSIBLE_AUTHORITY, indicator.getExtended().getResponsibleAuthority() );
                writer.writeElement( FIELD_UPDATE_RULES, indicator.getExtended().getUpdateRules() );
                writer.writeElement( FIELD_ACCESS_AUTHORITY, indicator.getExtended().getAccessAuthority() );
                writer.writeElement( FIELD_UPDATE_FREQUENCY, indicator.getExtended().getUpdateFrequency() );
                writer.writeElement( FIELD_LOCATION, indicator.getExtended().getLocation() );
                writer.writeElement( FIELD_REPORTING_METHODS, indicator.getExtended().getReportingMethods() );
                writer.writeElement( FIELD_VERSION_STATUS, indicator.getExtended().getVersionStatus() );
                writer.writeElement( FIELD_PREVIOUS_VERSION_REFERENCES, indicator.getExtended().getPreviousVersionReferences() );
                writer.writeElement( FIELD_SOURCE_DOCUMENT, indicator.getExtended().getSourceDocument() );
                writer.writeElement( FIELD_SOURCE_ORGANISATION, indicator.getExtended().getSourceOrganisation() );
                writer.writeElement( FIELD_COMMENT, indicator.getExtended().getComment() );
                writer.writeElement( FIELD_SAVED, DateUtils.getMediumDateString( indicator.getExtended().getSaved() ) );
                writer.writeElement( FIELD_LAST_UPDATED, DateUtils.getMediumDateString( indicator.getExtended().getLastUpdated() ) );
                
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

            // -----------------------------------------------------------------
            // Regular attributes
            // -----------------------------------------------------------------

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

            // -----------------------------------------------------------------
            // Identifying and Definitional attributes 
            // -----------------------------------------------------------------

            ExtendedDataElement extended = new ExtendedDataElement();
            
            reader.moveToStartElement( FIELD_MNEMONIC );            
            extended.setMnemonic( reader.getElementValue() );

            reader.moveToStartElement( FIELD_VERSION );            
            extended.setVersion( reader.getElementValue() );

            reader.moveToStartElement( FIELD_CONTEXT );            
            extended.setContext( reader.getElementValue() );

            reader.moveToStartElement( FIELD_SYNONYMS );            
            extended.setSynonyms( reader.getElementValue() );

            reader.moveToStartElement( FIELD_HONONYMS );            
            extended.setHononyms( reader.getElementValue() );

            reader.moveToStartElement( FIELD_KEYWORDS );            
            extended.setKeywords( reader.getElementValue() );

            reader.moveToStartElement( FIELD_STATUS );            
            extended.setStatus( reader.getElementValue() );

            reader.moveToStartElement( FIELD_STATUS_DATE );            
            extended.setStatusDate( DateUtils.getMediumDate( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_DATAELEMENT_TYPE );            
            extended.setDataElementType( reader.getElementValue() );

            // -----------------------------------------------------------------
            // Relational and Representational attributes
            // -----------------------------------------------------------------

            reader.moveToStartElement( FIELD_DATA_TYPE );            
            extended.setDataType( reader.getElementValue() );

            reader.moveToStartElement( FIELD_REPRESENTATIONAL_FORM );            
            extended.setRepresentationalForm( reader.getElementValue() );

            reader.moveToStartElement( FIELD_REPRESENTATIONAL_LAYOUT );            
            extended.setRepresentationalLayout( reader.getElementValue() );

            reader.moveToStartElement( FIELD_MINIMUM_SIZE );            
            extended.setMinimumSize( parseInteger( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_MAXIMUM_SIZE );            
            extended.setMaximumSize( parseInteger( reader.getElementValue() ) );

            reader.moveToStartElement( FIELD_DATA_DOMAIN );            
            extended.setDataDomain( reader.getElementValue() );

            reader.moveToStartElement( FIELD_VALIDATION_RULES );            
            extended.setValidationRules( reader.getElementValue() );

            reader.moveToStartElement( FIELD_RELATED_DATA_REFERENCES );            
            extended.setRelatedDataReferences( reader.getElementValue() );

            reader.moveToStartElement( FIELD_GUIDE_FOR_USE );            
            extended.setGuideForUse( reader.getElementValue() );

            reader.moveToStartElement( FIELD_COLLECTION_METHODS );            
            extended.setCollectionMethods( reader.getElementValue() );

            // -----------------------------------------------------------------
            // Administrative attributes 
            // -----------------------------------------------------------------

            reader.moveToStartElement( FIELD_RESPONSIBLE_AUTHORITY );            
            extended.setResponsibleAuthority( reader.getElementValue() );

            reader.moveToStartElement( FIELD_UPDATE_RULES );            
            extended.setUpdateRules( reader.getElementValue() );

            reader.moveToStartElement( FIELD_ACCESS_AUTHORITY );            
            extended.setAccessAuthority( reader.getElementValue() );

            reader.moveToStartElement( FIELD_UPDATE_FREQUENCY );            
            extended.setUpdateFrequency( reader.getElementValue() );

            reader.moveToStartElement( FIELD_LOCATION );            
            extended.setLocation( reader.getElementValue() );

            reader.moveToStartElement( FIELD_REPORTING_METHODS );            
            extended.setReportingMethods( reader.getElementValue() );

            reader.moveToStartElement( FIELD_VERSION_STATUS );            
            extended.setVersionStatus( reader.getElementValue() );

            reader.moveToStartElement( FIELD_PREVIOUS_VERSION_REFERENCES );            
            extended.setPreviousVersionReferences( reader.getElementValue() );

            reader.moveToStartElement( FIELD_SOURCE_DOCUMENT );            
            extended.setSourceDocument( reader.getElementValue() );            

            reader.moveToStartElement( FIELD_SOURCE_ORGANISATION );            
            extended.setSourceOrganisation( reader.getElementValue() );        

            reader.moveToStartElement( FIELD_COMMENT );            
            extended.setComment( reader.getElementValue() );            

            reader.moveToStartElement( FIELD_SAVED );            
            extended.setSaved( DateUtils.getMediumDate( reader.getElementValue() ) );            

            reader.moveToStartElement( FIELD_LAST_UPDATED );            
            extended.setLastUpdated( DateUtils.getMediumDate( reader.getElementValue() ) );

            // -----------------------------------------------------------------
            // Only set ExtendedDataElement if it contains values
            // -----------------------------------------------------------------

            indicator.setExtended( extended.isNull() ? null : extended );
            
            NameMappingUtil.addIndicatorMapping( indicator.getId(), indicator.getName() );
            
            read( indicator, Indicator.class, GroupMemberType.NONE, params );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private String convertFormula( String formula )
        throws IllegalArgumentException
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
}

