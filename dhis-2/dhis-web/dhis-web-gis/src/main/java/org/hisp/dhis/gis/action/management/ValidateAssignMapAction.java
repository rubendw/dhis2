package org.hisp.dhis.gis.action.management;
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
import org.hisp.dhis.gis.Feature;
import org.hisp.dhis.gis.FeatureService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class ValidateAssignMapAction implements Action{
	
	private I18n i18n;
	
	private String organisationUnitCode;
	
	private OrganisationUnitService organisationUnitService;
	
	private FeatureService featureService;		
	
	private String message;

	private Integer orgUnitId;
	
	
	
	

	public void setOrganisationUnitService(
			OrganisationUnitService organisationUnitService) {
		this.organisationUnitService = organisationUnitService;
	}




	public Integer getOrgUnitId() {
		return orgUnitId;
	}




	public String getMessage() {
		return message;
	}



	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}



	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}




	public void setOrganisationUnitCode(String organisationUnitCode) {
		this.organisationUnitCode = organisationUnitCode;
	}

	public void setFeatureService(FeatureService featureService) {
		this.featureService = featureService;
	}



	public String execute() throws Exception {
		
		OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit(orgUnitId);
		
		if(organisationUnit==null){
			message = i18n.getString("nonselected_orgunit");
			return INPUT;
		}
		
		if(organisationUnitCode==null){
			message = i18n.getString("nonselectd_map");
			return INPUT;
		}else{			
			organisationUnitCode = organisationUnitCode.trim();
			if(organisationUnitCode.length()==0){
				message = i18n.getString("nonselectd_map");
				return INPUT;
			}
		}
		
		Feature feature = featureService.get(organisationUnitCode);
		
		if(feature!=null){
			
			OrganisationUnit parent = organisationUnit.getParent();	
			
			if(parent!=null){
				
				if(parent.getChildren().contains(feature.getOrganisationUnit())){
					
					message = i18n.getString( "map_has_been_assigned" )+ 
					feature.getOrganisationUnit().getName() + ".<br/>" +
					i18n.getString( "please_select_other_map" );
					
					return INPUT;
					
				}
			}
		}
		
		message = i18n.getString("assigned");
		
		return SUCCESS;
	}




	



	

}
