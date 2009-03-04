package org.hisp.dhis.validationrule.action;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.util.SessionUtils;
import org.hisp.dhis.validation.ValidationResult;
import org.hisp.dhis.validation.ValidationRuleGroup;
import org.hisp.dhis.validation.ValidationRuleService;
import org.hisp.dhis.validation.comparator.ValidationResultComparator;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id: RunValidationAction.java 5438 2008-06-19 20:31:07Z larshelg $
 */
public class RunValidationAction
    extends ActionSupport
{
    private static final String KEY_VALIDATIONRESULT = "validationResult";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private String startDate;

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    private String endDate;

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }
    
    private Integer validationRuleGroupId;

    public void setValidationRuleGroupId( Integer validationRuleGroupId )
    {
        this.validationRuleGroupId = validationRuleGroupId;
    }
    
    private Boolean includeChildren;

    public void setIncludeChildren( Boolean includeChildren )
    {
        this.includeChildren = includeChildren;
    }

    private List<ValidationResult> validationResults;

    public List<ValidationResult> getValidationResults()
    {
        return validationResults;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public String execute()
    {
        Collection<? extends Source> sources = selectionTreeManager.getSelectedOrganisationUnits();

        if ( includeChildren )
        {
            Collection<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();

            for ( Source source : sources )
            {
                organisationUnits.addAll( organisationUnitService.getOrganisationUnitWithChildren( source.getId() ) );
            }

            sources = organisationUnits;
        }

        if ( validationRuleGroupId == -1 )
        {
            validationResults = new ArrayList<ValidationResult>( validationRuleService.validate( 
                format.parseDate( startDate ), format.parseDate( endDate ), sources  ) );
        }
        else
        {
            ValidationRuleGroup group = validationRuleService.getValidationRuleGroup( validationRuleGroupId );
            
            validationResults = new ArrayList<ValidationResult>( validationRuleService.validate( 
                format.parseDate( startDate ), format.parseDate( endDate ), sources, group ) );
        }

        Collections.sort( validationResults, new ValidationResultComparator() );
        
        SessionUtils.setSessionVar( KEY_VALIDATIONRESULT, validationResults );
        
        return SUCCESS;
    }

}
