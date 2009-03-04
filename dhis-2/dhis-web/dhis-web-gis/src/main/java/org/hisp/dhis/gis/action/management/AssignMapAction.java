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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hisp.dhis.gis.FeatureService;
import org.hisp.dhis.gis.MapFile;
import org.hisp.dhis.gis.state.SelectionManager;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitNameComparator;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class AssignMapAction implements Action{
	
	private FeatureService featureService;	
	
	private OrganisationUnitSelectionManager organisationUnitSelectionManager;
	
	private SelectionManager selectionManager;
	
	private I18n i18n;	
	
	
	// Input-------------------------------------------
	
	private List<OrganisationUnit> organisationUnitChildren;
	
	private OrganisationUnit organisationUnit;
	
	private MapFile mapFile;
	
	// Getter & Setter---------------------------------

	public void setSelectionManager( SelectionManager selectionManager )
	{
		this.selectionManager = selectionManager;
	}


	
	
	public OrganisationUnit getOrganisationUnit() {
		return organisationUnit;
	}




	public void setOrganisationUnit(OrganisationUnit organisationUnit) {
		this.organisationUnit = organisationUnit;
	}




	public MapFile getMapFile()
	{
		return mapFile;
	}

	

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}


	


	

	public List<OrganisationUnit> getOrganisationUnitChildren() {
		return organisationUnitChildren;
	}


	public void setOrganisationUnitChildren(
			List<OrganisationUnit> organisationUnitChildren) {
		this.organisationUnitChildren = organisationUnitChildren;
	}


	public void setFeatureService(FeatureService featureService) {
		this.featureService = featureService;
	}


	public void setOrganisationUnitSelectionManager(
			OrganisationUnitSelectionManager organisationUnitSelectionManager) {
		this.organisationUnitSelectionManager = organisationUnitSelectionManager;
	}


	
	public String execute() throws Exception {
		
		organisationUnit = organisationUnitSelectionManager.getSelectedOrganisationUnit();
		
		organisationUnitChildren = new ArrayList<OrganisationUnit>(organisationUnit.getChildren());
		
		Collections.sort(organisationUnitChildren,
				new OrganisationUnitNameComparator());
		
		mapFile = selectionManager.getSelectedMapFile();
		
		return INPUT;
	}


	
	




	

	




}
