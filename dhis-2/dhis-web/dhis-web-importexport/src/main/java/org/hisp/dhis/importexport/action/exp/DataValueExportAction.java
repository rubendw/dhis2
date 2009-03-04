package org.hisp.dhis.importexport.action.exp;

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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ExportService;
import org.hisp.dhis.importexport.ImportDataValueService;
import org.hisp.dhis.importexport.ImportExportServiceManager;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.period.PeriodService;

import static org.hisp.dhis.system.util.DateUtils.getMediumDate;
import static org.hisp.dhis.system.util.DateUtils.getMediumDateString;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataValueExportAction
    extends ActionSupport
{
    private final static String FILE_EXTENSION = ".zip";
    private final static String FILE_PREFIX = "Export";
    private final static String FILE_SEPARATOR = "_";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    private ImportExportServiceManager serviceManager;

    public void setServiceManager( ImportExportServiceManager serviceManager )
    {
        this.serviceManager = serviceManager;
    }

    private DataElementCategoryService categoryService;

    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }
    
    private DataElementCategoryOptionService categoryOptionService;

    public void setCategoryOptionService( DataElementCategoryOptionService categoryOptionService )
    {
        this.categoryOptionService = categoryOptionService;
    }

    private DataElementCategoryComboService categoryComboService;

    public void setCategoryComboService( DataElementCategoryComboService categoryComboService )
    {
        this.categoryComboService = categoryComboService;
    }

    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService categoryOptionComboService )
    {
        this.categoryOptionComboService = categoryOptionComboService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private CompleteDataSetRegistrationService registrationService;

    public void setRegistrationService( CompleteDataSetRegistrationService registrationService )
    {
        this.registrationService = registrationService;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private ImportDataValueService importDataValueService;

    public void setImportDataValueService( ImportDataValueService importDataValueService )
    {
        this.importDataValueService = importDataValueService;
    }

    private ImportObjectService importObjectService;

    public void setImportObjectService( ImportObjectService importObjectService )
    {
        this.importObjectService = importObjectService;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private InputStream inputStream;

    public InputStream getInputStream()
    {
        return inputStream;
    }
    
    private String fileName;

    public String getFileName()
    {
        return fileName;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private String exportFormat;

    public String getExportFormat()
    {
        return exportFormat;
    }

    public void setExportFormat( String exportFormat )
    {
        this.exportFormat = exportFormat;
    }
    
    private boolean excludeChildren;

    public void setExcludeChildren( boolean excludeChildren )
    {
        this.excludeChildren = excludeChildren;
    }
    
    private String startDate;

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }
    
    private String endDate;

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    private Collection<String> selectedDataSets;

    public void setSelectedDataSets( Collection<String> selectedDataSets )
    {
        this.selectedDataSets = selectedDataSets;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Remove existing elements
        // ---------------------------------------------------------------------

        importDataValueService.deleteImportDataValues();
        
        importObjectService.deleteImportObjects();

        ExportParams params = new ExportParams();
        
        // ---------------------------------------------------------------------
        // Get DataElements
        // ---------------------------------------------------------------------

        if ( selectedDataSets != null )
        {
            params.setCategories( categoryService.getAllDataElementCategories() );
            params.setCategoryCombos( categoryComboService.getAllDataElementCategoryCombos() );
            params.setCategoryOptions( categoryOptionService.getAllDataElementCategoryOptions() );
            params.setCategoryOptionCombos( categoryOptionComboService.getAllDataElementCategoryOptionCombos() );

            Set<DataElement> distinctDataElements = new HashSet<DataElement>();
            
            for ( String dataSetId : selectedDataSets )
            {
                DataSet dataSet = dataSetService.getDataSet( Integer.parseInt( dataSetId ) );
                
                distinctDataElements.addAll( dataSet.getDataElements() );
                
                params.getDataSets().add( dataSet );
            }
            
            params.setDataElements( distinctDataElements );
        }
        
        // ---------------------------------------------------------------------
        // Get Periods
        // ---------------------------------------------------------------------

        if ( startDate != null && startDate.trim().length() > 0 && endDate != null && endDate.trim().length() > 0 )
        {
            Date selectedStartDate = getMediumDate( startDate );
        
            Date selectedEndDate = getMediumDate( endDate );
        
            params.getPeriods().addAll( periodService.getIntersectingPeriods( selectedStartDate, selectedEndDate ) );
        }
        
        // ---------------------------------------------------------------------
        // Get OrganisationUnit
        // ---------------------------------------------------------------------
        
        Collection<OrganisationUnit> selectedUnits = selectionTreeManager.getSelectedOrganisationUnits();
        
        if ( selectedUnits != null )
        {
            for ( OrganisationUnit unit : selectedUnits )
            {
                if ( excludeChildren )
                {
                    params.getOrganisationUnits().add( organisationUnitService.getOrganisationUnit( unit.getId() ) );
                }
                else
                {
                    params.getOrganisationUnits().addAll( organisationUnitService.getOrganisationUnitWithChildren( unit.getId() ) );
                }
            }
        }
        
        params.setCompleteDataSetRegistrations( registrationService.
            getCompleteDataSetRegistrations( params.getDataSets(), params.getOrganisationUnits(), params.getPeriods() ) );
                
        params.setIncludeDataValues( true );
        
        ExportService exportService = serviceManager.getExportService( exportFormat );        
        
        inputStream = exportService.exportData( params );
        
        fileName = getFileName( params );
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private String getFileName( ExportParams params )
    {
        String fileName = FILE_PREFIX + FILE_SEPARATOR + 
            getMediumDateString( getMediumDate( startDate ) ) + FILE_SEPARATOR + 
            getMediumDateString( getMediumDate( endDate ) );
        
        if ( selectionTreeManager.getSelectedOrganisationUnits().size() == 1 )
        {
            fileName += FILE_SEPARATOR + fileNameEncode( selectionTreeManager.getSelectedOrganisationUnits().iterator().next().getShortName() );
        }
        
        if ( params.getDataSets().size() == 1 )
        {
            fileName += FILE_SEPARATOR + fileNameEncode( params.getDataSets().iterator().next().getName() );
        }
        
        fileName += FILE_EXTENSION;
        
        return fileName;
    }
    
    private String fileNameEncode( String in )
    {
        if ( in == null )
        {
            return new String();
        }
        
        in = in.replaceAll( " ", FILE_SEPARATOR );
        
        return in;
    }
}
