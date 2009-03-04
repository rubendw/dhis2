package org.hisp.dhis.dashboard.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;

import com.opensymphony.xwork.ActionSupport;

public class GetOrgUnitGroupsAction extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitGroupService organisationUnitGroupService;
    
    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    
    private Integer orgUnitGroupSetId;
    
    public void setOrgUnitGroupSetId( Integer orgUnitGroupSetId )
    {
        this.orgUnitGroupSetId = orgUnitGroupSetId;
    }
    
    private List<OrganisationUnitGroup> orgUnitGroups;
    
    public List<OrganisationUnitGroup> getOrgUnitGroups()
    {
        return orgUnitGroups;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {

        OrganisationUnitGroupSet organisationUnitGroupSet = organisationUnitGroupService.getOrganisationUnitGroupSet( orgUnitGroupSetId );
        
        if(organisationUnitGroupSet != null )
        {
            orgUnitGroups = new ArrayList<OrganisationUnitGroup>(organisationUnitGroupSet.getOrganisationUnitGroups());            
        }
        
        return SUCCESS;
    }

}
