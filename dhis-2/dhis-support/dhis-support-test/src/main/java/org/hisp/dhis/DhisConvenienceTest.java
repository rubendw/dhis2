package org.hisp.dhis;

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

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrderService;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrderService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.importexport.ImportDataValue;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleGroup;
import org.hisp.dhis.validation.ValidationRuleService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DhisConvenienceTest
    extends DhisSpringTest
{
    private static final String BASE_UUID = "C3C2E28D-9686-4634-93FD-BE3133935EC";

    private static Date date;
    
    private static Calendar calendar;

    // -------------------------------------------------------------------------
    // Service references
    // -------------------------------------------------------------------------

    protected DataElementService dataElementService;
    
    protected DataElementCategoryService categoryService;
    
    protected DataElementCategoryComboService categoryComboService;
    
    protected DataElementCategoryOptionService categoryOptionService;
    
    protected DataElementCategoryOptionComboService categoryOptionComboService;
    
    protected DataElementDimensionColumnOrderService dimensionColumnOrderService;
    
    protected DataElementDimensionRowOrderService dimensionRowOrderService;
    
    protected DataDictionaryService dataDictionaryService;
    
    protected IndicatorService indicatorService;
    
    protected DataSetService dataSetService;
    
    protected CompleteDataSetRegistrationService completeDataSetRegistrationService;
    
    protected SourceStore sourceStore;
    
    protected OrganisationUnitService organisationUnitService;
    
    protected OrganisationUnitGroupService organisationUnitGroupService;
    
    protected PeriodService periodService;
    
    protected ValidationRuleService validationRuleService;
    
    protected ExpressionService expressionService;
    
    protected DataValueService dataValueService;
    
    static
    {
        calendar = Calendar.getInstance();
    
        calendar.set( 1970, Calendar.JANUARY, 1 );
    
        date = calendar.getTime();
    }
    
    /**
     * Creates a date.
     * 
     * @param year the year.
     * @param month the month.
     * @param day the day of month.
     * @return a date.
     */
    protected static Date getDate( int year, int month, int day )
    {
        calendar.set( year, month - 1, day );
        
        return calendar.getTime();
    }

    // -------------------------------------------------------------------------
    // Create object methods 
    // -------------------------------------------------------------------------

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static DataElement createDataElement( char uniqueCharacter )
    {
        DataElement dataElement = new DataElement();
        
        dataElement.setUuid( BASE_UUID + uniqueCharacter );
        dataElement.setName( "DataElement" + uniqueCharacter );
        dataElement.setAlternativeName( "AlternativeName" + uniqueCharacter );
        dataElement.setShortName( "ShortName" + uniqueCharacter );
        dataElement.setCode( "Code" + uniqueCharacter );
        dataElement.setDescription( "Description" + uniqueCharacter );
        dataElement.setActive( true );
        dataElement.setType( DataElement.TYPE_INT );
        dataElement.setAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        return dataElement;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param categoryCombo The category combo.
     */
    protected static DataElement createDataElement( char uniqueCharacter, DataElementCategoryCombo categoryCombo )
    {
        DataElement dataElement = createDataElement( uniqueCharacter );
        
        dataElement.setCategoryCombo( categoryCombo );
        
        return dataElement;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param type The type.
     * @param aggregationOperator The aggregation operator.
     */
    protected static DataElement createDataElement( char uniqueCharacter, String type, String aggregationOperator )
    {
        DataElement dataElement = createDataElement( uniqueCharacter );
        
        dataElement.setType( type );
        dataElement.setAggregationOperator( aggregationOperator );
        
        return dataElement;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param type The type.
     * @param aggregationOperator The aggregation operator.
     * @param categoryCombo The category combo.
     */
    protected static DataElement createDataElement( char uniqueCharacter, String type, String aggregationOperator, DataElementCategoryCombo categoryCombo )
    {
        DataElement dataElement = createDataElement( uniqueCharacter );
        
        dataElement.setType( type );
        dataElement.setAggregationOperator( aggregationOperator );
        dataElement.setCategoryCombo( categoryCombo );
        
        return dataElement;
    }        
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static DataElementGroup createDataElementGroup( char uniqueCharacter )
    {
        DataElementGroup group = new DataElementGroup();
        
        group.setUuid( BASE_UUID + uniqueCharacter );
        group.setName( "DataElementGroup" + uniqueCharacter );
        
        return group;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static DataDictionary createDataDictionary( char uniqueCharacter )
    {
        DataDictionary dictionary = new DataDictionary();
        
        dictionary.setName( "DataDictionary" + uniqueCharacter );
        dictionary.setDescription( "Description" + uniqueCharacter );
        dictionary.setRegion( "Region" + uniqueCharacter );
        
        return dictionary;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static IndicatorType createIndicatorType( char uniqueCharacter )
    {
        IndicatorType type = new IndicatorType();
        
        type.setName( "IndicatorType" + uniqueCharacter );
        type.setFactor( 100 );
        
        return type;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param type The type.
     */
    protected static Indicator createIndicator( char uniqueCharacter, IndicatorType type )
    {
        Indicator indicator = new Indicator();
        
        indicator.setUuid( BASE_UUID + uniqueCharacter );
        indicator.setName( "Indicator" + uniqueCharacter );
        indicator.setAlternativeName( "AlternativeName" + uniqueCharacter );
        indicator.setShortName( "ShortName" + uniqueCharacter );
        indicator.setCode( "Code" + uniqueCharacter );
        indicator.setDescription( "Description" + uniqueCharacter );
        indicator.setAnnualized( false );
        indicator.setIndicatorType( type );
        indicator.setNumerator( "Numerator" );
        indicator.setNumeratorDescription( "NumeratorDescription" );
        indicator.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        indicator.setDenominator( "Denominator" );
        indicator.setDenominatorDescription( "DenominatorDescription" );
        indicator.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        return indicator;
    }
        
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static IndicatorGroup createIndicatorGroup( char uniqueCharacter )
    {
        IndicatorGroup group = new IndicatorGroup();
        
        group.setUuid( BASE_UUID + uniqueCharacter );
        group.setName( "IndicatorGroup" + uniqueCharacter );
        
        return group;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param periodType The period type.
     */
    protected static DataSet createDataSet( char uniqueCharacter, PeriodType periodType )
    {
        DataSet dataSet = new DataSet();
        
        dataSet.setName( "DataSet" + uniqueCharacter );
        dataSet.setShortName( "ShortName" + uniqueCharacter );
        dataSet.setPeriodType( periodType );
        
        return dataSet;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static OrganisationUnit createOrganisationUnit( char uniqueCharacter )
    {
        OrganisationUnit unit = new OrganisationUnit();
        
        unit.setUuid( BASE_UUID + uniqueCharacter );
        unit.setName( "OrganisationUnit" + uniqueCharacter );
        unit.setShortName( "ShortName" + uniqueCharacter );
        unit.setOrganisationUnitCode( "Code" + uniqueCharacter );
        unit.setOpeningDate( date );
        unit.setClosedDate( date );
        unit.setActive( true );
        unit.setComment( "Comment" + uniqueCharacter );
        unit.setGeoCode( "GeoCode" );
        unit.setLatitude( "Latitude" );
        unit.setLongitude( "Longitude" );
                
        return unit;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param parent The parent.
     */
    protected static OrganisationUnit createOrganisationUnit( char uniqueCharacter, OrganisationUnit parent )
    {
        OrganisationUnit unit = createOrganisationUnit( uniqueCharacter );
        
        unit.setParent( parent );
        
        return unit;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static OrganisationUnitGroup createOrganisationUnitGroup( char uniqueCharacter )
    {
        OrganisationUnitGroup group = new OrganisationUnitGroup();
        
        group.setUuid( BASE_UUID + uniqueCharacter );
        group.setName( "OrganisationUnitGroup" + uniqueCharacter );
        
        return group;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static OrganisationUnitGroupSet createOrganisationUnitGroupSet( char uniqueCharacter )
    {
        OrganisationUnitGroupSet groupSet = new OrganisationUnitGroupSet();
        
        groupSet.setName( "OrganisationUnitGroupSet" + uniqueCharacter );
        groupSet.setDescription( "Description" + uniqueCharacter );
        groupSet.setCompulsory( true );
        groupSet.setExclusive( true );
        
        return groupSet;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param startDate The start date.
     * @param endDate The end date.
     */
    protected static Period createPeriod( PeriodType type, Date startDate, Date endDate )
    {
        Period period = new Period();
        
        period.setPeriodType( type );
        period.setStartDate( startDate );
        period.setEndDate( endDate );
        
        return period;
    }
    
    /**
     * @param dataElement The data element.
     * @param period The period.
     * @param source The source.
     * @param value The value.
     * @param categoryOptionCombo The data element category option combo.
     */
    protected static DataValue createDataValue( DataElement dataElement, Period period, Source source, String value, DataElementCategoryOptionCombo categoryOptionCombo )
    {
        DataValue dataValue = new DataValue();
        
        dataValue.setDataElement( dataElement );
        dataValue.setPeriod( period );
        dataValue.setSource( source );
        dataValue.setValue( value );
        dataValue.setComment( "Comment" );
        dataValue.setStoredBy( "StoredBy" );
        dataValue.setTimestamp( date );
        dataValue.setOptionCombo( categoryOptionCombo );
        
        return dataValue;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param operator The operator.
     * @param leftSide The left side expression.
     * @param rightSide The right side expression.
     */
    protected static ValidationRule createValidationRule( char uniqueCharacter, String operator, Expression leftSide, Expression rightSide )
    {
        ValidationRule validationRule = new ValidationRule();
        
        validationRule.setName( "ValidationRule" + uniqueCharacter );
        validationRule.setDescription( "Description" + uniqueCharacter );
        validationRule.setType( ValidationRule.TYPE_ABSOLUTE );
        validationRule.setOperator( operator );
        validationRule.setLeftSide( leftSide );
        validationRule.setRightSide( rightSide );
        
        return validationRule;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @return
     */
    protected static ValidationRuleGroup createValidationRuleGroup( char uniqueCharacter )
    {
        ValidationRuleGroup group = new ValidationRuleGroup();
        
        group.setName( "ValidationRuleGroup" + uniqueCharacter );
        group.setDescription( "Description" + uniqueCharacter );
        
        return group;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param expressionString The expression string.
     * @param dataElementsInExpression A collection of the data elements entering into the expression.
     */
    protected static Expression createExpression( char uniqueCharacter, String expressionString, Set<DataElement> dataElementsInExpression )
    {
        Expression expression = new Expression();
        
        expression.setExpression( expressionString );
        expression.setDescription( "Description" + uniqueCharacter );
        expression.setDataElementsInExpression( dataElementsInExpression );
        
        return expression;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    protected static DataElement createExtendedDataElement( char uniqueCharacter )
    {
        DataElement dataElement = createDataElement( uniqueCharacter );
        
        ExtendedDataElement extended = createExtendedElement( uniqueCharacter );
        
        dataElement.setExtended( extended );
        
        return dataElement;
    }
    
    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param type The type.
     */
    protected static Indicator createExtendedIndicator( char uniqueCharacter, IndicatorType type )
    {
        Indicator indicator = createIndicator( uniqueCharacter, type );

        ExtendedDataElement extended = createExtendedElement( uniqueCharacter );
        
        indicator.setExtended( extended );
        
        return indicator;
    }

    /**
     * @param dataElementId The data element identifier.
     * @param categoryOptionComboId The data element category option combo identifier.
     * @param periodId The period identifier.
     * @param sourceId The source identifier.
     * @param status The status.
     */
    protected static ImportDataValue createImportDataValue( int dataElementId, int categoryOptionComboId, int periodId, int sourceId, ImportObjectStatus status )
    {
        ImportDataValue importDataValue = new ImportDataValue();
        
        importDataValue.setDataElementId( dataElementId );
        importDataValue.setCategoryOptionComboId( categoryOptionComboId );
        importDataValue.setPeriodId( periodId );
        importDataValue.setSourceId( sourceId );
        importDataValue.setValue( String.valueOf( 10 ) );
        importDataValue.setStoredBy( "StoredBy" );
        importDataValue.setTimestamp( new Date() );
        importDataValue.setComment( "Comment" );
        importDataValue.setStatus( status.name() );
        
        return importDataValue;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private static ExtendedDataElement createExtendedElement( char uniqueCharacter )
    {
        ExtendedDataElement extended = new ExtendedDataElement();
        
        extended.setMnemonic( "Mnemonic" + uniqueCharacter );
        extended.setVersion( "Version" + uniqueCharacter );
        extended.setContext( "Context" + uniqueCharacter );
        extended.setSynonyms( "Synonyms" + uniqueCharacter );
        extended.setHononyms( "Hononyms" + uniqueCharacter );
        extended.setKeywords( "Keywords" + uniqueCharacter );
        extended.setStatus( ExtendedDataElement.STATUS_CURRENT );
        extended.setStatusDate( date );
        extended.setDataElementType( ExtendedDataElement.TYPE_DATAELEMENT );
        
        extended.setDataType( ExtendedDataElement.DATATYPE_ALPHABETIC );
        extended.setRepresentationalForm( ExtendedDataElement.REPRESENTATIONAL_FORM_CODE );
        extended.setRepresentationalLayout( "RepresentationalLayout" + uniqueCharacter );
        extended.setMinimumSize( 0 );
        extended.setMaximumSize( 10 );
        extended.setDataDomain( "DataDomain" + uniqueCharacter );
        extended.setValidationRules( "ValidationRules" + uniqueCharacter );
        extended.setRelatedDataReferences( "RelatedDataReferences" + uniqueCharacter );
        extended.setGuideForUse( "GuideForUse" + uniqueCharacter );
        extended.setCollectionMethods( "CollectionMethods" + uniqueCharacter );
        
        extended.setResponsibleAuthority( "ResponsibleAuthority" + uniqueCharacter );
        extended.setUpdateRules( "UpdateRules" + uniqueCharacter );
        extended.setAccessAuthority( "AccessAuthority" + uniqueCharacter );
        extended.setUpdateFrequency( "UpdateFrequency" + uniqueCharacter );
        extended.setLocation( "Location" + uniqueCharacter );
        extended.setReportingMethods( "ReportingMethods" + uniqueCharacter );
        extended.setVersionStatus( "VersionStatus" + uniqueCharacter );
        extended.setPreviousVersionReferences( "PreviousVersionReferences" + uniqueCharacter );
        extended.setSourceDocument( "SourceDocument" + uniqueCharacter );
        extended.setSourceOrganisation( "SourceOrganisation" + uniqueCharacter );
        extended.setComment( "Comment" + uniqueCharacter );
        extended.setSaved( date );
        extended.setLastUpdated( date );
        
        return extended;
    }
}
