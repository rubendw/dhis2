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
public class AddLegendSetAction implements Action {
	
	private LegendService legendService;
	
	private IndicatorService indicatorService;
	
	/*--------------------------------------------------------
	 * Input
	 *--------------------------------------------------------*/
	
	private String name;
	
	private List<String> selectedList = new ArrayList<String>();
	
	private List<String> selectedIndicatorList = new ArrayList<String>();
	
	
	
	/*--------------------------------------------------------
	 * Ouput
	 *--------------------------------------------------------*/
	
	private Set<Legend> legends = new HashSet<Legend>();
	
	private Set<Indicator> indicators = new HashSet<Indicator>();
	
	private Set<IndicatorGroup> indicatorGroups = new HashSet<IndicatorGroup>();
	
	/*--------------------------------------------------------
	 * Getter & Setter
	 *--------------------------------------------------------*/

	public Set<IndicatorGroup> getIndicatorGroups()
	{
		return indicatorGroups;
	}

	
	
	public Set<Indicator> getIndicators()
	{
		return indicators;
	}

	public Set<Legend> getLegends()
	{
		return legends;
	}

	public void setLegendService( LegendService legendService )
	{
		this.legendService = legendService;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public void setSelectedList( List<String> selectedList )
	{
		this.selectedList = selectedList;
	}

	
	public void setSelectedIndicatorList( List<String> selectedIndicatorList )
	{
		this.selectedIndicatorList = selectedIndicatorList;
	}



	public void setIndicatorService( IndicatorService indicatorService )
	{
		this.indicatorService = indicatorService;
	}
	
	

	public String execute() throws Exception
	{
		indicators = new HashSet<Indicator>(indicatorService.getAllIndicators());
		
		Set<LegendSet> legendSets = legendService.getAllLegendSet();
		
		for(LegendSet legendSet:legendSets){
			indicators.removeAll(legendSet.getIndicators());
		}
		
		indicatorGroups = new HashSet<IndicatorGroup>(indicatorService.getAllIndicatorGroups());
		
		legends = legendService.getAllLegend();
		
		if(name==null){
			return INPUT;			
		}else{
			name = name.trim();
			if(name.length()==0){
				return INPUT;
			}
		}
		
		LegendSet legendset = new LegendSet(name);
		
		for(String id:selectedIndicatorList){
			Indicator indicator = indicatorService.getIndicator(Integer.parseInt(id));
			
			legendset.addIndicator(indicator);
			
		}
		
		for(String id:selectedList){
			Legend legend = legendService.getLegend(Integer.parseInt( id ));
			legendset.addLegend(legend);
		}
		
		legendService.addLegendSet(legendset);
		
		name=null;
		
		
		return SUCCESS;
	}

	
	

	
}
