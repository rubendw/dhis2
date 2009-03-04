package org.hisp.dhis.datadictionary;

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

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ExtendedDataElement
    implements Serializable
{
    public static final String TYPE_DATAELEMENT = "DataElement";
    public static final String TYPE_CONCEPT = "Concept";
    
    public static final String STATUS_CURRENT = "Current";
    public static final String STATUS_PAST = "Past";
    
    public static final String DATATYPE_ALPHABETIC = "Alphabetic";
    public static final String DATATYPE_ALPHANUMERIC = "AlphaNumeric";
    public static final String DATATYPE_NUMERIC = "Numeric";
    
    public static final String REPRESENTATIONAL_FORM_TEXT = "Text";
    public static final String REPRESENTATIONAL_FORM_CODE = "Code";
    public static final String REPRESENTATIONAL_FORM_QUANTITATIVE = "Quantitative";
    public static final String REPRESENTATIONAL_FORM_DATE = "Date";
    public static final String REPRESENTATIONAL_FORM_IDENTIFICATION = "Identification";

    // -------------------------------------------------------------------------
    // Identifier
    // -------------------------------------------------------------------------

    private int id;
    
    // -------------------------------------------------------------------------
    // Identifying and Definitional attributes 
    // -------------------------------------------------------------------------

    private String mnemonic;

    private String version;

    private String context;
    
    private String synonyms;
    
    private String hononyms;
    
    private String keywords;

    private String status;
    
    private Date statusDate;
    
    private String dataElementType;
    
    // -------------------------------------------------------------------------
    // Relational and Representational attributes
    // -------------------------------------------------------------------------

    private String dataType;
    
    private String representationalForm;
    
    private String representationalLayout;
    
    private Integer minimumSize;
    
    private Integer maximumSize;
    
    private String dataDomain;
    
    private String validationRules;
    
    private String relatedDataReferences;
    
    private String guideForUse;
    
    private String collectionMethods;

    // -------------------------------------------------------------------------
    // Administrative attributes 
    // -------------------------------------------------------------------------

    private String responsibleAuthority;
    
    private String updateRules;
    
    private String accessAuthority;
    
    private String updateFrequency;
    
    private String location;
    
    private String reportingMethods;
    
    private String versionStatus;
    
    private String previousVersionReferences;
    
    private String sourceDocument;
    
    private String sourceOrganisation;
    
    private String comment;
    
    private Date saved;
    
    private Date lastUpdated;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ExtendedDataElement()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean isNull()
    {
        return ( mnemonic == null || mnemonic.trim().length() == 0 ); // Not-null-property
    }
    
    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        return super.equals( o );
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }
    
    public String getAccessAuthority()
    {
        return accessAuthority;
    }

    public void setAccessAuthority( String accessAuthority )
    {
        this.accessAuthority = accessAuthority;
    }

    public String getCollectionMethods()
    {
        return collectionMethods;
    }

    public void setCollectionMethods( String collectionMethods )
    {
        this.collectionMethods = collectionMethods;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public String getDataDomain()
    {
        return dataDomain;
    }

    public void setDataDomain( String dataDomain )
    {
        this.dataDomain = dataDomain;
    }

    public String getDataElementType()
    {
        return dataElementType;
    }

    public void setDataElementType( String dataElementType )
    {
        this.dataElementType = dataElementType;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType( String dataType )
    {
        this.dataType = dataType;
    }

    public String getGuideForUse()
    {
        return guideForUse;
    }

    public void setGuideForUse( String guideForUse )
    {
        this.guideForUse = guideForUse;
    }

    public String getHononyms()
    {
        return hononyms;
    }

    public void setHononyms( String hononyms )
    {
        this.hononyms = hononyms;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public void setKeywords( String keywords )
    {
        this.keywords = keywords;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
    {
        this.location = location;
    }

    public Integer getMaximumSize()
    {
        return maximumSize;
    }

    public void setMaximumSize( Integer maximumSize )
    {
        this.maximumSize = maximumSize;
    }

    public Integer getMinimumSize()
    {
        return minimumSize;
    }

    public void setMinimumSize( Integer minimumSize )
    {
        this.minimumSize = minimumSize;
    }

    public String getMnemonic()
    {
        return mnemonic;
    }

    public void setMnemonic( String mnemonic )
    {
        this.mnemonic = mnemonic;
    }

    public String getPreviousVersionReferences()
    {
        return previousVersionReferences;
    }

    public void setPreviousVersionReferences( String previousVersionReferences )
    {
        this.previousVersionReferences = previousVersionReferences;
    }

    public String getRelatedDataReferences()
    {
        return relatedDataReferences;
    }

    public void setRelatedDataReferences( String relatedDataReferences )
    {
        this.relatedDataReferences = relatedDataReferences;
    }

    public String getReportingMethods()
    {
        return reportingMethods;
    }

    public void setReportingMethods( String reportingMethods )
    {
        this.reportingMethods = reportingMethods;
    }

    public String getRepresentationalForm()
    {
        return representationalForm;
    }

    public void setRepresentationalForm( String representationalForm )
    {
        this.representationalForm = representationalForm;
    }

    public String getRepresentationalLayout()
    {
        return representationalLayout;
    }

    public void setRepresentationalLayout( String representationalLayout )
    {
        this.representationalLayout = representationalLayout;
    }

    public String getResponsibleAuthority()
    {
        return responsibleAuthority;
    }

    public void setResponsibleAuthority( String responsibleAuthority )
    {
        this.responsibleAuthority = responsibleAuthority;
    }

    public Date getSaved()
    {
        return saved;
    }

    public void setSaved( Date saved )
    {
        this.saved = saved;
    }

    public String getSourceDocument()
    {
        return sourceDocument;
    }

    public void setSourceDocument( String sourceDocument )
    {
        this.sourceDocument = sourceDocument;
    }

    public String getSourceOrganisation()
    {
        return sourceOrganisation;
    }

    public void setSourceOrganisation( String sourceOrganisation )
    {
        this.sourceOrganisation = sourceOrganisation;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public Date getStatusDate()
    {
        return statusDate;
    }

    public void setStatusDate( Date statusDate )
    {
        this.statusDate = statusDate;
    }

    public String getSynonyms()
    {
        return synonyms;
    }

    public void setSynonyms( String synonyms )
    {
        this.synonyms = synonyms;
    }

    public String getUpdateFrequency()
    {
        return updateFrequency;
    }

    public void setUpdateFrequency( String updateFrequency )
    {
        this.updateFrequency = updateFrequency;
    }

    public String getUpdateRules()
    {
        return updateRules;
    }

    public void setUpdateRules( String updateRules )
    {
        this.updateRules = updateRules;
    }

    public String getValidationRules()
    {
        return validationRules;
    }

    public void setValidationRules( String validationRules )
    {
        this.validationRules = validationRules;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getVersionStatus()
    {
        return versionStatus;
    }

    public void setVersionStatus( String versionStatus )
    {
        this.versionStatus = versionStatus;
    }
}
