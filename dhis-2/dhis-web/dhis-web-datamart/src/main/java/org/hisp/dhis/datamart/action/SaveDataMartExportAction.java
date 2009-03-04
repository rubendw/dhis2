package org.hisp.dhis.datamart.action;

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

import static org.hisp.dhis.system.util.ConversionUtils.getIntegerCollection;

import java.util.Collection;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datamart.DataMartExport;
import org.hisp.dhis.datamart.DataMartExportService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.util.CollectionConversionUtils;

import com.opensymphony.xwork.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class SaveDataMartExportAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private DataMartExportService dataMartExportService;

    public void setDataMartExportService( DataMartExportService dataMartExportService )
    {
        this.dataMartExportService = dataMartExportService;
    }
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    private String name;

    public void setName( String name )
    {
        this.name = name;
    }
    
    private Collection<String> selectedDataElements;
    
    public void setSelectedDataElements( Collection<String> selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }

    private Collection<String> selectedIndicators;

    public void setSelectedIndicators( Collection<String> selectedIndicators )
    {
        this.selectedIndicators = selectedIndicators;
    }

    private Collection<String> selectedPeriods; 
    
    public void setSelectedPeriods( Collection<String> selectedPeriods )
    {
        this.selectedPeriods = selectedPeriods;
    }

    private Collection<String> selectedOrganisationUnits;
    
    public void setSelectedOrganisationUnits( Collection<String> selectedOrganisationUnits )
    {
        this.selectedOrganisationUnits = selectedOrganisationUnits;
    }
        
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute()
    {
        DataMartExport export = id == null ? new DataMartExport() : dataMartExportService.getDataMartExport( id );
        
        export.setName( name );
        
        export.setDataElements( new CollectionConversionUtils<DataElement>().getSet( 
            dataElementService.getDataElements( getIntegerCollection( selectedDataElements ) ) ) );
        export.setIndicators( new CollectionConversionUtils<Indicator>().getSet( 
            indicatorService.getIndicators( getIntegerCollection( selectedIndicators ) ) ) );
        export.setPeriods( new CollectionConversionUtils<Period>().getSet( 
            periodService.getPeriods( getIntegerCollection( selectedPeriods ) ) ) );
        export.setOrganisationUnits( new CollectionConversionUtils<OrganisationUnit>().getSet( 
            organisationUnitService.getOrganisationUnits( getIntegerCollection( selectedOrganisationUnits ) ) ) );
        
        dataMartExportService.saveDataMartExport( export );
        
        return SUCCESS;
    }
}
