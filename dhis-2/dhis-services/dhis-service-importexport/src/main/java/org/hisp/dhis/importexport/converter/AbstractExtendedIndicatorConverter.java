package org.hisp.dhis.importexport.converter;

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

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractExtendedIndicatorConverter.java 5896 2008-10-11 20:49:24Z larshelg $
 */
public class AbstractExtendedIndicatorConverter
    extends AbstractConverter<Indicator>
{
    protected BatchHandler extendedDataElementBatchHandler;
    
    protected IndicatorService indicatorService;
    
    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------
    
    protected void importUnique( Indicator object )
    {
        ExtendedDataElement extendedIndicator = object.getExtended();

        if ( extendedIndicator != null )
        {
            int id = extendedDataElementBatchHandler.insertObject( extendedIndicator, true );
            
            extendedIndicator.setId( id );
            object.setExtended( extendedIndicator );
        }
        
        batchHandler.addObject( object );      
    }
    
    protected void importMatching( Indicator object, Indicator match )
    {
        match.setName( object.getName() );
        match.setAlternativeName( object.getAlternativeName() );
        match.setShortName( object.getShortName() );
        match.setCode( object.getCode() );
        match.setDescription( object.getDescription() );
        match.setIndicatorType( object.getIndicatorType() );
        match.setNumerator( object.getNumerator() );
        match.setNumeratorDescription( object.getNumeratorDescription() );
        match.setNumeratorAggregationOperator( object.getNumeratorAggregationOperator() );
        match.setDenominator( object.getDenominator() );
        match.setDenominatorDescription( object.getDenominatorDescription() );
        match.setDenominatorAggregationOperator( object.getDenominatorAggregationOperator() );

        // ---------------------------------------------------------------------
        // Update matching if import object has extended dataelement
        // ---------------------------------------------------------------------

        if ( object.getExtended() != null )
        {
            ExtendedDataElement extended = new ExtendedDataElement();

            extended.setMnemonic( object.getExtended().getMnemonic() );
            extended.setVersion( object.getExtended().getVersion() );
            extended.setContext( object.getExtended().getContext() );
            extended.setSynonyms( object.getExtended().getSynonyms() );
            extended.setHononyms( object.getExtended().getHononyms() );
            extended.setKeywords( object.getExtended().getKeywords() );
            extended.setStatus( object.getExtended().getStatus() );
            extended.setStatusDate( object.getExtended().getStatusDate() );
            extended.setDataElementType( object.getExtended().getDataElementType() );
            
            extended.setDataType( object.getExtended().getDataType() );
            extended.setRepresentationalForm( object.getExtended().getRepresentationalForm() );
            extended.setRepresentationalLayout( object.getExtended().getRepresentationalLayout() );
            extended.setMinimumSize( object.getExtended().getMinimumSize() );
            extended.setMaximumSize( object.getExtended().getMaximumSize() );
            extended.setDataDomain( object.getExtended().getDataDomain() );
            extended.setValidationRules( object.getExtended().getValidationRules() );
            extended.setRelatedDataReferences( object.getExtended().getRelatedDataReferences() );
            extended.setGuideForUse( object.getExtended().getGuideForUse() );
            extended.setCollectionMethods( object.getExtended().getCollectionMethods() );
            
            extended.setResponsibleAuthority( object.getExtended().getResponsibleAuthority() );
            extended.setUpdateRules( object.getExtended().getUpdateRules() );
            extended.setAccessAuthority( object.getExtended().getAccessAuthority() );
            extended.setUpdateFrequency( object.getExtended().getUpdateFrequency() );
            extended.setLocation( object.getExtended().getLocation() );
            extended.setReportingMethods( object.getExtended().getReportingMethods() );
            extended.setVersionStatus( object.getExtended().getVersionStatus() );
            extended.setPreviousVersionReferences( object.getExtended().getPreviousVersionReferences() );
            extended.setSourceDocument( object.getExtended().getSourceOrganisation() );
            extended.setSourceOrganisation( object.getExtended().getSourceOrganisation() );
            extended.setComment( object.getExtended().getComment() );
            extended.setSaved( object.getExtended().getSaved() );
            extended.setLastUpdated( object.getExtended().getLastUpdated() );
            
            match.setExtended( extended );
        }
        
        indicatorService.updateIndicator( match );                
    }
    
    protected Indicator getMatching( Indicator object )
    {
        Indicator match = indicatorService.getIndicatorByName( object.getName() );
        
        if ( match == null )
        {
            match = indicatorService.getIndicatorByAlternativeName( object.getAlternativeName() );
        }
        if ( match == null )
        {
            match = indicatorService.getIndicatorByShortName( object.getShortName() );
        }
        if ( match == null )
        {
            match = indicatorService.getIndicatorByCode( object.getCode() );
        }

        if ( match != null && match.getExtended() != null )
        {
            match.getExtended().getMnemonic(); // Dirty loading of extended data element
        }
        
        return match;
    }
    
    protected boolean isIdentical( Indicator object, Indicator existing )
    {
        // ---------------------------------------------------------------------
        // Regular attributes
        // ---------------------------------------------------------------------

        if ( !object.getName().equals( existing.getName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getAlternativeName(), existing.getAlternativeName() ) || ( isNotNull( object.getAlternativeName(), existing.getAlternativeName() ) && !object.getAlternativeName().equals( existing.getAlternativeName() ) ) )
        {
            return false;
        }
        if ( !object.getShortName().equals( existing.getShortName() ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getCode(), existing.getCode() ) || ( isNotNull( object.getCode(), existing.getCode() ) && !object.getCode().equals( existing.getCode() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDescription(), existing.getDescription() ) || ( isNotNull( object.getDescription(), existing.getDescription() ) && !object.getDescription().equals( existing.getDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getNumerator(), existing.getNumerator() ) || ( isNotNull( object.getNumerator(), existing.getNumerator() ) && !object.getNumerator().equals( existing.getNumerator() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getNumeratorDescription(), existing.getNumeratorDescription() ) || ( isNotNull( object.getNumeratorDescription(), existing.getNumeratorDescription() ) && !object.getNumeratorDescription().equals( existing.getNumeratorDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getNumeratorAggregationOperator(), existing.getNumeratorAggregationOperator() ) || ( isNotNull( object.getNumeratorAggregationOperator(), existing.getNumeratorAggregationOperator() ) && !object.getNumeratorAggregationOperator().equals( existing.getNumeratorAggregationOperator() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDenominator(), existing.getDenominator() ) || ( isNotNull( object.getDenominator(), existing.getDenominator() ) && !object.getDenominator().equals( existing.getDenominator() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDenominatorDescription(), existing.getDenominatorDescription() ) || ( isNotNull( object.getDenominatorDescription(), existing.getDenominatorDescription() ) && !object.getDenominatorDescription().equals( existing.getDenominatorDescription() ) ) )
        {
            return false;
        }
        if ( !isSimiliar( object.getDenominatorAggregationOperator(), existing.getDenominatorAggregationOperator() ) || ( isNotNull( object.getDenominatorAggregationOperator(), existing.getDenominatorAggregationOperator() ) && !object.getDenominatorAggregationOperator().equals( existing.getDenominatorAggregationOperator() ) ) )
        {
            return false;
        }

        // ---------------------------------------------------------------------
        // Identifying and Definitional attributes 
        // ---------------------------------------------------------------------

        if ( object.getExtended() != null && existing.getExtended() != null )
        {
            if ( !isSimiliar( object.getExtended().getMnemonic(), existing.getExtended().getMnemonic() ) || ( isNotNull( object.getExtended().getMnemonic(), existing.getExtended().getMnemonic() ) && !object.getExtended().getMnemonic().equals( existing.getExtended().getMnemonic() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getVersion(), existing.getExtended().getVersion() ) || ( isNotNull( object.getExtended().getVersion(), existing.getExtended().getVersion() ) && !object.getExtended().getVersion().equals( existing.getExtended().getVersion() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getContext(), existing.getExtended().getContext() ) || ( isNotNull( object.getExtended().getContext(), existing.getExtended().getContext() ) && !object.getExtended().getContext().equals( existing.getExtended().getContext() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getSynonyms(), existing.getExtended().getSynonyms() ) || ( isNotNull( object.getExtended().getSynonyms(), existing.getExtended().getSynonyms() ) && !object.getExtended().getSynonyms().equals( existing.getExtended().getSynonyms() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getHononyms(), existing.getExtended().getHononyms() ) || ( isNotNull( object.getExtended().getHononyms(), existing.getExtended().getHononyms() ) && !object.getExtended().getHononyms().equals( existing.getExtended().getHononyms() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getKeywords(), existing.getExtended().getKeywords() ) || ( isNotNull( object.getExtended().getKeywords(), existing.getExtended().getKeywords() ) && !object.getExtended().getKeywords().equals( existing.getExtended().getKeywords() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getStatus(), existing.getExtended().getStatus() ) || ( isNotNull( object.getExtended().getStatus(), existing.getExtended().getStatus() ) && !object.getExtended().getStatus().equals( existing.getExtended().getStatus() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getDataElementType(), existing.getExtended().getDataElementType() ) || ( isNotNull( object.getExtended().getDataElementType(), existing.getExtended().getDataElementType() ) && !object.getExtended().getDataElementType().equals( existing.getExtended().getDataElementType() ) ) )
            {
                return false;
            }
            
            // ---------------------------------------------------------------------
            // Relational and Representational attributes
            // ---------------------------------------------------------------------
    
            if ( !isSimiliar( object.getExtended().getDataType(), existing.getExtended().getDataType() ) || ( isNotNull( object.getExtended().getDataType(), existing.getExtended().getDataType() ) && !object.getExtended().getDataType().equals( existing.getExtended().getDataType() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getRepresentationalForm(), existing.getExtended().getRepresentationalForm() ) || ( isNotNull( object.getExtended().getRepresentationalForm(), existing.getExtended().getRepresentationalForm() ) && !object.getExtended().getRepresentationalForm().equals( existing.getExtended().getRepresentationalForm() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getRepresentationalLayout(), existing.getExtended().getRepresentationalLayout() ) || ( isNotNull( object.getExtended().getRepresentationalLayout(), existing.getExtended().getRepresentationalLayout() ) && !object.getExtended().getRepresentationalLayout().equals( existing.getExtended().getRepresentationalLayout() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getMinimumSize(), existing.getExtended().getMinimumSize() ) || ( isNotNull( object.getExtended().getMinimumSize(), existing.getExtended().getMinimumSize() ) && !object.getExtended().getMinimumSize().equals( existing.getExtended().getMinimumSize() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getMaximumSize(), existing.getExtended().getMaximumSize() ) || ( isNotNull( object.getExtended().getMaximumSize(), existing.getExtended().getMaximumSize() ) && !object.getExtended().getMaximumSize().equals( existing.getExtended().getMaximumSize() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getDataDomain(), existing.getExtended().getDataDomain() ) || ( isNotNull( object.getExtended().getDataDomain(), existing.getExtended().getDataDomain() ) && !object.getExtended().getDataDomain().equals( existing.getExtended().getDataDomain() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getValidationRules(), existing.getExtended().getValidationRules() ) || ( isNotNull( object.getExtended().getValidationRules(), existing.getExtended().getValidationRules() ) && !object.getExtended().getValidationRules().equals( existing.getExtended().getValidationRules() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getRelatedDataReferences(), existing.getExtended().getRelatedDataReferences() ) || ( isNotNull( object.getExtended().getRelatedDataReferences(), existing.getExtended().getRelatedDataReferences() ) && !object.getExtended().getRelatedDataReferences().equals( existing.getExtended().getRelatedDataReferences() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getGuideForUse(), existing.getExtended().getGuideForUse() ) || ( isNotNull( object.getExtended().getGuideForUse(), existing.getExtended().getGuideForUse() ) && !object.getExtended().getGuideForUse().equals( existing.getExtended().getGuideForUse() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getCollectionMethods(), existing.getExtended().getCollectionMethods() ) || ( isNotNull( object.getExtended().getCollectionMethods(), existing.getExtended().getCollectionMethods() ) && !object.getExtended().getCollectionMethods().equals( existing.getExtended().getCollectionMethods() ) ) )
            {
                return false;
            }
    
            // ---------------------------------------------------------------------
            // Administrative attributes 
            // ---------------------------------------------------------------------
    
            if ( !isSimiliar( object.getExtended().getResponsibleAuthority(), existing.getExtended().getResponsibleAuthority() ) || ( isNotNull( object.getExtended().getResponsibleAuthority(), existing.getExtended().getResponsibleAuthority() ) && !object.getExtended().getResponsibleAuthority().equals( existing.getExtended().getResponsibleAuthority() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getUpdateRules(), existing.getExtended().getUpdateRules() ) || ( isNotNull( object.getExtended().getUpdateRules(), existing.getExtended().getUpdateRules() ) && !object.getExtended().getUpdateRules().equals( existing.getExtended().getUpdateRules() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getAccessAuthority(), existing.getExtended().getAccessAuthority() ) || ( isNotNull( object.getExtended().getAccessAuthority(), existing.getExtended().getAccessAuthority() ) && !object.getExtended().getAccessAuthority().equals( existing.getExtended().getAccessAuthority() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getUpdateFrequency(), existing.getExtended().getUpdateFrequency() ) || ( isNotNull( object.getExtended().getUpdateFrequency(), existing.getExtended().getUpdateFrequency() ) && !object.getExtended().getUpdateFrequency().equals( existing.getExtended().getUpdateFrequency() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getLocation(), existing.getExtended().getLocation() ) || ( isNotNull( object.getExtended().getLocation(), existing.getExtended().getLocation() ) && !object.getExtended().getLocation().equals( existing.getExtended().getLocation() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getReportingMethods(), existing.getExtended().getReportingMethods() ) || ( isNotNull( object.getExtended().getReportingMethods(), existing.getExtended().getReportingMethods() ) && !object.getExtended().getReportingMethods().equals( existing.getExtended().getReportingMethods() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getVersionStatus(), existing.getExtended().getVersionStatus() ) || ( isNotNull( object.getExtended().getVersionStatus(), existing.getExtended().getVersionStatus() ) && !object.getExtended().getVersionStatus().equals( existing.getExtended().getVersionStatus() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getPreviousVersionReferences(), existing.getExtended().getPreviousVersionReferences() ) || ( isNotNull( object.getExtended().getPreviousVersionReferences(), existing.getExtended().getPreviousVersionReferences() ) && !object.getExtended().getPreviousVersionReferences().equals( existing.getExtended().getPreviousVersionReferences() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getSourceDocument(), existing.getExtended().getSourceDocument() ) || ( isNotNull( object.getExtended().getSourceDocument(), existing.getExtended().getSourceDocument() ) && !object.getExtended().getSourceDocument().equals( existing.getExtended().getSourceDocument() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getSourceOrganisation(), existing.getExtended().getSourceOrganisation() ) || ( isNotNull( object.getExtended().getSourceOrganisation(), existing.getExtended().getSourceOrganisation() ) && !object.getExtended().getSourceOrganisation().equals( existing.getExtended().getSourceOrganisation() ) ) )
            {
                return false;
            }
            if ( !isSimiliar( object.getExtended().getComment(), existing.getExtended().getComment() ) || ( isNotNull( object.getExtended().getComment(), existing.getExtended().getComment() ) && !object.getExtended().getComment().equals( existing.getExtended().getComment() ) ) )
            {
                return false;
            }
        }
        
        return true;
    }

}
