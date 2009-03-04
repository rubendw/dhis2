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
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;


import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class AssignUnitSaveAction implements Action{
	
	private FeatureService featureService;	
		
		
	private OrganisationUnitService organisationUnitService;
	
	
	// Input-------------------------------------------
	
	private OrganisationUnit organisationUnit;
	
	private String organisationUnitCode;
	
	private String comment;
	
	private Integer orgUnitId;
	
	
	
	
	// Getter & Setter---------------------------------

	



	
	public String getComment() {
		return comment;
	}


	public String getOrganisationUnitCode() {
		return organisationUnitCode;
	}

	


	public OrganisationUnit getOrganisationUnit() {
		return organisationUnit;
	}


	public void setOrganisationUnit(OrganisationUnit organisationUnit) {
		this.organisationUnit = organisationUnit;
	}


	public void setFeatureService(FeatureService featureService) {
		this.featureService = featureService;
	}



	public void setOrganisationUnitCode(String organisationUnitCode) {
		this.organisationUnitCode = organisationUnitCode;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}


	public void setOrganisationUnitService(
			OrganisationUnitService organisationUnitService) {
		this.organisationUnitService = organisationUnitService;
	}

	
	public String execute() throws Exception {
		
		organisationUnit = organisationUnitService.getOrganisationUnit(this.orgUnitId);	
		
		
		
		Feature feature = featureService.get(organisationUnit);
	
		
		if(feature!=null){
			comment = comment.trim();
		}
		if(feature==null){
			Feature featureNew = new Feature();
			
			featureNew.setOrganisationUnit(organisationUnit);
			
			featureNew.setFeatureCode(organisationUnitCode);
			
			featureNew.setComment(comment);
			
			featureService.add(featureNew);			
			
		}else{
			feature.setComment(comment);
			
			feature.setFeatureCode(organisationUnitCode);
			
			featureService.update(feature);
		
		}
			
		
	
		return SUCCESS;
	}


	
}
