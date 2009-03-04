package org.hisp.dhis.reporting.dataset.action;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.order.manager.DataElementOrderManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.reporting.dataset.generators.DesignGenerator;
import org.hisp.dhis.reporting.dataset.generators.ReportGenerator;
import org.hisp.dhis.reporting.dataset.generators.TabularDesignGenerator;
import org.hisp.dhis.reporting.dataset.generators.TabularReportGenerator;
import org.hisp.dhis.reporting.dataset.report.Element;
import org.hisp.dhis.reporting.dataset.report.ReportStore;
import org.hisp.dhis.reporting.dataset.utils.NumberUtils;
import org.hisp.dhis.system.filter.AggregateableDataElementPredicate;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class GenerateDefaultDataSetReportAction
    extends AbstractAction
{
    // -------------------------------------------------------------------------
    // Parameters
    // -------------------------------------------------------------------------

    private Collection<DataElementCategory> orderedCategories = new ArrayList<DataElementCategory>();

    private Map<Integer, Integer> catColSize = new HashMap<Integer, Integer>();

    private boolean preview;

    private String fileName;

    private int reportType;

    public void setPreview( boolean preview )
    {
        this.preview = preview;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public int getReportType()
    {
        return reportType;
    }

    public void setReportType( int reportType )
    {
        this.reportType = reportType;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DesignGenerator designGenerator;

    public void setDesignGenerator( DesignGenerator designGenerator )
    {
        this.designGenerator = designGenerator;
    }

    private TabularDesignGenerator tabularDesignGenerator;

    public void setTabularDesignGenerator( TabularDesignGenerator tabularDesignGenerator )
    {
        this.tabularDesignGenerator = tabularDesignGenerator;
    }

    private ReportGenerator reportGenerator;

    public void setReportGenerator( ReportGenerator reportGenerator )
    {
        this.reportGenerator = reportGenerator;
    }

    private TabularReportGenerator tabularReportGenerator;

    public void setTabularReportGenerator( TabularReportGenerator tabularReportGenerator )
    {
        this.tabularReportGenerator = tabularReportGenerator;
    }

    private DataSetStore dataSetStore;

    public void setDataSetStore( DataSetStore dataSetStore )
    {
        this.dataSetStore = dataSetStore;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementOrderManager dataElementOrderManager;

    public void setDataElementOrderManager( DataElementOrderManager dataElementOrderManager )
    {
        this.dataElementOrderManager = dataElementOrderManager;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService(
        DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }

    private DataElementCategoryService dataElementCategoryService;

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit orgUnit = selectionTreeManager.getSelectedOrganisationUnit();

        if ( dataSetId() && orgUnit != null && startDate() && endDate() && isAfter() )
        {
            organisationUnitId = orgUnit.getId();

            int designTemplate = 2;

            int chartTemplate = 0;

            int numberOfColumns = 1;

            boolean isTabular = false;

            DataSet dataSet = dataSetStore.getDataSet( dataSetId );

            Date sDate = format.parseDate( startDate );
            Date eDate = format.parseDate( endDate );

            String dataSetReportName = dataSet.getName();

            Collection<DataElement> dataElements = dataElementOrderManager.getOrderedDataElements( dataSet );

            CollectionUtils.filter( dataElements, new AggregateableDataElementPredicate() );

            // -----------------------------------------------------------------
            // Adding new report based on the selected data set
            // -----------------------------------------------------------------

            if ( !reportStore.isXMLReportExists( dataSetReportName ) )
            {
                reportStore.addReport( dataSetReportName, ReportStore.GENERIC );
            }

            Collection<Element> reportCollection = new ArrayList<Element>(); // reportStore.getAllReportElements(
                                                                                // dataSetReportName
                                                                                // );

            List<String> textFieldNames = new ArrayList<String>();

            List<String> staticTextNames = new ArrayList<String>();

            SortedMap<String, String> reportElements = new TreeMap<String, String>();

            Collection<String> collectedDataElements = new ArrayList<String>();
            Collection<String> collectedOptionCombos = new ArrayList<String>();

            SortedMap<String, Collection<String>> complexReportElements = new TreeMap<String, Collection<String>>();

            Collection<String> tableHeaders = new ArrayList<String>();
            Collection<String> tableColumns = new ArrayList<String>();

            int rowCount = 1;

            statementManager.initialise();

            try
            {
                if ( dataElements.iterator().next().getCategoryCombo().getOptionCombos().size() > 1 )
                {
                    isTabular = true;

                    DataElementCategoryCombo catCombo = dataElements.iterator().next().getCategoryCombo();

                    for ( DataElementCategory category : catCombo.getCategories() )
                    {
                        for ( DataElementCategoryOption option : category.getCategoryOptions() )
                        {
                            tableHeaders.add( option.getName() );
                            reportElements.put( option.getName(), option.getName() );
                        }
                    }

                    for ( DataElement dataElement : dataElements )
                    {
                        int colCount = 0;

                        Collection<DataElementCategoryOptionCombo> optionCombos = dataElementCategoryOptionComboService
                            .sortDataElementCategoryOptionCombos( catCombo );

                        collectedDataElements.add( dataElement.getName() );

                        numberOfColumns = optionCombos.size();

                        isTabular = true;

                        for ( DataElementCategoryOptionCombo optionComb : optionCombos )
                        {
                            collectedOptionCombos.add( Integer.toString( optionComb.getId() ) );
                        }

                        for ( DataElementCategoryOptionCombo optionComb : optionCombos )
                        {
                            String value = new String();

                            if ( dataElement.getType().equals( DataElement.TYPE_INT ) )
                            {
                                double aggregatedValue = reportDataAccess.getAggregatedDataValue( dataElement.getId(),
                                    optionComb.getId(), sDate, eDate, String.valueOf( organisationUnitId ) );

                                value = (aggregatedValue != AggregationService.NO_VALUES_REGISTERED) ? NumberUtils
                                    .formatDataValue( aggregatedValue ) : "";
                            }
                            else
                            {
                                value = " ";
                            }

                            Collection<String> cellValues = new ArrayList<String>();

                            if ( complexReportElements.containsKey( "col" + colCount ) )
                            {
                                cellValues = complexReportElements.get( "col" + colCount );
                            }

                            cellValues.add( value );
                            complexReportElements.put( "col" + colCount, cellValues );
                            colCount++;
                        }

                        rowCount++;
                    }

                    complexReportElements.put( "tabularColumn", collectedOptionCombos );
                    complexReportElements.put( "tabularRow", collectedDataElements );
                }
                else
                {
                    for ( DataElement element : dataElements )
                    {
                        reportStore.addReportElement( dataSetReportName, ReportStore.DATAELEMENT, element.getId() );
                    }

                    reportCollection = reportStore.getAllReportElements( dataSetReportName );

                    for ( Element element : reportCollection )
                    {
                        textFieldNames.add( element.getElementKey() );

                        staticTextNames.add( element.getElementName() );
                    }

                    if ( !reportStore.isJRXMLReportExists( dataSetReportName ) )
                    {
                        designGenerator.generateDesign( dataSetReportName, textFieldNames, staticTextNames,
                            designTemplate, chartTemplate );
                    }

                    for ( Element reportElement : reportCollection )
                    {
                        String value = new String();

                        if ( dataElementService.getDataElement( reportElement.getElementId() ).getType().equals(
                            DataElement.TYPE_INT ) )
                        {
                            double aggregatedValue = reportDataAccess.getAggregatedDataValue( reportElement
                                .getElementId(), dataElements.iterator().next().getCategoryCombo().getOptionCombos()
                                .iterator().next().getId(), sDate, eDate, String.valueOf( organisationUnitId ) );

                            value = (aggregatedValue != AggregationService.NO_VALUES_REGISTERED) ? NumberUtils
                                .formatDataValue( aggregatedValue ) : "";
                        }

                        else
                        {
                            value = " ";
                        }

                        reportElements.put( reportElement.getElementKey(), value );

                    }
                }
            }
            finally
            {
                statementManager.destroy();
            }

            // -----------------------------------------------------------------
            // Adding report information
            // -----------------------------------------------------------------

            String unitName = reportDataAccess.getOrganisationUnitName( organisationUnitId );
            String periodName = format.formatDate( sDate ) + " - " + format.formatDate( eDate );

            reportElements.put( "ReportName", dataSetReportName );
            reportElements.put( "OrganisationUnit", unitName );
            reportElements.put( "Period", periodName );

            // -----------------------------------------------------------------
            // Generating report
            // -----------------------------------------------------------------

            if ( preview )
            {
                String buffer = new String();

                if ( isTabular )
                {
                    if ( !reportStore.isXMLReportExists( dataSetReportName ) )
                    {
                        reportStore.deleteReport( dataSetReportName );

                        reportStore.addReport( dataSetReportName, ReportStore.GENERIC );
                    }

                    if ( !reportStore.isJRXMLReportExists( dataSetReportName ) )
                    {
                        for ( int i = 0; i < numberOfColumns; i++ )
                        {
                            tableColumns.add( "col" + i );
                        }

                        Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap = getHeadingLayout( dataSet );

                        tabularDesignGenerator.generateDesign( dataSetReportName, tableHeaders, tableColumns,
                            orderedCategories, numberOfColumns, orderedOptionsMap );

                    }

                    buffer = tabularReportGenerator.generateReportPreview( dataSetReportName, reportElements,
                        complexReportElements );
                }
                else
                {
                    buffer = reportGenerator.generateReportPreview( dataSetReportName, reportElements, null );
                }

                setSessionVar( HTML_BUFFER, buffer );
                setSessionVar( REPORT_TYPE, ReportStore.ORGUNIT_SPECIFIC );
                setSessionVar( REPORT, report );
                setSessionVar( START_DATE, startDate );
                setSessionVar( END_DATE, endDate );
            }
            else
            {

                fileName = report + "-" + periodName + ".pdf";

                if ( isTabular )
                {
                    if ( !reportStore.isXMLReportExists( dataSetReportName ) )
                    {
                        reportStore.deleteReport( dataSetReportName );

                        reportStore.addReport( dataSetReportName, ReportStore.GENERIC );
                    }

                    if ( !reportStore.isJRXMLReportExists( dataSetReportName ) )
                    {
                        for ( int i = 0; i < numberOfColumns; i++ )
                        {
                            tableColumns.add( "col" + i );
                        }

                        Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap = getHeadingLayout( dataSet );

                        tabularDesignGenerator.generateDesign( dataSetReportName, tableHeaders, tableColumns,
                            orderedCategories, numberOfColumns, orderedOptionsMap );
                    }

                    inputStream = tabularReportGenerator.generateReportStream( dataSetReportName, reportElements,
                        complexReportElements );
                }
                else
                {
                    inputStream = reportGenerator.generateReportStream( dataSetReportName, reportElements, null );
                }
            }

            return SUCCESS;
        }

        return ERROR;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    Map<Integer, Collection<DataElementCategoryOption>> getHeadingLayout( DataSet dataSet )
    {
        Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap = new HashMap<Integer, Collection<DataElementCategoryOption>>();

        Collection<DataElement> dataElements = dataSet.getDataElements();

        DataElement sampleDataElement = dataElements.iterator().next();

        DataElementCategoryCombo catCombo = sampleDataElement.getCategoryCombo();

        orderedCategories = dataElementCategoryComboService.getOrderCategories( catCombo );

        // ---------------------------------------------------------------------
        // Calculating the number of times each category is supposed to be
        // repeated in the dataentry form
        // ---------------------------------------------------------------------

        Map<Integer, Integer> catRepeat = new HashMap<Integer, Integer>();

        int catColSpan = catCombo.getOptionCombos().size();

        for ( DataElementCategory cat : orderedCategories )
        {

            catColSpan = catColSpan / cat.getCategoryOptions().size();

            catColSize.put( cat.getId(), catColSpan );

            catRepeat.put( cat.getId(), catCombo.getOptionCombos().size()
                / (catColSpan * cat.getCategoryOptions().size()) );

        }

        // ---------------------------------------------------------------------
        // Get the order of options
        // ---------------------------------------------------------------------

        for ( DataElementCategory dec : orderedCategories )
        {
            Collection<DataElementCategoryOption> options = dataElementCategoryService.getOrderedOptions( dec );

            Collection<DataElementCategoryOption> allOptions = new ArrayList<DataElementCategoryOption>();

            for ( int i = 1; i <= catRepeat.get( dec.getId() ); i++ )
            {
                allOptions.addAll( options );
            }

            orderedOptionsMap.put( dec.getId(), allOptions );
        }

        return orderedOptionsMap;
    }
}
