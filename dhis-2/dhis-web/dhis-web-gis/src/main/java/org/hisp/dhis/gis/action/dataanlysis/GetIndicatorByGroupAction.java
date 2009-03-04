
package org.hisp.dhis.gis.action.dataanlysis;
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

import org.hisp.dhis.gis.state.SelectionManager;
import org.hisp.dhis.indicator.*;
import org.hisp.dhis.indicator.comparator.IndicatorNameComparator;

import java.util.*;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public class GetIndicatorByGroupAction implements Action {
	
	private SelectionManager selectionManager;
	
	private IndicatorService indicatorService;
	
	private Integer indicatorGroupId;
	
	private List<Indicator> indicators = new ArrayList<Indicator>();
	
	// Getter & setter
	
	
	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}


	public List<Indicator> getIndicators()
	{
		return indicators;
	}

	

	public void setIndicatorService( IndicatorService indicatorService )
	{
		this.indicatorService = indicatorService;
	}



	public void setIndicatorGroupId( Integer indicatorGroupId )
	{
		this.indicatorGroupId = indicatorGroupId;
	}
	
	public String execute() throws Exception
	{
		if(indicatorGroupId==null){
			
			indicators = new ArrayList<Indicator>(indicatorService.getAllIndicators());
			
		}else{
			IndicatorGroup indicatorGroup = indicatorService.getIndicatorGroup(new Integer(indicatorGroupId).intValue());
			
			selectionManager.setSelectedIndicatorGroup(indicatorGroup);
			
			indicators = new ArrayList<Indicator>(indicatorGroup.getMembers());
			
		}
		Collections.sort(indicators, new IndicatorNameComparator());
		
		return SUCCESS;
	}



	
}
