package org.hisp.dhis.importexport.action.imp;

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

import org.hisp.dhis.datadictionary.DataDictionaryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.action.util.ClassMapUtil;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.reporttable.ReportTableStore;
import org.hisp.dhis.validation.ValidationRuleService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetMatchOptionsAction
    extends ActionSupport  
{
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String objectType;

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType( String type )
    {
        this.objectType = type;
    }
    
    private Integer objectId;

    public Integer getObjectId()
    {
        return objectId;
    }

    public void setObjectId( Integer id )
    {
        this.objectId = id;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Object importObject;

    public Object getImportObject()
    {
        return importObject;
    }    
    
    private Collection<? extends Object> objects;

    public Collection<? extends Object> getObjects()
    {
        return objects;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ImportObjectService importObjectService;

    public void setImportObjectService( ImportObjectService importObjectService )
    {
        this.importObjectService = importObjectService;
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

    private DataDictionaryService dataDictionaryService;

    public void setDataDictionaryService( DataDictionaryService dataDictionaryService )
    {
        this.dataDictionaryService = dataDictionaryService;
    }
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
    }
    
    private ReportTableStore reportTableStore;

    public void setReportTableStore( ReportTableStore reportTableStore )
    {
        this.reportTableStore = reportTableStore;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        importObject = importObjectService.getImportObject( Integer.valueOf( objectId ) );
        
        if ( objectType.equals( ClassMapUtil.ObjectType.DATAELEMENT.name() ) )
        {
            objects = dataElementService.getAllDataElements();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.DATAELEMENTGROUP.name() ) )
        {
            objects = dataElementService.getAllDataElementGroups();        
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.INDICATORTYPE.name() ) )
        {
            objects = indicatorService.getAllIndicatorTypes();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.INDICATOR.name() ) ) 
        {
            objects = indicatorService.getAllIndicators();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.INDICATORGROUP.name() ) )
        {
            objects = indicatorService.getAllIndicatorGroups();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.DATADICTIONARY.name() ) )
        {
            objects = dataDictionaryService.getAllDataDictionaries();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.DATASET.name() ) )
        {
            objects = dataSetService.getAllDataSets();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.ORGANISATIONUNIT.name() ) )
        {
            objects = organisationUnitService.getAllOrganisationUnits();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.ORGANISATIONUNITGROUP.name() ) )
        {
            objects = organisationUnitGroupService.getAllOrganisationUnitGroups();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.ORGANISATIONUNITGROUPSET.name() ) )
        {
            objects = organisationUnitGroupService.getAllOrganisationUnitGroupSets();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.VALIDATIONRULE.name() ) )
        {
            objects = validationRuleService.getAllValidationRules();
        }
        else if ( objectType.equals( ClassMapUtil.ObjectType.REPORTTABLE.name() ) )
        {
            objects = reportTableStore.getAllReportTables();
        }
        
        return SUCCESS;
    }
}
