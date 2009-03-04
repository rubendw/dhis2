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
import org.hisp.dhis.gis.LegendService;
import org.hisp.dhis.gis.LegendSet;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class ValidateLegendSetAction implements Action{
	
	static final String ADD = "add";
	
	static final String UPDATE = "update";
	
	private LegendService legendService;
	
	private I18n i18n;
	
	
	/*--------------------------------------------------------
	 * Input
	 *--------------------------------------------------------*/
	
	private String name;
	
	private Integer indicatorId;
	
	private String action;
	
	/*--------------------------------------------------------
	 * Output
	 *--------------------------------------------------------*/	
	
	private String message;
	
	
	/*--------------------------------------------------------
	 * Getter & Setter
	 *--------------------------------------------------------*/	
	

	
	
	public void setLegendService( LegendService legendService )
	{
		this.legendService = legendService;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public void setIndicatorId( Integer indicatorId )
	{
		this.indicatorId = indicatorId;
	}

	public void setAction( String action )
	{
		this.action = action;
	}
	

	public String getMessage()
	{
		return message;
	}


	public void setI18n( I18n i18n )
	{
		this.i18n = i18n;
	}
	
	

	public String execute() throws Exception
	{
		if(name==null){
			message = i18n.getString( "enter_legendset_name" );		
			return INPUT;			
		}else{			
			name = name.trim();
			if(name.length()==0){
				message = i18n.getString( "enter_legendset_name" );			
				return INPUT;
			}
		}
		if(action==ADD){
			LegendSet legendSet = legendService.getLegendSet(name);
			
			if(legendSet!=null){
				message = i18n.getString( "legend_name_ready_exist" );		
				return INPUT;
			}
		}
		
		
		if(indicatorId==null){
			message = i18n.getString( "enter_indicator" );			
			return INPUT;	
		}	
		
		return SUCCESS;
	}

}
