package org.hisp.dhis.dd.action.extendeddataelement;

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

import java.util.Date;

import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18nFormat;

import com.opensymphony.xwork.ActionSupport;

import static org.hisp.dhis.system.util.TextUtils.nullIfEmpty;

public class AddExtendedDataElementAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String alternativeName;

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String code;

    public void setCode( String code )
    {
        this.code = code;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private String aggregationOperator;

    public void setAggregationOperator( String aggregationOperator )
    {
        this.aggregationOperator = aggregationOperator;
    }

    private Integer selectedCategoryComboId;
    
    public void setSelectedCategoryComboId( Integer selectedCategoryComboId )
    {
        this.selectedCategoryComboId = selectedCategoryComboId;
    }
    
    // -------------------------------------------------------------------------
    // Identifying and Definitional attributes 
    // -------------------------------------------------------------------------
    
    private String mnemonic;

    public void setMnemonic( String mnemonic )
    {
        this.mnemonic = mnemonic;
    }    
    
    private String version;

    public void setVersion( String version )
    {
        this.version = version;
    }
    
    private String context;

    public void setContext( String context )
    {
        this.context = context;
    }    

    private String synonyms;

    public void setSynonyms( String synonyms )
    {
        this.synonyms = synonyms;
    }
    
    private String hononyms;

    public void setHononyms( String hononyms )
    {
        this.hononyms = hononyms;
    }
    
    private String keywords;

    public void setKeywords( String keywords )
    {
        this.keywords = keywords;
    }
    
    private String status;

    public void setStatus( String status )
    {
        this.status = status;
    }
    
    private String statusDate;

    public void setStatusDate( String statusDate )
    {
        this.statusDate = statusDate;
    }
    
    private String dataElementType;

    public void setDataElementType( String dataElementType )
    {
        this.dataElementType = dataElementType;
    }

    // -------------------------------------------------------------------------
    // Relational and Representational attributes
    // -------------------------------------------------------------------------

    private String dataType;

    public void setDataType( String dataType )
    {
        this.dataType = dataType;
    }

    private String representationalForm;

    public void setRepresentationalForm( String representationalForm )
    {
        this.representationalForm = representationalForm;
    }

    private String representationalLayout;

    public void setRepresentationalLayout( String representationalLayout )
    {
        this.representationalLayout = representationalLayout;
    }

    private Integer minimumSize;

    public void setMinimumSize( Integer minimumSize )
    {
        this.minimumSize = minimumSize;
    }
    
    private Integer maximumSize;

    public void setMaximumSize( Integer maximumSize )
    {
        this.maximumSize = maximumSize;
    }

    private String dataDomain;

    public void setDataDomain( String dataDomain )
    {
        this.dataDomain = dataDomain;
    }

    private String validationRules;

    public void setValidationRules( String validationRules )
    {
        this.validationRules = validationRules;
    }

    private String relatedDataReferences;

    public void setRelatedDataReferences( String relatedDataReferences )
    {
        this.relatedDataReferences = relatedDataReferences;
    }

    private String guideForUse;

    public void setGuideForUse( String guideForUse )
    {
        this.guideForUse = guideForUse;
    }

    private String collectionMethods;

    public void setCollectionMethods( String collectionMethods )
    {
        this.collectionMethods = collectionMethods;
    }

    // -------------------------------------------------------------------------
    // Administrative attributes 
    // -------------------------------------------------------------------------

    private String responsibleAuthority;

    public void setResponsibleAuthority( String responsibleAuthority )
    {
        this.responsibleAuthority = responsibleAuthority;
    }

    private String updateRules;

    public void setUpdateRules( String updateRules )
    {
        this.updateRules = updateRules;
    }

    private String accessAuthority;

    public void setAccessAuthority( String accessAuthority )
    {
        this.accessAuthority = accessAuthority;
    }

    private String updateFrequency;

    public void setUpdateFrequency( String updateFrequency )
    {
        this.updateFrequency = updateFrequency;
    }

    private String location;

    public void setLocation( String location )
    {
        this.location = location;
    }

    private String reportingMethods;

    public void setReportingMethods( String reportingMethods )
    {
        this.reportingMethods = reportingMethods;
    }

    private String versionStatus;

    public void setVersionStatus( String versionStatus )
    {
        this.versionStatus = versionStatus;
    }

    private String previousVersionReferences;

    public void setPreviousVersionReferences( String previousVersionReferences )
    {
        this.previousVersionReferences = previousVersionReferences;
    }

    private String sourceDocument;

    public void setSourceDocument( String sourceDocument )
    {
        this.sourceDocument = sourceDocument;
    }

    private String sourceOrganisation;

    public void setSourceOrganisation( String sourceOrganisation )
    {
        this.sourceOrganisation = sourceOrganisation;
    }

    private String comment;

    public void setComment( String comment )
    {
        this.comment = comment;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {        
        DataElementCategoryCombo categoryCombo = dataElementCategoryComboService.getDataElementCategoryCombo( selectedCategoryComboId );
        
        DataElement dataElement = new DataElement();

        // -------------------------------------------------------------------------
        // DataElement
        // -------------------------------------------------------------------------

        dataElement.setName( name );        
        dataElement.setAlternativeName( nullIfEmpty( alternativeName ) );
        dataElement.setShortName( shortName );
        dataElement.setCode( nullIfEmpty( code ) );
        dataElement.setDescription( nullIfEmpty( description ) );
        dataElement.setActive( true );
        dataElement.setType( type );
        dataElement.setAggregationOperator( aggregationOperator );
        dataElement.setCategoryCombo( categoryCombo );

        ExtendedDataElement extendedDataElement = new ExtendedDataElement();
        
        // -------------------------------------------------------------------------
        // Identifying and Definitional attributes 
        // -------------------------------------------------------------------------
        
        extendedDataElement.setMnemonic( nullIfEmpty( mnemonic ) );
        extendedDataElement.setVersion( nullIfEmpty( version ) );
        extendedDataElement.setContext( nullIfEmpty( context ) );
        extendedDataElement.setSynonyms( nullIfEmpty( synonyms ) );
        extendedDataElement.setHononyms( nullIfEmpty( hononyms ) );
        extendedDataElement.setKeywords( nullIfEmpty( keywords ) );
        extendedDataElement.setStatus( nullIfEmpty( status ) );
        extendedDataElement.setStatusDate( format.parseDate( statusDate ) );
        extendedDataElement.setDataElementType( nullIfEmpty( dataElementType ) );
        
        // -------------------------------------------------------------------------
        // Relational and Representational attributes
        // -------------------------------------------------------------------------

        extendedDataElement.setDataType( nullIfEmpty( dataType ) );
        extendedDataElement.setRepresentationalForm( nullIfEmpty( representationalForm ) );
        extendedDataElement.setRepresentationalLayout( nullIfEmpty( representationalLayout ) );
        extendedDataElement.setMinimumSize( minimumSize );
        extendedDataElement.setMaximumSize( maximumSize );
        extendedDataElement.setDataDomain( nullIfEmpty( dataDomain ) );
        extendedDataElement.setValidationRules( nullIfEmpty( validationRules ) );
        extendedDataElement.setRelatedDataReferences( nullIfEmpty( relatedDataReferences ) );
        extendedDataElement.setGuideForUse( nullIfEmpty( guideForUse ) );
        extendedDataElement.setCollectionMethods( nullIfEmpty( collectionMethods ) );

        // -------------------------------------------------------------------------
        // Administrative attributes 
        // -------------------------------------------------------------------------

        extendedDataElement.setResponsibleAuthority( nullIfEmpty( responsibleAuthority ) );
        extendedDataElement.setUpdateRules( nullIfEmpty( updateRules ) );
        extendedDataElement.setAccessAuthority( nullIfEmpty( accessAuthority ) );
        extendedDataElement.setUpdateFrequency( nullIfEmpty( updateFrequency ) );
        extendedDataElement.setLocation( nullIfEmpty( location ) );
        extendedDataElement.setReportingMethods( nullIfEmpty( reportingMethods ) );
        extendedDataElement.setVersionStatus( nullIfEmpty( versionStatus ) );
        extendedDataElement.setPreviousVersionReferences( nullIfEmpty( previousVersionReferences ) );
        extendedDataElement.setSourceDocument( nullIfEmpty( sourceDocument ) );
        extendedDataElement.setSourceOrganisation( nullIfEmpty( sourceOrganisation ) );
        extendedDataElement.setComment( nullIfEmpty( comment ) );
        extendedDataElement.setSaved( new Date() );
        extendedDataElement.setLastUpdated( new Date() );
        
        dataElement.setExtended( extendedDataElement );
        
        dataElementService.addDataElement( dataElement );

        return SUCCESS;
    }
}
