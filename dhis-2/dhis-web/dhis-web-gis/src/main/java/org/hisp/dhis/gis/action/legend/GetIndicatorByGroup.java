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
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.gis.LegendService;
import org.hisp.dhis.gis.LegendSet;
import org.hisp.dhis.indicator.*;



import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class GetIndicatorByGroup implements Action {

	private IndicatorService indicatorService;
	
	private LegendService legendService;

	private Integer indicatorGroupId;
	
	private Set<Indicator> indicators = new HashSet<Indicator>();
	
	private Integer legendSetId;
	
	private LegendSet legendSet;
	
	

	public LegendSet getLegendSet()
	{
		return legendSet;
	}

	public void setLegendSetId( Integer legendSetId )
	{
		this.legendSetId = legendSetId;
	}

	public Set<Indicator> getIndicators()
	{
		return indicators;
	}

	
	
	public void setLegendService( LegendService legendService )
	{
		this.legendService = legendService;
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
			
			indicators = new HashSet<Indicator>(indicatorService.getAllIndicators());
			
		}else{
			IndicatorGroup indicatorGroup = indicatorService.getIndicatorGroup(indicatorGroupId.intValue());
			
			indicators = indicatorGroup.getMembers();
		}
		
		if(legendSetId!=null){
			
			legendSet = legendService.getLegendSet(legendSetId.intValue());
			
			indicators.removeAll(legendSet.getIndicators());
			
		}	
		
		
		return SUCCESS;
	}

}
