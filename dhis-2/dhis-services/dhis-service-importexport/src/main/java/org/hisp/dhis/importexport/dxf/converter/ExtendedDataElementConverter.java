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

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractExtendedDataElementConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.system.util.DateUtils;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: ExtendedDataElementConverter.java 5896 2008-10-11 20:49:24Z larshelg $
 */
public class ExtendedDataElementConverter
    extends AbstractExtendedDataElementConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "extendedDataElements";
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

    private Map<Object, Integer> categoryComboMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    /**
     * Constructor for write operations.
     */
    public ExtendedDataElementConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     *
     * @param batchHandler the batchHandler to use.
     * @param extendedDataElementBatchHandler the extendedDataElementBatchHandler to use.
     * @param importObjectService the importObjectService to use.
     * @param categoryComboMapping the categoryComboMapping to use.
     * @param dataElementService the dataElementService to use.
     */
    public ExtendedDataElementConverter( BatchHandler batchHandler,
        BatchHandler extendedDataElementBatchHandler, 
        ImportObjectService importObjectService,
        Map<Object, Integer> categoryComboMapping, 
        DataElementService dataElementService )
    {
        this.batchHandler = batchHandler;
        this.extendedDataElementBatchHandler = extendedDataElementBatchHandler;
        this.importObjectService = importObjectService;
        this.categoryComboMapping = categoryComboMapping;
        this.dataElementService = dataElementService;
    }    

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<DataElement> dataElements = params.getDataElements();
        
        if ( dataElements != null && dataElements.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( DataElement element : dataElements )
            {
                if ( element.getExtended() == null )
                {
                    element.setExtended( new ExtendedDataElement() );
                }
                
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
                
                writer.writeElement( FIELD_MNEMONIC, element.getExtended().getMnemonic() );
                writer.writeElement( FIELD_VERSION, element.getExtended().getVersion() );
                writer.writeElement( FIELD_CONTEXT, element.getExtended().getContext() );
                writer.writeElement( FIELD_SYNONYMS, element.getExtended().getSynonyms() );
                writer.writeElement( FIELD_HONONYMS, element.getExtended().getHononyms() );
                writer.writeElement( FIELD_KEYWORDS, element.getExtended().getKeywords() );
                writer.writeElement( FIELD_STATUS, element.getExtended().getStatus() );
                writer.writeElement( FIELD_STATUS_DATE, DateUtils.getMediumDateString( element.getExtended().getStatusDate() ) );
                writer.writeElement( FIELD_DATAELEMENT_TYPE, element.getExtended().getDataElementType() );
                
                writer.writeElement( FIELD_DATA_TYPE, element.getExtended().getDataType() );
                writer.writeElement( FIELD_REPRESENTATIONAL_FORM, element.getExtended().getRepresentationalForm() );
                writer.writeElement( FIELD_REPRESENTATIONAL_LAYOUT, element.getExtended().getRepresentationalLayout() );
                writer.writeElement( FIELD_MINIMUM_SIZE, valueOf( element.getExtended().getMinimumSize() ) );
                writer.writeElement( FIELD_MAXIMUM_SIZE, valueOf( element.getExtended().getMaximumSize() ) );
                writer.writeElement( FIELD_DATA_DOMAIN, element.getExtended().getDataDomain() );
                writer.writeElement( FIELD_VALIDATION_RULES, element.getExtended().getValidationRules() );
                writer.writeElement( FIELD_RELATED_DATA_REFERENCES, element.getExtended().getRelatedDataReferences() );
                writer.writeElement( FIELD_GUIDE_FOR_USE, element.getExtended().getGuideForUse() );
                writer.writeElement( FIELD_COLLECTION_METHODS, element.getExtended().getCollectionMethods() );
                
                writer.writeElement( FIELD_RESPONSIBLE_AUTHORITY, element.getExtended().getResponsibleAuthority() );
                writer.writeElement( FIELD_UPDATE_RULES, element.getExtended().getUpdateRules() );
                writer.writeElement( FIELD_ACCESS_AUTHORITY, element.getExtended().getAccessAuthority() );
                writer.writeElement( FIELD_UPDATE_FREQUENCY, element.getExtended().getUpdateFrequency() );
                writer.writeElement( FIELD_LOCATION, element.getExtended().getLocation() );
                writer.writeElement( FIELD_REPORTING_METHODS, element.getExtended().getReportingMethods() );
                writer.writeElement( FIELD_VERSION_STATUS, element.getExtended().getVersionStatus() );
                writer.writeElement( FIELD_PREVIOUS_VERSION_REFERENCES, element.getExtended().getPreviousVersionReferences() );
                writer.writeElement( FIELD_SOURCE_DOCUMENT, element.getExtended().getSourceDocument() );
                writer.writeElement( FIELD_SOURCE_ORGANISATION, element.getExtended().getSourceOrganisation() );
                writer.writeElement( FIELD_COMMENT, element.getExtended().getComment() );
                writer.writeElement( FIELD_SAVED, DateUtils.getMediumDateString( element.getExtended().getSaved() ) );
                writer.writeElement( FIELD_LAST_UPDATED, DateUtils.getMediumDateString( element.getExtended().getLastUpdated() ) );
                
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
            
            // -----------------------------------------------------------------
            // Regular attributes
            // -----------------------------------------------------------------

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

            element.setExtended( extended.isNull() ? null : extended );
            
            NameMappingUtil.addDataElementMapping( element.getId(), element.getName() );
            
            read( element, DataElement.class, GroupMemberType.NONE, params );
        }
    }
}
