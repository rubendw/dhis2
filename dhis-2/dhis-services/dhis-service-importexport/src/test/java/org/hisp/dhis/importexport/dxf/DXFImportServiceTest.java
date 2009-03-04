package org.hisp.dhis.importexport.dxf;

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

import java.io.InputStream;

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportDataValueService;
import org.hisp.dhis.importexport.ImportInternalProcess;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportObjectStatus;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.ImportStrategy;
import org.hisp.dhis.importexport.util.ImportExportUtils;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

/**
 * <p>dxfA.zip contains 3 objects of each meta-data type.</p>
 * 
 * <p>dxfB.zip contains 5 objects of each meta-data type.</p>
 * 
 * <p>dxfC.zip contains 5 objects of each meta-data type with variations in properties in 3 of them, except for one-property objects.</p>
 * 
 * <p>dxfD.zip contains 2 objects of type DataElement, Period, and OrganisationUnit and 8 DataValues registered for all combinations.
 * 
 * <p>dxfE.zip contains 3 DataElements, 2 Periods, and 2 OrganisationUnits and 12 DataValues registered for all combinations.
 * 
 * <p>dxfF.zip contains 3 DataElements with variations in properties in 2 of them, 2 Periods, and 2 OrganisationUnits and 12 DataValues registered for all combinations.
 * 
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DXFImportServiceTest
    extends DhisConvenienceTest
{
    private final int dataASize = 3;
    private final int dataBSize = 5;
    private final int dataCSize = 5;
    
    private ImportInternalProcess importService;
    
    private InputStream inputStreamA;
    
    private InputStream inputStreamB;
    
    private InputStream inputStreamC;
    
    private InputStream inputStreamD;
    
    private InputStream inputStreamE;
    
    private InputStream inputStreamF;
    
    private ImportObjectService importObjectService;
    
    private ImportDataValueService importDataValueService;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        inputStreamA = classLoader.getResourceAsStream( "dxfA.zip" );
        
        inputStreamB = classLoader.getResourceAsStream( "dxfB.zip" );

        inputStreamC = classLoader.getResourceAsStream( "dxfC.zip" );
        
        inputStreamD = classLoader.getResourceAsStream( "dxfD.zip" );

        inputStreamE = classLoader.getResourceAsStream( "dxfE.zip" );
        
        inputStreamF = classLoader.getResourceAsStream( "dxfF.zip" );
        
        importService = (ImportInternalProcess) getBean( "internal-process-DXFImportService" );
        
        categoryOptionService = (DataElementCategoryOptionService) getBean( DataElementCategoryOptionService.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );
        
        categoryComboService = (DataElementCategoryComboService) getBean( DataElementCategoryComboService.ID );
        
        categoryOptionComboService = (DataElementCategoryOptionComboService) getBean( DataElementCategoryOptionComboService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        dataDictionaryService = (DataDictionaryService) getBean( DataDictionaryService.ID );
        
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        
        dataSetService = (DataSetService) getBean( DataSetService.ID );
        
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        
        organisationUnitGroupService = (OrganisationUnitGroupService) getBean( OrganisationUnitGroupService.ID );
        
        dataValueService = (DataValueService) getBean( DataValueService.ID );
        
        validationRuleService = (ValidationRuleService) getBean( ValidationRuleService.ID );
        
        expressionService = (ExpressionService) getBean( ExpressionService.ID );
        
        importObjectService = (ImportObjectService) getBean( ImportObjectService.ID );
        
        importDataValueService = (ImportDataValueService) getBean( ImportDataValueService.ID );
    }
    
    public void tearDownTest()
        throws Exception
    {
        inputStreamA.close();
        
        inputStreamB.close();
        
        inputStreamC.close();
        
        inputStreamD.close();
        
        inputStreamE.close();
        
        inputStreamF.close();
    }
    
    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------
    
    // TODO Improve test on duplicate GroupMemberAssociations
    
    public void testImportMetaData()
    {        
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, false, false, false );
        
        importService.importData( importParams, inputStreamA );
        
        assertObjects( dataASize );
    }
    
    public void testImportMetaDataWithPreview()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, true, false, false );
        
        importService.importData( importParams, inputStreamA );
        
        assertImportObjects( ImportObjectStatus.NEW, dataASize );
        
        assertGroupMembers( dataASize );
    }
    
    public void testImportMetaDataWithPreviewAndDuplicates()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, false, false, false );
        
        importService.importData( importParams, inputStreamA );
        
        assertObjects( dataASize );
        
        importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, true, false, false );
        
        importService.importData( importParams, inputStreamB );
        
        assertImportObjects( ImportObjectStatus.NEW, dataBSize - dataASize );
        
        assertImportObjects( ImportObjectStatus.UPDATE, 0 );
        
        assertGroupMembers( dataBSize );
    }
    
    public void testImportMetaDataWithPreviewAndUpdates()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, false, false, false );
        
        importService.importData( importParams, inputStreamA );
        
        assertObjects( dataASize );

        importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, true, false, false );
        
        importService.importData( importParams, inputStreamC );
        
        assertUpdateableImportObjects( ImportObjectStatus.UPDATE, dataASize );
        
        assertImportObjects( ImportObjectStatus.NEW, dataCSize - dataASize );
        
        assertGroupMembers( dataCSize );
    }
    
    public void testImportDataValues()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, false, true, false );
        
        importService.importData( importParams, inputStreamD );
        
        assertEquals( dataElementService.getAllDataElements().size(), 2 );
        
        assertEquals( organisationUnitService.getAllOrganisationUnits().size(), 2 );
        
        assertEquals( dataValueService.getAllDataValues().size(), 8 );
    }
    
    public void testImportDataValuesWithPreview()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, true, true, false );
        
        importService.importData( importParams, inputStreamD );
        
        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.NEW, DataElement.class ).size(), 2 );
        
        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.NEW, OrganisationUnit.class ).size(), 2 );
        
        assertEquals( importDataValueService.getImportDataValues( ImportObjectStatus.NEW ).size(), 8 );
    }

    public void testImportDataValuesWithPreviewAndDuplicates()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, false, true, false );
        
        importService.importData( importParams, inputStreamD );
        
        assertEquals( dataElementService.getAllDataElements().size(), 2 );
        
        assertEquals( organisationUnitService.getAllOrganisationUnits().size(), 2 );
        
        assertEquals( dataValueService.getAllDataValues().size(), 8 );
        
        importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, true, true, false );
        
        importService.importData( importParams, inputStreamE );
        
        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.NEW, DataElement.class ).size(), 1 );
        
        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.NEW, OrganisationUnit.class ).size(), 0 );
        
        assertEquals( importDataValueService.getImportDataValues( ImportObjectStatus.NEW ).size(), 12 );        
    }
    
    public void testImportDataValuesWithPreviewAndUpdates()
    {
        ImportParams importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, false, true, false );

        importService.importData( importParams, inputStreamD );

        assertEquals( dataElementService.getAllDataElements().size(), 2 );
        
        assertEquals( organisationUnitService.getAllOrganisationUnits().size(), 2 );
        
        assertEquals( dataValueService.getAllDataValues().size(), 8 );

        importParams = ImportExportUtils.getImportParams( ImportStrategy.NEW_AND_UPDATES, true, true, false );

        importService.importData( importParams, inputStreamF );

        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.NEW, DataElement.class ).size(), 1 );
        
        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.NEW, OrganisationUnit.class ).size(), 0 );
        
        assertEquals( importObjectService.getImportObjects( ImportObjectStatus.UPDATE, DataElement.class ).size(), 2 );
        
        assertEquals( importDataValueService.getImportDataValues( ImportObjectStatus.NEW ).size(), 12 );        
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private void assertObjects( int expectedSize )
    {
        assertEquals( categoryOptionService.getAllDataElementCategoryOptions().size(), 5 ); // Including default
        
        assertEquals( categoryService.getAllDataElementCategories().size(), 3 ); // Including default
        
        assertEquals( categoryComboService.getAllDataElementCategoryCombos().size(), 3 ); // Including default
        
        assertEquals( categoryOptionComboService.getAllDataElementCategoryOptionCombos().size(), 5 ); // Including default
        
        assertEquals( dataElementService.getAllDataElements().size(), expectedSize );
        
        assertEquals( dataElementService.getAllDataElementGroups().size(), expectedSize );
        
        for ( DataElementGroup group : dataElementService.getAllDataElementGroups() )
        {
            assertEquals( group.getMembers().size(), expectedSize );
        }
        
        assertEquals( indicatorService.getAllIndicatorTypes().size(), expectedSize );
        
        assertEquals( indicatorService.getAllIndicators().size(), expectedSize );
        
        assertEquals( indicatorService.getAllIndicatorGroups().size(), expectedSize );
        
        for ( IndicatorGroup group : indicatorService.getAllIndicatorGroups() )
        {
            assertEquals( group.getMembers().size(), expectedSize );
        }
        
        assertEquals( dataSetService.getAllDataSets().size(), expectedSize );
        
        for ( DataSet dataSet : dataSetService.getAllDataSets() )
        {
            assertEquals( dataSet.getDataElements().size(), expectedSize );
        }
        
        assertEquals( organisationUnitService.getAllOrganisationUnits().size(), expectedSize );
        
        assertEquals( organisationUnitGroupService.getAllOrganisationUnitGroups().size(), expectedSize );
        
        for ( OrganisationUnitGroup group : organisationUnitGroupService.getAllOrganisationUnitGroups() )
        {
            assertEquals( group.getMembers().size(), expectedSize );
        }
        
        assertEquals( organisationUnitGroupService.getAllOrganisationUnitGroupSets().size(), expectedSize );
        
        for ( OrganisationUnitGroupSet groupSet : organisationUnitGroupService.getAllOrganisationUnitGroupSets() )
        {
            assertEquals( groupSet.getOrganisationUnitGroups().size(), expectedSize );
        }
        
        assertEquals( validationRuleService.getAllValidationRules().size(), expectedSize );
    }

    private void assertImportObjects( ImportObjectStatus status, int expectedSize )
    {
        assertUpdateableImportObjects( status, expectedSize );
        
        assertEquals( importObjectService.getImportObjects( status, DataElementGroup.class ).size(), expectedSize );
        
        assertEquals( importObjectService.getImportObjects( status, IndicatorType.class ).size(), expectedSize );

        assertEquals( importObjectService.getImportObjects( status, IndicatorGroup.class ).size(), expectedSize );
        
        assertEquals( importObjectService.getImportObjects( status, DataSet.class ).size(), expectedSize );
                
        assertEquals( importObjectService.getImportObjects( status, OrganisationUnitGroup.class ).size(), expectedSize );

        assertEquals( importObjectService.getImportObjects( status, OrganisationUnitGroupSet.class ).size(), expectedSize );

        assertEquals( importObjectService.getImportObjects( status, ValidationRule.class  ).size(), expectedSize );        
    }
    
    private void assertUpdateableImportObjects( ImportObjectStatus status, int expectedSize )
    {
        assertEquals( importObjectService.getImportObjects( status, DataElement.class ).size(), expectedSize );
        
        assertEquals( importObjectService.getImportObjects( status, Indicator.class ).size(), expectedSize );
        
        assertEquals( importObjectService.getImportObjects( status, OrganisationUnit.class ).size(), expectedSize );
    }
    
    private void assertGroupMembers( int expectedSize )
    {
        assertEquals( importObjectService.getImportObjects( GroupMemberType.DATAELEMENTGROUP ).size(), expectedSize * expectedSize );
        
        assertEquals( importObjectService.getImportObjects( GroupMemberType.INDICATORGROUP ).size(), expectedSize * expectedSize );
        
        assertEquals( importObjectService.getImportObjects( GroupMemberType.DATASET ).size(), expectedSize * expectedSize );
        
        assertEquals( importObjectService.getImportObjects( GroupMemberType.ORGANISATIONUNITGROUP ).size(), expectedSize * expectedSize );
        
        assertEquals( importObjectService.getImportObjects( GroupMemberType.ORGANISATIONUNITGROUPSET ).size(), expectedSize * expectedSize );
    }
}
