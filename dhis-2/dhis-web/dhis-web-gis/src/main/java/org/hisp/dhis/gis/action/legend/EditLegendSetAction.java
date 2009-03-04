package org.hisp.dhis.gis.action.legend;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.LegendService;
import org.hisp.dhis.gis.LegendSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class EditLegendSetAction implements Action {
	
	private LegendService legendService;

	private IndicatorService indicatorService;

	/*--------------------------------------------------------
	 * Input
	 *--------------------------------------------------------*/
	
	private Integer id;

	private List<String> selectedList = new ArrayList<String>();
	
	private List<String> selectedIndicatorList = new ArrayList<String>();
	
	
	
	
	

	/*--------------------------------------------------------
	 * Getter & Setter
	 *--------------------------------------------------------*/

	
	
	
	public void setLegendService( LegendService legendService )
	{
		this.legendService = legendService;
	}

	public void setSelectedList( List<String> selectedList )
	{
		this.selectedList = selectedList;
	}


	public void setIndicatorService( IndicatorService indicatorService )
	{
		this.indicatorService = indicatorService;
	}
	
	

	public void setSelectedIndicatorList( List<String> selectedIndicatorList )
	{
		this.selectedIndicatorList = selectedIndicatorList;
	}

	public void setId( Integer id )
	{
		this.id = id;
	}

	public String execute() throws Exception
	{

		LegendSet legendSet = legendService.getLegendSet(id.intValue());
		
		legendSet.setIndicators(new HashSet<Indicator>());
		
		for(String id:selectedIndicatorList){
			
			legendSet.addIndicator(indicatorService.getIndicator(Integer.parseInt(id)));
			
		}
		
		
		legendSet.setLegends(new ArrayList<Legend>());
		
		for(String legendId:selectedList){
			legendSet.addLegend(legendService.getLegend(Integer.parseInt(legendId)));
		}
		
		legendService.updateLegendSet(legendSet);
		
		
		return SUCCESS;
	}

}
