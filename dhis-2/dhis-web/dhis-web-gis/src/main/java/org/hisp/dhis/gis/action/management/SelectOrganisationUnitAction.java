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
import org.hisp.dhis.gis.MapFile;
import org.hisp.dhis.gis.state.SelectionManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class SelectOrganisationUnitAction implements Action{
	
	private OrganisationUnitSelectionManager organisationUnitSelectionManager;
	
	private FeatureService featureService;
	
	private  Feature feature;
	
	private OrganisationUnit organisationUnit;
	
	private SelectionManager selectionManager;
	
	private MapFile mapFile;	
	

	public MapFile getMapFile()
	{
		return mapFile;
	}



	public void setSelectionManager( SelectionManager selectionManager )
	{
		this.selectionManager = selectionManager;
	}



	public Feature getFeature() {
		return feature;
	}



	public OrganisationUnit getOrganisationUnit() {
		return organisationUnit;
	}



	public void setOrganisationUnitSelectionManager(
			OrganisationUnitSelectionManager organisationUnitSelectionManager) {
		this.organisationUnitSelectionManager = organisationUnitSelectionManager;
	}



	public void setFeatureService(FeatureService featureService) {
		this.featureService = featureService;
	}



	public String execute() throws Exception {
		
		organisationUnit = organisationUnitSelectionManager.getSelectedOrganisationUnit();
		
		feature = featureService.get(organisationUnit);
		
		if(organisationUnit!=null){
			mapFile = selectionManager.getSelectedMapFile();
		}
		if(mapFile==null){
			mapFile = new MapFile();
			mapFile.setMapFile("example.svg");
		}
		// TODO Auto-generated method stub
		return SUCCESS;
	}

}
