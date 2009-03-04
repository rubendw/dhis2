package org.hisp.dhis.system.deletion;

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

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrder;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrder;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.DataEntryForm;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.FrequencyOverrideAssociation;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserSetting;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleGroup;

/**
 * A DeletionHandler should override methods for objects that, when deleted,
 * will affect the current object in any way. Eg. a DeletionHandler for
 * DataElementGroup should override the deleteDataElement(..) method which
 * should remove the DataElement from all DataElementGroups. Also, it should
 * override the allowDeleteDataElement() method and return false if there exists
 * objects that are dependent on the DataElement and are considered not be
 * deleted.
 * 
 * @author Lars Helge Overland
 * @version $Id$
 */
@SuppressWarnings( "unused" )
public abstract class DeletionHandler
{
    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract String getClassName();

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

    public void deleteDataDictionary( DataDictionary dataDictionary )
    {   
    }
    
    public boolean allowDeleteDataDictionary( DataDictionary dataDictionary )
    {
        return true;
    }
    
    public void deleteDataElement( DataElement dataElement )
    {
    }
    
    public boolean allowDeleteDataElement( DataElement dataElement )
    {
        return true;
    }
    
    public void deleteDataElementGroup( DataElementGroup dataElementGroup )
    {
    }

    public boolean allowDeleteDataElementGroup( DataElementGroup dataElementGroup )
    {
        return true;
    }
    
    public void deleteDataElementCategory( DataElementCategory category )
    {
    }

    public boolean allowDeleteDataElementCategory( DataElementCategory category )
    {
        return true;
    }
    
    public void deleteDataElementCategoryOption( DataElementCategoryOption categoryOption )
    {
    }

    public boolean allowDeleteDataElementCategoryOption( DataElementCategoryOption categoryOption )
    {
        return true;
    }
    
    public void deleteDataElementCategoryCombo( DataElementCategoryCombo categoryCombo )
    {
    }

    public boolean allowDeleteDataElementCategoryCombo( DataElementCategoryCombo categoryCombo )
    {
        return true;
    }
    
    public void deleteDataElementCategoryOptionCombo( DataElementCategoryOptionCombo categoryOptionCombo )
    {
    }

    public boolean allowDeleteDataElementCategoryOptionCombo( DataElementCategoryOptionCombo categoryOptionCombo )
    {
        return true;
    }
    
    public void deleteDataElementDimensionRowOrder( DataElementDimensionRowOrder rowOrder )
    {
    }
    
    public boolean allowDeleteDataElementDimensionRowOrder( DataElementDimensionRowOrder rowOrder )
    {
        return true;
    }
    
    public void deleteDataElementDimensionColumnOrder( DataElementDimensionColumnOrder columnOrder )
    {   
    }
    
    public boolean allowDataElementDimensionColumnOrder( DataElementDimensionColumnOrder columnOrder )
    {
        return true;
    }
    
    public void deleteDataSet( DataSet dataSet )
    {
    }

    public boolean allowDeleteDataSet( DataSet dataSet )
    {
        return true;
    }
    
    public void deleteSection( Section section )
    {   
    }

    public boolean allowDeleteSection( Section section )
    {
        return true;
    }
    
    public void deleteCompleteDataSetRegistration( CompleteDataSetRegistration registration )
    {   
    }
    
    public boolean allowDeleteCompleteDataSetRegistration( CompleteDataSetRegistration registration )
    {
        return true;
    }
    
    public void deleteDataValue( DataValue dataValue )
    {
    }

    public boolean allowDeleteDataValue( DataValue dataValue )
    {
        return true;
    }
    
    public void deleteExpression( Expression expression )
    {
    }

    public boolean allowDeleteExpression( Expression expression )
    {
        return true;
    }
    
    public void deleteMinMaxDataElement( MinMaxDataElement minMaxDataElement )
    {   
    }

    public boolean allowDeleteMinMaxDataElement( MinMaxDataElement minMaxDataElement )
    {
        return true;
    }
    
    public void deleteIndicator( Indicator indicator )
    {
    }

    public boolean allowDeleteIndicator( Indicator indicator )
    {
        return true;
    }
    
    public void deleteIndicatorGroup( IndicatorGroup indicatorGroup )
    {
    }

    public boolean allowDeleteIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        return true;
    }
    
    public void deleteIndicatorType( IndicatorType indicatorType )
    {   
    }

    public boolean allowDeleteIndicatorType( IndicatorType indicatorType )
    {
        return true;
    }
    
    public void deletePeriod( Period period )
    {
    }

    public boolean allowDeletePeriod( Period period )
    {
        return true;
    }
    
    public void deleteSource( Source source )
    {
    }

    public boolean allowDeleteSource( Source source )
    {
        return true;
    }
    
    public void deleteValidationRule( ValidationRule validationRule )
    {
    }

    public boolean allowDeleteValidationRule( ValidationRule validationRule )
    {
        return true;
    }
    
    public void deleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {   
    }

    public boolean allowDeleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        return true;
    }
    
    public void deleteFrequencyOverrideAssociation( FrequencyOverrideAssociation association )
    {   
    }

    public boolean allowDeleteFrequencyOverrideAssociation( FrequencyOverrideAssociation association )
    {
        return true;
    }
    
    public void deleteDataEntryForm( DataEntryForm form )
    {   
    }

    public boolean allowDeleteDataEntryForm( DataEntryForm form )
    {
        return true;
    }
    
    public void deleteOrganisationUnitGroup( OrganisationUnitGroup group )
    {   
    }

    public boolean allowDeleteOrganisationUnitGroup( OrganisationUnitGroup group )
    {
        return true;
    }
    
    public void deleteOrganisationUnitGroupSet( OrganisationUnitGroupSet groupSet )
    {
    }

    public boolean allowDeleteOrganisationUnitGroupSet( OrganisationUnitGroupSet groupSet )
    {
        return true;
    }
    
    public void deleteOrganisationUnitLevel( OrganisationUnitLevel level )
    {   
    }

    public boolean allowDeleteOrganisationUnitLevel( OrganisationUnitLevel level )
    {
        return true;
    }
    
    public void deleteTarget( Target target )
    {   
    }

    public boolean allowDeleteTarget( Target target )
    {
        return true;
    }
    
    public void deleteReport( Report report )
    {   
    }
    
    public boolean allowDeleteReport( Report report )
    {
        return true;
    }
    
    public void deleteReportTable( ReportTable reportTable )
    {   
    }

    public boolean allowDeleteReportTable( ReportTable reportTable )
    {
        return true;
    }
    
    public void deleteUser( User user )
    {   
    }
    
    public boolean allowDeleteUser( User user )
    {
        return true;
    }
    
    public void deleteUserCredentials( UserCredentials credentials )
    {   
    }
    
    public boolean allowDeleteUserCredentials( UserCredentials credentials )
    {
        return true;
    }
    
    public void deleteUserAuthorityGroup( UserAuthorityGroup authorityGroup )
    {   
    }
    
    public boolean allowDeleteUserAuthorityGroup( UserAuthorityGroup authorityGroup )
    {
        return true;
    }
    
    public void deleteUserSetting( UserSetting userSetting )
    {   
    }
    
    public boolean allowDeleteUserSetting( UserSetting userSetting )
    {
        return true;
    }
}
