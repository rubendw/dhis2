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
import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.LegendService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class ValidateLegendAction  implements Action{
	
	static final String ADD = "add";
	
	static final String UPDATE = "update";
	
	private LegendService legendService;
	
	private String message;
	
	private String nameField;
	
	private String colorField;
	
	private String minField;
	
	private String maxField;
	
	private I18n i18n;	
	
	private String action;
	
	private boolean autoCreateMax;
	
	
	

	public void setAutoCreateMax(boolean autoCreateMax) {
		this.autoCreateMax = autoCreateMax;
	}



	public void setAction( String action )
	{
		this.action = action;
	}



	public String getMessage()
	{
		return message;
	}



	public void setLegendService( LegendService legendService )
	{
		this.legendService = legendService;
	}



	public void setNameField( String nameField )
	{
		this.nameField = nameField;
	}



	public void setColorField( String colorField )
	{
		this.colorField = colorField;
	}



	public void setMinField( String minField )
	{
		this.minField = minField;
	}



	public void setMaxField( String maxField )
	{
		this.maxField = maxField;
	}



	public void setI18n( I18n i18n )
	{
		this.i18n = i18n;
	}



	public String execute() throws Exception
	{		
		
		if(action.equals(ADD)){
			if(nameField==null){
				message = i18n.getString("enter_name");			
				return INPUT;
			}else {
				nameField = nameField.trim();
				if(nameField.length()==0){
					message = i18n.getString("enter_name");
					return INPUT;
				}
			}
			Legend legend = legendService.getLegendByName(nameField);
			
			if(legend!=null){
				message = i18n.getString("ready_exist");	
				return INPUT;
			}
		}
		
		
		
		
		if(colorField==null){
			message = i18n.getString("enter_color");			
			return INPUT;
		}else {
			colorField = colorField.trim();
			if(colorField.length()==0){
				message = i18n.getString("enter_color");
				return INPUT;
			}
		}
		
		if(minField==null){
			message = i18n.getString("enter_min");
			return INPUT;			
		}
		
		if(minField.trim().equals("")){
			message = i18n.getString("enter_min");
			return INPUT;	
		}
		
		try {
			
			Double.parseDouble(minField);
			
		} catch (RuntimeException e) {
			
			message = e.getMessage();
			
			return INPUT;
		}
		
		if(autoCreateMax==false){
			
			if(maxField==null){
				message = i18n.getString("enter_max");
				return INPUT;
			}
			if(maxField.trim().equals("")){
				message = i18n.getString("enter_max");
				return INPUT;	
			}
			try {
				
				Double.parseDouble(maxField);
				
			} catch (RuntimeException e) {
				
				message = e.getMessage();
				
				return INPUT;
			}
		
			
			if(Double.parseDouble(minField)>=Double.parseDouble(maxField)){
				message = i18n.getString("min_max");
				return INPUT;
			}
		}		
		
		
		
		return SUCCESS;
	}

}
